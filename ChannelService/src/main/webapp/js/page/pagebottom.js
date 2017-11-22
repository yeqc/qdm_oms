
/*

showPages v1.1
=================================

Infomation
----------------------
Author : Lapuasi
E-Mail : lapuasi@gmail.com
Web : http://www.lapuasi.com
Date : 2005-11-17


Example
----------------------
var pg = new showPages('pg');
pg.pageCount = 12; //定义总页数(必要)
pg.argName = 'p';    //定义参数名(可选,缺省为page)
pg.printHtml();        //显示页数


Supported in Internet Explorer, Mozilla Firefox
*/

/**
 * 用法：
 * 	   <script type="text/javascript">
 * 			var pg = new showPages('pg');
 * 			pg.pageCount =52;  // 定义总页数(必要)
 * 			
 * 			document.write('<br>Show Times: ' + pg.showTimes + ', Mood 2');
 * 			pg.printHtml(2);
 * 		</script>
 * <br />
 * date: 2011-08-17
 * author 刘洪建 
 * @param {} name
 */
 
function showPages(name) { //初始化属性
	this.name = name;      //对象名称
	this.page = 1;         //当前页数
	this.pageCount = 1;    //总页数
	this.argName = 'page'; //参数名
	this.showTimes = 1;    //打印次数
	this.isAjax = true;    //false:刷新页面，true: ajax方式. (date: 2011-08-17 , new add  , lhj)
	this.ajaxPageListFunction = "ajaxSservice";
	//this.url = location.search;
	this.sort = null;
	//this.sorts = {};//[]
}

showPages.prototype.getPage = function(){ //丛url获得当前页数,如果变量重复只获取最后一个
	var args = location.search;
	var reg = new RegExp('[\?&]?' + this.argName + '=([^&]*)[&$]?', 'gi');
	var chk = args.match(reg);
	this.page = RegExp.$1;
}
showPages.prototype.checkPages = function(){ //进行当前页数和总页数的验证
	if (isNaN(parseInt(this.page))) this.page = 1;
	if (isNaN(parseInt(this.pageCount))) this.pageCount = 1;
	if (this.page < 1) this.page = 1;
	if (this.pageCount < 1) this.pageCount = 1;
	if (this.page > this.pageCount) this.page = this.pageCount;
	this.page = parseInt(this.page);
	this.pageCount = parseInt(this.pageCount);
}
showPages.prototype.createHtml = function(mode){ //生成html代码
	var strHtml = '', prevPage = this.page - 1, nextPage = this.page + 1;
	if (mode == '' || typeof(mode) == 'undefined') mode = 0;
	switch (mode) {
		case 0 : //模式0 (页数,首页,前页,后页,尾页)
			strHtml += '<span class="count">Pages: ' + this.page + ' / ' + this.pageCount + '</span>';
			strHtml += '<span class="number">';
			if (prevPage < 1) {
				strHtml += '<span title="First Page">&#171;</span>';
				strHtml += '<span title="Prev Page">&#139;</span>';
			} else {
				strHtml += '<span title="First Page"><a href="javascript:' + this.name + '.toPage(1);">&#171;</a></span>';
				strHtml += '<span title="Prev Page"><a href="javascript:' + this.name + '.toPage(' + prevPage + ');">&#139;</a></span>';
			}
			for (var i = 1; i <= this.pageCount; i++) {
				if (i > 0) {
					if (i == this.page) {
						strHtml += '<span title="Page ' + i + '">[' + i + ']</span>';
					} else {
						strHtml += '<span title="Page ' + i + '"><a href="javascript:' + this.name + '.toPage(' + i + ');">[' + i + ']</a></span>';
					}
				}
			}
			if (nextPage > this.pageCount) {
				strHtml += '<span title="Next Page">&#155;</span>';
				strHtml += '<span title="Last Page">&#187;</span>';
			} else {
				strHtml += '<span title="Next Page"><a href="javascript:' + this.name + '.toPage(' + nextPage + ');">&#155;</a></span>';
				strHtml += '<span title="Last Page"><a href="javascript:' + this.name + '.toPage(' + this.pageCount + ');">&#187;</a></span>';
			}
			strHtml += '</span><br />';
			break;
		case 1 : //模式1 (10页缩略,首页,前页,后页,尾页)
			strHtml += '<span class="count">Pages: ' + this.page + ' / ' + this.pageCount + '</span>';
			strHtml += '<span class="number">';
			if (prevPage < 1) {
				strHtml += '<span title="First Page">&#171;</span>';
				strHtml += '<span title="Prev Page">&#139;</span>';
			} else {
				strHtml += '<span title="First Page"><a href="javascript:' + this.name + '.toPage(1);">&#171;</a></span>';
				strHtml += '<span title="Prev Page"><a href="javascript:' + this.name + '.toPage(' + prevPage + ');">&#139;</a></span>';
			}
			if (this.page % 10 ==0) {
				var startPage = this.page - 9;
			} else {
				var startPage = this.page - this.page % 10 + 1;
			}
			if (startPage > 10) strHtml += '<span title="Prev 10 Pages"><a href="javascript:' + this.name + '.toPage(' + (startPage - 1) + ');">...</a></span>';
			for (var i = startPage; i < startPage + 10; i++) {
				if (i > this.pageCount) break;
				if (i == this.page) {
					strHtml += '<span title="Page ' + i + '">[' + i + ']</span>';
				} else {
					strHtml += '<span title="Page ' + i + '"><a href="javascript:' + this.name + '.toPage(' + i + ');">[' + i + ']</a></span>';
				}
			}
			if (this.pageCount >= startPage + 10) strHtml += '<span title="Next 10 Pages"><a href="javascript:' + this.name + '.toPage(' + (startPage + 10) + ');">...</a></span>';
			if (nextPage > this.pageCount) {
				strHtml += '<span title="Next Page">&#155;</span>';
				strHtml += '<span title="Last Page">&#187;</span>';
			} else {
				strHtml += '<span title="Next Page"><a href="javascript:' + this.name + '.toPage(' + nextPage + ');">&#155;</a></span>';
				strHtml += '<span title="Last Page"><a href="javascript:' + this.name + '.toPage(' + this.pageCount + ');">&#187;</a></span>';
			}
			strHtml += '</span><br />';
			break;
		case 2 : //模式2 (前后缩略,页数,首页,前页,后页,尾页)
			strHtml += '<span class="count">Pages: ' + this.page + ' / ' + this.pageCount + '</span>';
			strHtml += '<span class="number">';
			if (prevPage < 1) {
				strHtml += '<span title="First Page">&#171;</span>';
				strHtml += '<span title="Prev Page">&#139;</span>';
			} else {
				strHtml += '<span title="First Page"><a href="javascript:' + this.name + '.toPage(1);">&#171;</a></span>';
				strHtml += '<span title="Prev Page"><a href="javascript:' + this.name + '.toPage(' + prevPage + ');">&#139;</a></span>';
			}
			if (this.page != 1) strHtml += '<span title="Page 1"><a href="javascript:' + this.name + '.toPage(1);">[1]</a></span>';
			if (this.page >= 5) strHtml += '<span>...</span>';
			if (this.pageCount > this.page + 2) {
				var endPage = this.page + 2;
			} else {
				var endPage = this.pageCount;
			}
			for (var i = this.page - 2; i <= endPage; i++) {
				if (i > 0) {
					if (i == this.page) {
						strHtml += '<span title="Page ' + i + '">[' + i + ']</span>';
					} else {
						if (i != 1 && i != this.pageCount) {
							strHtml += '<span title="Page ' + i + '"><a href="javascript:' + this.name + '.toPage(' + i + ');">[' + i + ']</a></span>';
						}
					}
				}
			}
			if (this.page + 3 < this.pageCount) strHtml += '<span>...</span>';
			if (this.page != this.pageCount) strHtml += '<span title="Page ' + this.pageCount + '"><a href="javascript:' + this.name + '.toPage(' + this.pageCount + ');">[' + this.pageCount + ']</a></span>';
			if (nextPage > this.pageCount) {
				strHtml += '<span title="Next Page">&#155;</span>';
				strHtml += '<span title="Last Page">&#187;</span>';
			} else {
				strHtml += '<span title="Next Page"><a href="javascript:' + this.name + '.toPage(' + nextPage + ');">&#155;</a></span>';
				strHtml += '<span title="Last Page"><a href="javascript:' + this.name + '.toPage(' + this.pageCount + ');">&#187;</a></span>';
			}
			strHtml += '</span><br />';
			break;
		case 3 : //模式3 (箭头样式,首页,前页,后页,尾页) (only IE)
			strHtml += '<span class="count">Pages: ' + this.page + ' / ' + this.pageCount + '</span>';
			strHtml += '<span class="arrow">';
			if (prevPage < 1) {
				strHtml += '<span title="First Page">9</span>';
				strHtml += '<span title="Prev Page">7</span>';
			} else {
				strHtml += '<span title="First Page"><a href="javascript:' + this.name + '.toPage(1);">9</a></span>';
				strHtml += '<span title="Prev Page"><a href="javascript:' + this.name + '.toPage(' + prevPage + ');">7</a></span>';
			}
			if (nextPage > this.pageCount) {
				strHtml += '<span title="Next Page">8</span>';
				strHtml += '<span title="Last Page">:</span>';
			} else {
				strHtml += '<span title="Next Page"><a href="javascript:' + this.name + '.toPage(' + nextPage + ');">8</a></span>';
				strHtml += '<span title="Last Page"><a href="javascript:' + this.name + '.toPage(' + this.pageCount + ');">:</a></span>';
			}
			strHtml += '</span><br />';
			break;
		case 4 : //模式4 (下拉框)
			if (this.pageCount < 1) {
				strHtml += '<select name="toPage" disabled>';
				strHtml += '<option value="0">No Pages</option>';
			} else {
				var chkSelect;
				strHtml += '<select name="toPage" onchange="' + this.name + '.toPage(this);">';
				for (var i = 1; i <= this.pageCount; i++) {
					if (this.page == i) chkSelect=' selected="selected"';
					else chkSelect='';
					strHtml += '<option value="' + i + '"' + chkSelect + '>Pages: ' + i + ' / ' + this.pageCount + '</option>';
				}
			}
			strHtml += '</select>';
			break;
		case 5 : //模式5 (输入框)
			strHtml += '<span class="input">';
			if (this.pageCount < 1) {
				strHtml += '<input type="text" name="toPage" value="No Pages" class="itext" disabled="disabled">';
				strHtml += '<input type="button" name="go" value="GO" class="ibutton" disabled="disabled"></option>';
			} else {
				strHtml += '<input type="text" value="Input Page:" class="ititle" readonly="readonly">';
				strHtml += '<input type="text" id="pageInput' + this.showTimes + '" value="' + this.page + '" class="itext" title="Input page" onkeypress="return ' + this.name + '.formatInputPage(event);" onfocus="this.select()">';
				strHtml += '<input type="text" value=" / ' + this.pageCount + '" class="icount" readonly="readonly">';
				strHtml += '<input type="button" name="go" value="GO" class="ibutton" onclick="' + this.name + '.toPage(document.getElementById(\'pageInput' + this.showTimes + '\').value);"></option>';
			}
			strHtml += '</span>';
			break;
		default :
			strHtml = 'Javascript showPage Error: not find mode ' + mode;
			break;
	}
	return strHtml;
}
showPages.prototype.createUrl = function (page) { //生成页面跳转url
	if (isNaN(parseInt(page))) page = 1;
	if (page < 1) page = 1;
	if (page > this.pageCount) page = this.pageCount;
	var url = location.protocol + '//' + location.host + location.pathname;
	var args = location.search;
	var reg = new RegExp('([\?&]?)' + this.argName + '=[^&]*[&$]?', 'gi');
	args = args.replace(reg,'$1');
	if (args == '' || args == null) {
		args += '?' + this.argName + '=' + page;
	} else if (args.substr(args.length - 1,1) == '?' || args.substr(args.length - 1,1) == '&') {
			args += this.argName + '=' + page;
	} else {
			args += '&' + this.argName + '=' + page;
	}
	return url + args;
}

showPages.prototype.toPage = function(page){ //页面跳转
	var turnTo = 1;
	if (typeof(page) == 'object') {
		turnTo = page.options[page.selectedIndex].value;
	} else {
		turnTo = page;
	}
	
	if(this.isAjax) {
		if("ajaxSservice"==this.ajaxPageListFunction){
			this.ajaxSservice(turnTo);
		} else {
			//this.page = turnTo;
			this.serviceProc(turnTo);
		}
		
		
	} else {
		self.location.href = this.createUrl(turnTo);
	}
}

showPages.prototype.printHtml = function(mode){ //显示html代码
	this.getPage();
	this.checkPages();
	this.showTimes += 1;
	document.write('<div id="pages_' + this.name + '_' + this.showTimes + '" class="pages"></div>');
	document.getElementById('pages_' + this.name + '_' + this.showTimes).innerHTML = this.createHtml(mode);
}

showPages.prototype.formatInputPage = function(e){ //限定输入页数格式
	var ie = navigator.appName=="Microsoft Internet Explorer"?true:false;
	if(!ie) var key = e.which;
	else var key = event.keyCode;
	if (key == 8 || key == 46 || (key >= 48 && key <= 57)) return true;
	return false;
}

/**
 * 参照：printHtml 方法。
 * 注：2011-08-18 lhj
 * @param {} mode 风格
 * @param {} pageCount  总页数
 * @param {} turnTo 要跳转的页数
 */
showPages.prototype.refreshPagebottom = function(mode, turnTo, totalPages){ //显示html代码
	this.pageCount = totalPages;  //刷新总页数
	this.page = turnTo;
	
	//this.getPage();
	this.checkPages();
	//this.showTimes += 1;
	//document.getElementById("comment_list_pagebottom").innerHTML = this.createHtml(mode);
	//document.write('<div id="pages_' + this.name + '_' + this.showTimes + '" class="pages"></div>');
	
	document.getElementById('pages_' + this.name + '_' + this.showTimes).innerHTML = this.createHtml(mode);
	
}

/**
 * 第以次时。
 * new add
 * 注：2011-08-18 lhj
 * @param {} mode 风格
 */
showPages.prototype.insertHtml = function(mode){ //显示html代码
	this.getPage();
	this.checkPages();
	this.showTimes += 1;
	document.getElementById("comment_list_pagebottom").innerHTML = '<div id="pages_' + this.name + '_' + this.showTimes + '" class="pages"></div>';
	document.getElementById('pages_' + this.name + '_' + this.showTimes).innerHTML = this.createHtml(mode);
}

/**
 * /this.isAjax = true时需要外面覆盖此方法。
 * （this.isAjax = true时执行的取数据的函数。）
 * 注：2011-08-18 lhj
 * @param {} page 要跳转的页数
 */
showPages.prototype.serviceProc = function(page){ 
	//this.printHtml(2);
	
	//覆盖时，ajax,请求返回成功时要调用此方法。
	 this.refreshPagebottom(2, page, 20);//刷新分页, 这是个例子。
}


//new add
function Config(){
	this.listUrl='';
	this.data='';
	this.callback=null;
}
showPages.prototype.config = new Config();/*function(){
	this.listUrl='';
	this.data='';
	this.callback=null;
};*/
showPages.prototype.ajaxSservice = function(page){
					var currentPage = page;
				    var pg = this;
				    var listUrl = pg.config.listUrl;
				    var data = pg.config.data;
				    var callback = pg.config.callback;
				    
					$.ajax({
						   type: "POST",
						   dataType: "json",
						   url: listAction,
						   data: "start=0&currentPage=" + currentPage + data,
						   success: function(json){
								 //alert(json.currentPage);
								 if(isFrist){
								 	pg.pageCount = json.totalPages;  // 定义总页数(必要)
								 	pg.insertHtml(2);
							
									isFrist = false;
								 } else {
									 pg.refreshPagebottom(2, page, json.totalPages);
								 }
								 
								 setPageList(json.currentData, json.startRow);
					  		 }
					 });
					 
				};
				
	

/* eg:
	var pg = new showPages('pg');
	pg.pageCount =52;  // 定义总页数(必要)
	//pg.argName = 'p';  // 定义参数名(可选,默认为page)
	
	document.write('<br>Show Times: ' + pg.showTimes + ', Mood Default');
	pg.printHtml();
	document.write('<br>Show Times: ' + pg.showTimes + ', Mood 0');
	pg.printHtml(0);
	document.write('<br>Show Times: ' + pg.showTimes + ', Mood 1');
	pg.printHtml(1);
	document.write('<br>Show Times: ' + pg.showTimes + ', Mood 2');
	pg.printHtml(2);
	document.write('<br>Show Times: ' + pg.showTimes + ', Mood 3 (only IE)');
	pg.printHtml(3);
	document.write('<br>Show Times: ' + pg.showTimes + ', Mood 4');
	pg.printHtml(4);
	document.write('<br>Show Times: ' + pg.showTimes + ', Mood 5');
	pg.printHtml(5);
*/

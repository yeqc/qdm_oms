function checkFormat(result) {

	var re = /^(([1-9]\d*)|0)(\.\d{1,2})?$/;
	
	if(re.test(result)) {
		return true;
	} else {
		return false;
	}
}

// 获取金额显示格式
function getMoneyFormate(value) {
	value = value.toString();
	if (value) {
		return "<div style='text-align:right;'>￥"+getMoney(value)+"</div>";
	} else {
		return "";
	}
}

function getPercentFormate(value) {
	value = value.toString();
	if (value) {
		return "<div style='text-align:right;'>"+getMoney(value)+"%</div>";
	} else {
		return "";
	}
}

// 获取左对齐格式
function getLeftFormate(value) {
	return "<div style='text-align:left;'>"+value+"</div>";
}

// 获取右对齐格式
function getRightFormate(value) {
	return "<div style='text-align:right;'>"+value+"</div>";
}

// 获取字符日期
function getStringDate(value) {
	if (value) {
		value = value.substring(0, 19);
	}
	return value;
}

function formatDate(millisecond){
	var dt = new Date(millisecond);
	return dt.format('Y-m-d');
}

function formatDateformate(millisecond, formate){
	if(millisecond==0){
		return '0000-00-00 00:00:00';
	}
	var dt = new Date(millisecond);
	return dt.format(formate);
}

// 获取2位小数点四舍五入
function changeTwoDecimal(x) {  
	var f_x = parseFloat(x);  
	if (isNaN(f_x)) {  
		return 0.00;  
	}  
	var f_x = Math.round(x*100)/100;  
	return f_x;  
} 

function numFixed(num, n){
	n = n || 2;
	var number = new Number(num);
	return number.toFixed(n);
}

// 获取2位金额
function getMoney(value) {
	if (isNaN(value)) {
		return value;
	} else {
		return numFixed(parseFloat(value), 2);
	}
}

// 设置序号
var record_start = 0;

function getRecordStart() {
	return record_start;
}

function setRecordStart(start) {
	return record_start = start;
}

// 为了与后台的Page分页对象一致才设置
function setDefaultPage(start, limit, store) {
	setRecordStart(start);
	store.setBaseParam("start", start);
	store.setBaseParam("limit", limit);
	
	var pageNo = 1;
	if (start > 0) {
		pageNo = start / limit + 1;
	}
	
    store.setBaseParam("cp",pageNo);
    store.setBaseParam("pp",limit);
    store.setBaseParam("page.pageNo", pageNo);
	store.setBaseParam("page.pageSize", limit);
}

// 创建JsonStore
function createJsonStore(root, fields,url) {
	var data = {};
	data.root = root;
	data.fields = fields;
	
	if (url) {
		data.url = url;
	}
	return new Ext.data.JsonStore(data);
}

// 创建ComboBox
function createComboBox(store, id, displayField, valueField, mode, fieldLabel, width, name) {
	return new Ext.form.ComboBox({
		store: store,
		id: id,
		name: name,
   		displayField: displayField,
		valueField: valueField, 
		mode: mode,
		width:width ? width : 150,
		triggerAction: 'all',
		selectOnFocus:true,
       	allowBlank:true,
       	forceSelection:true,
    	fieldLabel: fieldLabel,
    	hiddenName: name,
		editable: false // 不可输入
	});
}

// 创建ComboBox
function createComboBoxNoHidden(store, id, displayField, valueField, mode, fieldLabel, width, name) {
	return new Ext.form.ComboBox({
		store: store,
		id: id,
		name: name,
   		displayField: displayField,
		valueField: valueField, 
		mode: mode,
		width:width ? width : 150,
		triggerAction: 'all',
		selectOnFocus:true,
       	allowBlank:true,
       	forceSelection:true,
    	fieldLabel: fieldLabel,
		editable: false // 不可输入
	});
}

//创建ComboBox
function createComboBox2(store, id, displayField, valueField, mode, fieldLabel,name) {
	return new Ext.form.ComboBox({
		store: store,
		id: id,
		name: name,
   		displayField: displayField,
		valueField: valueField, 
		mode: mode,
		triggerAction: 'all',
		selectOnFocus:true,
       	allowBlank:true,
       	forceSelection:true,
    	fieldLabel: fieldLabel,
    	hiddenName: name,
		editable: false // 不可输入
	});
}

// 创建分页
function createPagingToolbar(store, limit) {
	return new Ext.PagingToolbar({
		pageSize: limit,
		store: store,
		displayInfo: true,
		displayMsg: '显示 {0} - {1}条记录 共 {2}条',
		style:"padding:0 15px",
		emptyMsg: "没有记录可以显示",
		beforePageText:"页数",
		afterPageText : '共 {0}页',
		firstText : '第一页',
		prevText : '上一页',
		nextText : '下一页',
		lastText : '最后一页',
		refreshText : '刷新',
        doLoad : function(start){
  			record_start = start;
  		 	setDefaultPage(start, limit, store);
   			store.load();
  		}
	});
}

// 创建ajax请求
function createAjax(url, successfun, failurefun, params, timeout) {

	if (!timeout) {
		// 默认超时10分钟
		timeout = 60000;
	}
	
	Ext.Ajax.request({
		url: url,
		timeout:timeout,
		success: function(response,opts){
			if (successfun) {
				successfun(response, opts);	
			}			
		},
		failure: function(response,opts){
			if (failurefun) {
				failurefun(response,opts);
			} else {
				createAlert("提示信息", "请求数据失败!");
			}
		},
		params: params
	});
}

/**
 * id : id
 * title : 标题
 * url : 地址
 * width : 宽度
 * height : 高度
 */
function showWindow(id,title,url, w, h, x,y) {
	w = w ? w : 800;
	h = h ? h : 500;
	x = x ? x : Ext.getBody().getWidth()/2 - 300;
	y = y ? y : Ext.getBody().getHeight()/2 -200;
	
	return new Ext.Window( {
		id:id,
		title : title,
		width : w,
		height: h,
		x:x,
		y:y,
		iconCls: 'tabs',
   		shim:false,
   		animCollapse:false,
   		modal : true,     //window窗体后面变灰,不可编辑
   		resizable:false,
     	border:false,
     	//maximizable:true,
       	constrainHeader:true,
      	layout: 'fit',
      	html:'<iframe frameborder="no" scrolling="auto" border="0" framespacing="0" id="ifr'+id+'" src="'+url+'" width="100%" height="100%"></iframe>'
	});
}

function showWindowUrl(url,id, title, x, y, w, h, button) {
	
	var data = {
		id:id,
		title: title,
  	 	layout:'fit',
  		plain: true,
		modal:true,
		resizable:false,
		closeAction: 'hide',
		buttonAlign:"center",
  	 	buttons: button,
		html:'<iframe frameborder="no" scrolling="auto" border="0" framespacing="0" id="ifr'+id+'" src="'+url+'" width="100%" height="100%"></iframe>'
	};
	
	w = w ? w : 800;
	h = h ? h : 500;
	x = x ? x : Ext.getBody().getWidth()/2 - 300;
	y = y ? y : Ext.getBody().getHeight()/2 -200;
	
	data.x = x;
	data.y = y;
	data.width = w;
	data.height = h;
	
	return new Ext.Window(data);
}

function showWindowForm(title, x, y, w, button) {
	w = w ? w : 800;
	//height = height ? height : 500;
	x = x ? x : Ext.getBody().getWidth()/2 - 300;
	y = y ? y : Ext.getBody().getHeight()/2 -200;
	return new Ext.Window( {
		title: title,
		x:x,
		y:y,
  	 	layout:'fit',
  	  	width:w,
  	 	autoHeight:true,
  	  	closeAction:'hide',
  		plain: true,
		modal:true,
		resizable:false,
		buttonAlign:"center",
  	 	buttons: button
	});
}

/**
 * 创建表格 附带分页条
 * id
 * title
 */
function createGridPanel(id, title, columns,store,limit, renderTo, sm, tb, tbar) {
	return new Ext.grid.GridPanel(getGridPanelModel(id, title, columns,store,limit, renderTo, sm, tb, tbar));
}

// 创建表格,传入对象
function createGridPanelByModel(model) {
	return new Ext.grid.GridPanel(model);
}

function getGridPanelModel(id, title, columns,store,limit, renderTo, sm, tb, tbar) {
	var model = {
		id:id,
		title:title,
		height:renderTo.getHeight(),
		columns:columns,
		store:store,
		//loadMask: true,  //读取数据时的遮罩和提示功能
		//enableColumnMove: false, //禁止拖放列
		//enableColumnResize: false,  //禁止改变列的宽度
		stripeRows: true,  //斑马线效果
		columnLines : true,
		viewConfig : {
			//forceFit:true,
			layout : function() {
				if (!this.mainBody) {     
					return; // not rendered     
			    }     
			    var g = this.grid;     
			    var c = g.getGridEl();     
			    var csize = c.getSize(true);     
			    var vw = csize.width;  
			    var vh = 0;
			    if (!g.hideHeaders && (vw < 20 || csize.height < 20)) { 
			        return;     
			    }     
			    if (g.autoHeight) {     
			        if (this.innerHd) {     
			            this.innerHd.style.width = (vw) + 'px';     
			        }     
			    } else {     
			        this.el.setSize(csize.width, csize.height);     
			        var hdHeight = this.mainHd.getHeight();     
			        vh = csize.height - (hdHeight);     
			        this.scroller.setSize(vw, vh);     
			        if (this.innerHd) {     
			            this.innerHd.style.width = (vw) + 'px';     
			        }     
			    }     
			    if (this.forceFit) {     
			        if (this.lastViewWidth != vw) {     
			            this.fitColumns(false, false);     
			            this.lastViewWidth = vw;     
			        }     
			    } else {     
			        this.autoExpand();     
			        this.syncHeaderScroll();     
			    }     
			    this.onLayout(vw, vh);
			},
			getColumnStyle : function(colIndex, isHeader) {
            	var colModel  = this.cm,
				colConfig = colModel.config,
				style = isHeader ? '' : colConfig[colIndex].css || '',
				align = colConfig[colIndex].align;
            	if(Ext.isChrome){
                	style += String.format("width: {0};", parseInt(this.getColumnWidth(colIndex))-2+'px');
            	}else{
                	style += String.format("width: {0};", this.getColumnWidth(colIndex));
            	}
            	if (colModel.isHidden(colIndex)) {
                	style += 'display: none; ';
            	}
            	if (align) {
                	style += String.format("text-align: {0};", align);
            	}
            	return style;
        	}
		},
		renderTo:renderTo
	};
	if (sm) {
		model.sm = sm;
	}
	if (tb) {
		model.bbar = tb;
	} else {
		if (limit > 0 ) {
			model.bbar = createPagingToolbar(store, limit);
		}
	}
	if (tbar) {
		model.tbar = tbar;
	}
	return model;
}



function getGridPanelModelSimple(id, title, columns,store,limit, renderTo, sm, tb, tbar,mh,height) {
	var model = {
		id:id,
		title:title,
		height:height,
		columns:columns,
		collapsible : true,
		store:store,
		//loadMask: true,  //读取数据时的遮罩和提示功能
		//enableColumnMove: false, //禁止拖放列
		//enableColumnResize: false,  //禁止改变列的宽度
		stripeRows: true,  //斑马线效果
		columnLines : true,
		viewConfig : {
			forceFit:true
		},
		renderTo:renderTo
	};
	if (sm) {
		model.sm = sm;
	}
	if (tb) {
		model.bbar = tb;
	} else {
		if (limit > 0 ) {
			//model.bbar = createPagingToolbar(store, limit);
		}
	}
	if (tbar) {
		model.tbar = tbar;
	}
	return new Ext.grid.GridPanel(model);
}





// grid默认选中
function onChecked(grid, store, sm) {
	grid.getStore().on('load', function(s, records) {
    	for (var i = 0; i < store.getCount(); i++) {
            sm.selectRow(i, true);
        }
    });
}

// 创建点击不取消其他选择的checkbox
function createMoreCheckboxSelectionModel() {
	return new Ext.grid.CheckboxSelectionModel({
	       	handleMouseDown: function(g, rowIndex, e){  
			    if(e.button !== 0 || this.isLocked()){  
			        return;  
			    }  
			    var view = this.grid.getView();  
			    if(e.shiftKey && !this.singleSelect && this.last !== false){  
			        var last = this.last;  
			        this.selectRange(last, rowIndex, e.ctrlKey);  
			        this.last = last;
			        view.focusRow(rowIndex);  
			    }
			} 
		});
}

function createAlert(msg, message, fn) {
	Ext.MessageBox.alert(msg, message, fn);
}

if  (!Ext.grid.GridView.prototype.templates) {  
    Ext.grid.GridView.prototype.templates = {};  
}  
Ext.grid.GridView.prototype.templates.cell =  new  Ext.Template(  
     '<td class="x-grid3-col x-grid3-cell x-grid3-td-{id} x-selectable {css}" style="{style}" tabIndex="0" {cellAttr}>' ,  
     '<div class="x-grid3-cell-inner x-grid3-col-{id}" {attr}>{value}</div>' ,  
     '</td>'  
);

function createOpenUrl(id) {
	var url = "initExpressDetail?invoiceNo=" + id;
	// initExpressDetail "快件明细"
	var obj = parent.createNodeTab(id, id, url);
	parent.createMainTab(obj);
}

Array.prototype.contains = function(item){ 
	return RegExp(item).test(this); 
};

function getQueryString(name) {
	var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
	var r = window.location.search.substr(1).match(reg);
	if (r != null) return unescape(r[2]); return null;
}


//获取文件后缀
function filePrefix(file_name){
	var result =/\.[^\.]+/.exec(file_name);
	return result;
}

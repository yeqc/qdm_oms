//var default_leng = 4;
var boxHeight = 100;//一个图标加上留白需要的高度
var clientHeight = document.body.clientHeight//屏幕网页可见高度
var default_leng = parseInt(clientHeight/boxHeight);//列记录数

var fee_edit = false;

function write(auth, type) {

	getChannelShop();

	var data = getMenuData();
	
	var html = "";
	
	//var url = path + "/banggo/desktop/treePanel.jsp?type=";
	var url = path + "/custom/index/index.spmvc?type=";
	var logoUrl = path+"/logo/";
	
	var icon = desktop_path + "images/";
	
	var leng = data.length;
	var count = leng % default_leng == 0 ?  leng / default_leng : (leng / default_leng) + 1;
	for (var j = 0; j < count; j++) {
		html += "<div style=\"padding-left:10px;;px;float:left\"><dl id=\"x-shortcuts\">";
		for (var i = 0; i < default_leng; i++) {
			var index = (j * default_leng) + i;
			var temp = data[index];
			if (temp.menu == true) {
				var self_url = url + temp.id;
				if (temp.leaf == true && temp.expanded == true) {
				  //self_url += "&flag=1";
					
					self_url+="&shopCode="+temp.shopCode;
					self_url+="&channelCode="+temp.channelCode;
					self_url+="&menuType="+temp.menuType;
				  //self_url+="&system=0";
			      //self_url+="&channelCode="+temp.channelCode;
				  //self_url+="&channelTitle="+temp.channelTitle;
					
					//系统
					if("system"==temp.shopCode ){
						self_url+="&system=1";
					}	
		
				}
				var theText = temp.text;
				/*if(theText=='日志'){
					temp.shopImg ="icon_log.png";
				}else if(theText=='集团门店'){
					temp.shopImg ="icon_groupshop.png";
				}else if(theText=='OPENSHOP店铺'){
					temp.shopImg ="icon_openshop.png";
				}else if(theText=='产品'){
					temp.shopImg ="icon_product.png";
				}else if(theText=='系统设置'){
					temp.shopImg ="icon_systeminstall.png";
				}*/
				
				/*if(theText.indexOf('天猫')>=0){
					temp.shopImg ="icon_tmall.png";
				}else if(theText.indexOf('淘宝')>=0){
					temp.shopImg ="icon_taobao.png";
				}else if(theText.indexOf('京东')>=0){
					temp.shopImg ="icon_jd.png";
				}else if(theText.indexOf('一号店')>=0){
					temp.shopImg ="icon_yhd.png";
				}else if(theText.indexOf('爱奇艺')>=0){
					temp.shopImg ="icon_qiy.png";
				}else if(theText.indexOf('有范APP')>=0){
					temp.shopImg ="icon_youfan.png";
				}else if(theText.indexOf('苏宁')>=0){
					temp.shopImg ="icon_sn.png";
				}else if(theText.indexOf('当当')>=0){
					temp.shopImg ="icon_dd.png";
				}else if(theText.indexOf('贝贝')>=0){
					temp.shopImg ="icon_bb.png";
				}else if(theText.indexOf('拍拍')>=0){
					temp.shopImg ="icon_pp.png";
				}else if(theText.indexOf('聚美')>=0){
					temp.shopImg ="icon_jumei.png";
				}else if(theText.indexOf('美丽说')>=0){
					temp.shopImg ="icon_mls.png";
				}else if(theText.indexOf('唯品会')>=0){
					temp.shopImg ="icon_wph.png";
				}else if(theText.indexOf('楚楚街')>=0){
					temp.shopImg ="icon_ccj.png";
				}else if(theText.indexOf('蚂蚁')>=0){
					temp.shopImg ="icon_my.png";
				}else if(theText.indexOf('1688')>=0){
					temp.shopImg ="icon_1688.png";
				}else if(theText.indexOf('微信')>=0){
					temp.shopImg ="icon_wx.png";
				}else if(theText.indexOf('蘑菇街')>=0){
					temp.shopImg ="icon_mgj.png";
				}else if(theText.indexOf('邦购')>=0){
					temp.shopImg ="icon_bg.png";
				}
                 if(null ==temp.shopImg  || "" == temp.shopImg){
                	 temp.shopImg ="icon_common.png";
                 }*/
				
				// html += getModel("win-" + temp.id, temp.text, self_url, icon + temp.icon);
				html += getModel("win-" + temp.id, temp.text, self_url, logoUrl+temp.shopImg);
				// html += getModel("win-" + temp.id, temp.text, self_url, logoUrl +  encodeURI(encodeURI(temp.shopImg)));
               //  html += getModel("win-" + temp.id, temp.text, self_url, logoUrl +encodeURIComponent(temp.shopImg));
			}
			if (index + 1 == leng) {
				j = count;
				break;
			}
		}
		html += "</dl></div>";
	}
	document.writeln(html);
	
	if (auth.indexOf(",cwgl_fee_edit,") != -1) {
		fee_edit = true;
	}
}

// 获取桌面图标
function getModel(name, title, url, img, id) {
	var _id = "grid-win-shortcut";
	
	if (id) {
		_id = id;
	}
	var dt = '<dt id="' +_id+'" url="' + url +'" title="' +title+ '" name="' + name +'">';
	dt += '<a href="#">';
	//dt += '<img src="'+desktop_path+'images/s.gif" style="background-image:url(' + img +');filter:progid:DXImageTransform.Microsoft.AlphaImageLoader(src=\'' + img +'\', sizingMethod=\'scale\');"/>';
	dt += '<img src="'+desktop_path+'images/s.gif" style="background-image:url(' + img +'); filter:progid:DXImageTransform.Microsoft.AlphaImageLoader(src=\'' + img +'\',   sizingMethod=\'scale\');"/>';
//	dt += '<img src="'+desktop_path+'images/s.gif" style="background-image:url(' + img +');filter:progid:DXImageTransform.Microsoft.AlphaImageLoader(src=\'' + img +'\', sizingMethod=\'scale\');"/>';
	dt += '<div >' + title +'</div>';
	dt += '</a>';
	dt += '</dt>';
	return dt;
}
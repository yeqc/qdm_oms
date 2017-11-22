<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Frameset//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-frameset.dtd">
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/page/page.jsp"%>
<input type="hidden" id="system" name="system"  value="<%=request.getParameter("system")%>" />
<input type="hidden" id="channelCode" name="channelCode"  value="<%=request.getParameter("channelCode")%>" />
<input type="hidden" id="shopCode" name="shopCode"  value="<%=request.getParameter("shopCode")%>" />
<input type="hidden" id="menuType" name="menuType"  value="<%=request.getParameter("menuType")%>" />
<%@ include file="/page/script.jsp"%>
<html>
<head>
	<title>Channel</title>
	<style type="text/css">
		html, body {
			/**font:normal 12px verdana;*/
			font:normal 12px ;
			margin:0;
			padding:0;
			border:0 none;
			overflow:hidden;
			height:100%;
		}
	</style>
<%
 String no = "admin";
%>
<script type="text/javascript">
// 全局路径
var basePath = "<%=basePath%>";
var no = "<%=no%>";
var cnode;
Ext.BLANK_IMAGE_URL = 'js/ext3.4/resources/images/default/s.gif';
var pageSize = 20;//分页-每页数据条数



function shop(systemStr,shopCode, channelCode, channelTitle,shopTitle){
	var obj=new Object();
	obj.system=systemStr;
	obj.shopCode=shopCode;
	obj.channelCode=channelCode;
	obj.channelTitle=channelTitle;
	obj.shopTitle=shopTitle;
	return obj; 
}
var shopObj = new shop('${system}','${channelShopVo.shopCode}', '${channelShopVo.channelCode}', '${channelShopVo.channelTitle}','${channelShopVo.shopTitle}');

function getGlobalValue(key){
	if (shopObj != null) {
		var value = shopObj[key];
		return value;
	}
}

function logout(){
	Ext.Msg.confirm("退出", "确定要退出吗？", function(btn) {
		if (btn == "yes") {
			location.href="<%=basePath%>page/user/userLogout.ct";
		}
	});
}

//头部菜单
var issMainMenu = new Ext.Toolbar({
	enableOverflow: true,
	height: 30
});
issMainMenu.add(
	new Ext.Toolbar.Fill({id:'fill'}),
	new Ext.Toolbar.TextItem(no+",欢迎您"),
	'-',
	{
		id: 'aad',
		text:'资料修改',
		handler: function(){
			FormEditWin.showAddDirWin("popWins","<%=basePath%>/page/user/userUpdate.ct?no=<%=no %>","userWinID","编辑用户",550,420);
		}
	},'-',
	{
		id: 'ee',
		text:'退出',
			handler: function(){
			logout();
			//Ext.Msg.alert("提示","你已安全退出");
			//location.href="<%=basePath%>login.html";
		}
	}
);

//主页面
IssmainCenterTabs = function() {
	IssmainCenterTabs.superclass.constructor.call(this, {
		id: 'issmainCenterTabs',
	 	region: 'center', // a center region is ALWAYS required for border layout
		deferredRender: false,///一次性将选项卡内容全部加载,不推荐
		activeTab: 0,
		resizeTabs:true, // turn on tab resizing
		minTabWidth: 115,
		tabWidth:135,
		enableTabScroll:true,
		width:600,
		height:250,
		margins: '0 5 0 0',
		defaults: {autoScroll:true},
		plugins: new Ext.ux.TabCloseMenu()
	}); 
};
	
Ext.extend(IssmainCenterTabs, Ext.TabPanel, {
	loadClass2 : function(href, cls){
		var id = 'iss-' + cls;
		var tab = this.getComponent(id);
		if(tab){
			this.setActiveTab(tab);
			/*if(cls.id=='iss-内容管理  '){
			 var autoLoad = {url: href, scripts: true};
			 tab.autoLoad=autoLoad;
			}*/
		}else{
			var autoLoad = {url: href, scripts: true};
			var p = this.add({
				id: id,
				title: cls,
				frame : true,
				autoScroll : true,
				autoShow : true,
				autoLoad: autoLoad,
				closable:true
			});
			this.setActiveTab(p);
		}
	},
	loadClass : function(href, cls, idx){
		var id = 'iss-' + idx;
		var tab = this.getComponent(id);
		if(tab){
			this.setActiveTab(tab);
			/*if(cls.id=='iss-内容管理  '){
			 var autoLoad = {url: href, scripts: true};
			 tab.autoLoad=autoLoad;
			}*/
		}else{
			var autoLoad = {url: href, scripts: true};
			var p = this.add({
		   		id: id,
				title: cls,
				frame : true,
				autoScroll : true,
				autoShow : true,
				autoLoad: autoLoad,
				closable:true
			});
			this.setActiveTab(p);
		}
	}
});


var issmainCenterTabs = new IssmainCenterTabs();

//共通菜单跳转函数
function commonMenu(item, url,idx){
	issmainCenterTabs.loadClass(url,item.text,idx);	
}

function initPage(){
	runContree();
	/**
	if(!issMainMenu.findById('2')) {
		var treepanel =  Ext.getCmp('tNode_20110714');
		if(treepanel) {
			treepanel.collapse();
			treepanel.hide();
		}
	} else {
		runContree();
	}
	**/
}


//页面初始化
Ext.onReady(function(){
		//			couponPanel = new Ext.Panel({
		//				renderTo : 'coupon-grid',

	//					layout : 'column',
		//				items : [ outChannelShopForm, couponGrid ]
			//		}).show();
			

	//issmainCenterTabs.add({
	//	id:'iss-welcome',
	//	title: 'welcome',
	//html: '统一渠道',
	//loader: new Ext.tree.TreeLoader({dataUrl:basePath+ "custom/channelArea/shopGoodsUpDownPage.spmvc"}),
		//closable:true
	//}).show();
	
	var panel = new Ext.Panel({
		layout : 'border',
		items : [{
			id:'tNode_20110714',
			layout:'fit',
			region: 'west',
			split: true,
			contentEl:'treeNode',
			width: 232,
			autoHeight:true,
			height:500
		} ,
		issmainCenterTabs]
	});

	var viewport = new Ext.Viewport({
		layout:'fit',
		items: [panel]
	});
	initPage();
	});
	</script>
</head>
<body>
	
	<div id="south" class="x-hide-display">
		<table>
			<tr>
				<td align="left" width="800">
				</td>
			</tr>
		</table>
		
	</div>
	<div id="treeNode"  class="x-hide-display" style="over-flow:scroll-y;">
		<div id="temtree"></div>
		<div id="contree"></div>
	</div>

</body>
</html>
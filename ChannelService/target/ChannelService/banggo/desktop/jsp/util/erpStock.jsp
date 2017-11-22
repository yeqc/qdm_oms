<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
%>

<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>ERP库存详情</title>
		<style>
    		.x-form-display-field-left{
    			padding-top: 2px;
    			text-align:left;
			}
		</style>
		
		<script type="text/javascript" src="<%=path%>/banggo/js/common/ext.js"></script>
		<script type="text/javascript" src="<%=path%>/banggo/js/common/common.js"></script>
		<script>
			var path = "<%=path%>";
			var isExpand = true;
			var tempUserData = [];
			var selectform;
			// 每页记录数
			var limit = 20;
			var store;
			Ext.onReady(function() {
				Ext.QuickTips.init();
				var maxSelectHeight = 95;
				var minSelectHeight = 25;
				var toolbarHeight = 25;
				
				setDefault();
				
				selectform = new Ext.form.FormPanel({
					title:"查询条件",
					frame:true,
					renderTo:"condition",
					collapsible:true,
					labelWidth:60,
                	labelAlign:'right',
                	keys:{
						key:Ext.EventObject.ENTER,
						fn:function(btn,e){
							getQuery();
						}
					},
					items:[
	            		{xtype: 'compositefield',
	                		items: [
	                			{xtype:"textfield",name:"sku",id:"sku",width:150,fieldLabel:"11位码"}
	                		]
	            		}
					],
					buttons:[
						{text:"检索",handler:function(){
					   		getQuery();
						}},
						{text:"重置",handler:function(){
	                    	selectform.getForm().reset();
	                  	}}
					],
					buttonAlign:"right"
				});
				
				// 展开
				selectform.on("expand", function(){
					isExpand = true;
					setColl();
				});
				
				// 收缩
				selectform.on("collapse", function(){
					isExpand = false;
					setColl();
				});
					
				// 列表数据
				store = new Ext.data.JsonStore({
					storeId:"dataStore",
					fields:[
						{name:'source'},
					    {name:'orgCode'},
					    {name:'caption'},
					    {name:'value'},
					    {name:'customCode'},
						{name:'remark'}
			      	],
					//url:path+'/erp/getErpStockList.htm',
					root:'list',
					totalProperty:'total'
				});
				
				store.on("beforeload", function(obj, opt) {
		            var body = Ext.getBody();
	            	body.mask('Loading', 'x-mask-loading');
	            });
	            
	            store.on("load", function(obj,record) {
	            	var body = Ext.getBody();
	            	body.unmask();
	            });
	            
				var columns = [
					new Ext.grid.RowNumberer({
						header:"序号",
						width:35,
		    	  		renderer:function(value,metadata,record,rowIndex){
		    		   		return getRecordStart() + 1 + rowIndex;
		    			}
		    		}),
		    		{header:'11位码',dataIndex:'customCode',align:"center",renderer:function(value,metadata,record){
						return value;
					}},
					{header:'信息来源',dataIndex:'source',align:"center",renderer:function(value,metadata,record){
						return value;
					}},
		    		{header:'仓库编码',dataIndex:'orgCode',align:"center",width:150,renderer:function(value,metadata,record){
						return getLeftFormate(value);
					}},	
					{header:'项目名称',dataIndex:'caption',align:"center",width:150,renderer:function(value,metadata,record){
						return getLeftFormate(value);
					}},	
					{header:'库存数量',dataIndex:'value',align:"center",width:150,renderer:function(value,metadata,record){
						return getLeftFormate(value);
					}},	
					{header:'备注',dataIndex:'remark',align:"center",width:150,renderer:function(value,metadata,record){
						return getLeftFormate(value);
					}}
				];
				
				// 列表
				var model = getGridPanelModel("grid0", "ERP库存详情", columns, store, 0, Ext.get("grid"));
				var grid = createGridPanelByModel(model);
				var toolBar = createLocalPagingToolbar(store, limit);
   		    	toolBar.setHeight(toolbarHeight);
				toolBar.render("toolBar");
   		    	setDefaultPage(0, limit, store);
   		    	
   		    	function checkQuery() {
					var sku =  Ext.fly('sku').getValue();
					
					if (!sku) {
						createAlert("提示信息", "请输入商品11位码 !");
						return false;
					}
					
					return true;
   		    	}
   		    	
				// 检索
				function getQuery() {
					
					if (checkQuery()) {
						tempUserData = [];
						setDefaultPage(0, limit, store);
						
						var sku = getById('sku');
				    	/*var store = Ext.StoreMgr.lookup("dataStore");
				    	setDefaultPage(0, limit, store);
				    	store.setBaseParam("sku", sku);
				    	store.load();*/
				    	var url = path+'/erp/getErpStockList.htm';
				    	var param = {};
				    	param.sku = sku;
				    	
				    	Ext.getBody().mask('Loading', 'x-mask-loading');
				    	createAjax(url, doSuccess, null, param);
					}
				}
				
				// 同步库存成功回调函数
				function doSuccess(response, opts) {
					Ext.getBody().unmask();
					var res = Ext.util.JSON.decode(response.responseText);
					if (res) {
						tempUserData = res.list;
						getPageList(0);
					}
				}
				
				function setDefault() {
					var clientHeight = document.body.clientHeight;
					
					Ext.get("condition").setHeight(maxSelectHeight);
					Ext.get("grid").setHeight(clientHeight - maxSelectHeight -toolbarHeight);
	           	}
	           	
	           	function setColl() {
					setResize();
				}
	           	
	           	function setResize() {
					var clientHeight = document.body.clientHeight;
					var clientWidth = document.body.clientWidth;
					var height = isExpand == true ? maxSelectHeight : minSelectHeight;
						
	        		Ext.get("condition").setHeight(height);
	        		Ext.get("grid").setHeight(clientHeight-height -toolbarHeight);
					grid.setHeight(clientHeight-height);
					
					Ext.get("condition").setWidth(clientWidth);
	        		Ext.get("grid").setWidth(clientWidth);
					grid.setWidth(clientWidth);
				}
				
				window.onresize=function(){
					setResize();
	        	}
			});
			
			// 设置每页数据
  			function getPageList(start) {
  				var data = {};
				var count = tempUserData.length;
				data.total = count;
				var tempData = [];
				if (start + limit <= count) {
					for (var i = 0; i < limit; i++) {
						var record = tempUserData[start + i];
						tempData.push(record);
					}
				} else {
					for (var i = start; i < count; i++) {
						var record = tempUserData[i];
						tempData.push(record);
					}
				}
				data.list = tempData;
				store.loadData(data, false);
			}
		</script>
	</head>
	
	<body>
		<div id="condition" style="width:100%;"></div>
    	<div class="clear"></div>
    	<div id="grid" style="width:100%"></div>
    	<div class="clear"></div>
    	<div id="toolBar" style="width:100%;height:25px;"></div>
  	</body>
</html>

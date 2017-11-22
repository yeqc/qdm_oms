<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
%>

<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>sku变动日志查询</title>
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
			
			Ext.onReady(function() {
				Ext.QuickTips.init();
				var maxSelectHeight = 95;
				var minSelectHeight = 25;
				setDefault();
				// 每页记录数
				var limit = 20;
				
				var selectform = new Ext.form.FormPanel({
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
				var store = new Ext.data.JsonStore({
					storeId:"dataStore",
					fields:[
						{name: "ids",mapping:"id.inc"},
						{name:'sku'},
					    {name:'oldRealStock'},
					    {name:'oldRealFrozenStock'},
					    {name:'oldVirStock'},
					    {name:'oldVirFrozenStock'},
					    {name:'updateTimestamp'},
					    {name:'businessId'},
					    {name:'changeSource'},
					    {name:'changeValue'},
					    {name:'changeType'}, // 1:增加、2:减少、3:覆盖 
					    {name:'resultCode'}, // 1：真实、2：虚拟、3：真实&虚拟、4：库存不足、5：参数错误、6：未知错误
					    {name:'resultMsg'}
			      	],
					url:path+'/log/getStockChangeLog.htm',
					root:'list',
					idProperty: 'ids',
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
					{header:'商品11位码',dataIndex:'sku',align:"center",renderer:function(value,metadata,record){
						return value;
					}},
		    		{header:'原实际库存',dataIndex:'oldRealStock',align:"center",renderer:function(value,metadata,record){
						return getRightFormate(value);
					}},
					{header:'原虚拟库存',dataIndex:'oldVirStock',align:"center",renderer:function(value,metadata,record){
						return getRightFormate(value);
					}},
					{header:'原实际库存冻结',dataIndex:'oldRealFrozenStock',align:"center",renderer:function(value,metadata,record){
						return getRightFormate(value);
					}},
					{header:'原虚拟库存冻结',dataIndex:'oldVirFrozenStock',align:"center",renderer:function(value,metadata,record){
						return getRightFormate(value);
					}},
					{header:'变化值',dataIndex:'changeValue',align:"center",renderer:function(value,metadata,record){
						return getRightFormate(value);
					}},
					{header:'变化类型',dataIndex:'changeType',align:"center",renderer:function(value,metadata,record){
						if (value == 1) {
							value = "增加";
						} else if (value == 2) {
							value = "减少";
						} else if (value == 3) {
							value = "覆盖 ";
						} 
						return value;
					}},
					{header:'来源',dataIndex:'changeSource',align:"center",renderer:function(value,metadata,record){
						return getLeftFormate(value);
					}},
					{header:'返回情况',dataIndex:'resultCode',align:"center",renderer:function(value,metadata,record){
						if (value == 1) {
							value = "实际";
						} else if (value == 2) {
							value = "虚拟";
						} else if (value == 3) {
							value = "实际 &虚拟";
						}  else if (value == 4) {
							value = "库存不足";
						}  else if (value == 5) {
							value = "参数错误";
						}  else if (value == 6) {
							value = "未知错误";
						} 
						return value;
					}},
					{header:'处理时间',dataIndex:'updateTimestamp',align:"center",width:150,renderer:function(value,metadata,record){
						if (value) {
							value = formatDateformate(value, "Y-m-d H:i:s");
						}
						return getLeftFormate(value);
					}},
					{header:'备注',dataIndex:'resultMsg',align:"center",width:150,renderer:function(value,metadata,record){
						return getLeftFormate(value);
					}}
				];
				
				// 列表
				var model = getGridPanelModel("grid0", "商品库存变化列表", columns, store, limit, Ext.get("grid"));
				var grid = createGridPanelByModel(model);
   		    	setDefaultPage(0, limit, store);
   		    	
   		    	function checkQuery() {
					var sku = getById('sku');
					
					if (!sku) {
						createAlert("提示信息", "请输入11位码 !");
						return false;
					}
					
					return true;
   		    	}
   		    	
				// 检索
				function getQuery() {
					
					if (checkQuery()) {
						var sku =  Ext.fly('sku').getValue();
				    	var store = Ext.StoreMgr.lookup("dataStore");
				    	setDefaultPage(0, limit, store);
				    	store.setBaseParam("sku", sku);
				    	store.load();
					}
				}
				
				function setDefault() {
					var clientHeight = document.body.clientHeight;
					
					Ext.get("condition").setHeight(maxSelectHeight);
					Ext.get("grid").setHeight(clientHeight - maxSelectHeight);
	           	}
	           	
	           	function setColl() {
					setResize();
				}
	           	
	           	function setResize() {
					var clientHeight = document.body.clientHeight;
					var clientWidth = document.body.clientWidth;
					var height = isExpand == true ? maxSelectHeight : minSelectHeight;
						
	        		Ext.get("condition").setHeight(height);
	        		Ext.get("grid").setHeight(clientHeight-height);
					grid.setHeight(clientHeight-height);
					
					Ext.get("condition").setWidth(clientWidth);
	        		Ext.get("grid").setWidth(clientWidth);
					grid.setWidth(clientWidth);
				}
				
				window.onresize=function(){
					setResize();
	        	}
			});
		</script>
	</head>
	
	<body>
		<div id="condition" style="width:100%;"></div>
    	<div class="clear"></div>
    	<div id="grid" style="width:100%"></div>
  	</body>
</html>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
%>

<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>增量运行结果日志</title>
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
			importJs("DateTimeField.js");
			
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
	                			{xtype:"textfield",name:"sku",id:"sku",width:150,fieldLabel:"商品编码"},
	                			{xtype:'displayfield',value:'运行时间:', width:60},
	                			{xtype : "datefield",width : 150,id : "startTime",name : "startTime",format : "Y-m-d H:i:s"},
	                			{xtype:'displayfield',value:'-', width:60},
	                			{xtype: "datefield",width : 150,id : "endTime",name : "endTime",format : "Y-m-d H:i:s"}
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
					    {name:'dateTime'},
					    {name:'custumCode'},
						{name:'isOnSell'},
						{name:'channelCode'}
			      	],
					url:path+'/jobLog/getJobLogList.htm',
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
		    		{header:'商品编码',dataIndex:'custumCode',align:"center",renderer:function(value,metadata,record){
						return value;
					}},	
					{header:'渠道',dataIndex:'channelCode',align:"center",width:150,renderer:function(value,metadata,record){
						return value;
					}},	
					{header:'上下架',dataIndex:'isOnSell',align:"center",renderer:function(value,metadata,record){
						if ("0" == value) {
							value = "下架";
						} else if ("1" == value) {
							value = "上架";
						}
						return value;
					}},	
					{header:'运行时间',dataIndex:'dateTime',align:"center",width:150,renderer:function(value,metadata,record){
						return value;
					}}
				];
				
				// 列表
				var model = getGridPanelModel("grid0", "日志列表", columns, store, limit, Ext.get("grid"));
				var grid = createGridPanelByModel(model);
   		    	setDefaultPage(0, limit, store);
   		    	
				// 检索
				function getQuery() {
					
					var startTime =  Ext.fly('startTime').getValue();
					var endTime =  Ext.fly('endTime').getValue();
					var sku = getById('sku');
					
					if (sku || (startTime && endTime)) {
						var store = Ext.StoreMgr.lookup("dataStore");
				    	setDefaultPage(0, limit, store);
				    	store.setBaseParam("sku", sku);
				    	store.setBaseParam("startTime", startTime);
				    	store.setBaseParam("endTime", endTime);
				    	store.load();
					} else {
						if (startTime) {
							createAlert("提示信息", "请输入运行结束时间");
						} else if (endTime) {
							createAlert("提示信息", "请输入运行开始时间");
						} else {
							createAlert("提示信息", "请输入查询条件");
						}
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

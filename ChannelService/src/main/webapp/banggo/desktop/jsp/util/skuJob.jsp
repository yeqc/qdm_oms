<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
%>

<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>渠道sku同步</title>
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
				var maxSelectHeight = 135;
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
	                			{xtype:"textarea",name:"goodsSn",id:"goodsSn",width:400,fieldLabel:"6位码"},
	                			{xtype:'displayfield',value:'(多个6位码以,隔开)', width:120}
	                		]
	            		}
					],
					buttons:[
						{text:"检索",handler:function(){
					   		getQuery();
						}},
						{text:"更新",handler:function(){
							doSubmit();
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
						{name:'goodsName'},
					    {name:'custumCode'},
					    {name:'isOnSell'},
					    {name:'channelCode'},
						{name:'reqBy'}
			      	],
					url:path+'/scChannelSku/getScChannelSkuList.htm',
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
					{header:'商品11位码',dataIndex:'custumCode',align:"center",renderer:function(value,metadata,record){
						return value;
					}},
		    		{header:'商品名称',dataIndex:'goodsName',align:"center",width:300,renderer:function(value,metadata,record){
						return getLeftFormate(value);
					}},	
					{header:'渠道',dataIndex:'channelCode',align:"center",width:150,renderer:function(value,metadata,record){
						return getLeftFormate(value);
					}},	
					{header:'上下架',dataIndex:'isOnSell',align:"center",renderer:function(value,metadata,record){
						if (value == 1) {
							value = "上架";
						} else if (value == 0) {
							value = "下架";
						} 
						return value;
					}}
				];
				
				// 列表
				var model = getGridPanelModel("grid0", "商品列表", columns, store, limit, Ext.get("grid"));
				var grid = createGridPanelByModel(model);
   		    	setDefaultPage(0, limit, store);
   		    	
   		    	function checkQuery() {
					var goodsSn = getById('goodsSn');
					
					if (!goodsSn) {
						createAlert("提示信息", "请输入6位码 !");
						return false;
					}
					
					var goodsLeng = goodsSn.split(",");
					if (goodsLeng.length > 10) {
						createAlert("提示信息", "最多更新10个6位码 !");
						return false;
					}
					
					return true;
   		    	}
   		    	
				// 检索
				function getQuery() {
					
					if (checkQuery()) {
						var goodsSn =  Ext.fly('goodsSn').getValue();
				    	var store = Ext.StoreMgr.lookup("dataStore");
				    	setDefaultPage(0, limit, store);
				    	store.setBaseParam("goodsSn", goodsSn);
				    	store.load();
					}
				}
				
				// 提交调整单
				function doSubmit() {
					
					if (checkQuery()) {
						var goodsSn =  Ext.fly('goodsSn').getValue();
						var msg = "确定批量更新吗？";
						
						Ext.Msg.confirm("警告",msg,function(btn){
	                    	if(btn == "yes"){
	                    		var params = {};
	    						params.goodsSn = goodsSn;
								Ext.getBody().mask('Loading', 'x-mask-loading');
								createAjax(path + "/scChannelSku/getScChannelSkuByGoods.htm", doSuccess, doFail, params);
				    		}
				    	});
					}
				}
				
				// 批量调整成功回调函数
				function doSuccess() {
					Ext.getBody().unmask();
					createAlert("提示信息", "操作成功 !");
				}
				
				function doFail() {
					Ext.getBody().unmask();
					createAlert("提示信息","操作失败");
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

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
%>

<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>可售库存调整单</title>
		<style>
    		.x-form-display-field-left{
    			padding-top: 2px;
    			text-align:left;
			}
		</style>
		<script type="text/javascript" src="<%=path%>/banggo/js/common/ext.js"></script>
		<script type="text/javascript" src="<%=path%>/banggo/js/common/common.js"></script>
		<script>
			// 查询form
			var selectform;
			var path = "<%=path%>";
			var isExpand = true;
			// 调整单号
			var stockChangeNo = getQueryString("stockChangeNo");
			var state = getQueryString("state");
			// 每页记录数
			var limit = 20;
			// 记录
			var store;
			// 记录批量调整值
			var batchChangeStock;
			// 记录手工指定的调整值对象
			var localChangeStock = [];
			// 记录手工执行的sku
			var localChangeSku = [];
			
			Ext.onReady(function() {
				Ext.QuickTips.init();
				// 查询区域的高度
				var maxSelectHeight = 95;
				var minSelectHeight = 25;
				var toolbarHeight = 25;
				
				setDefault();
				
				// 初始请求
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
								{xtype:"textfield",name:"num",id:"num",width:150,fieldLabel:"商品码"}
							]
						}
					],
					buttons:[
						{text:"检索",handler:function(){
					   		getQuery();
						}},
						{text:"重置",handler:function(){
	                    	selectform.getForm().reset();
	                  	}},
						{text:"关闭",handler:function(){
							close();
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
						{name:'id'},
						{name:'sku'},
						{name:'realStock'},
						{name:'saleStock'},
						{name:'virStock'},
						{name:'virFrozenStock'},
						{name:'status'},
						{name:'lastSyncDate'},
						{name:'kValue'},
						{name:'stock'},
						{name:'changeStock'}
			      	],
			      	url:path + "/scStockChange/getScStockChangeSkuMap.htm",
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
	            	var skuCount = localChangeSku.length;
            		if (skuCount > 0 || batchChangeStock) {
            			var count = record.length;
		            	for (var i = 0; i < count; i++) {
		            		var temp = record[i];
		            		var sku = temp.get('sku');
		            		var check = false;
		            		if (skuCount > 0) {
		            			for (var j = 0; j < skuCount; j++) {
		            				if (sku == localChangeSku[j]) {
		            					check = true;
		            					temp.set('changeStock', localChangeStock[j]);
		            					break;
		            				}
		            			}
		            		} 
		            		
		            		if (!check) {
		            			if (batchChangeStock) {
			            			temp.set('changeStock', batchChangeStock);
			            		}
		            		}
		            	}
            		}
	            });
				
	            var sm = new Ext.grid.CheckboxSelectionModel({  
    				checkOnly: true,  
    				singleSelect: false
				}); 
	            
	            var fm = Ext.form;
	            
				var columns = [
					sm,
					new Ext.grid.RowNumberer({
						header:"序号",
						width:35,
		    	  		renderer:function(value,metadata,record,rowIndex){
		    		   		return getRecordStart() + 1 + rowIndex;
		    			}
		    		}),
					{header:'商品11位码',dataIndex:'sku',width:120,align:"center",sortable:true,renderer:function(value){
						return getLeftFormate(value);
					}},
					{header:'实物库存',dataIndex:'realStock',align:"center",sortable:true,renderer:function(value,metadata,record){
						return getRightFormate(value);
					}},	
					{header:'最近一次同步时间',dataIndex:'lastSyncDate',align:"center",sortable:true,width:150,renderer:function(value, metadata, record){
						if(value){
							return getLeftFormate(formatDateformate(value, "Y-m-d H:i:s"));
						}
					}},
					{header:'人工设置库存',dataIndex:'changeStock',align:"center", editor: new fm.NumberField({
		                allowBlank: false,
		                allowNegative: false,
		                maxValue: 100000
		            }),renderer:function(value){
						return getRightFormate(value);
					}}
				];
				
				var grid;
				var barItem = [];
				barItem.push("-");
				//barItem.push({id:"delete", text:"删除", iconCls:"delete", handler:function(){doDelete();}});
				barItem.push({id:"log", text:"日志查看", iconCls:"add", handler:function(){doLog();}});
				
				var tb = new Ext.Toolbar({
					items:barItem
				});
				if (state == 0) {
					// 列表尾部工具条
					var foot_tb = new Ext.Toolbar({
						items:[
							{xtype : "tbfill"},
							{xtype: 'tbtext', text: '人工设置库存:'},
							{id: 'batchKValue',name:'batchKValue',xtype:'numberfield',width:100,emptyText:'批量设置库存'},
				   			"-",
							{id:"batch", text:"确定", iconCls:"add", handler:function(){setCommonBatchValue(2);}},
							"-",
							{id:"update", text:"提交调整单", iconCls:"update", handler:function(){doSubmit();}}
						],
						style : 'padding-right:20px'
					});
					
					var cm = new Ext.grid.ColumnModel({
				        columns: columns
				    });
					
					// 列表
					var model = getEditGridPanelModel("grid0", "可售库存调整列表", cm, store, 0, Ext.get("grid"),sm,foot_tb,tb);
					grid = createEditGridPanelByModel(model);
					grid.on('afteredit', afterEdit, this );
				} else if (state == 1) {
					// 列表
					var model = getGridPanelModel("grid0", "可售库存调整列表", columns, store, 0, Ext.get("grid"),sm);
					model.tbar = tb;
					grid = createGridPanelByModel(model);
				}
				
				function setCommonBatchValue(value) {
					localChangeSku = [];
					localChangeStock = [];
					setBatchValue(value);
				}
				
   		    	setDefaultPage(0, limit, store);
   		    	// 分页工具条
				var toolBar = createPagingToolbar(store, limit);
				toolBar.setHeight(toolbarHeight);
				toolBar.render("toolBar");
				
				function afterEdit(e) {
					var record = store.getAt(e.row);
					var sku = record.get("sku");
					var newValue = e.value;
					if (!localChangeSku.contains(sku)) {
						localChangeSku.push(sku); 
						localChangeStock.push(newValue);
					}
				}
				getQuery();
				// 检索
				function getQuery() {
					// 商品码
					var sku = getById('num');
					setDefaultPage(0, limit, store);

					store.setBaseParam("sku", sku);
					store.setBaseParam("stockChangeNo", stockChangeNo);
					store.load();
				}
				
				// 提交调整单
				function doSubmit() {
					var count = localChangeSku.length;
					if (count > 0 || batchChangeStock) {
						var msg = "确定提交调整单吗？";
						
						Ext.Msg.confirm("警告",msg,function(btn){
                        	if(btn == "yes"){
   								// 提交批量更新
   								var params = {};
   								params.stockChangeNo = stockChangeNo;
   								params.stockChangeType = 3;
   								// 备注
   								params.stocks = localChangeStock.join(",");
           						params.changeSkus = localChangeSku.join(",");
           						params.batchChangeStock = batchChangeStock;
           						Ext.getBody().mask('Loading', 'x-mask-loading');
   								createAjax(path + "/scStockChange/doUpdateScStockChangeBatch.htm", doSuccess, doFail, params);
				    		}
				    	});
					} else {
						createAlert("信息提示", "无调整记录信息！");
						return false;
					}
				}
				
				function doSuccess() {
					Ext.getBody().unmask();
					createAlert("提示信息", "操作成功 !" , function() {
						parent.tempLocalData=[];
						close();
					});
				}
				
				function doFail() {
					Ext.getBody().unmask();
					createAlert("提示信息","操作失败");
				}
				
				// 关闭 
				function close() {
					parent.closeTab("saleStockList-" + stockChangeNo);
					//parent.storeWindow.close();
				}
				
				// 查看日志
				function doLog() {
					var url = path + "/banggo/desktop/jsp/stock/log/logList.jsp?stockChangeNo="+stockChangeNo;
					var storeWindow = showWindow("logWindow", "日志查看", url, 650, 400, 50, 20);
					storeWindow.show();
				}
				
				function setDefault() {
					var clientHeight = document.body.clientHeight;
					
					Ext.get("condition").setHeight(maxSelectHeight);
					Ext.get("grid").setHeight(clientHeight - maxSelectHeight-toolbarHeight);
	           	}
	           	
	           	function setColl() {
					setResize();
				}
	           	
	           	function setResize() {
					var clientHeight = document.body.clientHeight;
					var clientWidth = document.body.clientWidth;
					var height = isExpand == true ? maxSelectHeight : minSelectHeight;
						
	        		Ext.get("condition").setHeight(height);
	        		Ext.get("grid").setHeight(clientHeight-height-toolbarHeight);
					grid.setHeight(clientHeight-height-toolbarHeight);
					
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
    	<div class="clear"></div>
    	<div id="toolBar" style="width:100%;height:25px;"></div>
  	</body>
</html>

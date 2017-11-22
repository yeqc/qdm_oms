<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
%>

<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>虚拟库存调整单</title>
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
			var flag = getQueryString("flag");
			var tempUserData = [];
			if (flag == 1 || flag == 3) {
				tempUserData = parent.tempLocalData;
			}
			// 每页记录数
			var limit = 20;
			// 记录
			var store;
			// 删除记录
			var deleteSku = [];
			// 记录批量调整值
			var batchChangeStock = 0;
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
			      	url:path + '/scstock/getSctockGoodsList.htm',
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
	            	if (flag == 2) {
	            		var skuCount = localChangeSku.length;
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
		            			} else {
		            				temp.set('changeStock', temp.get("virStock"));
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
					{header:'虚拟库存',dataIndex:'virStock',align:"center",sortable:true,renderer:function(value,metadata,record){
						return getRightFormate(value);
					}},	
					{header:'已占虚拟库存',dataIndex:'virFrozenStock',align:"center",sortable:true,renderer:function(value,metadata,record){
						return getRightFormate(value);
					}},
					{header:'虚拟库存调整',dataIndex:'changeStock',align:"center", editor: new fm.NumberField({
		                allowBlank: false,
		                allowNegative: false,
		                maxValue: 100000
		            }),renderer:function(value,metadata,record){
						return getRightFormate(value);
					}}
				];
				
				var barItem = [];
				barItem.push("-");
				barItem.push({id:"delete", text:"删除", iconCls:"delete", handler:function(){doDelete();}});
				
				var tb = new Ext.Toolbar({
					items:barItem
				});
				
				// 列表尾部工具条
				var foot_tb = new Ext.Toolbar({
					items:[
						{xtype : "tbfill"},
						{xtype: 'tbtext', text: '虚拟库存调整:'},
						{id: 'batchKValue',name:'batchKValue',xtype:'numberfield',width:100,emptyText:'虚拟库存调整'},
						"-",
						{id:"batch", text:"确定", iconCls:"add", handler:function(){setBatchValue(flag);}},
						"-",
						{id:"update", text:"提交调整单", iconCls:"update", handler:function(){doSubmit();}}
					],
					style : 'padding-right:20px'
				});
				var cm = new Ext.grid.ColumnModel({
			        columns: columns
			    });
				
				// 列表
				var model = getEditGridPanelModel("grid0", "虚拟库存调整列表", cm, store, limit, Ext.get("grid"),sm,foot_tb,tb);
				var grid = createEditGridPanelByModel(model);
				grid.on('afteredit', afterEdit, this );
				
				function afterEdit(e) {
					var record = store.getAt(e.row);
					var sku = record.get("sku");
					var newValue = e.value;
					if (flag == 2) {
						if (!localChangeSku.contains(sku)) {
							localChangeSku.push(sku); 
							localChangeStock.push(newValue);
						}
					} else {
						for (var i = 0, count = tempUserData.length; i < count; i++) {
							var temp = tempUserData[i];
							if (sku == temp.sku) {
								tempUserData[i].changeStock = newValue;
								return;
							}
						}
					}
				}
				
   		    	setDefaultPage(0, limit, store);
   		    	// 分页工具条
				var toolBar;
				if (flag == 2) {
   		    		toolBar = createPagingToolbar(store, limit);
   		    	} else{
   		    		toolBar = createLocalPagingToolbar(store, limit);
   		    	}
				toolBar.setHeight(toolbarHeight);
				toolBar.render("toolBar");
				
				getQuery();
				
				// 本地查询
				function getLocalQuery(sku) {
					
					if (sku) {
						var count = tempUserData.length;
						var tempData = [];
						var total = 0;
						for (var i = 0; i < count; i++) {
							var record = tempUserData[i];
							var tempSku = record.sku;
							if (tempSku == sku || tempSku.substring(0,6) == sku) {
								tempData.push(record);
								total++;
							}
						}
						var data = [];
						data.total = total;
						data.list = tempData;
						store.loadData(data, false);
					} else {
						getPageList(0);
					}
				}
    			
				// 检索
				function getQuery() {
					// 1 查询追加记录 | 3文件导入查询
					var sku = Ext.fly('num').getValue();
					
					if (flag == 1 || flag == 3) {
						setDefaultPage(0, limit, store);
						getLocalQuery();
					} else if (flag == 2) {
						// 查询父页面列表数据
						/*var param = {};
						var params = parent.Ext.StoreMgr.lookup("dataStore").baseParams;
						for (prop in params) {
							if (prop == 'start' || prop == 'limit' || prop == 'os') {
							} else {
								param[prop] = params[prop];
							}
						}
						param['deleteSku'] = deleteSku.join(",");
						param['flag'] = flag;

						Ext.getBody().mask('Loading', 'x-mask-loading');
						createAjax(path + "/scstock/getSctockList.htm", doSuccessFun, null, param);*/
						
						var params = parent.Ext.StoreMgr.lookup("dataStore").baseParams;
						for (prop in params) {
							if (prop == 'os') {
							} else {
								store.setBaseParam(prop, params[prop]);
							}
						}
						setDefaultPage(0, limit, store);
						store.setBaseParam("sku_or", sku);
						store.setBaseParam("flag", flag);
						store.setBaseParam("deleteSku", deleteSku.join(","));
						store.load();
					}
				}
			
				// 查询成功
				/*function doSuccessFun(response, opts) {
					Ext.getBody().unmask();
					var text = Ext.decode(response.responseText);
					var total = text.total;
					if (total > 0) {
						var list = text.list;
						if (list) {
							setData(list);
						}
					} else {
						createAlert("信息提示","查询无记录");
						return false;
					}
				}*/
				
				// 删除
				function doDelete() {
					var rows = grid.getSelectionModel().getSelections();
		    	  	var leng = rows.length;
		    	  	
		    	  	if (leng == 0) {
		    	  		createAlert("提示信息", "没有选择记录!");
		    	  		return false;
		    	  	} else {
						var msg = "确定删除吗？";
						
						Ext.Msg.confirm("警告",msg,function(btn){
                        	if(btn == "yes"){
                        		if (flag == 2) {
                        			for(var i = 0 ; i < leng; i++){
				    	  				var record = rows[i];
				    	  				deleteSku.push(record.get('sku'));
				    	  			}
				    	  			getQuery();
                        		} else {
                        			// 本地数据
                            		localDelete(rows);
                        		}
				    		}
				    	});
		    	  	}
				}
				
				// 删除本地数据
				function localDelete(row) {
					var leng = row.length;
					for(var i = 0 ; i < leng; i++){
						var record = row[i];
	    	  			var sku = record.get("sku");
	    	  			for (var j = 0; j < tempUserData.length; j++) {
							var temp = tempUserData[j];
							if (temp.sku == sku) {
								tempUserData.splice(j,1);
								break;
							}
						}
		    	 	}
		    	 	setDefaultPage(0, limit, store);
	    	  		getPageList(0);
				}
				
				// 提交调整单
				function doSubmit() {
					if (flag == 2) {
						var count = store.getCount();
						if (count > 0) {
							var msg = "确定批量调整吗？";
							
							Ext.Msg.confirm("警告",msg,function(btn){
	                        	if(btn == "yes"){
	                        		if (localChangeStock.length > 0 || batchChangeStock) {
	                        			// 如果没有batchChangeStock,就只更新执行的sku | 如果有就查询
	                        			// 提交批量更新
	                        			var params = {};
	                        			var param = parent.Ext.StoreMgr.lookup("dataStore").baseParams;
	            						for (prop in param) {
	            							if (prop == 'start' || prop == 'limit' || prop == 'os') {
	            							} else {
	            								params[prop] = param[prop];
	            							}
	            						}
	            						params.flag = flag;
	            						params.stockChangeType = 2;
	            						params.stocks = localChangeStock.join(",");
	            						params.changeSkus = localChangeSku.join(",");
	            						params.batchChangeStock = batchChangeStock;
	            						params.deleteSku = deleteSku.join(",");
	    								Ext.getBody().mask('Loading', 'x-mask-loading');
	    								createAjax(path + "/scStockChange/doSaveScStockChange.htm", doSuccess, doFail, params);
	                        		} else {
	                        			createAlert("信息提示","请设置虚拟库存调整值!");
        								return false;
	                        		}
					    		}
					    	});
						} else {
							createAlert("信息提示", "无调整记录信息！");
							return false;
						}
					} else {
						var count = tempUserData.length;
						if (count > 0) {
							var msg = "确定提交调整单吗？";
							
							Ext.Msg.confirm("警告",msg,function(btn){
	                        	if(btn == "yes"){
	                        		var skus = [];
	                        		var stocks = [];
	                        		for (var i = 0; i < count; i++) {
	    								var record = tempUserData[i];
	    								skus.push(record.sku);
	    								var changeStock = record.changeStock;
	    								if (changeStock || changeStock == 0) {
	    									stocks.push(changeStock);
	    								}
	    								
	    							}
	                        		if (skus.length > 0 && stocks.length > 0) {
	    								// 提交批量更新
	    								var params = {};
	    								params.stockChangeType = 2;
	    								// 备注
	    								params.changeSkus = skus.join(",");
	    								params.stocks = stocks.join(",");
	    								Ext.getBody().mask('Loading', 'x-mask-loading');
	    								createAjax(path + "/scStockChange/doSaveScStockChange.htm", doSuccess, doFail, params);
	    							} else {
	    								if (stocks.length == 0) {
	    									createAlert("信息提示","请设置虚拟库存调整值!");
	        								return false;
	    								} else {
	    									createAlert("信息提示","数据错误!");
	        								return false;
	    								}
	    							}
					    		}
					    	});
						} else {
							createAlert("信息提示", "无调整记录信息！");
							return false;
						}
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
					parent.storeWindow.close();
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
			
			function setData(list, flag) {
				var count = list.length;
				var leng = tempUserData.length;
				var isExsit = false;
				for (var i = 0; i < count; i++) {
					var record = list[i];
					for (var j = 0; j < leng; j++) {
						var temp = tempUserData[j]; 
						if (temp.sku == record.sku) {
							isExsit = true;
							break;
						}
					}
					if (!isExsit) {
						if (!flag) {
							record.changeStock = record.virStock;
						}
						tempUserData.push(record);
					}
				}
				getPageList(0);
			}
			
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

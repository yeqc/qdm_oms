<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
%>

<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>同步库存管理</title>
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
			// 区分操作来源 1追加 | 2批量查询| 3文件上传
			var flag = getQueryString("flag");
			// 文件上传,只是同一渠道sku
			var code = getQueryString("channelCode");
			// 类别 2 渠道库存查询
			var type = getQueryString("type");
			// 上个页面保存的操作记录
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
			// 删除记录id
			var deleteIds = [];
			
			Ext.onReady(function() {
				Ext.QuickTips.init();
				// 查询区域的高度
				var maxSelectHeight = 90;
				var minSelectHeight = 25;
				var toolbarHeight = 25;
				
				setDefault();
				
				// 记录是否查询过
				var check = false;

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
						{name:'sid'},
						{name:'sku'},
						{name:'realStock'},
						//{name:'saleStock'},
						{name:'shareStock'},
						{name:'virStock'},
						{name:'virFrozenStock'},
						{name:'syncFlag'},
						{name:'status'},
						{name:'lastSyncDate'},
						{name:'kValue'},
						{name:'channelCode'},
						{name:'channelName'},
						{name:'stock'}
			      	],
					url:path + '/scstock/getSctockList.htm',
					root:'list',
					idProperty: 'sid',
					//remoteSort:true,
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
				
	            var sm = new Ext.grid.CheckboxSelectionModel({  
    				checkOnly: true,  
    				singleSelect: false
				});
	            
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
						if (value != undefined) {
							return getLeftFormate(value);
						}
					}}
	            ];
	            
	            if (flag != 3) {
	            	 if (type && type == 2) {
	 	            	columns.push(
	                 		{header:'渠道',dataIndex:'channelName',width:150,align:"center",sortable:true,renderer:function(value,metadata,record){
	     						return getLeftFormate(value);
	     					}}
	 	    	  		);
	 	            } else {
	 	            	columns.push(
	 	            		{header:'可共享库存',dataIndex:'shareStock',align:"center",sortable:true,renderer:function(value, metadata, record){
	 	   						return getRightFormate(value);
	 	   					}}
 		 	       		);
	 	            	columns.push(
	 	            		{header:'真实库存',dataIndex:'realStock',align:"center",sortable:true,renderer:function(value,metadata,record){
	 	   						return getRightFormate(value);
	 	   					}}
 		 	       		);
 		 	            columns.push(
		 	            		{header:'各渠道强占实际库存合计',dataIndex:'stock',width:150,align:"center",sortable:true,renderer:function(value,metadata,record){
		 							return getRightFormate(value);
		 						}}
 		 	            );
 		 	            columns.push(
		 	            		{header:'虚拟库存',dataIndex:'virStock',align:"center",sortable:true,renderer:function(value,metadata,record){
		 							return getRightFormate(value);
		 						}}
 		 	            );
 		 	            columns.push(
		 	            		{header:'虚拟冻结库存',dataIndex:'virFrozenStock',align:"center",sortable:true,renderer:function(value,metadata,record){
		 							return getRightFormate(value);
		 						}}
 		 	            );
 		 	           /* columns.push(
		 	            		{header:'ERP未消减的库存',dataIndex:'saleStock',width:150,align:"center",sortable:true,renderer:function(value,metadata,record){
		 							return getRightFormate(value);
		 						}}
 		 	            );*/
 		 	            columns.push(
 		             		{header:'安全阀值',dataIndex:'kValue',align:"center",sortable:true,renderer:function(value,metadata,record){
 		 						return getRightFormate(value);
 		 					}}
 		 	            );
 		 	            columns.push(
 		             		{header:'上次同步时间',dataIndex:'lastSyncDate',width:150,align:"center",sortable:true,renderer:function(value, metadata, record){
 		 						if(value){
 		 							return getLeftFormate(formatDateformate(value, "Y-m-d H:i:s"));
 		 						}
 		 					}}
 		 	            );
	 	            }
	            }
				
				var barItem = [];
				barItem.push("-");
				barItem.push({id:"delete", text:"删除", iconCls:"delete", handler:function(){doDelete();}});
				
				var tb = new Ext.Toolbar({
					items:barItem
				});
				
				// 列表尾部工具条
				var foot_tb = new Ext.Toolbar({
					items:[
						{xtype : "tbfill"}
						,"-"
						,{id:"batchSynch", text:"批量同步库存", iconCls:"add", handler:function(){batchSynch();}}
					],
					style : 'padding-right:20px'
				});
				
				var bb = createLocalPagingToolbar(store, limit)
				
				var model = getGridPanelModel("grid0", "同步库存列表", columns, store, limit, Ext.get("grid"), sm ,foot_tb);
				model.tbar = tb;
				var grid = createGridPanelByModel(model);
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
    			
				// 检索
				function getQuery() {
					// 商品码
					var sku = getById('num');
					if (flag == 2) {
						// 查询父页面列表数据
						var params = parent.Ext.StoreMgr.lookup("dataStore").baseParams;
						for (prop in params) {
							if (prop == 'os') {
							} else {
								store.setBaseParam(prop, params[prop]);
							}
						}
						store.setBaseParam("sku_or", sku);
						store.setBaseParam("flag", flag);
						if (type == 2) {
							store.setBaseParam("uk", deleteSku.join(","));
							store.setBaseParam("deleteIds", deleteIds.join(","));
						} else {
							store.setBaseParam("deleteSku", deleteSku.join(","));
						}
						
						setDefaultPage(0, limit, store);
						store.load();
					} else {
						setDefaultPage(0, limit, store);
						// 查询追加记录
						if (sku || sku.length > 0) {
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
						
				}
				
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
				    	  				
				    	  				var sku;
				    	  				if (type == 2) {
				    	  					sku = record.get('sku') + "_" + record.get('channelCode') + "_" + record.get('channelCode');
				    	  					deleteIds.push(record.get("sid"));
				    	  				} else {
				    	  					sku = record.get('sku');
				    	  				}
				    	  				deleteSku.push(sku);
				    	  			}
				    	  			getQuery();
				    	  		} else {
				    	  			localDelete(rows);
				    	  		}
				    		}
				    	});
		    	  	}
				}

				// 批量同步库存
				function batchSynch() {
					var url = path + "/stock/doBatchStock.htm";
					var param = {};
					
					var msg = "确定批量同步库存吗？";
					Ext.Msg.confirm("警告",msg,function(btn){
                    	if(btn == "yes"){
                    		var params = parent.Ext.StoreMgr.lookup("dataStore").baseParams;
							if (flag == 2) {
								var count = store.getCount();
								
								if (count == 0) {
									createAlert("提示信息", "没有记录,无需同步库存!");
									return false;
								}
								var param = {};
								var sku = Ext.fly('num').getValue();
								for (prop in params) {
									param[prop] = params[prop];
								}
								param.sku_or = sku;
								if (type == 2) {
									param.uk = deleteSku.join(",");
									param.deleteIds = deleteIds.join(",");
								} else {
									param.deleteSku = deleteSku.join(",");
								}
								
							} else {
								var skus = getLocalSkus();
								if (!skus || skus.length == 0) {
									createAlert("提示信息", "没有记录,无需同步库存!" , function() {
										close();
									});
									return false;
								} else {
									param = {"skus" : skus};
									if (type && type == 2) {
										var channelCode = params['channelCode'];
										if (channelCode && channelCode.length > 0)
											param['channelCode'] = channelCode;
										else {
											param['channelCodes'] = getLocalChannelCodes();
										}
										
									}
									
								}
							} 
							param.flag = flag;
							param.type = type;
							Ext.getBody().mask('Loading', 'x-mask-loading');
							doSubmit(url, doSuccess, param);
                    	}
					});
				}
				
				// 同步库存成功回调函数
				function doSuccess(frm, action) {
					Ext.getBody().unmask();
					var res = action.result;
					var total = res.total;
					var error = res.error;
					var succ = res.succ;
					var data = res.skus;
					var msg = "";
					if (error > 0) {
						msg = "操作失败,总:" + total + "条,成功:" + succ + "条,失败:" + error + "条。";
					} else {
						msg = "操作成功,总:" + total + "条,成功:" + succ + "条。" 
					}
					createAlert("提示信息", msg, function() {
						parent.tempLocalData=[];
						parent.showDownLoad(data);
						close();
					});
				}
				
				// 本地数据sku
				function getLocalSkus() {
					var count = tempUserData.length;
					var checkData = [];
					for (var i = 0; i < count; i++) {
						checkData.push(tempUserData[i].sku);
					}
					
					return checkData.join(",");
				}
				
				// 本地数据channelCode
				function getLocalChannelCodes() {
					var count = tempUserData.length;
					var checkData = [];
					for (var i = 0; i < count; i++) {
						checkData.push(tempUserData[i].channelCode);
					}
					
					return checkData.join(",");
				}
				
				// 删除本地数据
				function localDelete(row) {
					var leng = row.length;
					if (type == 2) {
						for(var i = 0 ; i < leng; i++){
							var record = row[i];
		    	  			var sid = record.get("sid");
		    	  			for (var j = 0; j < tempUserData.length; j++) {
								var temp = tempUserData[j];
								if (temp.sid == sid) {
									tempUserData.splice(j,1);
									break;
								}
							}
			    	 	}
					} else {
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
					}
		    	 	setDefaultPage(0, limit, store);
	    	  		getPageList(0);
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
			
			// 设置每页数据
  			function getPageList(start) {
				var data = {};
				var count = tempUserData.length;
				data.total = count;
				var tempData = [];
				if (start + limit <= count) {
					for (var i = 0; i < limit; i++) {
						tempData.push(tempUserData[start + i]);
					}
				} else {
					for (var i = start; i < count; i++) {
						tempData.push(tempUserData[i]);
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

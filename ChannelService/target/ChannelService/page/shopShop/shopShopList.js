function changeShopStatus(val) {

	if ("1" == val) {
		return "<font color='green'>已激活</font>";
	} else if ("0" == val) {
		return "<font color='red'>未激活</font>";
	}else if ("2" == val) {
		return "<font color='red'>已移除</font>";
	}
}

Ext.onReady(function() {
			var shopShopForm;
			var shopShopGrid;
			ShopShop = function() {
				var shopShopStore;
				var shopShopProxy;
				var shopShopReader;
				var checkBoxSelect;
				var shopShopcolumnGrid;
				var tbar;
				var pageSize = 15;
				return {
					init : function() {
						//渠道选择（所属分公司）
						var typeOptionCombo = new Ext.form.ComboBox({
							id : 'shopShop.channelCode',
							store :  new Ext.data.Store({
								proxy : new Ext.data.HttpProxy({
									url : basePath + '/custom/channelInfo/channelList.spmvc',
									method : 'GET'
								}),
								reader : new Ext.data.JsonReader({
									fields : [ 'chanelCode', 'channelTitle' ]
								})
							}),
							xtype : 'combo',
							valueField : 'chanelCode',
							displayField : 'channelTitle',
							mode : 'remote',
							forceSelection : true,
							emptyText : '请选择渠道（门店上级）',
							editable : false,			
							triggerAction : 'all',
							fieldLabel : '门店上级',
							width : 150
						});
						
						//店铺状态
						var shopStatusSelect = new Ext.form.ComboBox({
							id : 'shopShop.shopStatus',
							store : new Ext.data.SimpleStore({
								data : [ [ '0', '未激活' ], [ '1', '已激活' ] ],
								fields : [ 'text', 'filed' ]
							}),
							xtype : 'combo',
							valueField : 'text',
							displayField : 'filed',
							mode : 'local',
							forceSelection : true,
							blankText : '请选择店铺状态',
							emptyText : '请选择店铺状态',
							name : 'shopShop.shopStatus',
							editable : false,
							triggerAction : 'all',
							fieldLabel : '店铺状态',
							width : 150
						});
						//店铺类型
						var shopTypeSelect = new Ext.form.ComboBox({
							id : 'shopShop.shopType',
							store : new Ext.data.SimpleStore({
								data : [ [ '0', '自营' ], [ '1', '加盟' ] ],
								fields : [ 'text', 'filed' ]
							}),
							xtype : 'combo',
							valueField : 'text',
							displayField : 'filed',
							mode : 'local',
							forceSelection : true,
							blankText : '请选择店铺类型',
							emptyText : '请选择店铺类型',
							name : 'shopShop.shopType',
							editable : false,
							triggerAction : 'all',
							fieldLabel : '店铺类型',
							width : 150
						});
						
						
						shopShopForm = new Ext.FormPanel({
							frame : true,
							bodyStyle : 'padding:5px 5px 0',
							autoHeight : true,
							autoScroll : true,
							autoWidth : true,
							url : "",
							labelAlign : 'right',
							layout : 'form',
							items : [ 
						            	{  
											layout : 'column',
											items : [{layout : 'form',
												labelWidth:80,
												columnWidth:0.33,
												items : [shopTypeSelect,shopStatusSelect,typeOptionCombo]
											       },{
														layout : 'form',
														labelWidth:80,
														columnWidth:0.3,
														items : [ {
															id : 'shopShop.shopCode',
															xtype : 'textfield',
															fieldLabel : '店铺编号',
															name : 'shopId', 
															width : '150'
														},{
															id : 'shopShop.marketTitle',
															xtype : 'textfield',
															fieldLabel : '市场名称',
															name : 'shopId', 
															width : '150'
														} ]
													},{layout : 'form',
														labelWidth:80,
														columnWidth:0.33,
														items : [{
															id : 'shopShop.shopTitle',
															xtype : 'textfield',
															fieldLabel : '店铺名称',
															name : '店铺名称',
															width : '150'
														},{
															id : 'shopShop.parentShopCode',
															xtype : 'textfield',
															fieldLabel : '父店铺编号',
															width : '150'
														}]
													}]}
							        ],
							buttons : [ {
								text : '查询',
								columnWidth : 0.1,
								handler : this.search
							}, {
								text : '重置',
								columnWidth : 0.1,
								handler : this.reset
							} ]
						});

						checkBoxSelect = new Ext.grid.CheckboxSelectionModel();

						shopShopcolumnGrid = new Ext.grid.ColumnModel([
								checkBoxSelect, {
									id : 'id',
									header : "id",
									align : "center",
									width : 80,
									hidden : true,
									dataIndex : 'id'
								}, {
									id : 'shopCode',
									align : "center",
									sortable : true,
									header : "店铺编号",
									width : 120,
									dataIndex : 'shopCode'
								}, {
									id : 'parentShopCode',
									align : "center",
									sortable : true,
									header : "父店铺编号",
									width : 120,
									dataIndex : 'parentShopCode'
								}, {
									id : 'channelTitle',
									header : "店面上级",
									align : "center",
									width : 120,
									dataIndex : 'channelTitle'
								} ,{
									id : 'shopTitle',
									header : "店铺名称",
									align : "center",
									width : 200,
									dataIndex : 'shopTitle'
								},
								{
									id : 'marketTitle',
									header : "市场名称",
									align : "center",
									width : 120,
									dataIndex : 'marketTitle'
								},
								{
									id : 'shopType',
									align : "center",
									sortable : true,
									header : "店铺类型",
									width : 80,
									renderer : function(e){
										var msg ="";
										if(e=="0"){
											msg="自营";											
										}
										if(e=="1"){
											msg="加盟";
										}
										return msg;
									},
									dataIndex : 'shopType'
								},{
									id : 'shopStatus',
									align : "center",
									sortable : true,
									header : "店铺状态",
									width : 80,
									renderer : changeShopStatus,
									dataIndex : 'shopStatus'
								},
								{
									id : 'shopAddress',
									align : "center",
									sortable : true,
									header : "店铺地址",
									width : 100,
									dataIndex : 'shopAddress'
								},
								{
									id : 'shopLinkman',
									align : "center",
									sortable : true,
									header : "联系人",
									width : 60,
									dataIndex : 'shopLinkman'
								},
								{
									id : 'shopTel',
									align : "center",
									sortable : true,
									header : "联系电话",
									width : 80,
									dataIndex : 'shopTel'
								},
								{
									id : 'backup',
									header : "备注信息",
									align : "center",
									align : "center",
									width : 100,
									dataIndex : 'backup'
								}

						]);

						// 与列对应的dataIndex
						couponrecord = Ext.data.Record.create([ {
							name : 'id'
						}, {
							name : 'shopCode'
						}, {
							name : 'parentShopCode'
						}, {
							name : 'shopTitle'
						}, {
							name : 'marketTitle'
						}, {
							name : 'shopStatus'
						}, {
							name : 'erpShopCode'
						}, {
							name : 'backup'
						}, {
							name : 'shopChannel'
						}, {
							name : 'marketTitle'
						}, {
							name : 'shopType'
						}, {
							name : 'shopAddress'
						}, {
							name : 'shopLinkman'
						},{
							name : 'shopTel'
						}, {
							name : 'channelTitle'
						}
						// {name : 'CREATE_USER'},
						// {name : 'MOD_TIME'},
						// {name : 'MOD_USER'},
						// {name : 'COUPON_TYPE'},
						// {name : 'REMARK'}
						]);

						shopShopProxy = new Ext.data.HttpProxy(
								{

									url : basePath
											+ "custom/channelShop/getExternalChannelShopList.spmvc",

									method : "post"
								});
						// Reader 读json中数据
						shopShopReader = new Ext.data.JsonReader({
							root : 'root',
							totalProperty : 'totalProperty'
						}, couponrecord);

						shopShopStore = new Ext.data.Store({
							autoLoad : {
								params : {
									start : 0,
									limit : pageSize
								}
							},
							proxy : shopShopProxy,
							reader : shopShopReader
						});
						
						 // 如果不是用ajax的表单封装提交,就要做如下操作.
				        //这里很关键，如果不加，翻页后搜索条件就变没了，这里的意思是每次数据载入前先把搜索表单值加上去，这样就做到了翻页保留搜索条件了   
						shopShopStore.on('beforeload', function(){
				         //   var keyword = getKeyword();
				            Ext.apply(this.baseParams, {shopChannel: 0});   
				        });	

						// 定义菜单栏
						tbar = [
								{
									id : 'message_grid_tBar@add',
									text : '添加店铺',
									tooltip : '添加店铺',
									iconCls : 'add',
									handler : function() {
										FormEditWin.showAddDirWin(
														"popWins",
														basePath
																+ "custom/shopShop/addShopShop.spmvc",
														"pop_message_winID",
														"新增店铺", 620, 450);
									}
								},
								{
									text : '更新/查看',
									tooltip : '更新/查看',
									iconCls : 'search',
									handler : function() {
										var selModel = shopShopGrid.getSelectionModel();
										if (selModel.hasSelection()) {
											var records = selModel.getSelections();
											if(records.length > 1) {
												alertMsg("错误", "只能选择一行!");
												return;
											} 
											var id = records[0].get("id");
											if (id != "") {
												
												FormEditWin.showAddDirWin("popWins",
														basePath+ "custom/shopShop/updateShopShop.spmvc?id="+id+"&type=init",
														"pop_message_winID",
														"更新/查看集团店铺", 620, 400);
											} // if (id != "") end
										} else {
											alertMsg("错误", "请选择一行记录查看!");
										}										
									}
								}, {
									id : 'message_grid_tBar@delete',
									text : '移除',
									tooltip : 'delete',
									iconCls : 'delete',
									handler : function() {
										var selModel = shopShopGrid.getSelectionModel();
										if (selModel.hasSelection()) {
											var records = selModel.getSelections();
											var ids = "";
										    var shopTitles="";
										    var shopCodes="";
											for ( var i = 0; i < records.length; i++) {
												var id = records[i].get("id");
												var shopCode=records[i].get("shopCode");
												var shopTitle = records[i].get("shopTitle");
												var error = records[i].get("shopStatus"); 
												if(error != "1"){
													alertMsg("结果", "只能移除已激活状态记录！");
													return;
												}
												if (id && id != ''&& id != null) {
													ids += "" + id + ",";
												}
												if (shopCode != ''&& shopCode != null) {
													shopCodes += "" + shopCode + ",";
												}
												
												if(shopTitle != "" && shopTitle!=null){
													shopTitles+=""+shopTitle+",";
												}
										    }
											if (id != "") {
												Ext.Msg.confirm("确认","确定要移除集团店铺:"+ shopTitles,
														function(btn) {
															if (btn == "yes"){
																Ext.Ajax.request({
																	waitMsg : '请稍等.....',
																	url : basePath+ 'custom/channelShop/activeChannelShop.spmvc',
																	method : 'post',
																	params : {ids : ids,shopCodes:shopCodes,shopStatus:0},
																	success : function(response) {
																		var respText = Ext.util.JSON.decode(response.responseText);
																		if (respText.isok) {
																			shopShopStore.reload();
																			alert("批量删除成功！");
																		} else {
																			shopShopStore.reload();
																			alert("移除失败！");
																		}
																	},
																	failure : function(response) {
																		
																		alert("失败");
																	}
																});
															}});
											}
						
										} 	 else {
											alertMsg("错误", "请选择需要移除的行!");
										}														
									
									}
								}, {

									text : '激活',
									tooltip : '',
									iconCls : 'refresh',
									handler : function() {
										// shopShopStore.reload();
									
										var selModel = shopShopGrid.getSelectionModel();
                                        if (selModel.hasSelection()) {
                                        	var records = selModel.getSelections();
											var ids = "";
											 var shopCodes="";
											for ( var i = 0; i < records.length; i++) {
												var id = records[i].get("id");
												var shopCode=records[i].get("shopCode");
												var error = records[i].get("shopStatus"); 
												if(error != "0"){
													alertMsg("结果", "只能选择未激活状态记录激活！");
													return;
												}
												if (id && id != ''
														&& id != null) {
													
													ids += "" + id + ",";
												}
												if (shopCode != ''&& shopCode != null) {
													shopCodes += "" + shopCode + ",";
												}
										    }
											
											if (id != "") {
												Ext.Ajax.request({
													waitMsg : '请稍等.....',
													url : basePath+ 'custom/channelShop/activeChannelShop.spmvc',
													method : 'post',
													params : {ids : ids,shopCodes:shopCodes,shopStatus:1},
													success : function(response) {
														var respText = Ext.util.JSON.decode(response.responseText);
														if (respText.isok) {
															shopShopStore.reload();
															alert("激活成功");
														} else {
															shopShopStore.reload();
															alert("激活失败");
														}

													},
													failure : function(response,options) {
														alert("失败");
													}
												});
											}

										} else {
                                            alertMsg("错误", "请选择需要激活的记录!");
										}
					
									}
								} ];

						shopShopGrid = new Ext.grid.GridPanel(
								{
								//	width : 1000,
									autoWidth : true,
									height : 500,
									// height : 400,
									title : '集团店铺管理',
									store : shopShopStore,
									id : 'addshopShopForm_grd_id',
									trackMouseOver : false,
									disableSelection : true,
									loadMask : true,
									frame : true,
									autoExpandColumn: "shopTitle", //自动伸展，占满剩余区域
									columnLines : true,
									tbar : tbar,
									// grid columns
									cm : shopShopcolumnGrid,
									sm : checkBoxSelect,
									// paging bar on the bottom
									bbar : new IssPagingToolbar(shopShopStore,
											pageSize),
									listeners : {
										'rowdblclick' : function(thisgrid,rowIndex, e) {
											var selectionModel = thisgrid.getSelectionModel();
											var record = selectionModel.getSelected();
											var id = record.data['id'];	
											FormEditWin.showAddDirWin("popWins",
													basePath+ "custom/shopShop/updateShopShop.spmvc?id="+id+"&type=init",
													"pop_message_winID",
													"更新/查看集团店铺",620, 400);
										}
									}
								});

					},
					show : function() {
						couponPanel = new Ext.Panel({
							renderTo : 'shopShop-grid',
							layout : 'column',
							items : [ shopShopForm, shopShopGrid ]
						}).show();
					},
					reset:function(){
						shopShopForm.form.reset();
					},
					refresh:function(){
						shopShopStore.reload();					
				},
					search : function() {
						
						var outStock = {};
						outStock["shopCode"] = Ext.getCmp("shopShop.shopCode").getValue();
						outStock["shopTitle"] = Ext.getCmp("shopShop.shopTitle").getValue();	
						outStock["shopStatus"] = Ext.getCmp("shopShop.shopStatus").getValue();	
						outStock["shopType"] = Ext.getCmp("shopShop.shopType").getValue();//店铺类型
						outStock["marketTitle"] = Ext.getCmp("shopShop.marketTitle").getValue();//市场名称	
						outStock["parentShopCode"] = Ext.getCmp("shopShop.parentShopCode").getValue();
						outStock["channelCode"] = Ext.getCmp("shopShop.channelCode").getValue();
						shopShopGrid.store.baseParams = outStock;
						shopShopGrid.store.load({
							params : {
								start : 0,
								limit : pageSize,
								shopChannel: 0  //集团店铺
							}
						});
					}

				}
			}();

			function runCouponList() {
				ShopShop.init();
				ShopShop.show();
				setResize();
			}
			runCouponList();

			function clickResetButton() {
				outChannelShopForm.form.reset();
			}
			function setResize() {
				var formHeight = shopShopForm.getHeight();
				var clientHeight = document.body.clientHeight;
				var clientWidth = document.body.clientWidth;
				shopShopGrid.setHeight(clientHeight-formHeight-50);
				shopShopGrid.setWidth(clientWidth-259);
			}
			window.onresize=function(){
				setResize();
			}
		});
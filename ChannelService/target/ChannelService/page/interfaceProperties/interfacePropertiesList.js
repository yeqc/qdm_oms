function changeShopStatus(val) {

	if ("1" == val) {
		return "<font color='green'>已激活</font>";
	} else if ("0" == val) {
		return "<font color='red'>未激活</font>";
	}

}
Ext.onReady(function() {
			var interfaceProForm;
			var interfaceProGrid;
			
			InterfaceProperty = function() { 
				var outChannelShopStore;
				var couponproxy;
				var couponreader;
				var checkBoxSelect;
				var couponcolumnGrid;
				var tbar;
				var pageSize = 15;
				return {
					init : function() {
						interfaceProForm = new Ext.FormPanel({
							labelAlign : 'top',
							frame : true,
							bodyStyle : 'padding:5px 5px 0',
							//autoHeight : true,
							autoScroll : true,
							autoWidth : true,
							url : "",
							labelAlign : 'right',
							layout : 'form',
							items : [
								{
									layout : 'column',
									//border : false,
								//	labelSeparator : '：',
									items : [ {
										columnWidth : .3,
										layout : 'form',
										border : false,
										items : [{
												layout : 'form',
												labelWidth:80,
												columnWidth:0.3,
												items : [ {
													id : 'proName',
													xtype : 'textfield',
													fieldLabel : '配置名称',
													name : 'proName', 
													width : '150'
												},{
													id : 'proValue',
													xtype : 'textfield',
													fieldLabel : '配置值',
													name : 'proValue', 
													width : '150'
												} ]
										}]
				                  },{
											
												layout : 'column',
												items : [ {
													layout : 'form',
													labelWidth : 80,
													columnWidth : 0.30,
													items : [ {
														id : 'interface.shopTitle',
														xtype : 'textfield',
														fieldLabel : '店铺名称',
														name : 'shopTitle', 
														width : '150'
													}, {
														id : 'interface.channelTitle',
														xtype : 'textfield',
														fieldLabel : '渠道名称',
														name : 'channelTitle', 
														width : '150'
													} ]
												} ]

								     }]
				                    
				                 }
				
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

						couponcolumnGrid = new Ext.grid.ColumnModel([
								checkBoxSelect, {
									id : 'id',
									header : "ID",
									align : "center",
									align : "center",
									width : 80,
									hidden : true,
									dataIndex : 'id'
								}, {
									id : 'proName',
									align : "center",
									sortable : true,
									header : "配置名称",
									width : 140,
									dataIndex : 'proName'
								}, {
									id : 'proValue',
									header : "配置值",
									align : "center",
									align : "center",
									width : 120,
									dataIndex : 'proValue'
								},{
									id : 'shopCode',
									header : "店铺代码",
									align : "center",
									align : "center",
									width : 120,
									dataIndex : 'shopCode'
								}, {
									id : 'channelCode',
									align : "center",
									sortable : true,
									header : "渠道code",
									width : 120,
									dataIndex : 'channelCode'
								},{
									id : 'shopTitle',
									align : "center",
									sortable : true,
									header : "店铺名称",
									width : 120,
									dataIndex : 'shopTitle'
								},{
									id : 'channelTitle',
									align : "center",
									sortable : true,
									header : "渠道名称",
									width : 120,
									dataIndex : 'channelTitle'
								}
								
						]);

						// 与列对应的dataIndex
						couponrecord = Ext.data.Record.create([ 
							{
								name : 'id'
							}, {
								name : 'proName'
							}, {
								name : 'proValue'
							}, {
								name : 'shopCode'
							}, {
								name : 'channelCode'
							}, {
								name : 'shopTitle'
							}, {
								name : 'channelTitle'
							}
					
						]);

						couponproxy = new Ext.data.HttpProxy(
								{

									url : basePath
											+ "/custom/interfaceProperties/getInterfacePropertiesList.spmvc",

									method : "post"
								});
						// Reader 读json中数据
						couponreader = new Ext.data.JsonReader({
							root : 'root',
							totalProperty : 'totalProperty'
						}, couponrecord);

						outChannelShopStore = new Ext.data.Store({
							autoLoad : {
								params : {
									start : 0,
									limit : pageSize
								}
							},
							proxy : couponproxy,
							reader : couponreader
						});
						
						 // 如果不是用ajax的表单封装提交,就要做如下操作.
				        //这里很关键，如果不加，翻页后搜索条件就变没了，这里的意思是每次数据载入前先把搜索表单值加上去，这样就做到了翻页保留搜索条件了   
						outChannelShopStore.on('beforeload', function(){
				         //   var keyword = getKeyword();
				            Ext.apply(this.baseParams);   
				        });	
						

						// 定义菜单栏
						tbar = [
								{
									id : 'message_grid_tBar@add',
									text : '添加配置信息',
									tooltip : '添加配置信息',
									iconCls : 'add',
									handler : function() {
										FormEditWin
												.showAddDirWin(
														"popWins",
														basePath+ "custom/interfaceProperties/toAddInterfaceProperties.spmvc",
														"pop_message_winID",
														"添加配置信息", 460, 300);
									}
								},
								{
									text : '更新/查看',
									tooltip : '更新/查看',
									iconCls : 'search',
									handler : function() {

										var selModel = interfaceProGrid.getSelectionModel();

										if (selModel.hasSelection()) {
											var records = selModel.getSelections();
											var ids = "";
											
											if(records.length > 1) {
												alertMsg("错误", "只能选择一行!");
												return;
											} 
											
											var id = records[0].get("id");
										
											if (id != "") {
												
												FormEditWin.showAddDirWin(
														"popWins",
														basePath
																+ "custom/interfaceProperties/updateInterfaceProperties.spmvc?id="+id+"&type=init",
														"pop_message_winID",
														"更新/查看配置信息", 460, 300);
								
											} // if (id != "") end
											
										} else {
											alertMsg("错误", "请选择一行记录!");
										}																				
									}
								}, {
									id : 'message_grid_tBar@delete',
									text : '移除',
									tooltip : 'delete',
									iconCls : 'delete',
									handler : function() {
						
										var selModel = interfaceProGrid.getSelectionModel();

										if (selModel.hasSelection()) {
											var records = selModel.getSelections();
											var ids = "";
										  //  var shopTitles="";
											for ( var i = 0; i < records.length; i++) {
												var id = records[i].get("id");
											//	var shopTitle = records[i].get("shopTitle");
												if (id && id != ''&& id != null) {
													ids += "" + id + ",";
												}
										    }
											
											if (id != "") {
												Ext.Msg.confirm("确认","确定要删除配置信息:",
														function(btn) {
															if (btn == "yes"){
																Ext.Ajax.request({
																	waitMsg : '请稍等.....',
																	url : basePath+ 'custom/interfaceProperties/deleteInterfaceProperties.spmvc',
																	method : 'post',
																	params : {ids : ids},
																	success : function(response) {
																		var respText = Ext.util.JSON.decode(response.responseText);
																		if (respText.isok) {
																			outChannelShopStore.reload();
																			alertMsg("验证","移除成功！");
																		} else {
																			outChannelShopStore.reload();
																			alert(respText.msg);
																		}

																	},
																	failure : function(response,options) {
																		alertMsg("验证","失败！");
																	//	alert("失败");
																	}
																});
																
																
															}});
											}
						
										} 	 else {
											alertMsg("错误", "请选择需要移除的行!");
										}														
									
									}
								}/*, {

									text : '激活',
									tooltip : '',
									iconCls : 'refresh',
									handler : function() {
										 outChannelShopStore.reload();									
										var selModel = interfaceProGrid.getSelectionModel();
                                        if (selModel.hasSelection()) {
                                        	var records = selModel.getSelections();
											var ids = "";
										
											for ( var i = 0; i < records.length; i++) {
												var id = records[i].get("id");
												if (id && id != ''&& id != null) {
													ids += "" + id + ",";
												}
										    }
																	
											if (id != "") {
												
												Ext.Ajax.request({
													waitMsg : '请稍等.....',
													url : basePath+ 'custom/channelShop/activeChannelShop.spmvc',
													method : 'post',
													params : {ids : ids},
													success : function(response, action) {
														var respText = Ext.util.JSON.decode(response.responseText);
														if (respText.isok) {
															outChannelShopStore.reload();
															alertMsg("验证","激活成功！");
															alert("激活成功");														} else {
															
															outChannelShopStore.reload();
															alertMsg("验证","激活失败！");
															alert("激活失败");														}
													},
													failure : function(response,options) {
														var respText = Ext.util.JSON.decode(response.responseText);
														alert(respText.msg);
													}
												});
											}

										} else {
                                            alertMsg("错误", "请选择一行记录!");
										}
					
									}
								} */];

						interfaceProGrid = new Ext.grid.GridPanel(
								{
					
									//width : 1000,
									height:500,
									//autoHeight : true,
									// height : 400,
									title : '配置信息列表',
									store : outChannelShopStore,
									id : 'addInterfaceProForm_grid_id',
									trackMouseOver : false,
									disableSelection : true,
									autoWidth : true,
									loadMask : true,
									frame : true,
									autoExpandColumn: "proValue", //自动伸展，占满剩余区域
									columnLines : true,
									tbar : tbar,
									// grid columns
									cm : couponcolumnGrid,
									sm : checkBoxSelect,
									// paging bar on the bottom
									bbar : new IssPagingToolbar(outChannelShopStore,
											pageSize),
									listeners : {
										'rowdblclick' : function(thisgrid,rowIndex, e) {
											var selectionModel = thisgrid
													.getSelectionModel();
											var record = selectionModel
													.getSelected();
											var id = record.data['id'];
											
											FormEditWin.showAddDirWin(
													"popWins",
													basePath+ "custom/interfaceProperties/updateInterfaceProperties.spmvc?id="+id+"&type=init",
													"pop_message_winID",
													"更新/查看配置信息", 460, 300);
										}
									}
								});

					},
					
					refresh:function(){
						outChannelShopStore.reload();					
					},
					show : function() {

						couponPanel = new Ext.Panel({
							renderTo : 'InterfacePropertiesListGrid',

							layout : 'column',
							items : [ interfaceProForm, interfaceProGrid ]
						}).show();
					},
					reset:function(){
						interfaceProForm.form.reset();
					},
					
					search : function() {
						var outStock = {};

					//	outStock["channelCode"] = Ext.getCmp("OutChannelShop.channelCode").getValue();
						outStock["proValue"] = Ext.getCmp("proValue").getValue();
						outStock["proName"] = Ext.getCmp("proName").getValue();
						outStock["shopTitle"] = Ext.getCmp("interface.shopTitle").getValue();
						outStock["channelTitle"] = Ext.getCmp("interface.channelTitle").getValue();
						
						interfaceProGrid.store.baseParams = outStock;
						interfaceProGrid.store.load({
							params : {
								start : 0,
								limit : pageSize,
							//	shopChannel: 1  //渠道店铺
							}
						});
					}

				}
			}();

			function runCouponList() {
				InterfaceProperty.init();
				InterfaceProperty.show();
			}
			runCouponList();
			function setResize() {
				var formHeight = interfaceProForm.getHeight();
				var clientHeight = document.body.clientHeight;
				var clientWidth = document.body.clientWidth;
				interfaceProGrid.setHeight(clientHeight-formHeight-50);
				interfaceProGrid.setWidth(clientWidth-259);
			}
			window.onresize=function(){
				setResize();
			}
});
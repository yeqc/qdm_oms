function changeChannelStatus(val) {

	if ("1" == val) {
		return "<font color='green'>已激活</font>";
	} else if ("0" == val) {
		return "<font color='red'>未激活</font>";
	}else if ("2" == val) {
		return "<font color='red'>已移除</font>";
	}
}
Ext
		.onReady(function() {
			var channelInfoForm;
			var channelInfoGrid;
			ChannelBaseInfo = function() { 

				var channelInfoStore;
				var channelInfoProxy;
				var channelInfoReader;
				var checkBoxSelect;
				var channelInfoClumnGrid;
				var tbar;
				var pageSize = 15;
				return {
					init : function() {

						var typeOptionCombo = new Ext.form.ComboBox({
							id : 'channelInfo.channelType',
							store : new Ext.data.SimpleStore({
								data : [ [ '1', '线上直营渠道' ], [ '2', '线上加盟渠道' ],['3','线下直营渠道'],['4','线下加盟渠道'] ],
								fields : [ 'text', 'filed' ]
							}),
							xtype : 'combo',
							valueField : 'text',
							displayField : 'filed',
							mode : 'local',
							forceSelection : true,
							blankText : '请选择渠道类型',
							emptyText : '请选择渠道类型',
							name : 'channelInfo.channelType',
							editable : false,
							hiddenName : 'channelInfo.channelType',
							triggerAction : 'all',
							fieldLabel : '渠道类型',
							width : 150
						});

						channelInfoForm = new Ext.FormPanel({
							labelAlign : 'top',
							frame : true,
							bodyStyle : 'padding:5px 5px 0',
							autoWidth:true,
							url : "",
							labelAlign : 'right',
							layout : 'form',
							items : [ {
								layout : 'column',
								items : [ {
									layout : 'form',
									labelWidth : 80,
									columnWidth : 0.3,
									items : [ {
										id : 'channelInfo.channelTitle',
										xtype : 'textfield',
										fieldLabel : '渠道名称',
										name : 'channelTitle', 
										width : '150'
									} ]
								},{
									layout : 'form',
									labelWidth : 80,
									columnWidth : 0.3,
									items : [typeOptionCombo]}]
							} ],
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

						channelInfoClumnGrid = new Ext.grid.ColumnModel([
								checkBoxSelect, {
									id : 'id',
									header : "id",
									align : "center",
									width : 80,
									hidden : true,
									dataIndex : 'id'
								}, {
									id : 'channelCode',
									align : "center",
									sortable : true,
									header : "渠道编号",
									width : 140,
									dataIndex : 'chanelCode'
								}, {
									id : 'channelTitle',
									header : "渠道名称",
									align : "center",
									width : 200,
									dataIndex : 'channelTitle'
								},

								{
									id : 'channelStatus',
									header : "渠道状态",
									align : "center",
									width : 160,
									renderer : changeChannelStatus,
									dataIndex : 'channelStatus'
								},
								{
									id : 'channelType',
									header : "渠道类型",
									align : "center",
									width : 160,
									dataIndex : 'channelType',
									renderer:function(val){
										switch (val){
										case 1: return "线上直营渠道";
										case 2: return "线上加盟渠道";
										case 3: return "线下直营渠道";
										case 4: return "线下加盟渠道";
										}
									}
								},
								{
									id : 'backup',
									header : "备注信息",
									align : "center",
									width : 200,
									dataIndex : 'backup'
								}

						]);

						// 与列对应的dataIndex
						couponrecord = Ext.data.Record.create([ {
							name : 'id'
						}, {
							name : 'chanelCode'
						}, {
							name : 'channelTitle'
						}, {
							name : 'channelStatus'
						}, {
							name : 'channelType'
						}, {
							name : 'backup'
						}
						]);

						channelInfoProxy = new Ext.data.HttpProxy(
								{

									url : basePath
											+ "/custom/channelInfo/getChannelInfoList.spmvc",

									method : "post"
								});
						// Reader 读json中数据
						channelInfoReader = new Ext.data.JsonReader({
							root : 'root',
							totalProperty : 'totalProperty'
						}, couponrecord);

						channelInfoStore = new Ext.data.Store({
							autoLoad : {
								params : {
									start : 0,
									limit : pageSize
								}
							},
							proxy : channelInfoProxy,
							reader : channelInfoReader
						});
						
						 // 如果不是用ajax的表单封装提交,就要做如下操作.
				        //这里很关键，如果不加，翻页后搜索条件就变没了，这里的意思是每次数据载入前先把搜索表单值加上去，这样就做到了翻页保留搜索条件了   
						channelInfoStore.on('beforeload', function(){
				         //   var keyword = getKeyword();
				       //     Ext.apply(this.baseParams, {shopChannel: 1});   
				        });	
						

						// 定义菜单栏
						tbar = [
								{
									id : 'message_grid_tBar@add',
									text : '添加渠道',
									tooltip : '添加渠道',
									iconCls : 'add',
									handler : function() {
										FormEditWin
												.showAddDirWin(
														"popWins",
														basePath
																+ "/custom/channelInfo/toAddChannelInfo.spmvc",
														"pop_message_winID",
														"新增渠道", 450, 350);
									}
								},
								{
									text : '更新/查看',
									tooltip : '更新/查看',
									iconCls : 'search',
									handler : function() {

										var selModel = channelInfoGrid.getSelectionModel();

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
														basePath+ "/custom/channelInfo/viewChannelInfo.spmvc?id="+id,
														"pop_message_winID",
														"更新/查看渠道基本信息", 450, 350);
								
											}
											
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
						
										var selModel = channelInfoGrid.getSelectionModel();

										if (selModel.hasSelection()) {
											var records = selModel.getSelections();
											var ids = "";
										    var channelTitles="";
											for ( var i = 0; i < records.length; i++) {
												var id = records[i].get("id");
												var error = records[i].get("channelStatus"); 
												if(error != "1"){
													alertMsg("结果", "只能移除已激活记录！");
													return;
												}
												var channelTitle = records[i].get("channelTitle");
												if (id && id != ''&& id != null) {
													ids += "" + id + ",";
												}
												if(channelTitle != "" && channelTitle!=null){
													channelTitles+=""+channelTitle+",";
												}
										    }
											
											if (id != "") {
												Ext.Msg.confirm("确认","确定要删除渠道:"+ channelTitles,
														function(btn) {
															if (btn == "yes"){
																Ext.Ajax.request({
																	waitMsg : '请稍等.....',
																	url : basePath+ '/custom/channelInfo/activeChannelInfo.spmvc',
																	method : 'post',
																	params : {ids : ids,channelStatus:0},
																	success : function(response) {
																		var respText = Ext.util.JSON.decode(response.responseText);
																		if (respText.isok) {
																			channelInfoStore.reload();
																		} else {
																			channelInfoStore.reload();
																			alert(respText.msg);
																		}

																	},
																	failure : function(response,options) {
																		alertMsg("验证", "失败！");
																		
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
									
										var selModel = channelInfoGrid.getSelectionModel();
                                        if (selModel.hasSelection()) {
                                        	var records = selModel.getSelections();
											var ids = "";
										
											for ( var i = 0; i < records.length; i++) {
												var id = records[i].get("id");
												var error = records[i].get("channelStatus"); 
												if(error != "0"){
													alertMsg("结果", "只能选择未激活状态记录激活！");
													return;
												}
												if (id && id != ''&& id != null) {
													ids += "" + id + ",";
												}
										    }
																	
											if (id != "") {
												
												Ext.Ajax.request({
													waitMsg : '请稍等.....',
													url : basePath+ '/custom/channelInfo/activeChannelInfo.spmvc',
													method : 'post',
													params : {ids : ids,channelStatus:1},
													success : function(response, action) {
														var respText = Ext.util.JSON.decode(response.responseText);
														if (respText.isok) {
															channelInfoStore.reload();
															//alert("激活成功");
															alertMsg("验证", "激活成功!");
														} else {
															
															channelInfoStore.reload();
															
															//alert("激活失败");
															alertMsg("验证", "激活失败!");
														}
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
								} ];

						channelInfoGrid = new Ext.grid.GridPanel(
								{
									//width : 1000,
//									autoWidth : true,
//									height : 500,
									title : '渠道基本信息管理',
									store : channelInfoStore,
									id : 'addchannelInfoForm_grid_id',
									trackMouseOver : false,
									disableSelection : true,
									loadMask : true,
									frame : true,
									autoExpandColumn: "backup", //自动伸展，占满剩余区域
									columnLines : true,
									tbar : tbar,
									// grid columns
									cm : channelInfoClumnGrid,
									sm : checkBoxSelect,
									// paging bar on the bottom
									bbar : new IssPagingToolbar(channelInfoStore,
											pageSize),
									listeners : {
										'rowdblclick' : function(thisgrid,rowIndex, e) {
									var selectionModel = thisgrid.getSelectionModel();
									var record = selectionModel.getSelected();
									var id = record.data['id'];

									FormEditWin.showAddDirWin(
											"popWins",
											basePath+ "/custom/channelInfo/viewChannelInfo.spmvc?id="+id,
											"pop_message_winID",
											"更新/查看渠道基本信息",  450, 400);

										}
									}
								});

					},
					
					refresh:function(){
						    channelInfoStore.reload();					
					},
					show : function() {

						couponPanel = new Ext.Panel({
							renderTo : 'channelInfo-grid',
							layout : 'column',
							items : [ channelInfoForm, channelInfoGrid ]
						}).show();
					},
					reset:function(){
						channelInfoForm.form.reset();
					},
					
					search : function() {
						var outStock = {};
						outStock["channelTitle"] = Ext.getCmp("channelInfo.channelTitle").getValue();
						outStock["channelType"] = Ext.getCmp("channelInfo.channelType").getValue();
						channelInfoGrid.store.baseParams = outStock;
						channelInfoGrid.store.load({
							params : {
								start : 0,
								limit : pageSize,
							}
						});
					}

				}
			}();

			function runCouponList() {
				ChannelBaseInfo.init();
				ChannelBaseInfo.show();
				setResize();
			}
			runCouponList();
			function setResize() {
				var formHeight = channelInfoForm.getHeight();
				var clientHeight = document.body.clientHeight;
				var clientWidth = document.body.clientWidth;
				channelInfoGrid.setHeight(clientHeight-formHeight-50);
				channelInfoGrid.setWidth(clientWidth-259);
			}
			window.onresize=function(){
				setResize();
			}
			
		});
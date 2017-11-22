function changeChannelStatus(val) {
	if ("0" == val) {
		return "国家";
	} else if ("1" == val) {
		return "省";
	} else if ("2" == val) {
		return "市";
	} else if ("3" == val) {
		return "区";
	} else if ("4" == val) {
		return "乡";
	}
}

Ext
	.onReady(function() {
		var channelAreaForm;
		var channelAreaGrid;
//			var channelSelect;
			var updateId="";
			Ext.QuickTips.init();
			Ext.form.Field.prototype.msgTarget = 'side';
			updatechannelArea = function() {
				var channelAreaStore;
				var channelAreaProxy;
				var channelAreaReader;
//				var channelAreaForm; 
				var checkBoxSelect;
				var pageSize = 15;
				return {
					init : function() {
						
						//********************start***********************
						//区域类型
						var areaTypeSelect = new Ext.form.ComboBox({
							id : 'newRegionType',
							store : new Ext.data.SimpleStore({
								data : [ [ '0', '国家' ], [ '1', '省' ],[ '2', '市' ],[ '3', '区' ] ],
								fields : [ 'text', 'filed' ]
							}),
							xtype : 'combo',
							valueField : 'text',
							displayField : 'filed',
							mode : 'local',
							//allowBlank : false,
							forceSelection : true,
							emptyText : '请选择区域类型',
							name : 'newRegionType',
							triggerAction : 'all',
							fieldLabel : '区域类型',
							width : 105
						});
						
						checkBoxSelect = new Ext.grid.CheckboxSelectionModel({
							singleSelect: true  
						});
						
						channelAreaClumnGrid = new Ext.grid.ColumnModel([
							checkBoxSelect, {
								id : 'regionId',
								header : "OS地区编码",
								align : "center",
								align : "center",
								width : 80,
								hidden : true,
								dataIndex : 'regionId'
							}, {
								id : 'regionId',
								align : "center",
								sortable : true,
								header : "OS地区编码",
								width : 180,
								dataIndex : 'regionId'
							}, {
								id : 'parentName',
								align : "center",
								sortable : true,
								header : "OS父地区名称",
								width : 180,
								dataIndex : 'parentName'
							}, {
								id : 'regionName',
								header : "OS地区名称",
								align : "center",
								align : "center",
								width : 180,
								dataIndex : 'regionName'
							} , {
								id : 'regionType',
								header : "地区类型",
								align : "center",
								align : "center",
								width : 80,
								renderer : changeChannelStatus,
								dataIndex : 'regionType'
							} 
						]);

						// 与列对应的dataIndex  region_id, parent_id, region_name, region_type
						couponrecord = Ext.data.Record.create([ 
							{ name : 'regionId' },
							{ name : 'parentName' },
							{ name : 'regionName' },
							{ name : 'regionType' }
						]);
						
						channelAreaProxy = new Ext.data.HttpProxy( {
							url : basePath+ "/custom/channelArea/getSystemRegionAreaList.spmvc",
							method : "post"
						});
						// Reader 读json中数据
						channelAreaReader = new Ext.data.JsonReader({
							root : 'root',
							totalProperty : 'totalProperty'
						}, couponrecord);
						
						channelAreaStore = new Ext.data.Store({
//							autoLoad : {
//								params : {
//									start : 0,
//									limit : pageSize
//								}
//							},
							proxy : channelAreaProxy,
							reader : channelAreaReader
						});       									  								
						//********************ended***********************
						
						channelAreaGrid = new Ext.grid.GridPanel(
								{
									width : 750,
									//autoWidth : true,
									height : 350,
									title : '渠道新地区数据',
									store : channelAreaStore,
									id : 'addchannelAreaForm_grid_id',
									trackMouseOver : false,
									disableSelection : true,
									loadMask : true,
									frame : true,
									autoExpandColumn: "regionType", //自动伸展，占满剩余区域
									columnLines : true,
									//tbar : tbar,
									// grid columns
									cm : channelAreaClumnGrid,
									sm : checkBoxSelect,
									// paging bar on the bottom
									bbar : new IssPagingToolbar(channelAreaStore, pageSize)
							});
						
						
//						channelSelect= new Ext.form.ComboBox({
//							id : 'channelCode',
//							store :  new Ext.data.Store({
//								proxy : new Ext.data.HttpProxy({
//									url : basePath + '/custom/channelInfo/channelList.spmvc',
//									method : 'GET'
//								}),
//								reader : new Ext.data.JsonReader({
//									fields : [ 'chanelCode', 'channelTitle' ]
//								})
//							}),
//							xtype : 'combo',
//							valueField : 'chanelCode',
//							displayField : 'channelTitle',
//							mode : 'remote',
//							forceSelection : true,
//							emptyText : '请选择渠道',
//							editable : false,
//							allowBlank : false,
//							name : 'chanelCode',
//							triggerAction : 'all',
//							fieldLabel : '渠道',
//							width : 180
//						});
						
						//区域类型
//						var areaTypeSelect = new Ext.form.ComboBox({
//							id : 'areaType',
//							store : new Ext.data.SimpleStore({
//								data : [ [ '0', '国家' ], [ '1', '省' ],[ '2', '市' ],[ '3', '区' ] ],
//								fields : [ 'text', 'filed' ]
//							}),
//							xtype : 'combo',
//							valueField : 'text',
//							displayField : 'filed',
//							mode : 'local',
//							allowBlank : false,
//							forceSelection : true,
//							emptyText : '请选择区域类型',
//							name : 'areaType',
//							triggerAction : 'all',
//							fieldLabel : '区域类型',
//							width : 180
//						});

						channelAreaForm = new Ext.FormPanel( {
							frame : true,
							bodyStyle : 'padding:10px 5px 5px 5px',
							height : 450,
							width : 400,
							labelAlign : 'right',
							buttonAlign : 'center',
							layout : 'form',
							
							items : [{
								layout : 'column',
								items : [{
									layout : 'form',
									labelWidth : 80,
									columnWidth : 0.25,
									items : [ {
										xtype : 'textfield',
										fieldLabel : '匹配类型',
										id:'matchType',
										width : '120',
									},{
										xtype : 'textfield',
										fieldLabel : '渠道父名称',
										id:'chParentName',
										width : '120'
										
									},{
										xtype : 'textfield',
										fieldLabel : 'OS地区父名称',
										id:'osParentName',
										width : '120'
										
									}]		
								},{
									layout : 'form',
									labelWidth : 80,
									columnWidth : 0.25,
									items : [ {
										xtype : 'textfield',
										fieldLabel : '渠道地区编码',
										id:'areaId',
										width : '120'
										
									},{
										xtype : 'textfield',
										fieldLabel : '渠道地区名称',
										id:'areaName',
										width : '120'	
									}]		
								},{
									layout : 'form',
									labelWidth : 80,
									columnWidth : 0.25,
									items : [ {
										xtype : 'textfield',
										fieldLabel : 'OS地区编码',
										id:'osRegionId',
										width : '120'
									},{
										xtype : 'textfield',
										fieldLabel : 'OS地区名称',
										id:'osRegionName',
										width : '120'
									}]		
								},{
									layout : 'form',
									labelWidth : 80,
									columnWidth : 0.25,
									items : [ {
										xtype : 'textfield',
										fieldLabel : '渠道名称',
										id:'channelCode',
										width : '100'
									},{
										xtype : 'textfield',
										fieldLabel : '渠道地区等级',
										id:'areaType',
										width : '120',
										renderer : changeChannelStatus
									},areaTypeSelect
								    ]		
								}]
								   
							}, {
								layout : 'column',
								items : [ channelAreaGrid ]
							}],
							
							buttons : [
								{
									text : '查询',
									columnWidth : 0.1,
									handler : this.search
								},       
								{
									text : '保存/更新',
									handler : function() {
										// 获取映射的名称
										var qty = channelAreaForm.getForm().findField('osRegionName').getValue();
										var record = checkBoxSelect.getSelected();
										var osRegionId = record.data['regionId'];
										if (!channelAreaForm.getForm().isValid()) {
											alertMsg("验证","请检查数据是否校验！");
											return;
										}
										Ext.Msg.confirm("确认","是否添加/修改地区映射:"+ qty,
											function(btn) {
												if (btn == "yes") {// 确认
													Ext.Ajax.request( {
														waitMsg : '请稍等.....',
														url : basePath + 'custom/channelArea/updateChannelArea.spmvc',
//														url : basePath + 'custom/channelArea/updateChannelAreaMatch.spmvc',
														method : 'post',
														params : {
															id:updateId, //用于更新主键把查看主键带回去
															osRegionId: osRegionId
//															osRegionName: Ext.getCmp("osRegionName").getValue(),
//															channelCode: Ext.getCmp('channelCode').getValue()
														},
														success : function(response) {
															var respText = Ext.util.JSON.decode(response.responseText);
															if(respText.isok){
															parent.ChannelArea.refresh();
																parent.FormEditWin.close();
															}else{
																alert(respText.message);
															}
														},
														failure : function(response) {
															alert("失败");
														}
													});
												}
											}
										);
									}
							
								} , {
									text : '关闭',
									handler : function() {
										parent.FormEditWin.close();
									}
								} ]
						
						});
					},
					show : function() {
						addoutStockPanel = new Ext.Panel({
							renderTo : 'updateChannelArea-grid',
							autoHeight : true,
							autoWidth : true,
							layout : 'fit',
							items : [ channelAreaForm ]
						}).show();
					},
					search : function() {
						var outStock = {};
						outStock["regionName"] = Ext.getCmp("osRegionName").getValue();
						outStock["regionType"] = Ext.getCmp("newRegionType").getValue();
						outStock["parentName"] = Ext.getCmp("osParentName").getValue();
						channelAreaGrid.store.baseParams = outStock;
						channelAreaGrid.store.load({
							params : {
								start : 0,
								limit : pageSize,
							}
						});
					}
				}
			}();

			function runupdatechannelArea() {
				updatechannelArea.init();
				updateId = $('#hId').val();//将修改记录的ID带回后台用于主键修改信息
				var val = $('#hAreaType').val();
				var areaType = "";
				if ("0" == val) {
					areaType = "国家";
				} else if ("1" == val) {
					areaType = "省";
				} else if ("2" == val) {
					areaType = "市";
				} else if ("3" == val) {
					areaType = "区";
				} else if ("4" == val) {
					areaType = "乡";
				}
				var val2 = $('#hMatchType').val();
				var matchType = "";
				if ("0" == val2) {
					matchType = "匹配";
				} else if ("1" == val2) {
					matchType = "部分匹配";
				} else if ("2" == val2) {
					matchType = "不匹配";
				}
				
				if("" != $('#hId').val()){
					Ext.getCmp('matchType').setValue(matchType);
					Ext.getCmp('chParentName').setValue($('#hChParentName').val());
					Ext.getCmp('areaId').setValue($('#hAreaId').val());
					Ext.getCmp('areaType').setValue(areaType); 
					Ext.getCmp('areaName').setValue($('#hAreaName').val()); 
					Ext.getCmp('osRegionId').setValue($('#hOsRegionId').val()); 
					Ext.getCmp('osRegionName').setValue($('#hOsRegionName').val());
					Ext.getCmp('channelCode').setValue($('#hchannelCode').val());
				}
				updatechannelArea.show();
			}
			runupdatechannelArea();
		});
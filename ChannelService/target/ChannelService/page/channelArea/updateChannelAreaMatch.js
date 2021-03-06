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
		var channelAreaMatchForm;
		var channelAreaMatchGrid;
			var channelSelect;
			var updateId="";
			Ext.QuickTips.init();
			Ext.form.Field.prototype.msgTarget = 'side';
			updatechannelArea = function() {
				var channelAreaStore;
				var channelAreaProxy;
				var channelAreaReader;
				var channelAreaForm; 
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
								header : "新地区编码",
								align : "center",
								align : "center",
								width : 80,
								hidden : true,
								dataIndex : 'regionId'
							}, {
								id : 'regionId',
								align : "center",
								sortable : true,
								header : "新地区编码",
								width : 180,
								dataIndex : 'regionId'
							}
//							,{
//								id : 'parentId',
//								align : "center",
//								sortable : true,
//								header : "地区父编码",
//								width : 120,
//								dataIndex : 'parentId'
//							}
							,{
								id : 'parentName',
								align : "center",
								sortable : true,
								header : "父地区名称",
								width : 180,
								dataIndex : 'parentName'
							}, {
								id : 'regionName',
								header : "新地区名称",
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
//							{ name : 'parentId' },
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
						
						channelAreaMatchGrid = new Ext.grid.GridPanel(
								{
									width : 750,
									//autoWidth : true,
									height : 375,
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
										width : '120'
										
									},{
										xtype : 'textfield',
										fieldLabel : '旧地区父名称',
										id:'parentName',
										width : '120'
										
									},{
										xtype : 'textfield',
										fieldLabel : '新地区父名称',
										id:'newParentName',
										width : '120'
										
									}]		
								},{
									layout : 'form',
									labelWidth : 80,
									columnWidth : 0.25,
									items : [ {
										xtype : 'textfield',
										fieldLabel : '旧地区编码',
										id:'oldRegionId',
										width : '120'
										
									},{
										xtype : 'textfield',
										fieldLabel : '旧地区名称',
										id:'oldRegionName',
										width : '120'	
									}]		
								},{
									layout : 'form',
									labelWidth : 80,
									columnWidth : 0.25,
									items : [ {
										xtype : 'textfield',
										fieldLabel : '新地区编码',
										id:'newAreaId',
										width : '120'
									},{
										xtype : 'textfield',
										fieldLabel : '新地区名称',
										id:'newAreaName',
										width : '120'
									}]		
								},{
									layout : 'form',
									labelWidth : 80,
									columnWidth : 0.25,
									items : [ {
										xtype : 'textfield',
										fieldLabel : '旧地区等级',
										id:'oldRegionType',
										width : '120'
									},areaTypeSelect
//									{
//										xtype : 'textfield',
//										fieldLabel : '渠道新地区等级',
//										id:'newRegionType',
//										width : '100'
//									}
								    ]		
								}]
								   
							}, {
								layout : 'column',
								items : [ channelAreaMatchGrid ]
							}],
							
							buttons : [
							    {
									text : '查询',
									columnWidth : 0.1,
									handler : this.search
								},{
									text : '保存/更新',
									handler : function() {
										// 获取映射的名称
										var qty = channelAreaForm.getForm().findField('newAreaName').getValue();
										var record = checkBoxSelect.getSelected();
										var newAreaId = record.data['regionId'];
										if (!channelAreaForm.getForm().isValid()) {
											alertMsg("验证","请检查数据是否校验！");
											return;
										}
										Ext.Msg.confirm("确认","是否修改新地区映射:"+ qty,
											function(btn) {
												if (btn == "yes") {// 确认
													Ext.Ajax.request( {
														waitMsg : '请稍等.....',
														url : basePath + 'custom/channelArea/updateChannelAreaMatch.spmvc',
														method : 'post',
														params : {
															id:updateId, //用于更新主键把查看主键带回去
															newAreaId:newAreaId, //新渠道编码
//															newAreaName: Ext.getCmp("newAreaName").getValue(), //新渠道名称
//															oldRegionId: id
//															areaType: Ext.getCmp("areaType").getValue(),//渠道类型
//															osRegionId: Ext.getCmp("osRegionId").getValue(),
//															osRegionName: Ext.getCmp("osRegionName").getValue(),
//															channelCode: Ext.getCmp('channelCode').getValue(),
//															pid: Ext.getCmp('pid').getValue()
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
							renderTo : 'updateChannelAreaMatch-grid',
							autoHeight : true,
							autoWidth : true,
							layout : 'fit',
							items : [ channelAreaForm]
						}).show();
					},
					search : function() {
						var outStock = {};
						outStock["regionId"] = Ext.getCmp("newAreaId").getValue();
//						outStock["oldRegionId"] = Ext.getCmp("oldRegionId").getValue();
//						outStock["newAreaName"] = Ext.getCmp("newAreaName").getValue();
						outStock["regionName"] = Ext.getCmp("newAreaName").getValue();
//						outStock["matchType"] = Ext.getCmp("matchType").getValue();
						outStock["regionType"] = Ext.getCmp("newRegionType").getValue();
						outStock["parentName"] = Ext.getCmp("newParentName").getValue();
						
						channelAreaMatchGrid.store.baseParams = outStock;
						channelAreaMatchGrid.store.load({
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
				if("" != $('#hId').val()){
					Ext.getCmp('oldRegionId').setValue($('#hOldRegionId').val()); 
					Ext.getCmp('oldRegionName').setValue($('#hOldRegionName').val());
					Ext.getCmp('oldRegionType').setValue($('#hOldRegionType').val()); 
					Ext.getCmp('newAreaId').setValue($('#hNewAreaId').val());
					Ext.getCmp('newAreaName').setValue($('#hNewAreaName').val()); 
					Ext.getCmp('matchType').setValue($('#hMatchType').val());
//					Ext.getCmp('pareaCode').setValue($('#hPareaCode').val());
					Ext.getCmp('parentName').setValue($('#hParentName').val());
//					channelSelect.store.on('load', function(store, record, opts) {
//						Ext.getCmp('channelCode').setValue($('#hchannelCode').val());
//					}
//					);
//					channelSelect.store.load();
				}
				updatechannelArea.show();
			}
			runupdatechannelArea();
		});
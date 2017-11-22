function changeChannelStatus(val) {
	if ("0" == val) {
		return "国家";
	} else if ("1" == val) {
		return "省";
	} else if ("2" == val) {
		return "市";
	} else if ("3" == val) {
		return "区";
	}
}

function changeMatchType(val) {
	if ("0" == val) {
		return "匹配";
	} else if ("1" == val) {
		return "部分匹配";
	} else if ("2" == val) {
		return "不匹配";
	}
}

Ext
	.onReady(function() {
			var channelAreaMatchForm;
			var channelAreaMatchGrid;
			ChannelArea = function() {
				var channelAreaStore;
				var channelAreaProxy;
				var channelAreaReader;
				var checkBoxSelect;
				var channelAreaClumnGrid;
				var tbar;
				var pageSize = 15;
				return {
					init : function() {
						//区域类型
						var areaTypeSelect = new Ext.form.ComboBox({
							id : 'channelAreaMatch.oldRegionType',
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
							name : 'oldRegionType',
							triggerAction : 'all',
							fieldLabel : '区域类型',
							width : 120
						});
						//映射状态
						var areaStatusSelect = new Ext.form.ComboBox({
							id : 'matchType',
							store : new Ext.data.SimpleStore({
								data : [ [ '0', '匹配' ], [ '1', '部分匹配' ],[ '2', '不匹配' ],[ '', '全部' ] ],
								fields : [ 'text', 'filed' ]
							}),
							xtype : 'combo',
							valueField : 'text',
							displayField : 'filed',
							mode : 'local',
							forceSelection : true,
							emptyText : '请选择映射状态',
							name : 'matchType',
							triggerAction : 'all',
							fieldLabel : '映射状态',
							width : 120
						});

						channelAreaMatchForm = new Ext.FormPanel({
							frame : true,
							autoWidth : true,
							url : "",
							labelAlign : 'right',
							layout : 'form',
							items : [ {
								layout : 'column',
								items : [ {
									layout : 'form',
									labelWidth : 100,
									columnWidth : 0.25,
									items : [areaStatusSelect,areaTypeSelect]
								},{
									layout : 'column',
									items : [ {
										layout : 'form',
										labelWidth : 100,
										columnWidth : 0.25,
										items : [{
											id : 'channelAreaMatch.oldRegionId',
											xtype : 'textfield',
											fieldLabel : '旧地区编码',
											name : 'channelAreaMatch.oldRegionId', 
											width : '150'
										} ,{
											id : 'channelAreaMatch.oldRegionName',
											xtype : 'textfield',
											fieldLabel : '旧地区名称',
											name : 'channelAreaMatch.oldRegionName', 
											width : '150'
										} ]
									} ]
								},{
									layout : 'column',
									items : [ {
										layout : 'form',
										labelWidth : 100,
										columnWidth : 0.25,
										items : [ {
											id : 'channelAreaMatch.newAreaId',
											xtype : 'textfield',
											fieldLabel : '新地区编码',
											name : 'channelAreaMatch.newAreaId', 
											width : '150'
										}, {
											id : 'channelAreaMatch.newAreaName',
											xtype : 'textfield',
											fieldLabel : '新地区名称',
											name : 'channelAreaMatch.newAreaName', 
											width : '150'
										} ]
									} ]
								}  ]
							}],
							buttons : [ {
								text : '查询',
								columnWidth : 0.1,
								handler : this.search
							}, {
								text : '重置',
								columnWidth : 0.1,
								handler : this.reset
							}]
						});

						checkBoxSelect = new Ext.grid.CheckboxSelectionModel();

						channelAreaClumnGrid = new Ext.grid.ColumnModel([
								checkBoxSelect, {
									id : 'id',
									header : "ID",
									align : "center",
									align : "center",
									width : 80,
									hidden : true,
									dataIndex : 'id'
								}, {
									id : 'id',
									align : "center",
									sortable : true,
									header : "ID",
									width : 80,
									dataIndex : 'id'
								}, {
									id : 'oldRegionType',
									header : "区域类型",
									align : "center",
									align : "center",
									width : 80,
									renderer : changeChannelStatus,
									dataIndex : 'oldRegionType'
								}, {
									id : 'pareaCode',
									align : "center",
									sortable : true,
									header : "父地区编码",
									width : 120,
									dataIndex : 'pareaCode'
								}, {
									id : 'oldRegionId',
									header : "旧地区编码",
									align : "center",
									align : "center",
									width : 120,
									dataIndex : 'oldRegionId'
								} , {
									id : 'oldRegionName',
									header : "旧地区名称",
									align : "center",
									align : "center",
									width : 160,
									dataIndex : 'oldRegionName'
								} , {
									id : 'newAreaId',
									header : "新地区编码",
									align : "center",
									align : "center",
									width : 120,
									dataIndex : 'newAreaId'
								} , {
									id : 'newAreaName',
									header : "新地区名称",
									align : "center",
									align : "center",
									width : 160,
									dataIndex : 'newAreaName'
								} , {
									id : 'matchType',
									header : "匹配类型",
									align : "center",
									align : "center",
									width : 160,
									renderer : changeMatchType,
									dataIndex : 'matchType'
								}
						]);

						// 与列对应的dataIndex
						couponrecord = Ext.data.Record.create([ 
							{ name : 'id' },
							{ name : 'oldRegionType' },
							{ name : 'pareaCode' },
							{ name : 'oldRegionId' },
							{ name : 'oldRegionName' },
							{ name : 'newAreaId' },
							{ name : 'newAreaName' },
							{ name : 'matchType' }
						]);

						channelAreaProxy = new Ext.data.HttpProxy( {
							url : basePath+ "/custom/channelArea/getchannelAreaMatchList.spmvc",
							method : "post"
						});
						// Reader 读json中数据
						channelAreaReader = new Ext.data.JsonReader({
							root : 'root',
							totalProperty : 'totalProperty'
						}, couponrecord);

						channelAreaStore = new Ext.data.Store({
							autoLoad : {
								params : {
									start : 0,
									limit : pageSize
								}
							},
							proxy : channelAreaProxy,
							reader : channelAreaReader
						});
						
						// 如果不是用ajax的表单封装提交,就要做如下操作.
						//这里很关键，如果不加，翻页后搜索条件就变没了，这里的意思是每次数据载入前先把搜索表单值加上去，这样就做到了翻页保留搜索条件了   
						channelAreaStore.on('beforeload', function(){
							// var keyword = getKeyword();
							// Ext.apply(this.baseParams, {shopChannel: 1});
						});

						// 定义菜单栏
						tbar = [
								 {
									id : 'message_grid_tBar@matchAll',
									text : '全量匹配',
									tooltip : 'matchAll',
									iconCls : 'matchAll',
									handler : function() {
										Ext.Msg.confirm("确认","确定要匹配全部新老地区:",
											function(btn) {
												if (btn == "yes"){
													Ext.Ajax.request({
														waitMsg : '请稍等.....',
														url : basePath+ '/custom/channelArea/matchChannelArea.spmvc',
														method : 'post',
														timeout:600000,
//														params : {ids : ids},
														success : function(response) {
															var respText = Ext.util.JSON.decode(response.responseText);
															if (respText.isok) {
																channelAreaStore.reload();
															} else {
																channelAreaStore.reload();
																alert(respText.message);
															}
														},
														failure : function(response,options) {
															alert("失败");
														}
													});
												}
											}
										);
									}
								} ];

						channelAreaMatchGrid = new Ext.grid.GridPanel(
							{
								//width : 1000,
								autoWidth : true,
								height : 500,
								title : '渠道新旧地区映射管理',
								store : channelAreaStore,
								id : 'addchannelAreaForm_grid_id',
								trackMouseOver : false,
								disableSelection : true,
								loadMask : true,
								frame : true,
								autoExpandColumn: "newAreaName", //自动伸展，占满剩余区域
								columnLines : true,
								tbar : tbar,
								// grid columns
								cm : channelAreaClumnGrid,
								sm : checkBoxSelect,
								// paging bar on the bottom
								bbar : new IssPagingToolbar(channelAreaStore, pageSize),
								listeners : {
									'rowdblclick' : function(thisgrid,rowIndex, e) {
										var selectionModel = thisgrid.getSelectionModel();
										var record = selectionModel.getSelected();
										var id = record.data['id'];
										FormEditWin.showAddDirWin( "popWins", basePath+ "/custom/channelArea/viewchannelAreaMatch.spmvc?id="+id,
												"pop_message_winID", "更新/查看新旧渠道地区映射", 800, 500);
									}
								}
						});
					},

					refresh:function(){
						channelAreaStore.reload();
					},

					doAfter : function(data){//上传后的数据返回
						var json = Ext.util.JSON.decode(data);
						FormEditWin.close();
						alertMsg("结果", json.message);
						channelAreaStore.reload();
						parent.ChannelArea.refresh();
					},
					show : function() {
						couponPanel = new Ext.Panel({
							renderTo : 'channelAreaMatch-grid',
							layout : 'column',
							items : [ channelAreaMatchForm, channelAreaMatchGrid ]
						}).show();
					},
					reset:function(){
						channelAreaMatchForm.form.reset();
					},
					search : function() {
						var outStock = {};
						outStock["newAreaId"] = Ext.getCmp("channelAreaMatch.newAreaId").getValue();
						outStock["oldRegionId"] = Ext.getCmp("channelAreaMatch.oldRegionId").getValue();
						outStock["newAreaName"] = Ext.getCmp("channelAreaMatch.newAreaName").getValue();
						outStock["oldRegionName"] = Ext.getCmp("channelAreaMatch.oldRegionName").getValue();
						outStock["matchType"] = Ext.getCmp("matchType").getValue();
						outStock["oldRegionType"] = Ext.getCmp("channelAreaMatch.oldRegionType").getValue();
						channelAreaMatchGrid.store.baseParams = outStock;
						channelAreaMatchGrid.store.load({
							params : {
								start : 0,
								limit : pageSize,
							}
						});
					},
					matchAll:function(){
						channelAreaMatchForm.form.reset();
					}
				}
			}();
			function runCouponList() {
				ChannelArea.init();
				ChannelArea.show();
				setResize();
			}
			runCouponList();

			function setResize() {
				var formHeight = channelAreaMatchForm.getHeight();
				var clientHeight = document.body.clientHeight;
				var clientWidth = document.body.clientWidth;
				channelAreaMatchGrid.setHeight(clientHeight-formHeight-50);
				channelAreaMatchGrid.setWidth(clientWidth-259);
			}
			window.onresize=function(){
				setResize();
			}
});
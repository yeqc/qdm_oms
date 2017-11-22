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
						var typeOptionCombo = new Ext.form.ComboBox({
							id : 'channelArea.channelCode',
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
							emptyText : '请选择渠道',
							editable : false,
							triggerAction : 'all',
							fieldLabel : '渠道',
							width : 150
						});
						//区域类型
						var areaTypeSelect = new Ext.form.ComboBox({
							id : 'channelArea.areaType',
							store : new Ext.data.SimpleStore({
								data : [ [ '0', '国家' ], [ '1', '省' ],[ '2', '市' ],[ '3', '区' ] ],
								fields : [ 'text', 'filed' ]
							}),
							xtype : 'combo',
							valueField : 'text',
							displayField : 'filed',
							mode : 'local',
							allowBlank : false,
							forceSelection : true,
							emptyText : '请选择区域类型',
							name : 'areaType',
							triggerAction : 'all',
							fieldLabel : '区域类型',
							width : 150
						});
						//映射状态
						var areaStatusSelect = new Ext.form.ComboBox({
							id : 'areaStatus',
							store : new Ext.data.SimpleStore({
								data : [ [ '0', '未映射' ], [ '1', '已映射' ],[ '', '全部' ] ],
								fields : [ 'text', 'filed' ]
							}),
							xtype : 'combo',
							valueField : 'text',
							displayField : 'filed',
							mode : 'local',
							forceSelection : true,
							emptyText : '请选择映射状态',
							name : 'areaStatus',
							triggerAction : 'all',
							fieldLabel : '映射状态',
							width : 150
						});

						channelAreaForm = new Ext.FormPanel({
							frame : true,
							autoWidth : true,
							url : "",
							labelAlign : 'right',
							layout : 'form',
							items : [ {
								layout : 'column',
								items : [ {
									layout : 'form',
									labelWidth : 80,
									columnWidth : 0.25,
									items : [typeOptionCombo,areaStatusSelect]
								},{
									layout : 'column',
									items : [ {
										layout : 'form',
										labelWidth : 80,
										columnWidth : 0.25,
										items : [ {
											id : 'channelArea.areaId',
											xtype : 'textfield',
											fieldLabel : '渠道地区编码',
											name : 'channelArea.areaId', 
											width : '150'
										}, {
											id : 'channelArea.areaName',
											xtype : 'textfield',
											fieldLabel : '渠道地区名称',
											name : 'channelArea.areaName', 
											width : '150'
										} ]
									} ]
								},{
									layout : 'column',
									items : [ {
										layout : 'form',
										labelWidth : 80,
										columnWidth : 0.25,
										items : [{
											id : 'channelArea.osRegionId',
											xtype : 'textfield',
											fieldLabel : 'OS地区编码',
											name : 'channelArea.osRegionId', 
											width : '150'
										} ,{
											id : 'channelArea.osRegionName',
											xtype : 'textfield',
											fieldLabel : 'OS地区名称',
											name : 'channelArea.osRegionName', 
											width : '150'
										} ]
									} ]
								} , {
									layout : 'column',
									items : [ {
										layout : 'form',
										labelWidth : 80,
										columnWidth : 0.25,
										items : [ areaTypeSelect ]
									} ]
								} ]
							}],
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
								},{
									id : 'pareaCode',
									align : "center",
									sortable : true,
									header : "渠道地区父编码",
									width : 120,
									dataIndex : 'pareaCode'
								}, {
									id : 'areaId',
									header : "渠道地区编码",
									align : "center",
									align : "center",
									width : 120,
									dataIndex : 'areaId'
								} , {
									id : 'areaType',
									header : "地区类型",
									align : "center",
									align : "center",
									width : 80,
									renderer : changeChannelStatus,
									dataIndex : 'areaType'
								} , {
									id : 'areaName',
									header : "渠道地区名称",
									align : "center",
									align : "center",
									width : 160,
									dataIndex : 'areaName'
								} , {
									id : 'osRegionId',
									header : "OS地区编码",
									align : "center",
									align : "center",
									width : 120,
									dataIndex : 'osRegionId'
								} , {
									id : 'osRegionName',
									header : "OS地区名称",
									align : "center",
									align : "center",
									width : 160,
									dataIndex : 'osRegionName'
								} , {
									id : 'channelCode',
									header : "渠道编码",
									align : "center",
									align : "center",
									width : 160,
									dataIndex : 'channelCode'
								}
						]);

						// 与列对应的dataIndex
						couponrecord = Ext.data.Record.create([ 
							{ name : 'id' },
							{ name : 'areaId' },
							{ name : 'pareaCode' },
							{ name : 'areaName' },
							{ name : 'areaType' },
							{ name : 'osRegionId' },
							{ name : 'osRegionName' },
							{ name : 'channelCode' }
						]);

						channelAreaProxy = new Ext.data.HttpProxy( {
							url : basePath+ "/custom/channelArea/getchannelAreaList.spmvc",
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
									id : 'area_upload_grid_tBar@add',
									text : '添加未映射区域',
									tooltip : '添加未映射区域',
									iconCls : 'add',
									handler : upload
								} , {
									id : 'area_upload_mapperd_tBar@add',
									text : '添加已映射区域',
									tooltip : '添加已映射区域',
									iconCls : 'add',
									handler : mapperdUpload
								} , {
									id : 'message_grid_tBar@delete',
									text : '批量移除',
									tooltip : 'delete',
									iconCls : 'delete',
									handler : function() {
										var selModel = channelAreaGrid.getSelectionModel();
										if (selModel.hasSelection()) {
											var records = selModel.getSelections();
											var ids = "";
											var channelTitles="";
											for ( var i = 0; i < records.length; i++) {
												var id = records[i].get("id");
												var areaName = records[i].get("areaName");
												if (id && id != ''&& id != null) {
													ids += "" + id + ",";
												}
												if(areaName != "" && areaName!=null){
													channelTitles+=""+areaName+","; //OS区域名称
												}
											}
											if (id != "") {
												Ext.Msg.confirm("确认","确定要删除映射:"+ channelTitles,
													function(btn) {
														if (btn == "yes"){
															Ext.Ajax.request({
																waitMsg : '请稍等.....',
																url : basePath+ '/custom/channelArea/deleteChannelArea.spmvc',
																method : 'post',
																params : {ids : ids},
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
										} else {
											alertMsg("错误", "请选择需要移除的行!");
										}
									}
								},'&nbsp;&nbsp;<a style="color:red" href = "'+ basePath + '/custom/fileUpload/fileDownloadCsv.spmvc?filePath=sysArea.csv' + '">地区映射下载</a>' ];

						channelAreaGrid = new Ext.grid.GridPanel(
							{
								//width : 1000,
								autoWidth : true,
								height : 500,
								title : '渠道地区映射管理',
								store : channelAreaStore,
								id : 'addchannelAreaForm_grid_id',
								trackMouseOver : false,
								disableSelection : true,
								loadMask : true,
								frame : true,
								autoExpandColumn: "areaName", //自动伸展，占满剩余区域
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
										FormEditWin.showAddDirWin( "popWins", basePath+ "/custom/channelArea/viewchannelArea.spmvc?id="+id,
												"pop_message_winID", "更新/查看渠道地区映射", 800, 530);
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
							renderTo : 'channelArea-grid',
							layout : 'column',
							items : [ channelAreaForm, channelAreaGrid ]
						}).show();
					},
					reset:function(){
						channelAreaForm.form.reset();
					},
					search : function() {
						var outStock = {};
						outStock["areaId"] = Ext.getCmp("channelArea.areaId").getValue();//渠道编码
						outStock["osRegionId"] = Ext.getCmp("channelArea.osRegionId").getValue();
						outStock["channelCode"] = Ext.getCmp("channelArea.channelCode").getValue();
						outStock["areaName"] = Ext.getCmp("channelArea.areaName").getValue();
						outStock["osRegionName"] = Ext.getCmp("channelArea.osRegionName").getValue();
						outStock["areaStatus"] = Ext.getCmp("areaStatus").getValue();
						outStock["areaType"] = Ext.getCmp("channelArea.areaType").getValue();
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
			function runCouponList() {
				ChannelArea.init();
				ChannelArea.show();
				setResize();
			}
			runCouponList();

			function setResize() {
				var formHeight = channelAreaForm.getHeight();
				var clientHeight = document.body.clientHeight;
				var clientWidth = document.body.clientWidth;
				channelAreaGrid.setHeight(clientHeight-formHeight-50);
				channelAreaGrid.setWidth(clientWidth-259);
			}
			window.onresize=function(){
				setResize();
			}
			function upload (){ //导入excel以后就与调整单绑定传入数据库保存
				//	 var params = "&params=ticketCode:"+ticketCode;
				FormEditWin.showAddDirWin(null,basePath+"/fileUpload.html?uploadUrl="+basePath+"/custom/channelArea/inportChannelArea.spmvc"+"&type=2&params=type:0","picADWinID","上传",580,300);
			}
		
			function mapperdUpload() {
				FormEditWin.showAddDirWin(null,basePath+"/fileUpload.html?uploadUrl="+basePath+"/custom/channelArea/inportChannelArea.spmvc"+"&type=2&params=type:1","picADWinID","上传",580,300);
			}
});
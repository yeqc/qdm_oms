Ext.onReady(init);

function init() {
	listPage();
};

function listPage() {
	
	var record_start = 0;
	var pageSize = 15;
	var params = {};

	var statusStore = new Ext.data.SimpleStore({
		fields : [ 'value', 'text' ],
		data : [ [ '', '请选择' ], [ '0', '关闭' ], [ '1', '启用' ] ]
	});

	var typeStore = new Ext.data.SimpleStore({
		fields : [ 'value', 'text' ],
		data : [ [ '', '请选择' ], [ '0', '满赠' ]/* , [ '1', '套装' ] */,
				[ '2', '买赠' ], [ '3', '集合赠' ] /* , [ '4', '福袋' ] */]
	});

	var promotionSearchForm = new Ext.FormPanel({
		applyTo : 'promotion_search_form',
		labelSeparator : "：",
		frame : true,
		border : false,
		items : [ {
			layout : 'column',
			border : false,
			labelSeparator : '：',
			items : [ {
				columnWidth : .3,
				layout : 'form',
				border : false,
				items : [ {
					id : 'promCode',
					xtype : 'textfield',
					name : 'promCode',
					anchor : '90%',
					fieldLabel : '活动ID'
				} ]
			}, {
				columnWidth : .3,
				layout : 'form',
				border : false,
				items : [ {
					id : 'promotion_shopCode',
					xtype : 'textfield',
					name : 'shopCode',
					readOnly : true,
					fieldLabel : '所属店铺',
					anchor : '90%'
				} ]
			}, {
				columnWidth : .3,
				layout : 'form',
				border : false,
				items : [ {
					id : 'promTitle',
					xtype : 'textfield',
					name : 'promTitle',
					fieldLabel : '活动名称',
					anchor : '90%'
				} ]
			}, {
				columnWidth : .3,
				layout : 'form',
				border : false,
				items : [ {
					readOnly : true,
					fieldLabel : '活动开始时间',
					anchor : '90%',
					html : '<input type="text" id="prom_beginTime" readonly="true" onClick="WdatePicker({dateFmt:\'yyyy-MM-dd HH:mm:ss\'})"/>'
				} ]
			}, {
				columnWidth : .3,
				layout : 'form',
				border : false,
				items : [ {
					fieldLabel : '活动结束时间',
					anchor : '90%',
					readOnly : true,
					html : '<input type="text" id="prom_endTime" readonly="true" onClick="WdatePicker({dateFmt:\'yyyy-MM-dd HH:mm:ss\'})"/>'
				} ]
			}, {
				columnWidth : .3,
				layout : 'form',
				border : false,
				items : [ {
					id : 'promStatus',
					xtype : 'combo',
					mode : 'local',
					editable : false,
					valueField : 'value',
					displayField : 'text',
					triggerAction : 'all',
					store : statusStore,
					name : 'promStatus',
					fieldLabel : '活动状态',
					emptyText : '请选择',
					anchor : '90%'
				} ]
			}, {
				columnWidth : .3,
				layout : 'form',
				border : false,
				items : [ {
					id : 'promType',
					xtype : 'combo',
					mode : 'local',
					editable : false,
					valueField : 'value',
					displayField : 'text',
					triggerAction : 'all',
					store : typeStore,
					name : 'promType',
					fieldLabel : '活动类型',
					emptyText : '请选择',
					anchor : '90%'
				} ]
			} ]
		} ],
		buttonAlign : 'right',
		buttons : [ {
			text : '查询',
			handler : clickSearchButton,
		}, {
			text : '重置',
			handler : clickResetButton,
		} ]
	});
	
	function clickSearchButton() {
		var searchProxy = new Ext.data.HttpProxy({
			url : basePath + '/custom/promotion/search.spmvc'
		});
		dataTableGrid.store.proxy = searchProxy;
		params["promCode"] = getValueById("promCode");
		params["shopCode"] = getGlobalValue('shopCode');
		params["promTitle"] = getValueById("promTitle");
		params["beginTime"] = $("#prom_beginTime").val();
		params["endTime"] = $("#prom_endTime").val();
		params["promStatus"] = getValueById("promStatus");
		params["promType"] = getValueById("promType");
		dataTableGrid.store.load({
			params : params
		});
	}

	function clickResetButton() {
		promotionSearchForm.form.reset();
		Ext.getCmp('promotion_shopCode').setValue(getGlobalValue('shopTitle'));
		$("#prom_beginTime").val("");
		$("#prom_endTime").val("");
	}
	
	Ext.getCmp('promotion_shopCode').setValue(getGlobalValue('shopTitle'));

	params["start"] = 0;
	params["limit"] = pageSize;
	DataTable = function() {
		// Record 定义记录结果
		this.record = Ext.data.Record.create([ {
			name : "id"
		}, {
			name : "promCode"
		}, {
			name : "promTitle"
		}, {
			name : "promStatus"
		}, {
			name : "shopTitle"
		}, {
			name : "promType"
		}, {
			name : "beginTime"
		}, {
			name : "endTime"
		}, {
			name : "backup"
		} ]);

		this.proxy = new Ext.data.HttpProxy({
			url : basePath + '/custom/promotion/search.spmvc',
			method : "post"
		});

		// Reader 读json中数据
		this.reader = new Ext.data.JsonReader({
			root : 'root',
			totalProperty : 'totalProperty'
		}, this.record);

		this.store = new Ext.data.Store({
			remoteSort : true,
			autoLoad : {
				params : params
			},
			proxy : this.proxy,
			reader : this.reader
		});

		this.store.on('beforeload', function() {
			Ext.apply(this.baseParams, {shopCode: getGlobalValue('shopCode')});
		});

		// 点击每列前面的复选框 的监听器
		this.sm = new Ext.grid.CheckboxSelectionModel({
			listeners : {
				selectionchange : function(sm) {
					if (sm.getCount()) {
						dataTableGrid.removeButton.enable();
						dataTableGrid.refreshButton.enable();
						dataTableGrid.unrefreshButton.enable();
					} else {
						dataTableGrid.removeButton.disable();
						dataTableGrid.refreshButton.disable();
						dataTableGrid.unrefreshButton.disable();
					}
				}
			}
		});

		// 列模型，定义GridPanel的表头
		this.cm = new Ext.grid.ColumnModel([ this.sm, {
			header : "序号",
			width : 40,
			renderer : function(value, metadata, record, rowIndex) {
				return record_start + 1 + rowIndex;
			}
		}, {
			id : 'promCode',
			header : "活动ID",
			width : 170,
			dataIndex : 'promCode'
		}, {
			id : 'promTitle',
			header : "活动名称",
			width : 200,
			dataIndex : 'promTitle'
		}, {
			id : 'promStatus',
			header : "活动状态",
			width : 60,
			dataIndex : 'promStatus',
			renderer : function(value, metadata, record, rowIndex) {
				var str = "";
				if (value == 0) {
					str = "关闭";
				}
				if (value == 1) {
					str = "启用";
				}
				return str;
			}
		}, {
			id : 'shopTitle',
			hidden : true,
			header : "所属店铺",
			width : 80,
			dataIndex : 'shopTitle'
		}, {
			id:'promType',
			header : "活动类型",
			width : 60,
			dataIndex : 'promType',
			renderer : function(value, metadata, record, rowIndex) {
				var str = "";
				if (value == 0) {
					str = "满赠";
				}
				if (value == 1) {
					str = "套装";
				}
				if (value == 2) {
					str = "买赠";
				}
				if (value == 3) {
					str = "集合赠";
				}
				if (value == 4) {
					str = "福袋";
				}
				return str;
			}
		}, {
			id:'beginTime',
			header : "活动开始时间",
			width : 140,
			dataIndex : 'beginTime'
		} , {
			id:'endTime',
			header : "活动结束时间",
			width : 140,
			dataIndex : 'endTime'
		} , {
			id:'backup',
			header : "备注",
			width : 250,
			dataIndex : 'backup'
		} ]);

		var menu = new Ext.menu.Menu({
			items : [
					{
						text : "满赠",
						handler : function() {
							FormEditWin.showAddDirWinS(null, basePath
									+ "/page/promotion/promotion-0.jsp",
									"temSelWinID", "增加满赠促销", 1000, 500);
						}
					},
					{
						text : "买赠",
						handler : function() {
							FormEditWin.showAddDirWinS(null, basePath
									+ "/page/promotion/promotion-2.jsp",
									"temSelWinID", "增加买赠促销", 1000, 500);
						}
					},
					{
						text : "集合赠",
						handler : function() {
							FormEditWin.showAddDirWinS(null, basePath
									+ "/page/promotion/promotion-3.jsp",
									"temSelWinID", "增加集合赠促销", 1000, 500);
						}
					} /*
						 * , { text : "福袋", handler : function() {
						 * FormEditWin.showAddDirWinS(null, basePath +
						 * "/page/promotion/promotion-4.jsp", "temSelWinID",
						 * "增加福袋促销", 1000, 500); } }
						 */]
		});

		// 定义菜单栏
		this.tbar = [ {
			text : '添加活动',
			menu : menu
		}, '-', {
			text : '查看更新',
			iconCls : 'modify',
			handler : this.modifyObject
		}, '-', {
			text : '批量删除',
			iconCls : 'delete',
			ref : '../removeButton',
			disabled : true,
			handler : this.deleteObject
		}, '-', {
			text : '批量启用',
			iconCls : 'refresh',
			ref : '../refreshButton',
			disabled : true,
			handler : this.activateObject
		}, '-', {
			text : '批量关闭',
			iconCls : 'refresh',
			ref : '../unrefreshButton',
			disabled : true,
			handler : this.unactivateObject
		} ];

		// 调用父类构造方法
		DataTable.superclass.constructor.call(this, {
			id : 'promotion_list_gridpanel',
			loadMask : true,
			frame : true,
			header : false,
			iconCls : 'icon-grid',
//			autoHeight : true,
			autoSizeColumns : true,
			autoExpandColumn: "promTitle", //自动伸展，占满剩余区域
//			autoWidth : true,
			animCollapse : false,
			columnLines : true,
			tbar : this.tbar,
			bbar : new IssPagingToolbar(this.store, pageSize),
			renderTo : "promotion_data_panel", // 规则：加上模块名；并且不能和id取重名
			listeners : {
				'rowdblclick' : function(grid, rowIndex, e) {
					var record = grid.getSelectionModel().getSelected();
					tomodify(record);
				}
			}
		});
	};

	Ext
			.extend(
					DataTable,
					Ext.grid.GridPanel,
					{
						loadStore : function() {
							dataTableGrid.store.load({
								params : params
							});
						},
						deleteObject : function() { // 删除
							var selModel = dataTableGrid.getSelectionModel();
							if (selModel.hasSelection()) {
								confirmMsg(
										"确认",
										"您确定要删除选择的记录吗?",
										function(btn) {
											if (btn == "yes") {// 确认
												var records = selModel
														.getSelections();
												var ids = [];
												for ( var i = 0; i < records.length; i++) {
													ids.push(records[i]
															.get("id"));
												}
												Ext.Ajax
														.request({
															waitMsg : '请稍等.....',
															url : basePath
																	+ '/custom/promotion/delete.spmvc',
															params : {
																ids : ids
															},
															method : "post",
															success : function(
																	response) {
																var json = Ext.util.JSON
																		.decode(response.responseText);
																alertMsg(
																		"结果",
																		json.message);
																dataTableGrid
																		.loadStore();// 更新页面
															},
															failure : function() {
																errorMsg("结果",
																		"删除失败！");
															}
														});
											}
										});
							} else {
								alertMsg("提示", "请选择要删除的数据!");
							}
						},
						activateObject : function() { // 激活
							var selModel = dataTableGrid.getSelectionModel();
							if (selModel.hasSelection()) {
								confirmMsg(
										"确认",
										"您确定要激活选择的记录吗?",
										function(btn) {
											if (btn == "yes") {// 确认
												var records = selModel
														.getSelections();
												var ids = [];
												for ( var i = 0; i < records.length; i++) {
													ids.push(records[i]
															.get("id"));
												}
												Ext.Ajax
														.request({
															waitMsg : '请稍等.....',
															url : basePath
																	+ '/custom/promotion/activate.spmvc',
															params : {
																ids : ids
															},
															method : "post",
															success : function(
																	response) {
																var json = Ext.util.JSON
																		.decode(response.responseText);
																alertMsg(
																		"结果",
																		json.message);
																dataTableGrid
																		.loadStore();// 更新页面
															},
															failure : function() {
																errorMsg("结果",
																		"数据激活失败！");
															}
														});
											}
										});
							} else {
								alertMsg("提示", "请选择要激活的数据!");
							}
						},
						unactivateObject : function() { // 未激活（失效）
							var selModel2 = dataTableGrid.getSelectionModel();
							if (selModel2.hasSelection()) {
								confirmMsg(
										"确认",
										"您确定要失效选择的记录吗?",
										function(btn) {
											if (btn == "yes") {// 确认
												var records = selModel2
														.getSelections();
												var ids = [];
												for ( var i = 0; i < records.length; i++) {
													ids.push(records[i]
															.get("id"));
												}
												Ext.Ajax
														.request({
															waitMsg : '请稍等.....',
															url : basePath
																	+ '/custom/promotion/unactivate.spmvc',
															params : {
																ids : ids
															},
															method : "post",
															success : function(
																	response) {
																var json = Ext.util.JSON
																		.decode(response.responseText);
																alertMsg(
																		"结果",
																		json.message);
																dataTableGrid
																		.loadStore();// 更新页面
															},
															failure : function() {
																errorMsg("结果",
																		"数据失效失败！");
															}
														});
											}
										});
							} else {
								alertMsg("提示", "请选择要失效的数据!");
							}
						},
						modifyObject : function() { // 查看更新
							var records = dataTableGrid.getSelectionModel()
									.getSelections();
							var num = records.length;
							if (num == 0) {
								Ext.MessageBox.alert('提示', '请选择要查看修改的数据!');
								return;
							}
							if (num > 1) {
								Ext.MessageBox.alert('提示', '自能选择一条数据进行查看修改!');
								return;
							}
							tomodify(records[0]);
						}
					});

	function tomodify(record) {
		var id = record.get('id');
		var promType = record.get('promType');
		var title = '';
		if (promType == 0) {
			title = '满赠促销查看更新';
		}
		if (promType == 1) {
			title = '套装促销查看更新';
		}
		if (promType == 2) {
			title = '买赠促销查看更新';
		}
		if (promType == 3) {
			title = '集合赠促销查看更新';
		}
		if (promType == 4) {
			title = '福袋促销查看更新';
		}
		FormEditWin.showAddDirWinS(null, basePath
				+ "/custom/promotion/tomodify.spmvc?id=" + id, "temSelWinID",
				title, 1000, 500);
	}

	var dataTableGrid = new DataTable();

	function setResize() {
		var formHeight = promotionSearchForm.getHeight();
		var clientHeight = document.body.clientHeight;
		var clientWidth = document.body.clientWidth;
		dataTableGrid.setHeight(clientHeight-formHeight-50);
		dataTableGrid.setWidth(clientWidth-259);
	}
	window.onresize=function(){
		setResize();
	};
	setResize();
};


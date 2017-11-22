Ext.onReady(openShopChannelInfo);

/**
 * OPENSHOP店铺信息维护
 */
function openShopChannelInfo() {
	Ext.QuickTips.init();

	Ext.form.Field.prototype.msgTarget = 'side';

	var pageSize = 15;

	var params = {};
	params["start"] = 0;
	params["limit"] = pageSize;

	// 执行类型选择框
	var channelTypeSelectOption = new Ext.form.ComboBox({
		id : 'addOrUpdate.channelType',
		store : new Ext.data.SimpleStore({
			data : [ [ 'none', '未配置' ], [ 'taobao', '淘宝店' ],
					[ 'paipai', '拍拍店' ], [ 'mall', '商城' ] ],
			fields : [ 'text', 'filed' ]
		}),
		allowBlank : false,
		xtype : 'combo',
		valueField : 'text',
		displayField : 'filed',
		mode : 'local',
		forceSelection : true,
		blankText : '请选择渠道类型',
		emptyText : '请选择渠道类型',
		name : 'addOrUpdate.channelType',
		editable : false,
		triggerAction : 'all',
		fieldLabel : '渠道类型',
		anchor : '90%'
	});

	// 执行类型选择框
	var isActiveSelectOption = new Ext.form.ComboBox({
		id : 'addOrUpdate.isActive',
		store : new Ext.data.SimpleStore({
			data : [ [ '0', '否' ], [ '1', '是' ] ],
			fields : [ 'text', 'filed' ]
		}),
		allowBlank : false,
		xtype : 'combo',
		valueField : 'text',
		displayField : 'filed',
		mode : 'local',
		forceSelection : true,
		blankText : '请选择是否激活',
		emptyText : '请选择是否激活',
		name : 'addOrUpdate.isActive',
		editable : false,
		triggerAction : 'all',
		fieldLabel : '是否激活',
		anchor : '90%'
	});

	var openShopChannelInfoSearchForm = new Ext.FormPanel({
		frame : true,
		forceFit : true,
		labelAlign : "left",
		waitMsgTarget : true,
		bodyBorder : false,
		applyTo : 'openshop-channelinfo-search-div',
		border : true,
		items : [ {
			layout : 'column',
			border : false,
			labelSeparator : '：',
			items : [ {
				columnWidth : .3,
				layout : 'form',
				border : false,
				items : [ {
					width : 165,
					id : 'openShopChannelInfo.search.parentChannelCode',
					xtype : 'textfield',
					name : 'channelCode',
					fieldLabel : '父店铺编码'
				} ]
			}, {
				columnWidth : .3,
				layout : 'form',
				border : false,
				items : [ {
					width : 165,
					id : 'openShopChannelInfo.search.channelCode',
					xtype : 'textfield',
					name : 'channelCode',
					fieldLabel : '店铺编码'
				} ]
			}, {
				columnWidth : .3,
				layout : 'form',
				border : false,
				items : [ {
					width : 165,
					id : 'openShopChannelInfo.search.channelName',
					xtype : 'textfield',
					name : 'channelCode',
					fieldLabel : '店铺名称'
				} ]
			} ]
		} ],
		buttonAlign : 'right',
		buttons : [ {
			text : '查询',
			handler : clickSearchButton
		}, {
			text : '重置',
			handler : clickResetButton
		} ]
	});

	function clickResetButton() {
		openShopChannelInfoSearchForm.form.reset();
	}

	var record = Ext.data.Record.create([ {
		name : 'channelId'
	}, {
		name : 'parentChannelCode'
	}, {
		name : 'channelCode'
	}, {
		name : 'channelName'
	}, {
		name : 'nickName'
	} ]);

	this.proxy = new Ext.data.HttpProxy({
		url : basePath + '/custom/openshop/channelinfo/search.spmvc',
		method : "post"
	});

	// Reader 读json中数据
	var reader = new Ext.data.JsonReader({
		root : 'root',
		totalProperty : 'totalProperty'
	}, record);

	// 定义数据集对象
	var dataStore = new Ext.data.Store({
		remoteSort : true,
		autoLoad : {
			params : params
		},
		proxy : proxy,
		reader : reader
	});

	// 创建工具栏组件
	var toolbar = new Ext.Toolbar({
		items : [ "菜单栏：", "-", {
			text : '添加',
			iconCls : 'add',
			handler : addDataWinOpen
		}, {
			text : '批量移除',
			iconCls : 'delete',
			handler : deleteDataForBatch
		} ]
	});

	// 创建全选组件
	var cb = new Ext.grid.CheckboxSelectionModel();

	// 创建Grid表格组件
	var dataGridPanel = new Ext.grid.GridPanel({
		id : 'openshop-channelinfo-data-panel',
		renderTo : 'openshop-channelinfo-data-div',
		autoExpandColumn : "channelName", // 自动伸展，占满剩余区域
		viewConfig : {
			forceFit : true
		},
		loadMask : true,
		frame : true,
		forceFit : true,
		bodyBorder : false,
		tbar : toolbar,
		store : dataStore,
		sm : cb,
		bbar : new IssPagingToolbar(dataStore, pageSize),
		columns : [ cb, {
			id : 'openShopChannelInfo.channelId',
			header : "ID",
			width : 100,
			dataIndex : 'channelId'
		}, {
			id : 'openShopChannelInfo.parentChannelCode',
			header : "父店铺编号",
			width : 100,
			dataIndex : 'parentChannelCode'
		}, {
			id : 'openShopChannelInfo.channelCode',
			header : "店铺编号",
			width : 100,
			dataIndex : 'channelCode'
		}, {
			id : 'openShopChannelInfo.channelName',
			header : "店铺名称",
			width : 200,
			dataIndex : 'channelName'
		}, {
			id : 'openShopChannelInfo.nickName',
			header : "店铺昵称",
			width : 200,
			dataIndex : 'nickName'
		} ],
		listeners : {
			'rowdblclick' : function(grid, rowIndex, e) {
				toModifyopenShopChannelInfo();
			}
		}
	});

	dataGridPanel.store.on('beforeload', function() {
		Ext.apply(this.baseParams, params);
	});

	toModifyopenShopChannelInfo = function() {
		var record = dataGridPanel.getSelectionModel().getSelected();
		var modifyChannelCode = record.get('channelCode');
		Ext.Ajax.request({
			waitMsg : '请稍等.....',
			url : basePath + '/custom/openshop/channelinfo/select.spmvc',
			params : {
				channelCode : modifyChannelCode
			},
			method : "post",
			success : function(response) {
				var json = Ext.util.JSON.decode(response.responseText);
				if (json.isok) {
					var data = json.data;
					Ext.getCmp('addOrUpdate.channelId')
							.setValue(data.channelId);
					Ext.getCmp('addOrUpdate.parentChannelCode').setValue(
							data.parentChannelCode);
					Ext.getCmp('addOrUpdate.channelCode').setValue(
							data.channelCode);
					Ext.getCmp('addOrUpdate.channelName').setValue(
							data.channelName);
					Ext.getCmp('addOrUpdate.nickName').setValue(data.nickName);
					Ext.getCmp('addOrUpdate.channelType').setValue(
							data.channelType);
					Ext.getCmp('addOrUpdate.isActive').setValue(data.isActive);
				} else {
					alertMsg("结果", json.message);
				}
			},
			failure : function() {
				errorMsg("结果", modifyChannelCode + "查询失败！");
			}
		});
		addOrUpdateWinOpen("修改店铺信息");
	};

	function clickSearchButton() {
		params["parentChannelCode"] = getValueById("openShopChannelInfo.search.parentChannelCode");
		params["channelCode"] = getValueById("openShopChannelInfo.search.channelCode");
		params["channelName"] = getValueById("openShopChannelInfo.search.channelName");
		dataGridPanel.store.load({
			params : params
		});
	}

	function deleteDataForBatch() {
		var recs = dataGridPanel.getSelectionModel().getSelections();
		var num = recs.length;
		if (num == 0) {
			Ext.MessageBox.alert('提示', '请选择要移除的数据!');
			return;
		}
		Ext.MessageBox.confirm("提示", "您确定要删除所选数据吗？", function(btnId) {
			if (btnId == 'yes') {
				var channelCodes = '';
				for ( var i = 0; i < recs.length; i++) {
					var channelCode = recs[i].get("channelCode");
					if (channelCodes == '') {
						channelCodes = channelCode;
					} else {
						channelCodes = channelCodes + "," + channelCode;
					}
				}
				Ext.Ajax.request({
					waitMsg : '请稍等.....',
					url : basePath
							+ '/custom/openshop/channelinfo/delete.spmvc',
					params : {
						channelCodes : channelCodes
					},
					method : "post",
					success : function(response) {
						var json = Ext.util.JSON.decode(response.responseText);
						alertMsg("结果", json.message);
						dataGridPanel.store.load({
							params : params
						});
					},
					failure : function() {
						errorMsg("结果", "删除失败！");
					}
				});
			}
		});
	}

	var addOrUpdateForm = new Ext.FormPanel({
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
					id : 'addOrUpdate.channelId',
					xtype : 'hidden'
				} ]
			}, {
				columnWidth : .3,
				layout : 'form',
				border : false,
				items : [ {
					id : 'addOrUpdate.parentChannelCode',
					xtype : 'textfield',
					width : 120,
					anchor : '90%',
					fieldLabel : '父店铺编号'
				} ]
			}, {
				columnWidth : .3,
				layout : 'form',
				border : false,
				items : [ {
					id : 'addOrUpdate.channelCode',
					allowBlank : false,
					xtype : 'textfield',
					width : 120,
					anchor : '90%',
					fieldLabel : '店铺编号'
				} ]
			}, {
				columnWidth : .3,
				layout : 'form',
				border : false,
				items : [ {
					id : 'addOrUpdate.channelName',
					allowBlank : false,
					xtype : 'textfield',
					width : 120,
					anchor : '90%',
					fieldLabel : '店铺名称'
				} ]
			}, {
				columnWidth : .3,
				layout : 'form',
				border : false,
				items : [ {
					id : 'addOrUpdate.nickName',
					xtype : 'textfield',
					width : 120,
					anchor : '90%',
					fieldLabel : '店铺昵称'
				} ]
			}, {
				columnWidth : .3,
				layout : 'form',
				border : false,
				items : [ channelTypeSelectOption ]
			}, {
				columnWidth : .3,
				layout : 'form',
				border : false,
				items : [ isActiveSelectOption ]
			} ]
		} ],
		buttonAlign : 'center',
		buttons : [ {
			text : '提交',
			handler : submitForm
		}, {
			text : '关闭',
			handler : function() {
				win.hide();
			}
		} ]
	});

	// 创建弹出窗口
	var win = new Ext.Window({
		layout : 'fit',
		width : 1000,
		closeAction : 'hide',
		height : 500,
		resizable : false,
		shadow : true,
		modal : true,
		closable : true,
		bodyStyle : 'padding:5 5 5 5',
		animCollapse : true,
		items : [ addOrUpdateForm ]
	});

	// 显示新建档口用户窗口
	function addOrUpdateWinOpen(title) {
		addOrUpdateForm.form.reset();
		win.setTitle(title);
		win.show();
	}

	// 显示添加细则窗口
	function addDataWinOpen() {
		addOrUpdateWinOpen("添加店铺信息");
	}

	function submitForm() {
		if (!addOrUpdateForm.getForm().isValid()) {
			alertMsg("验证", "请检查数据是否校验！");
			return;
		}

		Ext.Ajax
				.request({
					waitMsg : '请稍等.....',
					url : basePath
							+ '/custom/openshop/channelinfo/addOrUpdate.spmvc',
					params : {
						channelId : getValueById("addOrUpdate.channelId"),
						parentChannelCode : getValueById("addOrUpdate.parentChannelCode"),
						channelCode : getValueById("addOrUpdate.channelCode"),
						channelName : getValueById("addOrUpdate.channelName"),
						nickName : getValueById("addOrUpdate.nickName"),
						channelType : getValueById("addOrUpdate.channelType"),
						isActive : getValueById("addOrUpdate.isActive")
					},
					method : "post",
					success : function(response) {
						var json = Ext.util.JSON.decode(response.responseText);
						alertMsg("结果", json.message);
						if (json.isok) {
							win.hide();
							dataGridPanel.store.load({
								params : params
							});
						}
					},
					failure : function() {
						errorMsg("结果", "提交失败！");
					}
				});
	}

	function setResize() {
		var formHeight = openShopChannelInfoSearchForm.getHeight();
		var clientHeight = document.body.clientHeight;
		var clientWidth = document.body.clientWidth;
		dataGridPanel.setHeight(clientHeight - formHeight - 50);
		dataGridPanel.setWidth(clientWidth - 259);
	}
	window.onresize = function() {
		setResize();
	};
	setResize();
}

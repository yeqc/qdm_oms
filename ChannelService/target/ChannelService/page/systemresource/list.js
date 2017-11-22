Ext.onReady(initPage);

function initPage() {

	Ext.QuickTips.init();

	Ext.form.Field.prototype.msgTarget = 'side';

	var pageSize = 15;

	var systemResourceSearchForm = new Ext.FormPanel({
		frame : true,
		bodyStyle : 'padding:5px 5px 0',
//		autoHeight : true,
//		autoScroll : true,
		labelAlign : 'right',
		layout : 'form',
		items : [ {
			layout : 'column',
			items : [ {
				columnWidth : .3,
				layout : 'form',
				border : false,
				items : [ {
					id : 'systemResource.resourceName',
					xtype : 'textfield',
					fieldLabel : '资源名称',
					name : 'systemResource.resourceName'
				} ]
			}, {
				columnWidth : .3,
				layout : 'form',
				border : false,
				items : [ {
					id : 'systemResource.resourceUrl',
					xtype : 'textfield',
					fieldLabel : '资源URL',
					name : 'systemResource.resourceUrl'
				} ]
			} ]
		} ],
		buttons : [ {
			text : '查询',
			handler : clickSearchButton
		}, {
			text : '重置',
			handler : clickResetButton
		} ]
	});

	function clickSearchButton() {
		var params = {};
		params["resourceName"] = Ext.getCmp("systemResource.resourceName")
				.getValue();
		params["resourceUrl"] = Ext.getCmp("systemResource.resourceUrl")
				.getValue();
		systemResourceGrid.store.baseParams = params;
		systemResourceGrid.store.load({
			params : {
				start : 0,
				limit : pageSize
			}
		});
	}

	function clickResetButton() {
		systemResourceSearchForm.form.reset();
	}

	var checkBoxSelect = new Ext.grid.CheckboxSelectionModel();

	var systemResourceColumnGrid = new Ext.grid.ColumnModel([ checkBoxSelect, {
		id : 'resourceId',
		header : "resourceId",
		align : "center",
		width : 80,
		hidden : true,
		dataIndex : 'resourceId'
	}, {
		id : 'resourceCode',
		align : "center",
		sortable : true,
		header : "资源唯一码",
		width : 140,
		dataIndex : 'resourceCode'
	}, {
		id : 'resourceName',
		header : "资源名称",
		align : "center",
		width : 200,
		dataIndex : 'resourceName'
	}, {
		id : 'resourceUrl',
		header : "资源URL",
		align : "center",
		dataIndex : 'resourceUrl'
	}, {
		id : 'isShow',
		header : "是否显示",
		align : "center",
		width : 80,
		renderer : function(e) {
			var msg = "";
			if (e == '0') {
				msg = "否";
			}
			if (e == '1') {
				msg = "是";
			}
			return msg;
		},
		dataIndex : 'isShow'
	} ]);

	// 与列对应的dataIndex
	var couponrecord = Ext.data.Record.create([ {
		name : 'resourceId'
	}, {
		name : 'resourceCode'
	}, {
		name : 'parentCode'
	}, {
		name : 'resourceType'
	}, {
		name : 'resourceName'
	}, {
		name : 'resourceUrl'
	}, {
		name : 'isShow'
	} ]);

	// 加载列表数据
	var systemResourceProxy = new Ext.data.HttpProxy({
		url : basePath + "/custom/systemresource/list.spmvc",
		method : "post"
	});
	// Reader 读json中数据
	var systemResourceReader = new Ext.data.JsonReader({
		root : 'root',
		totalProperty : 'totalProperty'
	}, couponrecord);

	var systemResourceStore = new Ext.data.Store({
		autoLoad : {
			params : {
				start : 0,
				limit : pageSize

			}
		},
		proxy : systemResourceProxy,
		reader : systemResourceReader
	});

	// 如果不是用ajax的表单封装提交,就要做如下操作.
	// 这里很关键，如果不加，翻页后搜索条件就变没了，这里的意思是每次数据载入前先把搜索表单值加上去，这样就做到了翻页保留搜索条件了
	systemResourceStore.on('beforeload', function() {
		Ext.apply(this.baseParams, {

		});
	});

	// 定义菜单栏
	var tbar = [ "菜单栏：", "-", {
		text : '添加',
		iconCls : 'add',
		handler : addDataWinOpen
	}, {
		text : '批量移除',
		iconCls : 'delete',
		handler : deleteDataForBatch
	} ];

	var systemResourceGrid = new Ext.grid.GridPanel({
		autoWidth : true,
		title : '权限资源管理',
		store : systemResourceStore,
		id : 'systemresource-list-grid',
		trackMouseOver : false,
		disableSelection : true,
		loadMask : true,
		frame : true,
		columnLines : true,
		tbar : tbar,
		height : 550,
		autoExpandColumn : "resourceUrl", // 自动伸展，占满剩余区域
		cm : systemResourceColumnGrid,
		sm : checkBoxSelect,
		bbar : new IssPagingToolbar(systemResourceStore, pageSize),
		listeners : {
			'rowdblclick' : function(thisgrid, rowIndex, e) {
				var selectionModel = thisgrid.getSelectionModel();
				var record = selectionModel.getSelected();
				//systemResourceForm.form.reset();
				Ext.getCmp('resource_id').setValue("");
				Ext.getCmp('parent_code').setValue("");
				Ext.getCmp('resource_type').setValue("");
				Ext.getCmp('resource_code').setValue("");
				Ext.getCmp('resource_name').setValue("");
				Ext.getCmp('resource_url').setValue("");
				Ext.getCmp('is_show').setValue("");
				
				win.setTitle("资源修改");
				Ext.getCmp('resource_id').setValue(record.get('resourceId'));
				Ext.getCmp('parent_code').setValue(record.get('parentCode'));
				Ext.getCmp('resource_type')
						.setValue(record.get('resourceType'));
				Ext.getCmp('resource_code')
						.setValue(record.get('resourceCode'));
				Ext.getCmp('resource_name')
						.setValue(record.get('resourceName'));
				Ext.getCmp('resource_url').setValue(record.get('resourceUrl'));
				Ext.getCmp('is_show').setValue(record.get('isShow'));
				win.show();
			}
		}
	});

	// 执行类型选择框
	var isShowSelectOption = new Ext.form.ComboBox({
		id : 'is_show',
		store : new Ext.data.SimpleStore({
			data : [ [ '0', '否' ], [ '1', '是' ] ],
			fields : [ 'text', 'filed' ]
		}),
		xtype : 'combo',
		valueField : 'text',
		value : '1',
		displayField : 'filed',
		mode : 'local',
		forceSelection : true,
		editable : false,
		triggerAction : 'all',
		fieldLabel : '是否显示',
		anchor : '90%',
		width : 200
	});

	// 执行类型选择框
	var resourceTypeSelectOption = new Ext.form.ComboBox({
		id : 'resource_type',
		store : new Ext.data.SimpleStore({
			data : [ [ 'group', 'group' ], [ 'url', 'url' ] ],
			fields : [ 'text', 'filed' ]
		}),
		xtype : 'combo',
		valueField : 'text',
		value : '1',
		displayField : 'filed',
		allowBlank : false,
		mode : 'local',
		forceSelection : true,
		editable : false,
		triggerAction : 'all',
		fieldLabel : '资源类型',
		anchor : '90%',
		width : 200
	});

	var systemResourceForm = new Ext.FormPanel({
		bodyStyle : 'padding:10px 5px 5px 5px',
		labelSeparator : "：",
		frame : true,
		border : false,
		items : [ {
			layout : 'form',
			border : false,
			labelSeparator : '：',
			items : [ {
				layout : 'form',
				border : false,
				items : [ {
					id : 'resource_id',
					xtype : 'hidden'
				} ]
			}, {
				layout : 'form',
				border : false,
				items : [ {
					id : 'parent_code',
					xtype : 'textfield',
					width : 200,
					anchor : '90%',
					fieldLabel : '父资源唯一码'
				} ]
			}, {
				layout : 'form',
				border : false,
				items : [ resourceTypeSelectOption ]
			}, {
				layout : 'form',
				border : false,
				items : [ {
					id : 'resource_code',
					xtype : 'textfield',
					width : 200,
					allowBlank : false,
					blankText : '资源唯一码',
					anchor : '90%',
					fieldLabel : '资源唯一码'
				} ]
			}, {
				layout : 'form',
				border : false,
				items : [ {
					id : 'resource_name',
					xtype : 'textfield',
					allowBlank : false,
					width : 200,
					blankText : '资源名称不能为空',
					fieldLabel : '资源名称',
					anchor : '90%'
				} ]
			}, {
				layout : 'form',
				border : false,
				items : [ {
					id : 'resource_url',
					xtype : 'textfield',
					width : 200,
					fieldLabel : '资源URL',
					anchor : '90%'
				} ]
			}, {
				layout : 'form',
				border : false,
				items : [ isShowSelectOption ]
			} ]
		} ],
		buttonAlign : 'center',
		buttons : [ {
			text : '提交',
			handler : submitSystemResourceForm
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
		width : 400,
		closeAction : 'hide',
		height : 250,
		resizable : false,
		shadow : true,
		modal : true,
		closable : true,
		bodyStyle : 'padding:5 5 5 5',
		animCollapse : true,
		items : [ systemResourceForm ]
	});

	// 显示新建档口用户窗口
	function addDataWinOpen() {
		//systemResourceForm.form.reset();
		Ext.getCmp('resource_id').setValue("");
		Ext.getCmp('parent_code').setValue("");
		Ext.getCmp('resource_type').setValue("");
		Ext.getCmp('resource_code').setValue("");
		Ext.getCmp('resource_name').setValue("");
		Ext.getCmp('resource_url').setValue("");
		Ext.getCmp('is_show').setValue("");
		win.setTitle("资源添加");
		win.show();
	}

	function deleteDataForBatch() {
		var selModel = systemResourceGrid.getSelectionModel();
		if (selModel.hasSelection()) {
			var records = selModel.getSelections();
			var ids = "";
			for ( var i = 0; i < records.length; i++) {
				var id = records[i].get("resourceId");
				if (id && id != '' && id != null) {
					if (ids == "") {
						ids = ids + "" + id;
					} else {
						ids = ids + "," + id;
					}
				}
			}
			Ext.MessageBox
					.confirm(
							"提示",
							"您确定要删除所选数据吗？",
							function(btnId) {
								if (btnId == 'yes') {
									if (ids != "") {// 批量删除
										Ext.Ajax
												.request({
													waitMsg : '请稍等.....',
													url : basePath
															+ '/custom/systemresource/deleteByIds.spmvc',
													method : 'post',
													params : {
														ids : ids
													},
													success : function(response) {
														alertMsg("结果", "移除成功！");
														systemResourceStore
																.reload();
													},
													failure : function(
															response, options) {
														alert("失败！");
													}
												});
									}
								}
							});
		} else {
			alertMsg("提示", "请选择要批量删除的行!");
		}
	}

	function submitSystemResourceForm() {
		if (!systemResourceForm.getForm().isValid()) {
			alertMsg("验证", "请检查数据是否校验！");
			return;
		}
		var params = {};
		params["resourceId"] = getValueById("resource_id");
		params["parentCode"] = getValueById("parent_code");
		params["resourceType"] = getValueById("resource_type");
		params["resourceCode"] = getValueById("resource_code");
		params["resourceName"] = getValueById("resource_name");
		params["resourceUrl"] = getValueById("resource_url");
		params["isShow"] = getValueById("is_show");

		var myMask = new Ext.LoadMask(Ext.getBody(), {
			msg : '正在保存，请稍后！',
			removeMask : true
		// 完成后移除
		});
		myMask.show();

		Ext.Ajax.request({
			waitMsg : '请稍等.....',
			url : basePath + '/custom/systemresource/addOrSave.spmvc',
			params : params,
			method : "post",
			success : function(response) {
				myMask.hide();
				var json = Ext.util.JSON.decode(response.responseText);
				Ext.Msg.alert('结果', json.message);
				if(json.isok){
					Ext.getCmp("systemresource-list-grid").getStore().reload();
					win.hide();
				}
			},
			failure : function(response) {
				myMask.hide();
				var json = Ext.util.JSON.decode(response.responseText);
				errorMsg("结果", json.message);
				Ext.getCmp("systemresource-list-grid").getStore().reload();
			}
		});
	}

	function show() {
		couponPanel = new Ext.Panel({
			renderTo : 'systemresource-manager-grid',
			layout : 'column',
			items : [ systemResourceSearchForm, systemResourceGrid ]
		}).show();
		setResize();
	}

	show();
	function setResize() {
		var formHeight = systemResourceSearchForm.getHeight();
		var clientHeight = document.body.clientHeight;
		var clientWidth = document.body.clientWidth;
		systemResourceGrid.setHeight(clientHeight-formHeight-50);
		systemResourceGrid.setWidth(clientWidth-259);
	}
	window.onresize=function(){
		setResize();
	}

}
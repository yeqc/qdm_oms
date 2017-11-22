Ext.onReady(init);

function init() {

	Ext.QuickTips.init();

	Ext.form.Field.prototype.msgTarget = 'side';

	var record_start = 0;

	var modifyIndex = -1;

	var addOrSave = 'add';

	function generateGroupCode() {
		var groupCode = 'ASGP';
		var datatime = Ext.util.Format.date(new Date(), 'YmdHis');
		groupCode += (datatime + '0001');
		return groupCode;
	}

	var groupGoodsPanel = new Ext.Panel({
		frame : true,
		forceFit : true,
		autoScroll : false,
		autoWidth : true,
		autoHeight : true,
		labelAlign : "left",
		waitMsgTarget : true,
		bodyBorder : false,
		border : true,
		items : [ {
			layout : 'column',
			border : false,
			labelSeparator : '：',
			items : [ {
				columnWidth : .5,
				layout : 'form',
				items : [ {
					id : 'groupgoods.groupCode',
					xtype : 'textfield',
					maxLength : 11,
					width : 170,
					name : 'groupCode',
					fieldLabel : '套装商品编码',
					listeners : {
						"change" : function() {
							validateGroupCode();
						}
					}
				} ]
			}, {
				columnWidth : .5,
				layout : 'form',
				items : [ {
					id : 'groupgoods.price',
					regex : positive_regex,
					regexText : '非法数据',
					xtype : 'textfield',
					allowBlank : false,
					blankText : '套装商品价格不能为空',
					name : 'price',
					fieldLabel : '套装商品价格'
				} ]
			}, {
				columnWidth : .5,
				layout : 'form',
				items : [ {
					id : 'groupgoods.id',
					xtype : 'hidden',
					name : 'id'
				} ]
			} ]
		} ]
	});

	var MyRecord = Ext.data.Record.create([ {
		name : 'inedx'
	}, {
		name : 'goodsSn'
	}, {
		name : 'goodsCount'
	}, {
		name : 'goodsPrice'
	} ]);

	var record = Ext.data.Record.create([ {
		name : 'inedx'
	}, {
		name : 'goodsSn'
	}, {
		name : 'goodsCount'
	}, {
		name : 'goodsPrice'
	} ]);

	// Reader 读json中数据
	var reader = new Ext.data.JsonReader(record);

	// 定义数据集对象
	var dataStore = new Ext.data.Store({
		reader : reader
	});

	// 创建工具栏组件
	var toolbar = new Ext.Toolbar({
		items : [ "菜单栏：", "-", {
			text : '添加',
			iconCls : 'add',
			handler : openAddOneListWin
		}, {
			text : '删除',
			iconCls : 'delete',
			handler : deleteDataForBatch
		} ]
	});

	// 创建全选组件
	var cb = new Ext.grid.CheckboxSelectionModel();

	// 创建Grid表格组件
	var listDataGridPanel = new Ext.grid.GridPanel(
			{
				loadMask : true,
				height : 270,
				autoScroll : true,
				frame : true,
				tbar : toolbar,
				store : dataStore,
				autoScroll : true,
				viewConfig : {
					autoFill : true
				},
				sm : cb,
				columns : [// 配置表格列
				cb, {
					header : "序号",
					dataIndex : 'index',
					width : 10,
					renderer : function(value, metadata, record, rowIndex) {
						return record_start + 1 + rowIndex;
					}
				}, {
					header : "企业SKU编码",
					width : 50,
					dataIndex : 'goodsSn'
				}, {
					header : "数量",
					width : 50,
					dataIndex : 'goodsCount'
				}, {
					header : "商品原价",
					width : 60,
					dataIndex : 'goodsPrice'
				} ],
				listeners : {
					'rowdblclick' : function(grid, rowIndex, e) {
						modifyIndex = rowIndex;
						var record = grid.getSelectionModel().getSelected();
						Ext.getCmp('one.goodsSn').setValue(
								record.get('goodsSn'));
						Ext.getCmp('one.goodsCount').setValue(
								record.get('goodsCount'));
						Ext.getCmp('one.goodsPrice').setValue(
								record.get('goodsPrice'));
						win.show();
					}
				}

			});

	var groupgoods = new Ext.FormPanel({
		frame : true,
		forceFit : true,
		autoScroll : false,
		autoWidth : true,
		autoHeight : true,
		bodyBorder : false,
		applyTo : 'add_group_goods_panel',
		border : true,
		items : [ groupGoodsPanel, listDataGridPanel ],
		buttonAlign : "center",
		buttons : [ {
			text : '提交',
			handler : saveGroupGoods
		}, {
			text : '关闭',
			handler : function() {
				parent.FormEditWin.close();
			}
		} ]
	});

	var groupCodeValidateFlag = true;

	function validateGroupCode() {
		var groupCode = getValueById("groupgoods.groupCode");
		if (groupCode == '') {
			alertMsg("结果", "套装商品编码不能为空!");
			groupCodeValidateFlag = false;
			return;
		}
		if (groupCode.length > 11) {
			alertMsg("结果", "套装商品编码不能大于11位!");
			groupCodeValidateFlag = false;
			return;
		}
		var dataParams = {};
		dataParams['groupCode'] = groupCode;
		Ext.Ajax.request({
			waitMsg : '请稍等.....',
			url : basePath + '/custom/promotion/validateGroupCode.spmvc',
			params : dataParams,
			method : "post",
			success : function(response) {
				var json = Ext.util.JSON.decode(response.responseText);
				groupCodeValidateFlag = json.isok;
				if (!json.isok) {
					alertMsg("结果", json.message);
					Ext.getCmp("groupgoods.groupCode").focus();
				}
			},
			failure : function(response) {
				var json = Ext.util.JSON.decode(response.responseText);
				errorMsg("结果", json.message);
			}
		});
	}

	function saveGroupGoods() {
		if (!groupgoods.getForm().isValid()) {
			alertMsg("验证", "请检查数据是否校验！");
			return;
		}
		if('add' == addOrSave){
			validateGroupCode();
			if (!groupCodeValidateFlag) {
				return;
			}
		}
		var dataParams = {};
		dataParams['addOrSave'] = addOrSave;
		dataParams["id"] = getValueById("groupgoods.id");
		dataParams["groupCode"] = getValueById("groupgoods.groupCode");
		dataParams["price"] = getValueById("groupgoods.price");

		var i = 0;
		dataStore.each(function(rec) {
			var groupCode = 'groupGoodsListList[' + i + '].groupCode';
			var goodsSn = 'groupGoodsListList[' + i + '].goodsSn';
			var goodsCount = 'groupGoodsListList[' + i + '].goodsCount';
			var goodsPrice = 'groupGoodsListList[' + i + '].goodsPrice';
			dataParams[groupCode] = dataParams["groupCode"];
			dataParams[goodsSn] = rec.get("goodsSn");
			dataParams[goodsCount] = rec.get("goodsCount");
			dataParams[goodsPrice] = rec.get("goodsPrice");
			i++;
		});

		var myMask = new Ext.LoadMask(Ext.getBody(), {
			msg : '正在保存，请稍后！',
			removeMask : true
		// 完成后移除
		});
		myMask.show();

		Ext.Ajax
				.request({
					waitMsg : '请稍等.....',
					url : basePath
							+ '/custom/promotion/addOrSaveGroupGoodsList.spmvc',
					params : dataParams,
					method : "post",
					success : function(response) {
						myMask.hide();
						var json = Ext.util.JSON.decode(response.responseText);
						alertMsg("结果", json.message);
						parent.Ext.getCmp("group_goods_data_panel").getStore()
								.reload();
						parent.FormEditWin.close();

					},
					failure : function(response) {
						myMask.hide();
						var json = Ext.util.JSON.decode(response.responseText);
						errorMsg("结果", json.message);
					}
				});
	}

	initPage();

	function initPage() {
		if (groupGoods) {
			addOrSave = 'save';
			groupGoods = Ext.util.JSON.decode(groupGoods);
			Ext.getCmp("groupgoods.id").setValue(groupGoods.id);
			Ext.getCmp("groupgoods.groupCode").setValue(groupGoods.groupCode);
			Ext.getCmp("groupgoods.price").setValue(groupGoods.price);
			Ext.getCmp("groupgoods.groupCode").setDisabled(true);
		} else {
			// Ext.getCmp("groupgoods.groupCode").setValue(generateGroupCode());
		}
		if (groupGoodsListList) {
			var dataList = Ext.util.JSON.decode(groupGoodsListList);
			for ( var i = 0; i < dataList.length; i++) {
				var newRecord = new record({
					goodsSn : dataList[i].goodsSn,
					goodsCount : dataList[i].goodsCount,
					goodsPrice : dataList[i].goodsPrice
				});
				dataStore.add(newRecord);
			}
		}
	}

	var oneForm = new Ext.FormPanel({
		frame : true,
		items : [ {
			layout : 'column',
			border : false,
			labelSeparator : '：',
			items : [ {
				columnWidth : .5,
				layout : 'form',
				border : false,
				items : [ {
					id : 'one.goodsSn',
					xtype : 'textfield',
					width : 120,
					allowBlank : false,
					blankText : '企业SKU编码不能为空',
					name : 'goodsSn',
					anchor : '90%',
					fieldLabel : '企业SKU编码'
				} ]
			}, {
				columnWidth : .5,
				layout : 'form',
				border : false,
				items : [ {
					id : 'one.goodsCount',
					xtype : 'textfield',
					allowBlank : false,
					width : 120,
					blankText : '数量不能为空',
					regex : integer_regex,
					regexText : '非法数据',
					name : 'goodsCount ',
					fieldLabel : '数量',
					anchor : '90%'
				} ]
			}, {
				columnWidth : .5,
				layout : 'form',
				border : false,
				items : [ {
					id : 'one.goodsPrice',
					xtype : 'textfield',
					allowBlank : false,
					width : 120,
					blankText : '商品原价不能为空',
					regex : positive_regex,
					regexText : '非法数据',
					name : 'goodsPrice',
					fieldLabel : '商品原价',
					anchor : '90%'
				} ]
			} ]
		} ],
		buttonAlign : 'center',
		buttons : [ {
			text : '提交',
			handler : addOneList
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
		width : 600,
		closeAction : 'hide',
		height : 180,
		resizable : false,
		shadow : true,
		modal : true,
		closable : true,
		bodyStyle : 'padding:5 5 5 5',
		animCollapse : true,
		items : [ oneForm ]
	});

	// 显示新建档口用户窗口
	function openAddOneListWin() {
		oneForm.form.reset();
		win.setTitle("添加套装商品");
		win.show();
	}

	function deleteDataForBatch() {
		var recs = listDataGridPanel.getSelectionModel().getSelections();
		var num = recs.length;
		if (num == 0) {
			Ext.MessageBox.alert('提示', '请选择要移除的数据!');
			return;
		}
		Ext.MessageBox.confirm("提示", "您确定要删除所选数据吗？", function(btnId) {
			if (btnId == 'yes') {
				for ( var i = 0; i < recs.length; i++) {
					dataStore.remove(recs[i]);
				}
			}
		});
	}

	function addOneList() {
		if (!oneForm.getForm().isValid()) {
			alertMsg("验证", "请检查数据是否校验！");
			return;
		}
		if (modifyIndex == -1) {
			var newRecord = new MyRecord({
				goodsSn : getValueById("one.goodsSn"),
				goodsCount : getValueById("one.goodsCount"),
				goodsPrice : getValueById("one.goodsPrice")
			});
			dataStore.add(newRecord);
		} else {
			var record = dataStore.getAt(modifyIndex);
			record.set("goodsSn", getValueById("one.goodsSn"));
			record.set("goodsCount", getValueById("one.goodsCount"));
			record.set("goodsPrice", getValueById("one.goodsPrice"));
			modifyIndex = -1;
		}
		win.hide();
	}

};
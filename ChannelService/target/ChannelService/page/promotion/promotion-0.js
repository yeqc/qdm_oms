Ext.onReady(promotion);

/**
 * 统一渠道(满赠)
 */
function promotion() {
	Ext.QuickTips.init();

	Ext.form.Field.prototype.msgTarget = 'side';
	var uploadType = 0;
	var record_start = 0;

	var formPanel = new Ext.FormPanel({
		frame : true,
		forceFit : true,
		autoScroll : false,
		autoWidth : true,
		autoHeight : true,
		labelAlign : "left",
		waitMsgTarget : true,
		bodyBorder : false,
		applyTo : 'promotion_form',
		border : true,
		items : [ {
			layout : 'column',
			border : false,
			items : [ {
				columnWidth : .3,
				layout : 'form',
				border : false,
				items : [ {
					id : 'id',
					xtype : 'hidden',
					name : 'id'
				} ]
			}, {
				columnWidth : .3,
				layout : 'form',
				border : false,
				items : [ {
					id : 'promType',
					xtype : 'hidden',
					name : 'promType',
					value : '0'
				} ]
			}, {
				columnWidth : .3,
				layout : 'form',
				border : false,
				items : [ {
					id : 'promStatus',
					xtype : 'hidden',
					name : 'promStatus',
					value : '0'
				} ]
			}, {
				columnWidth : .3,
				layout : 'form',
				border : false,
				items : [ {
					id : 'promCode',
					xtype : 'textfield',
					name : 'promCode',
					anchor : '90%',
					readOnly : true,
					fieldLabel : '活动ID',
					allowBlank : false
				} ]
			}, {
				columnWidth : .3,
				layout : 'form',
				border : false,
				items : [ {
					id : 'manz_promotion_shopCode',
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
					anchor : '90%',
					allowBlank : false
				} ]
			}, {
				columnWidth : .3,
				layout : 'form',
				border : false,
				items : [ {
					fieldLabel : '活动开始时间',
					readOnly : true,
					allowBlank : false,
					html : '<input type="text" id="prom_beginTime" readonly="true" onClick="WdatePicker({startDate:\'%y-%M-%d 00:00:00\',dateFmt:\'yyyy-MM-dd HH:mm:ss\'})"/>'
				} ]
			}, {
				columnWidth : .3,
				layout : 'form',
				border : false,
				items : [ {
					fieldLabel : '活动结束时间',
					readOnly : true,
					allowBlank : false,
					html : '<input type="text" id="prom_endTime" readonly="true" onClick="WdatePicker({startDate:\'%y-%M-%d 23:59:59\',dateFmt:\'yyyy-MM-dd HH:mm:ss\'})"/>'
				} ]
			}, {
				columnWidth : .3,
				layout : 'form',
				border : false,
				items : [ {
					id : 'backup',
					xtype : 'textarea',
					fieldLabel : '活动备注',
					name : 'backup',
					width : 250,
					emptyText : '输入文本...'
				} ]
			} ]
		} ],
		buttonAlign : 'center',
		buttons : [ {
			id : 'clickSaveButton',
			text : '保存',
			handler : clickSaveButton
		}, {
			text : '关闭',
			handler : function() {
				parent.FormEditWin.close();
			}
		} ]
	});

	Ext.getCmp('manz_promotion_shopCode').setValue(parent.getGlobalValue('shopTitle'));
	
	function clickSaveButton() {
		if (!formPanel.getForm().isValid()) {
			alertMsg("验证", "请检查数据是否校验！");
			return;
		}

		var valid = true;
		var message = "";
		var prom_beginTime = $("#prom_beginTime").val();
		var prom_endTime = $("#prom_endTime").val();

		if (prom_beginTime == null || prom_beginTime == "") {
			valid = false;
			message = "活动开始时间";
		}

		if (prom_endTime == null || prom_endTime == "") {
			valid = false;
			if (message == null || message == "") {
				message = "活动结束时间";
			} else {
				message += ", 活动结束时间!";
			}
		}

		if (!valid) {
			alertMsg("验证", "请输入" + message);
			return;
		}
		var currentDateTime = new Date().format('Y-m-d H:i:s');
		if(!(prom_endTime > prom_beginTime && prom_endTime > currentDateTime)){
			alertMsg("验证", "请输入有效的活动时间!");
			return;
		}
		var params = {};
		params["id"] = getValueById("id");
		params["promType"] = getValueById("promType");
		params["promStatus"] = getValueById("promStatus");
		params["promCode"] = getValueById("promCode");
		params["shopCode"] = parent.getGlobalValue('shopCode');
		params["promTitle"] = getValueById("promTitle");
		params["beginTime"] = $("#prom_beginTime").val();
		params["endTime"] = $("#prom_endTime").val();
		params["backup"] = getValueById("backup");
		var i = 0;
//		dataStore.each(function(rec) {
//			var id = 'promotionsLimitMoneyList[' + i + '].id';
//			var promCode = 'promotionsLimitMoneyList[' + i + '].promCode';
//			var limitMoney = 'promotionsLimitMoneyList[' + i + '].limitMoney';
//			var giftsSkuSn = 'promotionsLimitMoneyList[' + i
//					+ '].giftsSkuSn';
//			var giftsGoodsCount = 'promotionsLimitMoneyList[' + i
//					+ '].giftsGoodsCount';
//			var giftsSkuCount = 'promotionsLimitMoneyList[' + i
//					+ '].giftsSkuCount';
//			params[id] = rec.get("id");
//			params[promCode] = params["promCode"];
//			params[limitMoney] = rec.get("limitMoney");
//			params[giftsSkuSn] = rec.get("giftsSkuSn");
//			params[giftsGoodsCount] = rec.get("giftsGoodsCount");
//			params[giftsSkuCount] = rec.get("giftsSkuCount");
//			i++;
//		});
		// Spring MVC 对象接受array对象最大下标为1000,传参时使用Json字符串传值
		var i = 0;
		params["limitMoneyGoodsInfo"] = "";
		dataStore.each(function(rec) {
			// id|promCode|promDetailsCode|limitMoney|giftsGoodsCount|giftsSkuSn|giftsSkuCount|giftsPriority
			var id = rec.get("id");
			var promDetailsCode = rec.get("promDetailsCode");
			var limitMoney = rec.get("limitMoney");
			var giftsGoodsCount = rec.get("giftsGoodsCount");
			var giftsSkuSn = rec.get("giftsSkuSn");
			var giftsSkuCount = rec.get("giftsSkuCount");
			var giftsPriority = rec.get("giftsPriority");
			if (promDetailsCode == null || promDetailsCode == undefined) {
				promDetailsCode = '';
			}
			if (id == undefined || id == null) {
				params["limitMoneyGoodsInfo"] += ":" + params["promCode"] + ":" + promDetailsCode 
					+ ":" + limitMoney + ":" + giftsGoodsCount + ":" + giftsSkuSn + ":" + giftsSkuCount
					+ ":" + giftsPriority + ";";
			} else {
				params["limitMoneyGoodsInfo"] += id + ":" + params["promCode"] + ":" + promDetailsCode 
				+ ":" + limitMoney + ":" + giftsGoodsCount + ":" + giftsSkuSn + ":" + giftsSkuCount
				+ ":" + giftsPriority + ";";
			}
			i++;
		});
		var j = 0;
		params["giftsGoodsInfo"] = "";
		giftsInfoDataStore.each(function(rec) {
			var id = rec.get("id");
			var goodsSn = rec.get("goodsSn");
			var giftsSum = rec.get("giftsSum");
			if (id == undefined || id == null) {
				params["giftsGoodsInfo"] += ":" + goodsSn + ":" + giftsSum +  ":" + params["promCode"] + ";";
			} else {
				params["giftsGoodsInfo"] += id + ":" + goodsSn + ":" + giftsSum + ":" + params["promCode"] + ";";
			}
			j++;
		});
		var myMask = new Ext.LoadMask(Ext.getBody(), {  
				msg: '正在保存，请稍后！',  
				removeMask: true //完成后移除  
			});  
		myMask.show();  

		Ext.Ajax.request({
			waitMsg : '请稍等.....',
			url : basePath + '/custom/promotion/addOrSave.spmvc',
			params : params,
			method : "post",
			success : function(response) {
				myMask.hide();
				var json = Ext.util.JSON.decode(response.responseText);
				alertMsg("结果", json.message);
				if(json.isok){
					parent.Ext.getCmp("promotion_list_gridpanel").getStore().reload();
					parent.FormEditWin.close();
				}
			},
			failure : function(response) {
				myMask.hide();
				var json = Ext.util.JSON.decode(response.responseText);
				errorMsg("结果", json.message);
			}
		});

	}

	var MyRecord = new Ext.data.Record.create([
//		{ name : 'inedx'},
		{ name : 'id' },
		{ name: 'limitMoney'},
		{ name: 'giftsSkuSn'},
		{ name: 'giftsGoodsCount'},
		{ name: 'giftsSkuCount'},
		{ name: 'promDetailsCode'},
		{ name: 'giftsPriority'}
	]);

	var record = Ext.data.Record.create([
//		{ name : 'inedx'},
		{ name : 'id' },
		{ name: 'limitMoney'},
		{ name: 'giftsSkuSn'},
		{ name: 'giftsGoodsCount'},
		{ name: 'giftsSkuCount'},
		{ name: 'promDetailsCode'},
		{ name: 'giftsPriority'}
	]);

	// Reader 读json中数据
//	var reader = new Ext.data.JsonReader({
//		root : 'root'
//	}, record);

	// 定义数据集对象
//	var dataStore = new Ext.data.Store({
//		reader : reader
//	});

	// shared reader
	var reader = new Ext.data.ArrayReader({idIndex: 0},record);
//	var dummyData = [
//		['199','123456', 10, 10],
//		['199','123457', 10, 10],
//		['299','123456', 10, 10],
//		['299','123458', 10, 10]
//	];
	var dataStore = new Ext.data.GroupingStore({
		reader: reader,
//		data: dummyData,
		sortInfo:{field: 'giftsSkuSn', direction: "ASC"},
		groupField:'limitMoney'
	});

	// 创建工具栏组件
	var toolbar = new Ext.Toolbar({
		items : [ "菜单栏：", "-", {
			id : 'addDataWinOpen',
			text : '添加细则',
			iconCls : 'add',
			handler : addDataWinOpen
		}, {
			id : 'deleteDataForBatch',
			text : '批量移除',
			iconCls : 'delete',
			handler : deleteDataForBatch
		}, {
			id : 'uploadDataWinOpen',
			text : '批量导入',
			iconCls : 'refresh',
			handler : uploadDataWinOpen
		} , 
			'<a style="color:red" href = "'+ basePath + '/page/promotion/promotion_limit_money.xls' + '">模板下载</a>'
		]
	});

	var tabPanel = new Ext.TabPanel({
		applyTo : 'data_panel',
		deferredRender : false,
		activeTab : '基本信息'
	});
	
	// 创建全选组件
	var cb = new Ext.grid.CheckboxSelectionModel();
	// 创建Grid表格组件
	var dataGridPanel = new Ext.grid.GridPanel({
		loadMask : true,
//		applyTo : 'data_panel',
		title : '满赠细则商品设置',
		height : 300,
		autoScroll : true,
		frame : true,
		tbar : toolbar,
		store : dataStore,
//		store : dataStore,
		autoScroll : true,
		viewConfig : {
			autoFill : true
		},
		sm : cb,
		view: new Ext.grid.GroupingView({
			forceFit:true,
			groupTextTpl: '满赠金额(大于等于)：{[values.rs[0].get("limitMoney")]}<br/>每单赠送总数量：{[values.rs[0].get("giftsGoodsCount")]}'
		}),
		columns : [ cb,
			{ header : "序号", dataIndex : 'index', width : 10, renderer : function( value, metadata, record, rowIndex ) {
				return record_start + 1 + rowIndex;
				}
			},
			{ header : "促销细则编码", hidden: true, width : 50, dataIndex : 'promDetailsCode' },
			{ header : "满赠金额(大于等于)", width : 50, dataIndex : 'limitMoney' },
			{ header : "赠送商品编码", width : 50, dataIndex : 'giftsSkuSn' },
			{ header : "每单每款赠送数量", width : 60, dataIndex : 'giftsSkuCount' },
			{ header : "赠送商品总数量", width : 50, hidden: true, dataIndex : 'giftsGoodsCount' },
			{ header : "是否随机赠", width : 50, dataIndex : 'giftsPriority',  renderer : 
				function( value ) {
					var msg = "";
					if (value == 0) { msg = "固定赠" } else { msg = "随机赠" }
					return msg;
				}
			}
		],
		listeners : {
			'rowdblclick' : function(grid, rowIndex, e) {
				var record = grid.getSelectionModel().getSelected();
				modifyIndex = rowIndex;
				tomodify(record);
			}
		}
	});
	tabPanel.add(dataGridPanel);
	tabPanel.setActiveTab(0);

	var modifyIndex = -1;

	function tomodify(record) {
		promotionForm.form.reset();
		promotionForm.isAdd = true;
		win.setTitle("修改细则");
		Ext.getCmp('limit_money_id').setValue(record.get('id'));
		Ext.getCmp('promDetailsCode').setValue(record.get('promDetailsCode'));
		Ext.getCmp('limitMoney').setValue(record.get('limitMoney')); 
		Ext.getCmp('giftsSkuSn').setValue(record.get('giftsSkuSn'));
		Ext.getCmp('giftsGoodsCount').setValue(record.get('giftsGoodsCount'));
		Ext.getCmp('giftsSkuCount').setValue(record.get('giftsSkuCount'));
		if (record.get('giftsPriority') == 0) {
			Ext.getCmp('giftsPriority').setValue(record.get('giftsPriority'));
		} else {
			Ext.getCmp('giftsPriority').setValue(record.get('giftsPriority'));
		}
		win.show();
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
				for ( var i = 0; i < recs.length; i++) {
					dataStore.remove(recs[i]);
				}
			}
		});
	}

	var promotionForm = new Ext.FormPanel({
		labelSeparator : "：",
		frame : true,
		border : false,
		items : [ {
			xtype : 'fieldset',
			columnWidth: 0.5,
			title: '细则设置',
			collapsible: true,
			autoHeight:true,
			defaults: {
				anchor: '-20' // leave room for error icon
			},
			defaultType: 'textfield',
			items :[{
				id : 'promDetailsCode',
				name : 'promDetailsCode',
				fieldLabel : '促销细则编码',
				xtype : 'hidden'
			} , {
				id : 'limitMoney',
				xtype: 'numberfield',
				allowBlank: false,
				minValue: 0,
				maxValue: 999999.99,
				decimalPrecision : 2,
				width : 120,
				blankText : '满赠金额不能为空',
				name : 'limitMoney',
				anchor : '60%',
				fieldLabel : '满赠金额(大于等于)'
			} , {
				id : 'giftsGoodsCount',
				xtype: 'numberfield',
				allowBlank: false,
				minValue: 1,
				maxValue: 99999,
				allowDecimals : false,
				width : 120,
				blankText : '每单赠送数量不能为空',
				name : 'giftsGoodsCount',
				fieldLabel : '每单赠送数量',
				anchor : '60%'
			} ]
		} , {
			xtype : 'fieldset',
			columnWidth: 0.5,
			title: '促销商品设置',
			collapsible: true,
			autoHeight:true,
			defaults: {
				anchor: '-20' // leave room for error icon
			},
			defaultType: 'textfield',
			items :[ {
				id : 'giftsSkuSn',
				xtype : 'textfield',
				allowBlank : false,
				width : 120,
				blankText : '商品编码不能为空',
				name : 'giftsSkuSn',
				fieldLabel : '赠送商品编码',
				anchor : '60%'
			} , {
				id : 'giftsSkuCount',
				xtype : 'textfield',
				allowBlank : false,
				width : 120,
				blankText : '每单每款赠送数量不能为空',
				regex: integer_regex,
				regexText : '非法数据',
				name : 'giftsSkuCount',
				fieldLabel : '每单每款赠送数量',
				anchor : '60%'
			} , {
				id: 'giftsPriority',
				xtype : 'radiogroup',
				name: "giftsPriority",
				margin: '4 1 1 1',
				fieldLabel: "是否随机赠",
				width: 200,
				anchor : '60%',
				items: [{
					boxLabel: '固定赠',
					name: 'giftsPriority',
					inputValue: '0',
					checked: true
				} , {
					boxLabel: '随机赠',
					name: 'giftsPriority',
					inputValue: '1'
				}]
			}]
		} , {
			id : 'limit_money_id',
			xtype : 'hidden'
		} ],
		buttonAlign : 'center',
		buttons : [ {
			id : 'submitForm',
			text : '提交',
			handler : submitForm
		}, {
			text : '关闭',
			handler : function() {
				win.hide();
			}
		} ]
	});

	// 创建赠品列表Form
	var giftsGoodsForm = new Ext.FormPanel({
		labelSeparator : "：",
		frame : true,
		border : false,
		items : [ {
			layout : 'column',
			border : false,
			labelSeparator : '：',
			items : [ {
				columnWidth : .5,
				layout : 'form',
				border : false,
				items : [ {
					id : 'gifts_id',
					xtype : 'hidden'
				} ]
			}, {
				columnWidth : .5,
				layout : 'form',
				border : false,
				items : [ {
					id : 'gifts_goods_sn',
					xtype : 'textfield',
					width : 120,
					allowBlank : false,
					blankText : '赠品编码不能为空',
					name : 'goodsSn',
					fieldLabel : '赠品编码'
				} ]
			}, {
				columnWidth : .5,
				layout : 'form',
				border : false,
				items : [ {
					id : 'giftsSum',
					xtype : 'textfield',
					width : 120,
					name : 'giftsSum',
					allowBlank : false,
					blankText : '赠送商品总数量不能为空',
					regex: integer_regex,
					regexText : '非法数据',
					fieldLabel : '赠送商品总数量'
				} ]
			} ]
		} ],
		buttonAlign : 'center',
		buttons : [ {
			text : '提交',
			handler : submitGiftsGoodsForm
		}, {
			text : '关闭',
			handler : function() {
				giftsGoodsWin.hide();
			}
		} ]
	});

	// 创建赠品列表窗口
	var giftsGoodsWin = new Ext.Window({
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
		items : [ giftsGoodsForm ]
	});

	// 赠品列表
	var giftsInfoNewRecord = Ext.data.Record.create([
		{ name : 'id' },
		{ name : 'inedx' },
		{ name : 'goodsSn' },
		{ name : 'giftsSum' }
	]);
	
	var giftsInfoRecord = Ext.data.Record.create([
		{ name : 'id' },
		{ name : 'inedx' },
		{ name : 'goodsSn' },
		{ name : 'giftsSum' }
	]);

	// Reader 读json中数据
	var giftsInfoReader = new Ext.data.JsonReader(giftsInfoRecord);

	// 定义数据集对象
	var giftsInfoDataStore = new Ext.data.Store({
		reader : giftsInfoReader
	});
	// 创建工具栏组件
	var giftsInfoToolbar = new Ext.Toolbar({
		items : [ "菜单栏：", "-", {
			text : '添加赠品',
			iconCls : 'add',
			handler : giftsGoodsWinOpen
		}, {
			text : '批量移除',
			iconCls : 'delete',
			handler : deleteGiftsGoodsForBatch
		}, {
			text : '批量导入',
			iconCls : 'refresh',
			handler : uploadGiftsGoodsDataWinOpen
		}, 
		'<a style="color:red" href = "'+ basePath + '/page/promotion/gifts_goods_list.xls' + '">模板下载</a>'
		 ,{
			text : '导出赠品',
			iconCls : 'refresh',
			handler : downloadGiftsGoods
		}
		]
	});
	// 创建全选组件
	var giftsInfocb = new Ext.grid.CheckboxSelectionModel();
	var giftsInfoStart = 0;
	// 创建Grid表格组件
	var giftsInfo = new Ext.grid.GridPanel({
		loadMask : true,
		title : '赠品列表',
		height : 300,
		autoScroll : true,
		frame : true,
		tbar : giftsInfoToolbar,
		store : giftsInfoDataStore,
		sm : giftsInfocb,
		columns : [ giftsInfocb, {
			header : "序号",
			dataIndex : 'index',
			width : 50,
			renderer : function(value, metadata, record, rowIndex) {
				return giftsInfoStart + 1 + rowIndex;
			}
		}, {
			header : "商品编码",
			width : 300,
			dataIndex : 'goodsSn'
		}, {
			header : "赠品初始数量",
			width : 300,
			dataIndex : 'giftsSum'
		} ],
		listeners : {
			'rowdblclick' : function(grid, rowIndex, e) {
				var record = giftsInfo.getSelectionModel().getSelected();
				modifyIndex = rowIndex;
				giftsGoodsForm.form.reset();
				giftsGoodsWin.setTitle("赠品修改");
				Ext.getCmp('gifts_id').setValue(record.get('id'));
				Ext.getCmp('gifts_goods_sn').setValue(record.get('goodsSn'));
				Ext.getCmp('giftsSum').setValue(record.get('giftsSum'));
				giftsGoodsWin.show();
			}
		}
	});
	tabPanel.add(giftsInfo);
	
	// 创建弹出窗口
	var win = new Ext.Window({
		layout : 'fit',
		width : 510,
		closeAction : 'hide',
		height : 340,
		resizable : false,
		shadow : true,
		modal : true,
		closable : true,
		bodyStyle : 'padding:5 5 5 5',
		animCollapse : true,
		items : [ promotionForm ]
	});
	// 显示新建档口用户窗口
	function addDataWinOpen() {
		promotionForm.form.reset();
		Ext.getCmp('limit_money_id').setValue('');
		Ext.getCmp('limitMoney').setValue('');
		Ext.getCmp('giftsSkuSn').setValue('');
		Ext.getCmp('giftsGoodsCount').setValue(1);
		Ext.getCmp('giftsSkuCount').setValue(1);
		promotionForm.isAdd = true;
		win.setTitle("添加细则");
		win.show();
	}

	function submitForm() {
		if (!promotionForm.getForm().isValid()) {
			alertMsg("验证", "请检查数据是否校验！");
			return;
		}
		var paramObj = promotionForm.getForm().getValues();
		if (modifyIndex == -1) {
			paramObj['limitMoney'] = parseFloat(paramObj.limitMoney);
			var newRecord = new MyRecord(paramObj);
			dataStore.each(function(record, i) {
				console.dir(record);
				var limitMoney = record.get("limitMoney");
				if (limitMoney != null && limitMoney == paramObj.limitMoney) {
					record.set("giftsGoodsCount", paramObj.giftsGoodsCount);
					newRecord.set("promDetailsCode", record.get("promDetailsCode"));
				}
			});
			dataStore.add(newRecord);
			dataGridPanel.view.refresh();
		} else {
			var record = dataStore.getAt(modifyIndex);
//			Ext.MessageBox.confirm("提示", "本次修改的"+paramObj.limitMoney + "满赠活动档每单赠送库存都相同！", function(btnId) {
//				if (btnId == 'yes') {
					dataStore.each(function(record, i) {
						var limitMoney = record.get("limitMoney");
						if (limitMoney != null && limitMoney == paramObj.limitMoney ) {
							record.set("giftsGoodsCount", paramObj.giftsGoodsCount);
						}
					});
					record.set("id", paramObj.limit_money_id);
					record.set("limitMoney", paramObj.limitMoney);
					record.set("giftsSkuSn", paramObj.giftsSkuSn);
					record.set("giftsGoodsCount", paramObj.giftsGoodsCount);
					record.set("giftsSkuCount", paramObj.giftsSkuCount);
					record.set("giftsPriority", paramObj.giftsPriority);
					record.set("promDetailsCode", paramObj.promDetailsCode);
					modifyIndex = -1;
					dataGridPanel.view.refresh();
//				}
//			});
		}
		win.hide();
	}

	function uploadDataWinOpen() {
		// 参数格式："key1:value1;key2:value2"
//		var params = "&params=promType:0";
		uploadType = 1
		var params = "&params=promType:0;dataType:1";
		var uploadUrl = basePath + '/custom/promotion/upload.spmvc' + params;
		FormEditWin.showAddDirWin("batch_add_detail_0", basePath
				+ "/fileUpload.html?type=1&fileTypes=*.xls&uploadUrl=" + uploadUrl,
				"picADWinID", "满赠细则导入", 580, 300);
	}

	doAfter = function(data) {
		var jsonData = Ext.util.JSON.decode(data);
		if (!jsonData.isok) {
			Ext.Msg.alert("提示", jsonData.message);
			return;
		}
//		addDataToStore(jsonData.data);
		if (uploadType == 1) {
			addDataToStore(jsonData.data);
		}
		if (uploadType == 2) {
			giftsGoodsAddDataToStore(jsonData.data);
		}
		FormEditWin.close();
		Ext.Msg.alert("提示", jsonData.message);
	};
	
	function addDataToStore(dataList) {
		for ( var i = 0; i < dataList.length; i++) {
			var newRecord = new MyRecord(dataList[i]);
			dataStore.add(newRecord);
		}
	}

	modifyinit();

	function modifyinit() {
		if (promotionsInfo) {
			promotionsInfo = Ext.util.JSON.decode(promotionsInfo);
			Ext.getCmp("id").setValue(promotionsInfo.id);
			Ext.getCmp("promType").setValue(promotionsInfo.promType);
			Ext.getCmp("promStatus").setValue(promotionsInfo.promStatus);
			Ext.getCmp("promCode").setValue(promotionsInfo.promCode);

			Ext.getCmp('manz_promotion_shopCode').setValue(parent.getGlobalValue('shopTitle'));

			Ext.getCmp("promTitle").setValue(promotionsInfo.promTitle);
//			Ext.getCmp("beginTime").setValue(new Date(promotionsInfo.beginTime));
//			Ext.getCmp("endTime").setValue(new Date(promotionsInfo.endTime));
			if(promotionsInfo.beginTime){
				$("#prom_beginTime").val(new Date(promotionsInfo.beginTime).Format("yyyy-MM-dd hh:mm:ss"));
			}
			if(promotionsInfo.endTime){
				$("#prom_endTime").val(new Date(promotionsInfo.endTime).Format("yyyy-MM-dd hh:mm:ss"));
			}
			Ext.getCmp("backup").setValue(promotionsInfo.backup);
			
		}else{
			var promotionCode = generatePromotionCode();
			Ext.getCmp("promCode").setValue(promotionCode);
		}
		if (promotionsLimitMoneyList) {
			promotionsLimitMoneyList = Ext.util.JSON
					.decode(promotionsLimitMoneyList);
			addDataToStore(promotionsLimitMoneyList);
		}
		// 赠品列表初始化加载展示
		if (giftsGoodsListList) {
			giftsGoodsListList = Ext.util.JSON.decode(giftsGoodsListList);
			giftsGoodsAddDataToStore(giftsGoodsListList);
		}
	}
	
	function generatePromotionCode() {
		var promotionCode = 'MANZ';
		var datatime = Ext.util.Format.date(new Date(), 'YmdHis');
		promotionCode += (datatime + '0001');
		return promotionCode;
	}
	
	/**
	 * 添加修改赠品信息
	 */
	function submitGiftsGoodsForm() {
		if (!giftsGoodsForm.getForm().isValid()) {
			alertMsg("验证", "请检查数据是否校验！");
			return;
		}
		if (modifyIndex == -1) {
			var newRecord = new giftsInfoNewRecord({
				id : getValueById('gifts_id'),
				goodsSn : getValueById('gifts_goods_sn'),
				giftsSum : getValueById('giftsSum')
			});
			giftsInfoDataStore.add(newRecord);
		} else {
			var record = giftsInfoDataStore.getAt(modifyIndex);
			record.set("id", getValueById("gifts_id"));
			record.set("goodsSn", getValueById("gifts_goods_sn"));
			record.set("giftsSum", getValueById("giftsSum"));
			modifyIndex = -1;
		}
		giftsGoodsWin.hide();
	}
	
	/**
	 * 开启添加修改赠品窗口
	 */
	function giftsGoodsWinOpen() {
		modifyIndex = -1;
		giftsGoodsForm.form.reset();
		Ext.getCmp('gifts_id').setValue('');
		Ext.getCmp('gifts_goods_sn').setValue('');
		Ext.getCmp('giftsSum').setValue('');
		giftsGoodsWin.setTitle("赠品添加");
		giftsGoodsWin.show();
	}

	/**
	 * 批量删除赠品
	 */
	function deleteGiftsGoodsForBatch() {
		var recs = giftsInfo.getSelectionModel().getSelections();
		var num = recs.length;
		if (num == 0) {
			Ext.MessageBox.alert('提示', '请选择要移除的数据!');
			return;
		}
		Ext.MessageBox.confirm("提示", "您确定要删除所选数据吗？", function(btnId) {
			if (btnId == 'yes') {
				for ( var i = 0; i < recs.length; i++) {
					giftsInfoDataStore.remove(recs[i]);
				}
			}
		});
	}

	/**
	 * 上传赠品模板
	 */
	function uploadGiftsGoodsDataWinOpen() {
		uploadType = 2;
		// 参数格式："key1:value1;key2:value2"
		var params = "&params=promType:0;dataType:2";
		var uploadUrl = basePath + '/custom/promotion/upload.spmvc' + params;
		FormEditWin.showAddDirWin("batch_add_detail_3_1", basePath
				+ "/fileUpload.html?type=1&fileTypes=*.xls&uploadUrl=" + uploadUrl,
				"picADWinID", "赠品导入", 580, 300);
	}
	/**
	 * 手工添加赠品写入列表中
	 * @param dataList
	 */
	function giftsGoodsAddDataToStore(dataList) {
		for ( var i = 0; i < dataList.length; i++) {
			var newRecord = new giftsInfoNewRecord({
				id : dataList[i].id,
				goodsSn : dataList[i].goodsSn,
				giftsSum : dataList[i].giftsSum
			});
			giftsInfoDataStore.add(newRecord);
		}
	}
	
	/**
	 * 导出赠品列表
	 */
	function downloadGiftsGoods(){
		var promCode = Ext.getCmp('promCode').getValue();
		if(promCode){
			var path = basePath + '/custom/promotion/exportGiftsGoods.spmvc?promCode='+promCode;
			window.location.href = path;
			alertMsg("结果", "导出赠品列表成功！");
		}
	}
}

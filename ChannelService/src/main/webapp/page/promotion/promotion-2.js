Ext.onReady(promotion);

/**
 * 统一渠道(买赠)
 */
function promotion() {
	Ext.QuickTips.init();

	Ext.form.Field.prototype.msgTarget = 'side';

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
					value : '2'
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
					id : 'maiz_promotion_shopCode',
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
			text : '保存',
			handler : clickSaveButton
		}, {
			text : '关闭',
			handler : function() {
				parent.FormEditWin.close();
			}
		} ]
	});
	
	Ext.getCmp('maiz_promotion_shopCode').setValue(parent.getGlobalValue('shopTitle'));

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
		// limitSnGoodsInfo
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
		params["limitSnGoodsInfo"] = "";
		dataStore.each(function(rec) {
			// id|promCode|limitGoodsSn|limitCount|giftsGoodsSn|giftsGoodsCount|giftsGoodsSum
			id = rec.get("id");
			promCode = params["promCode"];
			limitGoodsSn = rec.get("limitGoodsSn");
			limitCount = rec.get("limitCount");
			giftsGoodsSn = rec.get("giftsGoodsSn");
			giftsGoodsCount = rec.get("giftsGoodsCount");
			if (id == undefined || id == null) {
				params["limitSnGoodsInfo"] += ":" + promCode + ":" + limitGoodsSn 
					+ ":" + limitCount + ":" + giftsGoodsSn + ":" + giftsGoodsCount + ";";
			} else {
				params["limitSnGoodsInfo"] += id + ":" + promCode + ":" + limitGoodsSn 
				+ ":" + limitCount + ":" + giftsGoodsSn + ":" + giftsGoodsCount + ";";
			}
			i++;
		});
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
				};
			},
			failure : function(response) {
				myMask.hide();
				var json = Ext.util.JSON.decode(response.responseText);
				errorMsg("结果", json.message);
			}
		});

	}

	var MyRecord = new Ext.data.Record.create([ {
		name : 'inedx'
	}, {
		name : 'id'
	}, {
		name : 'limitGoodsSn'
	}, {
		name : 'limitCount'
	}, {
		name : 'giftsGoodsSn'
	}, {
		name : 'giftsGoodsCount'
	} ]);

	var record = Ext.data.Record.create([ {
		name : 'inedx'
	}, {
		name : 'id'
	}, {
		name : 'limitGoodsSn'
	}, {
		name : 'limitCount'
	}, {
		name : 'giftsGoodsSn'
	}, {
		name : 'giftsGoodsCount'
	} ]);

	// Reader 读json中数据
	var reader = new Ext.data.JsonReader({
		root : 'root'
	}, record);

	// 定义数据集对象
	var dataStore = new Ext.data.Store({
		reader : reader
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
		}, 
			'<a style="color:red" href = "'+ basePath + '/page/promotion/promotion_limit_sn.xls' + '">模板下载</a>'
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
		title: '买赠细则设置',
		height : 300,
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
			width : 10,
			dataIndex : 'index',
			renderer : function(value, metadata, record, rowIndex) {
				return record_start + 1 + rowIndex;
			}
		}, {
			header : "需购买SKU",
			width : 50,
			dataIndex : 'limitGoodsSn'
		}, {
			header : "需购买数量",
			width : 50,
			dataIndex : 'limitCount'
		}, {
			header : "赠送商品编码",
			width : 60,
			dataIndex : 'giftsGoodsSn'
		}, {
			header : "每单赠送数量",
			width : 50,
			dataIndex : 'giftsGoodsCount'
		}],
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
		Ext.getCmp('limit_sn_id').setValue(record.get('id'));
		Ext.getCmp('limitGoodsSn').setValue(record.get('limitGoodsSn'));
		Ext.getCmp('limitCount').setValue(record.get('limitCount'));
		Ext.getCmp('giftsGoodsSn').setValue(record.get('giftsGoodsSn'));
		Ext.getCmp('giftsGoodsCount').setValue(record.get('giftsGoodsCount'));
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
			id : 'limit_sn_id',
			name : 'id',
			xtype : 'hidden'
		} , { // 这里可以为多个Item，表现出来是该列的多行
			id : 'limitGoodsSn',
			xtype : 'textfield',
			allowBlank : false,
			name : 'limitGoodsSn',
			fieldLabel : '需购买SKU',
			width : 240
		} , {
			id : 'limitCount',
			xtype : 'numberfield',
			allowBlank : false,
			blankText : '需购买数量不能为空',
			minValue: 1,
			maxValue: 99999,
			allowDecimals : false,
			name : 'limitCount',
			fieldLabel : '需购买数量',
			width : 240
		} , {
			id : 'giftsGoodsSn',
			xtype : 'textfield',
			allowBlank : false,
			name : 'giftsGoodsSn',
			fieldLabel : '赠送商品编码',
			width : 240
		} , {
			id : 'giftsGoodsCount',
			xtype: 'numberfield',
			allowBlank: false,
			minValue: 1,
			maxValue: 99999,
			allowDecimals : false,
			width : 240,
			blankText : '每单赠送数量不能为空',
			name : 'giftsGoodsCount',
			fieldLabel : '每单赠送数量',
		}],
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

	// 创建弹出窗口
	var win = new Ext.Window({
		layout : 'fit',
		width : 480,
		closeAction : 'hide',
		height : 300,
		resizable : false,
		shadow : true,
		modal : true,
		closable : true,
		bodyStyle : 'padding:5 5 5 5',
		animCollapse : true,
		items : [ promotionForm ]
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
			header : "赠品总数量",
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
	// 显示新建档口用户窗口
	function addDataWinOpen() {
		modifyIndex = -1;
		promotionForm.form.reset();
		Ext.getCmp('limit_sn_id').setValue('');
		Ext.getCmp('limitGoodsSn').setValue('');
		Ext.getCmp('limitCount').setValue('');
		Ext.getCmp('giftsGoodsSn').setValue('');
		Ext.getCmp('giftsGoodsCount').setValue('');
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
			var newRecord = new MyRecord(paramObj);
			dataStore.add(newRecord);
		} else {
			var record = dataStore.getAt(modifyIndex);
			record.set("id", paramObj.id);
			record.set("limitGoodsSn", paramObj.limitGoodsSn);
			record.set("limitCount", paramObj.limitCount);
			record.set("giftsGoodsSn", paramObj.giftsGoodsSn);
			record.set("giftsGoodsCount", paramObj.giftsGoodsCount);
			modifyIndex = -1;
		}
		win.hide();
	}

	function uploadDataWinOpen() {
		uploadType = 1
		// 参数格式："key1:value1;key2:value2"
		var params = "&params=promType:2;dataType:1";
		var uploadUrl = basePath + '/custom/promotion/upload.spmvc' + params;
		FormEditWin.showAddDirWin("batch_add_detail_2", basePath
				+ "/fileUpload.html?type=1&fileTypes=*.xls&uploadUrl=" + uploadUrl,
				"picADWinID", "细则导入", 580, 300);

	}

	doAfter = function(data) {
		var jsonData = Ext.util.JSON.decode(data);
		if (!jsonData.isok) {
			Ext.Msg.alert("提示", jsonData.message);
			return;
		}
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
		// promotionslimitGoodsSnList = Ext.util.JSON.decode(data);
		for ( var i = 0; i < dataList.length; i++) {
			var newRecord = new MyRecord({
				id : dataList[i].id,
				limitGoodsSn : dataList[i].limitGoodsSn,
				limitCount : dataList[i].limitCount,
				giftsGoodsSn : dataList[i].giftsGoodsSn,
				giftsGoodsCount : dataList[i].giftsGoodsCount,
				giftsGoodsSum : dataList[i].giftsGoodsSum
			});
			dataStore.add(newRecord);
		}
	}
	;

	modifyinit();

	function modifyinit() {
		if (promotionsInfo) {
			promotionsInfo = Ext.util.JSON.decode(promotionsInfo);
			Ext.getCmp("id").setValue(promotionsInfo.id);
			Ext.getCmp("promType").setValue(promotionsInfo.promType);
			Ext.getCmp("promStatus").setValue(promotionsInfo.promStatus);
			Ext.getCmp("promCode").setValue(promotionsInfo.promCode);
			Ext.getCmp("promTitle").setValue(promotionsInfo.promTitle);

			Ext.getCmp('maiz_promotion_shopCode').setValue(parent.getGlobalValue('shopTitle'));

			if(promotionsInfo.beginTime){
				$("#prom_beginTime").val(new Date(promotionsInfo.beginTime).Format("yyyy-MM-dd hh:mm:ss"));
			}
			if(promotionsInfo.endTime){
				$("#prom_endTime").val(new Date(promotionsInfo.endTime).Format("yyyy-MM-dd hh:mm:ss"));
			}
			Ext.getCmp("backup").setValue(promotionsInfo.backup);
			
		} else {
			var promotionCode = generatePromotionCode();
			Ext.getCmp("promCode").setValue(promotionCode);
		}
		if (promotionsLimitSnList) {
			promotionsLimitSnList = Ext.util.JSON.decode(promotionsLimitSnList);
			addDataToStore(promotionsLimitSnList);
		}
		// 赠品列表初始化加载展示
		if (giftsGoodsListList) {
			giftsGoodsListList = Ext.util.JSON.decode(giftsGoodsListList);
			giftsGoodsAddDataToStore(giftsGoodsListList);
		}
	}
	
	function generatePromotionCode() {
		var promotionCode = 'MAIZ';
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
		var params = "&params=promType:2;dataType:2";
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

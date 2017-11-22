Ext.onReady(promotion);

/**
 * 统一渠道(集合赠)
 */
function promotion() {
	Ext.QuickTips.init();

	Ext.form.Field.prototype.msgTarget = 'side';

	// 区分上传类型(需购商品文件 1 , 赠品文件 2)
	var uploadType = 0;

	var modifyIndex = -1;

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
					value : '3'
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
					id : 'jihz_promotion_shopCode',
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
	
	Ext.getCmp('jihz_promotion_shopCode').setValue(parent.getGlobalValue('shopTitle'));

	function clickSaveButton() {
		var valid = true;
		valid = formPanel.getForm().isValid() && valid;
		valid = baseInfor.getForm().isValid() && valid;
		if (!valid) {
			alertMsg("验证", "请检查数据是否校验!");
			tabPanel.setActiveTab(0);
			return;
		}

		valid = true;
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

		params["promotionsListInfor.id"] = getValueById("promotion_list_infor_id");
		params["promotionsListInfor.promCode"] = params["promCode"];
		params["promotionsListInfor.goodsCount"] = getValueById("goodsCount");
		params["promotionsListInfor.limitMoney"] = getValueById("limitMoney");
		params["promotionsListInfor.giftsCount"] = getValueById("giftsCount");

		var i = 0;
		params["promotionsGoodsInfo"] = "";
		listGoodsDataStore.each(function(rec) {
			//var id = 'promotionsListGoodsList[' + i + '].id';
			//var promCode = 'promotionsListGoodsList[' + i + '].promCode';
			//var goodsSn = 'promotionsListGoodsList[' + i + '].goodsSn';
			//params[id] = rec.get("id");
			//params[promCode] = params["promCode"];
			//params[goodsSn] = rec.get("goodsSn");
			var goodsSn = rec.get("goodsSn");
			var id = rec.get("id");
			if (id == undefined || id == null) {
				params["promotionsGoodsInfo"] += ":" + goodsSn + ":" + params["promCode"] + ";";
			} else {
				params["promotionsGoodsInfo"] += id + ":" + goodsSn + ":" + params["promCode"] + ";";
			}
			i++;
		});
		var j = 0;
		params["giftsGoodsInfo"] = "";
		giftsInfoDataStore.each(function(rec) {
			//var id = 'giftsGoodsListList[' + j + '].id';
			//var promCode = 'giftsGoodsListList[' + j + '].promCode';
			//var goodsSn = 'giftsGoodsListList[' + j + '].goodsSn';
			//var giftsSum = 'giftsGoodsListList[' + j + '].giftsSum';
			//params[id] = rec.get("id");
			//params[promCode] = params["promCode"];
			//params[goodsSn] = rec.get("goodsSn");
			//params[giftsSum] = rec.get("giftsSum");
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
				};
			},
			failure : function(response, status, op) {
				myMask.hide();
				var json = Ext.util.JSON.decode(response.responseText);
				errorMsg("结果", json.message);
			}
		});
	}

	var tabPanel = new Ext.TabPanel({
		applyTo : 'data_panel',
		deferredRender : false,
		activeTab : '基本信息'
	});

	var baseInfor = new Ext.FormPanel({

		title : '基本信息',
		frame : true,
		forceFit : true,
		autoScroll : false,
		height : 300,
		labelAlign : "left",
		waitMsgTarget : true,
		bodyBorder : false,
		border : true,
		items : [ {
			layout : 'column',
			border : false,
			items : [{
				columnWidth : .3,
				layout : 'form',
				border : false,
				items : [ {
					id : 'goodsCount',
					xtype : 'textfield',
					fieldLabel : '需购商品数量',
					name : 'goodsCount',
					allowBlank : false,
					blankText : '需购商品数量不能为空',
					regex: integer_regex,
					regexText : '非法数据'
				} ]
			}, {
				columnWidth : .3,
				layout : 'form',
				border : false,
				items : [ {
					id : 'limitMoney',
					xtype : 'textfield',
					fieldLabel : '需购商品金额',
					name : 'limitMoney',
					allowBlank : false,
					blankText : '需购商品金额不能为空',
					regex: positive_regex,
					regexText : '非法数据'
				} ]
			}, {
				columnWidth : .3,
				layout : 'form',
				border : false,
				items : [ {
					id : 'giftsCount',
					xtype : 'textfield',
					fieldLabel : '每单赠品数量',
					name : 'giftsCount',
					allowBlank : false,
					blankText : '每单赠品数量不能为空',
					regex: integer_regex,
					regexText : '非法数据'
				} ]
			}, {
				columnWidth : .3,
				layout : 'form',
				border : false,
				items : [ {
					id : 'promotion_list_infor_id',
					xtype : 'hidden',
					name : 'id'
				} ]
			} ]
		} ]
	});
	tabPanel.add(baseInfor);
	tabPanel.setActiveTab(0);

	var listGoodsForm = new Ext.FormPanel({
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
					id : 'list_goods_id',
					xtype : 'hidden'
				} ]
			}, {
				columnWidth : .5,
				layout : 'form',
				border : false,
				items : [ {
					id : 'list_goods_sn',
					xtype : 'textfield',
					width : 170,
					allowBlank : false,
					blankText : '需购商品编码不能为空',
					name : 'goodsSn',
					fieldLabel : '需购商品编码',
					anchor : '90%'
				} ]
			} ]
		} ],
		buttonAlign : 'center',
		buttons : [ {
			text : '提交',
			handler : submitListGoodsForm
		}, {
			text : '关闭',
			handler : function() {
				listGoodsWin.hide();
			}
		} ]
	});

	function submitListGoodsForm() {
		if (!listGoodsForm.getForm().isValid()) {
			alertMsg("验证", "请检查数据是否校验！");
			return;
		}

		if (modifyIndex == -1) {
			var newRecord = new listGoodsNewRecord({
				id : getValueById('list_goods_id'),
				goodsSn : getValueById('list_goods_sn')
			});
			listGoodsDataStore.add(newRecord);
		} else {
			var record = listGoodsDataStore.getAt(modifyIndex);
			record.set("id", getValueById("list_goods_id"));
			record.set("goodsSn", getValueById("list_goods_sn"));
			modifyIndex = -1;
		}
		listGoodsWin.hide();
	}

	// 创建弹出窗口
	var listGoodsWin = new Ext.Window({
		layout : 'fit',
		width : 600,
		closeAction : 'hide',
		height : 120,
		resizable : false,
		shadow : true,
		modal : true,
		closable : true,
		bodyStyle : 'padding:5 5 5 5',
		animCollapse : true,
		items : [ listGoodsForm ]
	});

	var listGoodsNewRecord = Ext.data.Record.create([ {
		name : 'id'
	}, {
		name : 'inedx'
	}, {
		name : 'goodsSn'
	} ]);

	var listGoodsRecord = Ext.data.Record.create([ {
		name : 'id'
	}, {
		name : 'inedx'
	}, {
		name : 'goodsSn'
	} ]);

	// Reader 读json中数据
	var listGoodsReader = new Ext.data.JsonReader(listGoodsRecord);

	// 定义数据集对象
	var listGoodsDataStore = new Ext.data.Store({
		reader : listGoodsReader
	});

	// 创建工具栏组件
	var listGoodsToolbar = new Ext.Toolbar({
		items : [ "菜单栏：", "-", {
			text : '添加商品',
			iconCls : 'add',
			handler : listGoodsWinOpen
		}, {
			text : '批量移除',
			iconCls : 'delete',
			handler : deleteListGoodsForBatch
		}, {
			text : '批量导入',
			iconCls : 'refresh',
			handler : uploadListGoodsDataWinOpen
		}, 
			'<a style="color:red" href = "'+ basePath + '/page/promotion/promoton_list_goods.xls' + '">模板下载</a>'
		]
	});

	function listGoodsWinOpen() {
		listGoodsForm.form.reset();
		Ext.getCmp('list_goods_id').setValue('');
		Ext.getCmp('list_goods_sn').setValue('');
		listGoodsWin.setTitle("需购商品添加");
		listGoodsWin.show();
	}

	function deleteListGoodsForBatch() {
		var recs = listGoods.getSelectionModel().getSelections();
		var num = recs.length;
		if (num == 0) {
			Ext.MessageBox.alert('提示', '请选择要移除的数据!');
			return;
		}
		Ext.MessageBox.confirm("提示", "您确定要删除所选数据吗？", function(btnId) {
			if (btnId == 'yes') {
				for ( var i = 0; i < recs.length; i++) {
					listGoodsDataStore.remove(recs[i]);
				}
			}
		});
	}

	function uploadListGoodsDataWinOpen() {
		uploadType = 1;
		// 参数格式："key1:value1;key2:value2"
		var params = "&params=promType:3;dataType:1";
		var uploadUrl = basePath + '/custom/promotion/upload.spmvc' + params;
		FormEditWin.showAddDirWin("batch_add_detail_3_0", basePath
				+ "/fileUpload.html?type=1&fileTypes=*.xls&uploadUrl=" + uploadUrl,
				"picADWinID", "赠品导入", 580, 300);
	}

	// 创建全选组件
	var listGoodscb = new Ext.grid.CheckboxSelectionModel();

	var listGoodsStart = 0;

	// 创建Grid表格组件
	var listGoods = new Ext.grid.GridPanel({
		loadMask : true,
		title : '需购商品列表',
		height : 300,
		autoScroll : true,
		frame : true,
		tbar : listGoodsToolbar,
		store : listGoodsDataStore,
		sm : listGoodscb,
		columns : [ listGoodscb, {
			header : "序号",
			dataIndex : 'index',
			width : 50,
			renderer : function(value, metadata, record, rowIndex) {
				return listGoodsStart + 1 + rowIndex;
			}
		}, {
			header : "商品编码",
			width : 300,
			dataIndex : 'goodsSn'
		} ],
		listeners : {
			'rowdblclick' : function(grid, rowIndex, e) {
				var record = listGoods.getSelectionModel().getSelected();
				modifyIndex = rowIndex;
				listGoodsForm.form.reset();
				listGoodsWin.setTitle("需购商品修改");
				Ext.getCmp('list_goods_id').setValue(record.get('id'));
				Ext.getCmp('list_goods_sn').setValue(record.get('goodsSn'));
				listGoodsWin.show();
			}
		}
	});

	tabPanel.add(listGoods);

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

	// 创建弹出窗口
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

	var giftsInfoNewRecord = Ext.data.Record.create([ {
		name : 'id'
	}, {
		name : 'inedx'
	}, {
		name : 'goodsSn'
	}, {
		name : 'giftsSum'
	} ]);

	var giftsInfoRecord = Ext.data.Record.create([ {
		name : 'id'
	}, {
		name : 'inedx'
	}, {
		name : 'goodsSn'
	}, {
		name : 'giftsSum'
	} ]);

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

	function giftsGoodsWinOpen() {
		giftsGoodsForm.form.reset();
		Ext.getCmp('gifts_id').setValue('');
		Ext.getCmp('gifts_goods_sn').setValue('');
		Ext.getCmp('giftsSum').setValue('');
		giftsGoodsWin.setTitle("赠品添加");
		giftsGoodsWin.show();
	}

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

	function uploadGiftsGoodsDataWinOpen() {
		uploadType = 2;
		// 参数格式："key1:value1;key2:value2"
		var params = "&params=promType:3;dataType:2";
		var uploadUrl = basePath + '/custom/promotion/upload.spmvc' + params;
		FormEditWin.showAddDirWin("batch_add_detail_3_1", basePath
				+ "/fileUpload.html?type=1&fileTypes=*.xls&uploadUrl=" + uploadUrl,
				"picADWinID", "赠品导入", 580, 300);
	}

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

	doAfter = function(data) {
		var jsonData = Ext.util.JSON.decode(data);
		if (!jsonData.isok) {
			Ext.Msg.alert("提示", jsonData.message);
			return;
		}
		if (uploadType == 1) {
			listGoodsAddDataToStore(jsonData.data);
		}
		if (uploadType == 2) {
			giftsGoodsAddDataToStore(jsonData.data);
		}
		FormEditWin.close();
		Ext.Msg.alert("提示", jsonData.message);
	};

	function listGoodsAddDataToStore(dataList) {
		for ( var i = 0; i < dataList.length; i++) {
			var newRecord = new listGoodsNewRecord({
				id : dataList[i].id,
				goodsSn : dataList[i].goodsSn
			});
			listGoodsDataStore.add(newRecord);
		}
	}

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

	tabPanel.add(giftsInfo);

	modifyinit();

	function modifyinit() {
		if (promotionsInfo) {
			promotionsInfo = Ext.util.JSON.decode(promotionsInfo);
			Ext.getCmp("id").setValue(promotionsInfo.id);
			Ext.getCmp("promType").setValue(promotionsInfo.promType);
			Ext.getCmp("promStatus").setValue(promotionsInfo.promStatus);
			Ext.getCmp("promCode").setValue(promotionsInfo.promCode);
			Ext.getCmp("promTitle").setValue(promotionsInfo.promTitle);

			Ext.getCmp('jihz_promotion_shopCode').setValue(parent.getGlobalValue('shopTitle'));
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

		if (promotionsListInfor) {
			promotionsListInfor = Ext.util.JSON.decode(promotionsListInfor);
			Ext.getCmp("promotion_list_infor_id").setValue(
					promotionsListInfor.id);
			Ext.getCmp("goodsCount").setValue(promotionsListInfor.goodsCount);
			Ext.getCmp("limitMoney").setValue(promotionsListInfor.limitMoney);
			Ext.getCmp("giftsCount").setValue(promotionsListInfor.giftsCount);
		}

		if (promotionsListGoodsList) {
			promotionsListGoodsList = Ext.util.JSON
					.decode(promotionsListGoodsList);
			listGoodsAddDataToStore(promotionsListGoodsList);
		}

		if (giftsGoodsListList) {
			giftsGoodsListList = Ext.util.JSON.decode(giftsGoodsListList);
			giftsGoodsAddDataToStore(giftsGoodsListList);
		}
	}
	
	function generatePromotionCode() {
		var promotionCode = 'JIHZ';
		var datatime = Ext.util.Format.date(new Date(), 'YmdHis');
		promotionCode += (datatime + '0001');
		return promotionCode;
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

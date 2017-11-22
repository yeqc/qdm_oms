Ext.onReady(init);

function init() {
	listPage();
};

function listPage() {

	var record_start = 0;
	var pageSize = 15;
	var params = {};

	params["start"] = 0;
	params["limit"] = pageSize;

	// 经营店铺选择框
	var shopSelectOption = new Ext.form.ComboBox({
		id : 'channelstocklog.shopCode',
		store : new Ext.data.Store({
			proxy : new Ext.data.HttpProxy({
				url : basePath + '/custom/promotion/channelshoplist.spmvc',
				method : 'GET'
			}),
			reader : new Ext.data.JsonReader({
				fields : [ 'shopCode', 'shopTitle' ]
			})
		}),
		xtype : 'combo',
		valueField : 'shopCode',
		displayField : 'shopTitle',
		forceSelection : true,
		emptyText : '请选择经营店铺',
		mode : 'remote',
		editable : false,
		triggerAction : 'all',
		fieldLabel : '经营店铺'
	});

	// 渠道（调整类型）选择框
	var channelSelectOption = new Ext.form.ComboBox({
		id : 'channelstocklog.channelCode',
		store : new Ext.data.Store({
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
		blankText : '请选择渠道',
		emptyText : '渠道',
		name : 'channelCode',
		editable : false,
		triggerAction : 'all',
		fieldLabel : '渠道'
	});
	
	var returnCodeTypeStore = new Ext.data.SimpleStore(
			{
				fields : [ 'value', 'text' ],
				data : [
					[ '1', '执行成功' ], [ '0', '执行失败' ]
				]
			});

	var returnCodeSelectOption = new Ext.form.ComboBox({
		id : 'channelstocklog.returnCode',
		store : returnCodeTypeStore,
		xtype : 'combo',
		valueField : 'value',
		displayField : 'text',
		mode : 'local',
		forceSelection : true,
		blankText : '请执行结果',
		emptyText : '执行结果',
		name : 'returnCode',
		editable : false,
		triggerAction : 'all',
		fieldLabel : '执行结果'
	});



	var logSearchForm = new Ext.FormPanel({
		applyTo : 'stocklog_search_panel',
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
				items : [ channelSelectOption ]
			}, {
				columnWidth : .3,
				layout : 'form',
				border : false,
				items : [ shopSelectOption ]
			},{
				columnWidth : .3,
				layout : 'form',
				border : false,
				items : [ {
					id : 'channelstocklog.skuSn',
					xtype : 'textfield',
					name : 'sku',
					fieldLabel : '商品6或11码',
					anchor : '90%'
				} ]
			} , 
			{
				
				columnWidth : .3,
				layout : 'form',
				border : false,
				items : [ {
					readOnly : true,
					fieldLabel : '开始时间',
					anchor : '90%',
					html : '<input type="text" id="stock_beginTime" readonly="true" onClick="WdatePicker({dateFmt:\'yyyy-MM-dd HH:mm:ss\'})"/>'
				} ]
				
			},{
				columnWidth : .3,
				layout : 'form',
				border : false,
				items : [ {
					fieldLabel : '结束时间',
					anchor : '90%',
					readOnly : true,
					html : '<input type="text" id="stock_endTime" readonly="true" onClick="WdatePicker({dateFmt:\'yyyy-MM-dd HH:mm:ss\'})"/>'
				} ]
			},{
				columnWidth : .3,
				layout : 'form',
				border : false,
				items : [returnCodeSelectOption]
			}]
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

	var record = Ext.data.Record.create([ {
		name : 'channelTitle'
	}, {
		name : 'shopTitle'
	},  {
		name : 'skuSn'
	}, {
		name : 'returnMessage'
	}, {
		name : 'formatRequestTime'
	}, {
		name : 'returnCode'
	}, {
		name : 'user'
	}, {
		name : 'stock'
	}
	]);

	var proxy = new Ext.data.HttpProxy({
		url : basePath + '/custom/log/searchStockLog.spmvc',
		method : "post"
	});

	// Reader 读json中数据
	var reader = new Ext.data.JsonReader({
		root : 'root',
		totalProperty : 'totalProperty'
	}, record);

	// 定义数据集对象
	var dataStore = new Ext.data.Store({
		autoLoad : false,
		proxy : proxy,
		reader : reader
	});
	

	
	var myMask = new Ext.LoadMask(Ext.getBody(), {msg:"请稍候..."}); 
	tbar = [
			{
				text : '导出日志',
				tooltip : '导出日志',
				iconCls : 'add',
				handler : function() {
					
					var channelCode = getValueById("channelstocklog.channelCode");
					var shopCode = getValueById('channelstocklog.shopCode');
					var skuSn= getValueById('channelstocklog.skuSn');
					var returnCode = getValueById('channelstocklog.returnCode');
					var beginTime = $("#stock_beginTime").val();
					var endTime = $("#stock_endTime").val();
					var path = basePath + '/custom/log/exportStockLog.spmvc';
				    myMask.show(); 
					Ext.Ajax.request( {  
						waitMsg : '请稍等.....',
						url :path,
						method : 'post',
						timeout:1800000,
						//method : 'post',
						params : {
							shopCode:shopCode,
							channelCode:channelCode,
							skuSn:skuSn,
							returnCode:returnCode,
							beginTime:beginTime,
							endTime:endTime
						}, 
						success : function(response) {
							myMask.hide(); 	
							var respText = Ext.util.JSON.decode(response.responseText);
							
							if(respText.isok){
								
								// window.open(basePath+"/page/log/exportLog/"+respText.message);
								 myMask.hide(); 	
								 window.location.href= basePath + '/custom/log/downloadFile.spmvc?path='+respText.message;
								// Ext.getCmp('ShopBusinessgoodsBtn').setDisabled(false); 
					
								 alertMsg("验证","导出成功！");
							}else{
								myMask.hide();
								alertMsg(respText.message);
							}		
							
						},
						failure : function(response) {
							   myMask.hide(); 	
					
                            alertMsg("验证","失败");
						}
				    });
					
		
					
				}
				
			}
	];
	

	// 创建Grid表格组件
	var logDataGridPanel = new Ext.grid.GridPanel(
			{
				title : '库存同步日志列表',
				loadMask : true,
				applyTo : 'stocklog_data_panel',
				frame : true,
				store : dataStore,
				animCollapse : false,
				columnLines : true,
				tbar : tbar,
				autoSizeColumns : true,
				// autoExpandColumn: "returnMessage", //自动伸展，占满剩余区域
				viewConfig : {
					autoFill : true
				},
				bbar : new IssPagingToolbar(dataStore, pageSize),
				columns : [ {
					header : "序号",
					dataIndex : 'index',
					width : 10,
					renderer : function(value, metadata, record, rowIndex) {
						return record_start + 1 + rowIndex;
					}
				}, {
					header : "渠道",
					width : 30,
					dataIndex : 'channelTitle'
				}, {
					header : "店铺",
					width : 55,
					dataIndex : 'shopTitle'
				}, {
					header : "操作人",
					width : 40,
					dataIndex : 'user'
				}, {
					header : "11位商品码",
					width : 40,
					dataIndex : 'skuSn'
				},{
					header : "库存量",
					width : 25,
					dataIndex : 'stock'
				},
				{
					header : "执行结果",
					dataIndex : 'returnCode',
					width : 40,
					align : "center",
					renderer:function(v){
						var msg=""; 
						if(v=='1'){ msg="<font color='green'>执行成功</font>";}
						else { msg="<font color='red'>执行失败</font>"; }
						return msg;
					}
				}, {
					header : "执行时间",
					width : 48,
					dataIndex : 'formatRequestTime'
				}, {
					header : "执行信息",
					dataIndex : 'returnMessage'
				} ],
				listeners : {
					'rowdblclick' : function(grid, rowIndex, e) {
						var record = logDataGridPanel.getSelectionModel()
								.getSelected();
						var returnMessage = record.get("returnMessage");
						Ext.getCmp("channelstocklog.resultMessage").setValue(
								returnMessage);
						win.show();
					}
				}
			});

	logDataGridPanel.store.on('beforeload', function() {
		Ext.apply(this.baseParams, params);
	});

	function clickSearchButton() {
		params["channelCode"] = getValueById("channelstocklog.channelCode");
		params["shopCode"] = getValueById('channelstocklog.shopCode');
		params["skuSn"] = getValueById('channelstocklog.skuSn');
		params["returnCode"] = getValueById('channelstocklog.returnCode');
		params["beginTime"] = $("#stock_beginTime").val();
		params["endTime"] = $("#stock_endTime").val();
		if (params.channelCode == '' && params.shopCode == '' 
			&& params.skuSn == '' && params.returnCode == '' 
			&& params.beginTime == '' && params.endTime == '' ) {
			alertMsg("提示", "请选择查询条件再查询！");
			return;
		}
		logDataGridPanel.store.load({
			params : params
		});
	}

	function clickResetButton() {
		logSearchForm.form.reset();
		$("#stock_beginTime").val("");
		$("#stock_endTime").val("");
	}

	// 创建弹出窗口
	var win = new Ext.Window({
		title : "执行结果",
		layout : 'fit',
		closeAction : 'hide',
		resizable : false,
		width : 600,
		height : 180,
		shadow : true,
		modal : true,
		closable : true,
		bodyStyle : 'padding:5 5 5 5',
		animCollapse : true,
		items : [ {
			id : 'channelstocklog.resultMessage',
			xtype : 'textarea',
			readOnly : true,
			width : 575,
			height : 158
		} ]
	});

	function setResize() {
		var formHeight = logSearchForm.getHeight();
		var clientHeight = document.body.clientHeight;
		var clientWidth = document.body.clientWidth;
		logDataGridPanel.setHeight(clientHeight - formHeight - 61);
		logDataGridPanel.setWidth(clientWidth - 260);
	}
	window.onresize = function() {
		setResize();
	};
	setResize();
};


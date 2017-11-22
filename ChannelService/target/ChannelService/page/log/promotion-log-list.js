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
		id : 'promotionslog.shopCode',
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

	var promotionTypeStore = new Ext.data.SimpleStore(
			{
				fields : [ 'value', 'text' ],
				data : [
					[ '0', '满赠' ], [ '2', '买赠' ], [ '3', '集合赠' ]
				]
			});

	// 渠道（调整类型）选择框
	var promotionTypeSelectOption = new Ext.form.ComboBox({
		id : 'promotionslog.promType',
		store : promotionTypeStore,
		xtype : 'combo',
		valueField : 'value',
		displayField : 'text',
		mode : 'local',
		forceSelection : true,
		blankText : '请选择促销类型',
		emptyText : '促销类型',
		name : 'promType',
		editable : false,
		triggerAction : 'all',
		fieldLabel : '促销类型'
	});

	var logSearchForm = new Ext.FormPanel({
		applyTo : 'promotion_log_search_panel',
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
				items : [ shopSelectOption ]
			}, {
				columnWidth : .3,
				layout : 'form',
				border : false,
				items : [ promotionTypeSelectOption ]
			}, {
				columnWidth : .3,
				layout : 'form',
				border : false,
				items : [ {
					id : 'promotionslog.promCode',
					xtype : 'textfield',
					name : 'promCode',
					fieldLabel : '促销编号',
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

	var record = Ext.data.Record.create([ {
		name : 'shopTitle'
	}, {
		name : 'promCode'
	}, {
		name : 'promType'
	}, {
		name : 'userId'
	}, {
		name : 'formatAddTime'
	}, {
		name : 'message'
	} ]);

	var proxy = new Ext.data.HttpProxy({
		url : basePath + '/custom/log/searchPromotionsLog.spmvc',
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
					var shopCode = Ext.getCmp("promotionslog.shopCode").getValue();
					var promType = Ext.getCmp("promotionslog.promType").getValue();	
					var promCode = Ext.getCmp("promotionslog.promCode").getValue();
					 
					var path = basePath + '/custom/log/exportPromotionsLog.spmvc';
				    myMask.show(); 
					Ext.Ajax.request( {  
						waitMsg : '请稍等.....',
						url :path,
						method : 'post',
						timeout:1800000,
						//method : 'post',
						params : {
							shopCode:shopCode,
							promType:promType,
							promCode:promCode		
						}, 
						success : function(response) {
							myMask.hide(); 	
							var respText = Ext.util.JSON.decode(response.responseText);
							
							if(respText.isok){
								 myMask.hide(); 	
								 window.location.href= basePath + '/custom/log/downloadFile.spmvc?path='+respText.message;
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
				title : '促销操作日志列表',
				loadMask : true,
				applyTo : 'promotion_log_data_panel',
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
					header : "店铺",
					width : 60,
					dataIndex : 'shopTitle'
				}, {
					header : "促销类型",
					width : 20,
					dataIndex : 'promType',
					renderer: methodName
				}, {
					header : "促销编码",
					width : 45,
					dataIndex : 'promCode'
				}, {
					header : "操作人",
					width : 40,
					dataIndex : 'userId'
				}, {
					header : "操作时间",
					width : 35,
					dataIndex : 'formatAddTime'
				}, {
					header : "操作信息",
					dataIndex : 'message'
				} ],
				listeners : {
					'rowdblclick' : function(grid, rowIndex, e) {
						var record = logDataGridPanel.getSelectionModel()
								.getSelected();
						var returnMessage = record.get("message");
						Ext.getCmp("promotionslog.message").setValue(
								returnMessage);
						win.show();
					}
				}
			});
	function methodName(v){
		var msg = v;
		if(v=='0'){
			msg="满赠";
		} 
		if(v=='2'){
			msg="买赠";
		}
		if(v=='3'){
			msg="集合赠";
		}
		return msg;
	}
	logDataGridPanel.store.on('beforeload', function() {
		Ext.apply(this.baseParams, params);
	});

	function clickSearchButton() {
		params["shopCode"] = getValueById('promotionslog.shopCode');
		params["promType"] = getValueById("promotionslog.promType");
		params["promCode"] = getValueById("promotionslog.promCode");
		logDataGridPanel.store.load({
			params : params
		});
	}

	function clickResetButton() {
		logSearchForm.form.reset();
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
			id : 'promotionslog.message',
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


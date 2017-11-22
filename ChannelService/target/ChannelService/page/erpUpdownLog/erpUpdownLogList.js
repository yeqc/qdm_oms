Ext.onReady(init);

function init() {
	listPage();
};
function listPage() {
	var record_start = 0;
	var pageSize = 15;
	var params = {};
	var url = basePath + '/custom/log/searchErpUpdownLog.spmvc';
	params["start"] = 0;
	params["limit"] = pageSize;
	// 经营店铺选择框
	var shopSelectOption = new Ext.form.ComboBox({
		id : 'channelerpupdownlog.shopCode',
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
		id : 'channelerpupdownlog.channelCode',
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
	
	var codeTypeStore = new Ext.data.SimpleStore(
			{
				fields : [ 'value', 'text' ],
				data : [
					[ '1', '同步成功' ], [ '0', '同步失败' ]
				]
			});

	var codeSelectOption = new Ext.form.ComboBox({
		id : 'channelerpupdownlog.returnCode',
		store : codeTypeStore,
		xtype : 'combo',
		valueField : 'value',
		displayField : 'text',
		mode : 'local',
		forceSelection : true,
		blankText : '同步结果',
		emptyText : '同步结果',
		name : 'returnCode',
		editable : false,
		triggerAction : 'all',
		fieldLabel : '同步结果'
	});

	var erpLogSearchForm = new Ext.FormPanel({
		applyTo : 'erpLog_search_panel',
		labelSeparator : "：",
		height : '110',
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
			} , {
				columnWidth : .3,
				layout : 'form',
				border : false,
				items : [ {
					id : 'channelerpupdownlog.goodsSn',
					xtype : 'textfield',
					name : 'goodsSn',
					fieldLabel : '商品编号',
					anchor : '90%'
				} ]
			},{
				columnWidth : .3,
				layout : 'form',
				border : false,
				items : [ {
					readOnly : true,
					fieldLabel : '开始时间',
					anchor : '90%',
					html : '<input type="text" id="erpLog_beginTime" readonly="true" onClick="WdatePicker({dateFmt:\'yyyy-MM-dd HH:mm:ss\'})"/>'
				} ]
			},{
				columnWidth : .3,
				layout : 'form',
				border : false,
				items : [ {
					fieldLabel : '结束时间',
					anchor : '90%',
					readOnly : true,
					html : '<input type="text" id="erpLog_endTime" readonly="true" onClick="WdatePicker({dateFmt:\'yyyy-MM-dd HH:mm:ss\'})"/>'
				} ]
				
				
			},{
				columnWidth : .3,
				layout : 'form',
				border : false,
				items : [ codeSelectOption ]
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
	} , {
		name : 'shopTitle'
	} , {
		name : 'goodsSn'
	} , {
		name : 'code'
	} , {
		name : 'message'
	} , {
		name : 'status'
	} , {
		name : 'formatRequestTime'
	}]);

	var proxy = new Ext.data.HttpProxy({
		url : url,
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
					
				var shopCode = Ext.getCmp("channelerpupdownlog.shopCode").getValue();
				var channelCode = Ext.getCmp("channelerpupdownlog.channelCode").getValue();
				var goodsSn = Ext.getCmp("channelerpupdownlog.goodsSn").getValue();	
				var code=Ext.getCmp("channelerpupdownlog.returnCode").getValue();
				
				
				//"/page/erpUpdownLog/exportlog/
				
			/*	var path = basePath + '/custom/log/exportUpDownLog.spmvc?shopCode='+shopCode+'&channelCode='
				+channelCode+'&goodsSn='+goodsSn;*/
					 
				var path = basePath + '/custom/log/exportUpDownLog.spmvc';
				
					 /*window.location.href = path;
					 alertMsg("结果", "导出调整单成功！");*/
				
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
							code:code,
							goodsSn:goodsSn,
							beginTime:$("#erpLog_beginTime").val(),
							endTime:$("#erpLog_endTime").val()
						}, 
						success : function(response) {
							myMask.hide(); 	
							var respText = Ext.util.JSON.decode(response.responseText);
							
							if(respText.isok){
							
								// window.open(basePath+"/page/erpUpdownLog/exportLog/"+respText.message);
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
	var erpLogDataGridPanel = new Ext.grid.GridPanel(
			{
				title : '日志列表',
				loadMask : true,
				applyTo : 'erpLog_data_panel',
				frame : true,
				store : dataStore,
				animCollapse : false,
				tbar : tbar,
				columnLines : true,
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
					width : 30,
					dataIndex : 'shopTitle'
				}, {
					header : "商品编号",
					width : 20,
					dataIndex : 'goodsSn'
				}, {
					header : "同步结果",
					width : 40,
					dataIndex : 'code',
					align : "center",
					renderer:function(v){
						var msg=""; 
						if(v=='1'){ msg="<font color='green'>同步成功</font>";}
						else { msg="<font color='red'>同步失败</font>"; }
						return msg;
					}
				} , {
					header : "状态",
					width : 20,
					align : "center",
					dataIndex : 'status',
					renderer:function(v){
						var msg=""; 
						if(v=='1'){msg="上架";}
						else { msg="下架"; }
						return msg;
					}
				} , {
					header : "创建时间",
					width : 40,
					dataIndex : 'formatRequestTime'
				} , { 
					header : "同步信息",
					dataIndex : 'message'
				} ],
				listeners : {
					'rowdblclick' : function(grid, rowIndex, e) {
						var record = erpLogDataGridPanel.getSelectionModel()
								.getSelected();
						var returnMessage = record.get("message");
						Ext.getCmp("channelerpupdownlog.message").setValue(
								returnMessage);
						win.show();
					}
				}
			});

	erpLogDataGridPanel.store.on('beforeload', function() {
		Ext.apply(this.baseParams, params);
	});

	function clickSearchButton() {
		params["channelCode"] = getValueById("channelerpupdownlog.channelCode");
		params["shopCode"] = getValueById('channelerpupdownlog.shopCode');
		params["goodsSn"] = getValueById("channelerpupdownlog.goodsSn");
		params["code"] = getValueById('channelerpupdownlog.returnCode');
		params["beginTime"] = $("#erpLog_beginTime").val();
		params["endTime"] = $("#erpLog_endTime").val();
		if (params.channelCode == '' && params.shopCode == '' 
			&& params.goodsSn == '' && params.code == '' 
			&& params.beginTime == '' && params.endTime == '' ) {
			alertMsg("提示", "请选择查询条件再查询！");
			return;
		}
		erpLogDataGridPanel.store.load({
			params : params
		});
	}

	function clickResetButton() {
		erpLogSearchForm.form.reset();
		$("#erpLog_beginTime").val("");
		$("#erpLog_endTime").val("");
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
			id : 'channelerpupdownlog.message',
			xtype : 'textarea',
			readOnly : true,
			width : 575,
			height : 158
		} ]
	});

	function setResize() {
		var formHeight = erpLogSearchForm.getHeight();
		var clientHeight = document.body.clientHeight;
		var clientWidth = document.body.clientWidth;
		erpLogDataGridPanel.setHeight(clientHeight - formHeight - 50);
		erpLogDataGridPanel.setWidth(clientWidth - 259);
	}
	window.onresize = function() {
		setResize();
	};
	setResize();
};


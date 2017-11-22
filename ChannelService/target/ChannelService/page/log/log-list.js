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
		id : 'channelapilog.shopCode',
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
		id : 'channelapilog.channelCode',
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

	var adjustTypeStore = new Ext.data.SimpleStore(
			{
				fields : [ 'value', 'text' ],
				data : [
					[ '0', '价格调整' ], [ '1', '上下架调整' ], [ '2', '商品添加调整' ]
				]
			});

	// 渠道（调整类型）选择框
	var adjustTypeSelectOption = new Ext.form.ComboBox({
		id : 'channelapilog.methodName',
		store : adjustTypeStore,
		xtype : 'combo',
		valueField : 'value',
		displayField : 'text',
		mode : 'local',
		forceSelection : true,
		blankText : '请选择调整类型',
		emptyText : '调整类型',
		name : 'methodName',
		editable : false,
		triggerAction : 'all',
		fieldLabel : '调整类型'
	});

	
	var returnCodeTypeStore = new Ext.data.SimpleStore(
			{
				fields : [ 'value', 'text' ],
				data : [
					[ '1', '执行成功' ], [ '0', '执行失败' ]
				]
			});

	var returnCodeSelectOption = new Ext.form.ComboBox({
		id : 'channelapilog.returnCode',
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
		applyTo : 'log_search_panel',
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
			}, {
				columnWidth : .3,
				layout : 'form',
				border : false,
				items : [ adjustTypeSelectOption ]
			},
			{
				
				columnWidth : .3,
				layout : 'form',
				border : false,
				items : [ {
					readOnly : true,
					fieldLabel : '开始时间',
					anchor : '90%',
					html : '<input type="text" id="api_beginTime" readonly="true" onClick="WdatePicker({dateFmt:\'yyyy-MM-dd HH:mm:ss\'})"/>'
				} ]
				
			},{
				columnWidth : .3,
				layout : 'form',
				border : false,
				items : [ {
					fieldLabel : '结束时间',
					anchor : '90%',
					readOnly : true,
					html : '<input type="text" id="api_endTime" readonly="true" onClick="WdatePicker({dateFmt:\'yyyy-MM-dd HH:mm:ss\'})"/>'
				} ]
			},{
				columnWidth : .3,
				layout : 'form',
				border : false,
				items : [ {
					id : 'channelapilog.paramInfo',
					xtype : 'textfield',
					name : 'paramInfo',
					fieldLabel : '调整单号',
					anchor : '90%'
				} ]
			},{
				columnWidth : .3,
				layout : 'form',
				border : false,
				items : [ returnCodeSelectOption ]
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
		name : 'channelTitle'
	}, {
		name : 'shopTitle'
	}, {
		name : 'methodName'
	}, {
		name : 'paramInfo'
	}, {
		name : 'returnMessage'
	}, {
		name : 'formatRequestTime'
	}, {
		name : 'returnCode'
	}, {
		name : 'user'
	}]);

	var proxy = new Ext.data.HttpProxy({
		url : basePath + '/custom/log/search.spmvc',
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
	
/*	var deliverForm = new Ext.form.FormPanel({
		
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
				items : [ 
					{
						id : 'df.shopCode',
						xtype : 'hidden',
						name : 'shopCode',
						fieldLabel : '调整单号',
						anchor : '90%'
					}      
				         ]
			}, {
				columnWidth : .3,
				layout : 'form',
				border : false,
				items : [ 
					{
						id : 'df.channelCode',
						xtype : 'hidden',
						name : 'channelCode',
						fieldLabel : '调整单号',
						anchor : '90%'
					} 
				         
				        ]
			}, {
				columnWidth : .3,
				layout : 'form',
				border : false,
				items : [ 
					{
						id : 'df.methodName',
						xtype : 'hidden',
						name : 'methodName',
						fieldLabel : '调整单号',
						anchor : '90%'
					}
				]
			}, {
				columnWidth : .3,
				layout : 'form',
				border : false,
				items : [ {
					id : 'df.paramInfo',
					xtype : 'hidden',
					name : 'paramInfo',
					fieldLabel : '调整单号',
					anchor : '90%'
				} ]
			} ]
		} ]
	
	});
	*/
	
	var myMask = new Ext.LoadMask(Ext.getBody(), {msg:"请稍候..."}); 
	tbar = [
			{
				text : '导出日志',
				tooltip : '导出日志',
				iconCls : 'add',
				handler : function() {
				//	var loginForm = deliverForm.getForm();  
					var shopCode = Ext.getCmp("channelapilog.shopCode").getValue();
					var channelCode = Ext.getCmp("channelapilog.channelCode").getValue();
					var methodName = Ext.getCmp("channelapilog.methodName").getValue();	
					var paramInfo = Ext.getCmp("channelapilog.paramInfo").getValue();
					var returnCode=Ext.getCmp("channelapilog.returnCode").getValue();
					/*var path = basePath + '/custom/log/exportLog.spmvc?shopCode='+shopCode+'&channelCode='
					+channelCode+'&methodName='+methodName+'&paramInfo='+paramInfo;*/
					var path = basePath + '/custom/log/exportLog.spmvc';
					var beginTime = $("#api_beginTime").val();
					var endTime = $("#api_endTime").val();
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
							methodName:methodName,
							paramInfo:paramInfo,
							returnCode:returnCode,
							beginTime: beginTime,
							endTime: endTime
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
					
					
					/*	
					 window.location.href = path;
					 alertMsg("结果", "导出调整单成功！");*/
			//		Ext.getCmp("df.shopCode").setValue(Ext.getCmp("channelapilog.shopCode").getValue());
					
			//		Ext.getCmp("df.channelCode").setValue(Ext.getCmp("channelapilog.channelCode").getValue());
			//		Ext.getCmp("df.methodName").setValue(Ext.getCmp("channelapilog.methodName").getValue());
			//		Ext.getCmp("df.paramInfo").setValue(Ext.getCmp("channelapilog.paramInfo").getValue());
					
					//Ext.getCmp("channelapilog.channelCode").getValue();
				//	Ext.getCmp("channelapilog.methodName").getValue();	
				//	Ext.getCmp("channelapilog.paramInfo").getValue();
					
				//	var path = basePath + '/custom/log/exportLog.spmvc';
			/*		loginForm.doAction('submit', {   
			                url:path,   
			                method:'POST',                         
			                waitMsg:'正在登陆...',   
			                timeout:10000,//10秒超时,   
			           //   params:'',//获取表单数据   
			                success:function(form, action){   
			                    var isSuc = action.result.success;   
			                //    if(isSuc) {   
			                        //提示用户登陆成功   
			                    	 window.location.href = path;
			                      //  Ext.Msg.alert('消息', '登陆成功..');   
			                  //  }                                          
			                },   
			                failure:function(form, action){   
			                    alert('登陆失败');   
			                }   
			            }); */  

					
				}
				
			}
	];
	

	// 创建Grid表格组件
	var logDataGridPanel = new Ext.grid.GridPanel(
			{
				title : '日志列表',
				loadMask : true,
				applyTo : 'log_data_panel',
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
					width : 30,
					dataIndex : 'shopTitle'
				}, {
					header : "调整单类型",
					width : 38,
					dataIndex : 'methodName',
					renderer: methodName
				}, {
					header : "操作人",
					width : 35,
					dataIndex : 'user'
				}, {
					header : "调整单号",
					width : 70,
					dataIndex : 'paramInfo'
				}, {
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
					header : "创建时间",
					width : 38,
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
						Ext.getCmp("channelapilog.resultMessage").setValue(
								returnMessage);
						win.show();
					}
				}
			});
	function methodName(v){
		var msg = v;
		if(v=='0'){
			msg="价格调整";
		} 
		if(v=='1'){
			msg="上下架调整";
		}  
		if(v=='2'){
			msg="商品详情调整";
		}
		if(v=='3'){
			msg="卖点调整";
		}
		if(v=='4'){
			msg="商品名称调整";
		}
		if(v=='5'){
			msg="商品条形码调整";
		}
		if(v=='6'){
			msg="商品运费承担方式调整";
		}
		if(v=='7'){
			msg="商品支持会员打折调整";
		}
		if(v=='8'){
			msg="商品线上线下调整";
		}
		if(v=='9'){
			msg="商品详情生成";
		}
		if(v=='10'){
			msg="店铺经营商品生成";
		}
		if(v=='11'){
			msg="库存同步";
		}
		if(v=='12'){
			msg="更新店铺token";
		}
		if(v=='15'){
			msg="商品货款类型";
		}
		if(v=='16'){
			msg="拍下立减库存";
		}
		if(v=='19'){
			msg="库存同步管理";
		}
		if(v=='20'){
			msg="商品销售属性管理";
		}
		return msg;
	}
	logDataGridPanel.store.on('beforeload', function() {
		Ext.apply(this.baseParams, params);
	});

	function clickSearchButton() {
		params["channelCode"] = getValueById("channelapilog.channelCode");
		params["shopCode"] = getValueById('channelapilog.shopCode');
		params["beginTime"] = $("#api_beginTime").val();
		params["endTime"] = $("#api_endTime").val();
		params["methodName"] = getValueById("channelapilog.methodName");
		params["paramInfo"] = getValueById("channelapilog.paramInfo");
		params["returnCode"]= getValueById("channelapilog.returnCode");
		if (params.channelCode == '' && params.shopCode == '' 
			&& params.methodName == '' && params.returnCode == '' && params.paramInfo == '' 
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
		$("#api_beginTime").val("");
		$("#api_endTime").val("");
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
			id : 'channelapilog.resultMessage',
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


Ext.onReady(function() {
	var channelTemplateForm;
	var channelTemplateGrid;
	ChannelTemplatePage= function() {
		var channelTemplateStore;
		var channelTemplateProxy;
		var channelTemplateReader;
		var checkBoxSelect;
		var channelTemplateColumnGrid;
		var tbar;
		var pageSize=15;
		var listUrl = basePath+"custom/channelTemplate/channelTemplateList.spmvc?method=start";
		return {
			init : function() {
			//经营店铺选择框
			var shopSelectOption = new Ext.form.ComboBox({
					id : 'channelTemplate.keywords',
					store : new Ext.data.SimpleStore( {
						data : [ [ '1', '淘宝mc' ], [ '2', '京东' ]],
						fields : [ 'text', 'filed' ]
					}),
					xtype : 'combo',
					valueField : 'text',
					displayField : 'filed',
					mode : 'local',
					forceSelection : true,
					blankText : '请选择经营店铺',
					emptyText : '经营店铺',
					name : 'keywords1',
					editable : false,
					hiddenName : 'keywords1',
					triggerAction : 'all', 
					fieldLabel : '经营店铺',
					width : 150
			});
			
			
			var shopStore = new Ext.data.Store({
				proxy : new Ext.data.HttpProxy({
					url : basePath + '/custom/promotion/channelshoplist.spmvc',
					method : 'GET'
				}),
				reader : new Ext.data.JsonReader({
					fields : [ 'shopCode', 'shopTitle' ]
					})
			});
			channelTemplateForm = new Ext.FormPanel(
						{
							labelAlign : 'top',
							frame : true,
							bodyStyle : 'padding:5px 5px 0',
							//autoHeight : true,
							//autoScroll : true,
							//autoWidth : true,
							url : "",
							labelAlign : 'right',
							layout : 'form',
							items : [ {
								layout : 'column',
								items : [{
											layout : 'form',
											labelWidth:80,
											columnWidth:0.3,
											items : [ {
												xtype : 'textfield',
												fieldLabel : '所属渠道',
												id:'channelTemplateList.channelTitle',
												readOnly : true,
												value : getGlobalValue('channelTitle'),
												width : '150'//,
												//value:'${scs.shopCode}'
											} , {
												id : 'channelTemplate.templateName',
												xtype : 'textfield',
												fieldLabel : '模版名称',
												name : 'templateName',
												//allowBlank : false,
												//blankText : '模版名称不能为空',
												width : '150'
											}]
										} , {
											layout : 'form',
											labelWidth:80,
											columnWidth:0.33,
											items : [{
												xtype : 'textfield',
												fieldLabel : '应用店铺',
												id:'channelTemplateList.shopTitle',
												readOnly : true,
												value : getGlobalValue('shopTitle'),
												width : '150'//,
												//value:'${scs.shopCode}'	
											} , {
												id : 'channelTemplate.templateCode',
												xtype : 'textfield',
												fieldLabel : '模板编号',
												name : 'templateCode', 
												//allowBlank : false,
												//blankText : '模板编号不能为空',
												width : '150'
											}
											]
										}]
							}],
							buttons : [
									{
										text : '查询',
										columnWidth : .1,
										handler : this.search
									} ]
						});
						 
						checkBoxSelect = new Ext.grid.CheckboxSelectionModel();
				
				channelTemplateColumnGrid = new Ext.grid.ColumnModel( [
					checkBoxSelect, 
					{id : 'id', header : "id", align : "center", align : "center", width : 80, hidden:true, dataIndex : 'id' },
					{id : 'templateCode', sortable : true, header : "模板编号", width : 160, dataIndex : 'templateCode'},
					{id : 'templateName', sortable : true, header : "模板名称", width : 120, dataIndex : 'templateName'},
					{id : 'channelCode', header : "渠道代码", align : "center", width : 100, dataIndex : 'channelCode'},
					{id : 'shopCode', align : "center", sortable : true, header : "店铺代码", width : 100, dataIndex : 'shopCode'},
					{id : 'backup', align : "center", sortable : true, header : "备注信息", width : 100, dataIndex : 'backup'},
					{id : 'addTime', align : "center", sortable : true, header : "创建时间", width : 120, dataIndex : 'addTime'},
					{id : 'updateTime', align : "center", sortable : true, header : "最后更新时间", width : 140, dataIndex : 'updateTime'},
					{id : 'op', align : "center", sortable : true, header : "操作", width : 120, dataIndex : 'safePrice',renderer: opButton}
				]);

				function opButton () {
					return "<input type='button' value='模板修改'onclick='ChannelTemplatePage.editTemplate();'/>";
				}
				//与列对应的dataIndex
				couponrecord = Ext.data.Record.create( [
					{name : 'id' },
					{name : 'templateCode' },
					{name : 'templateName' },
					{name : 'channelCode'},
					{name : 'shopCode'},
					{name : 'backup' },
					{name : 'addTime'},
					{name : 'updateTime'},
					{name : 'templateType'}
					]);
				
				//加载列表数据
				channelTemplateProxy = new Ext.data.HttpProxy( {
					url : listUrl,
					method : "post"
				});
				//Reader 读json中数据
				channelTemplateReader = new Ext.data.JsonReader( {
					root : 'root',
					totalProperty : 'totalProperty'
				}, couponrecord);
				
				channelTemplateStore = new Ext.data.Store( { 
					autoLoad : {
						params : {
							start : 0,
							limit : pageSize
						}
					},
					proxy :  channelTemplateProxy,
					reader : channelTemplateReader
				});
				// 如果不是用ajax的表单封装提交,就要做如下操作.
				//这里很关键，如果不加，翻页后搜索条件就变没了，这里的意思是每次数据载入前先把搜索表单值加上去，这样就做到了翻页保留搜索条件了   
				channelTemplateStore.on('beforeload', function(){
					var templateName = Ext.getCmp('channelTemplate.templateName').getValue();
					var templateCode = Ext.getCmp('channelTemplate.templateCode').getValue();
					Ext.apply(this.baseParams, {channelCode: getGlobalValue('channelCode'), shopCode: getGlobalValue('shopCode'), 
						templateName: templateName, templateCode: templateCode});
				});

				var menu = new Ext.menu.Menu({
					items : [
							{
							//	id : 'message_grid_tBar@add',
								text : '添加电脑版模板',
								tooltip : '添加电脑版模板',
								iconCls : 'add',
								handler : function() {
									var shopCode = getGlobalValue('shopCode');
									var channelCode = getGlobalValue('channelCode');
									FormEditWin.showAddDirWin("popWins",basePath+"custom/channelTemplate/addTemplate.spmvc?method=init&shopCode="+
											shopCode+ "&channelCode=" + channelCode +"&templateType=1","pop_template_winID","添加电脑版模板","100%",580);
								}
							} ,{
									text : '添加手机版模板',
									tooltip : '添加手机版模板',
									iconCls : 'add',
									handler : function() {
										var shopCode = getGlobalValue('shopCode');
										var channelCode = getGlobalValue('channelCode');
										FormEditWin.showAddDirWin("popWins",basePath+"custom/channelTemplate/addTemplate.spmvc?method=init&shopCode="+
												shopCode+ "&channelCode=" + channelCode +"&templateType=2","pop_template_winID","添加手机版模板","100%",580);
									}
							}]
				});
				
				//定义菜单栏
				tbar = [
			   /*     {
						text : '添加模版',
						menu : menu
					} */
				        
				    {
						id : 'message_grid_tBar@add',
						text : '添加模板',
						tooltip : '添加模板',
						iconCls : 'add',
						handler : function() {
							var shopCode = getGlobalValue('shopCode');
							var channelCode = getGlobalValue('channelCode');
							FormEditWin.showAddDirWin("popWins",basePath+"custom/channelTemplate/addTemplate.spmvc?method=init&shopCode="+
									shopCode+ "&channelCode=" + channelCode,"pop_template_winID","添加模板","100%",580);
						}
					},{ 
						text : '批量移除',
						tooltip : '批量移除',
						iconCls : 'delete',
						handler : function () {
							ChannelTemplatePage.deleteObject();
						}
					}];
				
				
				
				channelTemplateGrid = new Ext.grid.GridPanel( {
					//width : 1000,
//					autoWidth : true,
					height : 500,
					title : '店铺宝贝详情模版管理',
					store : channelTemplateStore,
					id:'channelTemp_gridss_id',
					trackMouseOver : false,
					disableSelection : true,
					loadMask : true,
					frame : true,
					columnLines : true,
					autoExpandColumn: "templateName", //自动伸展，占满剩余区域
					tbar:  tbar,
					// grid columns  
					cm : channelTemplateColumnGrid,
					sm:checkBoxSelect,
					// paging bar on the bottom  
					bbar : new IssPagingToolbar(channelTemplateStore, pageSize),
					listeners : {
						'rowdblclick': function(thisgrid ,rowIndex ,e){
							var selectionModel = thisgrid.getSelectionModel();
							var record = selectionModel.getSelected();
							var id = record.data['id'];
							var shopCode = getGlobalValue('shopCode');
							var channelCode = getGlobalValue('channelCode');
						//	var templateType = record.data['templateType'];
						//	alert(templateType);
							FormEditWin.showAddDirWin("popWins",basePath+"custom/channelTemplate/addTemplate.spmvc?method=init&id="+id+"&shopCode="+
									shopCode+ "&channelCode=" + channelCode   ,"pop_template_winID","编辑模板","100%",580);
						}
					}
				});
			},
			deleteObject : function() { //删除整个panel里面被选择的行数据
				var selModel = channelTemplateGrid.getSelectionModel();
				if (selModel.hasSelection()) {
					confirmMsg("确认","您确定要删除选择的记录吗?",
							function(btn) {
						if (btn == "yes") {
							var records = selModel.getSelections();
							var ids="";
							for ( var i = 0; i < records.length; i++) {
								var id = records[i].get("id"); 
								if(id && id!='' && id!=null){
									ids += ""+id+",";
								}
							}
							if(ids != ""){//批量删除
							Ext.Ajax .request({
										waitMsg : '请稍等.....',
										url : basePath + 'custom/channelTemplate/deleteTemplate.spmvc',
										method : 'post',
										params : {
											ids : ids
										},
										success : function(response) {
											var respText = Ext.util.JSON.decode(response.responseText);
											if(respText.success){ //成功
												Ext.Msg.alert('结果',respText.msg,function(xx){channelTemplateStore.reload();});
											}else{
												errorMsg("结果", respText.msg);
											}
										},
										failure : function(response, options) {
											var respText = Ext.util.JSON.decode(response.responseText); 
											errorMsg("结果", respText.msg);
										}
								});
							}
						}
					});
				} else {
					alertMsg("错误", "请选择要批量删除的行!");
				}
			},
			show : function() {
				channelTemplatePanel = new Ext.Panel( {
					renderTo : 'channelTemplate-grid',
					autoHeight:true,
					layout : 'column',
					items : [ channelTemplateForm, channelTemplateGrid ]
				}).show();
			},
			search : function() {
				var searchParam = {};
					searchParam["templateName"] = Ext.getCmp("channelTemplate.templateName").getValue();
					searchParam["templateCode"] = Ext.getCmp("channelTemplate.templateCode").getValue();
					searchParam["channelCode"] = Ext.getCmp("channelTemplate.channelCode").getValue();
					searchParam["keywords"] = Ext.getCmp("channelTemplate.keywords").getValue();
					channelTemplateGrid.store.baseParams = searchParam;
				channelTemplateGrid.store.load({params : {start : 0, limit : pageSize}});
			},
			editTemplate : function(){
				var selModel = channelTemplateGrid.getSelectionModel();
				if (selModel.hasSelection()) {
					var records = selModel.getSelections();
					var id = records[0].get("id");
					if(id != ""){
						 FormEditWin.showAddDirWin("popWins",basePath+"custom/channelTemplate/editTemplateContent.spmvc?method=init&id="+id,"pop_etempalte_winID","修改模板",860,560);
					}else{
						 alertMsg("结果", "请选择需要修改的行！");
					}
				}else{
					 alertMsg("结果", "请选择需要修改的行！");
				}
			},
			refresh : function(){
				channelTemplateStore.reload();
			}
		}
	}();


	function runChannelTemplateList() {
		ChannelTemplatePage.init(); 
		ChannelTemplatePage.show();
		setResize();
	}
	runChannelTemplateList();

	function setResize() {
		var formHeight = channelTemplateForm.getHeight();
		var clientHeight = document.body.clientHeight;
		var clientWidth = document.body.clientWidth;
		channelTemplateGrid.setHeight(clientHeight-formHeight-50);
		channelTemplateGrid.setWidth(clientWidth-259);
	}
	window.onresize=function(){
		setResize();
	}
});
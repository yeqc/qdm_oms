Ext.onReady(function() {
	var channelTemplateMGrid;
	var channelTemplateMForm;
	ChannelTemplateModulePage= function() {
		var channelTemplateMStore;
		var channelTemplateProxy;
		var channelTemplateReader;
		var checkBoxSelect;
		var channelTemplateColumnGrid;
		var tbar;
		var pageSize=15;
		var listUrl = basePath+"custom/channelTemplate/channelTemplateModuleList.spmvc?method=start";
		return {
			init : function() {
				channelTemplateMForm = new Ext.FormPanel(
						{
							labelAlign : 'top',
							frame : true,
							bodyStyle : 'padding:5px 5px 0',
							autoHeight : true,
							autoScroll : true,
							autoWidth : true,
							url : "",
							labelAlign : 'right',
							layout : 'form',
							items : [ {
								layout : 'column',
								items : [{
											layout : 'form',
											labelWidth:80,
											columnWidth:0.3,
											items : [{
												id : 'channelTemplateModule.moduleName',
												xtype : 'textfield',
												fieldLabel : '模块名称',
												name : 'moduleName',
												//allowBlank : false,
												//blankText : '模块名称不能为空',
												width : '150'
											}]
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
					{id : 'moduleName', sortable : true, header : "模块名称", width : 120, dataIndex : 'moduleName'},
					{id : 'showTitle', align : "center", sortable : true, header : "显示标题", renderer: showStatus, width : 80, dataIndex : 'showTitle'},
					{id : 'showTitle', align : "center", sortable : true, header : "应用设备类型", renderer: moduleSize, width : 100, dataIndex : 'moduleSize'},
					{id : 'moduleType', align : "center", sortable : true, header : "模板类型", renderer: moduleType, width : 100, dataIndex : 'moduleType'},
					{id : 'addTime', align : "center", sortable : true, header : "创建日期", width : 160, dataIndex : 'addTime'},
					{id : 'updateTime', align : "center", sortable : true, header : "最后更新时间", width : 160, dataIndex : 'updateTime'}
				]);
				function showStatus(v) {
					var msg=""; 
					if(v=='0'){
						msg="显示";
					} else if(v=='1'){
						msg="不显示";
					}
					return msg;
				}
				function moduleSize(v) {
					var msg=""; 
					if(v=='0'){
						msg="电脑版";
					} else if(v=='1'){
						msg="移动版";
					}
					return msg;
				}
				function moduleType(v) {
					var msg=""; 
					if(v=='1'){
						msg="商品信息";
					} else if(v=='2'){
						msg="模特展示";
					} else if(v=='3'){
						msg="编辑推荐";
					} else if(v=='4'){
						msg="模特信息";
					} else if(v=='5'){
						msg="细节展示";
					} else if(v=='6'){
						msg="颜色选择";
					} else if(v=='7'){
						msg="尺码规格";
					}
					return msg;
				}
				//与列对应的dataIndex
				couponrecord = Ext.data.Record.create( [
					{name : 'id' },
					{name : 'moduleName'},
					{name : 'showTitle' },
					{name : 'moduleSize'},
					{name : 'moduleType'},
					{name : 'addTime'},
					{name : 'updateTime'}
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
				
				channelTemplateMStore = new Ext.data.Store( {
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
				channelTemplateMStore.on('beforeload', function(){
					var moduleName = Ext.getCmp('channelTemplateModule.moduleName').getValue();
					Ext.apply(this.baseParams, {channelCode: getGlobalValue('channelCode'), shopCode: getGlobalValue('shopCode'),
						moduleName: moduleName});
				});

				//定义菜单栏
				tbar = [{
						id : 'message_grid_tBar@add',
						text : '添加模板',
						tooltip : '添加模板',
						iconCls : 'add',
						handler : function() {
							FormEditWin.showAddDirWin("popWins",basePath+"custom/channelTemplate/addModule.spmvc?method=init","pop_template_winID","添加模板模块",'100%',540);
						}
					},{ 
						text : '移除',
						tooltip : '移除',
						iconCls : 'delete',
						handler : function() {
							ChannelTemplateModulePage.deleteObject();
						}
					}];
				channelTemplateMGrid = new Ext.grid.GridPanel( {
					autoWidth : true,
				//	width : 1000,
					height : 550,
					title : '自定义模块管理',
					store : channelTemplateMStore,
					id:'channelTemplateModule-grid',
					trackMouseOver : false,
					disableSelection : true,
					loadMask : true,
					frame : true,
					columnLines : true,
					autoExpandColumn: "moduleName", //自动伸展，占满剩余区域
					tbar:  tbar,
					// grid columns  
					cm : channelTemplateColumnGrid,
					sm:checkBoxSelect,
					// paging bar on the bottom  
					bbar : new IssPagingToolbar(channelTemplateMStore, pageSize),
					listeners : {
						'rowdblclick': function(thisgrid ,rowIndex ,e){
							var selectionModel = thisgrid.getSelectionModel();
							var record = selectionModel.getSelected();
							var id = record.data['id'];
							FormEditWin.showAddDirWin("popWins",basePath+"custom/channelTemplate/addModule.spmvc?method=init&id="+id, "pop_template_winID", "修改模板模块",'100%',560);
						}
					}
				});
			},
			deleteObject : function() { //删除整个panel里面被选择的行数据
				var selModel = channelTemplateMGrid.getSelectionModel();
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
										url : basePath + 'custom/channelTemplate/deleteModule.spmvc',
										method : 'post',
										params : {
											ids : ids
										},
										success : function(response) {
											var respText = Ext.util.JSON.decode(response.responseText);
											if(respText.success){ //成功
												Ext.Msg.alert('结果',respText.msg,function(xx){channelTemplateMStore.reload();});
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
				modulePanel = new Ext.Panel( {
					renderTo : 'channelTemplateModule-grid',
					layout : 'column',
					
					items : [ channelTemplateMForm, channelTemplateMGrid ]
				}).show();
			},
			search : function() {
				var searchParam = {};
					searchParam["moduleName"] = Ext.getCmp("channelTemplateModule.moduleName").getValue();
					channelTemplateMGrid.store.baseParams = searchParam;
				channelTemplateMGrid.store.load({params : {start : 0, limit : pageSize}});
			},
			refresh : function(){
				channelTemplateMStore.reload();
			}
		}
	}();

	function runChannelTemplateModulePageList() {
		ChannelTemplateModulePage.init(); 
		ChannelTemplateModulePage.show();
		setResize();
	}
	runChannelTemplateModulePageList();

	function setResize() {
		var formHeight = channelTemplateMForm.getHeight();
		var clientHeight = document.body.clientHeight;
		var clientWidth = document.body.clientWidth;
		channelTemplateMGrid.setHeight(clientHeight-formHeight-50);
		channelTemplateMGrid.setWidth(clientWidth-259);
	}
	window.onresize=function(){
		setResize();
	}
});
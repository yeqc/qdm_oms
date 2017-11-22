Ext.onReady(promotion);

/**
 * 套装列表
 */
function promotion() {
	Ext.QuickTips.init();

	Ext.form.Field.prototype.msgTarget = 'side';

	var record_start = 0;
	var pageSize = 15;

	var params = {};
	params["start"] = 0;
	params["limit"] = pageSize;

	var groupGoodsSearchForm = new Ext.FormPanel({
		frame : true,
		forceFit : true,
//		autoScroll : false,
//		autoWidth : true,
//		autoHeight : true,
		labelAlign : "left",
		waitMsgTarget : true,
		bodyBorder : false,
		applyTo : 'group_goods_search_div',
		border : true,
		items : [ {
			layout : 'form',
			border : false,
			items : [ {
				width : 165,
				id : 'groupgoods.search.groupCode',
				xtype : 'textfield',
				name : 'groupCode',
				fieldLabel : '套装商品代码'
			} ]
		} ],
		buttonAlign : 'right',
		buttons : [ {
			text : '查询',
			handler : clickSearchButton
		}, {
			text : '重置',
			handler : clickResetButton
		} ]
	});

	function clickResetButton() {
		groupGoodsSearchForm.form.reset();
	}

	var record = Ext.data.Record.create([ {
		name : 'inedx'
	}, {
		name : 'id'
	}, {
		name : 'groupCode'
	}, {
		name : 'price'
	}, {
		name : 'addTime'
	} ]);

	this.proxy = new Ext.data.HttpProxy({
		url : basePath + '/custom/promotion/groupgoodssearch.spmvc',
		method : "post"
	});

	// Reader 读json中数据
	var reader = new Ext.data.JsonReader({
		root : 'root',
		totalProperty : 'totalProperty'
	}, record);

	// 定义数据集对象
	var dataStore = new Ext.data.Store({
		remoteSort : true,
		autoLoad : {
			params : params
		},
		proxy : proxy,
		reader : reader
	});

	// 创建工具栏组件
	var toolbar = new Ext.Toolbar(
			{
				items : [
						"菜单栏：",
						"-",
						{
							text : '添加套装',
							iconCls : 'add',
							handler : addDataWinOpen
						},
						{
							text : '批量移除',
							iconCls : 'delete',
							handler : deleteDataForBatch
						},
						{
							text : '批量导入',
							iconCls : 'refresh',
							handler : uploadDataWinOpen
						},
						'<a style="color:red" href = "' + basePath
								+ '/page/promotion/group_goods_list.xls'
								+ '">模板下载</a>' ]
			});

	// 创建全选组件
	var cb = new Ext.grid.CheckboxSelectionModel();

	// 创建Grid表格组件
	var dataGridPanel = new Ext.grid.GridPanel(
			{
				id : 'group_goods_data_panel',
				renderTo : 'group_goods_data_div',
				autoExpandColumn : "groupCode", // 自动伸展，占满剩余区域
				viewConfig : {
					forceFit : true
				},
				loadMask : true,
				frame : true,
				forceFit : true,
//				autoScroll : true,
//				autoWidth : true,
//				autoHeight : true,
				bodyBorder : false,
				tbar : toolbar,
				store : dataStore,
				sm : cb,
				bbar : new IssPagingToolbar(dataStore, pageSize),
				columns : [
						cb,
						{
							header : "序号",
							width : 50,
							renderer : function(value, metadata, record,
									rowIndex) {
								return record_start + 1 + rowIndex;
							}
						},
						{
							id : 'GroupGoods.groupCode',
							header : "套装商品编码",
							width : 200,
							dataIndex : 'groupCode'
						},
						{
							id : 'GroupGoods.price',
							header : "套装商品价格",
							width : 200,
							dataIndex : 'price'
						},
						{
							id : 'GroupGoods.addTime',
							header : "添加时间",
							width : 200,
							dataIndex : 'addTime'
						},
						{
							header : "操作",
							width : 200,
							renderer : function() {
								return '<input type="button" value="查看" onclick="toModifyGroupGoods();" /> ';
							}
						} ],
				listeners : {
					'rowdblclick' : function(grid, rowIndex, e) {
						toModifyGroupGoods();
					}
				}
			});

	toModifyGroupGoods = function() {
		var record = dataGridPanel.getSelectionModel().getSelected();
		var modifyGroupCode = record.get('groupCode');
		var url = basePath
				+ "/custom/promotion/tomodifygroupgoodslist.spmvc?groupCode="
				+ modifyGroupCode;
		FormEditWin.showAddDirWin(null, url, "picADWinID", "修改套装数据", 800, 400);
	};

	function clickSearchButton() {
		params["groupCode"] = getValueById("groupgoods.search.groupCode");
		dataGridPanel.store.load({
			params : params
		});
	}

	function deleteDataForBatch() {
		var recs = dataGridPanel.getSelectionModel().getSelections();
		var num = recs.length;
		if (num == 0) {
			Ext.MessageBox.alert('提示', '请选择要移除的数据!');
			return;
		}
		Ext.MessageBox
				.confirm(
						"提示",
						"您确定要删除所选数据吗？",
						function(btnId) {
							if (btnId == 'yes') {
								var groupCodes = [];
								for ( var i = 0; i < recs.length; i++) {
									groupCodes.push(recs[i].get("groupCode"));
								}
								Ext.Ajax
										.request({
											waitMsg : '请稍等.....',
											url : basePath
													+ '/custom/promotion/deleteGroupGoods.spmvc',
											params : {
												groupCodes : groupCodes
											},
											method : "post",
											success : function(response) {
												var json = Ext.util.JSON
														.decode(response.responseText);
												alertMsg("结果", json.message);
												dataGridPanel.store.load({
													params : params
												});
											},
											failure : function() {
												errorMsg("结果", "删除失败！");
											}
										});
							}
						});
	}

	// 显示添加细则窗口
	function addDataWinOpen() {
		FormEditWin.showAddDirWin("group_goods_div", basePath
				+ "/custom/promotion/tomodifygroupgoodslist.spmvc",
				"picADWinID", "套装数据维护", 800, 400);
	}

	function uploadDataWinOpen() {
		// 参数格式："key1:value1;key2:value2"
		var params = "&params=promType:1";
		var uploadUrl = basePath + '/custom/promotion/upload.spmvc' + params;
		FormEditWin.showAddDirWin("group_goods_upload_div", basePath
				+ "/fileUpload.html?type=1&fileTypes=*.xls&uploadUrl=" + uploadUrl,
				"picADWinID", "套装数据导入", 580, 300);
	}

	// 上传文件回调函数
	doAfter = function(data) {
		var jsonData = Ext.util.JSON.decode(data);
		if (!jsonData.isok) {
			Ext.Msg.alert("提示", jsonData.message);
			return;
		}
		dataGridPanel.store.load({
			params : params
		});
		FormEditWin.close();
		Ext.Msg.alert("提示", jsonData.message);
	};
	
	function setResize() {
		var formHeight = groupGoodsSearchForm.getHeight();
		var clientHeight = document.body.clientHeight;
		var clientWidth = document.body.clientWidth;
		dataGridPanel.setHeight(clientHeight-formHeight-50);
		dataGridPanel.setWidth(clientWidth-259);
	}
	window.onresize=function(){
		setResize();
	};
	setResize();
}

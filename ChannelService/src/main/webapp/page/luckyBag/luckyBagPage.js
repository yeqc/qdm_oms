Ext.onReady(luckyBag);
function luckyBag(){
	Ext.QuickTips.init();

	Ext.form.Field.prototype.msgTarget = 'side';

	var record_start = 0;
	var pageSize = 15;

	var params = {};
	params["start"] = 0;
	params["limit"] = pageSize;
	
	var luckyBagPanelForm = new Ext.FormPanel({
		frame : true,
		forceFit : true,
		autoScroll : false,
		autoWidth : true,
		autoHeight : true,
		labelAlign : "right",
		waitMsgTarget : true,
		bodyBorder : false,
		applyTo : 'luckyBag_panel_div',
		border : true,
		items : [ {
			layout : 'column',
			border : false,
			items : [{
				columnWidth : .3,
				layout : 'form',
				border : false,
				items : [{
					id : 'luckyBag.panel.warehouseId',
					xtype : 'textfield',
					name : 'warehouseId',
					fieldLabel : '仓库ID',
					anchor : '90%'
				}]
			},{
				columnWidth : .3,
				layout : 'form',
				border : false,
				items : [ {
					id : 'luckyBag.panel.luckyBagSku',
					xtype : 'textfield',
					name : 'luckyBagSku',
					fieldLabel : '福袋虚拟SKU',
					anchor : '90%'
				} ]
			}]
		} ],
		buttonAlign : 'right',
		buttons : [{
			text : '查询',
			handler : clickSearchButton
		}, {
			text : '重置',
			handler : clickResetButton
		}]
	});
	
	var record = Ext.data.Record.create([ {
		name : 'index'
	}, {
		name : 'id'
	}, {
		name : 'warehouseId'
	}, {
		name : 'luckyBagSku'
	}, {
		name : 'luckyBagPrice'
	}, {
		name : 'addTime'
	}, {
		name : 'isValuable'
	} ]);
	
	this.proxy = new Ext.data.HttpProxy({
		url : basePath + '/custom/luckyBag/getLuckyBagList.spmvc?',
		method : "post"
	});
	
	var reader = new Ext.data.JsonReader({
		root : 'root',
		totalProperty : 'totalProperty'
	}, record);
	
	var dataStore = new Ext.data.Store({
		remoteSort : true,
		autoLoad : {
			params : params
		},
		proxy : proxy,
		reader : reader
	});
	
	dataStore.on('beforeload',function() {
			var warehouseId = Ext.getCmp("luckyBag.panel.warehouseId").getValue();
			var luckyBagSku = Ext.getCmp("luckyBag.panel.luckyBagSku").getValue();
				Ext.apply(this.baseParams, {
					warehouseId : warehouseId,
					luckyBagSku : luckyBagSku
				});
			});
	
	var toolbar = new Ext.Toolbar(
		{
			items : [
					"菜单栏：",
					"-",
					{
						text : '添加福袋',
						iconCls : 'add',
						handler : addDataWinOpen
					},
					"-",
					{
						text : '移除福袋',
						iconCls : 'delete',
						handler : deleteDataForBatch
					}
			]
		});
	
	var cb = new Ext.grid.CheckboxSelectionModel();
	
	var luckyBagGridPanel = new Ext.grid.GridPanel(
			{
				id : 'luckyBag.grid',
				renderTo : 'luckyBag_grid_div',
				autoExpandColumn : "luckyBagSku", // 自动伸展，占满剩余区域
				viewConfig : {
					forceFit : true
				},
				loadMask : true,
				frame : true,
				forceFit : true,
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
							id : 'luckyBag.grid.warehouseId',
							header : "仓库ID",
							width : 200,
							dataIndex : 'warehouseId'
						},
						{
							id : 'luckyBag.grid.luckyBagSku',
							header : "福袋SKU",
							width : 200,
							dataIndex : 'luckyBagSku'
						},
						{
							id : 'luckyBag.grid.luckyBagPrice',
							header : "福袋售价",
							width : 200,
							dataIndex : 'luckyBagPrice',
							renderer:function(value){ 
								if((value!=''&&value!=null)||value==0){
					             return new Number(value).toFixed(2);  
								}
					         }
						},
						{
							id : 'luckyBag.grid.addTime',
							header : "福袋添加时间",
							width : 200,
							dataIndex : 'addTime',
							renderer:function(value){ 
								if(value){
					             return Ext.util.Format.date(new Date(parseInt(value)),'Y-m-d H:i:s');  
								}
					         }
						},
						{
							id : 'luckyBag.grid.isValuable',
							header : "是否生效",
							width : 100,
							dataIndex : 'isValuable',
							renderer:function(value){ 
								if(value=='0'){
									return '无效';  
								}else if(value=='1'){
									return '生效';
								}
					         }
						},
						{
							header : "操作",
							width : 180,
							renderer : function() {
								return '   <input type="button" value="查看福袋" onclick="modifyLuckyBag();" /> <input type="button" value="查看商品" onclick="toModifyLuckyBagGoods();" />';
							}
						} ],
				listeners : {
					'rowdblclick' : function(grid, rowIndex, e) {
						modifyLuckyBag();
					}
				}
			});
	
	function clickResetButton() {
		luckyBagPanelForm.form.reset();
	}
	
	function clickSearchButton() {
		params['warehouseId'] = Ext.getCmp("luckyBag.panel.warehouseId").getValue();
		params['luckyBagSku'] = Ext.getCmp("luckyBag.panel.luckyBagSku").getValue();
		luckyBagGridPanel.store.load({
			params : params
		});
	}
	
	function deleteDataForBatch() {
		var recs = luckyBagGridPanel.getSelectionModel().getSelections();
		var num = recs.length;
		if (num == 0) {
			Ext.MessageBox.alert('提示', '请选择要移除的数据!');
			return;
		}
		Ext.MessageBox.confirm("提示","删除福袋同时删除该福袋下的子集和商品，确定删除吗？",function(btnId) {
			if (btnId == 'yes') {
				var luckyBagSkus = '';
				for ( var i = 0; i < recs.length; i++) {
					luckyBagSkus=luckyBagSkus+recs[i].get("luckyBagSku")+',';
				}
				Ext.Ajax.request({
					waitMsg : '请稍等.....',
					url : basePath + '/custom/luckyBag/batchDeleteLuckyBags.spmvc',
					params : {
						luckyBagSkus : luckyBagSkus
					},
					method : "post",
					success : function(response) {
						var result = Ext.util.JSON.decode(response.responseText);
						if(result.code=='1'){
							luckyBagGridPanel.store.reload();
						}else{
							alertMsg("结果", result.msg);
						}
					},
					failure : function() {
						errorMsg("结果", "删除失败！");
					}
				});
			}
		});
	}

	function addDataWinOpen(){
		FormEditWin.showAddDirWin("add_luckyBag_div", basePath
				+ "/custom/luckyBag/gotoAddLuckyBagPage.spmvc",
				"picADWinID", "新增福袋", 800, 450);
	}
	
	modifyLuckyBag = function(){
		var record = luckyBagGridPanel.getSelectionModel().getSelected();
		var luckyBagSku = record.get('luckyBagSku');
		var url = basePath + "/custom/luckyBag/gotoModifyLuckyBagPage.spmvc?luckyBagSku="+ luckyBagSku;
		FormEditWin.showAddDirWin(null, url, "picADWinID", "查看福袋", 800, 450);
	};
	
	toModifyLuckyBagGoods = function(){
		var record = luckyBagGridPanel.getSelectionModel().getSelected();
		var luckyBagSku = record.get('luckyBagSku');
		var warehouseId = record.get('warehouseId');
		var url = basePath + "/custom/luckyBag/gotoModifyLuckyBagGoodsPage.spmvc?luckyBagSku="+ luckyBagSku+"&warehouseId="+warehouseId;
		FormEditWin.showAddDirWin(null, url, "picADWinID", "查看商品", 900, 550);
	};
	
	function setResize() {
		var formHeight = luckyBagPanelForm.getHeight();
		var clientHeight = document.body.clientHeight;
		var clientWidth = document.body.clientWidth;
		luckyBagGridPanel.setHeight(clientHeight-formHeight-50);
		luckyBagGridPanel.setWidth(clientWidth-259);
	}
	window.onresize=function(){
		setResize();
	};
	setResize();
	
}
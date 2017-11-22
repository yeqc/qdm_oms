Ext.onReady(modifyLuckyGoodsBag);
function modifyLuckyGoodsBag(){
	Ext.QuickTips.init();
	Ext.form.Field.prototype.msgTarget = 'side';
	
	var record_start = 0;
	var pageSize = 15;

	var params = {};
	params["start"] = 0;
	params["limit"] = pageSize;
	
	var modifyLuckyGoodsBagPanelForm = new Ext.FormPanel({
		frame : true,
		forceFit : true,
		autoScroll : false,
		autoWidth : true,
		autoHeight : true,
		labelAlign : "right",
		waitMsgTarget : true,
		bodyBorder : false,
		applyTo : 'modify_luckybag_goods_panel_div',
		border : true,
		items : [{
			layout : 'column',
			border : false,
			items : [{
				columnWidth : .33,
				layout : 'form',
				items : [ {
					id : 'modifyLuckyGoodsBag.warehouseId',
					xtype : 'textfield',
					maxLength : 11,
					anchor : '100%',
					name : 'warehouseId',
					value : warehouseId,
					readOnly : true,
					fieldLabel : '仓库ID'
				}]
			},{
				columnWidth : .34,
				layout : 'form',
				items : [ {
					id : 'modifyLuckyGoodsBag.luckyBagSku',
					xtype : 'textfield',
					anchor : '100%',
					name : 'luckyBagSku',
					value : luckyBagSku,
					readOnly : true,
					fieldLabel : '福袋SKU'
				}]
			},{
				columnWidth : .33,
				layout : 'form',
				items : [ {
					id : 'modifyLuckyGoodsBag.subsetCode',
					store : new Ext.data.Store(
							{
								proxy : new Ext.data.HttpProxy(
										{
											url : basePath+ '/custom/luckyBag/getSubsetCodeListByWarehouseIdAndLuckyBagSku.spmvc?luckyBagSku='+luckyBagSku,
											method : 'POST'
										}),
								reader : new Ext.data.JsonReader({fields : ['code','name' ]})
							}),
					xtype : 'combo',
					valueField : 'code',
					displayField : 'name',
					name : 'subsetCode',
					mode : 'remote',
					forceSelection : true,
					emptyText : '--请选择--',
					editable : false,
					triggerAction : 'all',
					anchor : '100%',
					fieldLabel : '子集编码'
				}]
			}]
		}],
		buttonAlign : "center",
		buttons : [ {
			text : '查询',
			handler : search
		}, {
			text : '重置',
			handler : function() {
				modifyLuckyGoodsBagPanelForm.form.reset();
			}
		} ]
	});
			
	var record = Ext.data.Record.create([
	    { name : 'index' },
		{ name : 'id' },
		{ name : 'subsetCode' },
		{ name : 'sku' },
		{ name: 'goodsName'},
		{ name: 'goodsPrice'},
		{ name: 'preStockNumber'},
		{ name: 'actStockNumber'}
	]);
	
	this.proxy = new Ext.data.HttpProxy({
		url : basePath + '/custom/luckyBag/getLuckyBagGoodsListByWarehouseIdAndLuckyBagSku.spmvc?warehouseId='+warehouseId+'&luckyBagSku='+luckyBagSku,
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
	
	var toolbar = new Ext.Toolbar({
		items : [ "菜单栏：", "-", {
			id : 'modifyLuckyGoodsBag.delete',
			text : '移除商品',
			iconCls : 'delete',
			handler : deleteLuckyBagGoods
		},{
			id : 'modifyLuckyGoodsBag.import',
			text : '导入商品',
			iconCls : 'add',
			handler : importLuckyBagGoods
		},
		'<a style="color:red" href = "' + basePath
		+ '/page/luckyBag/lucky_bag_goods_import_template.xls'
		+ '">导入模板下载</a>',
		"-",
		{
			id : 'modifyLuckyGoodsBag.download',
			text : '导出所有商品',
			iconCls : 'copyTo',
			handler : downloadLuckyBagGoods
		}
		]
	});
	
	var cb = new Ext.grid.CheckboxSelectionModel();
	
	var modifyLuckyBagGoodsGrid = new Ext.grid.GridPanel({
		id : 'modifyLuckyGoodsBag.grid',
		renderTo : 'modify_luckybag_goods_grid_div',
		loadMask : true,
		autoExpandColumn : "goodsName", // 自动伸展，占满剩余区域
		height : 440,
		autoScroll : true,
		frame : true,
		tbar : toolbar,
		store : dataStore,
		autoScroll : true,
		viewConfig : {
			autoFill : true,
			forceFit : true
		},
		sm : cb,
		bbar : new IssPagingToolbar(dataStore, pageSize),
		columns : [ 
		    cb,
		    {
				header : "序号",
				width : 20,
				renderer : function(value, metadata, record,rowIndex) {
					return  record_start + 1 + rowIndex;
				}
			},
		    { header : "id", hidden: true, dataIndex : 'id' },
		    { header : "子集编码", width : 50, dataIndex : 'subsetCode' },
			{ header : "商品SKU", width : 50, dataIndex : 'sku' },
			{ header : "商品名称", width : 80, dataIndex : 'goodsName' },
			{ header : "吊牌价", width : 50, dataIndex : 'goodsPrice' },
			{ header : "库存预占量", width : 50, dataIndex : 'preStockNumber' },
			{ header : "库存量", width : 50, dataIndex : 'actStockNumber' },
		]
	});
	
	function search() {
		params["subsetCode"] = getValueById("modifyLuckyGoodsBag.subsetCode");
		modifyLuckyBagGoodsGrid.store.load({
			params : params
		});
	}
	
	function deleteLuckyBagGoods(){
		var recs = modifyLuckyBagGoodsGrid.getSelectionModel().getSelections();
		var num = recs.length;
		if (num == 0) {
			Ext.MessageBox.alert('提示', '请选择要移除的数据!');
			return;
		}
		Ext.MessageBox.confirm("提示","确定删除这些商品吗？",function(btnId) {
			if (btnId == 'yes') {
				var goodsRelationIds = '';
				for ( var i = 0; i < recs.length; i++) {
					goodsRelationIds=goodsRelationIds+recs[i].get("id")+',';
				}
				Ext.Ajax.request({
					waitMsg : '请稍等.....',
					url : basePath + '/custom/luckyBag/batchDeleteLuckyBagGoodsRelation.spmvc',
					params : {
						luckyBagSku : luckyBagSku,
						goodsRelationIds : goodsRelationIds
					},
					method : "post",
					success : function(response) {
						var result = Ext.util.JSON.decode(response.responseText);
						if(result.code=='1'){
							modifyLuckyBagGoodsGrid.store.reload();
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
	
	function importLuckyBagGoods(){
		var params = '&params=warehouseId:'+warehouseId+';luckyBagSku:'+luckyBagSku;
		var uploadUrl = basePath + '/custom/luckyBag/uploadLuckyBagGoods.spmvc'+params;
		FormEditWin.showAddDirWin("import_lucky_bag_goods", basePath
				+ "/fileUpload.html?type=1&fileTypes=*.xls&uploadUrl=" + uploadUrl,
				"picADWinID", "导入商品", 580, 300);
	}
	
	function downloadLuckyBagGoods(){
		var path = basePath + '/custom/luckyBag/downLoadLuckyBagGoods.spmvc?warehouseId='+warehouseId+'&luckyBagSku='+luckyBagSku;
		window.location.href = path;
	}
	
	doAfter = function(data){
		var result = Ext.util.JSON.decode(data);
		if(result.code!='1'){
			FormEditWin.close();
			alertMsg("结果", result.msg);
			return;
		}
		modifyLuckyBagGoodsGrid.store.reload();
		parent.Ext.getCmp("luckyBag.grid").getStore().reload();
		FormEditWin.close();
	};
	
}
var localObj = window.location;
var contextPath = localObj.pathname.split("/")[1];
var path = localObj.protocol + "//" + localObj.host + "/" + contextPath;
importJs("DateTimeField.js");
Ext.onReady(function() {

	var isExpand = true;

	Ext.QuickTips.init();
	// 查询区域的高度
	var maxSelectHeight = 125;
	var minSelectHeight = 25;
	var toolbarHeight = 25;

	setDefault();

	// 每页记录数
	var limit = 20;

	// 渠道下拉菜单
	var channelStore = createJsonStore('list',
			[ 'channelCode', 'channelName' ], path
					+ "/scChannel/getScChannelList.htm");
	channelStore.load();
	var channelRecord = Ext.data.Record
			.create([ 'channelCode', 'channelName' ]);
	var channelRecord_data = new channelRecord({
		channelCode : '',
		channelName : '请选择...'
	});

	// 渠道下拉框对象
	var channelComboBox = createComboBox(channelStore, 'channel',
			'channelName', 'channelCode', 'local', '');

	// checkBoxGroup
	var checkboxItems = [];
	checkboxItems.push({
		boxLabel : '含预售',
		name : 'containVirStock',
		inputValue : 1
	});
	checkboxItems.push({
		boxLabel : '含强占',
		name : 'containPrivateStock',
		inputValue : 2
	});

	var checkGroup = new Ext.form.CheckboxGroup({
		xtype : 'checkboxgroup',
		columns : 2,
		id : 'containGroup',
		name : 'containGroup',
		columns : [ 100, 100 ],
		vertical : true,
		items : checkboxItems
	});

	var disp_w = 80;
	var test_w = 100;

	// 初始请求
	var selectform = new Ext.form.FormPanel({
		title : "查询条件",
		frame : true,
		renderTo : "condition",
		collapsible : true,
		labelWidth : 60,
		labelAlign : 'right',
		keys : {
			key : Ext.EventObject.ENTER,
			fn : function(btn, e) {
				getQuery();
			}
		},
		items : [ {
			xtype : 'compositefield',
			items : [ {
				xtype : "displayfield",
				value : "商品编码：",
				width : disp_w
			}, {
				xtype : "textfield",
				name : "sku",
				id : "sku",
				width : test_w
			}, {
				xtype : "displayfield",
				value : "渠道：",
				width : 50
			}, channelComboBox, {
				xtype : "displayfield",
				value : "渠道未同步时间点：",
				width : 120
			}, {
				xtype : "datefield",
				width : 160,
				id : "channelLastSyncTimeBefore",
				name : "channelLastSyncTimeBefore",
				format : "Y-m-d H:i:s"
			}, {
				xtype : "displayfield",
				value : "最后更新时间点：",
				width : 100
			}, {
				xtype : "datefield",
				width : 160,
				id : "lastUpdataTimeBefore",
				name : "lastUpdataTimeBefore",
				format : "Y-m-d H:i:s"
			} ]
		},

		{
			xtype : 'compositefield',
			items : [ {
				xtype : "displayfield",
				value : "可用库存范围：",
				width : 100
			}, {
				xtype : "textfield",
				name : "usingStockStart",
				id : "usingStockStart",
				width : test_w
			}, {
				xtype : "displayfield",
				value : "~",
				width : 10
			}, {
				xtype : "textfield",
				name : "usingStockEnd",
				id : "usingStockEnd",
				width : 100
			}, {
				xtype : "displayfield",
				value : "",
				width : 10
			}, {
				xtype : "displayfield",
				value : "",
				width : 50
			}, checkGroup ]
		} ],
		buttons : [ {
			text : "查询",
			handler : function() {
				getQuery();
			}
		}, {
			text : "导出",
			handler : function() {
				getQuery();
			}
		}, {
			text : "重置",
			handler : function() {
				selectform.getForm().reset();
			}
		} ],
		buttonAlign : "right"
	});

	// 展开
	selectform.on("expand", function() {
		isExpand = true;
		setColl();
	});

	// 收缩
	selectform.on("collapse", function() {
		isExpand = false;
		setColl();
	});

	// CheckboxSelectionModel
	var sm = new Ext.grid.CheckboxSelectionModel({
		checkOnly : true,
		singleSelect : false
	});

	var columns = [ sm, new Ext.grid.RowNumberer({
		header : "序号",
		width : 35,
		renderer : function(value, metadata, record, rowIndex) {
			return getRecordStart() + 1 + rowIndex;
		}
	}), {
		header : '商品11位码',
		dataIndex : 'dsku',
		align : "center",
		sortable : true,
		renderer : function(value, metadata, record) {
			if (value != undefined) {
				return getLeftFormate(value);
			}
		}
	}, {
		header : '真实库存',
		dataIndex : 'stock',
		align : "center",
		sortable : true,
		renderer : function(value, metadata, record) {
			if (value != undefined) {
				return getLeftFormate(value);
			}
		}
	}, {
		header : '虚拟库存',
		dataIndex : 'virStock',
		align : "center",
		sortable : true,
		width : 150,
		renderer : function(value, metadata, record) {
			if (value != undefined) {
				return getLeftFormate(value);
			}
		}
	}, {
		header : '虚拟冻结库存',
		dataIndex : 'virFrozenStock',
		align : "center",
		sortable : true,
		renderer : function(value, metadata, record) {
			if (value != undefined) {
				return getLeftFormate(value);
			}
		}
	}, {
		header : '可共享库存',
		dataIndex : 'shareStock',
		align : "center",
		sortable : true,
		renderer : function(value, metadata, record) {
			if (value != undefined) {
				return getLeftFormate(value);
			}
		}
	} ];

	var structure = {};

	var products = [];
	var fields = [];
	var data = [];
	var channelGroupRow = [];
	// 先设置前6列
	channelGroupRow.push({
		header : '',
		colspan : 7,
		dataIndex : '',
		align : "left",
		sortable : true,
		renderer : function(value, metadata, record) {
			return '';
		}
	});

	// 汇总信息的四个列
	fields.push({
		name : 'id'
	}, {
		name : 'dsku'
	}, {
		name : 'stock'
	}, {
		name : 'virStock'
	}, {
		name : 'virFrozenStock'
	}, {
		name : 'shareStock'
	});

	var v1 = '渠道强占库存';
	var v2 = '渠道强占冻结库存';
	var v3 = '渠道可售库存';
	var v4 = '渠道最后一次同步库存';
	var v5 = '最后一次同步时间';

	var arrayValue = new Array(v1, v2, v3, v4, v5);
	var map = new HashMap();

	var grid;
	var store;

	// 先等待渠道数据的加载完成，再去渲染页面数据
	channelStore.on("load", function(obj, record) {

		channelStore.insert(0, channelRecord_data);
		channelComboBox.setValue("");

		var channelData = [];
		var count = record.length;
		for ( var i = 0; i < count; i++) {
			var temp = record[i];
			var channelName = temp.get('channelName');
			var channelCode = temp.get('channelCode');

			// 各个渠道的名称
			channelData.push(channelName);

			// 添加每个渠道对应的4个列名
			var c1 = channelCode + 'privateStock';
			var c2 = channelCode + 'privateFrozenStock';
			var c3 = channelCode + 'keshouStock';
			var c4 = channelCode + 'stock';
			var c5 = channelCode + 'lastUpdateTime';

			map.put(c1, v1);
			map.put(c2, v2);
			map.put(c3, v3);
			map.put(c4, v4);
			map.put(c5, v5);
		}

		// 渠道名称
		structure.channel = channelData;
		Ext.iterate(structure, function(continent, channels) {
			Ext.each(channels, function(channel) {
				channelGroupRow.push({
					header : channel,
					colspan : arrayValue.length,
					align : 'center'
				});
			});
		});

		var keys = map.keys();
		for ( var j = 0; j < keys.length; j++) {
			var key = keys[j];
			if (key != null && key != "") {
				var value = map.get(key);

				fields.push({
					name : key
				});
				columns.push({
					dataIndex : key,
					header : value,
					width : 150,
				});
			}
			;
		}

		var group = new Ext.ux.grid.ColumnHeaderGroup({
			rows : [ channelGroupRow ]
		});

		// 列表数据
		store = new Ext.data.JsonStore({
			storeId : "dataStore",
			fields : fields,
			url : path + '/stockImageQuery.htm',
			root : 'list',
			idProperty : 'dsku',
			remoteSort : true,
			totalProperty : 'total'
		});

		store.on("beforeload", function(obj, opt) {
			var body = Ext.getBody();
			body.mask('Loading', 'x-mask-loading');
		});
		store.on("load", function(obj, record) {
			var body = Ext.getBody();
			body.unmask();
		});

		// 列表尾部工具条
		var foot_tb = new Ext.Toolbar({
			items : [ {
				xtype : "tbfill"
			}, "-", {
				id : "batchUpadta",
				text : "批量更新库存镜像",
				iconCls : "add",
				handler : function() {
					batchUpadta();
				}
			}, {
				id : "fullUpadta",
				text : "全量更新库存镜像",
				iconCls : "add",
				handler : function() {
					fullUpadta();
				}
			} ],
			style : 'padding-right:20px'
		});

		// 列表
		var model = getGridPanelModel("grid0", "库存镜像查询列表", columns, store,
				limit, Ext.get("grid"), sm, foot_tb);
		model.plugins = group;
		grid = createGridPanelByModel(model);
		setDefaultPage(0, limit, store);

		// 分页工具条
		var toolBar = createPagingToolbar(store, limit);
		toolBar.setHeight(toolbarHeight);
		toolBar.render("toolBar");
	});

	function setDefault() {
		var clientHeight = document.body.clientHeight;

		Ext.get("condition").setHeight(maxSelectHeight);
		Ext.get("grid").setHeight(
				clientHeight - maxSelectHeight - toolbarHeight);
	}

	function setColl() {
		setResize();
	}

	function setResize() {
		var clientHeight = document.body.clientHeight;
		var clientWidth = document.body.clientWidth;
		var height = isExpand == true ? maxSelectHeight : minSelectHeight;

		Ext.get("condition").setHeight(height);
		Ext.get("grid").setHeight(clientHeight - height - toolbarHeight);
		grid.setHeight(clientHeight - height - toolbarHeight);

		Ext.get("condition").setWidth(clientWidth);
		Ext.get("grid").setWidth(clientWidth);
		grid.setWidth(clientWidth);
	}

	window.onresize = function() {
		setResize();
	};

	// 查询
	function getQuery() {

		// 商品编码
		var sku = Ext.fly('sku').getValue();

		// 渠道编码
		var channelCode = channelComboBox.getValue();

		// 渠道未同步开始时间
		var channelLastSyncTimeBefore = Ext.fly('channelLastSyncTimeBefore')
				.getValue();

		// 可用库存范围
		var usingStockStart = Ext.fly('usingStockStart').getValue();
		var usingStockEnd = Ext.fly('usingStockEnd').getValue();

		// 包含预售
		var containVirStock = null;

		// 包含强占
		var containPrivateStock = null;

		// 是否含预售或强占
		for ( var i = 0; i < checkGroup.items.length; i++) {
			if (checkGroup.items.itemAt(i).checked) {
				if (checkGroup.items.itemAt(i).name == "containVirStock") {
					containPresell = checkGroup.items.itemAt(i).getValue();
				}

				if (checkGroup.items.itemAt(i).name == "containPrivateStock") {
					containGrab = checkGroup.items.itemAt(i).getValue();
				}
			}
		}

		// 渠道未同步开始时间
		var lastUpdataTimeBefore = Ext.fly('lastUpdataTimeBefore').getValue();

		check = true;
		var store = Ext.StoreMgr.lookup("dataStore");
		setDefaultPage(0, limit, store);

		store.setBaseParam("sku", sku);
		store.setBaseParam("channelCode", channelCode);
		store.setBaseParam("channelLastSyncTimeBefore",
				channelLastSyncTimeBefore);
		store.setBaseParam("usingStockStart", usingStockStart);
		store.setBaseParam("usingStockEnd", usingStockEnd);
		store.setBaseParam("containVirStock", containVirStock);
		store.setBaseParam("containPrivateStock", containPrivateStock);
		store.setBaseParam("lastUpdataTimeBefore", lastUpdataTimeBefore);
		store.load();
	}

	// 批量更新镜像
	function batchUpadta() {
		var rows = grid.getSelectionModel().getSelections();
		var leng = rows.length;
		var checkData = [];
		for ( var i = 0; i < leng; i++) {
			var record = rows[i];
			checkData.push(record.get("dsku"));
		}

		var url = path + "/imageBatchUpadta.htm";
		var param = {
			'skus' : checkData
		};

		doSubmit(url, doSuccess, param);
	}

	// 全量更新库存镜像
	function fullUpadta() {
		var url = path + "/imageFullUpadta.htm";
		var param = {};
		doSubmit(url, doSuccess, param);
	}

	// 同步库存成功回调函数
	function doSuccess() {
		Ext.getBody().unmask();
		createAlert("提示信息", "操作成功 !", function() {
			parent.tempLocalData = [];
			close();
		});
	}
});
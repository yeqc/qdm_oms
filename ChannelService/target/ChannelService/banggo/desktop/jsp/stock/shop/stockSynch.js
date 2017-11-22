var localObj = window.location;
var contextPath = localObj.pathname.split("/")[1];
var path = localObj.protocol + "//" + localObj.host + "/" + contextPath;

// 查询form
var selectform;
var isExpand = true;

Ext
		.onReady(function() {
			Ext.QuickTips.init();
			// 查询区域的高度
			var maxSelectHeight = 95;
			var minSelectHeight = 25;
			var toolbarHeight = 25;

			setDefault();

			// 每页记录数
			var limit = 20;

			// 记录是否查询过
			var check = false;

			// 字段
			var fields = [];

			// 品牌
			var typeComboBox = getBrandCombox();

			// 渠道
			var channelStore = createJsonStore('list', [ 'channelCode',
					'channelName' ], path + "/scChannel/getScChannelList.htm");
			channelStore.load();

			// 默认选择
			var channelRecord = Ext.data.Record.create([ 'channelCode',
					'channelName' ]);
			var channelRecord_data = new channelRecord({
				channelCode : '',
				channelName : '请选择...'
			});

			// ERP的同步方式也座位一个渠道去处理，!!!!这里的repFlag是写死,需要特别注意前后台的统一
			var channelRecord_ERP = new channelRecord({
				channelCode : 'repFlag',
				channelName : 'ERP'
			});

			channelStore.on("load", function(obj, record) {
				channelStore.insert(0, channelRecord_data);
				channelStore.insert(1, channelRecord_ERP);
				channelComboBox.setValue("");
				addColumns();
			});

			var channelComboBox = createComboBox(channelStore, 'channel',
					'channelName', 'channelCode', 'local', '');

			// 查询条件
			selectform = new Ext.form.FormPanel({
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
						xtype : "textfield",
						name : "q_sku",
						id : "q_sku",
						width : 150,
						fieldLabel : "商品码"
					}, {
						xtype : 'displayfield',
						value : '商品名称:',
						width : 60
					}, {
						xtype : "textfield",
						name : "goodsName",
						id : "goodsName",
						width : 150
					}, {
						xtype : 'displayfield',
						value : '品类:',
						width : 60
					}, typeComboBox ]
				} ],
				buttons : [ {
					text : "查询",
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

			var fm = Ext.form;

			var sm = new Ext.grid.CheckboxSelectionModel({
				checkOnly : true,
				singleSelect : false
			});

			var comboData = {
				'0' : '人工同步',
				'1' : '自动同步'
			};

			var combosStore = new Ext.data.ArrayStore({
				fields : [ 'id', 'name' ],
				data : [ [ '0', '人工同步' ], [ '1', '自动同步' ] ]
			});

			var columns = [ sm, new Ext.grid.RowNumberer({
				header : "序号",
				width : 35,
				renderer : function(value, metadata, record, rowIndex) {
					return getRecordStart() + 1 + rowIndex;
				}
			}), {
				header : '商品11位码',
				dataIndex : 'sku',
				width : 120,
				align : "center",
				sortable : true,
				renderer : function(value) {
					return getLeftFormate(value);
				}
			}, {
				header : '商品名称',
				dataIndex : 'name',
				width : 240,
				align : "center",
				sortable : true,
				renderer : function(value, metadata, record) {
					return getLeftFormate(value);
				}
			}, {
				header : '颜色',
				dataIndex : 'color',
				width : 70,
				align : "center",
				sortable : true,
				renderer : function(value, metadata, record) {
					return getLeftFormate(value);
				}
			}, {
				header : '尺码',
				dataIndex : 'size',
				align : "center",
				sortable : true,
				renderer : function(value, metadata, record) {
					return getLeftFormate(value);
				}
			} ];

			var model;
			var grid;

			function addColumns() {

				fields.push({
					name : 'sku'
				}, {
					name : 'name'
				}, {
					name : 'color'
				}, {
					name : 'size'
				});

				var count = channelStore.getCount();
				for ( var i = 1; i < count; i++) {
					var data = channelStore.getAt(i);

					// 匹配后台字段和前台的关键字
					var channelCode = data.get('channelCode');
					fields.push({
						name : channelCode
					});

					var header = {
						dataIndex : channelCode,
						align : "center",
						width : 200,
						sortable : true,
						editor : new Ext.grid.GridEditor(new Ext.form.ComboBox(
								{
									store : combosStore,
									displayField : 'name',
									valueField : 'id',
									typeAhead : true,
									mode : 'local',
									forceSelection : true,
									triggerAction : 'all',
									emptyText : '',
									selectOnFocus : true,
								})),
						renderer : function(value, metadata, record) {
							Ext.iterate(comboData,
									function(id, name) {
										if (value != null && id != null
												&& value == id) {
											value = name;
										}
									});
							return getLeftFormate(value);
						}
					};
					header.header = data.get('channelName');
					columns.push(header);
				}

				var cm = new Ext.grid.ColumnModel({
					columns : columns
				});

				// 列表数据
				var store = new Ext.data.JsonStore({
					storeId : "dataStore",
					fields : fields,
					url : path + '/syncModeQuery.htm',
					root : 'list',
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

				setDefaultPage(0, limit, store);

				model = getEditGridPanelModel("grid0", "库存同步列表", cm, store,
						limit, Ext.get("grid"), sm, foot_tb);
				grid = createEditGridPanelByModel(model);

				grid.on('afteredit', afterEdit, this);

				// 修改指定SKU在某个渠道上的同步方式
				function afterEdit(e) {
					var record = store.getAt(e.row);
					if (record == null || record == '') {
						return;
					}

					// alert("record=" + record.get('sku') + " " + "选择后的值:"
					// + e.value + " " + "当前编辑的字段名:" + e.field);

					var sku = record.get('sku');
					var comboValue = e.value;
					var channelCode = e.field;

					var url = path + "/modifySingleSkuSyncMode.htm";
					var param = {
						'sku' : sku,
						'channel' : channelCode,
						'syncMode' : comboValue
					};
					doSubmit(url, doSuccess, param);

				}

				// 分页工具条
				var toolBar = createPagingToolbar(store, limit);
				toolBar.setHeight(toolbarHeight);
				toolBar.render("toolBar");
			}

			// 同步类型下拉框对象
			var synchTypeComboBox = createComboBox(combosStore, 'synchType',
					'name', 'id', 'local', '');
			synchTypeComboBox.setValue("0");

			// 列表尾部工具条
			var foot_tb = new Ext.Toolbar({
				items : [ {
					xtype : "tbfill"
				}, {
					xtype : 'tbtext',
					text : '渠道:'
				}, channelComboBox, {
					xtype : 'tbtext',
					text : '同步类型:'
				}, synchTypeComboBox, "-", {
					id : "add",
					text : "批量修改同步方式",
					iconCls : "add",
					handler : function() {
						modifyMultiSkusSyncMode();
					}
				} ],
				style : 'padding-right:20px;'
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
				var height = isExpand == true ? maxSelectHeight
						: minSelectHeight;

				Ext.get("condition").setHeight(height);
				Ext.get("grid")
						.setHeight(clientHeight - height - toolbarHeight);
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

				// 查询条件
				var q_sku = Ext.fly('q_sku').getValue();

				// 商品名称
				var goodsName = Ext.fly('goodsName').getValue();

				// 品类
				var stockChangeType = typeComboBox.getValue();

				// 查询条件不能全部为空
				var param1 = (q_sku == null || q_sku == '' || q_sku == undefined);
				var param2 = (goodsName == null || goodsName == '' || goodsName == undefined);
				var param3 = (stockChangeType == null || stockChangeType == '' || stockChangeType == undefined);

				if (param1 && param2 && param3) {
					createAlert('提示信息', "查询条件不能全部为空!");
					return false;
				}

				check = true;
				var store = Ext.StoreMgr.lookup("dataStore");
				setDefaultPage(0, limit, store);

				store.setBaseParam("sku", q_sku);
				store.setBaseParam("goodsName", goodsName);
				store.setBaseParam("brand", stockChangeType);
				store.load();
			}

			// 批量同步多个SKU在某个渠道上的同步方式
			function modifyMultiSkusSyncMode() {
				var selectRecords = sm.getSelections();
				var skus = '';
				if (selectRecords == undefined || selectRecords == null
						|| selectRecords.length == 0) {
					createAlert('提示信息', "请选择需要批量操作的商品!");
					return false;

				}

				var channelCode = channelComboBox.getValue();
				if (channelCode == undefined || channelCode == null
						|| channelCode == '') {
					createAlert('提示信息', "请选择需要批量操作的渠道数据!");
					return false;
				}

				var syncMode = synchTypeComboBox.getValue();
				if (syncMode == undefined || syncMode == null || syncMode == '') {
					createAlert('提示信息', "请选择批量数据的同步类型!");
					return false;
				}

				for ( var i = 0; i < selectRecords.length; i++) {
					skus += selectRecords[i].get("sku");
					if (i < selectRecords.length - 1) {
						skus += ",";
					}

				}

				var url = path + "/modifyMultiSkusSyncMode.htm";
				var param = {
					'skus' : skus,
					'channelCode' : channelCode,
					'syncMode' : syncMode
				};
				doSubmit(url, doSuccess, param);

			}

			// 同步库存成功回调函数
			function doSuccess(frm, action) {

			}

		});
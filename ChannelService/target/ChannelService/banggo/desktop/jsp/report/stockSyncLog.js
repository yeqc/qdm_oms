var localObj = window.location;
var contextPath = localObj.pathname.split("/")[1];
var path = localObj.protocol + "//" + localObj.host + "/" + contextPath;

importJs("DateTimeField.js");
// 查询form
var selectform;
var isExpand = true;

Ext
		.onReady(function() {
			Ext.QuickTips.init();
			// 查询区域的高度
			var maxSelectHeight = 95;
			var minSelectHeight = 25;

			setDefault();

			// 每页记录数
			var limit = 20;

			// 记录是否查询过
			var check = false;

			// 设置查询条件的日期
			var date = new Date();
			date.setMinutes(0);
			date.setSeconds(0);
			var now = date.dateFormat('Y-m-d H:i:s');

			var hour = date.getHours();
			date.setHours(hour - 1);
			var before = date.dateFormat('Y-m-d H:i:s');

			// 初始请求
			selectform = new Ext.form.FormPanel({
				title : "查询条件",
				frame : true,
				renderTo : "condition",
				collapsible : true,
				labelWidth : 120,
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
						name : "goodsSn",
						id : "goodsSn",
						width : 150,
						fieldLabel : "商品编码"
					}, {
						xtype : "displayfield",
						value : "开始时间",
						width : 60
					}, {
						xtype : "datefield",
						width : 150,
						id : "startTime",
						name : "startTime",
						format : "Y-m-d H:i:s",
						value : before
					}, {
						xtype : "displayfield",
						value : "结束时间",
						width : 60
					}, {
						xtype : "datefield",
						width : 150,
						id : "endTime",
						name : "endTime",
						format : "Y-m-d H:i:s",
						value : now
					} ]
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

			// 列表数据
			var store = new Ext.data.JsonStore({
				storeId : "dataStore",
				fields : [ {
					name : 'sku'
				}, {
					name : 'stock'
				}, {
					name : 'channelCode'
				}, {
					name : 'syncTime'
				}, {
					name : 'eventSource'
				} ],
				url : path + '/changLogQuery.htm',
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

			var columns = [ new Ext.grid.RowNumberer({
				header : "序号",
				width : 35,
				renderer : function(value, metadata, record, rowIndex) {
					return getRecordStart() + 1 + rowIndex;
				}
			}), {
				header : 'SKU',
				dataIndex : 'sku',
				width : 120,
				align : "center",
				sortable : true,
				renderer : function(value) {
					return getLeftFormate(value);
				}
			}, {
				header : '库存数量',
				dataIndex : 'stock',
				width : 120,
				align : "center",
				sortable : true,
				renderer : function(value) {
					return getLeftFormate(value);
				}
			}, {
				header : '渠道',
				dataIndex : 'channelCode',
				align : "center",
				sortable : true,
				renderer : function(value, metadata, record) {
					return getLeftFormate(value);
				}
			}, {
				header : '同步时间',
				dataIndex : 'syncTime',
				width : 180,
				align : "center",
				sortable : true,
				renderer : function(value) {

					// 格式化日期再显示
					var dt = formatDateformate(value, 'Y-m-d H:i:s');
					return getLeftFormate(dt);
				}
			}, {
				header : '触发来源',
				dataIndex : 'eventSource',
				width : 180,
				align : "center",
				sortable : true,
				renderer : function(value) {
					return getLeftFormate(value);
				}
			} ];

			// 列表
			var model = getGridPanelModel("grid0", "查询列表", columns, store,
					limit, Ext.get("grid"));
			var grid = createGridPanelByModel(model);
			setDefaultPage(0, limit, store);

			// 查询
			function getQuery() {
				// 开始时间
				var beginTime = Ext.fly('startTime').getValue();

				// 结束时间
				var endTime = Ext.fly('endTime').getValue();

				var flag1 = (beginTime == undefined || beginTime == null || beginTime == '');
				var flag2 = (endTime == undefined || endTime == null || endTime == '');

				if (!flag1 && !flag2 && endTime < beginTime) {
					createAlert('提示信息', "开始时间不能大于结束时间!");
					return false;
				}

				// 商品编码
				var goodsSn = getById('goodsSn');
				var flag3 = (goodsSn == undefined || goodsSn == null || goodsSn == '');
				if (flag1 && flag2 && flag3) {
					createAlert('提示信息', "查询条件不能全部为空!");
					return false;
				}

				check = true;
				var store = Ext.StoreMgr.lookup("dataStore");
				setDefaultPage(0, limit, store);

				store.setBaseParam("goodsSn", goodsSn);
				store.setBaseParam("beginTime", beginTime);
				store.setBaseParam("endTime", endTime);
				store.load();
			}

			function setDefault() {
				var clientHeight = document.body.clientHeight;
				Ext.get("condition").setHeight(maxSelectHeight);
				Ext.get("grid").setHeight(clientHeight - maxSelectHeight);
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
				Ext.get("grid").setHeight(clientHeight - height);
				grid.setHeight(clientHeight - height);

				Ext.get("condition").setWidth(clientWidth);
				Ext.get("grid").setWidth(clientWidth);
				grid.setWidth(clientWidth);
			}

			window.onresize = function() {
				setResize();
			};
		});
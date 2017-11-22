function changeShopBusinessGoodsStatus(val) {

	if ("1" == val) {
		return "<font color='green'>上架</font>";
	} else if ("2" == val) {
		return "<font color='red'>下架</font>";
	}
}

//搜索界面
var shopBusinessGoodsForm;
//列表显示界面
var shopBusinessGoodsGrid;
var shopCode = getGlobalValue("shopCode"); //店铺code
var date = new Date().format('Y-m-d');
Ext.onReady(function() {
			shopbusinessGoods = function() { 
			    //店铺代码
			    var shopCodeStr = getGlobalValue("shopCode");
			    var channelCodeStr = getGlobalValue("channelCode");

				var shopBusinessGoodStore;
				var couponproxy;
				var couponreader;
				var checkBoxSelect;
				var sbgColumnGrid;
				var tbar;
				var pageSize = 15;
				return {
					init : function() {

					   //上下架
				/*		var typeOptionCombo = new Ext.form.ComboBox({
							id : 'shopCode',
							store :  new Ext.data.Store({
								proxy : new Ext.data.HttpProxy({
									url : basePath + '/custom/channelInfo/channelList.spmvc',
									method : 'GET'
								}),
								reader : new Ext.data.JsonReader({
									fields : [ 'shopCode', 'shopName' ]
								})
							}),
							xtype : 'combo',
							valueField : 'chanelCode',
							displayField : 'channelTitle',
							mode : 'remote',
							forceSelection : true,
							emptyText : '请选择',
							editable : false,			
							triggerAction : 'all',
							fieldLabel : '上下架状态',
							width : 200
						});*/
						
						//单据类型选择框
					    var typeOptionCombo = new Ext.form.ComboBox( {
							id : 'status',
							store : new Ext.data.SimpleStore( {
								data : [ [ '1', '上架' ], [ '2', '下架' ], [ '3', '已售完' ]],
								fields : [ 'text', 'filed' ]
							}),
							xtype : 'combo',
							valueField : 'text',
							displayField : 'filed',
						//	labelAlign : 'left',
							//valueField :'2',
							value:'1',
							mode : 'local',
							forceSelection : true,
							name : 'status',
							editable : false,
							hiddenName : 'status',
							triggerAction : 'all', 
							fieldLabel : '上下架状态',
							width : 150
						});
						var hiddenAble = false;
							shopBusinessGoodsForm = new Ext.FormPanel({
								labelAlign : 'top',
								frame : true,
								bodyStyle : 'padding:5px 5px 0',
								//autoHeight : true,
	//							autoScroll : true,
	//							autoWidth : true,
								url : "",
								labelAlign : 'right',
								layout : 'form',
								items : [{
									layout : 'column',
									items : [{
										layout : 'form',
										labelWidth : 80,
										columnWidth : 0.3,
										items : [ {
												id : 'goodsSn',
												hidden: hiddenAble,
												xtype : 'textfield',
												fieldLabel : '经营商品编码',
												name : 'goodsSn', 
												width : '150'
											}
										]
									} , 
									{
										layout : 'form',
										labelWidth : 80,
										columnWidth : 0.3,
										items : [ typeOptionCombo ]
									}]
								} ],
								buttons : [ {
									text : '查询',
									columnWidth : 0.1,
									handler : this.search
								}, {
									text : '重置',
									columnWidth : 0.1,
									handler : this.reset
								} ]
							});
						checkBoxSelect = new Ext.grid.CheckboxSelectionModel();

						sbgColumnGrid = new Ext.grid.ColumnModel([
								checkBoxSelect, {
									id : 'id',
									header : "ID",
									align : "center",
									align : "center",
									width : 80,
									hidden : true,
									dataIndex : 'id'
								}, {
									id : 'shopCode',
									align : "center",
									sortable : true,
									header : "店铺编号",
									dataIndex : 'shopCode'
								}, {
									id : 'shopName',
									header : "店铺名称",
									align : "center",
									align : "center",
									width : 120,
									dataIndex : 'shopName'
								},{
									id : 'goodsSn',
									header : "商品款号",
									align : "center",
									align : "center",
									dataIndex : 'goodsSn'
								},

								{
									id : 'goodsName',
									header : "商品名称",
									align : "center",
									align : "center",
									width : 200,
									dataIndex : 'goodsName'
								},

								{
									id : 'price',
									header : "价格",
									align : "center",
									align : "center",
									dataIndex : 'price'
								},{
									id : 'status',
									header : "上下架状态",
									align : "center",
									align : "center",
									dataIndex : 'status',
									renderer : changeShopBusinessGoodsStatus
								}
								/*,{
									id : 'stockCount',
									header : "库存",
									align : "center",
									dataIndex : 'stockCount'
								}*/
						]);

						// 与列对应的dataIndex
						couponrecord = Ext.data.Record.create([ 
							{
							name : 'id'
						}, {
							name : 'shopCode'
						}, {
							name : 'shopName'
						}, {
							name : 'goodsSn'
						}, {
							name : 'goodsName'
						}, {
							name : 'price'
						}, {
							name : 'status'
						}
						/*, {
							name : 'stockCount'
						}*/
						]);

						couponproxy = new Ext.data.HttpProxy(
								{
									url : basePath+ "/custom/shopBusinessGoods/getShopBusinessGoods.spmvc",
									timeout: '300000',
									method : "post"
								});
						// Reader 读json中数据
						couponreader = new Ext.data.JsonReader({
							root : 'root',
							totalProperty : 'totalProperty'
						}, couponrecord);

						shopBusinessGoodStore = new Ext.data.Store({
							autoLoad : {
								params : {
									start : 0,
									limit : pageSize,
									status : 1
								}
							},
							proxy : couponproxy,
							reader : couponreader,
							listeners:{   
						        exception:function(dataProxy, type, action, options, response, arg) {
						        	 alertMsg("提示","请求超时!");
						        }
						    }
						});
						 // 如果不是用ajax的表单封装提交,就要做如下操作.
						//这里很关键，如果不加，翻页后搜索条件就变没了，这里的意思是每次数据载入前先把搜索表单值加上去，这样就做到了翻页保留搜索条件了   
						shopBusinessGoodStore.on('beforeload', function(){
							var status = Ext.getCmp('status').getValue();
							var goodsSn = "";
							if("TB_CHANNEL_CODE" != channelCodeStr && "BG_CHANNEL_CODE" != channelCodeStr
								&& "PP_CHANNEL_CODE" != channelCodeStr && "TMALL_CHANNEL_CODE" != channelCodeStr){
								goodsSn = Ext.getCmp("goodsSn").getValue();
							}
							Ext.apply(this.baseParams, {shopCode: shopCodeStr,channelCode:channelCodeStr, status: status, goodsSn: goodsSn});
						});
						

						// 定义菜单栏
						tbar = [
								{
									id : 'message_grid_tBar@export',
									text : '导出当前商品',
									tooltip : '导出',
									iconCls : 'add',
									handler : function() {
										var status = Ext.getCmp("status").getValue();
										FormEditWin.showAddDirWin("exprot_goods_win", basePath+ "/custom/shopBusinessGoods/exprotShopbusinessGoods.spmvc?shopCode="+shopCodeStr+"&channelCode="+channelCodeStr+"&status="+status,"picADWinID", "导出商品", 400, 300);
									}
								}
								];

						shopBusinessGoodsGrid = new Ext.grid.GridPanel(
								{
					
									//width : 1000,
									height:500,
									title : '店铺经营商品列表',
									store : shopBusinessGoodStore,
									id : 'addshopBusinessGoodsForm_grid_id',
									trackMouseOver : false,
									disableSelection : true,
					//				autoWidth : true,
									loadMask : true,
									frame : true,
									autoExpandColumn: "shopName", //自动伸展，占满剩余区域
									columnLines : true,
									tbar : tbar,
									// grid columns
									cm : sbgColumnGrid,
									sm : checkBoxSelect,
									// paging bar on the bottom
									bbar : new IssPagingToolbar(shopBusinessGoodStore,
											pageSize),
									listeners : {
										'rowdblclick' : function(thisgrid,
												rowIndex, e) {
											var selectionModel = thisgrid
													.getSelectionModel();
											var record = selectionModel
													.getSelected();
											var id = record.data['ID'];

											// FormEditWin.showAddDirWin("popWins",basePath+"macalline/notices/toEditInNoticesPage.action?id="+id,"pop_message_winID","编辑查看通知单信息",1350,580);

										}
									}
								});

					},
					
					refresh:function(){
						    shopBusinessGoodStore.reload();					
					},
					show : function() {

						couponPanel = new Ext.Panel({
							renderTo : 'shopBusinessGoodsList-grid',
							layout : 'column',
							items : [ shopBusinessGoodsForm, shopBusinessGoodsGrid ]
						}).show();
					},
					reset:function(){
						shopBusinessGoodsForm.form.reset();
					},
					
					search : function() {
						var outStock = {};

						outStock["status"] = Ext.getCmp("status").getValue();
						if("TB_CHANNEL_CODE" != channelCodeStr && "BG_CHANNEL_CODE" != channelCodeStr && "PP_CHANNEL_CODE" != channelCodeStr){
							outStock["goodsSn"] = Ext.getCmp("goodsSn").getValue();
						}
						shopBusinessGoodsGrid.store.baseParams = outStock;
						shopBusinessGoodsGrid.store.load({
							params : {
								start : 0,
								limit : pageSize
							
							}
						});
					}

				};
			}();

			function runCouponList() {
				shopbusinessGoods.init();
				shopbusinessGoods.show();
				setResize();
			}
			runCouponList();
			
			function setResize() {
				var formHeight =shopBusinessGoodsForm.getHeight();
				var clientHeight = document.body.clientHeight;
				var clientWidth = document.body.clientWidth;
				shopBusinessGoodsGrid.setHeight(clientHeight-formHeight-50);
				shopBusinessGoodsGrid.setWidth(clientWidth-259);
			}
			window.onresize=function(){
				setResize();
			};
	
		});
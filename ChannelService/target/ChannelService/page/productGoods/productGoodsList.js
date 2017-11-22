function chanageBarcodeChild(val, meta, record) {
	var obj = val;
  //var obj = Ext.util.JSON.decode(val);
	var shtml ="";
	if(null != obj &&  obj.length>0) {
		shtml +="<table border=1 bordercolor='#ADADAD' width=270 cellpadding='0' cellspacing='0'>";
	
		for(var i=0;i<obj.length; i++) {
			
			var colorNameStr  =	obj[i].colorName = null ?"": obj[i].colorName;
			var sizeNameStr  =	obj[i].sizeName = null ?"": obj[i].sizeName;
			var custumCode  =	obj[i].custumCode = null ?"": obj[i].custumCode;
			
			shtml+="<tr>" +
					  "<td>"+colorNameStr+"</td>" +
					  "<td>"+sizeNameStr +"</td>" +
					  "<td colspan=2>"+custumCode +"</td>" +
					"</tr>";
		}
		shtml+="</table>";
	}
	return shtml;
	
}

function linkDetails(val, metadata, record, rowIndex) {
	
	var goodsSn = record.get("goodsSn");
	return "<u><span onclick=\""+"onclickDetails('"+goodsSn +"')"+"\">"+val+"</span></u>";
	
}

//点击详情
function onclickDetails(goodsSn){
	
	if(null != goodsSn && !"" != goodsSn) {
		FormEditWin.showAddDirWin("detailsWins",basePath+"/custom/productGoods/toProductGoodsDetails.spmvc?goodsSn="+goodsSn,"details_message_winID","详情","100%",400);
	}
	
}

Ext.onReady(function() {
	var channelGoodsInfoForm;
	var channelGoodsInfoGrid;
	ChannelGoodsInfoPage = function() {
		var channelGoodsInfoStore;
		var channelGoodsInfoProxy;
		var channelGoodsInfoReader;
		var checkBoxSelect;
		var channelGoodsInfoColumnGrid;
		var tbar;
		var pageSize=15;
		var listUrl = basePath+"/custom/productGoods/productGoodsList.spmvc?method=start";
		return {
			init : function() {
			channelGoodsInfoForm = new Ext.FormPanel(
				{
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
							items : [/*{
									id : 'channelGoodsInfo.channelCode',
									xtype : 'textfield',
									fieldLabel : '渠道名称',
									readOnly : true,
									value : getGlobalValue('channelTitle'),
									width : '150'
								},*/{
									id : 'productGoods.goodsName',
									xtype : 'textfield',
									fieldLabel : '商品名称',
									name : 'goodsName',
									//allowBlank : false,
									blankText : '商品名称不能为空',
									width : '150'
								} ]
							},{
									layout : 'form',
									labelWidth:80,
									columnWidth:0.33,
									items : [/*{
										id : 'channelGoodsInfo.shopCode',
										xtype : 'textfield',
										fieldLabel : '经营店铺',
										value : getGlobalValue('shopTitle'),
										readOnly : true,
										width : '150'
									},*/{
										id : 'productGoods.goodsSn',
										xtype : 'textfield',
										fieldLabel : '商品款号',
										name : 'goodsSn', 
										blankText : '商品款号不能为空',
										width : '150'
									}]
								}]
							}],
						buttons : [
								{
									text : '查询',
									columnWidth : .1,
									handler : this.search
								},{
									text : '重置',
									columnWidth : .1,
									handler : function(){
										channelGoodsInfoForm.form.reset();
									//	Ext.getCmp('channelGoodsInfo.channelCode').setValue(getGlobalValue('channelTitle'));
									//	Ext.getCmp('channelGoodsInfo.shopCode').setValue(getGlobalValue('shopTitle'));
									}
							}]
				});
			
		//		Ext.getCmp('channelGoodsInfo.channelCode').setValue(getGlobalValue('channelTitle'));
			//	Ext.getCmp('channelGoodsInfo.shopCode').setValue(getGlobalValue('shopTitle'));
				
				checkBoxSelect = new Ext.grid.CheckboxSelectionModel();
				channelGoodsInfoColumnGrid = new Ext.grid.ColumnModel( [
					checkBoxSelect, 
					{id : 'id', header : "id", align : "center", align : "center", width : 70, hidden:true, dataIndex : 'id' },
					{id : 'goodsSn', sortable : true, header : "商品编号", width : 100, dataIndex : 'goodsSn'},
					{id : 'goodsName', header : "商品名称", sortable : true, renderer: linkDetails, width : 100, dataIndex : 'goodsName'},
			  //	{id : 'goodsName', header : "商品名称", sortable : true,  width : 120, dataIndex : 'goodsName'},
					{id : 'marketPrice', align : "center", header : "市场价格", width : 70, dataIndex : 'marketPrice'},
					{id : 'barcodeChild', align : "center", sortable : true, renderer:chanageBarcodeChild, header : "颜色/尺码/11位码", width : 280, dataIndex : 'barcodeChild'},
				//	{id : 'barcodeChild', align : "center", sortable : true,  header : "颜色/尺码", width : 180, dataIndex : 'barcodeChild'},
					{id : 'formatAddTime', align : "center", sortable : true, header : "创建时间", width : 160, dataIndex : 'formatAddTime'},
					{id : 'formatUpdateTime', align : "center", sortable : true, header : "更新时间", width : 160, dataIndex : 'formatUpdateTime'}
				]);

				//与列对应的dataIndex
				couponrecord = Ext.data.Record.create( [
					{name : 'id' },
					{name : 'goodsSn' }, 
					{name : 'goodsName'}, 
					{name : 'marketPrice'}, 
					{name : 'formatAddTime'}, 
					{name : 'formatUpdateTime'},
					{name : 'barcodeChild'}
					
					]);
				
				//加载列表数据
				channelGoodsInfoProxy = new Ext.data.HttpProxy( {
					url : listUrl,
					method : "post"
				});
				//Reader 读json中数据
				channelGoodsInfoReader = new Ext.data.JsonReader( {
					root : 'root',
					totalProperty : 'totalProperty',
					id : 'ticketCode'
				}, couponrecord);
				
				channelGoodsInfoStore = new Ext.data.Store( {
					autoLoad : {
						params : {
							start : 0,
							limit : pageSize
						}
					},
					proxy :  channelGoodsInfoProxy,
					reader : channelGoodsInfoReader
				});
				
				channelGoodsInfoStore.on('beforeload', function(){
					var goodsName = Ext.getCmp('productGoods.goodsName').getValue();
					var goodsSn = Ext.getCmp('productGoods.goodsSn').getValue();
					Ext.apply(this.baseParams, {goodsName: goodsName, goodsSn: goodsSn});
				});
				//定义菜单栏
				tbar = [{
						id : 'message_grid_tBar@add',
						text : '生成/更新渠道商品',
						tooltip : '生成/更新渠道商品',
						iconCls : 'add',
						handler :function(){
							 var params = "&params=channelCode:"+getGlobalValue('channelCode')+";shopCode:"+getGlobalValue('shopCode');
							FormEditWin.showAddDirWin(null,basePath+"/fileUpload.html?uploadUrl="+basePath+"/custom/productGoods/synChannelGoods.spmvc"+params+"&type=3","picADWinID","上传",480,300);
							
//							Ext.Ajax.request({
//								waitMsg : '请稍等.....',
//								url : basePath+ '/custom/productGoods/synChannelGoods.spmvc',
//								params : {
//									shopCode:getGlobalValue('shopCode'),
//									channelCode:getGlobalValue('channelCode')
//								},
//								method : "post",
//								success : function(response) {
//									alertMsg("结果","线程执行中，请稍后查看数据！");
//									
//								},
//								failure : function() {
//									errorMsg("结果","生成或者更新渠道商品失败！");
//								}
//							});
							
						}
					},'<a style="color:red" href = "'+ basePath + '/page/csvModel/upload_goods.csv' + '">模板下载</a>'

				];
				channelGoodsInfoGrid = new Ext.grid.GridPanel( {
					title : '渠道商品管理',
					store : channelGoodsInfoStore,
					id:'addcouponForm_gridss_id',
					trackMouseOver : false,
					disableSelection : true,
					loadMask : true,
					frame : true,
					stripeRows : true,
					columnLines : true,
					autoExpandColumn: "goodsName", //自动伸展，占满剩余区域
				//	tbar:  tbar,
					// grid columns  
					cm : channelGoodsInfoColumnGrid,
					sm:checkBoxSelect,
					// paging bar on the bottom  
					bbar : new IssPagingToolbar(channelGoodsInfoStore, pageSize),
					listeners : {
//						'rowdblclick': function(thisgrid ,rowIndex ,e){
//							ChannelGoodsInfoPage.modify();
//						}
					}
				});
			},
			doAfter : function(data){//上传后的数据返回
				 var json = Ext.util.JSON.decode(data);
                     alertMsg("结果", json.message);
                    FormEditWin.close();
                    channelGoodsInfoStore.reload();
				},
			deleteObject : function() { //删除整个panel里面被选择的行数据
				var selModel = channelGoodsInfoGrid.getSelectionModel();
				if (selModel.hasSelection()) {
					confirmMsg("确认","您确定要删除选择的记录吗?",
							function(btn) {
								if (btn == "yes") {// 确认
									var records = selModel.getSelections();
									var ids = "";
									for ( var i = 0; i < records.length; i++) {
										var id = records[i].get("id"); 
										var error = records[i].get("ticketStatus"); 
										if(error == "2" ||error=="3"){
											alertMsg("结果", "请检查单据状态！");
											return;
										}
										if(id && id!='' && id!=null){
											ids += ""+id+",";
										}
									}
									if (ids != "") {
										Ext.Ajax.request({
											waitMsg : '请稍等.....',
											url : basePath+ '/custom/shopGoods/reviewchannelGoodsInfoTicket.spmvc',
											params : {
												ids : ids, ticketStatus:2
											},
											method : "post",
											success : function(response) {
												var json = Ext.util.JSON.decode(response.responseText);
												if(json.isok){ //成功
													alertMsg("结果",json.message);
													ChannelGoodsInfoPage.search();
												}else{
													alert("移除出错！");
													ChannelGoodsInfoPage.search();
												}
											},
											failure : function() {
												errorMsg("结果","删除失败！");
											}
										});
									}
								}
							});
				} else {
					alertMsg("提示", "请选择要删除的数据!");
				}
			},
			show : function() {
				channelGoodsInfoPanel = new Ext.Panel( {
					renderTo : 'productGoods-panel',
					layout : 'column',
					items : [ channelGoodsInfoForm, channelGoodsInfoGrid]
				}).show();
			},
			search : function() {
				var searchParam = {};
				searchParam["goodsSn"] = Ext.getCmp("productGoods.goodsSn").getValue();
				searchParam["goodsName"] = Ext.getCmp("productGoods.goodsName").getValue();
				//searchParam["channelCode"] = getGlobalValue('channelCode');
				//searchParam["channelCode"] = getGlobalValue('shopCode');
				channelGoodsInfoGrid.store.baseParams = searchParam;
				channelGoodsInfoGrid.store.load({params : {start : 0, limit : pageSize}});
			},
			modify : function(){  //点击修改按钮
				var selModel = channelGoodsInfoGrid.getSelectionModel();
				if (selModel.hasSelection()) {
					var records = selModel.getSelections();
					var id = records[0].get("id");
					if(id != ""){
						 FormEditWin.showAddDirWin("modifyWins",basePath+"/custom/channelGoodsInfo/toModify.spmvc?id="+id,"modify_message_winID","查看/更新",560,400);
					}else{
						 alertMsg("结果", "请选择需要修改的行！");
					}
				}else{
					 alertMsg("结果", "请选择需要修改的行！");
				}
			}
		};
	}();

	function runChannelGoodsList() {
		ChannelGoodsInfoPage.init(); 
		ChannelGoodsInfoPage.show();
		setResize();
	}
	runChannelGoodsList();
	
	function setResize() {
		var formHeight = channelGoodsInfoForm.getHeight();
		var clientHeight = document.body.clientHeight;
		var clientWidth = document.body.clientWidth;
		channelGoodsInfoGrid.setHeight(clientHeight-formHeight-50);
		channelGoodsInfoGrid.setWidth(clientWidth-259);
	}
	window.onresize=function(){
		setResize();
	}

});
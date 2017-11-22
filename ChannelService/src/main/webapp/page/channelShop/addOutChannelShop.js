	var channelSelect;
var expiresTimeVal;
var updateId="";
var addcouponForm;
Ext.onReady(function() {
	Ext.QuickTips.init();
	Ext.form.Field.prototype.msgTarget = 'side';
	AddCoupon = function() {
		return {
			init : function() {
				shopTypeSelect= new Ext.form.ComboBox({
					id : 'shopType',
					store : new Ext.data.SimpleStore({
						data : [ [ '0', '自营' ], [ '1', '加盟' ] ],
						fields : [ 'text', 'filed' ]
					}),
					xtype : 'combo',
					valueField : 'text',
					displayField : 'filed',
					mode : 'local',
					forceSelection : true,
					blankText : '请选择店铺类型',
					emptyText : '店铺类型',
					name : 'shopType',
					editable : false,
					allowBlank : false,
					triggerAction : 'all',
					fieldLabel : '店铺类型',
					width : 160
				});
				
				channelSelect= new Ext.form.ComboBox({
					id : 'channelCode',
					store :  new Ext.data.Store({
						proxy : new Ext.data.HttpProxy({
							url : basePath + '/custom/channelInfo/channelList.spmvc?type=1,2',
							method : 'GET'
						}),
						reader : new Ext.data.JsonReader({
							fields : [ 'chanelCode', 'channelTitle' ]
						})
					}),
					xtype : 'combo',
					valueField : 'chanelCode',
					displayField : 'channelTitle',
					mode : 'remote',
					forceSelection : true,
					emptyText : '请选择渠道',
					editable : false,
					triggerAction : 'all',
					fieldLabel : '渠道',
					width : 160
				});
				
				shopCodeSelect= new Ext.form.ComboBox({
					id : 'parentShopCode',
					store :  new Ext.data.Store({
						proxy : new Ext.data.HttpProxy({
							url : basePath + '/custom/channelShop/parentShopCodeList.spmvc?shopChannel=1',
							method : 'GET'
						}),
						reader : new Ext.data.JsonReader({
							fields : [ 'shopCode','shopTitle']
						})
					}),
					xtype : 'combo',
					valueField : 'shopCode',
					displayField : 'shopTitle',
					mode : 'remote',
					forceSelection : true,
					emptyText : '请选择父店铺编号',
					//editable : false,
					triggerAction : 'all',
					fieldLabel : '父店铺编号',
					width : 160
				});
				isSynSelect= new Ext.form.ComboBox({
					id : 'isSyn',
					store : new Ext.data.SimpleStore({
						data : [ [ '0', '否' ], [ '1', '是' ] ],
						fields : [ 'text', 'filed' ]
					}),
					xtype : 'combo',
					valueField : 'text',
					displayField : 'filed',
					mode : 'local',
					forceSelection : true,
//					blankText : '请选择',
//					emptyText : '是否同步',
					value : '1',
					name : 'isSyn',
					editable : false,
					allowBlank : false,
					triggerAction : 'all',
					fieldLabel : '是否同步',
					width : 160
				});
				/*
				shopChannelSelectOption = new Ext.form.ComboBox({
						id : 'channelShop.shopChannel',
						store : new Ext.data.SimpleStore({
							data : [ [ '0', '集团店铺' ], [ '1', '外部渠道店铺' ] ],
							fields : [ 'text', 'filed' ]
						}),
						xtype : 'combo',
						valueField : 'text',
						displayField : 'filed',
						mode : 'local',
						forceSelection : true,
						blankText : '请选择店铺渠道',
						emptyText : '请选择店铺渠道',
						name : 'channelShop.shopChannel',
						editable : false,
						triggerAction : 'all',
						fieldLabel : '店铺渠道',
						width : 160
					});
				*/
				addcouponForm = new Ext.FormPanel({
					layout : 'column',
					height : 400,
					width : 600,
					frame : true,
					labelAlign : 'left',
					buttonAlign : 'center',
					items:[{
						columnWidth : .5,
//						frame : true,
						height : 370,
						width : 300,
						labelAlign : 'right',
						buttonAlign : 'center',
						layout : 'form',
//						title:"统一渠道",
						items : [{
							xtype : 'textfield',
							fieldLabel : '店铺编号',
							id:'shopCode',
							allowBlank : false,
							blankText : '店铺编号不能为空',
							width : '160'
						},/*{
							xtype : 'textfield',
							fieldLabel : '父店铺编号',
							id:'parentShopCode',
							name:'parentShopCode',
							width : '160'
						},*/ shopCodeSelect,shopTypeSelect, /*shopChannelSelectOption,*/ channelSelect,isSynSelect,
						{
							id:'msgSendType',
							xtype : 'textfield',
							fieldLabel : '短信签名类型',
							allowBlank : true,
							width : '160'
						},
						{
							id:'returnDepot',
							xtype : 'textfield',
							fieldLabel : '退货入库仓',
							width : '160'
						},{
							id:'backup',
							xtype : 'textarea',
							fieldLabel : '备注',
							width : '160'
						}]},{
							columnWidth : .5,
//							title:"OPENSHOP",
//							frame : true,
							height : 400,
							width : 280,
							labelAlign : 'left',
							buttonAlign : 'center',
							layout : 'form',
							items : [
										{
											id : 'openShopChannelInfo.channelId',
											xtype : 'hidden'
										},
										{
											xtype : 'textfield',
											fieldLabel : '店铺名称',
											id:'shopTitle',
											allowBlank : false,
											blankText : '店铺名称不能为空',
											width : '160',
										},
										{
											xtype: 'textfield', 
											fieldLabel: '平台店铺名称',
											id:'marketTitle',
											allowBlank : true,
											width : '160'
										},
										{
											id : 'openShopChannelInfo.nickName',
											xtype : 'textfield',
											width : '160',
											fieldLabel : '店铺昵称'
										},
										{
											xtype: 'textfield', 
											fieldLabel: '上传图片名',
											id:'shopImg',
											name : 'shopImg',
//											name: 'file', 
											width : '160'
										},
										{
											xtype : 'textfield',
											fieldLabel : '物流回调接口',
											id:'logisticsCallback',
											name : 'logisticsCallback',
											width : '160'
										},
										{
											id:'msgTemplateCode',
											xtype : 'textfield',
											fieldLabel : '短信模板编码',
											allowBlank : true,
											width : '160'
										},
										{
											readOnly : true,
											fieldLabel : 'token失效日期',
											width : '160',
										//	anchor : '90%',
											html : '<input type="text" id="expiresTimeVal" readonly="true" onClick="WdatePicker({el:\'expiresTimeVal\',dateFmt:\'yyyy-MM-dd HH:mm:ss\'})"/>'
										},
										{
											id:'preSaleShopCode',
											xtype : 'textfield',
											fieldLabel : '预售店铺编码',
											width : '160'
										}
							]
						}],
						buttons : [
								{
									text : '保存',
									handler : function() {
										// 获取店铺的名称
										var qty = addcouponForm.getForm().findField('shopTitle').getValue();
										if (!addcouponForm.getForm().isValid()) {
											alertMsg("验证","请检查数据是否校验！");
											return;
										}
										if(Ext.getCmp("channelCode").getValue()==""){
											alertMsg("验证","请选择渠道！");
											return;
										}
										var shopImg = Ext.getCmp("shopImg").getValue()
										
										if(""!= shopImg) {
											var extension = shopImg.substring(shopImg.lastIndexOf("."),shopImg.length);
											
											if(".jpg"  != extension  &&  ".png" !=extension &&  ".gif" !=extension){
												alertMsg("验证","图片格式不对！");
												return;
											}
										}
										Ext.Msg.confirm("确认","是否保存店铺:"+ qty, 
											function(btn) {
											if (btn == "yes") {// 确认
												Ext.Ajax.request( {
													waitMsg : '请稍等.....',
													url : basePath + 'custom/channelShop/insertChannelShop.spmvc',
													method : 'post',
													params : {
														id:updateId, //用于更新主键
														shopCode:Ext.getCmp("shopCode").getValue(), //店铺code
														parentShopCode:Ext.getCmp("parentShopCode").getValue(), //父级code
														shopChannel: 1 , //渠道店铺
														isSyn:Ext.getCmp("isSyn").getValue(),//是否同步
													//	shopStatus:0, //店铺状态为未激活状态
														backup:Ext.getCmp("backup").getValue(),
														returnDepot:Ext.getCmp("returnDepot").getValue(),
														marketTitle:Ext.getCmp("marketTitle").getValue(),
														shopTitle:Ext.getCmp("shopTitle").getValue(),
														logisticsCallback:Ext.getCmp("logisticsCallback").getValue(),
														shopType:Ext.getCmp("shopType").getValue(),//类型自营or加盟
														channelCode:Ext.getCmp("channelCode").getValue(), //渠道code
														shopImg:Ext.getCmp("shopImg").getValue(), //图片名称
														shopChannel: 1, //Ext.getCmp("channelShop.shopChannel").getValue()
														msgSendType: Ext.getCmp("msgSendType").getValue(),
														msgTemplateCode: Ext.getCmp("msgTemplateCode").getValue(),
														oChannelId : Ext.getCmp("openShopChannelInfo.channelId").getValue(),
														oNickName : Ext.getCmp("openShopChannelInfo.nickName").getValue(),
														expiresTimeStr : $("#expiresTimeVal").val(),
														preSaleShopCode: Ext.getCmp("preSaleShopCode").getValue()
													},
													success : function(response) {
														var respText = Ext.util.JSON.decode(response.responseText);
														if(respText.isok){
															parent.OutChannelShop.refresh();
															parent.FormEditWin.close();
														}else{
															alert(respText.message);
														}
													},
													failure : function(response) {
														var respText = Ext.util.JSON.decode(response.responseText); 
														alert("失败" + respText);
													}
												});
											}
										});
									}
								},
								{
									text : '关闭',
									handler : function() {
										parent.FormEditWin.close();
									}
								}
					]
				});
			},
			show : function() {
				addoutStockPanel = new Ext.Panel({
					renderTo : 'addcouponForm-grid',
					autoHeight : true,
					autoWidth : true,
					layout : 'fit',
					items : [ addcouponForm ]
				}).show();
			}
		}
	}();

	function uploadShowImg (){ //导入图片
		//var channelCode = Ext.getCmp("OutChannelShop.channelCode").getValue(); //渠道code
		var shopCode = Ext.getCmp("shopCode").getValue(); //店铺名称code
		var params = "&params=shopCode:"+shopCode;
		FormEditWin.showAddDirWin(null,basePath+"/fileUpload.html?uploadUrl="+basePath+"custom/channelShop/uploadShopImg.spmvc"+params,"picADWinID","上传",580,300);
	}
	function runAddCoupon() {
		Ext.getCmp('shopCode').setValue($('#neWShopCode').val()); 
		updateId = $('#hId').val();
		if("" != $('#hId').val()){
			Ext.getCmp('shopCode').setValue($('#hShopCode').val()); 
			shopCodeSelect.store.on('load', function(store, record, opts) {
				Ext.getCmp('parentShopCode').setValue($('#hParentShopCode').val()); 
			});
			shopCodeSelect.store.load();
			Ext.getCmp('parentShopCode').setValue($('#hParentShopCode').val());
			Ext.getCmp('shopTitle').setValue($('#hShopTitle').val()); 
			channelSelect.store.on('load', function(store, record, opts) {
				Ext.getCmp('channelCode').setValue($('#hChannelCode').val()); 
			});
			channelSelect.store.load();
			Ext.getCmp('shopImg').setValue($('#hshopImg').val()); 
			Ext.getCmp('logisticsCallback').setValue($('#hLogisticsCallback').val());
			Ext.getCmp('shopType').setValue($('#hShopType').val()); 
			Ext.getCmp('backup').setValue($('#hBackup').val());
			Ext.getCmp('returnDepot').setValue($('#hReturnDepot').val());
			Ext.getCmp('marketTitle').setValue($('#hMarketTitle').val());
			Ext.getCmp('isSyn').setValue($('#hIsSyn').val());
			Ext.getCmp('openShopChannelInfo.channelId').setValue($('#osChannelId').val());
			Ext.getCmp('openShopChannelInfo.nickName').setValue($('#osNickName').val());
			Ext.getCmp('msgSendType').setValue($('#hmsgSendType').val());
			Ext.getCmp('msgTemplateCode').setValue($('#hmsgTemplateCode').val());
			Ext.getCmp('preSaleShopCode').setValue($('#hpreSaleShopCode').val());

			//shopChannelSelectOption.store.on('load', function(store, record, opts) {
			//	Ext.getCmp('channelShop.shopChannel').setValue($('#hshopChannel').val()); 
			//});
			//shopChannelSelectOption.store.load();
			var osExpiresTime = $("#osExpiresTime").val();
			//alert(osExpiresTime);
			//	var date = new Date(osExpiresTime);
			//	alert(osExpiresTime);
			//	var ddd = getNowFormatDate(osExpiresTime);
			//	alert($("#expiresTimeVal").val());
			//expiresTimeVal = 	$("#expiresTimeVal");
			//	alert(expiresTimeVal);
			//alert($('#expiresTimeVal'));
			//var iddd =  	document.getElementById('expiresTimeVal');
			//iddd.value=osExpiresTime;
			$("#expiresTimeVal").val(osExpiresTime);
			//	console.dir($("#expiresTimeVal"));
			Ext.getCmp('shopCode').setReadOnly(true);
			var shopStatus = $('#hshopStatus').val();
			if(shopStatus == 1){
				Ext.getCmp('parentShopCode').setDisabled(true);
			}
		}
	}
	AddCoupon.init();
	AddCoupon.show();
	runAddCoupon();
});

function getNowFormatDate(dateTime) {
	var date = new Date(dateTime);
	var seperator1 = "-";
	var seperator2 = ":";
	var month = date.getMonth() + 1;
	var strDate = date.getDate();
	if (month >= 1 && month <= 9) {
		month = "0" + month;
	}
	if (strDate >= 0 && strDate <= 9) {
		strDate = "0" + strDate;
	}
	var currentdate = date.getFullYear() + seperator1 + month + seperator1 + strDate
		+ " " + date.getHours() + seperator2 + date.getMinutes()
		+ seperator2 + date.getSeconds();
	return currentdate;
}


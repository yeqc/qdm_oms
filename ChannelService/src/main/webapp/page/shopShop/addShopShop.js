Ext.onReady(function() {
			Ext.QuickTips.init();
			Ext.form.Field.prototype.msgTarget = 'side';
			var updateId="";
			AddCoupon = function() {
				var addShopShopForm;
				return {
					init : function() {
						channelSelect= new Ext.form.ComboBox({
								id : 'channelCode',
								store :  new Ext.data.Store({
									proxy : new Ext.data.HttpProxy({
										url : basePath + '/custom/channelInfo/channelList.spmvc?type=3,4',
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
								emptyText : '请选择渠道(店面上级)',
								editable : false,			
								triggerAction : 'all',
								fieldLabel : '店面上级',
								anchor : '90%',
								width : 130
							});
						
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
							anchor : '90%',
							width : 130
						});
						
						parentShopCodeSelect = new Ext.form.ComboBox({
							id : 'parentShopCode',
							store :  new Ext.data.Store({
								proxy : new Ext.data.HttpProxy({
									url : basePath + '/custom/channelShop/parentShopCodeList.spmvc?shopChannel=0',
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
							anchor : '90%',
							width : 130,
							listeners:{
								change:function(t,val){
									Ext.getCmp('openShopChannelInfo.parentChannelCode').setValue(val); 
								}
							}
						});

						addShopShopForm = new Ext.FormPanel({
							layout : 'column',
							height : 400,
							width : 600,
							frame : true,
							labelAlign : 'left',
							buttonAlign : 'center',
							items:[{
//								title:"统一渠道",
//								frame : true,
								columnWidth : .5,
								height : 370,
								width : 300,
								labelAlign : 'left',
								buttonAlign : 'center',
								layout : 'form',
								items : [
									{
										xtype : 'textfield',
										fieldLabel : '店铺编号',
										id:'shopCode',
										name : 'shopCode',
										allowBlank : false,
										blankText : '店铺编号不能为空',
										width : '160'
									},parentShopCodeSelect, {
										xtype : 'textfield',
										fieldLabel : 'ERP店铺编号',
										id:'erpShopCode',
										name : 'erpShopCode',
										allowBlank : false,
										blankText : 'ERP店铺编号不能为空',
										width : '160'
									}, shopTypeSelect ,channelSelect, {
										xtype : 'textfield',
										fieldLabel : '店铺地址',
										id:'shopAddress',
										name : 'shopAddress',
										allowBlank : false,
										blankText : '店铺地址不能为空',
										width : '160'
									} , {
										xtype : 'textarea',
										fieldLabel : '备注',
										id:'backup',
										name : 'backup',
									//	blankText : '店铺鉴权信息不能为空',
										allowBlank : true,
										width : '160'
									}]},{
//								title:"OPENSHOP",
//								frame : true,
								columnWidth : .5,
								height : 370,
								width : 300,
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
												name : 'shopTitle',
												allowBlank : false,
												blankText : '店铺名称不能为空',
												width : '160'
											},
											{
												id : 'openShopChannelInfo.nickName',
												xtype : 'textfield',
												fieldLabel : '店铺昵称',
												width : '160'
											},
											{
												xtype : 'textfield',
												fieldLabel : '市场名称',
												id:'marketTitle',
												name : 'marketTitle',
												allowBlank : false,
												blankText : '市场名称不能为空',
												width : '160'
											},{
												xtype : 'textfield',
												fieldLabel : '联系人',
												id:'shopLinkman',
												name : 'shopLinkman',
												allowBlank : false,
												blankText : '联系人不能为空',
												width : '160'
											},{
												xtype : 'textfield',
												fieldLabel : '联系电话',
												id:'shopTel',
												name : 'shopTel',
												allowBlank : false,
												blankText : '联系电话不能为空',
												width : '160'
											},
											{
												id:'msgSendType',
												xtype : 'textfield',
												fieldLabel : '短信签名类型',
												allowBlank : true,
												value: 'mb_123',
												width : '160'
											},
											{
												id:'msgTemplateCode',
												xtype : 'textfield',
												fieldLabel : '短信模板编码',
												value: '220',
												allowBlank : true,
												width : '160'
											}
								]
							}]
							,
							buttons : [
									{
										text : '保存',
										handler : function() {
											// 获取店铺的名称
											var qty = addShopShopForm.getForm().findField('shopTitle').getValue();
											if (!addShopShopForm.getForm().isValid()) {
												alertMsg("验证","请检查数据是否校验！");
												return;
											}
											if(Ext.getCmp("shopType").getValue()==""){
												alertMsg("验证","请选择店铺类型！");
												return;
											}
											Ext.Msg.confirm("确认","是否保存店铺:"+ qty, function(btn) {
												if (btn == "yes") {// 确认
													Ext.Ajax.request( {
														waitMsg : '请稍等.....',
														url : basePath + 'custom/channelShop/insertChannelShop.spmvc',
														method : 'post',
														params : {
															id: updateId, //用于更新主键
															shopCode:  Ext.getCmp("shopCode").getValue(), //店铺code
															shopChannel: 0 , //新增集团店铺
														//	shopStatus:0, //店铺状态为未激活状态
															channelCode:Ext.getCmp("channelCode").getValue(),  //渠道code(店面上级)
															shopTitle: Ext.getCmp("shopTitle").getValue(), //店铺名称
															shopType:  Ext.getCmp("shopType").getValue(),//店铺类型
															erpShopCode: Ext.getCmp("erpShopCode").getValue(), //ERP店铺编号
															marketTitle:Ext.getCmp("marketTitle").getValue(),//市场名称
															shopLinkman:Ext.getCmp("shopLinkman").getValue(), //联系人
															shopTel:    Ext.getCmp("shopTel").getValue(), //电话
															shopAddress:Ext.getCmp("shopAddress").getValue(), //店铺地址
															backup:     Ext.getCmp("backup").getValue(), //备注
															parentShopCode : Ext.getCmp("parentShopCode").getValue(),
															msgSendType: Ext.getCmp("msgSendType").getValue(),
															msgTemplateCode: Ext.getCmp("msgTemplateCode").getValue(),
															oChannelId : Ext.getCmp("openShopChannelInfo.channelId").getValue(),
															oNickName : Ext.getCmp("openShopChannelInfo.nickName").getValue()
														}, 
														success : function(response) {
															var respText = Ext.util.JSON.decode(response.responseText);
															if(respText.isok){
																alert(respText.message);
																parent.ShopShop.refresh();
																parent.FormEditWin.close();
															}else{
																alert(respText.message);
															}
														},
														failure : function(response) {
															alert("失败");
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
									} ]
						
						});
					},
					show : function() {
						addoutStockPanel = new Ext.Panel({
							renderTo : 'addShopShop-grid',
							autoHeight : true,
							autoWidth : true,
							layout : 'fit',
							items : [ addShopShopForm ]
						}).show();
					}
				}
			}();

			function runAddCoupon() {
				AddCoupon.init();
				updateId = $('#hId').val();//将修改记录的ID带回后台用于主键修改信息
				if("" != document.getElementById('hId').value ){ //查看修改信息
					Ext.getCmp('shopCode').setValue($('#hShopCode').val()); 
	 				Ext.getCmp('shopTitle').setValue($('#hShopTitle').val()); 
	 				Ext.getCmp('shopType').setValue($('#shopType').val());
	 				Ext.getCmp('erpShopCode').setValue($('#erpShopCode').val());
	 				Ext.getCmp('marketTitle').setValue($('#marketTitle').val()); 
	 				Ext.getCmp('shopLinkman').setValue($('#shopLinkman').val());
	 				Ext.getCmp('shopTel').setValue($('#shopTel').val()); 
	 				Ext.getCmp('shopAddress').setValue($('#shopAddress').val()); 
	 				Ext.getCmp('backup').setValue($('#hbackup').val()); 
	 				channelSelect.store.on('load', function(store, record, opts) {
	 					Ext.getCmp('channelCode').setValue($('#hChannelCode').val()); 
	 				});
	 				channelSelect.store.load();
	 				parentShopCodeSelect.store.on('load', function(store, record, opts) {
	 					Ext.getCmp('parentShopCode').setValue($('#hparentShopCode').val()); 
	 				});
	 				parentShopCodeSelect.store.load();
	 				Ext.getCmp('openShopChannelInfo.channelId').setValue($('#osChannelId').val()); 
	 				Ext.getCmp('openShopChannelInfo.nickName').setValue($('#osNickName').val()); 
	 				Ext.getCmp('shopCode').setReadOnly(true);
	 				
	 				Ext.getCmp('msgSendType').setValue($('#hmsgSendType').val());
	 				Ext.getCmp('msgTemplateCode').setValue($('#hmsgTemplateCode').val());
	 				var shopStatus = $('#hshopStatus').val();
	 				if(shopStatus == 1){
	 					Ext.getCmp('parentShopCode').setDisabled(true);
	 				}
				}
				AddCoupon.show();
			}
			runAddCoupon();
		});
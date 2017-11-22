Ext.onReady(function() {
			
			var updateId="";
			var channelSelect;
		   var 	shopSelect;
			Ext.QuickTips.init();
			Ext.form.Field.prototype.msgTarget = 'side';

			AddCoupon = function() {
				var addcouponForm;
				return {
					init : function() {
						
					    //渠道
						 channelSelect= new Ext.form.ComboBox({
							id : 'channelCode',
							store :  new Ext.data.Store({
								proxy : new Ext.data.HttpProxy({
									url : basePath + '/custom/channelInfo/channelList.spmvc',
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
							width : 200
							//atuoWidth:true
						});
						 
						  //店铺
						 shopSelect= new Ext.form.ComboBox({
							id : 'shopCode',
							store :  new Ext.data.Store({
								proxy : new Ext.data.HttpProxy({
									url : basePath + '/custom/promotion/channelshoplist.spmvc',
									method : 'GET'
								}),
								reader : new Ext.data.JsonReader({
									fields : [ 'shopCode', 'shopTitle' ]
								})
							}),
							xtype : 'combo',
							valueField : 'shopCode',
							displayField : 'shopTitle',
							mode : 'remote',
							forceSelection : true,
							emptyText : '请选择店铺',
							editable : false,			
							triggerAction : 'all',
							fieldLabel : '店铺',
							width : 200
							//	atuoWidth:true
						});
  
						addcouponForm = new Ext.FormPanel(
								{
									frame : true,
									bodyStyle : 'padding:10px 5px 5px 5px',
									width:400,
									height:250,
									labelAlign : 'left',
									buttonAlign : 'center',
									layout : 'form',
									items : [channelSelect,
									   shopSelect,{
											xtype : 'textfield',
											fieldLabel : '配置值',
											id:'proValue',
											name : 'proValue',
											allowBlank : false,
											blankText : '配置值不能为空',
											width : '200'
										},{
											xtype : 'textfield',
											fieldLabel : '配置名称',
										    id:'proName',
											name : 'proName',
											allowBlank : false,
											blankText : '配置名称不能为空',
											width : '200'
										}],
									buttons : [
											{
												text : '保存/更新',
												handler : function() {
													// 获取店铺的名称
													//var qty = addcouponForm.getForm().findField('shopTitle').getValue();
													if (!addcouponForm.getForm().isValid()) {
														alertMsg("验证","请检查数据是否校验！");
														return;
													}
												
													if(Ext.getCmp("proName").getValue()==""){
														alertMsg("验证","配置名称！");
														return;
													}
												    
													Ext.Msg.confirm("确认","是否添加/修改配置信息:",
																	function(btn) {
																		if (btn == "yes") {// 确认
																			
																			Ext.Ajax.request( {
																				waitMsg : '请稍等.....',
																				url : basePath + 'custom/interfaceProperties/insertInterfaceProperties.spmvc',
																				method : 'post',
																				params : {
																					id:updateId, //用于更新主键
																					shopCode:Ext.getCmp("shopCode").getValue(), //店铺code
																					proName:Ext.getCmp("proName").getValue(),
																					proValue:Ext.getCmp("proValue").getValue(),
																					channelCode:Ext.getCmp("channelCode").getValue()//渠道code
																				}, 
																				success : function(response) {
																					var respText = Ext.util.JSON.decode(response.responseText);
																					if(respText.isok){
																						parent.InterfaceProperty.refresh();
																						parent.FormEditWin.close();
																						
																					}else{
																						alert(respText.message);
																					}				
																				},
																				failure : function(response) {
																				//	var respText = Ext.util.JSON.decode(response.responseText); 
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
							renderTo : 'addInterfacePropertiesForm-grid',
							autoHeight : true,
							autoWidth : true,
							layout : 'fit',
							items : [ addcouponForm ]
						}).show();
					}
				}
			}();

			function runAddCoupon() {

				AddCoupon.init();
				//Ext.getCmp('shopCode').setValue($('#neWShopCode').val()); 
				updateId = $('#hId').val();
				if("" != $('#hId').val()){
					Ext.getCmp('proName').setValue($('#hProName').val()); 
	 				Ext.getCmp('proValue').setValue($('#hProValue').val()); 
		
		 	    	channelSelect.store.on('load', function(store, record, opts) {
	 					Ext.getCmp('channelCode').setValue($('#hChannelCode').val()); 
	 				});
					channelSelect.store.load();
		 	    	shopSelect.store.on('load', function(store, record, opts) {
	 					Ext.getCmp('shopCode').setValue($('#hShopCode').val()); 
	 				});
	 				shopSelect.store.load();
				}

				AddCoupon.show();

			}

			runAddCoupon();
		});
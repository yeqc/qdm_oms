Ext.onReady(function() {
			
			var updateId="";
			var shopBusinessGoodsSelect;
			Ext.QuickTips.init();
			Ext.form.Field.prototype.msgTarget = 'side';
			
		//	var shopCodeStr = getGlobalValue("shopCode");
		//	var channelCodeStr = getGlobalValue("channelCode");

			ExprotShopBusinessGoodsCoupon = function() {
				var exprotshopForm;
				return {
					init : function() {
						
						 shopBusinessGoodsSelect= new Ext.form.ComboBox({
							id : 'shopCodeType',
							/*store :  new Ext.data.Store({
								proxy : new Ext.data.HttpProxy({
									url : basePath + '/custom/channelInfo/channelList.spmvc',
									method : 'GET'
								}),
								reader : new Ext.data.JsonReader({
									fields : [ 'chanelCode', 'channelTitle' ]
								})
							}),*/
							store : new Ext.data.SimpleStore( {
								data : [ [ '6', '6位码' ] ],
								fields : [ 'text', 'filed' ]
							}),
							xtype : 'combo',
							valueField : 'text',
							displayField : 'filed',
							mode : 'local',
							forceSelection : true,
							emptyText : '请选择位码',
							editable : false,			
							triggerAction : 'all',
							fieldLabel : '位码',
							width : 150
						});
			
							var myMask = new Ext.LoadMask(Ext.getBody(), {msg:"请稍候..."}); 
						 
						exprotshopForm = new Ext.FormPanel(
								{
									frame : true,
									height : 200,
									width : 500,
									labelAlign : 'right',
									buttonAlign : 'center',
									layout : 'form',
									items : [ /*{
										xtype : 'textfield',
										fieldLabel : '店铺编号',
									    id:'shopCode',
										allowBlank : false,
										blankText : '店铺编号不能为空',
										width : '160'
									}, {
										xtype : 'textfield',
										fieldLabel : '店铺名称',
										id:'shopTitle',
										allowBlank : false,
										blankText : '店铺名称不能为空',
										width : '160',
									},{
										 xtype: 'textfield', 
									        fieldLabel: '上传图片名',
									        id:'shopImg',
											name : 'shopImg',
									         name: 'file', 
									         width : '160'
										 },*/
										 shopBusinessGoodsSelect,
								/*	{
										xtype : 'textfield',
										fieldLabel : '物流回调接口',
										id:'logisticsCallback',
										name : 'logisticsCallback',
										allowBlank : false,
										width : '160'
									},{
										id:'backup',
										xtype : 'textarea',
										fieldLabel : '备注',
										width : '160'
									},{
										xtype : 'textarea',
										fieldLabel : '店铺鉴权信息',
										id:'shopAuthentication',
										name : 'shopAuthentication',
										allowBlank : true,
										width : '160'
									}*/],
									buttons : [
											{
												id:'ShopBusinessgoodsBtn',
												text : '导出',
												handler : function() {
													var url ="";
													var sixCodeUrl = basePath + '/custom/shopBusinessGoods/exportshopBusinessGoods.spmvc';
													var elevenCodeUrl = basePath + '/custom/shopBusinessGoods/exportElevenShopBusinessGoods.spmvc';
													var type = Ext.getCmp("shopCodeType").getValue();
													
													if(null == type || ''==type){
														alertMsg("验证","位码不能为空！");
														return;
													}
						
													if(6==type){
														url = sixCodeUrl;
													} else if(11==type){
														url = elevenCodeUrl;
													}
													
													Ext.getCmp('ShopBusinessgoodsBtn').setDisabled(true); 
										
												//	Ext.Msg.confirm("确认","是否导出商品:",
												//					function(btn) {
														//				if (btn == "yes") {// 确认
																			
												     myMask.show(); 

													
																			Ext.Ajax.request( {  
																				waitMsg : '请稍等.....',
																				url :url,
																				method : 'post',
																				timeout:7200000,
																				//method : 'post',
																				params : {
																					shopCode : $("#hShopCode").val(),
																					channelCode : $("#hChannelCode").val(),
																					status:$("#hStatus").val()
																				}, 
																				success : function(response) {
																					var respText = Ext.util.JSON.decode(response.responseText);
																					if(respText.isok){
																						
																						//window.open(basePath+"/page/shopBusinessGoods/exportFile/"+respText.message);
																						myMask.hide(); 	
																						window.location.href= basePath + '/custom/shopBusinessGoods/downloadFile.spmvc?path='+respText.message;
																						Ext.getCmp('ShopBusinessgoodsBtn').setDisabled(false); 
																					//	parent.OutChannelShop.refresh();
																			//			parent.FormEditWin.close();
																						 alertMsg("验证","导出成功！");
																					}else{
																						myMask.hide();
																						alertMsg(respText.message);
																					}		
																					
																				},
																				failure : function(response) {
																					   myMask.hide(); 	
																				//   var respText = Ext.util.JSON.decode(response.responseText); 
											                                        alertMsg("验证","失败");
																				}
																		    });
																//		}
															//		});
													//Ext.getCmp('ShopBusinessgoodsBtn').setDisabled(false);
													
												}
											}]
								});
					},
					show : function() {
						addoutStockPanel = new Ext.Panel({
							renderTo : 'exprotShopbusinessGoods-grid',
							autoHeight : true,
							autoWidth : true,
							layout : 'fit',
							items : [ exprotshopForm ]
						}).show();
					}
				}
			}();

	

			
			function uploadShowImg (){ //导入图片
		//		 var ticketCode  = Ext.getCmp("channelGoodsTicket.ticketCode").getValue(); //单据编号
		//		 var time = new Date().format('Y-m-d H:i:s');
		//		 var excetTime = $("#channelGoodsTicket_excuteTime").val() ;//执行时间
		//		 var isTiming =   Ext.getCmp("channelGoodsTicket.isTiming").getValue();//1为定时执行
		//		 var channelCode = Ext.getCmp("OutChannelShop.channelCode").getValue(); //渠道code
			//	 var shopCode = Ext.getCmp("shopCode").getValue(); //店铺名称code
			//	 var params = "&params=shopCode:"+shopCode;
		//		 encodeURIComponent(excetTime) +";isTiming:"+isTiming+";channelCode:"+channelCode;
         //        if(channelCode == ""){
          //      	 alertMsg("结果", "请选择渠道！");
		//			 return;
         //        }
			//	 if(isTiming ==""){
		//			 alertMsg("结果", "请选择执行类型！");
		//			 return;
		//		 }
		//		 if(isTiming=="1" && excetTime<time){
		//			 alertMsg("结果", "定时执行时间不得小于当前时间！");
		//			 return;
		//		 }
			//	FormEditWin.showAddDirWin(null,basePath+"/fileUpload.html?uploadUrl="+basePath+"custom/channelShop/uploadShopImg.spmvc"+params,"picADWinID","上传",580,300);
			}
			
			function runAddCoupon() {
				ExprotShopBusinessGoodsCoupon.init();
			/*	Ext.getCmp('shopCode').setValue($('#neWShopCode').val()); 
				updateId = $('#hId').val();
				if("" != $('#hId').val()){
					Ext.getCmp('shopCode').setValue($('#hShopCode').val()); 
	 				Ext.getCmp('shopTitle').setValue($('#hShopTitle').val()); 
	 				shopBusinessGoodsSelect.store.on('load', function(store, record, opts) {
	 					Ext.getCmp('channelCode').setValue($('#hChannelCode').val()); 
	 				});
	 				shopBusinessGoodsSelect.store.load();
	 				Ext.getCmp('shopImg').setValue($('#hshopImg').val()); 
	 				
	 				Ext.getCmp('logisticsCallback').setValue($('#hLogisticsCallback').val()); 
	 				Ext.getCmp('shopAuthentication').setValue($('#hShopAuthentication').val()); 
	 				Ext.getCmp('backup').setValue($('#hBackup').val());
				}*/

				ExprotShopBusinessGoodsCoupon.show();

			}

			runAddCoupon();
		});
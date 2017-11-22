Ext.onReady(function() {
			
			var updateId="";
			var channelSelect;
			Ext.QuickTips.init();
			Ext.form.Field.prototype.msgTarget = 'side';

			AddCoupon = function() {
				var addcouponForm;
				return {
					init : function() {
						
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
							width : 150
						});
	 
						addcouponForm = new Ext.FormPanel(
								{
									frame : true,
									height : 330,
									atuoWidth : true,
									labelAlign : 'right',
									buttonAlign : 'center',
									layout : 'form',
									items : [ {
										xtype : 'textfield',
										fieldLabel : '商品编号',
									    id:'goodsSn',
										width : '160'
									}, {
										xtype : 'textfield',
										fieldLabel : '商品名称',
										id:'goodsName',
										width : '160',
									},{
										 xtype: 'textfield', 
									        fieldLabel: '品牌名称',
									        id:'brandName',
											name : 'brandName',
									         name: 'file', 
									         width : '160'
										 },//,
										 //channelSelect,
									{
										xtype : 'textfield',
										fieldLabel : '售价',
										id:'salePrice',
										name : 'salePrice',
										width : '160'
									}/*,{
										id:'backup',
										xtype : 'textarea',
										fieldLabel : '备注',
										width : '160'
									}*/,{
										xtype : 'textfield',
										fieldLabel : '分类名',
										id:'catName',
										name : 'catName',
								
										width : '160'
									},{
										xtype : 'textarea',
										fieldLabel : '颜色设置',
										id:'setColor',
										name : 'setColor',
									
										width : '160'
									},{
										xtype : 'textarea',
										fieldLabel : '尺码设置',
										id:'setSize',
										name : 'setSize',
									
										width : '160'
									}],
									buttons : [
											/*{
												text : '保存/更新',
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
													Ext.Msg.confirm("确认","是否添加/修改店铺:"+ qty,
																	function(btn) {
																		if (btn == "yes") {// 确认
																			
																			Ext.Ajax.request( {
																				waitMsg : '请稍等.....',
																				url : basePath + 'custom/channelShop/insertChannelShop.spmvc',
																				method : 'post',
																				params : {
																					id:updateId, //用于更新主键
																					shopCode:Ext.getCmp("shopCode").getValue(), //店铺code
																					shopChannel: 1 , //渠道店铺
																				//	shopStatus:0, //店铺状态为未激活状态
																					backup:Ext.getCmp("backup").getValue(),
																					shopTitle:Ext.getCmp("shopTitle").getValue(),
																					logisticsCallback:Ext.getCmp("logisticsCallback").getValue(),
																					shopAuthentication:Ext.getCmp("shopAuthentication").getValue(),
																					channelCode:Ext.getCmp("channelCode").getValue(), //渠道code
																					shopImg:Ext.getCmp("shopImg").getValue() //图片名称
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
																				//	var respText = Ext.util.JSON.decode(response.responseText); 
											                                        alert("失败");
																				}
																			});
																		}
																	});
												}
											},*/
										/*	{
												text : '关闭',
												handler : function() {
													parent.FormEditWin.close();
												}
											} */]
								});
					},
					show : function() {
						addoutStockPanel = new Ext.Panel({
							renderTo : 'productGoodsDetails-grid',
							autoHeight : true,
							autoWidth : true,
							layout : 'fit',
							items : [ addcouponForm ]
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
	//			 var shopCode = Ext.getCmp("shopCode").getValue(); //店铺名称code
		//		 var params = "&params=shopCode:"+shopCode;
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

		//	 <input type="hidden" id="hgoodsSn" name="hgoodsSn" value="${gp.goodsSn}" />
		//	 <input type="hidden" id="hgoodsName" name="hgoodsName" value="${gp.goodsName}" />
		//	 <input type="hidden" id="hbrandName" name="hbrandName" value="${gp.brandName}" />
		
		//	 <input type="hidden" id="hsalePrice" name="hsalePrice" value="${gp.salePrice}" />
		//	 <input type="hidden" id="hcatName" name="hcatName" value="${gp.catName}" />
			
			function runAddCoupon() {
				AddCoupon.init();
			//	Ext.getCmp('shopCode').setValue($('#neWShopCode').val()); 
			//	updateId = $('#hId').val();
		//		if("" != $('#hId').val()){
		//			Ext.getCmp('shopCode').setValue($('#hShopCode').val()); 
	 	//			Ext.getCmp('shopTitle').setValue($('#hShopTitle').val()); 
	 			//	channelSelect.store.on('load', function(store, record, opts) {
	 			//		Ext.getCmp('channelCode').setValue($('#hChannelCode').val()); 
	 		//		});
	 			//	channelSelect.store.load();
	 			//	Ext.getCmp('shopImg').setValue($('#hshopImg').val()); 
	 				
	 		//		Ext.getCmp('logisticsCallback').setValue($('#hLogisticsCallback').val()); 
	 		//		Ext.getCmp('shopAuthentication').setValue($('#hShopAuthentication').val()); 
	 		//		Ext.getCmp('backup').setValue($('#hBackup').val());
			//	}
				
				if("" !=  $('#hgoodsSn').val()) {
					Ext.getCmp('goodsSn').setValue($('#hgoodsSn').val()); 
					Ext.getCmp('goodsName').setValue($('#hgoodsName').val()); 
					Ext.getCmp('brandName').setValue($('#hbrandName').val()); 
					Ext.getCmp('salePrice').setValue($('#hsalePrice').val()); 
					Ext.getCmp('catName').setValue($('#hcatName').val());

					var colorHtml="";
					$("input[name='colorNameAndColorCode']").each(  
						function(){  
							colorHtml += $(this).val()+" ";
							
						}  
					); 
					Ext.getCmp('setColor').setValue(colorHtml);
					
					var sizeHtml = "";
					$("input[name='sizeNameAndSizeCode']").each(
						function(){  
							sizeHtml += $(this).val()+" ";
						}  
					); 
					Ext.getCmp('setSize').setValue(sizeHtml);
		
				}

				AddCoupon.show();

			}

			runAddCoupon();
		});
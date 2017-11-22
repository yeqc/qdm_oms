Ext
		.onReady(function() {
			var updateId="";

			Ext.QuickTips.init();
			Ext.form.Field.prototype.msgTarget = 'side';

			AddChannelInfo = function() {
				var channelInfoForm; 
				return {
					init : function() {
						var typeOptionComboadd = new Ext.form.ComboBox({
							id : 'channelType',
							store : new Ext.data.SimpleStore({
								data : [ [ '1', '线上直营渠道' ], [ '2', '线上加盟渠道' ],['3','线下直营渠道'],['4','线下加盟渠道'] ],
								fields : [ 'text', 'filed' ]
							}),
							xtype : 'combo',
							valueField : 'text',
							displayField : 'filed',
							mode : 'local',
							forceSelection : true,
							blankText : '请选择渠道类型',
							emptyText : '请选择渠道类型',
							name : 'channelType',
							editable : false,
							allowBlank : false,
							blankText : '渠道类型不能为空',
							hiddenName : 'channelType',
							triggerAction : 'all',
							fieldLabel : '渠道类型',
							width : 180
						});
						
						channelInfoForm = new Ext.FormPanel(
								{
									frame : true,
									bodyStyle : 'padding:10px 5px 5px 0px',
									width : 400,
									height : 310,
									labelAlign : 'left',
									buttonAlign : 'center',
									layout : 'form',
									items : [{
										xtype : 'textfield',
										fieldLabel : '渠道编号',
									    id:'channelCode',
										name : 'channelCode',
										allowBlank : false,
										blankText : '渠道编号不能为空',
										width : '180'
									},{
										xtype : 'textfield',
										fieldLabel : '渠道名称',
									    id:'channelTitle',
										allowBlank : false,
										blankText : '渠道名称不能为空',
										width : '180'//,
									} ,typeOptionComboadd,{
										xtype : 'textarea',
										fieldLabel : '备注信息',
										id:'backup',
										allowBlank : true,
										width : '180',
										height:'100'
									} 

								 ],
									buttons : [
											{
												text : '保存/更新',
												handler : function() {
													// 获取店铺的名称
													var qty = channelInfoForm.getForm().findField('channelTitle').getValue();
													if (!channelInfoForm.getForm().isValid()) {
														alertMsg("验证","请检查数据是否校验！");
														return;
													}
													Ext.Msg.confirm("确认","是否添加/修改渠道:"+ qty,
																	function(btn) {
																		if (btn == "yes") {// 确认
																			Ext.Ajax.request( {
																				waitMsg : '请稍等.....',
																				url : basePath + 'custom/channelInfo/insertChannelInfo.spmvc',
																				method : 'post',
																				params : {
																					id:updateId, //用于更新主键把查看主键带回去
																					channelTitle: Ext.getCmp("channelTitle").getValue(), //店铺code
																				//	channelStatus:0, //渠道状态为未激活状态
																					channelType:Ext.getCmp("channelType").getValue(),
																					chanelCode: Ext.getCmp("channelCode").getValue(), //数据库字段为chanelcode
																					backup: Ext.getCmp("backup").getValue()
																				}, 
																				success : function(response) {
																					var respText = Ext.util.JSON.decode(response.responseText);
																					if(respText.isok){
																						parent.ChannelBaseInfo.refresh();
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
							renderTo : 'addChannelInfo-grid',
							autoHeight : true,
							autoWidth : true,
							layout : 'fit',
							items : [ channelInfoForm ]
						}).show();
					}
				}
			}();

			function runAddChannelInfo() {
				AddChannelInfo.init();
			//	Ext.getCmp('channelCode').setValue($('#newChannelCode').val()); 
				updateId = $('#hId').val();//将修改记录的ID带回后台用于主键修改信息
				if("" != $('#hId').val()){
					
					Ext.getCmp('channelTitle').setValue($('#hChannelTitle').val()); 
	 				Ext.getCmp('channelCode').setValue($('#hChannelCode').val()); 
	 				Ext.getCmp('channelType').setValue($('#hChannelType').val()); 
	 				Ext.getCmp('backup').setValue($('#hBackup').val()); 
	 			 
				}
				AddChannelInfo.show();

			}

			runAddChannelInfo();
		});

Ext.onReady(function() {
	var ticketInfoGrid;
	var pageSize=15;
	var ticketInfoStore;
	var shopSelectOption;
	var channelInfoCombo;
	var shopCodeStr = parent.getGlobalValue("shopCode");
	var chanelCodeStr = parent.getGlobalValue("channelCode");
	TicketInfo= function() {
				var ticketInfoForm;
				var ticketInfoProxy;
				var ticketInfoReader;
				var ticketInfoCheckBoxSelect;
				var ticketInfoColumnGrid;
				var tbar;
				
				return {
					init : function() {
						
						//渠道信息选择框
						 channelInfoCombo = new Ext.form.ComboBox({
							id : 'OutChannelShop3.channelCode',
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
							readOnly:true,
							emptyText : '请选择渠道',
							editable : false,			
							triggerAction : 'all',
							fieldLabel : '渠道',
							width : 150
						});
			          //上下架调整类型
					 var typeOptionCombo = new Ext.form.ComboBox( {
							id : 'channelGoodsTicket3.ticketType',
							store : new Ext.data.SimpleStore( {
								data : [ [ '1', '上架' ], [ '2', '下架' ]],
								fields : [ 'text', 'filed' ]
							}),
							xtype : 'combo',
							valueField : 'text',
							displayField : 'filed',
							labelAlign : 'left',
							mode : 'local',
							forceSelection : true,
							blankText : '请选择调整类型',
							emptyText : '请选择调整类型',
							name : 'channelGoodsTicket3.ticketType',
							editable : false,
							hiddenName : 'channelGoodsTicket3.ticketType',
							triggerAction : 'all', 
							fieldLabel : '调整类型',
							width : 150
						});
					 
					 //经营店铺选择框
					  shopSelectOption = new Ext.form.ComboBox( {
							id : 'channelGoodsTicket3.shopCode',
							store : new Ext.data.Store({
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
							forceSelection : true,
							readOnly:true,
							emptyText : '请选择经营店铺',
							mode : 'remote',
							editable : false,
							triggerAction : 'all', 
							fieldLabel : '经营店铺',
							width : 150
						});
					 		
					 
					 //执行类型选择框
					 var isTimingSelectOption = new Ext.form.ComboBox( {
							id : 'channelGoodsTicket3.isTiming',
							store : new Ext.data.SimpleStore( {
								data : [ [ '0', '否' ], [ '1', '是' ]],  //, [ '1', '是' ]
								fields : [ 'text', 'filed' ]
							}),
							allowBlank:false,
							xtype : 'combo',
							valueField : 'text',
							displayField : 'filed',
							mode : 'local',
							forceSelection : true,
							emptyText : '请选择是否定时执行',							
							editable : false,
							triggerAction : 'all', 
							fieldLabel : '是否定时执行',	
							width : 150,
							listeners:{
					                'select': function(){
					                        if(Ext.getCmp("channelGoodsTicket3.isTiming").getValue() == '0')
					                        {    
					                        	$("#channelGoodsTicket_excuteTime").val("") ;//执行时间
					                        	Ext.getCmp("excuteTimeNo").disable(); 
					                        }else{
					                        	Ext.getCmp("excuteTimeNo").enable(); 
					                        }
					                    }
					                }
						});
					 
						ticketInfoForm = new Ext.FormPanel(
								{
									labelAlign : 'top',
									frame : true,
									bodyStyle : 'padding:5px 5px 0',
									autoHeight : true,
									autoScroll : true,
									autoWidth : true,
									url : "",
									labelAlign : 'left',
									layout : 'form',//shopSelectOption,payStatusOption
									items : [ {
										layout : 'column',
										items : [{
													layout : 'form',
													labelWidth:80,
													columnWidth:0.3,
													items : [{
														id : 'channelGoodsTicket3.ticketCode',
														xtype : 'textfield',
														fieldLabel : '调整单编号',
														name : 'channelGoodsTicket3.ticketCode', 
														allowBlank:false,
														blankText : '调整单编号不能为空',
														readOnly:true,
														
														width : '150'
													} ,isTimingSelectOption]
												},{
													layout : 'form',
													labelWidth:80,
													columnWidth:0.33,
													items : [ 
                                                channelInfoCombo,{
                                                	id : "excuteTimeNo",
												    html : '<div id="hiddenTime"><input type="text" id="channelGoodsTicket_excuteTime" onClick="WdatePicker({dateFmt:\'yyyy-MM-dd HH:mm:ss\'})"/></div>',
												    fieldLabel:'执行时间',
												    width:150
												}]
												},{
													layout : 'form',
													labelWidth:80,
													columnWidth:0.3,
													items : [shopSelectOption]
												}
												]
									}],
									buttons : []
								});
								 
								ticketInfoCheckBoxSelect = new Ext.grid.CheckboxSelectionModel();
						
						ticketInfoColumnGrid = new Ext.grid.ColumnModel( [
								ticketInfoCheckBoxSelect, {
									id : 'id',
									header : "id",
									align : "center",
									width : 80,
									hidden:true,
									dataIndex : 'id'
								},{
									id : 'goodsSn',
									align : "center",
									sortable : true,
									header : "商品款号",
									width : 200,
									dataIndex : 'goodsSn'
								},{
									id : 'goodsTitle',
									header : "商品名称",
									align : "center",
									width : 400,
									dataIndex : 'goodsTitle'
								},
								{
									id : 'isOnlineOffline',
									header : "线上线下款商品状态",
									align : "center",
									width : 200,
									renderer:function(e){
									       var msg=""; 
									       if(e=='1'){
									    	   msg="线上款";
									       } 
									       if(e=='2'){
									    	   msg="线下款";
									       } 
									       if(e=='3'){
									    	   msg="线上线下同款";
									       }
									       return msg;
									},  
									dataIndex : 'isOnlineOffline'
								}
						]);

						//与列对应的dataIndex
						couponrecord = Ext.data.Record.create( [{
							name : 'id'
						},{
							name : 'goodsSn'
						}, {
							name : 'channelGoodssn'
						}, {
							name : 'goodsTitle'
						}, {
							name : 'isOnlineOffline'
						}]);
						
						//进入页面加载数据
						ticketInfoProxy = new Ext.data.HttpProxy( {
							url : basePath+"custom/ticketInfo/getTicketInfoList.spmvc",
							method : "post"
						});
						
						//Reader 读json中数据
						ticketInfoReader = new Ext.data.JsonReader( {
							root : 'root',
							totalProperty : 'totalProperty'
						}, couponrecord);
						
						ticketInfoStore = new Ext.data.Store( { 
							autoLoad : {
								params : {
									start : 0,
									limit : pageSize,
									ticketCode : $("#addNewTicketCode").val()
								}
							},
							proxy :  ticketInfoProxy,
							reader : ticketInfoReader
						});

						  // 如果不是用ajax的表单封装提交,就要做如下操作.
				        //这里很关键，如果不加，翻页后搜索条件就变没了，这里的意思是每次数据载入前先把搜索表单值加上去，这样就做到了翻页保留搜索条件了   
						ticketInfoStore.on('beforeload', function(){
				            var keyWord = Ext.getCmp("channelGoodsTicket3.ticketCode").getValue();
				            Ext.apply(this.baseParams, {ticketCode: keyWord});   
				        });
						
						//定义菜单栏
						tbar = [{
							id : 'message_grid_add',
							text : '批量添加商品',
							tooltip : '批量添加商品',
							iconCls : 'add',
							handler :upload
						    },{ 
						    id : 'message_grid_delete',
							text : '批量移除',
							tooltip : '批量移除',
							iconCls : 'delete',
							handler : function(){
								TicketInfo.deleteTicketInfo();
							} //删除记录数
						    }
						    ,{ 
						    	id : 'message_grid_update',
								text : '更新调整单',
								tooltip : '更新调整单',
								iconCls : 'add',
								handler : addChannelGoodsTicket
							    },'<a style="color:red" href = "'+ basePath + '/custom/fileUpload/fileDownloadCsv.spmvc?filePath=goodsStoreBinding.csv' + '">模板下载</a>'
						    ];
						 
						ticketInfoGrid = new Ext.grid.GridPanel( {
							autoWidth : true,
							height : 400,
							title : '商品门店关联',
							store : ticketInfoStore,
							id:'addForms_gridss_id',
							trackMouseOver : false,
							disableSelection : true,
							loadMask : true,
							frame : true,
				            columnLines : true,
							tbar:  tbar,
							// grid columns  
							cm : ticketInfoColumnGrid,
							sm:ticketInfoCheckBoxSelect,
							// paging bar on the bottom  
							bbar : new IssPagingToolbar(ticketInfoStore, pageSize),
							listeners : {
					            'rowdblclick': function(thisgrid ,rowIndex ,e){
									var selectionModel = thisgrid.getSelectionModel();
									var record = selectionModel.getSelected();
									var id = record.data['id']; 
    											 
								   // FormEditWin.showAddDirWin("popWins",basePath+"macalline/notices/toEditInNoticesPage.action?id="+id,"pop_message_winID","编辑查看通知单信息",1350,580);
									   
					            }
					        }
						});

					},

					show : function() {
						 
						couponPanel = new Ext.Panel( {
							renderTo : 'shopGoodsBandingStore-grid',
							
							layout : 'fit',
							items : [ ticketInfoForm, ticketInfoGrid ]
						}).show();
					},
					
					doAfter : function(data){//上传后的数据返回
						
						var json = Ext.util.JSON.decode(data);
                             FormEditWin.close();
                        	 alertMsg("结果", json.message);
                             ticketInfoStore.reload();
                             parent.ChannelGoodsTicketVo.refresh();

					},
					//删除整个panel里面被选择的行数据
					  deleteTicketInfo :function(){ 
							var selModel = ticketInfoGrid.getSelectionModel();
							if (selModel.hasSelection()) {

								var records = selModel.getSelections();
								var ids="";
								for ( var i = 0; i < records.length; i++) {
									var id = records[i].get("id"); 
									
									if(id && id!='' && id!=null){
									// store.remove(records[i]);
									 ids += ""+id+",";
								     
									}								
									
								}
								if(ids != ""){//批量删除
								 Ext.Ajax
										.request( {
											waitMsg : '请稍等.....',
											url : basePath + 'custom/ticketInfo/deleteTicketInfo.spmvc',
											method : 'post',
											params : {
												ids : ids
											},
											success : function(response) {
												var respText = Ext.util.JSON.decode(response.responseText);
											    if(respText.isok){ //成功
											    	alertMsg("结果", "删除成功！");
											    	ticketInfoStore.reload();
											    }else{
											    	alertMsg("结果", "删除失败！");
											    	ticketInfoStore.reload();
											    }
														
											},
											failure : function(response, options) {
												var respText = Ext.util.JSON.decode(response.responseText); 
		                                        alert(respText.msg);
											}
										});
								}


							} else {
								alertMsg("错误", "请选择要批量删除的行!");
							}
						}
				}
			}();

			function runTicketInfoList() {
				TicketInfo.init(); 
				TicketInfo.show();
			}
 
			runTicketInfoList();
			
			function upload (){ //导入excel以后就与调整单绑定传入数据库保存
				 var ticketCode  = Ext.getCmp("channelGoodsTicket3.ticketCode").getValue(); //单据编号
				 var time = new Date().format('Y-m-d H:i:s');
				 var excetTime = $("#channelGoodsTicket_excuteTime").val() ;//执行时间
				 var isTiming =   Ext.getCmp("channelGoodsTicket3.isTiming").getValue();//1为定时执行
				 var channelCode = Ext.getCmp("OutChannelShop3.channelCode").getValue(); //渠道code
				 var shopCode = Ext.getCmp("channelGoodsTicket3.shopCode").getValue(); //店铺名称code
				 var params = "&params=ticketCode:"+ticketCode+";shopCode:"+shopCode+";excetTime:"+
				 encodeURIComponent(excetTime) +";isTiming:"+isTiming+";channelCode:"+channelCode;
                 if(channelCode == ""){
                	 alertMsg("结果", "请选择渠道！");
					 return;
                 }
				 if(isTiming ==""){
					 alertMsg("结果", "请选择执行类型！");
					 return;
				 }
				 if(isTiming=="1" && excetTime<time){
					 alertMsg("结果", "定时执行时间不得小于当前时间！");
					 return;
				 }
				FormEditWin.showAddDirWin(null,basePath+"/fileUpload.html?uploadUrl="+basePath+"custom/ticketInfo/inportChannelGoodsStoreBanding.spmvc"+params,"picADWinID","上传",480,300);
			}
            
			
			//修改调整单中包含的商品信息
			function addChannelGoodsTicket() { 
				      var ticketCode  = Ext.getCmp("channelGoodsTicket3.ticketCode").getValue(); //单据编号
				      var shopCode = Ext.getCmp("channelGoodsTicket3.shopCode").getValue(); //店铺名称code
				      var isTimming = Ext.getCmp("channelGoodsTicket3.isTiming").getValue(); //是否定时执行
				      var excuteTime = $("#channelGoodsTicket_excuteTime").val(); //Ext.getCmp("channelGoodsTicket_excuteTime").getValue().format('Y-m-d H:i:s'); //执行时间
				      var time = new Date().format('Y-m-d H:i:s');
				      if(isTimming ==""){
							 alertMsg("结果", "请选择执行类型！");
							 return;
						 }
				      if(isTimming=="1" && excuteTime<time){
							 alertMsg("结果", "定时执行时间不得小于当前时间！");
							 return;
						 }
				      var path = "";
				      if(excuteTime == null || excuteTime == ""){
				    	path =  basePath + '/custom/ticketInfo/addChannelGoodsAndTickets.spmvc';
				      }else{
				    	path = basePath + '/custom/ticketInfo/addChannelGoodsAndTickets.spmvc?excutTimeStr='+excuteTime;
				      }
				    //生成调整单
						 Ext.Ajax
								.request( {
									waitMsg : '请稍等.....',
									url : path,
									method : 'post',
									params : {
										ticketCode:ticketCode,
										channelCode:Ext.getCmp("OutChannelShop3.channelCode").getValue(),
										shopCode:shopCode,
										ticketType:1, //上下架类型
										isTiming:isTimming //是否定时执行
									},
									success : function(response) {
										var respText = Ext.util.JSON.decode(response.responseText);
									    if(respText.isok){ //成功
									    	alertMsg("结果", "生成/更新调整单成功！！");   //	upload(); //打开上传界面
									    	ticketInfoStore.reload();
									       parent.ChannelGoodsTicketVo.refresh();
									    }else{
									    	alertMsg("结果", "生成/更新调整单失败！！");
									    	ticketInfoStore.reload();
									    }
												
									},
									failure : function(response, options) {
									//	var respText = Ext.util.JSON.decode(response.responseText); 
										alertMsg("结果", "生成/更新调整单失败！！");
									}
								});
				}
			
	  function runChannelGoodsTicket() {//点击查看/更新页面展示		
		    // alert("shopCodeStr="+shopCodeStr);
					Ext.getCmp('channelGoodsTicket3.ticketCode').setValue($("#addNewTicketCode").val()); 
					
					shopSelectOption.store.on('load', function(store, record, opts) {
	 					Ext.getCmp('channelGoodsTicket3.shopCode').setValue(shopCodeStr); 
	 				});
					shopSelectOption.store.load();
					
					channelInfoCombo.store.on('load', function(store, record, opts) {
	 					Ext.getCmp('OutChannelShop3.channelCode').setValue(chanelCodeStr); 
	 				});
					channelInfoCombo.store.load();
	 			//	Ext.getCmp('channelGoodsTicket3.shopCode').setValue($("#addNewShopCode").val());
					$("#channelGoodsTicket_excuteTime").val($("#addNewExcetTime").val());
					//是否定时执行($("#addNewIsTiming").val())
					Ext.getCmp('channelGoodsTicket3.isTiming').setValue($("#addNewIsTiming").val());
					
					$("#channelGoodsTicket_excuteTime").val($("#addNewExcetTime").val()) ;//执行时间
					Ext.getCmp('channelGoodsTicket3.isTiming').setValue("0");
					var isTiming = $("#addNewIsTiming").val();
					if (null == isTiming || isTiming == '') {
						isTiming = '0';
					}
					Ext.getCmp('channelGoodsTicket3.isTiming').setValue(isTiming);
					if(isTiming == '0'){
						 Ext.getCmp("excuteTimeNo").disable(); 
						 $("#channelGoodsTicket_excuteTime").val("") ;//执行时间
					}
	}

	
	runChannelGoodsTicket();//点击查看修改的时候显示头部数据
	 
	function buttonShow(){
//		var excuteTime = $("#addNewExcetTime").val();
//		var time = new Date().format('Y-m-d H:i:s');
//		var isTiming = $("#addNewIsTiming").val(); //是否为定时执行，1为是
		var ticketStatus = $("#addNewTicketStatus").val(); //根据调整单状态显示按钮1审核，2移除，3已执行
		if(ticketStatus=="1" || ticketStatus=="2" || ticketStatus=="3"){
			Ext.getCmp("message_grid_add").setDisabled(true);
			Ext.getCmp("message_grid_delete").setDisabled(true);
			Ext.getCmp("message_grid_update").setDisabled(true);
		}
	}
	buttonShow();
		});
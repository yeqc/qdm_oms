Ext.onReady(function() {
	var channelGoodsInfoStore;
	var channelGoodsInfoForm;
	var channelGoodsInfoGrid;
	var shopCodeStr = getGlobalValue("shopCode");
	var shopTitle = getGlobalValue('shopTitle');
	var channelCodeStr = getGlobalValue('channelCode');
	ChannelGoodsInfoVo= function() {
				var channelGoodsInfoProxy;
				var channelGoodsInfoReader;
				var checkBoxSelect;
				var channelGoodsInfoColumnGrid;
				var tbar;
				var pageSize=15;
				return {
					init : function() {
					//单据类型选择框
					var typeOptionCombo = new Ext.form.ComboBox( {
							id : 'channelGoodsInfoVo.isTiming',
							store : new Ext.data.SimpleStore( {
								data : [ [ '0', '立即执行' ], [ '1', '定时执行' ]],
								fields : [ 'text', 'filed' ]
							}),
							xtype : 'combo',
							valueField : 'text',
							displayField : 'filed',
							labelAlign : 'left',
							mode : 'local',
							forceSelection : true,
							emptyText : '请选择执行类型',
							name : 'channelGoodsInfoVo.isTiming',
							editable : false,
							hiddenName : 'channelGoodsInfoVo.isTiming',
							triggerAction : 'all', 
							fieldLabel : '执行类型',
							width : 150
						});

					 //单据状态选择框
					 var payStatusOption = new Ext.form.ComboBox( {
							id : 'channelGoodsInfoVo.ticketStatus',
							store : new Ext.data.SimpleStore( {
								data : [ [ '0', '未审核' ], [ '1', '已审核' ],[ '2', '已移除' ],
										 [ '3', '已执行' ]],
								fields : [ 'text', 'filed' ]
							}),
							xtype : 'combo',
							valueField : 'text',
							displayField : 'filed',
							mode : 'local',
							forceSelection : true,
							emptyText : '请选择单据状态',
							name : 'channelGoodsInfoVo.ticketStatus',
							editable : false,
							hiddenName : 'channelGoodsInfoVo.ticketStatus',
							triggerAction : 'all', 
							fieldLabel : '单据状态',
							width : 150
						});
					 
					 
						
						channelGoodsInfoForm = new Ext.FormPanel(
								{
									id : "channelGoodsInfo_form",
									frame : true,
									bodyStyle : 'padding:5px 5px 0',
									autoHeight : true,
									autoScroll : true,
									autoWidth : true,
									url : "",
									labelAlign : 'right',
									layout : 'form',//shopSelectOption,payStatusOption
									items : [ {
										layout : 'column',
										items : [{
													layout : 'form',
													labelWidth:80,
													columnWidth:0.3,
													items : [{
														xtype : 'textfield',
														fieldLabel : '经营店铺',
														id:'channelGoodsInfoVo.shopCode',
														readOnly : true,
														value : shopTitle,
														width : '150'
													} , {
														id : 'channelGoodsInfoVo.ticketCode',
														xtype : 'textfield',
														labelAlign : 'left',
														fieldLabel : '调整单编号',
														name : 'channelGoodsInfoVo.ticketCode', 
														blankText : '调整单编号不能为空',
														width : '142'
													}]
												},{
													layout : 'form',
													labelWidth:80,
													columnWidth:0.33,
													items : [ 
											 typeOptionCombo ,payStatusOption
															]
												}]
									}],
									buttons : [
											{
												text : '查询',
												columnWidth : .1,
												handler : this.search
											}, {
												text : '重置',
												handler : clickResetButton,
											} 
											]
								});
						
						
						var menu = new Ext.menu.Menu({
							items : [{
								text : '生成电脑版调整单',
								iconCls : 'add',
								handler : function() {			
									FormEditWin.showAddDirWin("popWins",basePath+"/custom/ticketInfo/createTicket.spmvc?ticketType=2","pop_message_winID","生成电脑版调整单",860,560);
								}                                                      		
							},{
								text : '生成手机版调整单',
								iconCls : 'add',
								handler : function() {
									FormEditWin.showAddDirWin("popWins",basePath+"/custom/ticketInfo/createTicket.spmvc?ticketType=12","pop_message_winID","生成手机版调整单",860,560);
								}
							}]
						});
						
						checkBoxSelect = new Ext.grid.CheckboxSelectionModel();
						
						channelGoodsInfoColumnGrid = new Ext.grid.ColumnModel( [
								checkBoxSelect, {
									id : 'id',
									header : "id",
									align : "center",
									width : 80,
									hidden:true,
									dataIndex : 'id'
								},{
									id : 'ticketCode',
									align : "center",
									sortable : true,
									header : "调整单号",
									width : 150,
									dataIndex : 'ticketCode'
								},{
									id : 'shopTitle',
									header : "经营店铺",
									align : "center",
									width : 120,
									dataIndex : 'shopTitle'
								},

								{
									id : 'ticketType',
									header : "单据类型",
									align : "center",
									width : 120,
									renderer:function(e){
										var msg=""; 
										if(e=='0'){
											msg="修改价格";
										} 
										if(e=='1'){
											msg="上下架维护";
										}  
										if(e=='2'){
											msg="电脑版详情维护";
										}
										if(e=='3'){
											msg="卖点管理";
										}
										if(e=='4'){
											msg="商品名称调整";
										}
										/*if(e=='12'){
											msg="手机版详情维护";
										}*/
										return msg;
									},  
									dataIndex : 'ticketType'
								}, 
								{
									id : 'isTiming',
									header : "执行类型",
									align : "center",
									width : 120,
									renderer:function(e){
										var msg=""; 
										if(e=='0'){
											msg="立即执行";
										}
										if(e=='1'){
											msg="定时执行";
										}
										return msg;
									},
									dataIndex : 'isTiming'
								}, 
								{
									id : 'ticketStatus',
									align : "center",
									sortable : true,
									header : "单据状态",
									width : 120,
									renderer:function(e){
										var msg=""; 
										if(e=='0'){
											msg="<font color='red'>未审核<font>";
										} 
										if(e=='1'){
											msg="<font color='green'>已审核<font>";
										}
										if(e=='2'){
											msg="<font color='red'>已移除<font>";
										}
										if(e=='3'){
											msg="<font color='green'>已执行<font>";
										}
										return msg;
									},
									dataIndex : 'ticketStatus'
								}, 
								{
									id : 'formatAddTime',
									header : "单据生成时间",
									align : "center",
									width : 140,
									dataIndex : 'formatAddTime'
								},
								{
									id : 'formatExcetTime',
									header : "执行时间",
									align : "center",
									width : 140,
									dataIndex : 'formatExcetTime'
								},{
									id : 'note',
									header : "执行结果",
									align : "center",
									width : 100,
									renderer:function(e){
										return  "<font color='red'>"+e+"<font>";
									},
									dataIndex : 'note'
								} , {
									id : 'operUser',
									header : "申请人",
									align : "center",
									width : 100,
									dataIndex : 'operUser'
								} , {
									header : "操作",
									align : "center",
									width : 200,
									dataIndex : 'ticketStatus',
									renderer:function(e){
										var msg= "<input type='button' value='生成详情' onclick='ChannelGoodsInfoVo.selectTemplate();' />&nbsp;&nbsp;&nbsp;" +
											"<input type='button' value='导出调整单' onclick='ChannelGoodsInfoVo.exportRecord();' />"; 
										if(e=='3'){
											msg= "<input type='button' value='生成详情' onclick='ChannelGoodsInfoVo.selectTemplate();' />&nbsp;&nbsp;&nbsp;" +
											"<input type='button' value='导出执行结果' onclick='ChannelGoodsInfoVo.exportRecord();' />"; 
										}
										return msg;
									}
								}
						]);

						//与列对应的dataIndex
						goodsInforecord = Ext.data.Record.create( [{
							name : 'id'
						},{
							name : 'ticketCode'
						}, {
							name : 'shopTitle'
						}, {
							name : 'ticketType'
						}, {
							name: 'isTiming'
						}, {
							name : 'ticketStatus'
						}, {
							name : 'formatAddTime'
						}, {
							name : 'formatExcetTime'
						},{
							name : 'note'
						},{
							name : 'operUser'
						}]);
						
						//加载列表数据
						channelGoodsInfoProxy = new Ext.data.HttpProxy( {
							url : basePath+"/custom/shopGoods/shopGoodsUpDownList.spmvc",
							method : "post"
						});
						//Reader 读json中数据
						channelGoodsInfoReader = new Ext.data.JsonReader( {
							root : 'root',
							totalProperty : 'totalProperty'
						}, goodsInforecord);
						
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
						
						// 如果不是用ajax的表单封装提交,就要做如下操作.
						//这里很关键，如果不加，翻页后搜索条件就变没了，这里的意思是每次数据载入前先把搜索表单值加上去，这样就做到了翻页保留搜索条件了   
						channelGoodsInfoStore.on('beforeload', function(){
							Ext.apply(this.baseParams, {ticketType: 2, shopCode:shopCodeStr,channelCode:channelCodeStr});
						});

						//定义菜单栏
						tbar = [
						     
				       /* {
							text : '生成调整单',
							menu : menu
						} */
						        
						{
							id : 'message_grid_tBar@add',
							text : '生成调整单',
							tooltip : '生成调整单',
							iconCls : 'add',
							handler : function() {
								FormEditWin.showAddDirWin("popWins",basePath+"/custom/ticketInfo/createTicket.spmvc?ticketType=2","pop_message_winID","生成调整单",860,560);
							}
						}
						,{
							id: 'message_grid_tBar@update',
							text : '批量执行',
							tooltip : '批量执行',
							iconCls : 'add',
							handler : function() {
								ChannelGoodsInfoVo.reviewObject();
							 
							}
						} , {
							id : 'message_grid_tBar@delete',
							text : '批量移除',
							tooltip : 'delete',
							iconCls : 'delete',
							handler:function(){
								ChannelGoodsInfoVo.deleteObject();
							}
						}];
						channelGoodsInfoGrid = new Ext.grid.GridPanel( {
//							autoWidth : true,
							height : 500,
							title : '店铺商品信息管理',
							store : channelGoodsInfoStore,
							id:'channelGoodsInfo_gridss_id',
							trackMouseOver : false,
							disableSelection : true,
//							autoExpandColumn: "formatAddTime", //自动伸展，占满剩余区域
							loadMask : true,
							frame : true,
							columnLines : true,
							tbar:  tbar,
							// grid columns  
							cm : channelGoodsInfoColumnGrid,
							sm:checkBoxSelect,
							// paging bar on the bottom  
							bbar : new IssPagingToolbar(channelGoodsInfoStore, pageSize),
							listeners : {
								'rowdblclick': function(thisgrid ,rowIndex ,e){
									var selectionModel = thisgrid.getSelectionModel();
									var record = selectionModel.getSelected();
									var id = record.data['id']; 
									var ticketType = record.data['ticketType']; 
									
									FormEditWin.showAddDirWin("popWins",basePath+"/custom/ticketInfo/createTicket.spmvc?id="+id+"&ticketType="+ticketType,"pop_message_winID","查看/更新调整单",860,560);
								}
							}
						});

					},
					checkOrUpdate:function(){//查看编辑按钮，需要把调整单号作为参数传递过去
						
						var selModel = channelGoodsInfoGrid.getSelectionModel();
						if (selModel.hasSelection()) {

							var records = selModel.getSelections();					
							var id = records[0].get("id");
							if(id != ""){
								 FormEditWin.showAddDirWin("popWins",basePath+"/custom/InfoInfo/updateOrSearchInfoInfo.spmvc?id="+id,"pop_message_winID","查看/更新调整单",860,560);
							}else{
								 alertMsg("结果", "请选择需要查看的行！");
							}
						}
					},
					exportRecord:function(){//导出本行的数据
						var selModel = channelGoodsInfoGrid.getSelectionModel();
						if (selModel.hasSelection()) {
							var records = selModel.getSelections();
							var id = records[0].get("id");
							if(id != ""){
								var path = basePath + '/custom/shopGoods/exportChannelGoodsTickets.spmvc?id='+id+"&type=goodsInfo";
								
								window.location.href = path;
								alertMsg("结果", "导出调整单成功！");
							}
	

						} else {
							alertMsg("错误", "请选择要导出的行!");
						}
						
					},
					//批量未审核
					unReviewObject:function(){
						 //批量未审核整个panel里面被选择的行数据
						var selModel = channelGoodsInfoGrid.getSelectionModel();
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
							if(ids != ""){//批量审核
							 Ext.Ajax
									.request( {
										waitMsg : '请稍等.....',
										url : basePath + '/custom/shopGoods/reviewChannelGoodsInfo.spmvc',
										method : 'post',
										params : {
											ids : ids,ticketStatus:0   //0为未审核状态
										},
										success : function(response) {
											var respText = Ext.util.JSON.decode(response.responseText);
											if(respText.isok){ //成功
												alertMsg("结果", "批量未审核完成");
												channelGoodsInfoStore.reload();
											}else{
												alert("未审核出错");
												channelGoodsInfoStore.reload();
											}
										},
										failure : function(response, options) {
											var respText = Ext.util.JSON.decode(response.responseText); 
											alert("未审核失败");
										}
									});
							}
						} else {
							alertMsg("错误", "请选择要批量未审核的行!");
						}					
					},
					reviewObject:function(){
						
						 //批量审核整个panel里面被选择的行数据
						var selModel = channelGoodsInfoGrid.getSelectionModel();
						if (selModel.hasSelection()) {
							var records = selModel.getSelections();
							var ids="";
							for ( var i = 0; i < records.length; i++) {
								var id = records[i].get("id"); 
								var error = records[i].get("ticketStatus"); 
								var  note = records[i].get("note");
								if(error == "2" ||error=="3"){
									alertMsg("结果", "请检查单据状态！");
									return;
								}
								if(error == "1" && note != ""){
									alertMsg("结果", "请检查单据状态！");
									return;
								}
								if(id && id!='' && id!=null){
								// store.remove(records[i]);
								 ids += ""+id+",";
							     
								}								
								
							}
							if(ids != ""){//批量审核
								Ext.getCmp("message_grid_tBar@update").disable();
							 Ext.Ajax
									.request( {
										waitMsg : '请稍等.....',
										url : basePath + '/custom/shopGoods/reviewChannelGoodsTicket.spmvc',
										method : 'post',
										timeout:900000,
										params : {
											ids : ids,ticketStatus:1   //1为已经审核状态
										},
										success : function(response) {
											var respText = Ext.util.JSON.decode(response.responseText);
											Ext.getCmp("message_grid_tBar@update").enable();
										    if(respText.isok){ //成功
										    	alertMsg("结果", "审核完成");
										    	channelGoodsInfoStore.reload();
										    }else{
										    	alert("审核出错");
										    	channelGoodsInfoStore.reload();
										    }
													
										},
										failure : function(response) {
											var respText = Ext.util.JSON.decode(response.responseText);
											alertMsg("结果", "审核失败"); 
                                           
										}
									});
							}
	

						} else {
							alertMsg("错误", "请选择要批量执行的行!");
						}
					
						
					},
					deleteObject : function() { //删除整个panel里面被选择的行数据
						var selModel = channelGoodsInfoGrid.getSelectionModel();
						if (selModel.hasSelection()) {
							var records = selModel.getSelections();
							var ids="";
							for ( var i = 0; i < records.length; i++) {
								var id = records[i].get("id"); 
								var error = records[i].get("ticketStatus");
								if(error == "2" ||error=="3"){
									alertMsg("结果", "请检查单据状态！");
									return;
								}
								if(id && id!='' && id!=null){
									// store.remove(records[i]);
									ids += ""+id+",";
								}
							}
							if(ids != ""){//批量删除
							 Ext.Ajax
									.request( {
										waitMsg : '请稍等.....',
										url : basePath + '/custom/shopGoods/reviewChannelGoodsTicket.spmvc',
										method : 'post',
										params : {
											ids : ids,ticketStatus:2 //2为移除状态
										},
										success : function(response) {
											var respText = Ext.util.JSON.decode(response.responseText);
											if(respText.isok){ //成功
												alertMsg("结果", "移除成功！");
												channelGoodsInfoStore.reload();
											}else{
												alert("移除出错！");
												channelGoodsInfoStore.reload();
											}
													
										},
										failure : function(response, options) {
											var respText = Ext.util.JSON.decode(response.responseText); 
											 alert("移除失败！");
										}
									});
							}
	

						} else {
							alertMsg("错误", "请选择要批量删除的行!");
						}
					},
					show : function() {
						couponPanel = new Ext.Panel( {
							renderTo : 'shopGoodsInfo-grid',
							
							layout : 'column',
							items : [ channelGoodsInfoForm, channelGoodsInfoGrid ]
						}).show();
					},
					refresh:function(){
						channelGoodsInfoStore.reload();
					},
					search : function() {
					  var outStock = {};
						  outStock["isTiming"] = Ext.getCmp("channelGoodsInfoVo.isTiming").getValue();
						  outStock["shopCode"] = Ext.getCmp("channelGoodsInfoVo.shopCode").getValue();
						  outStock["ticketStatus"] = Ext.getCmp("channelGoodsInfoVo.ticketStatus").getValue();
						  outStock["ticketCode"] = Ext.getCmp("channelGoodsInfoVo.ticketCode").getValue(); //单据编号
						  channelGoodsInfoGrid.store.baseParams = outStock;
					//	  Ext.Msg.alert(Ext.getCmp("channelGoodsInfoVo.InfoCode").getValue());//1上下架单据类型
						  channelGoodsInfoGrid.store.load({params : {start : 0, limit : pageSize, ticketType: 2 }});
					},
					selectTemplate : function() {
						
						
						
						var selModel = channelGoodsInfoGrid.getSelectionModel();
						if (selModel.hasSelection()) {
							var records = selModel.getSelections();
							var ticketCode = records[0].get("ticketCode");
						//	var ticketType = records[0].get("ticketType");
							//FormEditWin.showAddDirWin("popWins",basePath+"/custom/channelGoodsInfo/selectTemplate.spmvc?ticketCode="+ticketCode+"&ticketType="+ticketType,"select_template_winID","查看/更新调整单",400,300);
							
							FormEditWin.showAddDirWin("popWins",basePath+"/custom/channelGoodsInfo/selectTemplate.spmvc?ticketCode="+ticketCode+"&ticketType=2","select_template_winID","查看/更新调整单",400,300);
							
						}
					}
				}
			}();
			
			function clickResetButton() {
				channelGoodsInfoForm.form.reset();
			}

			function runChannelGoodsInfoList() {
				ChannelGoodsInfoVo.init(); 
				ChannelGoodsInfoVo.show();
				setResize();
			}
			runChannelGoodsInfoList();
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
Ext.onReady(function() {
	var channelGoodsTitleStore;
	var channelGoodsTitleForm;
	var channelGoodsTitleGrid;
	var shopCodeStr = getGlobalValue("shopCode");
	var shopTitle = getGlobalValue('shopTitle');
	ChannelGoodsTitleVo= function() {
				var channelGoodsTitleProxy;
				var channelGoodsTitleReader;
				var checkBoxSelect;
				var channelGoodsTitleColumnGrid;
				var tbar;
				var pageSize=15;
				return {
					init : function() {
					//单据类型选择框
					var typeOptionCombo = new Ext.form.ComboBox( {
							id : 'channelGoodsTitleVo.isTiming',
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
							name : 'channelGoodsTitleVo.isTiming',
							editable : false,
							hiddenName : 'channelGoodsTitleVo.isTiming',
							triggerAction : 'all', 
							fieldLabel : '执行类型',
							width : 150
						});

					//单据状态选择框
					var payStatusOption = new Ext.form.ComboBox( {
							id : 'channelGoodsTitleVo.ticketStatus',
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
							name : 'channelGoodsTitleVo.ticketStatus',
							editable : false,
							hiddenName : 'channelGoodsTitleVo.ticketStatus',
							triggerAction : 'all', 
							fieldLabel : '单据状态',
							width : 150
						});
						
						channelGoodsTitleForm = new Ext.FormPanel(
								{
									id : "channelGoodsTitle_form",
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
													items : [ {
														xtype : 'textfield',
														fieldLabel : '经营店铺',
														id:'channelGoodsTitleVo.shopCode',
														readOnly : true,
														value : shopTitle,
														width : '200'
													} ,{
														id : 'channelGoodsTitleVo.ticketCode',
														xtype : 'textfield',
														labelAlign : 'left',
														fieldLabel : '调整单编号',
														name : 'channelGoodsTitleVo.ticketCode', 
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
								 
								checkBoxSelect = new Ext.grid.CheckboxSelectionModel();
						
						channelGoodsTitleColumnGrid = new Ext.grid.ColumnModel( [
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
											msg="宝贝详情维护";
										}
										if(e=='3'){
											msg="卖点管理";
										}
										if(e=='4'){
											msg="商品名称调整";
										}
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
										var msg= "<input type='button' value='导出调整单' onclick='ChannelGoodsTitleVo.exportRecord();' />"; 
										if(e=='3'){
											msg= "<input type='button' value='导出执行结果' onclick='ChannelGoodsTitleVo.exportRecord();' />"; 
										}
										return msg;
									}
								}
						]);

						//与列对应的dataIndex
						goodsTitlerecord = Ext.data.Record.create( [{
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
						channelGoodsTitleProxy = new Ext.data.HttpProxy( {
							url : basePath+"/custom/shopGoods/shopGoodsUpDownList.spmvc",
							method : "post"
						});
						//Reader 读json中数据
						channelGoodsTitleReader = new Ext.data.JsonReader( {
							root : 'root',
							totalProperty : 'totalProperty'
						}, goodsTitlerecord);
						
						channelGoodsTitleStore = new Ext.data.Store( {
							autoLoad : {
								params : {
									start : 0,
									limit : pageSize
								}
							},
							proxy :  channelGoodsTitleProxy,
							reader : channelGoodsTitleReader
						});
						
						// 如果不是用ajax的表单封装提交,就要做如下操作.
						//这里很关键，如果不加，翻页后搜索条件就变没了，这里的意思是每次数据载入前先把搜索表单值加上去，这样就做到了翻页保留搜索条件了   
						channelGoodsTitleStore.on('beforeload', function(){
							Ext.apply(this.baseParams, {ticketType: 4, shopCode:shopCodeStr});
						});

						//定义菜单栏
						tbar = [{
							id : 'message_grid_tBar@add',
							text : '生成调整单',
							tooltip : '生成调整单',
							iconCls : 'add',
							handler : function() {
								FormEditWin.showAddDirWin("popWins",basePath+"/custom/ticketInfo/createTicket.spmvc?ticketType=4","pop_message_winID","生成调整单",860,560);
							}
						},{
							id: 'message_grid_tBar@update',
							text : '批量执行',
							tooltip : '批量执行',
							iconCls : 'add',
							handler : function() {
								ChannelGoodsTitleVo.reviewObject();
							 
							}
						} , {
							id : 'message_grid_tBar@delete',
							text : '批量移除',
							tooltip : 'delete',
							iconCls : 'delete',
							handler:function(){
								ChannelGoodsTitleVo.deleteObject();
							}
						}];
						channelGoodsTitleGrid = new Ext.grid.GridPanel( {
//							autoWidth : true,
							height : 500,
							title : '店铺商品名称管理',
							store : channelGoodsTitleStore,
							id:'channelGoodsTitle_gridss_id',
							trackMouseOver : false,
							disableSelection : true,
//							autoExpandColumn: "formatAddTime", //自动伸展，占满剩余区域
							loadMask : true,
							frame : true,
							columnLines : true,
							tbar:  tbar,
							// grid columns  
							cm : channelGoodsTitleColumnGrid,
							sm:checkBoxSelect,
							// paging bar on the bottom  
							bbar : new IssPagingToolbar(channelGoodsTitleStore, pageSize),
							listeners : {
								'rowdblclick': function(thisgrid ,rowIndex ,e){
									var selectionModel = thisgrid.getSelectionModel();
									var record = selectionModel.getSelected();
									var id = record.data['id'];
									 FormEditWin.showAddDirWin("popWins",basePath+"/custom/ticketInfo/createTicket.spmvc?id="+id+"&ticketType=4","pop_message_winID","查看/更新调整单",860,560);
								}
							}
						});

					},
					checkOrUpdate:function(){//查看编辑按钮，需要把调整单号作为参数传递过去
						
						var selModel = channelGoodsTitleGrid.getSelectionModel();
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
						var selModel = channelGoodsTitleGrid.getSelectionModel();
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
						var selModel = channelGoodsTitleGrid.getSelectionModel();
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
												channelGoodsTitleStore.reload();
											}else{
												alert("未审核出错");
												channelGoodsTitleStore.reload();
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
						var selModel = channelGoodsTitleGrid.getSelectionModel();
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
										    	channelGoodsTitleStore.reload();
										    }else{
										    	alert("审核出错");
										    	channelGoodsTitleStore.reload();
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
						var selModel = channelGoodsTitleGrid.getSelectionModel();
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
												channelGoodsTitleStore.reload();
											}else{
												alert("移除出错！");
												channelGoodsTitleStore.reload();
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
						shopGoodsTitlePanel = new Ext.Panel( {
							renderTo : 'shopGoodsTitle-grid',
							layout : 'column',
							items : [ channelGoodsTitleForm, channelGoodsTitleGrid ]
						}).show();
					},
					refresh:function(){
						channelGoodsTitleStore.reload();
					},
					search : function() {
					  var outStock = {};
						  outStock["isTiming"] = Ext.getCmp("channelGoodsTitleVo.isTiming").getValue();
						  outStock["shopCode"] = Ext.getCmp("channelGoodsTitleVo.shopCode").getValue();
						  outStock["ticketStatus"] = Ext.getCmp("channelGoodsTitleVo.ticketStatus").getValue();
						  outStock["ticketCode"] = Ext.getCmp("channelGoodsTitleVo.ticketCode").getValue(); //单据编号
						  channelGoodsTitleGrid.store.baseParams = outStock;
					//	  Ext.Msg.alert(Ext.getCmp("channelGoodsTitleVo.TitleCode").getValue());//1上下架单据类型
						  channelGoodsTitleGrid.store.load({params : {start : 0, limit : pageSize, ticketType: 4 }});
					},
					selectTemplate : function() {
						var selModel = channelGoodsTitleGrid.getSelectionModel();
						if (selModel.hasSelection()) {
							var records = selModel.getSelections();
							var ticketCode = records[0].get("ticketCode");
							FormEditWin.showAddDirWin("popWins",basePath+"/custom/channelGoodsInfo/selectTemplate.spmvc?ticketCode="+ticketCode,"select_template_winID","查看/更新调整单",400,300);
						}
					}
				}
			}();
			
			function clickResetButton() {
				channelGoodsTitleForm.form.reset();
//				shopSelectOption.store.on('load', function(store, record, opts) {
// 					Ext.getCmp('channelGoodsTitleVo.shopCode').setValue(shopCodeStr); //获取登录人的店铺code
// 				});
//				shopSelectOption.store.load();
			}

			function runChannelGoodsTitleList() {
				ChannelGoodsTitleVo.init(); 
				ChannelGoodsTitleVo.show();
				setResize();
//				shopSelectOption.store.on('load', function(store, record, opts) {
// 					Ext.getCmp('channelGoodsTitleVo.shopCode').setValue(shopCodeStr); //获取登录人的店铺code
// 				});
//				shopSelectOption.store.load();
			}
			runChannelGoodsTitleList();
			function setResize() {
				var formHeight = channelGoodsTitleForm.getHeight();
				var clientHeight = document.body.clientHeight;
				var clientWidth = document.body.clientWidth;
				channelGoodsTitleGrid.setHeight(clientHeight-formHeight-50);
				channelGoodsTitleGrid.setWidth(clientWidth-259);
			}
			window.onresize=function(){
				setResize();
			}
		});
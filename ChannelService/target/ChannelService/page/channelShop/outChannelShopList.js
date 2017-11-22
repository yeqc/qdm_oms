function changeShopStatus(val) {

	if ("1" == val) {
		return "<font color='green'>已激活</font>";
	} else if ("0" == val) {
		return "<font color='red'>未激活</font>";
	}else if ("2" == val) {
		return "<font color='red'>已移除</font>";
	}

}
var outChannelShopGrid;
var outChannelShopForm;
Ext.onReady(function() {
			OutChannelShop = function() { 
				var outChannelShopStore;
				var couponproxy;
				var couponreader;
				var checkBoxSelect;
				var couponcolumnGrid;
				var tbar;
				var pageSize = 15;
				return {
					init : function() {

						var typeOptionCombo = new Ext.form.ComboBox({
							id : 'OutChannelShop.channelCode',
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
						});
						
						//店铺类型
						var shopTypeCombo = new Ext.form.ComboBox({
							id : 'OutChannelShop.shopType',
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
							emptyText : '请选择店铺类型',
							name : 'OutChannelShop.shopType',
							editable : false,
							triggerAction : 'all',
							fieldLabel : '店铺类型',
							width : 200
						});

						outChannelShopForm = new Ext.FormPanel({
							frame : true,
							bodyStyle : 'padding:5px 5px 0',
							//autoHeight : true,
//							autoScroll : true,
//							autoWidth : true,
							url : "",
							labelAlign : 'right',
							layout : 'form',
							items : [ {
								layout : 'column',
								items : [
									{
										layout : 'form',
										labelWidth : 80,
										columnWidth : 0.3,
										items : [ typeOptionCombo,shopTypeCombo]
									}, {
										layout : 'form',
										labelWidth : 80,
										columnWidth : 0.3,
										items : [ {
											id : 'shopTitle',
											xtype : 'textfield',
											//labelAlign : 'left',
											fieldLabel : '店铺名称',
											name : 'shopTitle', 
											width : '150'
									    },{
											
											id : 'shopCode',
											xtype : 'textfield',
											fieldLabel : '店铺编号',
											name : 'shopCode', 
											width : '150'
								    }]},{layout : 'form',
										labelWidth:80,
										columnWidth:0.33,
										items : [{
											id : 'parentShopCode',
											xtype : 'textfield',
											fieldLabel : '父店铺编号',
											name : 'parentShopCode',
											width : '150'
										}]
									}
								]
							} ],
							buttons : [ {
								text : '查询',
								columnWidth : 0.1,
								handler : this.search
							}, {
								text : '重置',
								columnWidth : 0.1,
								handler : this.reset
							} ]
						});

						checkBoxSelect = new Ext.grid.CheckboxSelectionModel();

						couponcolumnGrid = new Ext.grid.ColumnModel([
								checkBoxSelect, {
									id : 'id',
									header : "ID",
									align : "center",
									width : 80,
									hidden : true,
									dataIndex : 'id'
								}, {
									id : 'shopCode',
									align : "center",
									sortable : true,
									header : "店铺编号",
									width : 140,
									dataIndex : 'shopCode'
								}, {
									id : 'parentShopCode',
									align : "center",
									sortable : true,
									header : "父店铺编号",
									width : 140,
									dataIndex : 'parentShopCode'
								}, {
									id : 'shopTitle',
									header : "店铺名称",
									align : "center",
									width : 200,
									dataIndex : 'shopTitle'
								}, {
									id : 'channelTitle',
									header : "所属渠道",
									align : "center",
									width : 120,
									dataIndex : 'channelTitle'
								},
								{
									id : 'shopType',
									align : "center",
									sortable : true,
									header : "店铺类型",
									width : 80,
									renderer : function(e){
										var msg ="";
										if(e=="0"){
											msg="自营";											
										}
										if(e=="1"){
											msg="加盟";
										}
										return msg;
									},
									dataIndex : 'shopType'
								}, {
									id : 'shopStatus',
									align : "center",
									sortable : true,
									header : "店铺状态",
									width : 120,
									renderer : changeShopStatus,
									dataIndex : 'shopStatus'
								}, {
									id : 'backup',
									header : "备注信息",
									align : "center",
									align : "center",
									width : 200,
									dataIndex : 'backup'
								} ,{
									id : 'expiresTime',
									header : "token失效日期",
									align : "center",
									align : "center",
									width : 200,
									renderer : function(e){
										
										 if(null == e || '' == e){
											 return '';
										 }	
							
									 	 var expiresDate = new Date(e);  //到期时间									
									 	 var currDate=new Date(); //当前时间								
										 var days = dateDiff(expiresDate,currDate);
									
										 if( days <= 3) {
											 return  '<font style="color:red;">'+ e +'</font>';
										 } 
										 
										 return '<font >'+ e +'</font>';
					
									},
									dataIndex : 'expiresTime'
								}

						]);

						// 与列对应的dataIndex
						couponrecord = Ext.data.Record.create([ {
							name : 'id'
						}, {
							name : 'shopCode'
						},{
							name : 'parentShopCode'
						}, {
							name : 'shopTitle'
						}, {
							name : 'channelTitle'
						}, {
							name : 'shopStatus'
						}, {
							name : 'erpShopCode'
						}, {
							name : 'backup'
						}, {
							name : 'shopChannel'
						}, {
							name : 'marketTitle'
						}, {
							name : 'shopType'
						}, {
							name : 'shopAddress'
						}, {
							name : 'shopTel'
						},{
							name:'expiresTime'
						},{
							name:'isSyn'
						}
						// {name : 'CREATE_USER'},
						// {name : 'MOD_TIME'},
						// {name : 'MOD_USER'},
						// {name : 'COUPON_TYPE'},
						// {name : 'REMARK'}
						]);

						couponproxy = new Ext.data.HttpProxy(
								{
									url : basePath+ "/custom/channelShop/getExternalChannelShopList.spmvc",
									method : "post"
								});
						// Reader 读json中数据
						couponreader = new Ext.data.JsonReader({
							root : 'root',
							totalProperty : 'totalProperty'
						}, couponrecord);

						outChannelShopStore = new Ext.data.Store({
							autoLoad : {
								params : {
									start : 0,
									limit : pageSize
								}
							},
							proxy : couponproxy,
							reader : couponreader
						});
						
						 // 如果不是用ajax的表单封装提交,就要做如下操作.
				        //这里很关键，如果不加，翻页后搜索条件就变没了，这里的意思是每次数据载入前先把搜索表单值加上去，这样就做到了翻页保留搜索条件了   
						outChannelShopStore.on('beforeload', function(){
				         //   var keyword = getKeyword();
				            Ext.apply(this.baseParams, {shopChannel: 1});   
				        });	
						

						// 定义菜单栏
						tbar = [
								{
									id : 'message_grid_tBar@add',
									text : '添加店铺',
									tooltip : '添加店铺',
									iconCls : 'add',
									handler : function() {
										FormEditWin
												.showAddDirWin(
														"popWins",
														basePath
																+ "/custom/channelShop/addOutChannelShop.spmvc",
														"pop_message_winID",
														"新增店铺", 620, 430);
									}
								},
								{
									text : '更新/查看',
									tooltip : '更新/查看',
									iconCls : 'search',
									handler : function() {

										var selModel = outChannelShopGrid.getSelectionModel();

										if (selModel.hasSelection()) {
											var records = selModel.getSelections();
											var ids = "";
											
											if(records.length > 1) {
												alertMsg("错误", "只能选择一行!");
												return;
											} 
											
											var id = records[0].get("id");
										
											if (id != "") {
												
												FormEditWin.showAddDirWin(
														"popWins",
														basePath
																+ "/custom/channelShop/updateChannelShop.spmvc?id="+id+"&type=init",
														"pop_message_winID",
														"更新/查看店铺", 620, 480);
								
											} // if (id != "") end
											
										} else {
											alertMsg("错误", "请选择一行记录!");
										}																				
									}
								}, {
									id : 'message_grid_tBar@delete',
									text : '移除',
									tooltip : 'delete',
									iconCls : 'delete',
									handler : function() {
						
										var selModel = outChannelShopGrid.getSelectionModel();

										if (selModel.hasSelection()) {
											var records = selModel.getSelections();
											var ids = "";
										    var shopTitles="";
										    var shopCodes="";
											for ( var i = 0; i < records.length; i++) {
												var id = records[i].get("id");
												var error = records[i].get("shopStatus");
												var shopCode=records[i].get("shopCode");
												if(error != "1"){
													alertMsg("结果", "只能移除已激活状态记录！");
													return;
												}
												var shopTitle = records[i].get("shopTitle");
												if (id && id != ''&& id != null) {
													ids += "" + id + ",";
												}
												if(shopTitle != "" && shopTitle!=null){
													shopTitles+=""+shopTitle+",";
												}
												if (shopCode != ''&& shopCode != null) {
													shopCodes += "" + shopCode + ",";
												}
										    }
											
											if (id != "") {
												Ext.Msg.confirm("确认","确定要删除店铺:"+ shopTitles,
														function(btn) {
															if (btn == "yes"){
																Ext.Ajax.request({
																	waitMsg : '请稍等.....',
																	url : basePath+ '/custom/channelShop/activeChannelShop.spmvc',
																	method : 'post',
																	params : {ids : ids,shopCodes:shopCodes,shopStatus:0},
																	success : function(response) {
																		var respText = Ext.util.JSON.decode(response.responseText);
																		if (respText.isok) {
																			outChannelShopStore.reload();
																			alertMsg("验证","移除成功！");
																		} else {
																			outChannelShopStore.reload();
																			alert(respText.msg);
																		}

																	},
																	failure : function(response,options) {
																		alertMsg("验证","失败！");
																	//	alert("失败");
																	}
																});
																
																
															}});
											}
						
										} 	 else {
											alertMsg("错误", "请选择需要移除的行!");
										}														
									
									}
								}, {

									text : '激活',
									tooltip : '',
									iconCls : 'refresh',
									handler : function() {
										// outChannelShopStore.reload();
									
										var selModel = outChannelShopGrid.getSelectionModel();
                                        if (selModel.hasSelection()) {
                                        	var records = selModel.getSelections();
											var ids = "";
											 var shopCodes="";
											for ( var i = 0; i < records.length; i++) {
												var error = records[i].get("shopStatus"); 
												var shopCode=records[i].get("shopCode");
												if(error != "0"){
													alertMsg("结果", "只能选择未激活状态记录激活！");
													return;
												}
												var id = records[i].get("id");
												if (id && id != ''&& id != null) {
													ids += "" + id + ",";
												}
												if (shopCode != ''&& shopCode != null) {
													shopCodes += "" + shopCode + ",";
												}
										    }
																	
											if (id != "") {
												
												Ext.Ajax.request({
													waitMsg : '请稍等.....',
													url : basePath+ '/custom/channelShop/activeChannelShop.spmvc',
													method : 'post',
													params : {ids : ids ,shopCodes:shopCodes,shopStatus:1},
													success : function(response, action) {
														var respText = Ext.util.JSON.decode(response.responseText);
														if (respText.isok) {
															outChannelShopStore.reload();
															alertMsg("验证","激活成功！");
															//alert("激活成功");
														} else {
															
															outChannelShopStore.reload();
															alertMsg("验证","激活失败！");
															//alert("激活失败");
														}
													},
													failure : function(response,options) {
														var respText = Ext.util.JSON.decode(response.responseText);
														alert(respText.msg);
													}
												});
											}

										} else {
                                            alertMsg("错误", "请选择一行记录!");
										}
					
									}
								},
								{
									text : '批量导入',
									iconCls : 'refresh',
									handler : uploadDataWinOpen
								},
						'<a style="color:red" href = "' + basePath
								+ '/page/excelModel/shop_list.xls'
								+ '">模板下载</a>' ];

						outChannelShopGrid = new Ext.grid.GridPanel(
								{
									title : '外部渠道店铺管理',
									store : outChannelShopStore,
									id : 'outChannelShop_grid_id',
									trackMouseOver : false,
									disableSelection : true,
									loadMask : true,
									frame : true,
									//autoExpandColumn: "shopTitle", //自动伸展，占满剩余区域
									columnLines : true,
									tbar : tbar,
									// grid columns
									cm : couponcolumnGrid,
									sm : checkBoxSelect,
									// paging bar on the bottom
									bbar : new IssPagingToolbar(outChannelShopStore,
											pageSize),
									listeners : {
										'rowdblclick' : function(thisgrid,
												rowIndex, e) {
											var selectionModel = thisgrid
													.getSelectionModel();
											var record = selectionModel
													.getSelected();
											var id = record.data['id'];

											FormEditWin.showAddDirWin(
													"popWins",
													basePath
															+ "/custom/channelShop/updateChannelShop.spmvc?id="+id+"&type=init",
													"pop_message_winID",
													"更新/查看店铺",620, 480);
										}
									}
								});
					},
					
					refresh:function(){
						    outChannelShopStore.reload();
					},
					show : function() {

						couponPanel = new Ext.Panel({
							renderTo : 'coupon-grid',
							layout : 'column',
							items : [ outChannelShopForm, outChannelShopGrid ]
						}).show();
					},
					reset:function(){
						outChannelShopForm.form.reset();
					},
					
					search : function() {
						var outStock = {};
						outStock["shopTitle"] = Ext.getCmp("shopTitle").getValue();
						outStock["shopCode"] = Ext.getCmp("shopCode").getValue();
						outStock["parentShopCode"] = Ext.getCmp("parentShopCode").getValue();
						outStock["channelCode"] = Ext.getCmp("OutChannelShop.channelCode").getValue();
						outStock["shopType"] = Ext.getCmp("OutChannelShop.shopType").getValue();
						
						outChannelShopGrid.store.baseParams = outStock;
						outChannelShopGrid.store.load({
							params : {
								start : 0,
								limit : pageSize,
								shopChannel: 1  //渠道店铺
							}
						});
					}
				}
			}();

			function runCouponList() {
				OutChannelShop.init();
				OutChannelShop.show();
				setResize();
			}
			runCouponList();
	
			function setResize() {
				var formHeight = outChannelShopForm.getHeight();
				var clientHeight = document.body.clientHeight;
				var clientWidth = document.body.clientWidth;
				outChannelShopGrid.setHeight(clientHeight-formHeight-50);
				outChannelShopGrid.setWidth(clientWidth-259);
			}
			window.onresize=function(){
				setResize();
			}
			
			function uploadDataWinOpen() {
				// 参数格式："key1:value1;key2:value2"
				var params = "&params=promType:1";
				var uploadUrl = basePath + 'custom/channelShop/upload.spmvc' + params;
				FormEditWin.showAddDirWin("shop_upload_div", basePath
						+ "/fileUpload.html?type=1&fileTypes=*.xls&uploadUrl=" + uploadUrl,
						"picADWinID", "店铺数据导入", 580, 300);
			}
			
});

/**
 * 计算日期差 
 **/
function  dateDiff(sDate1,  sDate2){    //sDate1和sDate2是2006-12-18格式  
   /* var aDate; 
    var oDate1;
    var oDate2 ;
    var iDays ;
    aDate  =  sDate1.split("-");  
    oDate1  =  new  Date(aDate[1]  +  '-'  +  aDate[2]  +  '-'  +  aDate[0]);    //转换为12-18-2006格式  
    aDate  =  sDate2.split("-");
    oDate2  =  new  Date(aDate[1]  +  '-'  +  aDate[2]  +  '-'  +  aDate[0]);  
    iDays  =  parseInt(Math.abs(oDate1  -  oDate2)  /  1000  /  60  /  60  /24);   //把相差的毫秒数转换为天数  
*/  //  return  iDays;
//	var aDate; 
 //   var oDate1;
  //  var oDate2 ;
   // var iDays ;
    
	// aDate  =  sDate1.split("-");  
	//    oDate1  =  new  Date(aDate[0]  +  '-'  +  aDate[1]  +  '-'  +  aDate[2]);
	
	 //   aDate  =  sDate2.split("-");
	//    oDate2  =  new  Date(aDate[0]  +  '-'  +  aDate[1]  +  '-'  +  aDate[2]);
	    
	  var date3=sDate1.getTime()-sDate2.getTime();  //时间差的毫秒数

	  //计算出相差天数
	  iDays=Math.floor(date3/(24*3600*1000));
	  return  iDays;
}

function getCurrDate()
{
	var now=new Date();
	y=now.getFullYear();
	m=now.getMonth()+1;
	d=now.getDay();
	m=m<10?"0"+m:m;
	d=d<10?"0"+d:d;
	return y+"-"+m+"-"+d;
}

function getDate(date)
{
	var expiresDate = new Date(date);
	y=expiresDate.getFullYear();
	m=expiresDate.getMonth()+1;
	d=expiresDate.getDay();
	m=m<10?"0"+m:m;
	d=d<10?"0"+d:d;
	return y+"-"+m+"-"+d;
}


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
function changeChannelStatus(val) {
	if ("0" == val) {
		return "国家";
	} else if ("1" == val) {
		return "省";
	} else if ("2" == val) {
		return "市";
	} else if ("3" == val) {
		return "区";
	}
}
var channelAreaForm;
Ext
		.onReady(function() {
			AddChannelArea = function() {		
				var addChannelAreaGrid;
				var addChannelAreaStore;
				var addChannelAreaProxy;
				var addChannelAreaReader;
				var checkBoxSelect;
				var addChannelAreaClumnGrid;
				var tbar;
				var pageSize = 15;
				return {
					init : function() {
						
						//映射状态
						var areaStatusSelect = new Ext.form.ComboBox({
							id : 'areaStatus',
							store : new Ext.data.SimpleStore({
								data : [ [ '0', '未映射' ], [ '1', '已映射' ],[ '2', '全部' ] ],
								fields : [ 'text', 'filed' ]
							}),
							xtype : 'combo',
							valueField : 'text',
							displayField : 'filed',
							mode : 'local',
							forceSelection : true,
							blankText : '请选择映射状态',
							emptyText : '映射状态',
							name : 'areaStatus',
						//	editable : false,
							triggerAction : 'all',
							fieldLabel : '映射状态',
							width : 150
						});

						channelAreaForm = new Ext.FormPanel({
							frame : true,
						//	bodyStyle : 'padding:5px 5px 0',
							autoWidth:true,
							autoScroll : true,
							url : "",
							labelAlign : 'right',
							layout : 'form',
							items : [],
						});

						checkBoxSelect = new Ext.grid.CheckboxSelectionModel();

						addChannelAreaClumnGrid = new Ext.grid.ColumnModel([
								checkBoxSelect, {
									id : 'id',
									header : "ID",
									align : "center",
									align : "center",
									width : 80,
									hidden : true,
									dataIndex : 'id'
								}, {
									id : 'id',
									align : "center",
									sortable : true,
									header : "序号",
									width : 80,
									dataIndex : 'id'
								},{
									id : 'pid',
									align : "center",
									sortable : true,
									header : "地区父ID",
									width : 80,
									dataIndex : 'pid'
								}, {
									id : 'areaId',
									header : "渠道地区编码",
									align : "center",
									align : "center",
									width : 120,
									dataIndex : 'areaId'
								},

								{
									id : 'areaType',
									header : "地区类型",
									align : "center",
									align : "center",
									width : 120,
									renderer : changeChannelStatus,
									dataIndex : 'areaType'
								},
								{
									id : 'areaName',
									header : "渠道地区名称",
									align : "center",
									align : "center",
									width : 200,
									dataIndex : 'areaName'
								},{
									id : 'osRegionId',
									header : "OS地区编码",
									align : "center",
									align : "center",
									width : 200,
									dataIndex : 'osRegionId'
								},{
									id : 'osRegionName',
									header : "OS地区名称",
									align : "center",
									align : "center",
									width : 200,
									dataIndex : 'osRegionName'
								}

						]);

						// 与列对应的dataIndex
						couponrecord = Ext.data.Record.create([ {
							name : 'id'
						}, {
							name : 'areaId'
						}, {
							name : 'areaName'
						},{
							name : 'areaType'
						}, {
							name : 'osRegionId'
						}, {
							name : 'osRegionName'
						}
						]);

						addChannelAreaProxy = new Ext.data.HttpProxy(
								{
									url : basePath+ "custom/channelArea/getchannelAreaList.spmvc",
									method : "post"
								});
						// Reader 读json中数据
						addChannelAreaReader = new Ext.data.JsonReader({
							root : 'root',
							totalProperty : 'totalProperty'
						}, couponrecord);

						addChannelAreaStore = new Ext.data.Store({
							autoLoad : {
								params : {
									start : 0,
									limit : pageSize
								}
							},
							proxy : addChannelAreaProxy,
							reader : addChannelAreaReader
						});
						
						 // 如果不是用ajax的表单封装提交,就要做如下操作.
				        //这里很关键，如果不加，翻页后搜索条件就变没了，这里的意思是每次数据载入前先把搜索表单值加上去，这样就做到了翻页保留搜索条件了   
						addChannelAreaStore.on('beforeload', function(){
				         //   var keyword = getKeyword();
				       //     Ext.apply(this.baseParams, {shopChannel: 1});   
				        });	
						// 定义菜单栏
						tbar = [
								{
									id : 'message_grid_tBar@add',
									text : '导入地区映射',
									tooltip : '导入地区映射',
									iconCls : 'add',
									handler :upload
									}
									];

						addChannelAreaGrid = new Ext.grid.GridPanel(
								{
									width : 1000,
									height : 500,
									title : '渠道地区映射管理',
									store : addChannelAreaStore,
									id : 'addchannelAreaForm_grid_id',
									trackMouseOver : false,
									disableSelection : true,
									loadMask : true,
									frame : true,
									columnLines : true,
									tbar : tbar,
									cm : addChannelAreaClumnGrid,
									sm : checkBoxSelect,
									bbar : new IssPagingToolbar(addChannelAreaStore,
											pageSize),
									listeners : {}
								});

					},
					refresh:function(){
						    addChannelAreaStore.reload();					
					},
                 doAfter : function(data){//上传后的数据返回
						var json = Ext.util.JSON.decode(data);
                         FormEditWin.close();
                         if(json.totalProperty > 0){
                        	 AddChannelArea.refresh();
                             parent.ChannelArea.refresh();
                         }else{
                        	 alert("导入数据为空！");
                         }
					},
					show : function() {

						couponPanel = new Ext.Panel({
							renderTo : 'addChannelArea-grid',
								autoHeight : true,
							autoWidth : true,
							layout : 'fit',
							items : [ addChannelAreaGrid ]
						}).show();
					},
					reset:function(){
				//		addChannelAreaGrid.form.reset();
					},
					
					search : function() {}

				}
			}();
			function upload (){ //导入excel以后就与调整单绑定传入数据库保存
			//	 var params = "&params=ticketCode:"+ticketCode;
				FormEditWin.showAddDirWin(null,basePath+"/fileUpload.html?uploadUrl="+basePath+"custom/channelArea/inportChannelArea.spmvc"+"&type=2","picADWinID","上传",580,300);
			}
			function runCouponList() {
				AddChannelArea.init();
				AddChannelArea.show();
			}
			runCouponList();
		});
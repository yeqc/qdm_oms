<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
%>

<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>商品安全阀值调整单</title>
		<style>
    		.x-form-display-field-left{
    			padding-top: 2px;
    			text-align:left;
			}
		</style>
		<script type="text/javascript" src="<%=path%>/banggo/js/common/ext.js"></script>
		<script type="text/javascript" src="<%=path%>/banggo/js/common/common.js"></script>
		<script>
			// 查询form
			var selectform;
			// 调整单
			var storeWindow, fileWindow;
			var path = "<%=path%>";
			var isExpand = true;
			// 追加记录数据
			var tempLocalData = []; 
			
			Ext.onReady(function() {
				Ext.QuickTips.init();
				// 查询区域的高度
				var maxSelectHeight = 90;
				var minSelectHeight = 25;
				var toolbarHeight = 25;
				
				setDefault();
				
				// 每页记录数
				var limit = 20;
				
				// 记录是否查询过
				var check = false;
				
				// 品类下拉框对象
    			var typeComboBox = getBrandCombox();
				
				// 初始请求
				selectform = new Ext.form.FormPanel({
					title:"查询条件",
					frame:true,
					renderTo:"condition",
					collapsible:true,
					labelWidth:60,
                	labelAlign:'right',
                	keys:{
						key:Ext.EventObject.ENTER,
						fn:function(btn,e){
							getQuery();
						}
					},
					items:[
						{xtype: 'compositefield',
							items: [
								{xtype:"textfield",name:"num",id:"num",width:150,fieldLabel:"商品码",emptyText:'6/11码'},
								{xtype:'displayfield',value:'商品名称:', width:60},
								{xtype:"textfield",name:"name",id:"name",width:150},
								{xtype:'displayfield',value:'品牌:', width:60},
	                			typeComboBox
							]
						}
					],
					buttons:[
						{text:"检索",handler:function(){
					   		getQuery();
						}},
						{text:"重置",handler:function(){
							tempLocalData = [];
	                    	selectform.getForm().reset();
	                  	}},
						{text:"导出",handler:function(){
							downLoad();
	                  	}}
					],
					buttonAlign:"right"
				});
				
				// 展开
				selectform.on("expand", function(){
					isExpand = true;
					setColl();
				});
				
				// 收缩
				selectform.on("collapse", function(){
					isExpand = false;
					setColl();
				});
					
				// 列表数据
				var store = new Ext.data.JsonStore({
					storeId:"dataStore",
					fields:[
						{name:'id'},
						{name:'sku'},
						{name:'kValue'},
						{name:'ukValue'},
						{name:'name'},
						{name:'color'},
						{name:'size'},
						{name:'channelCode'},
						{name:'channelName'}
			      	],
			      	url:path + '/scstock/getSctockGoodsList.htm',
					root:'list',
					totalProperty:'total'
				});
				
				store.on("beforeload", function(obj, opt) {
		            var body = Ext.getBody();
	            	body.mask('Loading', 'x-mask-loading');
	            });
	            
	            store.on("load", function(obj,record) {
	            	var body = Ext.getBody();
	            	body.unmask();
	            });
				
	            var sm = new Ext.grid.CheckboxSelectionModel({  
    				checkOnly: true,  
    				singleSelect: false
				}); 
	            
				var columns = [
					sm,
					new Ext.grid.RowNumberer({
						header:"序号",
						width:35,
		    	  		renderer:function(value,metadata,record,rowIndex){
		    		   		return getRecordStart() + 1 + rowIndex;
		    			}
		    		}),
		    		{header:'商品11位码',dataIndex:'sku',width:120,align:"center",sortable:true,renderer:function(value){
						return getLeftFormate(value);
					}},
					{header:'商品名称',dataIndex:'name',width:300,align:"center",sortable:true,renderer:function(value, metadata, record){
						return getLeftFormate(value);
					}},	
					{header:'颜色',dataIndex:'color',align:"center",sortable:true,renderer:function(value, metadata, record){
						return getLeftFormate(value);
					}},	
					{header:'尺码',dataIndex:'size',align:"center",sortable:true,renderer:function(value,metadata,record){
						return getLeftFormate(value);
					}},	
					{header:'当前阀值',dataIndex:'kValue',align:"center",sortable:true,renderer:function(value,metadata,record){
						return getRightFormate(value);
					}},
					{header:'出售渠道',dataIndex:'channelName',align:"center",sortable:true,renderer:function(value,metadata,record){
						return getRightFormate(value);
					}}
				];
				
				// 列表尾部工具条
				var foot_tb = new Ext.Toolbar({
					items:[
							{xtype : "tbfill"},
							{id:"import", text:"文件导入", iconCls:"add", handler:function(){upload();}},
							"-",
							{id:"add", text:"追加记录", iconCls:"add", handler:function(){addRecord();}},
							"-",
							{id:"update", text:"追加记录调整", iconCls:"update", handler:function(){synch();}},
							"-",
							{id:"updateBatch", text:"列表记录调整", iconCls:"update", handler:function(){batchSynch();}}
						],
					
					style : 'padding-right:20px'
				});
				// 列表
				var model = getGridPanelModel("grid0", "查询列表", columns, store, limit, Ext.get("grid"),sm,foot_tb);
				var grid = createGridPanelByModel(model);
   		    	setDefaultPage(0, limit, store);
   		    	// 分页工具条
				var toolBar = createPagingToolbar(store, limit);
				toolBar.setHeight(toolbarHeight);
				toolBar.render("toolBar");
				
				// 检索
				function getQuery() {
					// 渠道和商品码必须输入
					var sku = getById('num');
					if (sku == Ext.getCmp('num').emptyText) {
						sku = "";
					}
					// 商品名
					var name = getById('name');
					// 品牌
					var brandCode = typeComboBox.getValue();
					
					check = true;
			    	var store = Ext.StoreMgr.lookup("dataStore");
			    	setDefaultPage(0, limit, store);
			    	
			    	store.setBaseParam("sku", sku);
			    	store.setBaseParam("saleStock", 1);
			    	store.setBaseParam("os", 1);
			    	store.setBaseParam("brandCode", brandCode);
			    	store.setBaseParam("title", name);
			    	store.load();
				}
				
				// 导出
				function downLoad() {
					
					if (check) {
						var params = store.baseParams;
		         		
		         		var postForm = document.createElement("form");//表单对象   
	            		postForm.method="post" ;   
	            		postForm.action = path + "/scstock/downLoadCommonList.htm";
	            		
		         		for (prop in params) {
		         			if (prop != 'os') {
		         				var input = document.createElement("input");  //email input 
		            			input.setAttribute("name", prop);   
		            			input.setAttribute("value", params[prop]);
		            			postForm.appendChild(input);  
		         			}
						}
		         		
		         		var name = document.createElement("input");  //email input 
		         		name.setAttribute("name", "name");   
		         		name.setAttribute("value", "productList");
            			postForm.appendChild(name); 
            			
            			var type = document.createElement("input");  //email input 
            			type.setAttribute("name", "type");   
            			type.setAttribute("value", "4");
            			postForm.appendChild(type); 
            			
	            		document.body.appendChild(postForm);   
	            		postForm.submit() ;   
	            		document.body.removeChild(postForm);
					} else {
            			createAlert("信息提示", "请选择查询条件并点击检索,再导出数据");
            		}
				}
				
				// 上传
				function upload() {
					var url = "../common/productSafeUpload.jsp?state=2";
					fileWindow = showWindow('productSafeUpload','文件上传',url,400,150);
					fileWindow.show();
				}
				
				// 追加记录
				function addRecord() {
					var rows = grid.getSelectionModel().getSelections();
		    	  	var leng = rows.length;
		    	  	
		    	  	if (leng == 0) {
		    	  		createAlert("信息提示", "请选择记录再进行追加！");
		    	  	} else {
		    	  		var count = tempLocalData.length;
		    	  		for(var i = 0 ; i < leng; i++){
		    	  			var record = rows[i];
							var bl = false;
		    	  			for (var j = 0; j < count; j++) {
		    	  				var tempRecord = tempLocalData[j];
		    	  				if (tempRecord.sku == record.get('sku')) {
		    	  					bl = true;
		    	  					break;
		    	  				}
		    	  			}
		    	  			
		    	  			if (!bl) {
		    	  				var data = {};
		    	  				data.id = getFiledValue(record, 'id');
		    	  				data.sku = getFiledValue(record, 'sku');
		    	  				data.realStock = getFiledValue(record, 'realStock');
		    	  				data.saleStock = getFiledValue(record, 'saleStock');
		    	  				data.virStock = getFiledValue(record, 'virStock');
		    	  				data.virFrozenStock = getFiledValue(record, 'virFrozenStock');
		    	  				data.lastSyncDate = getFiledValue(record, 'lastSyncDate');
		    	  				data.kValue = getFiledValue(record, 'kValue');
		    	  				data.stock = getFiledValue(record, 'stock');
		    	  				data.channelCode = getFiledValue(record, 'channelCode');
		    	  				data.channelName = getFiledValue(record, 'channelName');
		    	  				data.changeStock = data.kValue;
		    	  				
		    	  				tempLocalData.push(data);
		    	  			}
			    	 	}
		    	  		createAlert("信息提示", "追加记录成功！");
		    	  	}
				}
				
				function getFiledValue(record, name) {
					return record.get(name);
				}
				
				// 追加记录同步库存
				function synch() {
					var length = tempLocalData.length;
					if (length == 0) {
						createAlert("信息提示", "没有追加记录！");
					} else {
						var msg = "确定将追加的所有记录批量调整吗？";
						
						Ext.Msg.confirm("警告",msg,function(btn){
	                    	if(btn == "yes"){
								var url = "productSafeReport.jsp?flag=1";
								showStockWindow(url);
	                    	}
						});
					}
				}
				
				// 批量同步库存
				function batchSynch() {
					if (!check) {
						createAlert("信息提示","请点击检索后再进行批量调整操作!");
						return false;
					}
					var msg = "确定将列表中的所有记录批量调整吗？";
					
					Ext.Msg.confirm("警告",msg,function(btn){
                    	if(btn == "yes"){
							var url = "productSafeReport.jsp?flag=2";
							showStockWindow(url);
                    	}
					});
				}
				
				function setDefault() {
					var clientHeight = document.body.clientHeight;
					
					Ext.get("condition").setHeight(maxSelectHeight);
					Ext.get("grid").setHeight(clientHeight - maxSelectHeight-toolbarHeight);
	           	}
	           	
	           	function setColl() {
					setResize();
				}
	           	
	           	function setResize() {
					var clientHeight = document.body.clientHeight;
					var clientWidth = document.body.clientWidth;
					var height = isExpand == true ? maxSelectHeight : minSelectHeight;
						
	        		Ext.get("condition").setHeight(height);
	        		Ext.get("grid").setHeight(clientHeight-height-toolbarHeight);
					grid.setHeight(clientHeight-height-toolbarHeight);
					
					Ext.get("condition").setWidth(clientWidth);
	        		Ext.get("grid").setWidth(clientWidth);
					grid.setWidth(clientWidth);
				}
				
				window.onresize=function(){
					setResize();
	        	}
			});
			
			function showFileWindow(data) {
				tempLocalData = data;
				var url = "productSafeReport.jsp?flag=3";
				showStockWindow(url);
			}
			
			function showStockWindow(url) {
				storeWindow = showWindow('productSafeReport','商品安全阀值调整', url, 800, 500, 50, 20);
				storeWindow.show();
			}
		</script>
	</head>
	
	<body>
    	<div id="condition" style="width:100%;"></div>
    	<div class="clear"></div>
    	<div id="grid" style="width:100%"></div>
    	<div class="clear"></div>
    	<div id="toolBar" style="width:100%;height:25px;"></div>
  	</body>
</html>

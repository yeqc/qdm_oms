<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
%>

<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>实际库存查询管理</title>
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
			var path = "<%=path%>";
			var isExpand = true;
			// 存放追加记录数
			var tempLocalData=[];
			// 文件页面
			var fileWindow;
			// 记录展现
			var storeWindow;
			// 文件记录
			var fileData;
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
				
				// 品类
				var typeData = [
					['请选择','']
				];
				var typeStore = new Ext.data.ArrayStore({
        			fields: ['name', 'id'],
        			data : typeData
    			});
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
						{name:'company'},
						{name:'name'},
						{name:'color'},
						{name:'size'},
						{name:'realStock'},
						//{name:'saleStock'},
						{name:'shareStock'},
						{name:'virStock'},
						{name:'virFrozenStock'},
						{name:'syncFlag'},
						{name:'status'},
						{name:'lastSyncDate'},
						{name:'kValue'},
						{name:'stock'}
			      	],
					url:path + '/scstock/getSctockList.htm',
					root:'list',
					//remoteSort:true,
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
					{header:'商品企业码（13位）',dataIndex:'company',width:150,align:"center",sortable:true,renderer:function(value,metadata,record){
						return getLeftFormate(value);
					}},
					{header:'商品名称',dataIndex:'name',width:300,align:"center",sortable:true,width:200,renderer:function(value, metadata, record){
						return getLeftFormate(value);
					}},	
					{header:'颜色',dataIndex:'color',align:"center",sortable:true,renderer:function(value, metadata, record){
						return getLeftFormate(value);
					}},	
					{header:'尺码',dataIndex:'size',align:"center",sortable:true,renderer:function(value,metadata,record){
						return getLeftFormate(value);
					}},	
					{header:'可共享库存',dataIndex:'shareStock',align:"center",sortable:true,renderer:function(value, metadata, record){
						return getRightFormate(value);
					}},
					{header:'真实库存',dataIndex:'realStock',align:"center",sortable:true,renderer:function(value,metadata,record){
						return getRightFormate(value);
					}},	
					{header:'各渠道强占实际库存合计',dataIndex:'stock',width:150,align:"center",sortable:true,renderer:function(value,metadata,record){
						return getRightFormate(value);
					}},	
					{header:'虚拟库存',dataIndex:'virStock',align:"center",sortable:true,renderer:function(value,metadata,record){
						return getRightFormate(value);
					}},	
					{header:'虚拟冻结库存',dataIndex:'virFrozenStock',align:"center",sortable:true,renderer:function(value,metadata,record){
						return getRightFormate(value);
					}},	
					/*{header:'ERP未消减的库存',dataIndex:'saleStock',width:150,align:"center",sortable:true,renderer:function(value,metadata,record){
						return getRightFormate(value);
					}},*/
					{header:'安全阀值',dataIndex:'kValue',align:"center",sortable:true,renderer:function(value,metadata,record){
						return getRightFormate(value);
					}},
					{header:'上次同步时间',dataIndex:'lastSyncDate',width:150,align:"center",sortable:true,renderer:function(value, metadata, record){
						if(value){
							return getLeftFormate(formatDateformate(value, "Y-m-d H:i:s"));
						}
					}}
				];
				
				// 列表尾部工具条
				var foot_tb = new Ext.Toolbar({
					items:[
						{xtype : "tbfill"}
						,"-"
						,{id:"file", text:"文件上传", iconCls:"add", handler:function(){upload();}}
						,"-"
						,{id:"check", text:"追加记录", iconCls:"add", handler:function(){addRecord();}}
						,"-"
						,{id:"checkSynch", text:"追加记录同步库存", iconCls:"add", handler:function(){synch();}}
						,"-"
						,{id:"batchSynch", text:"批量同步库存", iconCls:"add", handler:function(){batchSynch();}}
					],
					style : 'padding-right:20px'
				});
				
				// 列表
				var model = getGridPanelModel("grid0", "实际库存查询列表", columns, store, limit, Ext.get("grid"),sm,foot_tb);
				var grid = createGridPanelByModel(model);
   		    	setDefaultPage(0, limit, store);
   		    	
   		    	// 分页工具条
				var toolBar = createPagingToolbar(store, limit);
				toolBar.setHeight(toolbarHeight);
				toolBar.render("toolBar");
    			
				// 检索
				function getQuery() {
					// 商品码
					var sku = getById('num');
					if (sku == Ext.getCmp('num').emptyText) {
						sku = "";
					}
					// 商品名称
					var name = getById('name');
					// 品牌
					var brandCode = typeComboBox.getValue();
					check = true;
					setDefaultPage(0, limit, store);
					store.setBaseParam("sku", sku);
					store.setBaseParam("os", 1); 
					store.setBaseParam("title", name); 
					store.setBaseParam("brandCode", brandCode); 
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
		         		name.setAttribute("value", "stockList");
            			postForm.appendChild(name); 
            			
            			var type = document.createElement("input");  //email input 
            			type.setAttribute("name", "type");   
            			type.setAttribute("value", "5");
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
					var url = "../common/stockUpload.jsp";
					fileWindow = showWindow('stockUpload','文件上传',url,400,150);
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
		    	  				data.shareStock = getFiledValue(record, 'shareStock');
		    	  				//data.saleStock = getFiledValue(record, 'saleStock');
		    	  				data.virStock = getFiledValue(record, 'virStock');
		    	  				data.virFrozenStock = getFiledValue(record, 'virFrozenStock');
		    	  				data.lastSyncDate = getFiledValue(record, 'lastSyncDate');
		    	  				data.kValue = getFiledValue(record, 'kValue');
		    	  				data.stock = getFiledValue(record, 'stock');
		    	  				tempLocalData.push(data);
		    	  			}
			    	 	}
		    	  		createAlert("信息提示", "追加记录成功！");
		    	  	}
				}
				
				// 获取指定字段的值
				function getFiledValue(record, name) {
					return record.get(name);	
				}
				
				// 追加记录同步库存
				function synch() {
					var length = tempLocalData.length;
					if (length == 0) {
						createAlert("信息提示", "没有追加记录！");
					} else {
						var msg = "确定将追加的所有记录批量同步库存吗？";
						
						Ext.Msg.confirm("警告",msg,function(btn){
	                    	if(btn == "yes"){
								var url = "realStockReport.jsp?flag=1";
								showStockWindow(url);
	                    	}
						});
					}
				}
				
				// 批量同步库存
				function batchSynch() {
					if (!check) {
						createAlert("信息提示","请点击检索后再进行批量同步库存操作!");
						return false;
					}
					var msg = "确定将列表中的所有记录批量同步库存吗？";
					
					Ext.Msg.confirm("警告",msg,function(btn){
                    	if(btn == "yes"){
							var url = "realStockReport.jsp?flag=2";
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
			
			function showDownLoad(skus) {
				if (skus) {
					var objs = skus.join(",");
					var msg = "是否下载失败的sku";
					Ext.Msg.confirm("提示",msg,function(btn){
                    	if(btn == "yes"){
                    		var postForm = document.createElement("form");//表单对象   
                    		postForm.method="post" ;   
                    		postForm.action = path + "/scstock/downLoadTemplate.htm";
                    		
                    		var input = document.createElement("input");  //email input 
                			input.setAttribute("name", 'title');   
                			input.setAttribute("value", '商品11位码');
                			postForm.appendChild(input);  
                			
                			var skus = document.createElement("input");  //email input 
                			skus.setAttribute("name", 'skus');   
                			skus.setAttribute("value", objs);
                			postForm.appendChild(skus);  
                			
                    		document.body.appendChild(postForm);   
                    		postForm.submit() ;   
                    		document.body.removeChild(postForm);
                    	}
					});
				}
			}
			
			function showFileWindow(data) {
				//fileData = data;
				tempLocalData = data;
				var url = "realStockReport.jsp?flag=3";
				showStockWindow(url);
			}
			
			function showStockWindow(url) {
				storeWindow = showWindow('realStockReport','同步库存', url, 800, 500, 50, 20);
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

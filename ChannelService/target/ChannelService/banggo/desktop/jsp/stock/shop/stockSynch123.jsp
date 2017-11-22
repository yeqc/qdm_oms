<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
%>

<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>商品库存同步管理</title>
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
			
			Ext.onReady(function() {
				Ext.QuickTips.init();
				// 查询区域的高度
				var maxSelectHeight = 95;
				var minSelectHeight = 25;
				var toolbarHeight = 25;
				
				setDefault();
				
				// 每页记录数
				var limit = 20;
				
				// 记录是否查询过
				var check = false;
				
				// 品牌
   				var typeComboBox = getBrandCombox();
   				// 渠道
 				// 渠道
				var channelStore = createJsonStore('list', ['channelCode','channelName'], path + "/scChannel/getScChannelList.htm");
				channelStore.load();
				// 默认选择
				var channelRecord = Ext.data.Record.create([
					'channelCode',
					'channelName'
				]);
				
				var channelRecord_data = new channelRecord({channelCode: '',channelName: '请选择...'});
				channelStore.on("load", function(obj, record) {
					channelStore.insert(0, channelRecord_data);
					channelComboBox.setValue("");
					addColumns();
				})
				
				var channelComboBox = createComboBox(channelStore, 'channel', 'channelName', 'channelCode', 'local', '');
				
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
	                			{xtype:"textfield",name:"num",id:"num",width:150,fieldLabel:"商品码"},
	                			{xtype:'displayfield',value:'商品名称:', width:60},
	                			{xtype:"textfield",name:"name",id:"name",width:150},
	                			{xtype:'displayfield',value:'品类:', width:60},
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
						{name:'realStock'},
						{name:'saleStock'},
						{name:'virStock'},
						{name:'virFrozenStock'},
						{name:'status'},
						{name:'lastSyncDate'},
						{name:'kValue'},
						{name:'stock'},
						{name:'channelCode'},
						{name:'channelName'},
						{name:'changeStock'},
						{name:'changeStock1'}
			      	],
			      	url:path + '/scstock/getSctockList.htm', // 测试随便给的一个请求
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
				
	            var fm = Ext.form;
	            
	            var comboData=[
				['人工同步','人工同步'],
				['自动同步','自动同步']];
	            
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
					{header:'商品名称',dataIndex:'name',align:"center",sortable:true,renderer:function(value, metadata, record){
						return getLeftFormate(value);
					}},	
					{header:'颜色',dataIndex:'color',align:"center",sortable:true,renderer:function(value, metadata, record){
						return getLeftFormate(value);
					}},	
					{header:'尺码',dataIndex:'size',align:"center",sortable:true,renderer:function(value,metadata,record){
						return getLeftFormate(value);
					}},
					{header:'ERP同步方式',dataIndex:'changeStock1',align:"center",sortable:true,renderer:function(value,metadata,record){
						return getLeftFormate(value);
					},
					editor:new Ext.grid.GridEditor(new Ext.form.ComboBox({
						store:new Ext.data.SimpleStore({
								fields:['value','text'],
								data:comboData}),
						//emptyText:'请选择',
						mode:'local',
						triggerAction:'all',
						valueField:'value',
						displayField:'text'
					}))
		        	}
				];
				
				var model;
				var grid;

				
				function addColumns() {
					var count = channelStore.getCount();
					for (var i = 1; i < count; i++) {
						var data = channelStore.getAt(i);
						var header = {dataIndex:'operUser',align:"center",sortable:true,renderer:function(value,metadata,record){
							return getLeftFormate(value);
						},editor:new Ext.grid.GridEditor(new Ext.form.ComboBox({
							store:new Ext.data.SimpleStore({
									fields:['value','text'],
									data:comboData}),
							//emptyText:'请选择',
							mode:'local',
							triggerAction:'all',
							valueField:'value',
							displayField:'text'
						}))};
						header.header = data.get('channelName');
						columns.push(header);
					}
					
					var cm = new Ext.grid.ColumnModel({
				        columns: columns
				    });
					
					model = getEditGridPanelModel("grid0", "库存同步列表", cm, store, limit, Ext.get("grid"),sm,foot_tb);
					grid = createEditGridPanelByModel(model);
					
					grid.on('afteredit', afterEdit, this );
					
					// 获取修改后值的
					function afterEdit(e) {
						var record = store.getAt(e.row);
						alert("record=" + record.get('sku') + " " + "选择后的值:" + e.value + " " + "当前编辑的字段名:" + e.field);
					}
				}
				
				// 同步类型
				var synchTypeData = [
					['请选择',''],
					['人工同步','1'],
					['自动同步','2']
				];
				var synchTypeStore = new Ext.data.ArrayStore({
        			fields: ['name', 'id'],
        			data : synchTypeData
    			});
				
    			// 同步类型下拉框对象
    			var synchTypeComboBox = createComboBox(synchTypeStore, 'synchType', 'name', 'id', 'local', '');
    			synchTypeComboBox.setValue("");
   				
				// 列表尾部工具条
				var foot_tb = new Ext.Toolbar({
					items:[
						{xtype : "tbfill"}
						,{xtype: 'tbtext', text: '同步类型:'}
						,channelComboBox
						,synchTypeComboBox
						,"-"
						,{id:"add", text:"批量同步库存", iconCls:"add", handler:function(){}}
					],
					style : 'padding-right:20px;'
				});
				
				
   		    	setDefaultPage(0, limit, store);
   		    	
   		    	// 分页工具条
				var toolBar = createPagingToolbar(store, limit);
				toolBar.setHeight(toolbarHeight);
				toolBar.render("toolBar");
    			
				// 检索
				function getQuery() {
					
					check = true;
			    	var store = Ext.StoreMgr.lookup("dataStore");
			    	setDefaultPage(0, limit, store);
			    	
			    	store.load();
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

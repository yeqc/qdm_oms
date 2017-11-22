<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
%>

<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>调整单结果日志</title>
		<style>
    		.x-form-display-field-left{
    			padding-top: 2px;
    			text-align:left;
			}
		</style>
		
		<script type="text/javascript" src="<%=path%>/banggo/js/common/ext.js"></script>
		<script type="text/javascript" src="<%=path%>/banggo/js/common/common.js"></script>
		<script>
			var path = "<%=path%>";
			var isExpand = true;
			var changeNo = getQueryString("changeNo");
			Ext.onReady(function() {
				Ext.QuickTips.init();
				setDefault();
				// 每页记录数
				var limit = 20;
					
				// 列表数据
				var store = new Ext.data.JsonStore({
					storeId:"dataStore",
					fields:[
						{name: "ids",mapping:"id.inc"},
					    {name:'id'},
					    {name:'stockChangeNo'},
					    {name:'sku'},
					    {name:'type'},
						{name:'reqBy'},
						{name:'reqDate'},
						{name:'memo'}
			      	],
					url:path+'/log/getReportLogList.htm',
					root:'list',
					idProperty: 'ids',
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
	            
				var columns = [
					new Ext.grid.RowNumberer({
						header:"序号",
						width:35,
		    	  		renderer:function(value,metadata,record,rowIndex){
		    		   		return getRecordStart() + 1 + rowIndex;
		    			}
		    		}),
		    		{header:'商品编码',dataIndex:'sku',align:"center",renderer:function(value,metadata,record){
						return value;
					}},	
					{header:'调整单号',dataIndex:'stockChangeNo',align:"center",width:150,renderer:function(value,metadata,record){
						return value;
					}},	
					{header:'调整类型',dataIndex:'type',align:"center",renderer:function(value,metadata,record){
						if ("grab_report" == value) {
							value = "强占库存";
						} else if ("vir_report" == value) {
							value = "虚拟库存";
						} else if ("sale_report" == value) {
							value = "可售库存";
						}
						return value;
					}},
					{header:'调整值',dataIndex:'memo',align:"center", width:150, renderer:function(value, metadata, record){
						//metadata.attr = 'style="white-space:normal;"';
						metadata.attr = 'ext:qtip="详细信息:<br/>' + value +'"';
						return getLeftFormate(value);
						//return   '<div ext:qtitle="" ext:qtip="'  + value +  '">' + value + '</div>' ; 
					}},	
					{header:'执行日期',dataIndex:'reqDate',align:"center", width:150,renderer:function(value, metadata, record){
						if(value){
							var dt=	new Date();
							dt.setTime(value);
							return formatDateformate(dt, "Y-m-d H:i:s");
						}
					}},	
					{header:'操作人',dataIndex:'reqBy',align:"center",renderer:function(value,metadata,record){
						return value;
					}}
				];
				
				// 列表
				var model = getGridPanelModel("grid0", "日志列表", columns, store, limit, Ext.get("grid"));
				var grid = createGridPanelByModel(model);
   		    	setDefaultPage(0, limit, store);
   		    	
				// 检索
				function getQuery() {
			    	var store = Ext.StoreMgr.lookup("dataStore");
			    	setDefaultPage(0, limit, store);
			    	store.setBaseParam("stockChangeNo", changeNo);
			    	store.setBaseParam("logType", 2);
			    	store.load();
				}
				
				getQuery();
				
				function setDefault() {
					var clientHeight = document.body.clientHeight;
					
					Ext.get("grid").setHeight(clientHeight);
	           	}
	           	
	           	function setColl() {
					setResize();
				}
	           	
	           	function setResize() {
					var clientHeight = document.body.clientHeight;
					var clientWidth = document.body.clientWidth;
					var height = isExpand == true ? maxSelectHeight : minSelectHeight;
	        		
	        		Ext.get("grid").setHeight(clientHeight);
					grid.setHeight(clientHeight);
					
					Ext.get("condition").setWidth(clientWidth);
	        		Ext.get("grid").setWidth(clientWidth);
					grid.setWidth(clientWidth);
				}
				
				window.onresize=function(){
					setResize();
	        	}
			});
			
			function showStockWindow(id, name, url) {
				storeWindow = showWindow(id, name, url, 800, 500, 50, 20);
				storeWindow.show();
			}
		</script>
	</head>
	
	<body>
		<div id="condition" style="width:100%;"></div>
    	<div class="clear"></div>
    	<div id="grid" style="width:100%"></div>
  	</body>
</html>

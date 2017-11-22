<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
%>

<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>渠道库存调整管理</title>
		<style>
    		.x-form-display-field-left{
    			padding-top: 2px;
    			text-align:left;
			}
		</style>
		<script type="text/javascript" src="<%=path%>/banggo/js/common/ext.js"></script>
		<script type="text/javascript" src="<%=path%>/banggo/js/common/common.js"></script>
		<script type="text/javascript" src="<%=path%>/banggo/js/common/cookie.js"></script>
		<script type="text/javascript" src="<%=path%>/banggo/desktop/js/CommonRights.js"></script>
		<script>
			importJs("DateTimeField.js");
			// 查询form
			var selectform, storeWindow, reportWindow;
			var path = "<%=path%>";
			var isExpand = true;
			
			Ext.onReady(function() {
				Ext.QuickTips.init();
				// 查询区域的高度
				var maxSelectHeight = 115;
				var minSelectHeight = 25;
				
				setDefault();
				
				// 每页记录数
				var limit = 20;
				
				// 记录是否查询过
				var check = false;
				
				// 调整类型
				var typeData = [
					['请选择',''],
					['强占库存','1'],
					//['虚拟库存','2'],
					['可售库存','3']
				];
				var typeStore = new Ext.data.ArrayStore({
        			fields: ['name', 'id'],
        			data : typeData
    			});
    			// 调整类型下拉框对象
    			var typeComboBox = createComboBox(typeStore, 'stockChangeType', 'name', 'id', 'local', '');
   				typeComboBox.setValue("");
   				
   				// 操作状态
				var stateData = [
					['请选择',''],
					['未执行','0'],
					['已执行','1']
				];
				var stateStore = new Ext.data.ArrayStore({
        			fields: ['name', 'id'],
        			data : stateData
    			});
    			// 操作状态下拉框对象
    			var stateComboBox = createComboBox(stateStore, 'stockChangeState', 'name', 'id', 'local', '');
    			stateComboBox.setValue("");
				
    			var buttons = [
					{text:"检索",handler:function(){
				   		getQuery();
					}},
					{text:"重置",handler:function(){
                 		selectform.getForm().reset();
               		}}	
				];
    			
    			/*if (!isUpdateUser()) {
    				buttons.push({text:"生成调整单",handler:function(){
    					//showReportWindow();
    					doReport(3);
    		       	}});
    			}*/
    			buttons.push({text:"生成调整单",handler:function(){
					showReportWindow();
		       	}});
    		
				// 初始请求
				selectform = new Ext.form.FormPanel({
					title:"查询条件",
					frame:true,
					renderTo:"condition",
					collapsible:true,
					labelWidth:120,
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
								{xtype:"textfield",name:"stockChangeNo",id:"stockChangeNo",width:150,fieldLabel:"调整单号"},
								{xtype:'displayfield',value:'调整类型:', width:60},
								typeComboBox,
								{xtype:'displayfield',value:'操作状态:', width:60},
								stateComboBox
							]
						},
	            		{xtype: 'compositefield',
	                		items: [
	                			{xtype: 'datefield',width:150,id: 'addTimeBegin',name: 'addTimeBegin',format:"Y-m-d",fieldLabel:"调整单生成日期"},
	                			{xtype:'displayfield',value:'~', width:60},
	                			{xtype: 'datefield',width:150,id: 'addTimeEnd',name: 'addTimeEnd',format:"Y-m-d"},
	                			{xtype:'displayfield',value:'执行日期:', width:60},
	                			{xtype: 'datefield',width:150,id: 'updateTimeBegin',name: 'updateTimeBegin',format:"Y-m-d"},
	                			{xtype:'displayfield',value:'~', width:60},
	                			{xtype: 'datefield',width:150,id: 'updateTimeEnd',name: 'updateTimeEnd',format:"Y-m-d"}
	                		]
	            		}
					],
					buttons:buttons,
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
						{name:'stockChangeNo'},
						{name:'stockChangeType'},
						{name:'stockChangeState'},
						{name:'addTime'},
						{name:'addBy'},
						{name:'updateTime'},
						{name:'updateBy'},
						{name:'memo'}
			      	],
					url:path+'/scStockChange/getScStockChangeList.htm',
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
					{header:'调整单号',dataIndex:'stockChangeNo',width:120,align:"center",sortable:true,renderer:function(value, metadata, record){
						var type = record.get("stockChangeType");
						var state = record.get("stockChangeState");
						if (record.get("addBy") != getLoginName()) {
		    	  			state = 1;
	   		    		}
						var html = '<a href="javascript:void(0)" onclick="doUpdateData(\''+value+'\',\''+type+'\',\''+state+'\')">'+value+'</a>';
						return getLeftFormate(html);
					}},
					{header:'类型',dataIndex:'stockChangeType',align:"center",sortable:true,renderer:function(value, metadata, record){
						return getLeftFormate(getType(value));
					}},
					{header:'状态',dataIndex:'stockChangeState',align:"center",sortable:true,renderer:function(value, metadata, record){
						
						if (value == 1) {
							value = getState(value);
							var changeNo = record.get("stockChangeNo");
							var htm = '<a href="javascript:void(0)" onclick="doLog(\''+changeNo+'\')">'+value+'</a>';
							return getLeftFormate(htm);
						} else {
							value = getState(value);
							return getLeftFormate(value);
						}
						
						
					}},	
					{header:'申请日期',dataIndex:'addTime',align:"center", width:150,sortable:true,renderer:function(value, metadata, record){
						if(value){
							return getLeftFormate(formatDateformate(value, "Y-m-d H:i:s"));
						}
					}},	
					{header:'申请人',dataIndex:'addBy',align:"center",sortable:true,renderer:function(value,metadata,record){
						return getLeftFormate(value);
					}},	
					{header:'执行日期',dataIndex:'updateTime',align:"center",width:150,sortable:true,renderer:function(value,metadata,record){
						if(value){
							return getLeftFormate(formatDateformate(value, "Y-m-d H:i:s"));
						}
					}},	
					{header:'执行人',dataIndex:'updateBy',align:"center",sortable:true,renderer:function(value, metadata, record){
						return getLeftFormate(value);
					}},
					{header:'备注',dataIndex:'memo',align:"center",sortable:true,renderer:function(value, metadata, record){
						return getLeftFormate(value);
					}}
				];
				// 权限设置  执行者 （执行|查看）, 申请者(查看|更新|删除), 其他人(只可以看列表)
				var barItems = [];
				if (isUpdateUser()) {
					barItems.push({id:"add", text:"批量执行", iconCls:"add", handler:function(){doBatchQuery();}});
					barItems.push("-");
					barItems.push({id:"select", text:"查看", iconCls:"update", handler:function(){doBatchUpdateData();}});
					barItems.push("-");
				} else {
					barItems.push({id:"update", text:"编辑/查看", iconCls:"update", handler:function(){doBatchUpdateData();}});
					barItems.push("-");
					barItems.push({id:"delete", text:"批量删除", iconCls:"delete", handler:function(){doDelete();}});
					barItems.push("-");
				}
				var tb = new Ext.Toolbar({
					items:[
						barItems
					]
				});
				
				// 列表
				var model = getGridPanelModel("grid0", "查询列表", columns, store, limit, Ext.get("grid"),sm);
				model.tbar = tb;
				var grid = createGridPanelByModel(model);
   		    	setDefaultPage(0, limit, store);
   		    	
   		    	// 双击查看
   		    	// 是否有执行者权限
   		    	function isUpdateUser() {
   		    		return isUserChangeInfoRight();
   		    	}
   		    	
   		    	// 批量更新
   		    	function doBatchUpdateData() {
   		    		var rows = grid.getSelectionModel().getSelections();
		    	  	var leng = rows.length;
		    	  	
		    	  	if (leng == 0) {
		    	  		createAlert("提示信息", "没有选择记录!");
		    	  		return false;
		    	  	} else if (leng > 1) {
		    	  		createAlert("提示信息", "只能选择单条记录!");
		    	  		return false;
		    	  	} else {
		    	  		var record = rows[0];
		    	  		var state = record.get("stockChangeState");
		    	  		if (record.get("addBy") != getLoginName()) {
		    	  			state = 1;
	   		    		}
	    	  			doUpdateData(record.get("stockChangeNo"), record.get("stockChangeType"), state);
		    	  	}
   		    	}
   		    	
   		    	// 批量执行
   		    	function doBatchQuery() {
   		    		var rows = grid.getSelectionModel().getSelections();
		    	  	var leng = rows.length;
		    	  	
		    	  	if (leng == 0) {
		    	  		createAlert("提示信息", "没有选择记录!");
		    	  		return false;
		    	  	} else {
						var msg = "确定执行吗？";
						
						Ext.Msg.confirm("警告",msg,function(btn){
                        	if(btn == "yes"){
                        		// 查询是否有已经执行的记录
                        		var checkData = [];
                        		var checkType = [];
                        		var isMsg = false;
                        		for (var i = 0; i < leng; i++) {
                        			var record = rows[i];
                        			if (record.get("stockChangeState") == 0) {
                        				checkData.push(record.get("stockChangeNo"));
                        				var type = record.get("stockChangeType");
                        				checkType.push(type);
                        				if (type == 3) {
                        					isMsg = true;
                        				}
                        			}
                        		}
                        		
                        		var nos = checkData.join(",");
                        		if (nos) {
                        			var param = {"nos":nos};
                        			param.type = checkType.join(",");
                        			if (isMsg) {
                        				// 批量执行, 提示将同步改为人工同步
                            			Ext.Msg.confirm("警告","本次操作包含可售库存调整单,是否将调整单中的记录设置为ERP不自动同步?",function(btn){
                            				if(btn == "yes"){
                            					param.flag = 0;
                            					doExecute(param);
                            				} else {
                            					param.flag = 1;
                            					doExecute(param);
                            				}
                            			});
                        			} else {
                        				doExecute(param);
                        			}
                        		} else {
                        			createAlert("提示信息", "选择记录全部执行，无需批量执行操作!");
            		    	  		return false;
                        		}
                        	}
						});
		    	  	}
   		    	}
   		    	
   		    	// 执行
   		    	function doExecute(param) {
   		    		createAjax(path + "/scStockChange/doUpdateScStockChange.htm", function(response, opts) {
            			createAlert("提示信息", "操作成功!", function() {
            				store.load();
            			});
            		}, null, param);
   		    	}
   		    	
   		    	// 批量删除
   		    	function doDelete() {
   		    		var rows = grid.getSelectionModel().getSelections();
		    	  	var leng = rows.length;
		    	  	
		    	  	if (leng == 0) {
		    	  		createAlert("提示信息", "没有选择记录!");
		    	  		return false;
		    	  	} else {
						var msg = "确定删除吗？";
						
						Ext.Msg.confirm("警告",msg,function(btn){
                        	if(btn == "yes"){
                        		// 查询是否是属于申请人
                        		var checkData = [];
                        		for (var i = 0; i < leng; i++) {
                        			var record = rows[i];
                        			if (record.get("addBy") == getLoginName()) {
                        				// 已经执行的不能删除
                        				if (record.get("stockChangeState") == 1) {
                        					createAlert("提示信息", "选择记录中存在已执行的调整单，请重新选择!");
                            				return false;
                        				}
                        				checkData.push(record.get("stockChangeNo"));
                        			} else {
                        				createAlert("提示信息", "选择记录中有其他人申请调整单，请重新选择!");
                        				return false;
                        			}
                        		}
                        		
                        		var nos = checkData.join(",");
                        		createAjax(path + "/scStockChange/doDeleteScStockChange.htm", function(response, opts) {
                        			createAlert("提示信息", "操作成功!", function() {
                        				store.load();
                        			});
                        		}, null, {"nos":nos});
                        	}
						});
		    	  	}
   		    	}
   		    	
   		    	// 生成对账单
   		    	function showReportWindow() {
   		    		if (!reportWindow) {
   		    			// 调整类型下拉框对象
   		    			var typeComboBox = createComboBox(typeStore, 'reportType', 'name', 'id', 'local', '调整单类型');
   		   				typeComboBox.setValue("");
   		   				
   		    			var form = new Ext.form.FormPanel({
   		    		        baseCls: 'x-plain',
   		    		        labelWidth: 80,
   		    		        items: [typeComboBox]
   		    		    });
   		    			
   		    			reportWindow = new Ext.Window({
   		    		        width: 300,
   		    		        height:100,
   		    		        layout: 'fit',
   		    		        plain:true,
   		    		        bodyStyle:'padding:5px;',
   		    		  		title:'生成调整单',
   		    				modal:true,
   		    				resizable:false,
   		    				closeAction: 'hide',
   	   		    			renderTo: document.body  ,  
   	   		    			items:form,
   	   		    			buttonAlign:"center",
   	   		    	  	 	buttons: [
   	   		    	  	 		{text:'确定',handler:function(){
   	   						   		var type = typeComboBox.getValue();
   	   						   		if (type) {
   	   						   			doReport(type);
   	   						   		} else {
   	   						   			createAlert("信息提示","请选择调整单类型");
   	   						   		}
   	   							}},
   	   		    	  	 		{text:'取消',handler:function(){
   	   		    	  	 			reportWindow.hide();
   	   							}}
   	   		    	  	 	] 
   	   		    		});
   		    		}
   		    		reportWindow.show();
   		    	}
   		    	
   		    	// 进入对账单
   		    	function doReport(type) {
   		    		var id, name, url;
   		    		url = path + "/banggo/desktop/jsp/stock/channel/";
   		    		if (type == 1) {
   		    			id = "grabStockAdd";
   		    			name = "强占库存调整单生成";
   		    			url += "grabStockAdd.jsp";
   		    		} else if (type == 2) {
   		    			id = "virtualStockAdd";
   		    			name = "虚拟库存调整单生成";
   		    			url += "virtualStockAdd.jsp";
   		    		} else if (type == 3) {
   		    			id = "saleStockAdd";
   		    			name = "可售库存调整单生成";
   		    			url += "saleStockAdd.jsp";
   		    		}
				  	var obj = parent.createNodeTab(id, name, url);
					parent.createMainTab(obj);
   		    		reportWindow.hide();
   		    	}
   		    	
				// 检索
				function getQuery() {
					
					// 生成日期
					var addTimeBegin = Ext.fly('addTimeBegin').getValue();
					var addTimeEnd = Ext.fly('addTimeEnd').getValue();
					// 执行日期
					var updateTimeBegin = Ext.fly('updateTimeBegin').getValue();
					var updateTimeEnd = Ext.fly('updateTimeEnd').getValue();
					
					if (addTimeBegin && addTimeEnd && addTimeEnd < addTimeBegin) {
						createAlert('提示信息', "调整单生成开始日期不能大于结束日期!");
						return false;
					}
					if (updateTimeBegin && updateTimeEnd && updateTimeEnd < updateTimeBegin) {
						createAlert('提示信息', "调整单执行开始日期不能大于结束日期!");
						return false;
					}
					
					// 调整单号
					var stockChangeNo = getById('stockChangeNo');
					// 调整类型
					var stockChangeType = typeComboBox.getValue();
					// 执行状态
					var stockChangeState = stateComboBox.getValue();
					
					check = true;
			    	var store = Ext.StoreMgr.lookup("dataStore");
			    	setDefaultPage(0, limit, store);
			    	
			    	store.setBaseParam("stockChangeNo", stockChangeNo);
			    	store.setBaseParam("stockChangeType", stockChangeType);
			    	store.setBaseParam("stockChangeState", stockChangeState);
			    	store.setBaseParam("addTimeBegin", addTimeBegin);
					store.setBaseParam("addTimeEnd", addTimeEnd);
					store.setBaseParam("updateTimeBegin", updateTimeBegin);
					store.setBaseParam("updateTimeEnd", updateTimeEnd);
			    	store.load();
				}
				
				function getType(value) {
					if (value == 1) {
						value = "强占库存";
					} else if (value == 2) {
						value = "虚拟库存";
					} else if (value == 3) {
						value = "可售库存";
					}
					
					return value;
				}
				
				function getState(value) {
					if (value == 0) {
						value = "未执行";
					} else if (value == 1) {
						value = "已执行";
					}
					return value;
				}
				
				function setDefault() {
					var clientHeight = document.body.clientHeight;
					
					Ext.get("condition").setHeight(maxSelectHeight);
					Ext.get("grid").setHeight(clientHeight - maxSelectHeight);
	           	}
	           	
	           	function setColl() {
					setResize();
				}
	           	
	           	function setResize() {
					var clientHeight = document.body.clientHeight;
					var clientWidth = document.body.clientWidth;
					var height = isExpand == true ? maxSelectHeight : minSelectHeight;
						
	        		Ext.get("condition").setHeight(height);
	        		Ext.get("grid").setHeight(clientHeight-height);
					grid.setHeight(clientHeight-height);
					
					Ext.get("condition").setWidth(clientWidth);
	        		Ext.get("grid").setWidth(clientWidth);
					grid.setWidth(clientWidth);
				}
				
				window.onresize=function(){
					setResize();
	        	}
			});
			
			// 查看日志
			function doLog(stockChangeNo) {
				var url = path + "/banggo/desktop/jsp/stock/log/changeLogList.jsp?changeNo="+stockChangeNo;
				var storeWindow = showWindow("logWindow", "调整单执行日志查看", url, 750, 400, 50, 20);
				storeWindow.show();
			}
			
			// 更新
	    	function doUpdateData(stockChangeNo, type, state) {
	    		if (type == 3) {
	    			var url = "saleStockList.jsp?stockChangeNo=" + stockChangeNo + "&state=" + state;
					showStockWindow("saleStockList-"+stockChangeNo, "可售库存调整单-" + stockChangeNo, url);
	    		} else if (type == 2) {
	    			var url = "virtualStockList.jsp?stockChangeNo=" + stockChangeNo + "&state=" + state;
				showStockWindow("virtualStockList-"+stockChangeNo, "虚拟库存调整单-" + stockChangeNo , url);
	    		} else if (type == 1) {
	    			var url = "grabStockList.jsp?stockChangeNo=" + stockChangeNo + "&state=" + state;
				showStockWindow("grabStockList-"+stockChangeNo, "强占库存调整单-" + stockChangeNo , url);
	    		}
	    	}
			
			function showStockWindow(id, name, url) {
				url = path + "/banggo/desktop/jsp/stock/channel/" + url;
				var obj = parent.createNodeTab(id, name, url);
				parent.createMainTab(obj);
			}
		</script>
	</head>
	
	<body>
    	<div id="condition" style="width:100%;"></div>
    	<div class="clear"></div>
    	<div id="grid" style="width:100%"></div>
  	</body>
</html>

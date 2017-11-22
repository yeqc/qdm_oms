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
		<script type="text/javascript" src="<%=path%>/banggo/js/ux/ColumnHeaderGroup.js"></script>
		<link rel="stylesheet" type="text/css" href="<%=path%>/banggo/js/ux/css/ColumnHeaderGroup.css" />
		<script>
			var path = "<%=path%>";
			
			Ext.onReady(function() {
				Ext.QuickTips.init();
				// 查询区域的高度
				var maxSelectHeight = 95;
				var minSelectHeight = 25;
				var toolbarHeight = 25;
				
				setDefault();
				// 每页记录数
				var limit = 20;
				
				var structure = {
					channel: ['MA','MB', 'MC', 'MD']
				},
				products = ['最后一次同步库存值', '预售可用库存', '预售占用库存', '上次更新时间'],
				fields = [], // 字段
				data = [],
				channelGroupRow = [];
				channelGroupRow.push({
                    header: '',
                    colspan: 6
                });
				
				// 初始请求
				var selectform = new Ext.form.FormPanel({
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
								{xtype:"textfield",name:"num",id:"num",width:150,fieldLabel:"商品码"}
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
						{text:"关闭",handler:function(){
							close();
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
					{header:'商品11位码',dataIndex:'sku',align:"center",sortable:true,renderer:function(value){
						if (value != undefined) {
							return getLeftFormate(value);
						}
					}},
					{header:'可用库存（总）',dataIndex:'company',align:"center",sortable:true,renderer:function(value,metadata,record){
						if (value != undefined) {
							return getLeftFormate(value);
						}
					}},
					{header:'ERP最后同步库存',dataIndex:'name',align:"center",sortable:true,renderer:function(value, metadata, record){
						if (value != undefined) {
							return getLeftFormate(value);
						}
					}},	
					{header:'渠道强占库存',dataIndex:'color',align:"center",sortable:true,renderer:function(value, metadata, record){
						if (value != undefined) {
							return getLeftFormate(value);
						}
					}}
				];
				
				function generateConfig(){
			        var arr,
			            numProducts = products.length;
			            
			    	Ext.iterate(structure, function(continent, channels){
			           /* continentGroupRow.push({
			                header: continent,
			                align: 'center',
			                colspan: channels.length * numProducts
			            });*/
			            
			            Ext.each(channels, function(channel){
			            	channelGroupRow.push({
			                    header: channel,
			                    colspan: numProducts,
			                    align: 'center'
			                });
			                Ext.each(products, function(product){
			                    fields.push({
			                        type: 'int',
			                        name: channel + product
			                    });
			                    columns.push({
			                        dataIndex: channel + product,
			                        header: product,
			                        width : 150,
			                        renderer: Ext.util.Format.usMoney
			                    });
			                });
			                arr = [];
			                for(var i = 0; i < 20; ++i){
			                    arr.push((Math.floor(Math.random()*11) + 1) * 100000);
			                }
			                data.push(arr);
			            });
			        })
			    }
				
				generateConfig();
				
				var group = new Ext.ux.grid.ColumnHeaderGroup({
			        rows: [channelGroupRow]
			    });
					
				// 列表数据
				/*var store = new Ext.data.JsonStore({
					storeId:"dataStore",
					fields:[
						{name:'id'},
						{name:'sku'},
						{name:'company'},
						{name:'name'},
						{name:'color'},
						{name:'size'},
						{name:'realStock'},
						{name:'saleStock'},
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
				
	            var sm = new Ext.grid.CheckboxSelectionModel({  
    				checkOnly: true,  
    				singleSelect: false
				});
				
				// 列表
				var model = getGridPanelModel("grid0", "实际库存查询列表", columns, store, limit, Ext.get("grid"),sm);
				var grid = createGridPanelByModel(model);
   		    	setDefaultPage(0, limit, store);
   		    	
				// 检索
				function getQuery() {
					// 商品码

					var sku = Ext.fly('num').getValue();
					if (sku == Ext.getCmp('num').emptyText) {
						sku = "";
					}
					check = true;
					setDefaultPage(0, limit, store);
					store.setBaseParam("sku", sku);
					store.setBaseParam("os", 1); 
			    	store.load();
				}*/
				
				/*var grid = new Ext.grid.GridPanel({
			        renderTo: 'grid',
			        title: 'Sales By Location',
			        height:Ext.get('grid').getHeight(),
			        store: new Ext.data.ArrayStore({
			            fields: fields,
			            data: data
			        }),
			        columns: columns,
			        viewConfig: {
			            forceFit: true
			        },
			        plugins: group
			    });*/
			    
			    var store = new Ext.data.ArrayStore({
		            fields: fields,
		            data: data
		        });
			    
				// 列表
				var model = getGridPanelModel("grid0", "库存镜像查询列表", columns, store, limit, Ext.get("grid"),sm);
			    model.plugins = group;
				var grid = createGridPanelByModel(model);
   		    	setDefaultPage(0, limit, store);
				
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
		</script>
	</head>
	
	<body>
		<div id="condition" style="width:100%;"></div>
    	<div class="clear"></div>
    	<div id="grid" style="width:100%"></div>
  	</body>
</html>

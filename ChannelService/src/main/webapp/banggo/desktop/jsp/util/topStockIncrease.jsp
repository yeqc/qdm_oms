<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
%>

<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>淘宝库存增量更新</title>
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
			var tempUserData = [];
			var selectform;
			// 每页记录数
			var limit = 20;
			var store;
			var fpFileUpload;
			Ext.onReady(function() {

				
				Ext.QuickTips.init();
				var p1 = new Ext.form.FormPanel({
					title : "指定sku处理",
					frame : true,
					renderTo : "condition",
					collapsible : true,
					labelWidth : 60,
                	labelAlign : 'right',
					buttonAlign : 'left',
					items : [ 
	            		{xtype : 'compositefield',
	                		items : [
	                			{xtype : "textarea", name : "skus", id : "skus", width : 400, fieldLabel : "SKU"},
	                			{xtype : 'displayfield', value : '(多个SKU以英文逗号隔开)', width : 250 }
	                		]
	            		}
					],
					buttons : [  
						{
							text:"确定处理",
							handler : function(){
								
							}
						}
					]
				});
				
				var p2 = new Ext.Panel({
					title : "通过sku文件处理",
					frame : true,
					renderTo : "condition",
					collapsible : true,
					labelWidth : 60,
					buttonAlign : 'left',
					buttons : [  
						{
							text : '通过文件处理',
							handler : function() {
			                    winFielUpload.show();  
							}
						} 
					]
				});
				
				fpFileUpload = new Ext.FormPanel({  
                    id : 'fpFileUpload',  
                    frame : true,  
                    fileUpload : true,  
                    //url : 'Default.aspx',  
                    items : [  
                        {  
                            xtype : 'textfield',  
                            allowBlank : false,  
                            fieldLabel : '选择文件',  
                            inputType : 'file',  
                            name : 'topFile'  
                        }  
                    ],  
                    buttonAlign : 'center',  
                    buttons : [  
                        {  
                            text : '上传',  
                            handler : function(){ 
                                if(fpFileUpload.form.isValid()){  
                                    fpFileUpload.form.submit({  
                                        method : 'post',  
                                        url  :  path + '/update/uploadTopFile1.htm',  
                                        waitMsg : '文件上传中...',  
                                        success :  function() {  
                                            Ext.Msg.alert("系统提示", "文件上传成功！");  
                                            winFielUpload.hide();  
                                        },  
                                        failure :  function() {  
                                            Ext.Msg.alert("系统提示", "文件上传失败！");  
                                        }  
                                    });  
                                }else{  
                                    Ext.Msg.alert("系统提示","请选择文件后再上传！");  
                                }  
                            }  
                        },  
                        {  
                            text : '取消',  
                            handler : function(){  
                                winFielUpload.hide();  
                            }  
                        }  
                    ]  
                });  
				
				
                var winFielUpload = new Ext.Window({  
                    id : 'win',  
                    title : '文件上传',  
                    //****renderTo : 'divWindow',//对于window不要使用renderTo属性，只需要调用show方法就可以显示，添加此属性会难以控制其位置  
                    width : 350,  
                    height : 100,  
                    layout : 'fit',  
                    autoDestory : true,  
                    modal : true,  
                    closeAction : 'hide',  
                    items : [  
                        fpFileUpload  
                    ]  
                });  
                window.winFielUpload = winFielUpload; 
				
                //showWindow();
			});
			 
            function showWindow(){  
                    winFielUpload.show();  
            };
            
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

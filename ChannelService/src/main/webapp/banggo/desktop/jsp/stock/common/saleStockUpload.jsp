<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
%>

<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>文件上传</title>
		<script type="text/javascript" src="<%=path%>/banggo/js/common/ext.js"></script>
		<script type="text/javascript" src="<%=path%>/banggo/js/common/common.js"></script>
		<script>
			var path = "<%=path%>";
			var selectform;
			var state = getQueryString("state");
			var code = getQueryString("code");
			var type = getQueryString("type");
			Ext.onReady(function() {
				Ext.QuickTips.init();
				setResize();
				
				var fromItem = [];
				var channelComboBox;
				if (code) {
	    			// 渠道下拉框对象
	    			channelComboBox = getChannelCombox('渠道', '');
					
					var item = {xtype: 'compositefield',
                		items: [
							channelComboBox
                		]
            		}
					fromItem.push(item);
				}
				
				var upItem = {xtype: 'compositefield',
               		items: [
						{xtype:"field",id:'upFile',name:'upFile',inputType : "file",fieldLabel:"请选择"},
						{xtype:"textfield",name:"state",id:"state",hidden:true},
						{xtype:"textfield",name:"channelCode",id:"channelCode",hidden:true}
               		]
            	}
				
				fromItem.push(upItem);
				
				selectform = new Ext.form.FormPanel({
					frame:true,
					renderTo:"condition",
					height: Ext.get("condition").getHeight(),
					labelWidth:60,
                	labelAlign:'right',
                	fileUpload:true,
					items: fromItem,
					buttons:[
						{text:"上传",handler:function(){
					   		upload();
						}},
						{text:"下载模版",handler:function(){
							download();
						}}
					],
					buttonAlign:"right"
				});
				
				function download() {
					var postForm = document.createElement("form");//表单对象   
            		postForm.method="post" ;   
            		postForm.action = path + "/scstock/downLoadTemplate.htm";
            		
            		var input = document.createElement("input");  //email input 
        			input.setAttribute("name", 'title');  
            		
            		var value = "商品11位码";
            		if (type == 1) {
            			value += ",强占库存调整";
            		} else if (type == 2) {
            			value += ",虚拟库存调整";
            		} else if (type == 3) {
            			value += ",人工设置库存";
            		}
        			input.setAttribute("value", value);
        			postForm.appendChild(input);  
        			
            		document.body.appendChild(postForm);   
            		postForm.submit() ;   
            		document.body.removeChild(postForm);
				}
				
				function upload() {
					// 判断
					var text = Ext.fly("upFile").getValue();
	           		if (!text) {
	                 	createAlert('提示信息', "请选择上传文件!");
	                	return false;
	              	}
	              	
	              	var index = text.lastIndexOf('.');
					if (index != -1) {
						var tempname = text.substring(index);
						if (tempname != ".csv") {
							createAlert('提示信息',"上传文件不是csv格式!");
							return false;
						}	
					} else {
						createAlert('提示信息', "请选择上传文件!");
	                	return false;
					}
					
					if (code) {
						var channelCode = channelComboBox.getValue();
						if (!channelCode) {
							createAlert('提示信息', "请选择渠道!");
		                	return false;
						}
						document.getElementById("channelCode").value = channelCode;
					}
					
					/*Ext.MessageBox.show({
						title:"正在上传文件",
						widht:240,
						progress:true,
						closable:false,
						buttons:{cancel:'取消'}
					})*/
					document.getElementById("state").value = state;
					var url = path + '/scstock/doUploadFile.htm';
					doSubmit(url, doSuccess);
					
					/*var i = 0;
					var timer = setInterval(function () {
						createAjax(path+'/base/processController.htm', function(response,opts){
							var res = Ext.util.JSON.decode(response.responseText);
							
							if (res.success == true) {
								if (res.finished == true) {
									clearInterval(timer);
									Ext.MessageBox.updateProgress(1, '1', '上传完成');
									Ext.MessageBox.hide();
								} else {
									Ext.MessageBox.updateProgress(res.percent, res.percent);
								}
							}
						}, function (response,opts) {
							clearInterval(timer);
							createAlert("提示信息", "请求失败!");
						});
					},500);*/
				}
				
				function doSuccess(frm, action) {
               		var res = action.result.success;
               		if (res == true) {
               			var data = action.result.data;
               			parent.showFileWindow(data);
               			parent.fileWindow.close();
               		}
				} 
	           	
	           	function setResize() {
					var clientHeight = document.body.clientHeight;
	        		Ext.get("condition").setHeight(clientHeight);
				}
				
				window.onresize=function(){
					setResize();
	        	}
			});
		</script>
	</head>
	
	<body>
    	<div id="condition" style="width:100%;"></div>
  	</body>
</html>

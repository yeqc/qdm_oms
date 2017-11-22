<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/page/page.jsp"%>
<html>
	<head>
		<title>更新渠道token</title>
	</head>
	<script type="text/javascript">
	var shopCode = getGlobalValue("shopCode");
	var channelCode = getGlobalValue("channelCode");
	var basePath = '<%=basePath%>';
	Ext.onReady(function() {
		var clientHeight = document.body.clientHeight;
		var clientWidth = document.body.clientWidth;
		var url = basePath + "/custom/auth/initAuth?method=get";
		Ext.Ajax.request( {
			waitMsg : '请稍等.....',
			url : url,
			method : 'post',
			timeout: 30000,
			params : {"shopCode": shopCode,"channelCode":channelCode},
			success : function(response) {
				var respText = Ext.util.JSON.decode(response.responseText);
				if(respText.isok){ //成功
					//alertMsg("结果", respText.data);
					$("#authFrame").attr("src",respText.data);
				}else{
					alertMsg("结果", respText.data);
				}
			},
			failure : function(response, options) {
				var respText = Ext.util.JSON.decode(response.responseText); 
				alert("未审核失败");
			}
		});
		
	})
	</script>
<body>
	<div>
		<iframe id="authFrame" width="100%" height="500" src=""></iframe>
	</div>
</body>
</html>
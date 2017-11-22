<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/page/page.jsp"%>
<%@ include file="/page/script.jsp"%>
<html>
	<head>
		<title>更新渠道token</title>
	</head>
	<script type="text/javascript" src="<%=basePath%>js/jquery/jquery.form.js"></script >
	<script type="text/javascript" src="<%=basePath%>js/jquery/jquery-1.6.2.js"></script >
	
	<script type="text/javascript">
		var authUrl = "${result.data.authUrl}";
		var code = "${result.data.code}";
		var redirectUri = "${result.data.redirectUri}";
		var clientId = "${result.data.clientId}";
		var clientSecret = "${result.data.clientSecret}";
		var state = "${result.data.state}";
		var view = "${result.data.view}";
		$(function() {
			$("#authCommitForm").submit(function(){
				$(this).ajaxSubmit({
					type:"post",  //提交方式  
					dataType:"jsonp", //数据类型  
					url: authUrl, //请求url
					data: {"code":code,"grant_type":"authorization_code","redirectUri":redirectUri,"clientId":clientId,"clientSecret":clientSecret,"state":state,"view":view},
					success:function(data){ //提交成功的回调函数  
						alert(data);
					},
					error : function(XmlHttpRequest, textStatus, errorThrown){
						alert(textStatus);
					}
				});
				return false; //不刷新页面  
			});
		});
	</script>
<body>
	<form action="${result.data.authUrl}" id="authCommitForm" method="post" title="更新token表单">
		<%-- <input type="hidden" name="client_id" value="${result.data.clientId}"/><br>
		<input type="hidden" name="client_secret" value="${result.data.clientSecret}"/><br>
		<label>grant_type :</label> <input type="text" name="grant_type" value="authorization_code" /><br>
		<label>code :</label> <input type="text" name="code" value="${result.data.code}"/><br>
		<label>redirect_uri :</label> <input type="text" name="redirect_uri" value="${result.data.redirectUri}"/><br>
		<label>state :</label> <input type="text" name="state" value="${result.data.state}"/><br>
		<label>view :</label> <input type="text" name="view" value="${result.data.view}"/><br> --%>
		<input type="submit" value="提交">
	</form>
</body>
	<%-- <script type="text/javascript" src="<%=basePath%>page/auth/commitAuth.js"></script > --%>
</html>
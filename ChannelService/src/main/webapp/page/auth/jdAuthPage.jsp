<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/page/page.jsp"%>
<%@ include file="/page/script.jsp" %>
<html>
	<head>
		<title>更新渠道token</title>
	</head>
<body>
	<div>
		<form id="authForm" action="https://auth.360buy.com/oauth/token">
			<c:forEach var="shopAuth" items="${shopAuth}">
				<input type="hidden" name="${shopAuth.name }" value="${shopAuth.value}}">
			</c:forEach>
		</form>
	</div>
</body>

<script type="text/javascript">
	
</script>
</html>
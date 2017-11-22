<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/page/page.jsp"%>
<html>
	<head>
		<title>更新渠道token</title>
	</head>
	<script type="text/javascript">
	</script>
<body>
	<div>
		<c:if test="${result.isok == 'true'}">
			<h3>${result.message}</h3><br>
			<font color="red">请告知渠道负责人渠道店铺token更新信息！</font>
		</c:if>
		<c:if test="${result.isok == 'false'}">
			<h3>${result.message}</h3>
			<font color="red">请联系开发人员解决！</font>
		</c:if>
	</div>
</body>
</html>
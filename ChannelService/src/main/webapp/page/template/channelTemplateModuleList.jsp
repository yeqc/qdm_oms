<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/page/page.jsp"%>

<html>
	<head>
		<title>自定义模块管理</title>
	</head>
	<body>
		<div id='channelTemplateModule-grid'></div>
	</body>
	
	<script type="text/javascript">
	var schedingType = "";
	var supplierAction = basePath+"custom/channelTemplate/channelTemplateModuleList.spmvc";
	var basePath = '<%=basePath%>'; 
	</script>
	<script type="text/javascript" src="<%=basePath%>/page/template/channelTemplateModuleList.js"></script>
</html>
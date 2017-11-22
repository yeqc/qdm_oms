<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/page/page.jsp"%>

<html>
	<head>
		<title>店铺商品信息管理</title>
	</head>
	<body>
		<div id='channelGoods-panel'></div>
	</body>
	
	<script type="text/javascript">
	var schedingType = "";
	var supplierAction = basePath+"/custom/channelGoodsInfo/channelGoodsPage.spmvc";
	var basePath = '<%=basePath%>'; 
	</script>
	<script type="text/javascript" src="<%=basePath%>/page/channelGoods/channelGoodsList.js"></script>
</html>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/page/page.jsp"%>

<html>
	<head>
		<title>店铺经营商品信息管理</title>
	</head>
	<body>
		<div id='channelGoods-panel2'></div>
	</body>
	
	<script type="text/javascript">
	var schedingType = "";
	var supplierAction = basePath+"/custom/channelGoodsInfo/channelGoodsPage2.spmvc";
	var basePath = '<%=basePath%>'; 
	</script>
	<script type="text/javascript" src="<%=basePath%>/page/channelGoods/channelGoodsList2.js"></script>
</html>
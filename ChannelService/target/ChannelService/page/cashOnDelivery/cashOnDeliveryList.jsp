<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/page/page.jsp"%>
<html>
<head>

<title>商品货到付款</title>
</head>
<body>
	<div id='cashOnDelivery-grid'></div>
	<input id="shopCodeStr" type="hidden" />
</body>

<script type="text/javascript">
	var supplierAction = basePath+"custom/shopGoods/shopGoodsUpDownList.spmvc";
	var basePath = '<%=basePath%>';
</script>
<script type="text/javascript"
	src="<%=basePath%>/page/cashOnDelivery/cashOnDeliveryList.js">
	
</script>
</html>
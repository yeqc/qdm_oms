<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/page/page.jsp"%>
<html>
	<head>

		<title>店铺商品价格批量调整</title>

	</head>
	<body>
      <div id='shopGoodsPrice-grid'></div>
	</body>
	
	<script type="text/javascript">
	var schedingType = "";		
	//var supplierAction = basePath+"custom/shopGoods/shopGoodsUpDownList.spmvc";
	var basePath = '<%=basePath%>'; 
	</script>
	<script type="text/javascript" src="<%=basePath%>/page/shopGoodsPrice/shopGoodsPrice.js">
	
    </script>
</html>
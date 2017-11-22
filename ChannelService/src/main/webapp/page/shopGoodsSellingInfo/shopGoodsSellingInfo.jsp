<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/page/page.jsp"%>
<html>
	<head>

		<title>店铺商品卖点管理</title>

	</head>
	<body>
      <div id='shopGoodsSellingInfo-grid'></div>
	</body>
	
	<script type="text/javascript">
	var schedingType = "";		
	//var supplierAction = basePath+"custom/shopGoods/shopGoodsUpDownList.spmvc";
	var basePath = '<%=basePath%>'; 
	</script>
	<script type="text/javascript" src="<%=basePath%>/page/shopGoodsSellingInfo/shopGoodsSellingInfo.js">
	
    </script>
</html>
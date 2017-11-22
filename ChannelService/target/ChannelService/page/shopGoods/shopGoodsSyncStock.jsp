<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/page/page.jsp"%>
<html>
	<head>

		<title>店铺经营商品信息管理</title>

	</head>
	<body>
      <div id='shopGoods-grid2'></div>
       <input id="shopCodeStr" type="hidden" />
	</body>
	
	<script type="text/javascript">
	var supplierAction = basePath+"custom/shopGoods/shopGoodsUpDownList.spmvc";
	var basePath = '<%=basePath%>'; 
	</script>
	<script type="text/javascript" src="<%=basePath%>/page/shopGoods/shopGoodsSyncStock.js">
	
    </script>
</html>
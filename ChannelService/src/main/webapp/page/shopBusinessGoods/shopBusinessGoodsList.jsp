<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/page/page.jsp"%>
<html>
	<head>

		<title>店铺经营商品</title>

	</head>
	<body>
      <div id='shopBusinessGoodsList-grid'></div>
	</body>
	
	<script type="text/javascript">
	var schedingType = "";		
	var supplierAction = "";
	var basePath = '<%=basePath%>'; 
	</script>
	<script type="text/javascript" src="<%=basePath%>/page/shopBusinessGoods/shopBusinessGoodsList.js">
	
    </script>
</html>
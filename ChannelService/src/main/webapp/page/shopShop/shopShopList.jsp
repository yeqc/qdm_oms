<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/page/page.jsp"%>
<html>
	<head>

		<title>集团店铺管理</title>

	</head>
	<body>
      <div id='shopShop-grid'></div>
	</body>
	
	<script type="text/javascript">
	var schedingType = "";		
	var supplierAction = "";
	var basePath = '<%=basePath%>'; 
	</script>
	<script type="text/javascript" src="<%=basePath%>/page/shopShop/shopShopList.js">
	
    </script>
</html>
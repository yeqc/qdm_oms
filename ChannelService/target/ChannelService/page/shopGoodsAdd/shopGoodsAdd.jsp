<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/page/page.jsp"%>
<html>
	<head>

		<title>店铺商品添加管理</title>

	</head>
	<body>
      <div id='shopGoodsAdd-grid'></div>
	</body>
	
	<script type="text/javascript">
	var supplierAction = basePath+"custom/shopGoodsAdd/shopGoodsAddPage.spmvc";
	var basePath = '<%=basePath%>'; 
	</script>
	<script type="text/javascript" src="<%=basePath%>/page/shopGoodsAdd/shopGoodsAdd.js">
	
    </script>
</html>
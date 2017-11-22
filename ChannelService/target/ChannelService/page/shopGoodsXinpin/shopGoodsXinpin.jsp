<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/page/page.jsp"%>
<html>
	<head>
		<title>店铺商品新品状态管理</title>
	</head>
	<body>
      <div id='shopGoodsXinpin-grid'></div>
       <input id="shopCodeStr" type="hidden" />
	</body>
	
	<script type="text/javascript">
//	var supplierAction = basePath+"custom/shopGoods/shopGoodsUpDownList.spmvc";
	var basePath = '<%=basePath%>'; 
	</script>
	<script type="text/javascript" src="<%=basePath%>/page/shopGoodsXinpin/shopGoodsXinpin.js">
	
    </script>
</html>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/page/page.jsp"%>
<html>
	<head>

		<title>外部渠道店铺管理</title>

	</head>
	<body>
      <div id='coupon-grid'></div>
	</body>
	
	<script type="text/javascript">
	var schedingType = "";		
	var supplierAction = "";
	var basePath = '<%=basePath%>'; 
	</script>
	<script type="text/javascript" src="<%=basePath%>/page/channelShop/outChannelShopList.js">
	
    </script>
</html>
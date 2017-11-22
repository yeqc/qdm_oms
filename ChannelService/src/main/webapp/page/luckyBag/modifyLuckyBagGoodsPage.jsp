<%@page language="java" pageEncoding="UTF-8"%>
<%@ include file="/page/page.jsp"%>
<%@ include file="/page/script.jsp"%>

<script type="text/javascript" src="${pageContext.request.contextPath }/page/luckyBag/modifyLuckyBagGoodsPage.js"></script>

<script type="text/javascript">
	var basePath = '${pageContext.request.contextPath }';
	var luckyBagSku = '${luckyBagSku}';
	var warehouseId = '${warehouseId}';
</script>

</head>
<body>
	<div id="modify_luckybag_goods_panel_div"></div>
	<div id="modify_luckybag_goods_grid_div"></div>
</body>
</html>
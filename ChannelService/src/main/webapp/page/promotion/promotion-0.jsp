<%@page language="java" pageEncoding="UTF-8"%>
<%@ include file="/page/page.jsp"%>
<%@ include file="/page/script.jsp"%>

<script type="text/javascript" src="${pageContext.request.contextPath }/page/promotion/promotion-0.js"></script>

<script type="text/javascript">
	var basePath = '${pageContext.request.contextPath }';
	var promotionsInfo = '${promotionsInfo}';
	var promotionsLimitMoneyList = '${promotionsLimitMoneyList}';
	var giftsGoodsListList = '${giftsGoodsListList}';
</script>

</head>
<body>
	<div id="promotion_form"></div>
	<div id="data_panel"></div>
</body>
</html>

<%@page language="java" pageEncoding="UTF-8"%>
<%@ include file="/page/page.jsp"%>
<%@ include file="/page/script.jsp"%>

<script type="text/javascript" src="${pageContext.request.contextPath }/page/luckyBag/modifyLuckyBagPage.js"></script>

<script type="text/javascript">
	var basePath = '${pageContext.request.contextPath }';
	var luckyBagInfo = '${luckyBagInfo}';
	var luckyBagSubsetList = '${luckyBagSubsetList}';
</script>

</head>
<body>
	<div id="modify_luckybag_panel_div"></div>
</body>
</html>
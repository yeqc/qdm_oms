<%@page language="java" pageEncoding="UTF-8"%>
<%@ include file="/page/page.jsp"%>
<%@ include file="/page/script.jsp"%>

<script type="text/javascript"
	src="${pageContext.request.contextPath }/page/promotion/groupgoods-add.js"></script>

<script type="text/javascript">
	var basePath = '${pageContext.request.contextPath }';
	var groupGoods = '${groupGoods}';
	var groupGoodsListList = '${groupGoodsListList}';
</script>

</head>
<body>
	<div id="add_group_goods_panel"></div>
</body>
</html>

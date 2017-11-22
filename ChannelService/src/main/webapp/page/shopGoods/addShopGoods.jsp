<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/page/page.jsp"%>
<%@ include file="/page/script.jsp"%>

<script type="text/javascript"
	src="<%=basePath%>/page/shopGoods/addShopGoods.js"></script>
<script type="text/javascript">
	var basePath = '${pageContext.request.contextPath }';
	var channelGoodsTicket = '${channelGoodsTicket}';
	var ticketInfo = '${ticketInfo}';
</script>
<body>
  
	<div id='channel_good_form'></div>
</body>

</html>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/page/page.jsp"%>
<%@ include file="/page/script.jsp"%>
<html>
	<head>

		<title>门店商品关联单据</title>

	</head>
	<body>
 <div id='shopGoodsBandingStore-grid'></div>
 
   <input type="hidden" id="addNewShopCode" value="${addNewCgtVo.shopCode}" />
   <input type="hidden" id="addNewTicketCode" value="${addNewCgtVo.ticketCode}" />
   <input type="hidden" id="addNewExcetTime" value="${addNewCgtVo.formatExcetTime}" />
   <input type="hidden" id="addNewIsTiming" value="${addNewCgtVo.isTiming}" />
   <input type="hidden" id="addNewTicketStatus" value="${addNewCgtVo.ticketStatus}" />
   <input type="hidden" id="addNewChannelCode" value="${addNewCgtVo.channelCode}" />
   </body>
	
	<script type="text/javascript">
	var basePath = '<%=basePath%>'; 
	</script>
	<script type="text/javascript" src="<%=basePath%>/page/shopGoods/addShopGoodsBandingStore.js">
	
    </script>
</html>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/page/page.jsp"%>
<%@ include file="/page/script.jsp"%>
<html>
	<head>
		<title>商品信息修改调整单据</title>
	</head>
	<body>
 <div id='shopGoodsInfo-grid'></div>
   <input type="hidden" id="addNewShopCode" value="${addNewCgtVo.shopCode}" />
   <input type="hidden" id="addNewTicketCode" value="${addNewCgtVo.ticketCode}" />
   <input type="hidden" id="addNewExcetTime" value="${addNewCgtVo.formatExcetTime}" />
   <input type="hidden" id="addNewIsTiming" value="${addNewCgtVo.isTiming}" />
   <input type="hidden" id="addNewTicketStatus" value="${addNewCgtVo.ticketStatus}" />
   <input type="hidden" id="addNewChannelCode" value="${addNewCgtVo.channelCode}" />
   <input type="hidden" id="addNewTicketType" value="${addNewCgtVo.ticketType}" />
   </body>
	
	<script type="text/javascript">
	var basePath = '<%=basePath%>'; 
	</script>
	<script type="text/javascript" src="<%=basePath%>/page/shopGoods/addShopGoodsInfo.js"></script>
</html>
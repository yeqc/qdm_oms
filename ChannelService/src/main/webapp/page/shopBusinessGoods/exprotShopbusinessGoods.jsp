<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/page/page.jsp"%>
<%@ include file="/page/script.jsp"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<%@page import="com.work.shop.bean.ChannelShop"%>
<%
  // ChannelShop  scs = (ChannelShop) request.getAttribute("scs");
  // System.out.println(scs.getId());
 //  request.setAttribute("scs",scs);
   //System.out.println("------------------------");
 %>
<div id='exprotShopbusinessGoods-grid'></div>
<input type="hidden" id="hShopCode"  value="${scs.shopCode}" />
<input type="hidden" id="hChannelCode"   value="${scs.channelCode}"/>
<input type="hidden" id="hStatus"   value="${scs.status}"/>

<script type="text/javascript"
	src="<%=basePath%>/page/shopBusinessGoods/exprotShopbusinessGoods.js">

</script>
<script type="text/javascript">
var schedingType = "";		
var basePath = '<%=basePath%>'; 
</script>

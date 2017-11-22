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
   
 %>
<div id='addInterfacePropertiesForm-grid'></div>
		
<input type="hidden" id="hId" name="hId" value="${ip.id}" />
<input type="hidden" id="hProName"  value="${ip.proName}" />
<input type="hidden" id="hProValue"  value="${ip.proValue}"/>
<input type="hidden" id="hChannelCode"   value="${ip.channelCode}"/>
<input type="hidden" id="hShopCode"  value="${ip.shopCode}"/>

<script type="text/javascript"
	src="<%=basePath%>/page/interfaceProperties/addInterfaceProperties.js">

</script>
<script type="text/javascript">
var schedingType = "";		
var basePath = '<%=basePath%>'; 
</script>


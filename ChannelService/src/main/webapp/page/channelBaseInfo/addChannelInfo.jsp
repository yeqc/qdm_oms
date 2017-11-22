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
<div id='addChannelInfo-grid'></div>

<input type="hidden" id="hId"  value="${channelInfo.id}" />
<input type="hidden" id="hChannelCode"  value="${channelInfo.chanelCode}" />
<input type="hidden" id="hChannelTitle" value="${channelInfo.channelTitle}"/>
<input type="hidden" id="hChannelStatus"  value="${channelInfo.channelStatus}"/>
<input type="hidden" id="hChannelType"  value="${channelInfo.channelType}"/>
<input type="hidden" id="hBackup"  value="${channelInfo.backup}"/>


<script type="text/javascript"
	src="<%=basePath%>/page/channelBaseInfo/addChannelInfo.js">

</script>
<script type="text/javascript">
var schedingType = "";		
var basePath = '<%=basePath%>'; 
</script>


<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/page/page.jsp"%>
<%@ include file="/page/script.jsp"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<%@page import="com.work.shop.bean.ChannelShop"%>

<div id='updateChannelArea-grid'></div>

<input type="hidden" id="hId"  value="${cArea.id}" />
<input type="hidden" id="hAreaId"  value="${cArea.areaId}" />
<input type="hidden" id="hAreaType"  value="${cArea.areaType}" />
<input type="hidden" id="hAreaName" value="${cArea.areaName}"/>
<input type="hidden" id="hOsRegionName"  value="${cArea.osRegionName}"/>
<input type="hidden" id="hOsRegionId"  value="${cArea.osRegionId}"/>
<input type="hidden" id="hchannelCode"  value="${cArea.channelCode}"/>
<input type="hidden" id="hMatchType"  value="${cArea.matchType}" />
<input type="hidden" id="hChParentName"  value="${cArea.chParentName}" />
<script type="text/javascript"
	src="<%=basePath%>/page/channelArea/updateChannelArea.js">
</script>
<script type="text/javascript">
var schedingType = "";
var basePath = '<%=basePath%>'; 
</script>


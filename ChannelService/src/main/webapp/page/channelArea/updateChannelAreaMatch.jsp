<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/page/page.jsp"%>
<%@ include file="/page/script.jsp"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<%@page import="com.work.shop.bean.ChannelShop"%>

<div id='updateChannelAreaMatch-grid'></div>

<input type="hidden" id="hId"  value="${sRegionMatch.id}" />
<input type="hidden" id="hOldRegionId"  value="${sRegionMatch.oldRegionId}"/>
<input type="hidden" id="hOldRegionName"  value="${sRegionMatch.oldRegionName}"/>
<input type="hidden" id="hOldRegionType"  value="${sRegionMatch.oldRegionType}" />
<input type="hidden" id="hNewAreaId"  value="${sRegionMatch.newAreaId}" />
<input type="hidden" id="hNewAreaName" value="${sRegionMatch.newAreaName}"/>
<input type="hidden" id="hMatchType"  value="${sRegionMatch.matchType}"/>
<input type="hidden" id="hPareaCode"  value="${sRegionMatch.pareaCode}" />
<input type="hidden" id="hRelateOsArea"  value="${sRegionMatch.relateOsArea}" />
<input type="hidden" id="hParentName"  value="${sRegionMatch.parentName}" />

<script type="text/javascript"
	src="<%=basePath%>/page/channelArea/updateChannelAreaMatch.js">
</script>
<script type="text/javascript">
var schedingType = "";
var basePath = '<%=basePath%>'; 
</script>


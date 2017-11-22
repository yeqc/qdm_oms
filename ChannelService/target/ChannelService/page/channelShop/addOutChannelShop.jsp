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
<div id='addcouponForm-grid'></div>
<input type="hidden" id="hId" name="hId" value="${scs.id}" />
<input type="hidden" id="hShopCode"  value="${scs.shopCode}" />
<input type="hidden" id="hParentShopCode"  value="${scs.parentShopCode}" />
<input type="hidden" id="hShopTitle"  value="${scs.shopTitle}"/>
<input type="hidden" id="hChannelCode"   value="${scs.channelCode}"/>
<input type="hidden" id="hLogisticsCallback"  value="${scs.logisticsCallback}"/>
<input type="hidden" id="hShopAuthentication"   value="${scs.shopAuthentication}"/>
<input type="hidden" id="hShopType"   value="${scs.shopType}"/>
<input type="hidden" id="hBackup" value="${scs.backup}" />
<input type="hidden" id="hshopImg" value="${scs.shopImg}" />
<input type="hidden" id="hLogisticsCallback" value="${scs.logisticsCallback}" />
<input type="hidden" id="hshopChannel" value="${scs.shopChannel}" />
<input type="hidden" id="neWShopCode"  value="${channelShop.shopCode}" />
<input type="hidden" id="hshopStatus"  value="${scs.shopStatus}" />
<input type="hidden" id="hIsSyn"  value="${scs.isSyn}" />
<input type="hidden" id="hReturnDepot"  value="${scs.returnDepot}" />
<input type="hidden" id="hMarketTitle" value="${scs.marketTitle }" />

<input type="hidden" id="hmsgSendType" value="${scs.msgSendType}" />
<input type="hidden" id="hmsgTemplateCode" value="${scs.msgTemplateCode}" />
<input type="hidden" id="hpreSaleShopCode" value="${scs.preSaleShopCode}" />

<input type="hidden" id="osExpiresTime"  value="${scs.formatExpiresTime}" />
<input type="hidden" id="osChannelId"  value="${osci.channelId}" />
<input type="hidden" id="osNickName"  value="${osci.nickName}" />

<script type="text/javascript"
	src="<%=basePath%>/page/channelShop/addOutChannelShop.js">

</script>
<script type="text/javascript">
var schedingType = "";		
var basePath = '<%=basePath%>'; 
</script>
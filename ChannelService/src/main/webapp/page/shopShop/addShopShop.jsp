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
<div id='addShopShop-grid'></div>

<input type="hidden" id="hId" name="hId" value="${scs.id}" />
<input type="hidden" id="hShopCode" name="hShopCode" value="${scs.shopCode}" />
<input type="hidden" id="hparentShopCode" name="hShopCode" value="${scs.parentShopCode}" />
<input type="hidden" id="hShopTitle" name="hShopTitle"  value="${scs.shopTitle}"/>
<input type="hidden" id="erpShopCode" name="erpShopCode"  value="${scs.erpShopCode}"/>
<input type="hidden" id="shopType" name="shopType"  value="${scs.shopType}"/>
<input type="hidden" id="marketTitle" name="marketTitle"  value="${scs.marketTitle}"/>
<input type="hidden" id="shopLinkman" name="shopLinkman"  value="${scs.shopLinkman}"/>
<input type="hidden" id="shopTel" name="shopTel"  value="${scs.shopTel}"/>
<input type="hidden" id="shopAddress" name="shopAddress"  value="${scs.shopAddress}"/>
<input type="hidden" id="hbackup" name="backup"  value="${scs.backup}"/>
<input type="hidden" id="hChannelCode"   value="${scs.channelCode}"/>
<input type="hidden" id="neWShopCode"  value="${channelShop.shopCode}" />
<input type="hidden" id="hshopStatus"  value="${scs.shopStatus}" />

<input type="hidden" id="hmsgSendType" value="${scs.msgSendType}" />
<input type="hidden" id="hmsgTemplateCode" value="${scs.msgTemplateCode}" />

<input type="hidden" id="osChannelId"  value="${osci.channelId}" />
<input type="hidden" id="osNickName"  value="${osci.nickName}" />

<script type="text/javascript"
	src="<%=basePath%>/page/shopShop/addShopShop.js">

</script>
<script type="text/javascript">
var schedingType = "";		
var basePath = '<%=basePath%>'; 
</script>


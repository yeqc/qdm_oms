<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/page/page.jsp"%>
<%@ include file="/page/script.jsp"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<script type="text/javascript">
var basePath = '<%=basePath%>'; 
</script>
<script type="text/javascript"
	src="<%=basePath%>/page/shopBusinessGoods/exprotShopGoodsFile.js">
</script>
<input type="hidden" id="fileType" value="${type}"/>
<input type="hidden" id="shopCode" value="${shopCode}"/>
选择时间：<input type="text" id="fileTime" readonly="true" onClick="WdatePicker({dateFmt:'yyyy-MM-dd'})"/>
<button type="button" onclick="selectFileName();">确定</button>
<div id="filediv"></div>


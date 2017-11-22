<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/page/page.jsp"%>
<%@ include file="/page/script.jsp"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<%@page import="com.work.shop.bean.ChannelShop"%>

<div id='addChannelArea-grid'></div>
<div align="center"><a href="<%=basePath%>/page/excelModel/sysArea.xls"><font color="red">导入地区映射模版下载</font></a></div>
<script type="text/javascript"
	src="<%=basePath%>/page/channelArea/addChannelArea.js">
</script>
<script type="text/javascript">
var schedingType = "";		
var basePath = '<%=basePath%>'; 
</script>


<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" pageEncoding="utf-8"%>
<%
	String path = request.getContextPath();
%>

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>同步异常日志</title>
<style>
.x-form-display-field-left {
	padding-top: 2px;
	text-align: left;
}
</style>
<script type="text/javascript" src="<%=path%>/banggo/js/common/ext.js"></script>
<script type="text/javascript"
	src="<%=path%>/banggo/js/common/common.js"></script>
<!-- 当前页面相关的JS -->
<script type="text/javascript"
	src="<%=path%>/banggo/desktop/jsp/errorlog/errorLog.js"></script>
</head>

<body>
	<div id="condition" style="width: 100%;"></div>
	<div class="clear"></div>
	<div id="grid" style="width: 100%"></div>
</body>
</html>

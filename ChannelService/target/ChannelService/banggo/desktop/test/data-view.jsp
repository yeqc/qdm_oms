<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<%String path = request.getContextPath();%>
	<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	    <title>DataView Example</title>
	    <script src="<%=path%>/banggo/js/common/ext.js"></script>
	    <script>
	    	var path = "<%=path%>";
	    </script>
	    <link rel="stylesheet" type="text/css" href="data-view.css" />
	</head>
<body>
<center>
<div id="img-div">
</div>
<div id="button-div" style= "padding-top:10px;padding-right:20px;">
</div>
</center>
<script type="text/javascript" src="../js/DataView-more-test.js"></script>
<script type="text/javascript" src="data-view.js"></script>
</body>
</html>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%String path = request.getContextPath();%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>欢迎使用统一渠道系统</title>
		<script type="text/javascript" src="<%=path%>/banggo/js/common/ext.js"></script>
		<script type="text/javascript">
			var path = "<%=path%>";
			importJsDesPath("js/Menu.js");
			importJsDesPath("js/treePanel.js");
			importJs("TabCloseMenu.js");
		</script>
	</head>
<body>
<div id="main" style="width:100%;height:100%;"></div>

<script type="text/javascript">
	var type = getParam("type");
	var flag = getParam("flag");
	
	var auth = parent.auth;
	
	Ext.onReady(function(){  
	    var panel = createTreePanel(type);
	    if (Ext.isIE) {
		    setTimeout(function(){
	    		panel.show();                                 
			}, 500);
		} 
	}); 
</script>
<br></body>

</html>
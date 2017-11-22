<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@page import="com.work.shop.united.client.filter.config.Config"%>
<%@page import="com.work.shop.united.client.facade.UserStore"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<%
			String path = request.getContextPath();
		
			String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
		
		    String  exitPath =  Config.getAuthCenterUrl();
		    
			String userName = UserStore.get(request).getUserName();
		%>
		<title>统一渠道系统</title>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<link rel="shortcut icon" href="<%=path%>/banggo/images/ico.ico">
		 <script type="text/javascript">
			var path = '<%=path%>';
			var basePath = '<%=basePath%>';
		 </script>
		<script type="text/javascript" src="<%=path%>/banggo/js/common/ext.js"></script>
		<script type="text/javascript" src="<%=path%>/js/jquery/jquery-1.6.2.min.js"></script>
	    <script type="text/javascript">
	 		// 路径
		
	    	importDesktopCss("desktop.css");
	    	var exitPath = '<%=exitPath%>';
	    	var userName = '<%=userName%>';
	    
	    </script>
	      
	    <style type="text/css">
	    
	    #x-shortcuts dt {
    clear: both;
    display: block;
    float: left;
    font: 12px tahoma,arial,verdana,sans-serif;
    height: 90px;
    margin: 0px 0 0 2px;
    padding-top: 10px;
    text-align: center;
    width: 82px;
}

#x-shortcuts dt a {
    color: #000000;
    display: block;
    text-decoration: none;
    width: 90px;
}
	    
	    
	    </style>
	</head>

	<body scroll="no">
		<form action="" method="POST" name="form">
			<input type="hidden" value="" name="flag" id="flag"/>
			<input type="hidden" value="" name="name" id="name"/>
		</form>

		<script>
	
			importJsPath("cookie.js");
	    	importJsDesPath("js/Menu.js?d="+new Date().getTime());
	    	importJsPath("right.js");
		</script>
		<div id="x-desktop">
			<div id="x-commcon">
			   	<script>
			   	
			   		write('', '<%=request.getAttribute("flag")%>');
			   	</script>
		   	</div>
		</div>
		<div id="ux-taskbar">
			<div id="ux-taskbar-start"></div>
			<div id="ux-taskbuttons-panel"></div>
			<div class="x-clear"></div>
		</div>
		<!-- DESKTOP -->
		<script>
			importJsDesPath("js/StartMenu.js");
			importJsDesPath("js/TaskBar.js");
			importJsDesPath("js/Desktop.js");
			importJsDesPath("js/App.js");
			importJsDesPath("js/Module.js");
			importJsDesPath("sample.js");
		</script>
	</body>
</html>

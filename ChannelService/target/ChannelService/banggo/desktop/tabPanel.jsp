<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%String path = request.getContextPath();%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>欢迎使用邦购LMS系统</title>

<style type="text/css">
body {
margin: 0;
padding: 0;
background: #FFFFFF;

}
</style>
<script type="text/javascript" src="<%=path%>/js/common/ext.js"></script>

<script type="text/javascript">
	var path = "<%=path%>";
	
	var auth = parent.auth;
	
	Ext.onReady(function(){  
	    Ext.QuickTips.init();
	    var items = [];
		
		if(auth.indexOf(",qxgl_fwzy,") != -1) {	
			// 查看资源					
			items.push({
	        	title: "访问资源",
	          	header:false,
	         	html : '<iframe frameborder="no" scrolling="auto" border="0" framespacing="0" id="ifr1" src="'+path+'/desktop/jsp/auth/sys_resource_ext.jsp" width="100%" height="100%"></iframe>',
	         	border:false
	 		});
 		}
 		
      	if(auth.indexOf(",qxgl_yh,") != -1) {	
	       	items.push({
	          	title: "用户",
	           	header:false,
	       		html : '<iframe frameborder="no" scrolling="auto" border="0" framespacing="0" id="ifr2" src="'+path+'/desktop/jsp/auth/sys_users_list_ext.jsp" width="100%" height="100%"></iframe>',
	          	border:false
	       	});
       	}
       	
       	if(auth.indexOf(",qxgl_js,") != -1) {
	       	items.push({
	         	title: "角色",
	           	header:false,
	        	html : '<iframe frameborder="no" scrolling="auto" border="0" framespacing="0" id="ifr3" src="'+path+'/desktop/jsp/auth/sys_acl_role_ext.jsp" width="100%" height="100%"></iframe>',
	      		border:false
	       	});
       	}
				         	
	    var view = new Ext.Viewport({
		layout: 'border',
		items: [
		{
	    	region: 'center',
	  		id:'mainTab',
	   		xtype: 'tabpanel',
	   		items:items,
	     	activeTab :0
		}]
	});
	});  
</script>
</head>
<body>
</body>
</html>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Frameset//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-frameset.dtd">
<%@ page language="java" pageEncoding="UTF-8"%>
<html>
<head>
	<title>京东美邦token获取</title>
</head>
<body>
	<a href="https://auth.360buy.com/oauth/authorize?response_type=code&client_id=DB66E40F6B2ABADBA99244AC8E8FB40B&redirect_uri=http://banggo.com&state=jd_mc" target="_blank">第一步:获取code</a>
	<br />
	<h5>第二步：填充code,提交表单</h5>
	<form action="https://auth.360buy.com/oauth/token" method="post">
	   <label>client_id :</label> <input type="text" name="client_id" value="DB66E40F6B2ABADBA99244AC8E8FB40B"/></br>
	   <label>client_secret :</label> <input type="text" name="client_secret" value="e7a28392bd8f4588965d45c88a0b18e9"/></br>
	   <label>grant_type :</label> <input type="text" name="grant_type" value="authorization_code" /></br>
	   <label>code :</label> <input type="text" name="code" value=""/></br>
	   <label>redirect_uri :</label> <input type="text" name="redirect_uri" value="http://banggo.com"/></br>
	   <label>state :</label> <input type="text" name="state" value="jd_mc"/></br>
       <input type="submit" value="提交"/>
	</form>
</body>
</html>
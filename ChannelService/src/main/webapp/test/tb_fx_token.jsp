<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Frameset//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-frameset.dtd">
<%@ page language="java" pageEncoding="UTF-8"%>
<html>
<head>
    <title>淘宝分销token获取</title>
</head>
<body>
    <a href="https://oauth.taobao.com/authorize?client_id=21814143&response_type=code&redirect_uri=http://www.banggo.com&state=mbfx&view=web" target="_blank">第一步:获取code</a>
    <br />
    <h5>第二步：填充code,提交表单</h5>
    <form action="https://oauth.taobao.com/token" method="post">
       <label>client_id :</label> <input type="text" name="client_id" value="21814143"/></br>
       <label>client_secret :</label> <input type="text" name="client_secret" value="ef5cc74a0b91656881545536628a5681"/></br>
       <label>grant_type :</label> <input type="text" name="grant_type" value="authorization_code" /></br>
       <label>code :</label> <input type="text" name="code" value=""/></br>
       <label>redirect_uri :</label> <input type="text" name="redirect_uri" value="http://www.banggo.com"/></br>
       <label>state :</label> <input type="text" name="state" value="mbfx"/></br>
       <label>view :</label> <input type="text" name="view" value="web"/></br>
       <input type="submit" value="提交"/>
    </form>
</body>
</html>
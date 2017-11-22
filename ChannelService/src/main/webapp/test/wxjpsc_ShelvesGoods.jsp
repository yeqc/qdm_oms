<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Frameset//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-frameset.dtd">
<%@ page language="java" pageEncoding="UTF-8"%>
<html>
<head>
	<title>微信精品商城上下架接口测试</title>
</head>
<body>
	
	<form action="http://10.100.5.12:8010/IInterface_GetOnSaleGoods.svc/ShelvesGoods">
	   <label>商品编码（6位码）</label>
	   <input type="text" name="goodsSn"/></br>
	   <label>上下架状态</label>
       <select name="wareStatus">
            <option value="1">上架</option>
            <option value="2">下架</option>
       </select>
       <input type="submit" value="提交"/>
	</form>

</body>
</html>
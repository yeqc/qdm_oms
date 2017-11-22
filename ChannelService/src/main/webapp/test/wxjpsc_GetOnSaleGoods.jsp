<%@ page language="java" pageEncoding="UTF-8"%>
<%@page import="com.work.shop.api.handler.WXJPApiHandler"%>
<%@page import="org.codehaus.jackson.map.ObjectMapper"%>
<%@page import="com.work.shop.api.bean.ApiResultVO"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
String goodsSn = request.getParameter("goodsSn");
String pg = request.getParameter("page");
String pageSize = request.getParameter("pageSize");
String wareStatus = request.getParameter("wareStatus");
if(pg == null || pg == ""){
%>
<html>
<head>
<title>微信精品商城商品列表接口测试</title>
</head>
<body>
	<form action="<%=basePath%>test/wxjpsc_GetOnSaleGoods.jsp" method="post">
		<label>商品6位码（不必须）</label>
		<input type="text" id="goodsSn" name="goodsSn" />
		</br>
		<label>当前页数(必须) </label>
		<input type="text" id="page" name="page" value="1"/>
		</br>
		<label>每页记录数(必须) </label>
		<input type="text" id="pageSize" name="pageSize" value="15"/>
		</br>
		<label>商品状态</label>
		<select name="wareStatus" id="wareStatus">
			<option value="1">上架</option>
			<option value="2">下架</option>
		</select>
	    <input type="submit" value="提交" id="submit"/>
	</form>
</body>
</html>
<%
} else {
    int p = Integer.valueOf(pg);
    int ps = Integer.valueOf(pageSize);
    WXJPApiHandler service = new WXJPApiHandler();
    ApiResultVO result = service.searchItemPage(goodsSn, null, p, ps, wareStatus);
    response.setCharacterEncoding("UTF-8");
    response.addHeader("CacheControl", "no-cache");
    response.addHeader("Content-Type", "application/json");
    ObjectMapper mapper = new ObjectMapper();
    try {
        String jsonStr = mapper.writeValueAsString(result);
        out.print(jsonStr);
    } catch (Exception e) {
        e.printStackTrace();
    } finally {
        out.flush();
    }
}
%>

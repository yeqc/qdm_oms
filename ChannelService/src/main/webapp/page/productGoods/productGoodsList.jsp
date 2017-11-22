<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/page/page.jsp"%>

<html>
	<head>
		<title>店铺商品信息管理</title>
		<style type="text/css">
			.x-grid3-row-alt {  
			  background-color:'#ccc'  
			 }
			 
			 .x-selectable, .x-selectable * {        
		        -moz-user-select: text! important ;        
		        -khtml-user-select: text! important ;  
		        -webkit-user-select: text!important;        
   			 }
		</style>
	</head>
	<body>
		<div id='productGoods-panel'></div>
	</body>
	
	<script type="text/javascript">
	var schedingType = "";
	var supplierAction = basePath+"/custom/channelGoodsInfo/channelGoodsPage.spmvc";
	var basePath = '<%=basePath%>'; 
	</script>
	<script type="text/javascript" src="<%=basePath%>/page/productGoods/productGoodsList.js"></script>
</html>
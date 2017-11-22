<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/page/page.jsp"%>
<%@ include file="/page/script.jsp"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<%@page import="com.work.shop.bean.ChannelShop"%>
<%
  // ChannelShop  scs = (ChannelShop) request.getAttribute("scs");
  // System.out.println(scs.getId());
 //  request.setAttribute("scs",scs);
   
 %>
<div id='channelGoodsDetails-grid'></div>
<div id="channelGoodsDetails-img" class="x-panel-mr">
	<c:forEach items="${pggList}" var="element" varStatus="s">
		<div>
			<table>
			    <tr>
			        <td>
			        	 <a target="_blank" href="${element.imgUrl}" >
					    	<img  src="${element.imgUrl}"  />
					    </a>
					        
			        <td>
			        <td>
					    <input id="img${s.index}" value="${element.imgUrl}" size="70" />
					    <button id="btn${s.index}"  onclick="copyToClipBoard(${s.index})">复制</button>
					    <br/>
					   
						<font style="font: 12px tahoma,arial,helvetica,sans-serif;">颜色：${element.colorCode}</font>			         
						<font style="font: 12px tahoma,arial,helvetica,sans-serif;">描述： ${element.imgDesc}</font>
						<font style="font: 12px tahoma,arial,helvetica,sans-serif;">排序：${element.sortOrder}</font>
			        
			        <td>
			    </tr>
			</table>
			<div><hr style="border-color:#DFE8F6;border:1;border-top:1px;"></hr></div>
			
		</div>
	</c:forEach>
</div>
<!-- 
<input type="hidden" id="hId" name="hId" value="${scs.id}" />
<input type="hidden" id="hShopCode"  value="${scs.shopCode}" />
<input type="hidden" id="hShopTitle"  value="${scs.shopTitle}"/>
<input type="hidden" id="hChannelCode"   value="${scs.channelCode}"/>
<input type="hidden" id="hLogisticsCallback"  value="${scs.logisticsCallback}"/>
<input type="hidden" id="hShopAuthentication"   value="${scs.shopAuthentication}"/>
<input type="hidden" id="hBackup" value="${scs.backup}" />
<input type="hidden" id="hshopImg" value="${scs.shopImg}" />
<input type="hidden" id="neWShopCode"  value="${channelShop.shopCode}" /> -->

<input type="hidden" id="hgoodsSn" name="hgoodsSn" value="${gp.goodsSn}" />
<input type="hidden" id="hgoodsName" name="hgoodsName" value="${gp.goodsName}" />
<input type="hidden" id="hbrandName" name="hbrandName" value="${gp.brandName}" />
<input type="hidden" id="hgoodsSn" name="hgoodsSn" value="${gp.goodsSn}" />
<input type="hidden" id="hsalePrice" name="hsalePrice" value="${gp.salePrice}" />
<input type="hidden" id="hcatName" name="hcatName" value="${gp.catName}" />

<c:forEach items="${colorList}" var="element" >
   <input type="hidden"  id="colorNameAndColorCode" name="colorNameAndColorCode" value="${element.colorName}(${element.colorCode})"  />   
</c:forEach>

<c:forEach items="${sizeList}" var="element" >
   <input type="hidden"  id="sizeNameAndSizeCode" name="sizeNameAndSizeCode" value="${element.sizeName}(${element.sizeCode})"  />   
</c:forEach>

<script type="text/javascript"
	src="<%=basePath%>/page/channelGoods/channelGoodsDetails.js">
</script>
<script type="text/javascript"
	src="<%=basePath%>/js/ZeroClipboard.js">
</script>
<script type="text/javascript">
var schedingType = "";		
var basePath = '<%=basePath%>'; 
ZeroClipboard.setMoviePath( basePath+'/js/ZeroClipboard.swf' );

function copyToClipBoard(index){
	var imgUrl = $("#img"+index).val();

	var clip = new ZeroClipboard.Client(); // 新建一个对象
	clip.setHandCursor( true );
	clip.setText(imgUrl); // 设置要复制的文本。
	clip.addEventListener( "mouseUp", function(client) {
	    alert("复制图片地址成功！");
	});
	clip.glue("btn"+index); 
}
</script>


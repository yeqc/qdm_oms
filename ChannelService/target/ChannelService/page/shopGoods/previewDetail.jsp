<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/page/page.jsp"%>
<%@ include file="/page/script.jsp"%>
<title>宝贝详情预览</title>
<head>
<meta http-equiv="content-type" content="text/html;charset=utf-8">
<script type="text/javascript">
	var basePath = '${pageContext.request.contextPath }';
</script>
<style type="text/css">

.tabs {
	float:left;
	width:100%;
	padding-top: 10px;
}
.tabs ul 
{
	list-style: none outside none;
	margin: 0;
	padding: 0;
}
.tabs ul li
{
	float: left;
	line-height: 24px;
	margin: 0;
	padding: 2px 20px 0 15px;
}
.tab-nav{
	cursor:pointer;
}
.tab-nav-action{
	cursor:pointer;
}
.tabs-body
{
	border-bottom: 1px solid #B4C9C6;
	border-left: 1px solid #B4C9C6;
	border-right: 1px solid #B4C9C6;
	float: left;
	padding: 5px 0 0;
	width: 100%;
}
</style>
</head>
<body>
	<div class="tabs">
		<ul id="tabs">
			<c:if test="${ticketType == 2 }">
				<li class="tab-nav-action"><input type="button" value="电脑版宝贝详情" /></li>
			</c:if>
			<c:if test="${ticketType == 12 }">
				<li class="tab-nav-action"><input type="button" value="移动版宝贝详情" /></li>
			</c:if>
			<li class="tab-nav"><input type="button" value="详情代码" /></li>
			<!-- <li class="tab-nav-save" style="margin-left: 600px;"><input type="button" value="详情保存" /></li> -->
		</ul>
	</div>
	<hr>
	<div id="tabs-body" class="tabs-body">
		<div style="display:block; width: 790px;" id="div1">
			<!-- 电脑版宝贝详情 -->
			<c:if test="${ticketType == 2 }">
				<c:if test="${extension.goodsDesc != null && extension.goodsDesc != ''}">
					<!-- 淘宝渠道详情 -->
					<c:if test="${channelCode == 'TB_CHANNEL_CODE'|| channelCode == 'TMALL_CHANNEL_CODE'||channelCode == 'TBFX_CHANNEL_CODE'}">
						<c:forEach items="${localModules}" var="module" varStatus="status">
							<div style="margin:0 0 0 15">${module.content}</div>
						</c:forEach>
					</c:if>
					<!-- 非淘宝渠道详情 -->
					<c:if test="${channelCode != null && channelCode != 'TB_CHANNEL_CODE' && channelCode != 'TMALL_CHANNEL_CODE'&&channelCode != 'TBFX_CHANNEL_CODE'}">
						<div style="margin:2 0 0 15">${extension.goodsDesc}</div>
					</c:if>
				</c:if>
				<c:if test="${extension.goodsDesc == null || extension.goodsDesc == ''}">
					<div style="margin:2 0 0 15; color: red; font-size: 16px;">该商品没有电脑版宝贝详情！</div>
				</c:if>
			</c:if>
			<!-- 移动版宝贝详情 -->
			<c:if test="${ticketType == 12 }">
				<c:if test="${extension.phoneGoodsDesc != null && extension.phoneGoodsDesc != ''}">
					<div style="margin:2 0 0 15">${extension.phoneGoodsDesc}</div>
				</c:if>
				<c:if test="${extension.phoneGoodsDesc == null || extension.phoneGoodsDesc == ''}">
					<div style="margin:2 0 0 15; color: red; font-size: 16px;">该商品没有移动版宝贝详情！</div>
				</c:if>
			</c:if>
		</div>
		<div style="display:none" id="div2">
			<!-- 电脑版宝贝详情 -->
			<c:if test="${ticketType == 2 }">
				<c:if test="${extension.goodsDesc != null && extension.goodsDesc != ''}">
					<!-- 淘宝渠道详情 -->
					<c:if test="${channelCode == 'TB_CHANNEL_CODE'||channelCode == 'TMALL_CHANNEL_CODE'||channelCode == 'TBFX_CHANNEL_CODE'}">
						<c:forEach items="${localModules}" var="module" varStatus="status">
							<div style='font-family: Tahoma, Geneva; font-size: 14px; color: green;'>${module.moduleName}</div>
							<textarea style="width:98%" rows="20">${module.content}</textarea>
						</c:forEach>
					</c:if>
					<!-- 非淘宝渠道详情 -->
					<c:if test="${channelCode != null &&channelCode != 'TB_CHANNEL_CODE'&&channelCode != 'TMALL_CHANNEL_CODE'&&channelCode != 'TBFX_CHANNEL_CODE'}">
						<textarea style="width:98%" rows="37">${extension.goodsDesc}</textarea> 
					</c:if>
				</c:if>
				<c:if test="${extension.goodsDesc == null || extension.goodsDesc == ''}">
					<div style="margin:2 0 0 15; color: red; font-size: 16px;">该商品没有电脑版宝贝详情！</div>
				</c:if>
			</c:if>
			<!-- 移动版宝贝详情 -->
			<c:if test="${ticketType == 12 }">
				<c:if test="${extension.phoneGoodsDesc != null && extension.phoneGoodsDesc != ''}">
					<textarea style="width:98%" rows="37">${extension.phoneGoodsDesc}</textarea> 
				</c:if>
				<c:if test="${extension.phoneGoodsDesc == null || extension.phoneGoodsDesc == ''}">
					<div style="margin:2 0 0 15; color: red; font-size: 16px;">该商品没有移动版宝贝详情！</div>
				</c:if>
			</c:if>
		</div>
	</div>
</body>
<script type="text/javascript">
	$(document).ready(function () {
		$("#tabs li").bind("click", function () {
			var index = $(this).index();
			var divs = $("#tabs-body").children();
			//$(this).parent().children("li").attr("class", "tab-nav");//将所有选项置为未选中
			//$(this).attr("class", "tab-nav-action"); //设置当前选中项为选中样式
			if (index == 0) {
				$("#div1").show();
				$("#div2").hide();
			}
			if (index == 1) {
				$("#div1").hide();
				$("#div2").show();
			}
			//divs.hide();//隐藏所有选中项内容
			//divs.eq(index).show(); //显示选中项对应内容
		});
	
	});
</script>
</html>
<%@ page language="java" pageEncoding="UTF-8"%>

<%@ include file="/page/page.jsp"%>
<%@ include file="/page/script.jsp" %>

<html>
	<head>
		<title>新增模板模块</title>
		<link href='<%=basePath%>css/gridly/sample.css' rel='stylesheet' type='text/css'>
		<script type="text/javascript">
			var schedingType = "";
			var basePath = '<%=basePath%>';
		</script>
		<script src='<%=basePath%>js/gridly/jquery.js' type='text/javascript'></script>
		<script src='<%=basePath%>js/gridly/jquery.gridly.js' type='text/javascript'></script>
		<script src='<%=basePath%>js/gridly/sample.js' type='text/javascript'></script>
		<script src='<%=basePath%>js/gridly/rainbow.js' type='text/javascript'></script>
	<style type="text/css">
	
	
	
	@font-face {
		font-family: 'Lato';
		font-style: normal;
		font-weight: 400;
		src: local('Lato Regular'), local('Lato-Regular'), url('<%=basePath%>css/gridly/9k-RPmcnxYEPm8CNFsH2gg.woff') format('woff');
	}
	form {
		margin: 0;
	}
	textarea {
		display: block;
	}
	
	/* .tempSmallArea {
		border:0px;
		width: 600px;
		BORDER-BOTTOM: 0px solid;
		BORDER-LEFT:   0px   solid;
		BORDER-RIGHT:   0px   solid;
		BORDER-TOP:   0px   solid;
		overflow-y:hidden;
		overflow-x:hidden;
		height: 150px;
	} */
	
	</style>
<style type="text/css">
	html,body {height:100%; margin:0px; font-size:12px;}
	.mydiv {
		background-color: #FFFFFF;
		border: 1px solid black;
		text-align: center;
		line-height: 40px;
		font-size: 12px;
		font-weight: bold;
		z-index:999;
		width: 752px;
		height: 520px;
		left:37%;
		top:15%;
		margin-left:-150px!important;/*FF IE7 该值为本身宽的一半 */
		margin-top:-60px!important;/*FF IE7 该值为本身高的一半*/
		margin-top:0px;
		position:fixed!important;/* FF IE7*/
		position:absolute;/*IE6*/
		_top: expression(eval(document.compatMode &&
		document.compatMode=='CSS1Compat') ?
		documentElement.scrollTop + (document.documentElement.clientHeight-this.offsetHeight)/2 :/*IE6*/
		document.body.scrollTop + (document.body.clientHeight - this.clientHeight)/2);/*IE5 IE5.5*/
	}
	.bg,.popIframe {
		background-color: #666; display:none;
		width: 100%;
		height: 100%;
		left:0;
		top:0;/*FF IE7*/
		filter:alpha(opacity=50);/*IE*/
		opacity:0.5;/*FF*/
		z-index:1;
		position:fixed!important;/*FF IE7*/
		position:absolute;/*IE6*/
		_top: expression(eval(document.compatMode &&
		document.compatMode=='CSS1Compat') ?
		documentElement.scrollTop + (document.documentElement.clientHeight-this.offsetHeight)/2 :/*IE6*/
		document.body.scrollTop + (document.body.clientHeight - this.clientHeight)/2);
	}
	.popIframe {
		filter:alpha(opacity=0);/*IE*/
		opacity:0;/*FF*/
	}
</style>
	</head>
<body>
	<input type="hidden" id="templateCode" value="${templateCode}"/>
	<input type="hidden" id="templateName" value="${template.templateName}"/>
	<input type="hidden" id="channelCode" value="${template.channelCode}"/>
	<input type="hidden" id="templateId" value="${template.id}"/>
	<input type="hidden" id="backup" value="${template.backup}"/>
	<input type="hidden" id="templateContent" value="${template.content}"/>
	<input type="hidden" id="editTemplateContent" value="${template.editTemplateContent}"/>
	<div style="float: left; width: 100%;">
		<div style="width: 40%; float: left;">
			<div id="template_form" style="width: 100%; float: left;"></div>
			<div style="margin-top:10px;float:left;font:12px '宋体';font-size:bold;color:#000">模板列表 <b style="color:red">(点击下面模板名称，在右侧添加模板)</b></div>
			<div style="width: 100%; float: left;">
			<c:forEach var="obj" items="${modules}" varStatus="i">
			    <!--模块类型：大  -->
				<c:if test="${obj.moduleSize == '0'}">
					<input class='largeP button' type="button" value="${obj.moduleName}" onclick="addLargeModule(${obj.id},'600px')"/>
				</c:if>
				<!-- 模块类型：小 -->
				<c:if test="${obj.moduleSize == '1'}">
					<input class='smallp button' type="button" value="${obj.moduleName}" onclick="addSmallModule(${obj.id},'600px')"/>
				</c:if>
			</c:forEach>
			</div>
			<div style="width: 100%;float:left;line-height:150%;margin-top:10px">
				图片服务器地址: $imgServer<br />
				款号: $goods.goodsSn <br />
				品牌: $goods.brandName <br />
				分类: $goods.catName <br />
				款名: $goods.goodsName <br />
				吊牌价: $goods.marketPrice <br />
				商品描述: $goods.salePoint <br />
				模特信息图片路径: $goods.cardPicture <br />
				尺寸图片路径: $goods.sizePicture <br />
				模特图列表: $modelImgs &nbsp;&nbsp;子项（img）属性：[$img.modelUrl 模特图路径,$img.imgDesc 模特图描述] <br />
				详细图正反面图片列表: $detailPoNeImgs &nbsp;&nbsp;子项（img）属性：[$img.detailPoNeUrl 正反面路径,$img.imgDesc 正反面描述] <br />
				详细图列表: $detailImgs &nbsp;&nbsp;子项（img）属性：[$img.detailUrl 详细图路径,$img.imgDesc 详细图描述] <br />
				颜色图列表: $colorImgs &nbsp;&nbsp;子项（img）属性：[$img.colorImgUrl 颜色图路径,$img.imgDesc 颜色图描述]
			</div>
		</div>
		<div style="width: 60%; float: left;">
			<div style="float:none;font:bold 12px '宋体';padding:10px 0 10px 15px;color:#000">模板编辑区域<b style="font-size:normal;color:#f00">(拖动编辑框，调整模板排列顺序)</b></div>
			<div class='content'>
				<section class='example'>
					<div class='gridly' style="width: 600px;z-index: 800;position: relative;font-family: Tahoma, Geneva, sans-serif; font-size: 12px; overflow: hidden;">
					</div>
				</section>
			</div>
		</div>
	</div>
	<input type="hidden" id="contentId"/>
	<script type="text/javascript" src="<%=basePath%>/page/template/addMobilePhoneTemplate.js"></script>
	<!-- 
	<div style="padding-top:10px">模板列表 <b style="color:red">(点击添加模板)</b></div>
	<div style="width: 36%; float: left;">
	<c:forEach var="obj" items="${modules}" varStatus="i">
		<c:if test="${obj.moduleSize == '0'}">
			<input class='largeP button' type="button" value="${obj.moduleName}" onclick="addLargeModule(${obj.id})"/>
		</c:if>
		<c:if test="${obj.moduleSize == '1'}">
			<input class='smallp button' type="button" value="${obj.moduleName}" onclick="addSmallModule(${obj.id})"/>
		</c:if>
	</c:forEach>
	</div>
	</div>
	 -->
	<!-- 
	<div style="width: 100%; float: left;">
		<div class='content'>
			<section class='example'>
				<div class='gridly' style="width: 750px;z-index: 800;position: relative;">
				</div>
			</section>
		</div>
		
		<div>
			<div>款号: $goodsSn </div>
			<div>品牌: $brandName </div>
			<div>分类: $catName </div>
			<div>款名: $goodsName </div>
			<div>吊牌价: $marketPrice</div>
			<div>模特信息图片路径: $cardPicture</div>
			<div>尺寸图片路径: $sizePicture</div>
			<div>模特图列表: $modelImgs &nbsp;&nbsp;子项属性：[$img.imgUrl 模特图路径,$img.imgDesc 模特图描述]</div>
			<div>详细图正反面图片列表: $detailPoNeImgs &nbsp;&nbsp;子项属性：[$img.imgUrl 模特图路径,$img.imgDesc 模特图描述]</div>
			<div>详细图列表: $detailImgs &nbsp;&nbsp;子项属性：[$img.imgUrl 模特图路径,$img.imgDesc 模特图描述]</div>
			<div>颜色图列表: $colorImgs &nbsp;&nbsp;子项属性：[$img.imgUrl 模特图路径,$img.imgDesc 模特图描述]</div>
		</div>
		
	</div>
	 -->
<div id="popDiv" class="mydiv" style="display:none;">
	<h3>编辑模板</h3>
		<textarea id="editModulecontent" name="editModulecontent" style="width:750px;height:350px;float: right;"></textarea>
		<p>
			<a class='smallp button' style="height: 10px;" href="javascript:saveModuleContent();" >保存模板</a>
			<!-- <input type="button" name="saveHtml" value="保存模板" onclick="saveModuleContent();"/>
			<input class='smallp button' type="button" value="保存模板" onclick="saveModuleContent();"/> -->
			<!-- <input type="button" name="clear" value="清空内容" /> -->
		</p>
	<a href="javascript:closeDiv();">关闭窗口</a>
</div>
<div id="bg" class="bg" style="display:none;"></div>
<iframe id='popIframe' class='popIframe' frameborder='0' ></iframe>
</body>
<script type="text/javascript">
	$(".gridly").html(decodeURI($("#editTemplateContent").val()));
</script>
</html>
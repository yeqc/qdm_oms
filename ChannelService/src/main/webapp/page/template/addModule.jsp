<%@ page language="java" pageEncoding="UTF-8"%>

<%@ include file="/page/page.jsp"%>
<%@ include file="/page/script.jsp" %>

<html>
	<head>
		<title>新增模板模块</title>
	</head>

<body>
	<div style="float:left;">
	<div id='addModule-panel' style="width: 55%;float:left;"></div>
	<div id="velocityDesc" style="width: 40%;float:right;font:12px '宋体';margin-top:10px">
			<!-- <b style="color:red">(Velocity模板语言)</b><br /> -->
				<font color="red">模板类型说明</font><br />
			           模块大小 ：  PC  大    手机        小   
				<font color="red">模板语言使用说明</font><br />
				图片服务器地址: $imgServer<br />
				款号: $goods.goodsSn <br />
				品牌: $goods.brandName <br />
				分类: $goods.catName <br />
				款名: $goods.goodsName <br />
				吊牌价: $goods.marketPrice <br />
				商品描述: $goods.salePoint <br />
				模特信息图片路径: $goods.cardPicture 非空判断字段 $goods.cardPictureT<br />
				尺寸图片路径: $goods.sizePicture 非空判断字段 $goods.sizePictureT<br />
				模特图列表: $modelImgs &nbsp;&nbsp;子项（img）属性：[$img.modelUrl 模特图路径,$img.imgDesc 模特图描述] <br />
				详细图正反面图片列表: $detailPoNeImgs &nbsp;&nbsp;子项（img）属性：[$img.detailPoNeUrl 正反面路径,$img.imgDesc 正反面描述] <br />
				详细图列表: $detailImgs &nbsp;&nbsp;子项（img）属性：[$img.detailUrl 详细图路径,$img.imgDesc 详细图描述] <br />
				颜色图列表: $colorImgs &nbsp;&nbsp;子项（img）属性：[$img.colorImgUrl 颜色图路径,$img.imgDesc 颜色图描述] <br />
				<font color="red">以下占位符字段是子模块标题图片链接使用</font><br />
				商品信息图片: $goodsInfoImg<br />
				模特展示图片: $modelShowImg <br />
				编辑推荐图片: $goodsEditImg <br />
				模特信息图片: $modelInfoImg <br />
				细节展示图片: $detailShowImg <br />
				颜色选择图片: $colorSelectImg <br />
				尺码规格图片: $goodsSizeImg <br />
				商品信息小图图片: $goodsInfoSubImg
	</div>
	</div>
	<input type="hidden" id="moduleId"  value="${obj.id}"/>
	<input type="hidden" id="moduleName"  value="${obj.moduleName}"/>
	<input type="hidden" id="showTitle"  value="${obj.showTitle}"/>
	<input type="hidden" id="moduleContent"  value="${obj.moduleContent}"/>
	<input type="hidden" id="addTime" value="${obj.addTime}"/>
	<input type="hidden" id="moduleSize"  value="${obj.moduleSize}"/>
	<input type="hidden" id="channelCode"  value="${obj.channelCode}"/>
	<input type="hidden" id="shopCode"  value="${obj.shopCode}"/>
	<input type="hidden" id="moduleType"  value="${obj.moduleType}"/>
	<input type="hidden" id="tbModuleName"  value="${obj.tbModuleName}"/>
</body>
	
	<script type="text/javascript">
		var schedingType = "";
		var basePath = '<%=basePath%>'; 
	</script>
	<script type="text/javascript" src="<%=basePath%>/page/template/addModule.js"></script>
</html>
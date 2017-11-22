<%@ page language="java" pageEncoding="UTF-8"%>

<%@ include file="/page/page.jsp"%>
<%@ include file="/page/script.jsp"%>
<html>
	<head>
		<title>新增模板模块</title>
		<script type="text/javascript">
			var schedingType = "";
			var basePath = '<%=basePath%>';
		</script>
		<script src='<%=basePath%>js/gridly/jquery.js' type='text/javascript'></script>
	</head>
<body>
<div id="edit_template_form"></div>
	<input type="hidden" id="templateName" value="${template.templateName}"/>
	<input type="hidden" id="channelCode" value="${template.channelCode}"/>
	<input type="hidden" id="templateId" value="${template.id}"/>
	<input type="hidden" id="backup" value="${template.backup}"/>
	<input type="hidden" id="templateCode" value="${template.templateCode}"/>
	<input type="hidden" id="templateContent" value="${template.content}"/>
	<input type="hidden" id="editTemplateContent" value="${template.editTemplateContent}"/>
<!-- 

<form id="editTemplateForm" name="editTemplateForm" action="<%=basePath%>custom/channelTemplate/editTemplateContent.spmvc?method=update">
	<input type="hidden" id="templateName" name="templateName" value="${template.templateName}"/>
	<input type="hidden" id="channelCode" name="channelCode" value="${template.channelCode}"/>
	<input type="hidden" id="templateId" name="templateId" value="${template.id}"/>
	<input type="hidden" id="backup" name="backup" value="${template.backup}"/>
	<input type="hidden" id="templateCode" name="templateCode" value="${templateCode}"/>
	<input type="hidden" id="templateContent" value="${template.content}"/>
	<input type="hidden" id="editTemplateContent" name="editTemplateContent" value="${template.editTemplateContent}"/>
	<div id="popDiv" class="mydiv">
		<h4>编辑模板</h4>
			<textarea id="editModulecontent" name="templateContent" style="width:750px;height:350px;"></textarea>
			<p>
				<input type="button" value="保存模板" onclick="saveTemplateContent();" />
				<input type="button" name="clear" value="清空内容" />
			</p>
		
	</div>
</form> -->
</body>
<script type="text/javascript">
function ltrim(str){ //删除左边的空格
	return str.replace(/(^\s*)/g,"");
}

$("#editModulecontent").html(ltrim(decodeURI($("#templateContent").val())));

/**
function saveTemplateContent() {
	var url = basePath + 'custom/channelTemplate/editTemplateContent.spmvc?method=update';
	//var queryString = jQuery("#editTemplateForm")[0].formSerialize();
	alert($("form[name='editTemplateForm']").formSerialize());
	$("form[name='editTemplateForm']")[0].formSerialize();
	$("#editTemplateForm").ajaxSubmit({
		type: 'post',
			url: url ,
			success: function(data){
				alert( "success");
				$( "#editTemplateForm").resetForm();
				},  
			error: function(XmlHttpRequest, textStatus, errorThrown){
				alert( "error");  
			}  
	});
}
**/
</script>
<script type="text/javascript" src="<%=basePath%>/page/template/editTemplateContent.js"></script>

</html>
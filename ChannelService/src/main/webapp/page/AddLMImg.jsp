<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/page/page.jsp"%>
<%
	String pid=request.getParameter("pid")==null?"":(String)request.getParameter("pid");
	System.out.println(pid+"###############@");
	StringBuffer uploadUrl = new StringBuffer("http://");
	uploadUrl.append(request.getHeader("Host"));
	uploadUrl.append(request.getContextPath());
	uploadUrl.append("/modules/content/lmImgUpload.action?pid="+pid+"&jsession="+session.getId());
%>

<script type="text/javascript" src="<%=basePath%>js/swfupload/swfupload.js"></script>
<script type="text/javascript" src="<%=basePath%>js/swfupload/swfupload.queue.js"></script>
<script type="text/javascript" src="<%=basePath%>js/swfupload/handlers.js"></script>
<style>
	.tit{height:30px;padding-top:8px;}
	div img{border:0;}
</style>
<%
	String folder_id=request.getParameter("folder_id")==null?"-1":(String)request.getParameter("folder_id");
%>
<script type="text/javascript">
	var swfu;
			window.onload = function () {
				swfu = new SWFUpload({
					upload_url: "<%=uploadUrl.toString()%>",
					post_params: {"name" : "isoftstone"},
					
					// File Upload Settings
					file_size_limit : "10 MB",	// 1000MB
					file_types : "*.jpg;*.gif;*.png;*.jpeg",
					file_types_description : "所有文件",
					file_upload_limit : "0",
									
					file_queue_error_handler : fileQueueError,
					file_dialog_complete_handler : fileDialogComplete,//选择好文件后提交
					file_queued_handler : fileQueued,
					upload_progress_handler : uploadProgress,
					upload_error_handler : uploadError,
					upload_success_handler : uploadSuccess,
					upload_complete_handler : uploadComplete,
	
					file_post_name:"logoImg",
	
					// Button Settings
					button_image_url : "<%=basePath%>/images/SmallSpyGlassWithTransperancy_17x18.png",
					button_placeholder_id : "spanButtonPlaceholder",
					button_width: 180,
					button_height: 18,
					button_text : '<span class="button">选择图片 <span class="buttonSmall">(10 MB Max)</span></span>',
					button_text_style : '.button { font-family: Helvetica, Arial, sans-serif; font-size: 12pt; } .buttonSmall { font-size: 10pt; }',
					button_text_top_padding: 0,
					button_text_left_padding: 18,
					button_window_mode: SWFUpload.WINDOW_MODE.TRANSPARENT,
					button_cursor: SWFUpload.CURSOR.HAND,
					
					// Flash Settings
					flash_url : "<%=basePath%>js/swfupload/swfupload.swf",
	
					custom_settings : {
						upload_target : "divFileProgressContainer"
					},
					// Debug Settings
					debug: false  //是否显示调试窗口
				});
			};
			function startUploadFile(){
				swfu.startUpload();
			}
</script>

</head>
<BODY >
	<div id="content" >
			<form enctype="multipart/form-data">
				<div
					style="display: inline; border: solid 1px #7FAAFF; background-color: #C5D9FF; padding: 2px;">
					<span id="spanButtonPlaceholder"></span>
					<input id="btnUpload" type="button" value="上  传"
						onclick="startUploadFile();self.close();" class="btn3_mouseout" onMouseUp="this.className='btn3_mouseup'"
						onmousedown="this.className='btn3_mousedown'"
						onMouseOver="this.className='btn3_mouseover'"
						onmouseout="this.className='btn3_mouseout'"/>
					<input id="btnCancel" type="button" value="取消所有上传"
						onclick="cancelUpload();" disabled="disabled" class="btn3_mouseout" onMouseUp="this.className='btn3_mouseup'"
						onmousedown="this.className='btn3_mousedown'"
						onMouseOver="this.className='btn3_mouseover'"
						onmouseout="this.className='btn3_mouseout'"/>
				</div>
			</form>
			<div id="divFileProgressContainer"></div>
			<div id="thumbnails">
				<table id="infoTable" border="0" width="530" style="display: inline; border: solid 1px #7FAAFF; background-color: #C5D9FF; padding: 2px;margin-top:8px;">
				</table>
			</div>
			<div></div>
		</div>
</BODY>
</HTML>

//var faceObj = null;
//表情图片单机事件。
var faceAction = "ccomment/getFaces.ct";
function clickFaceImg(code){
		$('#comment').insertAtCaret(code); 
}

// wait for the DOM to be loaded
$(document).ready(function() {
	$("#comment").textlimit("span.counter", 200);
		
	var options = {
		//target : '#output',
		beforeSubmit : showRequest,
		resetForm: true,
		success : showResponse
	};
	
	$('#commentForm').ajaxForm(options);

	function showRequest(formData, jqForm, options) {
		//test 
		//var queryString = $.param(formData);
		//alert('About to submit: \n\n' + queryString);

		//check
		var comment = $('#comment').val();
		if(comment==null || $.trim(comment) == ""){
			alert("评论不能为空！");
			return false;
		} else if(filterFace(comment).length > 200){
			alert("评论不能超过200字符！");
			return false;
		}
		
		$('#comment_Button').get(0).disabled = "disabled";
		return true;
	}

	function showResponse(responseText, statusText, xhr, $form) {
		if(responseText == "0") {
			alert("评论失败");
		} else {
			alert("感谢您的参与，您的评论正在审核当中，请耐心等待！");
		}
		
		$('#comment_Button').get(0).disabled = "";
		
		$('#submit_comment').hide("slow");//(300);
	}
	
	function getFaces(){
		if(faceObj != null) {
			$('#comment_face_div').show();
			return;
		}
		
		$.ajax({
		   type: "POST",
		   dataType: "json",
		   url: faceAction,
		   data: "",
		    //data: "{'start':'0';'limit':'10'; 'currentPage' : '" + currentPage + "'}",
		   success: function(json){
		     faceObj = json;
			 showFaceDiv();
		   }
		 });
	}
	//显示表情
	function showFaceDiv(){
		$.each(faceObj, function(index, valueObj) { 
				var img = "<span style=\"margin: 2px;\"><img class=\"insertImg\" alt=\"" + 
				valueObj.name + "\" src=\"" + valueObj.url + 
				"\" onclick=\"clickFaceImg('" + valueObj.code + "');\"></span>"
				;
				
				$('#comment_face_div').append(img);
		});
		
		$('#comment_face_div').show();
	}
	
	$('#comment_face').click(getFaces);//绑定表情按钮单机事件。
	
	
	//开始   表情帮定及文本域初始化。
	$('#comment').setCaret(); 
	$('.insertImg').hover( 
		function(){ 
			$(this).selectContents(); 
		}, 
		function(){ 
		$.unselectContents(); 
		}
	); 
	//结束  表情帮定及文本域初始化。
});



//分页评论查询 -------------start
			
 //填充列表
 function setPageList(currentData){
  if(currentData==null) return;
  $("#comment_list").empty();
 
  $.each(currentData, function(index, row) { 
		var oneRow = $('#list_template').html();
		
		var reg = new RegExp("(_" + "comment_index" + "_)", "g");
		oneRow = oneRow.replace(reg, index + "_"+ row.id);
		
	    var reg = new RegExp("(_comment_username_)", "g");
		oneRow = oneRow.replace(reg, (row.username==null || row.username=='' ? '游客': row.username));
		
		var reg = new RegExp("(_ding_count_)", "g");
		oneRow = oneRow.replace(reg, row.dingCount);
		
		var reg = new RegExp("(_comment_time_)", "g");
		oneRow = oneRow.replace(reg, row.createTime);
		
		if(tempFaceObj == null) {
			$.ajax({
			   type: "POST",
			   dataType: "json",
			   url: "ccomment/getFaces.ct",
			   data: "",
			   success: function(json){
			     tempFaceObj = json;
			   }
			 });
		}
		var content = row.content;
		if(tempFaceObj != null){
			$.each(tempFaceObj, function(index, valueObj) { 
					//先替换[]为\\[\\]
					var reg1 = new RegExp("(\\[)", "g"); 
					var temp =  valueObj.code.replace(reg1, "\\[");
					var reg1 = new RegExp("(\\])", "g"); 
					var temp = temp.replace(reg1, "\\]");
		
					var reg = new RegExp("(" + temp + ")", "g"); 
					content = content.replace(reg, '<img src="' + valueObj.url + '">');
			});
		}
		var reg = new RegExp("(_comment_content_)", "g");
		oneRow = oneRow.replace(reg, content);
		
		var reg = new RegExp("(_comment_id_)", "g");
		oneRow = oneRow.replace(reg, row.id);
		
		$('#comment_list').append(oneRow);
	});
 
    // $('#test_area').val($('#comment_list').html());
 }

function sortList(sortField){
	sort = sortField;
	pg.serviceProc(1);
}
				
//分页评论查询 -------------end

//顶功能
function ding(id, dingId){
	$.ajax({
		   type: "POST",
		   dataType: "json",
		   url: dingAction,
		   data: "id=" + id + "&currentPageTest=0",
		   success: function(json){
		   			if(json && json.dingCount){
				 		$('#' + dingId).replaceWith('顶(' + json.dingCount + ')');
		   			}
	  		 }
	 });
}

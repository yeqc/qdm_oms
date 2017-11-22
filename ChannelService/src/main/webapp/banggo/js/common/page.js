	var total = 0; // 记录总数
	var psize = 20; // 每页记录数
	var cpage = 1; // 当前页数 
	var pages = 0; // 总页数
	
	// 设置分页信息
	function setPage() {
		if($('#pageNum').length < 1){
			return;
		}
		
		pages = Math.ceil(total/psize);
		
		var pHtml = '';
		
		pHtml += ' 总计 '+pages+' 页 '+total+' 条记录  当前第 '+cpage+' 页，每页' +
	           			'<input type="text" onblur="setPSize(this);" style="width:20px;" value="'+psize+'" />条';
	           		
	    if (cpage - 1 > 0) {
	    	pHtml += ' <a href="javascript:void(0);" onclick="setJump(1);">第一页</a> ';
	    	pHtml += '<a href="javascript:void(0);" onclick="setJump('+(cpage-1)+');">上一页</a> ';
	    } else {
	    	pHtml += ' <a href="javascript:void(0);">第一页</a> ';
	    	pHtml += '<a href="javascript:void(0);">上一页</a> ';
	    }
	           		
		if (cpage + 1 > pages) {
	   		pHtml += '<a href="javascript:void(0);">下一页</a> ';
	   		pHtml += '<a href="javascript:void(0);">最末页</a> ';
		} else {
	   		pHtml += '<a href="javascript:void(0);" onclick="setJump('+(cpage-0+1)+');">下一页</a> ';
	   		pHtml += '<a href="javascript:void(0);" onclick="setJump('+pages+');">最末页</a> '
		}
	           		
		pHtml += '<input type="text" onkeyup="this.value=this.value.replace(/[^\\d]/g,\'\');" style="width:25px;" id="jumpNum" value="" /><input type="button" value="跳转" onclick="jumpPage('+pages+');" />';
		$('#pageNum').html(pHtml);
	}
	
	// 清空分页
	function setClearPage() {
		$('#pageNum').html("");
	}
    	
	// 设置上下页跳转
	function setJump(n) {
		//cpage = n || 1;
		doCommit(n);
	}
    	
	// 设置每页记录数
	function setPSize(o){
		var pp = $(o).val();
		psize = pp;
		setJump(1);
	}
	
	// 设置直接跳转
	function jumpPage(tPages){
		var v = $('#jumpNum').val();
		if($('#jumpNum').length > 0 && v != null && v.length > 0){
			var cp = v <= tPages ? v : tPages;
			
			cp = cp < 1 ? 1 : cp;
			setJump(cp);
		}
	}
    	
	// 每次提交page的值处理
	function setClear() {
    	total = 0; // 记录总数
		//psize = 20; // 每页记录数
		//cpage = 1; // 当前页数 
		// 当前页数的问题
		pages = 0; // 总页数
	}

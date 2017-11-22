function selectFileName(){
	var type=$("#fileType").val();
	var shopCode=$("#shopCode").val();
	var time=$("#fileTime").val();
	var html="";
	Ext.Ajax.request({
		waitMsg : '请稍等.....',
		url : basePath
				+ '/custom/shopBusinessGoods/getShopGoodsFileList.spmvc',
		method : 'post',
		params : {
			type :type ,
			shopCode :shopCode,
			time:time
		},
		success : function(response) {
			var respText = Ext.util.JSON
					.decode(response.responseText);
			for(var i = 0; i<respText.length; i++){
				if(respText[i].indexOf("csv")>=0){
				html+="<a style='color:red' target='_self' href = "+basePath+"/custom/shopBusinessGoods/downloadFTPFile.spmvc?fileName="+respText[i]+"&type="+type+"&shopCode="+shopCode+"&time="+time+">"+respText[i]+"</a></br>";
				};
				}
			if(html==""){
				html="<font style='color:red'>没有数据！</font>";
			}
			$("#filediv").html(html);

		},
		failure : function(response,
				options) {
			alert("读取文件列表失败！");
		}
	});
	
}
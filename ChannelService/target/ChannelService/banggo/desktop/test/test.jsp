<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>淘宝和邦购测试</title>
</head>
<body>
	<h2>淘宝测试</h2>
	<table>
		<tr>
			<td>sku</td>
			<td><input id="txt_tb_sku" type="text" name="tb_sku" /></td>

			<td>库存数</td>
			<td><input id="txt_tb_stock" type="text" name="tb_stock" /></td>
			<td align="left"><input id="bg_btn" type="button"
				style="background: #E0EDFF; height: 23px; width: 60px;" value="go淘宝"
				onclick="f_goTaobao()" /></td>
		</tr>
	</table>

	<h2>邦购测试</h2>
	<table>
		<tr>
			<td>sku</td>
			<td><input id="txt_bg_sku" type="text" name="bg_sku" /></td>

			<td>库存数</td>
			<td><input id="txt_bg_stock" type="text" name="bg_stock" /></td>
			<td align="left"><input id="bg_btn" type="button"
				style="background: #E0EDFF; height: 23px; width: 60px;" value="go邦购"
				onclick="f_goBanggo()" /></td>
		</tr>
	</table>
	
	<div>
		<h2>paipai</h2>
		<table>
			<tr>
				<td>sku:</td>
				<td><input id="pp_sku" type="text"  /><a href="javascript:void(0);" onclick="goChannel(0);">show item</a></td>
			</tr>
			<tr>
				<td>num:</td>
				<td><input id="pp_num" type="text"  /></td>
			</tr>
			<tr>
				<td></td>
				<td><input type="button" value="modify stock" onclick="goChannel(1)" /></td>
			</tr>
		</table>
	</div>
	
	<div>
		<h2>erp</h2>
		<table>
			<tr>
				<td>all stock:</td>
				<td><input id="_sku" type="button" value="all_stock" onclick="goChannel(2)"  /></td>
			</tr>
			<tr>
				<td>sku:<textarea rows="10" cols="30" id="erp_sku" >多个sku时，请填入25344730133:21110447152:21110436146格式的sku字符串
				</textarea>
				</td>
				<td><input type="text"  id="erp_channel" value="渠道编码"/>
				<input type="button" value="sku_channel stock" onclick="goChannel(3)" /></td>
			</tr>
		</table>
	</div>
	
	<div id="showId"></div>

	<script type="text/javascript">
	function goChannel(type) {
		var url = '<%=basePath%>';
		var d = new Date().getTime();
		if(type == 0) {
			var pp_sku = document.getElementById("pp_sku").value;
			url += 'paipaiItem.htm?sku='+pp_sku+'&t='+d;
		} else if(type == 1) {
			var pp_sku = document.getElementById("pp_sku").value;
			var pp_num = document.getElementById("pp_num").value;
			
			url += 'paipaiStock.htm?sku='+pp_sku+'&num='+pp_num+'&t='+d;
		} else if(type == 2) {
			url += 'erp2.htm?'+'t='+d;
		} else if(type == 3) {
			var erp_sku = document.getElementById("erp_sku").value;
			var erp_channel = document.getElementById("erp_channel").value;
			
			url += 'erp1.htm?skus='+erp_sku+'&channel='+erp_channel+'&t='+d;
		} else if(type == 4) {
			url += 'batch.htm?'+'t='+d;
		}
		
		alert("url:" + url);
		
		var xmlHttpRequest = null;
		if (window.ActiveXObject) {
			xmlHttpRequest = new ActiveXObject("Microsoft.XMLHTTP");
		} else if (window.XMLHttpRequest) {
			xmlHttpRequest = new XMLHttpRequest();
		}
		if (null != xmlHttpRequest) {
			xmlHttpRequest.open("GET", url, true);
			xmlHttpRequest.onreadystatechange = ajaxCallbackTo;
			xmlHttpRequest.send(null);
		}
		
		function ajaxCallbackTo() {
			if (xmlHttpRequest.readyState == 4) {
				if (xmlHttpRequest.status == 200) {
					var responseText = xmlHttpRequest.responseText;
				//	alert(responseText);
					
					document.getElementById("showId").innerHTML = responseText;
				}
			}
		}
	}
	
		function f_goTaobao() {
			var tb_sku = document.getElementById("txt_tb_sku").value;
			var tb_stock = document.getElementById("txt_tb_stock").value;

			var param = 'taobao.htm?';
			param += 'tb_sku=' + tb_sku;
			param += '&tb_stock=' + tb_stock;

			var xmlHttpRequest = null;
			if (window.ActiveXObject) {
				xmlHttpRequest = new ActiveXObject("Microsoft.XMLHTTP");
			} else if (window.XMLHttpRequest) {
				xmlHttpRequest = new XMLHttpRequest();
			}
			if (null != xmlHttpRequest) {
				xmlHttpRequest.open("GET", param, true);
				xmlHttpRequest.onreadystatechange = ajaxCallback;
				xmlHttpRequest.send(null);
			}

			function ajaxCallback() {
				if (xmlHttpRequest.readyState == 4) {
					if (xmlHttpRequest.status == 200) {
						var responseText = xmlHttpRequest.responseText;
						alert(responseText);
					}
				}
			}
		}

		function f_goBanggo() {
			var bg_sku = document.getElementById("txt_bg_sku").value;
			var bg_stock = document.getElementById("txt_bg_stock").value;

			var param = 'bg.htm?';
			param += 'bg_sku=' + bg_sku;
			param += '&bg_stock=' + bg_stock;

			var xmlHttpRequest = null;
			if (window.ActiveXObject) {
				xmlHttpRequest = new ActiveXObject("Microsoft.XMLHTTP");
			} else if (window.XMLHttpRequest) {
				xmlHttpRequest = new XMLHttpRequest();
			}
			if (null != xmlHttpRequest) {
				xmlHttpRequest.open("GET", param, true);
				xmlHttpRequest.onreadystatechange = ajaxCallback;
				xmlHttpRequest.send(null);
			}

			function ajaxCallback() {
				if (xmlHttpRequest.readyState == 4) {
					if (xmlHttpRequest.status == 200) {
						var responseText = xmlHttpRequest.responseText;
						alert(responseText);
					}
				}
			}

		}
	</script>
</body>
</html>
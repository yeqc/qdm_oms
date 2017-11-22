 //写入到Cookie           
function SetCookie(name, value, expires) {         
	var argv = SetCookie.arguments;         
	//本例中length = 3         
	var argc = SetCookie.arguments.length;         
	var expires = (argc > 2) ? argv[2] : null;         
	var path = (argc > 3) ? argv[3] : null;         
	var domain = (argc > 4) ? argv[4] : null;         
	var secure = (argc > 5) ? argv[5] : false;        
	document.cookie = name + "=" + escape(value) + ((expires == null) ? "" : ("; expires=" + expires.toGMTString())) + ((path == null) ? "" : ("; path=" + path)) + ((domain == null) ? "" : ("; domain=" + domain)) + ((secure == true) ? "; secure" : "");     
}
//过期处理        
function ResetCookie(value) {         
	var expdate = new Date();         
	SetCookie(value, null, expdate);     
}
//取Cookie的值           
function GetCookie(name) {         
	var arg = name + "=";         
	var alen = arg.length;         
	var clen = document.cookie.length;         
	var i = 0;         
	while (i < clen) {             
		var j = i + alen;             
		if (document.cookie.substring(i, j) == arg) 
			return getCookieVal(j);             
		i = document.cookie.indexOf(" ", i) + 1;             
		if (i == 0) 
			break;         
	}         
	return null;     
}
//获得Cookie内容
function getCookieVal(offset) {         
	var endstr = document.cookie.indexOf(";", offset);         
	if (endstr == -1) 
		endstr = document.cookie.length;         
	return unescape(document.cookie.substring(offset, endstr));     
} 

//编码程序：
function CodeCookie(str)
{
  var strRtn="";
  for (var i=str.length-1;i>=0;i--)
  {
    strRtn+=str.charCodeAt(i);
    if (i) strRtn+="a"; //用a作分隔符
  }
  return strRtn;
}
//解码程序：
function DecodeCookie(str)
{
  var strArr;
  var strRtn="";
  strArr=str.split("a");
  for (var i=strArr.length-1;i>=0;i--)
  strRtn+=String.fromCharCode(eval(strArr[i]));
  return strRtn;
}
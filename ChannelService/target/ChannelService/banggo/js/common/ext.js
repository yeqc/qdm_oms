

// ext3.4.0服务期地址
var service_path = "http://exts.bang-go.com.cn/ext-3.4.0/";

// js包
//var base_path = service_path + "extjs/";
var base_path = basePath + "/extjs/";

// desktop包
//var desktop_path = service_path + "desktop/";
var desktop_path = basePath + "/desktop/";

var all_css = "css/ext-all.css";
var base_js = "js/ext-base.js";
var all_js = "js/ext-all.js";
var cn_js = "js/ext-lang-zh_CN.js";

document.writeln('<link rel="stylesheet" type="text/css" href="'+base_path + all_css +'" />');
document.writeln('<script type="text/javascript" src="'+base_path + base_js +'"></script>');
document.writeln('<script type="text/javascript" src="'+base_path + all_js +'"></script>');
document.writeln('<script type="text/javascript" src="'+base_path + cn_js +'"></script>');

function importJs(name) {
	document.writeln('<script type="text/javascript" src="'+base_path + 'js/' + name +'"></script>');
}

function importJsPath(name) {
	document.writeln('<script type="text/javascript" src="'+path + '/banggo/js/common/' + name +'"></script>');
}

function importJsDesPath(name) {
	document.writeln('<script type="text/javascript" src="'+path + '/banggo/desktop/' + name +'"></script>');
}

function importDesktopCss(name) {
	document.writeln('<link rel="stylesheet" type="text/css" href="'+desktop_path + 'css/'+ name +'" />');
}

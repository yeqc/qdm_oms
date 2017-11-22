/**
 *  页面操作内容权限
 */

// 用户调整单审核权限
var user_change_info_right = "user_change_info_right";
var auth_right = GetCookie('Authority');

// 用户调整单是否有审核权限
function isUserChangeInfoRight() {
	if (auth_right) {
		var r = "," + user_change_info_right + ",";
		if (auth_right.indexOf(r) != -1) {
			return true;
		} 
	}
	
	return false;
}

//获取登录名
function getLoginName() {
	var name = GetCookie('49BAC005-7D5B-4231-8CEA-16939BEACD67');
	
	return name;
}
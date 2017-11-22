// 公共页面跳转
// 注销登录地址
var logout = "/index.jsp";
// 树形菜单地址
var treePanel = "/index.jsp";
//var treePanel = "/banggo/desktop/treePanel.jsp";
// 修改密码地址
var updatePass = "/banggo/desktop/jsp/auth/modifyPassword.jsp";
// 设置背景地址
var setBackGroundPath = "/banggo/desktop/test/data-view.jsp";
// 背景目录
var backGroundFile = "/desktop/wallpapers/";
// 图片地址
var imageServicePath = "/banggo";

// 菜单项配置
var menuData = [
	
/***
{id:"1",text:"京东渠道",link:"/index.jsp",leaf:true,expanded:true,menu:true, type:1, icon: "kjcx.png", right:"kccxgl" ,shopCode:"jingdongShop"},
{id:"2",text:"拍拍渠道",link:"/index.jsp",leaf:true,expanded:true, menu:true, type:1, icon: "kjcx.png", right:"spkcgl" ,shopCode:"paipaiShop"},
{id:"3",text:"淘宝渠道",link:"/index.jsp",leaf:true,expanded:true, menu:true, type:1, icon: "kjcx.png", right:"qdkcgl" ,shopCode:"taobaoShop"},
{id:"4",text:"苏宁渠道",link:"/index.jsp",leaf:true,expanded:true, menu:true, type:1, icon: "kjcx.png", right:"queryLog" ,shopCode:"suningShop"},
{id:"5",text:"1号店渠道",link:"/index.jsp",leaf:true,expanded:true, menu:true, type:1, icon: "kjcx.png", right:"queryLog" ,shopCode:"1haoShop"},


{id:"1-1001",text:"京东渠道",link:"/index.jsp",leaf:true,expanded:false, type:1, right:"sjkccx"},


{id:"1-1002",text:"渠道库存查询",link:"/custom/channelShop/outChannelShopList.spmvc",leaf:true,expanded:false, type:1, right:"qdkccx"},

{id:"2-2001",text:"商品库存同步管理",link:"/banggo/desktop/jsp/stock/shop/stockSynch.jsp",leaf:true,expanded:false, type:1, right:"spkctbgl"},

{id:"3-3001",text:"渠道库存调整管理",link:"/banggo/desktop/jsp/stock/channel/channelStockReport.jsp",leaf:true,expanded:false, type:1, right:"qdkctzgl"},
{id:"3-3002",text:"商品安全阀值管理",link:"/banggo/desktop/jsp/stock/channel/productSafeAdd.jsp",leaf:true,expanded:false, type:1, right:"spaqfztzd"},
{id:"3-3003",text:"调整单结果反馈",link:"/banggo/desktop/jsp/stock/log/reportLogList.jsp",leaf:true,expanded:false, type:1, right:"tzdjgfk"},
{id:"3-3004",text:"手动更新",link:"/banggo/desktop/jsp/util/skuJob.jsp",leaf:true,expanded:false, type:1, right:"sdgx"},
{id:"3-3005",text:"ERP库存详情",link:"/banggo/desktop/jsp/util/erpStock.jsp",leaf:true,expanded:false, type:1, right:"erpstock"},

{id:"3-3006",text:"淘宝库存增量更新",link:"/banggo/desktop/jsp/util/topStockIncrease.jsp",leaf:true,expanded:false, type:1, right:"topStockIncrease"},

{id:"4-4001",text:"同步日志查询",link:"/banggo/desktop/jsp/report/changLog.jsp",leaf:true,expanded:false, type:1, right:"queryLog01"},
{id:"4-4002",text:"异常日志查询",link:"/banggo/desktop/jsp/errorlog/errorLog.jsp",leaf:true,expanded:false, type:1, right:"queryLog02"},
{id:"4-4003",text:"增量脚本日志查询",link:"/banggo/desktop/jsp/stock/log/jobLogList.jsp",leaf:true,expanded:false, type:1, right:"jobLogSearch"},
{id:"4-4004",text:"SKU库存变化日志查询",link:"/banggo/desktop/jsp/util/stockChangeLog.jsp",leaf:true,expanded:false, type:1, right:"stockChangeLog"}
****/
];

/**
**获取所有店铺
**/
function getChannelShop() {
	
	$.ajax({
		url : path+"/custom/channelShop/getAllChannelShopList.spmvc",
		type : 'POST',
		async :false,
	//	data : '',
		success : function(jsonData) {
		  //var jsonObj = eval('(' + data + ')');
		    var data = jsonData.data;
			if (null == data) {
				//alert("获取数据失败！");
				return;
			}

		   if(true==jsonData.isok){
			   
			   if(data.length>0){
				   
					 for(var i=0;i<data.length; i++) {
				    	var o ={};
				    	o.id=i+1;
				    	o.text=data[i].text;
				    	o.link=data[i].link;
				    	o.leaf=data[i].leaf;
				    	
				    	o.expanded=data[i].expanded;
				    	o.menu=data[i].menu;
				    	o.type=data[i].type;
				    	o.icon=data[i].icon;
				    	
				    	o.right=data[i].right;
				    	o.shopCode=data[i].shopCode;
				    	o.channelCode=data[i].channelCode;
				    	o.channelTitle=data[i].channelTitle;
				    	o.menuType=data[i].menuType;
			    		o.shopImg=data[i].shopImg;
				    	menuData.push(o);
				    }
				   
			   }
	
		   }
		   
//		   getGroupAllianceShop();
//		   getSystemSet();
		   getLog();
//		   getProductGoods();
		   
		},
		error : function(aa,bb,cc) {
			alert("occur error");
		}
	});
	
}

//设置系统信息
function  getSystemSet() {
		var o ={};

		o.id=1234;
    	o.text="系统设置";
    	o.link="/custom/index/index.spmvc?system=1";
    	o.leaf=true;
    	
    	o.expanded=true;
    	o.menu=true;
    	o.type=3;
    	o.icon="kjcx.png";
    	
    	o.right="spkcgl";
    	o.shopCode="system";
    	o.shopImg="";
		    	
		menuData.push(o);
}

//设置集团店铺信息
function getGroupAllianceShop() {
		var o ={};

		o.id=56789;
    	o.text="集团门店";
    	o.link="/custom/index/index.spmvc?shopCode=GR_ALLIANCE_CODE";
    	o.leaf=true;
    	
    	o.expanded=true;
    	o.menu=true;
    	o.type=3;
    	o.icon="kjcx.png";
    	
    	o.right="spkcgl";
    	o.shopCode="GR_ALLIANCE_CODE";
		o.shopImg="";
		menuData.push(o);
}

//产品商品
function getProductGoods() {
	var o ={};

	o.id=99;
	o.text="产品";
	o.link="/custom/index/index.spmvc?shopCode=PRODUCT_GOODS_CODE";
	o.leaf=true;	
	o.expanded=true;
	o.menu=true;
	o.type=3;
	o.icon="kjcx.png";
	o.right="spkcgl";
	o.shopCode="PRODUCT_GOODS_CODE";
	o.shopImg="";
	menuData.push(o);
}

function  getLog() {
		var o ={};
	//	o.id=i+1		
    	o.text="日志";
    	o.link="/custom/index/index.spmvc?shopCode=log";
    	o.leaf=true;
    	
    	o.expanded=true;
    	o.menu=true;
    	o.type=3;
    	o.icon="kjcx.png";
    	
    	o.right="spkcgl";
    	o.shopCode="log";
		o.shopImg="icon_log.png";
		o.menuType="LOGS";
		menuData.push(o);
}


// 根据title获取菜单对象
function getMenuData(title) {
	
	for (var i = 0, leng = menuData.length; i < leng; i++) {
		
		if (menuData[i].title == title) {
			var data = menuData[i];
			var son = getMenuDataByParentId(data.id);
			
			data.son = son;
			
			return data;
		}
	}


	return {};
}

// 获取所有加入菜单的首菜单项
function getMenuData() {
	var data = [];

	//console.dir( auth );
	//console.dir( parent );
	//auth = auth ? auth : parent.auth;
	//auth='';
	//auth += ',topStockIncrease';
	
	//if (auth) {
		for (var i = 0, leng = menuData.length; i < leng; i++) {
			var temp = menuData[i];
			//if (temp.menu == true && (temp.right && auth.indexOf("," + temp.right + ",") != -1)) {
			if (temp.menu == true ) {
			//	var son = getMenuDataByParentId(temp.id);
			//	temp.son = son;
				temp.son = [];
				data.push(temp);
			}
			
		}
	//}
	
	return data;
}

// 根据id获取对应的菜单
function getMenuDataById(id) {
	
	for (var i = 0, leng = menuData.length; i < leng; i++) {
		var mid = menuData[i].id;
		
		if (mid == id) {
			return menuData[i];
		}
	}
	
	return {};
}

// 获取父id获取对应的子菜单
function getMenuDataByParentId(id) {
	var data = [];

	for (var i = 0, leng = menuData.length; i < leng; i++) {
		var mid = menuData[i].id;
		// 以-为分隔符号
		var mdata = mid.split("-");
		//if (mdata.length == 2 && mdata[0] == id && (menuData[i].right && auth.indexOf("," + menuData[i].right + ",") != -1)) {
	if (mdata.length == 2 && mdata[0] == id ) {
			data.push(menuData[i]);
		}
	}
	
	return data;
}


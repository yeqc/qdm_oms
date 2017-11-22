package com.work.shop.util;

/**
 * @ClassName: Constants
 * @Description: (存放常用的常量信息)
 * 
 */
public class Constants {

	public final static Integer EVERY_PAGE_SIZE = 10;

	/** 促销信息 (启用) 状态 */
	public final static byte ACTIVATE_STATUS = 1;

	/** 促销信息 (未启用 ) 状态 */
	public final static byte NO_ACTIVATE_STATUS = 0;

	/** 促销信息 (满赠 ) 类型 */
	public final static byte PROM_TYPE_0 = 0;

	/** 促销信息 (套装 ) 类型 */
	public final static byte PROM_TYPE_1 = 1;

	/** 促销信息 (买赠 ) 类型 */
	public final static byte PROM_TYPE_2 = 2;

	/** 促销信息 (集合赠 ) 类型 */
	public final static byte PROM_TYPE_3 = 3;

	/** 促销信息 (福袋) 类型 */
	public final static byte PROM_TYPE_4 = 4;
	
	/** 库存同步 全量*/
	public final static byte STOCK_UPDATE_TYPE_ALL=1;
	
	/** 库存同步 增量*/
	public final static byte STOCK_UPDATE_TYPE_PART=2;

	/** 套装商品代码 前缀 */
	public final static String GROUP_CODE_PREFIX = "ASGP";

	/** API 调用成功返回值 */
	public final static String API_RETURN_OK = "1";
	public final static String API_RETURN_NO = "0";

	/** 京东渠道Code */
	public final static String JD_CHANNEL_CODE = "JD_CHANNEL_CODE";

	/** 淘宝渠道Code */
	public final static String TB_CHANNEL_CODE = "TB_CHANNEL_CODE";
	
	/** 淘宝天猫渠道Code */
	public final static String TMALL_CHANNEL_CODE = "TMALL_CHANNEL_CODE";
	
	/** 淘宝分销渠道Code */
	public final static String TBFX_CHANNEL_CODE = "TBFX_CHANNEL_CODE";
	
	/** 淘宝分销 */
	public final static String TB_FX = "HQ01S999";

	/** 一号店渠道Code */
	public final static String YHD_CHANNEL_CODE = "YHD_CHANNEL_CODE";

	/** 微购物渠道Code */
	public final static String WGW_CHANNEL_CODE = "WGW_CHANNEL_CODE";

	/** 拍拍渠道Code */
	public final static String PP_CHANNEL_CODE = "PP_CHANNEL_CODE";

	/** 苏宁渠道Code */
	public final static String SN_CHANNEL_CODE = "SN_CHANNEL_CODE";

	/** 当当网渠道Code */
	public final static String DD_CHANNEL_CODE = "DD_CHANNEL_CODE";
	
	/** 邦购渠道Code */
	public final static String BG_CHANNEL_CODE = "BG_CHANNEL_CODE";
	
	/** 微信渠道Code */
	public final static String WX_CHANNEL_CODE = "WX_CHANNEL_CODE";
	
	/** 贝贝渠道code */
	public final static String BB_CHANNEL_CODE = "BB_CHANNEL_CODE";
	
	/** 蘑菇街渠道code */
	public final static String MG_CHANNEL_CODE = "MG_CHANNEL_CODE";
	
	/** 美丽说渠道code */
	public final static String MLS_CHANNEL_CODE = "MLS_CHANNEL_CODE";

	public final static String CHANNEL_CODE = "channelCode";

	public final static String SHOP_CODE = "shopCode";

	public final static String APP_URL = "app_url";
	
	public final static String APP_OLD_URL = "app_old_url";

	public final static String CH_1688_GOODS_SN_KEY = "CACHE_HQ01S227_";
	
	/** 天猫*/
    public final static String TMALL_SERVER_URL="http://qimen.api.taobao.com/router/qimen/service?"	;  //奇门线上地址
    
    public final static String TMALL_USER_ID="134363478";

	/** 京东 */
	//public final static String JD_SERVER_URL = "http://gw.api.360buy.com/routerjson";
	public final static String JD_SERVER_URL = "app_url";

	public final static String APP_KEY = "app_key";

	public final static String APP_SECRET = "app_secret";

	public final static String ACCESS_TOKEN = "access_token";
	
	public final static String SIGN_TYPE = "sign_type";

	/** 拍拍 */
	public final static String PAIPAI_ACCESS_TOKEN = "access_token";

	public final static String PAIPAI_APP_OAUTH_ID = "app_secret";

	public final static String PAIPAI_APP_OAUTH_KEY = "app_key";

	public final static String PAIPAI_HOST = "http://api.paipai.com";

	public final static String PAIPAI_QQ = "paipai_qq";

	/** 一号店 */
	public final static String YHD_SERVER_URL = "http://openapi.yhd.com/app/api/rest/router";

	public final static String YHD_APP_KEY = "app_key";

	public final static String YHD_SECRET_KEY = "app_secret";

	public final static String YHD_SESSION_KEY = "access_token";

	/** 苏宁 */
	public final static String SN_SERVER_URL = "http://open.suning.com/api/http/sopRequest";

	public final static String SN_APP_KEY = "app_key";

	public final static String SN_APP_SECRET = "app_secret";
	
	/** 当当网 */
	public final static String DD_SERVER_URL = "http://api.open.dangdang.com/openapi/rest?";

	public final static String DD_APP_KEY = "app_key";

	public final static String DD_APP_SECRET = "app_secret";

	public final static String DD_SESSION_KEY = "access_token";
	
	/** 贝贝网 */
	public final static String BB_SERVER_URL="http://api.open.beibei.com/outer_api/out_gateway/route.html";
	
	public final static String BB_GOODS_SN_KEY = "CACHE_";
	
	public final static String BB_APP_KEY = "app_key";
	
	public final static String BB_APP_SECRET = "app_secret";

	public final static String BB_SESSION_KEY = "access_token";
	
	/** 蘑菇街*/
	public final static String MOGU_SERVER_URL="https://openapi.mogujie.com/invoke";
	
	public final static String MOGU_GOODS_SN_KEY = "CACHE_";
	
	public final static String OUT_GOODS_SN_KEY = "OUT_GOOODS_";
	
	public final static String MOGU_APP_KEY = "app_key";
	
	public final static String MOGU_APP_SECRET = "app_secret";

	public final static String MOGU_SESSION_KEY = "access_token";
	
	/**美丽说*/
	public final static String MLS_SERVER_URL="https://api.open.meilishuo.com/router/rest";
	
	public final static String MLS_GOODS_SN_KEY = "CACHE_";
	
	public final static String MLS_APP_KEY = "app_key";
	
	public final static String MLS_APP_SECRET = "app_secret";
	
	public final static String MLS_SESSION_KEY = "access_token";
	
	/** 爱奇艺 */
	public final static String AQY_SERVER_URL = "http://mall.iqiyi.com:8080/apis/erp/";

	public final static String AQY_APP_KEY = "app_key";

	public final static String AQY_APP_SECRET = "app_secret";

	public final static String AQY_SESSION_KEY = "access_token";
	
	/**楚楚街*/
	public final static String CCJ_SERVER_URL="https://parter.api.chuchujie.com/sqe/Order";
	
	public final static String CCJ_ORG_NAME="METERSBWAu0809";
	
	/** 品牌官网  */
    public final static String GW_CHANNEL_CODE = "GW_CHANNEL_CODE";
    public final static String GW_ACCESS_TOKEN="access_token";
	public final static String GW_SERVER_URL = "app_url";
	
	/**苏宁分销渠道*/
	public final static String SNFX_CHANNEL_CODE = "SNFX_CHANNEL_CODE";
	public final static String SNFX_SERVER_URL = "app_url";
	public final static String SNFX_SERVER_VERSION = "version";
	public final static String SNFX_APP_ID = "app_id";
	public final static String SNFX_TRAND_ID = "trand_id";
	public final static String SNFX_PUBLICK_KEY = "public_key";
	public final static String SNFX_PRIVATE_KEY = "private_key";
	public final static String SNFX_MD5_KEY = "md5_key";
	
	/**有赞渠道*/
	public final static String YZ_ACCESS_TOKEN = "access_token";
	public final static String YZ_SHELVESUP_URL = "https://open.youzan.com/api/oauthentry/youzan.item.update/3.0.0/listing";
	public final static String YZ_SHELVESDOWN_URL = "https://open.youzan.com/api/oauthentry/youzan.item.update/3.0.0/delisting";
	public final static String YZ_SEARCHITEMPAGE_URL = "https://open.youzan.com/api/oauthentry/youzan.items.onsale/3.0.0/get";
	public final static String YZ_GETONEGOODSITEM_URL = "https://open.youzan.com/api/oauthentry/youzan.item/3.0.0/get";
	public final static String YZ_GOODS_SN_KEY = "CACHE_";

	/** 图片服务器域名地址 **/
	public final static String PICTURE_DOMAIN = PropertieFileReader.getString("PICTURE_DOMAIN");

	public static final String MENU_LOGS = "LOGS";

	public static final String MENU_PRODUCT = "PRODUCT";

	public static final String MENU_SYSTEM_INSTALL = "SYSTEM_INSTALL";

	public static final String MENU_GROUP_SHOP = "GROUP_SHOP";
	
	public static final String MENU_CHANNEL_SHOP = "CHANNEL_SHOP";
	
	public static final String GROUP_LIST_RESOURCE_KEY = "2_1";
	
	/**
	 * 集团店铺
	 */
	public final static String GROUP_ALLIANCE_SHOP = "GR_ALLIANCE_CODE";

	public final static Integer EVERY_PAGE_QUERY_SIZE = 100;

	public static final String QUEUE_FIX_ITEM = "cs_item_";
	
	public static final String QUEUE_FIX_STOCK = "-channel_stock_notice";

	public static final String REDIS_FIX_STRING = "CH_SC_AU_";
	
	public static final String SHOP_AUTH_ERP = "SHOP_AUTH_ERP_";
	
	public static final String QUEUE_FIX_CS_SYNC_STOCK = "CS_SYNC_STOCK_";
	/*****************  同步库存中心库存 ***************/
	public static final String QUEUE_FIX_CS_SYNC_STOCKCENTER = "CS_SYNC_STOCKCENTER_";

	/**
	 * 将调整单放入队列中，调整当的执行结果为"正在同步"
	 */
	public final static String EXECUTING = "正在同步";

	/**
	 * 京东上传图片，返回图片地址的前缀
	 */
	public final static String JD_IMG_URL_HEADER = "http://img10.360buyimg.com/imgzone/";

	/***** 一号店模板标题图片 *******/
	public final static String YHD_GOODS_INFO_URL = "http://d6.yihaodianimg.com/N02/M03/75/7B/CgQCslOw8MiAbsz7AAAIIwSkJyE93800.jpg";

	public final static String YHD_GOODS_MODEL_SHOW_URL = "http://d6.yihaodianimg.com/N01/M02/72/16/CgQCrVOw-t-AUrugAAAIVx3ywdw60900.jpg";

	public final static String YHD_GOODS_EDIT_URL = "http://d6.yihaodianimg.com/N01/M00/72/17/CgQCrVOw-_WAN7EoAAAIOcKBhHQ54200.jpg";

	public final static String YHD_GOODS_MODEL_INFO_URL = "http://d6.yihaodianimg.com/N01/M08/71/E7/CgQCrlOw_OqAHihOAAAJFyiGfcw26400.jpg";

	public final static String YHD_GOODS_DETAIL_SHOW_URL = "http://d6.yihaodianimg.com/N03/M06/71/BD/CgQCtFOw_wyAGquyAAALqPiSumo85000.jpg";

	public final static String YHD_GOODS_COLOR_SELECT_URL = "http://d6.yihaodianimg.com/N02/M03/75/83/CgQCsFOxAaeALt80AAAIoYBnieE94800.jpg";

	public final static String YHD_GOODS_SIZE_URL = "http://d6.yihaodianimg.com/N02/M0A/75/8F/CgQCslOxAj-ANp6nAAAIesY5K1s95700.jpg";

	public final static String YHD_GOODS_INFO_SUB_URL = "http://d6.yihaodianimg.com/N02/M0A/7A/25/CgQCsVO2fLiAdigHAAAFuXylnb043800.jpg";

	/***** 京东模板标题图片 *******/
	public final static String JD_GOODS_INFO_URL = "http://img10.360buyimg.com/imgzone/jfs/t190/117/1479195060/2083/405c8147/53b0f14dN08ece0eb.png";

	public final static String JD_GOODS_MODEL_SHOW_URL = "http://img10.360buyimg.com/imgzone/jfs/t166/312/1473901652/2135/5be637f6/53b0fa1eNa457f80b.png";

	public final static String JD_GOODS_EDIT_URL = "http://img10.360buyimg.com/imgzone/jfs/t202/75/1514926943/2105/3b43a480/53b0fbf7Naadefeee.png";

	public final static String JD_GOODS_MODEL_INFO_URL = "http://img10.360buyimg.com/imgzone/jfs/t175/19/1478498248/2327/61d84c71/53b0fceaNcfc2cf9c.png";

	public final static String JD_GOODS_DETAIL_SHOW_URL = "http://img10.360buyimg.com/imgzone/jfs/t184/31/1451470489/2984/64ae92a3/53b0ff0dNe842c23a.png";

	public final static String JD_GOODS_COLOR_SELECT_URL = "http://img10.360buyimg.com/imgzone/jfs/t181/134/1513494814/2209/5693efd4/53b101a8N883b196b.png";

	public final static String JD_GOODS_SIZE_URL = "http://img10.360buyimg.com/imgzone/jfs/t208/63/1527173419/2170/1c0213f3/53b1023fN683584ac.png";

	public final static String JD_GOODS_INFO_SUB_URL = "http://img10.360buyimg.com/imgzone/jfs/t205/91/1747321578/1465/39ce368f/53b67cbcNd947fb25.png";

	/***** 淘宝模板标题图片 *******/
	public final static String TB_GOODS_INFO_URL = "http://img02.taobaocdn.com/imgextra/i2/353050747/TB2OSfwXVXXXXa5XXXXXXXXXXXX-353050747.png";

	public final static String TB_GOODS_MODEL_SHOW_URL = "http://img02.taobaocdn.com/imgextra/i2/353050747/TB2pCLwXVXXXXbaXXXXXXXXXXXX-353050747.png";

	public final static String TB_GOODS_EDIT_URL = "http://img03.taobaocdn.com/imgextra/i3/353050747/TB2O_6wXVXXXXXdXXXXXXXXXXXX-353050747.png";

	public final static String TB_GOODS_MODEL_INFO_URL = "http://img03.taobaocdn.com/imgextra/i3/353050747/TB2iErwXVXXXXXpXXXXXXXXXXXX-353050747.png";

	public final static String TB_GOODS_DETAIL_SHOW_URL = "http://img03.taobaocdn.com/imgextra/i3/353050747/TB20UHwXVXXXXaFXXXXXXXXXXXX-353050747.png";

	public final static String TB_GOODS_COLOR_SELECT_URL = "http://img03.taobaocdn.com/imgextra/i3/353050747/TB25VTxXVXXXXXJXXXXXXXXXXXX-353050747.png";

	public final static String TB_GOODS_SIZE_URL = "http://img03.taobaocdn.com/imgextra/i3/353050747/TB2FFnxXVXXXXbXXXXXXXXXXXXX-353050747.png";

	public final static String TB_GOODS_INFO_SUB_URL = "http://img04.taobaocdn.com/imgextra/i4/134363478/TB2k.DRXVXXXXc3XXXXXXXXXXXX-134363478.png";

	/***** 美邦模板标题图片 *******/
	public final static String MB_GOODS_INFO_URL = "http://s.mb-go.com/pub6/style/images/goods_model/title_01_spxx.png";

	public final static String MB_GOODS_MODEL_SHOW_URL = "http://s.mb-go.com/pub6/style/images/goods_model/title_02_mtzs.png";

	public final static String MB_GOODS_EDIT_URL = "http://s.mb-go.com/pub6/style/images/goods_model/title_03_bjtj.png";

	public final static String MB_GOODS_MODEL_INFO_URL = "http://s.mb-go.com/pub6/style/images/goods_model/title_05_mtxx.png";

	public final static String MB_GOODS_DETAIL_SHOW_URL = "http://s.mb-go.com/pub6/style/images/goods_model/title_06_xjzs.png";

	public final static String MB_GOODS_COLOR_SELECT_URL = "http://s.mb-go.com/pub6/style/images/goods_model/title_07_ysxz.png";

	public final static String MB_GOODS_SIZE_URL = "http://s.mb-go.com/pub6/style/images/goods_model/title_08_cmgg.png";

	public final static String MB_GOODS_INFO_SUB_URL = "http://img.mbanggo.com/sources/detailzhishu/images/modle_news01.png";
	
	/***** 京东店铺权限设置配置项  *******/
	public final static String JD_AUTH_URl = "https://auth.360buy.com/oauth/authorize";
	public final static String JD_TOKEN_URl = "https://auth.360buy.com/oauth/token";
	public final static String RESPONSE_TYPE = "response_type=code";

	public final static String REDIRECT_URI = PropertieFileReader.getString("redirect_uri");

	
	public final static String TB_AUTH_URl = "https://oauth.taobao.com/authorize";
	public final static String TB_TOKEN_URl = "https://oauth.taobao.com/token";
	public final static String TB_TOKEN_VIEW = "view=web";
	
	public final static String AREA_TYPE_PROVICE = "1";
	public final static String AREA_TYPE_CITY = "2";
	public final static String AREA_TYPE_DISTINCT = "3";
	public final static String AREA_TYPE_TOWN = "4";
	
	/**
	 * 造型师、精品商城接口类型
	 */
	public static class JPSC_METHOD_TYPE {
		/** 售商品列表 **/
		public static final String GETSKU = "IInterface_GetOnSaleGoods.svc/GetOnSaleGoods";
		/** 库存同步  */
		public static final String SYNSTOCK = "IInterface_OSStock.svc/SingleSkuStock";
		/** 商品上下架  */
		public static final String SHELVESGOODS = "IInterface_GetOnSaleGoods.svc/ShelvesGoods";
		/** 商品名称 **/
		public static final String GOODSTITLE = "IInterface_SynProductInfo.svc/SynchronizationProductName";
		/** 商品价格 **/
		public static final String GOODSPRICE = "IInterface_SynProductInfo.svc/SynchronizationProductPrice";
	}
	
	/**
	 * 造型师、精品商城接口类型
	 */
	public static class BG_METHOD_TYPE {
		/** 售商品列表 **/
		public static final String GETSKU = "stock-center-service-api-provider/syncPlatformSelfStock.htm";
		/** 库存同步  */
		public static final String SYNSTOCK = "stock-center-service-api-provider/syncPlatformSelfStock.htm";
	}
}

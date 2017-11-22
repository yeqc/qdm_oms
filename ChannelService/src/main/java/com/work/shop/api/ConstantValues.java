/* 
 *       __        __           _______    _ 
 *      / / __    / /  __   __  \_  __/   |_|    ___     _____
 * 	   / / /  \  / /  / /  / /   / /     / /    / _ |   |  _  |  
 *    / /_/ /\ \/ /  / /__/ /   / /     / /    / __ |   / | | |
 *    \____/  \__/   \_____/   / /     /___\  /_/ | |  /_/  |_|
 *                      __    / /                 |/
 *                      \ \__/ /
 *                       \____/
 * 
 */

package com.work.shop.api;

import java.util.Date;

import org.apache.commons.lang.StringUtils;



/** 
 * @author 吴健  (HQ01U8435)	Email : wujian@metersbonwe.com 
 * @version 创建时间：2013-7-18 上午8:56:59 
 */
public class ConstantValues {

	/**
	 * 拍拍渠道code
	 */
	public static final String PAIPAI_CHANNEL_CODE = "A02588S004";
	/**
	 * 淘宝渠道code
	 */
	public static final String TAOBAO_CHANNEL_CODE = "A02588S003";

	/**
	 * 京东 MC渠道code
	 */
	public static final String JD_CHANNEL_CODE = "A02588S018";

	/**
	 * 京东 mb渠道code
	 */
	public static final String JD_MB_CHANNEL_CODE = "A02588S022";
	
	/**
	 * 苏宁
	 */
	public static final String SN_CHANNEL_CODE = "A02588S019";

	/**
	 * 一号店
	 */
	public static final String YIHAODIAN_CHANNEL_CODE = "A02588S011";

	/**
	 * 微购
	 */
	public static final String WEIGOU_CHANNEL_CODE = "A02588S021";

	/**
	 * 精品商城-erp渠道号
	 */
	public static final String ERP_CHANNEL_CODE = "A00010S001";
	
	
	public static final String SN_APP_KEY = "14cbe47d3a4bb878f677c241f9002d36";
	
	public static final String SN_APP_SECRET = "ae5f402b0284f1234d66ad84ebdfe842";
	
	public static final String SN_FORMT_JSON = "json";
	
	public static final String SN_API_VERSION = "v1.2";
	
	/**
	 * 获取随机字符串-日期+4位随机编码
	 */
	private static int systemCode = 0;
	public synchronized static String getRandomString() {
		String str = "";
		systemCode += 1;
		if (systemCode > 0 && systemCode < 10) {
			str = "000" + systemCode;
		}
		if (systemCode >= 10 && systemCode < 100) {
			str = "00" + systemCode;
		}
		if (systemCode >= 100 && systemCode < 1000) {
			str = "0" + systemCode;
		}
		if (systemCode >= 1000 && systemCode < 10000) {
			str = "" + systemCode;
		}
		if (systemCode >= 10000) {
			systemCode = 0;
			str = "0001";
		}
		if (StringUtils.isNotBlank(str)) {
			str = com.work.shop.oms.utils.TimeUtil.format3Date(new Date()) + str;
		}
		return str;
	}
}

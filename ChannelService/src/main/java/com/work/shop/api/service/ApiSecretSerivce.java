package com.work.shop.api.service;

import java.util.Map;

/**
 * 获取各个渠道安全验证参数
 * @author Administrator
 *
 */
public interface ApiSecretSerivce {
	
	/**
	 * 获取各个店铺渠道接口安全验证参数
	 * 
	 * 对接bgchanneldb.interface_properties
	 * 
	 * @param channelCode
	 * @param shopCode
	 * @return 
	 */
	Map<String,String> getSecretInfo(String channelCode,String shopCode);
	
}

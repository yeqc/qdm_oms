package com.work.shop.service;

import java.util.List;


import com.work.shop.bean.ChannelApiLog;
import com.work.shop.bean.InterfaceProperties;
import com.work.shop.bean.InterfacePropertiesExample;
import com.work.shop.vo.InterfacePropertiesVo;

public interface InterfacePropertiesService {
	
	/**
	 * 查询所有配置信息 
	 ***/
	public List<InterfaceProperties> getInterfacePropertiesList(InterfacePropertiesExample interfacePropertiesExample );
	
    /***
     * 
     * 获取所有店铺 数量 
     * @author chenrui;  
	 *  @return int;
     ***/
	public int getInterfacePropertiesCount(InterfacePropertiesExample interfaceProperties );
	
	/**
	 * 插入配置信息; 
	 ****/
	public int insertInterfaceProperties(InterfaceProperties interfaceProperties);
	
	
	/**
	 * 查询配置信息 
	 ***/	
	public List<InterfaceProperties> queryInterfaceProperties(InterfaceProperties interfaceProperties);
	
	
	/**
	 * 修改配置信息; 
	 ****/
	public int updateInterfaceProperties(InterfaceProperties interfaceProperties);
	
	
	/**
	 * 删除配置信息; 
	 ****/
	public int deleteMultiInterfaceProperties(String ids);
	

	/**
	 * 查询所有配置信息包括（包括：渠道名称，店铺名称） 
	 ***/
	public List<InterfacePropertiesVo> getInterfacePropertiesVoList(InterfacePropertiesExample interfacePropertiesExample );
	
	/**
	 * 查询所有配置信息树龄包括（包括：渠道名称，店铺名称） 
	 ***/
	public int getInterfacePropertiesVoCount(InterfacePropertiesExample interfaceProperties );
	
	/**
	 * 将更新后的权限token更新到权限库中
	 * @param apiLog
	 * @param channelCode
	 * @param shopCode
	 * @param newToken
	 */
	public void updateTokenAuthInfo(ChannelApiLog apiLog, String channelCode, String shopCode, String newToken);
	
	/**
	 * 将更新后鉴权信息放入redis
	 * @param shopCode
	 */
	public boolean syncSecurityInfo(String shopCode);
}

package com.work.shop.service;

import com.work.shop.bean.ChannelInfo;
import com.work.shop.bean.ChannelInfoExample;
import com.work.shop.util.extjs.Paging;
import com.work.shop.vo.JsonResult;

public interface ChannelInfoService {
	/**
	 * 渠道列表查询
	 * @author guoduanduan
	 * @param channelInfoExample
	 * @return paging
	 * */
	public Paging getChannelInfoList(ChannelInfoExample channelInfoExample); 
	
	/**
	 * 渠道单个对象查询
	 * @author guoduanduan
	 * @param id
	 * @return ChannelInfo
	 * */
    public 	ChannelInfo getChannelInfo(int id);
    
    
	/**
	 * 数据新增 修改
	 * @author guoduanduan
	 * @param channelInfo 渠道对象
	 * @return jsonResult
	 * */
	public JsonResult updateChannelInfo(ChannelInfo channelInfo);
	
	/**
	 * 渠道移除
	 * @author guoduanduan
	 * @param ids 主键ids
	 * @return jsonResult
	 * */
	public JsonResult deleteChannelInfo(String ids);
	
	/**
	 * 渠道状态激活
	 * @author guoduanduan
	 * @param ids 主键ids
	 * @return jsonResult
	 * */
	public JsonResult updateChannelStatus(String ids,String channelStatus);
	
	/**
	 * 根据Channeltype查找
	 * 
	 * */
	public JsonResult findShopChannelInfoByChanneltype(int ChannelType);

}

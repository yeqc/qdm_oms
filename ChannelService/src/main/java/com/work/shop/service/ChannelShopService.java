package com.work.shop.service;

import java.util.Date;
import java.util.List;

import com.work.shop.bean.ChannelShop;
import com.work.shop.vo.JsonResult;

public interface ChannelShopService {
	
	/***
     * 查询店铺信息 
	 * @param shopCode 店铺code;
	 * @return 店铺信息对象
     ***/
	public ChannelShop selectChannelShopByShopCode(String shopCode);
	
	/***
     * 查询父店铺信息 
     * @author zhouyouming;  
	 * @param shopCode 子店铺code;
	 * @return 封装了父店铺信息 的JsonResult对象
     ***/
	public JsonResult selectParentChannelShop(String shopCode);
	

	/***
     * 查询当前店铺信息 
     * @author zhouyouming;  
	 * @param shopCode 子店铺code;
	 * @return 封装了当前店铺信息 的JsonResult对象
     ***/
	public JsonResult selectCurrentChannelShop(String shopCode);
	
	
	/***
     * 查询子店铺信息 
     * @author zhouyouming;  
	 * @param shopCode 子店铺code;
	 * @return 封装了子店铺信息 的JsonResult对象
     ***/
	public JsonResult selectChildChannelShop(String shopCode);

	/**
	 * 根据所属渠道查找店铺
	 * @param channelCode
	 * @return
	 */
	public JsonResult findShopChannelShopByChannelCode(String channelCode);
	/**
	 * 批量添加
	 * @param ChannelShopList
	 * @return
	 */
	public JsonResult addShopChannelShop(List<ChannelShop> ChannelShopList);
	/**
	 * 
	 */

	public void updateChannelShop(String shopCode, String channelCode, Date expiresTime);
}

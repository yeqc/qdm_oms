package com.work.shop.service;

import java.util.List;

import com.work.shop.bean.OmsChannelInfo;
import com.work.shop.vo.JsonResult;
import com.work.shop.vo.OpenShopChannelInfoVO;

public interface OpenShopChannelInfoService {

	/**
	 * 列表查询
	 * 
	 * */
	public JsonResult searchOpenShopChannelInfoList(OpenShopChannelInfoVO searchVo);

	/**
	 * 通过channelId、channelCode查询
	 * 
	 * */
	public JsonResult searchOnlyChannelInfo(OmsChannelInfo searchVo);

	/**
	 * 渠道信息单个对象查询
	 * 
	 * */
	public JsonResult searchOpenShopChannelInfo(int id);
	

	/**
	 * 根据店铺id查找
	 * 
	 * */
	public OmsChannelInfo findOmsChannelInfoBychannelcode(String channelcode);
	

	/**
	 * 数据新增 修改
	 * 
	 * */
	public JsonResult addOrUpdateOpenShopChannelInfo(OmsChannelInfo omsChannelInfo);

	/**
	 * 渠道移除
	 * 
	 * */
	public JsonResult deleteOpenShopChannelInfo(List<String> channelCodeList);
	/**
	 * 批量添加
	 * @param omsChannelInfos
	 */
	public JsonResult addOpenShopChannelInfo(List<OmsChannelInfo> omsChannelInfos);

}

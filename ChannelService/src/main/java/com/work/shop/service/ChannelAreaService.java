package com.work.shop.service;

import java.util.List;
import java.util.Map;

import com.work.shop.bean.SystemArea;
import com.work.shop.bean.SystemRegion;
import com.work.shop.bean.SystemRegionArea;
import com.work.shop.bean.SystemRegionMatch;
import com.work.shop.util.extjs.PageHelper;
import com.work.shop.util.extjs.Paging;
import com.work.shop.vo.ChannelAreaMatchVo;
import com.work.shop.vo.ChannelAreaVo;
import com.work.shop.vo.JsonResult;

public interface ChannelAreaService {
	/**
	 * 获取地区列表
	 * @author guoduanduan
	 * @param systemAreaExample
	 * @return Paging
	 **/
	public Paging getSystemAreaList(ChannelAreaVo systemArea, PageHelper helper);
	
	
	/**
	 * 插入或者修改地区映射
	 * @author guoduanduan
	 * @param list
	 * @return JsonResult
	 **/
	public JsonResult insertSystemArea(List<SystemArea> list);
	
	
	/**
	 * 移除地区映射关系
	 * @author guoduanduan
	 * @param ids id数组
	 * @return JsonResult
	 **/
	
	public JsonResult deleteSystemArea(String ids);
	
	
	/**
	 * 修改对象信息
	 * @author guoduanduan
	 * @param SystemArea
	 * @return JsonResult
	 **/
	
	public JsonResult updateSystemArea(SystemArea systemArea);
	
	/**
	 * 匹配新旧地区信息
	 * @author xudanhua
	 * @param SystemArea
	 * @return JsonResult
	 **/
	
	public JsonResult matchSystemRegion(SystemArea systemArea);
	
	/**
	 * 获取渠道地区对象信息
	 * @author
	 * @param systemArea
	 * @return SystemArea
	 **/
	public SystemArea getSystemArea(SystemArea systemArea);
	
	
	/**
	 * 获取地区对象信息
	 * @author
	 * @param region
	 * @return SystemRegion
	 **/
	public List<SystemRegionArea> getSystemRegion(SystemRegionArea region);
	
	/**
	 * 获取地区对象信息
	 * @author
	 * @param region
	 * @return SystemRegion
	 **/
	public List<SystemRegionArea> getOS_SystemRegionArea(SystemRegionArea region,String searchType);
	
	/**
	 * 获取地区对象信息
	 * @author
	 * @param region
	 * @return SystemRegion
	 **/
	public List<SystemRegionArea> getOS_SystemRegion(SystemRegionArea region,String searchType);
	
	/**
	 * 获取新旧地区匹配列表
	 * @author guoduanduan
	 * @param systemRegionMatchExample
	 * @return Paging
	 **/
	public Paging getSystemRegionMatchList(ChannelAreaMatchVo systemRegionMatch, PageHelper helper);
	
	
	/**
	 * 获取渠道地区对象信息
	 * @author
	 * @param systemArea
	 * @return SystemArea
	 **/
	public SystemRegionMatch getSystemRegionMatch(SystemRegionMatch systemRegionMatch);
	
	public List<SystemRegionArea> getSystemRegionArea(SystemRegionArea region);
	
	public List<SystemRegionArea> getNew_SystemRegionArea(SystemRegionArea region,String searchType);

	public JsonResult updateSystemRegionMatch(SystemRegionMatch systemRegionMatch);

	public Paging getSystemRegionAreaList(SystemRegionArea systemRegionArea,PageHelper helper);
	
	public SystemRegion getSystemRegion(SystemRegion systemRegion);
	
	public Map<String,String> getSystemAreaMatch(String province,String city,String distinct,String channelCode);
}

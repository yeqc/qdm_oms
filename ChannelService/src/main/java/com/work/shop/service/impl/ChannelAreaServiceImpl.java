package com.work.shop.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.work.shop.bean.SystemArea;
import com.work.shop.bean.SystemAreaExample;
import com.work.shop.bean.SystemAreaExample.Criteria;
import com.work.shop.bean.SystemRegion;
import com.work.shop.bean.SystemRegionArea;
import com.work.shop.bean.SystemRegionAreaExample;
import com.work.shop.bean.SystemRegionMatch;
import com.work.shop.bean.SystemRegionMatchExample;
import com.work.shop.dao.BgChannelDbTableMapper;
import com.work.shop.dao.GoodsPropertyMapper;
import com.work.shop.dao.SystemAreaMapper;
import com.work.shop.dao.SystemRegionAreaMapper;
import com.work.shop.dao.SystemRegionMapper;
import com.work.shop.dao.SystemRegionMatchMapper;
import com.work.shop.service.ChannelAreaService;
import com.work.shop.util.Constants;
import com.work.shop.util.StringUtil;
import com.work.shop.util.extjs.PageHelper;
import com.work.shop.util.extjs.Paging;
import com.work.shop.vo.ChannelAreaMatchVo;
import com.work.shop.vo.ChannelAreaVo;
import com.work.shop.vo.JsonResult;

@Service("channelAreaService")
public class ChannelAreaServiceImpl implements ChannelAreaService{
	
	private static Logger logger = Logger.getLogger(ChannelAreaServiceImpl.class);
	
	@Resource(name = "systemAreaMapper")
	private SystemAreaMapper systemAreaMapper;
	
	@Resource(name = "systemRegionMapper")
	private SystemRegionMapper systemRegionMapper;
	
	@Resource(name = "systemRegionAreaMapper")
	private SystemRegionAreaMapper systemRegionAreaMapper;
	
	@Resource(name = "goodsPropertyMapper")
	private GoodsPropertyMapper goodsPropertyMapper;
    
	@Resource(name = "systemRegionMatchMapper")
	private SystemRegionMatchMapper systemRegionMatchMapper;
	
	@Resource
	private BgChannelDbTableMapper bgChannelDbTableMapper;

	/**
	 * 获取地区列表
	 * @author guoduanduan
	 * @param systemArea
	 * @param helper
	 * @return Paging
	 **/
	public Paging getSystemAreaList(ChannelAreaVo systemArea, PageHelper helper){
		SystemAreaExample systemAreaExample = new SystemAreaExample();
		systemAreaExample.setOrderByClause("Id desc");
		Criteria criteria = systemAreaExample.or();
		criteria.limit(helper.getStart(), helper.getLimit());
		if(StringUtil.isNotNull(systemArea.getChannelCode())){
			criteria.andChannelCodeEqualTo(systemArea.getChannelCode());
		}
		if(StringUtil.isNotNull(systemArea.getAreaId())){
			criteria.andAreaIdEqualTo(systemArea.getAreaId());//地区编码
		}
		if(StringUtil.isNotNull(systemArea.getAreaName())){ //地区名称
			criteria.andAreaNameLike("%" + systemArea.getAreaName() + "%");
		}
		if(StringUtil.isNotNull(systemArea.getOsRegionId())){
			criteria.andOsRegionIdEqualTo(systemArea.getOsRegionId());
		}
		if(StringUtil.isNotNull(systemArea.getOsRegionName())){
			criteria.andOsRegionNameLike("%" + systemArea.getOsRegionName() + "%");//os区域名称
		}
		if(StringUtil.isNotNull(systemArea.getAreaType())){
			criteria.andAreaTypeEqualTo(systemArea.getAreaType());
		}
		if(StringUtil.isNotNull(systemArea.getAreaStatus())){
			if("0".equals(systemArea.getAreaStatus())){ //0为未映射的地区
				criteria.andMatchTypeNotEqualTo(0);
			}else{
				criteria.andMatchTypeEqualTo(0); //1为已经映射的地区
			}
		}
		Paging paging = new Paging();
		List<SystemArea> list = systemAreaMapper.selectByExample(systemAreaExample);
		int count = systemAreaMapper.countByExample(systemAreaExample);
		paging.setRoot(list);
		paging.setTotalProperty(count);
		return paging;
	}

	/**
	 * 插入或者修改地区映射
	 * @author guoduanduan
	 * @param systemArea
	 * @return JsonResult
	 **/
	public JsonResult insertSystemArea(List<SystemArea> list){
		JsonResult jsonResult = new JsonResult();
		if(StringUtil.isNotNullForList(list)){
			for(int i=0;i<list.size();i++){
				SystemArea areaTemp = list.get(i);
				SystemAreaExample example = new SystemAreaExample();
				Criteria criteria = example.or();
				criteria.andAreaNameEqualTo(areaTemp.getAreaName());
				//criteria.andAreaTypeEqualTo(areaTemp.getAreaType());
				criteria.andAreaIdEqualTo(areaTemp.getAreaId());
				criteria.andChannelCodeEqualTo(areaTemp.getChannelCode());
				List<SystemArea> areas = this.systemAreaMapper.selectByExample(example);
				if (StringUtil.isNotNullForList(areas)) {
					SystemArea obj = areas.get(0);
					obj.setAreaName(areaTemp.getAreaName());
					obj.setAreaId(areaTemp.getAreaId());
					//obj.setPid(areaTemp.getPid());
					obj.setPareaCode(areaTemp.getPareaCode());
					obj.setOsRegionId(areaTemp.getOsRegionId());
					obj.setOsRegionName(areaTemp.getOsRegionName());
//					if (obj.getMatchType()!=0) {
						obj.setMatchType(areaTemp.getMatchType());
//						obj.setRelateOsArea(areaTemp.getRelateOsArea());
						obj.setZip(areaTemp.getZip());
//					}
					systemAreaMapper.updateByPrimaryKeySelective(obj);
				} else {
					systemAreaMapper.insertSelective(areaTemp);
				}
			}
			jsonResult.setIsok(true);
			jsonResult.setMessage("添加地区映射成功");
		} else {
			jsonResult.setIsok(false);
			jsonResult.setMessage("地区映射列表为空！");
		}
		return jsonResult;
	}
	
	
	/**
	 * 移除地区映射关系
	 * @author guoduanduan
	 * @param ids id数组
	 * @return JsonResult
	 **/
	public JsonResult deleteSystemArea(String ids){
		JsonResult jsonResult = new JsonResult();
		if(!"".equals(ids)){
			Map<String, Object> resultMap = new HashMap<String, Object>();	
			String [] idsArr =  ids.split(",");
			resultMap.put("idArr", idsArr);
			int i =  bgChannelDbTableMapper.deleteByPrimaryKeys(resultMap);
			if(i==idsArr.length){
				jsonResult.setIsok(true);
				jsonResult.setMessage("删除映射成功！");
			}else{
				jsonResult.setIsok(false);
				jsonResult.setMessage("移除失败！");
			}
		}
		return jsonResult;
	}
	//地区信息修改
	public JsonResult updateSystemArea(SystemArea systemArea){
		JsonResult jsonResult= new JsonResult();
		String regionId = systemArea.getOsRegionId();
		Integer id = systemArea.getId();
		if(id !=null && id>0 && StringUtil.isNotNull(regionId)){
			SystemArea uptSystemArea = systemAreaMapper.selectByPrimaryKey(id);
			SystemRegionArea systemRegionArea = systemRegionAreaMapper.selectByPrimaryKey(regionId);
			if (systemRegionArea!=null) {
				uptSystemArea.setOsRegionId(systemRegionArea.getRegionId());
				uptSystemArea.setOsRegionName(systemRegionArea.getRegionName());
				uptSystemArea.setOsParentId(systemRegionArea.getParentId());
				uptSystemArea.setMatchType(0);
				systemAreaMapper.updateByPrimaryKeySelective(uptSystemArea);
			}
			jsonResult.setIsok(true);
		}
		return jsonResult;
	}

	//地区信息修改
	public JsonResult matchSystemRegion(SystemArea systemArea){
		JsonResult jsonResult= new JsonResult();

		List<SystemRegion> regionList = goodsPropertyMapper.selectAllSystemRegion();
		if (regionList!=null) {
			int totalMatchSize = 0;
			for (SystemRegion systemRegion : regionList) {
				SystemRegionArea regionArea = new SystemRegionArea();
				regionArea.setParentName(systemRegion.getParentName());
				regionArea.setRegionName(systemRegion.getRegionName());
				regionArea.setRegionType(systemRegion.getRegionType());
				List<SystemRegionArea> regionAreaList = getSystemRegionArea(regionArea);
				if (regionAreaList!=null && regionAreaList.size()==1) {
					//匹配度 --> 0 ： 完全匹配；1：部分匹配；2：完全不匹配
					SystemRegionMatch regionMatch = new SystemRegionMatch();
					regionMatch.setOldRegionId(systemRegion.getRegionId()+"");
					regionMatch.setOldRegionName(systemRegion.getRegionName());
					regionMatch.setOldRegionType(systemRegion.getRegionType()+"");
					regionMatch.setNewAreaId(regionAreaList.get(0).getRegionId());
					regionMatch.setNewAreaName(regionAreaList.get(0).getRegionName());
					regionMatch.setPareaCode(systemRegion.getParentId()+"");
					regionMatch.setMatchType("0");
					totalMatchSize ++;
					systemRegionMatchMapper.insertSelective(regionMatch);
				}else{
					SystemRegionMatch regionMatch = new SystemRegionMatch();
					regionMatch.setOldRegionId(systemRegion.getRegionId()+"");
					regionMatch.setOldRegionName(systemRegion.getRegionName());
					regionMatch.setOldRegionType(systemRegion.getRegionType()+"");
					regionMatch.setPareaCode(systemRegion.getParentId()+"");
					//匹配度 --> 0 ： 完全匹配；1：部分匹配；2：完全不匹配
					if (regionAreaList!=null && regionAreaList.size()>1) {
						regionMatch.setNewAreaId(regionAreaList.get(0).getRegionId());
						regionMatch.setNewAreaName(regionAreaList.get(0).getRegionName());
						StringBuffer sbRelationOsArea = new StringBuffer();
						for (SystemRegionArea newSystemRegionArea : regionAreaList) {
//							data : [ [ '0', '匹配' ], [ '1', '部分匹配' ],[ '2', '不匹配' ],[ '', '全部' ] ],
							sbRelationOsArea.append("[ '").append(newSystemRegionArea.getRegionId()).append("', '")
								.append(newSystemRegionArea.getRegionName()).append("' ],");
						}
						if (sbRelationOsArea.length()>0) {
							String relationOsArea = sbRelationOsArea.substring(0, sbRelationOsArea.length()-1).toString();
							relationOsArea = ""+relationOsArea+"";
							regionMatch.setRelateOsArea(relationOsArea);
						}
						regionMatch.setMatchType("1");
					}else{
						regionMatch.setMatchType("2");
					}
					systemRegionMatchMapper.insertSelective(regionMatch);
				}
			}
			System.out.println("匹配完成，总条数："+totalMatchSize);
		}
		jsonResult.setIsok(true);
		jsonResult.setMessage("匹配完成!");
		return jsonResult;
	}
	
	/**
	 * 获取渠道地区对象信息
	 */
	@Override
	public SystemArea getSystemArea(SystemArea systemArea) {
		SystemArea childSystemArea = this.systemAreaMapper.selectByPrimaryKey(systemArea.getId());
		if (StringUtil.isNotEmpty(childSystemArea.getPareaCode())) {
			SystemAreaExample example = new SystemAreaExample();
			com.work.shop.bean.SystemAreaExample.Criteria criteria = example.or();
			criteria.andAreaIdEqualTo(childSystemArea.getPareaCode());
			criteria.andChannelCodeEqualTo(childSystemArea.getChannelCode());
			criteria.limit(0, 10);
			List<SystemArea> parentSystemAreaList = this.systemAreaMapper.selectByExample(example);
			if (parentSystemAreaList!=null && parentSystemAreaList.size()>0) {
				childSystemArea.setChParentName(parentSystemAreaList.get(0).getAreaName());
			}
		}
		return childSystemArea;
	}

	/**
	 * 获取地区对象信息
	 */
	@Override
	public List<SystemRegionArea> getSystemRegionArea(SystemRegionArea region) {
		
		String regionType = String.valueOf(region.getRegionType());
		String regionName = region.getRegionName();
		regionName = matchRegionName(regionName);
		if ("其他区县".equals(regionName)) {
			regionType = "3";
		}
		region.setRegionName(regionName);
		return getOS_SystemRegionArea(region,regionType);
	}
	
	/**
	 * 获取地区对象信息（新街口）
	 */
	@Override
	public List<SystemRegionArea> getSystemRegion(SystemRegionArea region) {
		SystemRegionAreaExample example = new SystemRegionAreaExample();
		com.work.shop.bean.SystemRegionAreaExample.Criteria criteria = example.or();
		String regionName = region.getRegionName();
		if (regionName != null && regionName.length() == 1) {
			// 匹配区域类型单个字符名称 例如“县”、“区”、“乡”等 为OS的"其他区县"
			regionName = "其他区县";
			region.setRegionType(3);
		} else {
			// 删除行政级别名称 模糊匹配
			regionName = this.subAreaName(regionName, "国");
			regionName = this.subAreaName(regionName, "省");
			regionName = this.subAreaName(regionName, "市");
			regionName = this.subAreaName(regionName, "自治区");
			regionName = this.subAreaName(regionName, "开发区");
			regionName = this.subAreaName(regionName, "地区");
			regionName = this.subAreaName(regionName, "新区");
			regionName = this.subAreaName(regionName, "区");
			regionName = this.subAreaName(regionName, "自治州");
			regionName = this.subAreaName(regionName, "乡");
			regionName = this.subAreaName(regionName, "县");
			regionName = this.subAreaName(regionName, "镇");
			//regionName = this.subAreaName(regionName, "乡");
			regionName = this.subAreaName(regionName, "州");
			regionName = this.subAreaName(regionName, "行委");
			regionName = this.subAreaName(regionName, "经济开发区");
			regionName = this.subAreaName(regionName, "城区");
			regionName = this.subAreaName(regionName, "市区内");
			regionName = this.subAreaName(regionName, "农场");
			regionName = this.subAreaName(regionName, "街道");
		}
		criteria.andRegionNameLike(regionName + "%");
		criteria.andRegionTypeEqualTo(region.getRegionType());
		return this.systemRegionAreaMapper.selectByExample(example);
	}
	
	/**
	 * 获取地区对象信息（新街口）
	 */
	@Override
	public List<SystemRegionArea> getOS_SystemRegionArea(SystemRegionArea region,String searchType) {
		Map<String,Object> map = new HashMap<String,Object>();
		List<SystemRegionArea> list = null;
		if ("0".equals(searchType)) {
			map.put("region_name",  region.getRegionName());
			map.put("region_type",  region.getRegionType());
			map.put("parent_name",  region.getParentName());
			list = goodsPropertyMapper.selectSystemRegionArea(map);
		}else {
			String parentName = region.getParentName();
			if (parentName!=null) {
				parentName = matchRegionName(parentName);
			}
			String regionName = region.getRegionName();
			if (regionName!=null) {
				regionName = matchRegionName(regionName);
			}
			map.put("region_name",  regionName);
			map.put("region_type",  region.getRegionType());
			map.put("parent_name",  parentName);
			list = goodsPropertyMapper.selectSystemRegionAreaLike(map);
		}
		return list;
	}
	
	/**
	 * 获取地区对象信息（新街口）
	 */
	@Override
	public List<SystemRegionArea> getOS_SystemRegion(SystemRegionArea region,String searchType) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<SystemRegionArea> list = new ArrayList<SystemRegionArea>();
		if ("0".equals(searchType)) {
			map.put("region_name",  region.getRegionName());
			map.put("region_type",  region.getRegionType());
			map.put("parent_name",  region.getParentName());
			list = goodsPropertyMapper.selectSystemRegion(map);
		} else {
			String parentName = region.getParentName();
			if (StringUtil.isNotEmpty(parentName)) {
				parentName = matchRegionName(parentName);
			}
			String regionName = region.getRegionName();
			if (StringUtil.isNotEmpty(regionName)) {
				regionName = matchRegionName(regionName);
			}
			map.put("region_name",  regionName);
			map.put("region_type",  region.getRegionType());
			map.put("parent_name",  parentName);
			list = goodsPropertyMapper.selectSystemRegionLike(map);
		}
		return list;
	}
	
	/**
	 * 获取地区对象信息（新街口）
	 */
	@Override
	public List<SystemRegionArea> getNew_SystemRegionArea(SystemRegionArea region,String searchType) {
		
		Map<String,Object> map = new HashMap<String,Object>();
		
		List<SystemRegionArea> list = null;
		if ("0".equals(searchType)) {
			map.put("region_name",  region.getRegionName());
			map.put("region_type",  region.getRegionType());
			map.put("parent_name",  region.getParentName());
			list = goodsPropertyMapper.selectSystemRegionArea(map);
		}else {
			String parentName = region.getParentName();
			String regionName = region.getRegionName();
			if (regionName!=null) {
				regionName = matchRegionName(regionName);
			}
			map.put("region_name",  regionName);
			map.put("region_type",  region.getRegionType());
			map.put("parent_name",  parentName);
			list = goodsPropertyMapper.selectNewSystemRegionAreaLike(map);
		}
		return list;
	}
	
	private String matchRegionName(String regionName){
		if (regionName != null && regionName.length() == 1) {
			// 匹配区域类型单个字符名称 例如“县”、“区”、“乡”等 为OS的"其他区县"
			regionName = "其他区县";
		} else {
			// 删除行政级别名称 模糊匹配
			regionName = this.subAreaName(regionName, "国");
			regionName = this.subAreaName(regionName, "省");
			regionName = this.subAreaName(regionName, "自治州");
			regionName = this.subAreaName(regionName, "州");
			regionName = this.subAreaName(regionName, "市");
			regionName = this.subAreaName(regionName, "自治区");
			regionName = this.subAreaName(regionName, "经济开发区");
			regionName = this.subAreaName(regionName, "开发区");
			regionName = this.subAreaName(regionName, "地区");
			regionName = this.subAreaName(regionName, "新区");
			regionName = this.subAreaName(regionName, "城区");
			regionName = this.subAreaName(regionName, "市区内");
			regionName = this.subAreaName(regionName, "区");
			regionName = this.subAreaName(regionName, "乡");
			regionName = this.subAreaName(regionName, "县");
			regionName = this.subAreaName(regionName, "镇");
			regionName = this.subAreaName(regionName, "行委");
			regionName = this.subAreaName(regionName, "农场");
			regionName = this.subAreaName(regionName, "街道");
			regionName = this.subAreaName(regionName, "市辖");//jumei
			regionName = this.subAreaName(regionName, "省直辖县级行政单位");//jumei
			regionName = this.subAreaName(regionName, "自治区直辖县级行政单位");//jumei
			// trim  地区数据空格
			regionName = regionName.trim();
		}
		return regionName;
	}
	
	/**
	 * 删除行政级别名称 以利于模糊匹配（只从最后面匹配截取，不能从中间截取）
	 * @param regionName 区域名称
	 * @param delName 需要截取掉的字符串
	 * @return String
	 */
	private String subAreaName(String regionName, String delName) {
		int delLength = delName.length(); 
		int areaLength = regionName.length();
		if (areaLength < delLength) {
			return regionName;
		}
		int delIndex = regionName.lastIndexOf(delName);
		String finallyName = "";
		try {
			// 截取字符串长度+ 需要截取字符串在区域名称中的下标  总长度 == 区域名称的长度 需要截取
			if ((delLength+delIndex) == areaLength) { // 含有需要截取掉的字符串
				finallyName = regionName.substring(0, delIndex);
			} else {
				finallyName = regionName;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return finallyName;
	}
	
	/**
	 * 获取地区列表
	 * @author guoduanduan
	 * @param systemArea
	 * @param helper
	 * @return Paging
	 **/
	public Paging getSystemRegionMatchList(ChannelAreaMatchVo systemRegionMatch, PageHelper helper){
		SystemRegionMatchExample systemRegionMatchExample = new SystemRegionMatchExample();
		systemRegionMatchExample.setOrderByClause("Id desc");
		SystemRegionMatchExample.Criteria criteria = systemRegionMatchExample.or();
		criteria.limit(helper.getStart(), helper.getLimit());
		
		if(StringUtil.isNotNull(systemRegionMatch.getOldRegionId())){
			criteria.andOldRegionIdEqualTo(systemRegionMatch.getOldRegionId());//地区编码
		}
		if(StringUtil.isNotNull(systemRegionMatch.getOldRegionName())){ //地区名称
			criteria.andOldRegionNameLike("%" + systemRegionMatch.getOldRegionName() + "%");
		}
		if(StringUtil.isNotNull(systemRegionMatch.getNewAreaId())){
			criteria.andNewAreaIdEqualTo(systemRegionMatch.getNewAreaId());
		}
		if(StringUtil.isNotNull(systemRegionMatch.getNewAreaName())){
			criteria.andNewAreaNameLike("%" + systemRegionMatch.getNewAreaName() + "%");
		}
		if(StringUtil.isNotNull(systemRegionMatch.getOldRegionType())){
			criteria.andOldRegionTypeEqualTo(systemRegionMatch.getOldRegionType());
		}
		if(StringUtil.isNotNull(systemRegionMatch.getMatchType())){
			criteria.andMatchTypeEqualTo(systemRegionMatch.getMatchType());
		}
		Paging paging = new Paging();
		List<SystemRegionMatch> list = systemRegionMatchMapper.selectByExample(systemRegionMatchExample);
		int count = systemRegionMatchMapper.countByExample(systemRegionMatchExample);
		paging.setRoot(list);
		paging.setTotalProperty(count);
		return paging;
	}
	
	/**
	 * 获取渠道地区对象信息
	 */
	@Override
	public SystemRegionMatch getSystemRegionMatch(SystemRegionMatch systemRegionMatch) {
		return this.systemRegionMatchMapper.selectMatchByPrimaryKey(systemRegionMatch.getId());
	}

	public SystemRegion getSystemRegion(SystemRegion systemRegion) {
		return this.systemRegionMapper.selectByPrimaryKey(systemRegion.getRegionId());
	}
	
	//地区信息修改
	public JsonResult updateSystemRegionMatch(SystemRegionMatch systemRegionMatch){
		JsonResult jsonResult= new JsonResult();
		String regionId = systemRegionMatch.getNewAreaId();
		Integer id = systemRegionMatch.getId();
		if(id !=null && id>0 && StringUtil.isNotNull(regionId)){
			SystemRegionArea systemRegionArea = systemRegionAreaMapper.selectByPrimaryKey(regionId);
			if (systemRegionArea!=null) {
				systemRegionMatch.setNewAreaId(systemRegionArea.getRegionId());
				systemRegionMatch.setNewAreaName(systemRegionArea.getRegionName());
				systemRegionMatch.setMatchType("0");
				systemRegionMatchMapper.updateByPrimaryKeySelective(systemRegionMatch);
			}
			jsonResult.setIsok(true);
		}
		return jsonResult;
	}
	
	public Paging getSystemRegionAreaList(SystemRegionArea region, PageHelper helper) {
		SystemRegionAreaExample systemRegionAreaExample = new SystemRegionAreaExample();
		com.work.shop.bean.SystemRegionAreaExample.Criteria criteria = systemRegionAreaExample.or();
		//systemRegionAreaExample.setOrderByClause("region_id desc");
//		criteria.limit(helper.getStart(), helper.getLimit());
		
		
		java.util.HashMap<String,Object> params = new java.util.HashMap<String,Object>();
		String regionName = region.getRegionName();
		if (StringUtil.isEmpty(regionName)) {
			regionName = "%%";
		}else{
			regionName = regionName + "%";
		}
		params.put("regionName", regionName);
		if (region.getRegionType()!=null) {
			params.put("regionType", region.getRegionType());
		}
		if (StringUtil.isNotEmpty(region.getParentName())) {
			params.put("parentName", region.getParentName()+"%");
		}
		
		params.put("start", helper.getStart());
		params.put("limit", 400);
		
		
		//page count
		criteria.andRegionNameLike(regionName);
		if (region.getRegionType()!=null) {
			criteria.andRegionTypeEqualTo(region.getRegionType());
		}
		criteria.limit(helper.getStart(), 400);
		
		Paging paging = new Paging();
		List<SystemRegionArea> list = this.systemRegionAreaMapper.selectAreaByPrimaryKey(params);

		int count = systemRegionAreaMapper.countByExample(systemRegionAreaExample);
		paging.setRoot(list);
		paging.setTotalProperty(count);
		return paging;
	}
	
	/**
	 * 获取地区ID接口
	 * @author xudanhua
	 * @param Map<String,String> channelArea
	 * @param province --省级地名
	 * @param city	   --市级地名
	 * @param distinct --区级地名
	 * @param channelCode -- 渠道名称 如JD_CHANNEL_CODE
	 
	 * @return Map<String,String>
	 * @param province --省级地区ID
	 * @param city	   --市级地区ID
	 * @param distinct --区级地区ID
	 * @param questionCode -- 0：不是问题单，1-是问题单
	 **/
	@Override
	public Map<String,String> getSystemAreaMatch(String province,String city,String distinct,String channelCode){
		
		HashMap<String,String> ship = null;
		String key = province + city + distinct + channelCode;
		
		logger.info("getSystemAreaMatch start. import params: provinceName,cityName,distinctName ="+key);
		
		String provinceId = "0";
		String cityId = "0";
		String distinctId = "0";
		String questionCode = "0";
		
		try {
			ship = new HashMap<String,String>();
			List<SystemArea> provinceList = this.getSystemAreaData(province,channelCode,Constants.AREA_TYPE_PROVICE,"");
			List<SystemArea> cityList = this.getSystemAreaData(city,channelCode,"","");
			List<SystemArea> distinctList = this.getSystemAreaData(distinct,channelCode,"","");
			
			//province is null. 省级地名为空，直接返回问题单
			if (!StringUtil.isListNotNull(provinceList)) {
				if (StringUtil.isListNotNull(cityList)) {
					cityId = cityList.get(0).getOsRegionId();
				}
				if (StringUtil.isListNotNull(distinctList)) {
					distinctId = distinctList.get(0).getOsRegionId();
				}
				
				ship.put("province", provinceId);
				ship.put("city", cityId);
				ship.put("distinct", distinctId);
				ship.put("questionCode", "1"); //设为问题单  questionCode = 1
				
				getOsRegionNames(ship,channelCode);
				logger.info("****************** getSystemAreaMatch end ****************");
				return ship;
			}
			
			provinceId = provinceList.get(0).getOsRegionId();
			//city is null.市级地名为空，直接返回问题单
			if (!StringUtil.isListNotNull(cityList)) {
				if (StringUtil.isListNotNull(distinctList)) {
					distinctId = distinctList.get(0).getOsRegionId();
				}
				ship.put("province", provinceId);
				ship.put("city", cityId);
				ship.put("distinct", distinctId);
				ship.put("questionCode", "1");//设为问题单  questionCode = 1
				
				getOsRegionNames(ship,channelCode);
				logger.info("****************** getSystemAreaMatch end ****************");
				return ship;
			}
			
			//特别处理巢湖市。等所有外渠道调整以后，删除这段代码。
			if ("巢湖市".equals(city)) {
				
				if (StringUtil.isListNotNull(distinctList)) {
					String[] distinctRegions = distinctMatchProvinceId(provinceId,distinctList,Constants.AREA_TYPE_CITY);
					String osDistinctId = distinctRegions[1];
					if (!"0".equals(osDistinctId)) {//channel cityId match osDistinctId
						cityId = distinctRegions[0];
						distinctId = distinctRegions[1];
					}else{
						String[] cityRegions = distinctMatchProvinceId(provinceId,cityList,Constants.AREA_TYPE_CITY);
						if (!"0".equals(cityRegions[1])) {
							cityId = cityRegions[0];
							distinctId = cityRegions[1];
						}else{
							questionCode="1";
						}
					}
				}else{
					String[] cityRegions = distinctMatchProvinceId(provinceId,cityList,Constants.AREA_TYPE_CITY);
					String osDistinctId = cityRegions[1];
					if (!"0".equals(osDistinctId)) {
						cityId = cityRegions[0];
						distinctId = cityRegions[1];
					}else{
						questionCode="1";
					}
				}	
				
				ship.put("province", provinceId);
				ship.put("city", cityId);
				ship.put("distinct", distinctId);
				ship.put("questionCode", questionCode);
				getOsRegionNames(ship,channelCode);
				logger.info("****************** getSystemAreaMatch end ****************");
				return ship;
			}
			
			//cityId 反向匹配    provinceId
			cityId = matchParentId(provinceId,cityList);
			if (!"0".equals(cityId)) {//cityId.parentId match provinceId
				if (StringUtil.isListNotNull(distinctList)) {
					distinctId = matchParentId(cityId,distinctList);
					if ("0".equals(distinctId)) {
						distinctId = this.matchOtherDistinct(cityId);
					}
				}else{
					//输入的区为Null 设置为其它区县
					distinctId = this.matchOtherDistinct(cityId);
				}
			}else{//cityId.parentId not match provinceId
				String[] cityRegions = distinctMatchProvinceId(provinceId,cityList,Constants.AREA_TYPE_CITY);
				String osDistinctId = cityRegions[1];
				if (!"0".equals(osDistinctId)) {//channel cityId match osDistinctId
					cityId = cityRegions[0];
					distinctId = cityRegions[1];
				}else{
					cityId = cityList.get(0).getOsRegionId();
					if (StringUtil.isListNotNull(distinctList)) {
						distinctId = distinctList.get(0).getOsRegionId();
					}else{
						distinctId = this.matchOtherDistinct(cityId);
					}
				}
			}
			
			ship.put("province", provinceId);
			ship.put("city", cityId);
			ship.put("distinct", distinctId);
			ship.put("questionCode", questionCode);
			getOsRegionNames(ship,channelCode);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		logger.info("****************** getSystemAreaMatch end ****************");
		return ship;
		
	}
	
	private String matchParentId(String regionId,List<SystemArea> systemAreaList){
		String matchRegionId = "0";
		for (SystemArea sysArea : systemAreaList) {
			if (regionId.equals(sysArea.getOsParentId())) {
				matchRegionId = sysArea.getOsRegionId();
				break;
			}
		}
		return matchRegionId;
	}
	
	private String matchOtherDistinct(String regionId){
		List<SystemRegionArea> osDistinct3LevelList = this.getSystemRegionAreaData(regionId,Constants.AREA_TYPE_DISTINCT,"其它区县");
		String distinctId = "0";
		if (StringUtil.isListNotNull(osDistinct3LevelList)) {
			distinctId = osDistinct3LevelList.get(0).getRegionId();
		}else{
			List<SystemRegionArea> osDistinctNoLevelList = this.getSystemRegionAreaData("",Constants.AREA_TYPE_DISTINCT,"其它区县");
			if (StringUtil.isListNotNull(osDistinctNoLevelList)) {
				distinctId = osDistinctNoLevelList.get(0).getRegionId();
			}
		}
		return distinctId;
	}
	
	private String[] distinctMatchProvinceId(String provinceId,List<SystemArea> distinctList,String regionType){
		String[] regions = {"0","0"};
		try {
			for (SystemArea distinctArea : distinctList) {
				if (StringUtil.isEmpty(distinctArea.getOsParentId())) {
					continue;
				}
				List<SystemRegionArea> osCityList = this.getSystemRegionAreaData(distinctArea.getOsParentId(),regionType,"");
				if (StringUtil.isListNotNull(osCityList)) {
					for (SystemRegionArea sysArea : osCityList) {
						if (provinceId.equals(sysArea.getParentId())) {
							regions[0] = distinctArea.getOsParentId();
							regions[1] = distinctArea.getOsRegionId();
							break;
						}
					}
					if (!"0".equals(regions[0])) {
						break;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return regions;
	}
	
	private void getOsRegionNames(Map<String,String> ship,String channelCode){
		
		List<SystemRegionArea> provinceList = this.getSystemRegionAreaData(ship.get("province"),"1","");
		List<SystemRegionArea> cityList = this.getSystemRegionAreaData(ship.get("city"),"","");
		List<SystemRegionArea> distinctList = this.getSystemRegionAreaData(ship.get("distinct"),"","");
//		getRegionStr(provinceList,ship,"provinceName");
//		getRegionStr(cityList,ship,"cityName");
//		getRegionStr(distinctList,ship,"distinctName");
		String provinceName = "";
		String cityName = "";
		String distinctName = "";
		if (StringUtil.isListNotNull(provinceList)) {
			provinceName =provinceList.get(0).getRegionName();
		}
		if (StringUtil.isListNotNull(cityList)) {
			cityName =cityList.get(0).getRegionName();
		}
		if (StringUtil.isListNotNull(distinctList)) {
			distinctName =distinctList.get(0).getRegionName();
		}
		
		
		logger.info(channelCode + ": provinceName="+provinceName+",cityName="+cityName+",distinctName="+distinctName);
		logger.info("provinceId="+ship.get("province")+",cityId="+ship.get("city")+",distinctId="+ship.get("distinct"));
	}
	
	private List<SystemArea> getSystemAreaData(String regionName,String channelCode,String areaType,String regionId){
		SystemAreaExample systemAreaExample = new SystemAreaExample();
		SystemAreaExample.Criteria criteria = systemAreaExample.or();
		criteria.andChannelCodeEqualTo(channelCode);
		if (StringUtil.isNotEmpty(regionName)) {
			criteria.andAreaNameEqualTo(regionName);
		}else{
			criteria.andAreaNameIsNull();
		}
		if (StringUtil.isNotEmpty(areaType)) {
			criteria.andAreaTypeEqualTo(areaType);
		}
		if (StringUtil.isNotEmpty(regionId)) {
			criteria.andOsParentIdEqualTo(regionId);
		}
		List<SystemArea> systemAreaList = systemAreaMapper.selectByExample(systemAreaExample);
		return systemAreaList;
	}
	
	private List<SystemRegionArea> getSystemRegionAreaData(String regionId,String regionType,String regionName){
		SystemRegionAreaExample systemRegionAreaExample = new SystemRegionAreaExample();
		SystemRegionAreaExample.Criteria criteriaFur = systemRegionAreaExample.or();
		if (StringUtils.isNotEmpty(regionName)) {
			criteriaFur.andRegionNameEqualTo(regionName);
			if (StringUtils.isNotEmpty(regionId)) {
				criteriaFur.andParentIdEqualTo(regionId);
			}
		}else{
			if (StringUtils.isNotEmpty(regionId)) {
				criteriaFur.andRegionIdEqualTo(regionId);
			}else{
				criteriaFur.andRegionIdIsNull();
			}
			if (StringUtils.isNotEmpty(regionType)) {
				criteriaFur.andRegionTypeEqualTo(Integer.parseInt(regionType));
			}
		}
		List<SystemRegionArea> regionlist = systemRegionAreaMapper.selectByExample(systemRegionAreaExample);
		return regionlist;
	}
	
}

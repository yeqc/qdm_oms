package com.work.shop.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.work.shop.bean.ChannelInfo;
import com.work.shop.bean.ChannelShop;
import com.work.shop.bean.ChannelShopExample;
import com.work.shop.bean.ChannelShopExample.Criteria;
import com.work.shop.dao.ChannelShopMapper;
import com.work.shop.dao.OmsChannelInfoMapper;
import com.work.shop.redis.RedisClient;
import com.work.shop.service.ChannelShopService;
import com.work.shop.util.StringUtil;
import com.work.shop.vo.JsonResult;

@Service("channelShopService")
public class ChannelShopServiceImpl implements ChannelShopService {

	@Resource(name = "channelShopMapper")
	private ChannelShopMapper channelShopMapper;
	
	@Resource
	private OmsChannelInfoMapper omsChannelInfoMapper;
	
	@Resource(name = "redisClient")
	private RedisClient redisClient;
	
	private static final ObjectMapper MAPPER = new ObjectMapper();

	/**
	 * 查询父店铺信息
	 * 
	 * @param shopCode
	 *            店铺编码
	 */
	@Override
	public JsonResult selectParentChannelShop(String shopCode) {
		JsonResult jsonResult = new JsonResult();
		List<ChannelShop> list=null;
		try {
			if (StringUtils.isEmpty(shopCode)) {
				jsonResult.setIsok(false);
				jsonResult.setMessage("查询父店铺失败，店铺编码为空!");
				return jsonResult;
			}
			//先从缓存中命中，如果没查询到，则查询数据库
			String json = redisClient.get("ParentChannelShop_"+shopCode);
			if(StringUtil.isNotBlank(json)){
				list = MAPPER.readValue(json, new TypeReference<List<ChannelShop>>() {
				}); 
			}
			if(CollectionUtils.isEmpty(list)){
				ChannelShopExample channelShopExample = new ChannelShopExample();
				channelShopExample.or().andShopCodeEqualTo(shopCode).andShopStatusEqualTo((byte)1);
				list = channelShopMapper.selectByExample(channelShopExample);
				if (list == null || list.size() != 1) {
					jsonResult.setIsok(false);
					jsonResult.setMessage("店铺[" + shopCode + "]不存在!");
					return jsonResult;
				}
				String parentShopCode = list.get(0).getParentShopCode();

				if (shopCode.equals(parentShopCode)) {
					jsonResult.setData(list.get(0));
				} else {
					channelShopExample.clear();
					channelShopExample.or().andShopCodeEqualTo(parentShopCode);
					list = channelShopMapper.selectByExample(channelShopExample);
					if (list == null || list.size() != 1) {
						jsonResult.setIsok(false);
						jsonResult.setMessage("店铺[" + shopCode + "]的父店铺[" + parentShopCode + "]不存在!");
						return jsonResult;
					}
				}
				  //存放到缓存
			    redisClient.set("ParentChannelShop_"+shopCode, MAPPER.writeValueAsString(list));
			}
			jsonResult.setData(list.get(0));
			jsonResult.setIsok(true);
			jsonResult.setMessage("查询店铺[" + shopCode + "]的父店铺信息成功!");
			return jsonResult;

		} catch (Exception e) {
			e.printStackTrace();
			jsonResult.setIsok(false);
			jsonResult.setMessage("查询店铺[" + shopCode + "]的父店铺信息异常：" + e.getMessage());
		}
		return jsonResult;
	}

	/**
	 * 查询当前铺信息
	 * 
	 * @param shopCode
	 *            店铺编码
	 * 
	 */
	@Override
	public JsonResult selectCurrentChannelShop(String shopCode) {
		JsonResult jsonResult = new JsonResult();
		List<ChannelShop> list=null;
		try {
			if (StringUtils.isEmpty(shopCode)) {
				jsonResult.setIsok(false);
				jsonResult.setMessage("查询失败，店铺编码为空!");
				return jsonResult;
			}
			//先从缓存中命中，如果没有，则查询数据库
			String json=redisClient.get("ChanelShop_"+shopCode);
			if(StringUtil.isNotBlank(json)){
				list = MAPPER.readValue(json, new TypeReference<List<ChannelShop>>() {
				}); 
			}
			if(CollectionUtils.isEmpty(list)){
				ChannelShopExample channelShopExample = new ChannelShopExample();
				channelShopExample.or().andShopCodeEqualTo(shopCode).andShopStatusEqualTo((byte)1);
			    list = channelShopMapper.selectByExample(channelShopExample);
			    redisClient.set("ChanelShop_"+shopCode, MAPPER.writeValueAsString(list));
			}
			if (list == null || list.size() != 1) {
				jsonResult.setIsok(true);
				jsonResult.setMessage("店铺[" + shopCode + "]不存在!");
				return jsonResult;
			}
			jsonResult.setData(list.get(0));
			jsonResult.setIsok(true);
			jsonResult.setMessage("查询店铺[" + shopCode + "]成功!");
			return jsonResult;
		} catch (Exception e) {
			e.printStackTrace();
			jsonResult.setIsok(false);
			jsonResult.setMessage("查询店铺[" + shopCode + "]异常：" + e.getMessage());
		}
		return jsonResult;
	}

	/**
	 * 查询子店铺信息
	 * 
	 * @param shopCode
	 *            店铺编码
	 * 
	 */
	@Override
	public JsonResult selectChildChannelShop(String shopCode) {
		JsonResult jsonResult = new JsonResult();
		List<ChannelShop> list=null;
		try {
			if (StringUtils.isEmpty(shopCode)) {
				jsonResult.setIsok(false);
				jsonResult.setMessage("查询子店铺失败，店铺编码为空!");
				return jsonResult;
			}
			//先从缓存中命中，如果没查询到，则查询数据库
			String json = redisClient.get("ChildChannelShop_"+shopCode);
			if(StringUtil.isNotBlank(json)){
				list = MAPPER.readValue(json, new TypeReference<List<ChannelShop>>() {
				}); 
			}
			if(CollectionUtils.isEmpty(list)){
				ChannelShopExample channelShopExample = new ChannelShopExample();
				channelShopExample.or().andParentShopCodeEqualTo(shopCode).andShopCodeNotEqualTo(shopCode).andShopStatusEqualTo((byte)1);
				list = channelShopMapper.selectByExample(channelShopExample);
				//存放到缓存
			    redisClient.set("ChildChannelShop_"+shopCode, MAPPER.writeValueAsString(list));
			}
			if (list == null || list.size() == 0) {
				jsonResult.setIsok(true);
				jsonResult.setMessage("店铺[" + shopCode + "]没有子店铺!");
				return jsonResult;
			}
			jsonResult.setData(list);
			jsonResult.setIsok(true);
			jsonResult.setMessage("查询[" + shopCode + "]子店铺成功!");
			return jsonResult;
		} catch (Exception e) {
			e.printStackTrace();
			jsonResult.setIsok(false);
			jsonResult.setMessage("查询[" + shopCode + "]子店铺异常：" + e.getMessage());
		}
		return jsonResult;
	}

	@Override
	public JsonResult findShopChannelShopByChannelCode(String channelCode) {
		JsonResult jsonResult = new JsonResult();
		List<ChannelShop> channelShopList= null;
		try {
			if (StringUtils.isEmpty(channelCode)) {
				jsonResult.setIsok(false);
				jsonResult.setMessage("查询店铺失败，渠道（店面上级）编码为空!");
				return jsonResult;
			}
			//先从缓存中命中，如果没查询到，则查询数据库
			String json = redisClient.get("ChannelShop_"+channelCode);
			if(StringUtil.isNotBlank(json)){
				channelShopList = MAPPER.readValue(json, new TypeReference<List<ChannelShop>>() {
				}); 
			}
			if(CollectionUtils.isEmpty(channelShopList)){
				ChannelShopExample channelShopExample = new ChannelShopExample();
				channelShopExample.or().andChannelCodeEqualTo(channelCode).andShopStatusEqualTo((byte)1);
//				List<ChannelShop> listc = channelShopMapper.selectBaseInfoByExample(channelShopExample);
				
				channelShopList = channelShopMapper.selectByExample(channelShopExample);
//				OpenShopChannelInfoExample openShopChannelInfoExample = new OpenShopChannelInfoExample();
//				openShopChannelInfoExample.or().andChannelTypeEqualTo(channelCode).andIsActiveEqualTo((byte)1);
//				List<OpenShopChannelInfo> list= openShopChannelInfoMapper.selectByExample(openShopChannelInfoExample);
//				List<ChannelShop> listo = new ArrayList<ChannelShop>();
//				if(null!=list){
//					for(OpenShopChannelInfo openShopChannelInfo:list){
//						ChannelShop channelShop=new ChannelShop();
//						channelShop.setChannelCode(openShopChannelInfo.getChannelType());
//						channelShop.setParentShopCode(openShopChannelInfo.getParentChannelCode());
//						channelShop.setShopCode(openShopChannelInfo.getChannelCode());
//						channelShop.setShopTitle(openShopChannelInfo.getChannelName());
//						listo.add(channelShop);
//					}
//				}
//				//根据店铺号取并集
//			loop1:for(ChannelShop channelShopA:listc){
//				   for(ChannelShop channelShopB:listo){
//						if(channelShopB.getShopCode().equals(channelShopA.getShopCode())){
//							continue loop1;
//						}
//					}
//				listo.add(channelShopA);
//				}
			    //存放到缓存
			    redisClient.set("ChannelShop_"+channelCode, MAPPER.writeValueAsString(channelShopList));
			}
			
			jsonResult.setData(channelShopList);
			jsonResult.setIsok(true);
			jsonResult.setMessage("查询[" + channelCode + "]店铺成功!");
			
		} catch (Exception e) {
			e.printStackTrace();
			jsonResult.setIsok(false);
			jsonResult.setMessage("查询[" + channelCode + "]店铺异常：" + e.getMessage());
		}
		return jsonResult;
	}

	@Override
	public JsonResult addShopChannelShop(List<ChannelShop> ChannelShopList) {
		JsonResult jsonResult=new JsonResult();
		StringBuffer error = new StringBuffer("");
		for(ChannelShop channelShop:ChannelShopList){
			try {
				channelShopMapper.insertSelective(channelShop);
			} catch (Exception e) {
				if(StringUtil.isEmpty(error.toString())){
					error.append(channelShop.getShopCode());
				}else{
					error.append(",").append(channelShop.getShopCode());
				}
			}
		}
		if(StringUtil.isEmpty(error.toString())){
			jsonResult.setIsok(true);
			jsonResult.setMessage("保存成功！");
		}else{
			jsonResult.setIsok(false);
			jsonResult.setMessage("[" + error.toString() + "]数据添加失败!");
		}
		return jsonResult;
	}

	@Override
	public ChannelShop selectChannelShopByShopCode(String shopCode) {
		if (StringUtils.isEmpty(shopCode)) {
			return null;
		}
		ChannelShopExample channelShopExample = new ChannelShopExample();
		channelShopExample.or().andShopCodeEqualTo(shopCode);
		List<ChannelShop> list = channelShopMapper.selectByExample(channelShopExample);
		if (list == null || list.size() != 1) {
			return null;
		}
		return list.get(0);
	}

	@Override
	public void updateChannelShop(String shopCode, String channelCode,Date expiresTime) {
	
		ChannelShopExample channelShopExample = new ChannelShopExample();
		Criteria criteria = channelShopExample.or();
		
		if(StringUtil.isNotBlank(shopCode)){
			criteria.andShopCodeEqualTo(shopCode);
		}
		
		if(StringUtil.isNotBlank(channelCode)){
			criteria.andChannelCodeEqualTo(channelCode);
		}
		
		ChannelShop  channelShop = new ChannelShop();
		channelShop.setExpiresTime(expiresTime);
		
		channelShopMapper.updateByExampleSelective(channelShop, channelShopExample);
	}
	
}

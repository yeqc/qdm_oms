package com.work.shop.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.work.shop.bean.ChannelInfo;
import com.work.shop.bean.ChannelInfoExample;
import com.work.shop.bean.ChannelInfoExample.Criteria;
import com.work.shop.dao.ChannelInfoMapper;
import com.work.shop.redis.RedisClient;
import com.work.shop.service.ChannelInfoService;
import com.work.shop.util.StringUtil;
import com.work.shop.util.extjs.Paging;
import com.work.shop.vo.JsonResult;

@Service("channelInfoService")
public class ChannelInfoServiceImpl implements ChannelInfoService {
	
	
	@Resource(name = "channelInfoMapper")
	private ChannelInfoMapper channelInfoMapper;

	@Resource(name = "redisClient")
	private RedisClient redisClient;
	
	private static final ObjectMapper MAPPER = new ObjectMapper();
	
	//查询渠道列表
	public Paging getChannelInfoList(ChannelInfoExample channelInfoExample){
		Paging paging = new Paging();
		List<ChannelInfo> list = channelInfoMapper.selectByExample(channelInfoExample);
		int count = channelInfoMapper.countByExample(channelInfoExample);
		
		paging.setRoot(list);
		paging.setTotalProperty(count);
		
		return paging;
		
	}
	/**
	 * 渠道单个对象查询
	 * @author guoduanduan
	 * @param id
	 * @return ChannelInfo
	 * */
    public 	ChannelInfo getChannelInfo(int id){
    	
    	return channelInfoMapper.selectByPrimaryKey(id);
    	
    }
	//插入数据/根据主键id修改渠道信息
	/**
	 * 渠道信息插入修改，判断渠道编号与名称的唯一性
	 * @author guoduanduan
	 * @param channelInfo
	 * @return jsonResult
	 * */
	public JsonResult updateChannelInfo(ChannelInfo channelInfo){
         
		JsonResult jsonResult = new JsonResult();
		ChannelInfoExample channelInfoExample = new ChannelInfoExample(); //用于查询渠道编号		
		Criteria criteria = channelInfoExample.or();
		ChannelInfoExample channelInfoExamples = new ChannelInfoExample();//用于查询渠道名称
		Criteria criterias = channelInfoExamples.or();
		if(channelInfo.getId()!=null && channelInfo.getId()>0){
			    ChannelInfo chanelInfoBean = channelInfoMapper.selectByPrimaryKey(channelInfo.getId());
			    if(channelInfo.getChanelCode().equals(chanelInfoBean.getChanelCode())){
			    	if(chanelInfoBean.getChannelTitle()!= null &&!channelInfo.getChannelTitle().equals(chanelInfoBean.getChannelTitle())){
							criterias.andChannelTitleEqualTo(channelInfo.getChannelTitle());
							List<ChannelInfo> list = channelInfoMapper.selectByExample(channelInfoExamples);
							if(list!= null && list.size()>0){
								jsonResult.setIsok(false);
								jsonResult.setMessage("该渠道名称已经存在！");
								return jsonResult;
							}
						}
			    	
			    	
			    }else{
			    	if(null != channelInfo.getChanelCode() && !"".equals(channelInfo.getChanelCode())){
						criteria.andChanelCodeEqualTo(channelInfo.getChanelCode());
						List<ChannelInfo> list = channelInfoMapper.selectByExample(channelInfoExample);
						if(list != null && list.size()>0){
							jsonResult.setIsok(false);
							jsonResult.setMessage("该渠道编号已经存在！");
							return jsonResult;
						}
					}
			    	if(!channelInfo.getChannelTitle().equals(chanelInfoBean.getChannelTitle())){
			    		if(null != channelInfo.getChannelTitle() && !"".equals(channelInfo.getChannelTitle())){
							criterias.andChannelTitleEqualTo(channelInfo.getChannelTitle());
							List<ChannelInfo> list = channelInfoMapper.selectByExample(channelInfoExamples);
							if(list!= null && list.size()>0){
								jsonResult.setIsok(false);
								jsonResult.setMessage("该渠道名称已经存在！");
								return jsonResult;
							}
						}
			    	
			    	}
			    }
			    channelInfo.setChannelStatus(chanelInfoBean.getChannelStatus());
				channelInfoMapper.updateByPrimaryKey(channelInfo);
			   //redisClient.set("channelId",channelInfo.getId().toString());//更新缓存渠道
				if(channelInfo.getChannelStatus()==1){//更新缓存channel_type 放渠道列表
					Map<String, Object> resultMap = new HashMap<String, Object>();
					resultMap.put("ChannelType", channelInfo.getChannelType());
					List<ChannelInfo> ChannelInfoList=channelInfoMapper.findByChanneltype(resultMap);
					try {
						redisClient.set("ChannelType_"+channelInfo.getChannelType(),MAPPER.writeValueAsString(ChannelInfoList));
					} catch (JsonProcessingException e) {
						e.printStackTrace();
					}
					if(!channelInfo.getChannelType().equals(chanelInfoBean.getChannelType())){
						resultMap.clear();
						resultMap.put("ChannelType", chanelInfoBean.getChannelType());
						List<ChannelInfo> channelInfoList=channelInfoMapper.findByChanneltype(resultMap);
						try {
							redisClient.set("ChannelType_"+chanelInfoBean.getChannelType(),MAPPER.writeValueAsString(channelInfoList));
						} catch (JsonProcessingException e) {
							e.printStackTrace();
						}
					}
				}
				jsonResult.setIsok(true);
				jsonResult.setMessage("修改渠道信息成功！");
		}else{
			if(null != channelInfo.getChanelCode() && !"".equals(channelInfo.getChanelCode())){
				criteria.andChanelCodeEqualTo(channelInfo.getChanelCode());
				List<ChannelInfo> list = channelInfoMapper.selectByExample(channelInfoExample);
				if(list != null && list.size()>0){
					jsonResult.setIsok(false);
					jsonResult.setMessage("该渠道编号已经存在！");
					return jsonResult;
				}
			}		
			
			if(null != channelInfo.getChannelTitle() && !"".equals(channelInfo.getChannelTitle())){
				criterias.andChannelTitleEqualTo(channelInfo.getChannelTitle());
				List<ChannelInfo> list = channelInfoMapper.selectByExample(channelInfoExamples);
				if(list!= null && list.size()>0){
					jsonResult.setIsok(false);
					jsonResult.setMessage("该渠道名称已经存在！");
					return jsonResult;
				}
			}
			channelInfo.setChannelStatus((short)0);
			channelInfoMapper.insert(channelInfo);
			jsonResult.setIsok(true);
			jsonResult.setMessage("新增渠道信息成功！");
		}
		return jsonResult;
	}
	
	/**
	 * 渠道移除
	 * @author guoduanduan
	 * @param ids
	 * @return jsonResult
	 * */
	public JsonResult deleteChannelInfo(String ids){
		JsonResult jsonResult = new JsonResult();
//		ChannelInfoExample channelInfoExample = new ChannelInfoExample();
//		Criteria criteria = channelInfoExample.or();
		List<Integer> list = new ArrayList<Integer>();
		int count=0;
         if(ids != null && StringUtils.isNotEmpty(ids)){
			String[] str = ids.substring(0, ids.length()-1).split(",");//去除最后一个","
			for(int i=0;i<str.length;i++){
				int id = Integer.parseInt(str[i]);
				list.add(id);//需要删除的id数组	
				ChannelInfo ci = channelInfoMapper.selectByPrimaryKey(id);
				 count+=channelInfoMapper.deleteByPrimaryKey(id);
				redisClient.del(ci.getChanelCode());//移除缓存
				
				Map<String, Object> resultMap = new HashMap<String, Object>();
				resultMap.put("ChannelType", ci.getChannelType()); //渠道类型
				List<ChannelInfo> ChannelInfoList=channelInfoMapper.findByChanneltype(resultMap);
				redisClient.set(ci.getChannelType().toString(), ChannelInfoList.toString());//放缓存channel_type 放渠道列表
			}
//			criteria.andIdIn(list);
		}
//         int record = channelInfoMapper.deleteByExample(channelInfoExample);
         if(list != null && count == list.size()){
 			jsonResult.setIsok(true);
 			jsonResult.setMessage("批量删除成功！");
 		}else{			
 			jsonResult.setIsok(false);
 			jsonResult.setMessage("批量删除失败！");
 		}
 		return jsonResult;
		
	}
	
	
	/**
	 * 渠道状态激活
	 * @author guoduanduan
	 * @param ids 主键id集合
	 * @return jsonResult
	 * */
	public JsonResult updateChannelStatus(String ids, String channelStatus) {
		JsonResult jsonResult = new JsonResult();
		if (null != ids && !"".equals(ids)) {
			String[] idsArr = ids.split(",");
			int record = 0;
			for (String id : idsArr) {
				ChannelInfo ci = channelInfoMapper.selectByPrimaryKey(Integer
						.parseInt(id));
				ci.setChannelStatus(Short.parseShort(channelStatus));
				ci.setId(ci.getId());
				record += channelInfoMapper.updateByPrimaryKey(ci);
				Map<String, Object> resultMap = new HashMap<String, Object>();
				resultMap.put("ChannelType", ci.getChannelType());
				List<ChannelInfo> ChannelInfoList=channelInfoMapper.findByChanneltype(resultMap);
				try {
					redisClient.set("ChannelType_"+ci.getChannelType(), MAPPER.writeValueAsString(ChannelInfoList));//放缓存channel_type 放渠道列表
				} catch (JsonProcessingException e) {
					e.printStackTrace();
				}
			}
			String[] str = ids.substring(0, ids.length() - 1).split(",");// 去除最后一个","
			if (record == str.length) {
				jsonResult.setIsok(true);
			} else {
				jsonResult.setIsok(false);
				jsonResult.setMessage("批量激活失败！");
			}
		}
		return jsonResult;
	}
	@Override
	public JsonResult findShopChannelInfoByChanneltype(int ChannelType) {
		JsonResult jsonResult = new JsonResult();
		List<ChannelInfo> ChannelInfoList=null;
		try {
			//先从缓存中命中，如果没查询到，则查询数据库
			String json = redisClient.get("ChannelType_"+ChannelType);
			if(StringUtil.isNotBlank(json)){
				ChannelInfoList = MAPPER.readValue(json, new TypeReference<List<ChannelInfo>>() {
				}); 
			}
			if(CollectionUtils.isEmpty(ChannelInfoList)){
				Map<String, Object> resultMap = new HashMap<String, Object>();
				resultMap.put("ChannelType", ChannelType); //渠道类型
			    ChannelInfoList=channelInfoMapper.findByChanneltype(resultMap);
			    //存放到缓存
			    redisClient.set("ChannelType_"+ChannelType, MAPPER.writeValueAsString(ChannelInfoList));
			}
			if(StringUtil.isNotNullForList(ChannelInfoList)){
				jsonResult.setIsok(true);
				jsonResult.setData(ChannelInfoList);
				jsonResult.setTotalProperty(ChannelInfoList.size());
			}else{
				jsonResult.setIsok(false);
			}
		} catch (Exception e) {
			e.printStackTrace();
			jsonResult.setIsok(false);
		} 
		return jsonResult;
	}
	
	
}

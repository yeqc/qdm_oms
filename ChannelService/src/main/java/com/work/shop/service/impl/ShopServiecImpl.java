package com.work.shop.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.work.shop.bean.ChannelShop;
import com.work.shop.bean.ChannelShopExample;
import com.work.shop.bean.ChannelShopExample.Criteria;
import com.work.shop.bean.InterfaceProperties;
import com.work.shop.bean.InterfacePropertiesExample;
import com.work.shop.bean.OmsChannelInfo;
import com.work.shop.bean.OmsChannelInfoExample;
import com.work.shop.dao.BgChannelDbTableMapper;
import com.work.shop.dao.ChannelShopDefineMapper;
import com.work.shop.dao.ChannelShopMapper;
import com.work.shop.dao.InterfacePropertiesMapper;
import com.work.shop.dao.OmsChannelInfoMapper;
import com.work.shop.mq.listener.SynchItemListener;
import com.work.shop.mq.listener.SynchStockListener;
import com.work.shop.oms.activemq.listener.DynamicListenerManager;
import com.work.shop.redis.RedisClient;
import com.work.shop.service.InterfacePropertiesService;
import com.work.shop.service.ShopService;
import com.work.shop.util.Constants;
import com.work.shop.util.StringUtil;
import com.work.shop.util.extjs.Paging;
import com.work.shop.vo.ChannelShopVo;
import com.work.shop.vo.JsonResult;

@Service("shopService") 
public class ShopServiecImpl implements ShopService {
	
	private static Logger log = Logger.getLogger(ShopServiecImpl.class);

	
	@Resource(name = "channelShopMapper")
	private ChannelShopMapper channelShopMapper;
	@Resource(name = "interfacePropertiesMapper")
	private InterfacePropertiesMapper interfacePropertiesMapper;
	@Resource
	private OmsChannelInfoMapper omsChannelInfoMapper;
	@Resource
	private BgChannelDbTableMapper bgChannelDbTableMapper;
	@Resource
	private ChannelShopDefineMapper channelShopDefineMapper;
	@Resource(name = "redisClient")
	private RedisClient redisClient;
	@Resource(name = "interfacePropertiesService")
	private InterfacePropertiesService interfacePropertiesService;
//	@Resource
//	private ServerQueueMqService serverQueueMqService;
	@Resource
	private DynamicListenerManager dynamicListenerManager;
	@Resource
	private SynchItemListener synchItemListener;
	@Resource
	private SynchStockListener synchStockListener;

	private static final ObjectMapper MAPPER = new ObjectMapper();
	public InterfacePropertiesMapper getInterfacePropertiesMapper() {
		return interfacePropertiesMapper;
	}

	public void setInterfacePropertiesMapper(
			InterfacePropertiesMapper interfacePropertiesMapper) {
		this.interfacePropertiesMapper = interfacePropertiesMapper;
	}

	@Override
	public List<ChannelShopVo> getChannelShopList(ChannelShopExample channelShopExample, boolean includeChildFlag) {
		if(includeChildFlag){
			return channelShopDefineMapper.selectByVoExampleIncludeChild(channelShopExample);
		}
		return channelShopDefineMapper.selectByVoExampleExcludeChild(channelShopExample);
	}
	
	@Override
	public int getChannelShopCount(ChannelShopExample channelShopExample) {
		// TODO Auto-generated method stub
		return channelShopMapper.countByExample(channelShopExample);
	}

	
	/**
	 * 店铺插入修改，判断渠道编号与名称的唯一性
	 * @author guoduanduan
	 * @param channelShop
	 * @return jsonResult
	 * */
	@Override
	public JsonResult insertChannelShop(ChannelShop channelShop) {
	 JsonResult jsonResult = new JsonResult();
	 ChannelShopExample channelShopExample = new ChannelShopExample();//店铺编号确定唯一性
	 Criteria  criteria = channelShopExample.or();
	 ChannelShopExample channelShopExamples = new ChannelShopExample();//店铺名称确定唯一性
	 Criteria  criterias = channelShopExamples.or();
	 if(channelShop.getShopChannel() != null){
		 criterias.andShopChannelEqualTo(channelShop.getShopChannel());  //渠道名称集团的和外部的可以相同
	 }
     if(channelShop.getId() != null && channelShop.getId() > 0){
    	 ChannelShop channelShopBean = channelShopMapper.selectByPrimaryKey(channelShop.getId());
    	 if(channelShop.getShopCode().equals(channelShopBean.getShopCode())){
    		
    		 if(channelShopBean.getShopTitle() != null && !channelShop.getShopTitle().equals(channelShopBean.getShopTitle())){
    			 criterias.andShopTitleEqualTo(channelShop.getShopTitle());
        		 List<ChannelShop> list = channelShopMapper.selectByExample(channelShopExamples);
        		 if(list != null && list.size() > 0){
        	    	 jsonResult.setIsok(false);
        	    	 jsonResult.setMessage("该店铺名称已经存在！！");
        	    	 return jsonResult;
        	     }
    		 }
    	 }else{
    		 if(null != channelShop.getShopCode() && !"".equals(channelShop.getShopCode()) ){
        		 criteria.andShopCodeEqualTo(channelShop.getShopCode());
        		 List<ChannelShop> list = channelShopMapper.selectByExample(channelShopExample);
        	     if(list != null && list.size() > 0){
        	    	 jsonResult.setIsok(false);
        	    	 jsonResult.setMessage("该店铺编号已经存在！！");
        	    	 return jsonResult;
        	     }
        	 }
    		 if(channelShopBean.getShopTitle() != null && !channelShop.getShopTitle().equals(channelShopBean.getShopTitle())){
    			 criterias.andShopTitleEqualTo(channelShop.getShopTitle());
        		 List<ChannelShop> list = channelShopMapper.selectByExample(channelShopExamples);
        		 if(list != null && list.size() > 0){
        	    	 jsonResult.setIsok(false);
        	    	 jsonResult.setMessage("该店铺名称已经存在！！");
        	    	 return jsonResult;
        	     }
    		 }
    	 }
    	 channelShop.setShopStatus(channelShopBean.getShopStatus());
    	 channelShopMapper.updateByPrimaryKey(channelShop);
    	 jsonResult.setIsok(true);
    	 jsonResult.setMessage("店铺信息修改成功！！");
//		setActiveMQStatus(channelShop.getShopCode(), channelShop.getShopStatus().toString(), channelShop.getIsSyn());
     }else{
    	 if(null != channelShop.getShopCode() && !"".equals(channelShop.getShopCode()) ){
    		 criteria.andShopCodeEqualTo(channelShop.getShopCode());
    		 List<ChannelShop> list = channelShopMapper.selectByExample(channelShopExample);
    	     if(list != null && list.size() > 0){
    	    	 jsonResult.setIsok(false);
    	    	 jsonResult.setMessage("该店铺编号已经存在！！");
    	    	 return jsonResult;
    	     }
    	 }
    	 if(channelShop.getShopTitle() != null && !"".equals(channelShop.getShopTitle())){
    		 criterias.andShopTitleEqualTo(channelShop.getShopTitle());
    		 List<ChannelShop> list = channelShopMapper.selectByExample(channelShopExamples);
    		 if(list != null && list.size() > 0){
    	    	 jsonResult.setIsok(false);
    	    	 jsonResult.setMessage("该店铺名称已经存在！！");
    	    	 return jsonResult;
    	     }
    	 }
    	 channelShop.setShopStatus((byte) 0);  //新增店铺设置为未激活状态
    	 channelShopMapper.insertSelective(channelShop);
    	 jsonResult.setIsok(true);
    	 jsonResult.setMessage("店铺信息新增成功！！");
     }
		return jsonResult;
	}
 
	//无用！！
	@Override
	public int updateChannelShop(ChannelShop channelShop) {
		if(channelShop.getId()!=null && channelShop.getId()>0){
			channelShopMapper.updateByPrimaryKey(channelShop);
			setActiveMQStatus(channelShop.getShopCode(), channelShop.getShopStatus().toString(), channelShop.getIsSyn());
		}
		return 0;
	}
	
	@Override
	public ChannelShop queryChannelShop(ChannelShop channelShop) {
		
		ChannelShopExample channelShopExample = new ChannelShopExample();
		Criteria criteria = channelShopExample.createCriteria();

		criteria.andIdEqualTo(channelShop.getId());
	
		List<ChannelShop> list = channelShopMapper.selectByExample(channelShopExample);

		if(list.size() > 0){
			return (ChannelShop)list.get(0);
		}
		return null;
	}
	
	
	
	/**
	 * 关联openshop店铺插入修改，判断渠道编号与名称的唯一性
	 * @param channelShop
	 * @return jsonResult
	 * */
	@Override
	public JsonResult insertChannelShop(ChannelShop channelShop,OmsChannelInfo omsChannelInfo) {
		JsonResult jsonResult = new JsonResult();
		ChannelShopExample channelShopExample = new ChannelShopExample();//店铺编号确定唯一性
		Criteria  criteria = channelShopExample.or();
		ChannelShopExample channelShopExamples = new ChannelShopExample();//店铺名称确定唯一性
		Criteria  criterias = channelShopExamples.or();
		if(channelShop.getShopChannel() != null){
			criterias.andShopChannelEqualTo(channelShop.getShopChannel());  //渠道名称集团的和外部的可以相同
		}
		// 店铺ID主键存在
		if(channelShop.getId() != null && channelShop.getId() > 0){
			ChannelShop channelShopBean = channelShopMapper.selectByPrimaryKey(channelShop.getId());
			if(channelShop.getShopCode().equals(channelShopBean.getShopCode())){
				if(channelShopBean.getShopTitle() != null && !channelShop.getShopTitle().equals(channelShopBean.getShopTitle())){
					criterias.andShopTitleEqualTo(channelShop.getShopTitle());
					List<ChannelShop> list = channelShopMapper.selectByExample(channelShopExamples);
					if(list != null && list.size() > 0){
						jsonResult.setIsok(false);
						jsonResult.setMessage("该店铺名称已经存在！！");
						return jsonResult;
					}
				}
			}else{
				if(null != channelShop.getShopCode() && !"".equals(channelShop.getShopCode()) ){
					criteria.andShopCodeEqualTo(channelShop.getShopCode());
					List<ChannelShop> list = channelShopMapper.selectByExample(channelShopExample);
					if(list != null && list.size() > 0){
						jsonResult.setIsok(false);
						jsonResult.setMessage("该店铺编号已经存在！！");
						return jsonResult;
					}
				}
				if(channelShopBean.getShopTitle() != null && !channelShop.getShopTitle().equals(channelShopBean.getShopTitle())){
					criterias.andShopTitleEqualTo(channelShop.getShopTitle());
					List<ChannelShop> list = channelShopMapper.selectByExample(channelShopExamples);
					if(list != null && list.size() > 0){
						jsonResult.setIsok(false);
						jsonResult.setMessage("该店铺名称已经存在！！");
						return jsonResult;
					}
				}
			}

			// 判断父店铺编号是否存在
			String parentChannelCode = omsChannelInfo.getParentChannelCode();
			if (StringUtil.isNotEmpty(parentChannelCode) && !parentChannelCode.equals(omsChannelInfo.getChannelCode())) {
				OmsChannelInfoExample example = new OmsChannelInfoExample();
				example.or().andChannelCodeEqualTo(parentChannelCode);
				int count = omsChannelInfoMapper.countByExample(example);
				if (count != 1) {
					jsonResult.setIsok(false);
					jsonResult.setMessage("OPENSHOP父店铺编号[" + parentChannelCode + "]不存在！");
					return jsonResult;
				}
			}
			
			if (omsChannelInfo.getChannelId() != null && omsChannelInfo.getChannelId() > 0) {
				// 数据修改
				OmsChannelInfo chanelInfoBean = omsChannelInfoMapper.selectByPrimaryKey(omsChannelInfo.getChannelId());
				if (!omsChannelInfo.getChannelCode().equals(chanelInfoBean.getChannelCode())) {
					// channel_code 属性被修改，判断新channel_code是否已经存在
					if (null != omsChannelInfo.getChannelCode() && !"".equals(omsChannelInfo.getChannelCode())) {
						OmsChannelInfoExample example = new OmsChannelInfoExample();
						example.or().andChannelCodeEqualTo(omsChannelInfo.getChannelCode());
						int count = omsChannelInfoMapper.countByExample(example);
						if (count != 0) {
							jsonResult.setIsok(false);
							jsonResult.setMessage("该OPENSHOP店铺编号已经存在！");
							return jsonResult;
						}
					}
				}
				OmsChannelInfoExample updateExample = new OmsChannelInfoExample();
				updateExample.or().andChannelIdEqualTo(omsChannelInfo.getChannelId());
				omsChannelInfoMapper.updateByExampleSelective(omsChannelInfo, updateExample);
				jsonResult.setIsok(true);
				jsonResult.setMessage("修OPENSHOP店铺编号信息成功！");
			} else {
				// 数据添加
				// 判断新增数据channel_code是否已经存在
				if (StringUtil.isNotEmpty(omsChannelInfo.getChannelCode())) {
					OmsChannelInfoExample example = new OmsChannelInfoExample(); // 用于查询渠道编号
					example.or().andChannelCodeEqualTo(omsChannelInfo.getChannelCode());
					int count = omsChannelInfoMapper.countByExample(example);
					if (count != 0) {
						jsonResult.setIsok(false);
						jsonResult.setMessage("该OPENSHOP店铺编号已经存在！");
						return jsonResult;
					}
				}
				omsChannelInfoMapper.insertSelective(omsChannelInfo);
				jsonResult.setIsok(true);
				jsonResult.setMessage("新增OPENSHOP店铺编号成功！");
			}
				
			channelShop.setShopStatus(channelShopBean.getShopStatus());
			channelShopMapper.updateByPrimaryKey(channelShop);
			//更新店铺缓存信息
			if(channelShop.getShopStatus()==1){
				//channelShop信息刷新到缓存
			    try {
			    	if(StringUtils.isNotBlank(channelShopBean.getChannelCode())){
			    		ChannelShopExample example1 = new ChannelShopExample();
			    		example1.or().andChannelCodeEqualTo(channelShopBean.getChannelCode()).andShopStatusEqualTo((byte)1);
			    		List<ChannelShop> oldChannelShoplist = channelShopMapper.selectByExample(example1);
			    		redisClient.set("ChannelShop_"+channelShopBean.getChannelCode(),MAPPER.writeValueAsString(oldChannelShoplist));
			    	}
			    	if(StringUtils.isNotBlank(channelShop.getChannelCode())){
			    		ChannelShopExample example2 = new ChannelShopExample();
			    		example2.or().andChannelCodeEqualTo(channelShop.getChannelCode()).andShopStatusEqualTo((byte)1);
			    		List<ChannelShop> list = channelShopMapper.selectByExample(example2);
			    		redisClient.set("ChannelShop_"+channelShop.getChannelCode(),MAPPER.writeValueAsString(list));
			    	}
			    	List<ChannelShop> list = new ArrayList<ChannelShop>();
			    	list.add(channelShop);
					redisClient.set("ChannelShop_"+channelShop.getShopCode(),MAPPER.writeValueAsString(list));
				} catch (JsonProcessingException e) {
					e.printStackTrace();
				}
			}
			setActiveMQStatus(channelShop.getShopCode(), channelShop.getShopStatus().toString(), channelShop.getIsSyn());
			String msg=jsonResult.getMessage();
			jsonResult.setMessage(msg+"店铺信息修改成功！！");
		} else {
			// 店铺ID主键不存在
			if(null != channelShop.getShopCode() && !"".equals(channelShop.getShopCode()) ){
				criteria.andShopCodeEqualTo(channelShop.getShopCode());
				List<ChannelShop> list = channelShopMapper.selectByExample(channelShopExample);
				if(list != null && list.size() > 0){
					jsonResult.setIsok(false);
					jsonResult.setMessage("该店铺编号已经存在！！");
					return jsonResult;
				}
			}
			if(channelShop.getShopTitle() != null && !"".equals(channelShop.getShopTitle())){
				criterias.andShopTitleEqualTo(channelShop.getShopTitle());
				List<ChannelShop> list = channelShopMapper.selectByExample(channelShopExamples);
				if(list != null && list.size() > 0) {
					jsonResult.setIsok(false);
					jsonResult.setMessage("该店铺名称已经存在！！");
					return jsonResult;
				}
			}
			// 判断父店铺编号是否存在
			String parentChannelCode = omsChannelInfo.getParentChannelCode();
			if (StringUtil.isNotEmpty(parentChannelCode) && !parentChannelCode.equals(omsChannelInfo.getChannelCode())) {
				OmsChannelInfoExample example = new OmsChannelInfoExample();
				example.or().andChannelCodeEqualTo(parentChannelCode);
				int count = omsChannelInfoMapper.countByExample(example);
				if (count != 1) {
					jsonResult.setIsok(false);
					jsonResult.setMessage("OPENSHOP父店铺编号[" + parentChannelCode + "]不存在！");
					return jsonResult;
				}
			}
				
			if (omsChannelInfo.getChannelId() != null && omsChannelInfo.getChannelId() > 0) {
				// 数据修改
				OmsChannelInfo chanelInfoBean = omsChannelInfoMapper.selectByPrimaryKey(omsChannelInfo.getChannelId());
				if (!omsChannelInfo.getChannelCode().equals(chanelInfoBean.getChannelCode())) {
					// channel_code 属性被修改，判断新channel_code是否已经存在
					if (null != omsChannelInfo.getChannelCode() && !"".equals(omsChannelInfo.getChannelCode())) {
						OmsChannelInfoExample example = new OmsChannelInfoExample();
						example.or().andChannelCodeEqualTo(omsChannelInfo.getChannelCode());
						int count = omsChannelInfoMapper.countByExample(example);
						if (count != 0) {
							jsonResult.setIsok(false);
							jsonResult.setMessage("该OPENSHOP店铺编号已经存在！");
							return jsonResult;
						}
					}
				}
				OmsChannelInfoExample updateExample = new OmsChannelInfoExample();
				updateExample.or().andChannelIdEqualTo(omsChannelInfo.getChannelId());
				omsChannelInfoMapper.updateByExampleSelective(omsChannelInfo, updateExample);
				jsonResult.setIsok(true);
				jsonResult.setMessage("修OPENSHOP店铺编号信息成功！");
			} else {
				// 判断新增数据channel_code是否已经存在
				if (StringUtil.isNotEmpty(omsChannelInfo.getChannelCode())) {
					OmsChannelInfoExample example = new OmsChannelInfoExample(); // 用于查询渠道编号
					example.or().andChannelCodeEqualTo(omsChannelInfo.getChannelCode());
					int count = omsChannelInfoMapper.countByExample(example);
					if (count != 0) {
						// 店铺号存在的情况下，做修改店铺信息处理
						omsChannelInfoMapper.updateByExampleSelective(omsChannelInfo, example);
						jsonResult.setIsok(true);
						jsonResult.setMessage("修改OPENSHOP店铺信息成功！");
					} else {
						omsChannelInfoMapper.insertSelective(omsChannelInfo);
						jsonResult.setIsok(true);
						jsonResult.setMessage("新增OPENSHOP店铺信息成功！");
					}
				}
			}
			channelShop.setShopStatus((byte) 0);  //新增店铺设置为未激活状态
			channelShopMapper.insertSelective(channelShop);
			interfacePropertiesService.syncSecurityInfo(channelShop.getShopCode());
			String msg=jsonResult.getMessage();
			jsonResult.setMessage(msg+"添加店铺信息成功！！");
		}
		return jsonResult;
	}
	
	@Override
	public int deleteChannelShopByPrimaryKey(String ids) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		String [] idsArr =  ids.split(",");
		resultMap.put("idArr", idsArr);
		List<Integer> values = new ArrayList<Integer>();
		for (String shopId : idsArr) {
			values.add(Integer.valueOf(shopId));
		}
		ChannelShopExample channelShopExample = new ChannelShopExample();
		channelShopExample.or().andIdIn(values);
		int i =  channelShopMapper.deleteByExample(channelShopExample);
//		int i =  channelShopMapper.deleteByPrimaryKeys(resultMap);
		// TODO Auto-generated method stub
		return i;
	}
	
	@Override
	public int activeChannelShop(String ids,String shopStatus, String shopCodes) {
		Map<String, Object> resultMap = new HashMap<String, Object>();	
		String [] idsArr =  ids.split(",");
		List<Integer> values = new ArrayList<Integer>();
		for (String shopId : idsArr) {
			values.add(Integer.valueOf(shopId));
		}
		ChannelShopExample channelShopExample = new ChannelShopExample();
		channelShopExample.or().andIdIn(values);
		ChannelShop channelShop = new ChannelShop();
		channelShop.setShopStatus(Byte.valueOf(shopStatus));
		int i =  channelShopMapper.updateByExampleSelective(channelShop, channelShopExample);
		resultMap.clear();
		String [] shopCodesArr =  shopCodes.split(",");
		resultMap.put("shopStatus", shopStatus);
		resultMap.put("shopCodesArr", shopCodesArr);
		OmsChannelInfoExample example = new OmsChannelInfoExample();
		example.or().andChannelCodeIn(Arrays.asList(shopCodesArr));
		OmsChannelInfo omsChannelInfo = new OmsChannelInfo();
		omsChannelInfo.setIsActive(Byte.valueOf(shopStatus));
		int j =  omsChannelInfoMapper.updateByExampleSelective(omsChannelInfo, example);
		for(String shopCode:shopCodesArr){
			ChannelShopExample channelShopExample2= new ChannelShopExample();
			channelShopExample2.or().andShopCodeEqualTo(shopCode);
			List<ChannelShop> shopList = channelShopMapper.selectByExample(channelShopExample2);
			//channelShop信息刷新到缓存
			try {
				if(StringUtils.isNotBlank(shopList.get(0).getChannelCode())){
					ChannelShopExample channelShopExample3 = new ChannelShopExample();
					channelShopExample3.or().andChannelCodeEqualTo(shopList.get(0).getChannelCode()).andShopStatusEqualTo((byte)1);
					List<ChannelShop> list = channelShopMapper.selectByExample(channelShopExample3);
					redisClient.set("ChannelShop_"+shopList.get(0).getChannelCode(),MAPPER.writeValueAsString(list));
				}
			} catch (JsonProcessingException e) {
				log.error("更新缓存失败：",e);
			}
			String itemQueueName = Constants.QUEUE_FIX_ITEM + shopCode;
			String stockQueueName = shopCode + Constants.QUEUE_FIX_STOCK;
			if ("1".equals(shopStatus)) {
				try {
					dynamicListenerManager.registeQueueLintener(itemQueueName, synchItemListener, "2");
				} catch (Exception e) {
					log.error("调整单监听激活异常:" + e.getMessage(), e);
				}
				try {
					dynamicListenerManager.registeQueueLintener(stockQueueName, synchStockListener, "2");
				} catch (Exception e) {
					log.error("库存同步监听激活异常:" + e.getMessage(), e);
				}
			} else {
				try {
					dynamicListenerManager.destroyRegisteMqLintener(itemQueueName);
				} catch (Exception e) {
					log.error("调整单监听注销异常:" + e.getMessage(), e);
				}
				try {
					dynamicListenerManager.destroyRegisteMqLintener(stockQueueName);
				} catch (Exception e) {
					log.error("库存同步监听注销异常:" + e.getMessage(), e);
				}
			}
		}
		return i+j;
	}

	@Override
	public int addChannelKey(InterfaceProperties interfaceProperties) {
		// TODO Auto-generated method stub
		return interfacePropertiesMapper.insert(interfaceProperties);
	}

	@Override
	public int updateChannelKey(InterfaceProperties interfaceProperties) {
		// TODO Auto-generated method stub
		return interfacePropertiesMapper.updateByPrimaryKey(interfaceProperties);
	}

	@Override
	public List<InterfaceProperties> queryChannelKey(
			InterfaceProperties interfaceProperties) {

        InterfacePropertiesExample interfacePropertiesExample = new InterfacePropertiesExample();
        
        com.work.shop.bean.InterfacePropertiesExample.Criteria criteria  = interfacePropertiesExample.createCriteria();
        
        if(null != interfaceProperties.getId()){
        	criteria.andIdEqualTo(interfaceProperties.getId());
        }
        
        if(StringUtil.isNotBlank(interfaceProperties.getProName())){
        	criteria.andProNameEqualTo(interfaceProperties.getProName());
        }
        
        if(StringUtil.isNotBlank(interfaceProperties.getProValue())){
        	criteria.andProValueEqualTo(interfaceProperties.getProValue());
        }
		
		// TODO Auto-generated method stub
		return interfacePropertiesMapper.selectByExample(interfacePropertiesExample);
	}

	@Override
	public List<ChannelShop> getAllChannelShopList(
			ChannelShopExample channelShopExample) {
		// TODO Auto-generated method stub
		return channelShopMapper.selectByExample(channelShopExample);
	}

	//查询父店铺编号列表
	@Override
	public Paging getParentChannelShop(ChannelShopExample channelShopExample) {
		Paging paging = new Paging();
		List<ChannelShop> list = channelShopMapper.selectByExample(channelShopExample);
		int count = channelShopMapper.countByExample(channelShopExample);
		paging.setRoot(list);
		paging.setTotalProperty(count);
		return paging;
	}

	/**
	 * 获取线上在售店铺列表
	 * @return
	 */
	@Override
	public List<ChannelShop> selectOnlineOnsaleChannelShop() {
		List<ChannelShop> list = null;
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			list = bgChannelDbTableMapper.selectOnlineOnsaleChannelShop(map);
		} catch (Exception e) {
			log.error("获取线上在售店铺列表异常", e);
		}
		return list;
	}
	
	private void setActiveMQStatus(String shopCode, String shopStatus, Integer isSyn) {
		try {
			/*QueueMqConfigBean configBean = new QueueMqConfigBean();
			configBean.setServerName("ChannelStockService");
			configBean.setQueueName(shopCode);
			ReturnInfo<List<QueueMqConfig>> info = serverQueueMqService.selectMQQueues(configBean);
			if (info != null && info.getIsOk() == Constant.OS_YES) {
				Integer queueStatus = (shopStatus.equals("1") && isSyn.intValue() == 1) ? 1 : 0;
				if (StringUtil.isListNotNull(info.getData())) {
					for (QueueMqConfig mqConfig : info.getData()) {
						mqConfig.setQueueStatus((byte)1);
						mqConfig.setQueueStatus(queueStatus.byteValue());
						serverQueueMqService.setActiveQueue(mqConfig, 1);
					}
				}
			}*/
		} catch (Exception e) {
			log.error("通知MQ队列变更异常：" + e.getMessage(), e);
		}
	}
}

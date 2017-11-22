package com.work.shop.service.impl;

import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.work.shop.bean.ChannelApiLog;
import com.work.shop.bean.InterfaceProperties;
import com.work.shop.bean.InterfacePropertiesExample;
import com.work.shop.bean.InterfacePropertiesExample.Criteria;
import com.work.shop.dao.ChannelApiLogMapper;
import com.work.shop.dao.InterfacePropertiesMapper;
import com.work.shop.redis.RedisClient;
import com.work.shop.service.InterfacePropertiesService;
import com.work.shop.util.Constants;
import com.work.shop.util.StringUtil;
import com.work.shop.vo.InterfacePropertiesVo;


@Service("interfacePropertiesService")
public class InterfacePropertiesServiceImpl implements
		InterfacePropertiesService {
	
	private final Logger logger = Logger.getLogger(InterfacePropertiesServiceImpl.class);

	@Resource(name = "interfacePropertiesMapper")
	private InterfacePropertiesMapper interfacePropertiesMapper;
	/**    Oms redis    **/
	@Resource
	private RedisClient redisClient;
	
	@Resource
	private ChannelApiLogMapper channelApiLogMapper;
	
	@Override
	public int getInterfacePropertiesCount(InterfacePropertiesExample interfaceProperties) {
		return interfacePropertiesMapper.countByExample(interfaceProperties);
	}

	@Override
	public List<InterfaceProperties> getInterfacePropertiesList(InterfacePropertiesExample example) {
		return interfacePropertiesMapper.selectByExample(example);
	}

	@Override
	public int insertInterfaceProperties(InterfaceProperties interfaceProperties) {
		int count = interfacePropertiesMapper.insert(interfaceProperties);
		if(count > 0){
			syncSecurityInfo(interfaceProperties.getShopCode());
		}
		return count;
	}

	@Override
	public List<InterfaceProperties> queryInterfaceProperties(InterfaceProperties interfaceProperties) {
		InterfacePropertiesExample example = new InterfacePropertiesExample();
		Criteria criteria = example.createCriteria();
		if(null !=interfaceProperties.getId()){
			criteria.andIdEqualTo(interfaceProperties.getId());
		}
		return interfacePropertiesMapper.selectByExample(example);
	}

	@Override
	public int updateInterfaceProperties(InterfaceProperties interfaceProperties) {
		int count = interfacePropertiesMapper.updateByPrimaryKey(interfaceProperties);
		if(count > 0){
			syncSecurityInfo(interfaceProperties.getShopCode());
		}
		return count;
	}

	@Override
	public int deleteMultiInterfaceProperties(String ids) {
		int count = 0 ;
		String [] idsArr =  ids.split(",");
		for (String idStr : idsArr) {
			int id = Integer.parseInt(idStr);
			InterfaceProperties ip = interfacePropertiesMapper.selectByPrimaryKey(id);
			int c = interfacePropertiesMapper.deleteByPrimaryKey(id);
			if(c > 0){
				count++;
				syncSecurityInfo(ip.getShopCode());
			}
		}
		return count;
	}

	@Override
	public List<InterfacePropertiesVo> getInterfacePropertiesVoList(InterfacePropertiesExample example) {
		return interfacePropertiesMapper.selectByVoExample(example);
	}

	@Override
	public int getInterfacePropertiesVoCount(InterfacePropertiesExample interfaceProperties) {
		return interfacePropertiesMapper.countByVoExample(interfaceProperties);
	}

	@Override
	public boolean syncSecurityInfo(String shopCode){
		try {
			InterfacePropertiesExample interfacePropertiesExample = new InterfacePropertiesExample();
			interfacePropertiesExample.or().andShopCodeEqualTo(shopCode);
			List<InterfaceProperties> list = getInterfacePropertiesList(interfacePropertiesExample);
			HashMap<String, String> dataMap = new HashMap<String, String>();
			for (InterfaceProperties interfaceProperties : list) {
				dataMap.put(interfaceProperties.getProName(), interfaceProperties.getProValue());
			}
			String key = Constants.REDIS_FIX_STRING + shopCode;
			if (!dataMap.isEmpty()) {
				redisClient.hmset(key, dataMap);
			}
			return true;
		} catch (Exception e) {
			logger.error("将更新后鉴权信息放入redis异常", e);
		}
		return false;
	}

	/**
	 * 将更新后的权限token更新到权限库中
	 * @param channelCode
	 * @param shopCode
	 * @param authJson
	 * @return
	 */
	@Override
	public void updateTokenAuthInfo(ChannelApiLog apiLog, String channelCode, String shopCode, String newToken) {
		logger.debug("channelCode=" + channelCode + ";shopCode=" +shopCode);
		try {
			String msg = "";
			if (StringUtil.isNotEmpty(newToken)) {
				InterfacePropertiesExample example = new InterfacePropertiesExample();
				example.or().andChannelCodeEqualTo(channelCode).andShopCodeEqualTo(shopCode);
				List<InterfaceProperties> list = interfacePropertiesMapper.selectByExample(example);
				if (!StringUtil.isNotNullForList(list)) {
					logger.error("将更新后的权限token更新到权限库中  获取权限列表失败");
					return ;
				}
				InterfaceProperties interfaceProperties = new InterfaceProperties();
				for (InterfaceProperties properties : list) {
					if (Constants.ACCESS_TOKEN.equals(properties.getProName())) {
						interfaceProperties = properties;
					}
				}
				String oldToken = interfaceProperties.getProValue();
				msg = "原token：" + oldToken + " → 新token:" + newToken;
				interfaceProperties.setProValue(newToken);
				interfacePropertiesMapper.updateByPrimaryKeySelective(interfaceProperties);
				syncSecurityInfo(interfaceProperties.getShopCode());
			}
			apiLog.setReturnMessage(apiLog.getReturnMessage() + msg);
			channelApiLogMapper.insertSelective(apiLog);
			logger.debug("将更新后的权限token更新到权限库中成功！");
		} catch (Exception e) {
			logger.error("将更新后的权限token更新到权限库中 失败", e);
		}
	}
}


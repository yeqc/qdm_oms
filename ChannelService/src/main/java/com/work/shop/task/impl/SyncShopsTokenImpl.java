package com.work.shop.task.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.work.shop.bean.InterfaceProperties;
import com.work.shop.bean.InterfacePropertiesExample;
import com.work.shop.dao.InterfacePropertiesMapper;
import com.work.shop.oms.common.utils.StringUtil;
import com.work.shop.redis.RedisClient;
import com.work.shop.task.SyncShopsToken;
import com.work.shop.util.Constants;
import com.work.shop.util.HttpClientUtil;

@Component("syncShopsTokenImpl")
public class SyncShopsTokenImpl implements SyncShopsToken {
	
	
	@Resource(name = "redisClient")
	private RedisClient redisClient;
	
	@Resource(name = "interfacePropertiesMapper")
	private InterfacePropertiesMapper interfacePropertiesMapper;
	
	private static Logger logger = Logger.getLogger(SyncShopsTokenImpl.class);
	
	@Override
	public void syncToken(Map<String, String> para) {
		
		String channelcode = para.get("channelcode")== null ? "" : para.get("channelcode");
		if(null == channelcode || "" == channelcode)
			return;
		if(channelcode.equals("HQ01S234") || channelcode.equals("HQ08S005")){
			SyncMGJToken(channelcode);
		}	
		if(channelcode.equals("HQ01S237")){
			SyncMLSToken(channelcode);
		}
		if("HQ01S304".equalsIgnoreCase(channelcode)){
			SyncYzToken(channelcode);
		}
	}

	private void SyncYzToken(String channelcode) {
		// TODO Auto-generated method stub
		logger.info("有赞平台token更新调度任务执行："+ new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date())+"---"+channelcode);
		String key = Constants.REDIS_FIX_STRING + channelcode;
		
		// 查询缓存，如果没有查到，则查数据库
		Map<String, String> para = redisClient.hgetAll(key);
		if( para == null || para.size() == 0){
			InterfacePropertiesExample exp = new InterfacePropertiesExample();
			exp.or().andShopCodeEqualTo(channelcode);
			List<InterfaceProperties> items = interfacePropertiesMapper.selectByExample(exp);
			if (null != items) {
				para = new HashMap<String, String>();
				for (InterfaceProperties item : items) {
					para.put(item.getProName(), item.getProValue());
				}	
			}else{
				return;
			}
		}
		
		String YZ_URL = para.get("token_url");
		
		// 参数组装
		List<NameValuePair> valuePairs = new ArrayList<NameValuePair>();
		valuePairs.add(new BasicNameValuePair("client_id", para.get("client_id")));
		valuePairs.add(new BasicNameValuePair("client_secret", para.get("client_secret")));
		valuePairs.add(new BasicNameValuePair("grant_type", "silent"));
		valuePairs.add(new BasicNameValuePair("kdt_id", 19279229+""));
		
		String result = HttpClientUtil.get(YZ_URL, valuePairs);
		logger.info("请求参数:"+JSONObject.toJSONString(valuePairs));
//		String result = "{\"access_token\":\"16e8634d821d3b5e854545d01d36d0a9\",\"expires_in\":604800,\"scope\":\"storage points reviews multi_store salesman pay_qrcode item user trade_advanced trade item_category logistics coupon_advanced shop coupon crm_advanced trade_virtual\"}";
		logger.info("有赞更新token接口返回："+result);
		Map<String, String> map = JSONObject.parseObject(result, Map.class);
		if(null != map && !map.isEmpty() && StringUtil.isNotBlank(map.get("access_token"))){
			InterfaceProperties record = new InterfaceProperties();
			record.setShopCode(channelcode);
			record.setProValue(map.get("access_token"));
			record.setProName("access_token");
			InterfacePropertiesExample example = new InterfacePropertiesExample();
			example.or().andShopCodeEqualTo(channelcode).andProNameEqualTo("access_token");
			
			// 更新表中的token
			try{
			interfacePropertiesMapper.updateByExampleSelective(record,example);
			}catch(Exception e){
				logger.info("数据库查询错误："+e.getMessage());
				throw new RuntimeException("数据库操作异常！",e);
			}
			
			// 查询shopCode为HQ01S304的所有记录		
			InterfacePropertiesExample interfacePropertiesExample = new InterfacePropertiesExample();
			interfacePropertiesExample.or().andShopCodeEqualTo(channelcode);
			List<InterfaceProperties> list = interfacePropertiesMapper.selectByExample(interfacePropertiesExample);
			HashMap<String, String> dataMap = new HashMap<String, String>();
			for (InterfaceProperties interfaceProperties : list) {
				dataMap.put(interfaceProperties.getProName(), interfaceProperties.getProValue());
			}
			// 更新缓存
			if(!dataMap.isEmpty()){
				redisClient.hmset(key, dataMap);
			}
			String erpKey = Constants.SHOP_AUTH_ERP + channelcode;
			String authStr = "ERP" + JSON.toJSONString(dataMap);
			redisClient.setPojo(erpKey, authStr);
		}
		logger.info("有赞token调度任务结束："+ new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date())+"---"+channelcode);
	}

	@SuppressWarnings("unchecked")
	private void SyncMGJToken(String channelcode) {
		
		logger.info("蘑菇街token更新调度任务执行："+ new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date())+"---"+channelcode);
		
		String key = Constants.REDIS_FIX_STRING + channelcode;
		// 查询缓存，如果没有查到，则查数据库
		Map<String, String> para = redisClient.hgetAll(key);
		// 测试
//		para = new  HashMap<String, String>();
		if( para == null || para.size() == 0){
			InterfacePropertiesExample exp = new InterfacePropertiesExample();
			exp.or().andShopCodeEqualTo(channelcode);
			List<InterfaceProperties> items = interfacePropertiesMapper.selectByExample(exp);
			if (null != items) {
				para = new HashMap<String, String>();
				for (InterfaceProperties item : items) {
					para.put(item.getProName(), item.getProValue());
				}	
			}		
		}
		String MGJ_URL = para.get("token_url");
		
		// 参数组装
		List<NameValuePair> valuePairs = new ArrayList<NameValuePair>();
		valuePairs.add(new BasicNameValuePair("app_key", para.get("app_key")));
		valuePairs.add(new BasicNameValuePair("app_secret", para.get("app_secret")));
		valuePairs.add(new BasicNameValuePair("grant_type", "refresh_token"));
		valuePairs.add(new BasicNameValuePair("refresh_token",para.get("refresh_token")));
					
		// 通过POST方式，请求token
		String responses = HttpClientUtil.post(MGJ_URL, valuePairs);	
		logger.info("蘑菇街token刷新接口返回："+responses);
		
		Map<String, String> map = JSONObject.parseObject(responses, Map.class);
		
		if (null != map && !map.isEmpty()&& map.get("statusCode").equals("0000000")) {
			// 填装需要更新的数据
			InterfaceProperties record = new InterfaceProperties();
			record.setShopCode(channelcode);
			record.setProValue(map.get("access_token"));
			record.setProName("access_token");
			InterfacePropertiesExample example = new InterfacePropertiesExample();
			example.or().andShopCodeEqualTo(channelcode).andProNameEqualTo("access_token");
			
			// 更新表中的token
			try{
			interfacePropertiesMapper.updateByExampleSelective(record,example);
			}catch(Exception e){
				logger.info("数据库查询错误："+e.getMessage());
				throw new RuntimeException("数据库操作异常！",e);
			}
			
			 record = new InterfaceProperties();
			 record.setProValue(map.get("refresh_token"));
			 record.setProName("refresh_token");
			 record.setShopCode(channelcode);
			 example = new InterfacePropertiesExample();
		     example.or().andShopCodeEqualTo(channelcode).andProNameEqualTo("refresh_token");
			 
			// 更新表中的refreshtoken
			try{
				interfacePropertiesMapper.updateByExampleSelective(record,example);
				}catch(Exception e){
					logger.info("数据库查询错误："+e.getMessage());
					throw new RuntimeException("数据库操作异常！",e);
				}
			
			// 查询shopCode为HQ01S234的所有记录		
			InterfacePropertiesExample interfacePropertiesExample = new InterfacePropertiesExample();
			interfacePropertiesExample.or().andShopCodeEqualTo(channelcode);
			List<InterfaceProperties> list = interfacePropertiesMapper.selectByExample(interfacePropertiesExample);
			HashMap<String, String> dataMap = new HashMap<String, String>();
			for (InterfaceProperties interfaceProperties : list) {
				dataMap.put(interfaceProperties.getProName(), interfaceProperties.getProValue());
			}
			// 更新缓存
			if(!dataMap.isEmpty()){
				redisClient.hmset(key, dataMap);
			}
			// Oms使用redis数据
//			redisClient.set(key, JSON.toJSONString(dataMap));
			String erpKey = Constants.SHOP_AUTH_ERP + channelcode;
			String authStr = "ERP" + JSON.toJSONString(dataMap);
			redisClient.setPojo(erpKey, authStr);
		}
		
		logger.info("蘑菇街token调度任务结束："+ new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date())+"---"+channelcode);
	}
	
	@SuppressWarnings("unchecked")
	private void SyncMLSToken(String channelcode){

		
		logger.info("美丽说token更新调度任务执行："+ new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date())+"---"+channelcode);
		
		String key = Constants.REDIS_FIX_STRING + channelcode;
		// 查询缓存，如果没有查到，则查数据库
		Map<String, String> para = redisClient.hgetAll(key);
		if( para == null || para.size() == 0){
			InterfacePropertiesExample exp = new InterfacePropertiesExample();
			exp.or().andShopCodeEqualTo(channelcode);
			List<InterfaceProperties> items = interfacePropertiesMapper.selectByExample(exp);
			if (null != items) {
				para = new HashMap<String, String>();
				for (InterfaceProperties item : items) {
					para.put(item.getProName(), item.getProValue());
				}	
			}		
		}
		String MLS_URL = para.get("token_url");
		
		// 参数组装
		List<NameValuePair> valuePairs = new ArrayList<NameValuePair>();
		valuePairs.add(new BasicNameValuePair("app_key", para.get("app_key")));
		valuePairs.add(new BasicNameValuePair("app_secret", para.get("app_secret")));
		valuePairs.add(new BasicNameValuePair("grant_type", "refresh_token"));
		valuePairs.add(new BasicNameValuePair("refresh_token",para.get("refresh_token")));
		valuePairs.add(new BasicNameValuePair("redirect_uri",para.get("http: //www.banggo.com/")));
					
		// 通过POST方式，请求token
		String responses = HttpClientUtil.post(MLS_URL, valuePairs);	
		logger.info("美丽说token刷新接口返回："+responses);
		
//		responses = "{\"access_expires_in\":1471397138,\"access_token\":\"5C91F2F36E3F8458FE0186A58DFB4250\",\"errorMsg\":\"成功！\",\"refresh_expires_in\":1473384338,\"refresh_token\":\"B9E6CCBB0B1136EC3526C70803EADD9B\",\"state\":\"\",\"statusCode\":\"0000000\",\"token_type\":\"Bearer\",\"userId\":\"111j3s7a\"}";
		
		Map<String, String> map = JSONObject.parseObject(responses, Map.class);
		
		if (null != map && !map.isEmpty()&& map.get("statusCode").equals("0000000")) {
			// 填装需要更新的数据
			InterfaceProperties record = new InterfaceProperties();
			record.setShopCode(channelcode);
			record.setProValue(map.get("access_token"));
			record.setProName("access_token");
			InterfacePropertiesExample example = new InterfacePropertiesExample();
			example.or().andShopCodeEqualTo(channelcode).andProNameEqualTo("access_token");
			
			// 更新表中的token
			try{
			interfacePropertiesMapper.updateByExampleSelective(record,example);
			}catch(Exception e){
				logger.info("数据库查询错误："+e.getMessage());
				throw new RuntimeException("数据库操作异常！",e);
			}
			
			 record = new InterfaceProperties();
			 record.setProValue(map.get("refresh_token"));
			 record.setProName("refresh_token");
			 record.setShopCode(channelcode);
			 example = new InterfacePropertiesExample();
		     example.or().andShopCodeEqualTo(channelcode).andProNameEqualTo("refresh_token");
			 
			// 更新表中的refreshtoken
			try{
				interfacePropertiesMapper.updateByExampleSelective(record,example);
				}catch(Exception e){
					logger.info("数据库查询错误："+e.getMessage());
					throw new RuntimeException("数据库操作异常！",e);
				}
			
			// 查询shopCode为HQ01S234的所有记录		
			InterfacePropertiesExample interfacePropertiesExample = new InterfacePropertiesExample();
			interfacePropertiesExample.or().andShopCodeEqualTo(channelcode);
			List<InterfaceProperties> list = interfacePropertiesMapper.selectByExample(interfacePropertiesExample);
			HashMap<String, String> dataMap = new HashMap<String, String>();
			for (InterfaceProperties interfaceProperties : list) {
				dataMap.put(interfaceProperties.getProName(), interfaceProperties.getProValue());
			}
			// 更新缓存
			if (!dataMap.isEmpty()) {
				redisClient.hmset(key, dataMap);
			}
			// Oms使用redis数据
//			redisClient.set(key, JSON.toJSONString(dataMap));
			String erpKey = Constants.SHOP_AUTH_ERP + channelcode;
			String authStr = "ERP" + JSON.toJSONString(dataMap);
			redisClient.setPojo(erpKey, authStr);
		}
		
		logger.info("美丽说token调度任务结束："+ new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date())+"---"+channelcode);
		
	}

}

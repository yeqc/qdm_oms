package com.work.shop.controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.work.shop.bean.ChannelApiLog;
import com.work.shop.bean.ChannelShop;
import com.work.shop.bean.InterfaceProperties;
import com.work.shop.bean.InterfacePropertiesExample;
import com.work.shop.redis.RedisClient;
import com.work.shop.service.ChannelShopService;
import com.work.shop.service.InterfacePropertiesService;
import com.work.shop.util.Constants;
import com.work.shop.util.HttpClientUtil;
import com.work.shop.util.StringUtil;
import com.work.shop.vo.JsonResult;

@Controller
@RequestMapping(value = "auth")
public class ChannelAuthController extends BaseController {
	private static final Logger logger = Logger.getLogger(ChannelAuthController.class);
	
	@Resource(name = "redisClient")
	private RedisClient redisClient;
	
	@Resource(name = "interfacePropertiesService")
	private InterfacePropertiesService interfacePropertiesService;
	
	@Resource(name = "channelShopService")
	ChannelShopService channelShopService;
	
	/*
	 * 更新渠道授权信息 回调页面url
	 */
	@RequestMapping(value = "/updateAuth")
	public void updateAuth(HttpServletRequest request,
			HttpServletResponse response , String code, String state, String shopCode) {
		logger.debug("更新渠道授权信息 回调页面url:code=" + code + ";state" + state);
		JsonResult jsonResult = new JsonResult();
		jsonResult.setIsok(false);
		if (StringUtil.isEmpty(code) || StringUtil.isEmpty(state)) {
			jsonResult.setMessage("回调页面url: code=" + code + ";state" + state + "都不能为空！");
			outPrintJson(response, jsonResult);
			return;
		}
		try {
//			String dataMap = redisClient.get(shopCode);
		} catch (Exception e) {
			logger.error("更新渠道授权信息异常", e);
		}
		outPrintJson(response, jsonResult);
	}

	/*
	 * 更新渠道授权信息 回调页面url
	 */
	@RequestMapping(value = "/redirect")
	public ModelAndView redirect(HttpServletRequest request,
			HttpServletResponse response , String code, String state) {
		logger.debug("更新渠道授权信息 回调页面url:code=" + code + ";state" + state);
		JsonResult jsonResult = new JsonResult();
		jsonResult.setIsok(false);
		ModelAndView mav = new ModelAndView();
		mav.setViewName("auth/authResult");
		if (StringUtil.isEmpty(code) || StringUtil.isEmpty(state)) {
			jsonResult.setMessage("回调页面url: code=" + code + ";state=" + state + "都不能为空！");
			mav.addObject("result", jsonResult);
			return mav;
		}
		try {
			String userName = getUserName(request);
			if (StringUtil.isEmpty(userName)) {
				jsonResult.setMessage("回调页面url:没有登录，请先登录！");
				mav.addObject("result", jsonResult);
				return mav;
			}
			String key = Constants.REDIS_FIX_STRING + state;
			Map<String, String> authMap = redisClient.hgetAll(key);
			if (authMap == null || authMap.isEmpty()) {
				authMap = getSecurityInfo(state);
				if (authMap == null || authMap.isEmpty()) {
					jsonResult.setMessage("回调页面url:没有获取到" + state + "店铺授权信息！");
					results(null, state, userName, "1", null);
					mav.addObject("result", jsonResult);
					return mav;
				}
			}
			ChannelShop channelShop = channelShopService.selectChannelShopByShopCode(state);
			if (channelShop == null) {
				logger.error("没有获取到" + state + "店铺授权信息！");
				jsonResult.setMessage("回调页面url:没有获取到" + state + "店铺授权信息！");
				results(null, state, userName, "1", null);
				mav.addObject("result", jsonResult);
				return mav;
			}
			logger.debug("提交获取token信息数据 start");
			String authUrl = "";
			List<NameValuePair> valuePairs = new ArrayList<NameValuePair>();
			valuePairs.add(new BasicNameValuePair("code", code));
			valuePairs.add(new BasicNameValuePair("state", state));
			valuePairs.add(new BasicNameValuePair("grant_type", "authorization_code"));
			valuePairs.add(new BasicNameValuePair("client_id", (String) authMap.get("app_key")));
			valuePairs.add(new BasicNameValuePair("client_secret", (String) authMap.get("app_secret")));
			valuePairs.add(new BasicNameValuePair("redirect_uri", Constants.REDIRECT_URI));
			if (Constants.JD_CHANNEL_CODE.equals(channelShop.getChannelCode())) {
				authUrl = Constants.JD_TOKEN_URl;
			} else if (Constants.TB_CHANNEL_CODE.equals(channelShop.getChannelCode())) {
				authUrl = Constants.TB_TOKEN_URl;
				valuePairs.add(new BasicNameValuePair("view", "web"));
			}
			String result = HttpClientUtil.post(authUrl, valuePairs);
			
		//	String result ="{\"access_token\": \"66323509-ef0e-4348-b3fc-733a03d550c3\",\"code\": 0,\"expires_in\": 24930107, \"refresh_token\": \"fe51870d-cf68-44b6-abdd-e7cdda9f521b\", \"time\": \"1433404470170\", \"token_type\": \"bearer\", \"uid\": \"8193864460\", \"user_nick\": \"jd_metersbonwenzsg\"}";
			
			logger.debug("更新渠道授权信息" + result);
			jsonResult = results(channelShop.getChannelCode(), state, userName, "0", result);
//			if(!jsonResult.isIsok()) {
//				mav.setViewName("auth/errorAuthResult");
//				mav.addObject("result", jsonResult);
//				return mav;
//			}
//			mav.addObject("result", jsonResult);
//			return mav;
		} catch (Exception e) {
			logger.error("更新渠道授权信息异常", e);
			jsonResult.setMessage("更新渠道授权信息异常" + e.getMessage());
//			mav.setViewName("auth/errorAuthResult");
		}
		mav.addObject("result", jsonResult);
		return mav;
	}
	
	/*
	 * 更新渠道授权信息 回调页面url
	 */
	@RequestMapping(value = "/initAuth")
	public ModelAndView initAuth(HttpServletRequest request,
			HttpServletResponse response , String method, String state,
			String shopCode, String channelCode) {
		logger.debug("更新渠道授权信息 回调页面url: method=" + method + ";state" + state);
		JsonResult jsonResult = new JsonResult();
		jsonResult.setIsok(false);
		ModelAndView mav = new ModelAndView();
		if ("init".equals(method)) {
			mav.setViewName("auth/initAuthPage");
			return mav;
		} else if ("get".equals(method)) {
			try {
				String userName = getUserName(request);
				if (StringUtil.isEmpty(userName)) {
					jsonResult.setMessage("没有登录，请先登录！");
					outPrintJson(response, jsonResult);
					return null;
				}
				String key = Constants.REDIS_FIX_STRING + shopCode;
				Map<String, String> authMap = redisClient.hgetAll(key);;
//				Map<String, Object> authMap = JSON.parseObject(shopAuth, Map.class);
				if (authMap == null || authMap.isEmpty()) {
					authMap = getSecurityInfo(shopCode);
					if (authMap == null || authMap.isEmpty()) {
						jsonResult.setMessage("没有获取到" + shopCode + "店铺授权信息！");
						outPrintJson(response, jsonResult);
						return null;
					}
				}
				StringBuffer buffer = new StringBuffer();
				if (Constants.JD_CHANNEL_CODE.equals(channelCode)) {
					buffer.append(Constants.JD_AUTH_URl + "?");
					buffer.append(Constants.RESPONSE_TYPE);
					buffer.append(getParams("app_key", "client_id", authMap));
					buffer.append("&redirect_uri=" + Constants.REDIRECT_URI);
					buffer.append("&state=" + shopCode);
				} else if (Constants.TB_CHANNEL_CODE.equals(channelCode)) {
					buffer.append(Constants.TB_AUTH_URl + "?");
					buffer.append("&" + Constants.RESPONSE_TYPE);
					buffer.append(getParams("app_key", "client_id", authMap));
					buffer.append("&redirect_uri=" + Constants.REDIRECT_URI);
					buffer.append("&state=" + shopCode);
					buffer.append(Constants.TB_TOKEN_VIEW);
				}
				logger.debug(buffer);
				jsonResult.setIsok(true);
				jsonResult.setData(buffer);
			} catch (Exception e) {
				logger.error("更新渠道授权信息异常", e);
			}
			outPrintJson(response, jsonResult);
		}
		return null;
	}
	
	private Map<String, String> getSecurityInfo(String shopCode){
		InterfacePropertiesExample example = new InterfacePropertiesExample();
		example.or().andShopCodeEqualTo(shopCode);
		List<InterfaceProperties> list = interfacePropertiesService.getInterfacePropertiesList(example);
		HashMap<String, String> dataMap = new HashMap<String, String>();
		for (InterfaceProperties interfaceProperties : list) {
			dataMap.put(interfaceProperties.getProName(), interfaceProperties.getProValue());
		}
		return dataMap;
	}
	
	private String getParams(String paramKey, String replaceKey, Map<String, String> authMap) {
		// grant_type=authorization_code
		if (StringUtil.isEmpty(paramKey)) {
			logger.error("paramKey=" + paramKey + "为空");
			return "";
		}
		if (StringUtil.isEmpty(replaceKey)) {
			logger.error("replaceKey=" + replaceKey + "为空");
			return "";
		}
		String paramValue = (String) authMap.get(paramKey);
		if (StringUtil.isEmpty(paramValue)) {
			logger.error("paramValue=" + paramValue + "为空");
			return "";
		}
		return "&" + replaceKey + "=" + paramValue;
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/results")
	public JsonResult results(String channelCode, String shopCode, String userName, String resultCode, String tokenJson) {
		JsonResult jsonResult = new JsonResult();
		jsonResult.setIsok(false);
		String newToken = "";
		String msg = "";
		Integer expiresIn = null;
		String time = null;
		Integer code = 1;
		try {
	//		tokenJson ="{\"access_token\": \"66323509-ef0e-4348-b3fc-733a03d550c3\",\"code\": 0,\"expires_in\": 24930107, \"refresh_token\": \"fe51870d-cf68-44b6-abdd-e7cdda9f521b\", \"time\": 1433404470170, \"token_type\": \"bearer\", \"uid\": \"8193864460\", \"user_nick\": \"jd_metersbonwenzsg\"}";
//			tokenJson = "{\"access_token\": \"50c69b33-4a10-4e80-a0d3-577d65ceca09\", \"code\": 0, \"expires_in\": 28768730, \"refresh_token\": \"5aacdc02-2198-4d21-89a2-0d0c4c1534a0\", \"time\": \"1433665320086\", \"token_type\": \"bearer\", \"uid\": \"3724844023\", \"user_nick\": \"jd_Moomoo2015\" }";
			if (StringUtil.isNotEmpty(tokenJson)) {
				Map<String, Object> authMap = JSON.parseObject(tokenJson, Map.class);
				if (authMap != null && !authMap.isEmpty()) {
					code = (Integer) authMap.get("code");
					msg = tokenJson + "\n";
					if (code != null && code.equals(0)) {
						newToken = (String) authMap.get(Constants.ACCESS_TOKEN);
						expiresIn = (Integer) authMap.get("expires_in");
						time = (String) authMap.get("time");
						if (StringUtil.isNotEmpty(time)) {
							long lExpiresIn = expiresIn * 1000;
							Calendar expiresCal = Calendar.getInstance();
							expiresCal.setTimeInMillis(Long.valueOf(time) + lExpiresIn);
							channelShopService.updateChannelShop(shopCode, channelCode, expiresCal.getTime());
						}
					}
				} else {
					resultCode = "1";
				}
			}
		} catch (Exception e) {
			logger.error("格式化新token字符串失败：tokenJson " + tokenJson, e);
			msg = "格式化新token字符串失败：tokenJson " + tokenJson;
			resultCode = "1";
			jsonResult.setMessage(msg);
		}
		ChannelApiLog apiLog = new ChannelApiLog();
		apiLog.setChannelCode(channelCode);
		apiLog.setShopCode(shopCode);
		apiLog.setRequestTime(new Date());
		apiLog.setUser(userName);
		apiLog.setParamInfo(shopCode);
		apiLog.setMethodName("12"); // 调整单类型
		apiLog.setReturnCode(resultCode);
		apiLog.setReturnMessage(msg);
		interfacePropertiesService.updateTokenAuthInfo(apiLog, channelCode, shopCode, newToken);
		if (code != null && code == 0) {
			resultCode = "0";
			jsonResult.setIsok(true);
			jsonResult.setMessage("渠道更新token成功！");
		} else {
			resultCode = "1";
			jsonResult.setMessage("渠道更新token失败！"+tokenJson);
			jsonResult.setIsok(false);
		}
		return jsonResult;
	}
	
//	private int  DateTimeDifference(int expiresIn, Long time) {
//		
//		long lExpiresIn = expiresIn*1000;
//		
//		Calendar expiresCal = Calendar.getInstance();	
//		expiresCal.setTimeInMillis(time+lExpiresIn);
//		
//		Calendar currCal = Calendar.getInstance();
//		currCal.setTimeInMillis(time);
//		
//	    long diff = expiresCal.getTimeInMillis() - currCal.getTimeInMillis();
//	    int days = new Long( diff / (1000 * 60 * 60 * 24)).intValue();
//
//		return days;
//		
//	}

}

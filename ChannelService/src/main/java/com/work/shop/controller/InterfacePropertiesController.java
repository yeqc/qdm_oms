package com.work.shop.controller;

import java.io.IOException;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.work.shop.bean.InterfaceProperties;
import com.work.shop.bean.InterfacePropertiesExample;
import com.work.shop.bean.InterfacePropertiesExample.Criteria;
import com.work.shop.service.InterfacePropertiesService;
import com.work.shop.util.StringUtil;
import com.work.shop.util.extjs.Paging;
import com.work.shop.vo.InterfacePropertiesVo;
import com.work.shop.vo.JsonResult;

@Controller
@RequestMapping(value = "interfaceProperties")
public class InterfacePropertiesController extends BaseController {
	private static final Logger logger = Logger.getLogger(InterfacePropertiesController.class);

	@Resource(name = "interfacePropertiesService")
	private InterfacePropertiesService interfacePropertiesService;
	
//	@Resource(name = "redisClient")
//	private RedisClient redisClient;
	/***
	 *进入配置页
	 */
	@RequestMapping(value = "interfacePropertiesList.spmvc")
	public ModelAndView interfacePropertiesList() {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("interfaceProperties/interfacePropertiesList");
		return mav;
	}

	@RequestMapping(value = "getInterfacePropertiesList.spmvc")
	public void getExternalChannelShopList(
			HttpServletRequest request,
			HttpServletResponse response,
			@ModelAttribute("interfacePropertiesVo") InterfacePropertiesVo interfaceProperties)
			throws IOException {
		//起始行
		String start = request.getParameter("start") == null ? "" : request.getParameter("start");
		//一页几行
		String limit = request.getParameter("limit") == null ? "" : request.getParameter("limit");

		InterfacePropertiesExample example = new InterfacePropertiesExample();
		Criteria criteria = example.createCriteria();
		criteria.limit(Integer.parseInt(start), Integer.parseInt(limit));
		//配置名称
		if (StringUtil.isNotBlank(interfaceProperties.getProName())) {
			criteria.andProNameLike("%" + interfaceProperties.getProName() + "%");
		}
		//配置值
		if (StringUtil.isNotBlank(interfaceProperties.getProValue())) {
			criteria.andProValueLike("%" + interfaceProperties.getProValue() + "%");
		}
		
		//店铺名称
		if (StringUtil.isNotBlank(interfaceProperties.getShopTitle())) {
			criteria.andShopTitleLike("%" + interfaceProperties.getShopTitle() + "%");
		}	
		
		//渠道名称
		if (StringUtil.isNotBlank(interfaceProperties.getChannelTitle())) {
			criteria.andChannlTitleLike("%" + interfaceProperties.getChannelTitle() + "%");
		}
		Paging paging = new Paging(interfacePropertiesService.getInterfacePropertiesVoCount(example),
				interfacePropertiesService.getInterfacePropertiesVoList(example));
		writeJson(paging, response);
	}

	@RequestMapping(value = "toAddInterfaceProperties.spmvc")
	public ModelAndView toAddInterfaceProperties() {
		ModelAndView mav = new ModelAndView();
		// ChannelShop channelShop = new ChannelShop();

		// mav.addObject(channelShop);
		mav.setViewName("interfaceProperties/addInterfaceProperties");
		return mav;
	}

	/**
	 * 进入修改页面
	 * 
	 * @param request
	 * @param response
	 * @param interfaceProperties
	 * @return ModelAndView
	 */
	@RequestMapping(value = "updateInterfaceProperties.spmvc")
	public ModelAndView updateInterfaceProperties(
			HttpServletRequest request,
			HttpServletResponse response,
			@ModelAttribute("interfaceProperties") InterfaceProperties interfaceProperties) {

		ModelAndView mav = new ModelAndView();

		InterfaceProperties ip = new InterfaceProperties();
		if ("init".equals(request.getParameter("type"))) {

			ip.setId(interfaceProperties.getId());
			List<InterfaceProperties> list = interfacePropertiesService
					.queryInterfaceProperties(ip);

			if (list.size() > 0) {
				mav.addObject("ip", list.get(0));
				mav.setViewName("interfaceProperties/addInterfaceProperties");
				return mav;
			}

		}
		return null;
	}

	/***
	 * 插入和修改配置信息
	 ***/
	@RequestMapping(value = "/insertInterfaceProperties.spmvc")
	public void insertInterfaceProperties(
			HttpServletResponse response,
			@ModelAttribute("interfaceProperties") InterfaceProperties interfaceProperties) {

		JsonResult jsonResult = new JsonResult();

		InterfacePropertiesExample interfacePropertiesExample = new InterfacePropertiesExample();
		Criteria criteria = interfacePropertiesExample.createCriteria();

		if (StringUtil.isNotBlank(interfaceProperties.getProName())) {
			criteria.andProNameEqualTo(interfaceProperties.getProName());
		}

		if (StringUtil.isNotBlank(interfaceProperties.getProValue())) {
			criteria.andProValueEqualTo(interfaceProperties.getProValue());
		}

		if (StringUtil.isNotBlank(interfaceProperties.getShopCode())) {
			criteria.andShopCodeEqualTo(interfaceProperties.getShopCode());
		}

		if (StringUtil.isNotBlank(interfaceProperties.getChannelCode())) {
			criteria
					.andChannelCodeEqualTo(interfaceProperties.getChannelCode());
		}

		List<InterfaceProperties> list = interfacePropertiesService
				.getInterfacePropertiesList(interfacePropertiesExample);

		if (0 < list.size()) {
			jsonResult.setIsok(false);
			jsonResult.setMessage("已有密钥数据！");
			outPrintJson(response, jsonResult);
			return;
		}

		if (null != interfaceProperties.getId()) {
			int re = interfacePropertiesService
					.updateInterfaceProperties(interfaceProperties);
			if (re > 0) {
				jsonResult.setIsok(true);
				jsonResult.setMessage("修改成功！");
			} else {
				jsonResult.setIsok(false);
				jsonResult.setMessage("修改失败！");
			}
			outPrintJson(response, jsonResult);
		} else {
			int re = interfacePropertiesService
					.insertInterfaceProperties(interfaceProperties);
			if (re > 0) {
				jsonResult.setIsok(true);
				jsonResult.setMessage("添加成功！");
			} else {
				jsonResult.setIsok(false);
				jsonResult.setMessage("添加失败！");
			}
			outPrintJson(response, jsonResult);
			return;
		}

	}

	// 批量删除ids
	@RequestMapping(value = "/deleteInterfaceProperties.spmvc")
	public void deleteInterfaceProperties(HttpServletRequest request,
			HttpServletResponse response) {
		JsonResult jsonResult = new JsonResult();
		String ids = StringUtils.trim(request.getParameter("ids"));
		try {
			int num = interfacePropertiesService
					.deleteMultiInterfaceProperties(ids);
			if (num > 0) {
				jsonResult.setIsok(true);
				jsonResult.setMessage("删除成功！");
			} else {
				jsonResult.setIsok(false);
				jsonResult.setMessage("删除失败！");
			}
			outPrintJson(response, jsonResult);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public InterfacePropertiesService getInterfacePropertiesService() {
		return interfacePropertiesService;
	}

	public void setInterfacePropertiesService(
			InterfacePropertiesService interfacePropertiesService) {
		this.interfacePropertiesService = interfacePropertiesService;
	}
	
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
	@RequestMapping(value = "/initAuth")
	public String initAuth(HttpServletRequest request,
			HttpServletResponse response , String method, String state, String shopCode) {
		logger.debug("更新渠道授权信息 回调页面url: method=" + method + ";state" + state);
		JsonResult jsonResult = new JsonResult();
		jsonResult.setIsok(false);
		if ("init".equals(method)) {
			return "auth/updateAuthPage";
		} else if ("get".equals(method)) {
			try {
//				String dataMap = redisClient.get(shopCode);
			} catch (Exception e) {
				logger.error("更新渠道授权信息异常", e);
			}
			outPrintJson(response, jsonResult);
		}
		
		
		return null;
	}
}

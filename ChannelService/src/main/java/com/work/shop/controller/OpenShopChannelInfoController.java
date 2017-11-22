package com.work.shop.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.work.shop.bean.OmsChannelInfo;
import com.work.shop.service.ChannelInfoService;
import com.work.shop.service.ChannelShopService;
import com.work.shop.service.OpenShopChannelInfoService;
import com.work.shop.util.StringUtil;
import com.work.shop.vo.JsonResult;
import com.work.shop.vo.OpenShopChannelInfoVO;

@Controller
@RequestMapping(value = "/openshop/channelinfo/")
public class OpenShopChannelInfoController extends BaseController {

	@Resource(name = "openShopChannelInfoService")
	private OpenShopChannelInfoService openShopChannelInfoService;
	
	@Resource(name = "channelInfoService")
	private ChannelInfoService channelInfoService;
	
	@Resource(name = "channelShopService")
	private ChannelShopService channelShopService;
	
	
	

	@RequestMapping(value = "/search.spmvc")
	public void search(@ModelAttribute OpenShopChannelInfoVO searchVO, HttpServletRequest request, HttpServletResponse response) {
		JsonResult result = openShopChannelInfoService.searchOpenShopChannelInfoList(searchVO);
		outPrintJson(response, result.getData());
	}

	@RequestMapping(value = "/delete.spmvc")
	public void delete(@ModelAttribute OpenShopChannelInfoVO searchVO, HttpServletRequest request, HttpServletResponse response) {
		String channelCodes = request.getParameter("channelCodes");
		if (StringUtil.isEmpty(channelCodes)) {
			return;
		}
		String[] channelCodeArr = channelCodes.split(",");
		if (channelCodeArr == null || channelCodeArr.length == 0) {
			return;
		}
		List<String> channelCodeList = new ArrayList<String>();
		for (String channelCode : channelCodeArr) {
			channelCodeList.add(channelCode);
		}
		JsonResult jsonResult = openShopChannelInfoService.deleteOpenShopChannelInfo(channelCodeList);
		outPrintJson(response, jsonResult);
	}

	@RequestMapping(value = "/select.spmvc")
	public void select(@ModelAttribute OpenShopChannelInfoVO searchVO, HttpServletRequest request, HttpServletResponse response) {
		JsonResult result = openShopChannelInfoService.searchOnlyChannelInfo(searchVO);
		outPrintJson(response, result);
	}

	@RequestMapping(value = "/addOrUpdate.spmvc")
	public void addOrUpdate(HttpServletRequest request, HttpServletResponse response, @ModelAttribute OmsChannelInfo omsChannelInfo) {
		// 数据校验
		JsonResult result = new JsonResult();
		if (omsChannelInfo == null) {
			result.setIsok(false);
			result.setMessage("参数为空!");
		} else if (StringUtil.isEmpty(omsChannelInfo.getChannelCode())) {
			result.setIsok(false);
			result.setMessage("店铺编号为空!");
		} else if (StringUtil.isEmpty(omsChannelInfo.getChannelName())) {
			result.setIsok(false);
			result.setMessage("店铺名称为空!");
		} else if (StringUtil.isEmpty(omsChannelInfo.getChannelType())) {
			result.setIsok(false);
			result.setMessage("渠道类型为空!");
		} else if (omsChannelInfo.getIsActive() == null) {
			result.setIsok(false);
			result.setMessage("是否激活为空!");
		} else {
			// 数据添加或修改
			if (StringUtil.isEmpty(omsChannelInfo.getParentChannelCode())) {
				omsChannelInfo.setParentChannelCode(omsChannelInfo.getChannelCode());
			}
			result = openShopChannelInfoService.addOrUpdateOpenShopChannelInfo(omsChannelInfo);
		}
		outPrintJson(response, result);

	}

	@RequestMapping(value = "/searchChannelInfo.spmvc")
	public void searchChannelInfo(@ModelAttribute OpenShopChannelInfoVO searchVO, HttpServletRequest request, HttpServletResponse response) {
		JsonResult result = openShopChannelInfoService.searchOnlyChannelInfo(searchVO);
		outPrintJson(response, result);
	}
	@RequestMapping(value = "/findChannelInfoByChannelType.spmvc")
	public void findChannelInfoByChannelType( HttpServletRequest request, HttpServletResponse response){
		int channelType=Integer.parseInt(request.getParameter("channelType"));
		JsonResult result =channelInfoService.findShopChannelInfoByChanneltype(channelType);
		outPrintJson(response, result);
	}
	@RequestMapping(value = "/findChannelShopByChannelCode.spmvc")
	public void findChannelShopByChannelCode( HttpServletRequest request, HttpServletResponse response){
		String channelCode=request.getParameter("channelCode");
		JsonResult result =channelShopService.findShopChannelShopByChannelCode(channelCode);
		outPrintJson(response, result);
	}
	
	

}

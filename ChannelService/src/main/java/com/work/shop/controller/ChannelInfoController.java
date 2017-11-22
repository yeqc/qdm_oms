package com.work.shop.controller;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.work.shop.bean.ChannelInfo;
import com.work.shop.bean.ChannelInfoExample;
import com.work.shop.bean.ChannelInfoExample.Criteria;
import com.work.shop.service.ChannelInfoService;
import com.work.shop.util.StringUtil;
import com.work.shop.util.extjs.Paging;
import com.work.shop.vo.JsonResult;

@Controller
@RequestMapping(value= "channelInfo")
public class ChannelInfoController extends BaseController {
	
	@Resource(name = "channelInfoService")
	private ChannelInfoService channelInfoService;
	
	//树状导航栏页面跳转
	@RequestMapping(value = "toChannelInfoList.spmvc")
	public ModelAndView channelShopListPage() {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("channelBaseInfo/channelInfoList");
		return mav;
	}
	
	//点击添加按钮页面跳转到添加页面，自动生成渠道code
	@RequestMapping(value = "toAddChannelInfo.spmvc")
	public ModelAndView toAddChannelInfo() {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("channelBaseInfo/addChannelInfo");
		return mav;
	}
	
	//获取渠道列表
	@RequestMapping(value = "/getChannelInfoList.spmvc")
	public void getChannelInfoList(HttpServletRequest request,HttpServletResponse response,
			@ModelAttribute("channelInfo") ChannelInfo channnelInfo){
		String start = request.getParameter("start") == null ? "" : request.getParameter("start");
		String limit = request.getParameter("limit") == null ? "" : request.getParameter("limit");
		
		ChannelInfoExample channelInfoExample = new ChannelInfoExample();
		Criteria criteria = channelInfoExample.or();
		
		criteria.limit(Integer.parseInt(start), Integer.parseInt(limit));
		
		if(null != channnelInfo.getChannelTitle() && !"".equals(channnelInfo.getChannelTitle())){
			criteria.andChannelTitleLike("%"+channnelInfo.getChannelTitle()+"%");	
		}
		
		if(null != channnelInfo.getChannelType()){
			criteria.andChannelTypeEqualTo(channnelInfo.getChannelType());	
		}
		
		Paging paging = channelInfoService.getChannelInfoList(channelInfoExample);
		
		try {
			writeJson(paging, response);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//更新，新增记录
	@RequestMapping(value = "/insertChannelInfo.spmvc")
	public void insertChannelInfo(HttpServletResponse response,@ModelAttribute("channelInfo") ChannelInfo channelInfo){
		JsonResult jsonResult = channelInfoService.updateChannelInfo(channelInfo);
		outPrintJson(response, jsonResult);
	}
	
	//查看渠道信息--》带入页面
	@RequestMapping(value = "/viewChannelInfo.spmvc")
	public ModelAndView viewChannelInfo(HttpServletRequest httpServletRequest,HttpServletResponse response,
			@ModelAttribute("id") String id	){
		ModelAndView modelAndView = new ModelAndView();
		ChannelInfo channelInfo = new ChannelInfo();
		if(id != null && !"".equals(id)){
			channelInfo = channelInfoService.getChannelInfo(Integer.parseInt(id));
			modelAndView.addObject(channelInfo);
		}
		modelAndView.setViewName("channelBaseInfo/addChannelInfo");
		return modelAndView;
		
	}
	
	
	//激活
	@RequestMapping(value = "/activeChannelInfo.spmvc")
	public void activeChannelInfo(HttpServletResponse response,@ModelAttribute("ids") String ids
			,@ModelAttribute("channelStatus") String channelStatus){
		JsonResult jsonResult = channelInfoService.updateChannelStatus(ids,channelStatus);
		outPrintJson(response, jsonResult); 
	}
	
	//移除
	@RequestMapping(value = "/deleteChannelInfo.spmvc")
	public void deleteChannelInfo(HttpServletResponse response,@ModelAttribute("ids") String ids){
		JsonResult jsonResult = channelInfoService.deleteChannelInfo(ids);
		outPrintJson(response, jsonResult);
	}
	
	//渲染渠道选择框，默认把激活状态（channel_status=1）的渠道加载到选择框中。。。
	@RequestMapping(value = "/channelList.spmvc")
	public void getChannelShopList(HttpServletRequest request, HttpServletResponse response, Short[] type) {
		ChannelInfoExample channelInfoExample = new ChannelInfoExample(); 
		Criteria criteria =channelInfoExample.or();
		criteria.andChannelStatusEqualTo(Short.valueOf("1"));
		if (StringUtil.isArrayNotNull(type)) {
			List<Short> values = Arrays.asList(type);
			criteria.andChannelTypeIn(values);
		}
		Paging paging = channelInfoService.getChannelInfoList(channelInfoExample);
		@SuppressWarnings("unchecked")
		List<ChannelInfo> list = (List<ChannelInfo>) paging.getRoot();
		outPrintJson(response, list);
	}
}

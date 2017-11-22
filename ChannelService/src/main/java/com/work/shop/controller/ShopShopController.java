package com.work.shop.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.work.shop.bean.ChannelShop;
import com.work.shop.bean.OmsChannelInfo;
import com.work.shop.service.OpenShopChannelInfoService;
import com.work.shop.service.ShopService;

@Controller
@RequestMapping(value="shopShop")
public class ShopShopController extends BaseController{

	@Resource(name = "shopService")
	private ShopService shopService;
	
	@Resource(name = "openShopChannelInfoService")
	private OpenShopChannelInfoService openShopChannelInfoService;
	
	//树状链接条状
	@RequestMapping(value = "shopShopList.spmvc")
	public ModelAndView ChannelShopListPage() {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("shopShop/shopShopList");
		return mav;
	}
	
	@RequestMapping(value = "addShopShop.spmvc")
	public ModelAndView toAddChannelShopPage() {
		ModelAndView mav = new ModelAndView();
		ChannelShop channelShop = new ChannelShop();
	//	String shopCode = "DP"+ DateTimeUtils.format(new Date(), "yyyy_MM_dd_HH_mm_ss").replace("_", "")+StringUtil.getSysCode();
	//	channelShop.setShopCode(shopCode);
		mav.addObject(channelShop);
		mav.setViewName("shopShop/addShopShop");
		return mav;
	}
	
	//点击更新查看的时候把，对象信息带到页面展示出来
	@RequestMapping(value = "/updateShopShop.spmvc")
	public ModelAndView updateChannelShop(HttpServletRequest request,HttpServletResponse response) {
		
		ModelAndView mav = new ModelAndView();
		OmsChannelInfo omsChannelInfo = new OmsChannelInfo();
		String id = StringUtils.trim(request.getParameter("id"));
		ChannelShop cs = new ChannelShop();
		ChannelShop scs = null;
		if (!"".equals(id) && "init".equals(request.getParameter("type"))) {
			try {
				cs.setId(Integer.valueOf(id));
				scs = shopService.queryChannelShop(cs);
				omsChannelInfo = openShopChannelInfoService.findOmsChannelInfoBychannelcode(scs.getShopCode());
				mav.addObject("osci", omsChannelInfo);
				mav.addObject("scs", scs);
				mav.setViewName("shopShop/addShopShop");
				return mav;
			} catch (Exception e) {
				e.printStackTrace();
			}
		} 
		return null;
	}

	

	
}

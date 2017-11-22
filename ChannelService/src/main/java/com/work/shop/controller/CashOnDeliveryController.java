package com.work.shop.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.work.shop.service.CashOnDeliverService;
import com.work.shop.service.ShopGoodsService;
import com.work.shop.util.StringUtil;
import com.work.shop.util.extjs.PageHelper;
import com.work.shop.util.extjs.Paging;
import com.work.shop.vo.ChannelGoodsInfoVo;
import com.work.shop.vo.ChannelGoodsTicketVo;

@Controller
@RequestMapping(value= "cashOnDelivery")
public class CashOnDeliveryController  extends BaseController {

	@Resource(name="cashOnDeliverService")
	private CashOnDeliverService cashOnDeliverService;
	
	@Resource(name = "shopGoodsService")
	private ShopGoodsService shopGoodsService;
	
	/**
	 *进入货到付款页面;  
	 ***/
	@RequestMapping(value = "cashOnDeliveryPage.spmvc")
	public ModelAndView cashOnDeliveryPage() {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("cashOnDelivery/cashOnDeliveryList");
		return mav;
	}
	
	//调整单号生成即新增调整单
	@RequestMapping(value= "addCashOnDeliveryPage.spmvc")
	public ModelAndView addCashOnDeliveryPage(){
		ModelAndView mav = new ModelAndView();
		ChannelGoodsTicketVo  addNewCgtVo = new ChannelGoodsTicketVo();
		String ticketCode = "HD"+StringUtil.getSysCode();
		addNewCgtVo.setTicketCode(ticketCode);//新增调整单的编号系统生成
		mav.addObject("addNewCgtVo",addNewCgtVo);
		mav.setViewName("cashOnDelivery/addCashOnDeliveryPage");
		return mav;
	}
	
	/**
	 * 店铺商品线上线下列表
	 * @param request
	 * @param response
	 * @param searchVo
	 * @param ticketType
	 */
	@RequestMapping(value= "cashOnDeliveryList.spmvc")
	public void cashOnDeliveryList(HttpServletRequest request,HttpServletResponse response,
			ChannelGoodsInfoVo model, PageHelper helper){ 
		Paging paging = cashOnDeliverService.getShopGoodsUpDownList(model, helper);
		try {
			writeJson(paging, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	// 点击店铺商品价格调整中查看更新按钮
	@RequestMapping(value = "updateCashOnDelivery.spmvc")
	public ModelAndView updateCashOnDelivery(@ModelAttribute("id") String id) {

		ModelAndView mav = new ModelAndView();

		ChannelGoodsTicketVo cgtVo = shopGoodsService.getChannelGoodsTicketVo(Integer.parseInt(id));
		mav.addObject("addNewCgtVo", cgtVo);
	///	mav.setViewName("barcode/add");
		mav.setViewName("cashOnDelivery/addCashOnDeliveryPage");
		return mav;
	}
}

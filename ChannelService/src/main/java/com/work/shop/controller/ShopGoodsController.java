package com.work.shop.controller;

import java.io.IOException;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.work.shop.bean.TicketInfoExample;
import com.work.shop.service.ShopGoodsService;
import com.work.shop.service.TicketInfoService;
import com.work.shop.util.StringUtil;
import com.work.shop.util.TimeUtil;
import com.work.shop.util.extjs.PageHelper;
import com.work.shop.util.extjs.Paging;
import com.work.shop.vo.ChannelGoodsInfoVo;
import com.work.shop.vo.ChannelGoodsTicketVo;
import com.work.shop.vo.JsonResult;
import com.work.shop.vo.TicketInfoVo;

@Controller
@RequestMapping(value= "shopGoods")
public class ShopGoodsController extends BaseController{
	
	@Resource(name="shopGoodsService")
	private ShopGoodsService shopGoodsService; 
	
	@Resource(name="ticketInfoService")
	private TicketInfoService ticketInfoService; //调整单商品信息
	
//	@Resource(name = "apiService")
//	private ChannelApiService apiService;
	
	private static Logger log = Logger.getLogger(ShopGoodsController.class);

	
	//树菜单页面跳转
	@RequestMapping(value= "shopGoodsUpDownPage.spmvc")
	public ModelAndView shopGoodsUpDownPage(){		
		ModelAndView mav = new ModelAndView();
		mav.setViewName("shopGoods/shopGoodsUpDown");
		return mav;
	}

	/**
	 * 跳转到商品线上线下管理页面
	 * @return
	 */
	@RequestMapping(value= "shopGoodsOnlineOfflinePage.spmvc")
	public ModelAndView shopGoodsOnlineOfflinePage(){		
		ModelAndView mav = new ModelAndView();
		mav.setViewName("shopGoods/shopGoodsOnlineOffline");
		return mav;
	}
	
	//点击生成调整单按钮页面跳转
	//调整单号生成即新增调整单
	@RequestMapping(value= "addShopGoodsUpDownPage.spmvc")
	public ModelAndView addShopGoodsUpDownPage(){
		ModelAndView mav = new ModelAndView();
		ChannelGoodsTicketVo  addNewCgtVo = new ChannelGoodsTicketVo();
		String ticketCode = "SX"+StringUtil.getSysCode();
		addNewCgtVo.setTicketCode(ticketCode);//新增调整单的编号系统生成
	//	addNewCgtVo.setShopCode("1"); //新增调整单的店铺
		mav.addObject("addNewCgtVo",addNewCgtVo);
		mav.setViewName("shopGoods/addShopGoodsUpDown");
		return mav;
	}
	
	//点击生成调整单按钮页面跳转
	//调整单号生成即新增调整单
	@RequestMapping(value= "addShopGoodsSyncStock.spmvc")
	public ModelAndView addShopGoodsSyncStock(){
		ModelAndView mav = new ModelAndView();
		ChannelGoodsTicketVo  addNewCgtVo = new ChannelGoodsTicketVo();
		String ticketCode = "SS"+StringUtil.getSysCode();
		addNewCgtVo.setTicketCode(ticketCode);//新增调整单的编号系统生成
	//	addNewCgtVo.setShopCode("1"); //新增调整单的店铺
		mav.addObject("addNewCgtVo",addNewCgtVo);
		mav.setViewName("shopGoods/addShopGoodsSyncStock");
		return mav;
	}
	
	//点击生成调整单按钮页面跳转
	//调整单号生成即新增调整单
	@RequestMapping(value= "addShopGoodsBandingStore.spmvc")
	public ModelAndView addShopGoodsBandingStore(){
		ModelAndView mav = new ModelAndView();
		ChannelGoodsTicketVo  addNewCgtVo = new ChannelGoodsTicketVo();
		String ticketCode = "SO"+StringUtil.getSysCode();
		addNewCgtVo.setTicketCode(ticketCode);//新增调整单的编号系统生成
	//	addNewCgtVo.setShopCode("1"); //新增调整单的店铺
		mav.addObject("addNewCgtVo",addNewCgtVo);
		mav.setViewName("shopGoods/addShopGoodsBandingStore");
		return mav;
	}
		
	
	/***
	 * 点击生成调整单按钮页面跳转
	 * 店铺商品线上线下生成即新增调整单
	 * @return mav;
	 */
	@RequestMapping(value= "addshopGoodsOnlineOfflinePage.spmvc")
	public ModelAndView addshopGoodsOnlineOfflinePage(){		
		ModelAndView mav = new ModelAndView();
		ChannelGoodsTicketVo  addNewCgtVo = new ChannelGoodsTicketVo();
		String ticketCode = "OO"+StringUtil.getSysCode();
		addNewCgtVo.setTicketCode(ticketCode);//新增调整单的编号系统生成
	//	addNewCgtVo.setShopCode("1"); //新增调整单的店铺
		mav.addObject("addNewCgtVo",addNewCgtVo);
		mav.setViewName("shopGoods/addshopGoodsOnlineOffline");
		return mav;
	}
	
	//店铺商品上下架管理查询列表
	//channelGoodsTicketVo 为查询条件
	//type 为调整单类型,不同的调整单页面加载相应的调整单
	@RequestMapping(value= "shopGoodsOnlineOffline.spmvc")
	public void shopGoodsOnlineOffline(HttpServletRequest request,HttpServletResponse response,
			@ModelAttribute("channelGoodsTicketVo") ChannelGoodsInfoVo searchVo,
			@ModelAttribute("ticketType") String ticketType,PageHelper helper){ 

	/*	String start = request.getParameter("start") == null ? "" : request.getParameter("start");
		String limit = request.getParameter("limit") == null ? "" : request.getParameter("limit");
		
		String shopCode  = request.getParameter("shopCode");*/
	
	/*	ChannelGoodsTicketExample channelGoodsTicketExample = new ChannelGoodsTicketExample();
		com.work.shop.bean.ChannelGoodsTicketExample.Criteria criteria = channelGoodsTicketExample.or();
		if(searchVo.getTicketCode() != null && StringUtil.isNotEmpty(searchVo.getTicketCode())){
			criteria.andTicketCodeEqualTo(searchVo.getTicketCode());		
		}
		if(searchVo.getShopCode()!= null && StringUtil.isNotEmpty(searchVo.getShopCode())){
			criteria.andShopCodeVoEqualTo(searchVo.getShopCode());		
		}
		if(searchVo.getTicketStatus() != null && StringUtil.isNotEmpty(searchVo.getTicketStatus())){
			criteria.andTicketStatusEqualTo(searchVo.getTicketStatus());		
		}
		if(searchVo.getIsTiming() != null && !"".equals(searchVo.getIsTiming()) ){
			criteria.andIsTimingEqualTo(Byte.parseByte(searchVo.getIsTiming()));//是否定时执行	
		}
		if(ticketType != null && !"".equals(ticketType)){
			criteria.andTicketTypeEqualTo(Byte.parseByte(ticketType));
		}
		
		
     	if(StringUtil.isNotBlank(shopCode)) {
			//criteria.andShopCodeEqualTo(shopCode.toString());
			criteria.andShopCodeVoEqualTo(shopCode.toString());
		
		}*/
		
		//分页字段
	//	criteria.limit(Integer.parseInt(start), Integer.parseInt(limit));
		//排序字段

		Paging paging = shopGoodsService.getShopGoodsUpDownList(searchVo,helper);
		try {
			writeJson(paging, response);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 店铺商品线上线下列表
	 * @param request
	 * @param response
	 * @param searchVo
	 * @param ticketType
	 */
	@RequestMapping(value= "shopGoodsUpDownList.spmvc")
	public void shopGoodsUpDownList(HttpServletRequest request,HttpServletResponse response,
			ChannelGoodsInfoVo model, PageHelper helper){ 
		Paging paging = shopGoodsService.getShopGoodsUpDownList(model, helper);
		try {
			writeJson(paging, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//店铺商品批量删除
	@RequestMapping(value= "deleteChannelGoodsTickets.spmvc")
	public void deleteChannelGoodsTickets(HttpServletResponse response,@ModelAttribute("ids") String ids){		
		JsonResult jsonResult = shopGoodsService.deleteChannelGoodsTickets(ids);
		outPrintJson(response, jsonResult);
	}
	
	//导出CSV文件
	//type 为updown,price,sellingInfo 类型
	@RequestMapping(value= "exportChannelGoodsTickets.spmvc")
	public void exportCsvFile(@ModelAttribute("id") String id,@ModelAttribute("type") String type ,HttpServletRequest request,HttpServletResponse response){
		ChannelGoodsTicketVo cgt = new ChannelGoodsTicketVo();
		TicketInfoExample ticketInfoExample = new TicketInfoExample();
		TicketInfoExample.Criteria criteria = ticketInfoExample.or();
		StringBuffer sb = new StringBuffer();
		if(id != null && !"".equals(id)){
			int idInt = Integer.parseInt(id);
			// 获取调整单信息
			cgt = shopGoodsService.getChannelGoodsTicketVo(idInt);
			// 获取调整单商品执行结果信息
			criteria.andTicketCodeEqualTo(cgt.getTicketCode());
			List<TicketInfoVo> list = ticketInfoService.selectTicketResult(cgt);
			String ticketType="";
			String ticketStatus="";
			String isTiming ="";
			String ticketCode = cgt.getTicketCode();
			switch(cgt.getTicketType()){
				case 0:ticketType = "修改价格";break;
				case 1:ticketType = "上下架维护";break;
				case 2:ticketType = "宝贝详情维护";break;
				case 3:ticketType = "卖点管理";break;
				case 4:ticketType = "商品名称调整";break;
				case 5:ticketType = "商品添加调整";break;
				case 6:ticketType = "商品运费承担方式调整";break;
				case 7:ticketType = "商品支持会员打折调整";break;
				case 8:ticketType = "商品线上线下调整";break;
				case 9:ticketType = "库存同步调整";break;
				case 10:ticketType = "商品销售属性管理";break;
				case 11:ticketType = "商品新品状态调整";break;
				case 15:ticketType = "商品货款类型调整";break;
				case 16:ticketType = "拍下立减库存调整";break;
				default : ticketType = ""; break;
			}
			if (StringUtil.isEmpty(ticketType)) {
				log.error("调整单类型未找到，终止执行！");
				return ;
			}
			if(cgt.getTicketStatus() != null && !"".equals(cgt.getTicketStatus())){
				byte status = Byte.parseByte(cgt.getTicketStatus());
				switch(status){
					case 0:ticketStatus = "未审核";break;
					case 1:ticketStatus = "已审核";break;
					case 2:ticketStatus = "已移除";break;
					case 3:ticketStatus = "已执行";break;
				}
			}
			if(StringUtil.isNotNull(cgt.getIsTiming())){
				byte status = Byte.parseByte(cgt.getIsTiming());
				switch(status){
					case 0:isTiming = "立即执行";break;
					case 1:isTiming = "定时执行";break;
					default:isTiming = "";break;
				}
			}
			String addtime = TimeUtil.formatDate(cgt.getAddTime());
			String excetTime = "";
			if(cgt.getExcetTime() != null){
				excetTime = TimeUtil.formatDate(cgt.getExcetTime());
			}
			String tType = cgt.getTicketType().toString();
			sb.append("调整单号,经营店铺,单据类型,单据状态,单据生成时间,执行类型,执行时间,执行结果,");
			if("1".equals(tType)){// 上下架
				sb.append("申请人,商品款号,渠道商品款号,商品名称,上下架状态,商品执行结果,商品执行时间"+"\r\n");
				if(list != null && list.size()>0){
					for(int i=0;i<list.size();i++){
						TicketInfoVo infoVo = list.get(i);
						String goodsSn = infoVo.getGoodsSn();
						String channelGoodsSn = infoVo.getChannelGoodssn();
						String goodsTitle = infoVo.getGoodsTitle();
						String updown = "";
						if (StringUtil.isNotEmpty(infoVo.getOnSellStatus())) {
							if (infoVo.getOnSellStatus().equals("0")) {
								updown = "下架";
							} else {
								updown = "上架";
							}
						}
						String execTime = TimeUtil.formatDate(infoVo.getRequestTime());
						sb.append(ticketCode+","+cgt.getShopTitle()+","+ticketType+",");
						sb.append(ticketStatus+","+addtime+","+isTiming+","+excetTime+","+cgt.getNote()+","+cgt.getOperUser()+","+goodsSn+
								","+channelGoodsSn+","+goodsTitle + "," + updown + ",\""+getMessage(infoVo.getReturnMessage())+"\","+execTime+"\r\n");
					}
				}
			} else if("0".equals(tType)){ // 价格
				sb.append("申请人,商品款号,渠道商品款号,商品名称,商品保护价格,商品新价格,商品执行结果,商品执行时间"+"\r\n");
				if(list != null && list.size()>0){
					for(int i=0;i<list.size();i++){
						TicketInfoVo infoVo = list.get(i);
						String goodsSn = infoVo.getGoodsSn();
						String channelGoodsSn = infoVo.getChannelGoodssn();
						String goodsTitle = infoVo.getGoodsTitle();
						String execTime = TimeUtil.formatDate(infoVo.getRequestTime());
						sb.append(ticketCode+","+cgt.getShopTitle()+","+ticketType+",");
						sb.append(ticketStatus+","+addtime+","+isTiming+","+excetTime+","+cgt.getNote()+","+cgt.getOperUser()+","+goodsSn+
								","+channelGoodsSn+","+goodsTitle+","+infoVo.getSafePrice()
								+","+infoVo.getNewPrice()+",\""+getMessage(infoVo.getReturnMessage())+"\","+execTime+"\r\n");
					}
				}
			} else if("3".equals(tType)){ // 卖点信息
				sb.append("申请人,商品款号,渠道商品款号,商品名称,商品卖点信息,带标签的广告词,广告词链接地址,商品执行结果,商品执行时间"+"\r\n");
				if(list != null && list.size()>0){
					for(int i=0;i<list.size();i++){
						TicketInfoVo infoVo = list.get(i);
						String goodsSn = infoVo.getGoodsSn();
						String channelGoodsSn = infoVo.getChannelGoodssn();
						String goodsTitle = infoVo.getGoodsTitle();
						String execTime = TimeUtil.formatDate(infoVo.getRequestTime());
						sb.append(ticketCode+","+cgt.getShopTitle()+","+ticketType+",");
						sb.append(ticketStatus+","+addtime+","+isTiming+","+excetTime+","+cgt.getNote()+","+cgt.getOperUser()+","+goodsSn+
								","+channelGoodsSn+","+goodsTitle+","+infoVo.getSellingInfo()+","+infoVo.getUrlWords()+","+infoVo.getUrl()+",\""+getMessage(infoVo.getReturnMessage())+"\","+execTime+"\r\n");
					}
				}
			} else if("4".equals(tType) || "2".equals(tType)
					|| "11".equals(tType) || "8".equals(tType)){ // 商品信息  宝贝详情维护 || 商品名称调整
				sb.append("申请人,商品款号,渠道商品款号,商品名称,商品执行结果,商品执行时间"+"\r\n");
				if(list != null && list.size()>0){
					for(int i=0;i<list.size();i++){
						TicketInfoVo infoVo = list.get(i);
						String goodsSn = infoVo.getGoodsSn();
						//查询日志内容,获取调整单下单个商品的执行结果
						String channelGoodsSn = infoVo.getChannelGoodssn();
						String goodsTitle = infoVo.getGoodsTitle();
						String execTime = TimeUtil.formatDate(infoVo.getRequestTime());
						sb.append(ticketCode+","+cgt.getShopTitle()+","+ticketType+",");
						sb.append(ticketStatus+","+addtime+","+isTiming+","+excetTime+","+cgt.getNote()+","+cgt.getOperUser()+","+goodsSn+
								","+channelGoodsSn+","+goodsTitle+",\""+getMessage(infoVo.getReturnMessage())+"\","+execTime+"\r\n");
					}
				}
			} else if("5".equals(tType)){ // 商品添加
				sb.append("申请人,商品款号,商品执行结果,商品执行时间"+"\r\n");
				if(list != null && list.size()>0){
					for(int i=0;i<list.size();i++){
						TicketInfoVo infoVo = list.get(i);
						String goodsSn = infoVo.getGoodsSn();
						String execTime = TimeUtil.formatDate(infoVo.getRequestTime());
//						String channelGoodsSn = infoVo.getChannelGoodssn();
//						String goodsTitle = infoVo.getGoodsTitle();
						sb.append(ticketCode+","+cgt.getShopTitle()+","+ticketType+",");
						sb.append(ticketStatus+","+addtime+","+isTiming+","+excetTime+","+cgt.getNote()+","+cgt.getOperUser()+","+goodsSn+
								",\""+getMessage(infoVo.getReturnMessage())+"\","+execTime+"\r\n");
					}
				}
			} else if("6".equals(tType)){ // 运费承担方式
				sb.append("申请人,商品款号,运费承担方式,商品执行结果,商品执行时间"+"\r\n");
				if(list != null && list.size()>0){
					for(int i=0;i<list.size();i++){
						TicketInfoVo infoVo = list.get(i);
						String goodsSn = infoVo.getGoodsSn();
						String  freightPayer = infoVo.getFreightPayer();
						String execTime = TimeUtil.formatDate(infoVo.getRequestTime());
//						String channelGoodsSn = infoVo.getChannelGoodssn();
//						String goodsTitle = infoVo.getGoodsTitle();
						sb.append(ticketCode+","+cgt.getShopTitle()+","+ticketType+",");
						sb.append(ticketStatus+","+addtime+","+isTiming+","+excetTime+","+cgt.getNote()+","+cgt.getOperUser()+","+goodsSn+","+freightPayer+
								",\""+getMessage(infoVo.getReturnMessage())+"\","+execTime+"\r\n");
					}
				}
			} else if("7".equals(tType)){ // 支持会员打折
				sb.append("申请人,商品款号,支持会员打折,商品执行结果,商品执行时间"+"\r\n");
				if(list != null && list.size()>0){
					for(int i=0;i<list.size();i++){
						TicketInfoVo infoVo = list.get(i);
						String goodsSn = infoVo.getGoodsSn();
						String hasDiscount = infoVo.getHasDiscount();
						String execTime = TimeUtil.formatDate(infoVo.getRequestTime());
						hasDiscount = "true".equals(hasDiscount) ? "Y" : "N";
//						String channelGoodsSn = infoVo.getChannelGoodssn();
//						String goodsTitle = infoVo.getGoodsTitle();
						sb.append(ticketCode+","+cgt.getShopTitle()+","+ticketType+",");
						sb.append(ticketStatus+","+addtime+","+isTiming+","+excetTime+","+cgt.getNote()+","+cgt.getOperUser()+","+goodsSn+","+hasDiscount+
								",\""+getMessage(infoVo.getReturnMessage())+"\","+execTime+"\r\n");
					}
				}
			} else if("9".equals(tType)){// 库存同步
				sb.append("申请人,商品款号,渠道商品款号,商品名称,库存同步状态,商品执行结果,商品执行时间"+"\r\n");
				if(list != null && list.size()>0){
					for(int i=0;i<list.size();i++){
						TicketInfoVo infoVo = list.get(i);
						String goodsSn = infoVo.getGoodsSn();
						String channelGoodsSn = infoVo.getChannelGoodssn();
						String goodsTitle = infoVo.getGoodsTitle();
						String syncStock = "";
						if (StringUtil.isNotEmpty(infoVo.getIsSyncStock())) {
							if (infoVo.getIsSyncStock().equals("0")) {
								syncStock = "不同步";
							} else {
								syncStock = "同步";
							}
						}
						String execTime = TimeUtil.formatDate(infoVo.getRequestTime());
						sb.append(ticketCode+","+cgt.getShopTitle()+","+ticketType+",");
						sb.append(ticketStatus+","+addtime+","+isTiming+","+excetTime+","+cgt.getNote()+","+cgt.getOperUser()+","+goodsSn+
								","+channelGoodsSn+","+goodsTitle + "," + syncStock + ",\""+getMessage(infoVo.getReturnMessage())+"\","+execTime+"\r\n");
					}
				}
			} else if("10".equals(tType)){// 商品销售属性管理
				sb.append("申请人,商品款号,渠道商品款号,商品名称,线上线下款商品状态,商品执行结果,商品执行时间"+"\r\n");
				if(list != null && list.size()>0){
					for(int i=0;i<list.size();i++){
						TicketInfoVo infoVo = list.get(i);
						String goodsSn = infoVo.getGoodsSn();
						String channelGoodsSn = infoVo.getChannelGoodssn();
						String goodsTitle = infoVo.getGoodsTitle();
						String isOnlineOffline = "";
						if (StringUtil.isNotEmpty(infoVo.getIsOnlineOffline().toString())) {
							if (infoVo.getIsOnlineOffline().toString().equals("1")) {
								isOnlineOffline = "线上款";
							} else if(infoVo.getIsOnlineOffline().toString().equals("2")) {
								isOnlineOffline = "线下款";
							}
							else if(infoVo.getIsOnlineOffline().toString().equals("3")) {
								isOnlineOffline = "线上线下款";
							}
						}
						String execTime = TimeUtil.formatDate(infoVo.getRequestTime());
						sb.append(ticketCode+","+cgt.getShopTitle()+","+ticketType+",");
						sb.append(ticketStatus+","+addtime+","+isTiming+","+excetTime+","+cgt.getNote()+","+cgt.getOperUser()+","+goodsSn+
								","+channelGoodsSn+","+goodsTitle + "," + isOnlineOffline + ",\""+getMessage(infoVo.getReturnMessage())+"\","+execTime+"\r\n");
					}
				}
			} else if("15".equals(tType)){ // 货款类型
				sb.append("申请人,商品款号,渠道商品款号,商品名称,商品货款类型,商品执行结果,商品执行时间"+"\r\n");
				if(list != null && list.size()>0){
					for(int i=0;i<list.size();i++){
						TicketInfoVo infoVo = list.get(i);
						String goodsSn = infoVo.getGoodsSn();
						String channelGoodsSn = infoVo.getChannelGoodssn();
						String goodsTitle = infoVo.getGoodsTitle();
						String paymentType = "";
						if (StringUtil.isNotEmpty(infoVo.getOnSellStatus())) {
							if (infoVo.getOnSellStatus().equals("0")) {
								paymentType = "款到发货";
							} else {
								paymentType = "货到付款";
							}
						}
						String execTime = TimeUtil.formatDate(infoVo.getRequestTime());
						sb.append(ticketCode+","+cgt.getShopTitle()+","+ticketType+",");
						sb.append(ticketStatus+","+addtime+","+isTiming+","+excetTime+","+cgt.getNote()+","+cgt.getOperUser()+","+goodsSn+
								","+channelGoodsSn+","+goodsTitle+ "," + paymentType + ",\""+getMessage(infoVo.getReturnMessage())+"\","+execTime+"\r\n");
					}
				}
			} else if("16".equals(tType)){ // 支持拍下立减库存
				sb.append("申请人,商品款号,支持拍下立减库存,商品执行结果,商品执行时间"+"\r\n");
				if(list != null && list.size()>0){
					for(int i=0;i<list.size();i++){
						TicketInfoVo infoVo = list.get(i);
						String goodsSn = infoVo.getGoodsSn();
						String subStock = infoVo.getHasDiscount();
						String execTime = TimeUtil.formatDate(infoVo.getRequestTime());
						subStock = "true".equals(subStock) ? "Y" : "N";
//						String channelGoodsSn = infoVo.getChannelGoodssn();
//						String goodsTitle = infoVo.getGoodsTitle();
						sb.append(ticketCode+","+cgt.getShopTitle()+","+ticketType+",");
						sb.append(ticketStatus+","+addtime+","+isTiming+","+excetTime+","+cgt.getNote()+","+cgt.getOperUser()+","+goodsSn+","+subStock+
								",\""+getMessage(infoVo.getReturnMessage())+"\","+execTime+"\r\n");
					}
				}
			}
		}
		try {
			exportCsvFile(request, response, sb);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
		//批量审核调整单
	    @RequestMapping(value= "reviewChannelGoodsTicket.spmvc")
	    public void reviewChannelGoodsTicket(HttpServletRequest request,HttpServletResponse response,@ModelAttribute("ids") String ids,
	    		@ModelAttribute("ticketStatus") String ticketStatus, @ModelAttribute("templateId") String templateId){
	    	String userName = getUserName(request);
	    	JsonResult jsonResult = shopGoodsService.updateChannelGoodsTicketStatus(ids, ticketStatus, userName, templateId);
	    	
	    // 调整单数据同步
	    // String[] str = ids.substring(0, ids.length() - 1).split(",");// 去除最后一个","
		// List<String> idList = new ArrayList<String>();
		// for (String id : str) {
		// idList.add(id);
		// }
		// jsonResult = apiService.ticketDisposal(idList, false);
			
	    	outPrintJson(response, jsonResult);
	    }

		//点击生成调整单按钮页面跳转
		//调整单号生成即新增调整单
		@RequestMapping(value= "addShopGoodsInfo.spmvc")
		public ModelAndView addShopGoodsInfo(){
			ModelAndView mav = new ModelAndView();
			ChannelGoodsTicketVo  addNewCgtVo = new ChannelGoodsTicketVo();
			String ticketCode = "GI"+StringUtil.getSysCode();
			addNewCgtVo.setTicketCode(ticketCode);//新增调整单的编号系统生成
		//	addNewCgtVo.setShopCode("1"); //新增调整单的店铺
			mav.addObject("addNewCgtVo",addNewCgtVo);
			mav.setViewName("shopGoods/addShopGoodsInfo");
			return mav;
		}

		private String getMessage(String message){
			return StringUtil.isNull(message) ? "" : message.replaceAll("\"", "'");
		}
}

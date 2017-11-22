package com.work.shop.controller;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.work.shop.bean.ChannelGoodsTicket;
import com.work.shop.bean.TicketInfo;
import com.work.shop.redis.RedisClient;
import com.work.shop.service.ShopGoodsService;
import com.work.shop.service.TicketInfoService;
import com.work.shop.util.ChannelGoodsReadExcelUtils;
import com.work.shop.util.DateTimeUtils;
import com.work.shop.util.StringUtil;
import com.work.shop.util.TicketInfoReadExcelUtils;
import com.work.shop.util.extjs.Paging;
import com.work.shop.vo.ChannelGoodsInfoVo;
import com.work.shop.vo.ChannelGoodsTicketVo;
import com.work.shop.vo.JsonResult;

@Controller
@RequestMapping(value= "ticketInfo")
public class TicketInfoController extends BaseController {
	
	@Resource(name="ticketInfoService")
	private TicketInfoService ticketInfoService; //调整单商品信息
     
	@Resource(name="shopGoodsService")
	private ShopGoodsService shopGoodsService; 
	
	@Resource
	private TicketInfoReadExcelUtils ticketInfoReadExcelUtils ;
	
	@Resource(name = "redisClient")
	private RedisClient redisClient;
	
	//点击店铺调整中查看更新按钮
	@RequestMapping(value= "updateOrSearchTicketInfo.spmvc")
	public ModelAndView addShopGoodsUpDownPage(@ModelAttribute("id") String id){	
		
		ModelAndView mav = new ModelAndView();
		
		ChannelGoodsTicketVo  cgtVo = shopGoodsService.getChannelGoodsTicketVo(Integer.parseInt(id));
		mav.addObject("addNewCgtVo",cgtVo);
		mav.setViewName("shopGoods/addShopGoodsUpDown");
		return mav;
	}
	
	//点击店铺调整中查看更新按钮
		@RequestMapping(value= "updateOrSearchSyncStockTicketInfo.spmvc")
		public ModelAndView updateOrSearchSyncStockTicketInfo(@ModelAttribute("id") String id){	
			
			ModelAndView mav = new ModelAndView();
			
			ChannelGoodsTicketVo  cgtVo = shopGoodsService.getChannelGoodsTicketVo(Integer.parseInt(id));
			mav.addObject("addNewCgtVo",cgtVo);
			mav.setViewName("shopGoods/addShopGoodsSyncStock");
			return mav;
		}
		
	//点击店铺调整中查看更新按钮
		@RequestMapping(value= "updateOrSearchGoodsStoreBandingTicketInfo.spmvc")
		public ModelAndView updateOrSearchGoodsStoreBandingTicketInfo(@ModelAttribute("id") String id){	
			
			ModelAndView mav = new ModelAndView();
			
			ChannelGoodsTicketVo  cgtVo = shopGoodsService.getChannelGoodsTicketVo(Integer.parseInt(id));
			mav.addObject("addNewCgtVo",cgtVo);
			mav.setViewName("shopGoods/addShopGoodsBandingStore");
			return mav;
		}
	
	
	//点击店铺调整中查看更新按钮
		@RequestMapping(value= "updateOrSearchOnlineOfflineTicketInfo.spmvc")
		public ModelAndView updateOrSearchOnlineOfflineTicketInfo(@ModelAttribute("id") String id){	
			
			ModelAndView mav = new ModelAndView();

			ChannelGoodsTicketVo  cgtVo = shopGoodsService.getChannelGoodsTicketVo(Integer.parseInt(id));
			mav.addObject("addNewCgtVo",cgtVo);
			mav.setViewName("shopGoods/addshopGoodsOnlineOffline");
			return mav;
		}

	//调整单商品信息查询
	@RequestMapping(value= "getTicketInfoList.spmvc")
	public ModelAndView getTicketInfoList(HttpServletRequest request,HttpServletResponse response,
			@ModelAttribute("ticketCode") String ticketCode){
   
		
		String start = request.getParameter("start") == null ? "0" : request.getParameter("start");
		String limit = request.getParameter("limit") == null ? "15" : request.getParameter("limit");
  
//		TicketInfoExample ticketInfoExample = new TicketInfoExample();
//		Criteria criteria = ticketInfoExample.or();
		Map<String,Object> params = new HashMap<String, Object>();
		if(!"".equals(ticketCode)){
//			criteria.andTicketCodeEqualTo(ticketCode);
		   params.put("ticketCode", ticketCode);
		}
		//分页字段
		if(!"".equals(start)&& !"".equals(limit)){
//			criteria.limit(Integer.parseInt(start), Integer.parseInt(limit));
			params.put("start", start);
			params.put("limit", limit);
		}
		Paging paging = ticketInfoService.getTicketInfoVoList(params);
		try {
			writeJson(paging, response);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	//调整单商品删除
	@RequestMapping(value= "deleteTicketInfo.spmvc")
	public void deleteTicketInfo(HttpServletResponse response,@ModelAttribute("ids") String ids){
		JsonResult jsonResult = ticketInfoService.deleteTicketInfo(ids);
		outPrintJson(response, jsonResult);
	}
	
	 //生成更新调整单和调整单商品表(上下架，修改价格，卖点信息统一使用，保存调整单)
	@RequestMapping(value= "addChannelGoodsAndTickets.spmvc")
	public void addChannelGoodsAndTickets(HttpServletRequest request,HttpServletResponse response,
			@ModelAttribute("channelGoodsTicket") ChannelGoodsTicket channelGoodsTicket,@ModelAttribute("excutTimeStr") String excutTimeStr){
		JsonResult jsonResult = new JsonResult();
		
		List<ChannelGoodsTicket> listCg = new ArrayList<ChannelGoodsTicket>();
		if(StringUtil.isNotNull(excutTimeStr)){
			channelGoodsTicket.setExecTime(DateTimeUtils.parseStr(excutTimeStr)); //执行时间
		}
		
		channelGoodsTicket.setOperUser(getUserName(request));//操作用户不用更新
		channelGoodsTicket.setIsSync((byte)0);
		channelGoodsTicket.setNote("");
		
		if(channelGoodsTicket.getIsTiming()!=null && channelGoodsTicket.getIsTiming()== 0){
			channelGoodsTicket.setExecTime(null);
		}
		listCg.add(channelGoodsTicket);
		boolean s =shopGoodsService.insertChannelGoodsTicket(listCg); //调整单表新生成一条记录
		
//		TicketInfoExample ticketInfoExample = new TicketInfoExample();
//		Criteria criteria = ticketInfoExample.or();
//		if(channelGoodsTicket.getTicketCode() != null && !"".equals(channelGoodsTicket.getTicketCode())){
//			criteria.andTicketCodeEqualTo(channelGoodsTicket.getTicketCode());//根据调整单号查询调整单下的商品列表
//		}
//		List<TicketInfo> list = ticketInfoService.getTicketInfoList(ticketInfoExample);
//		if(list != null && list.size()>0){
//			
//		}
		if(s){
			jsonResult.setIsok(true);				
		}else{
			jsonResult.setIsok(false);
		}
		outPrintJson(response, jsonResult);
	}
	
	 //批量商品导入--读取excel.xls  调整单生成
    @RequestMapping(value= "inportChannelGoods.spmvc", headers = "content-type=multipart/*")
    public void inportChannelGoods(@RequestParam MultipartFile myfile,@ModelAttribute("ticketCode") String ticketCode,HttpServletRequest request,HttpServletResponse response,
    		@ModelAttribute("shopCode") String shopCode ,@ModelAttribute("excetTime") String excetTime,
    		@ModelAttribute("isTiming") String isTiming,@ModelAttribute("channelCode") String channelCode){
    	Paging paging  = new Paging();
    	StringBuffer sb = new StringBuffer("");
    	ChannelGoodsTicket channelGoodsTicket = new ChannelGoodsTicket();
    	InputStream is = null;
    	try {
    		//将绑定的调整单号ticketCode传入解析方法
			is = myfile.getInputStream();
			// List<TicketInfo> list =
			// ticketInfoReadExcelUtils.readXls(is,ticketCode,shopCode,sb);
			List<TicketInfo> list = ticketInfoReadExcelUtils.readCsv(is,
					ticketCode, shopCode, sb);
			if(list != null){
//				if(list.size() >1){ //上传记录大于1  做比对记录
//					for(int i=0;i<list.size()-1;i++){
//						for(int j=i+1;j<list.size();j++){
//							if(list.get(i).getGoodsSn().equals(list.get(j).getGoodsSn())){
//								sb.append("["+list.get(i).getGoodsSn()+"],");
//								break;
//							}
//						} 
//					}
//				}
				/**********调整单中信息正常******************/
				if("".equals(sb.toString())){ 
					ticketInfoService.addTicketInfos(list);//保存调整单商品信息
					List<ChannelGoodsTicket> listCg = new ArrayList<ChannelGoodsTicket>();
					String addTime = DateTimeUtils.format(new Date(), DateTimeUtils.dateTimeString);
					Date date = DateTimeUtils.parseStr(addTime);
					channelGoodsTicket.setChannelCode(channelCode);//渠道code
					channelGoodsTicket.setShopCode(shopCode); //店铺code
					channelGoodsTicket.setAddTime(date); //生成调整单的时间
					channelGoodsTicket.setOperUser(getUserName(request));//操作用户
					channelGoodsTicket.setTicketCode(ticketCode); //调整单号
					if(StringUtil.isNotNull(isTiming)){
						channelGoodsTicket.setIsTiming(Byte.parseByte(isTiming)); //是否定时执行
					}
					
					if(StringUtil.isNotNull(excetTime)){
						excetTime = URLDecoder.decode(excetTime, "utf-8");
						Date exceTim = DateTimeUtils.parseStr(excetTime,DateTimeUtils.dateTimeString);
						channelGoodsTicket.setExecTime(exceTim); //执行时间
					}
					channelGoodsTicket.setTicketStatus("0"); //未审核状态
					channelGoodsTicket.setTicketType((byte)1);// 1 : 上下架类型
					
					channelGoodsTicket.setIsSync((byte)0);
					channelGoodsTicket.setNote("");
					
					listCg.add(channelGoodsTicket);
					shopGoodsService.insertChannelGoodsTicket(listCg); //调整单表新生成一条记录
					paging.setRoot(list);
					paging.setMessage("生成调整单成功！");
					paging.setTotalProperty(list.size());
					writeObject(paging, response);
					/**********调整单中信息异常有重复数据******************/
				}else{
					
					paging.setMessage(sb.toString()+"请检查模版中记录");
					paging.setTotalProperty(0);
					writeObject(paging, response);
					
				}

			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
		  if(is != null){
			  try {
				is.close();
				is=null;
			} catch (IOException e) {
				e.printStackTrace();
			}
		  }
		}
    	
    }
	
	// 点击批量添加商品按钮
	@RequestMapping(value = "uploadTicketFile.spmvc")
	public void uploadTicketFile(@RequestParam MultipartFile myfile, HttpServletRequest request, 
			HttpServletResponse response, ChannelGoodsTicketVo ticketVo) {
		JsonResult jsonResult = new JsonResult();
		if (null == ticketVo) {
			jsonResult.setIsok(false);
			jsonResult.setMessage("调整单信息不全！");
			outPrintJson(response, jsonResult);
			return;
		}
		if (StringUtil.isNull(ticketVo.getChannelCode()) || StringUtil.isNull(ticketVo.getShopCode())) {
			jsonResult.setIsok(false);
			jsonResult.setMessage("调整单信息不全！");
			outPrintJson(response, jsonResult);
			return;
		}
		ticketVo.setUserName(getUserName(request));
		InputStream is = null;
		try {
			is = myfile.getInputStream();
			StringBuffer sb = new StringBuffer();
			List<ChannelGoodsInfoVo> goodsList = ChannelGoodsReadExcelUtils.readTicketFile(redisClient,ticketVo, is, sb);
			if (StringUtil.isNotEmpty(sb.toString())) {
				jsonResult.setIsok(false);
				jsonResult.setMessage("商品编码(" + sb.toString() + ")存在重复数据，请检查文件!");
			} else {
				jsonResult = ticketInfoService.addChannelTicket(ticketVo, goodsList);
				jsonResult.setMessage("文件上传成功!");
			}
		} catch (Exception e) {
			jsonResult.setIsok(false);
			jsonResult.setMessage("数据格式不正确，请检查文件!");
			e.printStackTrace();
		} finally {
			if (is != null) {
				try {
					is.close();
					is = null;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		outPrintJson(response, jsonResult);
	}

	
	//批量商品导入--读取excel.xls  调整单生成
    @RequestMapping(value= "inportChannelGoodsSyncStock.spmvc", headers = "content-type=multipart/*")
    public void inportChannelGoodsSyncStock(@RequestParam MultipartFile myfile,@ModelAttribute("ticketCode") String ticketCode,HttpServletRequest request,HttpServletResponse response,
    		@ModelAttribute("shopCode") String shopCode ,@ModelAttribute("excetTime") String excetTime,
    		@ModelAttribute("isTiming") String isTiming,@ModelAttribute("channelCode") String channelCode){
    	Paging paging  = new Paging();
    	StringBuffer sb = new StringBuffer("");
    	ChannelGoodsTicket channelGoodsTicket = new ChannelGoodsTicket();
    	InputStream is = null;
    	try {
    		//将绑定的调整单号ticketCode传入解析方法
			is = myfile.getInputStream();
			List<TicketInfo> list = ticketInfoReadExcelUtils.readSyncStockCsv(is,
					ticketCode, shopCode, sb);
			if(list != null){
				/**********调整单中信息正常******************/
				if("".equals(sb.toString())){ 
					ticketInfoService.addTicketInfos(list);//保存调整单商品信息
					List<ChannelGoodsTicket> listCg = new ArrayList<ChannelGoodsTicket>();
					String addTime = DateTimeUtils.format(new Date(), DateTimeUtils.dateTimeString);
					Date date = DateTimeUtils.parseStr(addTime);
					channelGoodsTicket.setChannelCode(channelCode);//渠道code
					channelGoodsTicket.setShopCode(shopCode); //店铺code
					channelGoodsTicket.setAddTime(date); //生成调整单的时间
					channelGoodsTicket.setOperUser(getUserName(request));//操作用户
					channelGoodsTicket.setTicketCode(ticketCode); //调整单号
					if(StringUtil.isNotNull(isTiming)){
						channelGoodsTicket.setIsTiming(Byte.parseByte(isTiming)); //是否定时执行
					}
					
					if(StringUtil.isNotNull(excetTime)){
						excetTime = URLDecoder.decode(excetTime, "utf-8");
						Date exceTim = DateTimeUtils.parseStr(excetTime,DateTimeUtils.dateTimeString);
						channelGoodsTicket.setExecTime(exceTim); //执行时间
					}
					channelGoodsTicket.setTicketStatus("0"); //未审核状态
					channelGoodsTicket.setTicketType((byte)9);// 9 : 库存同步调整
					
					channelGoodsTicket.setIsSync((byte)0);
					channelGoodsTicket.setNote("");
					
					listCg.add(channelGoodsTicket);
					shopGoodsService.insertChannelGoodsTicket(listCg); //调整单表新生成一条记录
					paging.setRoot(list);
					paging.setMessage("生成调整单成功！");
					paging.setTotalProperty(list.size());
					writeObject(paging, response);
					/**********调整单中信息异常有重复数据******************/
				}else{
					
					paging.setMessage(sb.toString()+"请检查模版中记录");
					paging.setTotalProperty(0);
					writeObject(paging, response);
					
				}

			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
		  if(is != null){
			  try {
				is.close();
				is=null;
			} catch (IOException e) {
				e.printStackTrace();
			}
		  }
		}
    	
    }
    
  //批量商品导入--读取excel.xls  调整单生成
    @RequestMapping(value= "inportChannelGoodsStoreBanding.spmvc", headers = "content-type=multipart/*")
    public void inportChannelGoodsStoreBanding(@RequestParam MultipartFile myfile,@ModelAttribute("ticketCode") String ticketCode,HttpServletRequest request,HttpServletResponse response,
    		@ModelAttribute("shopCode") String shopCode ,@ModelAttribute("excetTime") String excetTime,
    		@ModelAttribute("isTiming") String isTiming,@ModelAttribute("channelCode") String channelCode){
    	Paging paging  = new Paging();
    	StringBuffer sb = new StringBuffer("");
    	ChannelGoodsTicket channelGoodsTicket = new ChannelGoodsTicket();
    	InputStream is = null;
    	try {
    		//将绑定的调整单号ticketCode传入解析方法
			is = myfile.getInputStream();
			List<TicketInfo> list = ticketInfoReadExcelUtils.readGoodsStoreBandingCsv(is,
					ticketCode, shopCode, sb);
			if(list != null){
				/**********调整单中信息正常******************/
				if("".equals(sb.toString())){ 
					ticketInfoService.addTicketInfos(list);//保存调整单商品信息
					List<ChannelGoodsTicket> listCg = new ArrayList<ChannelGoodsTicket>();
					String addTime = DateTimeUtils.format(new Date(), DateTimeUtils.dateTimeString);
					Date date = DateTimeUtils.parseStr(addTime);
					channelGoodsTicket.setChannelCode(channelCode);//渠道code
					channelGoodsTicket.setShopCode(shopCode); //店铺code
					channelGoodsTicket.setAddTime(date); //生成调整单的时间
					channelGoodsTicket.setOperUser(getUserName(request));//操作用户
					channelGoodsTicket.setTicketCode(ticketCode); //调整单号
					if(StringUtil.isNotNull(isTiming)){
						channelGoodsTicket.setIsTiming(Byte.parseByte(isTiming)); //是否定时执行
					}
					
					if(StringUtil.isNotNull(excetTime)){
						excetTime = URLDecoder.decode(excetTime, "utf-8");
						Date exceTim = DateTimeUtils.parseStr(excetTime,DateTimeUtils.dateTimeString);
						channelGoodsTicket.setExecTime(exceTim); //执行时间
					}
					channelGoodsTicket.setTicketStatus("0"); //未审核状态
					channelGoodsTicket.setTicketType((byte)10);// 10 : 商品的销售属性（线上款，线下款，线上线下同款）变更
					
					channelGoodsTicket.setIsSync((byte)0);
					channelGoodsTicket.setNote("");
					
					listCg.add(channelGoodsTicket);
					shopGoodsService.insertChannelGoodsTicket(listCg); //调整单表新生成一条记录
					paging.setRoot(list);
					paging.setMessage("生成调整单成功！");
					paging.setTotalProperty(list.size());
					writeObject(paging, response);
					/**********调整单中信息异常有重复数据******************/
				}else{
					
					paging.setMessage(sb.toString()+"请检查模版中记录");
					paging.setTotalProperty(0);
					writeObject(paging, response);
					
				}

			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
		  if(is != null){
			  try {
				is.close();
				is=null;
			} catch (IOException e) {
				e.printStackTrace();
			}
		  }
		}
    	
    }
	
	 //批量商品导入--读取excel.xls  调整单生成
    @RequestMapping(value= "inportOnlineOfflineChannelGoods.spmvc", headers = "content-type=multipart/*")
    public void inportOnlineOfflineChannelGoods(@RequestParam MultipartFile myfile,@ModelAttribute("ticketCode") String ticketCode,HttpServletRequest request,HttpServletResponse response,
    		@ModelAttribute("shopCode") String shopCode ,@ModelAttribute("excetTime") String excetTime,
    		@ModelAttribute("isTiming") String isTiming,@ModelAttribute("channelCode") String channelCode){
    	Paging paging  = new Paging();
    	StringBuffer sb = new StringBuffer("");
    	ChannelGoodsTicket channelGoodsTicket = new ChannelGoodsTicket();
    	InputStream is = null;
    	try {
    		//将绑定的调整单号ticketCode传入解析方法
			is = myfile.getInputStream();
			// List<TicketInfo> list =
			// ticketInfoReadExcelUtils.readXls(is,ticketCode,shopCode,sb);
			List<TicketInfo> list = ticketInfoReadExcelUtils.readOnlineOfflineCsv(is,
					ticketCode, shopCode, sb);
			if(list != null){
//				if(list.size() >1){ //上传记录大于1  做比对记录
//					for(int i=0;i<list.size()-1;i++){
//						for(int j=i+1;j<list.size();j++){
//							if(list.get(i).getGoodsSn().equals(list.get(j).getGoodsSn())){
//								sb.append("["+list.get(i).getGoodsSn()+"],");
//								break;
//							}
//						} 
//					}
//				}
				/**********调整单中信息正常******************/
				if("".equals(sb.toString())){ 
					ticketInfoService.addTicketInfos(list);//保存调整单商品信息
					List<ChannelGoodsTicket> listCg = new ArrayList<ChannelGoodsTicket>();
					String addTime = DateTimeUtils.format(new Date(), DateTimeUtils.dateTimeString);
					Date date = DateTimeUtils.parseStr(addTime);
					channelGoodsTicket.setChannelCode(channelCode);//渠道code
					channelGoodsTicket.setShopCode(shopCode); //店铺code
					channelGoodsTicket.setAddTime(date); //生成调整单的时间
					channelGoodsTicket.setOperUser(getUserName(request));//操作用户
					channelGoodsTicket.setTicketCode(ticketCode); //调整单号
					if(StringUtil.isNotNull(isTiming)){
						channelGoodsTicket.setIsTiming(Byte.parseByte(isTiming)); //是否定时执行
					}
					
					if(StringUtil.isNotNull(excetTime)){
						excetTime = URLDecoder.decode(excetTime, "utf-8");
						Date exceTim = DateTimeUtils.parseStr(excetTime,DateTimeUtils.dateTimeString);
						channelGoodsTicket.setExecTime(exceTim); //执行时间
					}
					channelGoodsTicket.setTicketStatus("0"); //未审核状态
					channelGoodsTicket.setTicketType((byte)8);// 1 : 上下架类型
					
					channelGoodsTicket.setIsSync((byte)0);
					channelGoodsTicket.setNote("");
					
					listCg.add(channelGoodsTicket);
					shopGoodsService.insertChannelGoodsTicket(listCg); //调整单表新生成一条记录
					paging.setRoot(list);
					paging.setMessage("生成调整单成功！");
					paging.setTotalProperty(list.size());
					writeObject(paging, response);
					/**********调整单中信息异常有重复数据******************/
				}else{
					
					paging.setMessage(sb.toString()+"请检查模版中记录");
					paging.setTotalProperty(0);
					writeObject(paging, response);
					
				}

			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
		  if(is != null){
			  try {
				is.close();
				is=null;
			} catch (IOException e) {
				e.printStackTrace();
			}
		  }
		}
    	
    }
	

	
	
	// 点击生成调整单按钮页面跳转
	// 调整单号生成即新增调整单
	@RequestMapping(value = "createTicket.spmvc")
	public ModelAndView addShopGoodsInfo(Integer id, String ticketType) {
		ModelAndView mav = new ModelAndView();
		ChannelGoodsTicketVo addNewCgtVo = new ChannelGoodsTicketVo();
		String viewPath = "";
		
		StringBuffer ticketCode = new StringBuffer();
		if (StringUtil.isNotEmpty(ticketType)) {
			if ("0".equals(ticketType)) {
				// 修改价格调整单
				ticketCode.append("GP");
				viewPath = "shopGoodsPrice/addShopGoodsPrice";
			} else if ("1".equals(ticketType)) {
				// 上下架维护调整单
				ticketCode.append("UD");
				viewPath = "shopGoods/addShopGoodsUpDown";
			} else if ("2".equals(ticketType)) {
				// pc商品描述维护调整单
				ticketCode.append("GI");
				viewPath = "shopGoods/addShopGoodsInfo";
				addNewCgtVo.setTicketType((byte)2);
			} else if ("3".equals(ticketType)) {
				// 商品卖点调整单
				ticketCode.append("SP");
				viewPath = "shopGoodsSellingInfo/addShopGoodsSellingInfo";
			} else if ("4".equals(ticketType)) {
				// 商品名称维护调整单
				ticketCode.append("GT");
				viewPath = "shopGoods/addShopGoodsTitle";
			}
		/*	else if ("12".equals(ticketType)) {
				//手机商品名称维护调整单
				ticketCode.append("GI");
				viewPath = "shopGoods/addShopGoodsInfo";
				addNewCgtVo.setTicketType((byte)2);
			}*/
		}
		if (null != id) {
			addNewCgtVo = shopGoodsService.getChannelGoodsTicketVo(id);
		} else {
			ticketCode.append(StringUtil.getSysCode());
			addNewCgtVo.setTicketCode(ticketCode.toString());// 新增调整单的编号系统生成
		}
		mav.addObject("addNewCgtVo", addNewCgtVo);
		mav.setViewName(viewPath);
		return mav;
	}
	
	 //批量商品导入--读取excel.xls  调整单生成
    @RequestMapping(value= "inportCashOnDeliveryChannelGoods.spmvc", headers = "content-type=multipart/*")
    public void inportCashOnDeliveryChannelGoods(@RequestParam MultipartFile myfile,@ModelAttribute("ticketCode") String ticketCode,HttpServletRequest request,HttpServletResponse response,
    		@ModelAttribute("shopCode") String shopCode ,@ModelAttribute("excetTime") String excetTime,
    		@ModelAttribute("isTiming") String isTiming,@ModelAttribute("channelCode") String channelCode){
    	Paging paging  = new Paging();
    	StringBuffer sb = new StringBuffer("");
    	ChannelGoodsTicket channelGoodsTicket = new ChannelGoodsTicket();
    	InputStream is = null;
    	try {
    		//将绑定的调整单号ticketCode传入解析方法
			is = myfile.getInputStream();
			// List<TicketInfo> list =
			// ticketInfoReadExcelUtils.readXls(is,ticketCode,shopCode,sb);
			List<TicketInfo> list = ticketInfoReadExcelUtils.readcODCsv(is,
					ticketCode, shopCode, sb);
			if(list != null){
//				if(list.size() >1){ //上传记录大于1  做比对记录
//					for(int i=0;i<list.size()-1;i++){
//						for(int j=i+1;j<list.size();j++){
//							if(list.get(i).getGoodsSn().equals(list.get(j).getGoodsSn())){
//								sb.append("["+list.get(i).getGoodsSn()+"],");
//								break;
//							}
//						} 
//					}
//				}
				/**********调整单中信息正常******************/
				if("".equals(sb.toString())){ 
					ticketInfoService.addTicketInfos(list);//保存调整单商品信息
					List<ChannelGoodsTicket> listCg = new ArrayList<ChannelGoodsTicket>();
					String addTime = DateTimeUtils.format(new Date(), DateTimeUtils.dateTimeString);
					Date date = DateTimeUtils.parseStr(addTime);
					channelGoodsTicket.setChannelCode(channelCode);//渠道code
					channelGoodsTicket.setShopCode(shopCode); //店铺code
					channelGoodsTicket.setAddTime(date); //生成调整单的时间
					channelGoodsTicket.setOperUser(getUserName(request));//操作用户
					channelGoodsTicket.setTicketCode(ticketCode); //调整单号
					if(StringUtil.isNotNull(isTiming)){
						channelGoodsTicket.setIsTiming(Byte.parseByte(isTiming)); //是否定时执行
					}
					
					if(StringUtil.isNotNull(excetTime)){
						excetTime = URLDecoder.decode(excetTime, "utf-8");
						Date exceTim = DateTimeUtils.parseStr(excetTime,DateTimeUtils.dateTimeString);
						channelGoodsTicket.setExecTime(exceTim); //执行时间
					}
					channelGoodsTicket.setTicketStatus("0"); //未审核状态
					channelGoodsTicket.setTicketType((byte)15);// 15 : 货到付款
					
					channelGoodsTicket.setIsSync((byte)0);
					channelGoodsTicket.setNote("");
					
					listCg.add(channelGoodsTicket);
					shopGoodsService.insertChannelGoodsTicket(listCg); //调整单表新生成一条记录
					paging.setRoot(list);
					paging.setMessage("生成调整单成功！");
					paging.setTotalProperty(list.size());
					writeObject(paging, response);
					/**********调整单中信息异常有重复数据******************/
				}else{
					
					paging.setMessage(sb.toString()+"请检查模版中记录");
					paging.setTotalProperty(0);
					writeObject(paging, response);
					
				}

			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
		  if(is != null){
			  try {
				is.close();
				is=null;
			} catch (IOException e) {
				e.printStackTrace();
			}
		  }
		}
    	
    }

}

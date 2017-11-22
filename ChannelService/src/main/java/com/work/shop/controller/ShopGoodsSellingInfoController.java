package com.work.shop.controller;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
import com.work.shop.service.ShopGoodsService;
import com.work.shop.service.TicketInfoService;
import com.work.shop.util.DateTimeUtils;
import com.work.shop.util.StringUtil;
import com.work.shop.util.TicketInfoSellingInfoReadExcelUtils;
import com.work.shop.util.extjs.Paging;
import com.work.shop.vo.ChannelGoodsTicketVo;

@Controller
@RequestMapping(value= "shopGoodsSellingInfo")
public class ShopGoodsSellingInfoController extends BaseController {
	
	@Resource(name="shopGoodsService")
	private ShopGoodsService shopGoodsService; 
	
	@Resource(name="ticketInfoService")
	private TicketInfoService ticketInfoService; //调整单商品信息
	
	@Resource
	private TicketInfoSellingInfoReadExcelUtils ticketInfoSellingInfoReadExcelUtils;
	//树状菜单导航页面加载
	@RequestMapping(value="shopGoodsSellingInfoList.spmvc")
	public ModelAndView  shopGoodsPriceList(HttpServletResponse response,HttpServletRequest request,
			@ModelAttribute("channelGoodsTicketVo") ChannelGoodsTicketVo searchVo){
		
		String method = request.getParameter("method") == null ? "" : request.getParameter("method");
		ModelAndView mav = new ModelAndView();
		if (!StringUtil.isTrimEmpty(method) && method.equals("init")) {
		mav.setViewName("shopGoodsSellingInfo/shopGoodsSellingInfo");
		return mav;
		}
		return null;
	}
	
	
	//调整单号生成即新增调整单
	@RequestMapping(value= "addShopGoodsPricePage.spmvc")
	public ModelAndView addShopGoodsUpDownPage(){	
		ModelAndView mav = new ModelAndView();
		ChannelGoodsTicketVo  addNewCgtVo = new ChannelGoodsTicketVo();
		String ticketCode = "MD"+StringUtil.getSysCode();
		addNewCgtVo.setTicketCode(ticketCode);//新增调整单的编号系统生成
//		addNewCgtVo.setShopCode("1"); //新增调整单的店铺
		mav.addObject("addNewCgtVo",addNewCgtVo);
		mav.setViewName("shopGoodsSellingInfo/addShopGoodsSellingInfo");
		return mav;
	}
	
	
	//点击店铺商品卖点信息调整中查看更新按钮
	@RequestMapping(value= "updateOrSearchTicketInfo.spmvc")
	public ModelAndView addShopGoodsSellingInfoPage(@ModelAttribute("id") String id){	
		
		ModelAndView mav = new ModelAndView();
		
		ChannelGoodsTicketVo  cgtVo = shopGoodsService.getChannelGoodsTicketVo(Integer.parseInt(id));
		mav.addObject("addNewCgtVo",cgtVo);
		mav.setViewName("shopGoodsSellingInfo/addShopGoodsSellingInfo");
		return mav;
	}
	
	 //批量商品卖点导入--读取excel.xls
    @RequestMapping(value= "inportGoodsSellingInfo.spmvc",headers = "content-type=multipart/*")
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
    		List<TicketInfo> list = ticketInfoSellingInfoReadExcelUtils.readCsv(is,ticketCode,shopCode, channelCode, sb);
//			List<TicketInfo> list = ticketInfoSellingInfoReadExcelUtils.readXls(is,ticketCode,shopCode,sb);
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
					channelGoodsTicket.setTicketType((byte)3);// 3 : 卖点信息
					
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

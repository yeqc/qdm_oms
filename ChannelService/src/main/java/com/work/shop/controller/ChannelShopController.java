package com.work.shop.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.work.shop.bean.ChannelShop;
import com.work.shop.bean.ChannelShopExample;
import com.work.shop.bean.ChannelShopExample.Criteria;
import com.work.shop.bean.OmsChannelInfo;
import com.work.shop.bean.SystemResource;
import com.work.shop.bean.SystemResourceExample;
import com.work.shop.dao.SystemResourceMapper;
import com.work.shop.service.ChannelShopService;
import com.work.shop.service.OpenShopChannelInfoService;
import com.work.shop.service.ShopService;
import com.work.shop.united.client.dataobject.DataPermission;
import com.work.shop.united.client.facade.AuthCenterFacade;
import com.work.shop.united.client.filter.config.Config;
import com.work.shop.util.Constants;
import com.work.shop.util.DateTimeUtils;
import com.work.shop.util.PromotionsReadExcelUtils;
import com.work.shop.util.StringUtil;
import com.work.shop.util.extjs.Paging;
import com.work.shop.vo.ChannelShopVo;
import com.work.shop.vo.ChannelShopVoo;
import com.work.shop.vo.JsonResult;

@Controller
@RequestMapping(value = "channelShop")
public class ChannelShopController extends BaseController {
	
	private static Logger logger = Logger.getLogger(ChannelShopController.class);

	@Resource(name = "shopService")
	private ShopService shopService;

	@Resource(name = "systemResourceMapper")
	private SystemResourceMapper systemResourceMapper;

	@Resource(name = "channelShopService")
	private ChannelShopService channelShopService;
	
	@Resource(name = "openShopChannelInfoService")
	private OpenShopChannelInfoService openShopChannelInfoService;
	
	@Resource(name = "promotionsReadExcelUtils")
	private PromotionsReadExcelUtils promotionsReadExcelUtils; 

	@RequestMapping(value = "outChannelShopList.spmvc")
	public ModelAndView ChannelShopListPage() {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("channelShop/outChannelShopList");
		return mav;
	}

	@RequestMapping(value = "addOutChannelShop.spmvc")
	public ModelAndView toAddChannelShopPage() {
		ModelAndView mav = new ModelAndView();
		ChannelShop channelShop = new ChannelShop();
		// String shopCode = "DP"+ DateTimeUtils.format(new Date(),
		// "yyyy_MM_dd_HH_mm_ss").replace("_", "")+StringUtil.getSysCode();
		// channelShop.setShopCode(shopCode);
		mav.addObject(channelShop);
		mav.setViewName("channelShop/addOutChannelShop");
		return mav;
	}

	@RequestMapping(value = "/getExternalChannelShopList.spmvc")
	public void getExternalChannelShopList(HttpServletRequest request, HttpServletResponse response, @ModelAttribute("channelShopVo") ChannelShopVo channelShopVo,
			@ModelAttribute("shopChannel") String shopChannel) throws IOException {

		String start = request.getParameter("start") == null ? "" : request.getParameter("start");
		String limit = request.getParameter("limit") == null ? "" : request.getParameter("limit");

		ChannelShopExample channelShopExample = new ChannelShopExample();
		Criteria criteria = channelShopExample.createCriteria();
		criteria.limit(Integer.parseInt(start), Integer.parseInt(limit));
		if (null != channelShopVo.getShopTitle() && !"".equals(channelShopVo.getShopTitle())) {
			criteria.andShopTitleLike("%" + channelShopVo.getShopTitle() + "%");
		}

		if (null != channelShopVo.getShopCode() && !"".equals(channelShopVo.getShopCode())) {

			criteria.andShopCodeEqualTo(channelShopVo.getShopCode()); // 店铺编号
		}
		if (null != channelShopVo.getParentShopCode() && !"".equals(channelShopVo.getParentShopCode())) {

			criteria.andParentShopCodeEqualTo(channelShopVo.getParentShopCode());// 父店铺编号

		}
		if (null != channelShopVo.getChannelCode() && !"".equals(channelShopVo.getChannelCode())) {

			criteria.andChannelCodeEqualTo(channelShopVo.getChannelCode());
		}
		if (null != shopChannel && !"".equals(shopChannel)) {

			criteria.andShopChannelEqualTo(Byte.parseByte(shopChannel)); // 0为集团店铺，1为渠道店铺
		}

		if (null != channelShopVo.getMarketTitle() && !"".equals(channelShopVo.getMarketTitle())) {

			criteria.andMarketTitleLike(channelShopVo.getMarketTitle()); // 市场名称
		}

		if (null != channelShopVo.getShopType() && !"".equals(channelShopVo.getShopType())) {

			criteria.andShopTypeEqualTo(channelShopVo.getShopType()); // 店铺类型
		}

		if (null != channelShopVo.getShopStatus() && !"".equals(channelShopVo.getShopStatus())) {
			criteria.andShopStatusEqualTo(Byte.valueOf(channelShopVo.getShopStatus()));// 店铺状态
		}
		if (null != channelShopVo.getShopTitle() && !"".equals(channelShopVo.getShopTitle())) {
			criteria.andShopTitleLike("%" + channelShopVo.getShopTitle() + "%");// 店铺名称
		}
		Paging paging = new Paging(shopService.getChannelShopCount(channelShopExample), shopService.getChannelShopList(channelShopExample, true));
		writeJson(paging, response);

	}

	// 新增店铺和修改店铺用的是同一个方法
	@RequestMapping(value = "/insertChannelShop.spmvc")
	public void insertChannelShop(HttpServletResponse response,HttpServletRequest request, @ModelAttribute("channelShop") ChannelShop channelShop ,  @ModelAttribute("expiresTimeStr") String expiresTimeStr) {
//		String actionUser = getUserName(request);
			JsonResult jsonResult = new JsonResult();
			try {
				OmsChannelInfo omsChannelInfo= new OmsChannelInfo();
				String oChannelId = request.getParameter("oChannelId");
				if(StringUtils.isNotEmpty(oChannelId)){
					omsChannelInfo.setChannelId(Integer.parseInt(oChannelId));
				}else{
					//新增数据默认值设定
					//添加默认未激活
					omsChannelInfo.setIsActive((byte)0);
				}
				omsChannelInfo.setChannelCode(channelShop.getShopCode());
				omsChannelInfo.setChannelName(channelShop.getShopTitle());
				omsChannelInfo.setNickName(request.getParameter("oNickName"));
				omsChannelInfo.setPreSaleShopCode(channelShop.getPreSaleShopCode());
				String channelType = null;
				if(StringUtil.isEmpty(channelShop.getChannelCode())){
					channelType = "none";
				}else{
					channelType = channelShop.getChannelCode();
				}
				omsChannelInfo.setChannelType(channelType);
				omsChannelInfo.setParentChannelCode(channelShop.getParentShopCode());
				omsChannelInfo.setErpVdepotCode(channelShop.getShopCode()); // ERP_VDEPT_CODE
				if (StringUtil.isEmpty(omsChannelInfo.getParentChannelCode())) {
					omsChannelInfo.setParentChannelCode(omsChannelInfo.getChannelCode());
				}
				if (StringUtils.isEmpty(channelShop.getParentShopCode())) {
					channelShop.setParentShopCode(channelShop.getShopCode());
				}
				if(StringUtil.isNotNull(expiresTimeStr)){
					channelShop.setExpiresTime(DateTimeUtils.parseStr(expiresTimeStr));//token失效时间
				}
				// 物流回调地址
				if (StringUtils.isNotEmpty(channelShop.getLogisticsCallback())) {
					omsChannelInfo.setDeliveryApi(channelShop.getLogisticsCallback());
				}
				if(channelShop.getShopChannel().equals((byte)1)){
					omsChannelInfo.setShopType("online");
				}else{
					omsChannelInfo.setShopType("offline");
				}
				omsChannelInfo.setMsgSendType(channelShop.getMsgSendType());
				omsChannelInfo.setMsgTemplateCode(channelShop.getMsgTemplateCode());
				channelShop.setErpShopCode(channelShop.getShopCode());
				jsonResult = shopService.insertChannelShop(channelShop,omsChannelInfo);
			} catch (Exception e) {
				jsonResult.setIsok(false);
				jsonResult.setMessage(e.toString());
				logger.error("添加渠道异常" + e.getMessage(), e);
			}
			outPrintJson(response, jsonResult);
	}

	// 将修改/查看的对象信息带入添加页面，展示信息
	@RequestMapping(value = "/updateChannelShop.spmvc")
	public ModelAndView updateChannelShop(HttpServletRequest request, HttpServletResponse response, @ModelAttribute("channelShop") ChannelShop channelShop) {
		ModelAndView mav = new ModelAndView();
		String id = StringUtils.trim(request.getParameter("id"));
		ChannelShop cs = new ChannelShop();
		ChannelShop scs = null;
		if ("init".equals(request.getParameter("type"))) {
			try {
				cs.setId(Integer.valueOf(id));
				scs = shopService.queryChannelShop(cs);
				ChannelShopVo channelShopVo = new ChannelShopVo();
				if (scs != null) {
					PropertyUtils.copyProperties(channelShopVo, scs);
				}
				OmsChannelInfo omsChannelInfo = openShopChannelInfoService.findOmsChannelInfoBychannelcode(scs.getShopCode());
				mav.addObject("osci", omsChannelInfo);
				mav.addObject("scs", channelShopVo);
				mav.setViewName("channelShop/addOutChannelShop");
				return mav;
			} catch (Exception e) {
				logger.error("查询修改渠道信息异常", e);
			}
		}
		return null;
	}

	// 批量删除ids
	@RequestMapping(value = "/deleteChannelShop.spmvc")
	public void deleteChannelShop(HttpServletRequest request, HttpServletResponse response) {
		JsonResult jsonResult = new JsonResult();
		String ids = StringUtils.trim(request.getParameter("ids"));
		try {
			int num = shopService.deleteChannelShopByPrimaryKey(ids);
			if (num > 0) {
				jsonResult.setIsok(true);
			} else {
				jsonResult.setIsok(false);
			}
			outPrintJson(response, jsonResult);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	// 批量激活、移除
	@RequestMapping(value = "/activeChannelShop.spmvc")
	public void activeChannelShop(HttpServletRequest request, HttpServletResponse response) {
		String ids = StringUtils.trim(request.getParameter("ids"));
		String shopCodes = StringUtils.trim(request.getParameter("shopCodes"));
		String shopStatus = StringUtils.trim(request.getParameter("shopStatus"));
		JsonResult jsonResult = new JsonResult();
		try {
			shopService.activeChannelShop(ids, shopStatus, shopCodes);
			jsonResult.setMessage("操作成功！");
			jsonResult.setIsok(true);
		} catch (Exception e) {
			jsonResult.setIsok(false);
			jsonResult.setMessage(e.toString());
			logger.error("批量激活、移除异常：", e);
		}
		outPrintJson(response, jsonResult);
	}
	
	@RequestMapping(value = "/upload.spmvc")
	public void uploadFile(@RequestParam MultipartFile myfile, HttpServletRequest request, HttpServletResponse response) {
		JsonResult jsonResult = new JsonResult();
		InputStream isO = null;
		InputStream isC = null;
		
		try {
				isO = myfile.getInputStream();
				isC = myfile.getInputStream();
				List<OmsChannelInfo> listO = promotionsReadExcelUtils.readOpenShopChannelShop(isO);
				List<ChannelShop> listC = promotionsReadExcelUtils.readChannelShop(isC);
				JsonResult one  = openShopChannelInfoService.addOpenShopChannelInfo(listO);
				StringBuffer error = new StringBuffer();
				if(!one.isIsok()){
					error.append("OS数据导入失败：").append(one.getMessage());
					logger.error("OS数据导入失败：" + one.getMessage());
				}
				JsonResult two  = channelShopService.addShopChannelShop(listC);
				if(!two.isIsok()){
					logger.error("渠道数据导入失败：" + two.getMessage());
					if(StringUtil.isEmpty(error.toString())){
						error.append("渠道数据导入失败：").append(two.getMessage());
					}else{
						error.append("\n").append("渠道数据导入失败：").append(two.getMessage());
					}
				}
				if(StringUtil.isEmpty(error.toString())){
					jsonResult.setIsok(true);
					jsonResult.setMessage("导入成功!");
				}else{
					jsonResult.setIsok(false);
					jsonResult.setMessage(error.toString());
				}
		} catch (Exception e) {
			jsonResult.setIsok(false);
			jsonResult.setMessage("数据格式不正确，请检查文件!");
			e.printStackTrace();
		} finally {
				try {
					if (isO != null) {
					isO.close();
					}
					if (isC != null) {
					isC.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
			}
		}
		outPrintJson(response, jsonResult);
	}
	

	/**
	 * 回去前台所有的店铺为桌面使用
	 ***/
	@RequestMapping(value = "/getAllChannelShopList.spmvc")
	public void getAllChannelShopList(HttpServletRequest request, HttpServletResponse response) {
		JsonResult jsonResult = new JsonResult();

		AuthCenterFacade auth = Config.getAuthCenterFacade();
		List<DataPermission> authList = auth.allDataPermissionOfApp(getUserName(request), Config.getAppId());

		List<String> authCodes = new ArrayList<String>();
		for (DataPermission premission : authList) {
			authCodes.add(premission.getCode());
		}

		ChannelShopExample channelShopExample = new ChannelShopExample();
		Criteria criteria = channelShopExample.createCriteria();
		criteria.andShopStatusEqualTo((byte) 1);
		criteria.andShopChannelEqualTo((byte) 1);
		channelShopExample.setOrderByClause(" shop_title DESC ");
		List<ChannelShopVo> channelShopList = shopService.getChannelShopList(channelShopExample, false);

		List<ChannelShopVoo> list = new ArrayList<ChannelShopVoo>();

		for (int i = 0; i < channelShopList.size(); i++) {
			ChannelShopVo obj = channelShopList.get(i);

			if (!isExist(obj.getShopCode(), authCodes)) {
				continue;
			}
			String theText = obj.getShopTitle();
			ChannelShopVoo channelShopVoo = new ChannelShopVoo();
			channelShopVoo.setShopCode(obj.getShopCode());
			channelShopVoo.setShopTitle(obj.getShopTitle());
			channelShopVoo.setText(theText);
			channelShopVoo.setChannelCode(obj.getChannelCode());
			channelShopVoo.setChannelTitle(obj.getChannelTitle());
			channelShopVoo.setType(3);
			channelShopVoo.setLink("/custom/index/index.spmvc?shopCode=" + obj.getShopCode() + "&system=0&channelCode=" + obj.getChannelCode() + "&channelTitle=" + obj.getChannelTitle()
					+ "&menuType=" + Constants.MENU_CHANNEL_SHOP);
			channelShopVoo.setLeaf(true);
			channelShopVoo.setExpanded(true);
			channelShopVoo.setMenu(true);
			channelShopVoo.setIcon("kjcx.png");
			channelShopVoo.setRight("kccxgl");
			/*channelShopVoo.setShopImg(obj.getShopImg());*/
			if(theText.indexOf("天猫")>=0){
				channelShopVoo.setShopImg("icon_tmall.png");
			}else if(theText.indexOf("淘宝")>=0){
				channelShopVoo.setShopImg("icon_taobao.png");
			}else if(theText.indexOf("京东")>=0){
				channelShopVoo.setShopImg("icon_jd.png");
			}else if(theText.indexOf("一号店")>=0){
				channelShopVoo.setShopImg("icon_yhd.png");
			}else if(theText.indexOf("爱奇艺")>=0){
				channelShopVoo.setShopImg("icon_qiy.png");
			}else if(theText.indexOf("有范APP")>=0){
				channelShopVoo.setShopImg("icon_youfan.png");
			}else if(theText.indexOf("苏宁")>=0){
				channelShopVoo.setShopImg("icon_sn.png");
			}else if(theText.indexOf("当当")>=0){
				channelShopVoo.setShopImg("icon_dd.png");
			}else if(theText.indexOf("贝贝")>=0){
				channelShopVoo.setShopImg("icon_bb.png");
			}else if(theText.indexOf("拍拍")>=0){
				channelShopVoo.setShopImg("icon_pp.png");
			}else if(theText.indexOf("聚美")>=0){
				channelShopVoo.setShopImg("icon_jumei.png");
			}else if(theText.indexOf("美丽说")>=0){
				channelShopVoo.setShopImg("icon_mls.png");
			}else if(theText.indexOf("唯品会")>=0){
				channelShopVoo.setShopImg("icon_wph.png");
			}else if(theText.indexOf("楚楚街")>=0){
				channelShopVoo.setShopImg("icon_ccj.png");
			}else if(theText.indexOf("蚂蚁")>=0){
				channelShopVoo.setShopImg("icon_my.png");
			}else if(theText.indexOf("1688")>=0){
				channelShopVoo.setShopImg("icon_1688.png");
			}else if(theText.indexOf("微信")>=0){
				channelShopVoo.setShopImg("icon_wx.png");
			}else if(theText.indexOf("蘑菇街")>=0){
				channelShopVoo.setShopImg("icon_mgj.png");
			}else if(theText.indexOf("邦购")>=0){
				channelShopVoo.setShopImg("icon_bg.png");
			}else if(theText.indexOf("穿衣助手")>=0){
				channelShopVoo.setShopImg("icon_cyzs.png");
			}else if(theText.indexOf("野糖")>=0){
				channelShopVoo.setShopImg("icon_yt.png");
			}else if(theText.indexOf("MB品牌官网")>=0){
				channelShopVoo.setShopImg("icon_mbgw.png");
			}else if(theText.indexOf("MC品牌官网")>=0){
				channelShopVoo.setShopImg("icon_mcgw.png");
			}else if(theText.indexOf("品牌官网MooMoo")>=0){
				channelShopVoo.setShopImg("icon_mmgw.png");
			}else if(theText.indexOf("品牌官网MCK")>=0){
				channelShopVoo.setShopImg("icon_mckgw.png");
			}else if(theText.indexOf("品牌官网CHIN")>=0){
				channelShopVoo.setShopImg("icon_chingw.png");
			}else if(theText.indexOf("拼多多")>=0){
				channelShopVoo.setShopImg("icon_pdd.png");
			}else if(theText.indexOf("有赞")>=0){
				channelShopVoo.setShopImg("icon_yz.png");
			}else{
				channelShopVoo.setShopImg("icon_common.png");
			}
			channelShopVoo.setMenuType(Constants.MENU_CHANNEL_SHOP);

			list.add(channelShopVoo);
		}

		SystemResourceExample systemResourceExample = new SystemResourceExample();
		systemResourceExample.or().andParentCodeEqualTo("0");

		List<SystemResource> resources = systemResourceMapper.selectByExample(systemResourceExample);

		for (SystemResource systemResource : resources) {
			if (Constants.MENU_CHANNEL_SHOP.equals(systemResource.getResourceCode())) {
				continue;
			}
			if (isNotShow(systemResource.getResourceCode(), authCodes)) {
				continue;
			}
			String theText = systemResource.getResourceName();
			ChannelShopVoo channelShopVoo = new ChannelShopVoo();
			channelShopVoo.setShopCode("");
			channelShopVoo.setShopTitle(theText);
			channelShopVoo.setText(theText);
			channelShopVoo.setType(3);
			channelShopVoo.setLink("/custom/index/index.spmvc?menuType=" + systemResource.getResourceCode());
			channelShopVoo.setLeaf(true);
			channelShopVoo.setExpanded(true);
			channelShopVoo.setMenu(true);
			channelShopVoo.setIcon("kjcx.png");
			if("日志".equals(theText)){
				channelShopVoo.setShopImg("icon_log.png");
			}else if("集团门店".equals(theText)){
				channelShopVoo.setShopImg("icon_groupshop.png");
			}else if("OPENSHOP店铺".equals(theText)){
				channelShopVoo.setShopImg("icon_openshop.png");
			}else if("产品".equals(theText)){
				channelShopVoo.setShopImg("icon_product.png");
			}else if("系统设置".equals(theText)){
				channelShopVoo.setShopImg("icon_systeminstall.png");
			}else if("促销".equals(theText)){
				channelShopVoo.setShopImg("icon_promotion.png");
			}else{
				channelShopVoo.setShopImg("icon_common.png");
			}
			channelShopVoo.setRight("kccxgl");
			channelShopVoo.setMenuType(systemResource.getResourceCode());
			list.add(channelShopVoo);
		}

		jsonResult.setData(list);
		outPrintJson(response, jsonResult);
	}

	private Boolean isExist(String shopCode, List<String> authCodes) {
		for (String authCode : authCodes) {
			if (authCode.startsWith(shopCode)) {
				return true;
			}
		}
		return false;
	}

	private Boolean isNotShow(String resrouceCode, List<String> authCodes) {
		SystemResourceExample systemResourceExample = new SystemResourceExample();
		systemResourceExample.or().andParentCodeEqualTo(resrouceCode);
		List<SystemResource> resources = systemResourceMapper.selectByExample(systemResourceExample);
		for (SystemResource systemResource : resources) {
			String code = systemResource.getResourceCode();
			for (String authCode : authCodes) {
				if (authCode.startsWith(code)) {
					return false;
				}
			}
		}
		return true;
	}

	@RequestMapping(value = "selectParentChannelShop.spmvc")
	public void selectParentChannelShop(HttpServletRequest request, HttpServletResponse response) {
		String shopCode = request.getParameter("shopCode");
		JsonResult jr = new JsonResult();
		jr = channelShopService.selectParentChannelShop(shopCode);
		outPrintJson(response, jr);
	}

	@RequestMapping(value = "selectCurrentChannelShop.spmvc")
	public void selectCurrentChannelShop(HttpServletRequest request, HttpServletResponse response) {
		String shopCode = request.getParameter("shopCode");
		JsonResult jr = new JsonResult();
		jr = channelShopService.selectCurrentChannelShop(shopCode);
		outPrintJson(response, jr);
	}

	@RequestMapping(value = "selectChildChannelShop.spmvc")
	public void selectChildChannelShop(HttpServletRequest request, HttpServletResponse response) {
		String shopCode = request.getParameter("shopCode");
		JsonResult jr = new JsonResult();
		jr = channelShopService.selectChildChannelShop(shopCode);
		outPrintJson(response, jr);
	}

	// 把父店铺编号加载到选择框中。。。
	@RequestMapping(value = "/parentShopCodeList.spmvc")
	public void getParentChannelShopList(HttpServletRequest request, HttpServletResponse response) {
		String shopChannel = request.getParameter("shopChannel");
		ChannelShopExample channelShopExample = new ChannelShopExample();
		channelShopExample.or().andShopChannelEqualTo(Byte.valueOf(shopChannel));
		Paging paging = shopService.getParentChannelShop(channelShopExample);
		@SuppressWarnings("unchecked")
		List<ChannelShop> list = (List<ChannelShop>) paging.getRoot();
		for (ChannelShop channelShop : list) {
			channelShop.setShopTitle(channelShop.getShopCode() + "(" + channelShop.getShopTitle() + ")");
		}
		outPrintJson(response, list);
	}
}

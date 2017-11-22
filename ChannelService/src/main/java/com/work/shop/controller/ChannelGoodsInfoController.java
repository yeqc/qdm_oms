package com.work.shop.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.work.shop.bean.ChannelGoods;
import com.work.shop.bean.ChannelGoodsExtension;
import com.work.shop.bean.ChannelGoodsTicket;
import com.work.shop.bean.ColorAndSize;
import com.work.shop.bean.GoodsProperty;
import com.work.shop.bean.TicketInfo;
import com.work.shop.bean.bgcontentdb.BGproductGoodsGallery;
import com.work.shop.bean.bgcontentdb.BGproductGoodsGalleryExample;
import com.work.shop.bean.bgcontentdb.BGproductGoodsGalleryExample.Criteria;
import com.work.shop.dao.BgGoodsPropertyMapper;
import com.work.shop.dao.bgcontentdb.BGproductGoodsGalleryMapper;
import com.work.shop.service.ChannelGoodsService;
import com.work.shop.service.ShopGoodsService;
import com.work.shop.util.ChannelGoodsReadExcelUtils;
import com.work.shop.util.StringUtil;
import com.work.shop.util.extjs.PageHelper;
import com.work.shop.util.extjs.Paging;
import com.work.shop.vo.ChannelGoodsInfoVo;
import com.work.shop.vo.ChannelGoodsTicketVo;
import com.work.shop.vo.DescModule;
import com.work.shop.vo.JsonResult;

@Controller
@RequestMapping(value = "channelGoodsInfo")
public class ChannelGoodsInfoController extends BaseController {

	private static final Logger logger = Logger.getLogger(ChannelGoodsInfoController.class);
	
	@Resource(name = "channelGoodsService")
	private ChannelGoodsService channelGoodsService;

	@Resource(name = "shopGoodsService")
	private ShopGoodsService shopGoodsService;
	
//	@Resource(name = "productBarcodeListMapper")
//	private ProductBarcodeListMapper productBarcodeListMapper;
	
	@Resource(name = "bgGoodsPropertyMapper")
	private BgGoodsPropertyMapper goodsPropertyMapper;
	
	@Resource(name = "bGproductGoodsGalleryMapper")
	private BGproductGoodsGalleryMapper productGoodsGalleryMapper;
	
	// 调整单商品信息列表
	@RequestMapping(value = "channelGoodsInfoList.spmvc")
	public ModelAndView channelGoodsInfoPage(HttpServletRequest request,
			HttpServletResponse response, ChannelGoodsInfoVo model,
			PageHelper helper) throws Exception {
		String method = request.getParameter("method") == null ? "" : request
				.getParameter("method");
		if (!StringUtil.isTrimEmpty(method) && method.equals("init")) {
			ModelAndView mav = new ModelAndView();
			mav.setViewName("shopGoods/shopGoodsList");
			return mav;
		}
		Paging paging = channelGoodsService.getChannelGoodsPage(model, helper);
		writeJson(paging, response);
		return null;
	}
	
	// 调整单商品名称列表
	@RequestMapping(value = "channelGoodsTitleList.spmvc")
	public ModelAndView channelGoodsTitlePage(HttpServletRequest request,
			HttpServletResponse response, ChannelGoodsInfoVo model,
			PageHelper helper) throws Exception {
		String method = request.getParameter("method") == null ? "" : request
				.getParameter("method");
		if (!StringUtil.isTrimEmpty(method) && method.equals("init")) {
			ModelAndView mav = new ModelAndView();
			mav.setViewName("shopGoods/shopGoodsTitle");
			return mav;
		}
		Paging paging = shopGoodsService.getShopGoodsUpDownList(model, helper);
		writeJson(paging, response);
		return null;
	}

	// 点击生成调整单按钮页面跳转
	// 调整单号生成即新增调整单
	@RequestMapping(value = "addShopGoodsInfo.spmvc")
	public ModelAndView addShopGoodsInfo(Integer id) {
		ModelAndView mav = new ModelAndView();
		ChannelGoodsTicketVo addNewCgtVo = new ChannelGoodsTicketVo();
		if (null != id) {
			addNewCgtVo = shopGoodsService.getChannelGoodsTicketVo(id);
		} else {
			String ticketCode = "GI" + StringUtil.getSysCode();
			addNewCgtVo.setTicketCode(ticketCode);// 新增调整单的编号系统生成
		}
		mav.addObject("addNewCgtVo", addNewCgtVo);
		mav.setViewName("shopGoods/addShopGoodsInfo");
		return mav;
	}

	// 点击批量添加商品按钮
	@RequestMapping(value = "upload.spmvc")
	public void uploadFile(@RequestParam MultipartFile myfile,
			HttpServletRequest request, HttpServletResponse response,
			ChannelGoodsTicketVo ticketVo) {
		JsonResult jsonResult = new JsonResult();
		InputStream is = null;
		try {
			is = myfile.getInputStream();
			String channelCode = request.getParameter("channelCode");
			String shopCode = request.getParameter("shopCode");
			StringBuffer sb = new StringBuffer();
//			List<ChannelGoodsInfoVo> goodsList = ChannelGoodsReadExcelUtils.readChannelGoodsFile(channelCode, shopCode, is, sb);
			List<ChannelGoodsInfoVo> goodsList = ChannelGoodsReadExcelUtils
					.readCsvrhannelGoodsFile(channelCode, shopCode, is, sb);

			 if (StringUtil.isNotEmpty(sb.toString())) {
				jsonResult.setIsok(false);
				jsonResult.setMessage("商品编码(" + sb.toString()
						+ ")存在重复数据，请检查文件!");
			} else {
				ticketVo.setUserName(getUserName(request));
				jsonResult = channelGoodsService.addChannelGoods(ticketVo,
						goodsList);
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
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		outPrintJson(response, jsonResult);
	}

	// 点击批量添加商品按钮
	@RequestMapping(value = "delete.spmvc")
	public void deleteBatch(HttpServletRequest request,
			HttpServletResponse response) {
		JsonResult jsonResult = new JsonResult();
		try {
			String[] ids = request.getParameterValues("ids");
			if (ids == null || ids.length == 0) {
				return;
			}
			List<String> idList = new ArrayList<String>();
			for (int i = 0; i < ids.length; i++) {
				idList.add(ids[i]);
			}
			jsonResult = channelGoodsService.deleteChannelGoods(idList);
			outPrintJson(response, jsonResult);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 点击批量添加商品按钮
	@RequestMapping(value = "toModify.spmvc")
	public ModelAndView toModify(HttpServletRequest request,
			HttpServletResponse response) {
		ModelAndView mav = new ModelAndView();
		try {
			String id = request.getParameter("id");
			if (StringUtils.isEmpty(id)) {
				return null;
			}
			ChannelGoodsTicket channelGoodsTicket = channelGoodsService
					.getChannelGoodsTicketById(id);
			mav.addObject("channelGoodsTicket",
					JSONObject.toJSON(channelGoodsTicket));
			List<TicketInfo> list = channelGoodsService
					.getTicketInfoByTicketCode(channelGoodsTicket
							.getTicketCode());
			if (list == null || list.size() != 1) {
				return null;
			}
			mav.addObject("ticketInfo", JSONObject.toJSON(list.get(0)));
			mav.setViewName("shopGoods/addShopGoods");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mav;
	}

	// 点击批量添加商品按钮
	@RequestMapping(value = "doUpdate.spmvc")
	public void doUpdate(@ModelAttribute ChannelGoodsInfoVo channelGoodsInfoVo,
			HttpServletRequest request, HttpServletResponse response) {
		JsonResult jsonResult = new JsonResult();
		try {
			jsonResult = channelGoodsService
					.updateChannelGoodsTicketInof(channelGoodsInfoVo);
			outPrintJson(response, jsonResult);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 预览宝贝详情
	@RequestMapping(value = "previewDetail.spmvc")
	public ModelAndView previewDetail(HttpServletRequest request,
			HttpServletResponse response, String shopCode, String goodsSn, String channelCode, String ticketType)
			throws Exception {
		ChannelGoodsTicket cgt = new ChannelGoodsTicket();
		cgt.setTicketType((byte)2);
		ModelAndView mav = new ModelAndView();
		
		ChannelGoodsExtension extension = null;
		List<DescModule> localModules = null;
		if (StringUtil.isNotEmpty(goodsSn) && StringUtil.isNotEmpty(shopCode)) {
			extension = this.channelGoodsService.getGoodsSnDetail(goodsSn, shopCode);
			//电脑版宝贝详情
			if (null != extension && "2".equals(ticketType) ) {
				if (StringUtil.isTaoBaoChannel(channelCode)) {
					try {
						localModules = JSONArray.parseArray(extension.getGoodsDesc(), DescModule.class);
					} catch (Exception e) {
						e.printStackTrace();
						logger.error("商品详情内容转[DescModule]对象出错："+e.getMessage());
					}
				}
			}
		}
		mav.addObject("channelGoodsTicket", cgt);
		mav.addObject("ticketType", ticketType);
		mav.addObject("extension", extension);
		mav.addObject("localModules", localModules);
		mav.addObject("channelCode", channelCode);
		mav.setViewName("shopGoods/previewDetail");
		return mav;
	}

	// 渠道商品信息列表
	@RequestMapping(value = "channelGoodsPage.spmvc")
	public ModelAndView channelGoodsPage(HttpServletRequest request,
			HttpServletResponse response, ChannelGoods model, PageHelper helper)
			throws Exception {
		String method = request.getParameter("method") == null ? "" : request
				.getParameter("method");
		if (!StringUtil.isTrimEmpty(method) && method.equals("init")) {
			ModelAndView mav = new ModelAndView();
			mav.setViewName("channelGoods/channelGoodsList");
			return mav;
		}
		Paging paging = channelGoodsService.getChannelGoodsList(model, helper);
		writeJson(paging, response);
		return null;
	}
	
	// 渠道商品信息列表
	@RequestMapping(value = "channelGoodsPage2.spmvc")
	public ModelAndView shopGoodsUpDownPage(){		
		ModelAndView mav = new ModelAndView();
		mav.setViewName("shopGoods/shopGoodsSyncStock");
		return mav;
	}
	
	// 渠道商品信息列表
	@RequestMapping(value = "shopGoodsBandingStorePage.spmvc")
	public ModelAndView channelGoodsBandingStorePage(){		
		ModelAndView mav = new ModelAndView();
		mav.setViewName("shopGoods/shopGoodsBandingStore");
		return mav;
	}

	// 渠道商品信息列表
	@RequestMapping(value = "selectTemplate.spmvc")
	public ModelAndView selectTemplate(HttpServletRequest request,
			HttpServletResponse response, String ticketCode, String ticketType) throws Exception {
		ModelAndView mav = new ModelAndView();
	//	mav.addObject("ticketType", ticketType);
		mav.addObject("ticketCode", ticketCode);
		mav.setViewName("shopGoods/selectTemplate");
		return mav;
	}

	/**
	 * 本地经营商品详情
	 ***/
	@RequestMapping(value = "toChannelGoodsDetails.spmvc")
	public ModelAndView toChannelGoodsDetails(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String goodsSn = request.getParameter("goodsSn");
		ModelAndView mav = new ModelAndView();
		GoodsProperty gp = channelGoodsService.getGoodsProperty(goodsSn);
		
		//pbCriteria.
		Map<String,Object> colorMap = new HashMap<String,Object>();
	 
		if(null != goodsSn && !"".equals(goodsSn) ) {
			colorMap.put("goodsSn", goodsSn);
		}
		colorMap.put("colorCode", "color_Code");
		colorMap.put("colorName", "color_Name");
		
		//List <ProductBarcodeList> pbList = productBarcodeListMapper.selectByExample(productBarcodeListExample);
		List<ColorAndSize> colorList = goodsPropertyMapper.selectDistinctColorAndSize(colorMap);
		
		Map<String,Object> sizeMap = new HashMap<String,Object>();
		
		if(null != goodsSn && !"".equals(goodsSn) ) {
			sizeMap.put("goodsSn", goodsSn);
		}
		
		sizeMap.put("sizeCode", "size_code");
		sizeMap.put("sizeName", "size_Name");
		
		List<ColorAndSize> sizeList = goodsPropertyMapper.selectDistinctColorAndSize(sizeMap);
		
		
		BGproductGoodsGalleryExample productGoodsGalleryExample = new BGproductGoodsGalleryExample();
		
		Criteria pbCriteria = productGoodsGalleryExample.or();
		
		if (StringUtil.isNotEmpty(goodsSn)) {
			pbCriteria.andGoodsSnEqualTo(goodsSn);
		}
		
		List<BGproductGoodsGallery> pggList = productGoodsGalleryMapper.selectByExample(productGoodsGalleryExample);
		
		mav.addObject("pggList",pggList);
		mav.addObject("sizeList",sizeList);
		mav.addObject("colorList",colorList);
		mav.addObject("gp",gp);
		mav.setViewName("channelGoods/channelGoodsDetails");
		return mav;
	}
	
	
	

}

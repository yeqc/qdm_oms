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

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.work.shop.bean.ColorAndSize;
import com.work.shop.bean.GoodsProperty;
import com.work.shop.bean.ProductBarcodeList;
import com.work.shop.bean.ProductBarcodeListExample;
import com.work.shop.bean.ProductGoodsGalleryVo;
import com.work.shop.bean.bgcontentdb.BGproductGoods;
import com.work.shop.bean.bgcontentdb.BGproductGoodsGallery;
import com.work.shop.bean.bgcontentdb.BGproductGoodsGalleryExample;
import com.work.shop.bean.bgcontentdb.BGproductGoodsGalleryExample.Criteria;
import com.work.shop.dao.BgGoodsPropertyMapper;
import com.work.shop.dao.ProductBarcodeListMapper;
import com.work.shop.dao.bgcontentdb.BGproductGoodsGalleryMapper;
import com.work.shop.service.ChannelGoodsService;
import com.work.shop.service.ProductGoodsService;
import com.work.shop.util.StringUtil;
import com.work.shop.util.SynChannelGoodsExcelUtil;
import com.work.shop.util.extjs.PageHelper;
import com.work.shop.util.extjs.Paging;
import com.work.shop.vo.JsonResult;




@Controller
@RequestMapping(value = "productGoods")
public class ProductGoodsController extends BaseController{
	
	private static Logger logger = Logger.getLogger(ProductGoodsController.class);

	@Resource(name="productGoodsService")
	private ProductGoodsService productGoodsService;
	
	@Resource(name = "channelGoodsService")
	private ChannelGoodsService channelGoodsService;
	
	@Resource(name = "bgGoodsPropertyMapper")
	private BgGoodsPropertyMapper goodsPropertyMapper;
	
	@Resource(name = "bGproductGoodsGalleryMapper")
	private BGproductGoodsGalleryMapper productGoodsGalleryMapper;
	
	@Resource
	private SynChannelGoodsExcelUtil synChannelGoodsExcelUtil;
	
	@Resource
	private ProductBarcodeListMapper productBarcodeListMapper;
	
	
	@RequestMapping(value = "synChannelGoods.spmvc")
	public void synProductGoodsToChannelGoods(@RequestParam MultipartFile myfile,HttpServletResponse response,HttpServletRequest request){
		String channelCode = request.getParameter("channelCode");
		String shopCode = request.getParameter("shopCode");
		JsonResult jsonResult = new JsonResult();
		StringBuffer sb = new StringBuffer("");
		InputStream is = null;
		try{
			is = myfile.getInputStream();
			// jsonResult = synChannelGoodsExcelUtil.readXls(is, channelCode,
			// shopCode, sb);
			jsonResult = synChannelGoodsExcelUtil.readCsv(is, channelCode,
					shopCode, sb , getUserName(request));
			outPrintJson(response, jsonResult);
		}catch(IOException e){
			e.printStackTrace();
		}finally{
			if(is!=null){
				try {
					is.close();
					is=null;
				} catch (IOException e) {
					e.printStackTrace();
				}
				
			}
		}
	}
	
	@RequestMapping(value = "synChannelGoods2.spmvc")
	public void synProductGoodsToChannelGoods2(@RequestParam MultipartFile myfile,HttpServletResponse response,HttpServletRequest request){
		String channelCode = request.getParameter("channelCode");
		String shopCode = request.getParameter("shopCode");
		JsonResult jsonResult = new JsonResult();
		StringBuffer sb = new StringBuffer("");
		InputStream is = null;
		try{
			is = myfile.getInputStream();
			// jsonResult = synChannelGoodsExcelUtil.readXls(is, channelCode,
			// shopCode, sb);
			jsonResult = synChannelGoodsExcelUtil.readCsv2(is, channelCode,
					shopCode, sb , getUserName(request));
			outPrintJson(response, jsonResult);
		}catch(IOException e){
			e.printStackTrace();
		}finally{
			if(is!=null){
				try {
					is.close();
					is=null;
				} catch (IOException e) {
					e.printStackTrace();
				}
				
			}
		}
	}
	
	class SynChannelGoodsThread extends Thread {
		
		private String channelCode;
		private String shopCode;
		
		public SynChannelGoodsThread(String channelCode,String shopCode){
			this.channelCode = channelCode;
			this.shopCode = shopCode;
		}
		
		public void run(){
			
//			productGoodsService.synProductGoodsInfo(channelCode,shopCode);
		}

	}

	// 调整单商品信息列表
	@RequestMapping(value = "productGoodsList.spmvc")
	public ModelAndView channelGoodsInfoPage(HttpServletRequest request,
			HttpServletResponse response, BGproductGoods model,
			PageHelper helper) throws Exception {
		String method = request.getParameter("method") == null ? "" : request
				.getParameter("method");
		if (!StringUtil.isTrimEmpty(method) && method.equals("init")) {
			ModelAndView mav = new ModelAndView();
			mav.setViewName("productGoods/productGoodsList");
			return mav;
		}
		Paging paging = productGoodsService.getProductGoodsList(model, helper);
		writeJson(paging, response);
		return null;
	}
	
	/**
	 * 本地经营商品详情
	 ***/
	@RequestMapping(value = "toProductGoodsDetails.spmvc")
	public ModelAndView toProductGoodsDetails(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String goodsSn = request.getParameter("goodsSn");
		ModelAndView mav = new ModelAndView();
		try {
			GoodsProperty gp = channelGoodsService.getGoodsProperty(goodsSn);
			//pbCriteria.
			Map<String,Object> colorMap = new HashMap<String,Object>();
		 
			if(null != goodsSn && !"".equals(goodsSn) ) {
				colorMap.put("goodsSn", goodsSn);
			}
			colorMap.put("colorCode", "color_Code");
			colorMap.put("colorName", "color_Name");
			
//			List<ColorAndSize> colorList = goodsPropertyMapper.selectDistinctColorAndSize(colorMap);
			List<ColorAndSize> colorList = new ArrayList<ColorAndSize>();
			List<ColorAndSize> sizeList =  new ArrayList<ColorAndSize>();
			ProductBarcodeListExample barcodeListExample = new ProductBarcodeListExample();
			barcodeListExample.or().andGoodsSnEqualTo(goodsSn);
			List<ProductBarcodeList> barcodeLists = productBarcodeListMapper.selectByExample(barcodeListExample);
			if (StringUtil.isListNotNull(barcodeLists)) {
				for (ProductBarcodeList barcodeList : barcodeLists) {
					ColorAndSize colorAndSize = new ColorAndSize();
					colorAndSize.setColorCode(barcodeList.getColorCode());
					colorAndSize.setColorName(barcodeList.getColorName());
					colorAndSize.setSizeCode(barcodeList.getSizeCode());
					colorAndSize.setSizeName(barcodeList.getSizeName());
					colorList.add(colorAndSize);
					sizeList.add(colorAndSize);
				}
			}
			Map<String,Object> sizeMap = new HashMap<String,Object>();
			
			if(null != goodsSn && !"".equals(goodsSn) ) {
				sizeMap.put("goodsSn", goodsSn);
			}
			
			sizeMap.put("sizeCode", "size_code");
			sizeMap.put("sizeName", "size_Name");
			
//			List<ColorAndSize> sizeList = goodsPropertyMapper.selectDistinctColorAndSize(sizeMap);
			
			BGproductGoodsGalleryExample productGoodsGalleryExample = new BGproductGoodsGalleryExample();
			
			Criteria pbCriteria = productGoodsGalleryExample.or();
			
			if (StringUtil.isNotEmpty(goodsSn)) {
				pbCriteria.andGoodsSnEqualTo(goodsSn);
			}
			
			List<BGproductGoodsGallery> pggList = productGoodsGalleryMapper.selectByExample(productGoodsGalleryExample);
			
			List<ProductGoodsGalleryVo> galleryVos = new ArrayList<ProductGoodsGalleryVo>();
			try {
				// ProductGoodsGallery 内容拷贝至 ProductGoodsGalleryVo中
				if (StringUtil.isListNotNull(pggList)) {
					for (BGproductGoodsGallery goodsGallery : pggList) {
						ProductGoodsGalleryVo galleryVo = new ProductGoodsGalleryVo();
						PropertyUtils.copyProperties(galleryVo, goodsGallery);
						galleryVos.add(galleryVo);
					}
				}
			} catch (Exception e) {
				logger.error("复制ProductGoodsGallery信息异常", e);
			}
			mav.addObject("pggList",galleryVos);
			mav.addObject("sizeList",sizeList);
			mav.addObject("colorList",colorList);
			mav.addObject("gp",gp);
			mav.setViewName("productGoods/productGoodsDetails");
		} catch (Exception e) {
			logger.error(goodsSn + "查询商品详情异常" + e.getMessage(), e);
		}
		return mav;
	}
	

}


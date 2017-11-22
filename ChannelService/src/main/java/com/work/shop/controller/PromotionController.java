package com.work.shop.controller;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.work.shop.bean.ChannelShopExample;
import com.work.shop.bean.GiftsGoodsList;
import com.work.shop.bean.GroupGoods;
import com.work.shop.bean.GroupGoodsList;
import com.work.shop.bean.PromotionsInfo;
import com.work.shop.bean.PromotionsLimitSn;
import com.work.shop.bean.PromotionsListGoods;
import com.work.shop.bean.PromotionsListInfor;
import com.work.shop.bean.PromotionsLog;
import com.work.shop.service.PromotionService;
import com.work.shop.service.ShopService;
import com.work.shop.util.Constants;
import com.work.shop.util.DateTimeUtils;
import com.work.shop.util.PromotionsReadExcelUtils;
import com.work.shop.util.StringUtil;
import com.work.shop.util.extjs.Paging;
import com.work.shop.vo.ChannelShopVo;
import com.work.shop.vo.GroupGoodsListSearchVO;
import com.work.shop.vo.GroupGoodsVO;
import com.work.shop.vo.JsonResult;
import com.work.shop.vo.PromotionEditVO;
import com.work.shop.vo.PromotionInfoSearchVO;
import com.work.shop.vo.PromotionVO;
import com.work.shop.vo.PromotionsLimitMoneyVO;

@Controller
@RequestMapping(value = "/promotion/")
@SuppressWarnings("unchecked")
public class PromotionController extends BaseController {
	
	private static Logger logger = Logger.getLogger(PromotionController.class);

	@Resource(name = "promotionServiceImpl")
	private PromotionService promotionService;

	@Resource(name = "shopService")
	private ShopService shopService;

	@Resource(name = "promotionsReadExcelUtils")
	private PromotionsReadExcelUtils promotionsReadExcelUtils;

	@RequestMapping(value = "/channelshoplist.spmvc", method = RequestMethod.GET)
	public void getChannelShopList(HttpServletRequest request, HttpServletResponse response) {

		// Object shopCode = request.getSession().getAttribute("shopCode");

		ChannelShopExample channelShopExample = new ChannelShopExample();
		com.work.shop.bean.ChannelShopExample.Criteria criteria = channelShopExample.or();
		criteria.andShopStatusEqualTo(Byte.valueOf("1")); // 默认加载已激活状态的店铺
		criteria.andShopChannelEqualTo(Byte.valueOf("1"));
		/*
		 * if(null != shopCode && !"".equals(shopCode) ){
		 * criteria.andShopCodeEqualTo(shopCode.toString()); }
		 */

		List<ChannelShopVo> list = shopService.getChannelShopList(channelShopExample, false);
		outPrintJson(response, list);
	}

	@RequestMapping(value = "/list.spmvc", method = RequestMethod.GET)
	public ModelAndView list(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("promotion/promotion-list");
		return mav;
	}

	@RequestMapping(value = "/groupgoodslist.spmvc", method = RequestMethod.GET)
	public ModelAndView groupgoodslist(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("promotion/groupgoods-list");
		return mav;
	}

	@RequestMapping("/search.spmvc")
	public void search(HttpServletRequest request, HttpServletResponse response) {
		try {
			PromotionInfoSearchVO searchVO = new PromotionInfoSearchVO();
			String start = request.getParameter("start");
			if (StringUtils.isNotEmpty(start)) {
				searchVO.setStart(Integer.valueOf(start));
			}
			String limit = request.getParameter("limit");
			if (StringUtils.isNotEmpty(limit)) {
				searchVO.setLimit(Integer.valueOf(limit));
			}
			String promCode = request.getParameter("promCode");
			searchVO.setPromCode(promCode);
			String shopCode = request.getParameter("shopCode");
			searchVO.setShopCode(shopCode);
			String promTitle = request.getParameter("promTitle");
			searchVO.setPromTitle(promTitle);
			String beginTime = request.getParameter("beginTime");
			searchVO.setBeginTime(DateTimeUtils.parseStr(beginTime, DateTimeUtils.YYYY_MM_DD_HH_mm_ss));
			String endTime = request.getParameter("endTime");
			searchVO.setEndTime(DateTimeUtils.parseStr(endTime, DateTimeUtils.YYYY_MM_DD_HH_mm_ss));
			String promStatus = request.getParameter("promStatus");
			searchVO.setPromStatus(StringUtils.isEmpty(promStatus) ? null : Byte.valueOf(promStatus));
			String promType = request.getParameter("promType");
			searchVO.setPromType(StringUtils.isEmpty(promType) ? null : Byte.valueOf(promType));

			JsonResult jsonResult = promotionService.searchPromotionsInfo(searchVO);

			Paging paging = new Paging();
			paging.setTotalProperty(jsonResult.getTotalProperty());
			paging.setRoot((List<PromotionsInfo>) jsonResult.getData());
			writeJson(paging, response);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@RequestMapping("/groupgoodssearch.spmvc")
	public void groupGoodsSearch(@ModelAttribute GroupGoodsVO searchVO, HttpServletRequest request, HttpServletResponse response) {
		try {
			JsonResult jsonResult = promotionService.searchGroupGoods(searchVO);
			Paging paging = new Paging();
			paging.setTotalProperty(jsonResult.getTotalProperty());
			paging.setRoot((List<GroupGoods>) jsonResult.getData());
			writeJson(paging, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@RequestMapping("/groupgoodlistssearch.spmvc")
	public void groupGoodsListSearch(HttpServletRequest request, HttpServletResponse response) {
		try {
			GroupGoodsListSearchVO searchVO = new GroupGoodsListSearchVO();
			String start = request.getParameter("start");
			if (StringUtils.isNotEmpty(start)) {
				searchVO.setStart(Integer.valueOf(start));
			}
			String limit = request.getParameter("limit");
			if (StringUtils.isNotEmpty(limit)) {
				searchVO.setLimit(Integer.valueOf(limit));
			}
			String groupCode = request.getParameter("groupCode");
			searchVO.setGroupCode(groupCode);

			JsonResult jsonResult = promotionService.searchGroupGoodsList(searchVO);

			Paging paging = new Paging();
			paging.setTotalProperty(jsonResult.getTotalProperty());
			paging.setRoot((List<PromotionsInfo>) jsonResult.getData());
			writeJson(paging, response);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@RequestMapping("/validateGroupCode.spmvc")
	public void validateGroupCode(HttpServletRequest request, HttpServletResponse response) {
		try {
			JsonResult jsonResult = new JsonResult();
			String groupCode = request.getParameter("groupCode");

			if (StringUtils.isEmpty(groupCode)) {
				jsonResult.setIsok(false);
				jsonResult.setMessage("套装商品编码不能为空!");
			} else {
				GroupGoods groupGoods = promotionService.searchGroupGoodsByGroupCode(groupCode);
				if (groupGoods != null) {
					jsonResult.setIsok(false);
					jsonResult.setMessage("套装商品编码已经存在!");
				}
			}
			outPrintJson(response, jsonResult);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@RequestMapping("/delete.spmvc")
	public void delete(HttpServletRequest request, HttpServletResponse response) {
		try {
			String[] ids = request.getParameterValues("ids");
			if (ids == null || ids.length == 0) {
				return;
			}
			List<Integer> idList = new ArrayList<Integer>();
			for (int i = 0; i < ids.length; i++) {
				idList.add(Integer.valueOf(ids[i]));
			}
			PromotionsLog promotionsLog = new PromotionsLog();
			promotionsLog.setUserId(getUserName(request));
			JsonResult jsonResult = promotionService.deletePromotionsInfo(idList, promotionsLog);
			outPrintJson(response, jsonResult);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@RequestMapping("/deleteGroupGoods.spmvc")
	public void deleteGroupGoods(HttpServletRequest request, HttpServletResponse response) {
		try {
			String[] groupCodes = request.getParameterValues("groupCodes");
			if (groupCodes == null || groupCodes.length == 0) {
				return;
			}
			List<String> groupCodeList = new ArrayList<String>();
			for (int i = 0; i < groupCodes.length; i++) {
				groupCodeList.add(groupCodes[i]);
			}
			JsonResult jsonResult = promotionService.deleteGroupGoods(groupCodeList);
			outPrintJson(response, jsonResult);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@RequestMapping("/deleteGroupGoodsList.spmvc")
	public void deleteGroupGoodsList(HttpServletRequest request, HttpServletResponse response) {
		try {
			String[] ids = request.getParameterValues("ids");
			if (ids == null || ids.length == 0) {
				return;
			}
			List<Integer> idList = new ArrayList<Integer>();
			for (int i = 0; i < ids.length; i++) {
				idList.add(Integer.valueOf(ids[i]));
			}
			JsonResult jsonResult = promotionService.deleteGroupGoodsList(idList);
			outPrintJson(response, jsonResult);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@RequestMapping("/activate.spmvc")
	public void activate(HttpServletRequest request, HttpServletResponse response) {
		try {
			String[] ids = request.getParameterValues("ids");
			if (ids == null || ids.length == 0) {
				return;
			}
			PromotionsLog promotionsLog = new PromotionsLog();
			promotionsLog.setUserId(getUserName(request));
			List<Integer> idList = new ArrayList<Integer>();
			for (int i = 0; i < ids.length; i++) {
				idList.add(Integer.valueOf(ids[i]));
			}
			JsonResult jsonResult = promotionService.activatePromotionsInfo(idList, promotionsLog);
			outPrintJson(response, jsonResult);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@RequestMapping("/unactivate.spmvc")
	public void unactivate(HttpServletRequest request, HttpServletResponse response) {
		try {
			String[] ids = request.getParameterValues("ids");
			if (ids == null || ids.length == 0) {
				return;
			}
			PromotionsLog promotionsLog = new PromotionsLog();
			promotionsLog.setUserId(getUserName(request));
			List<Integer> idList = new ArrayList<Integer>();
			for (int i = 0; i < ids.length; i++) {
				idList.add(Integer.valueOf(ids[i]));
			}
			JsonResult jsonResult = promotionService.unactivatePromotionsInfo(idList, promotionsLog);
			outPrintJson(response, jsonResult);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@RequestMapping("/addOrSave.spmvc")
	public void addOrSavePromotion(@ModelAttribute PromotionVO promotionVO, HttpServletRequest request, HttpServletResponse response) {
		JsonResult jsonResult = new JsonResult();
		try {
			PromotionsLog promotionsLog = new PromotionsLog();
			promotionsLog.setUserId(getUserName(request));
			promotionsLog.setShopCode(promotionVO.getShopCode());
			promotionsLog.setPromCode(promotionVO.getPromCode());
			promotionsLog.setPromType(Byte.valueOf(promotionVO.getPromType()));
			if (StringUtils.isEmpty(promotionVO.getId())) {
				jsonResult = promotionService.addPromotion(promotionVO, promotionsLog);
			} else {
				jsonResult = promotionService.savePromotion(promotionVO, promotionsLog);
			}
		} catch (Exception e) {
			logger.error("添加促销活动异常", e);
			jsonResult.setIsok(false);
			jsonResult.setMessage("添加促销活动异常" + e.getMessage());
		}
		outPrintJson(response, jsonResult);
	}

	@RequestMapping("/addOrSaveGroupGoodsList.spmvc")
	public void addOrSaveGroupGoodsList(@ModelAttribute PromotionVO promotionVO, HttpServletRequest request, HttpServletResponse response) {
		try {
			JsonResult jsonResult = new JsonResult();
			if (StringUtils.isEmpty(promotionVO.getAddOrSave())) {
				jsonResult.setIsok(false);
				jsonResult.setMessage("参数(AddOrSave)丢失");
			}
			if ("add".equals(promotionVO.getAddOrSave())) {
				jsonResult = promotionService.addGroupGoods(promotionVO);
			}
			if ("save".equals(promotionVO.getAddOrSave())) {
				jsonResult = promotionService.saveGroupGoods(promotionVO);
			}
			outPrintJson(response, jsonResult);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@RequestMapping("/tomodify.spmvc")
	public ModelAndView toModify(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView mav = new ModelAndView();
		try {
			String id = request.getParameter("id");
			PromotionsInfo promotionsInfo = promotionService.searchPromotionsInfoById(Integer.valueOf(id));
			mav.addObject("promotionsInfo", JSONObject.toJSON(promotionsInfo));

			byte promType = promotionsInfo.getPromType();
			String promCode = promotionsInfo.getPromCode();

			// 促销信息 (满赠 ) 类型
			if (Constants.PROM_TYPE_0 == promType) {
				List<PromotionsLimitMoneyVO> promotionsLimitMoneyList = promotionService.searchPromotionsLimitMoneyVOByPromCode(promCode);
				mav.addObject("promotionsLimitMoneyList", JSONObject.toJSON(promotionsLimitMoneyList));
				List<GiftsGoodsList> giftsGoodsListList = promotionService.searchGiftsGoodsListByPromCode(promCode);
				mav.addObject("giftsGoodsListList", JSONObject.toJSON(giftsGoodsListList));
				mav.setViewName("promotion/promotion-0");
			}

			// 促销信息 (买赠 ) 类型
			if (Constants.PROM_TYPE_2 == promType) {
				List<PromotionsLimitSn> promotionsLimitSnList = promotionService.searchPromotionsLimitSnByPromCode(promCode);
				mav.addObject("promotionsLimitSnList", JSONObject.toJSON(promotionsLimitSnList));
				List<GiftsGoodsList> giftsGoodsListList = promotionService.searchGiftsGoodsListByPromCode(promCode);
				mav.addObject("giftsGoodsListList", JSONObject.toJSON(giftsGoodsListList));
				mav.setViewName("promotion/promotion-2");
			}

			// 促销信息 (集合赠 ) 类型
			if (Constants.PROM_TYPE_3 == promType) {
				PromotionsListInfor promotionsListInfor = promotionService.searchPromotionsListInforByPromCode(promCode);
				mav.addObject("promotionsListInfor", JSONObject.toJSON(promotionsListInfor));

				List<PromotionsListGoods> promotionsListGoodsList = promotionService.searchPromotionsListGoodsByPromCode(promCode);
				mav.addObject("promotionsListGoodsList", JSONObject.toJSON(promotionsListGoodsList));

				List<GiftsGoodsList> giftsGoodsListList = promotionService.searchGiftsGoodsListByPromCode(promCode);
				mav.addObject("giftsGoodsListList", JSONObject.toJSON(giftsGoodsListList));

				mav.setViewName("promotion/promotion-3");
			}

		} catch (Exception e) {
			logger.error("查看促销数据异常",e);
		}
		return mav;
	}

	@RequestMapping("/tomodifygroupgoodslist.spmvc")
	public ModelAndView toModifyGroupGoodsList(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView mav = new ModelAndView();
		try {
			String groupCode = request.getParameter("groupCode");
			if (StringUtils.isNotEmpty(groupCode)) {
				GroupGoods groupGoods = promotionService.searchGroupGoodsByGroupCode(groupCode);
				mav.addObject("groupGoods", JSONObject.toJSON(groupGoods));
				List<GroupGoodsList> list = promotionService.searchGroupGoodsListByGroupCode(groupCode);
				mav.addObject("groupGoodsListList", JSONObject.toJSON(list));
			}
			mav.setViewName("promotion/groupgoods-add");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mav;
	}

	@RequestMapping(value = "/upload.spmvc")
	public void uploadFile(@RequestParam MultipartFile myfile, HttpServletRequest request, HttpServletResponse response) {
		String param = request.getParameter("promType");
		logger.debug("上传促销文件：promType = " + param);
		JsonResult jsonResult = new JsonResult();
		InputStream is = null;
		try {
			StringBuffer message = new StringBuffer();
			is = myfile.getInputStream();
			if (StringUtils.isEmpty(param)) {
				jsonResult.setIsok(false);
				jsonResult.setMessage("参数促销类型(promType)丢失!");
				outPrintJson(response, jsonResult);
				return;
			}
			int promType = Integer.valueOf(param);
			if (Constants.PROM_TYPE_0 == promType) {
				String dataType = request.getParameter("dataType");
				if (StringUtils.isEmpty(dataType)) {
					jsonResult.setIsok(false);
					jsonResult.setMessage("参数促销类型(dataType)丢失!");
					outPrintJson(response, jsonResult);
					return;
				}
				if ("1".equals(dataType)) {
					List<PromotionsLimitMoneyVO> list = promotionsReadExcelUtils.readPromotionsLimitMoneyFile(is, message);
					jsonResult.setData(list);
				}
				if ("2".equals(dataType)) {
					List<GiftsGoodsList> list = promotionsReadExcelUtils.readGiftsGoodsListFile(is, message);
					jsonResult.setData(list);
				}
				if (StringUtils.isNotEmpty(message.toString())) {
					jsonResult.setIsok(false);
					jsonResult.setMessage(message.toString());
					outPrintJson(response, jsonResult);
					return;
				}
//				if ("1".equals(dataType)) {
//					List<PromotionsListGoods> list = promotionsReadExcelUtils.readPromotionsListGoodsFile(is);
//					jsonResult.setData(list);
//				}
//				if ("2".equals(dataType)) {
//					List<GiftsGoodsList> list = promotionsReadExcelUtils.readGiftsGoodsListFile(is);
//					jsonResult.setData(list);
//				}
			}
			if (Constants.PROM_TYPE_1 == promType) {
				List<GroupGoodsVO> list = promotionsReadExcelUtils.readGroupGoodsFile(is, message);
				if (StringUtils.isNotEmpty(message.toString())) {
					jsonResult.setIsok(false);
					jsonResult.setMessage("套装商品编码[" + message.toString() + "]已经存在!");
					outPrintJson(response, jsonResult);
					return;
				} else {
					jsonResult = promotionService.uplaodGroupGoods(list);
				}
			}
			if (Constants.PROM_TYPE_2 == promType) { // 买赠
				String dataType = request.getParameter("dataType");
				if (StringUtils.isEmpty(dataType)) {
					jsonResult.setIsok(false);
					jsonResult.setMessage("参数促销类型(dataType)丢失!");
					outPrintJson(response, jsonResult);
					return;
				}
				if ("1".equals(dataType)) {
					List<PromotionsLimitSn> list = promotionsReadExcelUtils.readPromotionsLimitSnFile(is, message);
					jsonResult.setData(list);
				}
				if ("2".equals(dataType)) {
					List<GiftsGoodsList> list = promotionsReadExcelUtils.readGiftsGoodsListFile(is, message);
					jsonResult.setData(list);
				}
				if (StringUtils.isNotEmpty(message.toString())) {
					jsonResult.setIsok(false);
					jsonResult.setMessage(message.toString());
					outPrintJson(response, jsonResult);
					return;
				}
			}
			if (Constants.PROM_TYPE_3 == promType) {
				String dataType = request.getParameter("dataType");
				if (StringUtils.isEmpty(dataType)) {
					jsonResult.setIsok(false);
					jsonResult.setMessage("参数促销类型(dataType)丢失!");
					outPrintJson(response, jsonResult);
					return;
				}
				if ("1".equals(dataType)) {
					List<PromotionsListGoods> list = promotionsReadExcelUtils.readPromotionsListGoodsFile(is);
					jsonResult.setData(list);
				}
				if ("2".equals(dataType)) {
					List<GiftsGoodsList> list = promotionsReadExcelUtils.readGiftsGoodsListFile(is, message);
					jsonResult.setData(list);
				}
				if (StringUtils.isNotEmpty(message.toString())) {
					jsonResult.setIsok(false);
					jsonResult.setMessage(message.toString());
					outPrintJson(response, jsonResult);
					return;
				}
			}
			jsonResult.setMessage("文件上传成功!");
		} catch (Exception e) {
			jsonResult.setIsok(false);
			jsonResult.setMessage("数据格式不正确，请检查文件!");
			logger.error("数据格式不正确，请检查文件!", e);
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					logger.error("关闭文件流异常", e);
				}
			}
		}
		outPrintJson(response, jsonResult);
	}

	
	@RequestMapping("/editPromData")
	public void editPromData(HttpServletRequest request, HttpServletResponse response, byte promType, PromotionEditVO promotionVO) {
		JsonResult jsonResult = new JsonResult();
		jsonResult.setIsok(false);
		try {
			// 促销信息 (满赠 ) 类型
			if (Constants.PROM_TYPE_0 == promType) {
				if (promotionVO != null && StringUtil.isListNotNull(promotionVO.getLimitMoneyVOs())) {
					jsonResult.setData(promotionVO.getLimitMoneyVOs());
				}
			}
			// 促销信息 (买赠 ) 类型
			if (Constants.PROM_TYPE_2 == promType) {
			}

			// 促销信息 (集合赠 ) 类型
			if (Constants.PROM_TYPE_3 == promType) {
			}
			jsonResult.setIsok(true);
		} catch (Exception e) {
			logger.error("编辑促销数据异常", e);
		}
		outPrintJson(response, jsonResult);
	}
	
	/**
	 * 导出赠品列表
	 * @param request
	 * @param response
	 * @param promCode
	 */
	@RequestMapping("/exportGiftsGoods")
	public void exportGiftsGoods(HttpServletRequest request, HttpServletResponse response,@ModelAttribute("promCode") String promCode){
		try{
			if(StringUtil.isNotBlank(promCode)){
				StringBuffer sb = new StringBuffer();
				//头部名称
				sb.append("活动ID,赠品商品编码,赠品商品数量"+"\r\n");
				//数据
				List<GiftsGoodsList> list = promotionService.searchGiftsGoodsListByPromCode(promCode);
				if(list!=null&&list.size()>0){
					for(GiftsGoodsList bean : list){
						sb.append(bean.getPromCode()+","+bean.getGoodsSn()+","+bean.getGiftsSum()+"\r\n");
					}
				}
				//文件名
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
				String now = sdf.format(new Date());
				String fileName = "gift_goods_list_"+now+".csv";
				//导出到文件
				exportCsvFile1(request, response, sb, fileName);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
}

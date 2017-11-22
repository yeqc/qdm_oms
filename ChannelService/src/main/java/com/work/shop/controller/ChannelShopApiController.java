package com.work.shop.controller;

import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSON;
import com.work.shop.api.bean.ApiResultVO;
import com.work.shop.api.bean.ItemAdd;
import com.work.shop.api.bean.ItemQuery;
import com.work.shop.api.bean.ItemUpdate;
import com.work.shop.api.bean.ProdItem;
import com.work.shop.api.service.ChannelApiService;
import com.work.shop.bean.ChannelInfo;
import com.work.shop.bean.ChannelInfoExample;
import com.work.shop.bean.ChannelShop;
import com.work.shop.bean.InterfaceProperties;
import com.work.shop.bean.InterfacePropertiesExample;
import com.work.shop.dao.ChannelInfoMapper;
import com.work.shop.service.ChannelInfoService;
import com.work.shop.service.ChannelShopService;
import com.work.shop.service.InterfacePropertiesService;
import com.work.shop.util.Constants;
import com.work.shop.util.StringUtil;
import com.work.shop.util.SynChannelGoodsExcelUtil;
import com.work.shop.vo.JsonResult;

@Controller
@RequestMapping(value = "channelShopApi")
public class ChannelShopApiController extends BaseController {

	@Resource(name = "channelShopService")
	private ChannelShopService channelShopService;

	@Resource(name = "channelInfoService")
	private ChannelInfoService channelInfoService;

	@Resource(name = "interfacePropertiesService")
	private InterfacePropertiesService interfacePropertiesService;
	
	@Resource
	private SynChannelGoodsExcelUtil synChannelGoodsExcelUtil;
	@Resource(name = "apiService")
	private ChannelApiService apiService;
	@Resource
	private ChannelInfoMapper channelInfoMapper;
	
	
	private static Logger logger = Logger.getLogger(ChannelShopApiController.class);

	/**
	 * 根据渠道类型获取渠道信息列表
	 * 
	 * @param request
	 * @param response
	 * @param channelType
	 */
	@RequestMapping(value = "/findChannelInfoByChannelType.spmvc")
	public void findChannelInfoByChannelType(HttpServletRequest request, HttpServletResponse response, Integer channelType) {
		JsonResult result = new JsonResult();
		if (null == channelType) {
			channelType = 1;
		}
		try {
			result = channelInfoService.findShopChannelInfoByChanneltype(channelType);
		} catch (Exception e) {
			result.setIsok(false);
			result.setMessage("获取渠道信息列表失败：" + e);
		}
		outPrintJson(response, result);
	}

	/**
	 * 根据渠道（店面上级）获得店铺信息
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/findChannelShopByChannelCode.spmvc")
	public void findChannelShopByChannelCode(HttpServletRequest request, HttpServletResponse response, String channelCode) {
		JsonResult result = new JsonResult();
		try {
			result = channelShopService.findShopChannelShopByChannelCode(channelCode);
		} catch (Exception e) {
			result.setIsok(false);
			result.setMessage("获取渠道信息失败：" + e);
		}
		outPrintJson(response, result);
	}

	/**
	 * 根据店铺code，以及类型获取不同种类店铺列表信息
	 * 
	 * @param request
	 * @param response
	 * @param shopCode
	 * @param type
	 *            查询类型 0：自身店铺，1：父店铺，2：子店铺 默认设为0
	 */
	@RequestMapping(value = "findShopInfoByShopCode.spmvc")
	public void findShopInfoByShopCode(HttpServletRequest request, HttpServletResponse response, String shopCode, Integer type) {
		JsonResult jr = new JsonResult();
		try {
			if (null == type) {
				type = 0;
			}
			switch (type) {
			case 0:
				jr = channelShopService.selectCurrentChannelShop(shopCode);
				break;
			case 1:
				jr = channelShopService.selectParentChannelShop(shopCode);
				break;
			case 2:
				jr = channelShopService.selectChildChannelShop(shopCode);
				break;
			default: // 默认为自身店铺信息
				jr = channelShopService.selectCurrentChannelShop(shopCode);
				break;
			}
		} catch (Exception e) {
			jr.setIsok(false);
			jr.setMessage("店铺号[" + shopCode + "]获取店铺信息失败：" + e);
		}
		outPrintJson(response, jr);
	}

	/**
	 * 根据店铺code，获取授权信息
	 * 
	 * @param request
	 * @param response
	 * @param shopCode
	 * @param type
	 *            查询类型 0：自身店铺，1：父店铺，2：子店铺 默认设为0
	 */
	@RequestMapping(value = "securityInfo.spmvc")
	public void getsecurityInfoByShopCode(HttpServletRequest request, HttpServletResponse response, String code) {
		JsonResult result = new JsonResult();
		if (StringUtil.isEmpty(code)) {
			result.setIsok(false);
			result.setMessage("参数[code]不能为空!");
		} else {
			try {
				InterfacePropertiesExample interfacePropertiesExample = new InterfacePropertiesExample();
				interfacePropertiesExample.or().andShopCodeEqualTo(code);
				List<InterfaceProperties> list = interfacePropertiesService.getInterfacePropertiesList(interfacePropertiesExample);
				HashMap<String, String> dataMap = new HashMap<String, String>();
				for (InterfaceProperties interfaceProperties : list) {
					dataMap.put(interfaceProperties.getProName(), interfaceProperties.getProValue());
				}
				interfacePropertiesService.syncSecurityInfo(code);
				result.setIsok(true);
				result.setData(dataMap);
				result.setMessage("success");
			} catch (Exception e) {
				result.setIsok(false);
				result.setMessage("获取店铺[" + code + "]的授权信息异常：" + e);
			}
		}
		outPrintJson(response, result);
	}
	
	@RequestMapping(value = "goodsUpDown")
	public void goodsUpDown(HttpServletResponse response,HttpServletRequest request, String[] goodsSns, String status, String shopCode){
		logger.debug("上架下架同步：goodsSns = " + JSON.toJSONString(goodsSns) + ";status = " + status + ";shopCode=" + shopCode);
		JsonResult result = new JsonResult();
		result.setIsok(false);
		if (StringUtil.isEmpty(shopCode)) {
			result.setMessage("参数[shopCode]不能为空!");
			outPrintJson(response, result);
			return;
		}
		if (!StringUtil.isArrayNotNull(goodsSns)) {
			result.setMessage("参数[goodsSns]不能为空!");
			outPrintJson(response, result);
			return;
		}
		if (StringUtil.isEmpty(status)) {
			result.setMessage("参数[status]不能为空!");
			outPrintJson(response, result);
			return;
		}
		ChannelShop channelShop = channelShopService.selectChannelShopByShopCode(shopCode);
		if (channelShop == null) {
			result.setMessage(shopCode + " 店铺信息不能为空!");
			outPrintJson(response, result);
			return;
		}
		try{
			for (String goodsSn : goodsSns) {
				ItemUpdate itemUpdate = new ItemUpdate();
				itemUpdate.setGoodsSn(goodsSn);
				itemUpdate.setShopCode(shopCode);
				itemUpdate.setChannelCode(channelShop.getChannelCode());
				ApiResultVO apiResultVO = apiService.updatePrice(itemUpdate);
				if (apiResultVO == null) {
					result.setMessage(shopCode + "调用平台接口返回为空!");
					outPrintJson(response, result);
					return;
				}
				if (Constants.API_RETURN_OK.equals(apiResultVO.getCode())) {
					result.setMessage(apiResultVO.getMessage());
					result.setIsok(true);
					outPrintJson(response, result);
					return;
				}
				result.setMessage("success");
				result.setIsok(true);
			}
		} catch(Exception e){
			logger.error(JSON.toJSONString(goodsSns) +  "上架下架同步异常", e);
			result.setIsok(false);
			result.setMessage("上架下架同步异常" + e.getMessage());
		}
		outPrintJson(response, result);
		logger.info("上架下架结束：goodsSns = " + JSON.toJSONString(goodsSns) + ";status = " + status + ";shopCode=" + shopCode);
	}
	
	@RequestMapping(value = "updateGoodsPrice")
	public void updateGoodsPrice(HttpServletResponse response,HttpServletRequest request, String goodsSn, Double price, String shopCode){
		logger.info("商品价格更新：goodsSn = " + goodsSn + ";price = " + price + ";shopCode=" + shopCode);
		JsonResult result = new JsonResult();
		result.setIsok(false);
		if (StringUtil.isEmpty(shopCode)) {
			result.setMessage("参数[shopCode]不能为空!");
			outPrintJson(response, result);
			return;
		}
		if (StringUtil.isEmpty(goodsSn)) {
			result.setMessage("参数[goodsSn]不能为空!");
			outPrintJson(response, result);
			return;
		}
		if (null == price) {
			result.setMessage("参数[price]不能为空!");
			outPrintJson(response, result);
			return;
		}
		ChannelShop channelShop = channelShopService.selectChannelShopByShopCode(shopCode);
		if (channelShop == null) {
			result.setMessage(shopCode + " 店铺信息不能为空!");
			outPrintJson(response, result);
			return;
		}
		try{
			ItemUpdate itemUpdate = new ItemUpdate();
			itemUpdate.setGoodsSn(goodsSn);
			itemUpdate.setShopCode(shopCode);
			itemUpdate.setChannelCode(channelShop.getChannelCode());
			ApiResultVO apiResultVO = apiService.updatePrice(itemUpdate);
			if (apiResultVO == null) {
				result.setMessage(shopCode + "调用平台接口返回为空!");
				outPrintJson(response, result);
				return;
			}
			if (Constants.API_RETURN_OK.equals(apiResultVO.getCode())) {
				result.setMessage(apiResultVO.getMessage());
				result.setIsok(true);
				outPrintJson(response, result);
				return;
			}
			result.setMessage("success");
			result.setIsok(true);
		} catch(Exception e){
			logger.error(goodsSn +  "商品价格同步异常", e);
			result.setIsok(false);
			result.setMessage("商品价格更新异常" + e.getMessage());
		}
		outPrintJson(response, result);
		logger.info("上架下架结束：goodsSn = " + goodsSn + ";price = " + price + ";shopCode=" + shopCode);
	}
	
	@RequestMapping(value = "getBaseData")
	public void getBaseData(HttpServletResponse response,HttpServletRequest request, ItemQuery itemQuery){
		logger.info("商品基础数据查询：itemQuery = " + JSON.toJSONString(itemQuery));
		JsonResult result = new JsonResult();
		result.setIsok(false);
		if (null == itemQuery) {
			result.setMessage("参数[itemQuery]不能为空!");
			outPrintJson(response, result);
			return;
		}
		String channelCode = "YIKE";
		String shopCode = "YIKE";
		ChannelInfoExample example = new ChannelInfoExample();
		example.or().andChanelCodeEqualTo(channelCode);
		List<ChannelInfo> list = channelInfoMapper.selectByExample(example);
		if (!StringUtil.isListNotNull(list)) {
			result.setMessage("YIKE 店铺信息不能为空!");
			outPrintJson(response, result);
			return;
		}
		try{
			ApiResultVO<List<ProdItem>> apiResultVO = apiService.getBaseData(itemQuery, channelCode, shopCode);
			if (apiResultVO == null) {
				result.setMessage(shopCode + "调用平台接口返回为空!");
				outPrintJson(response, result);
				return;
			}
			if (Constants.API_RETURN_OK.equals(apiResultVO.getCode())) {
				result.setMessage(apiResultVO.getMessage());
				result.setData(apiResultVO.getApiGoods());
				result.setIsok(true);
				result.setTotalProperty(apiResultVO.getTotal());
				outPrintJson(response, result);
				return;
			}
			result.setMessage("success");
			result.setIsok(true);
		} catch(Exception e){
			logger.error("商品基础数据查询异常", e);
			result.setIsok(false);
			result.setMessage("商品基础数据查询异异常" + e.getMessage());
		}
		outPrintJson(response, result);
		logger.info("商品基础数据查询异结束：shopCode=" + shopCode);
	}
	
	@RequestMapping(value = "addItem")
	public void addItem(HttpServletResponse response,HttpServletRequest request, ItemAdd itemAdd){
		logger.info("新增商品：itemAdd = " + JSON.toJSONString(itemAdd));
		JsonResult result = new JsonResult();
		result.setIsok(false);
		if (itemAdd == null) {
			result.setMessage("参数[itemAdd]不能为空!");
			outPrintJson(response, result);
			return;
		}
		if (StringUtil.isEmpty(itemAdd.getItemNo())) {
			result.setMessage("参数[itemAdd.itemNo]不能为空!");
			outPrintJson(response, result);
			return;
		}
		if (StringUtil.isEmpty(itemAdd.getShopCode())) {
			result.setMessage("参数[itemAdd.shopCode]不能为空!");
			outPrintJson(response, result);
			return;
		}
		ChannelShop channelShop = channelShopService.selectChannelShopByShopCode(itemAdd.getShopCode());
		if (channelShop == null) {
			result.setMessage(itemAdd.getShopCode() + " 店铺信息不能为空!");
			outPrintJson(response, result);
			return;
		}
		try{
			ApiResultVO apiResultVO = apiService.addItem(itemAdd, channelShop.getChannelCode());
			if (apiResultVO == null) {
				result.setMessage(itemAdd.getShopCode() + "调用平台接口返回为空!");
				outPrintJson(response, result);
				return;
			}
			if (Constants.API_RETURN_OK.equals(apiResultVO.getCode())) {
				result.setMessage(apiResultVO.getMessage());
				result.setIsok(true);
				outPrintJson(response, result);
				return;
			}
			result.setMessage("success");
			result.setIsok(true);
		} catch(Exception e){
			logger.error(itemAdd.getItemNo() +  "新增商品异常", e);
			result.setIsok(false);
			result.setMessage("新增商品更新异常" + e.getMessage());
		}
		outPrintJson(response, result);
		logger.info("新增商品结束：goodsSn = " + itemAdd.getItemNo() + ";shopCode=" + itemAdd.getShopCode());
	}
}
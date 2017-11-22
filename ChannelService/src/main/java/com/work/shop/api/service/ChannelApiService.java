package com.work.shop.api.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.work.shop.api.bean.ApiResultVO;
import com.work.shop.api.bean.ChannelApiGoods;
import com.work.shop.api.bean.ItemAdd;
import com.work.shop.api.bean.ItemAdddetail;
import com.work.shop.api.bean.ItemQuery;
import com.work.shop.api.bean.ItemUpdate;
import com.work.shop.api.bean.LocalItemQuery;
import com.work.shop.api.bean.ProdItem;
import com.work.shop.api.handler.ChannelApiHandler;
import com.work.shop.api.handler.HandlerBuildFactory;
import com.work.shop.bean.ChannelApiLog;
import com.work.shop.bean.ChannelApiLogExample;
import com.work.shop.bean.ChannelGoodsTicket;
import com.work.shop.bean.ChannelGoodsTicketExample;
import com.work.shop.bean.ChannelShop;
import com.work.shop.bean.ChannelShopExample;
import com.work.shop.bean.ChannelStockLog;
import com.work.shop.bean.TicketInfo;
import com.work.shop.bean.TicketInfoExample;
import com.work.shop.bean.mbproduct.ProductChannelGoods;
import com.work.shop.bean.mbproduct.ProductChannelGoodsBarcode;
import com.work.shop.bean.mbproduct.ProductChannelGoodsBarcodeExample;
import com.work.shop.bean.mbproduct.ProductChannelGoodsExample;
import com.work.shop.bean.mbproduct.ProductChannelGoodsPrice;
import com.work.shop.bean.mbproduct.ProductChannelGoodsPriceExample;
import com.work.shop.bean.mbproduct.ProductSellerGoods;
import com.work.shop.bean.mbproduct.ProductSellerGoodsBarcode;
import com.work.shop.bean.mbproduct.ProductSellerGoodsBarcodeExample;
import com.work.shop.bean.mbproduct.ProductSellerGoodsExample;
import com.work.shop.dao.ChannelApiLogMapper;
import com.work.shop.dao.ChannelGoodsExtensionMapper;
import com.work.shop.dao.ChannelGoodsTicketMapper;
import com.work.shop.dao.ChannelShopMapper;
import com.work.shop.dao.ChannelStockLogMapper;
import com.work.shop.dao.TaoBaoO2OShopMapper;
import com.work.shop.dao.TicketInfoMapper;
import com.work.shop.dao.mbproduct.ProductChannelGoodsBarcodeMapper;
import com.work.shop.dao.mbproduct.ProductChannelGoodsMapper;
import com.work.shop.dao.mbproduct.ProductChannelGoodsPriceMapper;
import com.work.shop.dao.mbproduct.ProductSellerGoodsBarcodeMapper;
import com.work.shop.dao.mbproduct.ProductSellerGoodsMapper;
import com.work.shop.oms.utils.StringUtil;
import com.work.shop.redis.RedisClient;
import com.work.shop.service.ShopGoodsService;
import com.work.shop.util.Constants;
import com.work.shop.util.ExceptionStackTraceUtil;
import com.work.shop.vo.JsonResult;
import com.work.shop.vo.QueueManagerVo;

@Service("apiService")
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ChannelApiService extends ApiService {
	private Logger log = Logger.getLogger(ChannelApiService.class);

	@Resource
	private HandlerBuildFactory factory;
	@Resource
	private ChannelGoodsTicketMapper channelGoodsTicketMapper;
	@Resource
	private TicketInfoMapper ticketInfoMapper;
	@Resource
	private ChannelApiLogMapper channelApiLogMapper;
//	@Resource
//	private ChannelGoodsMapper channelGoodsMapper;
	@Resource
	private ChannelShopMapper channelShopMapper;
	@Resource
	private ChannelGoodsExtensionMapper channelGoodsExtensionMapper;
	@Resource
	TaoBaoO2OShopMapper taoBaoO2OShopMapper;
	@Resource
	ShopGoodsService shopGoodsService;
	@Resource
	RedisClient redisClient;
	@Resource
	private ChannelStockLogMapper channelStockLogMapper;
	@Resource
	private ProductSellerGoodsBarcodeMapper productSellerGoodsBarcodeMapper;
	@Resource
	private ProductSellerGoodsMapper productSellerGoodsMapper;
	@Resource
	private ProductChannelGoodsMapper productChannelGoodsMapper;
	@Resource
	private ProductChannelGoodsBarcodeMapper productChannelGoodsBarcodeMapper;
	@Resource
	private ProductChannelGoodsPriceMapper productChannelGoodsPriceMapper;
	
	@Override
	public JsonResult ticketDisposal(QueueManagerVo managerVo) {
		log.debug("执行MQ任务调整单：managerVo=" + JSON.toJSONString(managerVo));
		TicketInfo ticketInfo = managerVo.getTicketInfo();
		String userName = managerVo.getOperUser();
		JsonResult jsonResult = new JsonResult();
		try {
			if (ticketInfo == null) {
				log.error("调整单商品信息为空！");
				return jsonResult;
			}
			byte ticketType = managerVo.getTicketType();
			String channelCode = managerVo.getChannelCode();
			String shopCode = managerVo.getShopCode();
			String ticketCode = ticketInfo.getTicketCode();
			ChannelGoodsTicket goodsTicket = channelGoodsTicketMapper.selectByPrimaryKey(managerVo.getTicketId());
			// 调整单状态不是已审核 或 已经同步过的数据不做处理
			if ((!"1".equals(goodsTicket.getTicketStatus())) || (goodsTicket.getIsSync() != null && goodsTicket.getIsSync() == 1)) {
				log.error("调整单状态不是已审核 或 已经同步过的数据不做处理");
				jsonResult.setMessage("调整单状态不是已审核 或 已经同步过的数据不做处理");
				return jsonResult;
			}
			if (StringUtils.isEmpty(channelCode) || StringUtils.isEmpty(shopCode) || StringUtils.isEmpty(ticketCode)) {
				jsonResult.setIsok(false);
				jsonResult.setMessage("参数丢失!");
				return jsonResult;
			}
			try {// 卖点字段存储运费模板ID
				disposal(channelCode, shopCode, ticketInfo, ticketType, userName, ticketInfo.getSellingInfo());
			} catch (Exception e) {
				log.error("调整单:" + ticketCode + "处理异常! " + ExceptionStackTraceUtil.getExceptionTrace(e));
			}
			TicketInfoExample ticketInfoExample = new TicketInfoExample();
			ticketInfoExample.or().andIsExcutedEqualTo((byte)0).andTicketCodeEqualTo(ticketInfo.getTicketCode());
			int excutedCount = ticketInfoMapper.countByExample(ticketInfoExample);
			int ticketSize = managerVo.getTicketSize();
			//已成功执行的数量与调整单商品数量相等时，调整单执行
			if(excutedCount == ticketSize) {
				ChannelApiLogExample apiLogExample = new ChannelApiLogExample();
				apiLogExample.or().andParamInfoLike(ticketCode.toUpperCase() + "%").andReturnCodeEqualTo("1");
				int logCount = channelApiLogMapper.countByExample(apiLogExample);
				ChannelGoodsTicket ticket = new ChannelGoodsTicket();
				ticket.setExecTime(new Date());
				ticket.setIsSync((byte) 1);// 1已经同步 0未同步',
				ticket.setTicketStatus("3");// 3‘已经执行’
				if (logCount == ticketSize) {
					ticket.setNote("执行成功");
				} else if (logCount == 0) {
					ticket.setNote("执行失败");
				} else {
					ticket.setNote("部分执行失败");
				}
				ChannelGoodsTicketExample ticketExample = new ChannelGoodsTicketExample();
				ticketExample.or().andTicketCodeEqualTo(ticketCode);
				int count = channelGoodsTicketMapper.updateByExampleSelective(ticket, ticketExample);
				if (count != 1) {
					log.error("调整单:" + ticketCode + ",状态回写失败!");
				}
			}
			log.info("执行MQ任务调整单：ticketCode=" + ticketCode + ";goodsSn=" + ticketInfo.getGoodsSn() + "结束");
		} catch (Exception e) {
			log.error("调整单处理异常! " + ExceptionStackTraceUtil.getExceptionTrace(e));
			jsonResult.setIsok(false);
			jsonResult.setMessage("调整单同步失败!");
			return jsonResult;
		}
		return jsonResult;
	}

	private int disposal(String channelCode, String shopCode, TicketInfo ticketInfo, byte ticketType, String userName, String postageId) {
		Map<String, String> secretInforMap = getSecretInfo(channelCode, shopCode);
		ChannelApiHandler handler = factory.createrChannelApi(secretInforMap);
		ChannelApiLog apiLog = new ChannelApiLog();
		apiLog.setChannelCode(channelCode);
		apiLog.setShopCode(shopCode);
		apiLog.setRequestTime(new Date());
		apiLog.setUser(userName);
		// 单间调整单商品在日志表中的调整单CODE 商品调整单号 + 商品编号
		String goodsSnTicketCode = ticketInfo.getTicketCode() + "-" + ticketInfo.getGoodsSn();
		// 调整单号
		apiLog.setParamInfo(goodsSnTicketCode);
		apiLog.setMethodName(String.valueOf(ticketType)); // 调整单类型
		if (handler == null) {
			apiLog.setReturnCode("1");
			apiLog.setReturnMessage("接口实例化失败! ");
			log.error("channelCode="+channelCode+ ";shopCode" + shopCode + "店铺接口实例化失败! ");
			channelApiLogMapper.insertSelective(apiLog);
			return -2;
		}
		int returnFlag = 0;
		ApiResultVO result = new ApiResultVO();
		try {
			ItemUpdate itemUpdate = new ItemUpdate();
			itemUpdate.setGoodsSn(ticketInfo.getGoodsSn());
			itemUpdate.setShopCode(shopCode);
			itemUpdate.setChannelCode(channelCode);
			if (ticketType == 0) { // 0:修改价格
				itemUpdate.setPriceType(0);
				itemUpdate.setItemFieldValue(ticketInfo.getNewPrice() + "");
				result = updatePrice(itemUpdate);
			} else if (ticketType == 1) {// 1:上下架维护
				itemUpdate.setItemFieldValue(ticketInfo.getOnSellStatus());
				result = shelvesUpOrDown(itemUpdate);
			} else if (ticketType == 2) {// 2:店铺添加商品
				itemUpdate.setItemFieldValue(ticketInfo.getOnSellStatus());
				ItemAdd itemAdd = new ItemAdd();
				itemAdd.setIsOnSale(false);
				itemAdd.setItemNo(ticketInfo.getGoodsSn());
				itemAdd.setShopCode(shopCode);
				result = addItem(itemAdd, channelCode);
			}
			apiLog.setReturnCode(result.getCode());
			apiLog.setReturnMessage(result.getMessage());
			insertApiLog(apiLog);
			result.setMessage("调整单：" + goodsSnTicketCode + "执行信息：" + result.getMessage());
			ticketInfo.setIsExcuted((byte)0);
			ticketInfoMapper.updateByPrimaryKeySelective(ticketInfo);
		} catch (Exception e) {
			result.setCode("1");
			result.setMessage("调整单：" + goodsSnTicketCode + " 数据同步失败!" + ExceptionStackTraceUtil.getExceptionTrace(e));
		}
		return returnFlag;
	}

	/**
	 * 检索商品信息(6位码不包含11位码信息)
	 */
	@Override
	public ApiResultVO<List<ChannelApiGoods>> searchItemPage(LocalItemQuery query) {
		ApiResultVO<List<ChannelApiGoods>> result = new ApiResultVO<List<ChannelApiGoods>>(Constants.API_RETURN_NO, "店铺查询商品失败");
		if (query == null) {
			result.setMessage("query is null");
			return result;
		}
		String shopCode = query.getShopCode();
		if (StringUtil.isTrimEmpty(shopCode)) {
			result.setMessage("shopCode is empty");
			return result;
		}
		String channelCode = query.getChannelCode();
		if (StringUtil.isTrimEmpty(channelCode)) {
			result.setMessage("channelCode is empty");
			return result;
		}
		/*String itemNo = query.getItemNo();
		if (StringUtil.isTrimEmpty(itemNo)) {
			result.setMessage("itemNo is empty");
			return result;
		}*/
		ChannelShopExample example = new ChannelShopExample();
		example.or().andShopCodeEqualTo(shopCode);
		List<ChannelShop> channelShops = channelShopMapper.selectByExample(example);
		if (channelShops == null || channelShops.size() != 1) {
			result.setCode("-1");
			result.setMessage("店铺(" + shopCode + ")不存在!");
			return result;
		}
		String shopTitle = channelShops.get(0).getShopTitle();
		query.setShopTitle(shopTitle);
		ChannelApiHandler handler = getApiHandler(channelCode, shopCode);
		// 日志信息
		try {
			if (handler.getSecretInforMap() != null && !handler.getSecretInforMap().isEmpty()) {
				Map<String, String> map = handler.getSecretInforMap();
				channelCode = map.get(Constants.CHANNEL_CODE);
				shopCode = map.get(Constants.SHOP_CODE);
			}
			result = handler.searchItemPage(query);
			if (!Constants.API_RETURN_OK.equals(result.getCode())) {
				for (int i = 1; i <= 2; i++) {
					log.info("获取线上数据失败,重新获取次数：" + i);
					result = handler.searchItemPage(query);
					if (Constants.API_RETURN_OK.equals(result.getCode())) {
						break;
					}
				}
			}
		} catch (Exception e) {
			log.error("店铺查询商品异常" + e.getMessage(), e);
		}
		return result;
	}

	@Override
	public ApiResultVO updateItemStock(ItemUpdate itemUpdate) {
		ApiResultVO result = new ApiResultVO(Constants.API_RETURN_NO, "更新价格失败");
		if (itemUpdate == null) {
			result.setMessage("itemUpdate is null");
			return result;
		}
		String shopCode = itemUpdate.getShopCode();
		if (StringUtil.isTrimEmpty(shopCode)) {
			result.setMessage("shopCode is empty");
			return result;
		}
		String channelCode = itemUpdate.getChannelCode();
		if (StringUtil.isTrimEmpty(channelCode)) {
			result.setMessage("channelCode is empty");
			return result;
		}
		String skuSn = itemUpdate.getSkuSn();
		if (StringUtil.isTrimEmpty(skuSn)) {
			result.setMessage("skuSn is empty");
			return result;
		}
		Integer stockCount = itemUpdate.getStockCount();
		if (stockCount == null) {
			result.setMessage("stockCount is null");
			return result;
		}
		ChannelApiHandler handler = getApiHandler(channelCode, shopCode);
		// 日志信息
		try {
			if (handler.getSecretInforMap() != null && !handler.getSecretInforMap().isEmpty()) {
				Map<String, String> map = handler.getSecretInforMap();
				channelCode = map.get(Constants.CHANNEL_CODE);
				shopCode = map.get(Constants.SHOP_CODE);
			}
			log.info("同步库存调整:[shopCode : " + shopCode + ", skuSn : " + skuSn + "] 开始!");
			// 价格调整
			for (int j = 0; j < 3; j++) {
				result = handler.updateItemStock(itemUpdate);
				if (Constants.API_RETURN_OK.equals(result.getCode())) {
					break;
				}
			}
			Date date = new Date();
			ChannelStockLog stockLog = new ChannelStockLog();
			stockLog.setChannelCode(channelCode);
			stockLog.setShopCode(shopCode);
			stockLog.setRequestTime(date);
			stockLog.setSkuSn(skuSn);
			stockLog.setStock(stockCount);
			stockLog.setReturnCode(result.getCode());
			stockLog.setReturnMessage(result.getMessage());
			stockLog.setUser("system");
			insertStockLog(stockLog);
			log.info("修改价格: [shopCode : " + shopCode + ", goodsSn : " + skuSn + "] 结束!");
		} catch (Exception e) {
			result.setCode("1");
			result.setMessage(skuSn + ":同步库存异常!" + e.getMessage());
			log.error("goodsSn=" + skuSn + "同步库存异常"+ e.getMessage(), e);
		}
		return result;
	}

	@Override
	public ApiResultVO updatePrice(ItemUpdate itemUpdate) {
		ApiResultVO result = new ApiResultVO(Constants.API_RETURN_NO, "更新价格失败");
		if (itemUpdate == null) {
			result.setMessage("itemUpdate is null");
			return result;
		}
		String shopCode = itemUpdate.getShopCode();
		if (StringUtil.isTrimEmpty(shopCode)) {
			result.setMessage("shopCode is empty");
			return result;
		}
		String channelCode = itemUpdate.getChannelCode();
		if (StringUtil.isTrimEmpty(channelCode)) {
			result.setMessage("channelCode is empty");
			return result;
		}
		String goodsSn = itemUpdate.getGoodsSn();
		if (StringUtil.isTrimEmpty(channelCode)) {
			result.setMessage("goodsSn is empty");
			return result;
		}
		String itemFieldValue = itemUpdate.getItemFieldValue();
		if (StringUtil.isTrimEmpty(itemFieldValue)) {
			result.setMessage("itemFieldValue is empty");
			return result;
		}
		ProductSellerGoodsExample goodsExample = new ProductSellerGoodsExample();
		goodsExample.or().andProductCodeEqualTo(goodsSn);
		List<ProductSellerGoods> list = productSellerGoodsMapper.selectByExample(goodsExample);
		if (StringUtil.isListNull(list)) {
			result.setMessage("goodsSn" + goodsSn + "基础数据本地不存在！");
			return result;
		}
		ProductSellerGoods sellerGoods = list.get(0);
		ChannelApiHandler handler = getApiHandler(channelCode, shopCode);
		// 日志信息
		try {
			if (handler.getSecretInforMap() != null && !handler.getSecretInforMap().isEmpty()) {
				Map<String, String> map = handler.getSecretInforMap();
				channelCode = map.get(Constants.CHANNEL_CODE);
				shopCode = map.get(Constants.SHOP_CODE);
			}
			log.info("修改价格调整:[shopCode : " + shopCode + ", goodsSn : " + goodsSn + "] 开始!");
			// 价格调整
			for (int j = 0; j < 3; j++) {
				result = handler.updatePrice(itemUpdate);
				if (Constants.API_RETURN_OK.equals(result.getCode())) {
					updateChannelGoodsPrice(goodsSn, sellerGoods, shopCode, Double.valueOf(itemFieldValue));
					break;
				}
			}
			log.info("修改价格: [shopCode : " + shopCode + ", goodsSn : " + goodsSn + "] 结束!");
		} catch (Exception e) {
			result.setCode("1");
			result.setMessage(goodsSn + ":修改价格异常!" + e.getMessage());
			log.error("goodsSn=" + goodsSn + "修改价格异常"+ e.getMessage(), e);
		}
		return result;
	}

	@Override
	public ApiResultVO shelvesUpOrDown(ItemUpdate itemUpdate) {
		ApiResultVO result = new ApiResultVO(Constants.API_RETURN_NO, "上架下架失败");
		if (itemUpdate == null) {
			result.setMessage("itemUpdate is null");
			return result;
		}
		String shopCode = itemUpdate.getShopCode();
		if (StringUtil.isTrimEmpty(shopCode)) {
			result.setMessage("shopCode is empty");
			return result;
		}
		String channelCode = itemUpdate.getChannelCode();
		if (StringUtil.isTrimEmpty(channelCode)) {
			result.setMessage("channelCode is empty");
			return result;
		}
		String goodsSn = itemUpdate.getGoodsSn();
		if (StringUtil.isTrimEmpty(channelCode)) {
			result.setMessage("goodsSn is empty");
			return result;
		}
		String itemFieldValue = itemUpdate.getItemFieldValue();
		if (StringUtil.isTrimEmpty(itemFieldValue)) {
			result.setMessage("itemFieldValue is empty");
			return result;
		}
		ProductSellerGoodsExample goodsExample = new ProductSellerGoodsExample();
		goodsExample.or().andProductCodeEqualTo(goodsSn);
		List<ProductSellerGoods> list = productSellerGoodsMapper.selectByExample(goodsExample);
		if (StringUtil.isListNull(list)) {
			result.setMessage("goodsSn" + goodsSn + "基础数据本地不存在！");
			return result;
		}
		ProductSellerGoods sellerGoods = list.get(0);
		ChannelApiHandler handler = getApiHandler(channelCode, shopCode);
		// 日志信息
		try {
			if (handler.getSecretInforMap() != null && !handler.getSecretInforMap().isEmpty()) {
				Map<String, String> map = handler.getSecretInforMap();
				channelCode = map.get(Constants.CHANNEL_CODE);
				shopCode = map.get(Constants.SHOP_CODE);
			}
			log.info("上下架调整:[shopCode : " + shopCode + ", goodsSn : " + goodsSn + "] 开始!");
			// 上架下架调整
			for (int j = 0; j < 3; j++) {
				result = handler.shelvesUpOrDown(itemUpdate);
				if (Constants.API_RETURN_OK.equals(result.getCode())) {
					Integer status = 1;
					if (itemFieldValue.equals("0")) {
						status = 2;
					}
					updateChannelGoodsStatus(goodsSn, sellerGoods, shopCode, status);
					break;
				}
			}
			log.info("上下架调整: [shopCode : " + shopCode + ", goodsSn : " + goodsSn + "] 结束!");
		} catch (Exception e) {
			result.setCode("1");
			result.setMessage(goodsSn + ":上下架异常!" + e.getMessage());
			log.error("goodsSn=" + goodsSn + "上下架异常"+ e.getMessage(), e);
		}
		return result;
	}

	@Override
	public ApiResultVO<List<ProdItem>> getBaseData(ItemQuery itemQuery, String channelCode, String shopCode) {
		ApiResultVO<List<ProdItem>> result = new ApiResultVO<List<ProdItem>>(Constants.API_RETURN_NO, "查询商品基础数据失败");
		if (itemQuery == null) {
			result.setMessage("itemQuery is null");
			return result;
		}
		Integer pageIndex = itemQuery.getPageIndex();
		if (pageIndex == null) {
			result.setMessage("pageIndex is null");
			return result;
		}
		if (StringUtil.isTrimEmpty(channelCode)) {
			result.setMessage("channelCode is empty");
			return result;
		}
		ChannelApiHandler handler = getApiHandler(channelCode, shopCode);
		// 日志信息
		try {
			log.info("查询商品基础数据:[channelCode : " + channelCode + "] 开始!");
			// 上架下架调整
			for (int j = 0; j < 3; j++) {
				result = handler.getBaseData(itemQuery);
				if (Constants.API_RETURN_OK.equals(result.getCode())) {
					break;
				}
			}
			log.info("查询商品基础数据: [channelCode : " + channelCode + "]结束!");
		} catch (Exception e) {
			result.setCode("1");
			result.setMessage("查询商品基础数据异常!" + e.getMessage());
			log.error("查询商品基础数据异常"+ e.getMessage(), e);
		}
		return result;
	}

	@Override
	public ApiResultVO addItem(ItemAdd itemAdd, String channelCode) {
		ApiResultVO result = new ApiResultVO(Constants.API_RETURN_NO, "店铺新增商品失败");
		if (itemAdd == null) {
			result.setMessage("itemAdd is null");
			return result;
		}
		String shopCode = itemAdd.getShopCode();
		if (StringUtil.isTrimEmpty(shopCode)) {
			result.setMessage("shopCode is empty");
			return result;
		}
		String itemNo = itemAdd.getItemNo();
		if (StringUtil.isTrimEmpty(itemNo)) {
			result.setMessage("itemNo is empty");
			return result;
		}
		ProductSellerGoodsExample goodsExample = new ProductSellerGoodsExample();
		goodsExample.or().andProductCodeEqualTo(itemNo);
		List<ProductSellerGoods> list = productSellerGoodsMapper.selectByExample(goodsExample);
		if (StringUtil.isListNull(list)) {
			result.setMessage("itemNo" + itemNo + "基础数据本地不存在！");
			return result;
		}
		ProductSellerGoods sellerGoods = list.get(0);
		itemAdd.setRetailPrice(sellerGoods.getMarketPrice().doubleValue());
		itemAdd.setSalePrice(sellerGoods.getProtectPrice().doubleValue());
		ProductSellerGoodsBarcodeExample example = new ProductSellerGoodsBarcodeExample();
		example.or().andProductSysCodeEqualTo(sellerGoods.getProductSysCode());
		List<ProductSellerGoodsBarcode> barcodes = productSellerGoodsBarcodeMapper.selectByExample(example);
		if (StringUtil.isListNull(barcodes)) {
			result.setMessage("itemNo" + itemNo + "基础barcode数据本地不存在！");
			return result;
		}
		List<ItemAdddetail> itemAdddetails = new ArrayList<ItemAdddetail>();
		for (ProductSellerGoodsBarcode barcode : barcodes) {
			ItemAdddetail adddetail = new ItemAdddetail();
			adddetail.setBarCode(barcode.getBarcodeCode());
			adddetail.setRetailPrice(sellerGoods.getMarketPrice().doubleValue());
			adddetail.setSalePrice(sellerGoods.getMarketPrice().doubleValue());
			adddetail.setStocks(0);
			itemAdddetails.add(adddetail);
		}
		itemAdd.setDetailItems(itemAdddetails);
		ChannelApiHandler handler = getApiHandler(channelCode, shopCode);
		// 日志信息
		try {
			if (handler.getSecretInforMap() != null && !handler.getSecretInforMap().isEmpty()) {
				Map<String, String> map = handler.getSecretInforMap();
				channelCode = map.get(Constants.CHANNEL_CODE);
				shopCode = map.get(Constants.SHOP_CODE);
			}
			log.info("店铺新增商品:[shopCode : " + shopCode + ", itemNo : " + itemNo + "] 开始!");
			// 上架下架调整
			for (int j = 0; j < 3; j++) {
				result = handler.addItem(itemAdd);
				if (Constants.API_RETURN_OK.equals(result.getCode())) {
					// 保存至渠道表
					insertChannelGoods(itemNo, sellerGoods, barcodes, shopCode);
					break;
				}
			}
			log.info("店铺新增商品: [shopCode : " + shopCode + ", itemNo : " + itemNo + "] 结束!");
		} catch (Exception e) {
			result.setCode("1");
			result.setMessage(itemNo + ":店铺新增商品异常!" + e.getMessage());
			log.error("itemNo=" + itemNo + "店铺新增商品异常"+ e.getMessage(), e);
		}
		return result;
	}

	private ChannelApiHandler getApiHandler(String channelCode, String shopCode) {
		Map<String, String> secretInforMap = getSecretInfo(channelCode, shopCode);
		ChannelApiHandler handler = factory.createrChannelApi(secretInforMap);
		if (handler == null) {
			ChannelApiLog apiLog = new ChannelApiLog();
			apiLog.setReturnCode("1");
			apiLog.setReturnMessage("接口实例化失败! ");
			log.error("channelCode="+channelCode+ ";shopCode" + shopCode + "店铺接口实例化失败! ");
			channelApiLogMapper.insertSelective(apiLog);
			return null;
		}
		return handler;
	}
	
	/**
	 * 添加单操作日志记录
	 * @param apiLog
	 */
	private void insertApiLog(ChannelApiLog apiLog) {
		if (apiLog == null) {
			log.error("添加单操作日志记录信息为空！");
			return ;
		}
		// 日志插入
		log.debug(apiLog.getReturnMessage());
		try {
			if (StringUtil.isNotEmpty(apiLog.getReturnMessage()) && apiLog.getReturnMessage().length() > 1000) {
				apiLog.setReturnMessage(apiLog.getReturnMessage().substring(0, 1000));
			}
			channelApiLogMapper.insertSelective(apiLog);
		} catch (Exception e) {
			log.error("ticketCode=" + apiLog.getParamInfo() + "日志插入异常" + ExceptionStackTraceUtil.getExceptionTrace(e));
		}
	}
	
	/**
	 * 添加单操作日志记录
	 * @param apiLog
	 */
	private void insertStockLog(ChannelStockLog stockLog) {
		if (stockLog == null) {
			log.error("添加单操作日志记录信息为空！");
			return ;
		}
		// 日志插入
		try {
			if (StringUtil.isNotEmpty(stockLog.getReturnMessage()) && stockLog.getReturnMessage().length() > 1000) {
				stockLog.setReturnMessage(stockLog.getReturnMessage().substring(0, 1000));
			}
			channelStockLogMapper.insertSelective(stockLog);
		} catch (Exception e) {
			log.error("skuSn=" + stockLog.getSkuSn() + "日志插入异常" + e.getMessage(), e);
		}
	}
	
	private void insertChannelGoods(String goodsSn, ProductSellerGoods sellerGoods, List<ProductSellerGoodsBarcode> barcodes, String shopCode){
		ProductChannelGoodsExample channelGoodsExample = new ProductChannelGoodsExample();
		channelGoodsExample.or().andProductSysCodeEqualTo(sellerGoods.getProductSysCode()).andChannelCodeEqualTo(shopCode);
		List<ProductChannelGoods> channelGoods = productChannelGoodsMapper.selectByExample(channelGoodsExample);
		ProductChannelGoods productChannelGoods = new ProductChannelGoods();
		productChannelGoods.setSellerCode(sellerGoods.getSellerCode());
		productChannelGoods.setProductSysCode(sellerGoods.getProductSysCode());
		productChannelGoods.setProductName(sellerGoods.getProductName());
		productChannelGoods.setStatus((byte) 2);
		productChannelGoods.setChannelCode(shopCode);
		if (StringUtil.isListNull(channelGoods)) {
			productChannelGoods.setCreateDate(new Date());
			productChannelGoods.setCreateUser("system");
			productChannelGoodsMapper.insertSelective(productChannelGoods);
		} else {
			productChannelGoods.setLastUpdateDate(new Date());
			productChannelGoods.setLastUpdateUser("system");
			productChannelGoods.setId(channelGoods.get(0).getId());
			productChannelGoodsMapper.updateByPrimaryKeySelective(productChannelGoods);
		}
		for (ProductSellerGoodsBarcode barcode : barcodes) {
			ProductChannelGoodsBarcodeExample goodsBarcodeExample = new ProductChannelGoodsBarcodeExample();
			goodsBarcodeExample.or().andProductSysCodeEqualTo(sellerGoods.getProductSysCode())
				.andBarcodeSysCodeEqualTo(barcode.getBarcodeSysCode()).andChannelCodeEqualTo(shopCode);
			List<ProductChannelGoodsBarcode> goodsBarcodes = productChannelGoodsBarcodeMapper.selectByExample(goodsBarcodeExample);
			ProductChannelGoodsBarcode channelGoodsBarcode = new ProductChannelGoodsBarcode();
			channelGoodsBarcode.setBarcodeSysCode(barcode.getBarcodeSysCode());
			channelGoodsBarcode.setProductSysCode(barcode.getProductSysCode());
			channelGoodsBarcode.setChannelCode(shopCode);
			channelGoodsBarcode.setSellerCode(barcode.getSellerCode());
			if (StringUtil.isListNull(goodsBarcodes)) {
				channelGoodsBarcode.setCreateDate(new Date());
				channelGoodsBarcode.setCreateUser("system");
				productChannelGoodsBarcodeMapper.insert(channelGoodsBarcode);
			} else {
				channelGoodsBarcode.setLastUpdateDate(new Date());
				channelGoodsBarcode.setLastUpdateUser("system");
				channelGoodsBarcode.setId(goodsBarcodes.get(0).getId());
				productChannelGoodsBarcodeMapper.updateByPrimaryKeySelective(channelGoodsBarcode);
			}
		}
	}
	
	private void updateChannelGoodsStatus(String goodsSn, ProductSellerGoods sellerGoods, String shopCode,Integer status){
		ProductChannelGoodsExample channelGoodsExample = new ProductChannelGoodsExample();
		channelGoodsExample.or().andProductSysCodeEqualTo(sellerGoods.getProductSysCode()).andChannelCodeEqualTo(shopCode);
		List<ProductChannelGoods> channelGoods = productChannelGoodsMapper.selectByExample(channelGoodsExample);
		ProductChannelGoods productChannelGoods = new ProductChannelGoods();
		productChannelGoods.setSellerCode(sellerGoods.getSellerCode());
		productChannelGoods.setProductSysCode(sellerGoods.getProductSysCode());
		productChannelGoods.setProductName(sellerGoods.getProductName());
		if (status != null) {
			productChannelGoods.setStatus(status.byteValue());
		}
		productChannelGoods.setChannelCode(shopCode);
		if (StringUtil.isListNull(channelGoods)) {
			productChannelGoods.setCreateDate(new Date());
			productChannelGoods.setCreateUser("system");
			productChannelGoodsMapper.insertSelective(productChannelGoods);
		} else {
			productChannelGoods.setLastUpdateDate(new Date());
			productChannelGoods.setLastUpdateUser("system");
			productChannelGoods.setId(channelGoods.get(0).getId());
			productChannelGoodsMapper.updateByPrimaryKeySelective(productChannelGoods);
		}
	}
	
	private void updateChannelGoodsPrice(String goodsSn, ProductSellerGoods sellerGoods, String shopCode,Double price){
		ProductChannelGoodsExample channelGoodsExample = new ProductChannelGoodsExample();
		channelGoodsExample.or().andProductSysCodeEqualTo(sellerGoods.getProductSysCode()).andChannelCodeEqualTo(shopCode);
		List<ProductChannelGoods> channelGoods = productChannelGoodsMapper.selectByExample(channelGoodsExample);
		ProductChannelGoods productChannelGoods = new ProductChannelGoods();
		productChannelGoods.setSellerCode(sellerGoods.getSellerCode());
		productChannelGoods.setProductSysCode(sellerGoods.getProductSysCode());
		productChannelGoods.setProductName(sellerGoods.getProductName());
		productChannelGoods.setProtectPrice(new BigDecimal(price));
		productChannelGoods.setChannelCode(shopCode);
		if (StringUtil.isListNull(channelGoods)) {
			productChannelGoods.setCreateDate(new Date());
			productChannelGoods.setCreateUser("system");
			productChannelGoodsMapper.insertSelective(productChannelGoods);
		} else {
			productChannelGoods.setLastUpdateDate(new Date());
			productChannelGoods.setLastUpdateUser("system");
			productChannelGoods.setId(channelGoods.get(0).getId());
			productChannelGoodsMapper.updateByPrimaryKeySelective(productChannelGoods);
		}
		
		
		//添加到productChannelGoodsPrice表
				ProductChannelGoodsPriceExample channelGoodsPriceExample = new ProductChannelGoodsPriceExample();
				channelGoodsPriceExample.or().andProductSysCodeEqualTo(sellerGoods.getProductSysCode()).andChannelCodeEqualTo(shopCode);
				List <ProductChannelGoodsPrice> channelGoodsPrices = productChannelGoodsPriceMapper.selectByExample(channelGoodsPriceExample);
				ProductChannelGoodsPrice productChannelGoodsPrice = new ProductChannelGoodsPrice();	
				ProductSellerGoodsBarcodeExample productSellerGoodsBarcodeExample = new ProductSellerGoodsBarcodeExample();
				productSellerGoodsBarcodeExample.or().andProductSysCodeEqualTo(sellerGoods.getProductSysCode());
				List <ProductSellerGoodsBarcode> productSellerGoodsBarcodes = productSellerGoodsBarcodeMapper.selectByExample(productSellerGoodsBarcodeExample);
				for (ProductSellerGoodsBarcode productSellerGoodsBarcode : productSellerGoodsBarcodes) {
						productChannelGoodsPrice.setSellerCode(sellerGoods.getSellerCode());
						productChannelGoodsPrice.setProductSysCode(sellerGoods.getProductSysCode());
						productChannelGoodsPrice.setBarcodeSysCode(productSellerGoodsBarcode.getBarcodeSysCode());
						productChannelGoodsPrice.setSaleAttr1ValueCode(productSellerGoodsBarcode.getSaleAttr1ValueCode());
						productChannelGoodsPrice.setSaleAttr2ValueCode(productSellerGoodsBarcode.getSaleAttr2ValueCode());
						productChannelGoodsPrice.setStatus((byte) 1);
						productChannelGoodsPrice.setSpecPrice(new BigDecimal(price));
						productChannelGoodsPrice.setChannelCode(shopCode);
					}
				if(StringUtil.isListNull(channelGoodsPrices)){
					productChannelGoodsPrice.setCreateDate(new Date());
					productChannelGoodsPrice.setCreateUser("system");
					productChannelGoodsPriceMapper.insert(productChannelGoodsPrice);
				}else{
					productChannelGoodsPrice.setLastUpdateDate(new Date());
					productChannelGoodsPrice.setLastUpdateUser("system");
					productChannelGoodsPrice.setId(channelGoodsPrices.get(0).getId());
					productChannelGoodsPriceMapper.updateByPrimaryKeySelective(productChannelGoodsPrice);
				}
	}
}

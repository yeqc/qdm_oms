package com.work.shop.api.handler;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.http.params.CoreConnectionPNames;
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
import com.work.shop.api.bean.YkApiResult;
import com.work.shop.bean.mbproduct.ProductSellerGoods;
import com.work.shop.bean.mbproduct.ProductSellerGoodsBarcode;
import com.work.shop.bean.mbproduct.ProductSellerGoodsBarcodeExample;
import com.work.shop.bean.mbproduct.ProductSellerGoodsExample;
import com.work.shop.dao.mbproduct.MBProductSellerGoodsSelectMapper;
import com.work.shop.dao.mbproduct.ProductSellerGoodsBarcodeMapper;
import com.work.shop.dao.mbproduct.ProductSellerGoodsMapper;
import com.work.shop.oms.utils.StringUtil;
import com.work.shop.oms.utils.TimeUtil;
import com.work.shop.util.Constants;
import com.work.shop.util.MD5;
import com.work.shop.util.Sha1;

@Service("yikeApiHandler")
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class YikeApiHandler extends ChannelApiHandler {

	private Logger logger = Logger.getLogger(YikeApiHandler.class);
	
	private static final String method_price = "api/mprod/ShopProdPrice";
	private static final String method_stock = "api/mprod/ShopProdStocks";
	private static final String method_prod_down = "api/MProd/ProdDown";
	private static final String method_up_down = "api/mprod/ShopProdOnSale";
	private static final String method_add_prod = "api/mprod/ShopProdBatchAdd";

	private static final int api_return_status = 200;
	
	@Resource
	private ProductSellerGoodsBarcodeMapper productSellerGoodsBarcodeMapper;
	@Resource
	private ProductSellerGoodsMapper productSellerGoodsMapper;
	@Resource
	private MBProductSellerGoodsSelectMapper mbProductSellerGoodsSelectMapper;
	
	@Override
	public void buildApiClient(Map<String, String> secretInforMap) {
		super.buildApiClient(secretInforMap);
	}

	
	@Override
	public ApiResultVO updatePrice(ItemUpdate itemUpdate) {
		logger.info("更新商品价格 itemUpdate:" + JSON.toJSONString(itemUpdate));
		ApiResultVO result = new ApiResultVO(Constants.API_RETURN_NO, "更新商品价格报错");
		String appKey = secretInforMap.get(Constants.APP_KEY);
		String token = secretInforMap.get(Constants.ACCESS_TOKEN);
		String signType = secretInforMap.get(Constants.SIGN_TYPE);
		String url = secretInforMap.get(Constants.APP_URL);
		String timestamp = com.work.shop.oms.utils.TimeUtil.format3Date(new Date());
		String str = "AppId=" + appKey + "&Timestamp=" + timestamp + "&Token=" + token;
		System.out.println("sign str:" + str);
		String goodsSn = itemUpdate.getGoodsSn();
		try {
			String sign = getSign(appKey, token, signType);
			ProductSellerGoodsExample goodsExample = new ProductSellerGoodsExample();
			goodsExample.or().andProductCodeEqualTo(goodsSn);
			List<ProductSellerGoods> list = productSellerGoodsMapper.selectByExample(goodsExample);
			if (StringUtil.isListNull(list)) {
				result.setMessage("goodsSn:" + goodsSn + "基础数据本地不存在！");
				return result;
			}
			ProductSellerGoods goods = list.get(0);
			ProductSellerGoodsBarcodeExample example = new ProductSellerGoodsBarcodeExample();
			example.or().andProductSysCodeEqualTo(goods.getProductSysCode());
			List<ProductSellerGoodsBarcode> barcodes = productSellerGoodsBarcodeMapper.selectByExample(example);
			if (StringUtil.isListNull(barcodes)) {
				result.setMessage("goodsSn:" + goodsSn + "基础barcode数据本地不存在！");
				return result;
			}
			//拼装接口入参
			/*List<Map<String, Object>> maps = new ArrayList<Map<String,Object>>();
			for (ProductSellerGoodsBarcode barcode : barcodes) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("ShopCode", itemUpdate.getShopCode());
				map.put("barCode", barcode.getBarcodeCode());
				map.put("PriceType", itemUpdate.getPriceType());
				map.put("SalePrice", Double.valueOf(itemUpdate.getItemFieldValue()));
				maps.add(map);
			}*/
			ItemAdd itemAdd = new ItemAdd();
			itemAdd.setShopCode(itemUpdate.getShopCode());
			itemAdd.setItemNo(itemUpdate.getGoodsSn());
//			ProductSellerGoods sellerGoods = list.get(0);
//			itemAdd.setRetailPrice(sellerGoods.getMarketPrice().doubleValue());
			itemAdd.setSalePrice(Double.valueOf(itemUpdate.getItemFieldValue()));
			List<ItemAdddetail> itemAdddetails = new ArrayList<ItemAdddetail>();
			for (ProductSellerGoodsBarcode barcode : barcodes) {
				ItemAdddetail adddetail = new ItemAdddetail();
				adddetail.setBarCode(barcode.getBarcodeCode());
//				adddetail.setRetailPrice(sellerGoods.getMarketPrice().doubleValue());
				adddetail.setSalePrice(Double.valueOf(itemUpdate.getItemFieldValue()));
				itemAdddetails.add(adddetail);
			}
			itemAdd.setDetailItems(itemAdddetails);
			List<NameValuePair> ps = new ArrayList<NameValuePair>();
			ps.add(new NameValuePair("AppId", appKey));
			ps.add(new NameValuePair("Timestamp", timestamp));
			ps.add(new NameValuePair("Sign", sign.toUpperCase()));
			ps.add(new NameValuePair("Args", JSON.toJSONString(itemAdd)));
			ps.add(new NameValuePair("AppSystem", "POS"));
			String paramstring = "AppId=" + appKey + "&Timestamp=" + timestamp + "&Sign=" + sign.toUpperCase()
					+ "&Args=" + JSON.toJSONString(itemAdd) + "&AppSystem=POS";
			logger.info("提交参数：" + paramstring);
			String msg = post(url + method_add_prod, ps.toArray(new NameValuePair[ps.size()]));
			logger.info("返回结果：" + msg);
			YkApiResult<List<ProdItem>> apiResult = JSON.parseObject(msg, YkApiResult.class);
			if (apiResult == null) {
				result.setMessage("价格同步结果为空！");
				return result;
			}
			if (apiResult.getStatusCode() != null
					&& apiResult.getStatusCode().intValue() == api_return_status) {
				result.setCode(Constants.API_RETURN_OK);
				result.setMessage(apiResult.getMsg());
			} else {
				result.setMessage(apiResult.getMsg());
			}
		} catch (Exception e) {
			logger.error(goodsSn + "价格同步异常:", e);
			result.setCode("-1");
			result.setMessage(goodsSn + " ： 价格同步失败!" + e.getMessage());
		}
		logger.info("result(code :" + result.getCode() + " , message : " + result.getMessage() + ")");
		return result;
	}

	@Override
	public ApiResultVO updateGoodsName(ItemUpdate itemUpdate) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ApiResultVO shelvesUpOrDown(ItemUpdate itemUpdate) {
		logger.info("更新商品上下架 itemUpdate:" + JSON.toJSONString(itemUpdate));
		ApiResultVO result = new ApiResultVO(Constants.API_RETURN_NO, "更新商品上下架报错");
		String appKey = secretInforMap.get(Constants.APP_KEY);
		String token = secretInforMap.get(Constants.ACCESS_TOKEN);
		String signType = secretInforMap.get(Constants.SIGN_TYPE);
		String url = secretInforMap.get(Constants.APP_URL);
		String timestamp = com.work.shop.oms.utils.TimeUtil.format3Date(new Date());
		String str = "AppId=" + appKey + "&Timestamp=" + timestamp + "&Token=" + token;
		System.out.println("sign str:" + str);
		String goodsSn = itemUpdate.getItemFieldValue();
		try {
			String sign = getSign(appKey, token, signType);
			//拼装接口入参
			List<Map<String, Object>> maps = new ArrayList<Map<String,Object>>();
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("ShopCode", itemUpdate.getShopCode());
			map.put("ItemNo", itemUpdate.getGoodsSn());
			map.put("IsOnSale", itemUpdate.getItemFieldValue());
			maps.add(map);
			List<NameValuePair> ps = new ArrayList<NameValuePair>();
			ps.add(new NameValuePair("AppId", appKey));
			ps.add(new NameValuePair("Timestamp", timestamp));
			ps.add(new NameValuePair("Sign", sign.toUpperCase()));
			ps.add(new NameValuePair("Args", JSON.toJSONString(maps)));
			ps.add(new NameValuePair("AppSystem", "POS"));
			String paramstring = "AppId=" + appKey + "&Timestamp=" + timestamp + "&Sign=" + sign.toUpperCase()
					+ "&Args=" + JSON.toJSONString(map) + "&AppSystem=POS";
			System.out.println(paramstring);
			String msg = post(url + method_up_down, ps.toArray(new NameValuePair[ps.size()]));
			System.out.println(msg);
			YkApiResult<List<ProdItem>> apiResult = JSON.parseObject(msg, YkApiResult.class);
			if (apiResult == null) {
				result.setMessage("商品上下架结果为空！");
				return result;
			}
			if (apiResult.getStatusCode() != null
					&& apiResult.getStatusCode().intValue() == api_return_status) {
				result.setCode(Constants.API_RETURN_OK);
				result.setMessage(apiResult.getMsg());
			} else {
				result.setMessage(apiResult.getMsg());
			}
		} catch (Exception e) {
			logger.error(goodsSn + "上下架同步异常:", e);
			result.setMessage(goodsSn + " ： 上下架同步失败!" + e.getMessage());
		}
		logger.info("result(code :" + result.getCode() + " , message : " + result.getMessage() + ")");
		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public ApiResultVO<List<ProdItem>> getBaseData(ItemQuery itemQuery) {
		logger.info("查询商品 itemQuery:" + JSON.toJSONString(itemQuery));
		ApiResultVO<List<ProdItem>> result = new ApiResultVO<List<ProdItem>>(Constants.API_RETURN_NO, "查询失败");
		String appKey = secretInforMap.get(Constants.APP_KEY);
		String token = secretInforMap.get(Constants.ACCESS_TOKEN);
		String signType = secretInforMap.get(Constants.SIGN_TYPE);
		String url = secretInforMap.get(Constants.APP_URL);
		String timestamp = com.work.shop.oms.utils.TimeUtil.format3Date(new Date());
		try {
			String sign = getSign(appKey, token, signType);
			List<NameValuePair> ps = new ArrayList<NameValuePair>();
			ps.add(new NameValuePair("AppId", appKey));
			ps.add(new NameValuePair("Timestamp", timestamp));
			ps.add(new NameValuePair("Sign", sign.toUpperCase()));
			ps.add(new NameValuePair("Args", JSON.toJSONString(itemQuery)));
			ps.add(new NameValuePair("AppSystem", "POS"));
			String paramstring = "AppId=" + appKey + "&Timestamp=" + timestamp + "&Sign=" + sign.toUpperCase()
					+ "&Args=" + JSON.toJSONString(itemQuery) + "&AppSystem=POS";
			System.out.println(paramstring);
			String msg = post(url + method_prod_down, ps.toArray(new NameValuePair[ps.size()]));
			logger.info("返回结果：" + msg);
			YkApiResult<List<ProdItem>> apiResult = JSON.parseObject(msg, YkApiResult.class);
			if (apiResult == null) {
				result.setMessage("查询结果为空！");
				return result;
			}
			if (apiResult.getStatusCode() != null
					&& apiResult.getStatusCode().intValue() == api_return_status) {
				result.setCode(Constants.API_RETURN_OK);
				result.setMessage(apiResult.getMsg());
				result.setApiGoods(apiResult.getResult());
				result.setTotal(apiResult.getPageCount());
			} else {
				result.setMessage(apiResult.getMsg());
			}
			System.out.println(JSON.toJSON(apiResult));
		} catch (Exception e) {
			logger.error("查询商品异常:", e);
			result.setCode("-1");
			result.setMessage("查询商品失败!" + e.getMessage());
		}
		logger.info("result(code:" + result.getCode() + ",message:" + result.getMessage() + ")");
		return result;
	}

	@Override
	public ApiResultVO updateItemStock(ItemUpdate itemUpdate) {
		logger.info("同步商品库存 itemUpdate:" + JSON.toJSONString(itemUpdate));
		ApiResultVO result = new ApiResultVO(Constants.API_RETURN_NO, "同步库存报错");
		String appKey = secretInforMap.get(Constants.APP_KEY);
		String token = secretInforMap.get(Constants.ACCESS_TOKEN);
		String signType = secretInforMap.get(Constants.SIGN_TYPE);
		String url = secretInforMap.get(Constants.APP_URL);
		String timestamp = com.work.shop.oms.utils.TimeUtil.format3Date(new Date());
		String str = "AppId=" + appKey + "&Timestamp=" + timestamp + "&Token=" + token;
		System.out.println("sign str:" + str);
		String goodsSn = itemUpdate.getItemFieldValue();
		try {
			String sign = getSign(appKey, token, signType);
			//拼装接口入参
			List<Map<String, Object>> maps = new ArrayList<Map<String,Object>>();
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("ShopCode", itemUpdate.getShopCode());
			map.put("barCode", itemUpdate.getSkuSn());
			map.put("Stocks", itemUpdate.getStockCount());
			maps.add(map);
			List<NameValuePair> ps = new ArrayList<NameValuePair>();
			ps.add(new NameValuePair("AppId", appKey));
			ps.add(new NameValuePair("Timestamp", timestamp));
			ps.add(new NameValuePair("Sign", sign.toUpperCase()));
			ps.add(new NameValuePair("Args", JSON.toJSONString(maps)));
			ps.add(new NameValuePair("AppSystem", "POS"));
			String paramstring = "AppId=" + appKey + "&Timestamp=" + timestamp + "&Sign=" + sign.toUpperCase()
					+ "&Args=" + JSON.toJSONString(map) + "&AppSystem=POS";
			System.out.println(paramstring);
			String msg = post(url + method_stock, ps.toArray(new NameValuePair[ps.size()]));
			System.out.println(msg);
			YkApiResult apiResult = JSON.parseObject(msg, YkApiResult.class);
			if (apiResult == null) {
				result.setMessage("同步商品库存结果为空！");
				return result;
			}
			if (apiResult.getStatusCode() != null
					&& apiResult.getStatusCode().intValue() == api_return_status) {
				result.setCode(Constants.API_RETURN_OK);
				result.setMessage(apiResult.getMsg());
			} else {
				result.setMessage(apiResult.getMsg());
			}
		} catch (Exception e) {
			logger.error(goodsSn + "同步商品库存异常:", e);
			result.setMessage(goodsSn + " ： 同步商品库存失败!" + e.getMessage());
		}
		logger.info("result(code :" + result.getCode() + " , message : " + result.getMessage() + ")");
		return result;
	}
	
	@Override
	public ApiResultVO addItem(ItemAdd itemAdd) {
		logger.info("新增商品 itemAdd:" + JSON.toJSONString(itemAdd));
		ApiResultVO result = new ApiResultVO(Constants.API_RETURN_NO, "新增商品报错");
		String appKey = secretInforMap.get(Constants.APP_KEY);
		String token = secretInforMap.get(Constants.ACCESS_TOKEN);
		String signType = secretInforMap.get(Constants.SIGN_TYPE);
		String url = secretInforMap.get(Constants.APP_URL);
		String timestamp = com.work.shop.oms.utils.TimeUtil.format3Date(new Date());
		String str = "AppId=" + appKey + "&Timestamp=" + timestamp + "&Token=" + token;
		System.out.println("sign str:" + str);
		String goodsSn = itemAdd.getItemNo();
		try {
			String sign = getSign(appKey, token, signType);
			List<NameValuePair> ps = new ArrayList<NameValuePair>();
			ps.add(new NameValuePair("AppId", appKey));
			ps.add(new NameValuePair("Timestamp", timestamp));
			ps.add(new NameValuePair("Sign", sign.toUpperCase()));
			ps.add(new NameValuePair("Args", JSON.toJSONString(itemAdd)));
			ps.add(new NameValuePair("AppSystem", "POS"));
			String paramstring = "AppId=" + appKey + "&Timestamp=" + timestamp + "&Sign=" + sign.toUpperCase()
					+ "&Args=" + JSON.toJSONString(itemAdd) + "&AppSystem=POS";
			System.out.println(paramstring);
			String msg = post(url + method_add_prod, ps.toArray(new NameValuePair[ps.size()]));
			System.out.println(msg);
			YkApiResult apiResult = JSON.parseObject(msg, YkApiResult.class);
			if (apiResult == null) {
				result.setMessage("新增商品结果为空！");
				return result;
			}
			if (apiResult.getStatusCode() != null
					&& apiResult.getStatusCode().intValue() == api_return_status) {
				result.setCode(Constants.API_RETURN_OK);
				result.setMessage(apiResult.getMsg());
			} else {
				result.setMessage(apiResult.getMsg());
			}
		} catch (Exception e) {
			logger.error(goodsSn + "新增商品异常:", e);
			result.setMessage(goodsSn + " ： 新增商品失败!" + e.getMessage());
		}
		logger.info("result(code :" + result.getCode() + " , message : " + result.getMessage() + ")");
		return result;
	}

	@Override
	public ApiResultVO searchItemPage(LocalItemQuery query) {
		logger.info("本地商品查询 query:" + JSON.toJSONString(query));
		ApiResultVO result = new ApiResultVO(Constants.API_RETURN_NO, "本地商品查询报错");
		Integer start = (query.getPage() - 1) * query.getPageSize();
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("shopCode", query.getShopCode());
		param.put("status", query.getStatus());
		param.put("itemNo", query.getItemNo());
		param.put("start", start);
		param.put("limits", query.getPageSize());
		try {
			List<ChannelApiGoods> apiGoods =  mbProductSellerGoodsSelectMapper.selectChannelItem(param);
			if (StringUtil.isListNotNull(apiGoods)) {
				for (ChannelApiGoods goods : apiGoods) {
					goods.setShopName(query.getShopTitle());
				}
			}
			int total = mbProductSellerGoodsSelectMapper.selectChannelItemCount(param);
			result.setApiGoods(apiGoods);
			result.setTotal(total);
			result.setCode(Constants.API_RETURN_OK);
		} catch (Exception e) {
			logger.error("本地商品查询异常" + e.getMessage(), e);
		}
		return result;
	}

	private String getSign(String appKey, String token, String signType) {
		String timestamp = TimeUtil.format3Date(new Date());
		String str = "AppId=" + appKey + "&Timestamp=" + timestamp + "&Token=" + token;
		String sign = null;
		try {
			byte b[] = str.getBytes("utf-8");
			if ("MD5".equals(signType)) {
				sign = MD5.md5(b);
			} else {
				sign = Sha1.sha1(b);
			}
			System.out.println("sign:" + sign.toUpperCase());
		} catch (Exception e) {
			logger.error("制作签名失败" + e.getMessage(), e);
		}
		return sign;
	}
	private String post(String URL, NameValuePair[] params) {
		String responseBody = "";
		HttpClient httpClient = new HttpClient();
		PostMethod postMethod = new PostMethod(URL);
		postMethod.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
		HttpConnectionManagerParams managerParams = httpClient.getHttpConnectionManager().getParams();
		managerParams.setConnectionTimeout(30000);
		managerParams.setSoTimeout(5000);
		try {
			httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 30000);
			httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 5000);
			postMethod.addRequestHeader("Content-Type", "application/x-www-form-urlencoded");
			postMethod.setRequestBody(params);
			int statusCode = httpClient.executeMethod(postMethod);
			if (statusCode == HttpStatus.SC_OK) {
				responseBody = postMethod.getResponseBodyAsString();
			} else {
				System.out.println("Eorr occus");
				responseBody = "跳转失败，错误编码：" + statusCode;
			}
		} catch (Exception e) {
			responseBody = "网关限制无法跳转!";
		} finally {
			postMethod.releaseConnection();
		}
		return responseBody;
	}
}

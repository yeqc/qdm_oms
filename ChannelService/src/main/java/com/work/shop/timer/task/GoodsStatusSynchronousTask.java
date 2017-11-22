package com.work.shop.timer.task;

import java.util.Date;
import java.util.List;
import java.util.TimerTask;

import javax.annotation.Resource;

import org.apache.log4j.Logger;

import com.work.shop.api.bean.ApiResultVO;
import com.work.shop.api.bean.ChannelApiGoods;
import com.work.shop.api.bean.LocalItemQuery;
import com.work.shop.api.service.ApiService;
import com.work.shop.bean.ChannelApiLog;
import com.work.shop.bean.ChannelErpUpdownLog;
import com.work.shop.bean.ChannelGoods;
import com.work.shop.bean.ChannelGoodsExample;
import com.work.shop.bean.ChannelShopExample;
import com.work.shop.dao.ChannelApiLogMapper;
import com.work.shop.dao.ChannelErpUpdownLogMapper;
import com.work.shop.dao.ChannelGoodsMapper;
import com.work.shop.service.ShopService;
import com.work.shop.util.Constants;
import com.work.shop.util.StringUtil;
import com.work.shop.util.TimeUtil;
import com.work.shop.vo.ChannelShopVo;

public class GoodsStatusSynchronousTask extends TimerTask {

	private Logger logger = Logger.getLogger(GoodsStatusSynchronousTask.class);

	@Resource(name = "apiService")
	private ApiService apiService;

	@Resource(name = "shopService")
	private ShopService shopService;

	@Resource(name = "channelGoodsMapper")
	private ChannelGoodsMapper channelGoodsMapper;

	@Resource
	private ChannelApiLogMapper channelApiLogMapper;

	@Resource
	private ChannelErpUpdownLogMapper channelErpUpdownLogMapper;

	private final static String USER_NAME = "System";

	private final static int PAGE_SIZE = 20;

	@Override
	public void run() {
		logger.info("商品状态同步定时任务开始执行...");
		try {
			ChannelShopExample example = new ChannelShopExample();
			ChannelShopExample.Criteria criteria = example.or();
			criteria.andShopStatusEqualTo(Byte.valueOf("1")); // 默认加载已激活状态的店铺
			criteria.andShopChannelEqualTo(Byte.valueOf("1"));
			List<ChannelShopVo> list = shopService.getChannelShopList(example, false);
			String channelCode = "";
			String shopCode = "";
			if (list != null && list.size() > 0) {
				for (ChannelShopVo channelShopVo : list) {
					if (StringUtil.isNotNull(channelShopVo.getChannelCode()) && StringUtil.isNotNull(channelShopVo.getShopCode())) {
						channelCode = channelShopVo.getChannelCode();
						shopCode = channelShopVo.getShopCode();
						try {
							synchronousStatus(channelCode, shopCode, "1");
						} catch (Exception e) {
							e.printStackTrace();
							logger.error("定时任务：[channelCode : " + channelCode + " , shopCode : " + shopCode + "] 商品状态(上架)同步导出异常" + e.getMessage());
						}
						try {
							synchronousStatus(channelCode, shopCode, "2");
						} catch (Exception e) {
							e.printStackTrace();
							logger.error("定时任务：[channelCode : " + channelCode + " , shopCode : " + shopCode + "] 商品状态(下架)同步导出异常" + e.getMessage());
						}
					}
				}
			}
		} catch (Exception e) {
			logger.info(e.getMessage());
		}
		logger.info("商品状态同步定时任务执行结束...");
	}

	private void synchronousStatus(String channelCode, String shopCode, String wareStatus) {
		LocalItemQuery itemQuery = new LocalItemQuery();
		itemQuery.setChannelCode(channelCode);
		itemQuery.setShopCode(shopCode);
		itemQuery.setItemNo(null);
		itemQuery.setPage(1);
		itemQuery.setPageSize(PAGE_SIZE);
		itemQuery.setStatus(wareStatus);
		itemQuery.setUserName(USER_NAME);
		ApiResultVO rapiResultVO = apiService.searchItemPage(itemQuery);
		// 有异常返回
		if (!Constants.API_RETURN_OK.equals(rapiResultVO.getCode())) {
			logger.info("定时任务：商品状态同步ApiService.searchItemPage 分页查询总数失败！shopCode=" + shopCode + "页码：" + 1 + "上下架状态：" + wareStatus);
			return;
		}

		int iTotal = rapiResultVO.getTotal();

		int pageNum = 1;

		if (iTotal > PAGE_SIZE) {
			if (iTotal % PAGE_SIZE == 0) {
				pageNum = iTotal / PAGE_SIZE;
			} else {
				pageNum = (iTotal / PAGE_SIZE) + 1;
			}
		}

		String onLineStatus = null;
		String localStatus = null;
		String goodsSn = null;
		if (pageNum > 1) {
			ApiResultVO<List<ChannelApiGoods>> apiResultVO = null;
			for (int j = 1; j <= pageNum; j++) {
				try {
					itemQuery.setPage(j);
					apiResultVO = apiService.searchItemPage(itemQuery);
					List<ChannelApiGoods> channelApiGoodsList = apiResultVO.getApiGoods();

					for (ChannelApiGoods channelApiGoods : channelApiGoodsList) {
						try {
							goodsSn = channelApiGoods.getGoodsSn();
							onLineStatus = wareStatus;
							ChannelGoods channelGoods = getChannelGoods(channelApiGoods.getShopCode(), channelApiGoods.getGoodsSn());
							if (channelGoods != null) { // 商品存在
								localStatus = String.valueOf(channelGoods.getIsOnSell());
								if (isSameStatus(onLineStatus, localStatus)) { // 商品上下架相同
									continue;
								}
								// 商品上下架相同
								String status = "1".equals(onLineStatus) ? "1" : "0";

								// 同步本地商品状态
								ChannelApiLog apiLog = new ChannelApiLog();
								apiLog.setChannelCode(channelCode);
								apiLog.setShopCode(shopCode);
								apiLog.setRequestTime(new Date());
								apiLog.setMethodName("1");
								apiLog.setUser(USER_NAME);
								apiLog.setParamInfo(goodsSn);

								try {
									channelGoods.setIsOnSell(Byte.valueOf(status));
									channelGoods.setLastUpdate(new Date());
									channelGoods.setLastUpdateTime((int) TimeUtil.parseDateToNumeric(new Date()));
									int count = channelGoodsMapper.updateByPrimaryKeySelective(channelGoods);
									if (count != 1) {
										apiLog.setReturnCode("-1");
										logger.error("定时任务：商品状态同步[shopCode = " + shopCode + " , goodsSn =" + channelApiGoods.getGoodsSn() + " , 上下架状态：" + wareStatus + "]失败!");
										apiLog.setReturnMessage("定时任务：商品状态同步[shopCode = " + shopCode + " , goodsSn =" + channelApiGoods.getGoodsSn() + " , 上下架状态：" + wareStatus + "]失败!");
									} else {
										apiLog.setReturnCode("0");
										logger.info("定时任务：商品状态同步[shopCode = " + shopCode + " , goodsSn =" + channelApiGoods.getGoodsSn() + " , 上下架状态：" + wareStatus + "]成功!");
										apiLog.setReturnMessage("定时任务：商品状态同步[shopCode = " + shopCode + " , goodsSn =" + channelApiGoods.getGoodsSn() + " , 上下架状态：" + wareStatus + "]成功!");
									}
									// 插入日志
									channelApiLogMapper.insertSelective(apiLog);
								} catch (Exception e) {
									e.printStackTrace();
									logger.error(e.getMessage());
								}

								// 同步库存中心商品状态
								synchronousToERP(channelCode, shopCode, goodsSn, status);

							} else { // 商品不存在
								String status = "0";
								// 同步库存中心商品状态
								synchronousToERP(channelCode, shopCode, goodsSn, status);
							}

						} catch (Exception e) {
							e.printStackTrace();
							logger.info("定时任务：商品状态同步[shopCode = " + shopCode + " , goodsSn =" + channelApiGoods.getGoodsSn() + " , 上下架状态：" + wareStatus + "]异常! " + e.getMessage());
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
					logger.info("定时任务：商品状态同步[shopCode = " + shopCode + " , 页码 = " + j + " , 上下架状态 = " + wareStatus + "]异常! " + e.getMessage());
				}
			}
		}
	}

	private ChannelGoods getChannelGoods(String channelCode, String goodsSn) {
		ChannelGoodsExample example = new ChannelGoodsExample();
		example.or().andChannelCodeEqualTo(channelCode).andGoodsSnEqualTo(goodsSn);
		List<ChannelGoods> list = channelGoodsMapper.selectByExample(example);
		if (list == null || list.size() != 1) {
			return null;
		}
		return list.get(0);
	}

	private boolean isSameStatus(String onLineStatus, String localStatus) {
		// (线上(1:上架，2:下架) 本地(1:上架，0:下架))
		if ("1".equals(onLineStatus) && StringUtil.isNotEmpty(onLineStatus) && onLineStatus.equals(localStatus)) {
			return true;
		}
		if ("2".equals(onLineStatus) && "0".equals(localStatus)) {
			return true;
		}
		return false;
	}

	private void synchronousToERP(String channelCode, String shopCode, String goodsSn, String status) {
		Date requestTime = new Date();
		String resultCode = "0";
		String message = "";
		try {
//			List<UpDownErp> erpList = new ArrayList<UpDownErp>();
//			UpDownErp upDownErp = new UpDownErp(shopCode, goodsSn, Integer.valueOf(status));
//			erpList.add(upDownErp);
//			// 上下架同步成功后将状态同步至库存中心.
//			// 设置Post请求的表单参数对象
//			if (!StringUtil.isNotNullForList(erpList)) {
//				return;
//			}
//			String goodsSnInfo = JSON.toJSONString(erpList);
//			List<org.apache.http.NameValuePair> valuePairs = new ArrayList<org.apache.http.NameValuePair>();
//			valuePairs.add(new BasicNameValuePair("goodsSnInfo", goodsSnInfo));
//			String jsonStr = HttpClientUtil.post(erpUrl, valuePairs);
//			if (StringUtil.isNotEmpty(jsonStr)) {
//				try {
//					@SuppressWarnings("unchecked")
//					Map<String, String> map = JSONObject.parseObject(jsonStr, Map.class);
//					if (null != map && !map.isEmpty()) {
//						resultCode = map.get("flag");
//						if ("1".equals(map.get("flag"))) {
//							message = "定时任务：[shopCode = " + shopCode + " , goodsSn =" + goodsSn + "] 上下架状态同步至库存中心成功!";
//						} else {
//							message = goodsSnInfo + map.get("msg");
//							logger.error(goodsSn + map.get("msg"));
//						}
//					}
//				} catch (Exception e) {
//					resultCode = "0";
//					logger.error(goodsSn + jsonStr + "返回结果转换JSON异常", e);
//					message = goodsSn + jsonStr + "返回结果转换JSON异常:" + e.getMessage() ;
//				}
//			} else {
//				resultCode = "0";
//				message = goodsSn + "返回结果为空！";
//			}
			/*StockChannelStatusBean stockChannelStatusBean = new StockChannelStatusBean();
			stockChannelStatusBean.setChannelCode(shopCode);
			stockChannelStatusBean.setUpdateTime(new Date());
			stockChannelStatusBean.setSixProdId(goodsSn);
			stockChannelStatusBean.setCreateTime(new Date());
			stockChannelStatusBean.setSaleStatus(status);
			stockChannelStatusBean.setIsSync((byte)1);
			stockChannelStatusBean.setCreateBy(USER_NAME);
			stockChannelStatusBean.setUpdateBy(USER_NAME);
			logger.info("回写统一库存请求参数:" +JSON.toJSONString(stockChannelStatusBean));
			Message response = stockChannelStatusFacade.setStockChannelStatus(stockChannelStatusBean);
			logger.info("回写统一库存：" + JSON.toJSONString(response));
			if (response.getIsSetData()) {
				message = "定时任务：[shopCode = " + shopCode + " , goodsSn =" + goodsSn + "] 上下架状态同步至库存中心成功!";
				resultCode = "1";
			} else {
				message = goodsSn + "上下架状态同步至统一库存失败！";
			}*/
		} catch (Exception e) {
			message = "定时任务：[shopCode = " + shopCode + " , goodsSn =" + goodsSn + "] 上下架状态同步至库存中心失败：" + e.getMessage();
			logger.error("定时任务：[shopCode = " + shopCode + " , goodsSn =" + goodsSn + "] 上下架状态同步至库存中心失败：", e);
		} finally {
			ChannelErpUpdownLog updownLog = new ChannelErpUpdownLog();
			updownLog.setChannelCode(channelCode);
			updownLog.setShopCode(shopCode);
			updownLog.setCode(resultCode);
			updownLog.setMessage(message);
			updownLog.setGoodsSn(goodsSn);
			updownLog.setStatus(status);
			updownLog.setType("1");
			updownLog.setRequestTime(requestTime);
			updownLog.setUserId(USER_NAME);
			try {
				channelErpUpdownLogMapper.insertSelective(updownLog);
			} catch (Exception e2) {
				e2.printStackTrace();
				logger.error("记录日志：定时任务[shopCode = " + shopCode + " , goodsSn =" + goodsSn + "] 上下架状态同步至库存中心 异常:" + e2);
			}
		}
	}
}

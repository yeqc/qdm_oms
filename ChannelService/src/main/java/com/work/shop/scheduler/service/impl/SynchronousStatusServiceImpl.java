package com.work.shop.scheduler.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.work.shop.api.bean.ApiResultVO;
import com.work.shop.api.bean.ChannelApiGoods;
import com.work.shop.api.bean.LocalItemQuery;
import com.work.shop.api.service.ApiService;
import com.work.shop.bean.ChannelErpUpdownLog;
import com.work.shop.bean.ChannelGoods;
import com.work.shop.bean.ChannelGoodsExample;
import com.work.shop.bean.ChannelShop;
import com.work.shop.bean.ChannelShopExample;
import com.work.shop.bean.bgcontentdb.BGproductGoodsExample;
import com.work.shop.bean.bgcontentdb.BGproductGoodsWithBLOBs;
import com.work.shop.dao.ChannelErpUpdownLogMapper;
import com.work.shop.dao.ChannelGoodsMapper;
import com.work.shop.dao.ChannelShopMapper;
import com.work.shop.dao.bgcontentdb.BGproductGoodsMapper;
import com.work.shop.scheduler.service.SynchronousStatusService;
import com.work.shop.util.Constants;
import com.work.shop.util.StringUtil;
import com.work.shop.util.TimeUtil;

@Service("synchronousStatusService")
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class SynchronousStatusServiceImpl implements SynchronousStatusService {

	private Logger logger = Logger.getLogger(SynchronousStatusServiceImpl.class);

	@Resource(name = "apiService")
	private ApiService apiService;

	@Resource(name = "channelShopMapper")
	private ChannelShopMapper channelShopMapper;

	@Resource(name = "channelGoodsMapper")
	private ChannelGoodsMapper channelGoodsMapper;

	@Resource(name="bGproductGoodsMapper")
	private BGproductGoodsMapper productGoodsMapper;
	
	@Resource
	private ChannelErpUpdownLogMapper channelErpUpdownLogMapper;
	
//	@Resource
//	private HandlerBuildFactory factory;

	private final static String USER_NAME = "System";

//	private static final String erpUrl = PropertieFileReader.getString("ERP_STOCK_CENTER_URL");

	@Override
	public void synchronousStatus(String[] shopCodes) {
		logger.debug("商品状态同步调度任务开始执行...");
		try {
			if (shopCodes != null && shopCodes.length > 0) {
				for (String shopCode : shopCodes) {
					synchronousStatus(shopCode);
				}
			}
		} catch (Exception e) {
			logger.error("商品状态同步调度任务异常", e);
		}
	logger.debug("商品状态同步调度任务执行结束...");
	}

	private void synchronousStatus(String shopCode) {
		if (StringUtil.isNotNull(shopCode)) {
			shopCode = shopCode.trim();
			String channelCode = getChannelCode(shopCode);
			if (StringUtil.isNotNull(channelCode)) {
				// 获取本地商品信息列表
				List<String> localGoodsSnList = getLocalGoodsSnList(shopCode);
				try {
					synchronousStatus(channelCode, shopCode, "1", localGoodsSnList);
				} catch (Exception e) {
					logger.error("调度任务：[channelCode : " + channelCode + " , shopCode : " + shopCode + "] 商品状态(上架)同步导出异常", e);
				}
				try {
					if (!"HQ01S227".equals(shopCode)) {
						synchronousStatus(channelCode, shopCode, "2", localGoodsSnList);
					}
				} catch (Exception e) {
					logger.error("调度任务：[channelCode : " + channelCode + " , shopCode : " + shopCode + "] 商品状态(下架)同步导出异常", e);
				}
				
				/*if (StringUtil.isListNotNull(localGoodsSnList)) {
					// 将渠道上存在，线上不存在的商品改为下架状态
					logger.debug("调度任务：[shopCode : " + shopCode + "],渠道商品[" + localGoodsSnList.toString() + "]在线上不存在, 调整为下架状态");

					ChannelGoodsExample upExample = new ChannelGoodsExample();
					upExample.or().andChannelCodeEqualTo(shopCode).andGoodsSnIn(localGoodsSnList).andIsOnSellEqualTo(Byte.valueOf("1"));

					// 获取渠道商品localGoodsSnList中状态为上架的商品
					List<ChannelGoods> upChannelGoods = channelGoodsMapper.selectByExample(upExample);
					for (ChannelGoods record : upChannelGoods) {
						String msgInfo = "调度任务:[shopCode : " + shopCode + ", goodsSn ： " + record.getGoodsSn() + "]线上不存在";
						try {
							record.setIsOnSell(Byte.valueOf("0"));
							record.setLastUpdate(new Date());
							record.setLastUpdateTime((int) TimeUtil.parseDateToNumeric(new Date()));

							// 更新本地商品下架状态
							int count = channelGoodsMapper.updateByPrimaryKey(record);

							if (count != 1) {
								msgInfo = msgInfo + ",本地下架失败";
							} else {
								msgInfo = msgInfo + ",本地下架成功";
							}
							// 同步统一库存下架状态
							synchronousToERP(channelCode, shopCode, record.getGoodsSn(), "0", msgInfo);
						} catch (Exception e) {
							logger.error(msgInfo + ",下架状态调整异常" , e);
						}
					}
				}*/
			}
		}
	}

	private String getChannelCode(String shopCode) {
		ChannelShopExample example = new ChannelShopExample();
		example.or().andShopCodeEqualTo(shopCode);
		List<ChannelShop> channelShops = channelShopMapper.selectByExample(example);
		if (channelShops == null || channelShops.size() != 1) {
			return null;
		}
		return channelShops.get(0).getChannelCode();
	}

//	private HttpClient getHttpClient() {
//		MultiThreadedHttpConnectionManager connectionManager = new MultiThreadedHttpConnectionManager();
//		HttpClient httpClient = new HttpClient(connectionManager);
//		// 设置Http连接超时时间为 20秒
//		httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(20000);
//		httpClient.getParams().setContentCharset("UTF-8");
//		return httpClient;
//	}

//	private PostMethod getPostMethod() {
//		PostMethod method = new PostMethod(erpUrl);
//		method.getParams().setParameter(HttpMethodParams.SO_TIMEOUT, 45000);
//		// 设置成了默认的恢复策略，在发生异常时候将自动重试3次
//		method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler());
//		method.addRequestHeader("Connection", "close");
//		return method;
//	}

	/**
	 * 逻辑： 1.线上商品在线下存在，则把线上商品的上下架状态同步至线下，并同步至统一库存
	 * 2.线上商品在线下不存在，则将线上商品状态改为下架状态，并以下架状态同步至统一库存 3.如果线下商品在线上不存在，则将线下状态商品改为下架状态
	 * 
	 * @param channelCode
	 * @param shopCode
	 * @param wareStatus
	 */
	private void synchronousStatus(String channelCode, String shopCode, String wareStatus, List<String> localGoodsSnList) {

		int pageSize = 20;
		
		if(StringUtil.isTaoBaoChannel(channelCode)){
			if(!Constants.TB_FX.equals(shopCode)){
				pageSize = 200;
			}
		}
		if(Constants.JD_CHANNEL_CODE.equals(channelCode)){
			pageSize = 100;
		}
		
		if(Constants.YHD_CHANNEL_CODE.equals(channelCode)){
			pageSize = 50;
		}
		
		if(Constants.WX_CHANNEL_CODE.equals(channelCode)){
			pageSize = 100;
		}
		
		if(Constants.BG_CHANNEL_CODE.equals(channelCode)){
			pageSize = 100;
		}
		if(Constants.GW_CHANNEL_CODE.equals(channelCode)){
			pageSize = 100;
		}
		LocalItemQuery itemQuery = new LocalItemQuery();
		itemQuery.setChannelCode(channelCode);
		itemQuery.setShopCode(shopCode);
		itemQuery.setItemNo(null);
		itemQuery.setPage(1);
		itemQuery.setPageSize(pageSize);
		itemQuery.setStatus(wareStatus);
		itemQuery.setUserName(USER_NAME);
		ApiResultVO rapiResultVO = apiService.searchItemPage(itemQuery);
		// 有异常返回
		if (!Constants.API_RETURN_OK.equals(rapiResultVO.getCode())) {
			logger.error("调度任务：商品状态同步ApiService.searchItemPage 分页查询总数失败！shopCode=" + shopCode + "页码：" + 1 + "上下架状态：" + wareStatus);
			return;
		}

		int iTotal = rapiResultVO.getTotal();

		int pageNum = 1;

		if (iTotal > pageSize) {
			if (iTotal % pageSize == 0) {
				pageNum = iTotal / pageSize;
			} else {
				pageNum = (iTotal / pageSize) + 1;
			}
		}

		String onLineStatus = null;
		String localStatus = null;
		String goodsSn = null;
		ApiResultVO<List<ChannelApiGoods>> apiResultVO = null;

		// 存放线上存在而渠道上不存在的商品编码
//		List<String> needDownGoodsSnList = new ArrayList<String>();

		for (int j = 1; j <= pageNum; j++) {
			try {
				itemQuery.setPage(j);
				apiResultVO = apiService.searchItemPage(itemQuery);
				List<ChannelApiGoods> channelApiGoodsList = apiResultVO.getApiGoods();

				for (ChannelApiGoods channelApiGoods : channelApiGoodsList) {
					try {
						goodsSn = channelApiGoods.getGoodsSn();
						if (StringUtil.isEmpty(goodsSn)) {
							logger.error("shopCode:" + shopCode + ",goodsSn 为空,需要外渠人员将商品信息补全！");
							continue;
						}
//						if (localGoodsSnList.contains(goodsSn.trim())) {
//							localGoodsSnList.remove(goodsSn.trim());
//						}
						//onLineStatus = wareStatus;
						onLineStatus = channelApiGoods.getStatus();
						
						String statusName = "1".equals(onLineStatus) ? "上架" : "下架";
						ChannelGoods channelGoods = getChannelGoods(shopCode, channelApiGoods.getGoodsSn());
						String message = "";
						if (channelGoods != null) { // 商品存在
							localStatus = String.valueOf(channelGoods.getIsOnSell());
							if (isSameStatus(onLineStatus, localStatus)) { // 商品上下架状态相同
								logger.debug(channelApiGoods.getGoodsSn() + "上下架状态一致!");
								continue;
							}
							// 商品上下架状态不相同
							String status = "1".equals(onLineStatus) ? "1" : "0";

							// 同步本地商品状态
							try {
								channelGoods.setIsOnSell(Byte.valueOf(status));
								channelGoods.setLastUpdate(new Date());
								channelGoods.setLastUpdateTime((int) TimeUtil.parseDateToNumeric(new Date()));
								int count = channelGoodsMapper.updateByPrimaryKeySelective(channelGoods);
								if (count != 1) {
									message = "调度任务:[" + channelApiGoods.getGoodsSn() + "][" + statusName + "]状态同步至渠道失败!";
								} else {
									message = "调度任务:[" + channelApiGoods.getGoodsSn() + "][" + statusName + "]状态同步至渠道成功!";
								}
							} catch (Exception e) {
								logger.error("调度任务:[" + channelApiGoods.getGoodsSn() + "][" + statusName + "]状态同步至渠道异常", e);
								message = "调度任务:[" + channelApiGoods.getGoodsSn() + "][" + statusName + "]状态同步至渠道异常!"
										+ e.getMessage();
							}
							// 同步统一库存商品状态
//							synchronousToERP(channelCode, shopCode, goodsSn.trim(), status, message);
						} else { // 商品不存在
							message = "调度任务：[渠道商品表:goodsSn=" + channelApiGoods.getGoodsSn() + "]不存在!";
							// 如果线上商品为上架状态，就将线上商品状态改为下架
//							if ("1".equals(wareStatus)) {
//								needDownGoodsSnList.add(goodsSn);
//							}
//							String status = "0";
							
							String errorMsg = "";
							onLineStatus = channelApiGoods.getStatus();
							// 商品上下架状态不相同
							String status = "1".equals(onLineStatus) ? "1" : "0";
							
							List<BGproductGoodsWithBLOBs> productGoodsList = this.searchProductGoods(goodsSn,shopCode);
							if (StringUtil.isListNotNull(productGoodsList)) {
								//做进货操作
								int insertRow = 0;
								try {
									insertRow = this.insertChannelGoods(productGoodsList, shopCode, status);
									if (1 == insertRow) {
										// 同步统一库存商品状态
//										synchronousToERP(channelCode, shopCode, goodsSn.trim(), status, message);
										continue;
									}
								} catch (Exception e) {
									errorMsg = "调度任务-上下架比对，shopCode["+shopCode+"],[goodsSn=" + goodsSn + "] 进货操作异常:errorMsg="+e.getMessage();
								}
							}else{
								errorMsg = "调度任务-上下架比对，shopCode["+shopCode+"],[goodsSn=" + goodsSn + "] 线上商品存在，而在productGoods表中不存在";
							}
							this.insertChannelErpUpdownLog(goodsSn, status, channelCode, shopCode, errorMsg);
							
						}
					} catch (Exception e) {
						logger.error("调度任务：商品状态同步[goodsSn =" + channelApiGoods.getGoodsSn() 
								+ " , 上下架状态：" + wareStatus + "]异常! ", e);
					}
				}
			} catch (Exception e) {
				logger.error("调度任务：商品状态同步[页码 = " + j + " , 上下架状态 = " + wareStatus + "]异常! ", e);
			}
		}

	}

	private List<String> getLocalGoodsSnList(String shopCode) {
		ChannelGoodsExample example = new ChannelGoodsExample();
		example.or().andChannelCodeEqualTo(shopCode);
		List<ChannelGoods> goodsList = channelGoodsMapper.selectByExample(example);
		List<String> goodsSnList = new ArrayList<String>();
		for (ChannelGoods goods : goodsList) {
			goodsSnList.add(goods.getGoodsSn());
		}
		return goodsSnList;
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

	private List<BGproductGoodsWithBLOBs> searchProductGoods(String goodsSn,String shopCode){
		BGproductGoodsExample productExample = new BGproductGoodsExample();
		com.work.shop.bean.bgcontentdb.BGproductGoodsExample.Criteria criteria = productExample.or();
		criteria.andGoodsSnEqualTo(goodsSn);
		List<BGproductGoodsWithBLOBs> list = new ArrayList<BGproductGoodsWithBLOBs>();
		try {
			list = productGoodsMapper
					.selectByExampleWithBLOBs(productExample); // 根据goodsSn查询导入商品
		} catch (Exception e) {
			logger.error("记录日志：调度任务[goodsSn =" + goodsSn + "] 查询productGoods异常:", e);
		}
		return list;
	}
	
	
	private int insertChannelGoods(List<BGproductGoodsWithBLOBs> list,String shopCode,String status) throws Exception{

		int ii = 0;
		
			ChannelGoods channelGoods = new ChannelGoods();
			
			channelGoods.setChannelCode(shopCode); // 这里shopcode对应那边的channelcode
			Long addTime = TimeUtil
					.parseDateToNumeric(new Date());
			if (addTime != null) {
				channelGoods.setAddTime(Integer
						.parseInt(addTime.toString()));
			}
			channelGoods.setPresellOrder((short) 0);
			if (list.get(0).getAllowSearch() != null) {
				channelGoods.setAllowSearch(list.get(0)
						.getAllowSearch()==1?1:0);
			} else {
				channelGoods.setAllowSearch(1);
			}
			if (list.get(0).getBestOrder() != null) {
				channelGoods.setBestOrder(list.get(0)
						.getBestOrder().shortValue());
			} else {
				channelGoods.setBestOrder((short) 0);
			}
			channelGoods.setCardPicture(list.get(0)
					.getCardPicture());
			channelGoods.setChannelPrice(list.get(0)
					.getMarketPrice());
			if (list.get(0).getGoodsName() != null) {
				channelGoods.setGoodsName(list.get(0)
						.getGoodsName());
			} else {
				channelGoods.setGoodsName("");
			}

			channelGoods.setGoodsSn(list.get(0).getGoodsSn());
			if (list.get(0).getGoodsTitle() == null) {
				channelGoods.setGoodsTitle("");
			} else {
				channelGoods.setGoodsTitle(list.get(0)
						.getGoodsTitle());
			}
			channelGoods.setHotNumber(null);

			if (list.get(0).getHotOrder() != null) {
				channelGoods.setHotOrder(list.get(0)
						.getHotOrder().shortValue());
			} else {
				channelGoods.setHotOrder((short) 0);
			}

			if (list.get(0).getIsBest() != null) {
				channelGoods.setIsBest(list.get(0)
						.getIsBest().byteValue());
			} else {
				channelGoods.setIsBest((byte) 0);
			}

			if (list.get(0).getIsHot() != null) {
				channelGoods.setIsHot(list.get(0)
						.getIsHot().byteValue());
			} else {
				channelGoods.setIsHot((byte) 0);
			}

			if (list.get(0).getIsNew() != null) {
				channelGoods.setIsNew(list.get(0)
						.getIsNew().byteValue());
			} else {
				channelGoods.setIsNew((byte) 0);
			}

			channelGoods.setIsOnSell(Byte.parseByte(status));
			
			if (list.get(0).getIsOutlets() != null) {
				channelGoods.setIsOutlet(list.get(0)
						.getIsOutlets().byteValue());

			} else {
				channelGoods.setIsOutlet((byte) 0);
			}
			channelGoods.setIsPresale(null);
			
			channelGoods.setIsPresell(0);

			channelGoods.setIsSecondSale(null);
			channelGoods.setIsUpdate((byte) 0);
			channelGoods.setKeywords(list.get(0).getKeywords());
			
			channelGoods.setLastUpdateTime(0);
			if (list.get(0).getNewOrder() != null) {
				channelGoods.setNewOrder(list.get(0)
						.getNewOrder().shortValue());
			} else {
				channelGoods.setNewOrder((short) 0);
			}
			
			if(list.get(0)
					.getMarketPrice() != null ){
				channelGoods.setPlatformPrice(list.get(0)
						.getMarketPrice());
			} else{
				channelGoods.setPlatformPrice(new BigDecimal(0.00));
			}

			channelGoods.setPlatformPrice(list.get(0)
					.getMarketPrice());
			if (list.get(0).getSalePoint() != null
					&& list.get(0).getSalePoint().length() > 250) {
				channelGoods.setSalePoint(list.get(0)
						.getSalePoint().substring(0, 250));
			} else {
				channelGoods.setSalePoint(list.get(0)
						.getSalePoint());
			}
			channelGoods.setSizePicture(list.get(0)
					.getSizePicture());
			if (list.get(0).getSortOrder() != null) {
				channelGoods.setSortOrder(list.get(0)
						.getSortOrder().shortValue());
			} else {
				channelGoods.setSortOrder((short) 0);
			}
			if (list.get(0).getWarnNumber() != null) {
				channelGoods.setWarnNumber(list.get(0)
						.getWarnNumber());
			} else {
				channelGoods.setWarnNumber(1);
			}
			channelGoods.setTplId(0);
			channelGoods.setLimitNumber(0);
			channelGoods.setCanDiscount((byte) 1);
			channelGoods.setGoodsDesc(list.get(0)
					.getGoodsDesc());
			//2017-5-26 add by liyinghua 唯品会渠道默认不同步库存
			if("HQ01S155".equals(shopCode)){
				channelGoods.setIsSyncStock((byte)0);
			}
			ii = channelGoodsMapper.insertSelective(channelGoods);
		
		return ii;
	}
	
	private void insertChannelErpUpdownLog(String goodsSn,String status,String channelCode,String shopCode,String errorMsg){
		
//		sb.append("产品表中未查询到商品编号：" + goodsSn);
		/********** 记录异常日志 *********/
//		ChannelApiLog channelApiLog = new ChannelApiLog();
//		channelApiLog.setChannelCode(channelCode);
//		channelApiLog.setShopCode(shopCode);
//		channelApiLog.setMethodName("10");
//		channelApiLog.setParamInfo(goodsSn);
//		channelApiLog.setIpAddress("");
//		channelApiLog.setUser("System");
//		channelApiLog.setRequestTime(new Date());
//		channelApiLog.setReturnCode("1"); // 失败
//		channelApiLog.setReturnMessage("异常信息：" + errorMsg);
//		
//		try {
//			channelApiLogMapper.insert(channelApiLog); // 日志记录
//		} catch (Exception e) {
//			logger.error("日志记录异常", e);
//		}
//		
		ChannelErpUpdownLog updownLog = new ChannelErpUpdownLog();
		updownLog.setChannelCode(channelCode);
		updownLog.setShopCode(shopCode);
		updownLog.setCode("0");
		updownLog.setMessage(errorMsg);
		updownLog.setGoodsSn(goodsSn);
		updownLog.setStatus(status);
		updownLog.setType("1");
		updownLog.setRequestTime(new Date());
		updownLog.setUserId(USER_NAME);
		logger.info(updownLog.getMessage());
		try {
			channelErpUpdownLogMapper.insertSelective(updownLog);
		} catch (Exception e) {
			logger.error("记录日志：调度任务[goodsSn =" + goodsSn + "] 上下架状态同步至统一库存 异常:", e);
		}
		
	}
	
}
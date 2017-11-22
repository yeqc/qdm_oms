package com.work.shop.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import au.com.bytecode.opencsv.CSVReader;

import com.work.shop.api.service.ChannelApiService;
import com.work.shop.bean.ChannelErpUpdownLog;
import com.work.shop.bean.ChannelGoods;
import com.work.shop.bean.ChannelGoodsExample;
import com.work.shop.bean.bgcontentdb.BGproductGoodsExample;
import com.work.shop.bean.bgcontentdb.BGproductGoodsWithBLOBs;
import com.work.shop.dao.ChannelApiLogMapper;
import com.work.shop.dao.ChannelErpUpdownLogMapper;
import com.work.shop.dao.ChannelGoodsMapper;
import com.work.shop.dao.TaoBaoO2OShopMapper;
import com.work.shop.dao.bgcontentdb.BGproductGoodsMapper;
import com.work.shop.vo.JsonResult;

@Service
public class SynChannelGoodsExcelUtil {
	@Resource(name="bGproductGoodsMapper")
	private BGproductGoodsMapper productGoodsMapper;
	
	@Resource(name="channelGoodsMapper")
	private ChannelGoodsMapper channelGoodsMapper;
	
	@Resource(name="channelApiLogMapper")
	private ChannelApiLogMapper channelApiLogMapper;
	
	@Resource
	private ChannelErpUpdownLogMapper channelErpUpdownLogMapper;
	
	@Resource
	private TaoBaoO2OShopMapper taoBaoO2OShopMapper;

	public final static String REG_DIGIT = "[0-9]*";
	
	
//	private static final String erpUrl = PropertieFileReader.getString("ERP_STOCK_CENTER_URL");

	//private static final Integer PAGE_SIZE = 50;

	private static Logger log = Logger.getLogger(ChannelApiService.class);

	public boolean isDigit(String str) { //判断字符串是否全为数字
		return str.matches(REG_DIGIT);
	}
	
	/**
	 * 读取csv文件内容 
	 * shopCode 必须要有
	 * @return
	 * @throws IOException
	 * 输入/输出(i/o)异常
	 */
	public JsonResult readCsv(InputStream is, String channelCode, String shopCode,
			StringBuffer str, String userName) throws IOException {
		CSVReader reader = new CSVReader(new InputStreamReader(is, "GBK"));
		Map<String, String> map = new HashMap<String, String>();
		JsonResult jsonResult = new JsonResult();
		List<BGproductGoodsWithBLOBs> list = new ArrayList<BGproductGoodsWithBLOBs>();
		int i = 1;
		String[] nextLine;
		int count = 0;
		int rowNumFlag = 0;
		int j = 0;
		String ticketCode = "CG"+StringUtil.getSysCode();
		try {
			while ((nextLine = reader.readNext()) != null) {
				if (j != 0) {/*
					rowNumFlag += 1;
					StringBuffer sb = new StringBuffer("");
					String goodsSn = nextLine[0];
					if (goodsSn == null) {
						continue;
					}
					goodsSn = goodsSn.trim();
					String gs = "";
					if (goodsSn.indexOf(".") == -1) {
						gs = goodsSn;
					} else {
						gs = goodsSn.substring(0, goodsSn.indexOf("."));
					}
					if (map.get(gs) != null) {
						count += 1;
						continue;
					}
					map.put(gs, gs);
					String productTitle = nextLine[1];// 产品次标题
					String title = "";
					if (productTitle != null) {
						if (productTitle.indexOf(".") == -1) {
							title = productTitle;
						} else {
							title = productTitle.substring(0,
									productTitle.indexOf("."));
						}
					}
					Integer status;
					byte isSyncStock = 0;
					List<StockChannelStatusBean> list1 = new ArrayList<StockChannelStatusBean>();
					BGproductGoodsExample productExample = new BGproductGoodsExample();
					Criteria criteria = productExample.or();
					criteria.andGoodsSnEqualTo(gs);
					list = productGoodsMapper.selectByExampleWithBLOBs(productExample); // 根据goodsSn查询导入商品
					if (list != null && list.size() > 0) {
						List<ChannelGoods> listChannelGoods = null;
						try {
							ChannelGoodsExample channelGoodsExample = new ChannelGoodsExample();
							ChannelGoodsExample.Criteria channelGoodsCriteria = channelGoodsExample.or();
							channelGoodsCriteria.andChannelCodeEqualTo(shopCode);
							if (!StringUtil.isNotNull(list.get(0).getGoodsSn())) { // 商品编号为空继续下次处理
								continue;
							}
							channelGoodsCriteria.andGoodsSnEqualTo(list.get(0).getGoodsSn());
							listChannelGoods = channelGoodsMapper.selectByExample(channelGoodsExample);
							ChannelGoods channelGoods = new ChannelGoods();
							if (listChannelGoods != null
									&& listChannelGoods.size() > 0) { // 已经存在的数据不做任何操作
								count += 1;
								isSyncStock = listChannelGoods.get(0).getIsSyncStock();
							} else { // 新增操作
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

								if (list.get(0).getIsOnSell() != null) {
									channelGoods.setIsOnSell(list.get(0)
											.getIsOnSell().byteValue());
									status = list.get(0).getIsOnSell();
								} else {
									channelGoods.setIsOnSell((byte) 0);
									status = 0;
								}
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
								// channelGoods.setLastUpdate(TimeUtil.parseString2Date(TimeUtil.getDate()));
								// //修改时间
								// Long lastUpdateTime =
								// TimeUtil.parseDateToNumeric(new
								// Date());
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
								
								String goodsDesc = list.get(0).getGoodsDesc();
								if (StringUtil.isNotEmpty(goodsDesc) && goodsDesc.length() > 200) {
									goodsDesc = goodsDesc.substring(0, 199);
								}
								channelGoods.setGoodsDesc(goodsDesc);
								channelGoods.setIsOnlineOffline((byte)1);  //默认线上款
								//2017-5-26 add by liyinghua 唯品会渠道默认不同步库存
								if("HQ01S155".equals(shopCode)){
									isSyncStock = 0;
								} else {
									isSyncStock = 1;
								}
								channelGoods.setIsSyncStock(isSyncStock);
								int ii = channelGoodsMapper.insertSelective(channelGoods);
								if(1==ii){
									// 上下架同步成功后将状态同步至统一库存.
									goodsSn =  channelGoods.getGoodsSn();
									StockChannelStatusBean stockChannelStatusBean = new StockChannelStatusBean();
									stockChannelStatusBean.setCreateTime(new Date());
									stockChannelStatusBean.setUpdateTime(new Date());
									stockChannelStatusBean.setSixProdId(goodsSn);
									stockChannelStatusBean.setUpdateBy(userName);
									stockChannelStatusBean.setCreateBy(userName);
									stockChannelStatusBean.setChannelCode(shopCode);
									stockChannelStatusBean.setIsSync(isSyncStock);
									stockChannelStatusBean.setSaleStatus(String.valueOf(status));
									list1.add(stockChannelStatusBean);
									if (!StringUtil.isNotNullForList(list1)) {
										continue;
									}
									String goodsSnInfo = JSON.toJSONString(list1);
									Date requestTime = new Date();
									String resultCode = "0";
									String msg = "";
									try {
										log.info("回写统一库存请求参数:"+ goodsSnInfo);
										Message message = stockChannelStatusFacade.setStockChannelStatusList(list1);
										log.info("回写统一库存返回结果：" + JSON.toJSONString(message));
										if (message.getIsSetData()) {
											StringBuffer sb2 = new StringBuffer();
											sb2.append("店铺进货：");
											for(StockChannelStatusBean bean:list1){
												sb2.append(bean.getChannelCode()+";");
											}
											String stockStatus = (isSyncStock == 0 ? "关闭" : "开启");
											msg = sb2.toString()+ "库存同步[" + stockStatus + "]上下架状态同步至统一库存成功！";
											resultCode = "1";
										} else {
											msg = "店铺进货：" + goodsSn + "上下架状态同步至统一库存失败！";
										}
									} catch (Exception e) {
										log.error(goodsSn + goodsSnInfo + "上下架状态同步统一库存失败：" + e);
										msg = goodsSnInfo + "上下架状态同步至统一库存失败：" + e;
										resultCode = "0";
									} finally {
										ChannelErpUpdownLog updownLog = new ChannelErpUpdownLog();
										updownLog.setChannelCode(channelCode);
										updownLog.setShopCode(shopCode);
										updownLog.setCode(resultCode);
										if (StringUtil.isNotEmpty(msg) && msg.length() > 255) {
											updownLog.setMessage(msg.substring(0, 255));
										} else {
											updownLog.setMessage(msg);
										}
										updownLog.setGoodsSn(goodsSn);
										updownLog.setStatus(String.valueOf(status));
										updownLog.setType("1");
										updownLog.setRequestTime(requestTime);
										updownLog.setUserId(userName);
										try {
											this.channelErpUpdownLogMapper.insertSelective(updownLog);
										} catch (Exception e2) {
											log.error("记录日志：上下架状态同步至统一库存 异常:", e2);
										}
									}
								}
							}
						} catch (Exception e) {
							sb.append("[" + list.get(0).getGoodsSn() + "]同步异常,");
							log.error("[" + list.get(0).getGoodsSn() + "]同步异常:", e);
						} finally {
							*//********** 每1记录一次日志 *********//*
							if (!StringUtil.isNotNullForList(listChannelGoods)) {
								ChannelApiLog channelApiLog = new ChannelApiLog();
								channelApiLog.setChannelCode(channelCode);
								channelApiLog.setShopCode(shopCode);
								channelApiLog.setMethodName("10");
								channelApiLog.setParamInfo(ticketCode+ "-" + gs);
								channelApiLog.setIpAddress("");
								channelApiLog.setUser(userName);
								channelApiLog.setRequestTime(new Date());
								if ("".equals(sb.toString())) {
									count += 1;
									channelApiLog.setReturnCode("0"); // 成功
									channelApiLog.setReturnMessage("店铺经营商品生成成功！");
								} else {
									channelApiLog.setReturnCode("1"); // 失败
									channelApiLog.setReturnMessage("异常信息：" + sb.toString());
								}
								try {
									channelApiLogMapper.insert(channelApiLog); // 日志记录
								} catch (Exception e) {
									log.error("日志记录异常", e);
								}
							}
						}
					} else {
						sb.append("产品表中未查询到商品编号：" + gs);
						*//********** 每1记录一次日志 *********//*
						ChannelApiLog channelApiLog = new ChannelApiLog();
						channelApiLog.setChannelCode(channelCode);
						channelApiLog.setShopCode(shopCode);
						channelApiLog.setMethodName("10");
						channelApiLog.setParamInfo(ticketCode+ "-" + gs);
						channelApiLog.setIpAddress("");
						channelApiLog.setUser(userName);
						channelApiLog.setRequestTime(new Date());
						if ("".equals(sb.toString())) {
							count += 1;
							channelApiLog.setReturnCode("0"); // 成功
							channelApiLog.setReturnMessage("店铺经营商品生成成功！");
						} else {
							channelApiLog.setReturnCode("1"); // 失败
							channelApiLog.setReturnMessage("异常信息：" + sb.toString());
						}
						try {
							channelApiLogMapper.insert(channelApiLog); // 日志记录
						} catch (Exception e) {
							log.error("日志记录异常", e);
						}
					}
				*/}
				i++;
				j++;
			}
		} catch (Exception e) {
			log.error("读取csv文件内容异常", e);
		} finally {
			if (null != reader) {
				reader.close();
				reader = null;
			}
		}
		if (rowNumFlag == count) {
			jsonResult.setIsok(true);
			jsonResult.setMessage("导入成功！");
		} else {
			jsonResult.setIsok(false);
			jsonResult.setMessage("导入完成！导入数据部分存在异常，请到日志中查询具体原因！");
		}
		return jsonResult;
	}


	/**
	 * 更新邦购渠道商品的上下架状态
	 * shopCode 必须要有
	 * @return
	 * @throws IOException
	 * 输入/输出(i/o)异常
	 */
	public JsonResult readGoodsUpDownCsv(String channelCode,String shopCode, String[] goodsSns, String status) throws IOException {

		JsonResult jsonResult = new JsonResult();
		List<BGproductGoodsWithBLOBs> list = new ArrayList<BGproductGoodsWithBLOBs>();

		String goodsSnIndex = "";
		StringBuffer errorGoodsSnList = new StringBuffer("");
		
		try {
//			String tranShopCode = shopCode;
//			if ("A02588S001".equals(shopCode)) {
//				tranShopCode = "HQ01S116";
//			}
			for (int i = 0; i < goodsSns.length; i++) {
				String goodsSn = goodsSns[i];
				if (goodsSn == null) {
					continue;
				}
				byte isSyncStock = 0;
				goodsSnIndex = goodsSn;
//				List<UpDownErp> list1 = null;
				BGproductGoodsExample productExample = new BGproductGoodsExample();
				com.work.shop.bean.bgcontentdb.BGproductGoodsExample.Criteria criteria = productExample.or();
				criteria.andGoodsSnEqualTo(goodsSn);
				//1、 根据goodsSn查询商品表中的商品
				list = productGoodsMapper.selectByExampleWithBLOBs(productExample); 
				if (list != null && list.size() > 0) {
					List<ChannelGoods> listChannelGoods = null;
					try {
						ChannelGoodsExample channelGoodsExample = new ChannelGoodsExample();
						ChannelGoodsExample.Criteria channelGoodsCriteria = channelGoodsExample.or();
						channelGoodsCriteria.andChannelCodeEqualTo(shopCode);
						if (!StringUtil.isNotNull(list.get(0).getGoodsSn())) { // 商品编号为空继续下次处理
							continue;
						}
						channelGoodsCriteria.andGoodsSnEqualTo(list.get(0).getGoodsSn());
						listChannelGoods = channelGoodsMapper.selectByExample(channelGoodsExample);
						ChannelGoods channelGoods = new ChannelGoods();
						//2、更新渠道商品的上下架状态
						if (listChannelGoods != null && listChannelGoods.size() > 0) { // 已经存在的数据，直接更新上下架状态
							ChannelGoods selectGoods = listChannelGoods.get(0);
							channelGoods.setId(selectGoods.getId());
							channelGoods.setIsOnSell(Byte.parseByte(status));
							Long lastUpdateTime = TimeUtil.parseDateToNumeric(new Date());
							if (lastUpdateTime != null) {
								channelGoods.setLastUpdateTime(Integer.parseInt(lastUpdateTime.toString()));
							}
							channelGoods.setLastUpdate(TimeUtil.parseString2Date(TimeUtil.getDate()));
							channelGoodsMapper.updateByPrimaryKeySelective(channelGoods);
							isSyncStock = selectGoods.getIsSyncStock();
						} else { // 新增操作，进货处理
							fullChannelGoods(list,channelGoods,shopCode,Byte.parseByte(status));
							channelGoodsMapper.insertSelective(channelGoods);
							isSyncStock = channelGoods.getIsSyncStock();
						}
						//3、 上下架同步成功后将状态同步至统一库存.
						// 设置Post请求的表单参数对象
//						list1 = new ArrayList<UpDownErp>();
//						UpDownErp upDownErp = new UpDownErp(shopCode, goodsSn, Integer.parseInt(status));
//						list1.add(upDownErp);
//						if (!StringUtil.isNotNullForList(list1)) {
//							continue;
//						}
//						String goodsSnInfo = JSON.toJSONString(list1);
//						Date requestTime = new Date();
						String resultCode = "0";
						String msg = "";
						try {
//							List<org.apache.http.NameValuePair> valuePairs = new ArrayList<org.apache.http.NameValuePair>();
//							valuePairs.add(new BasicNameValuePair("goodsSnInfo", goodsSnInfo));
//							String jsonStr = HttpClientUtil.post(erpUrl, valuePairs);
//							if (StringUtil.isNotEmpty(jsonStr)) {
//								try {
//									@SuppressWarnings("unchecked")
//									Map<String, String> resultMap = JSONObject.parseObject(jsonStr, Map.class);
//									if (null != resultMap && !resultMap.isEmpty()) {
//										resultCode = resultMap.get("flag");
//										if ("1".equals(resultMap.get("flag"))) {
//											msg = "上下架状态同步至统一库存成功";
//										} else {
//											msg = goodsSnInfo + resultMap.get("msg");
//										}
//									}
//								} catch (Exception e) {
//									log.error(goodsSn + jsonStr + "返回结果转换JSON异常", e);
//									msg = goodsSn + jsonStr + "返回结果转换JSON异常:" + e.getMessage() ;
//								}
//							} else {
//								msg = goodsSn + "返回结果为空！";
//							}
							/*StockChannelStatusBean stockChannelStatusBean = new StockChannelStatusBean();
							stockChannelStatusBean.setChannelCode(shopCode);
							stockChannelStatusBean.setUpdateTime(new Date());
							stockChannelStatusBean.setSixProdId(goodsSn);
							stockChannelStatusBean.setCreateTime(new Date());
							stockChannelStatusBean.setSaleStatus(status);
							stockChannelStatusBean.setIsSync((byte)1);
							stockChannelStatusBean.setCreateBy("System");
							stockChannelStatusBean.setUpdateBy("System");
							log.info("回写统一库存请求参数:" +JSON.toJSONString(stockChannelStatusBean));
							Message response = stockChannelStatusFacade.setStockChannelStatus(stockChannelStatusBean);
							log.info("回写统一库存：" + JSON.toJSONString(response));
							if (response.getIsSetData()) {
								String stockStatus = (isSyncStock == 0 ? "关闭" : "开启");

								msg = "库存同步[" + stockStatus + "]上下架状态同步至统一库存成功";
								resultCode = "1";
							} else {
								msg = goodsSn + "上下架状态同步至统一库存失败！";
							}*/
						} catch (Exception e) {
							e.printStackTrace();
							log.error("上下架状态同步至统一库存失败：" + e);
							msg = "上下架状态同步至统一库存 异常：" + e.getMessage();
							resultCode = "0";
							errorGoodsSnList.append("goodsSn[").append(goodsSn).append("]:").append(msg).append(";");
						} finally {
							if (StringUtil.isNotEmpty(msg) && msg.length() > 255) {
								msg = msg.substring(0, 255);
							}
							insertErpUpDownLog(channelCode,shopCode,resultCode,msg,goodsSn,String.valueOf(status));
						}
					} catch (Exception e) {
						String msg = "异常信息：更新邦购渠道上下架状态-->"+e.getMessage();
						if ( msg.length() > 255) {
							msg = msg.substring(0, 255);
						}
						errorGoodsSnList.append("goodsSn[").append(goodsSn).append("]:").append(msg).append(";");
						insertErpUpDownLog(channelCode, shopCode,"0",msg,goodsSn,String.valueOf(status));
		
						log.error("[" + list.get(0).getGoodsSn() + "]同步异常:", e);
					} 
				} else {
					errorGoodsSnList.append("goodsSn[").append(goodsSn).append("]:").append("商品编号在产品表中不存在;");
					String msg = "产品表中没有查询到商品编号：" + goodsSn;
					insertErpUpDownLog(channelCode, shopCode,"0",msg,goodsSn,String.valueOf(status));
				}
			}
		} catch (Exception e) {
			String msg = "异常信息：根据goodsSn查询产品表的商品-->"+e.getMessage();
			if ( msg.length() > 255) {
				msg = msg.substring(0, 255);
			}
			insertErpUpDownLog(channelCode,shopCode,"0",msg,goodsSnIndex,String.valueOf(status));
			log.error("更新邦购渠道上下架状态异常", e);
		} 
		
		if ("".equals(errorGoodsSnList.toString())) {
			jsonResult.setIsok(true);
			jsonResult.setMessage("更新邦购渠道上下架状态成功！");
		}else {
			jsonResult.setIsok(false);
			jsonResult.setMessage(errorGoodsSnList.toString());
		}
		
		return jsonResult;
	}
	
	private void insertErpUpDownLog(String channelCode,String shopCode,String code,String msg,String goodsSn,String status){
		try {
			ChannelErpUpdownLog updownLog = new ChannelErpUpdownLog();
			updownLog.setChannelCode(channelCode);
			updownLog.setShopCode(shopCode);
			updownLog.setCode(code);
			updownLog.setMessage(msg);
			updownLog.setGoodsSn(goodsSn);
			updownLog.setStatus(String.valueOf(status));
			updownLog.setType("1");
			Date requestTime = new Date();
			updownLog.setRequestTime(requestTime);
			updownLog.setUserId("System");
		
			this.channelErpUpdownLogMapper.insertSelective(updownLog);
		} catch (Exception e2) {
			log.error("记录日志：上下架状态日志ChannelErpUpdownLog.Insert异常:", e2);
		}
    }
	
    /**
     * 得到Excel表中的值
     * 
     * @param hssfCell
     *            Excel中的每一个格子
     * @return Excel中每一个格子中的值
     */
//    @SuppressWarnings("static-access")
//    private  String getValue(HSSFCell hssfCell) {
//        if (hssfCell.getCellType() == hssfCell.CELL_TYPE_BOOLEAN) {
//            // 返回布尔类型的值
//            return String.valueOf(hssfCell.getBooleanCellValue());
//        }else if (hssfCell.getCellType() == hssfCell.CELL_TYPE_NUMERIC) {
//            // 返回数值类型的值
//            return String.valueOf(hssfCell.getNumericCellValue());
//        }else {
//            // 返回字符串类型的值
//            return String.valueOf(hssfCell.getStringCellValue());
//        }
//    }
	
	private void fullChannelGoods(List<BGproductGoodsWithBLOBs> list,ChannelGoods channelGoods,String shopCode,byte status){
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

		channelGoods.setIsOnSell(status);
			
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
		String goodsDesc = list.get(0).getGoodsDesc();
		if (StringUtil.isNotEmpty(goodsDesc) && goodsDesc.length() > 200) {
			goodsDesc = goodsDesc.substring(0, 199);
		}
		channelGoods.setGoodsDesc(goodsDesc);
		//2017-5-26 add by liyinghua 唯品会渠道默认不同步库存
		if("HQ01S155".equals(shopCode)){
			channelGoods.setIsSyncStock((byte)0);
		} else {
			channelGoods.setIsSyncStock((byte)1);
		}
	}
	
	
	/**
	 * 读取csv文件内容 
	 * shopCode 必须要有
	 * @return
	 * @throws IOException
	 * 输入/输出(i/o)异常
	 */
	public JsonResult readCsv2(InputStream is, String channelCode, String shopCode,
			StringBuffer str, String userName) throws IOException {
		CSVReader reader = new CSVReader(new InputStreamReader(is, "GBK"));
		Map<String, String> map = new HashMap<String, String>();
		JsonResult jsonResult = new JsonResult();
		List<ChannelGoods> list = new ArrayList<ChannelGoods>();
		int i = 1;
		String[] nextLine;
		int count = 0;
		int rowNumFlag = 0;
		int j = 0;
		String ticketCode = "CG"+StringUtil.getSysCode();
		try {
			while ((nextLine = reader.readNext()) != null) {
				if (j != 0) {/*
					rowNumFlag += 1;
					StringBuffer sb = new StringBuffer("");
					String goodsSn = nextLine[0];
					if (goodsSn == null) {
						continue;
					}
					goodsSn = goodsSn.trim();
					String gs = "";
					if (goodsSn.indexOf(".") == -1) {
						gs = goodsSn;
					} else {
						gs = goodsSn.substring(0, goodsSn.indexOf("."));
					}
					if (map.get(gs) != null) {
						count += 1;
						continue;
					}
					map.put(gs, gs);
					String status = "0";
					List<StockChannelStatusBean> beanList = null; 
					String isSyncStock = nextLine[1];
					if(StringUtils.isBlank(isSyncStock)){
						isSyncStock = "1";
					}
					if(!isSyncStock.equals("0") && !isSyncStock.equals("1")){
						isSyncStock = "1";
					}
					TaoBaoO2OShopExample example=new TaoBaoO2OShopExample();
					example.or().andShopCodeEqualTo(shopCode);
					List<TaoBaoO2OShop> o2oShopList = taoBaoO2OShopMapper.selectByExample(example);
					ChannelGoodsExample channelGoodsExample = new ChannelGoodsExample();
					channelGoodsExample.or().andGoodsSnEqualTo(gs).andChannelCodeEqualTo(shopCode);
					List<ChannelGoods> channelGoodsList = channelGoodsMapper.selectByExample(channelGoodsExample);
				if(CollectionUtils.isNotEmpty(channelGoodsList)){
					try {
						status = String.valueOf(channelGoodsList.get(0).getIsOnSell());
						int ii = 0;
						List<String> shopCodeList = new ArrayList<String>();
						if(CollectionUtils.isNotEmpty(o2oShopList)){
							shopCodeList.add(shopCode);
							if(channelGoodsList.get(0).getIsOnlineOffline()==3){
							for(TaoBaoO2OShop o2oShop: o2oShopList){
								shopCodeList.add(o2oShop.getRelatingShopCode());
							}
						  }
						}else{
							shopCodeList.add(shopCode);
						}
						ChannelGoods channelGoods = new ChannelGoods();
						channelGoods.setIsSyncStock(Byte.valueOf(isSyncStock));
						ChannelGoodsExample channelGoodsExample2 = new ChannelGoodsExample();
						com.work.shop.bean.ChannelGoodsExample.Criteria or = channelGoodsExample2.or();
						or.andChannelCodeIn(shopCodeList).andGoodsSnEqualTo(gs);
						ii = channelGoodsMapper.updateByExampleSelective(channelGoods, channelGoodsExample2);
						
							if(ii>0){
								count += 1;
								// 上下架同步成功后将状态同步至统一库存.
								beanList = new ArrayList<StockChannelStatusBean>();
								StockChannelStatusBean stockChannelStatusBean = new StockChannelStatusBean();
								stockChannelStatusBean.setUpdateTime(new Date());
								stockChannelStatusBean.setSixProdId(gs);
								stockChannelStatusBean.setCreateTime(new Date());
								stockChannelStatusBean.setSaleStatus(status);
								stockChannelStatusBean.setIsSync(Byte.valueOf(isSyncStock));
								stockChannelStatusBean.setCreateBy(userName);
								stockChannelStatusBean.setUpdateBy(userName);
								if(CollectionUtils.isNotEmpty(o2oShopList)){
									stockChannelStatusBean.setChannelCode(shopCode);
									beanList.add(stockChannelStatusBean);
									if(channelGoodsList.get(0).getIsOnlineOffline()==3){
									for(TaoBaoO2OShop o2oShop:o2oShopList){
										StockChannelStatusBean bean = new StockChannelStatusBean();
										BeanUtils.copyProperties(bean, stockChannelStatusBean);
										bean.setChannelCode(o2oShop.getRelatingShopCode());
										beanList.add(bean);
									}
								  }
								}else{
									stockChannelStatusBean.setChannelCode(shopCode);
									beanList.add(stockChannelStatusBean);
								}
								if (!StringUtil.isNotNullForList(beanList)) {
									continue;
								}
								String goodsSnInfo = JSON.toJSONString(beanList);
								Date requestTime = new Date();
								String resultCode = "0";
								String msg = "";
								try {
									log.info("回写统一库存请求参数:" +JSON.toJSONString(beanList));
									Message message = stockChannelStatusFacade.setStockChannelStatusList(beanList);
									log.info("回写统一库存：" + JSON.toJSONString(message));
									if (message.getIsSetData()) {
										StringBuffer sb2 = new StringBuffer();
										sb2.append("店铺进货：");
										for(StockChannelStatusBean bean:beanList){
											sb2.append(bean.getChannelCode()+";");
										}
										String stockStatus = (isSyncStock.equals("0") ? "关闭" : "开启");
										msg = sb2.toString()+ "库存同步[" + stockStatus + "]上下架状态同步至统一库存成功！";
										resultCode = "1";
									} else {
										msg = "店铺进货：" + goodsSn + "上下架状态同步至统一库存失败！";
									}
								} catch (Exception e) {
									log.error(goodsSn + goodsSnInfo + "上下架状态同步统一库存失败：" + e);
									msg = goodsSnInfo + "上下架状态同步至统一库存失败：" + e;
									resultCode = "0";
								} finally {
									ChannelErpUpdownLog updownLog = new ChannelErpUpdownLog();
									updownLog.setChannelCode(channelCode);
									updownLog.setShopCode(shopCode);
									updownLog.setCode(resultCode);
									if (StringUtil.isNotEmpty(msg) && msg.length() > 255) {
										updownLog.setMessage(msg.substring(0, 255));
									} else {
										updownLog.setMessage(msg);
									}
									updownLog.setGoodsSn(goodsSn);
									updownLog.setStatus(String.valueOf(status));
									updownLog.setType("1");
									updownLog.setRequestTime(requestTime);
									updownLog.setUserId(userName);
									try {
										this.channelErpUpdownLogMapper.insertSelective(updownLog);
									} catch (Exception e2) {
										log.error("记录日志：上下架状态同步至统一库存 异常:", e2);
									}
								}
							}
					} catch (Exception e) {
						sb.append("[" + list.get(0).getGoodsSn() + "]同步异常,");
						log.error("[" + list.get(0).getGoodsSn() + "]同步异常:", e);
					} finally{
						*//********** 每1记录一次日志 *********//*
						if (StringUtil.isNotNullForList(channelGoodsList)) {
							ChannelApiLog channelApiLog = new ChannelApiLog();
							channelApiLog.setChannelCode(channelCode);
							channelApiLog.setShopCode(shopCode);
							channelApiLog.setMethodName("10");
							channelApiLog.setParamInfo(ticketCode+ "-" + gs);
							channelApiLog.setIpAddress("");
							channelApiLog.setUser(userName);
							channelApiLog.setRequestTime(new Date());
							if ("".equals(sb.toString())) {
								channelApiLog.setReturnCode("0"); // 成功
								channelApiLog.setReturnMessage("店铺经营商品修改成功！");
							} else {
								channelApiLog.setReturnCode("0"); // 失败
								channelApiLog.setReturnMessage("异常信息：" + sb.toString());
							}
							try {
								channelApiLogMapper.insert(channelApiLog); // 日志记录
							} catch (Exception e) {
								log.error("日志记录异常", e);
							}
						}
					}
				  }else{
					  sb.append("商品表中未查询到商品编号：" + gs);
						*//********** 每1记录一次日志 *********//*
						ChannelApiLog channelApiLog = new ChannelApiLog();
						channelApiLog.setChannelCode(channelCode);
						channelApiLog.setShopCode(shopCode);
						channelApiLog.setMethodName("10");
						channelApiLog.setParamInfo(ticketCode+ "-" + gs);
						channelApiLog.setIpAddress("");
						channelApiLog.setUser(userName);
						channelApiLog.setRequestTime(new Date());
						channelApiLog.setReturnCode("1"); // 失败
						channelApiLog.setReturnMessage("异常信息：" + sb.toString());
					try {
						channelApiLogMapper.insert(channelApiLog); // 日志记录
					} catch (Exception e) {
						log.error("日志记录异常", e);
					}
				  }
				*/} 
				i++;
				j++;
			}
		} catch (Exception e) {
			log.error("读取csv文件内容异常", e);
		} finally {
			if (null != reader) {
				reader.close();
				reader = null;
			}
		}
		if (rowNumFlag == count) {
			jsonResult.setIsok(true);
			jsonResult.setMessage("导入成功！");
		} else {
			jsonResult.setIsok(false);
			jsonResult.setMessage("导入完成！导入数据部分存在异常，请到日志中查询具体原因！");
		}
		return jsonResult;
	}
	
}

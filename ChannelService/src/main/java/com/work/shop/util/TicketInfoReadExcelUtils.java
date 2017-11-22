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

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.stereotype.Service;

import au.com.bytecode.opencsv.CSVReader;

import com.work.shop.bean.ChannelGoods;
import com.work.shop.bean.ChannelGoodsExample;
import com.work.shop.bean.TicketInfo;
import com.work.shop.bean.bgcontentdb.BGproductGoodsExample;
import com.work.shop.bean.bgcontentdb.BGproductGoodsWithBLOBs;
import com.work.shop.dao.ChannelErpUpdownLogMapper;
import com.work.shop.dao.ChannelGoodsMapper;
import com.work.shop.dao.bgcontentdb.BGproductGoodsMapper;
import com.work.shop.dao.mbproduct.MBProductSellerGoodsSelectMapper;
import com.work.shop.redis.RedisClient;
import com.work.shop.service.ChannelGoodsService;
import com.work.shop.service.ChannelShopService;
import com.work.shop.service.ShopGoodsService;

@Service
public class TicketInfoReadExcelUtils {
	
	@Resource(name="shopGoodsService")
	private ShopGoodsService shopGoodsService;

	@Resource(name="channelGoodsService")
	private ChannelGoodsService channelGoodsService;

	public final static String REG_DIGIT = "[0-9]*";

	public boolean isDigit(String str) { //判断字符串是否全为数字
		return str.matches(REG_DIGIT);
	}
	
	@Resource(name = "redisClient")
	private RedisClient redisClient;
	
	@Resource(name = "bGproductGoodsMapper")
	BGproductGoodsMapper productGoodsMapper;
	
	@Resource
	ChannelGoodsMapper channelGoodsMapper;
	
	@Resource
	ChannelErpUpdownLogMapper channelErpUpdownLogMapper;
	
	@Resource
	ChannelShopService channelShopService;
	@Resource
	private MBProductSellerGoodsSelectMapper mbProductSellerGoodsSelectMapper;
	
	private static Logger logger = Logger.getLogger(TicketInfoReadExcelUtils.class);

	/**
	 * 读取csv文件内容 
	 * shopCode 必须要有
	 * @return List<TicketInfo>对象
	 * @throws IOException
	 * 输入/输出(i/o)异常
	 */
	public List<TicketInfo> readCsv(InputStream is, String ticketCode,
			String shopCode, StringBuffer sb) throws IOException {
		CSVReader reader = new CSVReader(new InputStreamReader(is, "GBK"));
		TicketInfo ticketInfo = null;
		List<TicketInfo> list = new ArrayList<TicketInfo>();
		Map<String, String> map = new HashMap<String, String>();
		List<BGproductGoodsWithBLOBs> list2 = new ArrayList<BGproductGoodsWithBLOBs>();
		String[] nextLine;
		int i = 1;
		int j = 0;
		while ((nextLine = reader.readNext()) != null) {
			if (j != 0) {
				ticketInfo = new TicketInfo();
				// 0商品款号 1渠道商品款号 2商品名称
				String goodsSn = nextLine[0];
				if (StringUtil.isEmpty(goodsSn)) {
					continue;
				}
				String gs = "";
				if (goodsSn.indexOf(".") == -1) {
					gs = goodsSn;
				} else {
					gs = goodsSn.substring(0, goodsSn.indexOf("."));
				}
				if (map.get(gs) != null) {
					sb.append("第" + (i) + "行[" + gs + "]已存在,");
					continue;
				}
				Map<String, Object> param = new HashMap<String, Object>();
				param.put("shopCode", shopCode);
				param.put("itemNo", gs);
				param.put("start", 1);
				param.put("limits", 1);
				int total = mbProductSellerGoodsSelectMapper.selectChannelItemCount(param);
				if (total == 0) {
					sb.append("第" + (i) + "行[" + gs + "]商品没有添加到该店铺,");
					continue;
				}
				/*ChannelGoods channelGoods = null;
				try {
					channelGoods = this.channelGoodsService.selectChannelGoods(goodsSn, shopCode);
				} catch (Exception e) {
					e.printStackTrace();
				}
				if (null == channelGoods) {
//					sb.append("第" + (i) + "行[" + gs + "]渠道商品表中不存在,");
					//商品未进货，先进货
					Integer status = 0;
					BGproductGoodsExample productExample = new BGproductGoodsExample();
					com.work.shop.bean.bgcontentdb.BGproductGoodsExample.Criteria criteria = productExample.or();
					criteria.andGoodsSnEqualTo(gs);
					list2 = productGoodsMapper
							.selectByExampleWithBLOBs(productExample); // 根据goodsSn查询导入商品
					if (list2 != null && list2.size() > 0) {
						try {
							ChannelGoodsExample channelGoodsExample = new ChannelGoodsExample();
							com.work.shop.bean.ChannelGoodsExample.Criteria channelGoodsCriteria = channelGoodsExample
									.or();
							channelGoodsCriteria.andChannelCodeEqualTo(shopCode);
							if (!StringUtil.isNotNull(list2.get(0).getGoodsSn())) { // 商品编号为空继续下次处理
								continue;
							}
							channelGoodsCriteria.andGoodsSnEqualTo(list2.get(0)
									.getGoodsSn());
							channelGoods = new ChannelGoods();
						     // 新增操作
							 int ii = setChannelGoods(channelGoods,shopCode,list2,status);
								if(ii>0){
									// 上下架同步成功后将状态同步至统一库存.
									goodsSn =  list2.get(0).getGoodsSn();
									StockChannelStatusBean stockChannelStatusBean = new StockChannelStatusBean();
									stockChannelStatusBean.setUpdateTime(new Date());
									stockChannelStatusBean.setSixProdId(goodsSn);
									stockChannelStatusBean.setCreateTime(new Date());
									stockChannelStatusBean.setSaleStatus(String.valueOf(status));
									//2017-5-26 add by liyinghua 唯品会渠道默认不同步库存
									if("HQ01S155".equals(shopCode)){
										stockChannelStatusBean.setIsSync((byte)0);
									}else{
										stockChannelStatusBean.setIsSync((byte)1);
									}
									stockChannelStatusBean.setCreateBy("System");
									stockChannelStatusBean.setUpdateBy("System");
									stockChannelStatusBean.setChannelCode(shopCode);
									String goodsSnInfo = JSON.toJSONString(stockChannelStatusBean);
									Date requestTime = new Date();
									String resultCode = "0";
									String msg = "";
									try {
										logger.info("回写统一库存请求参数:" +JSON.toJSONString(stockChannelStatusBean));
										Message message = stockChannelStatusFacade.setStockChannelStatus(stockChannelStatusBean);
										logger.info("回写统一库存：" + JSON.toJSONString(message));
										if (message.getIsSetData()) {
											msg = goodsSn+ "上下架状态同步至统一库存成功！";
											resultCode = "1";
										} else {
											msg = goodsSn + "上下架状态同步至统一库存失败！";
										}
									} catch (Exception e) {
										logger.error(goodsSn + goodsSnInfo + "上下架状态同步统一库存失败：" + e);
										msg = goodsSnInfo + "上下架状态同步至统一库存失败：" + e;
										resultCode = "0";
									} finally {
										ChannelErpUpdownLog updownLog = new ChannelErpUpdownLog();
										JsonResult jsonResult = channelShopService.selectCurrentChannelShop(shopCode);
										ChannelShop channelShop = (ChannelShop) jsonResult.getData();
										updownLog.setChannelCode(channelShop.getChannelCode());
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
										updownLog.setUserId("System");
										try {
											this.channelErpUpdownLogMapper.insertSelective(updownLog);
										} catch (Exception e2) {
											logger.error("记录日志：上下架状态同步至统一库存 异常:", e2);
										}
									}
								}
						} catch (Exception e) {
							sb.append("[" + list2.get(0).getGoodsSn() + "]同步异常,");
							logger.error("[" + list2.get(0).getGoodsSn() + "]同步异常:", e);
						}
					}else{
						sb.append("第" + (i) + "行[" + gs + "]商品信息表中不存在,");
					}
				}*/
				map.put(gs, gs);
				ticketInfo.setGoodsSn(gs);// 商品款号
				String channelGoodsSn=redisClient.get(Constants.MOGU_GOODS_SN_KEY
						+ shopCode + "_" + gs);
				if(StringUtils.isNotBlank(channelGoodsSn)){
					ticketInfo.setChannelGoodssn(channelGoodsSn);
				}
				String onSellStatus = nextLine[1];
				if (onSellStatus == null) {
					sb.append("第" + (i) + "行[" + gs + "]上下架状态异常,");
					continue;
				}
				String cs = "";
				if (onSellStatus.indexOf(".") == -1) {
					cs = onSellStatus;
				} else {
					cs = onSellStatus.substring(0, onSellStatus.indexOf("."));
				}
				if (!"0".equals(cs.trim()) && !"1".equals(cs.trim())) {
					sb.append("第" + (i) + "行[" + gs + "]上下架状态异常,");
					continue;
				}
				ticketInfo.setOnSellStatus(cs);
				ticketInfo.setTicketCode(ticketCode); // 绑定的调整单编号
				List<ChannelGoods> listChannelGoods = shopGoodsService
						.selectChannelGoodsList(gs, shopCode);
				if (listChannelGoods != null && listChannelGoods.size() > 0) {
					ticketInfo.setGoodsTitle(listChannelGoods.get(0)
							.getGoodsTitle());
				}
				//2017-5-26 add by liyinghua 唯品会渠道默认不同步库存
				if("HQ01S155".equals(shopCode)){
					ticketInfo.setIsSyncStock("0");
				}
				
				list.add(ticketInfo);
			}
			i++;
			j++;
		}
		return list;
	}
    
	 private int setChannelGoods(ChannelGoods channelGoods, String shopCode,
			List<BGproductGoodsWithBLOBs> list2, Integer status) {
		    int i = 0;
		 try {
				channelGoods.setChannelCode(shopCode); // 这里shopcode对应那边的channelcode
				Long addTime = TimeUtil
						.parseDateToNumeric(new Date());
				if (addTime != null) {
					channelGoods.setAddTime(Integer
							.parseInt(addTime.toString()));
				}
				channelGoods.setPresellOrder((short) 0);
				if (list2.get(0).getAllowSearch() != null) {
					channelGoods.setAllowSearch(list2.get(0)
							.getAllowSearch()==1?1:0);
				} else {
					channelGoods.setAllowSearch(1);
				}
				if (list2.get(0).getBestOrder() != null) {
					channelGoods.setBestOrder(list2.get(0)
							.getBestOrder().shortValue());
				} else {
					channelGoods.setBestOrder((short) 0);
				}
				channelGoods.setCardPicture(list2.get(0)
						.getCardPicture());
				channelGoods.setChannelPrice(list2.get(0)
						.getMarketPrice());
				if (list2.get(0).getGoodsName() != null) {
					channelGoods.setGoodsName(list2.get(0)
							.getGoodsName());
				} else {
					channelGoods.setGoodsName("");
				}

				channelGoods.setGoodsSn(list2.get(0).getGoodsSn());
				if (list2.get(0).getGoodsTitle() == null) {
					channelGoods.setGoodsTitle("");
				} else {
					channelGoods.setGoodsTitle(list2.get(0)
							.getGoodsTitle());
				}
				channelGoods.setHotNumber(null);

				if (list2.get(0).getHotOrder() != null) {
					channelGoods.setHotOrder(list2.get(0)
							.getHotOrder().shortValue());
				} else {
					channelGoods.setHotOrder((short) 0);
				}

				if (list2.get(0).getIsBest() != null) {
					channelGoods.setIsBest(list2.get(0)
							.getIsBest().byteValue());
				} else {
					channelGoods.setIsBest((byte) 0);
				}

				if (list2.get(0).getIsHot() != null) {
					channelGoods.setIsHot(list2.get(0)
							.getIsHot().byteValue());
				} else {
					channelGoods.setIsHot((byte) 0);
				}

				if (list2.get(0).getIsNew() != null) {
					channelGoods.setIsNew(list2.get(0)
							.getIsNew().byteValue());
				} else {
					channelGoods.setIsNew((byte) 0);
				}

				if (list2.get(0).getIsOnSell() != null) {
					channelGoods.setIsOnSell(list2.get(0)
							.getIsOnSell().byteValue());
					status = list2.get(0).getIsOnSell();
				} else {
					channelGoods.setIsOnSell((byte) 0);
					status = 0;
				}
				if (list2.get(0).getIsOutlets() != null) {
					channelGoods.setIsOutlet(list2.get(0)
							.getIsOutlets().byteValue());

				} else {
					channelGoods.setIsOutlet((byte) 0);
				}
				channelGoods.setIsPresale(null);
				
				channelGoods.setIsPresell(0);

				channelGoods.setIsSecondSale(null);
				channelGoods.setIsUpdate((byte) 0);
				channelGoods.setKeywords(list2.get(0).getKeywords());
				// channelGoods.setLastUpdate(TimeUtil.parseString2Date(TimeUtil.getDate()));
				// //修改时间
				// Long lastUpdateTime =
				// TimeUtil.parseDateToNumeric(new
				// Date());
				channelGoods.setLastUpdateTime(0);
				if (list2.get(0).getNewOrder() != null) {
					channelGoods.setNewOrder(list2.get(0)
							.getNewOrder().shortValue());
				} else {
					channelGoods.setNewOrder((short) 0);
				}
				
				if(list2.get(0)
						.getMarketPrice() != null ){
					channelGoods.setPlatformPrice(list2.get(0)
							.getMarketPrice());
				} else{
					channelGoods.setPlatformPrice(new BigDecimal(0.00));
				}

				channelGoods.setPlatformPrice(list2.get(0)
						.getMarketPrice());
				if (list2.get(0).getSalePoint() != null
						&& list2.get(0).getSalePoint().length() > 250) {
					channelGoods.setSalePoint(list2.get(0)
							.getSalePoint().substring(0, 250));
				} else {
					channelGoods.setSalePoint(list2.get(0)
							.getSalePoint());
				}
				channelGoods.setSizePicture(list2.get(0)
						.getSizePicture());
				if (list2.get(0).getSortOrder() != null) {
					channelGoods.setSortOrder(list2.get(0)
							.getSortOrder().shortValue());
				} else {
					channelGoods.setSortOrder((short) 0);
				}
				if (list2.get(0).getWarnNumber() != null) {
					channelGoods.setWarnNumber(list2.get(0)
							.getWarnNumber());
				} else {
					channelGoods.setWarnNumber(1);
				}
				channelGoods.setTplId(0);
				channelGoods.setLimitNumber(0);
				channelGoods.setCanDiscount((byte) 1);
				channelGoods.setGoodsDesc(list2.get(0)
						.getGoodsDesc());
				channelGoods.setIsOnlineOffline((byte)1);
				//2017-5-26 add by liyinghua 唯品会渠道默认不同步库存
				if("HQ01S155".equals(shopCode)){
					channelGoods.setIsSyncStock((byte)0);
				}
				
				i = channelGoodsMapper.insertSelective(channelGoods);
			} catch (NumberFormatException e) {
				logger.error("setChannelGoods异常："+e.getMessage(),e);
			}
		 return i ;
		
	}
	 
	 /**
		 * 读取csv文件内容 
		 * shopCode 必须要有
		 * @return List<TicketInfo>对象
		 * @throws IOException
		 * 输入/输出(i/o)异常
		 */
	public List<TicketInfo> readSyncStockCsv(InputStream is, String ticketCode,
			String shopCode, StringBuffer sb) throws IOException {
		CSVReader reader = new CSVReader(new InputStreamReader(is, "GBK"));
		TicketInfo ticketInfo = null;
		List<TicketInfo> list = new ArrayList<TicketInfo>();
		Map<String, String> map = new HashMap<String, String>();
		String[] nextLine;
		int i = 1;
		int j = 0;
		while ((nextLine = reader.readNext()) != null) {
			if (j != 0) {
				ticketInfo = new TicketInfo();
				// 0商品款号 1渠道商品款号 2商品名称
				String goodsSn = nextLine[0];
				if (StringUtil.isEmpty(goodsSn)) {
					continue;
				}
				String gs = "";
				if (goodsSn.indexOf(".") == -1) {
					gs = goodsSn;
				} else {
					gs = goodsSn.substring(0, goodsSn.indexOf("."));
				}
				if (map.get(gs) != null) {
					sb.append("第" + (i) + "行[" + gs + "]已存在,");
					continue;
				}
				ChannelGoods channelGoods = null;
				try {
					channelGoods = this.channelGoodsService.selectChannelGoods(goodsSn, shopCode);
				} catch (Exception e) {
					e.printStackTrace();
				}
				if (null == channelGoods) {
					sb.append("第" + (i) + "行[" + gs + "]渠道商品表中不存在,");
				}
				map.put(gs, gs);
				ticketInfo.setGoodsSn(gs);// 商品款号
				String syncStock = nextLine[1];
				if (syncStock == null) {
					sb.append("第" + (i) + "行[" + gs + "]库存同步状态异常,");
					continue;
				}
				String cs = "";
				if (syncStock.indexOf(".") == -1) {
					cs = syncStock;
				} else {
					cs = syncStock.substring(0, syncStock.indexOf("."));
				}
				if (!"0".equals(cs.trim()) && !"1".equals(cs.trim())) {
					sb.append("第" + (i) + "行[" + gs + "]库存同步状态异常,");
					continue;
				}
				ticketInfo.setIsSyncStock(cs);
//				ticketInfo.setOnSellStatus(cs);
				ticketInfo.setTicketCode(ticketCode); // 绑定的调整单编号
				List<ChannelGoods> listChannelGoods = shopGoodsService
						.selectChannelGoodsList(gs, shopCode);
				if (listChannelGoods != null && listChannelGoods.size() > 0) {
					ticketInfo.setGoodsTitle(listChannelGoods.get(0)
							.getGoodsTitle());
				}
				list.add(ticketInfo);
			}
			i++;
			j++;
		}
		return list;
	}
	
	/**
	 * 读取csv文件内容 
	 * shopCode 必须要有
	 * @return List<TicketInfo>对象
	 * @throws IOException
	 * 输入/输出(i/o)异常
	 */
public List<TicketInfo> readGoodsStoreBandingCsv(InputStream is, String ticketCode,
		String shopCode, StringBuffer sb) throws IOException {
	CSVReader reader = new CSVReader(new InputStreamReader(is, "GBK"));
	TicketInfo ticketInfo = null;
	List<TicketInfo> list = new ArrayList<TicketInfo>();
	Map<String, String> map = new HashMap<String, String>();
	String[] nextLine;
	int i = 1;
	int j = 0;
	while ((nextLine = reader.readNext()) != null) {
		if (j != 0) {
			ticketInfo = new TicketInfo();
			// 0商品款号 1渠道商品款号 2商品名称
			String goodsSn = nextLine[0];
			if (StringUtil.isEmpty(goodsSn)) {
				continue;
			}
			String gs = "";
			if (goodsSn.indexOf(".") == -1) {
				gs = goodsSn;
			} else {
				gs = goodsSn.substring(0, goodsSn.indexOf("."));
			}
			if (map.get(gs) != null) {
				sb.append("第" + (i) + "行[" + gs + "]已存在,");
				continue;
			}
			ChannelGoods channelGoods = null;
			try {
				channelGoods = this.channelGoodsService.selectChannelGoods(goodsSn, shopCode);
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (null == channelGoods) {
				sb.append("第" + (i) + "行[" + gs + "]渠道商品表中不存在,");
			}
			map.put(gs, gs);
			ticketInfo.setGoodsSn(gs);// 商品款号
			String isOnlineOffline = nextLine[1];
			if (isOnlineOffline == null) {
				sb.append("第" + (i) + "行[" + gs + "]商品线上线下状态异常,");
				continue;
			}
//			String cs = "";
//			if (syncStock.indexOf(".") == -1) {
//				cs = syncStock;
//			} else {
//				cs = syncStock.substring(0, syncStock.indexOf("."));
//			}
			if (!"1".equals(isOnlineOffline.trim()) && !"2".equals(isOnlineOffline.trim())
					&& !"3".equals(isOnlineOffline)) {
				sb.append("第" + (i) + "行[" + gs + "]商品关联状态异常,");
				continue;
			}
			ticketInfo.setIsOnlineOffline(Byte.valueOf(isOnlineOffline));
//			ticketInfo.setOnSellStatus(cs);
			ticketInfo.setTicketCode(ticketCode); // 绑定的调整单编号
			List<ChannelGoods> listChannelGoods = shopGoodsService
					.selectChannelGoodsList(gs, shopCode);
			if (listChannelGoods != null && listChannelGoods.size() > 0) {
				ticketInfo.setGoodsTitle(listChannelGoods.get(0)
						.getGoodsTitle());
			}
			list.add(ticketInfo);
		}
		i++;
		j++;
	}
	return list;
}

	/**
     * 读取xls文件内容
     *  shopCode 必须要有
     * @return List<TicketInfo>对象
     * @throws IOException
     *   输入/输出(i/o)异常 
     */
    public List<TicketInfo> readXls(InputStream is,String ticketCode,String shopCode,StringBuffer sb) throws IOException {
      //  InputStream is = new FileInputStream(file);//读取excel名称
        HSSFWorkbook hssfWorkbook = new HSSFWorkbook(is);
        TicketInfo ticketInfo = null;
        List<TicketInfo> list = new ArrayList<TicketInfo>();
        Map<String,String> map = new HashMap<String,String>();
        // 循环工作表Sheet
        for (int numSheet = 0; numSheet < hssfWorkbook.getNumberOfSheets(); numSheet++) {
            HSSFSheet hssfSheet = hssfWorkbook.getSheetAt(numSheet);
            if (hssfSheet == null) {
                continue;
            }
            // 循环行Row
            for (int rowNum = 1; rowNum <= hssfSheet.getLastRowNum(); rowNum++) {
                HSSFRow hssfRow = hssfSheet.getRow(rowNum);
                if (hssfRow == null) {
                    continue;
                }
                ticketInfo = new TicketInfo();
                // 循环列Cell
                // 0商品款号 1渠道商品款号 2商品名称 
                // for (int cellNum = 0; cellNum <=4; cellNum++) {
                HSSFCell goodsSn = hssfRow.getCell((short) 0);
                if (goodsSn == null) {
                    continue;
                }
                String gs = "";
                if(getValue(goodsSn).indexOf(".")==-1){
                	gs = getValue(goodsSn);
                }else{
                	gs = getValue(goodsSn).substring(0, getValue(goodsSn).indexOf("."));
                }
                
                if(map.get(gs) != null){
                	 sb.append("第"+(rowNum+1)+"行["+map.get(gs)+"]已存在,");
             	   continue;
                }
                map.put(gs, gs);
                ticketInfo.setGoodsSn(gs);//商品款号
                
//                HSSFCell channelGoodsSn = hssfRow.getCell((short) 1);
//                if (channelGoodsSn == null) {
//                    continue;
//                }
//                
//                String cg = "";
//                if(getValue(channelGoodsSn).indexOf(".")==-1){
//                	cg = getValue(channelGoodsSn);
//                }else{
//                	cg = getValue(channelGoodsSn).substring(0, getValue(channelGoodsSn).indexOf("."));
//                }
//                
//                ticketInfo.setChannelGoodssn(cg);//渠道商品款号
//                HSSFCell goodsTitle = hssfRow.getCell((short) 2);
//                if (goodsTitle == null) {
//                    continue;
//                }
//                String gt = "";
//                if(getValue(goodsTitle).indexOf(".")==-1){
//                	gt = getValue(goodsTitle);
//                }else{
//                	gt = getValue(goodsTitle).substring(0, getValue(goodsTitle).indexOf("."));
//                }
//                ticketInfo.setGoodsTitle(gt);//商品名称
                
                HSSFCell onSellStatus = hssfRow.getCell((short) 1);
                
                if (onSellStatus == null) {
                	 sb.append("第"+(rowNum+1)+"行["+map.get(gs)+"]上下架状态异常,");
                    continue;
                }
                
                String cs ="";
                if(getValue(onSellStatus).indexOf(".")==-1){
                	cs = getValue(onSellStatus);
                }else{
                	cs = getValue(onSellStatus).substring(0, getValue(onSellStatus).indexOf("."));
                }
                if(!"0".equals(cs) && !"1".equals(cs)){
                	 sb.append("第"+(rowNum+1)+"行["+map.get(gs)+"]上下架状态异常,");
                     continue;
                }
                ticketInfo.setOnSellStatus(cs);
                
                ticketInfo.setTicketCode(ticketCode); //绑定的调整单编号
              
                List<ChannelGoods> listChannelGoods = shopGoodsService.selectChannelGoodsList(gs, shopCode);
                if(listChannelGoods !=null && listChannelGoods.size()>0){
                	ticketInfo.setGoodsTitle(listChannelGoods.get(0).getGoodsTitle());
                }
                list.add(ticketInfo);
            }
        }
        return list;
    }
 
    /**
     * 得到Excel表中的值
     * 
     * @param hssfCell
     *            Excel中的每一个格子
     * @return Excel中每一个格子中的值
     */
    @SuppressWarnings("static-access")
    private  String getValue(HSSFCell hssfCell) {
        if (hssfCell.getCellType() == hssfCell.CELL_TYPE_BOOLEAN) {
            // 返回布尔类型的值
            return String.valueOf(hssfCell.getBooleanCellValue());
        }else if (hssfCell.getCellType() == hssfCell.CELL_TYPE_NUMERIC) {
            // 返回数值类型的值
            return String.valueOf(hssfCell.getNumericCellValue());
        }else {
            // 返回字符串类型的值
            return String.valueOf(hssfCell.getStringCellValue());
        }
    }
    
    
    
	/**
	 * 读取csv文件内容 
	 * shopCode 必须要有
	 * @return List<TicketInfo>对象
	 * @throws IOException
	 * 输入/输出(i/o)异常
	 */
	public List<TicketInfo> readOnlineOfflineCsv(InputStream is, String ticketCode,
			String shopCode, StringBuffer sb) throws IOException {
		CSVReader reader = new CSVReader(new InputStreamReader(is, "GBK"));
		TicketInfo ticketInfo = null;
		List<TicketInfo> list = new ArrayList<TicketInfo>();
		Map<String, String> map = new HashMap<String, String>();
		String[] nextLine;
		int i = 1;
		int j = 0;
		while ((nextLine = reader.readNext()) != null) {
			if (j != 0) {
				ticketInfo = new TicketInfo();
				// 0商品款号 1上下线状态
				String goodsSn = nextLine[0];
				if (StringUtil.isEmpty(goodsSn)) {
					continue;
				}
				String gs = "";
				if (goodsSn.indexOf(".") == -1) {
					gs = goodsSn;
				} else {
					gs = goodsSn.substring(0, goodsSn.indexOf("."));
				}
				if (map.get(gs) != null) {
					sb.append("第" + (i) + "行[" + map.get(gs) + "]已存在,");
					continue;
				}
				map.put(gs, gs);
				ticketInfo.setGoodsSn(gs);// 商品款号
				
				String onSellStatus = nextLine[1];
				if (onSellStatus == null) {
					sb.append("第" + (i) + "行[" + map.get(gs) + "]上下线状态异常,");
					continue;
				}
				String cs = "";
				if (onSellStatus.indexOf(".") == -1) {
					cs = onSellStatus;
				} else {
					cs = onSellStatus.substring(0, onSellStatus.indexOf("."));
				}
				if (!"1".equals(cs) && !"2".equals(cs) && !"3".equals(cs)) {
					sb.append("第" + (i) + "行[" + map.get(gs) + "]上下线状态异常,");
					continue;
				}
				ticketInfo.setIsOffline(cs);
				ticketInfo.setTicketCode(ticketCode); // 绑定的调整单编号
				List<ChannelGoods> listChannelGoods = shopGoodsService
						.selectChannelGoodsList(gs, shopCode);
				if (listChannelGoods != null && listChannelGoods.size() > 0) {
					ticketInfo.setGoodsTitle(listChannelGoods.get(0)
							.getGoodsTitle());
				}
				list.add(ticketInfo);
			}
			i++;
			j++;
		}
		return list;
	}
 
	
	
	/**
	 * 读取csv文件内容 
	 * shopCode 必须要有
	 * @return List<TicketInfo>对象
	 * @throws IOException
	 * 输入/输出(i/o)异常
	 */
	public List<TicketInfo> readcODCsv(InputStream is, String ticketCode,
			String shopCode, StringBuffer sb) throws IOException {
		CSVReader reader = new CSVReader(new InputStreamReader(is, "GBK"));
		TicketInfo ticketInfo = null;
		List<TicketInfo> list = new ArrayList<TicketInfo>();
		Map<String, String> map = new HashMap<String, String>();
		String[] nextLine;
		int i = 1;
		int j = 0;
		while ((nextLine = reader.readNext()) != null) {
			if (j != 0) {
				ticketInfo = new TicketInfo();
				// 0商品款号 1渠道商品款号 2商品名称
				String goodsSn = nextLine[0];
				if (StringUtil.isEmpty(goodsSn)) {
					continue;
				}
				String gs = "";
				if (goodsSn.indexOf(".") == -1) {
					gs = goodsSn;
				} else {
					gs = goodsSn.substring(0, goodsSn.indexOf("."));
				}
				if (map.get(gs) != null) {
					sb.append("第" + (i) + "行[" + gs + "]已存在,");
					continue;
				}
				map.put(gs, gs);
				ticketInfo.setGoodsSn(gs);// 商品款号
				String onSellStatus = nextLine[1];
				if (onSellStatus == null) {
					sb.append("第" + (i) + "行[" + gs + "]数据状态异常,");
					continue;
				}
				String cs = "";
				if (onSellStatus.indexOf(".") == -1) {
					cs = onSellStatus;
				} else {
					cs = onSellStatus.substring(0, onSellStatus.indexOf("."));
				}
				if (!"0".equals(cs.trim()) && !"1".equals(cs.trim())) {
					sb.append("第" + (i) + "行[" + gs + "]数据状态异常,");
					continue;
				}
				ticketInfo.setOnSellStatus(cs);
				ticketInfo.setTicketCode(ticketCode); // 绑定的调整单编号
				List<ChannelGoods> listChannelGoods = shopGoodsService
						.selectChannelGoodsList(gs, shopCode);
				if (listChannelGoods != null && listChannelGoods.size() > 0) {
					ticketInfo.setGoodsTitle(listChannelGoods.get(0)
							.getGoodsTitle());
				}
				list.add(ticketInfo);
			}
			i++;
			j++;
		}
		return list;
	}
}



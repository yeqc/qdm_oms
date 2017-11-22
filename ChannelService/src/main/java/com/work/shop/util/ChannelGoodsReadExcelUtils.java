package com.work.shop.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
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

import au.com.bytecode.opencsv.CSVReader;

import com.work.shop.redis.RedisClient;
import com.work.shop.vo.ChannelGoodsInfoVo;
import com.work.shop.vo.ChannelGoodsTicketVo;

public class ChannelGoodsReadExcelUtils {
	
	private static final Logger logger = Logger.getLogger(ChannelGoodsReadExcelUtils.class);
	

	/**
	 * 读取csv文件内容
	 * 
	 * @return List<TicketInfo>对象
	 * @throws IOException
	 * 输入/输出(i/o)异常
	 */
	public static List<ChannelGoodsInfoVo> readCsvrhannelGoodsFile(
			String channelCode, String shopCode, InputStream is, StringBuffer sb)
			throws IOException {
		List<ChannelGoodsInfoVo> list = new ArrayList<ChannelGoodsInfoVo>();
		CSVReader reader = new CSVReader(new InputStreamReader(is, "GBK"));
		try {
			String[] nextLine;
			ChannelGoodsInfoVo bean = null;
			Map<String, Object> repeatMap = new HashMap<String, Object>();
			int j = 0;
			while ((nextLine = reader.readNext()) != null) {
				if (j != 0) {
					bean = new ChannelGoodsInfoVo();
					// 循环列Cell
					bean.setChannelCode(channelCode);
					bean.setShopCode(shopCode);

					// 商品款号
					String goodsSn = nextLine[0];
					if (StringUtil.isEmpty(goodsSn)) {
						continue;
					}
					bean.setProductGoodsSn(getValue(getValue(goodsSn)));
					// 新商品名称
					String goodsName = nextLine[1];
					if (goodsName != null) {
						bean.setGoodsTitle(goodsName);
					}
					if (repeatMap.containsKey(bean.getProductGoodsSn())) {
						if (StringUtil.isNotEmpty(sb.toString())) {
							sb.append(",").append(bean.getProductGoodsSn());
						} else {
							sb.append(bean.getProductGoodsSn());
						}
					} else {
						repeatMap.put(bean.getProductGoodsSn(), bean);
					}

					list.add(bean);

				}
				j++;
			}
		} catch (Exception e) {
			logger.error("读取csv文件内容异常", e);
		} finally {
			if (null != reader) {
				reader.close();
				reader = null;
			}
		}
		return list;
	}

	/**
	 * 批量添加商品文件导入,读取xls文件内容
	 * 
	 * @return List<ChannelGoods>对象
	 * @throws IOException
	 *             输入/输出(i/o)异常
	 */
	public static List<ChannelGoodsInfoVo> readChannelGoodsFile(String channelCode, String shopCode, InputStream is, StringBuffer sb) throws IOException {
		HSSFWorkbook hssfWorkbook = new HSSFWorkbook(is);
		ChannelGoodsInfoVo bean = null;
		List<ChannelGoodsInfoVo> list = new ArrayList<ChannelGoodsInfoVo>();
		Map<String, Object> repeatMap = new HashMap<String, Object>();
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
				bean = new ChannelGoodsInfoVo();

				// 循环列Cell
				bean.setChannelCode(channelCode);
				bean.setShopCode(shopCode);

				// 商品款号
				HSSFCell goodsSn = hssfRow.getCell((short) 0);
				if (goodsSn == null) {
					continue;
				}
				bean.setProductGoodsSn(getValue(getValue(goodsSn)));

//				// 是否定时执行
//				HSSFCell isTiming = hssfRow.getCell((short) 1);
//				if (isTiming != null) {
//					bean.setIsTiming(getValue(getValue(isTiming)));
//				}else{
//					bean.setIsTiming("0");
//				}

//				// 定时执行时间
//				HSSFCell excetTime = hssfRow.getCell((short) 2);
//				if (excetTime != null) {
//					bean.setExecTime(getValue(excetTime));
//				}

				// 新商品名称
				HSSFCell goodsName = hssfRow.getCell((short) 1);
				if (goodsName != null) {
					bean.setGoodsTitle(getValue(goodsName));
				}

				// // 保护价
				// HSSFCell channelPrice = hssfRow.getCell((short) 4);
				// if (channelPrice != null) {
				// bean.setSafePrice(new BigDecimal(getValue(channelPrice)));
				// }
				//
				// // 售价
				// HSSFCell platformPrice = hssfRow.getCell((short) 5);
				// if (platformPrice != null) {
				// bean.setNewPrice(new BigDecimal(getValue(platformPrice)));
				// }

				if (repeatMap.containsKey(bean.getProductGoodsSn())) {
					if (StringUtil.isNotEmpty(sb.toString())) {
						sb.append(",").append(bean.getProductGoodsSn());
					} else {
						sb.append(bean.getProductGoodsSn());
					}
				} else {
					repeatMap.put(bean.getProductGoodsSn(), bean);
				}

				list.add(bean);
			}
		}
		return list;
	}
	
	/**
	 * 批量添加商品文件导入,读取xls文件内容
	 * 
	 * @return List<ChannelGoods>对象
	 * @throws IOException
	 *             输入/输出(i/o)异常
	 */
	public static List<ChannelGoodsInfoVo> readTicketFile(RedisClient redisClient,ChannelGoodsTicketVo ticketVo, InputStream is, StringBuffer sb) throws IOException {
		CSVReader reader = new CSVReader(new InputStreamReader(is, "GBK"));
		byte ticketType = ticketVo.getTicketType();
		List<ChannelGoodsInfoVo> list = new ArrayList<ChannelGoodsInfoVo>();
		String[] nextLine;
		ChannelGoodsInfoVo bean = null;
		Map<String, Object> repeatMap = new HashMap<String, Object>();
		int j = 0;
		try {
			while ((nextLine = reader.readNext()) != null) {
				if (j != 0) {
					bean = new ChannelGoodsInfoVo();
					// 商品款号
					String goodsSn = nextLine[0];
					if (StringUtil.isEmpty(goodsSn)) {
						continue;
					}
					bean.setProductGoodsSn(getValue(getValue(goodsSn)));
					String channelGoodsSn=redisClient.get(Constants.MOGU_GOODS_SN_KEY
							+ ticketVo.getShopCode() + "_" + goodsSn);
					if(StringUtils.isNotBlank(channelGoodsSn)){
						bean.setChannelGoodsCode(channelGoodsSn);
					}
					
					if (ticketType == 0) {
						// 修改价格调整单
						
					} else if (ticketType == 1) {
						// 上下架维护调整单
						
					} else if (ticketType == 2) {
						// 商品描述维护调整单
						
					} else if (ticketType == 3) {
						// 商品卖点调整单
						
					} else if (ticketType == 4) {
						// 商品名称维护调整单
						// 新商品名称
						String goodsName = nextLine[1];
						if (goodsName != null) {
							bean.setGoodsTitle(goodsName);
						}
						//2017-7-4  天猫渠道  新增   商品展示标题
						String showName = nextLine[2];
						if(StringUtil.isNotBlank(showName)){
							if(showName.length()>60){
								if (StringUtil.isNotEmpty(sb.toString())) {
									sb.append(",").append(bean.getProductGoodsSn()).append("商品副标题超过60字符");
								} else {
									sb.append(bean.getProductGoodsSn()).append("商品副标题超过60字符");
								}
							}else{
								bean.setShowName(showName);
							}
							
						}
					}
					if (repeatMap.containsKey(bean.getProductGoodsSn())) {
						if (StringUtil.isNotEmpty(sb.toString())) {
							sb.append(",").append(bean.getProductGoodsSn());
						} else {
							sb.append(bean.getProductGoodsSn());
						}
					} else {
						repeatMap.put(bean.getProductGoodsSn(), bean);
					}
					list.add(bean);
				}
				j++;
			}
		} catch (IOException e) {
			e.printStackTrace();
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
	private static String getValue(HSSFCell hssfCell) {
		if (hssfCell.getCellType() == hssfCell.CELL_TYPE_BOOLEAN) {
			// 返回布尔类型的值
			return String.valueOf(hssfCell.getBooleanCellValue());
		} else if (hssfCell.getCellType() == hssfCell.CELL_TYPE_NUMERIC) {
			// 返回数值类型的值
			return String.valueOf(hssfCell.getNumericCellValue());
		} else {
			// 返回字符串类型的值
			String value = String.valueOf(hssfCell.getStringCellValue());
			return StringUtil.isNotEmpty(value) ? value : null;
		}
	}

	private static String getValue(String value) {
		if (value.contains(".")) {
			return value.substring(0, value.indexOf("."));
		}
		return value;
	}
}

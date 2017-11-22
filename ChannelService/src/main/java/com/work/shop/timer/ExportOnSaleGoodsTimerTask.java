package com.work.shop.timer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.eclipse.jetty.util.log.Log;
import org.springframework.stereotype.Service;

import com.work.shop.api.bean.ApiResultVO;
import com.work.shop.api.bean.ChannelApiGoods;
import com.work.shop.api.bean.LocalItemQuery;
import com.work.shop.api.service.ApiService;
import com.work.shop.bean.ChannelGoods;
import com.work.shop.bean.ChannelGoodsExample;
import com.work.shop.bean.ChannelShopExample;
import com.work.shop.bean.ChannelShopExample.Criteria;
import com.work.shop.dao.ChannelGoodsMapper;
import com.work.shop.service.ShopService;
import com.work.shop.util.Constants;
import com.work.shop.util.DateTimeUtils;
import com.work.shop.util.FileUtil;
import com.work.shop.util.StringUtil;
import com.work.shop.vo.ChannelShopVo;

@Service("exportOnSaleGoodsTimerTask")
public class ExportOnSaleGoodsTimerTask {

	private Logger logger = Logger.getLogger(ExportOnSaleGoodsTimerTask.class);
	@Resource(name = "apiService")
	private ApiService apiService;

	@Resource(name = "shopService")
	private ShopService shopService;

	@Resource(name = "channelGoodsMapper")
	private ChannelGoodsMapper channelGoodsMapper;

	private final static String USER_NAME = "System";

	public void execute() {
		logger.info("定时任务启动开始导出各个平台经营商品    start 时间：" + DateTimeUtils.format(new Date(), DateTimeUtils.YYYY_MM_DD_HH_mm_ss));
		ChannelShopExample example = new ChannelShopExample();
		Criteria criteria = example.or();
		criteria.andShopStatusEqualTo(Byte.valueOf("1")); // 默认加载已激活状态的店铺
		criteria.andShopChannelEqualTo(Byte.valueOf("1"));
		List<ChannelShopVo> list = shopService.getChannelShopList(example,false);
		if (list != null && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				if (StringUtil.isNotNull(list.get(i).getChannelCode()) && StringUtil.isNotNull(list.get(i).getShopCode())) {
					String channelCode = list.get(i).getChannelCode();
					String shopCode = list.get(i).getShopCode();
					StringBuffer illegalData = new StringBuffer();
					try {
						Map<String, Object> returnDataMap = exportSixShopBusinessGoods(channelCode, shopCode); // 导出六位码商品,csv
																												// 文件
						illegalData.append(returnDataMap.get("illegal_data"));
					} catch (Exception e) {
						e.printStackTrace();
						logger.error("[channelCode : " + channelCode + " , shopCode : " + shopCode + "] 6位码导出异常" + e.getMessage());
					}
					try {
						Map<String, Object> returnDataMap = exportElevenShopBusinessGoods(channelCode, shopCode); // 导出11位码商品，csv
																													// 文件
						illegalData.append(returnDataMap.get("illegal_data"));
					} catch (Exception e) {
						e.printStackTrace();
						logger.error("[channelCode : " + channelCode + " , shopCode : " + shopCode + "] 11位码导出异常" + e.getMessage());
					}
					try {
						exportIllegalData(channelCode, shopCode, illegalData); // 导出非法数据
					} catch (Exception e) {
						e.printStackTrace();
						logger.error("[channelCode : " + channelCode + " , shopCode : " + shopCode + "] 11位码导出异常" + e.getMessage());
					}
				}
			}
		}
		logger.info("定时任务导出各个平台经营商品    end");
	}

	/**
	 * 取项目地址
	 ***/
	private String getPath() {
		String path = this.getClass().getResource("/").getPath(); // 得到d:/tomcat/webapps/工程名WEB-INF/classes/路径
		path = path.substring(0, path.indexOf("WEB-INF/classes"));// 从路径字符串中取出工程路劲
		path = path.replace("%20", " ");
		File file = new File(path);
		if (!file.exists()) {
			path = path.substring(1, path.length());
		} else {
			path = file.getPath();
		}

		return path;
	}

	/***
	 * 添加6位码数据;
	 * 
	 * @param sb
	 *            作为引用对象使用
	 * @param pageSize
	 * @param shopCode
	 * @param channelCode
	 * @param wareStatus
	 * @return
	 */
	private Map<String, Object> writeStringForSixCode(StringBuffer sb, int pageSize, String shopCode, String channelCode, String wareStatus) {

		logger.info("ExportOnsaleGoodsTask.writeStringForSixCode start,shopCode=" + shopCode);

		StringBuffer illegalData = new StringBuffer();
		LocalItemQuery itemQuery = new LocalItemQuery();
		itemQuery.setChannelCode(channelCode);
		itemQuery.setShopCode(shopCode);
		itemQuery.setItemNo(null);
		itemQuery.setPage(1);
		itemQuery.setPageSize(pageSize);
		itemQuery.setStatus(wareStatus);
		itemQuery.setUserName(USER_NAME);
		ApiResultVO<List<ChannelApiGoods>> rapiResultVO = apiService.searchItemPage(itemQuery);
		List<ChannelApiGoods> flist = rapiResultVO.getApiGoods();
		// 有异常返回
		if (!"0".equals(rapiResultVO.getCode())) {
			logger.info("导出数据 ApiService.searchItemPage 分页查询总数失败！shopCode=" + shopCode + "页码：" + 1 + "上下架状态：" + wareStatus);
			Map<String, Object> returnDataMap = new HashMap<String, Object>();
			returnDataMap.put("export_data", sb);
			returnDataMap.put("illegal_data", illegalData);
			return returnDataMap;
		}

		int iTotal = rapiResultVO.getTotal();

		// int pageNum =0;
		int pageNum = iTotal / pageSize;
		if (iTotal % pageSize > 0) {
			++pageNum; // 获取总得页数
		}
		if (pageNum > 1) {
			for (int j = 1; j <= pageNum; j++) {
				ApiResultVO<List<ChannelApiGoods>> apiResultVO = new ApiResultVO<List<ChannelApiGoods>>();
				itemQuery.setPage(j);
				apiResultVO = apiService.searchItemPage(itemQuery);
				List<ChannelApiGoods> list = apiResultVO.getApiGoods();
				if (!"0".equals(apiResultVO.getCode())) {
					logger.info("导出数据 ApiService.searchItemPage 分页查询失败！shopCode=" + shopCode + "页码：" + j + "上下架状态：" + wareStatus);
					continue;
				}

				if (null != list && list.size() > 0) {
					String goodsSn = "";
					String isOnSell = "";
					for (int i = 0; i < list.size(); i++) {

						if (StringUtil.isEmpty(list.get(i).getChannelGoodsSn())) {
							illegalData.append(list.get(i).getGoodsSn() + "\n");
						}

						// 同步渠道商品的上下架状态
						goodsSn = list.get(i).getGoodsSn();

						sb.append(list.get(i).getShopCode() == null ? "" : list.get(i).getShopCode() + ",");
						sb.append(list.get(i).getShopName() == null ? "" : list.get(i).getShopName() + ",");
						sb.append(list.get(i).getGoodsSn() == null ? "" : list.get(i).getGoodsSn() + ",");
						sb.append(list.get(i).getChannelGoodsSn() == null ? " " : list.get(i).getChannelGoodsSn()).append(",");
						sb.append(list.get(i).getGoodsName() == null ? "" : list.get(i).getGoodsName() + ",");
						sb.append(list.get(i).getPrice() == null ? "" : list.get(i).getPrice() + ",");
						String status = list.get(i).getStatus() == null ? "" : list.get(i).getStatus();
						if (StringUtil.isNotNull(status) && "1".equals(status)) {
							status = "上架";
							isOnSell = "1";
						}
						if (StringUtil.isNotNull(status) && "2".equals(status)) {
							status = "下架";
							isOnSell = "0";
						}
						if (StringUtil.isNotNull(status) && "8".equals(status)) {
							status = "已售完";
							isOnSell = "0";
						}
						sb.append(status + ",");

						sb.append(list.get(i).getStockCount() == null ? "请参照11位码库存" : list.get(i).getStockCount());
						sb.append("\r\n");

						// 同步渠道商品的上下架状态
						shelvesUpDownSyn(shopCode, goodsSn, isOnSell);
					}
				}
			}

		} else {

			if (null != flist && flist.size() > 0) {
				for (int i = 0; i < flist.size(); i++) {
					if (StringUtil.isEmpty(flist.get(i).getChannelGoodsSn())) {
						illegalData.append(flist.get(i).getGoodsSn() + "\n");
					}
					sb.append(flist.get(i).getShopCode() == null ? "" : flist.get(i).getShopCode() + ",");
					sb.append(flist.get(i).getShopName() == null ? "" : flist.get(i).getShopName() + ",");
					sb.append(flist.get(i).getGoodsSn() == null ? "" : flist.get(i).getGoodsSn() + ",");
					sb.append(flist.get(i).getChannelGoodsSn() == null ? " " : flist.get(i).getChannelGoodsSn()).append(",");
					sb.append(flist.get(i).getGoodsName() == null ? "" : flist.get(i).getGoodsName() + ",");
					sb.append(flist.get(i).getPrice() == null ? "" : flist.get(i).getPrice() + ",");
					String status = flist.get(i).getStatus() == null ? "" : flist.get(i).getStatus();
					if (StringUtil.isNotNull(status) && "1".equals(status)) {
						status = "上架";
					}
					if (StringUtil.isNotNull(status) && "2".equals(status)) {
						status = "下架";
					}
					if (StringUtil.isNotNull(status) && "8".equals(status)) {
						status = "已售完";
					}
					sb.append(status + ",");

					sb.append(flist.get(i).getStockCount() == null ? "请参照11位码库存" : flist.get(i).getStockCount());
					sb.append("\r\n");
				}
			}
		}

		logger.info("ExportOnsaleGoodsTask.writeStringForSixCode end");
		Map<String, Object> returnDataMap = new HashMap<String, Object>();
		returnDataMap.put("export_data", sb);
		returnDataMap.put("illegal_data", illegalData);
		return returnDataMap;

	}

	/***
	 * 添加11位码数据;
	 * 
	 * @param sb
	 * @param pageSize
	 * @param shopCode
	 * @param channelCode
	 * @param wareStatus
	 * @return
	 */
	private Map<String, Object> writeStringForElevenCode(StringBuffer sb, int pageSize, String shopCode, String channelCode, String wareStatus) {

		logger.info("ExportOnsaleGoodsTask.writeStringForElevenCode start ,shopCode=" + shopCode);
		StringBuffer illegalData = new StringBuffer();
//		ApiResultVO rapiResultVO = apiService.searchChildItemPage(channelCode, shopCode, "", "", 1, pageSize, wareStatus, USER_NAME);
		ApiResultVO<List<ChannelApiGoods>> rapiResultVO = null;
		List<ChannelApiGoods> flist = rapiResultVO.getApiGoods();

		// 有异常返回
		if (!"0".equals(rapiResultVO.getCode())) {
			logger.info("查询总记录数失败！");
			Map<String, Object> returnDataMap = new HashMap<String, Object>();
			returnDataMap.put("export_data", sb);
			returnDataMap.put("illegal_data", illegalData);
			return returnDataMap;
		}

		int iTotal = rapiResultVO.getTotal();

		// int pageNum =0;
		int pageNum = iTotal / pageSize;
		if (iTotal % pageSize > 0) {
			++pageNum;
		}

		// 大于1页;
		if (pageNum > 1) {

			for (int j = 1; j <= pageNum; j++) {
				ApiResultVO<List<ChannelApiGoods>> apiResultVO = new ApiResultVO();
//				apiResultVO = apiService.searchChildItemPage(channelCode, shopCode, "", "", j, pageSize, wareStatus, USER_NAME);
				List<ChannelApiGoods> list = apiResultVO.getApiGoods();

				if (!"0".equals(apiResultVO.getCode())) {
					logger.info("apiService.searchChildItemPage 查询失败 shopcode=" + shopCode + "页码：" + j);
				}

				if (null != list && list.size() > 0) {
					for (int i = 0; i < list.size(); i++) {

						ChannelApiGoods father = list.get(i);
						List<ChannelApiGoods> zzlist = list.get(i).getApiGoodsChild();

						if (null != zzlist && zzlist.size() > 0) {
							for (int z = 0; z < zzlist.size(); z++) {
								if (StringUtil.isEmpty(zzlist.get(z).getChannelGoodsSn())) {
									illegalData.append(zzlist.get(z).getGoodsSn() + "\n");
								}
								sb.append(father.getShopCode() == null ? "" : father.getShopCode() + ",");
								sb.append(father.getShopName() == null ? "" : father.getShopName() + ",");
								sb.append(zzlist.get(z).getGoodsSn() == null ? "" : zzlist.get(z).getGoodsSn() + ",");
								sb.append(zzlist.get(z).getChannelGoodsSn() == null ? " " : zzlist.get(z).getChannelGoodsSn()).append(",");

								String goodsName = zzlist.get(z).getGoodsName() == null ? "" : zzlist.get(z).getGoodsName();
								if (!StringUtil.isNotNull(goodsName)) {
									goodsName = father.getGoodsName() == null ? "" : father.getGoodsName();
								}
								sb.append(goodsName + ",");

								sb.append(zzlist.get(z).getPrice() == null ? "" : zzlist.get(z).getPrice() + ",");

								String status = zzlist.get(z).getStatus() == null ? "0" : zzlist.get(z).getStatus();
								if (!StringUtil.isNotNull(status)) {
									status = father.getStatus() == null ? "" : father.getStatus();
								}
								if (StringUtil.isNotNull(status) && "1".equals(status)) {
									status = "上架";
								}
								if (StringUtil.isNotNull(status) && "2".equals(status)) {
									status = "下架";
								}
								if (Constants.PP_CHANNEL_CODE.equals(channelCode)) {
									// if(StringUtil.isNotNull(status) &&
									// "5".equals(status)){
									// status ="等待上架 ";
									// }
									if (StringUtil.isNotNull(status) && "8".equals(status)) {
										status = "已售完";
									}
								}
								sb.append(status + ",");
								sb.append(zzlist.get(z).getStockCount() == null ? "0" : zzlist.get(z).getStockCount());
								sb.append("\r\n");
							}
						}
					}
				}
			}

			// 1页
		} else {
			if (null != flist && flist.size() > 0) {
				for (int i = 0; i < flist.size(); i++) {

					ChannelApiGoods father = flist.get(i);
					List<ChannelApiGoods> zlist = flist.get(i).getApiGoodsChild();

					if (zlist.size() > 0) {
						for (int z = 0; z < zlist.size(); z++) {
							if (StringUtil.isEmpty(zlist.get(z).getChannelGoodsSn())) {
								illegalData.append(zlist.get(z).getGoodsSn() + "\n");
							}
							sb.append(father.getShopCode() == null ? "" : father.getShopCode() + ",");
							sb.append(father.getShopName() == null ? "" : father.getShopName() + ",");
							sb.append(zlist.get(z).getGoodsSn() == null ? "" : zlist.get(z).getGoodsSn() + ",");
							sb.append(zlist.get(z).getChannelGoodsSn() == null ? " " : zlist.get(z).getChannelGoodsSn()).append(",");

							String goodsName = zlist.get(z).getGoodsName() == null ? "" : zlist.get(z).getGoodsName();
							if (!StringUtil.isNotNull(goodsName)) {
								goodsName = father.getGoodsName() == null ? "" : father.getGoodsName();
							}

							sb.append(goodsName + ",");
							sb.append(zlist.get(z).getPrice() == null ? "" : zlist.get(z).getPrice() + ",");

							String status = zlist.get(z).getStatus() == null ? "0" : zlist.get(z).getStatus();
							if (!StringUtil.isNotNull(status)) {
								status = father.getStatus() == null ? "" : father.getStatus();
							}
							if (StringUtil.isNotNull(status) && "1".equals(status)) {
								status = "上架";
							}
							if (StringUtil.isNotNull(status) && "2".equals(status)) {
								status = "下架";
							}
							sb.append(status + ",");

							sb.append(zlist.get(z).getStockCount() == null ? "0" : zlist.get(z).getStockCount());
							sb.append("\r\n");
						}
					}
				}
			}
		}
		logger.info("ExportOnsaleGoodsTask.writeStringForElevenCode end");
		Map<String, Object> returnDataMap = new HashMap<String, Object>();
		returnDataMap.put("export_data", sb);
		returnDataMap.put("illegal_data", illegalData);
		return returnDataMap;
	}

	/**
	 * 导出6位码csv文件
	 * */
	private Map<String, Object> exportSixShopBusinessGoods(String channelCode, String shopCode) {
		logger.info("ExportOnsaleGoodsTask.exportSixShopBusinessGoods start，shopcode=" + shopCode);
		String path = getPath();
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, -1); // 得到前一天
		String dateBefore = DateTimeUtils.format(calendar.getTime(), DateTimeUtils.YYYY_MM_DD);
		File fileBefore = new File(path + "/page/shopBusinessGoods/exportFile/" + "onsale6_" + shopCode + "_" + dateBefore + ".csv");
		fileBefore.delete(); // 删除前一天生成的文件
		/**********************/
		String date = DateTimeUtils.format(new Date(), DateTimeUtils.YYYY_MM_DD);

		String fileName = "onsale6_" + shopCode + "_" + date + ".csv";

		// 文件路径
		String deployPath = FileUtil.getDeployPath();
		File file = new File(deployPath + "/page/shopBusinessGoods/exportFile/" + fileName);
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		logger.info("文件路径：" + file.getPath());

		int pageSize = 20;

		if (StringUtil.isTaoBaoChannel(channelCode)) {
			if (!Constants.TB_FX.equals(shopCode)) {
				pageSize = 200;
			}
		}
		if (Constants.JD_CHANNEL_CODE.equals(channelCode)) {
			pageSize = 100;
		}
		StringBuffer illegalData = new StringBuffer();
		StringBuffer sb = new StringBuffer("");
		sb.append("店铺编码" + "," + "店铺名称" + "," + "商品款号" + "," + "渠道商品款号" + "," + "店铺商品名称" + "," + "店铺商品价格" + "," + "上下架状态" + "," + "库存量" + "\r\n");
		Map<String, Object> data = writeStringForSixCode(sb, pageSize, shopCode, channelCode, "1");
		StringBuffer stringBuffer1 = (StringBuffer) data.get("export_data");
		illegalData.append(data.get("illegal_data"));

		data = writeStringForSixCode(stringBuffer1, pageSize, shopCode, channelCode, "2");
		StringBuffer stringBuffer2 = (StringBuffer) data.get("export_data");
		illegalData.append(data.get("illegal_data"));

		if (Constants.PP_CHANNEL_CODE.equals(channelCode)) {
			// 待上架商品
			// stringBuffer2 = writeStringForSixCode(stringBuffer2, pageSize,
			// shopCode,channelCode,"5");
			// 已售完商品
			data = writeStringForSixCode(stringBuffer2, pageSize, shopCode, channelCode, "8");
			stringBuffer2 = (StringBuffer) data.get("export_data");
			illegalData.append(data.get("illegal_data"));
		}

		BufferedWriter bw = null;
		try {
			bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "GBK"));
			bw.write(stringBuffer2.toString());
			bw.close();
		} catch (Exception e) {
			Log.info("error:" + e.getMessage());
			e.printStackTrace();
		} finally {
			if (bw != null) {
				try {
					logger.info("ExportOnsaleGoodsTask.exportSixShopBusinessGoods end");
					bw.close();
					bw = null;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		Map<String, Object> returnDataMap = new HashMap<String, Object>();
		returnDataMap.put("illegal_data", illegalData);
		return returnDataMap;
	}

	/**
	 * 导出11位码csv文件
	 **/
	private Map<String, Object> exportElevenShopBusinessGoods(String channelCode, String shopCode) {

		logger.info("ExportOnsaleGoodsTask.exportElevenShopBusinessGoods start，shopCode=" + shopCode);
		String path = getPath();
		int pageSize = 20;
		if (StringUtil.isTaoBaoChannel(channelCode)) {
			if (!Constants.TB_FX.equals(shopCode)) {
				pageSize = 200;
			}
		}
		if (Constants.JD_CHANNEL_CODE.equals(channelCode)) {
			pageSize = 100;
		}
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, -1); // 得到前一天
		String dateBefore = DateTimeUtils.format(calendar.getTime(), DateTimeUtils.YYYY_MM_DD);
		File fileBefore = new File(path + "/page/shopBusinessGoods/exportFile/" + "onsale11_" + shopCode + "_" + dateBefore + ".csv");
		fileBefore.delete(); // 删除前一天生成的文件
		/***********************************************/
		String date = DateTimeUtils.format(new Date(), DateTimeUtils.YYYY_MM_DD);

		String fileName = "onsale11_" + shopCode + "_" + date + ".csv";

		// 文件路径
		File file = new File(path + "/page/shopBusinessGoods/exportFile/" + fileName);
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		logger.info("文件路径：" + file.getPath());

		StringBuffer illegalData = new StringBuffer();
		StringBuffer sb = new StringBuffer();
		sb.append("店铺编码" + "," + "店铺名称" + "," + "商品款号" + "," + "渠道商品款号" + "," + "店铺商品名称" + "," + "店铺商品价格" + "," + "上下架状态" + "," + "库存量" + "\r\n");

		Map<String, Object> data = writeStringForElevenCode(sb, pageSize, shopCode, channelCode, "1");
		StringBuffer stringBuffer1 = (StringBuffer) data.get("export_data");
		illegalData.append(data.get("illegal_data"));

		data = writeStringForElevenCode(stringBuffer1, pageSize, shopCode, channelCode, "2");
		StringBuffer stringBuffer2 = (StringBuffer) data.get("export_data");
		illegalData.append(data.get("illegal_data"));

		if (Constants.PP_CHANNEL_CODE.equals(channelCode)) {
			// 待上架商品
			// stringBuffer2 = writeStringForElevenCode(stringBuffer2, pageSize,
			// shopCode,channelCode,"5");
			// 已售完商品
			data = writeStringForElevenCode(stringBuffer2, pageSize, shopCode, channelCode, "8");
			stringBuffer2 = (StringBuffer) data.get("export_data");
			illegalData.append(data.get("illegal_data"));
		}

		BufferedWriter bw = null;
		try {
			bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "GBK"));
			bw.write(stringBuffer2.toString());
		} catch (Exception e) {
			logger.error("error:" + e.getMessage());
			e.printStackTrace();
		} finally {
			if (bw != null) {
				try {
					logger.info("ExportOnsaleGoodsTask.exportElevenShopBusinessGoods end");
					bw.close();
					bw = null;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		Map<String, Object> returnDataMap = new HashMap<String, Object>();
		returnDataMap.put("illegal_data", illegalData);
		return returnDataMap;
	}

	private void shelvesUpDownSyn(String shopCode, String goodsSn, String isOnSell) {
		try {
			if (StringUtil.isEmpty(goodsSn)) {
				logger.info("同步商品上下架状态出错[shopCode:" + shopCode + ", goodsSn:" + goodsSn + "]: 商品编号为空!");
				return;
			}
			ChannelGoodsExample example = new ChannelGoodsExample();
			example.or().andChannelCodeEqualTo(shopCode).andGoodsSnEqualTo(goodsSn);

			List<ChannelGoods> list = channelGoodsMapper.selectByExample(example);

			if (list != null && list.size() == 1) {
				ChannelGoods record = list.get(0);
				record.setChannelCode(shopCode);
				record.setGoodsSn(goodsSn);
				record.setIsOnSell(Byte.valueOf(isOnSell));
				record.setLastUpdate(new Date());
				channelGoodsMapper.updateByExampleSelective(record, example);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("同步商品上下架状态出错[shopCode:" + shopCode + ", goodsSn:" + goodsSn + "]: " + e.getMessage());
		}
	}

	private void exportIllegalData(String channelCode, String shopCode, StringBuffer data) {
		logger.info("ExportOnsaleGoodsTask.exportIllegalData start，shopcode=" + shopCode);
		String path = getPath();
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, -1); // 得到前一天
		String dateBefore = DateTimeUtils.format(calendar.getTime(), DateTimeUtils.YYYY_MM_DD);
		File fileBefore = new File(path + "/page/shopBusinessGoods/exportFile/" + "ILLEGAL_DATA_" + shopCode + "_" + dateBefore + ".csv");
		fileBefore.delete(); // 删除前一天生成的文件
		/**********************/
		String date = DateTimeUtils.format(new Date(), DateTimeUtils.YYYY_MM_DD);

		String fileName = "ILLEGAL_DATA_" + shopCode + "_" + date + ".csv";

		// 文件路径
		String deployPath = FileUtil.getDeployPath();
		File file = new File(deployPath + "/page/shopBusinessGoods/exportFile/" + fileName);
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		logger.info("文件路径：" + file.getPath());

		StringBuffer illegalData = new StringBuffer("");
		illegalData.append("非法数据(6位码、11位码)" + "\r\n").append(data);

		BufferedWriter bw = null;
		try {
			bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "GBK"));
			bw.write(illegalData.toString());
			bw.close();
		} catch (Exception e) {
			Log.info("error:" + e.getMessage());
			e.printStackTrace();
		} finally {
			if (bw != null) {
				try {
					logger.info("ExportOnsaleGoodsTask.exportSixShopBusinessGoods end");
					bw.close();
					bw = null;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

}

package com.work.shop.scheduler;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
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
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.banggo.scheduler.exception.FatalException;
import com.banggo.scheduler.exception.WarnningException;
import com.banggo.scheduler.task.RunInOneNodeTask;
import com.banggo.scheduler.task.TaskExecuteRequest;
import com.work.shop.api.bean.ApiResultVO;
import com.work.shop.api.bean.ChannelApiGoods;
import com.work.shop.api.bean.LocalItemQuery;
import com.work.shop.api.service.ApiService;
import com.work.shop.bean.ChannelGoods;
import com.work.shop.bean.ChannelGoodsExample;
import com.work.shop.bean.ChannelShopExample;
import com.work.shop.bean.ChannelShopExample.Criteria;
import com.work.shop.dao.ChannelGoodsMapper;
import com.work.shop.scheduler.service.BaseScheduleService;
import com.work.shop.service.ShopService;
import com.work.shop.util.Constants;
import com.work.shop.util.DateTimeUtils;
import com.work.shop.util.FileUtil;
import com.work.shop.util.FtpUtil;
import com.work.shop.util.PropertieFileReader;
import com.work.shop.util.StringUtil;
import com.work.shop.util.TimeUtil;
import com.work.shop.vo.ChannelShopVo;

@Component("exportOnSaleDataTimerTask")
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ExportOnSaleDataTimerTask extends RunInOneNodeTask implements BaseScheduleService {

	private Logger logger = Logger.getLogger(ExportOnSaleDataTimerTask.class);

	@Resource(name = "apiService")
	private ApiService apiService;

	@Resource(name = "shopService")
	private ShopService shopService;

	@Resource(name = "channelGoodsMapper")
	private ChannelGoodsMapper channelGoodsMapper;

	private final static String USER_NAME = "System";

	@Override
	public void doScheduler() {
	}

	/**
	 * 删除指定文件夹下所有文件
	 * 
	 * @param path
	 *            文件夹完整绝对路径
	 * @return
	 */
	public boolean delAllFile(String path, String delReg) {
		boolean flag = false;
		File file = new File(path);
		if (!file.exists()) {
			return flag;
		}
		if (!file.isDirectory()) {
			return flag;
		}
		String[] tempList = file.list();
		File temp = null;
		for (int i = 0; i < tempList.length; i++) {
			String name = tempList[i];
			if (name.indexOf(delReg) != -1) {
				if (path.endsWith(File.separator)) {
					temp = new File(path + tempList[i]);
				} else {
					temp = new File(path + File.separator + tempList[i]);
				}
				if (temp.isFile()) {
					logger.info("删除文件路径：" + temp.getPath());
					temp.delete();
				}
			}
		}
		return flag;
	}

	@Override
	public void execute(TaskExecuteRequest request) throws WarnningException, FatalException {
		String shopCodeParam = request.getParam("shopCodes");
		logger.info("调度中心操作：开始导出平台[" + shopCodeParam + "]经营商品    start 时间：" + DateTimeUtils.format(new Date(), DateTimeUtils.YYYY_MM_DD_HH_mm_ss));
//		String path = getPath();
//		File filePath = new File(path + "/page/shopBusinessGoods/exportFile/");
//		if (filePath.exists()) {
//			delAllFile(filePath.getPath(), shopCodeParam);
//		}
		ChannelShopExample example = new ChannelShopExample();
		Criteria criteria = example.or();
		criteria.andShopCodeEqualTo(shopCodeParam);
		criteria.andShopStatusEqualTo(Byte.valueOf("1")); // 默认加载已激活状态的店铺
		criteria.andShopChannelEqualTo(Byte.valueOf("1"));
		List<ChannelShopVo> list = shopService.getChannelShopList(example, false);
		if (list != null && list.size() == 1) {
			ChannelShopVo channelShop = list.get(0);
			if (channelShop != null) {
				if (StringUtil.isNotNull(channelShop.getChannelCode()) && StringUtil.isNotNull(channelShop.getShopCode())) {
					String channelCode = channelShop.getChannelCode();
					String shopCode = channelShop.getShopCode();
					try {
//						exportData(channelCode, shopCode);
						exportOnlineChannelGoods(channelCode, shopCode);
					} catch (Exception e) {
						logger.error("[channelCode : " + channelCode + " , shopCode : " + shopCode + "] 数据导出异常", e);
					}
				}
			}
		}
		logger.info("调度中心操作：导出平台[" + shopCodeParam + "]经营商品    end");
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

		logger.info("ExportOnSaleDataTimerTask.writeStringForSixCode start,shopCode=" + shopCode);

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
						sb.append(list.get(i).getGoodsSn() == null ? " " : list.get(i).getGoodsSn()).append(",");
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
					sb.append(flist.get(i).getGoodsSn() == null ? " " : flist.get(i).getGoodsSn()).append(",");
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

		logger.info("ExportOnSaleDataTimerTask.writeStringForSixCode end");
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

		logger.info("ExportOnSaleDataTimerTask.writeStringForElevenCode start ,shopCode=" + shopCode);
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
								sb.append(zzlist.get(z).getGoodsSn() == null ? " " : zzlist.get(z).getGoodsSn()).append(",");
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
							sb.append(zlist.get(z).getGoodsSn() == null ? " " : zlist.get(z).getGoodsSn()).append(",");
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
		logger.info("ExportOnSaleDataTimerTask.writeStringForElevenCode end");
		Map<String, Object> returnDataMap = new HashMap<String, Object>();
		returnDataMap.put("export_data", sb);
		returnDataMap.put("illegal_data", illegalData);
		return returnDataMap;
	}

	/**
	 * 导出6位码csv文件
	 * */
	private Map<String, Object> exportSixShopBusinessGoods(String channelCode, String shopCode) {
		logger.info("ExportOnSaleDataTimerTask.exportSixShopBusinessGoods start，shopcode=" + shopCode);

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
					logger.info("ExportOnSaleDataTimerTask.exportSixShopBusinessGoods end");
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

		logger.info("ExportOnSaleDataTimerTask.exportElevenShopBusinessGoods start，shopCode=" + shopCode);
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
					logger.info("ExportOnSaleDataTimerTask.exportElevenShopBusinessGoods end");
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
		logger.info("ExportOnSaleDataTimerTask.exportIllegalData start，shopcode=" + shopCode);

		String date = DateTimeUtils.format(new Date(), DateTimeUtils.YYYY_MM_DD);

		String fileName = "ILLEGAL_DATA_" + shopCode + "_" + date + ".csv";

		// 文件路径
		String deployPath = FileUtil.getDeployPath();
		File file = new File(deployPath + "/page/shopBusinessGoods/exportFile/" + fileName);
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				logger.error(file.getPath() + "创建文件路径异常", e);
			}
		}
		logger.info("文件路径：" + file.getPath());

		BufferedWriter bw = null;
		try {
			bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "GBK"));
			bw.write(data.toString());
			bw.close();
		} catch (Exception e) {
			logger.error("导出异常数据异常", e);
		} finally {
			if (bw != null) {
				try {
					logger.info("ExportOnSaleDataTimerTask.exportIllegalData end");
					bw.close();
					bw = null;
				} catch (IOException e) {
					logger.error("关闭导出异常数据流异常", e);
				}
			}
		}
	}

	private void exportSixData(String channelCode, String shopCode, StringBuffer data) {
		logger.info("ExportOnSaleDataTimerTask.exportSixData start，shopcode=" + shopCode);

		String date = DateTimeUtils.format(new Date(), DateTimeUtils.YYYY_MM_DD);

		String fileName = "onsale6_" + shopCode + "_" + date + ".csv";

		// 文件路径
		String deployPath = FileUtil.getDeployPath();
		File file = new File(deployPath + "/page/shopBusinessGoods/exportFile/" + fileName);
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				logger.error(file.getPath() + "创建文件路径异常", e);
			}
		}
		BufferedWriter bw = null;
		try {
			bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "GBK"));
			bw.write(data.toString());
			bw.close();
		} catch (Exception e) {
			logger.error("导出6位码数据异常", e);
		} finally {
			if (bw != null) {
				try {
					bw.close();
					bw = null;
				} catch (IOException e) {
					logger.error("关闭六位码数据流异常", e);
				}
			}
		}
	}

	private void exportElevenData(String channelCode, String shopCode, StringBuffer data) {
		logger.info("ExportOnSaleDataTimerTask.exportElevenData start,shopcode=" + shopCode);

		String date = DateTimeUtils.format(new Date(), DateTimeUtils.YYYY_MM_DD);

		String fileName = "onsale11_" + shopCode + "_" + date + ".csv";

		// 文件路径
		String deployPath = FileUtil.getDeployPath();
		File file = new File(deployPath + "/page/shopBusinessGoods/exportFile/" + fileName);
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				logger.error(file.getPath() + "创建文件路径异常", e);
			}
		}

		BufferedWriter bw = null;
		try {
			bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "GBK"));
			bw.write(data.toString());
			bw.close();
		} catch (Exception e) {
			logger.error("导出11位码数据异常", e);
		} finally {
			if (bw != null) {
				try {
					logger.info("ExportOnSaleDataTimerTask.exportElevenData end");
					bw.close();
					bw = null;
				} catch (IOException e) {
					logger.error("关闭11位码数据流异常", e);
				}
			}
		}
	}

	private void exportData(String channelCode, String shopCode) {
		logger.info("ExportOnSaleDataTimerTask.exportData start，shopcode=" + shopCode);
		StringBuffer sixData = new StringBuffer();
		sixData.append("店铺编码" + "," + "店铺名称" + "," + "商品款号" + "," + "渠道商品款号" + "," + "店铺商品名称" + "," + "店铺商品价格" + "," + "上下架状态" + "," + "库存量" + "\r\n");
		StringBuffer elevenData = new StringBuffer();
		elevenData.append("店铺编码" + "," + "店铺名称" + "," + "商品款号" + "," + "渠道商品款号" + "," + "店铺商品名称" + "," + "店铺商品价格" + "," + "上下架状态" + "," + "库存量" + "\r\n");
		StringBuffer illegalData = new StringBuffer();
		illegalData.append("非法数据(6位码、11位码)" + "\r\n");
		Map<String, StringBuffer> dataMap = new HashMap<String, StringBuffer>();
		dataMap.put("six_data", sixData);
		dataMap.put("eleven_data", elevenData);
		dataMap.put("illegal_data", illegalData);

		int pageSize = 20;
		if (StringUtil.isTaoBaoChannel(channelCode)) {
			if (!Constants.TB_FX.equals(shopCode)) {
				pageSize = 200;
			}
		}
		if (Constants.JD_CHANNEL_CODE.equals(channelCode)) {
			pageSize = 100;
		}
		if (Constants.YHD_CHANNEL_CODE.equals(channelCode)) {
			pageSize = 50;
		}
		try {
			dataMap = exportData(pageSize, shopCode, channelCode, "1", dataMap);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("[channelCode : " + channelCode + " , shopCode : " + shopCode + "] 导出上架商品异常" + e.getMessage());
		}
		try {
			dataMap = exportData(pageSize, shopCode, channelCode, "2", dataMap);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("[channelCode : " + channelCode + " , shopCode : " + shopCode + "] 导出下架商品异常" + e.getMessage());
		}
		try {
			if (Constants.PP_CHANNEL_CODE.equals(channelCode)) {
				// 已售完商品
				dataMap = exportData(pageSize, shopCode, channelCode, "8", dataMap);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("[channelCode : " + channelCode + " , shopCode : " + shopCode + "] 导出已售完商品异常" + e.getMessage());
		}

		exportSixData(channelCode, shopCode, dataMap.get("six_data"));
		exportElevenData(channelCode, shopCode, dataMap.get("eleven_data"));
		exportIllegalData(channelCode, shopCode, dataMap.get("illegal_data"));

		logger.info("ExportOnSaleDataTimerTask.exportData end，shopcode=" + shopCode);
	}

	private Map<String, StringBuffer> exportData(int pageSize, String shopCode, String channelCode, String wareStatus, Map<String, StringBuffer> dataMap) {
		logger.info("ExportOnSaleDataTimerTask.exportData start ,shopCode=" + shopCode + ", wareStatus=" + wareStatus);
		StringBuffer sixData = dataMap.get("six_data");
		StringBuffer elevenData = dataMap.get("eleven_data");
		StringBuffer illegalData = dataMap.get("illegal_data");
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
		if (!"0".equals(rapiResultVO.getCode())) {
			logger.info("查询总记录数失败！");
			dataMap.put("six_data", sixData);
			dataMap.put("eleven_data", elevenData);
			dataMap.put("illegal_data", illegalData);
			return dataMap;
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

		ApiResultVO<List<ChannelApiGoods>> apiResultVO = null;
		// String goodsSn = "";
		// String isOnSell = "";
		for (int j = 1; j <= pageNum; j++) {
//			apiResultVO = apiService.searchChildItemPage(channelCode, shopCode, "", "", j, pageSize, wareStatus, USER_NAME);
			List<ChannelApiGoods> channelApiGoodsList = apiResultVO.getApiGoods();
			for (ChannelApiGoods six : channelApiGoodsList) { // 6位码数据
				// 6位码非法数据验证
				if (StringUtil.isEmpty(six.getChannelGoodsSn())) {
					illegalData.append(six.getGoodsSn() + "\n");
				}
				// 同步渠道商品的上下架状态
				// goodsSn = six.getGoodsSn();
				try {
					sixData.append(six.getShopCode() == null ? "" : six.getShopCode() + ",");
					sixData.append(six.getShopName() == null ? "" : six.getShopName() + ",");
					sixData.append(six.getGoodsSn() == null ? " " : six.getGoodsSn()).append(",");
					sixData.append(six.getChannelGoodsSn() == null ? " " : six.getChannelGoodsSn()).append(",");
					sixData.append(six.getGoodsName() == null ? "" : six.getGoodsName() + ",");
					sixData.append(six.getPrice() == null ? "" : six.getPrice() + ",");
					String status = six.getStatus() == null ? "" : six.getStatus();
					if (StringUtil.isNotNull(status) && "1".equals(status)) {
						status = "上架";
						// isOnSell = "1";
					}
					if (StringUtil.isNotNull(status) && "2".equals(status)) {
						status = "下架";
						// isOnSell = "0";
					}
					if (StringUtil.isNotNull(status) && "8".equals(status)) {
						status = "已售完";
						// isOnSell = "0";
					}
					sixData.append(status + ",");
					sixData.append(six.getStockCount() == null ? "请参照11位码库存" : six.getStockCount());
					sixData.append("\r\n");
				} catch (Exception e) {
					e.printStackTrace();
					logger.error("[channelCode : " + channelCode + " , shopCode : " + shopCode +  " , goodsSn : " + six.getGoodsSn() + "] 数据导出解析异常" + e.getMessage());
				}
				
				// 同步渠道商品的上下架状态
				// shelvesUpDownSyn(shopCode, goodsSn, isOnSell);

				List<ChannelApiGoods> childList = six.getApiGoodsChild();
				if (childList != null && childList.size() > 0) {
					for (ChannelApiGoods eleven : childList) { // 11位码数据

						// 11位码非法数据验证
						if (StringUtil.isEmpty(eleven.getChannelGoodsSn())) {
							illegalData.append(eleven.getGoodsSn() + "\n");
						}
						try {
							elevenData.append(six.getShopCode() == null ? "" : six.getShopCode() + ",");
							elevenData.append(six.getShopName() == null ? "" : six.getShopName() + ",");
							elevenData.append(eleven.getGoodsSn() == null ? " " : eleven.getGoodsSn()).append(",");
							elevenData.append(eleven.getChannelGoodsSn() == null ? " " : eleven.getChannelGoodsSn()).append(",");

							String goodsName = eleven.getGoodsName() == null ? "" : eleven.getGoodsName();
							if (!StringUtil.isNotNull(goodsName)) {
								goodsName = six.getGoodsName() == null ? "" : six.getGoodsName();
							}
							elevenData.append(goodsName + ",");

							elevenData.append(eleven.getPrice() == null ? "" : eleven.getPrice() + ",");

							String status = eleven.getStatus() == null ? "" : eleven.getStatus();
							if (!StringUtil.isNotNull(status)) {
								status = six.getStatus() == null ? "" : six.getStatus();
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
							elevenData.append(status + ",");
							elevenData.append(eleven.getStockCount() == null ? "0" : eleven.getStockCount());
							elevenData.append("\r\n");
						} catch (Exception e) {
							logger.error("[channelCode : " + channelCode + " , shopCode : " + shopCode + " , goodsSn : "
									+ eleven.getGoodsSn() + "] 数据导出解析异常", e);
						}
					}
				}
			}
		}
		dataMap.put("six_data", sixData);
		dataMap.put("eleven_data", elevenData);
		dataMap.put("illegal_data", illegalData);
		return dataMap;
	}
	
	
	/**
	 * 定时导出在售商品和非法数据
	 * @param channelCode
	 * @param shopCode
	 * @return
	 */
	private Map<String, Object> exportOnlineChannelGoods(String channelCode, String shopCode) {
		logger.info("ExportOnSaleDataTimerTask.exportOnlineChannelGoods start，shopcode=" + shopCode);
		String date= DateTimeUtils.format(new Date(), DateTimeUtils.dateTimeString4FileName);
		String goodsFileName = "GOODS_SN_" + shopCode + "_" +date+ ".csv";
		String skuFileName = "SKU_" + shopCode + "_" +date+ ".csv";
		String illegalFileName = "ERROR_" + shopCode + "_" +date+ ".csv";
		// 文件路径
		String deployPath = FileUtil.getDeployPath();
		String date1= DateTimeUtils.format(new Date(), DateTimeUtils.YYYY_MM_DD);
		String path = deployPath+ "/page/shopBusinessGoods/exportFile/"+date1+"/";
		String rootpath =  deployPath+ "/page/shopBusinessGoods/exportFile/";
		File rootfile = new File(rootpath);
		File file = new File(path);
		try {
			if(!rootfile.exists()){
				rootfile.mkdir();
			}
			if(!file.exists()){
				file.mkdir();
			}
		} catch (Exception e) {
			logger.error("创建exportFile文件夹异常", e);
		}
		
		// 删除前一天文件
				try {
					Calendar calendar = Calendar.getInstance();
					calendar.add(Calendar.DAY_OF_YEAR, -1);
					String beforeDate = DateTimeUtils.format(calendar.getTime(), DateTimeUtils.YYYY_MM_DD);
					String beforeFilePath = deployPath+ "/page/shopBusinessGoods/exportFile/"+beforeDate;
					File beforeFile = new File(beforeFilePath);
					//删除前一天临时文件夹下所有文件及文件夹
					deleteDir(beforeFile);
//					String beforeGoodsFileName = "GOODS_SN_" + shopCode + "_" + beforeDate + ".csv";
//					String beforeSkuFileName = "SKU_" + shopCode + "_" + beforeDate + ".csv";
//					String beforeErrorFileName = "ERROR_" + shopCode + "_" + beforeDate + ".csv";
//					File beforeGoodsFile = new File(path + beforeGoodsFileName);
//					File beforeSkuFile = new File(path + beforeSkuFileName);
//					File beforeErrorFile = new File(path + beforeErrorFileName);
//					if (beforeGoodsFile.isFile()) {
//						beforeGoodsFile.delete();
//					}
//					if (beforeSkuFile.isFile()) {
//						beforeSkuFile.delete();
//					}
//					if (beforeErrorFile.isFile()) {
//						beforeErrorFile.delete();
//					}
				} catch (Exception e) {
					logger.error("删除前一天生成文件异常", e);
				}
		
		File goodsFile = new File(path + goodsFileName);
		if (!goodsFile.exists()) {
			try {
				goodsFile.createNewFile();
			} catch (IOException e) {
				logger.info("创建文件路径：" + goodsFile.getPath() + "异常" , e);
			}
		}
		
		File skuFile = new File(path + skuFileName);
		if (!skuFile.exists()) {
			try {
				skuFile.createNewFile();
			} catch (IOException e) {
				logger.info("创建文件路径：" + skuFile.getPath() + "异常" , e);
			}
		}
		File errorFile = new File(path + illegalFileName);
		if (!errorFile.exists()) {
			try {
				errorFile.createNewFile();
			} catch (IOException e) {
				logger.info("创建文件路径：" + errorFile.getPath() + "异常" , e);
			}
		}
		StringBuffer illegalData = new StringBuffer();
		illegalData.append("非法数据(6位码、11位码)" + "\r\n");
		StringBuffer sb = new StringBuffer("");
		sb.append("店铺编码,店铺名称,商品款号,渠道商品款号,店铺商品名称,店铺商品价格,上下架状态,库存量\r\n");
		BufferedWriter goodsBw = null;
		BufferedWriter skuBw = null;
		BufferedWriter illegalBw = null;
		try {
			goodsBw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(goodsFile), "GBK"));
			goodsBw.write(sb.toString());
			goodsBw.flush();
			skuBw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(skuFile), "GBK"));
			skuBw.write(sb.toString());
			skuBw.flush();
			illegalBw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(errorFile), "GBK"));
			illegalBw.write(illegalData.toString());
			illegalBw.flush();
			onlineChannelGoodsWriteFile(shopCode, channelCode, "1", goodsBw, skuBw, illegalBw);
			onlineChannelGoodsWriteFile(shopCode, channelCode, "2", goodsBw, skuBw, illegalBw);
			if (Constants.PP_CHANNEL_CODE.equals(channelCode)) {
				onlineChannelGoodsWriteFile(shopCode, channelCode, "8", goodsBw, skuBw, illegalBw);
			}
			goodsBw.flush();
			skuBw.flush();
			illegalBw.flush();
			String tFtpPath = PropertieFileReader.getString("ftp_path") + "/"+ TimeUtil.format2Date(new Date())+"/"+shopCode;
			FileInputStream goodsis = new FileInputStream(path+ goodsFileName);
			FileInputStream skuis = new FileInputStream(path+ skuFileName);
			FileInputStream illegalis = new FileInputStream(path+ illegalFileName);
			HashMap<String, Object> goodsMap = FtpUtil.uploadFile(goodsFileName, goodsis,tFtpPath +"/goods/");
			HashMap<String, Object> skuMap = FtpUtil.uploadFile(skuFileName, skuis, tFtpPath +"/sku/");
			HashMap<String, Object> illegalMap = FtpUtil.uploadFile(illegalFileName, illegalis, tFtpPath +"/illegal/");
			if((Boolean)goodsMap.get("isok")) {
				logger.info("文件"+goodsFileName+"，ftp服务器路径"+tFtpPath +"/goods/");
			} else {
				logger.error("文件"+goodsFileName+"存放ftp服务器失败！");
			}
			if((Boolean)skuMap.get("isok")) {
				logger.info("文件"+skuFileName+"，ftp服务器路径"+tFtpPath +"/sku/");
			} else {
				logger.error("文件"+skuFileName+"存放ftp服务器失败！");
			}
			if((Boolean)illegalMap.get("isok")) {
				logger.info("文件"+illegalFileName+"，ftp服务器路径"+tFtpPath +"/illegal/");
			} else {
				logger.error("文件"+illegalFileName+"存放ftp服务器失败！");
			}
		} catch (Exception e) {
			logger.error("error:", e);
		} finally {
			try {
				if (goodsBw != null) {
					goodsBw.close();
					goodsBw = null;
				}
				if (skuBw != null) {
					skuBw.close();
					skuBw = null;
				}
				if (illegalBw != null) {
					illegalBw.close();
					illegalBw = null;
				}
			} catch (Exception e2) {
				logger.error("关闭流异常", e2);
			}
			logger.info("ExportOnSaleDataTimerTask.exportOnlineChannelGoods end，shopcode=" + shopCode);
		}
		return null;
	}
	
	/**
	 * 将查询出的商品写入流文件
	 * @param pageSize
	 * @param shopCode
	 * @param channelCode
	 * @param wareStatus
	 */
	private void onlineChannelGoodsWriteFile(String shopCode,
			String channelCode, String wareStatus, BufferedWriter goodsBw , BufferedWriter skuBw , BufferedWriter illegalBw) {
		logger.info("onlineChannelGoodsWriteFile start ,shopCode=" + shopCode + ", wareStatus=" + wareStatus);
		try {
			int pageSize = 20;
			if (StringUtil.isTaoBaoChannel(channelCode)) {
				if (!Constants.TB_FX.equals(shopCode)) {
					pageSize = 200;
				}
			}
			if (Constants.JD_CHANNEL_CODE.equals(channelCode)) {
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
			if (!"0".equals(rapiResultVO.getCode())) {
				logger.error(channelCode + " " + shopCode+" 查询总记录数失败！");
				return ;
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
			ApiResultVO<List<ChannelApiGoods>> apiResultVO = null;
			for (int j = 1; j <= pageNum; j++) {
//				apiResultVO = apiService.searchChildItemPage(channelCode, shopCode, "", "", j, pageSize, wareStatus, USER_NAME);
				List<ChannelApiGoods> channelApiGoodsList = apiResultVO.getApiGoods();
				for (ChannelApiGoods apiGoods : channelApiGoodsList) { // 6位码数据
					StringBuffer inputGoodsMsg = new StringBuffer();
					StringBuffer inputSkuMsg = new StringBuffer();
					StringBuffer inputIllegalMsg = new StringBuffer();
					// 6位码非法数据验证
					if (StringUtil.isEmpty(apiGoods.getChannelGoodsSn())) {
						inputIllegalMsg.append(apiGoods.getGoodsSn() + "\n");
					}
					// 同步渠道商品的上下架状态
					String goodsSn = apiGoods.getGoodsSn() == null ? "" : apiGoods.getGoodsSn();
					String goodsName = apiGoods.getGoodsName() == null ? "" : apiGoods.getGoodsName();
					String status = apiGoods.getStatus() == null ? "" : apiGoods.getStatus();
					String shopName = apiGoods.getShopName() == null ? "" : apiGoods.getShopName();
					if (StringUtil.isNotNull(status) && "1".equals(status)) {
						status = "上架";
					}
					if (StringUtil.isNotNull(status) && "2".equals(status)) {
						status = "下架";
					}
					if (Constants.PP_CHANNEL_CODE.equals(channelCode)) {
						if (StringUtil.isNotNull(status) && "8".equals(status)) {
							status = "已售完";
						}
					}
					try {
						inputGoodsMsg.append(shopCode).append(",");
						inputGoodsMsg.append(shopName).append(",");
						inputGoodsMsg.append(goodsSn).append(",");
						inputGoodsMsg.append(apiGoods.getChannelGoodsSn() == null ? " " : apiGoods.getChannelGoodsSn()).append(",");
						inputGoodsMsg.append(goodsName).append(",");
						inputGoodsMsg.append(apiGoods.getPrice() == null ? "" : apiGoods.getPrice() + ",");
						inputGoodsMsg.append(status).append(",");
						inputGoodsMsg.append(apiGoods.getStockCount() == null ? "请参照11位码库存" : apiGoods.getStockCount());
						inputGoodsMsg.append("\r\n");
					} catch (Exception e) {
						logger.error("[channelCode:" + channelCode + " , shopCode : " + shopCode 
								+ " , goodsSn : " + goodsSn + "] 数据导出解析异常", e);
					}
					List<ChannelApiGoods> skuList = apiGoods.getApiGoodsChild();
					if (StringUtil.isListNotNull(skuList)) {
						for (ChannelApiGoods eleven : skuList) { // 11位码数据
							// 11位码非法数据验证
							if (StringUtil.isEmpty(eleven.getChannelGoodsSn())) {
								inputIllegalMsg.append(eleven.getGoodsSn() + "\n");
							}
							try {
								inputSkuMsg.append(shopCode).append(",");
								inputSkuMsg.append(shopName).append(",");
								inputSkuMsg.append(eleven.getGoodsSn() == null ? " " : eleven.getGoodsSn()).append(",");
								inputSkuMsg.append(eleven.getChannelGoodsSn() == null ? " " : eleven.getChannelGoodsSn()).append(",");
								inputSkuMsg.append(goodsName + ",");
								inputSkuMsg.append(eleven.getPrice() == null ? "" : eleven.getPrice() + ",");
								inputSkuMsg.append(status + ",");
								inputSkuMsg.append(eleven.getStockCount() == null ? "0" : eleven.getStockCount());
								inputSkuMsg.append("\r\n");
							} catch (Exception e) {
								logger.error("[channelCode:" + channelCode + ",shopCode:" + shopCode + ", goodsSn : "
										+ eleven.getGoodsSn() + "] 数据导出解析异常", e);
							}
						}
					}
					try {
						goodsBw.write(inputGoodsMsg.toString());
						inputGoodsMsg = null;
						goodsBw.flush();
						skuBw.write(inputSkuMsg.toString());
						inputSkuMsg = null;
						skuBw.flush();
						illegalBw.write(inputIllegalMsg.toString());
						inputIllegalMsg = null;
						illegalBw.flush();
					} catch (Exception e) {
						logger.error("信息写入文件异常：", e);
					}
				}
			}
		} catch (Exception e) {
			logger.error("生成商品列表文件", e);
		}
	}
	
	
    private static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int i=0; i<children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        return dir.delete();
    }
	
}

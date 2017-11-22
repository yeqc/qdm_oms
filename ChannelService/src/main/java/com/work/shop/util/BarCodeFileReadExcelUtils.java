package com.work.shop.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import au.com.bytecode.opencsv.CSVReader;

import com.work.shop.bean.TicketInfo;

public class BarCodeFileReadExcelUtils {

	public final static String REG_DIGIT = "\\d{1,10}\\.*\\d{0,2}"; // 判断字符串是否全为数字，否继续下次操作
	
	private static final Logger logger = Logger.getLogger(BarCodeFileReadExcelUtils.class);

	public boolean isDigit(String str) {
		return str.matches(REG_DIGIT);
	}
	/**
	 * 读取csv文件内容
	 * 
	 * @return List<TicketInfo>对象
	 * @throws IOException
	 * 输入/输出(i/o)异常
	 */
	public static List<TicketInfo> readCsv(InputStream is, String ticketCode,
			String shopCode, StringBuffer sb) throws Exception {
		CSVReader reader = new CSVReader(new InputStreamReader(is, "GBK"));
		List<TicketInfo> list = new ArrayList<TicketInfo>();
		try {
			String[] nextLine;
			TicketInfo ticketInfo = null;
			Map<String, String> map = new HashMap<String, String>();
			int j = 0;
			int i = 1;
			while ((nextLine = reader.readNext()) != null) {
				if (j != 0) {
					// 循环行Row
					ticketInfo = new TicketInfo();
					// 0商品款号
					// 商品款号
					String goodsSn = nextLine[0];
					if (StringUtil.isEmpty(goodsSn)) {
						continue;
					}
					String gs = "";
					gs = new BigDecimal(new Double(goodsSn)).toString();
					if (map.get(gs) != null) {
						sb.append("第" + (i) + "行[" + map.get(gs) + "]已存在,");
						continue;
					}
					map.put(gs, gs);
					ticketInfo.setGoodsSn(gs);// 商品款号

					ticketInfo.setTicketCode(ticketCode); // 绑定的调整单编号
					//2017-5-26 add by liyinghua 唯品会渠道默认不同步库存
					if("HQ01S155".equals(shopCode)){
						ticketInfo.setIsSyncStock("0");
					}
					
					list.add(ticketInfo);
				}
				j++;
				i++;
			}
		} catch (Exception e) {
			logger.error("读取csv文件内容异常", e);
		} finally {
			if (null != reader) {
				reader.close();
				reader = null;
			}
		}
		if (StringUtil.isNotEmpty(sb.toString())) {
			throw new Exception(sb.toString());
		}
		return list;
	}
	/**
	 * 读取xls文件内容
	 * 
	 * @return List<TicketInfo>对象
	 * @throws IOException
	 *             输入/输出(i/o)异常
	 */
	public static List<TicketInfo> readXls(InputStream is, String ticketCode, String shopCode, StringBuffer sb) throws Exception {
		HSSFWorkbook hssfWorkbook = new HSSFWorkbook(is);
		TicketInfo ticketInfo = null;
		List<TicketInfo> list = new ArrayList<TicketInfo>();
		Map<String, String> map = new HashMap<String, String>();
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
				// 0商品款号
				HSSFCell goodsSn = hssfRow.getCell((short) 0);
				if (goodsSn == null) {
					continue;
				}
				String gs = "";
				String goodsSnValue = getValue(goodsSn);
				gs = new BigDecimal(new Double(goodsSnValue)).toString();
				if (map.get(gs) != null) {
					sb.append("第" + (rowNum + 1) + "行[" + map.get(gs) + "]已存在,");
					continue;
				}
				map.put(gs, gs);
				ticketInfo.setGoodsSn(gs);// 商品款号

				ticketInfo.setTicketCode(ticketCode); // 绑定的调整单编号

				list.add(ticketInfo);
			}
		}
		if (StringUtil.isNotEmpty(sb.toString())) {
			throw new Exception(sb.toString());
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
			return String.valueOf(hssfCell.getStringCellValue());
		}
	}

}

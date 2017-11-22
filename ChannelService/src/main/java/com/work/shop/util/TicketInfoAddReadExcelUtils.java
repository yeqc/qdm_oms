package com.work.shop.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.stereotype.Service;

import au.com.bytecode.opencsv.CSVReader;

import com.work.shop.bean.ChannelGoods;
import com.work.shop.bean.TicketInfo;
import com.work.shop.bean.bgcontentdb.BGproductGoods;
import com.work.shop.redis.RedisClient;
import com.work.shop.service.ProductGoodsService;
import com.work.shop.service.ShopGoodsService;

@Service
public class TicketInfoAddReadExcelUtils {
	
	
	@Resource(name="shopGoodsService")
	private ShopGoodsService shopGoodsService; 
	
	@Resource(name="productGoodsService")
	private ProductGoodsService productGoodsService; 
	
	public final static String REG_DIGIT = "\\d{1,10}\\.*\\d{0,2}"; //判断字符串是否全为数字，否继续下次操作

	public boolean isDigit(String str) {
		return str.matches(REG_DIGIT);
	}
	
	@Resource(name = "redisClient")
	private RedisClient redisClient;
	
	/**
	 * 读取csv文件内容
	 * 
	 * @return List<TicketInfo>对象
	 * @throws IOException
	 * 输入/输出(i/o)异常
	 */
	public List<TicketInfo> readCsv(File file, String ticketCode,
			String shopCode, StringBuffer sb)
			throws IOException {
		CSVReader reader = new CSVReader(new InputStreamReader(
				new FileInputStream(file), "GBK"));
		List<TicketInfo> list = new ArrayList<TicketInfo>();
		try {
			// CSVReader reader = new CSVReader(new InputStreamReader(is,
			// "GBK"));
			// CSVReader reader = new CSVReader(new FileReader(file));

			Map<String, String> map = new HashMap<String, String>();
			String[] nextLine;
			int i = 1;
			TicketInfo ticketInfo = null;
			int j = 0;
			while ((nextLine = reader.readNext()) != null) {
				if (j != 0) {
					ticketInfo = new TicketInfo();
					String goodsSn = nextLine[0];
					if (StringUtil.isEmpty(goodsSn)) {
						continue;
					}
					String gs = goodsSn;
					if (map.get(gs) != null) {
						sb.append("第" + (i) + "行[" + map.get(gs) + "]已存在,");
						break;
					}
					map.put(gs, gs);
					ticketInfo.setGoodsSn(gs);// 商品款号
					String channelGoodsSn=redisClient.get(Constants.MOGU_GOODS_SN_KEY
							+ shopCode + "_" + gs);
					if(StringUtils.isNotBlank(channelGoodsSn)){
						ticketInfo.setChannelGoodssn(channelGoodsSn);
					}
					

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
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			if (reader != null) {
				try {
					reader.close();
					reader = null;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
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
   public  List<TicketInfo> readXls(InputStream is,String ticketCode,String shopCode,StringBuffer sb) throws IOException {
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
               // 0商品款号 1渠道商品款号 2商品名称 3商品保护价格，4商品新价格
               // for (int cellNum = 0; cellNum <=4; cellNum++) {
               HSSFCell goodsSn = hssfRow.getCell((short) 0);
               if (goodsSn == null) {
                   continue;
               }
               String gs = "";
               String goodsSnValue = getValue(goodsSn);
               gs = new BigDecimal(new Double(goodsSnValue)).toString();
               if(map.get(gs) != null){
            	   sb.append("第"+(rowNum+1)+"行["+map.get(gs)+"]已存在,");
            	   continue;
               }
               map.put(gs, gs);
               ticketInfo.setGoodsSn(gs);//商品款号

//               HSSFCell safePrice = hssfRow.getCell((short) 1);//商品保护价格
//               if (safePrice == null) {
//            	   sb.append("第"+(rowNum+1)+"行["+map.get(gs)+"]保护价异常,");
//                   continue;
//               }
//               if(!isDigit(getValue(safePrice))){
//            	   sb.append("第"+(rowNum+1)+"行["+map.get(gs)+"]保护价异常,");
//            	   continue;
//               }
//               ticketInfo.setSafePrice(BigDecimal.valueOf(Double.parseDouble(getValue(safePrice))));//商品保护价格  
//               
//               
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

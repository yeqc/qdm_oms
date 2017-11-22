package com.work.shop.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.stereotype.Service;

import au.com.bytecode.opencsv.CSVReader;

import com.work.shop.bean.ChannelGoods;
import com.work.shop.bean.TicketInfo;
import com.work.shop.service.ShopGoodsService;

@Service
public class TicketInfoSellingInfoReadExcelUtils {
	

	
	@Resource(name="shopGoodsService")
	private ShopGoodsService shopGoodsService; 
	
	public final static String REG_DIGIT = "[0-9]*"; //判断字符串是否全为数字

	public boolean isDigit(String str) {
		return str.matches(REG_DIGIT);
	}

	 /**
	   * 读取csv文件内容
	   * 
	   * @return List<TicketInfo>对象
	   * @throws IOException
	   *			 输入/输出(i/o)异常
	   */
	  public  List<TicketInfo> readCsv(InputStream is,String ticketCode,String shopCode, String channelCode, StringBuffer sb) throws IOException {
		//  InputStream is = new FileInputStream(file);//读取excel名称
//		HSSFWorkbook hssfWorkbook = new HSSFWorkbook(is);
		CSVReader reader = new CSVReader(new InputStreamReader(is, "GBK"));
		String[] nextLine;
		TicketInfo ticketInfo = null;
		List<TicketInfo> list = new ArrayList<TicketInfo>();
		Map<String,String> map = new HashMap<String,String>();
		int j = 0;
		int i = 1;
		while ((nextLine = reader.readNext()) != null) {
			if (j != 0) {
				ticketInfo = new TicketInfo();
				// 0商品款号 1渠道商品款号 2商品名称 3商品卖点信息
				String goodsSn = nextLine[0];
				if (StringUtil.isEmpty(goodsSn)) {
					continue;
				}
				String gs = "";
				if(goodsSn.indexOf(".")==-1){
					gs = goodsSn;
				}else{
					gs = goodsSn.substring(0, goodsSn.indexOf("."));
				}
				if(map.get(gs) != null){
					sb.append("第"+(i)+"行["+map.get(gs)+"]已存在,");
					continue;
				}
				map.put(gs, gs);
				ticketInfo.setGoodsSn(gs);//商品款号
				String sellingInfo =  nextLine[1];
				if (sellingInfo == null) {
					sb.append("第"+(i)+"行["+ gs +"]卖点信息异常,");
					continue;
				}
				// 天猫渠道卖点模块化信息校验 卖点最多允许5个短语，每个短语最多6个字，总字数不超过20个字(每个短语用英文逗号,隔开)
				if (Constants.TMALL_CHANNEL_CODE.equals(channelCode)) {
					sellingInfo = sellingInfo.replaceAll("，", ","); // 中文逗号转换成英文逗号
					String[] arr = sellingInfo.split(",");
					String sellPointTemp = sellingInfo.replaceAll(",", "");
					if (sellPointTemp.length() > 20) {
						sb.append("第"+(i)+"行["+ gs +"]卖点信息不符合规则【总字数不超过20个字】");
						continue;
					}
					if (StringUtil.isArrayNotNull(arr)) {
						if (arr.length > 5) {
							sb.append("第"+(i)+"行["+ gs +"]卖点信息不符合规则【卖点最多允许5个短语】");
							continue;
						}
						/*for (String str : arr){
							if (str.length() > 6) {
								sb.append("第"+(i)+"行["+ gs +"]卖点信息不符合规则【每个短语最多6个字】");
								continue;
							}
						}*/
					}
				}
				ticketInfo.setSellingInfo(sellingInfo);//商品卖点信息
				String urlWords = nextLine[2];
				if(urlWords != null && sellingInfo.indexOf(urlWords) < 0) {
					sb.append("第"+(i)+"行["+ gs +"]带标签的广告词异常,");
					continue;
				}
				ticketInfo.setUrlWords(urlWords);
				String url = nextLine[3];
				if(url != null){
					ticketInfo.setUrl(url);
				}
				ticketInfo.setTicketCode(ticketCode); //绑定的调整单编号
				List<ChannelGoods> listChannelGoods = shopGoodsService.selectChannelGoodsList(gs, shopCode);
				if(listChannelGoods !=null && listChannelGoods.size()>0){
					ticketInfo.setGoodsTitle(listChannelGoods.get(0).getGoodsTitle());
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
	 /**
   * 读取xls文件内容
   * 
   * @return List<TicketInfo>对象
   * @throws IOException
   *			 输入/输出(i/o)异常
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
			  // 0商品款号 1渠道商品款号 2商品名称 3商品卖点信息
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
			  
			  
			  HSSFCell sellingInfo = hssfRow.getCell((short) 1);
			  if (sellingInfo == null) {
				  sb.append("第"+(rowNum+1)+"行["+map.get(gs)+"]卖点信息异常,");
				  continue;
			  }
			  ticketInfo.setSellingInfo(getValue(sellingInfo));//商品卖点信息
				   
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
   *			Excel中的每一个格子
   * @return Excel中每一个格子中的值
   */
  @SuppressWarnings("static-access")
  private static String getValue(HSSFCell hssfCell) {
	  if (hssfCell.getCellType() == hssfCell.CELL_TYPE_BOOLEAN) {
		  // 返回布尔类型的值
		  return String.valueOf(hssfCell.getBooleanCellValue());
	  }else if (hssfCell.getCellType() == hssfCell.CELL_TYPE_NUMERIC) {
		  // 返回数值类型的值
		  return String.valueOf(hssfCell.getNumericCellValue());
	  } else{
		  // 返回字符串类型的值
		  return String.valueOf(hssfCell.getStringCellValue());
	  }
  }

}

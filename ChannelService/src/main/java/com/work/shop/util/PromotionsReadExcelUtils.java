package com.work.shop.util;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
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

import com.work.shop.bean.ChannelInfo;
import com.work.shop.bean.ChannelShop;
import com.work.shop.bean.GiftsGoodsList;
import com.work.shop.bean.GroupGoods;
import com.work.shop.bean.OmsChannelInfo;
import com.work.shop.bean.PromotionsLimitMoney;
import com.work.shop.bean.PromotionsLimitSn;
import com.work.shop.bean.PromotionsListGoods;
import com.work.shop.service.PromotionService;
import com.work.shop.vo.GroupGoodsVO;
import com.work.shop.vo.PromotionsLimitMoneyVO;

@Service("promotionsReadExcelUtils")
public class PromotionsReadExcelUtils {

	@Resource(name = "promotionServiceImpl")
	private PromotionService promotionService;
	
	public List<OmsChannelInfo> readOpenShopChannelShop(InputStream is) throws IOException  {
		HSSFWorkbook hssfWorkbook = new HSSFWorkbook(is);
		OmsChannelInfo bean = null;
		List<OmsChannelInfo> list = new ArrayList<OmsChannelInfo>();
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
				bean = new OmsChannelInfo();

				// 循环列Cell
				HSSFCell channelCode = hssfRow.getCell((short) 0);
				if (channelCode == null) {
					continue;
				}
				bean.setChannelCode(getValue(channelCode));

				HSSFCell channelName = hssfRow.getCell((short) 1);
				if (channelName == null) {
					continue;
				}
				bean.setChannelName(getValue(channelName));

				HSSFCell channelType = hssfRow.getCell((short) 2);
				if (channelType == null) {
					continue;
				}
				String str = getValue(channelType);
				str = str.replace("_CHANNEL_CODE", "");
				bean.setChannelType(str);

				HSSFCell isActive = hssfRow.getCell((short) 3);
				if (isActive == null) {
					continue;
				}
				bean.setIsActive(Double.valueOf(getValue(isActive)).byteValue());
				
				HSSFCell erpVdepotCode = hssfRow.getCell((short) 4);
				if (erpVdepotCode == null) {
					continue;
				}
				bean.setErpVdepotCode(getValue(erpVdepotCode));
				
				HSSFCell shopType = hssfRow.getCell((short) 5);
				if (shopType == null) {
					continue;
				}
				bean.setShopType(getValue(shopType));
				
				HSSFCell parentChannelCode = hssfRow.getCell((short) 7);
				if (parentChannelCode == null) {
					continue;
				}
				bean.setParentChannelCode(getValue(parentChannelCode));
				
				HSSFCell vdepotCode = hssfRow.getCell((short) 8);
				if (vdepotCode == null) {
					continue;
				}
				bean.setVdepotCode(getValue(vdepotCode));
				
				HSSFCell depotCode = hssfRow.getCell((short) 9);
				if (depotCode == null) {
					continue;
				}
				if(null!=getValue(depotCode)){
				bean.setDepotCode(getValue(depotCode));
				}
				
				HSSFCell address = hssfRow.getCell((short) 10);
				if (address == null) {
					continue;
				}
				bean.setAddress(getValue(address));
				
				HSSFCell loginId = hssfRow.getCell((short) 11);
				if (loginId == null) {
					continue;
				}
				bean.setLoginId(getValue(loginId));
				
				HSSFCell servicePhone = hssfRow.getCell((short) 12);
				if (servicePhone == null) {
					continue;
				}
				bean.setServicePhone(getValue(servicePhone));
				
				HSSFCell deliveryApi = hssfRow.getCell((short) 13);
				if (deliveryApi == null) {
					continue;
				}
				bean.setDeliveryApi(getValue(deliveryApi));
				
				HSSFCell defaultTransType = hssfRow.getCell((short) 14);
				if (defaultTransType == null) {
					continue;
				}
				bean.setDefaultTransType(Integer.valueOf(getValue(defaultTransType)));
				HSSFCell rank = hssfRow.getCell((short) 15);
				if (rank == null) {
					continue;
				}
				bean.setRank(Double.valueOf(getValue(rank)).intValue());
				
				HSSFCell tplId = hssfRow.getCell((short) 16);
				if (tplId == null) {
					continue;
				}
				bean.setTplId(Double.valueOf(getValue(tplId)).intValue());
				
				list.add(bean);
			}
		}
		return list;
	}
	
	
	public List<ChannelShop> readChannelShop(InputStream is) throws IOException  {
		HSSFWorkbook hssfWorkbook = new HSSFWorkbook(is);
		ChannelShop bean = null;
		List<ChannelShop> list = new ArrayList<ChannelShop>();
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
				bean = new ChannelShop();

				// 循环列Cell
				HSSFCell shopCode = hssfRow.getCell((short) 0);
				if (shopCode == null) {
					continue;
				}
				bean.setShopCode(getValue(shopCode));

				HSSFCell shopTitle = hssfRow.getCell((short) 1);
				if (shopTitle == null) {
					continue;
				}
				bean.setShopTitle(getValue(shopTitle));

				HSSFCell channelCode = hssfRow.getCell((short) 2);
				if (channelCode == null) {
					continue;
				}
				
				bean.setChannelCode(getValue(channelCode));

				HSSFCell shopStatus = hssfRow.getCell((short) 3);
				if (shopStatus == null) {
					continue;
				}
				bean.setShopStatus(Double.valueOf((getValue(shopStatus))).byteValue());
				
				HSSFCell erpShopCode = hssfRow.getCell((short) 4);
				if (erpShopCode == null) {
					continue;
				}
				bean.setErpShopCode(getValue(erpShopCode));
				
				HSSFCell shopChannel = hssfRow.getCell((short) 5);
				if (shopChannel == null) {
					continue;
				}
				bean.setShopChannel(getValue(shopChannel).equals("online")?(byte)1:(byte)0);
				
				
				HSSFCell shopType = hssfRow.getCell((short)6 );
				if (shopType == null) {
					continue;
				}
				bean.setShopType(getValue(shopType));
				
				HSSFCell parentShopCode = hssfRow.getCell((short)7 );
				if (parentShopCode == null) {
					continue;
				}
				bean.setParentShopCode(getValue(parentShopCode));
				
				
				HSSFCell shopAddress = hssfRow.getCell((short) 10);
				if (shopAddress == null) {
					continue;
				}
				bean.setShopAddress(getValue(shopAddress));
				
				HSSFCell shopLinkman = hssfRow.getCell((short) 11);
				if (shopLinkman == null) {
					continue;
				}
				bean.setShopLinkman(getValue(shopLinkman));
				
				HSSFCell shopTel = hssfRow.getCell((short) 12);
				if (shopTel == null) {
					continue;
				}
				bean.setShopTel(getValue(shopTel));
				
				list.add(bean);
			}
		}
		return list;
		
		
	}
	
	public List<ChannelInfo> readChannelInfo(InputStream is) throws IOException  {
		HSSFWorkbook hssfWorkbook = new HSSFWorkbook(is);
		ChannelInfo bean = null;
		List<ChannelInfo> list = new ArrayList<ChannelInfo>();
		// 循环工作表Sheet
		for (int numSheet = 0; numSheet < hssfWorkbook.getNumberOfSheets(); numSheet++) {
			HSSFSheet hssfSheet = hssfWorkbook.getSheetAt(numSheet);
			if (hssfSheet == null) {
				continue;
			}
			// 循环行Row
			loop:for (int rowNum = 1; rowNum <= hssfSheet.getLastRowNum(); rowNum++) {
				HSSFRow hssfRow = hssfSheet.getRow(rowNum);
				if (hssfRow == null) {
					continue;
				}
				bean = new ChannelInfo();


				HSSFCell chanelCode = hssfRow.getCell((short) 3);
				if (chanelCode == null) {
					continue;
				}
				bean.setChanelCode((getValue(chanelCode)));
				
				HSSFCell channelTitle = hssfRow.getCell((short) 4);
				if (channelTitle == null) {
					continue;
				}
				bean.setChannelTitle(getValue(channelTitle));
				
				
				HSSFCell channelType = hssfRow.getCell((short) 5);
				if (channelType == null) {
					bean.setChannelType((short) 0);
				}else
				if(getValue(channelType).equals("代理商")){
					bean.setChannelType((short) 4);
				}else
				if(getValue(channelType).equals("分公司")){
					bean.setChannelType((short) 3);
				}else{
					bean.setChannelType((short) 0);
				}
				bean.setChannelStatus((short)0);
				for(ChannelInfo channell:list){
					if(bean.getChanelCode().equals(channell.getChanelCode())){
						continue loop;
					}
				}
				
				list.add(bean);
			}
		}
		return list;
		
		
	}
	
	

	/**
	 * 满赠文件导入,读取xls文件内容
	 * 
	 * @return List<PromotionsLimitMoney>对象
	 * @throws IOException
	 *             输入/输出(i/o)异常
	 */
	public List<PromotionsLimitMoneyVO> readPromotionsLimitMoneyFile(InputStream is, StringBuffer message) throws IOException {
		// InputStream is = new FileInputStream(file);//读取excel名称
		HSSFWorkbook hssfWorkbook = new HSSFWorkbook(is);
		PromotionsLimitMoneyVO bean = null;
		List<PromotionsLimitMoneyVO> limitMoneys = new ArrayList<PromotionsLimitMoneyVO>();
		// 第一个sheet满赠活动赠品列表|第二个sheet满赠细则金额商品设置
		if (hssfWorkbook.getNumberOfSheets() < 1) {
			message.append("上传模板不符合规则,请检查更新后再上传！");
			return null;
		}
		// 循环工作表Sheet
		List<GiftsGoodsList> skuList = new ArrayList<GiftsGoodsList>();
		Map<String, PromotionsLimitMoney> skuMap = new HashMap<String, PromotionsLimitMoney>();
		for (int numSheet = 0; numSheet < 1; numSheet++) {
			HSSFSheet hssfSheet = hssfWorkbook.getSheetAt(numSheet);
			if (hssfSheet == null) {
				continue;
			}
			// 循环行Row
			String sheetName = hssfWorkbook.getSheetName(numSheet); // sheet Name
			for (int rowNum = 1; rowNum <= hssfSheet.getLastRowNum(); rowNum++) {
				HSSFRow hssfRow = hssfSheet.getRow(rowNum);
				if (hssfRow == null) {
					continue;
				}
				System.out.println("工作簿[" + sheetName + "] 行[" + (rowNum + 1) + "]");
				// 第一个sheet满赠细则金额商品设置
				bean = new PromotionsLimitMoneyVO();
				// 循环列Cell
				HSSFCell cell0 = hssfRow.getCell((short) 0); // 满赠金额
				HSSFCell cell1 = hssfRow.getCell((short) 1); // 每单赠送总数量
				HSSFCell cell2 = hssfRow.getCell((short) 2); // 赠送商品编码
				HSSFCell cell3 = hssfRow.getCell((short) 3); // 每款赠送数量
				HSSFCell cell4 = hssfRow.getCell((short) 4); // 是否随机赠
				if ( StringUtil.isEmpty(getValue(cell0)) 
						&& StringUtil.isEmpty(getValue(cell1)) 
						&& StringUtil.isEmpty(getValue(cell2))
						&& StringUtil.isEmpty(getValue(cell3))
						&& StringUtil.isEmpty(getValue(cell4))) {
					continue;
				}
				if (cell0 == null) {
					message.append("工作簿[" + sheetName + "] 行[" + (rowNum + 1) + "] 满赠金额为空!" );
					break;
				}
				if (cell2 == null) {
					message.append("工作簿[" + sheetName + "] 行[" + (rowNum + 1) + "] 赠送商品编码为空!" );
					break;
				}
				if (cell3 == null) {
					message.append("工作簿[" + sheetName + "] 行[" + (rowNum + 1) + "] 每款赠送数量为空!" );
					break;
				}
				if (cell4 == null) {
					message.append("工作簿[" + sheetName + "] 行[" + (rowNum + 1) + "] 是否随机赠为空!" );
					break;
				}
				int count = 0;
				String limitMoney = getValue(cell0);
				if (cell1 != null) {
					count = Double.valueOf(getValue(cell1)).intValue();
					if (count == 0) {
						message.append("工作簿[" + sheetName + "] 行[" + (rowNum + 1) + "] 每单赠送总数量不能为0!" );
						break;
					}
					PromotionsLimitMoney money = skuMap.get(limitMoney);
					if (money == null) {
						money = new PromotionsLimitMoney();
						money.setLimitMoney(new BigDecimal(getValue(cell0)));
						money.setGiftsGoodsCount(count);
						skuMap.put(limitMoney, money);
					} else {
						if (count != money.getGiftsGoodsCount()) {
							message.append("工作簿[" + sheetName + "] 行[" + (rowNum + 1) + "] 一个满赠活动设置了多个不同的【每单赠送总数量】!" );
							break;
						}
					}
				}
				bean.setLimitMoney(new BigDecimal(limitMoney));
				bean.setGiftsSkuSn(getValue(getValue(cell2)));
				bean.setGiftsGoodsCount(count);
				bean.setGiftsSkuCount(Double.valueOf(getValue(cell3)).intValue());
				bean.setGiftsGoodsCount(Double.valueOf(getValue(cell3)).intValue());
				bean.setGiftsPriority(Double.valueOf(getValue(cell4)).byteValue());
				limitMoneys.add(bean);
			}
			if (StringUtil.isNotEmpty(message.toString())) {
				break;
			}
		}
		// 将每单赠送总数量设置到每条记录中
		for (PromotionsLimitMoneyVO moneyVO : limitMoneys) {
			PromotionsLimitMoney money = skuMap.get(moneyVO.getLimitMoney().toString());
			if (money != null && money.getLimitMoney().equals(moneyVO.getLimitMoney())) {
				moneyVO.setGiftsGoodsCount(money.getGiftsGoodsCount());
			}
		}
		return limitMoneys;
	}

	/**
	 * 套装文件导入,读取xls文件内容
	 * 
	 * @return List<PromotionsLimitMoney>对象
	 * @throws IOException
	 *             输入/输出(i/o)异常
	 */
	public List<GroupGoodsVO> readGroupGoodsFile(InputStream is, StringBuffer message) throws IOException {
		// InputStream is = new FileInputStream(file);//读取excel名称
		HSSFWorkbook hssfWorkbook = new HSSFWorkbook(is);
		GroupGoodsVO bean = null;
		List<GroupGoodsVO> list = new ArrayList<GroupGoodsVO>();
		// 循环工作表Sheet
		for (int numSheet = 0; numSheet < hssfWorkbook.getNumberOfSheets(); numSheet++) {
			HSSFSheet hssfSheet = hssfWorkbook.getSheetAt(numSheet);
			if (hssfSheet == null) {
				continue;
			}

			String groupCodeBak = "";
			// 循环行Row
			for (int rowNum = 1; rowNum <= hssfSheet.getLastRowNum(); rowNum++) {
				HSSFRow hssfRow = hssfSheet.getRow(rowNum);
				if (hssfRow == null) {
					continue;
				}
				bean = new GroupGoodsVO();

				// 循环列Cell
				HSSFCell groupCode = hssfRow.getCell((short) 0);
				if (rowNum == 1 && groupCode == null) {
					throw new RuntimeException();
				}
				if (groupCode != null) {
					groupCodeBak = getValue(groupCode);
					GroupGoods groupGoods = promotionService.searchGroupGoodsByGroupCode(groupCodeBak);
					if (groupGoods != null) {
						if (StringUtil.isNotEmpty(message.toString())) {
							message.append(",").append(groupCodeBak);
						} else {
							message.append(groupCodeBak);
						}
					}
				}
				bean.setGroupCode(groupCodeBak);

				HSSFCell groupPrice = hssfRow.getCell((short) 1);
				if (groupPrice != null) {
					bean.setPrice(new BigDecimal(getValue(groupPrice)));
				}

				HSSFCell goodsSn = hssfRow.getCell((short) 2);
				if (goodsSn != null) {
					bean.setGoodsSn(getValue(getValue(goodsSn)));
				}

				HSSFCell groupCount = hssfRow.getCell((short) 3);
				if (groupCount != null) {
					bean.setGoodsCount(Double.valueOf(getValue(groupCount)).intValue());
				}

				HSSFCell goodsPrice = hssfRow.getCell((short) 4);
				if (goodsPrice != null) {
					bean.setGoodsPrice(new BigDecimal(getValue(goodsPrice)));
				}

				list.add(bean);
			}
		}
		return list;
	}

	/**
	 * 买赠文件导入,读取xls文件内容
	 * 
	 * @return List<PromotionsLimitMoney>对象
	 * @throws IOException
	 *             输入/输出(i/o)异常
	 */
	public List<PromotionsLimitSn> readPromotionsLimitSnFile(InputStream is, StringBuffer message) throws IOException {
		// InputStream is = new FileInputStream(file);//读取excel名称
		HSSFWorkbook hssfWorkbook = new HSSFWorkbook(is);
		PromotionsLimitSn bean = null;
		List<PromotionsLimitSn> list = new ArrayList<PromotionsLimitSn>();
		// 循环工作表Sheet
		// hssfWorkbook.getNumberOfSheets() 获取第一个工作簿
		for (int numSheet = 0; numSheet < 1; numSheet++) {
			HSSFSheet hssfSheet = hssfWorkbook.getSheetAt(numSheet);
			if (hssfSheet == null) {
				continue;
			}
			String sheetName = hssfWorkbook.getSheetName(numSheet); // sheet Name
			// 循环行Row
			for (int rowNum = 1; rowNum <= hssfSheet.getLastRowNum(); rowNum++) {
				HSSFRow hssfRow = hssfSheet.getRow(rowNum);
				if (hssfRow == null) {
					continue;
				}
				bean = new PromotionsLimitSn();
				// 循环列Cell
				HSSFCell cell0 = hssfRow.getCell((short) 0); // 需购买SKU
				HSSFCell cell1 = hssfRow.getCell((short) 1); // 需购买数量
				HSSFCell cell2 = hssfRow.getCell((short) 2); // 赠品商品编码
				HSSFCell cell3 = hssfRow.getCell((short) 3); // 赠送数量
				if ( StringUtil.isEmpty(getValue(cell0)) 
						&& StringUtil.isEmpty(getValue(cell1)) 
						&& StringUtil.isEmpty(getValue(cell2))
						&& StringUtil.isEmpty(getValue(cell3))) {
					continue;
				}
				if (cell0 == null) {
					message.append("工作簿[" + sheetName + "] 行[" + (rowNum + 1) + "] 需购买SKU为空!" );
					break;
				}
				if (cell1 == null) {
					message.append("工作簿[" + sheetName + "] 行[" + (rowNum + 1) + "] 需购买数量为空!" );
					break;
				}
				if (Double.valueOf(getValue(cell1)).intValue() <= 0) {
					message.append("工作簿[" + sheetName + "] 行[" + (rowNum + 1) + "] 需购买数量不能小于0!" );
					break;
				}
				if (cell2 == null) {
					message.append("工作簿[" + sheetName + "] 行[" + (rowNum + 1) + "] 赠品商品编码为空!" );
					break;
				}
				if (cell3 == null) {
					message.append("工作簿[" + sheetName + "] 行[" + (rowNum + 1) + "] 赠送数量为空!" );
					break;
				}
				if (Double.valueOf(getValue(cell3)).intValue() <= 0) {
					message.append("工作簿[" + sheetName + "] 行[" + (rowNum + 1) + "] 赠送数量不能小于0!" );
					break;
				}
				bean.setLimitGoodsSn(getValue(getValue(cell0)));
				bean.setLimitCount(Double.valueOf(getValue(cell1)).intValue());
				bean.setGiftsGoodsSn(getValue(getValue(cell2)));
				bean.setGiftsGoodsCount(Double.valueOf(getValue(cell3)).intValue());
				bean.setGiftsGoodsSum(0);
				list.add(bean);
			}
		}
		return list;
	}

	/**
	 * 集合赠(需购商品)文件导入,读取xls文件内容
	 * 
	 * @return List<PromotionsListGoods>对象
	 * @throws IOException
	 *             输入/输出(i/o)异常
	 */
	public List<PromotionsListGoods> readPromotionsListGoodsFile(InputStream is) throws IOException {
		// InputStream is = new FileInputStream(file);//读取excel名称
		HSSFWorkbook hssfWorkbook = new HSSFWorkbook(is);
		PromotionsListGoods bean = null;
		List<PromotionsListGoods> list = new ArrayList<PromotionsListGoods>();
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
				bean = new PromotionsListGoods();

				// 循环列Cell
				HSSFCell goodsSn = hssfRow.getCell((short) 0);
				if (goodsSn == null) {
					continue;
				}
				bean.setGoodsSn(getValue(getValue(goodsSn)));

				list.add(bean);
			}
		}
		return list;
	}

	/**
	 * 集合赠(赠品)文件导入,读取xls文件内容
	 * 
	 * @return List<PromotionsListGoods>对象
	 * @throws IOException
	 *             输入/输出(i/o)异常
	 */
	public List<GiftsGoodsList> readGiftsGoodsListFile(InputStream is, StringBuffer message) throws IOException {
		// InputStream is = new FileInputStream(file);//读取excel名称
		HSSFWorkbook hssfWorkbook = new HSSFWorkbook(is);
		GiftsGoodsList bean = null;
		List<GiftsGoodsList> skuList = new ArrayList<GiftsGoodsList>();
		Map<String, Object> skuMap = new HashMap<String, Object>();
		// 循环工作表Sheet
		for (int numSheet = 0; numSheet < 1; numSheet++) {
			HSSFSheet hssfSheet = hssfWorkbook.getSheetAt(numSheet);
			if (hssfSheet == null) {
				continue;
			}
			String sheetName = hssfWorkbook.getSheetName(numSheet); // sheet Name
			// 循环行Row
			for (int rowNum = 1; rowNum <= hssfSheet.getLastRowNum(); rowNum++) {
				HSSFRow hssfRow = hssfSheet.getRow(rowNum);
				if (hssfRow == null) {
					continue;
				}
//				bean = new GiftsGoodsList();
//
//				// 循环列Cell
//				HSSFCell goodsSn = hssfRow.getCell((short) 0);
//				if (goodsSn == null) {
//					continue;
//				}
//				bean.setGoodsSn(getValue(getValue(goodsSn)));
//
//				// 循环列Cell
//				HSSFCell giftsSum = hssfRow.getCell((short) 1);
//				if (giftsSum == null) {
//					continue;
//				}
//				bean.setGiftsSum(Double.valueOf(getValue(giftsSum)).intValue());
//
//				list.add(bean);
				String customCode = "";
				int giftsSum = 0;
				HSSFCell cell0 = hssfRow.getCell((short) 0); // 赠送商品编码
				HSSFCell cell1 = hssfRow.getCell((short) 1); // 赠送商品总数量
				if ( StringUtil.isEmpty(getValue(cell0)) && StringUtil.isEmpty(getValue(cell1)) ) {
					continue;
				} else {
					if (cell0 == null) {
						message.append("工作簿[" + sheetName + "] 行[" + (rowNum + 1) + "] 赠送商品编码为空!" );
						break;
					}
					if (cell1 == null) {
						message.append("工作簿[" + sheetName + "] 行[" + (rowNum + 1) + "] 赠送商品总数量为空!" );
						break;
					}
					customCode = getValue(getValue(cell0));
					if (StringUtil.isEmpty(customCode) && giftsSum != 0) {
						message.append("工作簿[" + sheetName + "] 行[" + (rowNum + 1) + "] 赠送商品编码为空!" );
						break ;
					}
					giftsSum = Double.valueOf(getValue(cell1)).intValue();
					if (StringUtil.isNotEmpty(customCode) && giftsSum == 0) {
						message.append("工作簿[" + sheetName + "] 行[" + (rowNum + 1) + "] 赠送商品总数量为0!" );
						break ;
					}
				}
				GiftsGoodsList giftsSku = new GiftsGoodsList();
				giftsSku.setGoodsSn(customCode);
				giftsSku.setGiftsSum(giftsSum);
				Object object = skuMap.get(customCode);
				if (null != object && StringUtil.isNotEmpty(object.toString())) {
					message.append("Sheet[" + sheetName + "] line[" + (rowNum + 1) + "] 赠送商品编码[" + object.toString() + "]重复!" );
					break ;
				} else {
					skuMap.put(customCode, customCode);
				}
				skuList.add(giftsSku);
			
			}
		}
		return skuList;
	}

	/**
	 * 得到Excel表中的值
	 * 
	 * @param hssfCell
	 *            Excel中的每一个格子
	 * @return Excel中每一个格子中的值
	 */
	@SuppressWarnings("static-access")
	private String getValue(HSSFCell hssfCell) {
		if (null == hssfCell) {
			return null;
		}
		if (hssfCell.getCellType() == hssfCell.CELL_TYPE_BOOLEAN) {
			// 返回布尔类型的值
			return String.valueOf(hssfCell.getBooleanCellValue());
		} else if (hssfCell.getCellType() == hssfCell.CELL_TYPE_NUMERIC) {
			// 返回数值类型的值
			BigDecimal decimal = new BigDecimal(hssfCell.getNumericCellValue());
			return decimal.toString();
		} else {
			// 返回字符串类型的值
			String value = String.valueOf(hssfCell.getStringCellValue());
			return value == null ? value : value.trim();
		}
	}

	private String getValue(String value) {
		if (StringUtil.isNotEmpty(value) && value.contains(".")) {
			return value.substring(0, value.indexOf("."));
		}
		return value;
	}

}

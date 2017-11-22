package com.work.shop.vo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.work.shop.bean.GiftsGoodsList;
import com.work.shop.bean.GroupGoodsList;
import com.work.shop.bean.PromotionsLimitMoney;
import com.work.shop.bean.PromotionsLimitMoneyGoods;
import com.work.shop.bean.PromotionsLimitSn;
import com.work.shop.bean.PromotionsListGoods;
import com.work.shop.bean.PromotionsListInfor;
import com.work.shop.util.StringUtil;

public class PromotionVO {

	private String id;
	private String promType;
	private String promStatus;
	private String promCode;
	private String shopCode;
	private String promTitle;
	private String beginTime;
	private String endTime;
	private String backup;

	private String addOrSave;

	private String groupCode;
	private BigDecimal price;

	private List<PromotionsLimitMoney> promotionsLimitMoneyList;
	
	private List<PromotionsLimitMoneyGoods> limitMoneyGoods;

	private List<GroupGoodsList> groupGoodsListList;

	private List<PromotionsLimitSn> promotionsLimitSnList;

	private PromotionsListInfor promotionsListInfor;
	
	private List<PromotionsListGoods> promotionsListGoodsList;
	
	private List<GiftsGoodsList> giftsGoodsListList;
	
	private String promotionsGoodsInfo;
	
	private String limitMoneyGoodsInfo;
	
	private String limitSnGoodsInfo;

	private String giftsGoodsInfo;
	
	public String getId() {
		return id;
	}

	public String getPromType() {
		return promType;
	}

	public void setPromType(String promType) {
		this.promType = promType;
	}

	public String getPromStatus() {
		return promStatus;
	}

	public void setPromStatus(String promStatus) {
		this.promStatus = promStatus;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPromCode() {
		return promCode;
	}

	public void setPromCode(String promCode) {
		this.promCode = promCode;
	}

	public String getShopCode() {
		return shopCode;
	}

	public void setShopCode(String shopCode) {
		this.shopCode = shopCode;
	}

	public String getPromTitle() {
		return promTitle;
	}

	public void setPromTitle(String promTitle) {
		this.promTitle = promTitle;
	}

	public String getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(String beginTime) {
		this.beginTime = beginTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getBackup() {
		return backup;
	}

	public void setBackup(String backup) {
		this.backup = backup;
	}

	public String getAddOrSave() {
		return addOrSave;
	}

	public void setAddOrSave(String addOrSave) {
		this.addOrSave = addOrSave;
	}

	public String getGroupCode() {
		return groupCode;
	}

	public void setGroupCode(String groupCode) {
		this.groupCode = groupCode;
	}

	public List<PromotionsLimitMoney> getPromotionsLimitMoneyList() {
		return promotionsLimitMoneyList;
	}

	public void setPromotionsLimitMoneyList(List<PromotionsLimitMoney> promotionsLimitMoneyList) {
		this.promotionsLimitMoneyList = promotionsLimitMoneyList;
	}

	public List<GroupGoodsList> getGroupGoodsListList() {
		return groupGoodsListList;
	}

	public void setGroupGoodsListList(List<GroupGoodsList> groupGoodsListList) {
		this.groupGoodsListList = groupGoodsListList;
	}

	public List<PromotionsLimitSn> getPromotionsLimitSnList() {
		return promotionsLimitSnList;
	}

	public void setPromotionsLimitSnList(List<PromotionsLimitSn> promotionsLimitSnList) {
		this.promotionsLimitSnList = promotionsLimitSnList;
	}

	public PromotionsListInfor getPromotionsListInfor() {
		return promotionsListInfor;
	}

	public void setPromotionsListInfor(PromotionsListInfor promotionsListInfor) {
		this.promotionsListInfor = promotionsListInfor;
	}

	public List<PromotionsListGoods> getPromotionsListGoodsList() {
		return promotionsListGoodsList;
	}

	public void setPromotionsListGoodsList(List<PromotionsListGoods> promotionsListGoodsList) {
		this.promotionsListGoodsList = promotionsListGoodsList;
	}

	public List<GiftsGoodsList> getGiftsGoodsListList() {
		return giftsGoodsListList;
	}

	public void setGiftsGoodsListList(List<GiftsGoodsList> giftsGoodsListList) {
		this.giftsGoodsListList = giftsGoodsListList;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public String getPromotionsGoodsInfo() {
		return promotionsGoodsInfo;
	}

	public void setPromotionsGoodsInfo(String promotionsGoodsInfo) {
		// sku:num;sku:num   123:1;234:2
		if (StringUtil.isNotEmpty(promotionsGoodsInfo)) {
			promotionsListGoodsList = new ArrayList<PromotionsListGoods>();
			String[] goodsArr = promotionsGoodsInfo.split(";");
			for (String str : goodsArr) {
				String[] goods = str.split(":");
				PromotionsListGoods goodsList = new PromotionsListGoods();
				if (StringUtil.isNotEmpty(goods[0])) {
					goodsList.setId(Integer.valueOf(goods[0]));
				}
				goodsList.setGoodsSn(goods[1]);
				goodsList.setPromCode(goods[2]);
				promotionsListGoodsList.add(goodsList);
			}
		}
		this.promotionsGoodsInfo = promotionsGoodsInfo;
	}

	public String getGiftsGoodsInfo() {
		return giftsGoodsInfo;
	}

	public void setGiftsGoodsInfo(String giftsGoodsInfo) {
		// id:sku:num;id:sku:num   1:123:1;2:234:2
		if (StringUtil.isNotEmpty(giftsGoodsInfo)) {
			giftsGoodsListList = new ArrayList<GiftsGoodsList>();
			String[] goodsArr = giftsGoodsInfo.split(";");
			for (String str : goodsArr) {
				String[] goods = str.split(":");
				GiftsGoodsList goodsList = new GiftsGoodsList();
				if (StringUtil.isNotEmpty(goods[0])) {
					goodsList.setId(Integer.valueOf(goods[0]));
				}
				goodsList.setGoodsSn(goods[1]);
				goodsList.setGiftsSum(Integer.valueOf(goods[2]));
				goodsList.setPromCode(goods[3]);
				giftsGoodsListList.add(goodsList);
			}
		}
		this.giftsGoodsInfo = giftsGoodsInfo;
	}

	public String getLimitMoneyGoodsInfo() {
		return limitMoneyGoodsInfo;
	}

	public void setLimitMoneyGoodsInfo(String limitMoneyGoodsInfo) {
		// sku:num;sku:num   123:1;234:2
		// id|promCode|promDetailsCode|limitMoney|giftsGoodsCount|giftsSkuSn|giftsSkuCount|giftsPriority
		if (StringUtil.isNotEmpty(limitMoneyGoodsInfo)) {
			promotionsLimitMoneyList = new ArrayList<PromotionsLimitMoney>();
			limitMoneyGoods = new ArrayList<PromotionsLimitMoneyGoods>();
			Map<String, PromotionsLimitMoney> map = new HashMap<String, PromotionsLimitMoney>();
			String[] goodsArr = limitMoneyGoodsInfo.split(";");
			for (String str : goodsArr) {
				String[] goods = str.split(":");
				String id = goods[0];
				String promCode = goods[1];
				String promDetailsCode = goods[2];
				String limitMoney = goods[3];
				Integer count = Integer.valueOf(goods[4]);
				String giftsSkuSn = goods[5];
				String giftsSkuCount = goods[6];
				PromotionsLimitMoneyGoods moneyGoods = new PromotionsLimitMoneyGoods();
				PromotionsLimitMoney comCount = map.get(limitMoney);
				if (comCount == null) {
					PromotionsLimitMoney money = new PromotionsLimitMoney();
					if (StringUtil.isNotEmpty(id)) {
						money.setId(Integer.valueOf(id));
					}
					money.setPromCode(promCode);
					money.setGiftsGoodsCount(count);
					if (StringUtil.isEmpty(promDetailsCode)) {
						promDetailsCode = "DT" + StringUtil.getSysCode();
					}
					money.setPromDetailsCode(promDetailsCode);
					money.setLimitMoney(new BigDecimal(limitMoney));
					promotionsLimitMoneyList.add(money);
					map.put(limitMoney, money);
					comCount = money;
				}
				moneyGoods.setPromCode(promCode);
				moneyGoods.setPromDetailsCode(comCount.getPromDetailsCode());
				moneyGoods.setGiftsSkuSn(giftsSkuSn);
				moneyGoods.setGiftsSkuCount(Integer.valueOf(giftsSkuCount));
				moneyGoods.setGiftsPriority(Byte.valueOf(goods[7]));
				limitMoneyGoods.add(moneyGoods);
			}
		}
		this.limitMoneyGoodsInfo = limitMoneyGoodsInfo;
	}

	public String getLimitSnGoodsInfo() {
		return limitSnGoodsInfo;
	}

	public void setLimitSnGoodsInfo(String limitSnGoodsInfo) {
		// sku:num;sku:num   123:1;234:2
		// id|promCode|limitGoodsSn|limitCount|giftsGoodsSn|giftsGoodsCount
		if (StringUtil.isNotEmpty(limitSnGoodsInfo)) {
			promotionsLimitSnList = new ArrayList<PromotionsLimitSn>();
			String[] goodsArr = limitSnGoodsInfo.split(";");
			for (String str : goodsArr) {
				String[] goods = str.split(":");
				String id = goods[0];
				String promCode = goods[1];
				String limitGoodsSn = goods[2];
				Integer limitCount = Integer.valueOf(goods[3]);
				String giftsGoodsSn = goods[4];
				Integer giftsGoodsCount = Integer.valueOf(goods[5]);
				PromotionsLimitSn limitSn = new PromotionsLimitSn();
				if (StringUtil.isNotEmpty(id)) {
					limitSn.setId(Integer.valueOf(id));
				}
				limitSn.setPromCode(promCode);
				limitSn.setLimitGoodsSn(limitGoodsSn);
				limitSn.setLimitCount(limitCount);
				limitSn.setGiftsGoodsSn(giftsGoodsSn);
				limitSn.setGiftsGoodsCount(giftsGoodsCount);
				limitSn.setGiftsGoodsSum(0);
				promotionsLimitSnList.add(limitSn);
			}
		}
		this.limitSnGoodsInfo = limitSnGoodsInfo;
	}

	public List<PromotionsLimitMoneyGoods> getLimitMoneyGoods() {
		return limitMoneyGoods;
	}

	public void setLimitMoneyGoods(List<PromotionsLimitMoneyGoods> limitMoneyGoods) {
		this.limitMoneyGoods = limitMoneyGoods;
	}
}

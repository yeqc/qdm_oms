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

public class PromotionEditVO {

	private List<PromotionsLimitMoneyVO> limitMoneyVOs;
	
	private String limitMoneyGoodsInfo;

	public String getLimitMoneyGoodsInfo() {
		return limitMoneyGoodsInfo;
	}

	public void setLimitMoneyGoodsInfo(String limitMoneyGoodsInfo) {
		// sku:num;sku:num   123:1;234:2
		// id|promCode|promDetailsCode|limitMoney|giftsGoodsCount|giftsSkuSn|giftsSkuCount|giftsPriority
		if (StringUtil.isNotEmpty(limitMoneyGoodsInfo)) {
			limitMoneyVOs = new ArrayList<PromotionsLimitMoneyVO>();
			Map<String, String> map = new HashMap<String, String>();
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
				PromotionsLimitMoneyVO moneyGoods = new PromotionsLimitMoneyVO();
				String promDetailsCodeTemp = map.get(limitMoney);
				if (StringUtil.isEmpty(promDetailsCodeTemp)) {
					PromotionsLimitMoneyVO money = new PromotionsLimitMoneyVO();
					if (StringUtil.isEmpty(promDetailsCode)) {
						promDetailsCode = "DT" + StringUtil.getSysCode();
					}
					moneyGoods.setPromDetailsCode(promDetailsCode);
					map.put(limitMoney, promDetailsCode);
				} else {
					promDetailsCode = promDetailsCodeTemp;
				}
				if (StringUtil.isNotEmpty(id)) {
					moneyGoods.setId(Integer.valueOf(id));
				}
				moneyGoods.setGiftsGoodsCount(count);
				moneyGoods.setPromCode(promCode);
				moneyGoods.setLimitMoney(new BigDecimal(limitMoney));
				moneyGoods.setPromCode(promCode);
				moneyGoods.setPromDetailsCode(promDetailsCode);
				moneyGoods.setGiftsSkuSn(giftsSkuSn);
				moneyGoods.setGiftsSkuCount(Integer.valueOf(giftsSkuCount));
				moneyGoods.setGiftsPriority(Byte.valueOf(goods[7]));
				limitMoneyVOs.add(moneyGoods);
			}
		}
		this.limitMoneyGoodsInfo = limitMoneyGoodsInfo;
	}

	public List<PromotionsLimitMoneyVO> getLimitMoneyVOs() {
		return limitMoneyVOs;
	}

	public void setLimitMoneyVOs(List<PromotionsLimitMoneyVO> limitMoneyVOs) {
		this.limitMoneyVOs = limitMoneyVOs;
	}
}

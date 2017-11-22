package com.work.shop.vo;

import java.math.BigDecimal;

import com.work.shop.bean.PromotionsLimitMoneyGoods;

public class PromotionsLimitMoneyVO extends PromotionsLimitMoneyGoods {
	
	private Integer id;
	
	private BigDecimal limitMoney;
	
	private Integer giftsGoodsCount;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public BigDecimal getLimitMoney() {
		return limitMoney;
	}

	public void setLimitMoney(BigDecimal limitMoney) {
		this.limitMoney = limitMoney;
	}

	public Integer getGiftsGoodsCount() {
		return giftsGoodsCount;
	}

	public void setGiftsGoodsCount(Integer giftsGoodsCount) {
		this.giftsGoodsCount = giftsGoodsCount;
	}
}

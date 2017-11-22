package com.work.shop.api.bean;

public class ItemUpdate {

	private String goodsSn;
	
	private String skuSn;
	
	private String itemFieldValue;
	
	private String shopCode;
	
	private String channelCode;
	
	private Integer stockCount;
	
	private Integer type = 1;// 1：全量更新，2：增量更新
	
	private Integer priceType = 0; // 0. 售价 1.吊牌价 2.两者都修改

	public String getGoodsSn() {
		return goodsSn;
	}

	public void setGoodsSn(String goodsSn) {
		this.goodsSn = goodsSn;
	}

	public String getItemFieldValue() {
		return itemFieldValue;
	}

	public void setItemFieldValue(String itemFieldValue) {
		this.itemFieldValue = itemFieldValue;
	}

	public String getShopCode() {
		return shopCode;
	}

	public void setShopCode(String shopCode) {
		this.shopCode = shopCode;
	}

	public String getSkuSn() {
		return skuSn;
	}

	public void setSkuSn(String skuSn) {
		this.skuSn = skuSn;
	}

	public Integer getStockCount() {
		return stockCount;
	}

	public void setStockCount(Integer stockCount) {
		this.stockCount = stockCount;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getChannelCode() {
		return channelCode;
	}

	public void setChannelCode(String channelCode) {
		this.channelCode = channelCode;
	}

	public Integer getPriceType() {
		return priceType;
	}

	public void setPriceType(Integer priceType) {
		this.priceType = priceType;
	}
}

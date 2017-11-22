package com.work.shop.api.bean;

import java.util.List;

public class ChannelApiGoods {

	private String shopCode;

	private String shopName;

	private String goodsSn;
	
	private String localGoodsSn;

	private String channelGoodsSn;

	private String goodsName;

	private String price;

	private String status;

	private String stockCount;
	
	private String colorName;
	
	private String sizeName;
	
	private String prodOrigin;

	private List<ChannelApiGoods> apiGoodsChild;

	public String getShopCode() {
		return shopCode;
	}

	public void setShopCode(String shopCode) {
		this.shopCode = shopCode;
	}

	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}

	public String getGoodsSn() {
		return goodsSn;
	}

	public void setGoodsSn(String goodsSn) {
		this.goodsSn = goodsSn;
	}

	public String getChannelGoodsSn() {
		return channelGoodsSn;
	}

	public void setChannelGoodsSn(String channelGoodsSn) {
		this.channelGoodsSn = channelGoodsSn;
	}

	public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getStockCount() {
		return stockCount;
	}

	public void setStockCount(String stockCount) {
		this.stockCount = stockCount;
	}

	public List<ChannelApiGoods> getApiGoodsChild() {
		return apiGoodsChild;
	}

	public void setApiGoodsChild(List<ChannelApiGoods> apiGoodsChild) {
		this.apiGoodsChild = apiGoodsChild;
	}

	public String getColorName() {
		return colorName;
	}

	public void setColorName(String colorName) {
		this.colorName = colorName;
	}

	public String getSizeName() {
		return sizeName;
	}

	public void setSizeName(String sizeName) {
		this.sizeName = sizeName;
	}

	public String getLocalGoodsSn() {
		return localGoodsSn;
	}

	public void setLocalGoodsSn(String localGoodsSn) {
		this.localGoodsSn = localGoodsSn;
	}

	public String getProdOrigin() {
		return prodOrigin;
	}

	public void setProdOrigin(String prodOrigin) {
		this.prodOrigin = prodOrigin;
	}
}

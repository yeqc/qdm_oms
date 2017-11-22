package com.work.shop.vo;

import java.math.BigDecimal;

public class ChannelGoodsInfoVo extends ChannelGoodsTicketVo {

	private String goodsSn;
	private String productGoodsSn;
	private String channelGoodsCode;
	private String goodsTitle;// 商品名称
	private String onSellStatus;// 上下架状态
	private BigDecimal safePrice;// 商品保护价格
	private BigDecimal newPrice;// 商品新价格
	private String sellingInfo;// 卖点信息
	private String execTime;
	private String showName;//商品展示标题//2017-7-4  天猫渠道  新增   商品展示标题
	
//	private String channelCode;
	
/*	public String getChannelCode() {
		return channelCode;
	}

	public void setChannelCode(String channelCode) {
		this.channelCode = channelCode;
	}*/

	public String getProductGoodsSn() {
		return productGoodsSn;
	}

	public void setProductGoodsSn(String productGoodsSn) {
		this.productGoodsSn = productGoodsSn;
	}

	public String getChannelGoodsCode() {
		return channelGoodsCode;
	}

	public void setChannelGoodsCode(String channelGoodsCode) {
		this.channelGoodsCode = channelGoodsCode;
	}

	public String getGoodsTitle() {
		return goodsTitle;
	}

	public void setGoodsTitle(String goodsTitle) {
		this.goodsTitle = goodsTitle;
	}

	public String getOnSellStatus() {
		return onSellStatus;
	}

	public void setOnSellStatus(String onSellStatus) {
		this.onSellStatus = onSellStatus;
	}

	public BigDecimal getSafePrice() {
		return safePrice;
	}

	public void setSafePrice(BigDecimal safePrice) {
		this.safePrice = safePrice;
	}

	public BigDecimal getNewPrice() {
		return newPrice;
	}

	public void setNewPrice(BigDecimal newPrice) {
		this.newPrice = newPrice;
	}

	public String getSellingInfo() {
		return sellingInfo;
	}

	public void setSellingInfo(String sellingInfo) {
		this.sellingInfo = sellingInfo;
	}

	public String getGoodsSn() {
		return goodsSn;
	}

	public void setGoodsSn(String goodsSn) {
		this.goodsSn = goodsSn;
	}

	public String getExecTime() {
		return execTime;
	}

	public void setExecTime(String execTime) {
		this.execTime = execTime;
	}

	public String getShowName() {
		return showName;
	}

	public void setShowName(String showName) {
		this.showName = showName;
	}
	

}

package com.work.shop.vo;

import java.util.Date;

public class TicketInfoVo {
	private int id;
	private String ticketCode;// 调整单编号
	private String channelCode; // 渠道编码
	private Byte ticketType = -1;// 单据类型,默认为-1,条件会排除-1
	private String shopCode;// 经营店铺名称
	private String goodsSn;// 商品款号
	private String channelGoodssn;// 渠道商品款号
	private String goodsTitle;// 商品名称
	private String onSellStatus;// 上下架状态
	private String safePrice;// 商品保护价格
	private String newPrice;// 商品新价格
	private String sellingInfo;// 卖点信息
	private String hasDiscount; // 支持会员打折
	private String freightPayer;// 运费承担方式
	private String isOffline; // 是否是线下商品
	private String isSyncStock; //是否需要同步库存 0不需要，1需要
	private String urlWords;  //带链接的广告词
	private String url; //广告词链接地址
	private Byte isOnlineOffline;  //线上线下款商品状态。1：线上款（默认值）；2：线下款；3：线上线下同款
	private String returnMessage;
	private Date requestTime;
	private String sku; // 商品11位码
	private String channelStock; // 商品渠道库存
	private String showName;//2017-7-4  天猫渠道  新增   商品展示标题

	public String getSafePrice() {
		return safePrice;
	}

	public void setSafePrice(String safePrice) {
		this.safePrice = safePrice;
	}

	public String getNewPrice() {
		return newPrice;
	}

	public void setNewPrice(String newPrice) {
		this.newPrice = newPrice;
	}

	public String getSellingInfo() {
		return sellingInfo;
	}

	public void setSellingInfo(String sellingInfo) {
		this.sellingInfo = sellingInfo;
	}

	public String getOnSellStatus() {
		return onSellStatus;
	}

	public void setOnSellStatus(String onSellStatus) {
		this.onSellStatus = onSellStatus;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTicketCode() {
		return ticketCode;
	}

	public void setTicketCode(String ticketCode) {
		this.ticketCode = ticketCode;
	}

	public String getChannelCode() {
		return channelCode;
	}

	public void setChannelCode(String channelCode) {
		this.channelCode = channelCode;
	}

	public Byte getTicketType() {
		return ticketType;
	}

	public void setTicketType(Byte ticketType) {
		this.ticketType = ticketType;
	}

	public String getShopCode() {
		return shopCode;
	}

	public void setShopCode(String shopCode) {
		this.shopCode = shopCode;
	}

	public String getGoodsSn() {
		return goodsSn;
	}

	public void setGoodsSn(String goodsSn) {
		this.goodsSn = goodsSn;
	}

	public String getChannelGoodssn() {
		return channelGoodssn;
	}

	public void setChannelGoodssn(String channelGoodssn) {
		this.channelGoodssn = channelGoodssn;
	}

	public String getGoodsTitle() {
		return goodsTitle;
	}

	public void setGoodsTitle(String goodsTitle) {
		this.goodsTitle = goodsTitle;
	}

	public String getHasDiscount() {
		return hasDiscount;
	}

	public void setHasDiscount(String hasDiscount) {
		this.hasDiscount = hasDiscount;
	}

	public String getFreightPayer() {
		return freightPayer;
	}

	public void setFreightPayer(String freightPayer) {
		this.freightPayer = freightPayer;
	}

	public String getIsOffline() {
		return isOffline;
	}

	public void setIsOffline(String isOffline) {
		this.isOffline = isOffline;
	}

	public String getReturnMessage() {
		return returnMessage;
	}

	public void setReturnMessage(String returnMessage) {
		this.returnMessage = returnMessage;
	}

	public Date getRequestTime() {
		return requestTime;
	}

	public void setRequestTime(Date requestTime) {
		this.requestTime = requestTime;
	}

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	public String getChannelStock() {
		return channelStock;
	}

	public void setChannelStock(String channelStock) {
		this.channelStock = channelStock;
	}

	public String getIsSyncStock() {
		return isSyncStock;
	}

	public void setIsSyncStock(String isSyncStock) {
		this.isSyncStock = isSyncStock;
	}

	public String getUrlWords() {
		return urlWords;
	}

	public void setUrlWords(String urlWords) {
		this.urlWords = urlWords;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Byte getIsOnlineOffline() {
		return isOnlineOffline;
	}

	public void setIsOnlineOffline(Byte isOnlineOffline) {
		this.isOnlineOffline = isOnlineOffline;
	}

	public String getShowName() {
		return showName;
	}

	public void setShowName(String showName) {
		this.showName = showName;
	}
	
	
	
	

	
}

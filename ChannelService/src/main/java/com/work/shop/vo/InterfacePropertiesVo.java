package com.work.shop.vo;

import com.work.shop.bean.InterfaceProperties;

public class InterfacePropertiesVo extends InterfaceProperties {

	//店铺名称
	private String shopTitle;
	//渠道名称
	private String channelTitle;
	
	public String getShopTitle() {
		return shopTitle;
	}
	public void setShopTitle(String shopTitle) {
		this.shopTitle = shopTitle;
	}
	public String getChannelTitle() {
		return channelTitle;
	}
	public void setChannelTitle(String channelTitle) {
		this.channelTitle = channelTitle;
	}
	
}

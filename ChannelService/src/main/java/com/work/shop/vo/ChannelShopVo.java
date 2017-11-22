package com.work.shop.vo;

import com.work.shop.bean.ChannelShop;
import com.work.shop.util.TimeUtil;

public class ChannelShopVo extends ChannelShop {
	
	private String channelTitle; //渠道名称

	public String getChannelTitle() {
		return channelTitle;
	}

	public void setChannelTitle(String channelTitle) {
		this.channelTitle = channelTitle;
	}
	
	public String getFormatExpiresTime() {
		String time = "";
		if (getExpiresTime() != null) {
			time = TimeUtil.formatDate(getExpiresTime());
		}
		return time;
	}

}

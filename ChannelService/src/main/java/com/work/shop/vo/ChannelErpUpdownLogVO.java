package com.work.shop.vo;

import java.util.Date;

import com.work.shop.bean.ChannelErpUpdownLog;
import com.work.shop.util.TimeUtil;

public class ChannelErpUpdownLogVO extends ChannelErpUpdownLog {
	private String channelTitle;
	private String shopTitle;
	
	private String beginTime;
	private String endTime;
	
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

	public String getChannelTitle() {
		return channelTitle;
	}

	public void setChannelTitle(String channelTitle) {
		this.channelTitle = channelTitle;
	}

	public String getShopTitle() {
		return shopTitle;
	}

	public void setShopTitle(String shopTitle) {
		this.shopTitle = shopTitle;
	}
	
	public String getFormatRequestTime() {
		String requestTime = "";
		Date date = getRequestTime();
		if (null != date) {
			requestTime = TimeUtil.formatDate(date);
		}
		return requestTime;
	}
}

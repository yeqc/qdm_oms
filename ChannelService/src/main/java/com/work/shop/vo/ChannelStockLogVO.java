package com.work.shop.vo;

import java.util.Date;

import com.work.shop.bean.ChannelStockLog;
import com.work.shop.util.Constants;
import com.work.shop.util.TimeUtil;

public class ChannelStockLogVO extends ChannelStockLog{
	
	//用于查询结果集
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

		private Integer start = 0;
		private Integer limit = Constants.EVERY_PAGE_SIZE;

		public Integer getStart() {
			return start;
		}

		public void setStart(Integer start) {
			this.start = start;
		}

		public Integer getLimit() {
			return limit;
		}

		public void setLimit(Integer limit) {
			this.limit = limit;
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

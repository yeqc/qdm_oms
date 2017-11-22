package com.work.shop.service;

public class ProductParam {

	private String BeginTime;
	
	private String EndTime;
	
	private String pageIndex;
	
	private String ProdOrigin;

	public String getBeginTime() {
		return BeginTime;
	}

	public void setBeginTime(String beginTime) {
		BeginTime = beginTime;
	}

	public String getEndTime() {
		return EndTime;
	}

	public void setEndTime(String endTime) {
		EndTime = endTime;
	}

	public String getPageIndex() {
		return pageIndex;
	}

	public void setPageIndex(String pageIndex) {
		this.pageIndex = pageIndex;
	}

	public String getProdOrigin() {
		return ProdOrigin;
	}

	public void setProdOrigin(String prodOrigin) {
		ProdOrigin = prodOrigin;
	}
	
	
}

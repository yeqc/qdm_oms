package com.work.shop.api.bean;

public class ItemQuery {

	private String BeginTime;
	
	private String EndTime;
	
	private Integer pageIndex;
	
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

	public Integer getPageIndex() {
		return pageIndex;
	}

	public void setPageIndex(Integer pageIndex) {
		this.pageIndex = pageIndex;
	}

	public String getProdOrigin() {
		return ProdOrigin;
	}

	public void setProdOrigin(String prodOrigin) {
		ProdOrigin = prodOrigin;
	}
}

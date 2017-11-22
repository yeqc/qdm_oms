package com.work.shop.service;

public class OrderParam {

	private String OrderTimeStart;
	private String OrderTimeEnd;
	private String HaveOrderDetail;
	private String PageIndex;
	private String OrderStatus;
	private String IsGetDiscountAmountDtl;
	

	public String getOrderTimeStart() {
		return OrderTimeStart;
	}
	public void setOrderTimeStart(String orderTimeStart) {
		OrderTimeStart = orderTimeStart;
	}
	public String getOrderTimeEnd() {
		return OrderTimeEnd;
	}
	public void setOrderTimeEnd(String orderTimeEnd) {
		OrderTimeEnd = orderTimeEnd;
	}
	public String getHaveOrderDetail() {
		return HaveOrderDetail;
	}
	public void setHaveOrderDetail(String haveOrderDetail) {
		HaveOrderDetail = haveOrderDetail;
	}
	public String getPageIndex() {
		return PageIndex;
	}
	public void setPageIndex(String pageIndex) {
		PageIndex = pageIndex;
	}
	public String getOrderStatus() {
		return OrderStatus;
	}
	public void setOrderStatus(String orderStatus) {
		OrderStatus = orderStatus;
	}
	public String getIsGetDiscountAmountDtl() {
		return IsGetDiscountAmountDtl;
	}
	public void setIsGetDiscountAmountDtl(String isGetDiscountAmountDtl) {
		IsGetDiscountAmountDtl = isGetDiscountAmountDtl;
	}
}

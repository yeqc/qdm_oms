package com.work.shop.api.beibei.vo.item.itemGet;


public class BBResponseDetailInfo {
	private boolean success;
	private String message;
	private BBItemInfo data;
	public boolean isSuccess() {
		return success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public BBItemInfo getData() {
		return data;
	}
	public void setData(BBItemInfo data) {
		this.data = data;
	}
	
}

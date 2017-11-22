package com.work.shop.api.beibei.vo.item.itemGet;

import java.util.List;


public class BBResponseInfo {
	private boolean success;
	private String message;
	private List<BBItemInfo> data;
	private Integer count;
	private Integer page_no;
	private Integer page_size;
	
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

	public List<BBItemInfo> getData() {
		return data;
	}
	public void setData(List<BBItemInfo> data) {
		this.data = data;
	}
	public Integer getCount() {
		return count;
	}
	public void setCount(Integer count) {
		this.count = count;
	}
	public Integer getPage_no() {
		return page_no;
	}
	public void setPage_no(Integer page_no) {
		this.page_no = page_no;
	}
	public Integer getPage_size() {
		return page_size;
	}
	public void setPage_size(Integer page_size) {
		this.page_size = page_size;
	}
	
}

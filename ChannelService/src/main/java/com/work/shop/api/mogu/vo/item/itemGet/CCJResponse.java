package com.work.shop.api.mogu.vo.item.itemGet;

import java.util.List;

public class CCJResponse {

	private Integer code;
	private List<CCJItem> info;
	private String total_num;
	public Integer getCode() {
		return code;
	}
	public void setCode(Integer code) {
		this.code = code;
	}
	public List<CCJItem> getInfo() {
		return info;
	}
	public void setInfo(List<CCJItem> info) {
		this.info = info;
	}
	public String getTotal_num() {
		return total_num;
	}
	public void setTotal_num(String total_num) {
		this.total_num = total_num;
	}
	
	
	
}

package com.work.shop.api.mogu.vo.item.itemGet;

import java.util.List;

public class MLSItemList {

	private Long total_num;
	private List<MLSResponseInfo> info;
	public Long getTotal_num() {
		return total_num;
	}
	public void setTotal_num(Long total_num) {
		this.total_num = total_num;
	}
	public List<MLSResponseInfo> getInfo() {
		return info;
	}
	public void setInfo(List<MLSResponseInfo> info) {
		this.info = info;
	}
	
	
}

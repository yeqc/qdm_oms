package com.work.shop.api.mls.vo.item.itemGet;

import java.util.List;

public class GoodsDetail {

	private String key;
	private String title;
	private String description;
	private List<ItemDetailData> data;
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public List<ItemDetailData> getData() {
		return data;
	}
	public void setData(List<ItemDetailData> data) {
		this.data = data;
	}
	
	
}

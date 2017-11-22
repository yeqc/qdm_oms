package com.work.shop.api.mogu.vo.item.itemGet;

public class MoGuResponseInfo {
	private int list_total;
	private int page_no;
	private int page_size;
	private MoGoItemInfo[] list_items;
	public int getList_total() {
		return list_total;
	}
	public void setList_total(int list_total) {
		this.list_total = list_total;
	}
	public MoGoItemInfo[] getList_items() {
		return list_items;
	}
	public void setList_items(MoGoItemInfo[] list_items) {
		this.list_items = list_items;
	}
	public int getPage_no() {
		return page_no;
	}
	public void setPage_no(int page_no) {
		this.page_no = page_no;
	}
	public int getPage_size() {
		return page_size;
	}
	public void setPage_size(int page_size) {
		this.page_size = page_size;
	}
	
}

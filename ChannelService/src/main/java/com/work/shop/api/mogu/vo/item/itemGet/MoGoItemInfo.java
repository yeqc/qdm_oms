package com.work.shop.api.mogu.vo.item.itemGet;

import java.util.List;


public class MoGoItemInfo {
	private String item_id;
	private String item_name;
	private String item_code;
	private String item_price;
	private String item_stock;
	private String item_status;
	private String item_isShelf;
	private String item_img;
	private String item_link;
	private List<ItemSku> item_skus;
	public String getItem_id() {
		return item_id;
	}
	public void setItem_id(String item_id) {
		this.item_id = item_id;
	}
	public String getItem_name() {
		return item_name;
	}
	public void setItem_name(String item_name) {
		this.item_name = item_name;
	}
	public String getItem_code() {
		return item_code;
	}
	public void setItem_code(String item_code) {
		this.item_code = item_code;
	}
	public String getItem_price() {
		return item_price;
	}
	public void setItem_price(String item_price) {
		this.item_price = item_price;
	}
	public String getItem_stock() {
		return item_stock;
	}
	public void setItem_stock(String item_stock) {
		this.item_stock = item_stock;
	}
	public String getItem_status() {
		return item_status;
	}
	public void setItem_status(String item_status) {
		this.item_status = item_status;
	}
	public String getItem_isShelf() {
		return item_isShelf;
	}
	public void setItem_isShelf(String item_isShelf) {
		this.item_isShelf = item_isShelf;
	}
	public String getItem_img() {
		return item_img;
	}
	public void setItem_img(String item_img) {
		this.item_img = item_img;
	}
	public String getItem_link() {
		return item_link;
	}
	public void setItem_link(String item_link) {
		this.item_link = item_link;
	}
	public List<ItemSku> getItem_skus() {
		return item_skus;
	}
	public void setItem_skus(List<ItemSku> item_skus) {
		this.item_skus = item_skus;
	}
	
}

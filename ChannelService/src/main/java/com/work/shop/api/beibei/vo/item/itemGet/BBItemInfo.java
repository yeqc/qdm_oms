package com.work.shop.api.beibei.vo.item.itemGet;

import java.util.List;

public class BBItemInfo {
	private String iid;
	private String mid;
	private String goods_num;
	private String title;
	private String img;
	private String price;
	private String forex_price;
	private String origin_price;
	private String forex_origin_price;
	private List<BBSku> sku;
	private List<BBSku> skus;
	public String getIid() {
		return iid;
	}
	public void setIid(String iid) {
		this.iid = iid;
	}
	public String getMid() {
		return mid;
	}
	public void setMid(String mid) {
		this.mid = mid;
	}
	public String getGoods_num() {
		return goods_num;
	}
	public void setGoods_num(String goods_num) {
		this.goods_num = goods_num;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getImg() {
		return img;
	}
	public void setImg(String img) {
		this.img = img;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getOrigin_price() {
		return origin_price;
	}
	public void setOrigin_price(String origin_price) {
		this.origin_price = origin_price;
	}
	public List<BBSku> getSku() {
		return sku;
	}
	public void setSku(List<BBSku> sku) {
		this.sku = sku;
	}
	public String getForex_price() {
		return forex_price;
	}
	public void setForex_price(String forex_price) {
		this.forex_price = forex_price;
	}
	public String getForex_origin_price() {
		return forex_origin_price;
	}
	public void setForex_origin_price(String forex_origin_price) {
		this.forex_origin_price = forex_origin_price;
	}
	public List<BBSku> getSkus() {
		return skus;
	}
	public void setSkus(List<BBSku> skus) {
		this.skus = skus;
	}
	
	
}

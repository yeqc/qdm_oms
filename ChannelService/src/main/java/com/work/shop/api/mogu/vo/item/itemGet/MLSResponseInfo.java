package com.work.shop.api.mogu.vo.item.itemGet;

import java.util.List;

public class MLSResponseInfo {

	private Long twitter_id;
	private Long goods_id;
	private String goods_title;
	private String goods_img;
	private Long goods_no;
	private Double goods_price;
	private Byte goods_status;
	private Long sale_num;
	private String goods_first_catalog;
	private String goods_catalog;
	private String stocked_type;
	private List<MLSShopCategory> shop_category;
	private List<MLSGoodsStocks> stocks;
	public Long getTwitter_id() {
		return twitter_id;
	}
	public void setTwitter_id(Long twitter_id) {
		this.twitter_id = twitter_id;
	}
	public Long getGoods_id() {
		return goods_id;
	}
	public void setGoods_id(Long goods_id) {
		this.goods_id = goods_id;
	}
	public String getGoods_title() {
		return goods_title;
	}
	public void setGoods_title(String goods_title) {
		this.goods_title = goods_title;
	}
	public String getGoods_img() {
		return goods_img;
	}
	public void setGoods_img(String goods_img) {
		this.goods_img = goods_img;
	}
	public Long getGoods_no() {
		return goods_no;
	}
	public void setGoods_no(Long goods_no) {
		this.goods_no = goods_no;
	}
	public Double getGoods_price() {
		return goods_price;
	}
	public void setGoods_price(Double goods_price) {
		this.goods_price = goods_price;
	}
	public Byte getGoods_status() {
		return goods_status;
	}
	public void setGoods_status(Byte goods_status) {
		this.goods_status = goods_status;
	}
	public Long getSale_num() {
		return sale_num;
	}
	public void setSale_num(Long sale_num) {
		this.sale_num = sale_num;
	}
	public String getGoods_first_catalog() {
		return goods_first_catalog;
	}
	public void setGoods_first_catalog(String goods_first_catalog) {
		this.goods_first_catalog = goods_first_catalog;
	}
	public String getGoods_catalog() {
		return goods_catalog;
	}
	public void setGoods_catalog(String goods_catalog) {
		this.goods_catalog = goods_catalog;
	}
	public List<MLSShopCategory> getShop_category() {
		return shop_category;
	}
	public void setShop_category(List<MLSShopCategory> shop_category) {
		this.shop_category = shop_category;
	}
	public List<MLSGoodsStocks> getStocks() {
		return stocks;
	}
	public void setStocks(List<MLSGoodsStocks> stocks) {
		this.stocks = stocks;
	}
	public String getStocked_type() {
		return stocked_type;
	}
	public void setStocked_type(String stocked_type) {
		this.stocked_type = stocked_type;
	}
	
	
}

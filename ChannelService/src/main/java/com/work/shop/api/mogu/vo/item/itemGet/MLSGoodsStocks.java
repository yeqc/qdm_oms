package com.work.shop.api.mogu.vo.item.itemGet;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MLSGoodsStocks {

	private Long sku_id;
	private Long repertory;
	private String goods_code;
	private String color;
	private String size;
	private Double price;
	@JsonProperty(value="1st")
	private String _1st;
	@JsonProperty(value="2rd")
	private String _2rd;
	public Long getSku_id() {
		return sku_id;
	}
	public void setSku_id(Long sku_id) {
		this.sku_id = sku_id;
	}
	public Long getRepertory() {
		return repertory;
	}
	public void setRepertory(Long repertory) {
		this.repertory = repertory;
	}
	public String getGoods_code() {
		return goods_code;
	}
	public void setGoods_code(String goods_code) {
		this.goods_code = goods_code;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	public String getSize() {
		return size;
	}
	public void setSize(String size) {
		this.size = size;
	}
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
	}
	public String get_1st() {
		return _1st;
	}
	public void set_1st(String _1st) {
		this._1st = _1st;
	}
	public String get_2rd() {
		return _2rd;
	}
	public void set_2rd(String _2rd) {
		this._2rd = _2rd;
	}
	
	
	
}

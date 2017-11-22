package com.work.shop.api.mogu.vo.item.itemGet;

import java.util.List;

public class CCJItem {

	private String create_time ; 
	private String good_url ; 
	private String goods_code ;
	private String goods_id ;
	private String goods_img ;
	private String goods_status ;
	private String goods_title ;
	private Integer sale_num ;
	private List<CCJSku> sku ;
	private String version ; 
	
	public String getCreate_time() {
		return create_time;
	}
	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}
	public String getGood_url() {
		return good_url;
	}
	public void setGood_url(String good_url) {
		this.good_url = good_url;
	}
	public String getGoods_code() {
		return goods_code;
	}
	public void setGoods_code(String goods_code) {
		this.goods_code = goods_code;
	}
	public String getGoods_id() {
		return goods_id;
	}
	public void setGoods_id(String goods_id) {
		this.goods_id = goods_id;
	}
	public String getGoods_img() {
		return goods_img;
	}
	public void setGoods_img(String goods_img) {
		this.goods_img = goods_img;
	}
	public String getGoods_status() {
		return goods_status;
	}
	public void setGoods_status(String goods_status) {
		this.goods_status = goods_status;
	}
	public String getGoods_title() {
		return goods_title;
	}
	public void setGoods_title(String goods_title) {
		this.goods_title = goods_title;
	}
	public Integer getSale_num() {
		return sale_num;
	}
	public void setSale_num(Integer sale_num) {
		this.sale_num = sale_num;
	}
	public List<CCJSku> getSku() {
		return sku;
	}
	public void setSku(List<CCJSku> sku) {
		this.sku = sku;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	
	
	
}

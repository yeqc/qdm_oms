package com.work.shop.api.pdd.vo.item.itemGet;

import java.util.List;

public class PddGoods {

	private Long goods_id;
	private String goods_name;
	private String image_url;
	private Integer is_more_sku;
	private Long goods_quantity;
	private Integer is_onsale;
	private List<PddSku> sku_list;
	public Long getGoods_id() {
		return goods_id;
	}
	public void setGoods_id(Long goods_id) {
		this.goods_id = goods_id;
	}
	public String getGoods_name() {
		return goods_name;
	}
	public void setGoods_name(String goods_name) {
		this.goods_name = goods_name;
	}
	public String getImage_url() {
		return image_url;
	}
	public void setImage_url(String image_url) {
		this.image_url = image_url;
	}
	public Integer getIs_more_sku() {
		return is_more_sku;
	}
	public void setIs_more_sku(Integer is_more_sku) {
		this.is_more_sku = is_more_sku;
	}
	public Long getGoods_quantity() {
		return goods_quantity;
	}
	public void setGoods_quantity(Long goods_quantity) {
		this.goods_quantity = goods_quantity;
	}
	public Integer getIs_onsale() {
		return is_onsale;
	}
	public void setIs_onsale(Integer is_onsale) {
		this.is_onsale = is_onsale;
	}
	public List<PddSku> getSku_list() {
		return sku_list;
	}
	public void setSku_list(List<PddSku> sku_list) {
		this.sku_list = sku_list;
	}
	
	
	
	
}

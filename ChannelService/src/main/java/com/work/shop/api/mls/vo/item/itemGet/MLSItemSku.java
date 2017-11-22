package com.work.shop.api.mls.vo.item.itemGet;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MLSItemSku {

	private String sku_id;
	private String color_meta_id;
	private String size_meta_id;
	private Double price;
	private Long repertory;
	private String sku_no;
	private Integer status;
	private String etime;
	private String ctime;
	private String color_name;
	private String color;
	private String size_name;
	private String size;
	public String getSku_id() {
		return sku_id;
	}
	public void setSku_id(String sku_id) {
		this.sku_id = sku_id;
	}
	public String getColor_meta_id() {
		return color_meta_id;
	}
	public void setColor_meta_id(String color_meta_id) {
		this.color_meta_id = color_meta_id;
	}
	public String getSize_meta_id() {
		return size_meta_id;
	}
	public void setSize_meta_id(String size_meta_id) {
		this.size_meta_id = size_meta_id;
	}
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
	}
	public Long getRepertory() {
		return repertory;
	}
	public void setRepertory(Long repertory) {
		this.repertory = repertory;
	}
	public String getSku_no() {
		return sku_no;
	}
	public void setSku_no(String sku_no) {
		this.sku_no = sku_no;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getEtime() {
		return etime;
	}
	public void setEtime(String etime) {
		this.etime = etime;
	}
	public String getCtime() {
		return ctime;
	}
	public void setCtime(String ctime) {
		this.ctime = ctime;
	}
	public String getColor_name() {
		return color_name;
	}
	public void setColor_name(String color_name) {
		this.color_name = color_name;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	public String getSize_name() {
		return size_name;
	}
	public void setSize_name(String size_name) {
		this.size_name = size_name;
	}
	public String getSize() {
		return size;
	}
	public void setSize(String size) {
		this.size = size;
	}
	
	
}

package com.work.shop.api.bean;

import com.alibaba.fastjson.annotation.JSONField;

public class ProdItemSku {
	
	@JSONField(name="BarCode")
	private String barCode;
	
	@JSONField(name="Stocks")
	private String stocks;
	
	@JSONField(name="ItemNo")
	private String itemNo;
	
	@JSONField(name="ItemName")
	private String itemName;
	
	@JSONField(name="CategoryName")
	private String categoryName;
	
	@JSONField(name="CreateDate")
	private String createDate;
	
	@JSONField(name="LastModifiedDate")
	private String lastModifiedDate;
	
	@JSONField(name="RetailPrice")
	private String retailPrice;
	
	@JSONField(name="SalePrice")
	private String salePrice;

	public String getBarCode() {
		return barCode;
	}

	public void setBarCode(String barCode) {
		this.barCode = barCode;
	}

	public String getStocks() {
		return stocks;
	}

	public void setStocks(String stocks) {
		this.stocks = stocks;
	}

	public String getItemNo() {
		return itemNo;
	}

	public void setItemNo(String itemNo) {
		this.itemNo = itemNo;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public String getLastModifiedDate() {
		return lastModifiedDate;
	}

	public void setLastModifiedDate(String lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	public String getRetailPrice() {
		return retailPrice;
	}

	public void setRetailPrice(String retailPrice) {
		this.retailPrice = retailPrice;
	}

	public String getSalePrice() {
		return salePrice;
	}

	public void setSalePrice(String salePrice) {
		this.salePrice = salePrice;
	}
}

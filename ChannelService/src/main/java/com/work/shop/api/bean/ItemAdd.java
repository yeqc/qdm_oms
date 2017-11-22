package com.work.shop.api.bean;

import java.util.List;

public class ItemAdd {

	private String ShopCode;
	
	private String ItemNo;
	
	private Boolean IsOnSale;
	
	private Double RetailPrice;
	
	private Double SalePrice;
	
	private List<ItemAdddetail> detailItems;

	public String getShopCode() {
		return ShopCode;
	}

	public void setShopCode(String shopCode) {
		ShopCode = shopCode;
	}

	public String getItemNo() {
		return ItemNo;
	}

	public void setItemNo(String itemNo) {
		ItemNo = itemNo;
	}

	public Boolean isIsOnSale() {
		return IsOnSale;
	}

	public void setIsOnSale(Boolean isOnSale) {
		IsOnSale = isOnSale;
	}

	public Double getRetailPrice() {
		return RetailPrice;
	}

	public void setRetailPrice(Double retailPrice) {
		RetailPrice = retailPrice;
	}

	public Double getSalePrice() {
		return SalePrice;
	}

	public void setSalePrice(Double salePrice) {
		SalePrice = salePrice;
	}

	public List<ItemAdddetail> getDetailItems() {
		return detailItems;
	}

	public void setDetailItems(List<ItemAdddetail> detailItems) {
		this.detailItems = detailItems;
	}
}

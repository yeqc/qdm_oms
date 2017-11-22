package com.work.shop.api.bean;

public class ItemAdddetail {

	private String BarCode;
	
	private Integer Stocks;
	
	private Double RetailPrice;
	
	private Double SalePrice;

	public String getBarCode() {
		return BarCode;
	}

	public void setBarCode(String barCode) {
		BarCode = barCode;
	}

	public Integer getStocks() {
		return Stocks;
	}

	public void setStocks(Integer stocks) {
		Stocks = stocks;
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
}
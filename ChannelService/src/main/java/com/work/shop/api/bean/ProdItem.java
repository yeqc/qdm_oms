package com.work.shop.api.bean;

import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;

public class ProdItem {

	@JSONField(name="ItemName")
	private String itemName;
	@JSONField(name="ProdOrigin")
	private String prodOrigin;
	@JSONField(name="PfCategoryName")
	private String pfCategoryName;
	@JSONField(name="IsStatus")
	private String isStatus;
	@JSONField(name="CreateDate")
	private String createDate;
	@JSONField(name="LastModifiedDate")
	private String lastModifiedDate;
	@JSONField(name="RetailPrice")
	private String retailPrice;
	@JSONField(name="SalePrice")
	private String salePrice;
	@JSONField(name="ItemNo")
	private String itemNo;
	@JSONField(name="PictureUrl")
	private String pictureUrl;
	@JSONField(name="IsOnSale")
	private String isOnSale;
	
	@JSONField(name="Dtls")
	private List<ProdItemSku> dtls;

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getProdOrigin() {
		return prodOrigin;
	}

	public void setProdOrigin(String prodOrigin) {
		this.prodOrigin = prodOrigin;
	}

	public String getPfCategoryName() {
		return pfCategoryName;
	}

	public void setPfCategoryName(String pfCategoryName) {
		this.pfCategoryName = pfCategoryName;
	}

	public String getIsStatus() {
		return isStatus;
	}

	public void setIsStatus(String isStatus) {
		this.isStatus = isStatus;
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

	public String getItemNo() {
		return itemNo;
	}

	public void setItemNo(String itemNo) {
		this.itemNo = itemNo;
	}

	public String getPictureUrl() {
		return pictureUrl;
	}

	public void setPictureUrl(String pictureUrl) {
		this.pictureUrl = pictureUrl;
	}

	public String getIsOnSale() {
		return isOnSale;
	}

	public void setIsOnSale(String isOnSale) {
		this.isOnSale = isOnSale;
	}

	public List<ProdItemSku> getDtls() {
		return dtls;
	}

	public void setDtls(List<ProdItemSku> dtls) {
		this.dtls = dtls;
	}
}

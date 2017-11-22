package com.work.shop.bean;

import java.io.Serializable;
import java.math.BigDecimal;

public class LuckyBagGoodsRelationDetailBean implements Serializable,Comparable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3708242899322779382L;
	private Integer  id;
	private String luckyBagSku;
	private String warehouseId;
	private String subsetCode;
	private String sku;
	private String goodsName;
	private BigDecimal goodsPrice;
	private Integer preStockNumber;
	private Integer actStockNumber;
	private Integer restStockNumber;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getSubsetCode() {
		return subsetCode;
	}
	public void setSubsetCode(String subsetCode) {
		this.subsetCode = subsetCode;
	}
	public String getSku() {
		return sku;
	}
	public void setSku(String sku) {
		this.sku = sku;
	}
	public String getGoodsName() {
		return goodsName;
	}
	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}
	public BigDecimal getGoodsPrice() {
		return goodsPrice;
	}
	public void setGoodsPrice(BigDecimal goodsPrice) {
		this.goodsPrice = goodsPrice;
	}
	public Integer getPreStockNumber() {
		return preStockNumber;
	}
	public void setPreStockNumber(Integer preStockNumber) {
		this.preStockNumber = preStockNumber;
	}
	public Integer getActStockNumber() {
		return actStockNumber;
	}
	public void setActStockNumber(Integer actStockNumber) {
		this.actStockNumber = actStockNumber;
	}
	public String getLuckyBagSku() {
		return luckyBagSku;
	}
	public void setLuckyBagSku(String luckyBagSku) {
		this.luckyBagSku = luckyBagSku;
	}
	public String getWarehouseId() {
		return warehouseId;
	}
	public void setWarehouseId(String warehouseId) {
		this.warehouseId = warehouseId;
	}
	public Integer getRestStockNumber() {
		return restStockNumber;
	}
	public void setRestStockNumber(Integer restStockNumber) {
		this.restStockNumber = restStockNumber;
	}
	@Override
	public int compareTo(Object o) {
		// TODO Auto-generated method stub
		LuckyBagGoodsRelationDetailBean lbgrBean = (LuckyBagGoodsRelationDetailBean)o;
		int otherRestStockNumber = lbgrBean.getRestStockNumber();
		return this.restStockNumber.compareTo(otherRestStockNumber);
	}
	
	

}

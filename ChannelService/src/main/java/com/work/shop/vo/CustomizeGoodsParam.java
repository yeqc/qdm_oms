package com.work.shop.vo;

import java.io.Serializable;
import java.math.BigDecimal;

public class CustomizeGoodsParam implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5129384212941934169L;

	private String sku; // 新增加商品11位码,福袋编码，套装编码
	private BigDecimal goodsPrice; // 商品(福袋)价格
	private String actiontype ; // 操作类型：0->新增 ,1->删除 ,2->修改
	private String goodsType ; // 商品类型：  0->普通商品 ,1->福袋 ,2->套装
	public String getSku() {
		return sku;
	}
	public void setSku(String sku) {
		this.sku = sku;
	}
	public BigDecimal getGoodsPrice() {
		return goodsPrice;
	}
	public void setGoodsPrice(BigDecimal goodsPrice) {
		this.goodsPrice = goodsPrice;
	}
	public String getActiontype() {
		return actiontype;
	}
	public void setActiontype(String actiontype) {
		this.actiontype = actiontype;
	}
	public String getGoodsType() {
		return goodsType;
	}
	public void setGoodsType(String goodsType) {
		this.goodsType = goodsType;
	}
	
}

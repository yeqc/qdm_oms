package com.work.shop.vo;

import java.math.BigDecimal;

import com.work.shop.bean.GroupGoods;
import com.work.shop.util.Constants;

public class GroupGoodsVO extends GroupGoods {

	private Integer start = 0;
	private Integer limit = Constants.EVERY_PAGE_SIZE;
	
	//套装文件上传时使用
	private String goodsSn;
    private BigDecimal goodsPrice;
    private Integer goodsCount;

	public Integer getStart() {
		return start;
	}

	public void setStart(Integer start) {
		this.start = start;
	}

	public Integer getLimit() {
		return limit;
	}

	public void setLimit(Integer limit) {
		this.limit = limit;
	}

	public String getGoodsSn() {
		return goodsSn;
	}

	public void setGoodsSn(String goodsSn) {
		this.goodsSn = goodsSn;
	}

	public BigDecimal getGoodsPrice() {
		return goodsPrice;
	}

	public void setGoodsPrice(BigDecimal goodsPrice) {
		this.goodsPrice = goodsPrice;
	}

	public Integer getGoodsCount() {
		return goodsCount;
	}

	public void setGoodsCount(Integer goodsCount) {
		this.goodsCount = goodsCount;
	}

	
}

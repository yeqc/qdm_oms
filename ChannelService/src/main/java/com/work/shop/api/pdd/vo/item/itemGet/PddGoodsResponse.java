package com.work.shop.api.pdd.vo.item.itemGet;

import java.util.List;

public class PddGoodsResponse {

	private Integer total_count;
	private List<PddGoods> goods_list;
	public Integer getTotal_count() {
		return total_count;
	}
	public void setTotal_count(Integer total_count) {
		this.total_count = total_count;
	}
	public List<PddGoods> getGoods_list() {
		return goods_list;
	}
	public void setGoods_list(List<PddGoods> goods_list) {
		this.goods_list = goods_list;
	}
	
	
	
	
	
}

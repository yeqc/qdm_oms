package com.work.shop.vo;

import com.work.shop.bean.GroupGoodsList;
import com.work.shop.util.Constants;

public class GroupGoodsListSearchVO extends GroupGoodsList {

	private Integer start = 0;
	private Integer limit = Constants.EVERY_PAGE_SIZE;

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

}

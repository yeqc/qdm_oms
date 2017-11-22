package com.work.shop.task;

import java.util.Map;

	
public interface IProductGoodsDataTimerTask {
	/**
	 * 商品同步调度任务
	 * @param para
	 */
	public void syncProduct(Map<String, String> para);

}

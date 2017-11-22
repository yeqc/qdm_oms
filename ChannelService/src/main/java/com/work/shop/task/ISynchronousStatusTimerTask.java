package com.work.shop.task;

import java.util.Map;

public interface ISynchronousStatusTimerTask {

	/**
	 * 调度中心操作：渠道店铺商品状态同步
	 * @param para
	 */
	public void synchronousStatus(Map<String, String> para);
}

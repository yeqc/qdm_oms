package com.work.shop.task;

import java.util.Map;

public interface SyncShopsToken {

	/*
	 * 刷新平台店铺token
	 */
	
	public void syncToken(Map<String,String> para);
}

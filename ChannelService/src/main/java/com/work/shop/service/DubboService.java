package com.work.shop.service;

import com.work.shop.api.bean.ApiResultVO;

public interface DubboService {

	String testMothed(String aa);
	
	ApiResultVO batchUpdateItemStock(String shopCode, String stockInfo);
	
}

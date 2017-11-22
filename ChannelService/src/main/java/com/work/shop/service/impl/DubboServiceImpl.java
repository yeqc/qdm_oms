package com.work.shop.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.work.shop.api.bean.ApiResultVO;
import com.work.shop.api.service.ApiService;
import com.work.shop.service.DubboService;
import com.work.shop.vo.SyncStockParam;

@Service
public class DubboServiceImpl implements DubboService {

	@Resource(name = "apiService")
	private ApiService apiService;

	@Override
	public String testMothed(String aa) {
		return "Hello World: " + aa;
	}

	@Override
	public ApiResultVO batchUpdateItemStock(String shopCode, String stockInfo) {
		List<SyncStockParam> infoList = JSON.parseArray(stockInfo, SyncStockParam.class);
		return null;
	}

}

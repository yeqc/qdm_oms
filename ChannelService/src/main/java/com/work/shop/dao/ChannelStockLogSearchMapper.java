package com.work.shop.dao;

import java.util.List;
import java.util.Map;

import com.work.shop.invocation.ReadOnly;
import com.work.shop.vo.ChannelStockLogVO;

public interface ChannelStockLogSearchMapper {
	
	  @ReadOnly
	    List<ChannelStockLogVO> selectVOByExample(Map<String, Object> params);

}

package com.work.shop.dao;

import java.util.List;
import java.util.Map;

import com.work.shop.invocation.ReadOnly;
import com.work.shop.vo.TicketInfoVo;

public interface ChannelSyncStockMapper {

	@ReadOnly
	List<TicketInfoVo> selectChannelStockResult(Map<String, Object> map);
}
package com.work.shop.dao;

import java.util.List;
import java.util.Map;

import com.work.shop.invocation.ReadOnly;
import com.work.shop.invocation.Writer;
import com.work.shop.bean.ChannelShop;
import com.work.shop.vo.PromotionsLimitMoneyVO;
import com.work.shop.vo.TicketInfoVo;

public interface BgChannelDbTableMapper {

//	@ReadOnly
//	List<ChannelTemplateContentVo> selectModuleContent(Map<String, Object> map);

	@ReadOnly
	List<TicketInfoVo> selectTicketResult(Map<String, Object> map);
	
//	@ReadOnly
//	List<TicketInfoVo> selectChannelStockResult(Map<String, Object> map);
	
	@ReadOnly
	List<ChannelShop> selectOnlineOnsaleChannelShop(Map<String, Object> map);
	
	@Writer
	int deleteByPrimaryKeys(Map map);
	
	@ReadOnly
	List<PromotionsLimitMoneyVO> selectPromotionsLimitMoneyGoods(String promCode);
	
}
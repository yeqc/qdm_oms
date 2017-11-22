package com.work.shop.service;

import com.work.shop.util.extjs.PageHelper;
import com.work.shop.util.extjs.Paging;
import com.work.shop.vo.ChannelGoodsInfoVo;

public interface CashOnDeliverService {

	
	/**
	 * 商品货到付款调整列表查询
	 * @param channelGoodsTicket
	 * @return
	 */
	public Paging getShopGoodsUpDownList(ChannelGoodsInfoVo model, PageHelper helper);
	

	
}

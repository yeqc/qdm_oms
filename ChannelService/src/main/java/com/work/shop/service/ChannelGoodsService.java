package com.work.shop.service;

import java.util.List;

import com.work.shop.bean.ChannelGoods;
import com.work.shop.bean.ChannelGoodsExample;
import com.work.shop.bean.ChannelGoodsExtension;
import com.work.shop.bean.ChannelGoodsExtensionExample;
import com.work.shop.bean.ChannelGoodsExtensionWithBLOBs;
import com.work.shop.bean.ChannelGoodsTicket;
import com.work.shop.bean.GoodsProperty;
import com.work.shop.bean.TicketInfo;
import com.work.shop.util.extjs.PageHelper;
import com.work.shop.util.extjs.Paging;
import com.work.shop.vo.ChannelGoodsInfoVo;
import com.work.shop.vo.ChannelGoodsTicketVo;
import com.work.shop.vo.JsonResult;

public interface ChannelGoodsService {

	Paging getChannelGoodsList(ChannelGoods model, PageHelper helper);

	int getChannelGoodsCount(ChannelGoodsExample example);

	public JsonResult addChannelGoods(ChannelGoodsTicketVo ticketVo, List<ChannelGoodsInfoVo> goodsList);

	public JsonResult deleteChannelGoods(List<String> idList);

	
	/**
	 * 模板列表(分页)
	 * 
	 * @param example
	 * @return Paging
	 */
	Paging getChannelGoodsPage(ChannelGoodsInfoVo model, PageHelper helper) throws Exception;
	
	public ChannelGoodsTicket getChannelGoodsTicketById(String id);
	
	public List<TicketInfo> getTicketInfoByTicketCode(String ticketCode);
	
	public JsonResult updateChannelGoodsTicketInof(ChannelGoodsInfoVo channelGoodsInfoVo);
	
	public ChannelGoodsExtension getGoodsSnDetail(String goodsSn, String channelCode) throws Exception;
	
	GoodsProperty getGoodsProperty(String goodsSn);
	
	public ChannelGoods selectChannelGoods(String goodsSn, String channelCode) throws Exception;
	
	int insertChannelGoods(ChannelGoods channelGoods);

}

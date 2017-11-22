package com.work.shop.service;

import java.util.List;
import java.util.Map;

import com.work.shop.bean.TicketInfo;
import com.work.shop.bean.TicketInfoExample;
import com.work.shop.util.extjs.Paging;
import com.work.shop.vo.ChannelGoodsInfoVo;
import com.work.shop.vo.ChannelGoodsTicketVo;
import com.work.shop.vo.JsonResult;
import com.work.shop.vo.TicketInfoVo;

public interface TicketInfoService {
	
	/**
	 * 调整单商品数据插入
	 * @param list
	 * @return
	 */
	public boolean addTicketInfos(List<TicketInfo> list);
	
	
	/**
	 * 调整单和调整单商品信息
	 * @param id
	 * @return Vo
	 */
	public Paging getTicketInfoVoList(Map<String,Object> params );
	
	/**
	 * 调整单商品信息删除
	 * @param ids
	 * @return
	 */
	public JsonResult deleteTicketInfo(String ids);
	
	
	/**
	 * 调整单商品信息
	 * @param id
	 * @return ticketInfo
	 */
	public List<TicketInfo> getTicketInfoList(TicketInfoExample ticketInfoExample);
	
	/**
	 * 新增调整单与调整单商品
	 * @param ticketVo
	 * @param goodsList
	 * @return JsonResult
	 */
	public JsonResult addChannelTicket(ChannelGoodsTicketVo ticketVo, List<ChannelGoodsInfoVo> goodsList);

	/**
	 * 调整单商品执行结果信息
	 * @param cgt
	 * @return List<TicketInfoVo>
	 */
	public List<TicketInfoVo> selectTicketResult(ChannelGoodsTicketVo cgt);
	
}

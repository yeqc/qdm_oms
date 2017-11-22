package com.work.shop.service;
import java.util.List;

import com.work.shop.bean.ChannelGoods;
import com.work.shop.bean.ChannelGoodsTicket;
import com.work.shop.util.extjs.PageHelper;
import com.work.shop.util.extjs.Paging;
import com.work.shop.vo.ChannelGoodsInfoVo;
import com.work.shop.vo.ChannelGoodsTicketVo;
import com.work.shop.vo.JsonResult;

public interface ShopGoodsService {
	
	/**
	 * 调整单是否 已经 存在
	 * @param id
	 * @return
	 */
	public Boolean isExistTicket(String tcicketCode);
	/**
	 * 店铺商品上下架调整列表查询
	 * @param channelGoodsTicket
	 * @return
	 */
	public Paging getShopGoodsUpDownList(ChannelGoodsInfoVo model, PageHelper helper);
	
	
	/**
	 * 调整单信息查询
	 * @param id
	 * @return
	 */
	public ChannelGoodsTicketVo getChannelGoodsTicketVo(int id);
	
	
	/**
	 * 调整单信息删除
	 * @param id
	 * @return
	 */
	public JsonResult deleteChannelGoodsTickets(String ids);
	
	/**
	 * 批量审核调整单
	 * @param id
	 * @return
	 */
	public JsonResult updateChannelGoodsTicketStatus(String ids,String ticketStatus, String userName, String postageId);
	
	/**
	 * 调整单生成
	 * @param 
	 * @return
	 */
	public boolean insertChannelGoodsTicket(List<ChannelGoodsTicket> list);
	
	/**
	 * 根据商品款号（6位码的goodsSn）查询商品
	 * @param goodsSn
	 * @param shopCode  店铺code
	 * @return list
	 **/
	public List<ChannelGoods> selectChannelGoodsList(String goodsSn,String shopCode);
	


}

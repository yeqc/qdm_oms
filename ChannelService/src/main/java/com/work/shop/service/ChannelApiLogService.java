package com.work.shop.service;

import java.io.File;
import java.util.List;

import com.work.shop.bean.ChannelErpUpdownLog;
import com.work.shop.bean.PromotionsLog;
import com.work.shop.util.extjs.PageHelper;
import com.work.shop.vo.ChannelApiLogVO;
import com.work.shop.vo.ChannelErpUpdownLogVO;
import com.work.shop.vo.ChannelStockLogVO;
import com.work.shop.vo.JsonResult;

public interface ChannelApiLogService {

	/**
	 * 日志数据查询
	 * 
	 * @param channelApiName
	 * @return
	 */
	public JsonResult searchChannelApiLog(ChannelApiLogVO searchVo);
	/**
	 * 查询库存同步日志
	 * @param searchVo
	 * @return
	 */
	
	public JsonResult searchChannelStockLog(ChannelStockLogVO searchVo);
	
	
	/**
	 * 上下架状态同步至ERP日志数据查询
	 * 
	 * @param channelApiName
	 * @return
	 */
	public JsonResult searchErpUpdownLog(ChannelErpUpdownLogVO log, PageHelper helper);
	
	/**
	 * 促销日志数据查询
	 * 
	 * @param channelApiName
	 * @return
	 */
	public JsonResult searchPromotionsLog(PromotionsLog log, PageHelper helper);
	
	public List<ChannelApiLogVO> searchLog(ChannelApiLogVO searchVo);
	
	public List<ChannelStockLogVO> searchStockLog(ChannelStockLogVO searchVo);
	
	public  List<ChannelErpUpdownLogVO> searchUpdownLog(ChannelErpUpdownLogVO searchVo, PageHelper helper);
	
	public  List<PromotionsLog> searchOnePagePromotionsLog(PromotionsLog log, PageHelper helper);
	
	public int countChannelApiLog(ChannelApiLogVO searchVo);
	
	public int countChannelStockLog(ChannelStockLogVO searchVo);
	
	public int countErpUpdownLog(ChannelErpUpdownLogVO log);
	
	public int countPromotionsLog(PromotionsLog log);
	
	public void deleteBeforeDateForFile(File CreateFile,String fileName);

}

package com.work.shop.service.impl;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.work.shop.bean.ChannelApiLogExample;
import com.work.shop.bean.ChannelApiLogExample.Criteria;
import com.work.shop.bean.ChannelErpUpdownLogExample;
import com.work.shop.bean.ChannelStockLogExample;
import com.work.shop.bean.PromotionsLog;
import com.work.shop.bean.PromotionsLogExample;
import com.work.shop.dao.ChannelApiLogMapper;
import com.work.shop.dao.ChannelErpUpdownLogMapper;
import com.work.shop.dao.ChannelStockLogMapper;
import com.work.shop.dao.ChannelStockLogSearchMapper;
import com.work.shop.dao.PromotionsLogMapper;
import com.work.shop.service.ChannelApiLogService;
import com.work.shop.util.DateTimeUtils;
import com.work.shop.util.StringUtil;
import com.work.shop.util.extjs.PageHelper;
import com.work.shop.util.extjs.Paging;
import com.work.shop.vo.ChannelApiLogVO;
import com.work.shop.vo.ChannelErpUpdownLogVO;
import com.work.shop.vo.ChannelStockLogVO;
import com.work.shop.vo.JsonResult;

@Service("channelApiLogService")
public class ChannelApiLogServiceImpl implements ChannelApiLogService {

	@Resource(name = "channelApiLogMapper")
	private ChannelApiLogMapper channelApiLogMapper;
	
	@Resource(name = "channelStockLogMapper")
	private ChannelStockLogMapper channelStockLogMapper;
	
	@Resource(name = "channelStockLogSearchMapper")
	private ChannelStockLogSearchMapper channelStockLogSearchMapper;
	
	@Resource(name = "channelErpUpdownLogMapper")
	private ChannelErpUpdownLogMapper channelErpUpdownLogMapper;

	@Resource(name = "promotionsLogMapper")
	private PromotionsLogMapper promotionsLogMapper;
	
	@Override
	public JsonResult searchChannelApiLog(ChannelApiLogVO searchVo) {

		JsonResult jsonResult = new JsonResult();
		Paging page = new Paging();
		ChannelApiLogExample example = new ChannelApiLogExample();
		example.setOrderByClause("request_time DESC");
		Criteria criteria = example.or();
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("start", searchVo.getStart());
		params.put("limit", searchVo.getLimit());
		if (searchVo != null) {
			if (StringUtil.isNotEmpty(searchVo.getChannelCode())) {
				params.put("channel_code", searchVo.getChannelCode());
				criteria.andChannelCodeEqualTo(searchVo.getChannelCode());
			}
			if (StringUtil.isNotEmpty(searchVo.getShopCode())) {
				params.put("shop_code", searchVo.getShopCode());
				criteria.andShopCodeEqualTo(searchVo.getShopCode());
			}
			if (StringUtil.isNotEmpty(searchVo.getMethodName())) {
				params.put("method_name", searchVo.getMethodName());
				criteria.andMethodNameEqualTo(searchVo.getMethodName());
			}
			if (StringUtil.isNotEmpty(searchVo.getParamInfo())) {
				params.put("param_info", searchVo.getParamInfo().toUpperCase() + "%");
				criteria.andParamInfoLike(searchVo.getParamInfo().toUpperCase() + "%");
			}
			if (StringUtil.isNotEmpty(searchVo.getReturnCode())) {
				if(searchVo.getReturnCode().equals("0")){
				params.put("return_code", searchVo.getReturnCode());
				criteria.andReturnCodeEqualTo(searchVo.getReturnCode());
				}else{
					params.put("return_code", searchVo.getReturnCode());
					criteria.andReturnCodeNotEqualTo("0");
				}
			}
			if (StringUtil.isNotEmpty(searchVo.getBeginTime())) {
				String beginTime = searchVo.getBeginTime();
				params.put("begin_Time", DateTimeUtils.parseStr(beginTime, DateTimeUtils.YYYY_MM_DD_HH_mm_ss));
				criteria.andRequestTimeGreaterThanOrEqualTo(DateTimeUtils.parseStr(beginTime, DateTimeUtils.YYYY_MM_DD_HH_mm_ss));
			}
			if (StringUtil.isNotEmpty(searchVo.getEndTime())) {
				String endTime = searchVo.getEndTime();
				params.put("end_Time", DateTimeUtils.parseStr(endTime, DateTimeUtils.YYYY_MM_DD_HH_mm_ss));
				criteria.andRequestTimeLessThanOrEqualTo(DateTimeUtils.parseStr(endTime, DateTimeUtils.YYYY_MM_DD_HH_mm_ss));
			}
		}
		criteria.limit(searchVo.getStart(), searchVo.getLimit());
		page.setTotalProperty(channelApiLogMapper.countByExample(example));
		page.setRoot(channelApiLogMapper.selectVOByExample(params));
		jsonResult.setIsok(true);
		jsonResult.setData(page);
		return jsonResult;
	}

	@Override
	public JsonResult searchErpUpdownLog(ChannelErpUpdownLogVO log, PageHelper helper) {
		JsonResult jsonResult = new JsonResult();
		Paging page = new Paging();
		ChannelErpUpdownLogExample example = new ChannelErpUpdownLogExample();
		example.setOrderByClause("request_time DESC");
		com.work.shop.bean.ChannelErpUpdownLogExample.Criteria criteria = example.or();
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("start", helper.getStart());
		params.put("limit", helper.getLimit());
		if (log != null) {
			if (StringUtil.isNotEmpty(log.getChannelCode())) {
				params.put("channel_code", log.getChannelCode());
				criteria.andChannelCodeEqualTo(log.getChannelCode());
			}
			if (StringUtil.isNotEmpty(log.getShopCode())) {
				params.put("shop_code", log.getShopCode());
				criteria.andShopCodeEqualTo(log.getShopCode());
			}
			if (StringUtil.isNotEmpty(log.getCode())) {
				params.put("code", log.getCode());
				criteria.andCodeEqualTo(log.getCode());
			}
			if (StringUtil.isNotEmpty(log.getGoodsSn())) {
				params.put("goods_sn", log.getGoodsSn());
				criteria.andGoodsSnEqualTo(log.getGoodsSn());
			}
			if (StringUtil.isNotEmpty(log.getBeginTime())) {
				String beginTime = log.getBeginTime();
				params.put("begin_Time", DateTimeUtils.parseStr(beginTime, DateTimeUtils.YYYY_MM_DD_HH_mm_ss));
				criteria.andRequestTimeGreaterThanOrEqualTo(DateTimeUtils.parseStr(beginTime, DateTimeUtils.YYYY_MM_DD_HH_mm_ss));
			}
			if (StringUtil.isNotEmpty(log.getEndTime())) {
				String endTime = log.getEndTime();
				params.put("end_Time", DateTimeUtils.parseStr(endTime, DateTimeUtils.YYYY_MM_DD_HH_mm_ss));
				criteria.andRequestTimeLessThanOrEqualTo(DateTimeUtils.parseStr(endTime, DateTimeUtils.YYYY_MM_DD_HH_mm_ss));
			}

		}
		criteria.limit(helper.getStart(), helper.getLimit());
		page.setTotalProperty(channelErpUpdownLogMapper.countByExample(example));
		page.setRoot(channelErpUpdownLogMapper.selectVOByExample(params));
		jsonResult.setIsok(true);
		jsonResult.setData(page);
		return jsonResult;
	}

	@Override
	public List<ChannelApiLogVO> searchLog(ChannelApiLogVO searchVo) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("start", searchVo.getStart());
		params.put("limit", searchVo.getLimit());
		if (searchVo != null) {
			if (StringUtil.isNotEmpty(searchVo.getChannelCode())) {
				params.put("channel_code", searchVo.getChannelCode());
			}
			if (StringUtil.isNotEmpty(searchVo.getShopCode())) {
				params.put("shop_code", searchVo.getShopCode());
			}
			if (StringUtil.isNotEmpty(searchVo.getMethodName())) {
				params.put("method_name", searchVo.getMethodName());
			}
			if (StringUtil.isNotEmpty(searchVo.getParamInfo())) {
				params.put("param_info", searchVo.getParamInfo().toUpperCase() + "%");
			}
			if (StringUtil.isNotEmpty(searchVo.getReturnCode())) {
				if(searchVo.getReturnCode().equals("0")){
				params.put("return_code", searchVo.getReturnCode());
				}else{
					params.put("return_code", searchVo.getReturnCode());
				}
			}
			if (StringUtil.isNotEmpty(searchVo.getBeginTime())) {
				String beginTime = searchVo.getBeginTime();
				params.put("begin_Time", DateTimeUtils.parseStr(beginTime, DateTimeUtils.YYYY_MM_DD_HH_mm_ss));
			}
			if (StringUtil.isNotEmpty(searchVo.getEndTime())) {
				String endTime = searchVo.getEndTime();
				params.put("end_Time", DateTimeUtils.parseStr(endTime, DateTimeUtils.YYYY_MM_DD_HH_mm_ss));
			}
		}
		return channelApiLogMapper.selectVOByExample(params);
	}

	@Override
	public List<ChannelErpUpdownLogVO> searchUpdownLog(ChannelErpUpdownLogVO searchVo, PageHelper helper) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("start", helper.getStart());
		params.put("limit", helper.getLimit());
		if (null != searchVo) {
			if (StringUtil.isNotEmpty(searchVo.getChannelCode())) {
				params.put("channel_code", searchVo.getChannelCode());
			}
			if (StringUtil.isNotEmpty(searchVo.getShopCode())) {
				params.put("shop_code", searchVo.getShopCode());
			}
			if (StringUtil.isNotEmpty(searchVo.getGoodsSn())) {
				params.put("goods_sn", searchVo.getGoodsSn());
			}
			if (StringUtil.isNotEmpty(searchVo.getCode())) {
				params.put("code",searchVo.getCode());
			}
			if (StringUtil.isNotEmpty(searchVo.getBeginTime())) {
				String beginTime = searchVo.getBeginTime();
				params.put("begin_Time", DateTimeUtils.parseStr(beginTime, DateTimeUtils.YYYY_MM_DD_HH_mm_ss));
			}
			if (StringUtil.isNotEmpty(searchVo.getEndTime())) {
				String endTime = searchVo.getEndTime();
				params.put("end_Time", DateTimeUtils.parseStr(endTime, DateTimeUtils.YYYY_MM_DD_HH_mm_ss));
			}
		}
		return channelErpUpdownLogMapper.selectVOByExample(params);
	}
	
	/***
	 * 算计日志数量
	 */
	public int countChannelApiLog(ChannelApiLogVO searchVo) {
		ChannelApiLogExample example = new ChannelApiLogExample();
		example.setOrderByClause("request_time DESC");
		Criteria criteria = example.or();
		
		if (searchVo != null) {
			if (StringUtil.isNotEmpty(searchVo.getChannelCode())) {
				criteria.andChannelCodeEqualTo(searchVo.getChannelCode());
			}
			if (StringUtil.isNotEmpty(searchVo.getShopCode())) {
				criteria.andShopCodeEqualTo(searchVo.getShopCode());
			}
			if (StringUtil.isNotEmpty(searchVo.getMethodName())) {
				criteria.andMethodNameEqualTo(searchVo.getMethodName());
			}
			if (StringUtil.isNotEmpty(searchVo.getParamInfo())) {
				criteria.andParamInfoLike(searchVo.getParamInfo().toUpperCase() + "%");
			}
			if (StringUtil.isNotEmpty(searchVo.getReturnCode())) {
				if(searchVo.getReturnCode().equals("0")){
				criteria.andReturnCodeEqualTo(searchVo.getReturnCode());
				}else{
					criteria.andReturnCodeNotEqualTo("0");
				}
			}
			if (StringUtil.isNotEmpty(searchVo.getBeginTime())) {
				String beginTime = searchVo.getBeginTime();
				criteria.andRequestTimeGreaterThanOrEqualTo(DateTimeUtils.parseStr(beginTime, DateTimeUtils.YYYY_MM_DD_HH_mm_ss));
			}
			if (StringUtil.isNotEmpty(searchVo.getEndTime())) {
				String endTime = searchVo.getEndTime();
				criteria.andRequestTimeLessThanOrEqualTo(DateTimeUtils.parseStr(endTime, DateTimeUtils.YYYY_MM_DD_HH_mm_ss));
			}
		}
		return channelApiLogMapper.countByExample(example);
	}

	@Override
	public int countErpUpdownLog(ChannelErpUpdownLogVO log) {
		ChannelErpUpdownLogExample example = new ChannelErpUpdownLogExample();
		com.work.shop.bean.ChannelErpUpdownLogExample.Criteria criteria = example.or();
		if (log != null) {
			if (StringUtil.isNotEmpty(log.getChannelCode())) {
				criteria.andChannelCodeEqualTo(log.getChannelCode());
			}
			if (StringUtil.isNotEmpty(log.getShopCode())) {
				criteria.andShopCodeEqualTo(log.getShopCode());
			}
			if (StringUtil.isNotEmpty(log.getCode())) {
				criteria.andCodeEqualTo(log.getCode());
			}
			if (StringUtil.isNotEmpty(log.getGoodsSn())) {
				criteria.andGoodsSnEqualTo(log.getGoodsSn());
			}
			if (StringUtil.isNotEmpty(log.getBeginTime())) {
				String beginTime = log.getBeginTime();
				criteria.andRequestTimeGreaterThanOrEqualTo(DateTimeUtils.parseStr(beginTime, DateTimeUtils.YYYY_MM_DD_HH_mm_ss));
			}
			if (StringUtil.isNotEmpty(log.getEndTime())) {
				String endTime = log.getEndTime();
				criteria.andRequestTimeLessThanOrEqualTo(DateTimeUtils.parseStr(endTime, DateTimeUtils.YYYY_MM_DD_HH_mm_ss));
			}
		}
		return channelErpUpdownLogMapper.countByExample(example);
	}

	/***
	 * 删除当天以外的所有文件
	 */
	public void deleteBeforeDateForFile(File CreateFile, String fileName) {
		// TODO Auto-generated method stub
		
		File[] fileList = CreateFile.listFiles();
		
		//删除当前之前所有文件
		for(File xflie: fileList){
			if(!fileName.equals(xflie.getName())){
				xflie.delete();
			}
		}

	}

	@Override
	public JsonResult searchPromotionsLog(PromotionsLog log, PageHelper helper) {
		JsonResult jsonResult = new JsonResult();
		Paging page = new Paging();
		PromotionsLogExample example1 = new PromotionsLogExample();
		PromotionsLogExample example2 = new PromotionsLogExample();
		example2.setOrderByClause("add_time DESC");
		com.work.shop.bean.PromotionsLogExample.Criteria criteria1 = example1.or();
		com.work.shop.bean.PromotionsLogExample.Criteria criteria2 = example2.or();
		if (log != null) {
			if (StringUtil.isNotEmpty(log.getShopCode())) {
				criteria1.andShopCodeEqualTo(log.getShopCode());
				criteria2.andShopCodeEqualTo(log.getShopCode());
			}
			if (StringUtil.isNotEmpty(log.getPromCode())) {
				criteria1.andPromCodeEqualTo(log.getPromCode());
				criteria2.andPromCodeEqualTo(log.getPromCode());
			}
			if (log.getPromType() != null) {
				criteria1.andPromTypeEqualTo(log.getPromType());
				criteria2.andPromTypeEqualTo(log.getPromType());
			}
		}
		page.setTotalProperty(promotionsLogMapper.countByExample(example1));
		criteria2.limit(helper.getStart(), helper.getLimit());
		page.setRoot(promotionsLogMapper.selectByExampleWithBLOBs(example2));
		jsonResult.setIsok(true);
		jsonResult.setData(page);
		return jsonResult;
	}

	@Override
	public int countPromotionsLog(PromotionsLog log) {
		PromotionsLogExample example = new PromotionsLogExample();
		example.setOrderByClause("add_time DESC");
		com.work.shop.bean.PromotionsLogExample.Criteria criteria = example.or();
		if (log != null) {
			if (StringUtil.isNotEmpty(log.getShopCode())) {
				criteria.andShopCodeEqualTo(log.getShopCode());
			}
			if (StringUtil.isNotEmpty(log.getPromCode())) {
				criteria.andPromCodeEqualTo(log.getPromCode());
			}
			if (log.getPromType() != null) {
				criteria.andPromTypeEqualTo(log.getPromType());
			}
		}
		return promotionsLogMapper.countByExample(example);
	}
	
	@Override
	public List<PromotionsLog> searchOnePagePromotionsLog(PromotionsLog log, PageHelper helper){
		PromotionsLogExample example = new PromotionsLogExample();
		example.setOrderByClause("add_time DESC");
		com.work.shop.bean.PromotionsLogExample.Criteria criteria = example.or();
		if (log != null) {
			if (StringUtil.isNotEmpty(log.getShopCode())) {
				criteria.andShopCodeEqualTo(log.getShopCode());
			}
			if (StringUtil.isNotEmpty(log.getPromCode())) {
				criteria.andPromCodeEqualTo(log.getPromCode());
			}
			if (log.getPromType() != null) {
				criteria.andPromTypeEqualTo(log.getPromType());
			}
		}
		return promotionsLogMapper.selectByExampleWithBLOBs(example);
	}

	@Override
	public JsonResult searchChannelStockLog(ChannelStockLogVO searchVo) {
		JsonResult jsonResult = new JsonResult();
		Paging page = new Paging();
		ChannelStockLogExample example = new ChannelStockLogExample();
		example.setOrderByClause("request_time DESC");
		ChannelStockLogExample.Criteria criteria = example.or();
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("start", searchVo.getStart());
		params.put("limit", searchVo.getLimit());
		if (searchVo != null) {
			if (StringUtil.isNotEmpty(searchVo.getChannelCode())) {
				params.put("channel_code", searchVo.getChannelCode());
				criteria.andChannelCodeEqualTo(searchVo.getChannelCode());
			}
			if (StringUtil.isNotEmpty(searchVo.getShopCode())) {
				params.put("shop_code", searchVo.getShopCode());
				criteria.andShopCodeEqualTo(searchVo.getShopCode());
			}
			if (StringUtil.isNotEmpty(searchVo.getSkuSn())) {
				
				params.put("sku_sn", searchVo.getSkuSn()+"%");
				criteria.andSkuSnLike(searchVo.getSkuSn()+"%");
			}
			if (StringUtil.isNotEmpty(searchVo.getReturnCode())) {
				if(searchVo.getReturnCode().equals("0")){
				params.put("return_code", searchVo.getReturnCode());
				criteria.andReturnCodeEqualTo(searchVo.getReturnCode());
				}else{
					params.put("return_code", searchVo.getReturnCode());
					criteria.andReturnCodeNotEqualTo("0");
				}
			}
			if (StringUtil.isNotEmpty(searchVo.getBeginTime())) {
				String beginTime = searchVo.getBeginTime();
				params.put("begin_Time", DateTimeUtils.parseStr(beginTime, DateTimeUtils.YYYY_MM_DD_HH_mm_ss));
				criteria.andRequestTimeGreaterThanOrEqualTo(DateTimeUtils.parseStr(beginTime, DateTimeUtils.YYYY_MM_DD_HH_mm_ss));
			}
			if (StringUtil.isNotEmpty(searchVo.getEndTime())) {
				String endTime = searchVo.getEndTime();
				params.put("end_Time", DateTimeUtils.parseStr(endTime, DateTimeUtils.YYYY_MM_DD_HH_mm_ss));
				criteria.andRequestTimeLessThanOrEqualTo(DateTimeUtils.parseStr(endTime, DateTimeUtils.YYYY_MM_DD_HH_mm_ss));
			}
		}
		criteria.limit(searchVo.getStart(), searchVo.getLimit());
		page.setTotalProperty(channelStockLogMapper.countByExample(example));
		page.setRoot(channelStockLogSearchMapper.selectVOByExample(params));
		jsonResult.setIsok(true);
		jsonResult.setData(page);
		return jsonResult;
	}

	@Override
	public int countChannelStockLog(ChannelStockLogVO searchVo) {
		ChannelStockLogExample example = new ChannelStockLogExample();
		ChannelStockLogExample.Criteria criteria = example.or();
		if (searchVo != null) {
			if (StringUtil.isNotEmpty(searchVo.getChannelCode())) {
				criteria.andChannelCodeEqualTo(searchVo.getChannelCode());
			}
			if (StringUtil.isNotEmpty(searchVo.getShopCode())) {
				criteria.andShopCodeEqualTo(searchVo.getShopCode());
			}
			if (StringUtil.isNotEmpty(searchVo.getSkuSn())) {
				criteria.andSkuSnLike(searchVo.getSkuSn()+"%");
			}
			if (StringUtil.isNotEmpty(searchVo.getReturnCode())) {
				if(searchVo.getReturnCode().equals("0")){
				criteria.andReturnCodeEqualTo(searchVo.getReturnCode());
				}else{
					criteria.andReturnCodeNotEqualTo("0");
				}
			}
			if (StringUtil.isNotEmpty(searchVo.getBeginTime())) {
				String beginTime = searchVo.getBeginTime();
				criteria.andRequestTimeGreaterThanOrEqualTo(DateTimeUtils.parseStr(beginTime, DateTimeUtils.YYYY_MM_DD_HH_mm_ss));
			}
			if (StringUtil.isNotEmpty(searchVo.getEndTime())) {
				String endTime = searchVo.getEndTime();
				criteria.andRequestTimeLessThanOrEqualTo(DateTimeUtils.parseStr(endTime, DateTimeUtils.YYYY_MM_DD_HH_mm_ss));
			}
		}
		return channelStockLogMapper.countByExample(example);
	}

	@Override
	public List<ChannelStockLogVO> searchStockLog(ChannelStockLogVO searchVo) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("start", searchVo.getStart());
		params.put("limit", searchVo.getLimit());
		if (searchVo != null) {
			if (StringUtil.isNotEmpty(searchVo.getChannelCode())) {
				params.put("channel_code", searchVo.getChannelCode());
			}
			if (StringUtil.isNotEmpty(searchVo.getShopCode())) {
				params.put("shop_code", searchVo.getShopCode());
			}
			if (StringUtil.isNotEmpty(searchVo.getSkuSn())) {
				params.put("sku_sn", searchVo.getSkuSn()+"%");
			}
			if (StringUtil.isNotEmpty(searchVo.getReturnCode())) {
				if(searchVo.getReturnCode().equals("0")){
				params.put("return_code", searchVo.getReturnCode());
				}else{
					params.put("return_code", searchVo.getReturnCode());
				}
			}
			if (StringUtil.isNotEmpty(searchVo.getBeginTime())) {
				String beginTime = searchVo.getBeginTime();
				params.put("begin_Time", DateTimeUtils.parseStr(beginTime, DateTimeUtils.YYYY_MM_DD_HH_mm_ss));
			}
			if (StringUtil.isNotEmpty(searchVo.getEndTime())) {
				String endTime = searchVo.getEndTime();
				params.put("end_Time", DateTimeUtils.parseStr(endTime, DateTimeUtils.YYYY_MM_DD_HH_mm_ss));
			}
		}
		return channelStockLogSearchMapper.selectVOByExample(params);
	}
}

package com.work.shop.service.impl;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.work.shop.bean.ChannelGoodsTicket;
import com.work.shop.bean.TicketInfo;
import com.work.shop.bean.TicketInfoExample;
import com.work.shop.bean.TicketInfoExample.Criteria;
import com.work.shop.dao.BgChannelDbTableMapper;
import com.work.shop.dao.ChannelGoodsTicketMapper;
import com.work.shop.dao.TicketInfoMapper;
import com.work.shop.service.TicketInfoService;
import com.work.shop.util.StringUtil;
import com.work.shop.util.TimeUtil;
import com.work.shop.util.extjs.Paging;
import com.work.shop.vo.ChannelGoodsInfoVo;
import com.work.shop.vo.ChannelGoodsTicketVo;
import com.work.shop.vo.JsonResult;
import com.work.shop.vo.TicketInfoVo;

@Service("ticketInfoService")
public class TicketInfoServiceImpl implements TicketInfoService {
	
	@Resource(name="ticketInfoMapper")
	private TicketInfoMapper ticketInfoMapper;
	
	@Resource(name = "channelGoodsTicketMapper")
	private ChannelGoodsTicketMapper channelGoodsTicketMapper;
	
	@Resource(name = "bgChannelDbTableMapper")
	private BgChannelDbTableMapper bgChannelDbTableMapper;
	
	//调整单商品信息保存
	public boolean addTicketInfos(List<TicketInfo> list){
		int record = 0;
		if(list != null && list.size()>0){
			for(int i=0;i<list.size();i++){
				ticketInfoMapper.insert(list.get(i));
				record++; //正确插入记录数
			}
			
		}	
		if(list.size() == record){
			return true;
		}else{
			return false;
		}
	} 
	
	/**
	 * 调整单商品信息查询：调整单表，调整单商品表
	 * @param id
	 * @return
	 */
	public Paging getTicketInfoVoList(Map<String,Object> params){
		Paging paging = new Paging();

		List<TicketInfoVo> list = ticketInfoMapper.selectByVo(params);
		int count = ticketInfoMapper.countByVo(params);
		paging.setRoot(list);
		paging.setTotalProperty(count);
		return paging;
	}
    
	
	/**
	 * 调整单商品信息删除
	 * @param ids
	 * @return
	 */
	public JsonResult deleteTicketInfo(String ids){
		JsonResult jsonResult = new JsonResult();
		List<Integer> list = new ArrayList<Integer>();
		TicketInfoExample ticketInfoExample = new TicketInfoExample();
		Criteria criteria = ticketInfoExample.or();
      if(ids != null && StringUtils.isNotEmpty(ids)){
			
			String[] str = ids.substring(0, ids.length()-1).split(",");//去除最后一个","
			for(int i=0;i<str.length;i++){
				int id = Integer.parseInt(str[i]);
				list.add(id);//需要删除的id数组				
			}
			criteria.andIdIn(list);
		} 
      
      int record = ticketInfoMapper.deleteByExample(ticketInfoExample);
		
		if(list != null && record == list.size()){
			jsonResult.setIsok(true);
			jsonResult.setMessage("批量删除成功！");
		}else{			
			jsonResult.setIsok(false);
			jsonResult.setMessage("批量删除失败！");
		}
		return jsonResult;
	}
	
	
	//获取调整单商品列表，用于更新
	public List<TicketInfo> getTicketInfoList(TicketInfoExample ticketInfoExample){
		
		List<TicketInfo> list = ticketInfoMapper.selectByExample(ticketInfoExample);
		
		return list;
		
	}

	/**
	 * 新增调整单与调整单商品
	 * @param ticketVo
	 * @param goodsList
	 * @return JsonResult
	 */
	@Override
	public JsonResult addChannelTicket(ChannelGoodsTicketVo ticketVo, List<ChannelGoodsInfoVo> goodsList) {
		JsonResult jsonResult = new JsonResult();
		try {
			int count = 0;
			ChannelGoodsTicket channelGoodsTicket = new ChannelGoodsTicket();// 商品款号
			channelGoodsTicket = this.channelGoodsTicketMapper.selectByTicketCode(ticketVo.getTicketCode());
			if (null != channelGoodsTicket) {
				channelGoodsTicket.setIsTiming(Byte.valueOf(ticketVo.getIsTiming()));
				if (StringUtil.isNotNull(ticketVo.getExcetTimes())) {
					Date excetDate = TimeUtil.parseString2Date(URLDecoder.decode(ticketVo.getExcetTimes(), "utf-8"));
					channelGoodsTicket.setExecTime(excetDate);
				}
				count = channelGoodsTicketMapper.updateByPrimaryKeySelective(channelGoodsTicket);
			} else {
				channelGoodsTicket = new ChannelGoodsTicket();// 商品款号
				channelGoodsTicket.setTicketCode(ticketVo.getTicketCode());
				channelGoodsTicket.setChannelCode(ticketVo.getChannelCode());// 渠道code
				channelGoodsTicket.setShopCode(ticketVo.getShopCode()); // 店铺code
				channelGoodsTicket.setIsTiming(Byte.valueOf(ticketVo.getIsTiming()));
				if (StringUtil.isNotNull(ticketVo.getExcetTimes())) {
					Date excetDate = TimeUtil.parseString2Date(URLDecoder.decode(ticketVo.getExcetTimes(), "utf-8"));
					channelGoodsTicket.setExecTime(excetDate);
				}
				channelGoodsTicket.setAddTime(TimeUtil.getTimestamp());
				channelGoodsTicket.setTicketType(ticketVo.getTicketType());
				channelGoodsTicket.setTicketStatus("0");
				channelGoodsTicket.setOperUser(ticketVo.getUserName());
				count = channelGoodsTicketMapper.insertSelective(channelGoodsTicket);
			}
			for (ChannelGoodsInfoVo infoVo : goodsList) {
				if (count != 1) {
					jsonResult.setIsok(false);
					jsonResult.setMessage("添加失败!");
					return jsonResult;
				}
				TicketInfo info = new TicketInfo();
				info.setTicketCode(ticketVo.getTicketCode());
				info.setChannelGoodssn(infoVo.getChannelGoodsCode());// 渠道商品款号
				info.setGoodsSn(infoVo.getProductGoodsSn());// 商品款号
				info.setGoodsTitle(infoVo.getGoodsTitle());// 商品名称
				info.setNewPrice(infoVo.getNewPrice());// 商品新价格
				info.setSafePrice(infoVo.getSafePrice());//保护价
				info.setShowName(infoVo.getShowName());//商品展示标题//2017-7-4  天猫渠道  新增   商品展示标题
				count = ticketInfoMapper.insertSelective(info);
				if (count != 1) {
					jsonResult.setIsok(false);
					jsonResult.setMessage("添加失败!");
					return jsonResult;
				}
			}
			jsonResult.setIsok(true);
			jsonResult.setMessage("添加成功!");
		} catch (Exception e) {
			e.printStackTrace();
			jsonResult.setIsok(false);
			jsonResult.setMessage("添加失败!");
		}
		return jsonResult;
	}

	@Override
	public List<TicketInfoVo> selectTicketResult(ChannelGoodsTicketVo cgt) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("ticketCode", cgt.getTicketCode());
		paramMap.put("ticketCodeLike", cgt.getTicketCode()+ "%");
		return this.bgChannelDbTableMapper.selectTicketResult(paramMap);
	}


}

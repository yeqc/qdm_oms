package com.work.shop.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.work.shop.bean.ChannelGoodsTicketExample;
import com.work.shop.bean.ChannelGoodsTicketExample.Criteria;
import com.work.shop.dao.ChannelGoodsTicketMapper;
import com.work.shop.service.CashOnDeliverService;
import com.work.shop.util.StringUtil;
import com.work.shop.util.extjs.PageHelper;
import com.work.shop.util.extjs.Paging;
import com.work.shop.vo.ChannelGoodsInfoVo;
import com.work.shop.vo.ChannelGoodsTicketVo;

@Service("cashOnDeliverService")
public class CashOnDeliverServiceImpl implements CashOnDeliverService {

	@Resource(name = "channelGoodsTicketMapper")
	private ChannelGoodsTicketMapper channelGoodsTicketMapper;
	
	@Override
	public Paging getShopGoodsUpDownList(ChannelGoodsInfoVo model,
			PageHelper helper) {
		
		ChannelGoodsTicketExample channelGoodsTicketExample = new ChannelGoodsTicketExample();
		channelGoodsTicketExample.setOrderByClause("add_time desc");
		Criteria criteria = channelGoodsTicketExample.or();
		if(model.getTicketCode() != null && StringUtil.isNotEmpty(model.getTicketCode())){
			criteria.andTicketCodeEqualTo(model.getTicketCode());
		}
		if(model.getShopCode()!= null && StringUtil.isNotEmpty(model.getShopCode())){
			criteria.andShopCodeVoEqualTo(model.getShopCode());
		}
		if(model.getTicketStatus() != null && StringUtil.isNotEmpty(model.getTicketStatus())){
			criteria.andTicketStatusEqualTo(model.getTicketStatus());
		}
		if(model.getIsTiming() != null && !"".equals(model.getIsTiming()) ){
			criteria.andIsTimingEqualTo(Byte.parseByte(model.getIsTiming()));//是否定时执行	
		}
		if(null != model.getTicketType()) {
			
	/*		if(2 == model.getTicketType()) {
				List<Byte> list = new ArrayList<Byte>();
				list.add((byte)2);
				list.add((byte)12);
				criteria.andTicketTypeIn(list);
			} else {*/
			criteria.andTicketTypeEqualTo(model.getTicketType());
		///	}
		}
		if(StringUtil.isNotBlank(model.getShopCode())) {
			criteria.andShopCodeVoEqualTo(model.getShopCode());
		}
		//分页字段
		criteria.limit(helper.getStart(), helper.getLimit());
		Paging paging = new Paging();
		List<ChannelGoodsTicketVo> list = channelGoodsTicketMapper.selectByExample(channelGoodsTicketExample);
		int count = channelGoodsTicketMapper.countByExample(channelGoodsTicketExample);
		paging.setRoot(list);
		paging.setTotalProperty(count);
		return paging;

	}



}

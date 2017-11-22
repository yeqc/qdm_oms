package com.work.shop.service.impl;

import java.net.URLDecoder;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.work.shop.bean.ChannelGoods;
import com.work.shop.bean.ChannelGoodsExample;
import com.work.shop.bean.ChannelGoodsExtension;
import com.work.shop.bean.ChannelGoodsExtensionExample;
import com.work.shop.bean.ChannelGoodsExtensionExample.Criteria;
import com.work.shop.bean.ChannelGoodsTicket;
import com.work.shop.bean.ChannelGoodsTicketExample;
import com.work.shop.bean.ChannelGoodsVo;
import com.work.shop.bean.GoodsProperty;
import com.work.shop.bean.TicketInfo;
import com.work.shop.bean.TicketInfoExample;
import com.work.shop.bean.bgcontentdb.BGproductBarcodeList;
import com.work.shop.bean.bgcontentdb.BGproductBarcodeListExample;
import com.work.shop.dao.BgGoodsPropertyMapper;
import com.work.shop.dao.ChannelGoodsExtensionMapper;
import com.work.shop.dao.ChannelGoodsMapper;
import com.work.shop.dao.ChannelGoodsTicketMapper;
import com.work.shop.dao.GoodsPropertyMapper;
import com.work.shop.dao.TicketInfoMapper;
import com.work.shop.dao.bgcontentdb.BGproductBarcodeListMapper;
import com.work.shop.service.ChannelGoodsService;
import com.work.shop.util.DateTimeUtils;
import com.work.shop.util.StringUtil;
import com.work.shop.util.TimeUtil;
import com.work.shop.util.extjs.PageHelper;
import com.work.shop.util.extjs.Paging;
import com.work.shop.vo.ChannelGoodsInfoVo;
import com.work.shop.vo.ChannelGoodsTicketVo;
import com.work.shop.vo.JsonResult;

@Service("channelGoodsService")
public class ChannelGoodsServiceImpl implements ChannelGoodsService {

	@Resource(name = "channelGoodsMapper")
	private ChannelGoodsMapper channelGoodsMapper;

	@Resource(name = "channelGoodsTicketMapper")
	private ChannelGoodsTicketMapper channelGoodsTicketMapper;

	@Resource(name = "ticketInfoMapper")
	private TicketInfoMapper ticketInfoMapper;

	@Resource(name = "channelGoodsExtensionMapper")
	private ChannelGoodsExtensionMapper channelGoodsExtensionMapper;
	
	@Resource(name = "goodsPropertyMapper")
	private GoodsPropertyMapper goodsPropertyMapper;
	
	@Resource(name = "bgGoodsPropertyMapper")
	private BgGoodsPropertyMapper bgGoodsPropertyMapper;
	
	@Resource(name = "bGproductBarcodeListMapper")
	private BGproductBarcodeListMapper productBarcodeListMapper;

	@Override
	public Paging getChannelGoodsPage(ChannelGoodsInfoVo model, PageHelper helper) throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		if (StringUtil.isNotEmpty(model.getChannelCode())) {
			params.put("channel_code", model.getChannelCode());
		}
		if (StringUtil.isNotEmpty(model.getShopCode())) {
			params.put("shop_code", model.getShopCode());
		}
		if (StringUtil.isNotEmpty(model.getGoodsSn())) {
			params.put("goods_sn", model.getGoodsSn());
		}
		if (StringUtil.isNotEmpty(model.getGoodsTitle())) {
			params.put("goods_title", model.getGoodsTitle());
		}
		params.put("start", helper.getStart());
		params.put("limit", helper.getLimit());
		Paging paging = new Paging(channelGoodsTicketMapper.selectGoodsInfoCount(params), channelGoodsTicketMapper.selectGoodsInfo(params));
		return paging;
	}

	/* 渠道商品列表
	 * (non-Javadoc)
	 * @see com.work.shop.service.ChannelGoodsService#getChannelGoodsList(com.work.shop.bean.ChannelGoods, com.work.shop.util.extjs.PageHelper)
	 */
	@Override
	public Paging getChannelGoodsList(ChannelGoods model, PageHelper helper) {
		ChannelGoodsExample example = new ChannelGoodsExample();
		com.work.shop.bean.ChannelGoodsExample.Criteria criteria = example.or();
		criteria.limit(helper.getStart(), helper.getLimit());
	
		Map<String,Object> map = new HashMap<String,Object>();
		if (StringUtil.isNotEmpty(model.getGoodsSn())) {
			criteria.andGoodsSnEqualTo(model.getGoodsSn());
			map.put("goodsSn", model.getGoodsSn());
		}
		if (StringUtil.isNotEmpty(model.getGoodsName())) {
			criteria.andGoodsNameLike("%"+model.getGoodsName()+"%");
			map.put("goodsName",model.getGoodsName());
		}
		if (StringUtil.isNotEmpty(model.getChannelCode())) {
			criteria.andChannelCodeEqualTo(model.getChannelCode());
			map.put("channelCode",model.getChannelCode());
		}
	//	List<ChannelGoods> list = this.channelGoodsMapper.selectByExample(example);
		
		map.put("start", helper.getStart());
		map.put("limit",  helper.getLimit());
	
		List<ChannelGoodsVo> list = goodsPropertyMapper.selectChannelGoods(map);
		
		for(ChannelGoodsVo channelGoodsVo : list){
			BGproductBarcodeListExample productBarcodeListExample = new BGproductBarcodeListExample();
			
			com.work.shop.bean.bgcontentdb.BGproductBarcodeListExample.Criteria pbCriteria = productBarcodeListExample.or();
			
			if (StringUtil.isNotEmpty(channelGoodsVo.getGoodsSn())) {
				pbCriteria.andGoodsSnEqualTo(channelGoodsVo.getGoodsSn());
			}
			
			List <BGproductBarcodeList> pbList = productBarcodeListMapper.selectByExample(productBarcodeListExample);
			channelGoodsVo.setBarcodeChild(pbList);
			if(null != channelGoodsVo.getIsSyncStock()){
				if(channelGoodsVo.getIsSyncStock().intValue()==0){
					channelGoodsVo.setIsSync("不同步");
				}
				else if(channelGoodsVo.getIsSyncStock().intValue()==1){
					channelGoodsVo.setIsSync("同步");
				}
			}
		}

		Paging paging = new Paging(this.channelGoodsMapper.countByExample(example), list);
		return paging;
	}

	@Override
	public ChannelGoodsTicket getChannelGoodsTicketById(String id) {
		return channelGoodsTicketMapper.selectByPrimaryKey(Integer.valueOf(id));
	}

	@Override
	public List<TicketInfo> getTicketInfoByTicketCode(String ticketCode) {
		TicketInfoExample example = new TicketInfoExample();
		example.or().andTicketCodeEqualTo(ticketCode);
		return ticketInfoMapper.selectByExample(example);
	}

	@Override
	public int getChannelGoodsCount(ChannelGoodsExample example) {
		return channelGoodsMapper.countByExample(example);
	}

	@Override
	public JsonResult addChannelGoods(ChannelGoodsTicketVo ticketVo, List<ChannelGoodsInfoVo> goodsList) {
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
				channelGoodsTicket.setTicketType((byte) 2);
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
	public JsonResult deleteChannelGoods(List<String> idList) {
		JsonResult jsonResult = new JsonResult();
		try {
			int count = 0;
			ChannelGoodsTicketExample example = new ChannelGoodsTicketExample();
			example.or().andTicketCodeIn(idList);
			count = channelGoodsTicketMapper.deleteByExample(example);
			if (count == 0) {
				jsonResult.setIsok(false);
				jsonResult.setMessage("删除失败!");
				return jsonResult;
			}
			TicketInfoExample ticketInfoExample = new TicketInfoExample();
			ticketInfoExample.or().andTicketCodeIn(idList);
			count = ticketInfoMapper.deleteByExample(ticketInfoExample);
			if (count == 0) {
				jsonResult.setIsok(false);
				jsonResult.setMessage("删除失败!");
				return jsonResult;
			}
			jsonResult.setMessage("删除成功!");
		} catch (Exception e) {
			jsonResult.setIsok(false);
			jsonResult.setMessage("删除失败!");
		}
		return jsonResult;
	}

	@Override
	public JsonResult updateChannelGoodsTicketInof(ChannelGoodsInfoVo channelGoodsInfoVo) {
		JsonResult jsonResult = new JsonResult();
		try {
			ChannelGoodsTicket channelGoodsTicket = channelGoodsTicketMapper.selectByPrimaryKey(channelGoodsInfoVo.getId());
			if (channelGoodsTicket == null) {
				jsonResult.setIsok(false);
				jsonResult.setMessage("保存失败!");
				return jsonResult;
			}

			TicketInfoExample example = new TicketInfoExample();
			example.or().andTicketCodeEqualTo(channelGoodsTicket.getTicketCode());
			List<TicketInfo> list = ticketInfoMapper.selectByExample(example);
			if (list == null || list.size() != 1) {
				jsonResult.setIsok(false);
				jsonResult.setMessage("保存失败!");
				return jsonResult;
			}
			TicketInfo ticketInfo = list.get(0);

			channelGoodsTicket.setChannelCode(channelGoodsInfoVo.getChannelCode());
			channelGoodsTicket.setShopCode(channelGoodsInfoVo.getShopCode());
			channelGoodsTicket.setIsTiming(Byte.valueOf(channelGoodsInfoVo.getIsTiming()));
			channelGoodsTicket.setExecTime(DateTimeUtils.parseStr(channelGoodsInfoVo.getExecTime(), DateTimeUtils.YYYY_MM_DD_HH_mm_ss));
			int count = channelGoodsTicketMapper.updateByPrimaryKey(channelGoodsTicket);
			if (count != 1) {
				jsonResult.setIsok(false);
				jsonResult.setMessage("保存失败!");
				return jsonResult;
			}

			ticketInfo.setChannelGoodssn(channelGoodsInfoVo.getChannelGoodsCode());
			ticketInfo.setGoodsSn(channelGoodsInfoVo.getGoodsSn());
			ticketInfo.setGoodsTitle(channelGoodsInfoVo.getGoodsTitle());

			count = ticketInfoMapper.updateByExample(ticketInfo, example);
			if (count != 1) {
				jsonResult.setIsok(false);
				jsonResult.setMessage("保存失败!");
				return jsonResult;
			}
			jsonResult.setMessage("保存成功!");
		} catch (Exception e) {
			jsonResult.setIsok(false);
			jsonResult.setMessage("保存失败!");
		}
		return jsonResult;
	}
	
	@Override
	public ChannelGoodsExtension getGoodsSnDetail(String goodsSn, String channelCode) throws Exception{
		ChannelGoodsExtensionExample example = new ChannelGoodsExtensionExample();
		Criteria criteria = example.or();
		criteria.andGoodsSnEqualTo(goodsSn);
		criteria.andChannelCodeEqualTo(channelCode);
		List<ChannelGoodsExtension> list = this.channelGoodsExtensionMapper.selectByExampleWithBLOBs(example);
		if (StringUtil.isNotNullForList(list)) {
			return list.get(0);
		}
		return null;
	}

	@Override
	public GoodsProperty getGoodsProperty(String goodsSn) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("goodsSn", goodsSn);
		GoodsProperty  gp = bgGoodsPropertyMapper.selectGoodsProperties(map);
		return gp;
	}

	@Override
	public ChannelGoods selectChannelGoods(String goodsSn, String channelCode)
			throws Exception {
		ChannelGoodsExample example = new ChannelGoodsExample();
		com.work.shop.bean.ChannelGoodsExample.Criteria criteria = example.or();
		criteria.andGoodsSnEqualTo(goodsSn);
		criteria.andChannelCodeEqualTo(channelCode);
		List<ChannelGoods> list = this.channelGoodsMapper.selectByExample(example);
		if (StringUtil.isNotNullForList(list)) {
			return list.get(0);
		}
		return null;
	}
	@Override
	public int insertChannelGoods(ChannelGoods channelGoods){
		return channelGoodsMapper.insert(channelGoods);
	}
}

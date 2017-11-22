package com.work.shop.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.work.shop.bean.ChannelGoods;
import com.work.shop.bean.ChannelGoodsExample;
import com.work.shop.bean.ChannelGoodsTicket;
import com.work.shop.bean.ChannelGoodsTicketExample;
import com.work.shop.bean.ChannelGoodsTicketExample.Criteria;
import com.work.shop.bean.TicketInfo;
import com.work.shop.bean.TicketInfoExample;
import com.work.shop.dao.ChannelGoodsMapper;
import com.work.shop.dao.ChannelGoodsTicketMapper;
import com.work.shop.dao.TicketInfoMapper;
import com.work.shop.mq.mtsbw.producer.OnsProducerServiceImpl;
import com.work.shop.service.ShopGoodsService;
import com.work.shop.util.Constants;
import com.work.shop.util.ExceptionStackTraceUtil;
import com.work.shop.util.StringUtil;
import com.work.shop.util.extjs.PageHelper;
import com.work.shop.util.extjs.Paging;
import com.work.shop.vo.ChannelGoodsInfoVo;
import com.work.shop.vo.ChannelGoodsTicketVo;
import com.work.shop.vo.JsonResult;
import com.work.shop.vo.QueueManagerVo;

@Service("shopGoodsService")
public class ShopGoodsServiceImpl implements ShopGoodsService {

	@Resource(name = "channelGoodsTicketMapper")
	private ChannelGoodsTicketMapper channelGoodsTicketMapper;

	@Resource(name = "channelGoodsMapper")
	private ChannelGoodsMapper channelGoodsMapper;

	@Resource
	private TicketInfoMapper ticketInfoMapper;
	@Resource
	private OnsProducerServiceImpl onsProducerServiceImpl;
	
	private static final Logger logger = Logger.getLogger(ShopGoodsServiceImpl.class);

	/**
	 * 调整单是否 已经 存在
	 * 
	 * @param id
	 * @return
	 */
	public Boolean isExistTicket(String ticketCode) {

		ChannelGoodsTicket cgt = channelGoodsTicketMapper.selectByTicketCode(ticketCode);

		if (StringUtil.isNotNull(cgt.getTicketCode()) && !"".equals(cgt.getTicketCode())) {
			return true;
		} else {
			return false;
		}

	}

	/**
	 * 店铺商品上下架调整列表查询
	 * 
	 * @param channelGoodsTicket
	 * @return
	 */

	public Paging getShopGoodsUpDownList(ChannelGoodsInfoVo model, PageHelper helper) {
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

	/**
	 * 调整单信息查询 
	 * @param id
	 * @return
	 */
	public ChannelGoodsTicketVo getChannelGoodsTicketVo(int id) {

		ChannelGoodsTicketVo cgtVo = channelGoodsTicketMapper.selectVoByPrimaryKey(id);
		return cgtVo;
	}

	// 批量删除调整单
	@Override
	public JsonResult deleteChannelGoodsTickets(String ids) {
		JsonResult jsonResult = new JsonResult();
		List<Integer> list = new ArrayList<Integer>();
		ChannelGoodsTicketExample channelGoodsTicketExample = new ChannelGoodsTicketExample();
		Criteria criteria = channelGoodsTicketExample.or();
		if (ids != null && StringUtils.isNotEmpty(ids)) {

			String[] str = ids.substring(0, ids.length() - 1).split(",");// 去除最后一个","
			for (int i = 0; i < str.length; i++) {
				int id = Integer.parseInt(str[i]);
				list.add(id);// 需要删除的id数组
			}
			criteria.andIdIn(list);
		}

		int record = channelGoodsTicketMapper.deleteByExample(channelGoodsTicketExample);

		if (list != null && record == list.size()) {
			jsonResult.setIsok(true);
			jsonResult.setMessage("批量删除成功！");
		} else {
			jsonResult.setIsok(false);
			jsonResult.setMessage("批量删除失败！");
		}
		return jsonResult;
	}

	/**
	 * 批量审核调整单，更改调整单状态 0：未审核，1：已经审核，2:已经移除
	 * 
	 * @param channelGoodsTicketExample
	 *            修改的条件
	 * @param record
	 *            修改成的内容
	 * @return
	 */
	public JsonResult updateChannelGoodsTicketStatus(String ids, String ticketStatus, String userName, String postageId) {
		JsonResult jsonResult = new JsonResult();
		String channelCode = "";
		String shopCode = "";
		// 存放需要立即执行的数据Id
		List<ChannelGoodsTicket> list = new ArrayList<ChannelGoodsTicket>();
		if (StringUtil.isNotEmpty(ids)) {
			String[] str = ids.split(",");// 去除最后一个","
			for (int i = 0; i < str.length; i++) {
				if (StringUtil.isNotEmpty(str[i])) {
					int id = Integer.parseInt(str[i]);
					ChannelGoodsTicket cgt = channelGoodsTicketMapper.selectByPrimaryKey(id);
					if (cgt != null) {
						// 判断是否是运费承担方式调整  只有买家“buyer”承担运费 将邮费模板放入 sellingInfo 字段中
						if (StringUtil.isNotEmpty(postageId)) {
							TicketInfo ticketInfo = new TicketInfo();
							ticketInfo.setSellingInfo(postageId);
							TicketInfoExample example = new TicketInfoExample();
							example.or().andTicketCodeEqualTo(cgt.getTicketCode());
							ticketInfoMapper.updateByExampleSelective(ticketInfo, example);
						}
						cgt.setTicketStatus(ticketStatus); // 修改调整单状态为已经审核
						channelGoodsTicketMapper.updateByPrimaryKey(cgt);
						channelCode = cgt.getChannelCode();
						shopCode = cgt.getShopCode();
						if (!"2".equals(ticketStatus) && cgt.getIsTiming() == 0) { // 需要立即执行
							list.add(cgt);
						}
					}
				}
			}
		}
		// 将调整单商品放入自己店铺MQ任务中，并且将调整单改成“正在同步”状态
		try {
			String queueName = Constants.QUEUE_FIX_ITEM + shopCode;
			if (StringUtil.isNotNullForList(list)) {
				List<String> ticketCodes = new ArrayList<String>();
				for (ChannelGoodsTicket goodsTicket : list) {
					String ticketCode = goodsTicket.getTicketCode();
					if (StringUtil.isEmpty(ticketCode)) {
						logger.error("channelCode="+ goodsTicket.getChannelCode() + ";shopCode=" 
								+ goodsTicket.getShopCode() + ";放入MQ任务的调整单号为空！");
						continue;
					}
					Integer ticketId = goodsTicket.getId();
					// 将调整单商品放入自己店铺MQ任务中，并且将调整单改成“正在同步”状态
					ChannelGoodsTicket record = new ChannelGoodsTicket();
					record.setNote(Constants.EXECUTING);
					ChannelGoodsTicketExample goodsTicketExample = new ChannelGoodsTicketExample();
					goodsTicketExample.or().andIdEqualTo(ticketId);
					channelGoodsTicketMapper.updateByExampleSelective(record, goodsTicketExample);
					Byte ticketType = goodsTicket.getTicketType();
					TicketInfoExample example = new TicketInfoExample();
					com.work.shop.bean.TicketInfoExample.Criteria criteria = example.or();
					criteria.andTicketCodeEqualTo(ticketCode);
					List<TicketInfo> ticketInfos = ticketInfoMapper.selectByExample(example);
					if (StringUtil.isNotNullForList(ticketInfos)) {
						int ticketSize = ticketInfos.size();
						int index = 1;
						for (TicketInfo info : ticketInfos) {
							QueueManagerVo managerVo = new QueueManagerVo();
							managerVo.setChannelCode(channelCode);
							managerVo.setOperUser(userName);
							managerVo.setShopCode(shopCode);
							managerVo.setOperateType(0);
							managerVo.setTicketType(ticketType);
//							managerVo.setPostageId(postageId);
//							// 邮费模板不为空的时候，使用卖点字段存储
							if (StringUtil.isNotEmpty(postageId)) {
								info.setSellingInfo(postageId);
							}
							info.setTicketCode(ticketCode);
							managerVo.setTicketInfo(info);
							managerVo.setTicketIndex(index);
							managerVo.setTicketSize(ticketSize);
							managerVo.setTicketId(ticketId);
							String msg = JSONObject.toJSONString(managerVo);
							onsProducerServiceImpl.sendMessage(queueName, msg);
							logger.info("调整单" + ticketCode + "-" + info.getGoodsSn() + "任务加入MQ队列");
							index ++;
						}
					}
					ticketCodes.add(ticketCode);
				}
			}
		} catch (Exception e) {
			jsonResult.setIsok(true);
			jsonResult.setMessage(e.getMessage());
			logger.error("将调整单商品放入自己店铺MQ任务中异常" + ExceptionStackTraceUtil.getExceptionTrace(e));
		}

		jsonResult.setIsok(true);
		jsonResult.setMessage("数据已经审核，等待同步。");
		return jsonResult;
	}

	// 生成调整单
	public boolean insertChannelGoodsTicket(List<ChannelGoodsTicket> list) {
		int record = 0;
		if (list != null && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				ChannelGoodsTicket cgt = channelGoodsTicketMapper.selectByTicketCode(list.get(i).getTicketCode());
				if (cgt != null && !"".equals(cgt.getTicketCode())) { // 已经存在的调整单做修改操作
					list.get(0).setId(cgt.getId());
					list.get(0).setTicketStatus(cgt.getTicketStatus());
					channelGoodsTicketMapper.updateByPrimaryKeySelective(list.get(0));
				} else {
					list.get(0).setTicketStatus("0"); // 未审核状态
					list.get(0).setAddTime(new Date());//生成调整单的时间
					channelGoodsTicketMapper.insert(list.get(0));
				}
				record++;
			}

		}
		if (list.size() == record) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 根据商品款号（6位码的goodsSn）查询商品
	 * 
	 * @param goodsSn
	 * @param shopCode
	 *            店铺code(就是这个系统的channelCode)
	 * @return list
	 **/
	public List<ChannelGoods> selectChannelGoodsList(String goodsSn, String shopCode) {

		ChannelGoodsExample channelGoodsExample = new ChannelGoodsExample();
		com.work.shop.bean.ChannelGoodsExample.Criteria criteria = channelGoodsExample.or();
		if (StringUtil.isNotNull(goodsSn)) {
			criteria.andGoodsSnEqualTo(goodsSn);
		}
		if (StringUtil.isNotNull(shopCode)) {
			criteria.andChannelCodeEqualTo(shopCode);
		}
		List<ChannelGoods> list = channelGoodsMapper.selectByExample(channelGoodsExample);
		return list;
	}
}

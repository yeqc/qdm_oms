package com.work.shop.timer.task;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimerTask;

import javax.annotation.Resource;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONObject;
import com.work.shop.bean.ChannelGoodsTicket;
import com.work.shop.bean.ChannelGoodsTicketExample;
import com.work.shop.bean.TicketInfo;
import com.work.shop.bean.TicketInfoExample;
import com.work.shop.dao.ChannelGoodsTicketMapper;
import com.work.shop.dao.TicketInfoMapper;
import com.work.shop.oms.utils.Constant;
import com.work.shop.util.Constants;
import com.work.shop.util.ExceptionStackTraceUtil;
import com.work.shop.util.StringUtil;
import com.work.shop.vo.ChannelGoodsTicketVo;
import com.work.shop.vo.QueueManagerVo;

public class TicketSynchronousTask extends TimerTask {

	private Logger logger = Logger.getLogger(TicketSynchronousTask.class);

	@Resource
	private ChannelGoodsTicketMapper channelGoodsTicketMapper;

	@Resource
	private TicketInfoMapper ticketInfoMapper;
	
//	@Resource
//	private OnsProducerService onsProducerService;

	@Override
	public void run() {
		logger.debug("调整单定时任务开始执行...");
		try {
			ChannelGoodsTicketExample example = new ChannelGoodsTicketExample();
			com.work.shop.bean.ChannelGoodsTicketExample.Criteria criteria = example.or();
			criteria.andIsTimingEqualTo((byte) 1); // 是否定时执行，0否1是
			criteria.andTicketStatusEqualTo("1"); // '单据状态 '1'已审核
			criteria.andIsSyncEqualTo((byte) 0); // '1已经同步 0未同步',
			criteria.andExecTimeLessThanOrEqualTo(new Date()); // 执行时间小于等于当前时间,
			List<String> list = new ArrayList<String>();
			list.add(Constants.EXECUTING);
			criteria.andNoteNotIn(list);
			List<ChannelGoodsTicketVo> dataList = channelGoodsTicketMapper.selectByExample(example);
			if (StringUtil.isNotNullForList(dataList)) {
				Map<String, List<ChannelGoodsTicketVo>> dataMap = distinguishData(dataList);
				for (Map.Entry<String, List<ChannelGoodsTicketVo>> entry : dataMap.entrySet()) {
					List<ChannelGoodsTicketVo> ticketVos = entry.getValue();
					sendToMQ(ticketVos);
				}
			}
		} catch (Exception e) {
			logger.info(e.getMessage());
		}
		logger.debug("调整单定时任务执行结束...");
	}

	/**
	 * 将调整单数据按店铺code分组
	 * 
	 * @param dataList
	 * @return
	 */
	private Map<String, List<ChannelGoodsTicketVo>> distinguishData(List<ChannelGoodsTicketVo> dataList) {
		Map<String, List<ChannelGoodsTicketVo>> dataMap = new HashMap<String, List<ChannelGoodsTicketVo>>();
		List<ChannelGoodsTicketVo> list = null;
		for (ChannelGoodsTicketVo ticketVo : dataList) {
			String shopCode = ticketVo.getShopCode();
			if (dataMap.containsKey(shopCode)) {
				list = dataMap.get(shopCode);
			} else {
				list = new ArrayList<ChannelGoodsTicketVo>();
			}
			list.add(ticketVo);
			dataMap.put(shopCode, list);
		}
		return dataMap;
	}

	private void sendToMQ(List<ChannelGoodsTicketVo> ticketVos) {
		List<Integer> ticketIds = new ArrayList<Integer>();
		String channelCode = "";
		String shopCode = "";
		try {
			List<String> ticketCodes = new ArrayList<String>();
			for (ChannelGoodsTicketVo channelGoodsTicketVo : ticketVos) {
				channelCode = channelGoodsTicketVo.getChannelCode();
				shopCode = channelGoodsTicketVo.getShopCode();
				ticketIds.add(channelGoodsTicketVo.getId());
				String queueName = Constants.QUEUE_FIX_ITEM + shopCode;
				String ticketCode = channelGoodsTicketVo.getTicketCode();
				if (StringUtil.isEmpty(ticketCode)) {
					logger.error("channelCode="+ channelGoodsTicketVo.getChannelCode() + ";shopCode=" 
							+ channelGoodsTicketVo.getShopCode() + ";放入MQ任务的调整单号为空！");
					continue;
				}
				Integer ticketId = channelGoodsTicketVo.getId();
				// 将调整单商品放入自己店铺MQ任务中，并且将调整单改成“正在同步”状态
				ChannelGoodsTicket record = new ChannelGoodsTicket();
				record.setNote(Constants.EXECUTING);
				ChannelGoodsTicketExample goodsTicketExample = new ChannelGoodsTicketExample();
				goodsTicketExample.or().andIdEqualTo(ticketId);
				channelGoodsTicketMapper.updateByExampleSelective(record, goodsTicketExample);
				Byte ticketType = channelGoodsTicketVo.getTicketType();
				TicketInfoExample example = new TicketInfoExample();
				com.work.shop.bean.TicketInfoExample.Criteria criteria = example.or();
				criteria.andTicketCodeEqualTo(ticketCode);
				List<TicketInfo> ticketInfos = ticketInfoMapper.selectByExample(example);
				if (StringUtil.isNotNullForList(ticketInfos)) {
					int ticketSize = ticketInfos.size();
					int index = 1;
					for (TicketInfo info : ticketInfos) {
						QueueManagerVo managerVo = new QueueManagerVo();
						managerVo.setOperUser(Constant.OS_STRING_SYSTEM);
						managerVo.setChannelCode(channelCode);
						managerVo.setShopCode(shopCode);
						managerVo.setOperateType(0);
						managerVo.setTicketType(ticketType);
						info.setTicketCode(ticketCode);
						managerVo.setTicketInfo(info);
						managerVo.setTicketIndex(index);
						managerVo.setTicketSize(ticketSize);
						managerVo.setTicketId(ticketId);
						String msg = JSONObject.toJSONString(managerVo);
//						onsProducerService.sendMessage(MqConfig.getShopCID(queueName), msg);
						logger.info("调整单" + ticketCode + "-" + info.getGoodsSn() + "生成宝贝详情任务加入MQ队列");
						index ++;
					}
				}
				ticketCodes.add(ticketCode);
			}
			/*// 
			ChannelGoodsTicket record = new ChannelGoodsTicket();
			record.setNote(Constants.EXECUTING);
			ChannelGoodsTicketExample example = new ChannelGoodsTicketExample();
			example.or().andTicketCodeIn(ticketCodes);
			channelGoodsTicketMapper.updateByExampleSelective(record, example);*/
		} catch (Exception e) {
			logger.error("执行异常: (店铺编码:" + shopCode + " , 调整单Ids ： " + ticketIds.toString() + ") "
					+"将调整单商品放入自己店铺MQ任务中异常" + ExceptionStackTraceUtil.getExceptionTrace(e));
		}
	}

}

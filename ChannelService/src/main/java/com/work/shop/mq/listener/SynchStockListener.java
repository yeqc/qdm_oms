package com.work.shop.mq.listener;

import java.util.List;

import javax.annotation.Resource;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.work.shop.api.bean.ApiResultVO;
import com.work.shop.api.bean.ItemUpdate;
import com.work.shop.api.service.ChannelApiService;
import com.work.shop.bean.ChannelShop;
import com.work.shop.bean.ChannelShopExample;
import com.work.shop.dao.ChannelShopMapper;
import com.work.shop.oms.utils.StringUtil;
import com.work.shop.util.ExceptionStackTraceUtil;
import com.work.shop.vo.SyncStockParam;

@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class SynchStockListener implements MessageListener {

	private final static Logger logger = Logger.getLogger(SynchStockListener.class);

	@Resource(name = "apiService")
	private ChannelApiService apiService;
	@Resource
	private ChannelShopMapper channelShopMapper;

	@SuppressWarnings("unused")
	public void onMessage(Message message) {
		String queueName = null;
		try {
			logger.info("get a message:" + ((TextMessage) message).getText());
			if (message == null) {
				return;
			}
			String text = ((TextMessage) message).getText().toString();
			SyncStockParam stockParam = (SyncStockParam) JSONObject.parseObject(text, SyncStockParam.class);
			if (stockParam != null) {
				String shopCode = stockParam.getShopCode();
				if (StringUtil.isTrimEmpty(shopCode)) {
					logger.error("shopCode is empty");
					return ;
				}
				ChannelShopExample example = new ChannelShopExample();
				example.or().andShopCodeEqualTo(shopCode);
				List<ChannelShop> shops = channelShopMapper.selectByExample(example);
				if (StringUtil.isListNull(shops)) {
					logger.error("店铺[" + shopCode + "]不存在！");
					return ;
				}
				queueName = stockParam.getShopCode();
				logger.info("MQ队列名称：" + queueName +",get a message:" + ((TextMessage) message).getText());
				ItemUpdate itemUpdate = new ItemUpdate();
				itemUpdate.setSkuSn(stockParam.getSku());
				itemUpdate.setStockCount(stockParam.getStockCount());
				itemUpdate.setShopCode(shopCode);
				itemUpdate.setChannelCode(shops.get(0).getChannelCode());
				ApiResultVO resultVO = apiService.updateItemStock(itemUpdate);
				logger.info("库存同步结果resultVO：" + JSON.toJSONString(resultVO));
			}
		} catch (Throwable e) {
			// 处理异常保证后续MQ任务继续执行！
			logger.error("MQ队列名称：" + queueName + "执行MQ任务异常" + ExceptionStackTraceUtil.getExceptionTrace(e));
		}
	}

}

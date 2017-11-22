package com.work.shop.mq.listener;

import javax.annotation.Resource;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.work.shop.api.service.ChannelApiService;
import com.work.shop.util.Constants;
import com.work.shop.util.ExceptionStackTraceUtil;
import com.work.shop.vo.QueueManagerVo;

@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class SynchItemListener implements MessageListener {

	private final static Logger logger = Logger.getLogger(SynchItemListener.class);

	@Resource(name = "apiService")
	private ChannelApiService apiService;

	@SuppressWarnings("unused")
	public void onMessage(Message message) {
		String queueName = null;
		try {
			logger.info("get a message:" + ((TextMessage) message).getText());
			if (message == null) {
				return;
			}
			String text = ((TextMessage) message).getText().toString();
			QueueManagerVo managerVo = (QueueManagerVo) JSONObject.parseObject(text, QueueManagerVo.class);
			if (managerVo != null) {
				String shopCode = managerVo.getShopCode();
				queueName = Constants.QUEUE_FIX_ITEM + shopCode;
				logger.debug("MQ队列名称：" + queueName +",get a message:" + ((TextMessage) message).getText());
				apiService.ticketDisposal(managerVo);
			}
		} catch (Throwable e) {
			// 处理异常保证后续MQ任务继续执行！
			logger.error("MQ队列名称：" + queueName + "执行MQ任务异常" + ExceptionStackTraceUtil.getExceptionTrace(e));
		}
	}

}

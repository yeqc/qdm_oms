package com.work.shop.mq;

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
import com.work.shop.service.ChannelTemplateService;
import com.work.shop.util.Constants;
import com.work.shop.util.ExceptionStackTraceUtil;
import com.work.shop.vo.QueueManagerVo;

@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class MqTaskReceiver implements MessageListener {

	private final static Logger logger = Logger.getLogger(MqTaskReceiver.class);

	@Resource(name = "apiService")
	private ChannelApiService apiService;

	@Resource(name = "channelTemplateService")
	private ChannelTemplateService channelTemplateService;

	private String shopCode;

	public String getShopCode() {
		return shopCode;
	}

	public void setShopCode(String shopCode) {
		this.shopCode = shopCode;
	}

	@SuppressWarnings("unused")
	public void onMessage(Message message) {
		String channelCode = null;
		String shopCode = null;
		String queueName = null;
		try {
			logger.info("get a message:" + ((TextMessage) message).getText());
			if (message == null) {
				return;
			}
			String text = ((TextMessage) message).getText().toString();
			QueueManagerVo managerVo = (QueueManagerVo) JSONObject.parseObject(text, QueueManagerVo.class);
			if (managerVo != null) {
				channelCode = managerVo.getChannelCode();
				shopCode = managerVo.getShopCode();
				queueName = Constants.QUEUE_FIX_ITEM + shopCode;
				logger.debug("MQ队列名称：" + queueName +",get a message:" + ((TextMessage) message).getText());
				int operateType = managerVo.getOperateType();
				if (operateType == 0) {// 0 ：调整单操作
					apiService.ticketDisposal(managerVo);
				}
				if (operateType == 1) {// 1 ：详情生成
					channelTemplateService.createTemplate(managerVo);
				}
			}
		} catch (Throwable e) {
			// 处理异常保证后续MQ任务继续执行！
			logger.error("MQ队列名称：" + queueName + "执行MQ任务异常" + ExceptionStackTraceUtil.getExceptionTrace(e));
		}
	}

}

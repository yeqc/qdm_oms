package com.work.shop.mq.mtsbw.producer;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import com.work.shop.api.bean.ReturnInfo;
import com.work.shop.oms.mq.bean.TextMessageCreator;
import com.work.shop.oms.utils.Constant;


@Service
public class OnsProducerServiceImpl{

	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Resource(name = "putQueueJmsTemplate")
	private JmsTemplate putQueueJmsTemplate;
	
	public ReturnInfo<String> sendMessage(String queueName, final String message) {
		logger.info("quereName:" + queueName + ";message=" + message);
		String temp = "";
		logger.info(temp = ("业务监控接口.队列名称 : " + queueName + ",message:" + message));
		ReturnInfo<String> info = new ReturnInfo<String>(Constant.OS_NO);
		try {
			putQueueJmsTemplate.send(queueName, new TextMessageCreator(message));
			logger.debug(temp + ".发送成功");
			info.setMessage("success");
			info.setIsOk(Constant.OS_YES);
		} catch (Exception e) {
			logger.error("quereName:" + queueName + "写入MQ异常：" + e.getMessage(), e);
			info.setMessage("quereName:" + queueName + "写入MQ异常：" + e.getMessage());
		}
		return info;
	}
}

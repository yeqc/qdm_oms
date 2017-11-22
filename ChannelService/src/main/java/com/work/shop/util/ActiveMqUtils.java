package com.work.shop.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.log4j.Logger;


public class ActiveMqUtils {

	private Logger logger = Logger.getLogger(ActiveMqUtils.class);

//	private static final int MESSAGE_TIME_TO_LIVE = 1 * 3600 * 1000;
	private static final int MESSAGE_TIME_TO_LIVE = 0; // 0 表示永不失效
	
	private String brokerURL;

	public String getBrokerURL() {
		return brokerURL;
	}

	public void setBrokerURL(String brokerURL) {
		this.brokerURL = brokerURL;
		try {
			init();
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}

	private ConnectionFactory connectionFactory;

	private Connection connection;

	private Session session;

	private boolean isInit = false;

	private Map<String, MessageProducer> messageProducers = new HashMap<String, MessageProducer>();

	private static Map<MessageConsumer, Session> mcs = new HashMap<MessageConsumer, Session>();
	
	private static Map<List<MessageConsumer>, Session> mcss = new HashMap<List<MessageConsumer>, Session>();


	public void init() throws JMSException {
		if (!isInit) {
			isInit = true;
			connectionFactory = new ActiveMQConnectionFactory(brokerURL);
			connection = connectionFactory.createConnection();
			connection.start();
			session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		}
	}

	public MessageProducer getMessageProducer(String queueName) throws JMSException {
		MessageProducer producer = messageProducers.get(queueName);
		if (producer != null)
			return producer;
		producer = session.createProducer(session.createQueue(queueName));
		producer.setTimeToLive(MESSAGE_TIME_TO_LIVE);
		producer.setDeliveryMode(1);
		messageProducers.put(queueName, producer);
		return producer;
	}

	public MessageProducer getTopicMessageProducer(String topicName) throws JMSException {
		MessageProducer producer = messageProducers.get(topicName);
		if (producer != null)
			return producer;
		producer = session.createProducer(session.createTopic(topicName));
		producer.setTimeToLive(MESSAGE_TIME_TO_LIVE);
		producer.setDeliveryMode(1);
		messageProducers.put(topicName, producer);
		return producer;
	}

	public void registTopicListener(String topicName, MessageListener messageListener) throws JMSException {
		MessageConsumer messageConsumer = createTopicMessageConsumer(topicName);
		messageConsumer.setMessageListener(messageListener);
	}
	
	/**
	 * 
	 * @param queueName
	 * @param messageListener
	 * @param queueNum
	 * @throws JMSException
	 */
	public void registQueueListener(String queueName, MessageListener messageListener, int queueNum) throws JMSException {
//		if (queueNum == 0 && queueNum < 0) {
//			queueNum = 1;
//		}
		if (queueNum == 0) {
			return ;
		}
		List<MessageConsumer> consumers = createMessageConsumers(queueName, queueNum);
		for (MessageConsumer consumer :consumers) {
			consumer.setMessageListener(messageListener);
		}
	}

	public MessageConsumer createMessageConsumer(String queueName) throws JMSException {
		Session s = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		MessageConsumer mc = s.createConsumer(session.createQueue(queueName));
		mcs.put(mc, s);
		return mc;
	}
	
	public List<MessageConsumer> createMessageConsumers(String queueName, int queueNum) throws JMSException {
		List<MessageConsumer> consumers = new ArrayList<MessageConsumer>();
		for (int n = 0 ; n < queueNum ; n++) {
			Session s = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			MessageConsumer mc = s.createConsumer(session.createQueue(queueName));
			consumers.add(mc);
			mcs.put(mc, s);
		}
		return consumers;
	}

	public MessageConsumer createTopicMessageConsumer(String topicName) throws JMSException {
		Session s = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		MessageConsumer mc = s.createConsumer(session.createTopic(topicName));
		mcs.put(mc, s);
		return mc;
	}

	public void sendQueueMessage(String queueName, Message message) throws JMSException {
		getMessageProducer(queueName).send(message);
	}

	public static void closeSession(MessageConsumer mc) {
		try {
			mcs.get(mc).close();
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}

	public Session getSession() {
		return session;
	}

	public static Map<String, Object> simpleBeanToMap(Object obj) throws IllegalArgumentException,
			IllegalAccessException {
		Map<String, Object> item = new HashMap<String, Object>();
		Field[] fields = obj.getClass().getDeclaredFields();
		for (int i = 0; i < fields.length; i++) {
			boolean ss = fields[i].isAccessible();
			fields[i].setAccessible(true);
			item.put(fields[i].getName(), fields[i].get(obj));
			fields[i].setAccessible(ss);
		}
		return item;
	}

	public static <T> T simpleMapToBean(Map<String, Object> map, Class<T> clazz) throws InstantiationException,
			IllegalAccessException {
		Set<String> keyset = map.keySet();
		T t = clazz.newInstance();
		for (Iterator<String> iterator = keyset.iterator(); iterator.hasNext();) {
			String string = iterator.next();
			try {
				Field field = clazz.getDeclaredField(string);
				if (field != null) {
					boolean f = field.isAccessible();
					field.setAccessible(true);
					field.set(t, map.get(string));
					field.setAccessible(f);
				}
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (NoSuchFieldException e) {
				e.printStackTrace();
			}
		}
		return t;
	}


	public MapMessage createMapMessage() throws JMSException {
		return session.createMapMessage();
	}
	
	public Message createMessage() throws JMSException {
		return session.createMessage();
	}

	public void sendMessage(String queueName, String message) throws JMSException {
		logger.info("send a message:(queueName:" + queueName + ", message:" + message + ")");
		MessageProducer producer = getMessageProducer(queueName);
		Session session = getSession();
		TextMessage textMessage = session.createTextMessage(message);
		producer.send(textMessage);

	}

}

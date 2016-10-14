package com.renke.core.common;

import java.io.Serializable;

import javax.jms.Destination;

import org.apache.activemq.command.ActiveMQQueue;
import org.apache.activemq.command.ActiveMQTopic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.util.Assert;

import com.renke.core.utils.SpringUtils;

/**
 * title: MessageSender.java 
 * description
 *
 * @author rplees
 * @email rplees.i.ly@gmail.com
 * @version 1.0  
 * @created 2015年8月12日 下午4:41:10
 */
public class MessageSender {
	public static final Logger logger = LoggerFactory.getLogger(MessageSender.class);
	
	public static void sendMessageByTopic(Serializable s) {
		Destination destination = SpringUtils.getBean(ActiveMQTopic.class);
		Assert.notNull(destination, "找不到Topic的实现.");
		sendMessage(destination , s);
	}
	
	public static void sendMessageByQueue(Serializable s) {
		Destination destination = SpringUtils.getBean(ActiveMQQueue.class);
		Assert.notNull(destination, "找不到Queue的实现.");
		sendMessage(destination , s);
	}
	
	public static void sendMessageByTopic(String s) {
		Destination destination = SpringUtils.getBean(ActiveMQTopic.class);
		Assert.notNull(destination, "找不到Topic的实现.");
		sendMessage(destination , s);
	}
	
	public static void sendMessageByQueue(String s) {
		Destination destination = SpringUtils.getBean(ActiveMQQueue.class);
		Assert.notNull(destination, "找不到Queue的实现.");
		sendMessage(destination , s);
	}
	
	public static JmsTemplate getJmsTemplate() {
		JmsTemplate jmsTemplate = SpringUtils.getBean(JmsTemplate.class);
		if(jmsTemplate == null) {
			return null;
		}
		
		return jmsTemplate;
	}
	
	public static void sendMessage(Destination destination, Serializable s) {
		JmsTemplate jmsTemplate = getJmsTemplate();
		if(jmsTemplate == null) {
			logger.error("找不到JmsTemplate的实现，将不发送MQ:{}", s);
			return;
		}
		
		jmsTemplate.convertAndSend(destination, s);
	}
}
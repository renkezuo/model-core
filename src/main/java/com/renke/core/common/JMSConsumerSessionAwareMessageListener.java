package com.renke.core.common;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.listener.SessionAwareMessageListener;

import com.renke.core.utils.SpringUtils;

public class JMSConsumerSessionAwareMessageListener implements SessionAwareMessageListener<Message> {
	public final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Override
	public void onMessage(Message message, Session session) throws JMSException {
		logger.info("SessionAware接收到一个消息.");
		
		if(message instanceof TextMessage) {
			logger.info("消息是：" + ((TextMessage) message).getText());
			
			TextMessage tm = ((TextMessage) message);
			String text = tm.getText();
			if(StringUtils.startsWith(text, "_cmd_")) {
				SpringUtils.publishEvent(new CommonEvent(text));
			}
		} else if(message instanceof ObjectMessage) {
			ObjectMessage om = (ObjectMessage) message;
			
			if(om.getObject() instanceof CommonEvent) {//程序
				CommonEvent ce = (CommonEvent) om.getObject();
				logger.info("消息是：--CommonEvent--cmd:{}, data:{}", ce.getCmd(), ce.getData());
				
				ce.setMessage(message);
				ce.setSession(session);
				
				SpringUtils.publishEvent(ce);
			} else {
				logger.info("消息是：--ObjectMessage--" + message);
			}
		}
	}

}
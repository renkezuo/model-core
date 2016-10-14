package com.renke.core.common;

import javax.jms.Message;
import javax.jms.Session;

import org.springframework.context.ApplicationEvent;

public class CommonEvent extends ApplicationEvent {
	/**描述*/  
	private static final long serialVersionUID = 5630833815851069438L;
	String cmd;
	Object data;
	
	Message message;
	Session session;
	
	public CommonEvent(String cmd) {
		this(cmd, null);
	}
	
	public CommonEvent(String cmd, Object data) {
		super(cmd);
		
		this.cmd = cmd;
		this.data = data;
	}
	
	public CommonEvent(String cmd, Object data, Message message, Session session) {
		super(cmd);
		
		this.cmd = cmd;
		this.data = data;
		
		this.message = message;
		this.session = session;
	}

	public String getCmd() {
		return cmd;
	}

	public void setCmd(String cmd) {
		this.cmd = cmd;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public Message getMessage() {
		return message;
	}

	public void setMessage(Message message) {
		this.message = message;
	}

	public Session getSession() {
		return session;
	}

	public void setSession(Session session) {
		this.session = session;
	}
}
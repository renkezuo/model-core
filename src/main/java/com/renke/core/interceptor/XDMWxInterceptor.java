package com.renke.core.interceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;


/**
 * title: XDMApiInterceptor.java 
 * 想De美拦截器，统计处理超时的请求
 * @email rplees.i.ly@gmail.com
 * @version 1.0  
 * @created 2015年9月11日 下午3:59:24
 */
public class XDMWxInterceptor implements HandlerInterceptor {
	static final Logger logger = LoggerFactory.getLogger(XDMWxInterceptor.class);
	
	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		logger.info(" before  doSth ");
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
	
	}

	@Override
	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
	}

}

package com.renke.core.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;


/***
 * 安全拦截
 * session
 * @author renke.zuo@foxmail.com
 * @time 2016-12-05 13:38:47
 */
public class SecurityInterceptor implements HandlerInterceptor {
	static final Logger logger = LoggerFactory.getLogger(SecurityInterceptor.class);
	
	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		/***
		 * debug模式
		 */
		
		/**
		 * 检查ticket
		 */
		
		/**
		 * 检查session
		 */
		logger.info("请求URI:{}",request.getRequestURI());
		/**
		 * 登陆功能直接过，也可以在Interceptor标签中设置
		 */
		if(request.getRequestURI().indexOf("login.do") != -1){
			return true;
		}
		
		
		//建议，将session替换成其他内容，因为session是基于cookie
//		HttpSession session = request.getSession(false);
//		if(session == null){
//			//@FIXME response返回异常
//			response.getOutputStream().write("请先登陆！".getBytes());
//			return false;
//		}else{
//			
//		}

		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		response.getOutputStream().write("postHandle".getBytes());
		logger.info("postHandle");
	
	}

	@Override
	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		response.getOutputStream().write("afterCompletion".getBytes());
		logger.info("afterCompletion");
	}
	
}

package com.renke.core.exception;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ExceptionHandlerController {
	public static final Logger logger = LoggerFactory.getLogger(ExceptionHandlerController.class);
	
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(value = Exception.class)
	public String defaultExceptionHandler(HttpServletRequest request,Exception e){
		request.setAttribute("e","系统异常");
		e.printStackTrace();
		logger.error("系统异常：{}",e.getMessage());
		return "500";
	}
	
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(value = DataAccessException.class)
	public String sqlExceptionHandler(HttpServletRequest request,Exception e){
		e.printStackTrace();
		request.setAttribute("errorMsg", "数据库访问异常");
		logger.error("数据库访问异常：{}",e.getMessage());
		return "500";
	}
	
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(value = IllegalArgumentException.class)
	public String argumentExceptionHandler(HttpServletRequest request,Exception e){
		e.printStackTrace();
		request.setAttribute("errorMsg", "参数异常");
		logger.error("参数异常：{}",e.getMessage());
		return "500";
	}
	
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(value = NullPointerException.class)
	public String nullExceptionHandler(HttpServletRequest request,Exception e){
		e.printStackTrace();
		request.setAttribute("errorMsg", "空指针异常");
		logger.error("空指针异常：{}",e.getMessage());
		return "500";
	}
}

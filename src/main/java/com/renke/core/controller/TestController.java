package com.renke.core.controller;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.renke.core.service.TestService;

@Controller
@RequestMapping("/test")
public class TestController {
	private final static Logger logger = LoggerFactory.getLogger(ModelController.class);
	

	@Resource
	private TestService testService;

	@RequestMapping(value = "/mysql")
	public String mysqlTest(){
		logger.info("mysqlTest--->");
		testService.transactionMysql();
		return "index";
	}

	@RequestMapping(value = "/sqlite")
	public String sqliteTest(){
		logger.info("sqliteTest--->");
		testService.transactionSqlite();
		return "index";
	}
}

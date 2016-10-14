package com.renke.core.controller;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.renke.core.entity.Log;
import com.renke.core.service.ModelService;

@Controller
@RequestMapping("/atest")
public class ModelController {
	@Resource
	private ModelService modelService;
	
	@RequestMapping(value = "/hello")
	public String helloWorld(){
		Log log = new Log();
		log.setId(102);
		log.setIp("1231231");
		log.setPort(1080);
		modelService.insertLog(log);
		log.setIp("33333");
		log.setPort(1080);
		modelService.updateLog(log);
		return "index";
	}
}

package com.renke.core.controller;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.renke.core.entity.Model;
import com.renke.core.service.ModelService;
import com.renke.core.tools.ToStringTool;

@Controller
@RequestMapping("/atest")
public class ModelController {
	@Resource
	private ModelService modelService;
	
	@RequestMapping(value = "/hello")
	public String helloWorld(){
		Model log = new Model();
//		log.setId(109);
		log.setIp("1231231");
		log.setPort(1080);
		
		modelService.insertModel(log);
		System.out.println(ToStringTool.entityToString(log));
		log.setIp("33333");
		log.setPort(1080);
		modelService.updateModel(log);

		log = new Model();
		log.setIp("33333");
		System.out.println(ToStringTool.listToString(modelService.selectList(log)));
		log = modelService.selectModel(log);
		System.out.println(ToStringTool.entityToString(log));
		log = modelService.selectModelById(100);
		System.out.println(ToStringTool.entityToString(log));
		return "index";
	}
}

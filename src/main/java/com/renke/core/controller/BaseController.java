package com.renke.core.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

@Controller
@RequestMapping("/base")
public class BaseController {
	
	@RequestMapping("/{table}/insert")
	public @ResponseBody JsonElement insert(@PathVariable("table") String tableName
				,HttpServletRequest request,HttpServletResponse response){
		JsonObject jo = new JsonObject();
		jo.addProperty("msg", "base insert");
		return jo;
	}

	@RequestMapping("/{table}/update")
	public @ResponseBody JsonElement update(@PathVariable("table") String tableName
			,HttpServletRequest request,HttpServletResponse response){
		JsonObject jo = new JsonObject();
		jo.addProperty("msg", "base update");
		return jo;
	}

	@RequestMapping("/{table}/delete")
	public @ResponseBody JsonElement delete(@PathVariable("table") String tableName
			,HttpServletRequest request,HttpServletResponse response){
		JsonObject jo = new JsonObject();
		jo.addProperty("msg", "base delete");
		return jo;
	}

	@RequestMapping("/{table}/select")
	public @ResponseBody JsonElement select(@PathVariable("table") String tableName
			,HttpServletRequest request,HttpServletResponse response){
		JsonObject jo = new JsonObject();
		jo.addProperty("msg", "base select");
		return jo;
	}
}

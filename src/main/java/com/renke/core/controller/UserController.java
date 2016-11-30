package com.renke.core.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.renke.core.entity.Param;
import com.renke.core.enums.Type;
import com.renke.core.tools.HTTPTool;
import com.renke.core.tools.ToStringTool;

@Controller
@RequestMapping("/user")
public class UserController{
	@RequestMapping(value="/insert")
	public @ResponseBody JsonElement insert(HttpServletRequest request){
		String contentType = request.getContentType();
		List<Param> result = new ArrayList<Param>();
		try {
			HTTPTool.parsePost(request.getInputStream(), contentType,result);
			System.out.println(ToStringTool.listToString(result, true));
			for(Param param:result){
				String value = param.tmpPath;
				if(param.type != Type.FILE){
					value = new String(param.bytes);
				}
				System.out.println(param.key+":"+value);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		JsonObject jo = new JsonObject();
		jo.addProperty("msg", "user insert");
		return jo;
	}
}

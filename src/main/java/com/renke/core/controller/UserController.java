package com.renke.core.controller;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.renke.core.entity.Param;
import com.renke.core.tools.RequestTool;

@Controller
@RequestMapping("/user")
public class UserController{
	
	@RequestMapping(value="/login")
	public @ResponseBody JsonElement login(HttpServletRequest request){
		
		
		return null;
	}
	
	
	@RequestMapping(value="/insert")
	public @ResponseBody JsonElement insert(HttpServletRequest request){
//		Test test = RequestTool.assembleRequest(Test.class, request);
//		System.out.println(test);
		Map<String,List<Param>> map = RequestTool.assembleRequest(request);
		
//		Iterator<Entry<String,List<Param>>> it = map.entrySet().iterator();
//		while(it.hasNext()){
//			Entry<String,List<Param>> entry = it.next();
//			if(entry.getValue()!=null && entry.getValue().size()>0){
//				System.out.println(entry.getKey()+":"+new String(entry.getValue().get(0).bytes));
//			}
//		}
		
		
//		String contentType = request.getContentType();
//		List<Param> result = new ArrayList<Param>();
//		try {
//			HTTPTool.parsePost(request.getInputStream(), contentType,result);
//			System.out.println(ToStringTool.listToString(result, true));
//			for(Param param:result){
//				String value = param.tmpPath;
//				if(param.type != Type.FILE){
//					value = new String(param.bytes);
//				}
//				System.out.println(param.key+":"+value);
//			}
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
		JsonObject jo = new JsonObject();
		jo.addProperty("msg", "user insert");
		return jo;
	}
}

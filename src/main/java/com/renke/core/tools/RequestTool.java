package com.renke.core.tools;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import com.renke.core.common.SortField;
import com.renke.core.common.SortParam;
import com.renke.core.entity.Param;
import com.renke.core.entity.RequestFile;
import com.renke.core.entity.Test;

public class RequestTool {
	public static Map<String,List<Param>> assembleRequest(HttpServletRequest request){
		String contentType = request.getContentType();
		List<Param> result = new ArrayList<Param>();
		Map<String,List<Param>> map = new HashMap<String,List<Param>>();
		try {
			//此处添加一个接口，可以控制request请求，比如file的大小等
			HTTPTool.parsePost(request.getInputStream(),contentType,result);
			for(Param param:result){
				List<Param> list = map.get(param.key); 
				if(list==null){
					list = new ArrayList<Param>();
				}
				map.put(param.key, list);
				list.add(param);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		result = null;
		return Collections.unmodifiableMap(map);
	}
	
	
	public static <Entity> Entity assembleRequest(Class<Entity> clazz,HttpServletRequest request){
		Entity entity = null;
		String contentType = request.getContentType();
		List<Param> result = new ArrayList<Param>();
		try {
			//此处添加一个接口，可以控制request请求，比如file的大小等
			HTTPTool.parsePost(request.getInputStream(),contentType,result);
			
			//根据key排序result
			entity = clazz.newInstance();
			result.sort(new SortParam());
			
			//根据名称排序fields
			Field[] fields = clazz.getDeclaredFields();
			List<Field> fieldList = new ArrayList<Field>();
			for(Field field : fields){
				String fieldName = field.getName();
				if(CheckTool.isEquals("serialVersionUID", fieldName)){
					continue;
				}
				fieldList.add(field);
			}
			fieldList.sort(new SortField());
			
			for(Field field : fieldList){
				String fieldName = field.getName();
				field.setAccessible(true);
				Class<?> type = field.getType();
//				String typeName = type.getTypeName().toUpperCase();
				int index = 0;
				List<Object> pArray = new ArrayList<Object>();
				while(index < result.size()){
					Param param = result.get(index);
					if(CheckTool.isEquals(fieldName, param.key)){
						result.remove(index);
						index -- ;
						Object obj = getObject(type,param);
						if(obj!=null)
							pArray.add(obj);
					}
					index ++;
				}
				if(type.isArray() && pArray.size()>0){
//					field.set(entity, pArray.toArray(new Object[pArray.size()]));
				}else if(pArray.size()>0){
					field.set(entity, pArray.get(0));
				}
				// help GC
				pArray = null;
			}
		} catch (ReflectiveOperationException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return entity;
	}
	
	public static Object getObject(Class<?> type,Param param){
		String typeName = type.getTypeName().toUpperCase();
		Object obj = null;
		if(typeName.indexOf("REQUESTFILE") != -1){
			if(CheckTool.isNotBlank(param.tmpPath)){
				RequestFile rf = new RequestFile();
				rf.file = new File(param.tmpPath);
				rf.fileName = param.fileName;
				rf.contentType = param.contentType;
				obj = rf;
			}
		}else if(typeName.indexOf("STRING") != -1){
			obj = new String(param.bytes);
		}else if(typeName.indexOf("DOUBLE") != -1){
			if(param.bytes.length>0){
				obj = Double.parseDouble(new String(param.bytes));
			}
		}else if(typeName.indexOf("FLOAT") != -1){
			if(param.bytes.length>0){
				obj = Float.parseFloat(new String(param.bytes));
			}
		}else if(typeName.indexOf("LONG") != -1){
			if(param.bytes.length>0){
				obj = Long.parseLong(new String(param.bytes));
			}
		}else if(typeName.indexOf("INT") != -1){
			if(param.bytes.length>0){
				obj = Integer.parseInt(new String(param.bytes));
			}
		}else if(typeName.indexOf("SHORT") != -1){
			if(param.bytes.length>0){
				obj = Short.parseShort(new String(param.bytes));
			}
		}else if(typeName.indexOf("CHAR") != -1 ){
			if(param.bytes.length>0){
				obj = new String(param.bytes).charAt(0);
			}
		}else if(typeName.indexOf("BOOLEAN") != -1 ){
			if(param.bytes.length>0){
				obj = Boolean.parseBoolean(new String(param.bytes));
			}
		}
		return obj;
	}
	
	
	public static void main(String[] args) {
		
		Field[] fields = Test.class.getDeclaredFields();
		
		for(Field field : fields){
			System.out.println(field.getType().getTypeName());
		}
		System.out.println(Test.class.getInterfaces());
		
		
		try {
			Class<?> c = Class.forName("int");
			System.out.println(c.getTypeName());
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}

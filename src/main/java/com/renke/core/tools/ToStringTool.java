package com.renke.core.tools;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ToStringTool {
	public final static String mapToString(Map<String,Object> map){
		StringBuilder msg = new StringBuilder("{");
		Iterator<String> it = map.keySet().iterator();
		while(it.hasNext()){
			String key = it.next();
			msg.append(key).append(":").append(objectToString(map.get(key))).append(",");
		}
		msg.deleteCharAt(msg.length()-1).append("}");
		return msg.toString();
	}
	
	public final static <T> String arrayToString(T[] ts){
		StringBuilder msg = new StringBuilder("{");
		for(int i=0;i<ts.length;i++){
			msg.append(objectToString(ts[i])).append(",");
		}
		msg.deleteCharAt(msg.length()-1).append("}");
		return msg.toString();
	}
	
	public final static String listToString(List<?> ls){
		return arrayToString(ls.toArray());
	}
	
	public final static String objectToString(Object obj){
		if(obj == null){
			return "null";
		}else if(obj instanceof Serializable){
			return entityToString((Serializable)obj);
		}else{
			return obj.toString();
		}
	}
	
	public final static String entityToString(Serializable entity){
		StringBuilder msg = new StringBuilder("{");
		Class<?> clazz = entity.getClass();
		Field[] fields = clazz.getDeclaredFields();
		for(Field field : fields){
			msg.append(field.getName()).append(":");
			try {
				field.setAccessible(true);
				msg.append(field.get(entity));
			} catch (IllegalArgumentException | IllegalAccessException e) {
				// TODO Auto-generated catch block
				msg.append("null");
			}
			msg.append(",");
		}
		msg.deleteCharAt(msg.length()-1).append("}");
		return msg.toString();
	}
}

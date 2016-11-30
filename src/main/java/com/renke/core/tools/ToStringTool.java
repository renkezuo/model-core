package com.renke.core.tools;

import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ToStringTool {
	
	public final static String mapToString(Map<String,Object> map,boolean isEntity){
		StringBuilder msg = new StringBuilder("{");
		Iterator<String> it = map.keySet().iterator();
		while(it.hasNext()){
			String key = it.next();
			if(isEntity){
				msg.append(key).append(":").append(entityToString(map.get(key))).append(",");
			}else{
				msg.append(key).append(":").append(objectToString(map.get(key))).append(",");
			}
		}
		msg.deleteCharAt(msg.length()-1).append("}");
		return msg.toString();
	}
	
	public final static String mapToString(Map<String,Object> map){
		return mapToString(map,false);
	}
	
	public final static <T> String arrayToString(T[] ts,boolean isEntity){
		StringBuilder msg = new StringBuilder("[");
		for(int i=0;i<ts.length;i++){
			if(isEntity){
				msg.append(entityToString(ts[i])).append(",");
			}else{
				msg.append(objectToString(ts[i])).append(",");
			}
		}
		msg.deleteCharAt(msg.length()-1).append("]");
		return msg.toString();
	}
	
	public final static String listToString(List<?> ls,boolean isEntity){
		return arrayToString(ls.toArray(),isEntity);
	}
	
	public final static String listToString(List<?> ls){
		return arrayToString(ls.toArray(),false);
	}
	
	public final static String objectToString(Object obj){
		if(obj == null){
			return "null";
		}else{
			return obj.toString();
		}
	}
	
	public final static <Entity> String entityToString(Entity entity){
		StringBuilder msg = new StringBuilder("{");
		Class<?> clazz = entity.getClass();
		Field[] fields = clazz.getDeclaredFields();
		for(Field field : fields){
			msg.append(field.getName()).append(":");
			try {
				field.setAccessible(true);
				msg.append(field.get(entity));
			} catch (IllegalArgumentException | IllegalAccessException e) {
				msg.append("null");
			}
			msg.append(",");
		}
		msg.deleteCharAt(msg.length()-1).append("}");
		return msg.toString();
	}
}

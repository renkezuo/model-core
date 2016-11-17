package com.renke.core.test;

import java.lang.reflect.Field;

public class Reflect {
	public final static void getClass(Object obj){
		System.out.println(obj.getClass().getSimpleName());
		StringBuilder msg = new StringBuilder("{");
		Class<?> clazz = obj.getClass();
		Field[] fields = clazz.getDeclaredFields();
		for(Field field : fields){
			msg.append(field.getName()).append(":");
			try {
				field.setAccessible(true);
				msg.append(field.get(obj));
			} catch (IllegalArgumentException | IllegalAccessException e) {
				// TODO Auto-generated catch block
				msg.append("null");
			}
			msg.append(",");
		}
		msg.deleteCharAt(msg.length()-1).append("}");
		System.out.println(msg.toString());
	}
	public static void main(String[] args) {
//		Model model = new Model();
//		getClass(model);
//		String str = "123";
//		getClass(str);
		int i = 0;
		Object[] objs= new Object[]{i};
		System.out.println(objs[0] instanceof Integer);
	}
}

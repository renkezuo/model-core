package com.renke.core.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import org.springframework.util.ReflectionUtils;

public class TypeConvertUtils {
	public static <T> T fromProperties(Class<T> clazz, final String prefix) throws InstantiationException, IllegalAccessException {
		if(prefix == null) {
			throw new RuntimeException("prefix cannot be nil.");
		}
		final T newInstance = clazz.newInstance();
		ReflectionUtils.doWithFields(clazz, new ReflectionUtils.FieldCallback() {
			
			@Override
			public void doWith(Field f) throws IllegalArgumentException,
					IllegalAccessException {
				if(Modifier.isStatic(f.getModifiers()) && Modifier.isFinal(f.getModifiers())) {//常量
					return;
				}
				f.setAccessible(true);//设置些属性是可以访问的 
				
				String propertiesKey = prefix + f.getName();
				String p = DictionarySettingUtils.getParameterValue(propertiesKey);
				if(p == null) {
					return;
				}
				
				String type = f.getType().toString();
				if (type.endsWith("String")) {
					f.set(newInstance, p);
				} else if (type.endsWith("int") || type.endsWith("Integer")) {
					f.set(newInstance, Integer.parseInt(p));
				} else if (type.endsWith("long") || type.endsWith("Long")) {
					f.set(newInstance, Long.parseLong(p));
				} else if (type.endsWith("double") || type.endsWith("Double")) {
					f.set(newInstance, Double.parseDouble(p));
				} else if (type.endsWith("float") || type.endsWith("Float")) {
					f.set(newInstance, Float.parseFloat(p));
				} else {
					System.out.println("unknow type:" + type);
				}
			}
		});
		
		return newInstance;
	}
}

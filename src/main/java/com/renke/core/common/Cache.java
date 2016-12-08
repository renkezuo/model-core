package com.renke.core.common;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Cache {
	
//	private static Map<String , Object> cache = new ConcurrentSkipListMap<String , Object>(); O(log(n)) 
	private static Map<String , Object> cache = new ConcurrentHashMap<String , Object>();
	public static Object get(String key){
		return cache.get(key);
	}
	
	public static void put(String key,Object value){
		cache.put(key, value);
	}
}

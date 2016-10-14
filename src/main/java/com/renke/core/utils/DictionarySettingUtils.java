package com.renke.core.utils;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.core.util.OptionConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.renke.core.IFreemarkerMarker;

@Component("dicSetting")
public class DictionarySettingUtils extends IFreemarkerMarker {
	protected static final Logger logger = LoggerFactory.getLogger(DictionarySettingUtils.class);
	
	/**
	 * 原始数据
	 * 优先级别
	 * localData > dataFromDB > properties
	 */
	static Map<String, String> localData;
	/**
	 * 原始数据 数据库
	 */
	static Map<String, String> dataFromDB;
	
	/**
	 * 原始数据 配置文件
	 */
	static Properties properties;
	
	/**
	 * 构造好的数据
	 */
	static Map<String, Object> structureMap = new LinkedHashMap<String, Object>();
	
	/**
	 * @param settings eg:{ftp,ftp1} --> Object Map.toString()
	 * 					  {ftp,ftp1,port} --> Object String
	 * @return String
	 */
	public static String getParameterValue(String... settings) {
		String v = (String) getParameter(settings);
		if(StringUtils.isBlank(v)) return v;
		try {
			String substVars = OptionConverter.substVars(v, DictionarySettingUtils.properties);
			if(! v.equals(substVars)) {
				logger.info("settings[{}] 原始值:{},转化后的值为:" + substVars, Arrays.toString(settings), v);
			}
			
			return substVars;
		} catch (IllegalArgumentException e) {
			logger.warn("Could not perform variable substitution.{}", e);
			return v;
		}
	}
	
	public static String getParameterValue(String setting) {
		return (String) getParameterValue(StringUtils.split(setting, "."));
	}
	
	public static String getParameterValueWithDefault(String setting, String defaultValue) {
		String parameterValue = getParameterValue(setting);
		if(StringUtils.isBlank(parameterValue)) {
			return defaultValue;
		}
		
		return parameterValue;
	}
	
	public static int getParameterValueWithDefaultInt(String setting, int defaultValue) {
		String parameterValue = getParameterValue(setting);
		if(StringUtils.isBlank(parameterValue)) {
			return defaultValue;
		}
		
		return NumberUtils.parseInt(parameterValue, defaultValue);
	}
	
	public static long getParameterValueWithDefaultLong(String setting, long defaultValue) {
		String parameterValue = getParameterValue(setting);
		if(StringUtils.isBlank(parameterValue)) {
			return defaultValue;
		}
		
		return NumberUtils.parseLong(parameterValue, defaultValue);
	}
	
	public static double getParameterValueWithDefaultDouble(String setting, double defaultValue) {
		String parameterValue = getParameterValue(setting);
		if(StringUtils.isBlank(parameterValue)) {
			return defaultValue;
		}
		
		return NumberUtils.parseDouble(parameterValue, defaultValue);
	}
	
	
	/**
	 * @param settings eg:{ftp,ftp1} --> Object Map
	 * 					  {ftp,ftp1,port} --> Object String
	 * @return Object
	 */
	@SuppressWarnings("rawtypes")
	public static Object getParameter(String... settings) {
		if (settings == null || settings.length < 1)
			return null;
		if (settings.length == 1) {
			settings = StringUtils.split(settings[0], ".");
		}
		
		if (settings.length == 1)
			return structureMap.get(settings[0]);
		Map tempMap = (Map) structureMap.get(settings[0]);
		for (int i = 1; i < settings.length - 1; i++) {
			
			if(tempMap == null){
				//System.out.println();
				return null;
			}
			tempMap = (Map) tempMap.get(settings[i]);
			if(tempMap == null) {
				return null;
			}
		}
		
		if(tempMap == null) {
			return null;
		}
		return tempMap.get(settings[settings.length - 1]);
	}
	
	/**
	 * @param settings eg:{ftp,ftp1} --> Object Map
	 * 					  {ftp,ftp1,port} --> throw exception
	 * @return Map<String, String>
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, String> getParameterMap(String... settings){
		return (Map<String, String>) getParameter(settings);
	}
	
	public static Map<String, String> getParameterMap(String setting){
		return (Map<String, String>) getParameterMap(StringUtils.split(setting, "."));
	}
	
	static void assemble() {
		Map<String, String> assembleMap = new LinkedHashMap<String, String>();
		
		if(CollectionUtils.isNotEmpty(DictionarySettingUtils.dataFromDB)) {
			assembleMap.putAll(dataFromDB);
		}
		
		if(DictionarySettingUtils.properties != null) {
			String key;
			for (Entry<Object, Object> entry : properties.entrySet()) {
				key = (String) entry.getKey();
				if(assembleMap.containsKey(key)) {//properties的优先级别没数据库的高
					logger.warn("properties 忽略已经存在的key:{}. ", key);
					continue;
				}
				
				assembleMap.put(key, (String) entry.getValue());
			}
		}
		
		if(CollectionUtils.isNotEmpty(localData)) {
			assembleMap.putAll(localData);
		}
		
		if (CollectionUtils.isEmpty(assembleMap)) {
			logger.warn("assembleMap collection is empty.");
		}
		
		Map<String, Object> structureMap = buildStructureMap(assembleMap);
		
		synchronized (DictionarySettingUtils.structureMap) {
			DictionarySettingUtils.structureMap = structureMap;
		}
		
		logger.info(AGsonBuilder.create().toJson(DictionarySettingUtils.structureMap));
	}
	
	public static void replaceFormProperties(Properties properties) {
		DictionarySettingUtils.properties = properties;
		assemble();
	}
	
	public static void replaceFormDB(Map<String, String> dataFromDB) {
		DictionarySettingUtils.dataFromDB = dataFromDB;
		assemble();
	}
	
	public static void replaceLocalDataDB(Map<String, String> localData) {
		DictionarySettingUtils.localData = localData;
		assemble();
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	static Map<String, Object> buildStructureMap(Map<String, String> assembleMap) {
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		if(CollectionUtils.isEmpty(assembleMap)) {
			return map;
		}
		for (Map.Entry<String, String> set : assembleMap.entrySet()) {
			String variable = set.getKey();
			String value = set.getValue();
			if (StringUtils.isBlank(variable)) {
				continue;
			}

			String[] keys = StringUtils.split(variable, '.');
			Map<String, Object> supMap = null;
			Map<String, Object> subMap = null;
			Object obj = null;
			int len = keys.length - 1;
			if (len < 1) { // only root node
				map.put(variable, value);
				continue;
			}

			supMap = (Map<String, Object>) map.get(keys[0]);
			if (supMap == null) { // new root node
				supMap = new LinkedHashMap<String, Object>();
				map.put(keys[0], supMap);
			}

			for (int i = 1; i < len; i++) {
				String key = keys[i];
				obj = supMap.get(key);

				if (obj != null && obj instanceof Map) {
					supMap = (Map) obj;
				} else {
					subMap = new LinkedHashMap<String, Object>();
					// subMap = new LinkedHashMap<String, Object>();
					supMap.put(key, subMap);
					supMap = subMap;
				}
			}
			supMap.put(keys[len], value);
		}
		
		return map;
	}
}
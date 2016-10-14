package com.renke.core.utils;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSyntaxException;
import com.renke.core.IFreemarkerMarker;

@Component("gsonUtils")
public class GsonUtils extends IFreemarkerMarker {
	public static JsonElement parse(String jeString) {
		try {
			JsonElement jsonElement = new JsonParser().parse(jeString);
			return jsonElement;
		} catch (JsonSyntaxException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static JsonArray parseJsonArray(String jsonArray) {
		if(StringUtils.isBlank(jsonArray)) {
			return null;
		}
		JsonElement parseJsonElement = parse(jsonArray);
		if(parseJsonElement != null && parseJsonElement.isJsonArray()) {
			return parseJsonElement.getAsJsonArray();
		}
		return null;
	}
	
	public static JsonObject parseJsonObject(String json) {
		if(StringUtils.isBlank(json)) {
			return null;
		}
		
		JsonElement parseJsonElement = parse(json);
		if(parseJsonElement != null && parseJsonElement.isJsonObject()) {
			return parseJsonElement.getAsJsonObject();
		}
		return null;
	}
	
	public static JsonElement deepGet(String jeString, String split) {
		JsonElement jsonElement = parse(jeString);
		if(jsonElement == null) return null;
		return deepGet(jsonElement, StringUtils.split(split, "."));
	}
	
	public static JsonElement deepGet(JsonElement je, String split) {
		return deepGet(je, StringUtils.split(split, "."));
	}
	
	public static JsonElement deepGet(JsonElement je, String[] split) {
		if (je == null || je.isJsonNull()) {
			return null;
		}
		JsonElement tempJE = je;
		for (int i = 0; i < split.length; i++) {
			String string = split[i];
			
			//处理 a.arr[0].id情况 //数组arr的第一个的id
			String isArray = StringUtils.substringBetween(string, "[", "]");
			if(isArray != null) {
				string = StringUtils.substringBeforeLast(string, "[");
			}
			
			if(tempJE.isJsonNull()) {
				return null;
			} else if (tempJE.isJsonObject()) {
				JsonObject asJsonObject = tempJE.getAsJsonObject();
				if (asJsonObject.has(string)) {
					tempJE = asJsonObject.get(string);
				} else {
					return null;
				}
			}
			
			if(isArray != null && tempJE.isJsonArray()) {
				if(tempJE.getAsJsonArray().size() < 1) {
					return null;
				}
				tempJE = tempJE.getAsJsonArray().get(NumberUtils.parseInt(isArray, 0));
			}

			if (i == split.length - 1) {
				return tempJE;
			}
		}
		
		return null;
	}
	
	public static long getLong(JsonObject jsonObject, String key,
			Long defaultValue) {
		JsonElement jsonElement = jsonObject.get(key);
		if (jsonElement == null || jsonElement.isJsonNull()) {
			return defaultValue;
		}

		return jsonElement.getAsLong();
	}

	public static Long getLong(JsonObject jsonObject, String key) {
		return getLong(jsonObject, key, 0L);
	}

	public static int getInt(JsonObject jsonObject, String key,
			Integer defaultValue) {
		JsonElement jsonElement = jsonObject.get(key);
		if (jsonElement == null || jsonElement.isJsonNull()) {
			return defaultValue;
		}
		
		if(jsonElement.isJsonPrimitive()) {
			try {
				return jsonElement.getAsJsonPrimitive().getAsNumber().intValue();
			} catch (Exception e) {
				return defaultValue;
			}
		}
		return defaultValue;
	}

	public static int getInt(JsonObject jsonObject, String key) {
		return getInt(jsonObject, key, 0);
	}

	public static String getString(JsonObject jsonObject, String key, String defaultValue) {
		
		JsonElement jsonElement = null;
		if(jsonObject == null || (jsonElement = jsonObject.get(key)) == null || jsonElement.isJsonNull()){
			return defaultValue;
		}
		return jsonElement.getAsString();
	}

	public static String getJsonValue(JsonObject jsonObject, String key) {
		return getJsonValue(jsonObject, key, "");
	}

	public static String getJsonValue(JsonObject jsonObject, String key,
			String defaultValue) {
		JsonElement jsonElement = jsonObject.get(key);

		if (jsonElement == null || jsonElement.isJsonNull()) {
			return defaultValue;
		}
		
		if(jsonElement  instanceof JsonPrimitive) {
			return 	jsonElement.getAsString();
		}

		return jsonElement.toString();
	}

	public static String getString(JsonObject jsonObject, String key) {
		return getString(jsonObject, key, "");
	}

	public static Double getDouble(JsonObject jsonObject, String key,
			Double defaultValue) {
		JsonElement jsonElement = jsonObject.get(key);
		if (jsonElement == null || jsonElement.isJsonNull()) {
			return defaultValue;
		}
		
		if(jsonElement.isJsonPrimitive()) {
			try {
				return jsonElement.getAsJsonPrimitive().getAsNumber().doubleValue();
			} catch (Exception e) {
				return defaultValue;
			}
		}
		return defaultValue;
	}

	public static Double getDouble(JsonObject jsonObject, String key) {
		return getDouble(jsonObject, key, 0D);
	}
	
	public static Float getFloat(JsonObject jsonObject, String key,
			Float defaultValue) {
		JsonElement jsonElement = jsonObject.get(key);
		if (jsonElement == null || jsonElement.isJsonNull()) {
			return defaultValue;
		}

		if(jsonElement.isJsonPrimitive()) {
			try {
				return jsonElement.getAsJsonPrimitive().getAsNumber().floatValue();
			} catch (Exception e) {
				return defaultValue;
			}
		}
		
		return defaultValue;
	}

	public static Float getFloat(JsonObject jsonObject, String key) {
		return getFloat(jsonObject, key, 0F);
	}

	public static List<String> array2List(String string) {

		if (StringUtils.isBlank(string)) {
			return null;
		}
		JsonArray array = new JsonParser().parse(string).getAsJsonArray();
		List<String> list = new ArrayList<String>();

		for (JsonElement je : array) {
			list.add(je.getAsString());
		}

		return list;
	}

	public static String getDeepValue(String jsonString, String deepKey) {
		return getDeepValue(jsonString, deepKey, "");
	}

	public static String getDeepValue(String jsonString, String deepKey,
			String defaultValue) {
		if (StringUtils.isBlank(jsonString)) {
			return defaultValue;
		}

		JsonElement je = new JsonParser().parse(jsonString);
		return getDeepValueByJson(je, deepKey, defaultValue);
	}

	public static String getDeepValueByJson(JsonElement je, String deepKey,
			String defaultValue) {
		String[] split = StringUtils.split(deepKey, ".");

		return getDeepValue(je, split, defaultValue);
	}

	public static String getDeepValueByJson(JsonElement je, String deepKey) {
		return getDeepValueByJson(je, deepKey, "");
	}

	public static String getDeepValue(JsonElement je, String[] split,
			String defaultValue) {
		if (je == null || je.isJsonNull()) {
			return defaultValue;
		}
		if (split.length < 2) {
			if (je.isJsonObject()) {
				return getString(je.getAsJsonObject(), split[0], defaultValue);
			}
		}

		JsonElement tempJE = je;

		for (int i = 0; i < split.length; i++) {
			String string = split[i];

			if (tempJE.isJsonObject()) {
				JsonObject asJsonObject = tempJE.getAsJsonObject();
				if (asJsonObject.has(string)) {
					tempJE = asJsonObject.get(string);
				} else {
					return defaultValue;
				}
			}

			if (i == split.length - 1) {
				if (tempJE == null || tempJE.isJsonNull()) {
					return defaultValue;
				} else {
					if (tempJE.isJsonPrimitive()) {
						if (tempJE.getAsJsonPrimitive().isString()) {
							return tempJE.getAsJsonPrimitive().getAsString();
						} else if (tempJE.getAsJsonPrimitive().isNumber()) {
							return tempJE.getAsJsonPrimitive().getAsNumber()
									.toString();
						} else if (tempJE.getAsJsonPrimitive().isBoolean()) {
							return String.valueOf(tempJE.getAsJsonPrimitive()
									.getAsBoolean());
						}

					}
					return tempJE.toString();
				}
			}
		}

		return defaultValue;
	}
	
	public static void main(String[] args) {
		
		JsonObject json = new JsonObject();
		json.addProperty("gg", "2.35");
		json.addProperty("gg", 2.34d);
		json.addProperty("gg2", 1);
		System.out.println(GsonUtils.getInt(json, "gg"));
		System.out.println(GsonUtils.getInt(json, "gg2"));
		
		System.out.println(GsonUtils.getFloat(json, "gg"));
		System.out.println(GsonUtils.getFloat(json, "gg2"));
		
		System.out.println(GsonUtils.getDouble(json, "gg"));
		System.out.println(GsonUtils.getDouble(json, "gg2"));
	}
}
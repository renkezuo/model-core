package com.renke.core.tools;

public class CheckTool {
	public final static boolean isBlank(Object obj){
		return isNull(obj) || obj.toString().length()<=0;
	}
	public final static boolean isNotBlank(Object obj){
		return !isBlank(obj);
	}
	public final static boolean isNull(Object obj){
		return obj == null;
	}
	public final static boolean isNotNull(Object obj){
		return !isNull(obj);
	}
	public final static boolean isTrue(boolean obj){
		return obj;
	}
	public final static boolean isNotTrue(boolean obj){
		return !obj;
	}
}

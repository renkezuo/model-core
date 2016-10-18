package com.renke.core.tools;

import com.renke.core.exception.CheckException;

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
	public final static boolean isEquals(Object obj1 , Object obj2){
		if(obj1 == null || obj2 == null) return false;
		return obj1 == obj2 || obj1.equals(obj2);
	}
	public final static boolean isNotEquals(Object obj1 , Object obj2){
		return !isEquals(obj1, obj2);
	}
	public final static boolean isBlankArray(Object[] arr){
		return arr == null || arr.length <= 0;
	}
	
	public final static void throwBlank(Object obj , String msg){
		if(isBlank(obj)){
			throw new CheckException(msg);
		}
	}
	public final static void throwNotBlank(Object obj , String msg){
		if(!isBlank(obj)){
			throw new CheckException(msg);
		}
	}
	public final static void throwNull(Object obj , String msg){
		if(isNull(obj)){
			throw new CheckException(msg);
		}
	}
	public final static void throwNotNull(Object obj , String msg){
		if(!isNull(obj)){
			throw new CheckException(msg);
		}
	}
	public final static void throwTrue(boolean obj , String msg){
		if(obj){
			throw new CheckException(msg);
		}
	}
	public final static void throwNotTrue(boolean obj , String msg){
		if(!obj){
			throw new CheckException(msg);
		}
	}
	public final static void throwEquals(Object obj1 , Object obj2 , String msg){
		if(isEquals(obj1,obj2)){
			throw new CheckException(msg);
		}
	}
	public final static void throwNotEquals(Object obj1 , Object obj2 , String msg){
		if(isNotEquals(obj1,obj2)){
			throw new CheckException(msg);
		}
	}
	
	public final static void throwBlankArray(Object[] arr , String msg){
		if(isBlankArray(arr)){
			throw new CheckException(msg);
		}
	}
}

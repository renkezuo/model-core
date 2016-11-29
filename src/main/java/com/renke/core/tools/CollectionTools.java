package com.renke.core.tools;

import java.util.List;

public class CollectionTools{
	public static Object[] changeToLength(Object[] src,int length){
		Object[] arrays = new Object[length];
		length = src.length > length ? length:src.length;
		System.arraycopy(src,0, arrays, 0, length);
		return arrays;
	}
	
	public static Object[] autoExtend(Object[] src){
		Object[] arrays = new Object[src.length * 2];
		System.arraycopy(src,0, arrays, 0, src.length);
		return arrays;
	}
	
	public static byte[] appendArray(byte[] src,int srcPos,byte[] append,int appendPos, int length){
		byte[] arrays = new byte[srcPos + length];
		System.arraycopy(src, 0, arrays, 0, srcPos);
		System.arraycopy(append, appendPos, arrays, srcPos, length);
		return arrays;
	}
	
	public static byte[] appendArray(byte[] src,byte[] append){
		return appendArray(src,src.length,append,0,append.length);
	}
	
	public static <Entity> Entity getLastE(List<Entity> list){
		if(list.size() <= 0){return null;}
		return list.get(list.size());
	}
	
	
	public static void main(String[] args) {
		String str = "12345678";
		String str2 = "1234567890";
		byte[] buf  = appendArray(str.getBytes(), 2, str2.getBytes(), 5, 3);
		System.out.print(new String(buf));
	}
	
}

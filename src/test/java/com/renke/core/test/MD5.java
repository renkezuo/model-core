package com.renke.core.test;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5 {
	public static void main(String[] args) throws NoSuchAlgorithmException {
		//E10ADC3949BA59ABBE56E057F20F883E
//		String str = "123456555sdfasdflsdajkflksadflksdajflksadjflkjds";
//		long begin = System.currentTimeMillis();
//		for(int i=0;i<1000000;i++){
//			com.renke.core.utils.MD5.getMD5ofStr(str);
//		}
//		md5--c:1633ms
//		System.out.println("md5--c:"+(System.currentTimeMillis()-begin)+"ms");
//		
//		begin = System.currentTimeMillis();
//		for(int i=0;i<1000000;i++){
//			MessageDigest md = MessageDigest.getInstance("MD5");
//			md.update(str.getBytes());
//			new BigInteger(1, md.digest()).toString(16);
//		}
//		md5--java:1599ms
//		System.out.println("md5--java:"+(System.currentTimeMillis()-begin)+"ms");
		
		System.out.println(com.renke.core.utils.MD5.getMD5ofStr("123456"));
		System.out.println(com.renke.core.utils.MD5.getMD5ofStr("123").toLowerCase());
	}
}

package com.renke.core.utils;

import java.util.Random;

public class GenerateUtils {
   
    /**
     * java生成随机数字和字母组合
     * @param length[生成随机数的长度]
     * @return
     */
    public static String generateCharAndNumr(int length) {
		String val = "";
		Random random = new Random();
		for (int i = 0; i < length; i++) {
			// 输出字母还是数字
			String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num";
			// 字符串
			if ("char".equalsIgnoreCase(charOrNum)) {
				// 取得大写字母还是小写字母
				int choice = random.nextInt(2) % 2 == 0 ? 65 : 97;
				val += (char) (choice + random.nextInt(26));
			} else if ("num".equalsIgnoreCase(charOrNum)) { // 数字
				val += String.valueOf(random.nextInt(10));
			}
		}
        return val;
    }
    
    /**
     * java生成随机数字和字母组合,都是小写的
     * @param length[生成随机数的长度]
     * @return
     */
    public static String generateLowerCharAndNumr(int length) {
    	return generateCharAndNumr(length).toLowerCase();
    }
    
    /**
     * 获取一定长度的随机字符串
     * @param length 指定字符串长度
     * @return 一定长度的字符串
     */
    public static String getRandomStringByLength(int length) {
        String seed = "abcdefghijklmnopqrstuvwxyz0123456789";
        return getRandomStringByLength(seed, length);
    }
    
    /**
     * 获取一定长度的随机字符串
     * @param length 指定字符串长度
     * @return 一定长度的字符串
     */
    public static String getRandomStringByLength(String seed, int length) {
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(seed.length());
            sb.append(seed.charAt(number));
        }
        return sb.toString();
    }
    
    public static void main(String[] args) {
    	String base = "abcdefghjkmnpqrstuvwxyz23456789";
    	for (int i=0; i<1000; i++) {
    		System.out.println("生成的10为随机数为：" + getRandomStringByLength(base, 6));
		}
    }
}

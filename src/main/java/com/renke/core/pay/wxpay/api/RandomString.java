package com.renke.core.pay.wxpay.api;

import java.util.Random;

/**
 * User: rizenguo
 * Date: 2014/10/29
 * Time: 14:18
 */
public class RandomString {
	static Random random = new Random();
    /**
     * 获取一定长度的随机字符串
     * @param length 指定字符串长度
     * @return 一定长度的字符串
     */
    public static String getRandomStringByLength(int length) {
        String base = "abcdefghijklmnopqrstuvwxyz0123456789";
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }

}

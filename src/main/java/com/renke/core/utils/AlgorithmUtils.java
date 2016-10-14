package com.renke.core.utils;

import java.io.IOException;
import java.security.MessageDigest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 * @author <a href="mailto:rplees.i.ly@gmail.com">rplees</a>
 * date 2010-04-07
 * {@code} 一些常用的算法，加密
 */
public class AlgorithmUtils {
	private final static Log log = LogFactory.getLog(AlgorithmUtils.class);
	
	/**
	 * BASE64 编码
	 * @param s
	 * @return
	 */
	public static String enBase64(String s) {
		return new BASE64Encoder().encode(s.getBytes());
	}

	/**
	 * BASE64 解码
	 * @param s
	 * @return
	 */
	public static String deBase64(String s) {
		try {
			return new String(new BASE64Decoder().decodeBuffer(s));
		} catch (Exception e) {
			log.error("deBase64 error.", e);
		}
		return null;
	}

    /**
     * 加密方法（单向）
     * @param password 原始密码
     * @param algorithm 加密算法类型，如：SHA
     * @return String 加密后的值
     */
    public static String encode(String password, AlgorithmEnum algorithm) {
        byte[] unencodedPassword = password.getBytes();
        MessageDigest md = null;
        String algorithmString = "";
        if(algorithm == AlgorithmEnum.MD5) {
        	algorithmString = "MD5";
        } else if(algorithm == AlgorithmEnum.SHA) {
        	algorithmString = "SHA";
        }
        try {
            // first create an instance, given the provider
            md = MessageDigest.getInstance( algorithmString );
        } catch (Exception e) {
            log.error("错误，获得加密算法！Exception: " + e);
            return password;
        }
        md.reset();
        md.update(unencodedPassword);

        byte[] encodedPassword = md.digest();
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < encodedPassword.length; i++) {
            if ((encodedPassword[i] & 0xff) < 0x10) {
                buf.append("0");
            }
            buf.append(Long.toString(encodedPassword[i] & 0xff, 16));
        }
        return buf.toString();
    }
    
    
    /**
	 * Description 根据键值进行加密
	 * 
	 * @param data
	 * @param key
	 *            加密键byte数组
	 * @return
	 * @throws Exception
	 */
	public static String encrypt(String data, String key) throws Exception {
		return DesUtil.encrypt(data, key);
	}

	/**
	 * Description 根据键值进行解密
	 * 
	 * @param data
	 * @param key
	 *            加密键byte数组
	 * @return
	 * @throws IOException
	 * @throws Exception
	 */
	public static String decrypt(String data, String key) throws IOException,
			Exception {
		return DesUtil.decrypt(data, key);
	}
	
	public static void main(String[] args) {
		int parseInt = NumberUtils.parseInt("10329569873");
		System.out.println(parseInt);
		String contact = "15390891113";
		
		String s = contact.substring(0,  3) + "***" + contact.substring(contact.length() - 3, contact.length());
		System.out.println(s);
		
		System.out.println(encode("111111", AlgorithmEnum.MD5));
//    	DESede buildDesedeAlgorithm2 = AlgorithmUtils.buildDesedeAlgorithm(key + "2");
//    	String decrypt = buildDesedeAlgorithm2.decrypt(encrypt);
//    	
//    	System.out.println(encrypt);
	}
}
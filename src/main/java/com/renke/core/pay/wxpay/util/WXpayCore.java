package com.renke.core.pay.wxpay.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.net.ssl.SSLContext;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.renke.core.pay.wxpay.config.WXpayConfig;

/**
 * 微信支付核心方法
 * @author eagle
 * @since 2015年7月8日
 * @version 1.1.0
 */
public class WXpayCore {
	static final Logger logger = LoggerFactory.getLogger(WXpayCore.class);
    // 返回字段 成功标识
    private static final String SUCCESS_CODE = "SUCCESS";

    // 返回字段 失败标识
    private static final String FAIL_CODE = "FAIL";

    // ------------------HTTP 请求相关--------------------------------
    // ---------------------------------------------------------
    /**
     * 请求 SSL
     * @param url
     * @param xml
     * @return
     * @throws KeyStoreException
     * @throws NoSuchAlgorithmException
     * @throws CertificateException
     * @throws IOException
     * @throws KeyManagementException
     * @throws UnrecoverableKeyException
     */
	public static String postXmlSSL(String url, String xml) throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException, KeyManagementException, UnrecoverableKeyException {
		KeyStore keyStore = KeyStore.getInstance("PKCS12");
		FileInputStream instream = new FileInputStream(new File(WXpayConfig.cert_p12));// 放退款证书的路径
		try {
			keyStore.load(instream, WXpayConfig.mch_id.toCharArray());
		} finally {
			instream.close();
		}

		SSLContext sslcontext = SSLContexts.custom().loadKeyMaterial(keyStore, WXpayConfig.mch_id.toCharArray()).build();
		SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
				sslcontext, new String[] { "TLSv1" }, null,
				SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
		CloseableHttpClient closeableHttpClient = HttpClients.custom()
				.setSSLSocketFactory(sslsf).build();

		return _postXml(url, xml, closeableHttpClient);
	}
    
    /**
     * 想微信服务器post xml请求
     * @param url
     * @param xml
     * @return
     */
    public static String postXml(String url, String xml) {
    	HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
    	CloseableHttpClient closeableHttpClient = httpClientBuilder.build();
    	return _postXml(url, xml, closeableHttpClient);
    }

    private static String _postXml(String url, String xml, CloseableHttpClient closeableHttpClient) {
    	logger.info("请求：" + url);
    	logger.debug(xml);

        String result = "";
        // HttpClient
        HttpPost httpPost = new HttpPost(url);
        StringEntity entity;
        try {
            entity = new StringEntity(xml, WXpayConfig.DEFAULT_CHAR_SET);
            httpPost.setEntity(entity);
            HttpResponse httpResponse;
            // post请求
            httpResponse = closeableHttpClient.execute(httpPost);
            HttpEntity httpEntity = httpResponse.getEntity();
            if (httpEntity != null) {
                // 打印响应内容
                result = EntityUtils.toString(httpEntity, WXpayConfig.DEFAULT_RESP_CHAR_SET);
                // 过滤
                // result = result.replaceAll("<![CDATA[|]]>", "");
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 释放资源
            try {
                closeableHttpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        logger.info("返回结果:");
        logger.debug(result);
        return result;
    }
    
    // -------------------参数签名相关-------------------------------
    // ---------------------------------------------------------

    /**
     * 除去数组中的空值和签名参数
     * @param sArray 签名参数组
     * @return 去掉空值与签名参数后的新签名参数组
     */
    public static Map<String, String> paraFilter(Map<String, String> sArray) {

        Map<String, String> result = new HashMap<String, String>();

        if (sArray == null || sArray.size() <= 0) {
            return result;
        }

        for (String key : sArray.keySet()) {
            String value = sArray.get(key);
            if (value == null || value.equals("") || key.equalsIgnoreCase("sign")) {
                continue;
            }
            result.put(key, value);
        }

        return result;
    }

    /**
     * 把数组所有元素排序，并按照“参数=参数值”的模式用“&”字符拼接成字符串
     * @param params 需要排序并参与字符拼接的参数组
     * @return 拼接后字符串
     */
    public static String createLinkString(Map<String, String> params) {

        List<String> keys = new ArrayList<String>(params.keySet());
        Collections.sort(keys);

        String prestr = "";

        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            String value = params.get(key);

            if (i == keys.size() - 1) {// 拼接时，不包括最后一个&字符
                prestr = prestr + key + "=" + value;
            } else {
                prestr = prestr + key + "=" + value + "&";
            }
        }

        return prestr;
    }

    /**
     * 生成签名
     * @param content 待签名字符串
     * @param key 密钥
     * @param contentEncode 待签名字符串字符编码,默认UTF-8
     * @return 签名结果
     */
    public static String getSign(String content, String key, String charset) {
        String resultSign = "";
        String tobesign = content + "&key=" + key;
        if (null == charset || "".equals(charset)) {
            resultSign = DigestUtils.md5Hex(tobesign);
        } else {
            try {
                resultSign = DigestUtils.md5Hex((tobesign).getBytes(charset));
            } catch (UnsupportedEncodingException e) {
                resultSign = DigestUtils.md5Hex(tobesign);
            }
        }
        return resultSign.toUpperCase();
    }

    /**
     * 生成签名
     * @param content 待签名字符串
     * @param key 密钥
     * @return 签名结果
     */
    public static String getSign(String content, String key) {
        String tobesign = content + "&key=" + key;
        // DigestUtils.md5Hex(String) 默认采用utf8编码
        return DigestUtils.md5Hex(tobesign).toUpperCase();
    }

    /**
     * 生成签名
     * @param params 待签名Map
     * @param key 密钥
     * @return 签名结果
     */
    public static String getSign(Map<String, String> params, String key) {
        Map<String, String> newMap = paraFilter(params);
        String tobeSignStr = createLinkString(newMap);
        return getSign(tobeSignStr, key);
    }

    /**
     * 给Map添加签名
     * @param params 待签名Map
     * @param key 密钥
     * @return 添加签名后的结果Map
     */
    public static Map<String, String> signMap(Map<String, String> params, String key) {
        Map<String, String> newMap = paraFilter(params);
        String tobeSignStr = createLinkString(newMap);
        String sign = getSign(tobeSignStr, key);
        newMap.put("sign", sign);
        return newMap;
    }

    /**
     * 验证签名
     * @param params 含有签名 ( key为sign ) 的map
     * @param key 商户api密钥
     * @return 返回是否验证通过
     */
    public static boolean verify(Map<String, String> params, String key) {
        return null != params && null != params.get("sign")
                && params.get("sign").equalsIgnoreCase(getSign(params, key));
    }

    /**
     * 获取一定长度的随机字符串
     * @param length 指定字符串长度
     * @return 一定长度的字符串
     */
    public static String getRandomStringByLength(int length) {
        String base = "abcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }

    // --------------------返回结果相关------------------------------
    // ---------------------------------------------------------
    /**
     * 将返回结果转为Map输出
     * @param retStr 微信支付服务器返回的xml结果
     * @return 转换成map后输出
     */
    public static Map<String, String> getRetMap(String retStr) {
        return WXpayXmlUtils.xml2map(retStr);
    }

    /**
     * 提取错误信息
     * @param retMap 微信支付服务器返回的结果(已转成Map的形式)
     * @return 错误信息
     */
    public static String getErrMsg(Map<String, String> retMap) {
        String errMsg = "";
        if (null == retMap) {
            return errMsg;
        }

        if (FAIL_CODE.equalsIgnoreCase(retMap.get("return_code"))) {
            errMsg = retMap.get("return_msg");
        } else if (FAIL_CODE.equalsIgnoreCase(retMap.get("result_code"))) {
            errMsg = retMap.get("err_code") + " : " + retMap.get("err_code_des");
        }
        return errMsg;
    }

    /**
     * 提取错误信息
     * @param retStr 微信支付服务器返回的结果(字符串形式)
     * @return 错误信息
     * @see WXpayCore#getErrMsg(Map)
     */
    public static String getErrMsg(String retStr) {
        return getErrMsg(getRetMap(retStr));
    }

    /**
     * 判断微信接口返回结果是否代表成功(会验证返回的签名) <br/>
     * @param 预支付结果XML字符串
     * @return 是否调用成功
     * @see WXpayCore#isRetSuccess(Map)
     */
    public static boolean isRetSuccess(String retStr) {
        return isRetSuccess(getRetMap(retStr));
    }

    /**
     * 判断微信接口返回结果是否代表成功(会验证返回的签名) <br/>
     * 逻辑：检查 return_code【标识通讯是否成功】 和 result_code【标识业务是否成功】为 "SUCCESS" 且 签名验证通过
     * @param 微信支付结果Map
     * @return 是否调用成功
     */
    public static boolean isRetSuccess(Map<String, String> resposeMap) {
        return resposeMap != null && SUCCESS_CODE.equalsIgnoreCase(resposeMap.get("return_code"))
                && WXpayCore.verify(resposeMap, WXpayConfig.api_key)
                && SUCCESS_CODE.equalsIgnoreCase(resposeMap.get("result_code"));
    }

    /**
     * 判断微信接口返回结果是否代表成功(会验证返回的签名) <br/>
     * 逻辑：检查 return_code【标识通讯是否成功】 和 result_code【标识业务是否成功】为 "SUCCESS" 且 签名验证通过
     * @param 微信支付结果Map
     * @return 是否调用成功
     */
    public static boolean isTradeStateSuccess(Map<String, String> resposeMap) {
        return resposeMap != null && SUCCESS_CODE.equalsIgnoreCase(resposeMap.get("trade_state"))
                && WXpayCore.verify(resposeMap, WXpayConfig.api_key);
    }
}
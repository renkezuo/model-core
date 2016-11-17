package com.renke.core.tools;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Map;

import javax.net.ssl.SSLContext;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;

/**
 * HTTP请求类
 * @author LiHong
 */
public class HttpInvoker {

	public static HttpClient httpclient = HttpClientBuilder.create().build();
	
    /**
	 * 模拟浏览器post提交
	 * 
	 * @param url
	 * @return
	 */  
    public static HttpPost getPostMethod(String url) {  
        HttpPost pmethod = new HttpPost(url); // 设置响应头信息
        pmethod.addHeader("Connection", "keep-alive");  
        pmethod.addHeader("Accept", "*/*");  
        pmethod.addHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");  
        pmethod.addHeader("Host", "mp.weixin.qq.com");  
        pmethod.addHeader("X-Requested-With", "XMLHttpRequest");  
        pmethod.addHeader("Cache-Control", "max-age=0");  
        pmethod.addHeader("User-Agent", "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.0) ");  
        return pmethod;
    }
  
    /**  
     * 模拟浏览器GET提交  
     * @param url  
     * @return  
     */  
    public static HttpGet getGetMethod(String url) {  
        HttpGet pmethod = new HttpGet(url);  
        // 设置响应头信息  
        pmethod.addHeader("Connection", "keep-alive");  
        pmethod.addHeader("Cache-Control", "max-age=0");  
        pmethod.addHeader("User-Agent", "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.0) ");  
        pmethod.addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/;q=0.8");  
        return pmethod;  
    }  
}

package com.renke.core.tools;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;

import com.renke.core.exception.ApacheHTTPException;
import com.renke.core.pay.wxpay.api.Util;

public class HTTPTools {
	private static final String[] PROXY_REMOTE_IP_ADDRESS = { "X-Real-IP", "X-Forwarded-For", "Proxy-Client-IP",
			"WL-Proxy-Client-IP", "HTTP_CLIENT_IP" };

	public static String getRemoteIp(HttpServletRequest request) {
		for (int i = 0; i < PROXY_REMOTE_IP_ADDRESS.length; i++) {
			String ip = request.getHeader(PROXY_REMOTE_IP_ADDRESS[i]);
			if (ip != null && ip.trim().length() > 0) {
				return getRemoteIpFromForward(ip.trim());
			}
		}
		return request.getRemoteHost();
	}

	private static String getRemoteIpFromForward(String xforwardIp) {
		int commaOffset = xforwardIp.indexOf(',');
		if (commaOffset < 0) {
			return xforwardIp;
		}
		return xforwardIp.substring(0, commaOffset);
	}

	public static void setWxWebConfig(HttpServletRequest request) {
		String url = request.getRequestURL().toString();
		String queryString = request.getQueryString();
		if(CheckTool.isNotBlank(queryString)){
			url += "?" + queryString;
		}
		System.out.println("url:------------"+url);
		request.setAttribute("config", Util.getConfig(url));
	}

	private static CloseableHttpClient createSSLClientDefault() {
		try {
			SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
				// 信任所有
				public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
					return true;
				}
			}).build();
			SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext);
			return HttpClients.custom().setSSLSocketFactory(sslsf).build();
		} catch (GeneralSecurityException e) {
			e.printStackTrace();
		}
		return HttpClients.createDefault();
	}
	
	public static HttpResponse accessGet(String url) {
		return accessGet(url, "UTF-8");
	}

	public static HttpResponse accessGet(String url, String charset) {
		HttpGet httpGet = new HttpGet(url);
		httpGet.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.71 Safari/537.36");
		try {
			return createSSLClientDefault().execute(httpGet);
		} catch (IOException e) {
			e.printStackTrace();
			throw new ApacheHTTPException(e.getMessage());
		}
	}

	public static HttpResponse accessPost(String url, String body) {
		return accessPost(url, body, "UTF-8");
	}

	public static HttpResponse accessPost(String url, String body, String charset) {
		HttpPost httpPost = new HttpPost(url);
		StringEntity entity = new StringEntity(body, charset);
		httpPost.setEntity(entity);
		httpPost.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.71 Safari/537.36");
		try {
			return createSSLClientDefault().execute(httpPost);
		} catch (IOException e) {
			e.printStackTrace();
			throw new ApacheHTTPException(e.getMessage());
		}
	}
}

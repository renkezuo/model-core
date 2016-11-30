package com.renke.core.tools;

import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.List;

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

import com.renke.core.entity.ByteArray;
import com.renke.core.entity.Param;
import com.renke.core.entity.PostDataTmp;
import com.renke.core.enums.Type;
import com.renke.core.exception.ApacheHTTPException;
import com.renke.core.pay.wxpay.api.RandomString;
import com.renke.core.pay.wxpay.api.Util;

public class HTTPTool {
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
	
	public static void parsePost(InputStream is , String contentType,List<Param> result){
		int boundaryIndex = contentType.indexOf("boundary");
		byte[] segmentFlag = null;
		if(boundaryIndex>-1){
			segmentFlag = ("--"+contentType.substring(boundaryIndex+9)).getBytes();
		}
		try {
			byte[] buf = new byte[2048];
			int len = 0;
			//临时数据
			PostDataTmp pdt = new PostDataTmp();
			//每读一次，保存上一次和本次的内容，和段标记做比较，有则分析，没则继续
			while( (len=is.read(buf) )!=-1){
				//组装数据
				if(pdt!= null){
					ByteArray array = null;
					pdt.data = CollectionTool.appendArray(pdt.data,buf,0,len);
					array = new ByteArray(pdt.data);
					pdt.data = null;
					pdt = parseData(pdt, array, segmentFlag, result);
				}else{
					break;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/***
	 * 逻辑参考uml图
	 * @param pdt
	 * @param array
	 * @param segFlag
	 * @param result
	 * @return
	 * @author renke.zuo@foxmail.com
	 * @time 2016-11-29 15:02:43
	 */
	private static PostDataTmp parseData(PostDataTmp pdt,ByteArray array,byte[] segFlag,List<Param> result){
		int mark = array.mark();
		while(array.position() < array.limit()){
			int pos = array.position();
			boolean move = false;
			//处理分段标记内容和正文内容
			if(pdt.flag == 0){
				//如果长度不够，则直接返回剩余数据
				if(pos+segFlag.length+2 > array.limit()){
					pdt.data = new byte[array.limit() - pos];
					System.arraycopy(array.getArray(), pos, pdt.data, 0, pdt.data.length);
					array.clear();
					return pdt;
				}
				for(int s=0;s<segFlag.length;s++){
					if(array.get(pos+s) != segFlag[s]){
						break;
					}
					if(s==segFlag.length-1){
						pdt.flag = 1;
						pos = array.moveByN(segFlag.length+2);
						mark = array.mark();
						move = true;
					}
				}
//				if(pdt.flag == 1 && array.get(pos-2)=='-' && array.get(pos-1)=='-'){
//					return null;
//				}
			}
			//分段内容处理完毕，开始处理key信息
			if(pdt.flag == 1){
				if(array.get(pos) == '\r'){
					if(pos+1 >= array.limit()){
						pdt.data = new byte[array.limit()-mark];
						System.arraycopy(array.getArray(), pos, pdt.data, 0, array.limit()-mark);
						array.clear();
						return pdt;
					}
					if(array.get(pos+1) == '\n'){
						String[] params = new String(array.getArray(),mark,pos-mark).split(";");
						if(params.length>=2){
							Param param = new Param();
							param.key = params[1].split("=")[1];
							param.type = Type.TEXT;
							if(params.length==3){
								param.fileName = params[2].split("=")[1];
								param.type = Type.FILE;
								String tmpName = System.currentTimeMillis()+RandomString.getRandomStringByLength(10);
								param.tmpPath = "/tmp/"+tmpName;
							}
							pdt.flag += 2;
							result.add(param);
							pos = array.moveByN(2);
							mark = array.mark();
							move = true;
						}
					}
				}
			}
			//key信息处理完毕，开始处理内容类型信息，或者直接处理为空行
			if(pdt.flag == 3){
				if(array.get(pos) == '\r'){
					if(pos+1 >= array.limit()){
						pdt.data = new byte[array.limit()-mark];
						System.arraycopy(array.getArray(), pos, pdt.data, 0, array.limit()-mark);
						array.clear();
						return pdt;
					}
					if(array.get(pos+1) == '\n'){
						if(mark != pos){
							//param可能为空
							Param param = CollectionTool.getLastE(result);
							String[] params = new String(array.getArray(),mark,pos-mark).split(":");
							//可能出现异常
							param.contentType = params[1].trim();
							pdt.flag += 4;
							pos = array.moveByN(2);
							mark = array.mark();
							move = true;
						}else{
							pdt.flag += 12;
							pos = array.moveByN(2);
							mark = array.mark();
							move = true;
						}
					}
				}
			}
			
			//开始处理空行信息
			if(pdt.flag == 7){
				if(array.get(pos) == '\r'){
					if(pos+1 >= array.limit()){
						pdt.data = new byte[array.limit()-mark];
						System.arraycopy(array.getArray(), pos, pdt.data, 0, array.limit()-mark);
						array.clear();
						return pdt;
					}
					if(array.get(pos+1) == '\n'){
						pdt.flag += 8;
						pos = array.moveByN(2);
						mark = array.mark();
						move = true;
					}
				}
			}
			
			if(pdt.flag == 15){
				Param param = CollectionTool.getLastE(result);
				if(pos+segFlag.length+4 > array.limit()){
					if(param!=null && !param.status){
						if(param.type == Type.TEXT){
							//直接追加数组
							param.bytes = CollectionTool.appendArray(param.bytes,array.getArray(),mark,pos-mark);
							if(param.bytes.length > PostDataTmp.MIN_FILE_SIZE){
								String tmpName = System.currentTimeMillis()+RandomString.getRandomStringByLength(10);
								param.tmpPath = "/tmp/"+tmpName;
								param.type = Type.BINARY;
								FileTool.appendBytes(param.tmpPath,param.bytes);
							}
						}else{
							FileTool.appendBytes(param.tmpPath, array.getArray(),mark,pos-mark);
						}
					}
					pdt.data = new byte[array.limit() - pos];
					System.arraycopy(array.getArray(), pos, pdt.data, 0, pdt.data.length);
					array.clear();
					return pdt;
				}else{
					for(int s=0;s<segFlag.length;s++){
						if(array.get(pos+s) != segFlag[s]){
							break;
						}
						if(s==segFlag.length-1){
							if(pos != mark && pdt.flag == 15){
								//此处param可能为空
								if(param.type == Type.TEXT){
									//直接追加数组
									param.bytes = CollectionTool.appendArray(param.bytes,array.getArray(),mark,pos-mark-2);
								}else{
									//直接追加文件
									FileTool.appendBytes(param.tmpPath, array.getArray(),mark,pos-mark-2);
								}
								param.status = true;
								pdt.data = null;
							}
							pdt.flag = 1;
							pos = array.moveByN(segFlag.length+2);
							mark = array.mark();
							move = true;
						}
					}
				}
				if(pdt.flag == 1 && array.get(pos-2)=='-' && array.get(pos-1)=='-'){
					return null;
				}
			}
			if(pos == array.limit()){
				return pdt;
			}
			
			if(!move) array.next();
		}
		return null;
	}
	
	
}

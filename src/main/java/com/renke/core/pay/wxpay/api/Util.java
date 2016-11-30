package com.renke.core.pay.wxpay.api;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.renke.core.pay.wxpay.config.WXpayConfig;
import com.renke.core.pay.wxpay.last.Config;
import com.renke.core.pay.wxpay.last.WebConfig;
import com.thoughtworks.xstream.XStream;

/**
 * User: rizenguo Date: 2014/10/23 Time: 14:59
 */
public class Util {

	// 打log用
	private static Logger logger = LoggerFactory.getLogger(Util.class);

	/**
	 * 通过反射的方式遍历对象的属性和属性值，方便调试
	 *
	 * @param o
	 *            要遍历的对象
	 * @throws Exception
	 */
	public static void reflect(Object o) throws Exception {
		Class<?> cls = o.getClass();
		Field[] fields = cls.getDeclaredFields();
		for (int i = 0; i < fields.length; i++) {
			Field f = fields[i];
			f.setAccessible(true);
			Util.log(f.getName() + " -> " + f.get(o));
		}
	}

	public static byte[] readInput(InputStream in) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		int len = 0;
		byte[] buffer = new byte[1024];
		while ((len = in.read(buffer)) > 0) {
			out.write(buffer, 0, len);
		}
		out.close();
		in.close();
		return out.toByteArray();
	}

	public static String inputStreamToString(InputStream is) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		int i;
		while ((i = is.read()) != -1) {
			baos.write(i);
		}
		return baos.toString();
	}

	public static InputStream getStringStream(String sInputString) {
		ByteArrayInputStream tInputStringStream = null;
		if (sInputString != null && !sInputString.trim().equals("")) {
			tInputStringStream = new ByteArrayInputStream(sInputString.getBytes());
		}
		return tInputStringStream;
	}

	public static Object getObjectFromXML(String xml, Class<?> tClass) {
		// 将从API返回的XML数据映射到Java对象
		XStream xStreamForResponseData = new XStream();
		xStreamForResponseData.alias("xml", tClass);
		xStreamForResponseData.ignoreUnknownElements();// 暂时忽略掉一些新增的字段
		return xStreamForResponseData.fromXML(xml);
	}

	public static String getStringFromMap(Map<String, Object> map, String key, String defaultValue) {
		if (key == "" || key == null) {
			return defaultValue;
		}
		String result = (String) map.get(key);
		if (result == null) {
			return defaultValue;
		} else {
			return result;
		}
	}

	public static int getIntFromMap(Map<String, Object> map, String key) {
		if (key == "" || key == null) {
			return 0;
		}
		if (map.get(key) == null) {
			return 0;
		}
		return Integer.parseInt((String) map.get(key));
	}

	/**
	 * 打log接口
	 * 
	 * @param log
	 *            要打印的log字符串
	 * @return 返回log
	 */
	public static String log(Object log) {
		logger.info(log.toString());
		// System.out.println(log);
		return log.toString();
	}

	/**
	 * 读取本地的xml数据，一般用来自测用
	 * 
	 * @param localPath
	 *            本地xml文件路径
	 * @return 读到的xml字符串
	 */
	public static String getLocalXMLString(String localPath) throws IOException {
		return Util.inputStreamToString(Util.class.getResourceAsStream(localPath));
	}

	public static String map2xml(Map<String, Object> map) {
		StringBuilder xml = new StringBuilder();
		xml.append("<xml>\n");
		for (Map.Entry<String, Object> entry : map.entrySet()) {
			xml.append("<").append(entry.getKey()).append("><![CDATA[").append(entry.getValue()).append("]]></")
					.append(entry.getKey()).append(">\n");
		}
		xml.append("</xml>");

		String ret = xml.toString();
		return ret;
	}

	/**
	 * 将微信返回的xml字符串转为Map<String,String>
	 * 
	 * @param map
	 * @return
	 */
	public static Map<String, String> xml2map(String xml) {
		Map<String, String> ret = new HashMap<String, String>();

		if (xml == null)
			return ret;
		// 这里用Dom的方式解析回包的最主要目的是防止API新增回包字段
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder;
		Document document = null;
		try {
			builder = factory.newDocumentBuilder();
			document = builder.parse(new ByteArrayInputStream(xml.getBytes(WXpayConfig.DEFAULT_CHAR_SET)));
			document.normalize();
		} catch (Exception e) {
			return ret;
		}

		// 获取到document里面的全部结点
		NodeList allNodes = document.getFirstChild().getChildNodes();
		Node node;
		int i = 0;
		while (i < allNodes.getLength()) {
			node = allNodes.item(i);
			if (node instanceof Element) {
				ret.put(node.getNodeName(), node.getTextContent());
			}
			i++;
		}

		return ret;
	}
	
	public static WebConfig getConfig(String url){
		return getConfig(url,null);
	}
	
	public static WebConfig getConfig(String url,String signType){
		WebConfig config = new WebConfig();
		long timestamp = System.currentTimeMillis() / 1000;
		String noncestr = RandomString.getRandomStringByLength(32);
		String signStr = "noncestr="+noncestr+"&timestamp=" + timestamp + "&url="+url;
		if("SHA".equals(signType)){
			config.setSignature(DigestUtils.sha1Hex(signStr));
		}else{
			config.setSignature(DigestUtils.md5Hex(signStr));
		}
		config.setAppid(Config.appid);
		config.setNoncestr(noncestr);
		config.setTimestamp(timestamp);
		config.setUrl(url);
		return config;
	}

	public static String createSign(Map<String, String> packageParams, String signKey) {
		SortedMap<String, String> sortedMap = new TreeMap<String, String>();
		sortedMap.putAll(packageParams);

		List<String> keys = new ArrayList<String>(packageParams.keySet());
		Collections.sort(keys);

		StringBuffer toSign = new StringBuffer();
		for (String key : keys) {
			String value = packageParams.get(key);
			if (null != value && !"".equals(value) && !"sign".equals(key) && !"key".equals(key)) {
				toSign.append(key + "=" + value + "&");
			}
		}
		toSign.append("key=" + signKey);
		String sign = DigestUtils.md5Hex(toSign.toString()).toUpperCase();
		return sign;
	}
}

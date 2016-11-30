package com.renke.core.test;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.util.EntityUtils;

import com.renke.core.tools.HTTPTool;

public class ApacheHttpTest {
	public static final String APP_ID = "wx422747cb9405ac50";
	public static final String APP_SECRET = "dbe4a7a363c2f80261491826f1fe3abb";
	public static void main(String[] args) throws ParseException, IOException {
		String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=" + APP_ID + "&secret=" + APP_SECRET;
		url = "https://www.baidu.com/";
		HttpResponse response= HTTPTool.accessGet(url);
		System.out.println(EntityUtils.toString(response.getEntity(),"UTF-8"));
	}
}

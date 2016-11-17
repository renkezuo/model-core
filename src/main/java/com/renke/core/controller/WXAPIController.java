package com.renke.core.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.renke.core.pay.wxpay.last.Config;

import me.chanjar.weixin.common.bean.WxJsapiSignature;
import me.chanjar.weixin.common.util.RandomUtils;

@Controller
@RequestMapping("/wx")
public class WXAPIController {
	private final static Logger logger = LoggerFactory.getLogger(WXAPIController.class);

	public String wxConfig(HttpServletRequest request){
		String url = "";
		url = request.getRequestURL().toString();
		if(StringUtils.isNotBlank(request.getQueryString())) {
			url += "?" + request.getQueryString();
		}
		long timestamp = System.currentTimeMillis();
		String noncestr = RandomUtils.getRandomStr();
		String signature = "&noncestr=" + noncestr +
				"&timestamp=" + timestamp+ "&url=" + url;

		WxJsapiSignature jsapiSignature = new WxJsapiSignature();
		jsapiSignature.setAppid(Config.singleAppid);
		jsapiSignature.setTimestamp(timestamp);
		jsapiSignature.setNoncestr(noncestr);
		jsapiSignature.setUrl(url);
		jsapiSignature.setSignature(signature);
		request.setAttribute("config", "");
		return null;
	}
}

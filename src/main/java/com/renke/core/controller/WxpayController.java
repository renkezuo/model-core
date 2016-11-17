package com.renke.core.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.JsonObject;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.renke.core.pay.wxpay.api.HttpsRequest;
import com.renke.core.pay.wxpay.api.RandomStringGenerator;
import com.renke.core.pay.wxpay.api.Signature;
import com.renke.core.pay.wxpay.api.Util;
import com.renke.core.pay.wxpay.api.WXpayApi;
import com.renke.core.pay.wxpay.config.WXpayConfig;
import com.renke.core.pay.wxpay.last.Config;
import com.renke.core.pay.wxpay.util.WXpayCore;
import com.renke.core.tools.HTTPTools;
import com.renke.core.tools.ImageTools;

@Controller
@RequestMapping("/wxpay")
public class WxpayController {
	private final static Logger logger = LoggerFactory.getLogger(WxpayController.class);

	@Resource HttpsRequest wxRequest;
	
	@RequestMapping(value = "/pcprepay")
	public String pcPreparePay(HttpServletRequest request) {
		SimpleDateFormat sdf = new SimpleDateFormat("YYYYMMddHHmmssSSS");
		String timestamp = sdf.format(System.currentTimeMillis());
		//失效时间设置为30分钟
		String endtime = sdf.format(System.currentTimeMillis() + 1000 * 60 * 30);
		logger.info("remote:{}", request.getRemoteAddr());
		logger.info("local:{}", request.getLocalAddr());
		String productName = "智能面膜";
		int fee = 1;
		Map<String, String> params = new HashMap<String, String>();
		// testMap.put("device_info", "aaa"); // 设备号
		params.put("body", productName); // 商品描述
		// testMap.put("detail", "离子水1，离子水2"); // 商品详情
		// testMap.put("attach", "附加数据"); // 附加数据
		params.put("out_trade_no", timestamp); // 商户订单号
		params.put("total_fee", ""+fee); // 总金额
		params.put("spbill_create_ip", request.getRemoteAddr()); // 终端IP
		params.put("time_start", timestamp.substring(0, endtime.length() - 3)); //交易起始时间
		params.put("time_expire", endtime.substring(0, endtime.length() - 3)); //失效时间
//		params.put("time_start", ""); // 交易起始时间
//		params.put("time_expire", ""); // 交易结束时间
		params.put("goods_tag", ""); // 商品标记
		params.put("notify_url", "http://pay.ngrok.joinclub.cn/model-core/wxpay/callbackpay.do"); // 通知地址
		params.put("trade_type", "NATIVE"); // 交易类型
		params.put("product_id", "10001"); // 商品ID
		params.put("openid", ""); // 用户标识
		Map<String, String> retMap = WXpayApi.unifiedOrderRetMap(params);
		request.setAttribute("code_url", retMap.get("code_url"));
		request.setAttribute("body", "智能面膜");
		request.setAttribute("order_no", timestamp);
		request.setAttribute("order_fee", (double)fee / 100);
		logger.info("{}", retMap);
		return "pay/qrcode";
	}
	
	@RequestMapping(value = "/mwebpay")
	public String mwebPay(HttpServletRequest request) {
		SimpleDateFormat sdf = new SimpleDateFormat("YYYYMMddHHmmssSSS");
		String timestamp = sdf.format(System.currentTimeMillis());
		//失效时间设置为30分钟
		String endtime = sdf.format(System.currentTimeMillis() + 1000 * 60 * 30);
		logger.info("remote:{}", request.getRemoteAddr());
		logger.info("local:{}", request.getLocalAddr());
		String productName = "智能面膜";
		int fee = 1;
		Map<String, String> params = new HashMap<String, String>();
		// testMap.put("device_info", "aaa"); // 设备号
		params.put("body", productName); // 商品描述
		// testMap.put("detail", "离子水1，离子水2"); // 商品详情
		// testMap.put("attach", "附加数据"); // 附加数据
		params.put("out_trade_no", timestamp); // 商户订单号
		params.put("total_fee", ""+fee); // 总金额
		params.put("spbill_create_ip", request.getRemoteAddr()); // 终端IP
		params.put("time_start", timestamp.substring(0, endtime.length() - 3)); //交易起始时间
		params.put("time_expire", endtime.substring(0, endtime.length() - 3)); //失效时间
//		params.put("time_start", ""); // 交易起始时间
//		params.put("time_expire", ""); // 交易结束时间
		params.put("goods_tag", ""); // 商品标记
		params.put("notify_url", "http://pay.ngrok.joinclub.cn/model-core/wxpay/callbackpay.do"); // 通知地址
		params.put("trade_type", "MWEB"); // 交易类型
		params.put("product_id", "10001"); // 商品ID
		params.put("openid", ""); // 用户标识
		Map<String, String> retMap = WXpayApi.unifiedOrderRetMap(params);
		request.setAttribute("code_url", retMap.get("code_url"));
		request.setAttribute("body", "智能面膜");
		request.setAttribute("order_no", timestamp);
		request.setAttribute("order_fee", (double)fee / 100);
		logger.info("{}", retMap);
		return "pay/mweb";
	}
	

	@RequestMapping(value = "/jssdk")
	public String payConfig(HttpServletRequest request){
		String timeStamp = String.valueOf(System.currentTimeMillis());
		String nonceStr = RandomStringGenerator.getRandomStringByLength(32);
		Map<String,String> map = new HashMap<String,String>();
		map.put("appid", WXpayConfig.pay_app_id);
		map.put("timeStamp", timeStamp);
		map.put("nonceStr", nonceStr);
//		Signature.getSign(map);
		map.put("sign", Util.createSign(map,WXpayConfig.pay_app_secret));
		request.setAttribute("config", map);
		return "pay/jssdk";
	}
	
	@RequestMapping(value = "/wxprepay")
	public String wxPreparePay(HttpServletRequest request){
//		WxSession wxSession = (WxSession) request.getAttribute("wxSession");
//		WxMpOAuth2AccessToken wxMpOAuth2AccessToken = (WxMpOAuth2AccessToken) wxSession.getAttribute("wxMpOAuth2AccessToken");
//		User user = (User) wxSession.getAttribute("user");
//		String body = "面膜";
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
//		String outTradeNo = sdf.format(System.currentTimeMillis());
//		Double amt = 100d;
//		String tradeType = "JSAPI";
		HTTPTools.setWxWebConfig(request);
//		String ip = HTTPTools.getRemoteIp(request);
//		WxMpInMemoryConfigStorage wmcs = new WxMpInMemoryConfigStorage();
//		wmcs.setAppId(WXpayConfig.pay_app_id);
//		wmcs.setPartnerId(WXpayConfig.pay_mch_id);
//		wmcs.setPartnerKey(WXpayConfig.pay_app_secret);
//		wxMpService.setWxMpConfigStorage(wmcs);
//		String notifyUrl = FileUtils.splicePaths("http://pay.ngrok.joinclub.cn/wxpay", "/callbackpay.do");
//		Map<String, String> jssdkPayInfo = wxMpService.getJSSDKPayInfo(wxMpOAuth2AccessToken.getOpenId(), outTradeNo, amt, body, tradeType, ip, notifyUrl);
//		Map<String, String> jssdkPayInfo = wxMpService.getJSSDKPayInfo("oH5HYt-s0hEepEZTZ1ryz8VWGy0Q", outTradeNo, amt, body, tradeType, ip, notifyUrl);
//		logger.info(AGsonBuilder.create().toJsonTree(jssdkPayInfo).toString());
//		String url = WXpayConfig.get_user_id;
//		url = url.replace("#appid#", WXpayConfig.pay_app_id);
//		try {
//			url = url.replace("#redirect_uri#",java.net.URLEncoder.encode( "http://pay.ngrok.joinclub.cn/wxpay/getuserid.do","UTF-8"));
//			System.out.println("getUserId:"+wxRequest.sendPost(url, ""));
//		} catch (UnsupportedEncodingException e1) {
//		} catch (UnrecoverableKeyException | KeyManagementException | KeyStoreException | NoSuchAlgorithmException
//				| IOException e1) {
//			e1.printStackTrace();
//		}
		String url = WXpayConfig.unifiedorder_url;
		
		Map<String, Object> params = new HashMap<String,Object>();
		// 创建订单
		params = createOrderParams();
		
		//申请退款
//		url = Configure.refundorder_url;
//		params = refundOrderParams();
		
		params = createCommonParams(params);
		String objXml = Util.map2xml(params);
		String resultStr = "";
		try {
			resultStr = wxRequest.sendPost(url, objXml);
		} catch (UnrecoverableKeyException | KeyManagementException | KeyStoreException | NoSuchAlgorithmException
				| IOException e) {
			e.printStackTrace();
		}
		Map<String,String> resultMap = Util.xml2map(resultStr);
		
		System.out.println(resultMap);
		
		Map<String, String> payInfo = new HashMap<String, String>();
		payInfo.put("appId", Config.appid);
		// 支付签名时间戳，注意微信jssdk中的所有使用timestamp字段均为小写。但最新版的支付后台生成签名使用的timeStamp字段名需大写其中的S字符
		payInfo.put("timeStamp", String.valueOf(System.currentTimeMillis()));
		payInfo.put("nonceStr", resultMap.get("nonce_str"));
		payInfo.put("package", "prepay_id=" + resultMap.get("prepay_id"));
		payInfo.put("signType", "MD5");
		String finalSign = Util.createSign(payInfo, Config.api_key);
		payInfo.put("paySign", finalSign);
		payInfo.put("payPackage", "prepay_id=" + resultMap.get("prepay_id"));
		request.setAttribute("pay", payInfo);
		return "pay/pay";
	}
	
	@ResponseBody
	@RequestMapping(value = "/orderquery")
	public String queryOrder(HttpServletRequest request) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("out_trade_no", "20161028093728480");
		String result = WXpayApi.orderQuery(params);
		Map<String,String> retMap = WXpayCore.getRetMap(result);
		logger.info("retMap----{}"+retMap);
		String ret = "<xml><return_code><![CDATA[{}]]></return_code><return_msg><![CDATA[{}]]></return_msg></xml>";
		ret = ret.replace("{}", "SUCCESS");
		ret = ret.replace("{}", "OK");
		return ret;
	}
	

	@ResponseBody
	@RequestMapping(value = "/callbackpay")
	public String callbackPay(@RequestBody(required=false) String body,HttpServletRequest request) {
		//微信回调
		//返回成功或者异常告知微信服务端
		//如果不告知微信服务端，会一直接收到微信服务端请求
		String ret = "<xml><return_code><![CDATA[{}]]></return_code> <return_msg><![CDATA[{}]]></return_msg></xml>";
		//如果body为空，返回异常
		if(body==null){
			ret = ret.replace("{}", "FAIL");
			ret = ret.replace("{}", "no request body");
		}else{
			ret = ret.replace("{}", "SUCCESS");
			ret = ret.replace("{}", "OK");
		}
		Map<String,String> result = WXpayCore.getRetMap(body);
		logger.info("result----{}"+result);
		
		logger.info(body);
		
		logger.info(request.getQueryString());
		return ret;
	}

	public String wxQrcode(){
		String url = WXpayConfig.ewm_ticket_create;
		String accessToken = wxRequest.getToken();
		url = url + accessToken;
//		HttpPost httpPost = HttpInvoker.getPostMethod(url + accessToken);
		JsonObject arg = new JsonObject();
		JsonObject actionInfo = new JsonObject();
		JsonObject scene = new JsonObject();
		scene.addProperty("scene_id", "123");
		actionInfo.add("scene", scene);
		arg.addProperty("expire_seconds", 604800);
		arg.addProperty("action_name", "QR_SCENE");
		arg.add("action_info", actionInfo);
		
//		JSONObject arg = new JSONObject();
//		JSONObject actionInfo = new JSONObject();
//		JSONObject scene = new JSONObject();
//		scene.put("scene_id", param);
//		actionInfo.put("scene", scene);
//		arg.put("expire_seconds", 604800);
//		arg.put("action_name", "QR_LIMIT_SCENE");
//		arg.put("action_info", actionInfo);
//		httpPost.setEntity(new StringEntity(arg.toString(), "UTF-8"));
//		HttpResponse response = HttpInvoker.createSSLClientDefault().execute(httpPost);
//		String res = EntityUtils.toString(response.getEntity(), "UTF-8");
//		System.out.println(res);
		try {
			System.out.println(wxRequest.sendPost(url,arg.toString()));
		} catch (UnrecoverableKeyException | KeyManagementException | KeyStoreException | NoSuchAlgorithmException
				| IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		JSONObject js = JSONObject.fromObject(res);
//		String ticket = js.getString("ticket");
//		return ticket;
		return null;
	}
	

	@RequestMapping(value = "/qrcode")
	public void createQrcode(HttpServletResponse response,
			@RequestParam(value = "content", required = false) String content) {
		logger.info("url:{}", content);
		int width = 300; // 二维码图片宽度 300
		int height = 300; // 二维码图片高度300
		String format = "png";// 二维码的图片格式 gif
		Hashtable<EncodeHintType, Object> hints = new Hashtable<EncodeHintType, Object>();
		// 指定纠错等级,纠错级别（L 7%、M 15%、Q 25%、H 30%）
		hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
		// 内容所使用字符集编码[如果没有中文，建议不要设置此项]
		hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
		hints.put(EncodeHintType.MARGIN, 1);// 设置二维码边的空度，非负数
		BitMatrix bitMatrix;
		try {
			bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, width, height, hints);
			ImageTools.writeToStream(bitMatrix, format, response.getOutputStream());
			response.flushBuffer();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (WriterException e) {
			e.printStackTrace();
		}
	}

	private static Map<String, Object> createOrderParams() {
		Map<String, Object> params = new HashMap<String, Object>();
		SimpleDateFormat sdf = new SimpleDateFormat("YYYYMMddHHmmssSSS");
		String timestamp = sdf.format(System.currentTimeMillis());
		// 失效时间设置为30分钟
		String endtime = sdf.format(System.currentTimeMillis() + 1000 * 60 * 30);
		String productName = "智能面膜";
		params.put("body", productName); // 商品描述
		// testMap.put("detail", "离子水1，离子水2"); // 商品详情
		// testMap.put("attach", "附加数据"); // 附加数据
		params.put("out_trade_no", timestamp); // 商户订单号
		params.put("total_fee", "1"); // 总金额
		params.put("spbill_create_ip", "192.168.0.1"); // 终端IP
		params.put("time_start", timestamp.substring(0, endtime.length() - 3)); // 交易起始时间
		params.put("time_expire", endtime.substring(0, endtime.length() - 3)); // 失效时间
		// params.put("time_start", ""); // 交易起始时间
		// params.put("time_expire", ""); // 交易结束时间
		params.put("goods_tag", ""); // 商品标记
		params.put("notify_url", "http://pay.ngrok.joinclub.cn/wxpay/callbackpay.do"); // 通知地址
//		params.put("trade_type", "NATIVE"); // 交易类型
		params.put("trade_type", "JSAPI");
		//目前只有NATIVE和APP权限，JSAPI和H5支付都没有权限
		params.put("product_id", "10001"); // 商品ID
		params.put("openid", "oH5HYt-s0hEepEZTZ1ryz8VWGy0Q"); // 用户标识
		return params;
	}
	public static Map<String, Object> createCommonParams(Map<String, Object> map) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("appid", Config.appid);
		params.put("mch_id", Config.mch_id);
		params.put("nonce_str", RandomStringGenerator.getRandomStringByLength(32));
		params.putAll(map);
		params.remove("sign");
		params.put("sign", Signature.getSign(params));
		return params;
	}
	
	@ResponseBody
	@RequestMapping(value = "/pointMy")
	public String pointMy(@RequestBody(required=false) String body,HttpServletRequest request) throws UnsupportedEncodingException{
		String method = request.getMethod();
		if ("GET".equals(method)) {
			return request.getParameter("echostr");
		} else {
			Map<String,String> rq = Util.xml2map(body);
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("ToUserName", rq.get("FromUserName"));
			map.put("FromUserName", rq.get("ToUserName"));
			map.put("CreateTime", System.currentTimeMillis());
			map.put("Content", "你好，世界！");
			map.put("MsgType", "text");
			System.out.println(Util.map2xml(map));
			return Util.map2xml(map);
		}
	}
	
	@ResponseBody
	@RequestMapping(value = "/getuserid")
	public String getUserId(@RequestBody(required=false) String body,HttpServletRequest request) throws UnsupportedEncodingException{
		System.out.println("callback.do:"+body);
		System.out.println("queryString:"+request.getQueryString());
		return "123";
	}
	
	
	public static void main(String[] args) {
		try {
			System.out.println(java.net.URLEncoder.encode("http://sdlkfdlskfj.com","UTF-8"));
			Map<String, String> payInfo = new HashMap<String, String>();
//			payInfo.put("appId", WXpayConfig.pay_app_id);
			// 支付签名时间戳，注意微信jssdk中的所有使用timestamp字段均为小写。但最新版的支付后台生成签名使用的timeStamp字段名需大写其中的S字符
			payInfo.put("timeStamp", "1478485211989");
			payInfo.put("nonceStr", "5oHEGzL9hcwJpj1w");
			payInfo.put("package", "prepay_id=wx201611071020127cfc8205db0395662477");
			payInfo.put("signType", "MD5");
			//key = fe6c442f13023bf0f84bf759edeb6318
			//result : 685B6F8E19C6AD495730C536279F3CC3
			//8B25FF30C8879DBBB8230F3D6BE40DB0
			System.out.println("1:"+Signature.getSign(payInfo));
			System.out.println(Util.createSign(payInfo,WXpayConfig.pay_app_secret));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/***
	 * 
	 * 2016-11-07 10:20:11 [INFO] [HttpsRequest:144] [http-nio-8080-exec-10] - executing request POST https://api.mch.weixin.qq.com/pay/unifiedorder HTTP/1.1
nonceStr=5oHEGzL9hcwJpj1w&package=prepay_id=wx201611071020127cfc8205db0395662477&signType=MD5&timeStamp=1478485211989&key=fe6c442f13023bf0f84bf759edeb6318
{"timeStamp":"1478485211989","package":"prepay_id=wx201611071020127cfc8205db0395662477","paySign":"685B6F8E19C6AD495730C536279F3CC3","signType":"MD5","nonceStr":"5oHEGzL9hcwJpj1w"}
2016-11-07 10:20:14 [INFO] [WxpayController:240] [http-nio-8080-exec-1] - result----{}{}
2016-11-07 10:20:14 [INFO] [WxpayController:242] [http-nio-8080-exec-1] - null
	 * 
	 */
}

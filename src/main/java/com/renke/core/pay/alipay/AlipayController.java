package com.renke.core.pay.alipay;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayOpenPublicTemplateMessageIndustryModifyRequest;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.request.AlipayTradeWapPayRequest;
import com.alipay.api.response.AlipayOpenPublicTemplateMessageIndustryModifyResponse;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.google.gson.JsonObject;

@Controller
@RequestMapping("/alipay")
public class AlipayController {
	public final static Logger logger = LoggerFactory.getLogger(AlipayController.class);
	static AlipayClient client = new DefaultAlipayClient("https://openapi.alipaydev.com/gateway.do"
			,AlipayConfig.APP_ID,AlipayConfig.APP_PRIVATE_KEY,"json","GBK",AlipayConfig.ALIPAY_PUBLIC_KEY);
	
	//error 40006  ISV权限不足
	@RequestMapping("/msg")
	public @ResponseBody JsonObject msg() throws AlipayApiException{
//		AlipayOpenPublicTemplateMessageIndustryModifyRequest request = new AlipayOpenPublicTemplateMessageIndustryModifyRequest();
		AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();//创建API对应的request类
		JsonObject jo = new JsonObject();
		jo.addProperty("out_trade_no", "20150320010101001");
		jo.addProperty("trade_no", "2014112611001004680073956707");
		request.setBizContent(jo.toString());
		AlipayTradeQueryResponse response = client.execute(request);//通过alipayClient调用API，获得对应的response类
		if(response.isSuccess()){
			logger.info("alipay success!");
		}
		return jo;
	}
	
	@RequestMapping("/pay")
	public @ResponseBody String alipay() throws AlipayApiException{
	    AlipayTradeWapPayRequest alipayRequest = new AlipayTradeWapPayRequest();//创建API对应的request
	    alipayRequest.setReturnUrl("http://domain.com/CallBack/return_url.jsp");
	    alipayRequest.setNotifyUrl("http://domain.com/CallBack/notify_url.jsp");//在公共参数中设置回跳和通知地址
	    JsonObject jo = new JsonObject();
		jo.addProperty("out_trade_no", "20150320010101002");
		jo.addProperty("total_amount", 88.88);
		jo.addProperty("subject", "Iphone6 16G");
		jo.addProperty("seller_id", "2088123456789012");
		jo.addProperty("product_code", "QUICK_WAP_PAY");
		alipayRequest.setBizContent(jo.toString());
	    String form = client.pageExecute(alipayRequest).getBody(); //调用SDK生成表单
	    logger.info(form);
	    return form;
//	    httpResponse.setContentType("text/html;charset=" + AlipayServiceEnvConstants.CHARSET);
//	    httpResponse.getWriter().write(form);//直接将完整的表单html输出到页面
//	    httpResponse.getWriter().flush();
	}
	@RequestMapping("/demo")
	public @ResponseBody String demo() throws AlipayApiException{
		AlipayClient client = new DefaultAlipayClient("https://openapi.alipaydev.com/gateway.do"
				,AlipayConfig.APP_ID,AlipayConfig.APP_PRIVATE_KEY,"json","GBK",AlipayConfig.ALIPAY_PUBLIC_KEY);
		AlipayOpenPublicTemplateMessageIndustryModifyRequest request = new AlipayOpenPublicTemplateMessageIndustryModifyRequest();
		JsonObject jo = new JsonObject();
		jo.addProperty("primary_industry_name", "IT科技/IT软件与服务");
		jo.addProperty("primary_industry_code", "10001/20102");
		jo.addProperty("secondary_industry_code", "10001/20102");
		jo.addProperty("secondary_industry_name", "IT科技/IT软件与服务");
		request.setBizContent(jo.toString());
		AlipayOpenPublicTemplateMessageIndustryModifyResponse response = client.execute(request);
		if(response.isSuccess()){
			logger.info("alipay success!");
		}
		return jo.toString();
	}
	
}

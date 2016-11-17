package com.renke.core.pay.wxpay.config;


/* *
 *类名：WXpayConfig
 *功能：微信支付相关配置
 */

public class WXpayConfig {

    // 发送内容的编码格式
    public static final String DEFAULT_CHAR_SET = "UTF-8";

    // 接收返回内容的编码格式
    public static final String DEFAULT_RESP_CHAR_SET = "UTF-8";

    // 统一下单接口中使用 trade_type
    public static final String DEFAULT_TRADE_TYPE = "APP";

    // 拼接真正支付使用参数用的，目前固定为 Sign=WXPay
    public static final String PACKAGE = "Sign=WXPay";

    // ↓↓↓↓↓↓↓↓↓↓请在这里配置您的基本信息↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
    // APPid
    
    public static String appid = "wx21922ce19d2a4dc6"; // 客户端 在pay.properties里，
    // API 密钥
    public static String api_key = "1b3f056c0f01ec04c2cfbf777346762f"; // 注意在两个商户平台里面，设置的是同一个api_key，这样签名的时候不用区分 客户端 和 合伙人端
    // 商户号
    public static String mch_id = "1249261801"; // 客户端
    
	public static String pay_app_id = "wx01f74326d262fb36";
	public static String pay_app_secret = "fe6c442f13023bf0f84bf759edeb6318";
	public static String pay_mch_id = "1276621901";
    
    
    public static String notifyURL = "";
    //证书目录//针对SSL通信级别要求高的
    public static String cert_p12 = "/apiclient_cert.p12";
    // ↑↑↑↑↑↑↑↑↑↑请在这里配置您的基本信息↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑

    /**
     * 统一支付类
     */
    public static final String unifiedorder_url = "https://api.mch.weixin.qq.com/pay/unifiedorder";
    
    /**
     * 订单查询类
     */
    public static final String orderquery_url = "https://api.mch.weixin.qq.com/pay/orderquery";
    
    /**
     * 退款类
     */
    public static final String refund_url = "https://api.mch.weixin.qq.com/secapi/pay/refund";
    
    public static final String create_qrcode_url = "https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token=";
    
    public static final String get_user_id = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=#appid#&redirect_uri=#redirect_uri#&response_type=code&scope=snsapi_base#wechat_redirect";
    
    public static final String ewm_ticket_create = "https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token=";
}

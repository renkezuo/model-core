package com.renke.core.pay.wxpay.api;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.Map;

import com.renke.core.pay.wxpay.api.param.OrderQueryParam;
import com.renke.core.pay.wxpay.api.param.UnifiedOrderParam;
import com.renke.core.pay.wxpay.config.WXpayConfig;
import com.renke.core.pay.wxpay.util.WXpayCore;

public class WXpayApi {

    /**
     * 微信统一下单接口( 预支付接口 ) API
     * @param params 请求参数<br />
     *            -- appid 公众账号ID 未输入时默认 WXpayConfig.appid <br/>
     *            -- mch_id 商户号 未输入时默认 WXpayConfig.mch_id <br/>
     *            -- device_info 设备号 <br/>
     *            -- body 商品描述 **必输** <br/>
     *            -- detail 商品详情 <br/>
     *            -- attach 附加数据 <br/>
     *            -- out_trade_no 商户订单号 **必输** 注意 ，为货币最小单位，比如人民币时，单位为分<br/>
     *            -- fee_type 货币类型 <br/>
     *            -- total_fee 总金额 **必输** <br/>
     *            -- spbill_create_ip 终端IP **必输** <br/>
     *            -- time_start 交易起始时间 <br/>
     *            -- time_expire 交易结束时间 <br/>
     *            -- goods_tag 商品标记 <br/>
     *            -- notify_url 通知地址 **必输** <br/>
     *            -- trade_type 交易类型 **必输** JSAPI，NATIVE，APP，WAP, <br/>
     *            -- product_id 商品ID <br/>
     *            -- openid 用户标识 <br/>
     * @return 响应结果字符串
     * @see UnifiedOrderParam#makeRequest(Map)
     */
    public static String unifiedOrder(Map<String, String> params) {

        String reqStr = UnifiedOrderParam.makeRequest(params);
        return WXpayCore.postXml(WXpayConfig.unifiedorder_url, reqStr);
    }

    /**
     * 微信统一下单接口( 预支付接口 ) API， 结果以map方式返回
     * @param params 请求参数<br />
     * @return 响应结果(Map形式)
     * @see WXpayApi#unifiedOrder(Map)
     */
    public static Map<String, String> unifiedOrderRetMap(Map<String, String> params) {
        return WXpayCore.getRetMap(unifiedOrder(params));
    }

    /**
     * 将 统一下单接口(预支付) 成功返回的数据 拼接再签名后发给App,发起真正支付使用 准备发起支付的参数，返回给客户端App使用，
     * @param unifiedorderRetMap 统一支付成功返回的结果
     * @return
     */
    public static Map<String, String> makePaymentMap(Map<String, String> unifiedorderRetMap) {
        return UnifiedOrderParam.makePaymentMap(unifiedorderRetMap);
    }

    /**
     * 订单查询接口 API
     * @param params 请求参数<br />
     *            -- appid 公众账号ID 未输入时默认 WXpayConfig.appid <br/>
     *            -- mch_id 商户号 未输入时默认 WXpayConfig.mch_id <br/>
     *            -- transaction_id 微信订单号 <br/>
     *            -- out_trade_no 商户订单号 **没提供transaction_id时需要传这个** <br/>
     * @return 响应结果字符串
     * @see OrderQueryParam#makeRequest(Map)
     */
    public static String orderQuery(Map<String, String> params) {
        String reqStr = OrderQueryParam.makeRequest(params);
        return WXpayCore.postXml(WXpayConfig.orderquery_url, reqStr);
    }

    /**
     * 订单查询接口 API， 结果以map方式返回
     * @param params 请求参数<br />
     * @return 响应结果(Map形式)
     * @see WXpayApi#orderQuery(Map)
     */
    public static Map<String, String> orderQueryRetMap(Map<String, String> params) {
        return WXpayCore.getRetMap(orderQuery(params));
    }


    /**
     * 退款 API https://pay.weixin.qq.com/wiki/doc/api/app.php?chapter=9_4&index=6
     * @param params 请求参数<br />
     *            -- appid 公众账号ID 未输入时默认 WXpayConfig.appid <br/>
     *            -- mch_id 商户号 未输入时默认 WXpayConfig.mch_id <br/>
     *            -- 设备号	device_info	否	String(32)	013467007045764	终端设备号
     *            	随机字符串	nonce_str	是	String(32)	5K8264ILTKCH16CQ2502SI8ZNMTM67VS	随机字符串，不长于32位。推荐随机数生成算法
					签名	sign	是	String(32)	C380BEC2BFD727A4B6845133519F3AD6	签名，详见签名生成算法
					微信订单号	transaction_id	二选一	String(28)	1217752501201407033233368018	微信生成的订单号，在支付通知中有返回
					商户订单号	out_trade_no	String(32)	1217752501201407033233368018	商户侧传给微信的订单号
					商户退款单号	out_refund_no	是	String(32)	1217752501201407033233368018	商户系统内部的退款单号，商户系统内部唯一，同一退款单号多次请求只退一笔
					总金额	total_fee	是	Int	100	订单总金额，单位为分，只能为整数，详见支付金额
					退款金额	refund_fee	是	Int	100	退款总金额，订单总金额，单位为分，只能为整数，详见支付金额
					货币种类	refund_fee_type	否	String(8)	CNY	货币类型，符合ISO 4217标准的三位字母代码，默认人民币：CNY，其他值列表详见货币类型
					操作员	op_user_id	是	String(32)	1900000109	操作员帐号, 默认为商户号
     *            -- transaction_id 微信订单号 <br/>
     *            -- out_trade_no 商户订单号 **没提供transaction_id时需要传这个** <br/>
     * @return 响应结果字符串
     * @see OrderQueryParam#makeRequest(Map)
     */
    public static String refund(Map<String, String> params) {
        String reqStr = OrderQueryParam.makeRequest(params);
        try {
			return WXpayCore.postXmlSSL(WXpayConfig.refund_url, reqStr);
		} catch (KeyManagementException | UnrecoverableKeyException
				| KeyStoreException | NoSuchAlgorithmException
				| CertificateException | IOException e) {
			e.printStackTrace();
			
			return null;
		}
    }
    
    /**
     * 订单查询接口 API， 结果以map方式返回
     * @param params 请求参数<br />
     * @return 响应结果(Map形式)
     * @see WXpayApi#orderQuery(Map)
     */
    public static Map<String, String> refundRetMap(Map<String, String> params) {
        return WXpayCore.getRetMap(refund(params));
    }
}

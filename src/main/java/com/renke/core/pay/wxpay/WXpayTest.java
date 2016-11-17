package com.renke.core.pay.wxpay;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import com.renke.core.pay.wxpay.api.WXpayApi;
import com.renke.core.pay.wxpay.util.WXpayCore;

/**
 * 微信支付对接测试
 * @author eagle
 *
 */
public class WXpayTest {

    private static String out_trade_no = "2016071617151134111";

    public static void main(String[] args) throws IOException, URISyntaxException {
        System.out.println(" =============》预付款开始:");
        Map<String, String> retMap = testUnifiedorder();
        System.out.println(" =============》预付款结束:");
        System.out.println(retMap);
//        System.out.println(WXpayCore.isRetSuccess(retMap)); // 判断统一下单（预支付）接口是否成功
//        if (WXpayCore.isRetSuccess(retMap)) {
//            // 预支付成功，组装真正支付需要的参数，返回给app使用
//            System.out.println(" =============》组装app使用参数:");
//            System.out.println(WXpayApi.makePaymentMap(retMap));
//        } else {
//            System.out.println(WXpayCore.getErrMsg(retMap));
//        }
//
//        System.out.println(" =============》查询订单开始:");
//        Map<String, String> queRet = testOrderQuery();
//        System.out.println(" =============》查询订单结束:");
//        System.out.println(WXpayCore.isRetSuccess(queRet));
        
        
       System.out.println( URLEncoder.encode("http://", "UTF-8"));;
        
    }

    public static Map<String, String> testUnifiedorder() {
        Map<String, String> testMap = new HashMap<String, String>();
        testMap.put("device_info", "aaa"); // 设备号
        testMap.put("body", "私人订制离子水2"); // 商品描述
        testMap.put("detail", "离子水1，离子水2"); // 商品详情
        testMap.put("attach", "附加数据"); // 附加数据
        testMap.put("out_trade_no", out_trade_no); // 商户订单号
        testMap.put("total_fee", "1"); // 总金额
        testMap.put("spbill_create_ip", "192.168.0.1"); // 终端IP
        testMap.put("time_start", ""); // 交易起始时间
        testMap.put("time_expire", ""); // 交易结束时间

        testMap.put("goods_tag", ""); // 商品标记
        testMap.put("notify_url", "http://pay.ngrok.joinclub.cn/model-core/wxpay/pay.do"); // 通知地址
        testMap.put("trade_type", "NATIVE"); // 交易类型
        testMap.put("product_id", ""); // 商品ID
        testMap.put("openid", ""); // 用户标识
        Map<String, String> retMap = WXpayApi.unifiedOrderRetMap(testMap);

        return retMap;
    }

    public static Map<String, String> testOrderQuery() {
        Map<String, String> testMap = new HashMap<String, String>();
        testMap.put("out_trade_no", out_trade_no); // 商户订单号
        System.out.println(testMap);
        Map<String, String> retMap = WXpayApi.orderQueryRetMap(testMap);
        System.out.println(retMap);
        WXpayCore.isRetSuccess(retMap);
        return retMap;
    }
    
}

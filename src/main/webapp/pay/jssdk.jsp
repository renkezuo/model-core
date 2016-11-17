<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
<script type="text/javascript" src="http://res.wx.qq.com/open/js/jweixin-1.1.0.js"></script>
<script type="text/javascript" src="../js/jquery-1.11.1.min.js"></script>
<script type="text/javascript" src="../js/StringUtils.js"></script>
<!-- <script type="text/javascript" src="../js/jquery.xdm.js?v=1.8"></script> -->
<script>

function dopayment(){
	var mainUrl = "http://pay.ngrok.joinclub.cn";
	$.ajax({
		type: "POST",
		url: mainUrl+"/wxpay/wxprepay.do",
		dataType: "json",
		data: {},
		success: function (data) {
			if(data && data.succ != false) {
				wx.config({
				    debug: true,
				    appId: '${jsapiSignature.appid}',
				    timestamp: ''+'${jsapiSignature.timestamp}',
				    nonceStr: '${jsapiSignature.nonceStr}',
				    signature: '${jsapiSignature.signature}',
				    jsApiList: ['chooseWXPay']
				});
				
				wx.chooseWXPay({
					timestamp: ''+data.timeStamp, // 支付签名时间戳，注意微信jssdk中的所有使用timestamp字段均为小写。但最新版的支付后台生成签名使用的timeStamp字段名需大写其中的S字符
					nonceStr: ''+data.nonceStr, // 支付签名随机串，不长于 32 位
					package: ''+data.package, // 统一支付接口返回的prepay_id参数值，提交格式如：prepay_id=***）
					signType: ''+data.signType, // 签名方式，默认为'SHA1'，使用新版支付需传入'MD5'
					paySign: ''+data.paySign, // 支付签名
					success: function (res) {
						// 支付成功后的回调函数
						top.location.href = mainUrl+"/wxpay/callbackpay.do";
					},
					fail: function (res) {
						top.location.href = mainUrl+"/wxpay/callbackpay.do";
					},
					cancel: function(res){
						alert("已取消支付，可以在稍后重新支付");
					}
				});
			} else {
				alert("操作失败:" + data.msg);
			}
		}
	});
}

</script>
</head>
<body style="text-align: center">
	<input type="button"  onclick="dopayment()" value="立即支付" height="80px" width="500px"/>
</body>
</html>
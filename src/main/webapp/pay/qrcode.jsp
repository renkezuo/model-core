<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>${body}</title>
</head>
<body>
<h2>[logo]我的收银台</h2>
<br/>
订单编号：${order_no}
<br/>
订单类型：消费
<br/>
金额：${order_fee}元
<br/>
<img alt="二维码" src="http://pay.ngrok.joinclub.cn/model-core/wxpay/qrcode.do?content=${code_url}">
</body>
</html>
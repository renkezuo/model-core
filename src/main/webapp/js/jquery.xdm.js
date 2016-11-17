/**
 * jQuery xdm
 *
 * @author     rplees <rplees.i.ly@gmail.com>
 * @copyright  XDM
 * @date       2015.10.20
 * @license    GPL Version 2 (or later version) licenses.
 * @version    0.1
 */

(function($) {
	$.xdm = {
//	_lastAjaxCall : {},
	_currTimeoutId : null,
	init: function() {
		$(".maskbtn").click(function(){
			$.xdm.showMaskBox();
			return true;
		});

		$(document).ajaxStart(function(){
			$.xdm.showMaskBox();
		});

		$(document).ajaxComplete(function(){
			 $.xdm.hideMaskBox();
		});

		$(document).ajaxError(function(event, jqxhr, settings) {
			 $.xdm.hideMaskBox();
             if (jqxhr.status == 401) {
            	 if(window.confirm("你的登陆已过期，请刷新页面重试!")) {
            		 window.location.reload();
            	 }
             } else if (jqxhr.status === 502 || jqxhr.status === 500) {
         		alert("服务器繁忙，请稍后重试!");
             }
         });
	},

	ajax: function(options) {
//		options.beforeSend = function(xhr){
//			$.xdm.showMaskBox();
//		}
//
//		options.complete = function(xhr, ts) {
//			alert("complete....");
//			$.xdm.hideMaskBox();
//		}
		$.ajax(options);
	},

	onWxError: function(res) {
		if(res.errMsg == "config:invalid signature") {
			if(window.confirm("你的签名已过期，请刷新页面重试!")) {
	       		 window.location.reload();
	       	 }
		} else {
			alert(ObjUtils.toString(res));
		}
	},

	showMaskBox: function() {
		$("body").append("<div class='maskBox'><img src='"+ ctx +"/resources/images/loading.gif' /></div>");
		this._currTimeoutId = setTimeout(function(){
			$.xdm.hideMaskBox();
		}, 7000);
	},

	hideMaskBox: function() {
		$(".maskBox").remove();
		if(this._currTimeoutId) {
			clearTimeout(this._currTimeoutId);
			this._currTimeoutId = null;
		}
	},

	getCookie: function(name) {
	    var arr,reg=new RegExp("(^| )" + name + "=([^;]*)(;|$)");
	    if(arr=document.cookie.match(reg)){
	    	return unescape(arr[2]); 
		}else{
			return null; 
	    } 
	},

	shareOrderItem: function(orderId,imgUrl) {
		var setting = {
		    title: '我在“想De美”做的牙齿美白，效果超好。你也来试试吧~', // 分享标题
		    link: location.protocol + "//" + location.host + ctx + '/order/' + orderId + '/share',
		    imgUrl: imgUrl, // 分享图标
//		    imgUrl: location.protocol + "//" + location.host + ctx + '/resources/images/notPay.png', // 分享图标
		    desc:'我在“想De美”做的牙齿美白，效果超好。你也来试试吧~',// 分享描述
		    trigger: function (res) {
//		      alert('用户点击发送给朋友：' + res);
		    },
		    complete: function(res){
//		    	alert(JSON.stringify(res));
					wx.hideOptionMenu();
		    },
		    success: function () {
		        // 用户确认分享后执行的回调函数
		    	alert('分享成功.');
		    },
		    cancel: function () {
		        // 用户取消分享后执行的回调函数
		    	alert('分享取消.');
		    },
		    fail: function() {
		    	//alert(JSON.stringify(res));
		    }
		};

		wx.onMenuShareTimeline(setting);
		wx.onMenuShareQQ(setting);
		wx.onMenuShareAppMessage(setting);
		wx.onMenuShareQZone(setting);
		// 分享到腾讯微博，直接设置link参数无效？追加到desc上。
		setting.desc = setting.desc + setting.link;
		wx.onMenuShareWeibo(setting);

		//弹出分享
		wx.showOptionMenu();
		alert("请点击右上角的[...]菜单分享！");
	},

	dopayment: function(){
		var mainUrl = "http://pay.ngrok.joinclub.cn/model-core";
		$.xdm.ajax({
			type: "POST",
			url: mainUrl+"/wxpay/wxprepay.do",
			dataType: "json",
			data: {},
			success: function (data) {
				if(data && data.succ != false) {
					//alert(ObjUtils.toString(data));
					wx.chooseWXPay({
						timestamp: data.timeStamp, // 支付签名时间戳，注意微信jssdk中的所有使用timestamp字段均为小写。但最新版的支付后台生成签名使用的timeStamp字段名需大写其中的S字符
						nonceStr: data.nonceStr, // 支付签名随机串，不长于 32 位
						package: data.package, // 统一支付接口返回的prepay_id参数值，提交格式如：prepay_id=***）
						signType: data.signType, // 签名方式，默认为'SHA1'，使用新版支付需传入'MD5'
						paySign: data.paySign, // 支付签名
						success: function (res) {
							// 支付成功后的回调函数
							//alert("succ." + ObjUtils.toString(res));
							
							top.location.href = mainUrl+"/wxpay/callbackpay";
						},
						fail: function (res) {
							top.location.href = mainUrl+"/wxpay/callbackpay";
							//alert("支付失败:" + ObjUtils.toString(res));
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

  };
	
  downloadUrl:$(function(){
		var _val  = navigator.platform ;
		var _appVerson = navigator.appVersion;

	 	if(_val.indexOf("iPad") != -1 || _val.indexOf("iPhone") != -1  ){
		    //iphone
		    $("#downloadUrl").attr("href","http://a.app.qq.com/o/simple.jsp?pkgname=com.joinclub.xdm.customer");
		    $("#downloadText").text("下载iPhone");
			$("#icon").attr("src","dtewmimg/ios.png");
		   } else if(_appVerson.match(/linux/i) || _appVerson.indexOf("Android") != -1 || _val.indexOf("Linux") != -1){
		    //android
		    $("#downloadUrl").attr("href","http://fir.im/xdmcustomer");
		    //$("#downloadUrl").attr("href","http://android.myapp.com/myapp/detail.htm?apkName=com.joinclub.xdm.customer");
		    $("#downloadText").text("下载android");
			$("#icon").attr("src","dtewmimg/android.png");
	   }else{
	   		//其他
		    $("#downloadUrl").attr("href","http://www.xdmei.cn/");
			$("#icon").attr("src","dtewmimg/android.png");
	   }
	   
  });
})(jQuery);

$(document).ready(function(){
	 $.xdm.init();
});

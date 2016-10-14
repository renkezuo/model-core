package com.renke.core.exception;

/**
 * 
 * title: Code.java 
 * 错误码
 * 
 * @author rplees
 * @email rplees.i.ly@gmail.com
 * @version 1.0  
 * @created 2015年9月15日 下午1:55:59
 */
public class CommonCode {
	/**
	 * 10 用户模块
	 * 11 物品模块
	 * 12 订单模块
	 * 13 版本模块
	 * 14 短信模块
	 * 15 生活优惠
	 * 16 商品点赞
	 * 17 评论模块
	 */
	
	/**未登录*/
	public static final int NO_LOGIN = 999;
	/**访问频率限制*/
	public static final int REQUEST_FREQUENCY_LIMIT = 998;
	/**数据库操作失败*/
	public static final int SQL_EXCEPTION = 997;
	/**签名验证不通过*/
	public static final int API_SIGN_EXCEPTION = 994;
	/**数据未找到*/
	public static final int ENTITY_NOTFOUND_EXCEPTION = 993;
	/**无效非法的参数*/
	public static final int INVAILD_PARAM_EXCEPTION = 992;
	/**token验证错误*/
	public static final int TOKEN_ILLEGAL_EXCEPTION = 990;
	/**验证码不正确*/
	public static final int INVALID_VERIFY_CODE = 989;
	/**系统提示*/
	public static final int SYS_TIP = 988;
	/**访问受限制*/
	public static final int VISIT_LIMIT = 987;
	/**系统异常*/
	public static final int SYS = 986;
}

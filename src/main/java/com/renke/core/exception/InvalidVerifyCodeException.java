package com.renke.core.exception;

public class InvalidVerifyCodeException extends CodeException {

	public InvalidVerifyCodeException() {
		super(CommonCode.INVALID_VERIFY_CODE, "验证码不正确");
	}


	/**描述*/  
	private static final long serialVersionUID = 1L;
}

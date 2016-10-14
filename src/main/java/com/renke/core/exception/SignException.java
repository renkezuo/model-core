package com.renke.core.exception;

public class SignException extends CodeException {

	public SignException() {
		super(CommonCode.API_SIGN_EXCEPTION, "验签失败.");
	}
	public SignException(String msg) {
		super(CommonCode.API_SIGN_EXCEPTION, msg);
	}

	/**描述*/  
	private static final long serialVersionUID = 1L;
}

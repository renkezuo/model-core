package com.renke.core.exception;

public class TokenIllegalException extends CodeException {

	public TokenIllegalException() {
		super(CommonCode.TOKEN_ILLEGAL_EXCEPTION, "token验证非法.");
	}

	public TokenIllegalException(String detail) {
		super(CommonCode.TOKEN_ILLEGAL_EXCEPTION, "token验证非法.", detail);
	}
	
	/**描述*/  
	private static final long serialVersionUID = 1L;
}

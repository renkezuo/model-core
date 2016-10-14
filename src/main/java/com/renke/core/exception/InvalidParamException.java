package com.renke.core.exception;

public class InvalidParamException extends CodeException {

	public InvalidParamException() {
		super(CommonCode.INVAILD_PARAM_EXCEPTION, "非法参数");
	}
	
	public InvalidParamException(String msg) {
		super(CommonCode.INVAILD_PARAM_EXCEPTION, msg);
	}
	
	public InvalidParamException(String msg, String detail) {
		super(CommonCode.INVAILD_PARAM_EXCEPTION, msg, detail);
	}

	/**描述*/  
	private static final long serialVersionUID = 1L;
}

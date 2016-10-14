package com.renke.core.exception;

public class VisitLimitException extends CodeException {

	public VisitLimitException() {
		super(CommonCode.INVAILD_PARAM_EXCEPTION, "访问受限制");
	}
	
	public VisitLimitException(String msg) {
		super(CommonCode.INVAILD_PARAM_EXCEPTION, msg);
	}

	/**描述*/  
	private static final long serialVersionUID = 1L;
}

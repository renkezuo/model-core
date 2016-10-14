package com.renke.core.exception;

public class NoLoginException extends CodeException {

	public NoLoginException() {
		super(CommonCode.NO_LOGIN, "请先登陆");
	}

	/**描述*/  
	private static final long serialVersionUID = 1L;
}

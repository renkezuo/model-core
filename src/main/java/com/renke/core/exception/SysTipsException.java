package com.renke.core.exception;

public class SysTipsException extends CodeException {

	public SysTipsException(String msg) {
		super(CommonCode.SYS_TIP, msg);
	}

	/**描述*/  
	private static final long serialVersionUID = 1L;
}

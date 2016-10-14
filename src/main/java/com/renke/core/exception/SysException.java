package com.renke.core.exception;

public class SysException extends CodeException {

	public SysException(String msg) {
		super(CommonCode.SYS_TIP, "系统异常", msg);
	}

	/**描述*/  
	private static final long serialVersionUID = 1L;
}

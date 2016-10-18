package com.renke.core.exception;

public class CheckException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	private static final String COMMON_MSG = "Check Error : ";
	
	public CheckException(String msg) {
        this(msg, null);
    }

    public CheckException(Throwable cause) {
        super(COMMON_MSG, cause);
    }

    public CheckException(String msg, Throwable cause) {
        super(COMMON_MSG + msg, cause);
    }
    
}

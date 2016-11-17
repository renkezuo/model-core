package com.renke.core.exception;

public class ApacheHTTPException  extends RuntimeException {
	private static final long serialVersionUID = 1L;
	private static final String COMMON_MSG = "apache http Error : ";
	
	public ApacheHTTPException(String msg) {
        this(msg, null);
    }

    public ApacheHTTPException(Throwable cause) {
        super(COMMON_MSG, cause);
    }

    public ApacheHTTPException(String msg, Throwable cause) {
        super(COMMON_MSG + msg, cause);
    }
}

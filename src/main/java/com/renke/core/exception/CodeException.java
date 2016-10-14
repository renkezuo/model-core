package com.renke.core.exception;


public class CodeException extends RuntimeException {
	private int code;
	private String msg;
	private String detailMsg;
	
	public CodeException(int code, String msg) {
		this(code, msg, msg);
	}
	
	public CodeException(int code, String msg, String detailMsg) {
		super("XDM Error[" + code + "] : " + msg);
		this.code = code;
		this.msg = msg;
		this.detailMsg = detailMsg;
	}

	/**描述*/  
	private static final long serialVersionUID = 1L;

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getDetailMsg() {
		return detailMsg;
	}

	public void setDetailMsg(String detailMsg) {
		this.detailMsg = detailMsg;
	}
	
	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("[code:").append(this.code).append(",msg:").append(this.msg)
				.append(",detail:").append(this.detailMsg).append("]");
		return buffer.toString();
	}
}

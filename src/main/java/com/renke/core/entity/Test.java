package com.renke.core.entity;
import java.io.Serializable;
import com.renke.core.tools.ToStringTool;

public class Test implements Serializable{
	private static final long serialVersionUID = 1L;
	public String phone;
	public String login_pwd;
	public int hello;
	public String nickName;
	public RequestFile[] file;
	public RequestFile[] file2;
	
	@Override
	public String toString() {
		return ToStringTool.entityToString(this);
	}
}

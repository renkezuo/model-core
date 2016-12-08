package com.renke.core.entity;

import java.io.Serializable;

import com.renke.core.annotations.Column;
import com.renke.core.annotations.Table;

@Table("t_employee")
public class Employee implements Serializable{
	private static final long serialVersionUID = 1L;
	public Integer id;
	public String name;
	public String phone;
	public String cardType;
	public String cardNo;
	public String job;
	
	@Column(unColumn=true)
	public RequestFile headPic;
	
	public String headPicPath;
	public String status;
}

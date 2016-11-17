package com.renke.core.entity;
import java.io.Serializable;
import java.sql.Blob;
import java.sql.Timestamp;

import com.renke.core.annotations.Column;
import com.renke.core.annotations.Table;
import com.renke.core.tools.ToStringTool;

@Table("t_test")
public class Test implements Serializable{
	private static final long serialVersionUID = 1L;
	@Column(isPrimary=true)
	public Integer id;
	public String name;
	public Timestamp birthday;
	public Integer sex;
	public Blob headpic;
	public byte[] fullpic;
	
	@Override
	public String toString() {
		return ToStringTool.entityToString(this);
	}
}

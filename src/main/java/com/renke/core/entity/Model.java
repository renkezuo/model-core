package com.renke.core.entity;
import java.io.Serializable;
import java.util.Date;

import com.renke.core.annotations.Column;
import com.renke.core.annotations.Table;
import com.renke.core.tools.ToStringTool;

@Table("t_log")
public class Model implements Serializable{
	private static final long serialVersionUID = 1L;
	@Column(isPrimary=true)
	private Integer id;
	private String ip;
	private Integer port;
	@Column(colName="time")
	private Date myTime;
	@Column(unColumn=true)
	private String search;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public Integer getPort() {
		return port;
	}
	public void setPort(Integer port) {
		this.port = port;
	}
//	public Date getTime() {
//		return time;
//	}
//	public void setTime(Date time) {
//		this.time = time;
//	}
	public String getSearch() {
		return search;
	}
	public void setSearch(String search) {
		this.search = search;
	}
	
	@Override
	public String toString() {
		return ToStringTool.entityToString(this);
	}
}

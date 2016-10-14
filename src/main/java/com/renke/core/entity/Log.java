package com.renke.core.entity;
import com.renke.core.annotations.Column;
import com.renke.core.annotations.Table;

@Table("t_log")
public class Log {
	@Column(isPrimary=true)
	private Integer id;
	private String ip;
	private Integer port;
	private String time;
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
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getSearch() {
		return search;
	}
	public void setSearch(String search) {
		this.search = search;
	}
	
}

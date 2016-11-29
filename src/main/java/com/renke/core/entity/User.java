package com.renke.core.entity;
import java.io.Serializable;

import com.renke.core.annotations.Column;
import com.renke.core.annotations.Table;
import com.renke.core.tools.ToStringTool;

@Table("t_user")
public class User implements Serializable{
	private static final long serialVersionUID = 1L;
	@Column(isPrimary=true)
	private Integer id;
	private String login_key;
	private String login_pwd;
	private String nickname;
	private String email;
	private String phone;
	private Integer primary_dept_id;
	private Integer primary_org_id;
	private Integer primary_role_id;
	private String dept_ids;
	private String org_ids;
	private String role_ids;
	private String status;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getLogin_key() {
		return login_key;
	}

	public void setLogin_key(String login_key) {
		this.login_key = login_key;
	}

	public String getLogin_pwd() {
		return login_pwd;
	}

	public void setLogin_pwd(String login_pwd) {
		this.login_pwd = login_pwd;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Integer getPrimary_dept_id() {
		return primary_dept_id;
	}

	public void setPrimary_dept_id(Integer primary_dept_id) {
		this.primary_dept_id = primary_dept_id;
	}

	public Integer getPrimary_org_id() {
		return primary_org_id;
	}

	public void setPrimary_org_id(Integer primary_org_id) {
		this.primary_org_id = primary_org_id;
	}

	public Integer getPrimary_role_id() {
		return primary_role_id;
	}

	public void setPrimary_role_id(Integer primary_role_id) {
		this.primary_role_id = primary_role_id;
	}

	public String getDept_ids() {
		return dept_ids;
	}

	public void setDept_ids(String dept_ids) {
		this.dept_ids = dept_ids;
	}

	public String getOrg_ids() {
		return org_ids;
	}

	public void setOrg_ids(String org_ids) {
		this.org_ids = org_ids;
	}

	public String getRole_ids() {
		return role_ids;
	}

	public void setRole_ids(String role_ids) {
		this.role_ids = role_ids;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return ToStringTool.entityToString(this);
	}
}

package com.renke.core.dao;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.renke.core.db.JdbcExecutor;

@Component
public class BaseDao<Entity> {
	@Resource JdbcExecutor<Entity> mysql;
	public void insert(Entity entity){
		mysql.insertEntity(entity);
	}
	
	public void update(Entity entity){
		mysql.updateEntity(entity);
	}
	
	public JdbcExecutor<Entity> getMysql(){
		return mysql;
	}
	
}

package com.renke.core.dao;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.renke.core.db.JdbcExecutor;
import com.renke.core.entity.Model;
@Component
public class TestDao {
	@Resource JdbcExecutor<Model> sqlite;
	@Resource JdbcExecutor<Model> mysql;
	public void insertMysql(Model entity){
		mysql.insertEntity(entity);
	}
	
	public void insertSqlite(Model entity){
		sqlite.insertEntity(entity);
	}
}

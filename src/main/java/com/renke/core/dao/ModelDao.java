package com.renke.core.dao;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.renke.core.db.JdbcExecutor;
@Component
public class ModelDao {
	@Resource JdbcExecutor mysql;
	public void insert(String sql,List<String> params){
		mysql.update(sql, params.toArray());
	}
	public <Entity> void insert(Entity entity){
		mysql.insertEntity(entity);
	}
	
	public <Entity> void update(Entity entity){
		mysql.updateEntity(entity);
	}
}

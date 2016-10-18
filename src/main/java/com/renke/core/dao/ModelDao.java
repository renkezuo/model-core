package com.renke.core.dao;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.renke.core.db.JdbcExecutor;
import com.renke.core.entity.Model;
@Component
public class ModelDao {
	@Resource JdbcExecutor<Model> mysql;
	public void insert(String sql,List<String> params){
		mysql.update(sql, params.toArray());
	}
	public void insert(Model entity){
		mysql.insertEntity(entity);
	}
	
	public void update(Model entity){
		mysql.updateEntity(entity);
	}
	
	public Model get(Integer id){
		return mysql.selectEntityByKey(id,Model.class);
	}
	
	public Model get(Model model){
		return mysql.selectEntityByEntity(model);
	}
	
	public List<Model> getList(Model model){
		return mysql.selectListEntityByEntity(model);
	}
	
}

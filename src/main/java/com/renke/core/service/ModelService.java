package com.renke.core.service;


import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.renke.core.dao.ModelDao;
import com.renke.core.entity.Model;

@Component
public class ModelService {
	@Autowired
	ModelDao modelDao;
	
	@Transactional
	public void insertModel(Model log){
		modelDao.insert(log);
	}
	@Transactional
	public void updateModel(Model log){
		modelDao.update(log);
	}
	public Model selectModel(Model log){
		return modelDao.get(log);
	}
	public Model selectModelById(Integer id){
		return modelDao.get(id);
	}
	public List<Model> selectList(Model log){
		return modelDao.getList(log);
	}
	public List<Map<String,Object>> selectList2(Model log){
		return modelDao.getList2(log);
	}
}

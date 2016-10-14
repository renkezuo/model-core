package com.renke.core.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.renke.core.dao.ModelDao;
import com.renke.core.entity.Log;

@Component
public class ModelService {
	@Autowired
	ModelDao modelDao;
	
	@Transactional
	public void insertLog(Log log){
		modelDao.insert(log);
	}
	@Transactional
	public void updateLog(Log log){
		modelDao.update(log);
	}
}

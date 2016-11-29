package com.renke.core.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.renke.core.dao.TestDao;
import com.renke.core.entity.Model;

@Service
public class TestService {
	@Autowired
	TestDao testDao;
	
	@Transactional("mysqlManager")
	public void transactionMysql(){
		Model model = new Model();
		model.setId(3);
		model.setPort(91);
		model.setIp("112.124.38.22");
		testDao.insertMysql(model);
		model = new Model();
		model.setId(10);
		model.setPort(91);
		model.setIp("112.124.38.223333333333333");
		testDao.insertMysql(model);
	}
	
	@Transactional("sqliteManager")
	public void transactionSqlite(){
		Model model = new Model();
		model.setId(4);
		model.setPort(91);
		model.setIp("112.124.38.22");
		testDao.insertSqlite(model);
		model = new Model();
		model.setPort(91);
		model.setIp("112.124.38.223333333333333");
		testDao.insertSqlite(model);
	}
}

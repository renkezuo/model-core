package com.renke.core.db;

import java.sql.SQLException;

import javax.sql.DataSource;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class TransactionManager {
	private static final Logger logger = LoggerFactory.getLogger(TransactionManager.class);
	@Autowired
	private DataSource tomcatDataSource ;
	@Pointcut("execution(* com.renke..*Service.save*(..)) ||" +
			" execution(* com.renke..*Service.insert*(..)) ||" +
			" execution(* com.renke..*Service.delete*(..)) ||" +
			" execution(* com.renke..*Service.update*(..)) ||" +
			" execution(* com.renke..*Service.execute*(..)) ")
	public void transactionMethod(){}
	
	@After("transactionMethod()")
	public void closeConnection(JoinPoint jp) throws SQLException{
		logger.info(tomcatDataSource.getConnection().toString());
		logger.info("closeConnectionMethod()");
//		System.out.println(jp.getTarget());
//		System.out.println(jp.getThis());
//		System.out.println(jp.getTarget());
//		System.out.println(jp.getKind());
//		System.out.println(jp.getArgs().length);
//		for(Object o : jp.getArgs()){
//			System.out.println(o);
//		}
	}
}
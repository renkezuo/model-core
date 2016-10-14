package com.renke.core.db;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.jdbc.support.JdbcUtils;

import com.renke.core.tools.CheckTool;
import com.renke.core.tools.EntityTool;

public class JdbcExecutor<Entity> extends JdbcTemplate{
	private final static Logger logger = LoggerFactory.getLogger(JdbcExecutor.class);
	public int insertEntity(Entity entity) throws DataAccessException {
		Connection con = DataSourceUtils.getConnection(getDataSource());
		PreparedStatement pstmt = null;
		JdbcEntity je = EntityTool.toInsertJdbcEntity(entity);
		try {
			pstmt = con.prepareStatement(je.getSql(),Statement.RETURN_GENERATED_KEYS);
			for(int i =0 ;i < je.getTypes().length;i++){
				pstmt.setObject(i+1, je.getValues()[i], je.getTypes()[i].getJdbcType());
			}
			int m = pstmt.executeUpdate();
			if(CheckTool.isNull(je.getPrimaryVal())){
				Field key = EntityTool.getPrimaryField(entity);
				if(CheckTool.isNotNull(key)){
					ResultSet rs = pstmt.getGeneratedKeys();
					rs.next();
					Object primaryVal = rs.getObject(1,key.getType());
					EntityTool.setField(entity, key.getName(), primaryVal);
				}
			}
			logger.info("insertSql:{}",je.getSql());
			je = null;
			return m;
		}catch (SQLException ex) {
			JdbcUtils.closeStatement(pstmt);
			pstmt = null;
			DataSourceUtils.releaseConnection(con, getDataSource());
			con = null;
			throw getExceptionTranslator().translate("StatementCallback", je.getSql(), ex);
		}
		finally {
			JdbcUtils.closeStatement(pstmt);
			DataSourceUtils.releaseConnection(con, getDataSource());
		}
	}
	
	public int updateEntity(Entity entity) throws DataAccessException {
		Connection con = DataSourceUtils.getConnection(getDataSource());
		PreparedStatement pstmt = null;
		JdbcEntity je = EntityTool.toUpdateJdbcEntity(entity);
		try {
			pstmt = con.prepareStatement(je.getSql(),Statement.RETURN_GENERATED_KEYS);
			for(int i =0 ;i < je.getTypes().length;i++){
				pstmt.setObject(i+1, je.getValues()[i], je.getTypes()[i].getJdbcType());
			}
			int m = pstmt.executeUpdate();
			logger.info("updateSql:{}",je.getSql());
			je = null;
			return m;
		}catch (SQLException ex) {
			JdbcUtils.closeStatement(pstmt);
			pstmt = null;
			DataSourceUtils.releaseConnection(con, getDataSource());
			con = null;
			throw getExceptionTranslator().translate("StatementCallback", je.getSql(), ex);
		}
		finally {
			JdbcUtils.closeStatement(pstmt);
			DataSourceUtils.releaseConnection(con, getDataSource());
		}
	}
	
	@SuppressWarnings("unchecked")
	public Entity selectEntityById(Object id){
		Connection con = DataSourceUtils.getConnection(getDataSource());
		PreparedStatement pstmt = null;
		Class<?> c = EntityTool.getEntityClass(this.getClass(), "selectEntityById", Object.class);
		try {
			Entity obj = (Entity)c.newInstance();
		} catch (ReflectiveOperationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
//		Entity entity = new Entity();
//		JdbcEntity je = EntityTool.toUpdateJdbcEntity(entity);
//		try {
//			pstmt = con.prepareStatement(je.getSql(),Statement.RETURN_GENERATED_KEYS);
//			for(int i =0 ;i < je.getTypes().length;i++){
//				pstmt.setObject(i+1, je.getValues()[i], je.getTypes()[i].getJdbcType());
//			}
//			int m = pstmt.executeUpdate();
//			logger.info("updateSql:{}",je.getSql());
//			je = null;
//			return m;
//		}catch (SQLException ex) {
//			JdbcUtils.closeStatement(pstmt);
//			pstmt = null;
//			DataSourceUtils.releaseConnection(con, getDataSource());
//			con = null;
//			throw getExceptionTranslator().translate("StatementCallback", je.getSql(), ex);
//		}
//		finally {
//			JdbcUtils.closeStatement(pstmt);
//			DataSourceUtils.releaseConnection(con, getDataSource());
//		}
	}
	
}

package com.renke.core.db;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

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
	
	/**
	 * 如果primaryKey不存在，则返回primaryKey
	 * @param entity
	 * @return
	 * @throws DataAccessException
	 * @author renke.zuo@foxmail.com
	 * @time 2016-10-17 13:59:57
	 */
	public int insertEntity(Entity entity) throws DataAccessException {
		Connection con = DataSourceUtils.getConnection(getDataSource());
		PreparedStatement pstmt = null;
		JdbcEntity je = EntityTool.toInsertJdbcEntity(entity);
		try {
			pstmt = con.prepareStatement(je.getSql(),Statement.RETURN_GENERATED_KEYS);
			for(int i =0 ;i < je.getTypes().length;i++){
				pstmt.setObject(i+1, je.getValues()[i], je.getTypes()[i]);
			}
			int m = pstmt.executeUpdate();
			if(CheckTool.isNull(je.getPrimaryVal())){
				Field key = EntityTool.getPrimaryField(entity);
				if(CheckTool.isNotNull(key)){
					ResultSet rs = pstmt.getGeneratedKeys();
					rs.next();
					Object primaryVal = rs.getObject(1,key.getType());
					EntityTool.setField(entity, key, primaryVal);
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
		}finally {
			JdbcUtils.closeStatement(pstmt);
			DataSourceUtils.releaseConnection(con, getDataSource());
		}
	}
	
	public int updateEntity(Entity entity) throws DataAccessException {
		return executeUpdate(EntityTool.toUpdateJdbcEntity(entity));
	}
	
	public int deleteEntity(Entity entity) throws DataAccessException {
		return executeUpdate(EntityTool.toDeleteJdbcEntity(entity));
	}
	
	public Entity selectEntityByKey(Object id,Class<Entity> clazz) throws DataAccessException {
		Connection con = DataSourceUtils.getConnection(getDataSource());
		PreparedStatement pstmt = null;
		JdbcEntity je = EntityTool.toSelectById(id, clazz);
		try {
			logger.info("selectByKeySql:{}",je.getSql());
			Entity entity = (Entity)clazz.newInstance();
			pstmt = con.prepareStatement(je.getSql());
			pstmt.setObject(1, je.getPrimaryVal(),je.getPrimaryType());
			ResultSet rs = pstmt.executeQuery();
			setEntityByResultSet(rs, entity);
			je = null;
			rs = null;
			return entity;
		}catch (SQLException e) {
			JdbcUtils.closeStatement(pstmt);
			pstmt = null;
			DataSourceUtils.releaseConnection(con, getDataSource());
			con = null;
			throw getExceptionTranslator().translate("StatementCallback", je.getSql(), e);
		}catch(ReflectiveOperationException e){
			logger.error("{}",e.getMessage());
		}finally {
			JdbcUtils.closeStatement(pstmt);
			DataSourceUtils.releaseConnection(con, getDataSource());
		}
		return null;
	}
	
	public Entity selectEntityByEntity(Entity entity) throws DataAccessException {
		Connection con = DataSourceUtils.getConnection(getDataSource());
		PreparedStatement pstmt = null;
		JdbcEntity je = EntityTool.toSelectByEntity(entity);
		try {
			logger.info("selectByEntitySql:{}",je.getSql());
			pstmt = con.prepareStatement(je.getSql());
			for(int i =0 ;i < je.getTypes().length;i++){
				pstmt.setObject(i+1, je.getValues()[i], je.getTypes()[i]);
			}
			ResultSet rs = pstmt.executeQuery();
			setEntityByResultSet(rs, entity);
			je = null;
			rs = null;
			return entity;
		}catch (SQLException e) {
			JdbcUtils.closeStatement(pstmt);
			pstmt = null;
			DataSourceUtils.releaseConnection(con, getDataSource());
			con = null;
			throw getExceptionTranslator().translate("StatementCallback", je.getSql(), e);
		}catch(ReflectiveOperationException e){
			e.printStackTrace();
			logger.error("{}",e.getMessage());
			return null;
		}finally {
			JdbcUtils.closeStatement(pstmt);
			DataSourceUtils.releaseConnection(con, getDataSource());
		}
	}

	@SuppressWarnings("unchecked")
	public List<Entity> selectListEntityByEntity(Entity entity) throws DataAccessException {
		Connection con = DataSourceUtils.getConnection(getDataSource());
		PreparedStatement pstmt = null;
		JdbcEntity je = EntityTool.toSelectByEntity(entity);
		List<Entity> list = new ArrayList<Entity>();
		try {
			pstmt = con.prepareStatement(je.getSql());
			for(int i =0 ;i < je.getTypes().length;i++){
				pstmt.setObject(i+1, je.getValues()[i], je.getTypes()[i]);
			}
			ResultSet rs = pstmt.executeQuery();
			logger.info("selectListByEntitySql:{}",je.getSql());
			do{
				Entity e = (Entity)entity.getClass().newInstance();
				setEntityByResultSet(rs, e);
				list.add(e);
			}while(CheckTool.isNotTrue(rs.isLast()));
			je = null;
			rs = null;
			return list;
		}catch (SQLException e) {
			JdbcUtils.closeStatement(pstmt);
			pstmt = null;
			DataSourceUtils.releaseConnection(con, getDataSource());
			con = null;
			throw getExceptionTranslator().translate("StatementCallback", je.getSql(), e);
		}catch(ReflectiveOperationException e){
			logger.error("{}",e.getMessage());
			return null;
		}finally {
			JdbcUtils.closeStatement(pstmt);
			DataSourceUtils.releaseConnection(con, getDataSource());
		}
	}
	
	public int executeUpdate(JdbcEntity je) throws DataAccessException {
		Connection con = DataSourceUtils.getConnection(getDataSource());
		PreparedStatement pstmt = null;
		try {
			pstmt = con.prepareStatement(je.getSql());
			for(int i =0 ;i < je.getTypes().length;i++){
				pstmt.setObject(i+1, je.getValues()[i], je.getTypes()[i]);
			}
			int m = pstmt.executeUpdate();
			logger.info("deleteSql:{}",je.getSql());
			je = null;
			return m;
		}catch (SQLException ex) {
			JdbcUtils.closeStatement(pstmt);
			pstmt = null;
			DataSourceUtils.releaseConnection(con, getDataSource());
			con = null;
			throw getExceptionTranslator().translate("StatementCallback", je.getSql(), ex);
		}finally {
			JdbcUtils.closeStatement(pstmt);
			DataSourceUtils.releaseConnection(con, getDataSource());
		}
	}
	
	private void setEntityByResultSet(ResultSet rs , Entity entity) throws SQLException, ReflectiveOperationException, SecurityException{
		JdbcEntity je = EntityTool.assembleJdbcEntity(entity.getClass());
		String[] fieldNames = je.getFieldNames();
		String[] colNames = je.getColNames();
		if(rs.next()){
			for(int i=0 ; i< fieldNames.length ; i++){
				String fieldName = fieldNames[i];
				EntityTool.setField(entity,entity.getClass().getDeclaredField(fieldName),rs.getObject(colNames[i]));
			}
		}
	}
}

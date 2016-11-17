package com.renke.core.db;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementCreatorFactory;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapperResultSetExtractor;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.jdbc.support.JdbcUtils;

import com.renke.core.tools.CheckTool;
import com.renke.core.tools.EntityTool;
import com.renke.core.tools.ResultSetTool;

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
	
	@SuppressWarnings("unchecked")
	public Entity selectEntityByKey(Object id,Class<Entity> clazz) throws DataAccessException {
		JdbcEntity je = EntityTool.toSelectById(id, clazz);
		return (Entity)queryOne(je.getSql(), new Integer[]{je.getPrimaryType()}, new Object[]{je.getPrimaryVal()}, clazz);
	}
	
	@SuppressWarnings("unchecked")
	public Entity selectEntityByEntity(Entity entity) throws DataAccessException {
		JdbcEntity je = EntityTool.toSelectByEntity(entity);
		return (Entity)queryOne(je.getSql(), je.getTypes(), je.getValues(), entity.getClass());
	}

	@SuppressWarnings("unchecked")
	public List<Entity> selectListEntityByEntity(Entity entity) throws DataAccessException {
		JdbcEntity je = EntityTool.toSelectByEntity(entity);
		return (List<Entity>) query(je.getSql(),je.getTypes(),je.getValues(),entity.getClass());
	}
	
	/**
	 * val 类型，只能有String,Date,Integer
	 * @return
	 * @author renke.zuo@foxmail.com
	 * @time 2016-10-19 16:49:57
	 */
	public List<Map<String,Object>> queryForListM(String sql,Object[] val){
		CheckTool.throwBlank(sql, "sql语句不能为空");
		int[] types = ResultSetTool.getTypes(val);
		PreparedStatementCreatorFactory pscf = new PreparedStatementCreatorFactory(sql,types);
		PreparedStatementCreator psc = pscf.newPreparedStatementCreator(val);
		PreparedStatementSetter pss = (PreparedStatementSetter)psc;
		ResultSetExtractor<List<Map<String,Object>>> rse = new RowMapperResultSetExtractor<Map<String,Object>>(getColumnMapRowMapper());
		return query(psc,pss,rse);
	}
	
	/**
	 * @FIXME:sql,数据,分页
	 * 
	 * @return
	 * @author renke.zuo@foxmail.com
	 * @time 2016-10-19 16:49:57
	 */
	public List<Entity> queryForListE(String sql,Object[] val,Class<Entity> clazz){
		CheckTool.throwBlank(sql, "sql语句不能为空");
		int[] types = ResultSetTool.getTypes(val);
		PreparedStatementCreatorFactory pscf = new PreparedStatementCreatorFactory(sql,types);
		PreparedStatementCreator psc = pscf.newPreparedStatementCreator(val);
		PreparedStatementSetter pss = (PreparedStatementSetter)psc;
//		JdbcUtils.getResultSetValue(rs, index);
		ResultSetExtractor<List<Entity>> rse = new RowMapperResultSetExtractor<Entity>(new AnnoBeanRowMapper<Entity>(clazz));
		return query(psc,pss,rse);
	}
	
	private List<?> query(String sql,Integer[] types,Object[] val,Class<?> clazz){
		Connection con = DataSourceUtils.getConnection(getDataSource());
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			logger.info("querySql:{}",sql);
			pstmt = con.prepareStatement(sql);
			for(int i =0 ;i < types.length;i++){
				pstmt.setObject(i+1, val[i], types[i]);
			}
			rs = pstmt.executeQuery();
			List<?> list = null;
			if(CheckTool.isNull(clazz)){
				list = ResultSetTool.getListEntityByResultSet(rs, clazz);
			}else{
				list = ResultSetTool.getListMapByResultSet(rs);
			}
			return list;
		}catch (SQLException e) {
			JdbcUtils.closeStatement(pstmt);
			pstmt = null;
			DataSourceUtils.releaseConnection(con, getDataSource());
			con = null;
			throw getExceptionTranslator().translate("StatementCallback", sql, e);
		}catch(ReflectiveOperationException e){
			logger.error("{}",e.getMessage());
			return null;
		}finally {
			JdbcUtils.closeResultSet(rs);
			JdbcUtils.closeStatement(pstmt);
			DataSourceUtils.releaseConnection(con, getDataSource());
		}
	}
	
	
	/**
	 * @TODO:将types设置为自动获取[根据val的类型]
	 * 
	 * @param sql
	 * @param types
	 * @param val
	 * @param clazz
	 * @return
	 * @author renke.zuo@foxmail.com
	 * @time 2016-10-19 17:23:52
	 */
	private Object queryOne(String sql,Integer[] types,Object[] val,Class<?> clazz){
		Connection con = DataSourceUtils.getConnection(getDataSource());
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			logger.info("querySql:{}",sql);
			pstmt = con.prepareStatement(sql);
			for(int i =0 ;i < types.length;i++){
				pstmt.setObject(i+1, val[i], types[i]);
			}
			rs = pstmt.executeQuery();
			Object obj = null;
			if(rs.next()){
				if(CheckTool.isNull(clazz)){
					obj = ResultSetTool.getMap(rs);
				}else{
					obj = ResultSetTool.getEntity(rs, clazz);
				}
			}
			return obj;
		}catch (SQLException e) {
			JdbcUtils.closeStatement(pstmt);
			pstmt = null;
			DataSourceUtils.releaseConnection(con, getDataSource());
			con = null;
			throw getExceptionTranslator().translate("StatementCallback", sql, e);
		}catch(ReflectiveOperationException e){
			logger.error("{}",e.getMessage());
			return null;
		}finally {
			JdbcUtils.closeResultSet(rs);
			JdbcUtils.closeStatement(pstmt);
			DataSourceUtils.releaseConnection(con, getDataSource());
		}
	}

}

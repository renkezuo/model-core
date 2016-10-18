package com.renke.core.tools;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.renke.core.annotations.Column;
import com.renke.core.annotations.Table;
import com.renke.core.db.JdbcEntity;
import com.renke.core.entity.Model;
import com.renke.core.enums.MethodType;

/***
 * 实体类操作工具
 * @author renke.zuo@foxmail.com
 * @time 2016-10-12 14:33:01
 */
public class EntityTool {
	private final static Logger logger = LoggerFactory.getLogger(EntityTool.class);
	/**
	 * 根据entity对象，装配Insert SQL
	 * 非字段，不参与装配
	 * 字段内容为null，不参与装配
	 * @param entity
	 * @return
	 * @author renke.zuo@foxmail.com
	 * @time 2016-10-12 14:33:15
	 */
	public final static <Entity> JdbcEntity toInsertJdbcEntity(Entity entity){
		final JdbcEntity je = assembleJdbcEntity(entity);
		je.setSql(SQLTool.getInsertSql(je.getColNames(), je.getTableName()));
		return je;
	}
	
	/**
	 * 根据entity对象，装配Update SQL
	 * 非字段，不参与装配
	 * 字段内容为null，不参与装配
	 * entity对象必须有主键，且值不为空
	 * @param entity
	 * @return
	 * @author renke.zuo@foxmail.com
	 * @time 2016-10-12 14:33:15
	 */
	public final static <Entity> JdbcEntity toUpdateJdbcEntity(Entity entity){
		final JdbcEntity je = assembleJdbcEntity(entity);
		je.setSql(SQLTool.getUpdateSql(je.getColNames(), je.getTableName(), je.getPrimaryCol()));
		return je;
	}
	
	/**
	 * 根据entity对象，装配Delete SQL
	 * 非字段，不参与装配
	 * 字段内容为null，不参与装配
	 * 删除匹配entity对象中非空字段的全部数据
	 * @param entity
	 * @return
	 * @author renke.zuo@foxmail.com
	 * @time 2016-10-12 14:33:15
	 */
	public final static <Entity> JdbcEntity toDeleteJdbcEntity(Entity entity){
		final JdbcEntity je = assembleJdbcEntity(entity);
		je.setSql(SQLTool.getDeleteSql(je.getColNames(), je.getTableName()));
		return je;
	}
	
	public final static <Entity> JdbcEntity toSelectById(Object id,Class<Entity> clazz){
		final JdbcEntity je = assembleJdbcEntity(clazz);
		je.setPrimaryVal(id);
		je.setSql(SQLTool.getSelectById(je.getTableName(), je.getPrimaryCol()));
		return je;
	}
	
	public final static <Entity> JdbcEntity toSelectByEntity(Entity entity){
		final JdbcEntity je = assembleJdbcEntity(entity);
		final StringBuilder result = new StringBuilder();
		result.append(" select * from ").append(je.getTableName()).append(" where ");
		final String[] colNames = je.getColNames();
		for(int i=0;i<colNames.length;i++){
			result.append(colNames[i]).append("=? and ");
		}
		result.delete(result.length()-4,result.length());
		je.setSql(result.toString());
		return je;
	}
	
	/**
	 * 获取field值
	 * @param field
	 * @param entity
	 * @return
	 * @author renke.zuo@foxmail.com
	 * @time 2016-10-17 14:47:16
	 */
	public final static <Entity> Object getFieldVal(Field field,Entity entity){
		if(CheckTool.isNull(field)){
			return null;
		}else{
			try {
				field.setAccessible(true);
				return field.get(entity);
			} catch (IllegalArgumentException | IllegalAccessException e) {
				logger.debug("field read error");
				return null;
			}
		}

	}
	
	@Deprecated
	public final static <Entity> Object getFieldVal(String fieldName,Entity entity){
		String methodName = getMethodNameByFieldName(fieldName,MethodType.GET);
		try{
			Method m = entity.getClass().getMethod(methodName);
			return m.invoke(entity);
		} catch ( SecurityException | ReflectiveOperationException | IllegalArgumentException ex) {
			return null;
		}
	}
	
	
//	public final static <Entity> void setField(Entity entity,Field field,Object fieldValue){
//		String methodName = getMethodNameByFieldName(field.getName(),MethodType.SET);
//		try {
//			Method m = entity.getClass().getMethod(methodName,field.getType());
//			m.invoke(entity, fieldValue);
//		} catch ( SecurityException | ReflectiveOperationException | IllegalArgumentException e) {
//			logger.error("error:{}",e.getMessage());
//			throw new IllegalArgumentException("not found ："+e.getMessage());
//		}
//	}
	
	/**
	 * 设置field值
	 * @param entity
	 * @param field
	 * @param fieldValue
	 * @author renke.zuo@foxmail.com
	 * @time 2016-10-12 14:34:50
	 */
	public final static <Entity> void setField(Entity entity,Field field,Object fieldValue){
		if(CheckTool.isNull(field)){
			throw new IllegalArgumentException("成员参数不存在！");
		}else{
			try {
				field.setAccessible(true);
				field.set(entity,fieldValue);
				return ;
			} catch (IllegalArgumentException | IllegalAccessException e) {
				logger.debug("field write error");
				return ;
			}
		}
	}
	
	/**
	 * 通过成员变量，获取成员对应的get/set方法名
	 * @param field
	 * @param methodType
	 * @return
	 * @author renke.zuo@foxmail.com
	 * @time 2016-10-12 15:12:24
	 */
	public final static String getMethodNameByFieldName(String fieldName,MethodType methodType){
		String methodName = fieldName;
		char[] chars = fieldName.toCharArray();
		char[] results = new char[chars.length+3];
		if(methodType == MethodType.GET){
			results[0] = 'g';
			results[1] = 'e';
			results[2] = 't';
		}else{
			results[0] = 's';
			results[1] = 'e';
			results[2] = 't';
		}
		results[3] = Character.toUpperCase(chars[0]);
		methodName.getChars(1, chars.length, results, 4);
		methodName = new String(results);
		chars = null;
		results = null;
		return methodName;
	}
	
	/**
	 * 获取主键变量
	 * @param t
	 * @return
	 * @author renke.zuo@foxmail.com
	 * @time 2016-10-12 17:22:10
	 */
	public final static <Entity> Field getPrimaryField(Entity entity){
		Class<?> clazz = entity.getClass();
		Field[] fields = clazz.getDeclaredFields();
		for(Field field:fields){
			Column col = field.getAnnotation(Column.class);
			if(CheckTool.isNotNull(col)){
				if(col.isPrimary()){
					return field;
				}
			}
		}
		return null;
	}
	
	
	public final static Class<?> getEntityClass(Class<?> clazz,String methodName,Class<?>... parameterTypes){
		try {
			Method method = clazz.getMethod(methodName, parameterTypes);
			return method.getReturnType();
		} catch (NoSuchMethodException | SecurityException e) {
			throw new IllegalArgumentException("获取Entity对象失败！");
		}
	}
	
	/**
	 * 通过entity对象，组装JdbcEntity对象
	 * @param entity
	 * @return
	 * @author renke.zuo@foxmail.com
	 * @time 2016-10-13 14:58:28
	 */
	public final static <Entity> JdbcEntity assembleJdbcEntity(Entity entity){
		JdbcEntity je = new JdbcEntity();
		boolean isClazz = entity instanceof java.lang.Class<?>;
		Class<?> clazz = null;
		if(isClazz){
			clazz = (Class<?>)entity;
		}else{
			clazz = entity.getClass();
		}
		Table table = clazz.getAnnotation(Table.class);
		if(CheckTool.isNull(table)){
			throw new IllegalArgumentException(clazz.getName()+"必须声明为["+Table.class.getName()+"]注解!");
		}
		Field[] fields = clazz.getDeclaredFields();
		List<Object> values = new ArrayList<>();
		List<Integer> types = new ArrayList<>();
		List<String> colNames = new ArrayList<>();
		List<String> fieldNames = new ArrayList<>();
		Object fieldVal = null;
		for(Field field : fields){
			Column col = field.getAnnotation(Column.class);
			String fieldName = field.getName();
			if(CheckTool.isEquals("serialVersionUID", fieldName)){
				continue;
			}
			if(CheckTool.isNotTrue(isClazz)){
				fieldVal = getFieldVal(field, entity);
			}
			String colName = null;
			//默认，字段名等于变量名
			if(CheckTool.isNull(col)){
				colName = fieldName;
			}else{
				//非字段为否
				if(!col.unColumn()){
					if(CheckTool.isBlank(col.colName())){
						colName = fieldName;
					}else{
						colName = col.colName();
					}
					//主键时，将主键信息加入JdbcEntity中
					if(CheckTool.isTrue(col.isPrimary())){
						if(CheckTool.isNotBlank(je.getPrimaryCol())){
							throw new IllegalArgumentException(clazz.getName()+"，不支持多主键！");
						}
						je.setPrimaryCol(colName);
						je.setPrimaryField(fieldName);
						je.setPrimaryType(SQLTool.getSqlTypes(field.getType().getSimpleName()));
						je.setPrimaryVal(fieldVal);
					}
				}
			}
			//传入参数值非class时，fieldVal为空，则不处理该字段
			if(CheckTool.isNotTrue(isClazz) && CheckTool.isNull(fieldVal)){
				continue;
			}
			//字段名称不能为空
			if(CheckTool.isNotBlank(colName)){
				//column注解为空，或者字段非否
				if( CheckTool.isNull(col) || CheckTool.isNotTrue(col.isPrimary()) ){
					values.add(fieldVal);
					types.add(SQLTool.getSqlTypes(field.getType().getSimpleName()));
					colNames.add(colName);
					fieldNames.add(fieldName);
				//主键时，如果传入参数为class
				}else if(CheckTool.isTrue(isClazz)){
					values.add(fieldVal);
					types.add(SQLTool.getSqlTypes(field.getType().getSimpleName()));
					colNames.add(colName);
					fieldNames.add(fieldName);
				}
			}
		}
		//如果传入参数非class，同时主键有值，则将值加入到列表最后
		if( CheckTool.isNotTrue(isClazz) && CheckTool.isNotNull(je.getPrimaryVal())){
			values.add(je.getPrimaryVal());
			types.add(je.getPrimaryType());
			colNames.add(je.getPrimaryCol());
			fieldNames.add(je.getPrimaryCol());
		}
		if(colNames.size()<=0){
			throw new IllegalArgumentException(clazz.getName()+"没有可操作字段!");
		}
		je.setTableName(table.value());
		je.setValues(values.toArray());
		je.setTypes(types.toArray(new Integer[types.size()]));
		je.setColNames(colNames.toArray(new String[colNames.size()]));
		je.setFieldNames(fieldNames.toArray(new String[fieldNames.size()]));
		values = null;
		types = null;
		colNames = null;
		fieldNames = null;
		return je;
	}
	
	
	
	public final static java.sql.Date dateToSqlDate(java.util.Date date){
		return new java.sql.Date(date.getTime());
	}
	
	public static void main(String[] args) {
		assembleJdbcEntity(Model.class);
	}
}

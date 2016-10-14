package com.renke.core.tools;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mysql.cj.core.MysqlType;
import com.renke.core.annotations.Column;
import com.renke.core.annotations.Table;
import com.renke.core.db.JdbcEntity;
import com.renke.core.enums.MethodType;

/***
 * 实体类操作工具
 * @author renke.zuo@foxmail.com
 * @time 2016-10-12 14:33:01
 */
public class  EntityTool {
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
		final JdbcEntity je = assembleJdbcEntityByEntity(entity);
		final StringBuilder result = new StringBuilder();
		final Class<?> clazz = entity.getClass();
		Table table = clazz.getAnnotation(Table.class);
		result.append(" insert into ").append(table.value()).append("( ");
		final String[] colNames = je.getColNames();
		for(int i=0;i<colNames.length;i++){
			result.append(colNames[i]).append(",");
		}
		result.deleteCharAt(result.length()-1).append(") values(");
		for(int i=0;i<colNames.length;i++){
			result.append("?,");
		}
		result.deleteCharAt(result.length()-1).append(")");
		je.setSql(result.toString());
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
		final JdbcEntity je = assembleJdbcEntityByEntity(entity);
		final StringBuilder result = new StringBuilder();
		final Class<?> clazz = entity.getClass();
		Table table = clazz.getAnnotation(Table.class);
		result.append("update ").append(table.value()).append(" set ");
		final String[] colNames = je.getColNames();
		if(CheckTool.isBlank(je.getPrimaryCol())){
			throw new IllegalArgumentException(clazz.getName()+"不存在主键，不可使用update方法！");
		}
		for(int i=0;i<colNames.length-1;i++){
			result.append(colNames[i]).append("=?,");
		}
		result.deleteCharAt(result.length()-1).append(" where ").append(je.getPrimaryCol()).append("=?");
		je.setSql(result.toString());
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
		final JdbcEntity je = assembleJdbcEntityByEntity(entity);
		final StringBuilder result = new StringBuilder();
		final Class<?> clazz = entity.getClass();
		Table table = clazz.getAnnotation(Table.class);
		result.append(" delete from ").append(table.value()).append(" where ");
		final String[] colNames = je.getColNames();
		for(int i=0;i<colNames.length-1;i++){
			result.append(colNames[i]).append("=? and ");
		}
		result.deleteCharAt(result.length()-4);
		je.setSql(result.toString());
		return je;
	}
	
	/**
	 * entity变量fieldName赋值
	 * @param entity
	 * @param fieldName
	 * @param fieldValue
	 * @author renke.zuo@foxmail.com
	 * @time 2016-10-12 14:34:50
	 */
	public final static <Entity> void setField(Entity entity,String fieldName,Object fieldValue){
		Class<?> clazz = entity.getClass();
		Class<?> paramClazz = fieldValue.getClass();
//		String fieldName = getPrimaryKey(t);
		if(CheckTool.isBlank(fieldName)){
			throw new IllegalArgumentException(clazz.getName()+"不存在"+fieldName);
		}
		String methodName = getMethodNameByFieldName(fieldName,MethodType.SET);
		Method m = null;
		try {
			m = clazz.getMethod(methodName,paramClazz);
			m.invoke(entity, fieldValue);
		} catch ( SecurityException | ReflectiveOperationException | IllegalArgumentException e) {
			logger.error("error:{}",e.getMessage());
			throw new IllegalArgumentException("not found ："+e.getMessage());
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
	
	public final static <Entity> Object getFieldVal(String fieldName,Entity entity){
		String methodName = getMethodNameByFieldName(fieldName,MethodType.GET);
		try{
			Method m = entity.getClass().getMethod(methodName);
			return m.invoke(entity);
		} catch ( SecurityException | ReflectiveOperationException | IllegalArgumentException e) {
			return null;
		}
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
	public final static <Entity> JdbcEntity assembleJdbcEntityByEntity(Entity entity){
		JdbcEntity je = new JdbcEntity();
		Class<?> clazz = entity.getClass();
		Table table = clazz.getAnnotation(Table.class);
		if(CheckTool.isNull(table)){
			throw new IllegalArgumentException(clazz.getName()+"必须声明为["+Table.class.getName()+"]注解!");
		}
		Field[] fields = clazz.getDeclaredFields();
		List<Object> values = new ArrayList<>();
		List<MysqlType> mysqlTypes = new ArrayList<>();
		List<String> colNames = new ArrayList<>();
		for(Field field : fields){
			Column col = field.getAnnotation(Column.class);
			String fieldName = field.getName();
			Object fieldVal = getFieldVal(fieldName, entity);
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
					if(CheckTool.isTrue(col.isPrimary())){
						if(CheckTool.isNotBlank(je.getPrimaryCol())){
							throw new IllegalArgumentException(clazz.getName()+"，不支持多主键！");
						}
						je.setPrimaryCol(colName);
						je.setPrimaryType(MysqlType.getByName(field.getType().getName()));
						je.setPrimaryVal(fieldVal);
					}
				}
			}
			//fieldVal为空，则不处理该字段
			if(CheckTool.isNull(fieldVal)){
				continue;
			}
			//字段名称不能为空，同时 无注解或注解不是主键 的字段可以加入到列表中
			if(CheckTool.isNotBlank(colName) && ( CheckTool.isNull(col) || CheckTool.isNotTrue(col.isPrimary()) )){
				values.add(fieldVal);
				mysqlTypes.add(MysqlType.getByName(field.getType().getName()));
				colNames.add(colName);
			}
		}
		//如果主键有值，则将值加入到列表最后
		if(CheckTool.isNotNull(je.getPrimaryVal())){
			values.add(je.getPrimaryVal());
			mysqlTypes.add(je.getPrimaryType());
			colNames.add(je.getPrimaryCol());
		}
		if(values.size()<=0){
			throw new IllegalArgumentException(clazz.getName()+"没有可操作字段!");
		}
		je.setValues(values.toArray());
		je.setTypes(mysqlTypes.toArray(new MysqlType[mysqlTypes.size()]));
		je.setColNames(colNames.toArray(new String[colNames.size()]));
		values = null;
		mysqlTypes = null;
		colNames = null;
		return je;
	}
	
	
	public final static java.sql.Date dateToSqlDate(java.util.Date date){
		return new java.sql.Date(date.getTime());
	}
	
	
	
	@Test
	public void test(){
//		toSelectJdbcEntity();
//		String str = "a/1232/b";
//		System.out.println(str.matches("a/(\\w+)/b"));
//		assembelEntityForField(log,"port",2);
//		System.out.println(log.getPort());
//		System.out.println(EntityTool.getInsertSqlByEntity(new Log()));
	}
	public static void main(String[] args) {
		String str = "a/1232/b";
		System.out.println(str.matches("a/(\\w+)/b"));
	}
}

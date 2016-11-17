package com.renke.core.db;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.util.StringUtils;
import com.renke.core.tools.CheckTool;

public class SimpleBeanRowMapper<Entity> implements BeanRowMapper<Entity> {

	private Class<Entity> clazz;
	private Map<String,Integer> colIndex;
	private Field[] fields;
	private Class<?>[] types;
	
	public SimpleBeanRowMapper(Class<Entity> clazz){
		this.clazz = clazz;
		extractProperties(this.clazz);
	}
	
	@Override
	public Entity mapRow(ResultSet rs, int rowNum) throws SQLException {
		Entity entity = null;
		try {
			ResultSetMetaData rsmd = rs.getMetaData();
			entity = clazz.newInstance();
			int count = rsmd.getColumnCount();
			for(int i=1;i<=count;i++){
				String colName = JdbcUtils.lookupColumnName(rsmd, i);
				int index = colIndex.get(colName);
				fields[index].set(entity,JdbcUtils.getResultSetValue(rs, i, types[index]));
			}
		} catch (IllegalArgumentException | IllegalAccessException | InstantiationException e) {
			e.printStackTrace();
		}
		return entity;
	}

	@Override
	public void extractProperties(Class<Entity> clazz) {
		Field[] fields = clazz.getDeclaredFields();
		List<Field> listFields = new ArrayList<>();
		List<Class<?>> listTypes = new ArrayList<>();
		colIndex = new HashMap<String,Integer>();
		int index = 0;
		for(Field field : fields){
			String fieldName = field.getName();
			if(CheckTool.isEquals("serialVersionUID", fieldName)){
				continue;
			}
			//大小写改下划线
			String colName = underscoreName(fieldName);
			//字段名称不能为空
			if(CheckTool.isNotBlank(colName)){
				field.setAccessible(true);
				listFields.add(field);
				listTypes.add(field.getType());
				colIndex.put(colName, index ++ );
			}
		}
		CheckTool.throwBlankList(listFields, clazz.getName()+"没有可操作字段!");
		this.fields = listFields.toArray(new Field[listFields.size()]);
		this.types = listTypes.toArray(new Class[listTypes.size()]);
	}
	
	protected String underscoreName(String name) {
		if (!StringUtils.hasLength(name)) {
			return "";
		}
		StringBuilder result = new StringBuilder();
		result.append(name.substring(0, 1).toLowerCase(Locale.US));
		for (int i = 1; i < name.length(); i++) {
			String s = name.substring(i, i + 1);
			String slc = s.toLowerCase(Locale.US);
			if (!s.equals(slc)) {
				result.append("_").append(slc);
			}
			else {
				result.append(s);
			}
		}
		return result.toString();
	}
}
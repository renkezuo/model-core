package com.renke.core.db;

public class JdbcEntity {
	//预编译sql
	private String sql;
	//tableName
	private String tableName;
	//字段名称数组
	private String[] colNames;
	//变量名称数组
	private String[] fieldNames;
	//值类型数组
	private Integer[] types;
	//值数组
	private Object[] values;
	//主键字段
	private String primaryCol;
	//主键字段
	private String primaryField;
	//主键类型
	private Integer primaryType;
	//主键值
	private Object primaryVal;
	public String getSql() {
		return sql;
	}
	public void setSql(String sql) {
		this.sql = sql;
	}
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public Object[] getValues() {
		return values;
	}
	public void setValues(Object[] values) {
		this.values = values;
	}
	public Integer[] getTypes() {
		return types;
	}
	public void setTypes(Integer[] types) {
		this.types = types;
	}
	public String[] getColNames() {
		return colNames;
	}
	public void setColNames(String[] colNames) {
		this.colNames = colNames;
	}
	public String getPrimaryCol() {
		return primaryCol;
	}
	public void setPrimaryCol(String primaryCol) {
		this.primaryCol = primaryCol;
	}
	public String getPrimaryField() {
		return primaryField;
	}
	public void setPrimaryField(String primaryField) {
		this.primaryField = primaryField;
	}
	public Integer getPrimaryType() {
		return primaryType;
	}
	public void setPrimaryType(Integer primaryType) {
		this.primaryType = primaryType;
	}
	public Object getPrimaryVal() {
		return primaryVal;
	}
	public void setPrimaryVal(Object primaryVal) {
		this.primaryVal = primaryVal;
	}
	public String[] getFieldNames() {
		return fieldNames;
	}
	public void setFieldNames(String[] fieldNames) {
		this.fieldNames = fieldNames;
	}
}

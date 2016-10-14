package com.renke.core.db;
import com.mysql.cj.core.MysqlType;

public class JdbcEntity {
	//预编译sql
	private String sql;
	//除主键外，值数组
	private Object[] values;
	//除主键外，值类型数组
	private MysqlType[] types;
	//除主键外，字段名称数组
	private String[] colNames;
	//主键字段
	private String primaryCol;
	//主键类型
	private MysqlType primaryType;
	//主键值
	private Object primaryVal;
	public String getSql() {
		return sql;
	}
	public void setSql(String sql) {
		this.sql = sql;
	}
	public Object[] getValues() {
		return values;
	}
	public void setValues(Object[] values) {
		this.values = values;
	}
	public MysqlType[] getTypes() {
		return types;
	}
	public void setTypes(MysqlType[] types) {
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
	public MysqlType getPrimaryType() {
		return primaryType;
	}
	public void setPrimaryType(MysqlType primaryType) {
		this.primaryType = primaryType;
	}
	public Object getPrimaryVal() {
		return primaryVal;
	}
	public void setPrimaryVal(Object primaryVal) {
		this.primaryVal = primaryVal;
	}
}

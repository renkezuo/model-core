package com.renke.core.tools;

import java.util.HashMap;
import java.util.Map;

import com.mysql.cj.core.MysqlType;

public class SQLTool {
	//insert -- all 
	public final static String getInsertSql(final String[] colNames,final String tableName){
		CheckTool.throwBlankArray(colNames, "colNames不能为空");
		CheckTool.throwBlank(tableName, "tableName不能为空");
		StringBuilder sql = new StringBuilder();
		StringBuilder val = new StringBuilder();
		sql.append("insert into ").append(tableName).append("(");
		for(String colName : colNames){
			sql.append(colName).append(",");
			val.append("?,");
		}
		sql.deleteCharAt(sql.length()-1).append(") values(").append(val.deleteCharAt(val.length()-1)).append(")");
		val = null;
		return sql.toString();
	}
	
	//update -- byId
	public final static String getUpdateSql(final String[] colNames,final String tableName,final String primaryCol){
		CheckTool.throwBlankArray(colNames, "colNames不能为空");
		CheckTool.throwBlank(tableName, "tableName不能为空");
		CheckTool.throwBlank(primaryCol, "primaryCol不能为空");
		final StringBuilder sql = new StringBuilder();
		sql.append("update ").append(tableName).append(" set ");
		for(int i=0;i<colNames.length-1;i++){
			sql.append(colNames[i]).append("=?,");
		}
		sql.deleteCharAt(sql.length()-1).append(" where ").append(primaryCol).append("=?");
		return sql.toString();
	}
	
	//delete -- byId byAll
	public final static String getDeleteSql(final String[] colNames,final String tableName){
		CheckTool.throwBlankArray(colNames, "colNames不能为空");
		CheckTool.throwBlank(tableName, "tableName不能为空");
		StringBuilder sql = new StringBuilder();
		sql.append("delete from ").append(tableName).append(" where ");
		for(String colName : colNames){
			sql.append(colName).append(" = ? and ");
		}
		sql.delete(sql.length() - 4,sql.length());
		return sql.toString();
	}
	
	public final static String getSelectById(final String tableName,String primaryCol){
		CheckTool.throwBlank(tableName, "tableName不能为空");
		CheckTool.throwBlank(primaryCol, "primaryCol不能为空");
		StringBuilder sql = new StringBuilder();
		sql.append(" select * from ").append(tableName).append(" where ")
				.append(primaryCol).append("=?");
		return sql.toString();
	}
	
	private final static Map<String,MysqlType> simpleNameToMysqlType = new HashMap<String,MysqlType>();
	private final static Map<String,Integer> simpleNameToTypes = new HashMap<String,Integer>();
	static{
		simpleNameToMysqlType.put("DECIMAL",MysqlType.DECIMAL);
//		simpleNameToMysqlType.put("DEC",MysqlType.DECIMAL);
//		simpleNameToMysqlType.put("NUMERIC",MysqlType.DECIMAL);
//		simpleNameToMysqlType.put("FIXED",MysqlType.DECIMAL);
//		simpleNameToMysqlType.put("TINYBLOB",MysqlType.TINYBLOB);
//		simpleNameToMysqlType.put("TINYTEXT",MysqlType.TINYTEXT);
//		simpleNameToMysqlType.put("TINYINT",MysqlType.TINYINT);
//		simpleNameToMysqlType.put("TINY",MysqlType.TINYINT);
//		simpleNameToMysqlType.put("INT",MysqlType.INT);
//		simpleNameToMysqlType.put("INT1",MysqlType.TINYINT);
//		simpleNameToMysqlType.put("INT2",MysqlType.SMALLINT);
//		simpleNameToMysqlType.put("INT3",MysqlType.MEDIUMINT);
//		simpleNameToMysqlType.put("INT4",MysqlType.INT);
//		simpleNameToMysqlType.put("INT8",MysqlType.BIGINT);
//		simpleNameToMysqlType.put("INT24",MysqlType.MEDIUMINT);
		simpleNameToMysqlType.put("INTEGER",MysqlType.INT);
		simpleNameToMysqlType.put("BIGINTEGER",MysqlType.BIGINT);
//		simpleNameToMysqlType.put("MEDIUMINT",MysqlType.MEDIUMINT);
//		simpleNameToMysqlType.put("MIDDLEINT",MysqlType.MEDIUMINT);
//		simpleNameToMysqlType.put("SMALLINT",MysqlType.SMALLINT);
//		simpleNameToMysqlType.put("BIGINT",MysqlType.BIGINT);
//		simpleNameToMysqlType.put("SERIAL",MysqlType.BIGINT);
//		simpleNameToMysqlType.put("POINT",MysqlType.GEOMETRY);
		simpleNameToMysqlType.put("DOUBLE",MysqlType.DOUBLE);
//		simpleNameToMysqlType.put("REAL",MysqlType.DOUBLE);
//		simpleNameToMysqlType.put("DOUBLE PRECISION",MysqlType.DOUBLE);
		simpleNameToMysqlType.put("FLOAT",MysqlType.FLOAT);
//		simpleNameToMysqlType.put("FLOAT4",MysqlType.FLOAT);
//		simpleNameToMysqlType.put("FLOAT8",MysqlType.DOUBLE);
//		simpleNameToMysqlType.put("NULL",MysqlType.NULL);
		simpleNameToMysqlType.put("TIMESTAMP",MysqlType.TIMESTAMP);
		simpleNameToMysqlType.put("DATETIME",MysqlType.DATETIME);
		simpleNameToMysqlType.put("DATE",MysqlType.DATE);
//		simpleNameToMysqlType.put("TIME",MysqlType.TIME);
//		simpleNameToMysqlType.put("YEAR",MysqlType.YEAR);
//		simpleNameToMysqlType.put("LONGBLOB",MysqlType.LONGBLOB);
//		simpleNameToMysqlType.put("LONGTEXT",MysqlType.LONGTEXT);
//		simpleNameToMysqlType.put("MEDIUMBLOB",MysqlType.MEDIUMBLOB);
//		simpleNameToMysqlType.put("LONG VARBINARY",MysqlType.MEDIUMBLOB);
//		simpleNameToMysqlType.put("MEDIUMTEXT",MysqlType.MEDIUMTEXT);
//		simpleNameToMysqlType.put("LONG VARCHAR",MysqlType.MEDIUMTEXT);
		simpleNameToMysqlType.put("LONG",MysqlType.BIGINT);
//		simpleNameToMysqlType.put("VARCHAR",MysqlType.VARCHAR);
//		simpleNameToMysqlType.put("NVARCHAR",MysqlType.VARCHAR);
//		simpleNameToMysqlType.put("NATIONAL VARCHAR",MysqlType.VARCHAR);
//		simpleNameToMysqlType.put("CHARACTER VARYING",MysqlType.VARCHAR);
//		simpleNameToMysqlType.put("VARBINARY",MysqlType.VARBINARY);
		simpleNameToMysqlType.put("BYTE[]",MysqlType.BINARY);
//		simpleNameToMysqlType.put("CHAR BYTE",MysqlType.BINARY);
//		simpleNameToMysqlType.put("LINESTRING",MysqlType.GEOMETRY);
//		simpleNameToMysqlType.put("STRING",MysqlType.CHAR);
//		simpleNameToMysqlType.put("CHAR",MysqlType.CHAR);
//		simpleNameToMysqlType.put("NCHAR",MysqlType.CHAR);
//		simpleNameToMysqlType.put("NATIONAL CHAR",MysqlType.CHAR);
		simpleNameToMysqlType.put("CHARACTER",MysqlType.CHAR);
		simpleNameToMysqlType.put("BOOLEAN",MysqlType.BOOLEAN);
//		simpleNameToMysqlType.put("BOOL",MysqlType.BOOLEAN);
//		simpleNameToMysqlType.put("BIT",MysqlType.BIT);
		simpleNameToMysqlType.put("JSON",MysqlType.JSON);
		simpleNameToMysqlType.put("ENUM",MysqlType.ENUM);
		simpleNameToMysqlType.put("SET",MysqlType.SET);
		simpleNameToMysqlType.put("BLOB",MysqlType.BLOB);
		simpleNameToMysqlType.put("STRING",MysqlType.VARCHAR);
//		simpleNameToMysqlType.put("GEOMETRY",MysqlType.GEOMETRY);
//		simpleNameToMysqlType.put("GEOMETRYCOLLECTION",MysqlType.GEOMETRY);
//		simpleNameToMysqlType.put("MULTIPOINT",MysqlType.GEOMETRY);
//		simpleNameToMysqlType.put("POLYGON",MysqlType.GEOMETRY);
//		simpleNameToMysqlType.put("MULTIPOLYGON",MysqlType.GEOMETRY);
		
		simpleNameToTypes.put("DECIMAL",3);
//		simpleNameToTypes.put("DEC",3);
//		simpleNameToTypes.put("NUMERIC",3);
//		simpleNameToTypes.put("FIXED",3);
//		simpleNameToTypes.put("TINYBLOB",-3);
//		simpleNameToTypes.put("TINYTEXT",12);
//		simpleNameToTypes.put("TINYINT",-6);
//		simpleNameToTypes.put("TINY",-6);
//		simpleNameToTypes.put("INT1",-6);
//		simpleNameToTypes.put("MEDIUMINT",4);
//		simpleNameToTypes.put("INT24",4);
//		simpleNameToTypes.put("INT3",4);
//		simpleNameToTypes.put("MIDDLEINT",4);
//		simpleNameToTypes.put("SMALLINT",5);
//		simpleNameToTypes.put("INT2",5);
		simpleNameToTypes.put("BIGINTEGER",-5);
//		simpleNameToTypes.put("SERIAL",-5);
//		simpleNameToTypes.put("INT8",-5);
//		simpleNameToTypes.put("POINT",-2);
//		simpleNameToTypes.put("INT",4);
		simpleNameToTypes.put("INTEGER",4);
//		simpleNameToTypes.put("INT4",4);
		simpleNameToTypes.put("DOUBLE",8);
//		simpleNameToTypes.put("REAL",8);
//		simpleNameToTypes.put("DOUBLE PRECISION",8);
//		simpleNameToTypes.put("FLOAT8",8);
		simpleNameToTypes.put("FLOAT",7);
//		simpleNameToTypes.put("FLOAT4",7);
//		simpleNameToTypes.put("NULL",0);
		simpleNameToTypes.put("TIMESTAMP",93);
		simpleNameToTypes.put("DATETIME",93);
		simpleNameToTypes.put("DATE",91);
//		simpleNameToTypes.put("TIME",92);
//		simpleNameToTypes.put("YEAR",91);
//		simpleNameToTypes.put("LONGBLOB",-4);
//		simpleNameToTypes.put("LONGTEXT",-1);
//		simpleNameToTypes.put("MEDIUMBLOB",-4);
//		simpleNameToTypes.put("LONG VARBINARY",-4);
//		simpleNameToTypes.put("MEDIUMTEXT",-1);
//		simpleNameToTypes.put("LONG VARCHAR",-1);
		simpleNameToTypes.put("LONG",-5);
//		simpleNameToTypes.put("VARCHAR",12);
//		simpleNameToTypes.put("NVARCHAR",12);
//		simpleNameToTypes.put("NATIONAL VARCHAR",12);
//		simpleNameToTypes.put("CHARACTER VARYING",12);
//		simpleNameToTypes.put("VARBINARY",-3);
//		simpleNameToTypes.put("BINARY",-2);
//		simpleNameToTypes.put("CHAR BYTE",-2);
//		simpleNameToTypes.put("LINESTRING",-2);
		simpleNameToTypes.put("STRING",12);
//		simpleNameToTypes.put("CHAR",1);
//		simpleNameToTypes.put("NCHAR",1);
//		simpleNameToTypes.put("NATIONAL CHAR",1);
		simpleNameToTypes.put("CHARACTER",1);
		simpleNameToTypes.put("BOOLEAN",16);
//		simpleNameToTypes.put("BOOL",16);
//		simpleNameToTypes.put("BIT",-7);
		simpleNameToTypes.put("JSON",-1);
		simpleNameToTypes.put("ENUM",1);
		simpleNameToTypes.put("SET",1);
		simpleNameToTypes.put("BLOB",-4);
		simpleNameToTypes.put("BYTE[]",-2);
//		simpleNameToTypes.put("TEXT",-1);
//		simpleNameToTypes.put("GEOMETRY",-2);
//		simpleNameToTypes.put("GEOMETRYCOLLECTION",-2);
//		simpleNameToTypes.put("POINT",-2);
//		simpleNameToTypes.put("MULTIPOINT",-2);
//		simpleNameToTypes.put("POLYGON",-2);
//		simpleNameToTypes.put("MULTIPOLYGON",-2);
		
	}
	
	/**
	 * @param classSimpleName
	 * @return MysqlType
	 * @author renke.zuo@foxmail.com
	 * @time 2016-10-18 16:33:32
	 */
	public final static MysqlType getMysqlType(String classSimpleName){
		return simpleNameToMysqlType.get(classSimpleName.toUpperCase());
	}
	
	/**
	 * @param classSimpleName
	 * @return java.sql.Types
	 * @author renke.zuo@foxmail.com
	 * @time 2016-10-18 17:14:05
	 */
	public final static int getSqlTypes(String classSimpleName){
		return simpleNameToTypes.get(classSimpleName.toUpperCase());
	}
}

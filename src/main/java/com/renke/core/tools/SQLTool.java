package com.renke.core.tools;

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
	
	public final static String getSelectSql(final String[] colNames,final String tableName){
		CheckTool.throwBlankArray(colNames, "colNames不能为空");
		CheckTool.throwBlank(tableName, "tableName不能为空");
		final StringBuilder sql = new StringBuilder();
		sql.append(" select * from ").append(tableName).append(" where ");
		for(int i=0;i<colNames.length;i++){
			sql.append(colNames[i]).append("=? and ");
		}
		sql.delete(sql.length()-4,sql.length());
		return sql.toString();
	}
	
	
	public final static String getSelect(final String[] colNames,final String tableName){
		CheckTool.throwBlankArray(colNames, "colNames不能为空");
		CheckTool.throwBlank(tableName, "tableName不能为空");
		final StringBuilder sql = new StringBuilder();
		sql.append(" select * from ").append(tableName).append(" where ");
		for(int i=0;i<colNames.length;i++){
			sql.append(colNames[i]).append("=? and ");
		}
		sql.delete(sql.length()-4,sql.length());
		return sql.toString();
	}

}

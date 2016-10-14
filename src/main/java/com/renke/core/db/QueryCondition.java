package com.renke.core.db;

public class QueryCondition<E> {
	private String querySql;
	private String countSql;
	private String  outSql;
	private PageInfo<E> pageInfo;
	
	public QueryCondition() {
	}
	
	public void setPageInfo(PageInfo<E> pageInfo) {
		this.pageInfo = pageInfo;
	}
	public PageInfo<E> getPageInfo() {
		return pageInfo;
	}
	public void setCountSql(String countSql) {
		this.countSql = countSql;
	}
	public void setOutSql(String outSql) {
		this.outSql = outSql;
	}
	public void setQuerySql(String querySql) {
		this.querySql = querySql;
	}
	public String getCountSql() {
		return countSql;
	}
	public String getOutSql() {
		return outSql;
	}
	public String getQuerySql() {
		return querySql;
	}
}

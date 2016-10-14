package com.renke.core.db;

import java.util.ArrayList;
import java.util.List;

public class PageInfo<E> {
	private int pageNo = 1;//当前页码
	private int pageSize = 10;//每页记录数
	private int pageCount = 1;//总页数
	private int count = 0;//总记录数
	private List<E> list = new ArrayList<E>();//当前页记录集
	
	public PageInfo() {
	}

	public PageInfo(PageInfo<?> pageInfo) {
		pageNo = pageInfo.getPageNo();
		pageSize = pageInfo.getPageSize();
		pageCount = pageInfo.getPageCount();
	}
	private void validate(){
		int temp = count / pageSize;
		pageCount = ( temp * pageSize < count) ? temp + 1 : temp;
	}

	public int getPageNo() {
		return pageNo;
	}

	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
		validate();
	}

	public int getPageCount() {
		return pageCount;
	}

	public void setPageCount(int pageCount) {
		this.pageCount = pageCount;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
		validate();
	}

	public List<E> getList() {
		return list;
	}

	public void setList(List<E> list) {
		this.list = list;
	}
	
	
}

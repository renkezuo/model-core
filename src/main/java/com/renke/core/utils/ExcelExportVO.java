package com.renke.core.utils;

import java.util.List;
import java.util.Map;

/**  
 * title: ExcelExportVO.java 
 * 到处excel所需的实体类
 *
 * @author rplees
 * @email rplees.i.ly@gmail.com
 * @version 1.0  
 * @created 2013-7-24 下午4:10:16 
 */  
public class ExcelExportVO {
	List<Map<String, Object>> list;
	String title;
	String sheetTitle = "Sheet1";
	
	public String getSheetTitle() {
		return sheetTitle;
	}
	public void setSheetTitle(String sheetTitle) {
		this.sheetTitle = sheetTitle;
	}
	public List<Map<String, Object>> getList() {
		return list;
	}
	public void setList(List<Map<String, Object>> list) {
		this.list = list;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
}

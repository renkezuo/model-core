package com.renke.core.common;

import java.lang.reflect.Field;
import java.util.Comparator;


public class SortField implements Comparator<Field> {

	@Override
	public int compare(Field o1, Field o2) {
		if(o1==null){
			return -1;
		}
		if(o2 == null){
			return 1;
		}
		return o1.getName().compareTo(o2.getName()); 
	}
}

package com.renke.core.common;

import java.util.Comparator;

import com.renke.core.entity.Param;

public class SortParam implements Comparator<Param> {

	@Override
	public int compare(Param o1, Param o2) {
		if(o1==null){
			return -1;
		}
		if(o2 == null){
			return 1;
		}
		return o1.key.compareTo(o2.key); 
	}
}

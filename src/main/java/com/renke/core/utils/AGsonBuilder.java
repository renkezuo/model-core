package com.renke.core.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class AGsonBuilder {
	public static final GsonBuilder INSTANCE = new GsonBuilder();

	static {
		 INSTANCE.disableHtmlEscaping();
	}
	
	public static Gson create() {
		return INSTANCE.create();
	}
	
	public static void changed() {
		
	}
}

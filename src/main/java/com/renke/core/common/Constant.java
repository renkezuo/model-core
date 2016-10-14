package com.renke.core.common;

import java.sql.Connection;

public class Constant {
	public static final ThreadLocal<Connection> connectionHolder = new ThreadLocal<Connection>();
}

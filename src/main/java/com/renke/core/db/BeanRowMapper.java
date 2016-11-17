package com.renke.core.db;

import org.springframework.jdbc.core.RowMapper;

public interface BeanRowMapper<Entity> extends RowMapper<Entity>{
	public void extractProperties(Class<Entity> entity);
}

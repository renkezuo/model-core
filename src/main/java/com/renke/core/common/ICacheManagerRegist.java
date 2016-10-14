package com.renke.core.common;

import org.springframework.cache.CacheManager;
import org.springframework.context.ApplicationContext;

public interface ICacheManagerRegist {
	CacheManager cacheManager(ApplicationContext applicationContext);
}

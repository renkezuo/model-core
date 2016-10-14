package com.renke.core.common;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.cache.interceptor.CacheErrorHandler;
import org.springframework.cache.interceptor.CacheResolver;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.cache.interceptor.SimpleCacheErrorHandler;
import org.springframework.cache.interceptor.SimpleCacheResolver;
import org.springframework.cache.interceptor.SimpleKeyGenerator;
import org.springframework.cache.support.CompositeCacheManager;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.renke.core.utils.CollectionUtils;

@Configuration
@ComponentScan
@EnableCaching(proxyTargetClass = true)
public class AgCachingConfigurer implements CachingConfigurer, ApplicationContextAware {
	public final Logger logger = LoggerFactory.getLogger(getClass());
	public static final String CACHE_MAP = "map";
	public static final String CACHE_SIMPLE = "simple";
	public static final String CACHE_CONCURRENTMAP = "concurrentMap";
	
	CompositeCacheManager cacheManager;
	Collection<CacheManager> list = new ArrayList<CacheManager>();
	@Override
	public CacheManager cacheManager() {
		return _cacheManager();
	}
	
	public CompositeCacheManager _cacheManager() {
		if(cacheManager == null) {
			CompositeCacheManager compositeCacheManager = new CompositeCacheManager();
			compositeCacheManager.setCacheManagers(list);
//			compositeCacheManager.setFallbackToNoOpCache(true);
			cacheManager = compositeCacheManager;
		}
		
		return cacheManager;
	}
	
	@Override
	public CacheResolver cacheResolver() {
		return new SimpleCacheResolver(cacheManager());
	}

	@Override
	public KeyGenerator keyGenerator() {
		return new SimpleKeyGenerator();
	}

	@Override
	public CacheErrorHandler errorHandler() {
		return new SimpleCacheErrorHandler();
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		Map<String, ICacheManagerRegist> beansOfType = applicationContext.getBeansOfType(ICacheManagerRegist.class);
		ConcurrentMapCacheManager concurrentMapCacheManager = new ConcurrentMapCacheManager(CACHE_CONCURRENTMAP, CACHE_SIMPLE, CACHE_MAP);
		list.add(concurrentMapCacheManager);
		
		if(CollectionUtils.isNotEmpty(beansOfType)) {
			for(Map.Entry<String, ICacheManagerRegist> entry : beansOfType.entrySet()) {
				logger.info("regist 缓存:" + entry.getValue());
				
				CacheManager manager = entry.getValue().cacheManager(applicationContext);
				if(manager != null) {
					list.add(manager);
				} else {
					logger.warn("cacheManager is nil.{}", entry.getKey());
				}
			}
		}
		if(this.cacheManager != null) {
			CompositeCacheManager _cacheManager = _cacheManager();
			_cacheManager.setCacheManagers(list);
		}
	}
}

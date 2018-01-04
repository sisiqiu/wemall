package com.fulltl.wemall.modules.his.util;

import java.lang.reflect.Method;

import org.apache.commons.lang.StringUtils;
import org.springframework.cache.interceptor.KeyGenerator;

import com.fulltl.wemall.common.persistence.DataEntity;
import com.fulltl.wemall.common.persistence.Page;

/**
 * cache的key的生成器
 * @author Administrator
 *
 */
public class CacheKeyGenerator implements KeyGenerator {

	@Override
	public Object generate(Object arg0, Method arg1, Object... arg2) {
		String key = arg0.getClass().getName();
		for(Object arg : arg2) {
			if(arg instanceof DataEntity) {
				//如果为实体类
				String cacheKey = ((DataEntity) arg).formatToCacheKey();
				key = key + "|" + cacheKey;
			} else if(arg instanceof Page) {
				Page p = ((Page) arg);
				String orderBy = p.getOrderBy();
				int pageNo = p.getPageNo();
				int pageSize = p.getPageSize();
				key = key + "|orderBy=" + orderBy + ",pageNo=" + pageNo + ",pageSize=" + pageSize;
			} else {
				key = key + arg.toString();
			}
		}
		System.err.println(key);
		return key;
	}

}

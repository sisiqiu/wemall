package com.fulltl.wemall.modules.his.util;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fulltl.wemall.common.utils.CacheUtils;

/**
 * 用于管理互联网医院项目中所有实体类服务层的缓存。
 * @author ldk
 *
 */
@Service
@Transactional(readOnly = true)
public class HisServiceCacheManager {
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	/**
	 * 互联网医院基础数据缓存
	 */
	public static final String HIS_BASE_CACHE = "hisBaseCache";
	/**
	 * 互联网医院临时数据缓存
	 */
	public static final String HIS_TEMP_CACHE = "hisTempCache";
	/**
	 * 互联网医院临时短暂数据缓存
	 */
	public static final String HIS_TEMP_SHORT_CACHE = "hisTempShortCache";
	
	/**
	 * 互联网医院缓存枚举类
	 * @author ldk
	 *
	 */
	public enum HisCacheEnum {
		/**
		 * 科室列表
		 */
		hosDeptList(HIS_BASE_CACHE),
		/**
		 * 单个科室详情
		 */
		hosDeptDetail(HIS_BASE_CACHE),
		/**
		 * 用户端首页数据
		 */
		userHomePageData(HIS_BASE_CACHE),
		/**
		 * 膏方节页数据
		 */
		gaofangPageData(HIS_BASE_CACHE),
		/**
		 * 特色中医页数据
		 */
		speCNDoctorPageData(HIS_BASE_CACHE),
		/**
		 * 独立的url配置数据
		 */
		configUrls(HIS_BASE_CACHE);
		
		private String cacheName;
		
	    private HisCacheEnum(String cacheName) {  
	        this.cacheName = cacheName;
	    }

	    /**
	     * 获取对应缓存枚举类对应的缓存cacheName
	     * @return
	     */
		public String getCacheName() {
			return cacheName;
		}
	}
	
	/**
	 * 根据枚举缓存类别，查询指定缓存数据。
	 * @param hisCacheEnum
	 * @return
	 */
	public Object getCache(HisCacheEnum hisCacheEnum) {
		return getCache(hisCacheEnum, null);
	}
	
	/**
	 * 根据枚举缓存类别，设置缓存
	 * @param hisCacheEnum
	 * @param data
	 */
	public void putCache(HisCacheEnum hisCacheEnum, Object data) {
		putCache(hisCacheEnum, data, null);
	}
	
	/**
	 * 根据枚举缓存类别，删除缓存
	 * @param hisCacheEnum
	 */
	public void deleteCache(HisCacheEnum hisCacheEnum) {
		deleteCache(hisCacheEnum, null);
	}
	
	
	/**
	 * 根据枚举缓存类别，和标示信息（如id，且可不填），查询指定缓存数据。
	 * @param hisCacheEnum
	 * @param mark 可为空
	 * @return
	 */
	public Object getCache(HisCacheEnum hisCacheEnum, String mark) {
		if(StringUtils.isBlank(mark)) {
			//mark为空
			return CacheUtils.get(hisCacheEnum.getCacheName(), hisCacheEnum.toString());
		} else {
			//mark不为空
			return CacheUtils.get(hisCacheEnum.getCacheName(), hisCacheEnum.toString() + "_" + mark);
		}
	}
	
	/**
	 * 根据枚举缓存类别，和标示信息（如id，且可不填），设置缓存
	 * @param hisCacheEnum
	 * @param mark
	 * @param data
	 */
	public void putCache(HisCacheEnum hisCacheEnum, Object data, String mark) {
		if(StringUtils.isBlank(mark)) {
			//mark为空
			CacheUtils.put(hisCacheEnum.getCacheName(), hisCacheEnum.toString(), data);
		} else {
			//mark不为空
			CacheUtils.put(hisCacheEnum.getCacheName(), hisCacheEnum.toString() + "_" + mark, data);
		}
	}
	
	/**
	 * 根据枚举缓存类别，和标示信息（如id，且可不填），删除缓存
	 * @param hisCacheEnum
	 */
	public void deleteCache(HisCacheEnum hisCacheEnum, String mark) {
		if(StringUtils.isBlank(mark)) {
			//mark为空
			CacheUtils.remove(hisCacheEnum.getCacheName(), hisCacheEnum.toString());
		} else {
			//mark不为空
			CacheUtils.remove(hisCacheEnum.getCacheName(), hisCacheEnum.toString() + "_" + mark);
		}
	}
	
}

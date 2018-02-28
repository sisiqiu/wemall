/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.fulltl.wemall.modules.sys.service;

import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;
import com.fulltl.wemall.common.config.Global;
import com.fulltl.wemall.common.persistence.Page;
import com.fulltl.wemall.common.service.CrudService;
import com.fulltl.wemall.modules.sys.entity.SlSysAdvertise;
import com.fulltl.wemall.modules.his.util.DataStorageUtil;
import com.fulltl.wemall.modules.sys.dao.SlSysAdvertiseDao;

/**
 * 广告位管理Service
 * @author ldk
 * @version 2017-11-22
 */
@Service
@Transactional(readOnly = true)
public class SlSysAdvertiseService extends CrudService<SlSysAdvertiseDao, SlSysAdvertise> {

	/**
	 * 初始化app页面配置到Global缓存中
	 */
	@PostConstruct
	public void initAppUrlConfig() {
		SlSysAdvertise slSysAdvertise = this.get(DataStorageUtil.GLOBAL_HISUSER_URL_CONFIG_ADVERTISEID);
		Map addMap = new Gson().fromJson(slSysAdvertise.getAdBody(), Map.class);
		Global.putAllToWemallMap(addMap);
	}
	
	public SlSysAdvertise get(String id) {
		return super.get(id);
	}
	
	public List<SlSysAdvertise> findList(SlSysAdvertise slSysAdvertise) {
		return super.findList(slSysAdvertise);
	}
	
	public Page<SlSysAdvertise> findPage(Page<SlSysAdvertise> page, SlSysAdvertise slSysAdvertise) {
		return super.findPage(page, slSysAdvertise);
	}
	
	@Transactional(readOnly = false)
	public void save(SlSysAdvertise slSysAdvertise) {
		super.save(slSysAdvertise);
		if(DataStorageUtil.GLOBAL_HISUSER_URL_CONFIG_ADVERTISEID.equals(slSysAdvertise.getId())) {
			//若更新的广告为是全局用户端或医生端url配置的项，更新缓存
			initAppUrlConfig();
		}
	}
	
	@Transactional(readOnly = false)
	public void delete(SlSysAdvertise slSysAdvertise, Boolean isRe) {
		if(isRe != null && isRe) {
			dao.audit(slSysAdvertise);
		} else {
			super.delete(slSysAdvertise);
		}
	}
	
}
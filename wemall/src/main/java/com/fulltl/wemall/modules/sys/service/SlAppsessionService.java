/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.fulltl.wemall.modules.sys.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fulltl.wemall.common.persistence.Page;
import com.fulltl.wemall.common.service.CrudService;
import com.fulltl.wemall.modules.sys.entity.SlAppsession;
import com.fulltl.wemall.modules.sys.dao.SlAppsessionDao;

/**
 * appsession管理Service
 * @author kangyang
 * @version 2017-12-06
 */
@Service
@Transactional(readOnly = true)
public class SlAppsessionService extends CrudService<SlAppsessionDao, SlAppsession> {

	public SlAppsession get(String id) {
		return super.get(id);
	}
	
	public List<SlAppsession> findList(SlAppsession slAppsession) {
		return super.findList(slAppsession);
	}
	
	public Page<SlAppsession> findPage(Page<SlAppsession> page, SlAppsession slAppsession) {
		return super.findPage(page, slAppsession);
	}
	
	@Transactional(readOnly = false)
	public void save(SlAppsession slAppsession) {
		super.save(slAppsession);
	}
	
	@Transactional(readOnly = false)
	public void delete(SlAppsession slAppsession) {
		super.delete(slAppsession);
	}

	@Transactional(readOnly = false)
	public void updateByOldSid(SlAppsession slAppsession) {
		slAppsession.preUpdate();
		dao.updateByOldSid(slAppsession);
	}
	
	@Transactional(readOnly = false)
	public void updateByUserId(SlAppsession slAppsession) {
		slAppsession.preUpdate();
		dao.updateByUserId(slAppsession);
	}

	@Transactional(readOnly = false)
	public void deleteByUserId(String userId) {
		dao.deleteByUserId(userId);
	}
}
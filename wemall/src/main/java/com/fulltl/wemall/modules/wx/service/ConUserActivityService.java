/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.fulltl.wemall.modules.wx.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fulltl.wemall.common.persistence.Page;
import com.fulltl.wemall.common.service.CrudService;
import com.fulltl.wemall.modules.wx.dao.ConUserActivityDao;
import com.fulltl.wemall.modules.wx.entity.ConUserActivity;

/**
 * gvvService
 * @author bhcfg
 * @version 2017-10-14
 */
@Service
@Transactional(readOnly = true)
public class ConUserActivityService extends CrudService<ConUserActivityDao, ConUserActivity> {
	
	public ConUserActivity get(String id) {
		return super.get(id);
	}
	
	public List<ConUserActivity> findList(ConUserActivity conUserActivity) {
		return super.findList(conUserActivity);
	}
	
	public Page<ConUserActivity> findPage(Page<ConUserActivity> page, ConUserActivity conUserActivity) {
		return super.findPage(page, conUserActivity);
	}
	
	@Transactional(readOnly = false)
	public void save(ConUserActivity conUserActivity) {
		super.save(conUserActivity);
	}
	
	@Transactional(readOnly = false)
	public void delete(ConUserActivity conUserActivity) {
		super.delete(conUserActivity);
	}
	public ConUserActivity getByActUserId(String activityId, String userId){
		return dao.getByActUserId(activityId, userId);
	}
	public ConUserActivity getByActUserPhone(String mobile){
		return dao.getByActUserPhone(mobile);
	}
	public List<ConUserActivity> getByActidStatu (String activityId, String status){
		return dao.getByActidStatu(activityId, status);
	}
	
}
/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.fulltl.wemall.modules.cms.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fulltl.wemall.common.persistence.Page;
import com.fulltl.wemall.common.service.CrudService;
import com.fulltl.wemall.modules.cms.dao.CmsFeedbackDao;
import com.fulltl.wemall.modules.cms.entity.CmsFeedback;

/**
 * 管理用户反馈信息Service
 * @author hj
 * @version 2017-11-02
 */
@Service
@Transactional(readOnly = true)
public class CmsFeedbackService extends CrudService<CmsFeedbackDao, CmsFeedback> {

	public CmsFeedback get(String id) {
		return super.get(id);
	}
	
	public List<CmsFeedback> findList(CmsFeedback cmsFeedback) {
		return super.findList(cmsFeedback);
	}
	
	public Page<CmsFeedback> findPage(Page<CmsFeedback> page, CmsFeedback cmsFeedback) {
		return super.findPage(page, cmsFeedback);
	}
	
	@Transactional(readOnly = false)
	public void save(CmsFeedback cmsFeedback) {
		super.save(cmsFeedback);
	}
	
	@Transactional(readOnly = false)
	public void delete(CmsFeedback cmsFeedback) {
		super.delete(cmsFeedback);
	}
	
}
/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.fulltl.wemall.modules.cms.service;

import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fulltl.wemall.common.persistence.Page;
import com.fulltl.wemall.common.service.CrudService;
import com.fulltl.wemall.modules.cms.entity.CmsUserRegid;
import com.fulltl.wemall.modules.cms.dao.CmsUserRegidDao;

/**
 * 用户-极光注册id管理Service
 * @author ldk
 * @version 2017-12-09
 */
@Service
@Transactional(readOnly = true)
public class CmsUserRegidService extends CrudService<CmsUserRegidDao, CmsUserRegid> {

	public CmsUserRegid get(String id) {
		return super.get(id);
	}
	
	public List<CmsUserRegid> findList(CmsUserRegid cmsUserRegid) {
		return super.findList(cmsUserRegid);
	}
	
	public Page<CmsUserRegid> findPage(Page<CmsUserRegid> page, CmsUserRegid cmsUserRegid) {
		return super.findPage(page, cmsUserRegid);
	}
	
	@Transactional(readOnly = false)
	public void save(CmsUserRegid cmsUserRegid) {
		super.save(cmsUserRegid);
	}
	
	@Transactional(readOnly = false)
	public void delete(CmsUserRegid cmsUserRegid) {
		super.delete(cmsUserRegid);
	}

	/**
	 * 根据用户id列表查询极光注册id列表
	 * @param userIdList
	 * @return
	 */
	public List<CmsUserRegid> findListByUserIds(Set<String> userIdList) {
		CmsUserRegid cmsUserRegid = new CmsUserRegid();
		cmsUserRegid.setUserIdList(userIdList);
		return dao.findList(cmsUserRegid);
	}
	
}
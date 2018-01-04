/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.fulltl.wemall.modules.cms.service;

import java.util.List;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fulltl.wemall.common.persistence.Page;
import com.fulltl.wemall.common.service.CrudService;
import com.fulltl.wemall.modules.cms.dao.CmsCollectionDao;
import com.fulltl.wemall.modules.cms.entity.CmsCollection;


/**
 * 对用户所收藏的商品进行管理Service
 * @author hj
 * @version 2017-11-15
 */
@Service
@Transactional(readOnly = true)
public class CmsCollectionService extends CrudService<CmsCollectionDao, CmsCollection> {

	public CmsCollection get(String id) {
		return super.get(id);
	}
	
	public List<CmsCollection> findList(CmsCollection cmsCollection) {
		return super.findList(cmsCollection);
	}
	
	public Page<CmsCollection> findPage(Page<CmsCollection> page, CmsCollection cmsCollection) {
		return super.findPage(page, cmsCollection);
	}
	
	@Transactional(readOnly = false)
	public void save(CmsCollection cmsCollection) {
		super.save(cmsCollection);
	}
	
	@Transactional(readOnly = false)
	public void delete(CmsCollection cmsCollection) {
		super.delete(cmsCollection);
	}
	
}
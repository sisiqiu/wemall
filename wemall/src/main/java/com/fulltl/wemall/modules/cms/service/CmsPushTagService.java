/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.fulltl.wemall.modules.cms.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fulltl.wemall.common.persistence.Page;
import com.fulltl.wemall.common.service.CrudService;
import com.fulltl.wemall.modules.cms.entity.CmsPushTag;
import com.fulltl.wemall.modules.cms.dao.CmsPushTagDao;

/**
 * 标签管理Service
 * @author ldk
 * @version 2017-12-09
 */
@Service
@Transactional(readOnly = true)
public class CmsPushTagService extends CrudService<CmsPushTagDao, CmsPushTag> {

	public CmsPushTag get(String id) {
		return super.get(id);
	}
	
	public List<CmsPushTag> findList(CmsPushTag cmsPushTag) {
		return super.findList(cmsPushTag);
	}
	
	public Page<CmsPushTag> findPage(Page<CmsPushTag> page, CmsPushTag cmsPushTag) {
		return super.findPage(page, cmsPushTag);
	}
	
	@Transactional(readOnly = false)
	public void save(CmsPushTag cmsPushTag) {
		super.save(cmsPushTag);
	}
	
	@Transactional(readOnly = false)
	public void delete(CmsPushTag cmsPushTag) {
		super.delete(cmsPushTag);
	}

	/**
	 * 根据id列表查询数据
	 * @param tagIdList
	 * @return
	 */
	public List<CmsPushTag> findListByIds(List<String> tagIdList) {
		CmsPushTag cmsPushTag = new CmsPushTag();
		cmsPushTag.setIdList(tagIdList);
		return dao.findList(cmsPushTag);
	}
	
}
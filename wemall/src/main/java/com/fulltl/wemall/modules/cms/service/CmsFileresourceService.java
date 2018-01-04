/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.fulltl.wemall.modules.cms.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fulltl.wemall.common.persistence.Page;
import com.fulltl.wemall.common.service.CrudService;
import com.fulltl.wemall.modules.cms.dao.CmsFileresourceDao;
import com.fulltl.wemall.modules.cms.entity.CmsFileresource;

/**
 * 上传文件管理Service
 * @author ldk
 * @version 2017-11-15
 */
@Service
@Transactional(readOnly = true)
public class CmsFileresourceService extends CrudService<CmsFileresourceDao, CmsFileresource> {

	public CmsFileresource get(String id) {
		return super.get(id);
	}
	
	public List<CmsFileresource> findList(CmsFileresource cmsFileresource) {
		return super.findList(cmsFileresource);
	}
	
	public Page<CmsFileresource> findPage(Page<CmsFileresource> page, CmsFileresource cmsFileresource) {
		return super.findPage(page, cmsFileresource);
	}
	
	@Transactional(readOnly = false)
	public void save(CmsFileresource cmsFileresource) {
		super.save(cmsFileresource);
	}
	
	@Transactional(readOnly = false)
	public void delete(CmsFileresource cmsFileresource) {
		super.delete(cmsFileresource);
	}

	/**
	 * 更新文件管理中对应文件的下载数（+1）
	 * @param filePath
	 */
	@Transactional(readOnly = false)
	public void updateDownloadNum(String filePath) {
		dao.updateDownloadNum(filePath);
	}

	/**
	 * 根据文件路径查询文件数据
	 * @param filePath
	 * @return
	 */
	public List<CmsFileresource> findByFilePath(String filePath) {
		return dao.findByFilePath(filePath);
	}
	
}
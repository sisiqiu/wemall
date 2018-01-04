/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.fulltl.wemall.modules.cms.dao;

import java.util.List;

import com.fulltl.wemall.common.persistence.CrudDao;
import com.fulltl.wemall.common.persistence.annotation.MyBatisDao;
import com.fulltl.wemall.modules.cms.entity.CmsFileresource;

/**
 * 上传文件管理DAO接口
 * @author ldk
 * @version 2017-11-15
 */
@MyBatisDao
public interface CmsFileresourceDao extends CrudDao<CmsFileresource> {

	/**
	 * 更新文件管理中对应文件的下载数（+1）
	 * @param filePath
	 */
	public void updateDownloadNum(String filePath);

	/**
	 * 根据文件路径查询文件数据
	 * @param filePath
	 * @return
	 */
	public List<CmsFileresource> findByFilePath(String filePath);
	
}
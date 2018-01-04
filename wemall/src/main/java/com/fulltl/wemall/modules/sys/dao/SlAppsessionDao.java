/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.fulltl.wemall.modules.sys.dao;

import com.fulltl.wemall.common.persistence.CrudDao;
import com.fulltl.wemall.common.persistence.annotation.MyBatisDao;
import com.fulltl.wemall.modules.sys.entity.SlAppsession;

/**
 * appsession管理DAO接口
 * @author kangyang
 * @version 2017-12-06
 */
@MyBatisDao
public interface SlAppsessionDao extends CrudDao<SlAppsession> {
	
	/**
	 * 根据userid更新
	 * @param entity
	 * @return
	 */
	public int updateByUserId(SlAppsession entity);
	
	/**
	 * 根据oldSid更新
	 * @param entity
	 * @return
	 */
	public int updateByOldSid(SlAppsession entity);

	/**
	 * 根据userId删除对应该用户的appsession
	 * @param userId
	 */
	public void deleteByUserId(String userId);
}
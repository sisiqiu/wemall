/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.fulltl.wemall.modules.sys.dao;

import com.fulltl.wemall.common.persistence.CrudDao;
import com.fulltl.wemall.common.persistence.annotation.MyBatisDao;
import com.fulltl.wemall.modules.sys.entity.SlSysAdvertise;

/**
 * 广告位管理DAO接口
 * @author ldk
 * @version 2017-11-22
 */
@MyBatisDao
public interface SlSysAdvertiseDao extends CrudDao<SlSysAdvertise> {
	/**
	 * 发布数据（更新del_flag字段为2）
	 * @param entity
	 * @return
	 */
	public int audit(SlSysAdvertise entity);
}
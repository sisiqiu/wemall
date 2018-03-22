/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.fulltl.wemall.modules.wemall.dao;

import com.fulltl.wemall.common.persistence.CrudDao;
import com.fulltl.wemall.common.persistence.annotation.MyBatisDao;
import com.fulltl.wemall.modules.wemall.entity.WemallItemActivity;

/**
 * 商品活动中间表管理DAO接口
 * @author fulltl
 * @version 2018-01-13
 */
@MyBatisDao
public interface WemallItemActivityDao extends CrudDao<WemallItemActivity> {

	String findItemsByActId(String id,int activityType);
	
}
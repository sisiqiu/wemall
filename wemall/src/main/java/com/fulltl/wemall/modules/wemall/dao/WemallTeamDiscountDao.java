/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.fulltl.wemall.modules.wemall.dao;

import java.util.List;

import com.fulltl.wemall.common.persistence.CrudDao;
import com.fulltl.wemall.common.persistence.annotation.MyBatisDao;
import com.fulltl.wemall.modules.wemall.entity.WemallTeamDiscount;

/**
 * 限时拼团活动管理DAO接口
 * @author ldk
 * @version 2018-01-05
 */
@MyBatisDao
public interface WemallTeamDiscountDao extends CrudDao<WemallTeamDiscount> {
	
	public List<WemallTeamDiscount> findListNotTimeout(WemallTeamDiscount wemallTeamDiscount);
}
/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.fulltl.wemall.modules.wx.dao;

import java.util.List;

import com.fulltl.wemall.common.persistence.CrudDao;
import com.fulltl.wemall.common.persistence.annotation.MyBatisDao;
import com.fulltl.wemall.modules.wx.entity.ConActivity;

/**
 * 活动表DAO接口
 * @author 黄健
 * @version 2017-10-14
 */
@MyBatisDao
public interface ConActivityDao extends CrudDao<ConActivity> {

	List<ConActivity> findCurActivityByDate(String date);

	ConActivity findLastActivityByFromdate();
	
}
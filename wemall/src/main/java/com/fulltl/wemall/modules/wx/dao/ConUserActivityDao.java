/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.fulltl.wemall.modules.wx.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import com.fulltl.wemall.common.persistence.CrudDao;
import com.fulltl.wemall.common.persistence.annotation.MyBatisDao;
import com.fulltl.wemall.modules.wx.entity.ConUserActivity;

/**
 * 用户活动表DAO接口
 * @author 黄健
 * @version 2017-10-14
 */
@MyBatisDao
public interface ConUserActivityDao extends CrudDao<ConUserActivity> {
	public ConUserActivity getByActUserId (@Param(value="activityId") String activityId, @Param(value="userId") String userId);
	//mobile
	public ConUserActivity getByActUserPhone(@Param(value="mobile") String mobile);
	//根据活动Id与状态
	public List<ConUserActivity> getByActidStatu (@Param(value="activityId") String activityId, @Param(value="status") String status);
	
	public void updatePriceBy(ConUserActivity conUserActivity);
	
}
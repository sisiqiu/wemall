/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.fulltl.wemall.modules.cms.dao;

import com.fulltl.wemall.common.persistence.CrudDao;
import com.fulltl.wemall.common.persistence.annotation.MyBatisDao;
import com.fulltl.wemall.modules.cms.entity.CmsFeedback;

/**
 * 管理用户反馈信息DAO接口
 * @author hj
 * @version 2017-11-02
 */
@MyBatisDao
public interface CmsFeedbackDao extends CrudDao<CmsFeedback> {
	
}
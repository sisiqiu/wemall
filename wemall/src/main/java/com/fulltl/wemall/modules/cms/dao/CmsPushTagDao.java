/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.fulltl.wemall.modules.cms.dao;

import com.fulltl.wemall.common.persistence.CrudDao;
import com.fulltl.wemall.common.persistence.annotation.MyBatisDao;
import com.fulltl.wemall.modules.cms.entity.CmsPushTag;

/**
 * 标签管理DAO接口
 * @author ldk
 * @version 2017-12-09
 */
@MyBatisDao
public interface CmsPushTagDao extends CrudDao<CmsPushTag> {
	
}
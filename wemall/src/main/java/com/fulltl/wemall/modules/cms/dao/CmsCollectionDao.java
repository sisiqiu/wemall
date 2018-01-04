/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.fulltl.wemall.modules.cms.dao;

import com.fulltl.wemall.common.persistence.CrudDao;
import com.fulltl.wemall.common.persistence.annotation.MyBatisDao;
import com.fulltl.wemall.modules.cms.entity.CmsCollection;

/**
 * 对用户所收藏的商品进行管理DAO接口
 * @author hj
 * @version 2017-11-15
 */
@MyBatisDao
public interface CmsCollectionDao extends CrudDao<CmsCollection> {
	
}
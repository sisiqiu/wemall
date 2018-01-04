/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.fulltl.wemall.modules.cms.dao;

import com.fulltl.wemall.common.persistence.CrudDao;
import com.fulltl.wemall.common.persistence.annotation.MyBatisDao;
import com.fulltl.wemall.modules.cms.entity.CmsUserRegid;

/**
 * 用户-极光注册id管理DAO接口
 * @author ldk
 * @version 2017-12-09
 */
@MyBatisDao
public interface CmsUserRegidDao extends CrudDao<CmsUserRegid> {

}
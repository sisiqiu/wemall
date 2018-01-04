/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.fulltl.wemall.modules.wx.dao;

import com.fulltl.wemall.common.persistence.CrudDao;
import com.fulltl.wemall.common.persistence.annotation.MyBatisDao;
import com.fulltl.wemall.modules.wx.entity.ConGroupsms;

/**
 * 群发短信管理DAO接口
 * @author ldk
 * @version 2017-10-24
 */
@MyBatisDao
public interface ConGroupsmsDao extends CrudDao<ConGroupsms> {
	
}
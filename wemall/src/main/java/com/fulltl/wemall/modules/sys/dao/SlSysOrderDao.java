/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.fulltl.wemall.modules.sys.dao;

import com.fulltl.wemall.common.persistence.CrudDao;
import com.fulltl.wemall.common.persistence.annotation.MyBatisDao;
import com.fulltl.wemall.modules.sys.entity.SlSysOrder;

/**
 * 订单管理DAO接口
 * @author ldk
 * @version 2017-11-27
 */
@MyBatisDao
public interface SlSysOrderDao extends CrudDao<SlSysOrder> {
	
}
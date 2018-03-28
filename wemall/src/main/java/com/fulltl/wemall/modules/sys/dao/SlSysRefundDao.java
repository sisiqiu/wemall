/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.fulltl.wemall.modules.sys.dao;

import com.fulltl.wemall.common.persistence.CrudDao;
import com.fulltl.wemall.common.persistence.annotation.MyBatisDao;
import com.fulltl.wemall.modules.sys.entity.SlSysRefund;

/**
 * 退款管理DAO接口
 * @author ldk
 * @version 2018-01-30
 */
@MyBatisDao
public interface SlSysRefundDao extends CrudDao<SlSysRefund> {
	
}
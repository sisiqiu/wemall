/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.fulltl.wemall.modules.wemall.dao;

import com.fulltl.wemall.common.persistence.CrudDao;
import com.fulltl.wemall.common.persistence.annotation.MyBatisDao;
import com.fulltl.wemall.modules.wemall.entity.WemallRecharge;

/**
 * 充值设定管理DAO接口
 * @author ldk
 * @version 2018-04-18
 */
@MyBatisDao
public interface WemallRechargeDao extends CrudDao<WemallRecharge> {
	
}
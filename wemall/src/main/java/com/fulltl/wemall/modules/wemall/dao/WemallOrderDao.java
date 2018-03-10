/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.fulltl.wemall.modules.wemall.dao;

import com.fulltl.wemall.common.persistence.CrudDao;
import com.fulltl.wemall.common.persistence.annotation.MyBatisDao;
import com.fulltl.wemall.modules.wemall.entity.WemallOrder;

/**
 * 订单管理DAO接口
 * @author ldk
 * @version 2018-01-05
 */
@MyBatisDao
public interface WemallOrderDao extends CrudDao<WemallOrder> {
	
	public void updateTotalRefundFee(WemallOrder wemallOrder);

	public void updatePrepayIdAndPayMethod(WemallOrder wemallOrder);
}
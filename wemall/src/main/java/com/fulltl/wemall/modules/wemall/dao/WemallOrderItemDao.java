/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.fulltl.wemall.modules.wemall.dao;

import com.fulltl.wemall.common.persistence.CrudDao;
import com.fulltl.wemall.common.persistence.annotation.MyBatisDao;
import com.fulltl.wemall.modules.wemall.entity.WemallOrderItem;

/**
 * 订单-商品管理DAO接口
 * @author ldk
 * @version 2018-01-05
 */
@MyBatisDao
public interface WemallOrderItemDao extends CrudDao<WemallOrderItem> {

	public void saveBuyerEvaluate(WemallOrderItem wemallOrderItem);

	public void updateStatusByOrderNo(WemallOrderItem wemallOrderItem);
	
}
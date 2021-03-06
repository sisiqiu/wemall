/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.fulltl.wemall.modules.wemall.dao;

import java.util.List;

import com.fulltl.wemall.common.persistence.CrudDao;
import com.fulltl.wemall.common.persistence.annotation.MyBatisDao;
import com.fulltl.wemall.modules.wemall.entity.WemallItemSpec;

/**
 * 商品-属性管理DAO接口
 * @author ldk
 * @version 2018-02-01
 */
@MyBatisDao
public interface WemallItemSpecDao extends CrudDao<WemallItemSpec> {

	public void insertAll(List<WemallItemSpec> insertList);

	public void removeAll(List<WemallItemSpec> removeList);
	
}
/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.fulltl.wemall.modules.wemall.dao;

import com.fulltl.wemall.common.persistence.TreeDao;
import com.fulltl.wemall.common.persistence.annotation.MyBatisDao;
import com.fulltl.wemall.modules.wemall.entity.WemallItemSort;

/**
 * 商品分类管理DAO接口
 * @author ldk
 * @version 2018-01-10
 */
@MyBatisDao
public interface WemallItemSortDao extends TreeDao<WemallItemSort> {
	
}
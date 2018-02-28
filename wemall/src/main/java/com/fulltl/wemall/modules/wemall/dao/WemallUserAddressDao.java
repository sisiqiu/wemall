/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.fulltl.wemall.modules.wemall.dao;

import java.util.List;

import com.fulltl.wemall.common.persistence.CrudDao;
import com.fulltl.wemall.common.persistence.annotation.MyBatisDao;
import com.fulltl.wemall.modules.wemall.entity.WemallUserAddress;

/**
 * 收货地址管理DAO接口
 * @author ldk
 * @version 2018-01-05
 */
@MyBatisDao
public interface WemallUserAddressDao extends CrudDao<WemallUserAddress> {

	public void deleteByIds(List<String> ids);

	public void setNotDefaultUserAddr(String userId);

	public void setDefaultUserAddr(String id);
	
}
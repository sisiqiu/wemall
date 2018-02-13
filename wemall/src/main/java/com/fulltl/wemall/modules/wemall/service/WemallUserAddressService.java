/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.fulltl.wemall.modules.wemall.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fulltl.wemall.common.persistence.Page;
import com.fulltl.wemall.common.service.CrudService;
import com.fulltl.wemall.modules.wemall.entity.WemallUserAddress;
import com.fulltl.wemall.modules.wemall.dao.WemallUserAddressDao;

/**
 * 收货地址管理Service
 * @author ldk
 * @version 2018-01-05
 */
@Service
@Transactional(readOnly = true)
public class WemallUserAddressService extends CrudService<WemallUserAddressDao, WemallUserAddress> {

	public WemallUserAddress get(String id) {
		return super.get(id);
	}
	
	public List<WemallUserAddress> findList(WemallUserAddress wemallUserAddress) {
		return super.findList(wemallUserAddress);
	}
	
	public Page<WemallUserAddress> findPage(Page<WemallUserAddress> page, WemallUserAddress wemallUserAddress) {
		return super.findPage(page, wemallUserAddress);
	}
	
	@Transactional(readOnly = false)
	public void save(WemallUserAddress wemallUserAddress) {
		super.save(wemallUserAddress);
	}
	
	@Transactional(readOnly = false)
	public void delete(WemallUserAddress wemallUserAddress) {
		super.delete(wemallUserAddress);
	}

	@Transactional(readOnly = false)
	public void delete(List<String> ids) {
		dao.deleteByIds(ids);
	}

	@Transactional(readOnly = false)
	public void setNotDefaultUserAddr(String userId) {
		dao.setNotDefaultUserAddr(userId);
	}
	
	@Transactional(readOnly = false)
	public void setDefaultUserAddr(String id) {
		dao.setDefaultUserAddr(id);
	}
}
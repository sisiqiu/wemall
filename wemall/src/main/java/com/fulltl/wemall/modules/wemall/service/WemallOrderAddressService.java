/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.fulltl.wemall.modules.wemall.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fulltl.wemall.common.persistence.Page;
import com.fulltl.wemall.common.service.CrudService;
import com.fulltl.wemall.modules.wemall.entity.WemallOrderAddress;
import com.fulltl.wemall.modules.wemall.dao.WemallOrderAddressDao;

/**
 * 订单-地址管理Service
 * @author ldk
 * @version 2018-01-05
 */
@Service
@Transactional(readOnly = true)
public class WemallOrderAddressService extends CrudService<WemallOrderAddressDao, WemallOrderAddress> {

	public WemallOrderAddress get(String id) {
		return super.get(id);
	}
	
	public List<WemallOrderAddress> findList(WemallOrderAddress wemallOrderAddress) {
		return super.findList(wemallOrderAddress);
	}
	
	public Page<WemallOrderAddress> findPage(Page<WemallOrderAddress> page, WemallOrderAddress wemallOrderAddress) {
		return super.findPage(page, wemallOrderAddress);
	}
	
	@Transactional(readOnly = false)
	public void save(WemallOrderAddress wemallOrderAddress) {
		super.save(wemallOrderAddress);
	}
	
	@Transactional(readOnly = false)
	public void delete(WemallOrderAddress wemallOrderAddress) {
		super.delete(wemallOrderAddress);
	}
	
}
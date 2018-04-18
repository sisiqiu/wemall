/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.fulltl.wemall.modules.wemall.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fulltl.wemall.common.persistence.Page;
import com.fulltl.wemall.common.service.CrudService;
import com.fulltl.wemall.modules.wemall.entity.WemallRecharge;
import com.fulltl.wemall.modules.wemall.dao.WemallRechargeDao;

/**
 * 充值设定管理Service
 * @author ldk
 * @version 2018-04-18
 */
@Service
@Transactional(readOnly = true)
public class WemallRechargeService extends CrudService<WemallRechargeDao, WemallRecharge> {

	public WemallRecharge get(String id) {
		return super.get(id);
	}
	
	public List<WemallRecharge> findList(WemallRecharge wemallRecharge) {
		return super.findList(wemallRecharge);
	}
	
	public Page<WemallRecharge> findPage(Page<WemallRecharge> page, WemallRecharge wemallRecharge) {
		return super.findPage(page, wemallRecharge);
	}
	
	@Transactional(readOnly = false)
	public void save(WemallRecharge wemallRecharge) {
		super.save(wemallRecharge);
	}
	
	@Transactional(readOnly = false)
	public void delete(WemallRecharge wemallRecharge) {
		super.delete(wemallRecharge);
	}
	
}
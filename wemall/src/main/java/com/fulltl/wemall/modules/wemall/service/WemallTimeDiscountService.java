/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.fulltl.wemall.modules.wemall.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fulltl.wemall.common.persistence.Page;
import com.fulltl.wemall.common.service.CrudService;
import com.fulltl.wemall.modules.wemall.entity.WemallTeamDiscount;
import com.fulltl.wemall.modules.wemall.entity.WemallTimeDiscount;
import com.fulltl.wemall.modules.wemall.dao.WemallTimeDiscountDao;

/**
 * 限时打折活动管理Service
 * @author ldk
 * @version 2018-01-05
 */
@Service
@Transactional(readOnly = true)
public class WemallTimeDiscountService extends CrudService<WemallTimeDiscountDao, WemallTimeDiscount> {

	public WemallTimeDiscount get(String id) {
		return super.get(id);
	}
	
	public List<WemallTimeDiscount> findList(WemallTimeDiscount wemallTimeDiscount) {
		return super.findList(wemallTimeDiscount);
	}
	
	public Page<WemallTimeDiscount> findPage(Page<WemallTimeDiscount> page, WemallTimeDiscount wemallTimeDiscount) {
		return super.findPage(page, wemallTimeDiscount);
	}
	
	@Transactional(readOnly = false)
	public void save(WemallTimeDiscount wemallTimeDiscount) {
		super.save(wemallTimeDiscount);
	}
	
	@Transactional(readOnly = false)
	public void delete(WemallTimeDiscount wemallTimeDiscount) {
		super.delete(wemallTimeDiscount);
	}
	
	/**
	 * 查询未过期的限时打折活动列表
	 * @return
	 */
	public List<WemallTimeDiscount> findListNotTimeout() {
		return dao.findListNotTimeout(new WemallTimeDiscount());
	}
	
}
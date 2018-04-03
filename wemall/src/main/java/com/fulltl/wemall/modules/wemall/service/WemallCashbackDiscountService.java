/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.fulltl.wemall.modules.wemall.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fulltl.wemall.common.persistence.Page;
import com.fulltl.wemall.common.service.CrudService;
import com.fulltl.wemall.modules.wemall.dao.WemallCashbackDiscountDao;
import com.fulltl.wemall.modules.wemall.entity.WemallCashbackDiscount;

/**
 * 限时返现活动管理Service
 * @author ldk
 * @version 2018-01-05
 */
@Service
@Transactional(readOnly = true)
public class WemallCashbackDiscountService extends CrudService<WemallCashbackDiscountDao, WemallCashbackDiscount> {

	public WemallCashbackDiscount get(String id) {
		return super.get(id);
	}
	
	public List<WemallCashbackDiscount> findList(WemallCashbackDiscount wemallCashbackDiscount) {
		return super.findList(wemallCashbackDiscount);
	}
	
	public Page<WemallCashbackDiscount> findPage(Page<WemallCashbackDiscount> page, WemallCashbackDiscount wemallCashbackDiscount) {
		return super.findPage(page, wemallCashbackDiscount);
	}
	
	@Transactional(readOnly = false)
	public void save(WemallCashbackDiscount wemallCashbackDiscount) {
		super.save(wemallCashbackDiscount);
	}
	
	@Transactional(readOnly = false)
	public void delete(WemallCashbackDiscount wemallCashbackDiscount) {
		super.delete(wemallCashbackDiscount);
	}
	
	/**
	 * 查询未过期的限时返现活动列表
	 * @return
	 */
	public List<WemallCashbackDiscount> findListNotTimeout() {
		return dao.findListNotTimeout(new WemallCashbackDiscount());
	}
	
}
/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.fulltl.wemall.modules.wemall.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fulltl.wemall.common.persistence.Page;
import com.fulltl.wemall.common.service.CrudService;
import com.fulltl.wemall.modules.wemall.entity.WemallCashbackDiscount;
import com.fulltl.wemall.modules.wemall.entity.WemallFullDiscount;
import com.fulltl.wemall.modules.wemall.dao.WemallFullDiscountDao;

/**
 * 满减送活动管理Service
 * @author ldk
 * @version 2018-01-05
 */
@Service
@Transactional(readOnly = true)
public class WemallFullDiscountService extends CrudService<WemallFullDiscountDao, WemallFullDiscount> {

	public WemallFullDiscount get(String id) {
		return super.get(id);
	}
	
	public List<WemallFullDiscount> findList(WemallFullDiscount wemallFullDiscount) {
		return super.findList(wemallFullDiscount);
	}
	
	public Page<WemallFullDiscount> findPage(Page<WemallFullDiscount> page, WemallFullDiscount wemallFullDiscount) {
		return super.findPage(page, wemallFullDiscount);
	}
	
	@Transactional(readOnly = false)
	public void save(WemallFullDiscount wemallFullDiscount) {
		super.save(wemallFullDiscount);
	}
	
	@Transactional(readOnly = false)
	public void delete(WemallFullDiscount wemallFullDiscount) {
		super.delete(wemallFullDiscount);
	}
	
	/**
	 * 查询未过期的满减送活动列表
	 * @return
	 */
	public List<WemallFullDiscount> findListNotTimeout() {
		return dao.findListNotTimeout(new WemallFullDiscount());
	}
}
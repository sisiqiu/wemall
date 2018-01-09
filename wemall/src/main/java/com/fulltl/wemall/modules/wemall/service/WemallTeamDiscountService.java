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
import com.fulltl.wemall.modules.wemall.dao.WemallTeamDiscountDao;

/**
 * 限时拼团活动管理Service
 * @author ldk
 * @version 2018-01-05
 */
@Service
@Transactional(readOnly = true)
public class WemallTeamDiscountService extends CrudService<WemallTeamDiscountDao, WemallTeamDiscount> {

	public WemallTeamDiscount get(String id) {
		return super.get(id);
	}
	
	public List<WemallTeamDiscount> findList(WemallTeamDiscount wemallTeamDiscount) {
		return super.findList(wemallTeamDiscount);
	}
	
	public Page<WemallTeamDiscount> findPage(Page<WemallTeamDiscount> page, WemallTeamDiscount wemallTeamDiscount) {
		return super.findPage(page, wemallTeamDiscount);
	}
	
	@Transactional(readOnly = false)
	public void save(WemallTeamDiscount wemallTeamDiscount) {
		super.save(wemallTeamDiscount);
	}
	
	@Transactional(readOnly = false)
	public void delete(WemallTeamDiscount wemallTeamDiscount) {
		super.delete(wemallTeamDiscount);
	}
	
}
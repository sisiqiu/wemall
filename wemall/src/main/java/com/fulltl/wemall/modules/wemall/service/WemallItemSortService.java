/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.fulltl.wemall.modules.wemall.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fulltl.wemall.common.persistence.Page;
import com.fulltl.wemall.common.service.CrudService;
import com.fulltl.wemall.modules.wemall.entity.WemallItemSort;
import com.fulltl.wemall.modules.wemall.dao.WemallItemSortDao;

/**
 * 商品分类管理Service
 * @author ldk
 * @version 2018-01-05
 */
@Service
@Transactional(readOnly = true)
public class WemallItemSortService extends CrudService<WemallItemSortDao, WemallItemSort> {

	public WemallItemSort get(String id) {
		return super.get(id);
	}
	
	public List<WemallItemSort> findList(WemallItemSort wemallItemSort) {
		return super.findList(wemallItemSort);
	}
	
	public Page<WemallItemSort> findPage(Page<WemallItemSort> page, WemallItemSort wemallItemSort) {
		return super.findPage(page, wemallItemSort);
	}
	
	@Transactional(readOnly = false)
	public void save(WemallItemSort wemallItemSort) {
		super.save(wemallItemSort);
	}
	
	@Transactional(readOnly = false)
	public void delete(WemallItemSort wemallItemSort) {
		super.delete(wemallItemSort);
	}
	
}
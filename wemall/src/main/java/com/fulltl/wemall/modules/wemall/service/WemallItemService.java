/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.fulltl.wemall.modules.wemall.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fulltl.wemall.common.persistence.Page;
import com.fulltl.wemall.common.service.CrudService;
import com.fulltl.wemall.modules.wemall.entity.WemallItem;
import com.fulltl.wemall.modules.wemall.dao.WemallItemDao;

/**
 * 商品管理Service
 * @author ldk
 * @version 2018-01-05
 */
@Service
@Transactional(readOnly = true)
public class WemallItemService extends CrudService<WemallItemDao, WemallItem> {

	public WemallItem get(String id) {
		return super.get(id);
	}
	
	public List<WemallItem> findList(WemallItem wemallItem) {
		return super.findList(wemallItem);
	}
	
	public Page<WemallItem> findPage(Page<WemallItem> page, WemallItem wemallItem) {
		return super.findPage(page, wemallItem);
	}
	
	@Transactional(readOnly = false)
	public void save(WemallItem wemallItem) {
		super.save(wemallItem);
	}
	
	@Transactional(readOnly = false)
	public void delete(WemallItem wemallItem) {
		super.delete(wemallItem);
	}
	
}
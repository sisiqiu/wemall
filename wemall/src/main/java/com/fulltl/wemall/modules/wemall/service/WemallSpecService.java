/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.fulltl.wemall.modules.wemall.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fulltl.wemall.common.persistence.Page;
import com.fulltl.wemall.common.service.CrudService;
import com.fulltl.wemall.modules.wemall.entity.WemallSpec;
import com.fulltl.wemall.modules.wemall.dao.WemallSpecDao;

/**
 * 属性类别管理Service
 * @author ldk
 * @version 2018-01-05
 */
@Service
@Transactional(readOnly = true)
public class WemallSpecService extends CrudService<WemallSpecDao, WemallSpec> {

	public WemallSpec get(String id) {
		return super.get(id);
	}
	
	public List<WemallSpec> findList(WemallSpec wemallSpec) {
		return super.findList(wemallSpec);
	}
	
	public Page<WemallSpec> findPage(Page<WemallSpec> page, WemallSpec wemallSpec) {
		return super.findPage(page, wemallSpec);
	}
	
	@Transactional(readOnly = false)
	public void save(WemallSpec wemallSpec) {
		super.save(wemallSpec);
	}
	
	@Transactional(readOnly = false)
	public void delete(WemallSpec wemallSpec) {
		super.delete(wemallSpec);
	}
	
}
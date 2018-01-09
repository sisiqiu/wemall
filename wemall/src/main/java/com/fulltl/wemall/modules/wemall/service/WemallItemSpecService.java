/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.fulltl.wemall.modules.wemall.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fulltl.wemall.common.persistence.Page;
import com.fulltl.wemall.common.service.CrudService;
import com.fulltl.wemall.modules.wemall.dao.WemallItemSpecDao;
import com.fulltl.wemall.modules.wemall.entity.WemallItemSpec;

/**
 * 商品-属性管理Service
 * @author ldk
 * @version 2018-01-05
 */
@Service
@Transactional(readOnly = true)
public class WemallItemSpecService extends CrudService<WemallItemSpecDao, WemallItemSpec> {

	public WemallItemSpec get(Integer itemId, Integer specId) {
		WemallItemSpec wemallItemSpec = new WemallItemSpec();
		wemallItemSpec.setItemId(itemId);
		wemallItemSpec.setSpecId(specId);
		return super.get(wemallItemSpec);
	}
	
	public List<WemallItemSpec> findList(WemallItemSpec wemallItemSpec) {
		return super.findList(wemallItemSpec);
	}
	
	public Page<WemallItemSpec> findPage(Page<WemallItemSpec> page, WemallItemSpec wemallItemSpec) {
		return super.findPage(page, wemallItemSpec);
	}
	
	@Transactional(readOnly = false)
	public void save(WemallItemSpec wemallItemSpec) {
		super.save(wemallItemSpec);
	}
	
	@Transactional(readOnly = false)
	public void delete(WemallItemSpec wemallItemSpec) {
		super.delete(wemallItemSpec);
	}
	
}
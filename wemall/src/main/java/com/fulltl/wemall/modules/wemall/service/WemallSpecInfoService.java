/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.fulltl.wemall.modules.wemall.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fulltl.wemall.common.persistence.Page;
import com.fulltl.wemall.common.service.CrudService;
import com.fulltl.wemall.modules.wemall.entity.WemallSpecInfo;
import com.fulltl.wemall.modules.wemall.dao.WemallSpecInfoDao;

/**
 * 属性值管理Service
 * @author ldk
 * @version 2018-01-05
 */
@Service
@Transactional(readOnly = true)
public class WemallSpecInfoService extends CrudService<WemallSpecInfoDao, WemallSpecInfo> {

	public WemallSpecInfo get(String id) {
		return super.get(id);
	}
	
	public List<WemallSpecInfo> findList(WemallSpecInfo wemallSpecInfo) {
		return super.findList(wemallSpecInfo);
	}
	
	public Page<WemallSpecInfo> findPage(Page<WemallSpecInfo> page, WemallSpecInfo wemallSpecInfo) {
		return super.findPage(page, wemallSpecInfo);
	}
	
	@Transactional(readOnly = false)
	public void save(WemallSpecInfo wemallSpecInfo) {
		super.save(wemallSpecInfo);
	}
	
	@Transactional(readOnly = false)
	public void delete(WemallSpecInfo wemallSpecInfo) {
		super.delete(wemallSpecInfo);
	}
	
}
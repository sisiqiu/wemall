/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.fulltl.wemall.modules.wemall.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fulltl.wemall.common.persistence.Page;
import com.fulltl.wemall.common.service.CrudService;
import com.fulltl.wemall.modules.wemall.entity.WemallBountyInfo;
import com.fulltl.wemall.modules.wemall.dao.WemallBountyInfoDao;

/**
 * 奖励金管理Service
 * @author ldk
 * @version 2018-01-05
 */
@Service
@Transactional(readOnly = true)
public class WemallBountyInfoService extends CrudService<WemallBountyInfoDao, WemallBountyInfo> {

	public WemallBountyInfo get(String id) {
		return super.get(id);
	}
	
	public List<WemallBountyInfo> findList(WemallBountyInfo wemallBountyInfo) {
		return super.findList(wemallBountyInfo);
	}
	
	public Page<WemallBountyInfo> findPage(Page<WemallBountyInfo> page, WemallBountyInfo wemallBountyInfo) {
		return super.findPage(page, wemallBountyInfo);
	}
	
	@Transactional(readOnly = false)
	public void save(WemallBountyInfo wemallBountyInfo) {
		super.save(wemallBountyInfo);
	}
	
	@Transactional(readOnly = false)
	public void delete(WemallBountyInfo wemallBountyInfo) {
		super.delete(wemallBountyInfo);
	}
	
}
/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.fulltl.wemall.modules.wemall.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fulltl.wemall.common.persistence.Page;
import com.fulltl.wemall.common.service.CrudService;
import com.fulltl.wemall.modules.wemall.entity.WemallFreightInfo;
import com.fulltl.wemall.modules.wemall.dao.WemallFreightInfoDao;

/**
 * 物流明细管理Service
 * @author ldk
 * @version 2018-01-05
 */
@Service
@Transactional(readOnly = true)
public class WemallFreightInfoService extends CrudService<WemallFreightInfoDao, WemallFreightInfo> {

	public WemallFreightInfo get(String id) {
		return super.get(id);
	}
	
	public List<WemallFreightInfo> findList(WemallFreightInfo wemallFreightInfo) {
		return super.findList(wemallFreightInfo);
	}
	
	public Page<WemallFreightInfo> findPage(Page<WemallFreightInfo> page, WemallFreightInfo wemallFreightInfo) {
		return super.findPage(page, wemallFreightInfo);
	}
	
	@Transactional(readOnly = false)
	public void save(WemallFreightInfo wemallFreightInfo) {
		super.save(wemallFreightInfo);
	}
	
	@Transactional(readOnly = false)
	public void delete(WemallFreightInfo wemallFreightInfo) {
		super.delete(wemallFreightInfo);
	}
	
}
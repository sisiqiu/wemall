/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.fulltl.wemall.modules.wemall.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fulltl.wemall.common.persistence.Page;
import com.fulltl.wemall.common.service.CrudService;
import com.fulltl.wemall.modules.wemall.entity.WemallScoreInfo;
import com.fulltl.wemall.modules.wemall.dao.WemallScoreInfoDao;

/**
 * 积分明细管理Service
 * @author ldk
 * @version 2018-01-05
 */
@Service
@Transactional(readOnly = true)
public class WemallScoreInfoService extends CrudService<WemallScoreInfoDao, WemallScoreInfo> {

	public WemallScoreInfo get(String id) {
		return super.get(id);
	}
	
	public List<WemallScoreInfo> findList(WemallScoreInfo wemallScoreInfo) {
		return super.findList(wemallScoreInfo);
	}
	
	public Page<WemallScoreInfo> findPage(Page<WemallScoreInfo> page, WemallScoreInfo wemallScoreInfo) {
		return super.findPage(page, wemallScoreInfo);
	}
	
	@Transactional(readOnly = false)
	public void save(WemallScoreInfo wemallScoreInfo) {
		super.save(wemallScoreInfo);
	}
	
	@Transactional(readOnly = false)
	public void delete(WemallScoreInfo wemallScoreInfo) {
		super.delete(wemallScoreInfo);
	}
	
}
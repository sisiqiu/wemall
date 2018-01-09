/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.fulltl.wemall.modules.wemall.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fulltl.wemall.common.persistence.Page;
import com.fulltl.wemall.common.service.CrudService;
import com.fulltl.wemall.modules.wemall.entity.WemallSignIn;
import com.fulltl.wemall.modules.wemall.dao.WemallSignInDao;

/**
 * 签到管理Service
 * @author ldk
 * @version 2018-01-05
 */
@Service
@Transactional(readOnly = true)
public class WemallSignInService extends CrudService<WemallSignInDao, WemallSignIn> {

	public WemallSignIn get(String id) {
		return super.get(id);
	}
	
	public List<WemallSignIn> findList(WemallSignIn wemallSignIn) {
		return super.findList(wemallSignIn);
	}
	
	public Page<WemallSignIn> findPage(Page<WemallSignIn> page, WemallSignIn wemallSignIn) {
		return super.findPage(page, wemallSignIn);
	}
	
	@Transactional(readOnly = false)
	public void save(WemallSignIn wemallSignIn) {
		super.save(wemallSignIn);
	}
	
	@Transactional(readOnly = false)
	public void delete(WemallSignIn wemallSignIn) {
		super.delete(wemallSignIn);
	}
	
}
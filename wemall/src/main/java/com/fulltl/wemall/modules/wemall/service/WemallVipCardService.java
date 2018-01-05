/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.fulltl.wemall.modules.wemall.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fulltl.wemall.common.persistence.Page;
import com.fulltl.wemall.common.service.CrudService;
import com.fulltl.wemall.modules.wemall.entity.WemallVipCard;
import com.fulltl.wemall.modules.wemall.dao.WemallVipCardDao;

/**
 * 会员卡管理Service
 * @author ldk
 * @version 2018-01-05
 */
@Service
@Transactional(readOnly = true)
public class WemallVipCardService extends CrudService<WemallVipCardDao, WemallVipCard> {

	public WemallVipCard get(String id) {
		return super.get(id);
	}
	
	public List<WemallVipCard> findList(WemallVipCard wemallVipCard) {
		return super.findList(wemallVipCard);
	}
	
	public Page<WemallVipCard> findPage(Page<WemallVipCard> page, WemallVipCard wemallVipCard) {
		return super.findPage(page, wemallVipCard);
	}
	
	@Transactional(readOnly = false)
	public void save(WemallVipCard wemallVipCard) {
		super.save(wemallVipCard);
	}
	
	@Transactional(readOnly = false)
	public void delete(WemallVipCard wemallVipCard) {
		super.delete(wemallVipCard);
	}
	
}
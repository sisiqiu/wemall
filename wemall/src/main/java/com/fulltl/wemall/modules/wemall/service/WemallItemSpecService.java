/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.fulltl.wemall.modules.wemall.service;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fulltl.wemall.common.persistence.Page;
import com.fulltl.wemall.common.service.CrudService;
import com.fulltl.wemall.modules.wemall.entity.WemallItemSpec;
import com.google.common.collect.Lists;
import com.fulltl.wemall.modules.wemall.dao.WemallItemSpecDao;

/**
 * 商品-属性管理Service
 * @author ldk
 * @version 2018-02-01
 */
@Service
@Transactional(readOnly = true)
public class WemallItemSpecService extends CrudService<WemallItemSpecDao, WemallItemSpec> {

	public WemallItemSpec get(String id) {
		return super.get(id);
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

	@Transactional(readOnly = false)
	public void updateItemSpecList(List<WemallItemSpec> newList, String itemId) {
		Date now = new Date();
		for(WemallItemSpec entity : newList) {
			entity.setCreateDate(now);
			entity.setUpdateDate(now);
			entity.setItemId(itemId);
		}
		WemallItemSpec query = new WemallItemSpec();
		query.setItemId(itemId);
		List<WemallItemSpec> oldList = this.findList(query);
		
		List<WemallItemSpec> addList = Lists.newArrayList();
		List<WemallItemSpec> removeList = Lists.newArrayList();
		addList.addAll(newList);
		removeList.addAll(oldList);
		
		Collection retainAll = CollectionUtils.retainAll(newList, oldList);
		addList.removeAll(retainAll);
		removeList.removeAll(retainAll);
		
		if(addList.size() > 0) dao.insertAll(addList);
		if(removeList.size() > 0) dao.removeAll(removeList);
	}
	
}
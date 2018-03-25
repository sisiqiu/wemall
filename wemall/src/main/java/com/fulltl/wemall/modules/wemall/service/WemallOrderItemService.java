/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.fulltl.wemall.modules.wemall.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fulltl.wemall.common.persistence.Page;
import com.fulltl.wemall.common.service.CrudService;
import com.fulltl.wemall.modules.wemall.entity.WemallItemSpec;
import com.fulltl.wemall.modules.wemall.entity.WemallOrderItem;
import com.fulltl.wemall.modules.wemall.dao.WemallOrderItemDao;

/**
 * 订单-商品管理Service
 * @author ldk
 * @version 2018-01-05
 */
@Service
@Transactional(readOnly = true)
public class WemallOrderItemService extends CrudService<WemallOrderItemDao, WemallOrderItem> {

	public WemallOrderItem get(String orderNo, String itemId) {
		WemallOrderItem wemallOrderItem = new WemallOrderItem();
		wemallOrderItem.setOrderNo(orderNo);
		wemallOrderItem.setItemId(itemId);
		return super.get(wemallOrderItem);
	}
	
	public List<WemallOrderItem> findList(WemallOrderItem wemallOrderItem) {
		return super.findList(wemallOrderItem);
	}
	
	public Page<WemallOrderItem> findPage(Page<WemallOrderItem> page, WemallOrderItem wemallOrderItem) {
		return super.findPage(page, wemallOrderItem);
	}
	
	@Transactional(readOnly = false)
	public void save(WemallOrderItem wemallOrderItem) {
		super.save(wemallOrderItem);
	}
	
	@Transactional(readOnly = false)
	public void delete(WemallOrderItem wemallOrderItem) {
		super.delete(wemallOrderItem);
	}

	@Transactional(readOnly = false)
	public void saveBuyerEvaluate(WemallOrderItem wemallOrderItem) {
		dao.saveBuyerEvaluate(wemallOrderItem);
	}

	@Transactional(readOnly = false)
	public void updateStatusByOrderNo(String orderNo, Integer status) {
		WemallOrderItem wemallOrderItem = new WemallOrderItem();
		wemallOrderItem.setOrderNo(orderNo);
		wemallOrderItem.setStatus(status);
		dao.updateStatusByOrderNo(wemallOrderItem);
	}
	
}
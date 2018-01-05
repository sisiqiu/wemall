/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.fulltl.wemall.modules.wemall.service;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fulltl.wemall.common.persistence.Page;
import com.fulltl.wemall.common.service.CrudService;
import com.fulltl.wemall.common.utils.IdGen;
import com.fulltl.wemall.modules.wemall.entity.WemallOrder;
import com.fulltl.wemall.modules.wemall.dao.WemallOrderDao;

/**
 * 订单管理Service
 * @author ldk
 * @version 2018-01-05
 */
@Service
@Transactional(readOnly = true)
public class WemallOrderService extends CrudService<WemallOrderDao, WemallOrder> {

	public WemallOrder get(String id) {
		return super.get(id);
	}
	
	public List<WemallOrder> findList(WemallOrder wemallOrder) {
		return super.findList(wemallOrder);
	}
	
	public Page<WemallOrder> findPage(Page<WemallOrder> page, WemallOrder wemallOrder) {
		return super.findPage(page, wemallOrder);
	}
	
	@Transactional(readOnly = false)
	public void save(WemallOrder wemallOrder) {
		if(StringUtils.isBlank(wemallOrder.getOrderNo())) {
			wemallOrder.setOrderNo(IdGen.uuid());
		}
		super.save(wemallOrder);
	}
	
	@Transactional(readOnly = false)
	public void delete(WemallOrder wemallOrder) {
		super.delete(wemallOrder);
	}
	
}
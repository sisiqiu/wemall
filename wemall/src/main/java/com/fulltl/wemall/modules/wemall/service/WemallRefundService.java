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
import com.fulltl.wemall.modules.wemall.entity.WemallRefund;
import com.fulltl.wemall.modules.wemall.dao.WemallRefundDao;

/**
 * 退款管理Service
 * @author ldk
 * @version 2018-01-05
 */
@Service
@Transactional(readOnly = true)
public class WemallRefundService extends CrudService<WemallRefundDao, WemallRefund> {

	public WemallRefund get(String id) {
		return super.get(id);
	}
	
	public List<WemallRefund> findList(WemallRefund wemallRefund) {
		return super.findList(wemallRefund);
	}
	
	public Page<WemallRefund> findPage(Page<WemallRefund> page, WemallRefund wemallRefund) {
		return super.findPage(page, wemallRefund);
	}
	
	@Transactional(readOnly = false)
	public void save(WemallRefund wemallRefund) {
		if (wemallRefund.getIsNewRecord()){
			wemallRefund.preInsert();
			dao.insert(wemallRefund);
		}else{
			if(StringUtils.isBlank(wemallRefund.getRefundId())) {
				wemallRefund.setRefundId(IdGen.uuid());
				wemallRefund.preInsert();
				dao.insert(wemallRefund);
			} else {
				wemallRefund.preUpdate();
				dao.update(wemallRefund);
			}
		}
	}
	
	@Transactional(readOnly = false)
	public void delete(WemallRefund wemallRefund) {
		super.delete(wemallRefund);
	}
	
}
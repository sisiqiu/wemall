/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.fulltl.wemall.modules.sys.service;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fulltl.wemall.common.persistence.Page;
import com.fulltl.wemall.common.service.CrudService;
import com.fulltl.wemall.common.utils.IdGen;
import com.fulltl.wemall.modules.sys.dao.SlSysRefundDao;
import com.fulltl.wemall.modules.sys.entity.SlSysRefund;

/**
 * 退款管理Service
 * @author ldk
 * @version 2018-01-30
 */
@Service
@Transactional(readOnly = true)
public class SlSysRefundService extends CrudService<SlSysRefundDao, SlSysRefund> {

	public SlSysRefund get(String id) {
		return super.get(id);
	}
	
	public List<SlSysRefund> findList(SlSysRefund slSysRefund) {
		return super.findList(slSysRefund);
	}
	
	public Page<SlSysRefund> findPage(Page<SlSysRefund> page, SlSysRefund slSysRefund) {
		return super.findPage(page, slSysRefund);
	}
	
	@Transactional(readOnly = false)
	public void save(SlSysRefund slSysRefund) {
		if (slSysRefund.getIsNewRecord()){
			slSysRefund.preInsert();
			dao.insert(slSysRefund);
		}else{
			if(StringUtils.isBlank(slSysRefund.getRefundId())) {
				slSysRefund.setRefundId(IdGen.generateRefundId());
				slSysRefund.preInsert();
				dao.insert(slSysRefund);
			} else {
				slSysRefund.preUpdate();
				dao.update(slSysRefund);
			}
		}
	}
	
	@Transactional(readOnly = false)
	public void delete(SlSysRefund slSysRefund) {
		super.delete(slSysRefund);
	}
	
}
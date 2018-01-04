/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.fulltl.wemall.modules.wx.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fulltl.wemall.common.persistence.Page;
import com.fulltl.wemall.common.service.CrudService;
import com.fulltl.wemall.modules.wx.dao.WxServiceaccountDao;
import com.fulltl.wemall.modules.wx.entity.WxServiceaccount;

/**
 * 微信服务号管理Service
 * @author ldk
 * @version 2017-10-13
 */
@Service
@Transactional(readOnly = true)
public class WxServiceaccountService extends CrudService<WxServiceaccountDao, WxServiceaccount> {

	public WxServiceaccount get(String id) {
		return super.get(id);
	}
	
	public List<WxServiceaccount> findList(WxServiceaccount wxServiceaccount) {
		return super.findList(wxServiceaccount);
	}
	
	public Page<WxServiceaccount> findPage(Page<WxServiceaccount> page, WxServiceaccount wxServiceaccount) {
		return super.findPage(page, wxServiceaccount);
	}
	
	@Transactional(readOnly = false)
	public void save(WxServiceaccount wxServiceaccount) {
		super.save(wxServiceaccount);
	}
	
	@Transactional(readOnly = false)
	public void delete(WxServiceaccount wxServiceaccount) {
		super.delete(wxServiceaccount);
	}

	public WxServiceaccount findByServiceId(String serviceId) {
		return dao.findByServiceId(serviceId);
	}
	
}
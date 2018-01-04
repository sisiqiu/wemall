/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.fulltl.wemall.modules.wx.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fulltl.wemall.common.persistence.Page;
import com.fulltl.wemall.common.service.CrudService;
import com.fulltl.wemall.modules.wx.dao.WxMessagearticlesDao;
import com.fulltl.wemall.modules.wx.entity.WxMessagearticles;

/**
 * 微信消息文章实体类Service
 * @author ldk
 * @version 2017-10-11
 */
@Service
@Transactional(readOnly = true)
public class WxMessagearticlesService extends CrudService<WxMessagearticlesDao, WxMessagearticles> {

	public WxMessagearticles get(String id) {
		return super.get(id);
	}
	
	public List<WxMessagearticles> findList(WxMessagearticles wxMessagearticles) {
		return super.findList(wxMessagearticles);
	}
	
	public Page<WxMessagearticles> findPage(Page<WxMessagearticles> page, WxMessagearticles wxMessagearticles) {
		return super.findPage(page, wxMessagearticles);
	}
	
	@Transactional(readOnly = false)
	public void save(WxMessagearticles wxMessagearticles) {
		super.save(wxMessagearticles);
	}
	
	@Transactional(readOnly = false)
	public void delete(WxMessagearticles wxMessagearticles) {
		super.delete(wxMessagearticles);
	}
	
}
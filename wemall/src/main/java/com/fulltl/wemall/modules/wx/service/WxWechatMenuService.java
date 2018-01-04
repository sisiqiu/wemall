/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.fulltl.wemall.modules.wx.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fulltl.wemall.common.service.TreeService;
import com.fulltl.wemall.common.utils.StringUtils;
import com.fulltl.wemall.modules.wx.dao.WxWechatMenuDao;
import com.fulltl.wemall.modules.wx.entity.WxWechatMenu;

/**
 * 微信服务号菜单管理Service
 * @author ldk
 * @version 2017-10-13
 */
@Service
@Transactional(readOnly = true)
public class WxWechatMenuService extends TreeService<WxWechatMenuDao, WxWechatMenu> {

	public WxWechatMenu get(String id) {
		return super.get(id);
	}
	
	public List<WxWechatMenu> findList(WxWechatMenu wxWechatMenu) {
		if (StringUtils.isNotBlank(wxWechatMenu.getParentIds())){
			wxWechatMenu.setParentIds(","+wxWechatMenu.getParentIds()+",");
		}
		return super.findList(wxWechatMenu);
	}
	
	@Transactional(readOnly = false)
	public void save(WxWechatMenu wxWechatMenu) {
		super.save(wxWechatMenu);
	}
	
	@Transactional(readOnly = false)
	public void delete(WxWechatMenu wxWechatMenu) {
		super.delete(wxWechatMenu);
	}

	/**
	 * 根据服务号主键id获取菜单列表。
	 * @param saId
	 * @return
	 */
	public List<WxWechatMenu> findBySaId(int saId) {
		List<WxWechatMenu> menus = dao.findBySaId(saId);
		return menus;
	}

	/**
	 * 根据服务号主键id和key值获取指定的菜单对象。
	 * @param i
	 * @param keyValue
	 * @return
	 */
	public WxWechatMenu findBySaIdAndKey(int saId, String menukey) {
		// TODO Auto-generated method stub
		return dao.findBySaIdAndKey(saId, menukey);
	}
	
}
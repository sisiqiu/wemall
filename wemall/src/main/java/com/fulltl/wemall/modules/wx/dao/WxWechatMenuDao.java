/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.fulltl.wemall.modules.wx.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.fulltl.wemall.common.persistence.TreeDao;
import com.fulltl.wemall.common.persistence.annotation.MyBatisDao;
import com.fulltl.wemall.modules.wx.entity.WxWechatMenu;

/**
 * 微信服务号菜单管理DAO接口
 * @author ldk
 * @version 2017-10-13
 */
@MyBatisDao
public interface WxWechatMenuDao extends TreeDao<WxWechatMenu> {

	List<WxWechatMenu> findBySaId(int saId);

	WxWechatMenu findBySaIdAndKey(@Param(value="saId") int saId, @Param(value="menukey") String menukey);
	
}
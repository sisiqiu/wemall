/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.fulltl.wemall.modules.wx.dao;

import com.fulltl.wemall.common.persistence.CrudDao;
import com.fulltl.wemall.common.persistence.annotation.MyBatisDao;
import com.fulltl.wemall.modules.wx.entity.WxServiceaccount;

/**
 * 微信服务号管理DAO接口
 * @author ldk
 * @version 2017-10-13
 */
@MyBatisDao
public interface WxServiceaccountDao extends CrudDao<WxServiceaccount> {

	WxServiceaccount findByServiceId(String serviceId);
	
}
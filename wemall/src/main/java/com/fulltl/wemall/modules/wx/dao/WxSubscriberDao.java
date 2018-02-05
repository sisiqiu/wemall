/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.fulltl.wemall.modules.wx.dao;

import com.fulltl.wemall.common.persistence.CrudDao;
import com.fulltl.wemall.common.persistence.annotation.MyBatisDao;
import com.fulltl.wemall.modules.wx.entity.WxSubscriber;

/**
 * 微信关注用户管理DAO接口
 * @author ldk
 * @version 2017-10-13
 */
@MyBatisDao
public interface WxSubscriberDao extends CrudDao<WxSubscriber> {

	WxSubscriber findByOpenId(String openId);
	
	public void updateByOpenId(WxSubscriber wxSubscriber);
	
}
/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.fulltl.wemall.modules.wx.dao;

import com.fulltl.wemall.common.persistence.CrudDao;
import com.fulltl.wemall.common.persistence.annotation.MyBatisDao;
import com.fulltl.wemall.modules.wx.entity.WxUserInfo;

/**
 * 微信用户信息管理DAO接口
 * @author ldk
 * @version 2018-02-18
 */
@MyBatisDao
public interface WxUserInfoDao extends CrudDao<WxUserInfo> {

	public WxUserInfo findByOpenId(String openId);

	public void updateInfoByOpenId(WxUserInfo curWxUserInfo);

	public WxUserInfo getByUserId(String userId);
	
}
/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.fulltl.wemall.modules.wx.dao;

import com.fulltl.wemall.common.persistence.CrudDao;
import com.fulltl.wemall.common.persistence.annotation.MyBatisDao;
import com.fulltl.wemall.modules.wx.entity.WxMessagearticles;

/**
 * 微信消息文章实体类DAO接口
 * @author ldk
 * @version 2017-10-11
 */
@MyBatisDao
public interface WxMessagearticlesDao extends CrudDao<WxMessagearticles> {
	
}
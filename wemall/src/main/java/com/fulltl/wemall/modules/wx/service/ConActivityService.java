/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.fulltl.wemall.modules.wx.service;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fulltl.wemall.common.persistence.Page;
import com.fulltl.wemall.common.service.CrudService;
import com.fulltl.wemall.common.sms.sendmode.ccp.utils.DateUtil;
import com.fulltl.wemall.modules.wx.dao.ConActivityDao;
import com.fulltl.wemall.modules.wx.entity.ConActivity;

/**
 * 活动表Service
 * @author 黄健
 * @version 2017-10-14
 */
@Service
@Transactional(readOnly = true)
public class ConActivityService extends CrudService<ConActivityDao, ConActivity> {

	public ConActivity get(String id) {
		return super.get(id);
	}
	
	public List<ConActivity> findList(ConActivity conActivity) {
		return super.findList(conActivity);
	}
	
	public Page<ConActivity> findPage(Page<ConActivity> page, ConActivity conActivity) {
		return super.findPage(page, conActivity);
	}
	
	@Transactional(readOnly = false)
	public void save(ConActivity conActivity) {
		super.save(conActivity);
	}
	
	@Transactional(readOnly = false)
	public void delete(ConActivity conActivity) {
		super.delete(conActivity);
	}

	/**
	 * 寻找当前活动，若当前没有活动，则寻找最新添加的活动。
	 * @return
	 */
	public ConActivity findCurActivity() {
		List<ConActivity> conActivitys =  dao.findCurActivityByDate(DateUtil.dateToStr(new Date(), "yyyy-MM-dd HH-mm-ss"));
		if(conActivitys.size() > 0) {
			//由当前正在进行的活动
			//返回正在进行活动列表的第一个
			return conActivitys.get(0);
		} else {
			//当前没有活动正在进行
			//返回活动开始时间最迟的活动
			return dao.findLastActivityByFromdate();
		}
	}
	
}
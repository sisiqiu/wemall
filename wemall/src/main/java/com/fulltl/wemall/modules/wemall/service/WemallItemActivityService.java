/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.fulltl.wemall.modules.wemall.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fulltl.wemall.common.persistence.Page;
import com.fulltl.wemall.common.service.CrudService;
import com.fulltl.wemall.modules.wemall.entity.WemallItem;
import com.fulltl.wemall.modules.wemall.entity.WemallItemActivity;
import com.fulltl.wemall.modules.wemall.entity.WemallOrderItem;
import com.mysql.fabric.xmlrpc.base.Array;

import cn.jiguang.common.utils.StringUtils;

import com.fulltl.wemall.modules.wemall.dao.WemallItemActivityDao;

/**
 * 商品活动中间表管理Service
 * @author fulltl
 * @version 2018-01-13
 */
@Service
@Transactional(readOnly = true)

public class WemallItemActivityService extends CrudService<WemallItemActivityDao, WemallItemActivity> {
	@Autowired
	private WemallItemService wemallItemcService;
	
	public WemallItemActivity get(Integer itemId, Integer activityId) {
		WemallItemActivity wemallItemActivity = new WemallItemActivity();
		wemallItemActivity.setItemId(itemId);;
		wemallItemActivity.setActivityId(activityId);;
		return super.get(wemallItemActivity);
	}
	
	public List<WemallItemActivity> findList(WemallItemActivity wemallItemActivity) {
		return super.findList(wemallItemActivity);
	}
	
	public Page<WemallItemActivity> findPage(Page<WemallItemActivity> page, WemallItemActivity wemallItemActivity) {
		return super.findPage(page, wemallItemActivity);
	}
	
	@Transactional(readOnly = false)
	public void save(WemallItemActivity wemallItemActivity) {
		super.save(wemallItemActivity);
	}
	
	@Transactional(readOnly = false)
	public void delete(WemallItemActivity wemallItemActivity) {
		super.delete(wemallItemActivity);
	}

	public List<WemallItem> findItemsByActId(String id) {
		// TODO Auto-generated method stub
		String itemIds = dao.findItemsByActId(id);
		List<WemallItem> actItems = new ArrayList<>();
		if(StringUtils.isNotEmpty(itemIds)){
			String idArr [] = itemIds.split(",");
			if(idArr.length>0){
				for(int i =0;i<idArr.length;i++){
					WemallItem w = new WemallItem();
					w.setId(idArr[i]);
					WemallItem we = wemallItemcService.get(w);
					actItems.add(we);
				}
			}
		}
		return actItems;
		
	}
	
}
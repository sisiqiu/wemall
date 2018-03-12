/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.fulltl.wemall.modules.wemall.service;

import java.util.List;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fulltl.wemall.common.persistence.Page;
import com.fulltl.wemall.common.service.CrudService;
import com.fulltl.wemall.modules.wemall.dao.WemallItemDao;
import com.fulltl.wemall.modules.wemall.entity.WemallItem;
import com.fulltl.wemall.modules.wemall.entity.WemallItemSpec;
import com.google.gson.reflect.TypeToken;

/**
 * 商品管理Service
 * @author ldk
 * @version 2018-01-05
 */
@Service
@Transactional(readOnly = true)
public class WemallItemService extends CrudService<WemallItemDao, WemallItem> {
	@Autowired
	private WemallItemSpecService wemallItemSpecService;
	
	@Autowired
	private WemallItemActivityService wemallItemActivityService;
	
	public WemallItem get(WemallItem entity) {
		WemallItem wemallItem = super.get(entity);
		//获取商品属性列表
		WemallItemSpec query = new WemallItemSpec();
		query.setItemId(entity.getId());
		List<WemallItemSpec> itemSpecList = wemallItemSpecService.findList(query);
		if(wemallItem == null) return null;
		wemallItem.setSpecInfoStr(gson.toJson(itemSpecList));
		return wemallItem;
	}
	
	public WemallItem get(String id) {
		WemallItem wemallItem = super.get(id);
		//获取商品属性列表
		WemallItemSpec query = new WemallItemSpec();
		query.setItemId(id);
		List<WemallItemSpec> itemSpecList = wemallItemSpecService.findList(query);
		wemallItem.setSpecInfoStr(gson.toJson(itemSpecList));
		return wemallItem;
	}
	
	public List<WemallItem> findList(WemallItem wemallItem) {
		return super.findList(wemallItem);
	}
	
	public Page<WemallItem> findPage(Page<WemallItem> page, WemallItem wemallItem) {
		return super.findPage(page, wemallItem);
	}
	
	@Transactional(readOnly = false)
	public void save(WemallItem wemallItem) {
		super.save(wemallItem);
		wemallItem.setSpecInfoStr(StringEscapeUtils.unescapeHtml4(wemallItem.getSpecInfoStr()));
		//判断实体类中的商品属性值列表是否为空，若不为空，则要对应保存商品属性值列表
		if(StringUtils.isNotBlank(wemallItem.getSpecInfoStr())) {
			List<WemallItemSpec> itemSpecList = gson.fromJson(wemallItem.getSpecInfoStr(), new TypeToken<List<WemallItemSpec>>() {}.getType());
			//根据现在的列表数据。执行批量更新，插入，删除
			wemallItemSpecService.updateItemSpecList(itemSpecList, wemallItem.getId());
		}
	}
	
	@Transactional(readOnly = false)
	public void delete(WemallItem wemallItem) {
		super.delete(wemallItem);
	}

}
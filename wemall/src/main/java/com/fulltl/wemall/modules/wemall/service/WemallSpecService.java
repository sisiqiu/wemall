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
import com.fulltl.wemall.modules.wemall.dao.WemallSpecDao;
import com.fulltl.wemall.modules.wemall.entity.WemallItemSpec;
import com.fulltl.wemall.modules.wemall.entity.WemallSpec;
import com.fulltl.wemall.modules.wemall.entity.WemallSpecInfo;
import com.google.gson.reflect.TypeToken;

/**
 * 属性类别管理Service
 * @author ldk
 * @version 2018-01-05
 */
@Service
@Transactional(readOnly = true)
public class WemallSpecService extends CrudService<WemallSpecDao, WemallSpec> {

	@Autowired
	private WemallSpecInfoService wemallSpecInfoService;
	
	public WemallSpec get(WemallSpec entity) {
		WemallSpec wemallSpec = super.get(entity);
		//获取属性类别下的属性值列表
		WemallSpecInfo query = new WemallSpecInfo();
		query.setSpecId(entity.getId());
		List<WemallSpecInfo> itemSpecList = wemallSpecInfoService.findList(query);
		wemallSpec.setSpecInfoStr(gson.toJson(itemSpecList));
		return super.get(entity);
	}
	
	public WemallSpec get(String id) {
		WemallSpec wemallSpec = super.get(id);
		//获取属性类别下的属性值列表
		WemallSpecInfo query = new WemallSpecInfo();
		query.setSpecId(id);
		List<WemallSpecInfo> itemSpecList = wemallSpecInfoService.findList(query);
		wemallSpec.setSpecInfoStr(gson.toJson(itemSpecList));
		return wemallSpec;
	}
	
	public List<WemallSpec> findList(WemallSpec wemallSpec) {
		return super.findList(wemallSpec);
	}
	
	public Page<WemallSpec> findPage(Page<WemallSpec> page, WemallSpec wemallSpec) {
		return super.findPage(page, wemallSpec);
	}
	
	@Transactional(readOnly = false)
	public void save(WemallSpec wemallSpec) {
		super.save(wemallSpec);
		wemallSpec.setSpecInfoStr(StringEscapeUtils.unescapeHtml4(wemallSpec.getSpecInfoStr()));
		//判断实体类中的类别下属性值列表是否为空，若不为空，则要对应保存类别下属性值列表
		if(StringUtils.isNotBlank(wemallSpec.getSpecInfoStr())) {
			List<WemallSpecInfo> specInfoList = gson.fromJson(wemallSpec.getSpecInfoStr(), new TypeToken<List<WemallSpecInfo>>() {}.getType());
			//根据现在的列表数据。执行批量更新，插入，删除
			wemallSpecInfoService.updateSpecInfoList(specInfoList, wemallSpec.getId());
		}
	}
	
	@Transactional(readOnly = false)
	public void delete(WemallSpec wemallSpec) {
		super.delete(wemallSpec);
	}
	
}
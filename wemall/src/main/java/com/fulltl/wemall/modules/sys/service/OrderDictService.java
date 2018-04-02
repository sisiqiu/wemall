/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.fulltl.wemall.modules.sys.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fulltl.wemall.common.service.CrudService;
import com.fulltl.wemall.common.utils.CacheUtils;
import com.fulltl.wemall.modules.sys.dao.OrderDictDao;
import com.fulltl.wemall.modules.sys.entity.OrderDict;
import com.fulltl.wemall.modules.sys.utils.OrderDictUtils;

/**
 * 字典Service
 * @author ThinkGem
 * @version 2014-05-16
 */
@Service
@Transactional(readOnly = true)
public class OrderDictService extends CrudService<OrderDictDao, OrderDict> {
	
	/**
	 * 查询字段类型列表
	 * @return
	 */
	public List<String> findTypeList(){
		return dao.findTypeList(new OrderDict());
	}

	@Transactional(readOnly = false)
	public void save(OrderDict orderDict) {
		super.save(orderDict);
		CacheUtils.remove(OrderDictUtils.CACHE_ORDER_DICT_MAP);
	}

	@Transactional(readOnly = false)
	public void delete(OrderDict orderDict) {
		super.delete(orderDict);
		CacheUtils.remove(OrderDictUtils.CACHE_ORDER_DICT_MAP);
	}

}

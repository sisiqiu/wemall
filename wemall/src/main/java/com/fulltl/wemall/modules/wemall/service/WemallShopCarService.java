/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.fulltl.wemall.modules.wemall.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fulltl.wemall.common.persistence.Page;
import com.fulltl.wemall.common.service.CrudService;
import com.fulltl.wemall.modules.wemall.entity.WemallShopCar;
import com.fulltl.wemall.modules.wemall.dao.WemallShopCarDao;

/**
 * 购物车管理Service
 * @author ldk
 * @version 2018-01-05
 */
@Service
@Transactional(readOnly = true)
public class WemallShopCarService extends CrudService<WemallShopCarDao, WemallShopCar> {

	public WemallShopCar get(String id) {
		return super.get(id);
	}
	
	public List<WemallShopCar> findList(WemallShopCar wemallShopCar) {
		return super.findList(wemallShopCar);
	}
	
	public Page<WemallShopCar> findPage(Page<WemallShopCar> page, WemallShopCar wemallShopCar) {
		return super.findPage(page, wemallShopCar);
	}
	
	@Transactional(readOnly = false)
	public void save(WemallShopCar wemallShopCar) {
		super.save(wemallShopCar);
	}
	
	@Transactional(readOnly = false)
	public void delete(WemallShopCar wemallShopCar) {
		super.delete(wemallShopCar);
	}

	@Transactional(readOnly = false)
	public void delete(List<String> ids) {
		dao.deleteByIds(ids);
	}
	
}
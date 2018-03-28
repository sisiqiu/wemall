/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.fulltl.wemall.modules.wemall.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fulltl.wemall.common.persistence.Page;
import com.fulltl.wemall.common.service.CrudService;
import com.fulltl.wemall.modules.wemall.entity.WemallItem;
import com.fulltl.wemall.modules.wemall.entity.WemallItemSpec;
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
	@Autowired
	private WemallItemService wemallItemService;
	@Autowired
	private WemallItemSpecService wemallItemSpecService;
	
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

	/**
	 * 填充购物车中商品规格信息
	 * @param wemallShopCar
	 */
	public void fillItemSpecs(WemallShopCar wemallShopCar) {
		if(StringUtils.isNotBlank(wemallShopCar.getItemSpecIds())){
			String [] specIds = wemallShopCar.getItemSpecIds().split(",");
			List<WemallItemSpec> itemSpecs = new ArrayList<>();
			if(specIds.length>0){
				WemallItemSpec itemSpec = wemallItemSpecService.get(specIds[0]);
				if(itemSpec!=null){
					itemSpecs.add(itemSpec);
				}
			}
			wemallShopCar.setItemSpecs(itemSpecs);;
		}
	}
	
	/**
	 * 获取购物车价格
	 * @param wemallShopCar
	 * @return
	 */
	public Integer getPriceByWemallShopCar(WemallShopCar wemallShopCar) {
		//根据商品规格价格
		if(wemallShopCar.getItemSpecs() != null && wemallShopCar.getItemSpecs().size() > 0) {
			return wemallShopCar.getItemSpecs().get(0).getPrice()*wemallShopCar.getItemNum();
		} else {
			fillItemSpecs(wemallShopCar);
			if(wemallShopCar.getItemSpecs() != null && wemallShopCar.getItemSpecs().size() > 0) {
				return wemallShopCar.getItemSpecs().get(0).getPrice()*wemallShopCar.getItemNum();
			}
		}
		
		//根据商品价格
		if(wemallShopCar.getItem() != null) {
			return wemallShopCar.getItem().getCurrentPrice()*wemallShopCar.getItemNum();
		} else if(wemallShopCar.getItemId() != null) {
			WemallItem wemallItem = wemallItemService.get(wemallShopCar.getItemId().toString());
			return wemallItem.getCurrentPrice()*wemallShopCar.getItemNum();
		}
		
		return null;
	}

	/**
	 * 根据id列表查询
	 * @param shopCarIdList
	 * @return
	 */
	public List<WemallShopCar> findByIds(List<String> shopCarIdList) {
		return dao.findByIds(shopCarIdList);
	}
}
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
import com.fulltl.wemall.modules.wemall.entity.WemallOrderItem;
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
	@Autowired
	private WemallOrderItemService wemallOrderItemService;
	
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

	/**
	 * 校验库存
	 * @return
	 */
	public boolean checkStorage(List<WemallOrderItem> wemallOrderItems) {
		boolean result = true;
		for(WemallOrderItem w :wemallOrderItems){
			WemallItem item = super.get(w.getItemId());
			int thisStorage = 0;
			if(item!=null){
				 thisStorage = item.getStorage();	
			}
			if(StringUtils.isEmpty(w.getItemsData()) || w.getItemsData().equals("[]")){
				if(thisStorage >= w.getItemNum()){
					result = true;
				}else{
					result = false;
				}
			}else{
				int thisSpecStorage = 0;
				List<WemallItemSpec> itemSpecList = gson.fromJson(w.getItemsData(), new TypeToken<List<WemallItemSpec>>() {}.getType());
				WemallItemSpec entity = new WemallItemSpec();
				if(itemSpecList.size()>0){
					entity = itemSpecList.get(0);
				}
				entity.setItemId(w.getItemId());
				WemallItemSpec itemSpec = wemallItemSpecService.get(entity);
				if(itemSpec!=null){
					thisSpecStorage = Integer.valueOf(itemSpec.getStorage());	
				}
				if(thisSpecStorage >=w.getItemNum()){
					result = true;
				}else{
					result = false;
				}
			}
		}
		return result;
	}
	
	/**
	 * 减库存
	 * @return
	 */
	@Transactional(readOnly = false)
	public boolean reduceStorage(List<WemallOrderItem> wemallOrderItems) {
		return true;
	}
	
	/**
	 * 减库存
	 * @return
	 */
	@Transactional(readOnly = false)
	public boolean reduceStorage(String orderNo) {
		WemallOrderItem query = new WemallOrderItem();
		query.setOrderNo(orderNo);
		List<WemallOrderItem> wemallOrderItemList = wemallOrderItemService.findList(query);
		return this.reduceStorage(wemallOrderItemList);
	}
	
	/**
	 * 释放库存
	 * @return
	 */
	@Transactional(readOnly = false)
	public boolean releaseStorage(List<WemallOrderItem> wemallOrderItems) {
		boolean result = true;
		for(WemallOrderItem w :wemallOrderItems){
			WemallItem item = super.get(w.getItemId());
			if(StringUtils.isEmpty(w.getItemsData())){
				int remainStorage = item.getStorage()-w.getItemNum();
				if(remainStorage>=0){
					result = true;
					item.setStorage(remainStorage);
					super.save(item);
				}else{
					result = false;
				}
			}else{
				List<WemallItemSpec> itemSpecList = gson.fromJson(w.getItemsData(), new TypeToken<List<WemallItemSpec>>() {}.getType());
				WemallItemSpec entity = new WemallItemSpec();
				if(itemSpecList.size()>0){
					entity = itemSpecList.get(0);
				}
				entity.setItemId(w.getItemId());
				WemallItemSpec itemSpec = wemallItemSpecService.get(entity);
				if(itemSpec!=null){
					int remainStorage = Integer.valueOf(itemSpec.getStorage())-w.getItemNum();
					if(remainStorage>=0){
						result = true;
						itemSpec.setStorage(String.valueOf(remainStorage));
						wemallItemSpecService.save(itemSpec);
					}else{
						result = false;
					}
				}
			}
		}
		return result;
	}
	
	/**
	 * 释放库存
	 * @return
	 */
	@Transactional(readOnly = false)
	public boolean releaseStorage(String orderNo) {
		WemallOrderItem query = new WemallOrderItem();
		query.setOrderNo(orderNo);
		List<WemallOrderItem> wemallOrderItemList = wemallOrderItemService.findList(query);
		return this.releaseStorage(wemallOrderItemList);
	}
	
	/**
	 * 增销量
	 * @return
	 */
	@Transactional(readOnly = false)
	public boolean increaseSalesNum(List<WemallOrderItem> wemallOrderItems) {
		boolean result = true;
		for(WemallOrderItem w :wemallOrderItems){
			WemallItem item = super.get(w.getItemId());
			if(StringUtils.isEmpty(w.getItemsData())){
				int remainStorage = item.getStorage()+w.getItemNum();
				result = true;
				item.setStorage(remainStorage);
				super.save(item);
			}else{
				List<WemallItemSpec> itemSpecList = gson.fromJson(w.getItemsData(), new TypeToken<List<WemallItemSpec>>() {}.getType());
				WemallItemSpec entity = new WemallItemSpec();
				if(itemSpecList.size()>0){
					entity = itemSpecList.get(0);
				}
				entity.setItemId(w.getItemId());
				WemallItemSpec itemSpec = wemallItemSpecService.get(entity);
				if(itemSpec!=null){
					int remainStorage = Integer.valueOf(itemSpec.getStorage())+w.getItemNum();
					result = true;
					itemSpec.setStorage(String.valueOf(remainStorage));
					wemallItemSpecService.save(itemSpec);
				}
			}
		}
		return result;
	}
	
	/**
	 * 增销量
	 * @return
	 */
	@Transactional(readOnly = false)
	public boolean increaseSalesNum(String orderNo) {
		WemallOrderItem query = new WemallOrderItem();
		query.setOrderNo(orderNo);
		List<WemallOrderItem> wemallOrderItemList = wemallOrderItemService.findList(query);
		return this.increaseSalesNum(wemallOrderItemList);
	}

}
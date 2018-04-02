/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.fulltl.wemall.modules.wemall.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fulltl.wemall.common.persistence.Page;
import com.fulltl.wemall.common.service.CrudService;
import com.fulltl.wemall.modules.wemall.dao.WemallItemActivityDao;
import com.fulltl.wemall.modules.wemall.dao.WemallItemDao;
import com.fulltl.wemall.modules.wemall.entity.WemallItem;
import com.fulltl.wemall.modules.wemall.entity.WemallItemActivity;
import com.fulltl.wemall.modules.wemall.entity.WemallOrder;
import com.fulltl.wemall.modules.wemall.entity.WemallOrderItem;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import cn.jiguang.common.utils.StringUtils;

/**
 * 商品活动中间表管理Service
 * @author fulltl
 * @version 2018-01-13
 */
@Service
@Transactional(readOnly = true)

public class WemallItemActivityService extends CrudService<WemallItemActivityDao, WemallItemActivity> {
	@Autowired
	private WemallItemDao wemallItemDao;
	
	public WemallItemActivity get(Integer itemId, Integer activityId) {
		WemallItemActivity wemallItemActivity = new WemallItemActivity();
		wemallItemActivity.setItemId(itemId);;
		wemallItemActivity.setActivityId(activityId);
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

	/**
	 * 根据活动id和活动类型获取商品列表
	 * @param id
	 * @param activityType
	 * @return
	 */
	public List<WemallItem> findItemsByActId(String id, int activityType) {
		// TODO Auto-generated method stub
		String itemIds = dao.findItemsByActId(id,activityType);
		List<WemallItem> actItems = new ArrayList<>();
		if(StringUtils.isNotEmpty(itemIds)){
			String idArr [] = itemIds.split(",");
			if(idArr.length>0){
				/*for(int i =0;i<idArr.length;i++){
					WemallItem w = new WemallItem();
					w.setId(idArr[i]);
					WemallItem we = wemallItemDao.get(w);
					actItems.add(we);
				}*/
				WemallItem query = new WemallItem();
				query.setIds(Arrays.asList(idArr));
				actItems = wemallItemDao.findList(query);
			}
		}
		return actItems;
		
	}

	/**
	 * 根据商品id列表获取未过期商品活动列表
	 * @param itemIds
	 * @return
	 */
	public List<WemallItemActivity> findListByItems(List<String> itemIds) {
		WemallItemActivity query = new WemallItemActivity();
		query.setItemIds(itemIds);
		return super.findList(query);
	}

	/**
	 * 校验该活动列表，是否都参与了传入的活动
	 * @param wemallOrderItemList
	 * @param activityId
	 * @param activityType
	 * @return
	 */
	public WemallItemActivity checkActivityByIdAndType(List<WemallOrderItem> wemallOrderItemList, String activityId,
			String activityType) {
		WemallItemActivity result = null;
		List<String> itemIds = Lists.newArrayList();
		for(WemallOrderItem wemallOrderItem : wemallOrderItemList) {
			itemIds.add(wemallOrderItem.getItemId());
		}
		List<WemallItemActivity> findListByItems = this.findListByItems(itemIds);
		Map<Integer, Boolean> itemResultMap = Maps.newHashMap(); 
		
		for(WemallItemActivity itemActivity : findListByItems) {
			Integer itemId = itemActivity.getItemId();
			if(itemResultMap.get(itemId) != null && itemResultMap.get(itemId)) continue;
			
			if(itemActivity.getActivityId().equals(activityId) && 
					itemActivity.getActivityType().equals(activityType)) {
				itemResultMap.put(itemId, true);
				result = itemActivity;
			} else {
				itemResultMap.put(itemId, false);
			}
		}

		for(Integer key : itemResultMap.keySet()) {
			if(!itemResultMap.get(key)) return null;
		}
		
		return result;
	}

	/**
	 * 根据传入的价格，根据活动打折后，返回打折后的价格。
	 * @param orderPrice
	 * @param wemallItemActivity
	 */
	public Integer getPriceByOrderPrice(Integer orderPrice, WemallItemActivity wemallItemActivity) {
		return orderPrice;
	}
	
}
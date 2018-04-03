/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.fulltl.wemall.modules.wemall.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fulltl.wemall.common.persistence.DataEntity;
import com.fulltl.wemall.common.persistence.Page;
import com.fulltl.wemall.common.service.CrudService;
import com.fulltl.wemall.common.utils.MathUtil;
import com.fulltl.wemall.modules.wemall.dao.WemallItemActivityDao;
import com.fulltl.wemall.modules.wemall.dao.WemallItemDao;
import com.fulltl.wemall.modules.wemall.entity.WemallCashbackDiscount;
import com.fulltl.wemall.modules.wemall.entity.WemallFullDiscount;
import com.fulltl.wemall.modules.wemall.entity.WemallItem;
import com.fulltl.wemall.modules.wemall.entity.WemallItemActivity;
import com.fulltl.wemall.modules.wemall.entity.WemallItemActivity.ActivityTypeEnum;
import com.fulltl.wemall.modules.wemall.entity.WemallOrderItem;
import com.fulltl.wemall.modules.wemall.entity.WemallTeamDiscount;
import com.fulltl.wemall.modules.wemall.entity.WemallTimeDiscount;
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
	@Autowired
	private WemallCashbackDiscountService wemallCashbackDiscountService;
	@Autowired
	private WemallFullDiscountService wemallFullDiscountService;
	@Autowired
	private WemallTimeDiscountService wemallTimeDiscountService;
	@Autowired
	private WemallTeamDiscountService wemallTeamDiscountService;
	
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
	 * @param activityId
	 * @param activityType
	 * @return
	 */
	public List<WemallItem> findItemsByActId(String activityId, ActivityTypeEnum activityTypeEnum) {
		if(activityTypeEnum == null) return null;
		String itemIds = dao.findItemsByActId(activityId, activityTypeEnum.getValue());
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
	 * 根据活动类别，获取对应类别未过期的活动列表
	 * @param activityTypeEnum
	 * @return
	 */
	public List<DataEntity> findListByActivityType(ActivityTypeEnum activityTypeEnum) {
		List<DataEntity> list = Lists.newArrayList();
		switch(activityTypeEnum) {
		case CashbackDiscount:
			list.addAll(wemallCashbackDiscountService.findListNotTimeout());
			return list;
		case FullDiscount:
			list.addAll(wemallFullDiscountService.findListNotTimeout());
			return list;
		case TimeDiscount:
			list.addAll(wemallTimeDiscountService.findListNotTimeout());
			return list;
		case TeamDiscount:
			list.addAll(wemallTeamDiscountService.findListNotTimeout());
			return list;
		}
		return null;
	}

	/**
	 * 根据商品id列表获取当前可参与的未过期活动列表（所有商品都参与该活动）(因活动对象不同，现保存在WemallItemActivity对象中)
	 * @param itemIds
	 * @return
	 */
	public List<WemallItemActivity> findListByItems(List<String> itemIds) {
		List<WemallItemActivity> result = null;
		List<DataEntity> cashbackDiscountList = this.findListByActivityType(ActivityTypeEnum.CashbackDiscount);
		List<DataEntity> fullDiscountList = this.findListByActivityType(ActivityTypeEnum.FullDiscount);
		List<DataEntity> timeDiscountList = this.findListByActivityType(ActivityTypeEnum.TimeDiscount);
		List<DataEntity> teamDiscountList = this.findListByActivityType(ActivityTypeEnum.TeamDiscount);
		
		WemallItemActivity query = new WemallItemActivity();
		query.setItemIds(itemIds);
		//获取所有商品id--活动id中间对象。
		List<WemallItemActivity> itemActivityList = super.findList(query);
		//定义商品id--商品活动中间对象的map
		Map<Integer, List<WemallItemActivity>> itemResultMap = Maps.newHashMap(); 
		for(WemallItemActivity entity : itemActivityList) {
			List<WemallItemActivity> list = itemResultMap.get(entity.getItemId());
			if(list != null) {
				list.add(entity);
			} else {
				list = Lists.newArrayList();
				list.add(entity);
			}
			itemResultMap.put(entity.getItemId(), list);
		}
		for(Integer key : itemResultMap.keySet()) {
			List<WemallItemActivity> list = itemResultMap.get(key);
			if(result == null) {
				//第一个，直接赋值
				result = list;
			} else {
				//求活动的交集(result中的活动和list中的活动)
				Iterator<WemallItemActivity> resultIterator = result.iterator();
				while(resultIterator.hasNext()) {  
					WemallItemActivity resultActivity = resultIterator.next();  
					boolean resultActivityInList = false;
					for(WemallItemActivity listActivity : list) {
						if(listActivity.getActivityId().equals(resultActivity.getActivityId()) &&
								listActivity.getActivityType().equals(resultActivity.getActivityType())) {
							resultActivityInList = true;
						}
					}
					if(!resultActivityInList) {
						//该活动不在list中的活动列表里，执行删除
						resultIterator.remove();
					}
				}
			}
		}
		
		if(result == null) return Lists.newArrayList();
		
		//根据几类活动列表，将活动对象赋值进商品活动对象中
		for(WemallItemActivity entity : result) {
			switch(ActivityTypeEnum.getEnumByValue(entity.getActivityType())) {
			case CashbackDiscount:
				for(DataEntity dataEntity : cashbackDiscountList) {
					if(entity.getActivityId().toString().equals(dataEntity.getId())) {
						entity.setActivity(dataEntity);
					}
				}
				break;
			case FullDiscount:
				for(DataEntity dataEntity : fullDiscountList) {
					if(entity.getActivityId().toString().equals(dataEntity.getId())) {
						entity.setActivity(dataEntity);
					}
				}
				break;
			case TeamDiscount:
				for(DataEntity dataEntity : teamDiscountList) {
					if(entity.getActivityId().toString().equals(dataEntity.getId())) {
						entity.setActivity(dataEntity);
					}
				}
				break;
			case TimeDiscount:
				for(DataEntity dataEntity : timeDiscountList) {
					if(entity.getActivityId().toString().equals(dataEntity.getId())) {
						entity.setActivity(dataEntity);
					}
				}
				break;
			}
		}
		
		//将不包含活动对象的商品活动对象从列表中去除
		Iterator<WemallItemActivity> resultIterator = result.iterator();
		while(resultIterator.hasNext()) {  
			WemallItemActivity resultActivity = resultIterator.next();
			if(resultActivity.getActivity() == null) {
				resultIterator.remove();
			}
		}
		
		return result;
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
		
		for(WemallItemActivity entity : findListByItems) {
			if(entity.getActivityId().toString().equals(activityId) &&
					entity.getActivityType().toString().equals(activityType)) {
				return entity;
			}
		}
		return null;
	}

	/**
	 * 根据传入的价格，根据活动打折后，返回打折后的价格。
	 * @param orderPrice
	 * @param wemallItemActivity
	 */
	public Integer getPriceByOrderPrice(Integer orderPrice, WemallItemActivity wemallItemActivity) throws Exception {
		Object activity = wemallItemActivity.getActivity();
		switch(ActivityTypeEnum.getEnumByValue(wemallItemActivity.getActivityType())) {
		case CashbackDiscount:
			WemallCashbackDiscount wemallCashbackDiscount = (WemallCashbackDiscount)activity;
			
			break;
		case FullDiscount:
			//满减送
			WemallFullDiscount wemallFullDiscount = (WemallFullDiscount)activity;
			Integer newFullDiscountPrice = MathUtil.calculateByOrderPrice(orderPrice, wemallFullDiscount.getDiscountCond());
			if(newFullDiscountPrice != null) return newFullDiscountPrice;
			break;
		case TeamDiscount:
			WemallTeamDiscount wemallTeamDiscount = (WemallTeamDiscount)activity;
			
			break;
		case TimeDiscount:
			//限时打折
			WemallTimeDiscount wemallTimeDiscount = (WemallTimeDiscount)activity;
			if(wemallTimeDiscount.getType().equals(0)) {
				//百分比
				Integer newTimeDiscountPrice = new BigDecimal(orderPrice)
						.multiply(new BigDecimal(wemallTimeDiscount.getDiscount()))
						.setScale(0, RoundingMode.HALF_UP)
						.intValue();
				if(newTimeDiscountPrice != null) return newTimeDiscountPrice;
			} else if(wemallTimeDiscount.getType().equals(1)) {
				//固定值
				Integer newTimeDiscountPrice = new BigDecimal(orderPrice)
					.subtract(new BigDecimal(wemallTimeDiscount.getDiscount())
								.multiply(new BigDecimal(100))
							)
					.setScale(0, RoundingMode.HALF_UP)
					.intValue();
				if(newTimeDiscountPrice != null) return newTimeDiscountPrice;
			}
			break;
		}
		
		return orderPrice;
	}
	
}
/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.fulltl.wemall.modules.wemall.entity;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.fulltl.wemall.common.persistence.DataEntity;

/**
 * 商品活动中间表管理Entity
 * @author fulltl
 * @version 2018-01-13
 */
public class WemallItemActivity extends DataEntity<WemallItemActivity> {
	
	private static final long serialVersionUID = 1L;
	private Integer itemId;		// 商品id
	private Integer activityId;		// 活动id
	private Integer activityType;		// 对应活动类型 1-限时返现 2-满减送 3-限时折扣 4-限时团购'
	
	private Object activity;
	
	private List<String> itemIds;
	
	private Integer joinPrice;
	
	public enum ActivityTypeEnum {
		/**
		 * 限时返现
		 */
		CashbackDiscount(1),
		/**
		 * 满减送
		 */
		FullDiscount(2),
		/**
		 * 限时打折
		 */
		TimeDiscount(3),
		/**
		 * 限时拼团
		 */
		TeamDiscount(4);
		
		private int value;
		
		private ActivityTypeEnum(int value) {
			this.value = value;
		}

		public int getValue() {
			return value;
		}
		
		public static ActivityTypeEnum getEnumByValue(int value) {
			switch(value) {
			case 1:
				return CashbackDiscount;
			case 2:
				return FullDiscount;
			case 3:
				return TimeDiscount;
			case 4:
				return TeamDiscount;
			}
			return null;
		}
	}
	
	public WemallItemActivity() {
		super();
	}

	public WemallItemActivity(String id){
		super(id);
	}

	@NotNull(message="商品id不能为空")
	public Integer getItemId() {
		return itemId;
	}

	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}
	
	@NotNull(message="活动id不能为空")
	public Integer getActivityId() {
		return activityId;
	}

	public void setActivityId(Integer activityId) {
		this.activityId = activityId;
	}
	
	@NotNull(message="活动类型id不能为空")
	public Integer getActivityType() {
		return activityType;
	}

	public void setActivityType(Integer activityType) {
		this.activityType = activityType;
	}
	
	public Object getActivity() {
		return activity;
	}

	public void setActivity(Object activity) {
		this.activity = activity;
	}

	public List<String> getItemIds() {
		return itemIds;
	}

	public void setItemIds(List<String> itemIds) {
		this.itemIds = itemIds;
	}

	public Integer getJoinPrice() {
		return joinPrice;
	}

	public void setJoinPrice(Integer joinPrice) {
		this.joinPrice = joinPrice;
	}
	
}
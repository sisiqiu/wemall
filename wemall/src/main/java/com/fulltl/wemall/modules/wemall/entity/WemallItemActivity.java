/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.fulltl.wemall.modules.wemall.entity;

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
	
}
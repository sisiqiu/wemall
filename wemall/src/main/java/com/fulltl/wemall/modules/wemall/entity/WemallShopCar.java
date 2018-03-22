/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.fulltl.wemall.modules.wemall.entity;

import com.fulltl.wemall.modules.sys.entity.User;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.fulltl.wemall.common.persistence.DataEntity;

/**
 * 购物车管理Entity
 * @author ldk
 * @version 2018-01-05
 */
public class WemallShopCar extends DataEntity<WemallShopCar> {
	
	private static final long serialVersionUID = 1L;
	private User user;		// 用户id
	private Integer itemId;		// 商品id
	private Integer itemNum;		// 商品数量
	private String itemSpecIds;		// 商品属性id列表
	private Integer status;		// 状态（0--禁用；1--可用）
	
	private WemallItem item;		// 商品
	
	private List<WemallItemSpec> itemSpecs;
	
	public WemallShopCar() {
		super();
	}

	public WemallShopCar(String id){
		super(id);
	}

	@NotNull(message="用户id不能为空")
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	@NotNull(message="商品id不能为空")
	public Integer getItemId() {
		return itemId;
	}

	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}
	
	@NotNull(message="商品数量不能为空")
	public Integer getItemNum() {
		return itemNum;
	}

	public void setItemNum(Integer itemNum) {
		this.itemNum = itemNum;
	}
	
	@NotNull(message="状态（0--禁用；1--可用）不能为空")
	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public WemallItem getItem() {
		return item;
	}

	public void setItem(WemallItem item) {
		this.item = item;
	}

	public String getItemSpecIds() {
		return itemSpecIds;
	}

	public void setItemSpecIds(String itemSpecIds) {
		this.itemSpecIds = itemSpecIds;
	}

	public List<WemallItemSpec> getItemSpecs() {
		return itemSpecs;
	}

	public void setItemSpecs(List<WemallItemSpec> itemSpecs) {
		this.itemSpecs = itemSpecs;
	}
	
	

}
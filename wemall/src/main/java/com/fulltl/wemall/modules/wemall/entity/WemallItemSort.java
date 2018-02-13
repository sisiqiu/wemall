/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.fulltl.wemall.modules.wemall.entity;

import java.util.Map;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fulltl.wemall.common.persistence.TreeEntity;
import com.google.common.collect.Maps;

/**
 * 商品分类管理Entity
 * @author ldk
 * @version 2018-01-10
 */
public class WemallItemSort extends TreeEntity<WemallItemSort> {
	
	private static final long serialVersionUID = 1L;
	private String name;		// 商品类别名
	private String photo;		// 缩略图
	private WemallItemSort parent;		// 父级编号
	private String parentIds;		// 所有父级编号
	private Integer sort;		// 排序
	
	public WemallItemSort() {
		super();
	}

	public WemallItemSort(String id){
		super(id);
	}

	@Length(min=1, max=100, message="商品类别名长度必须介于 1 和 100 之间")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@Length(min=0, max=200, message="缩略图长度必须介于 0 和 200 之间")
	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}
	
	@JsonBackReference
	@NotNull(message="父级编号不能为空")
	public WemallItemSort getParent() {
		return parent;
	}

	public void setParent(WemallItemSort parent) {
		this.parent = parent;
	}
	
	@Length(min=1, max=1000, message="所有父级编号长度必须介于 1 和 1000 之间")
	public String getParentIds() {
		return parentIds;
	}

	public void setParentIds(String parentIds) {
		this.parentIds = parentIds;
	}
	
	@NotNull(message="排序不能为空")
	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}
	
	public String getParentId() {
		return parent != null && parent.getId() != null ? parent.getId() : "0";
	}

	/**
	 * 获取列表接口展示使用的小型数据map
	 * @return
	 */
	public Map<String, Object> getSmallEntityMap() {
		Map<String, Object> map = Maps.newHashMap();
		map.put("id", this.getId());
		map.put("name", this.getName());
		map.put("photo", this.getPhoto());
		map.put("sort", this.getSort());
		map.put("parentId", this.getParent() == null ? null : this.getParent().getId());
		super.formatEmptyString(map);
		return map;
	}
}
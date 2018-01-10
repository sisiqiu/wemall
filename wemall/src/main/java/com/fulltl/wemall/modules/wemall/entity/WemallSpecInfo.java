/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.fulltl.wemall.modules.wemall.entity;

import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

import com.fulltl.wemall.common.persistence.DataEntity;

/**
 * 属性值管理Entity
 * @author ldk
 * @version 2018-01-05
 */
public class WemallSpecInfo extends DataEntity<WemallSpecInfo> {
	
	private static final long serialVersionUID = 1L;
	private Integer specId;		// 属性类别id
	private String name;		// 属性名称
	private Integer sort;		// 排序
	
	private String specName;		// 属性类别名称

	public WemallSpecInfo() {
		super();
	}

	public WemallSpecInfo(String id){
		super(id);
	}

	@NotNull(message="属性类别id不能为空")
	public Integer getSpecId() {
		return specId;
	}

	public void setSpecId(Integer specId) {
		this.specId = specId;
	}
	
	@Length(min=1, max=40, message="属性名称长度必须介于 1 和 40 之间")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@NotNull(message="排序不能为空")
	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public String getSpecName() {
		return specName;
	}

	public void setSpecName(String specName) {
		this.specName = specName;
	}
	
}
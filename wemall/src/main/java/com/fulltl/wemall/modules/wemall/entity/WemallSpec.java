/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.fulltl.wemall.modules.wemall.entity;

import org.hibernate.validator.constraints.Length;
import javax.validation.constraints.NotNull;

import com.fulltl.wemall.common.persistence.DataEntity;

/**
 * 属性类别管理Entity
 * @author ldk
 * @version 2018-01-05
 */
public class WemallSpec extends DataEntity<WemallSpec> {
	
	private static final long serialVersionUID = 1L;
	private String name;		// 属性类别名
	private Integer sort;		// 排序
	
	private String specInfoStr; //规格值列表json字符串
	
	public WemallSpec() {
		super();
	}

	public WemallSpec(String id){
		super(id);
	}

	@Length(min=1, max=100, message="属性类别名长度必须介于 1 和 100 之间")
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

	public String getSpecInfoStr() {
		return specInfoStr;
	}

	public void setSpecInfoStr(String specInfoStr) {
		this.specInfoStr = specInfoStr;
	}
	
}
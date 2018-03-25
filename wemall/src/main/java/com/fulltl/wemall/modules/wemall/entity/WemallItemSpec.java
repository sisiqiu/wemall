/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.fulltl.wemall.modules.wemall.entity;

import org.hibernate.validator.constraints.Length;

import com.drew.lang.annotations.NotNull;
import com.fulltl.wemall.common.persistence.DataEntity;
import com.google.common.base.Objects;

/**
 * 商品-属性管理Entity
 * @author ldk
 * @version 2018-02-01
 */
public class WemallItemSpec extends DataEntity<WemallItemSpec> {
	
	private static final long serialVersionUID = 1L;
	private String itemId;		// 商品id
	private String specName;		// 属性类别名
	private String specInfoName;		// 属性值名称
	private String sort;		// 排序
	private Integer price;		// 价格
	private String teamPrice;		// 拼团价
	private String storage;		// 库存量
	
	public WemallItemSpec() {
		super();
	}

	public WemallItemSpec(String id){
		super(id);
	}

	@Length(min=1, max=11, message="商品id长度必须介于 1 和 11 之间")
	public String getItemId() {
		return itemId;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}
	
	@Length(min=1, max=100, message="属性类别名长度必须介于 1 和 100 之间")
	public String getSpecName() {
		return specName;
	}

	public void setSpecName(String specName) {
		this.specName = specName;
	}
	
	@Length(min=1, max=40, message="属性值名称长度必须介于 1 和 40 之间")
	public String getSpecInfoName() {
		return specInfoName;
	}

	public void setSpecInfoName(String specInfoName) {
		this.specInfoName = specInfoName;
	}
	
	@Length(min=1, max=11, message="排序长度必须介于 1 和 11 之间")
	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}
	
	@NotNull
	public Integer getPrice() {
		return price;
	}

	public void setPrice(Integer price) {
		this.price = price;
	}
	
	@Length(min=1, max=11, message="拼团价长度必须介于 1 和 11 之间")
	public String getTeamPrice() {
		return teamPrice;
	}

	public void setTeamPrice(String teamPrice) {
		this.teamPrice = teamPrice;
	}
	
	@Length(min=1, max=11, message="库存量长度必须介于 1 和 11 之间")
	public String getStorage() {
		return storage;
	}

	public void setStorage(String storage) {
		this.storage = storage;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (null == obj) {
	        return false;
	    }
	    if (this == obj) {
	        return true;
	    }
	    if (!getClass().equals(obj.getClass())) {
	        return false;
	    }
	    WemallItemSpec itemSpec = (WemallItemSpec)obj;
	    if(Objects.equal(this.id, itemSpec.getId()) &&
	    		Objects.equal(this.itemId, itemSpec.getItemId()) &&
	    		Objects.equal(this.specName, itemSpec.getSpecName()) &&
	    		Objects.equal(this.specInfoName, itemSpec.getSpecInfoName()) &&
	    		Objects.equal(this.sort, itemSpec.getSort()) &&
	    		Objects.equal(this.price, itemSpec.getPrice()) &&
	    		Objects.equal(this.teamPrice, itemSpec.getTeamPrice()) &&
	    		Objects.equal(this.storage, itemSpec.getStorage())) {
	    	return true;
	    }
	    return false;
	}
}
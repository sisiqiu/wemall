/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.fulltl.wemall.modules.wemall.entity;

import org.hibernate.validator.constraints.Length;
import javax.validation.constraints.NotNull;

import com.fulltl.wemall.common.persistence.DataEntity;

/**
 * 商品-属性管理Entity
 * @author ldk
 * @version 2018-01-05
 */
public class WemallItemSpec extends DataEntity<WemallItemSpec> {
	
	private static final long serialVersionUID = 1L;
	private Integer itemId;		// 商品id
	private Integer specId;		// 属性类别id
	private Integer specInfoId;		// 属性值id
	private Integer price;		// 价格
	private Integer teamPrice;		// 拼团价
	private Integer storage;		// 库存量
	private Integer beginPrice;		// 开始 价格
	private Integer endPrice;		// 结束 价格
	private Integer beginTeamPrice;		// 开始 拼团价
	private Integer endTeamPrice;		// 结束 拼团价
	private Integer beginStorage;		// 开始 库存量
	private Integer endStorage;		// 结束 库存量
	
	public WemallItemSpec() {
		super();
	}

	public WemallItemSpec(String id){
		super(id);
	}

	@NotNull(message="商品id不能为空")
	public Integer getItemId() {
		return itemId;
	}

	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}
	
	@NotNull(message="属性类别id不能为空")
	public Integer getSpecId() {
		return specId;
	}

	public void setSpecId(Integer specId) {
		this.specId = specId;
	}
	
	@NotNull(message="属性值id不能为空")
	public Integer getSpecInfoId() {
		return specInfoId;
	}

	public void setSpecInfoId(Integer specInfoId) {
		this.specInfoId = specInfoId;
	}
	
	@NotNull(message="价格不能为空")
	public Integer getPrice() {
		return price;
	}

	public void setPrice(Integer price) {
		this.price = price;
	}
	
	@NotNull(message="拼团价不能为空")
	public Integer getTeamPrice() {
		return teamPrice;
	}

	public void setTeamPrice(Integer teamPrice) {
		this.teamPrice = teamPrice;
	}
	
	@NotNull(message="库存量不能为空")
	public Integer getStorage() {
		return storage;
	}

	public void setStorage(Integer storage) {
		this.storage = storage;
	}
	
	public Integer getBeginPrice() {
		return beginPrice;
	}

	public void setBeginPrice(Integer beginPrice) {
		this.beginPrice = beginPrice;
	}
	
	public Integer getEndPrice() {
		return endPrice;
	}

	public void setEndPrice(Integer endPrice) {
		this.endPrice = endPrice;
	}
		
	public Integer getBeginTeamPrice() {
		return beginTeamPrice;
	}

	public void setBeginTeamPrice(Integer beginTeamPrice) {
		this.beginTeamPrice = beginTeamPrice;
	}
	
	public Integer getEndTeamPrice() {
		return endTeamPrice;
	}

	public void setEndTeamPrice(Integer endTeamPrice) {
		this.endTeamPrice = endTeamPrice;
	}
		
	public Integer getBeginStorage() {
		return beginStorage;
	}

	public void setBeginStorage(Integer beginStorage) {
		this.beginStorage = beginStorage;
	}
	
	public Integer getEndStorage() {
		return endStorage;
	}

	public void setEndStorage(Integer endStorage) {
		this.endStorage = endStorage;
	}
		
	@Override
	public boolean getIsNewRecord() {
		return isNewRecord;
	}
}
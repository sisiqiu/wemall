/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.fulltl.wemall.modules.wemall.entity;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.fulltl.wemall.common.persistence.DataEntity;

/**
 * 充值设定管理Entity
 * @author ldk
 * @version 2018-04-18
 */
public class WemallRecharge extends DataEntity<WemallRecharge> {
	
	private static final long serialVersionUID = 1L;
	private String name;		// 名称
	private Integer originalPrice;		// 充值原价
	private Integer currentPrice;		// 充值现价
	private String needaddress;		// 是否需要填写地址(0--不需要，1--需要)
	private String desc;		// 描述
	
	public WemallRecharge() {
		super();
	}

	public WemallRecharge(String id){
		super(id);
	}

	@Length(min=1, max=50, message="名称长度必须介于 1 和 50 之间")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@NotNull(message="充值原价不能为空")
	public Integer getOriginalPrice() {
		return originalPrice;
	}

	public void setOriginalPrice(Integer originalPrice) {
		this.originalPrice = originalPrice;
	}
	
	public Integer getCurrentPrice() {
		return currentPrice;
	}

	public void setCurrentPrice(Integer currentPrice) {
		this.currentPrice = currentPrice;
	}
	
	@Length(min=1, max=1, message="是否需要填写地址(0--不需要，1--需要)长度必须介于 1 和 1 之间")
	public String getNeedaddress() {
		return needaddress;
	}

	public void setNeedaddress(String needaddress) {
		this.needaddress = needaddress;
	}
	
	@Length(min=0, max=200, message="描述长度必须介于 0 和 200 之间")
	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}
	
}
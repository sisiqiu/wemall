/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.fulltl.wemall.modules.wemall.entity;

import org.hibernate.validator.constraints.Length;

import com.fulltl.wemall.common.persistence.DataEntity;

/**
 * 物流明细管理Entity
 * @author ldk
 * @version 2018-01-05
 */
public class WemallFreightInfo extends DataEntity<WemallFreightInfo> {
	
	private static final long serialVersionUID = 1L;
	private String freightNo;		// 物流单号
	private String name;		// 物流名称
	private String curPlace;		// 包裹所在地
	private String sendPeople;		// 包裹派送人
	
	public WemallFreightInfo() {
		super();
	}

	public WemallFreightInfo(String id){
		super(id);
	}

	@Length(min=1, max=64, message="物流单号长度必须介于 1 和 64 之间")
	public String getFreightNo() {
		return freightNo;
	}

	public void setFreightNo(String freightNo) {
		this.freightNo = freightNo;
	}
	
	@Length(min=1, max=50, message="物流名称长度必须介于 1 和 50 之间")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@Length(min=1, max=100, message="包裹所在地长度必须介于 1 和 100 之间")
	public String getCurPlace() {
		return curPlace;
	}

	public void setCurPlace(String curPlace) {
		this.curPlace = curPlace;
	}
	
	@Length(min=1, max=40, message="包裹派送人长度必须介于 1 和 40 之间")
	public String getSendPeople() {
		return sendPeople;
	}

	public void setSendPeople(String sendPeople) {
		this.sendPeople = sendPeople;
	}
	
}
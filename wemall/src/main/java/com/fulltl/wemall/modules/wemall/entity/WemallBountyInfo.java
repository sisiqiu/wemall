/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.fulltl.wemall.modules.wemall.entity;

import com.fulltl.wemall.modules.sys.entity.User;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

import com.fulltl.wemall.common.persistence.DataEntity;

/**
 * 奖励金管理Entity
 * @author ldk
 * @version 2018-01-05
 */
public class WemallBountyInfo extends DataEntity<WemallBountyInfo> {
	
	private static final long serialVersionUID = 1L;
	private User user;		// 用户id
	private String fromType;		// 获取途径
	private String orderNo;		// 订单号
	private Integer type;		// 类型（0--支出；1--收入）
	private Integer price;		// 金额
	private Integer beginPrice;		// 开始 金额
	private Integer endPrice;		// 结束 金额
	
	public WemallBountyInfo() {
		super();
	}

	public WemallBountyInfo(String id){
		super(id);
	}

	@NotNull(message="用户id不能为空")
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	@Length(min=1, max=10, message="获取途径长度必须介于 1 和 10 之间")
	public String getFromType() {
		return fromType;
	}

	public void setFromType(String fromType) {
		this.fromType = fromType;
	}
	
	@Length(min=1, max=64, message="订单号长度必须介于 1 和 64 之间")
	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	
	@NotNull(message="类型（0--支出；1--收入）不能为空")
	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}
	
	@NotNull(message="金额不能为空")
	public Integer getPrice() {
		return price;
	}

	public void setPrice(Integer price) {
		this.price = price;
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
		
}
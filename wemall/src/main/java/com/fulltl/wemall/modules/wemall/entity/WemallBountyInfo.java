/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.fulltl.wemall.modules.wemall.entity;

import com.fulltl.wemall.modules.sys.entity.User;
import com.fulltl.wemall.modules.sys.utils.DictUtils;
import com.google.common.collect.Maps;

import java.util.Date;
import java.util.Map;

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
	private String type;		// 类型（0--支出；1--收入）
	private Integer price;		// 金额
	private Integer beginPrice;		// 开始 金额
	private Integer endPrice;		// 结束 金额
	private Date beginCreateDate;		// 开始 创建时间
	private Date endCreateDate;		// 结束 创建时间
	
	public enum BountyFromType {
		/**
		 * 充值
		 */
		recharge("1"),
		/**
		 * 购买商品
		 */
		buyItems("2"),
		/**
		 * 奖励金撤回
		 */
		rollback("3")
		;
		
		private String value;
		private BountyFromType(String value) {
			this.value = value;
		}
		
		public String getValue() {
			return value;
		}
		public void setValue(String value) {
			this.value = value;
		}
	}
	
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
	
	@Length(min=0, max=64, message="订单号长度必须介于 0 和 64 之间")
	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	
	@Length(min=1, max=1, message="类型（0--支出；1--收入）长度必须介于 1 和 1 之间")
	public String getType() {
		return type;
	}

	public void setType(String type) {
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
	
	public Date getBeginCreateDate() {
		return beginCreateDate;
	}

	public void setBeginCreateDate(Date beginCreateDate) {
		this.beginCreateDate = beginCreateDate;
	}
	
	public Date getEndCreateDate() {
		return endCreateDate;
	}

	public void setEndCreateDate(Date endCreateDate) {
		this.endCreateDate = endCreateDate;
	}
	
	/**
	 * 获取列表接口展示使用的小型数据map
	 * @return
	 */
	public Map<String, Object> getSmallEntityMap() {
		Map<String, Object> map = Maps.newHashMap();
		map.put("id", this.getId());
		map.put("userId", this.getUser().getId());
		map.put("orderNo", this.getOrderNo());
		map.put("fromType", DictUtils.getDictLabel(this.getFromType(), "bounty_fromType", ""));
		map.put("type", this.getType());
		map.put("price", this.getPrice());
		map.put("createDate", this.getCreateDate());
		super.formatEmptyString(map);
		return map;
	}
	
}
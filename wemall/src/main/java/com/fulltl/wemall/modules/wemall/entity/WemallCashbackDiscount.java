/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.fulltl.wemall.modules.wemall.entity;

import org.hibernate.validator.constraints.Length;
import javax.validation.constraints.NotNull;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.fulltl.wemall.common.persistence.DataEntity;

/**
 * 限时返现活动管理Entity
 * @author ldk
 * @version 2018-01-05
 */
public class WemallCashbackDiscount extends DataEntity<WemallCashbackDiscount> {
	
	private static final long serialVersionUID = 1L;
	private String name;		// 限时返现活动名称
	private Integer sort;		// 排序
	private Date startDate;		// 开始时间
	private Date endDate;		// 结束时间
	private String label;		// 标签名
	private Integer type;		// 类型（0--随机返现，1--固定返现）
	private String cashback;		// 返现比例
	private Integer limit;		// 返现订单数
	private Date beginStartDate;		// 开始 开始时间
	private Date endStartDate;		// 结束 开始时间
	private Date beginEndDate;		// 开始 结束时间
	private Date endEndDate;		// 结束 结束时间
	private Integer beginLimit;		// 开始 返现订单数
	private Integer endLimit;		// 结束 返现订单数
	
	public WemallCashbackDiscount() {
		super();
	}

	public WemallCashbackDiscount(String id){
		super(id);
	}

	@Length(min=1, max=100, message="限时返现活动名称长度必须介于 1 和 100 之间")
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
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@NotNull(message="开始时间不能为空")
	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@NotNull(message="结束时间不能为空")
	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	
	@Length(min=1, max=100, message="标签名长度必须介于 1 和 100 之间")
	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}
	
	@NotNull(message="类型（0--随机返现，1--固定返现）不能为空")
	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}
	
	@Length(min=1, max=20, message="返现比例长度必须介于 1 和 20 之间")
	public String getCashback() {
		return cashback;
	}

	public void setCashback(String cashback) {
		this.cashback = cashback;
	}
	
	public Integer getLimit() {
		return limit;
	}

	public void setLimit(Integer limit) {
		this.limit = limit;
	}
	
	public Date getBeginStartDate() {
		return beginStartDate;
	}

	public void setBeginStartDate(Date beginStartDate) {
		this.beginStartDate = beginStartDate;
	}
	
	public Date getEndStartDate() {
		return endStartDate;
	}

	public void setEndStartDate(Date endStartDate) {
		this.endStartDate = endStartDate;
	}
		
	public Date getBeginEndDate() {
		return beginEndDate;
	}

	public void setBeginEndDate(Date beginEndDate) {
		this.beginEndDate = beginEndDate;
	}
	
	public Date getEndEndDate() {
		return endEndDate;
	}

	public void setEndEndDate(Date endEndDate) {
		this.endEndDate = endEndDate;
	}
		
	public Integer getBeginLimit() {
		return beginLimit;
	}

	public void setBeginLimit(Integer beginLimit) {
		this.beginLimit = beginLimit;
	}
	
	public Integer getEndLimit() {
		return endLimit;
	}

	public void setEndLimit(Integer endLimit) {
		this.endLimit = endLimit;
	}
		
}
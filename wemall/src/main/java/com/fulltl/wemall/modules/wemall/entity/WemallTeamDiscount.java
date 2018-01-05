/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.fulltl.wemall.modules.wemall.entity;

import java.util.Date;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fulltl.wemall.common.persistence.DataEntity;

/**
 * 限时拼团活动管理Entity
 * @author ldk
 * @version 2018-01-05
 */
public class WemallTeamDiscount extends DataEntity<WemallTeamDiscount> {
	
	private static final long serialVersionUID = 1L;
	private String name;		// 限时拼团活动名称
	private Integer sort;		// 排序
	private Date startDate;		// 开始时间
	private Date endDate;		// 结束时间
	private String label;		// 标签名
	private Integer usefulTime;		// 团有效时间(小时数)
	private Integer failUserNum;		// 拼团失败人数
	private Integer limitItemNum;		// 商品限购数
	private Date beginStartDate;		// 开始 开始时间
	private Date endStartDate;		// 结束 开始时间
	private Date beginEndDate;		// 开始 结束时间
	private Date endEndDate;		// 结束 结束时间
	private Integer beginUsefulTime;		// 开始 团有效时间(小时数)
	private Integer endUsefulTime;		// 结束 团有效时间(小时数)
	private Integer beginFailUserNum;		// 开始 拼团失败人数
	private Integer endFailUserNum;		// 结束 拼团失败人数
	private Integer beginLimitItemNum;		// 开始 商品限购数
	private Integer endLimitItemNum;		// 结束 商品限购数
	
	public WemallTeamDiscount() {
		super();
	}

	public WemallTeamDiscount(String id){
		super(id);
	}

	@Length(min=1, max=100, message="限时拼团活动名称长度必须介于 1 和 100 之间")
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
	
	@NotNull(message="团有效时间(小时数)不能为空")
	public Integer getUsefulTime() {
		return usefulTime;
	}

	public void setUsefulTime(Integer usefulTime) {
		this.usefulTime = usefulTime;
	}
	
	@NotNull(message="拼团失败人数不能为空")
	public Integer getFailUserNum() {
		return failUserNum;
	}

	public void setFailUserNum(Integer failUserNum) {
		this.failUserNum = failUserNum;
	}
	
	@NotNull(message="商品限购数不能为空")
	public Integer getLimitItemNum() {
		return limitItemNum;
	}

	public void setLimitItemNum(Integer limitItemNum) {
		this.limitItemNum = limitItemNum;
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
		
	public Integer getBeginUsefulTime() {
		return beginUsefulTime;
	}

	public void setBeginUsefulTime(Integer beginUsefulTime) {
		this.beginUsefulTime = beginUsefulTime;
	}
	
	public Integer getEndUsefulTime() {
		return endUsefulTime;
	}

	public void setEndUsefulTime(Integer endUsefulTime) {
		this.endUsefulTime = endUsefulTime;
	}
		
	public Integer getBeginFailUserNum() {
		return beginFailUserNum;
	}

	public void setBeginFailUserNum(Integer beginFailUserNum) {
		this.beginFailUserNum = beginFailUserNum;
	}
	
	public Integer getEndFailUserNum() {
		return endFailUserNum;
	}

	public void setEndFailUserNum(Integer endFailUserNum) {
		this.endFailUserNum = endFailUserNum;
	}
		
	public Integer getBeginLimitItemNum() {
		return beginLimitItemNum;
	}

	public void setBeginLimitItemNum(Integer beginLimitItemNum) {
		this.beginLimitItemNum = beginLimitItemNum;
	}
	
	public Integer getEndLimitItemNum() {
		return endLimitItemNum;
	}

	public void setEndLimitItemNum(Integer endLimitItemNum) {
		this.endLimitItemNum = endLimitItemNum;
	}
		
}
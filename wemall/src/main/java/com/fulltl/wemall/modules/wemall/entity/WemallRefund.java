/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.fulltl.wemall.modules.wemall.entity;

import org.hibernate.validator.constraints.Length;
import com.fulltl.wemall.modules.sys.entity.User;
import javax.validation.constraints.NotNull;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.fulltl.wemall.common.persistence.DataEntity;

/**
 * 退款管理Entity
 * @author ldk
 * @version 2018-01-05
 */
public class WemallRefund extends DataEntity<WemallRefund> {
	
	private static final long serialVersionUID = 1L;
	private String refundId;		// 退款业务号
	private String orderNo;		// 订单号
	private User user;		// 用户id
	private Integer orderPrice;		// 订单金额
	private Integer payment;		// 实付金额
	private Integer refundFee;		// 退款金额
	private String refundDesc;		// 退款描述
	private Integer refundStatus;		// 退款状态（0--未成功，1--已退款）
	private Date refundDate;		// 退款时间
	private Integer beginOrderPrice;		// 开始 订单金额
	private Integer endOrderPrice;		// 结束 订单金额
	private Integer beginPayment;		// 开始 实付金额
	private Integer endPayment;		// 结束 实付金额
	private Integer beginRefundFee;		// 开始 退款金额
	private Integer endRefundFee;		// 结束 退款金额
	private Date beginRefundDate;		// 开始 退款时间
	private Date endRefundDate;		// 结束 退款时间
	
	private String platformOrderNo;		// 第三方订单号
	
	public WemallRefund() {
		super();
	}

	public WemallRefund(String id){
		super(id);
	}

	@Length(min=1, max=64, message="退款业务号长度必须介于 1 和 64 之间")
	public String getRefundId() {
		return refundId;
	}

	public void setRefundId(String refundId) {
		this.refundId = refundId;
	}
	
	@Length(min=1, max=64, message="订单号长度必须介于 1 和 64 之间")
	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	
	@NotNull(message="用户id不能为空")
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	@NotNull(message="订单金额不能为空")
	public Integer getOrderPrice() {
		return orderPrice;
	}

	public void setOrderPrice(Integer orderPrice) {
		this.orderPrice = orderPrice;
	}
	
	@NotNull(message="实付金额不能为空")
	public Integer getPayment() {
		return payment;
	}

	public void setPayment(Integer payment) {
		this.payment = payment;
	}
	
	@NotNull(message="退款金额不能为空")
	public Integer getRefundFee() {
		return refundFee;
	}

	public void setRefundFee(Integer refundFee) {
		this.refundFee = refundFee;
	}
	
	@Length(min=1, max=200, message="退款描述长度必须介于 1 和 200 之间")
	public String getRefundDesc() {
		return refundDesc;
	}

	public void setRefundDesc(String refundDesc) {
		this.refundDesc = refundDesc;
	}
	
	@NotNull(message="退款状态（0--未成功，1--已退款）不能为空")
	public Integer getRefundStatus() {
		return refundStatus;
	}

	public void setRefundStatus(Integer refundStatus) {
		this.refundStatus = refundStatus;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@NotNull(message="退款时间不能为空")
	public Date getRefundDate() {
		return refundDate;
	}

	public void setRefundDate(Date refundDate) {
		this.refundDate = refundDate;
	}
	
	public Integer getBeginOrderPrice() {
		return beginOrderPrice;
	}

	public void setBeginOrderPrice(Integer beginOrderPrice) {
		this.beginOrderPrice = beginOrderPrice;
	}
	
	public Integer getEndOrderPrice() {
		return endOrderPrice;
	}

	public void setEndOrderPrice(Integer endOrderPrice) {
		this.endOrderPrice = endOrderPrice;
	}
		
	public Integer getBeginPayment() {
		return beginPayment;
	}

	public void setBeginPayment(Integer beginPayment) {
		this.beginPayment = beginPayment;
	}
	
	public Integer getEndPayment() {
		return endPayment;
	}

	public void setEndPayment(Integer endPayment) {
		this.endPayment = endPayment;
	}
		
	public Integer getBeginRefundFee() {
		return beginRefundFee;
	}

	public void setBeginRefundFee(Integer beginRefundFee) {
		this.beginRefundFee = beginRefundFee;
	}
	
	public Integer getEndRefundFee() {
		return endRefundFee;
	}

	public void setEndRefundFee(Integer endRefundFee) {
		this.endRefundFee = endRefundFee;
	}
		
	public Date getBeginRefundDate() {
		return beginRefundDate;
	}

	public void setBeginRefundDate(Date beginRefundDate) {
		this.beginRefundDate = beginRefundDate;
	}
	
	public Date getEndRefundDate() {
		return endRefundDate;
	}

	public void setEndRefundDate(Date endRefundDate) {
		this.endRefundDate = endRefundDate;
	}
	
	public String getPlatformOrderNo() {
		return platformOrderNo;
	}

	public void setPlatformOrderNo(String platformOrderNo) {
		this.platformOrderNo = platformOrderNo;
	}

	@Override
	public boolean getIsNewRecord() {
		 return isNewRecord;
	}
		
}
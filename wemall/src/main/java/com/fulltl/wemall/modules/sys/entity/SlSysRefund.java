/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.fulltl.wemall.modules.sys.entity;

import java.util.Date;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fulltl.wemall.common.persistence.DataEntity;

/**
 * 退款管理Entity
 * @author ldk
 * @version 2018-01-30
 */
public class SlSysRefund extends DataEntity<SlSysRefund> {
	
	private static final long serialVersionUID = 1L;
	private String refundId;		// 退款业务号
	private String orderNo;		// 订单号
	private User user;		// 用户id
	private String orderPrice;		// 订单金额
	private String payMethod;		// 付款方式（alipay--支付宝；weixin--微信支付）
	private String payment;		// 实付金额
	private String refundFee;		// 退款金额
	private String refundDesc;		// 退款描述
	private String refundStatus;		// 退款状态（0--未成功，1--已退款）
	private Date refundDate;		// 退款时间
	
	public SlSysRefund() {
		super();
	}

	public SlSysRefund(String id){
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
	
	@Length(min=1, max=11, message="订单金额长度必须介于 1 和 11 之间")
	public String getOrderPrice() {
		return orderPrice;
	}

	public void setOrderPrice(String orderPrice) {
		this.orderPrice = orderPrice;
	}
	
	@Length(min=1, max=10, message="付款方式长度必须介于 1 和 10 之间")
	public String getPayMethod() {
		return payMethod;
	}

	public void setPayMethod(String payMethod) {
		this.payMethod = payMethod;
	}

	@Length(min=1, max=11, message="实付金额长度必须介于 1 和 11 之间")
	public String getPayment() {
		return payment;
	}

	public void setPayment(String payment) {
		this.payment = payment;
	}
	
	@Length(min=1, max=11, message="退款金额长度必须介于 1 和 11 之间")
	public String getRefundFee() {
		return refundFee;
	}

	public void setRefundFee(String refundFee) {
		this.refundFee = refundFee;
	}
	
	@Length(min=0, max=200, message="退款描述长度必须介于 0 和 200 之间")
	public String getRefundDesc() {
		return refundDesc;
	}

	public void setRefundDesc(String refundDesc) {
		this.refundDesc = refundDesc;
	}
	
	@Length(min=1, max=1, message="退款状态（0--未成功，1--已退款）长度必须介于 1 和 1 之间")
	public String getRefundStatus() {
		return refundStatus;
	}

	public void setRefundStatus(String refundStatus) {
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
	
	@Override
	public boolean getIsNewRecord() {
		 return isNewRecord;
	}
}
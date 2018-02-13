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
 * 订单管理Entity
 * @author ldk
 * @version 2018-01-05
 */
public class WemallOrder extends DataEntity<WemallOrder> {
	
	private static final long serialVersionUID = 1L;
	private String orderNo;		// 订单号
	private User user;		// 用户id
	private String platformOrderNo;		// 支付平台订单号
	private Integer orderPrice;		// 订单金额
	private Integer payment;		// 实付金额
	private Integer paymentType;		// 支付类型
	private Integer totalRefundFee;		// 总退款金额
	private String title;		// 订单名称
	private String body;		// 订单描述
	private Integer status;		// 状态（1、未付款，2、已付款，3、未发货，4、已发货，5、交易成功，6、交易退货，7、交易关闭）
	private Date paymentDate;		// 付款时间
	private Date consignDate;		// 发货时间
	private Date endDate;		// 交易完成时间
	private Date closeDate;		// 交易关闭时间
	private String type;		// 类别
	private Integer scoreUsageNum;		// 使用积分数
	private Integer bountyUsageNum;		// 使用奖励金数
	private Integer couponUsageNum;		// 使用优惠券数
	private Integer vipCardId;		// 使用会员卡id
	private Integer beginOrderPrice;		// 开始 订单金额
	private Integer endOrderPrice;		// 结束 订单金额
	private Integer beginPayment;		// 开始 实付金额
	private Integer endPayment;		// 结束 实付金额
	private Integer beginTotalRefundFee;		// 开始 总退款金额
	private Integer endTotalRefundFee;		// 结束 总退款金额
	private Date beginPaymentDate;		// 开始 付款时间
	private Date endPaymentDate;		// 结束 付款时间
	
	public WemallOrder() {
		super();
	}

	public WemallOrder(String id){
		super(id);
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
	
	@Length(min=1, max=64, message="支付平台订单号长度必须介于 1 和 64 之间")
	public String getPlatformOrderNo() {
		return platformOrderNo;
	}

	public void setPlatformOrderNo(String platformOrderNo) {
		this.platformOrderNo = platformOrderNo;
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
	
	@NotNull(message="支付类型不能为空")
	public Integer getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(Integer paymentType) {
		this.paymentType = paymentType;
	}
	
	@NotNull(message="总退款金额不能为空")
	public Integer getTotalRefundFee() {
		return totalRefundFee;
	}

	public void setTotalRefundFee(Integer totalRefundFee) {
		this.totalRefundFee = totalRefundFee;
	}
	
	@Length(min=1, max=50, message="订单名称长度必须介于 1 和 50 之间")
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	@Length(min=1, max=200, message="订单描述长度必须介于 1 和 200 之间")
	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}
	
	@NotNull(message="状态（1、未付款，2、已付款，3、未发货，4、已发货，5、交易成功，6、交易关闭）不能为空")
	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@NotNull(message="付款时间不能为空")
	public Date getPaymentDate() {
		return paymentDate;
	}

	public void setPaymentDate(Date paymentDate) {
		this.paymentDate = paymentDate;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@NotNull(message="发货时间不能为空")
	public Date getConsignDate() {
		return consignDate;
	}

	public void setConsignDate(Date consignDate) {
		this.consignDate = consignDate;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@NotNull(message="交易完成时间不能为空")
	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@NotNull(message="交易关闭时间不能为空")
	public Date getCloseDate() {
		return closeDate;
	}

	public void setCloseDate(Date closeDate) {
		this.closeDate = closeDate;
	}
	
	@Length(min=1, max=10, message="类别长度必须介于 1 和 10 之间")
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	@NotNull(message="使用积分数不能为空")
	public Integer getScoreUsageNum() {
		return scoreUsageNum;
	}

	public void setScoreUsageNum(Integer scoreUsageNum) {
		this.scoreUsageNum = scoreUsageNum;
	}
	
	@NotNull(message="使用奖励金数不能为空")
	public Integer getBountyUsageNum() {
		return bountyUsageNum;
	}

	public void setBountyUsageNum(Integer bountyUsageNum) {
		this.bountyUsageNum = bountyUsageNum;
	}
	
	@NotNull(message="使用优惠券数不能为空")
	public Integer getCouponUsageNum() {
		return couponUsageNum;
	}

	public void setCouponUsageNum(Integer couponUsageNum) {
		this.couponUsageNum = couponUsageNum;
	}
	
	@NotNull(message="使用会员卡id不能为空")
	public Integer getVipCardId() {
		return vipCardId;
	}

	public void setVipCardId(Integer vipCardId) {
		this.vipCardId = vipCardId;
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
		
	public Integer getBeginTotalRefundFee() {
		return beginTotalRefundFee;
	}

	public void setBeginTotalRefundFee(Integer beginTotalRefundFee) {
		this.beginTotalRefundFee = beginTotalRefundFee;
	}
	
	public Integer getEndTotalRefundFee() {
		return endTotalRefundFee;
	}

	public void setEndTotalRefundFee(Integer endTotalRefundFee) {
		this.endTotalRefundFee = endTotalRefundFee;
	}
		
	public Date getBeginPaymentDate() {
		return beginPaymentDate;
	}

	public void setBeginPaymentDate(Date beginPaymentDate) {
		this.beginPaymentDate = beginPaymentDate;
	}
	
	public Date getEndPaymentDate() {
		return endPaymentDate;
	}

	public void setEndPaymentDate(Date endPaymentDate) {
		this.endPaymentDate = endPaymentDate;
	}
		
}
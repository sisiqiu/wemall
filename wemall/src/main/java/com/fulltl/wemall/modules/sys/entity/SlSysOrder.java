/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.fulltl.wemall.modules.sys.entity;

import java.util.Date;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fulltl.wemall.common.persistence.DataEntity;
import com.fulltl.wemall.common.utils.IdGen;
import com.fulltl.wemall.common.utils.RegExpValidatorUtil;
import com.fulltl.wemall.modules.sys.utils.UserUtils;

/**
 * 订单管理Entity
 * @author ldk
 * @version 2017-11-27
 */
public class SlSysOrder extends DataEntity<SlSysOrder> {
	
	private static final long serialVersionUID = 1L;
	private String orderNo;		// 订单号
	private User user;		// 用户ID
	private String subject;		// 商品名称
	private String orderType;		// 订单类别
	private String orderPrice;		// 订单价格
	private String actualPayment;		// 实际支付价格
	private String payMethod;		// 付款方式（alipay--支付宝；weixin--微信支付）
	private String freightFee;		// 运费
	private String totalRefundFee;		// 运费
	private Integer usedPoints;		// 使用积分数
	private String status;		// 订单状态（1--未付款；2--已付款；3--未发货；4--已发货；5--交易成功；6--交易关闭）
	private String payState;		// 支付状态（1--交易创建，等待买家付款；此状态不会接收到通知
											//2--未付款交易超时关闭，或支付完成后全额退款；此状态不会接收到通知
											//3--交易支付成功；
											//4--交易结束，不可退款；）
	private Date orderDate;		// 下单日期
	private String mobile;		// 手机号
	private String description;		// 订单描述
	private String redEvpUseAmount;		// 使用红包金额
	private String coupUseAmount;		// 使用优惠券金额
	private Office office;		// 单位ID
	private String prepayId;		// 订单付款的预备码
	
	private String regId;		// 预约id

	/**
	 * 订单类别
	 * @author Administrator
	 *
	 */
	public enum OrderTypeEnum {
		/**
		 * 专家
		 */
		expert("01"), 
		/**
		 * 普通
		 */
		oridinary("02"), 
		/**
		 * 特需
		 */
		need("03"),
		/**
		 * 膏方
		 */
		formula("04"),
		/**
		 * 护理预约
		 */
		careAppo("05")
		;
		
		private String value;
		private OrderTypeEnum(String value) {
			this.value = value;
		}
		public String getValue() {
			return value;
		}
	}
	
	/**
	 * 预约大类别
	 * @author Administrator
	 *
	 */
	public enum AppoTypeEnum {
		/**
		 * 预约
		 */
		reg, 
		/**
		 * 护理预约
		 */
		careAppo
		;
	}
	
	/**
	 * 付款方式
	 * @author Administrator
	 *
	 */
	public enum PayMethod {
		/**
		 * 支付宝
		 */
		alipay, 
		/**
		 * 微信支付
		 */
		weixin
		;
	}
	
	public SlSysOrder() {
		super();
	}

	public SlSysOrder(String id){
		super(id);
	}

	@NotNull(message="订单号不能为空！")
	@Length(min=1, max=64, message="订单号长度必须介于 1 和 64 之间")
	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	
	@NotNull(message="用户ID不能为空")
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	@NotNull(message="商品名称不能为空！")
	@Length(min=1, max=100, message="商品名称长度必须介于 1 和 100 之间")
	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}
	
	@NotNull(message="订单类别不能为空！")
	@Length(min=1, max=10, message="订单类别长度必须介于 1 和 10 之间")
	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}
	
	@NotNull(message="订单价格不能为空！")
	@Pattern(regexp=RegExpValidatorUtil.REGEX_DECIMAL, 
			message="订单价格格式错误")
	public String getOrderPrice() {
		return orderPrice;
	}

	public void setOrderPrice(String orderPrice) {
		this.orderPrice = orderPrice;
	}
	
	@NotNull(message="实际支付价格不能为空！")
	@Pattern(regexp=RegExpValidatorUtil.REGEX_DECIMAL, 
			message="实际支付价格格式错误")
	public String getActualPayment() {
		return actualPayment;
	}

	public void setActualPayment(String actualPayment) {
		this.actualPayment = actualPayment;
	}
	
	//@NotNull(message="付款方式不能为空！")
	@Length(min=0, max=10, message="付款方式长度必须介于 0 和 10 之间")
	public String getPayMethod() {
		return payMethod;
	}

	public void setPayMethod(String payMethod) {
		this.payMethod = payMethod;
	}
	
	@Pattern(regexp=RegExpValidatorUtil.REGEX_NULL_OR_DECIMAL, 
			message="运费格式错误")
	public String getFreightFee() {
		return freightFee;
	}

	public void setFreightFee(String freightFee) {
		this.freightFee = freightFee;
	}
	
	@Pattern(regexp=RegExpValidatorUtil.REGEX_NULL_OR_DECIMAL, 
			message="总退款金额格式错误")
	public String getTotalRefundFee() {
		return totalRefundFee;
	}

	public void setTotalRefundFee(String totalRefundFee) {
		this.totalRefundFee = totalRefundFee;
	}

	public Integer getUsedPoints() {
		return usedPoints;
	}

	public void setUsedPoints(Integer usedPoints) {
		this.usedPoints = usedPoints;
	}
	
	@NotNull(message="订单状态不能为空！")
	@Length(min=1, max=10, message="订单状态长度必须介于 1 和 10 之间")
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	@NotNull(message="支付状态不能为空！")
	@Length(min=1, max=1, message="支付状态长度必须介于 1 和 1 之间")
	public String getPayState() {
		return payState;
	}

	public void setPayState(String payState) {
		this.payState = payState;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@NotNull(message="下单日期不能为空")
	public Date getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(Date orderDate) {
		this.orderDate = orderDate;
	}
	
	@NotNull(message="手机号不能为空！")
	@Length(min=1, max=20, message="手机号长度必须介于 1 和 20 之间")
	@Pattern(regexp=RegExpValidatorUtil.REGEX_MOBILE, 
				message="手机号格式错误")
	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	
	@Length(min=0, max=200, message="订单描述长度必须介于 0 和 200 之间")
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	@Pattern(regexp=RegExpValidatorUtil.REGEX_NULL_OR_DECIMAL, 
			message="红包金额格式错误")
	public String getRedEvpUseAmount() {
		return redEvpUseAmount;
	}

	public void setRedEvpUseAmount(String redEvpUseAmount) {
		this.redEvpUseAmount = redEvpUseAmount;
	}
	
	@Pattern(regexp=RegExpValidatorUtil.REGEX_NULL_OR_DECIMAL, 
			message="优惠券金额格式错误")
	public String getCoupUseAmount() {
		return coupUseAmount;
	}

	public void setCoupUseAmount(String coupUseAmount) {
		this.coupUseAmount = coupUseAmount;
	}
	
	public Office getOffice() {
		return office;
	}

	public void setOffice(Office office) {
		this.office = office;
	}
	
	@Override
	public boolean getIsNewRecord() {
		return isNewRecord;
	}
	
	public String getRegId() {
		return regId;
	}

	public void setRegId(String regId) {
		this.regId = regId;
	}
	
	public String getPrepayId() {
		return prepayId;
	}

	public void setPrepayId(String prepayId) {
		this.prepayId = prepayId;
	}

	/**
	 * 初始化新建订单对象，
	 * 生成订单号、匹配当前用户id、订单状态、支付状态、下单日期
	 * @param payMethod
	 */
	public void initSlSysOrder() {
		String outTradeNo = IdGen.generateOrderNo();
		this.setOrderNo(outTradeNo);		//订单号
		User user = UserUtils.getUser();
		this.setUser(user);	//用户id
		this.setStatus("1");	//订单状态；1--未付款
		this.setPayState("1");	//支付状态；1--未支付
		this.setOrderDate(new Date());	//下单日期
		this.setTotalRefundFee("0");	//总退款金额
	}
}
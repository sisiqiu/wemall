/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.fulltl.wemall.modules.wemall.entity;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fulltl.wemall.common.persistence.DataEntity;
import com.fulltl.wemall.common.utils.IdGen;
import com.fulltl.wemall.modules.sys.entity.User;
import com.fulltl.wemall.modules.sys.utils.UserUtils;

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
	private Integer originalOrderPrice;		// 原始订单金额
	private Integer orderPrice;		// 订单金额
	private Integer payment;		// 实付金额
	private Integer paymentType;		// 支付类型
	private Integer freightPrice;		// 总运费
	private Integer totalRefundFee;		// 总退款金额
	private String title;		// 订单名称
	private String body;		// 订单描述
	private String orderCategory;		// 订单类别（1--商品订单，2--充值订单）
	private Integer status;		// 状态（1、未付款，2、已付款，3、已发货，4、已收货，5、已评论，6、交易退货，7、交易关闭，8、未付款，已取消，9、已付款，已取消）
	private Integer applyForReject;		// 是否提交退货申请（1、是，0、否）
	private Date paymentDate;		// 付款时间
	private Date consignDate;		// 发货时间
	private Date rejectDate;		// 申请退货时间
	private Date endDate;		// 交易完成时间
	private Date closeDate;		// 交易关闭时间
	private String type;		// 类别
	private Integer scoreUsageNum;		// 使用积分数
	private Integer bountyUsageNum;		// 使用奖励金数
	private Integer couponUsageNum;		// 使用优惠券数
	private Integer vipCardId;		// 使用会员卡id
	private String prepayId;		// 预付款id
	private String buyerMessage;		// 买家留言
	private String shopCarIds;		// 购物车id列表
	private Integer beginOrderPrice;		// 开始 订单金额
	private Integer endOrderPrice;		// 结束 订单金额
	private Integer beginPayment;		// 开始 实付金额
	private Integer endPayment;		// 结束 实付金额
	private Integer beginTotalRefundFee;		// 开始 总退款金额
	private Integer endTotalRefundFee;		// 结束 总退款金额
	private Date beginPaymentDate;		// 开始 付款时间
	private Date endPaymentDate;		// 结束 付款时间
	private String freightName;			//物流公司
	private String freightNo;			//物流单号
	private String activityId;			//活动id
	private String activityType;			//活动类别
	
	private List<String> statusList; //状态列表
	
	/**
	 * 付款方式
	 * @author Administrator
	 *
	 */
	public enum PaymentType {
		/**
		 * 微信支付
		 */
		weixin(0),
		/**
		 * 支付宝
		 */
		alipay(1), 
		/**
		 * 货到付款
		 */
		cashOnDelivery(2)
		;
		
		private Integer value;
		
		private PaymentType(Integer value) {
			this.value = value;
		}
		
		public Integer getValue() {
			return value;
		}
	}

	/**
	 * 付款方式
	 * 1、未付款，2、已付款，3、已发货，4、已收货，5、已评论，6、交易退货，7、交易关闭，8、已取消
	 * @author Administrator
	 *
	 */
	public enum OrderStatus {
		/**
		 * 未付款
		 */
		unPaid(1),
		/**
		 * 已付款
		 */
		alreadyPaid(2), 
		/**
		 * 已发货
		 */
		alreadyShipped(3),
		/**
		 * 已收货
		 */
		alreadyReceived(4),
		/**
		 * 已评论
		 */
		alreadyCommented(5),
		/**
		 * 交易退货
		 */
		alreadyRejected(6),
		/**
		 * 交易关闭
		 */
		alreadyClosed(7),
		/**
		 * 未付款，已取消
		 */
		alreadyCancelled_unPaid(8),
		/**
		 * 已付款，已取消
		 */
		alreadyCancelled_alreadyPaid(9),
		;
		
		private Integer value;
		
		private OrderStatus(Integer value) {
			this.value = value;
		}
		
		public Integer getValue() {
			return value;
		}
		
		public static OrderStatus getOrderStatusByValue(Integer value) {
			switch(value) {
			case 1:
				return unPaid;
			case 2:
				return alreadyPaid;
			case 3:
				return alreadyShipped;
			case 4:
				return alreadyReceived;
			case 5:
				return alreadyCommented;
			case 6:
				return alreadyRejected;
			case 7:
				return alreadyClosed;
			case 8:
				return alreadyCancelled_unPaid;
			case 9:
				return alreadyCancelled_alreadyPaid;
			}
			return null;
		}
	}
	
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
	
	public Integer getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(Integer paymentType) {
		this.paymentType = paymentType;
	}
	
	@NotNull(message="总运费不能为空")
	public Integer getFreightPrice() {
		return freightPrice;
	}

	public void setFreightPrice(Integer freightPrice) {
		this.freightPrice = freightPrice;
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
	
	@NotNull(message="状态（1、未付款，2、已付款，3、已发货，4、已收货，5、已评论，6、交易退货，7、交易关闭，8、未付款，已取消，9、已付款，已取消）不能为空")
	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getPaymentDate() {
		return paymentDate;
	}

	public void setPaymentDate(Date paymentDate) {
		this.paymentDate = paymentDate;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getConsignDate() {
		return consignDate;
	}

	public void setConsignDate(Date consignDate) {
		this.consignDate = consignDate;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getCloseDate() {
		return closeDate;
	}

	public void setCloseDate(Date closeDate) {
		this.closeDate = closeDate;
	}
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	public Integer getScoreUsageNum() {
		return scoreUsageNum;
	}

	public void setScoreUsageNum(Integer scoreUsageNum) {
		this.scoreUsageNum = scoreUsageNum;
	}
	
	public Integer getBountyUsageNum() {
		return bountyUsageNum;
	}

	public void setBountyUsageNum(Integer bountyUsageNum) {
		this.bountyUsageNum = bountyUsageNum;
	}
	
	public Integer getCouponUsageNum() {
		return couponUsageNum;
	}

	public void setCouponUsageNum(Integer couponUsageNum) {
		this.couponUsageNum = couponUsageNum;
	}
	
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

	public String getPrepayId() {
		return prepayId;
	}

	public void setPrepayId(String prepayId) {
		this.prepayId = prepayId;
	}
	
	public String getBuyerMessage() {
		return buyerMessage;
	}

	public void setBuyerMessage(String buyerMessage) {
		this.buyerMessage = buyerMessage;
	}
	
	public String getShopCarIds() {
		return shopCarIds;
	}

	public void setShopCarIds(String shopCarIds) {
		this.shopCarIds = shopCarIds;
	}

	@Override
	public boolean getIsNewRecord() {
		return isNewRecord;
	}
	
	public String getFreightName() {
		return freightName;
	}

	public void setFreightName(String freightName) {
		this.freightName = freightName;
	}

	public String getFreightNo() {
		return freightNo;
	}

	public void setFreightNo(String freightNo) {
		this.freightNo = freightNo;
	}
	
	@NotNull(message="原始订单金额不能为空")
	public Integer getOriginalOrderPrice() {
		return originalOrderPrice;
	}

	public void setOriginalOrderPrice(Integer originalOrderPrice) {
		this.originalOrderPrice = originalOrderPrice;
	}

	public Integer getApplyForReject() {
		return applyForReject;
	}

	public void setApplyForReject(Integer applyForReject) {
		this.applyForReject = applyForReject;
	}

	public Date getRejectDate() {
		return rejectDate;
	}

	public void setRejectDate(Date rejectDate) {
		this.rejectDate = rejectDate;
	}
	
	public String getActivityId() {
		return activityId;
	}

	public void setActivityId(String activityId) {
		this.activityId = activityId;
	}

	public String getActivityType() {
		return activityType;
	}

	public void setActivityType(String activityType) {
		this.activityType = activityType;
	}

	public List<String> getStatusList() {
		return statusList;
	}

	public void setStatusList(List<String> statusList) {
		this.statusList = statusList;
	}
	
	public String getOrderCategory() {
		return orderCategory;
	}

	public void setOrderCategory(String orderCategory) {
		this.orderCategory = orderCategory;
	}

	/**
	 * 初始化新建订单对象，
	 * 生成订单号、匹配当前用户id、订单状态、支付状态、下单日期
	 * @param payMethod
	 */
	public void init() {
		String outTradeNo = IdGen.generateOrderNo();
		this.setOrderNo(outTradeNo);		//订单号
		User user = UserUtils.getUser();
		this.setUser(user);	//用户id
		this.setStatus(1);	//订单状态；1--未付款
		this.setCreateDate(new Date());	//下单日期
		this.setTotalRefundFee(0);	//总退款金额
		this.setPayment(0);	//实付金额
		this.setApplyForReject(0); //未申请退货
	}
	
	/**
	 * 更新订单状态值时，对状态做校验
	 * @param orderStatus 要更新到的状态值
	 * @return
	 */
	public Map<String, Object> checkUpdateStatus(OrderStatus orderStatus) {
		Map<String, Object> map = new HashMap<String, Object>();
		switch(orderStatus) {
		case unPaid:
			break;
		case alreadyPaid:
			if(!orderStatus.unPaid.getValue().equals(this.getStatus())) {
				map.put("ret", "-1");
				map.put("retMsg", "订单处于不可支付状态，请确认订单处于未付款状态。");
				return map;
			}
			break;
		case alreadyShipped:
			if(!orderStatus.alreadyPaid.getValue().equals(this.getStatus())) {
				map.put("ret", "-1");
				map.put("retMsg", "请确认订单处于已支付状态。");
				return map;
			}
			break;
		case alreadyReceived:
			if(!orderStatus.alreadyShipped.getValue().equals(this.getStatus())) {
				map.put("ret", "-1");
				map.put("retMsg", "请确认订单处于已发货状态。");
				return map;
			}
			break;
		case alreadyCommented:
			if(!orderStatus.alreadyReceived.getValue().equals(this.getStatus())) {
				map.put("ret", "-1");
				map.put("retMsg", "请确认订单处于已收货状态。");
				return map;
			}
			break;
		case alreadyRejected:
			if(!orderStatus.alreadyReceived.getValue().equals(this.getStatus())) {
				map.put("ret", "-1");
				map.put("retMsg", "请确认订单已发货且未收货。");
				return map;
			}
			break;
		case alreadyClosed:
			break;
		case alreadyCancelled_unPaid:
			//更新
			if(!orderStatus.unPaid.getValue().equals(this.getStatus())) {
				map.put("ret", "-1");
				map.put("retMsg", "请确认订单处于未付款状态。");
				return map;
			}
			break;
		case alreadyCancelled_alreadyPaid:
			//更新
			if(!orderStatus.alreadyPaid.getValue().equals(this.getStatus())) {
				map.put("ret", "-1");
				map.put("retMsg", "请确认订单处于已付款未发货状态。");
				return map;
			}
			break;
		}
		
		map.put("ret", "0");
		map.put("retMsg", "校验成功");
		return map;
	}
}
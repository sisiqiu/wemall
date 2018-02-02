package com.fulltl.wemall.modules.wx.core.pojo.trade;

import java.math.BigDecimal;

import com.fulltl.wemall.modules.alipay.core.pojo.base.AlipayTradeBaseEntity;

/**
 * 支付宝退款查询实体类。
 * @author Administrator
 *
 */
public class WeixinTradeRefundQueryEntity extends AlipayTradeBaseEntity {
	private String out_request_no; //本笔退款对应的退款请求号
	private String refund_reason; //发起退款时，传入的退款原因
	private BigDecimal total_amount; //该笔退款所对应的交易的订单金额
	private BigDecimal refund_amount; //本次退款请求，对应的退款金额
	
	public String getOut_request_no() {
		return out_request_no;
	}
	public void setOut_request_no(String out_request_no) {
		this.out_request_no = out_request_no;
	}
	public String getRefund_reason() {
		return refund_reason;
	}
	public void setRefund_reason(String refund_reason) {
		this.refund_reason = refund_reason;
	}
	public BigDecimal getTotal_amount() {
		return total_amount;
	}
	public void setTotal_amount(BigDecimal total_amount) {
		this.total_amount = total_amount;
	}
	public BigDecimal getRefund_amount() {
		return refund_amount;
	}
	public void setRefund_amount(BigDecimal refund_amount) {
		this.refund_amount = refund_amount;
	}
}

package com.fulltl.wemall.modules.wx.core.pojo.trade.base;

/**
 * 微信业务基础实体类
 * @author Administrator
 *
 */
public class WeixinTradeBaseEntity extends WeixinBaseEntity {
	private String transaction_id; //微信支付订单号
	private String out_trade_no; //	商户订单号
	
	public String getTransaction_id() {
		return transaction_id;
	}
	public void setTransaction_id(String transaction_id) {
		this.transaction_id = transaction_id;
	}
	public String getOut_trade_no() {
		return out_trade_no;
	}
	public void setOut_trade_no(String out_trade_no) {
		this.out_trade_no = out_trade_no;
	}
	
}

package com.fulltl.wemall.modules.alipay.core.pojo.base;

/**
 * 支付宝业务基础实体类
 * @author Administrator
 *
 */
public class AlipayTradeBaseEntity extends AlipayBaseEntity {
	private String trade_no; //支付宝交易号
	private String out_trade_no; //	商户订单号
	
	public String getTrade_no() {
		return trade_no;
	}
	public void setTrade_no(String trade_no) {
		this.trade_no = trade_no;
	}
	public String getOut_trade_no() {
		return out_trade_no;
	}
	public void setOut_trade_no(String out_trade_no) {
		this.out_trade_no = out_trade_no;
	}
	
}

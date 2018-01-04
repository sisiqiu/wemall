package com.fulltl.wemall.modules.alipay.core.pojo;

/**
 * 支付宝交易状态
 * @author Administrator
 *
 */
public enum AlipayTradeStatusEnum {
	/**
	 * 交易创建，等待买家付款
	 */
	WAIT_BUYER_PAY(1), 
	/**
	 * 未付款交易超时关闭，或支付完成后全额退款
	 */
	TRADE_CLOSED(2),
	/**
	 * 交易支付成功
	 */
	TRADE_SUCCESS(3),
	/**
	 * 交易结束，不可退款
	 */
	TRADE_FINISHED(4);
	
	private int value;
	
	private AlipayTradeStatusEnum(int value) {
        this.value = value;
    }
	
	public int getValue() {
        return value;
    }
	
}

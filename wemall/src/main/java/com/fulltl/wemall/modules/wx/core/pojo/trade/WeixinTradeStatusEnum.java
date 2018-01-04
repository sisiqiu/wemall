package com.fulltl.wemall.modules.wx.core.pojo.trade;

/**
 * 支付宝交易状态
 * @author Administrator
 *
 */
public enum WeixinTradeStatusEnum {
	/**
	 * 支付成功
	 */
	SUCCESS(1), 
	/**
	 * 转入退款
	 */
	REFUND(2),
	/**
	 * 未支付
	 */
	NOTPAY(3),
	/**
	 * 已关闭
	 */
	CLOSED(4),
	/**
	 * 已撤销（刷卡支付）
	 */
	REVOKED(5),
	/**
	 * 用户支付中
	 */
	USERPAYING(6),
	/**
	 * 支付失败(其他原因，如银行返回失败)
	 */
	PAYERROR(7);
	
	private int value;
	
	private WeixinTradeStatusEnum(int value) {
        this.value = value;
    }
	
	public int getValue() {
        return value;
    }
	
}

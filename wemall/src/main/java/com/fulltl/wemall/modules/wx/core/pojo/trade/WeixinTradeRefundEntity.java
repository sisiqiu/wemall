package com.fulltl.wemall.modules.wx.core.pojo.trade;

import java.util.Date;

import com.alipay.api.domain.TradeFundBill;
import com.fulltl.wemall.modules.alipay.core.pojo.base.AlipayTradeBaseEntity;

/**
 * 支付宝退款实体类。
 * @author Administrator
 *
 */
public class WeixinTradeRefundEntity extends AlipayTradeBaseEntity {
	private String buyer_logon_id; //	用户的登录id
	private String fund_change; //本次退款是否发生了资金变化
	private String refund_fee; //退款总金额, Y
	private Date gmt_refund_pay; //退款支付时间
	private TradeFundBill trefund_detail_item_listrade_no; //退款使用的资金渠道
	private String store_name; //支付宝交易号
	private String buyer_user_id; //支付宝交易号
	
	public String getBuyer_logon_id() {
		return buyer_logon_id;
	}
	public void setBuyer_logon_id(String buyer_logon_id) {
		this.buyer_logon_id = buyer_logon_id;
	}
	public String getFund_change() {
		return fund_change;
	}
	public void setFund_change(String fund_change) {
		this.fund_change = fund_change;
	}
	public String getRefund_fee() {
		return refund_fee;
	}
	public void setRefund_fee(String refund_fee) {
		this.refund_fee = refund_fee;
	}
	public Date getGmt_refund_pay() {
		return gmt_refund_pay;
	}
	public void setGmt_refund_pay(Date gmt_refund_pay) {
		this.gmt_refund_pay = gmt_refund_pay;
	}
	public TradeFundBill getTrefund_detail_item_listrade_no() {
		return trefund_detail_item_listrade_no;
	}
	public void setTrefund_detail_item_listrade_no(TradeFundBill trefund_detail_item_listrade_no) {
		this.trefund_detail_item_listrade_no = trefund_detail_item_listrade_no;
	}
	public String getStore_name() {
		return store_name;
	}
	public void setStore_name(String store_name) {
		this.store_name = store_name;
	}
	public String getBuyer_user_id() {
		return buyer_user_id;
	}
	public void setBuyer_user_id(String buyer_user_id) {
		this.buyer_user_id = buyer_user_id;
	}
	
}

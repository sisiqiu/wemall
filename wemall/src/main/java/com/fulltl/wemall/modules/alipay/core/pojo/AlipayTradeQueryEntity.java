package com.fulltl.wemall.modules.alipay.core.pojo;

import java.math.BigDecimal;
import java.util.Date;

import com.alipay.api.domain.TradeFundBill;
import com.fulltl.wemall.modules.alipay.core.pojo.base.AlipayTradeBaseEntity;

/**
 * 支付宝交易查询实体类
 * @author Administrator
 *
 */
public class AlipayTradeQueryEntity extends AlipayTradeBaseEntity {
	private String buyer_logon_id; //买家支付宝账号
	private String trade_status; //交易状态：WAIT_BUYER_PAY（交易创建，等待买家付款）、TRADE_CLOSED（未付款交易超时关闭，或支付完成后全额退款）、TRADE_SUCCESS（交易支付成功）、TRADE_FINISHED（交易结束，不可退款）
	private BigDecimal total_amount; //交易的订单金额，单位为元，两位小数。
	private BigDecimal receipt_amount; //实收金额，单位为元，两位小数。
	private BigDecimal buyer_pay_amount; //买家实付金额，单位为元，两位小数。
	private BigDecimal point_amount; //积分支付的金额，单位为元，两位小数。
	private BigDecimal invoice_amount; //交易中用户支付的可开具发票的金额，单位为元，两位小数。
	private Date send_pay_date; //本次交易打款给卖家的时间
	private String store_id; //商户门店编号
	private String terminal_id; //商户机具终端编号
	private TradeFundBill fund_bill_list; //交易支付使用的资金渠道
	private String store_name; //请求交易支付中的商户店铺的名称
	private String buyer_user_id; //买家在支付宝的用户id
	
	public String getBuyer_logon_id() {
		return buyer_logon_id;
	}
	public void setBuyer_logon_id(String buyer_logon_id) {
		this.buyer_logon_id = buyer_logon_id;
	}
	public String getTrade_status() {
		return trade_status;
	}
	public void setTrade_status(String trade_status) {
		this.trade_status = trade_status;
	}
	public BigDecimal getTotal_amount() {
		return total_amount;
	}
	public void setTotal_amount(BigDecimal total_amount) {
		this.total_amount = total_amount;
	}
	public BigDecimal getReceipt_amount() {
		return receipt_amount;
	}
	public void setReceipt_amount(BigDecimal receipt_amount) {
		this.receipt_amount = receipt_amount;
	}
	public BigDecimal getBuyer_pay_amount() {
		return buyer_pay_amount;
	}
	public void setBuyer_pay_amount(BigDecimal buyer_pay_amount) {
		this.buyer_pay_amount = buyer_pay_amount;
	}
	public BigDecimal getPoint_amount() {
		return point_amount;
	}
	public void setPoint_amount(BigDecimal point_amount) {
		this.point_amount = point_amount;
	}
	public BigDecimal getInvoice_amount() {
		return invoice_amount;
	}
	public void setInvoice_amount(BigDecimal invoice_amount) {
		this.invoice_amount = invoice_amount;
	}
	public Date getSend_pay_date() {
		return send_pay_date;
	}
	public void setSend_pay_date(Date send_pay_date) {
		this.send_pay_date = send_pay_date;
	}
	public String getStore_id() {
		return store_id;
	}
	public void setStore_id(String store_id) {
		this.store_id = store_id;
	}
	public String getTerminal_id() {
		return terminal_id;
	}
	public void setTerminal_id(String terminal_id) {
		this.terminal_id = terminal_id;
	}
	public TradeFundBill getFund_bill_list() {
		return fund_bill_list;
	}
	public void setFund_bill_list(TradeFundBill fund_bill_list) {
		this.fund_bill_list = fund_bill_list;
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

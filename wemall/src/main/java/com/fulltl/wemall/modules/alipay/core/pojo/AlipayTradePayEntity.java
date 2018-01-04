package com.fulltl.wemall.modules.alipay.core.pojo;

import java.math.BigDecimal;
import java.util.Date;

import com.alipay.api.domain.TradeFundBill;
import com.alipay.api.domain.VoucherDetail;
import com.fulltl.wemall.modules.alipay.core.pojo.base.AlipayTradeBaseEntity;

/**
 * 支付宝支付实体类。
 * @author Administrator
 *
 */
public class AlipayTradePayEntity extends AlipayTradeBaseEntity {
	private String buyer_logon_id; //买家支付宝账号
	private BigDecimal total_amount; //	交易金额
	private BigDecimal receipt_amount; //实收金额
	private BigDecimal buyer_pay_amount; //买家付款的金额
	private BigDecimal point_amount; //	使用积分宝付款的金额
	private BigDecimal invoice_amount; //交易中可给用户开具发票的金额
	private Date gmt_payment; //交易支付时间
	private TradeFundBill fund_bill_list; //交易支付使用的资金渠道
	private BigDecimal card_balance; //支付宝卡余额
	private String store_name; //发生支付交易的商户门店名称
	private String buyer_user_id; //买家在支付宝的用户id
	private String discount_goods_detail; //本次交易支付所使用的单品券优惠的商品优惠信息
	private VoucherDetail voucher_detail_list; //本交易支付时使用的所有优惠券信息
	private String business_params; //商户传入业务信息，具体值要和支付宝约定，将商户传入信息分发给相应系统，应用于安全，营销等参数直传场景 ，格式为json格式
	
	public String getBuyer_logon_id() {
		return buyer_logon_id;
	}
	public void setBuyer_logon_id(String buyer_logon_id) {
		this.buyer_logon_id = buyer_logon_id;
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
	public Date getGmt_payment() {
		return gmt_payment;
	}
	public void setGmt_payment(Date gmt_payment) {
		this.gmt_payment = gmt_payment;
	}
	public TradeFundBill getFund_bill_list() {
		return fund_bill_list;
	}
	public void setFund_bill_list(TradeFundBill fund_bill_list) {
		this.fund_bill_list = fund_bill_list;
	}
	public BigDecimal getCard_balance() {
		return card_balance;
	}
	public void setCard_balance(BigDecimal card_balance) {
		this.card_balance = card_balance;
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
	public String getDiscount_goods_detail() {
		return discount_goods_detail;
	}
	public void setDiscount_goods_detail(String discount_goods_detail) {
		this.discount_goods_detail = discount_goods_detail;
	}
	public VoucherDetail getVoucher_detail_list() {
		return voucher_detail_list;
	}
	public void setVoucher_detail_list(VoucherDetail voucher_detail_list) {
		this.voucher_detail_list = voucher_detail_list;
	}
	public String getBusiness_params() {
		return business_params;
	}
	public void setBusiness_params(String business_params) {
		this.business_params = business_params;
	}
	
}

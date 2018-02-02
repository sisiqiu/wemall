package com.fulltl.wemall.modules.wx.core.pojo.trade;

import java.math.BigDecimal;
import java.util.Map;

import com.fulltl.wemall.modules.wx.core.pojo.trade.base.WeixinTradeBaseEntity;

/**
 * 业务参数整合
 * @author Administrator
 *
 */
public class WeixinTradeAllEntity extends WeixinTradeBaseEntity {
	//////////////公共参数////////////////
	//////////////return_code为SUCCESS的时候有返回的参数////////////////
	private String appid; //应用APPID
	private String mch_id; //商户号
	private String nonce_str; //随机字符串
	private String sign; //签名
	private String result_code; //业务结果
	private String err_code; //错误代码
	private String err_code_des; //错误代码描述
	//////////////业务参数////////////////
	private String trade_type; //交易类型,取值如下：JSAPI，NATIVE，APP
	private String trade_state; //交易状态,SUCCESS—支付成功,
								//REFUND—转入退款,
								//NOTPAY—未支付,
								//CLOSED—已关闭,
								//REVOKED—已撤销（刷卡支付）,
								//USERPAYING--用户支付中,
								//PAYERROR--支付失败(其他原因，如银行返回失败)
	private String prepay_id; //预支付交易会话标识
	private String openid; //用户在商户appid下的唯一标识
	private String is_subscribe; //用户是否关注公众账号，Y-关注，N-未关注，仅在公众账号类型支付有效
	private String bank_type; //银行类型，采用字符串类型的银行标识
	private BigDecimal total_fee; //订单总金额，单位为分
	private String fee_type; //货币类型，符合ISO4217标准的三位字母代码，默认人民币：CNY
	private BigDecimal cash_fee; //现金支付金额订单现金支付金额
	private String cash_fee_type; //货币类型，符合ISO4217标准的三位字母代码，默认人民币：CNY
	private BigDecimal coupon_fee; //代金券金额
	private String coupon_count; //代金券使用数量
	//private String coupon_id_$n; //代金券或立减优惠ID,$n为下标，从0开始编号
	//private String coupon_type_$n; //代金券类型，开通免充值券功能，并且订单使用了优惠券后有返回（取值：CASH、NO_CASH）。$n为下标,从0开始编号
	//private String coupon_type_$n_$m; //代金券类型，开通免充值券功能，并且订单使用了优惠券后有返回（取值：CASH、NO_CASH）。$n为下标,$m为下标,从0开始编号
	//private BigDecimal coupon_fee_$n; //单个代金券或立减优惠支付金额,$n为下标，从0开始编号
	private String attach; //商家数据包，原样返回
	private String time_end; //支付完成时间，格式为yyyyMMddHHmmss
	private String trade_state_desc; //交易状态描述
	private String device_info; //设备号
	private BigDecimal settlement_total_fee; //应结订单金额
	
	private String out_refund_no; //商户退款单号
	private String refund_id; //微信退款单号
	private BigDecimal refund_fee; //退款金额
	private String refund_status; //SUCCESS-退款成功,CHANGE-退款异常,REFUNDCLOSE—退款关闭
	//private String refund_status_$n; //退款状态：SUCCESS—退款成功，REFUNDCLOSE—退款关闭。，PROCESSING—退款处理中，CHANGE—退款异常，$n为下标，从0开始编号
	private BigDecimal settlement_refund_fee; //应结退款金额
	private BigDecimal cash_refund_fee; //现金退款金额
	private BigDecimal coupon_refund_fee; //代金券退款总金额
	//private BigDecimal coupon_refund_fee_$n; //单个代金券退款金额
	//private BigDecimal coupon_refund_fee_$n_$m; //单个代金券或立减优惠退款金额, $n为下标，$m为下标，从0开始编号
	//private String coupon_refund_id_$n; //退款代金券ID, $n为下标，从0开始编号
	//private String coupon_refund_id_$n_$m; //代金券或立减优惠ID, $n为下标，$m为下标，从0开始编号
	//private String coupon_refund_count_$n; //代金券或立减优惠使用数量 ,$n为下标,从0开始编号
	//private String out_refund_no_$n; //商户退款单号
	//private String refund_id_$n; //微信退款单号
	//private BigDecimal refund_fee_$n; //退款金额
	//private String refund_channel_$n; //退款渠道,ORIGINAL—原路退款,BALANCE—退回到余额,OTHER_BALANCE—原账户异常退到其他余额账户,OTHER_BANKCARD—原银行卡异常退到其他银行卡
	private String coupon_refund_count; //退款代金券使用数量
	private String total_refund_count; //订单总退款次数
	private String refund_count; //当前返回退款笔数
	//private String refund_account_$n; //退款资金来源
	//private String refund_recv_accout_$n; //退款入账账户
	//private String refund_success_time_$n; //退款成功时间
	private String refund_account; //退款资金来源
	private String refund_recv_accout; //退款入账账户
	private String refund_request_source; //退款发起来源
	private String success_time; //退款成功时间
	
	public String getAppid() {
		return appid;
	}
	public void setAppid(String appid) {
		this.appid = appid;
	}
	public String getMch_id() {
		return mch_id;
	}
	public void setMch_id(String mch_id) {
		this.mch_id = mch_id;
	}
	public String getNonce_str() {
		return nonce_str;
	}
	public void setNonce_str(String nonce_str) {
		this.nonce_str = nonce_str;
	}
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
	public String getResult_code() {
		return result_code;
	}
	public void setResult_code(String result_code) {
		this.result_code = result_code;
	}
	public String getErr_code() {
		return err_code;
	}
	public void setErr_code(String err_code) {
		this.err_code = err_code;
	}
	public String getErr_code_des() {
		return err_code_des;
	}
	public void setErr_code_des(String err_code_des) {
		this.err_code_des = err_code_des;
	}
	public String getTrade_type() {
		return trade_type;
	}
	public void setTrade_type(String trade_type) {
		this.trade_type = trade_type;
	}
	public String getTrade_state() {
		return trade_state;
	}
	public void setTrade_state(String trade_state) {
		this.trade_state = trade_state;
	}
	public String getPrepay_id() {
		return prepay_id;
	}
	public void setPrepay_id(String prepay_id) {
		this.prepay_id = prepay_id;
	}
	public String getOpenid() {
		return openid;
	}
	public void setOpenid(String openid) {
		this.openid = openid;
	}
	public String getIs_subscribe() {
		return is_subscribe;
	}
	public void setIs_subscribe(String is_subscribe) {
		this.is_subscribe = is_subscribe;
	}
	public String getBank_type() {
		return bank_type;
	}
	public void setBank_type(String bank_type) {
		this.bank_type = bank_type;
	}
	public BigDecimal getTotal_fee() {
		return total_fee;
	}
	public void setTotal_fee(BigDecimal total_fee) {
		this.total_fee = total_fee;
	}
	public String getFee_type() {
		return fee_type;
	}
	public void setFee_type(String fee_type) {
		this.fee_type = fee_type;
	}
	public BigDecimal getCash_fee() {
		return cash_fee;
	}
	public void setCash_fee(BigDecimal cash_fee) {
		this.cash_fee = cash_fee;
	}
	public String getCash_fee_type() {
		return cash_fee_type;
	}
	public void setCash_fee_type(String cash_fee_type) {
		this.cash_fee_type = cash_fee_type;
	}
	public BigDecimal getCoupon_fee() {
		return coupon_fee;
	}
	public void setCoupon_fee(BigDecimal coupon_fee) {
		this.coupon_fee = coupon_fee;
	}
	public String getCoupon_count() {
		return coupon_count;
	}
	public void setCoupon_count(String coupon_count) {
		this.coupon_count = coupon_count;
	}
	public String getAttach() {
		return attach;
	}
	public void setAttach(String attach) {
		this.attach = attach;
	}
	public String getTime_end() {
		return time_end;
	}
	public void setTime_end(String time_end) {
		this.time_end = time_end;
	}
	public String getTrade_state_desc() {
		return trade_state_desc;
	}
	public void setTrade_state_desc(String trade_state_desc) {
		this.trade_state_desc = trade_state_desc;
	}
	public String getDevice_info() {
		return device_info;
	}
	public void setDevice_info(String device_info) {
		this.device_info = device_info;
	}
	public BigDecimal getSettlement_total_fee() {
		return settlement_total_fee;
	}
	public void setSettlement_total_fee(BigDecimal settlement_total_fee) {
		this.settlement_total_fee = settlement_total_fee;
	}
	public String getOut_refund_no() {
		return out_refund_no;
	}
	public void setOut_refund_no(String out_refund_no) {
		this.out_refund_no = out_refund_no;
	}
	public String getRefund_id() {
		return refund_id;
	}
	public void setRefund_id(String refund_id) {
		this.refund_id = refund_id;
	}
	public BigDecimal getRefund_fee() {
		return refund_fee;
	}
	public void setRefund_fee(BigDecimal refund_fee) {
		this.refund_fee = refund_fee;
	}
	public String getRefund_status() {
		return refund_status;
	}
	public void setRefund_status(String refund_status) {
		this.refund_status = refund_status;
	}
	public BigDecimal getSettlement_refund_fee() {
		return settlement_refund_fee;
	}
	public void setSettlement_refund_fee(BigDecimal settlement_refund_fee) {
		this.settlement_refund_fee = settlement_refund_fee;
	}
	public BigDecimal getCash_refund_fee() {
		return cash_refund_fee;
	}
	public void setCash_refund_fee(BigDecimal cash_refund_fee) {
		this.cash_refund_fee = cash_refund_fee;
	}
	public BigDecimal getCoupon_refund_fee() {
		return coupon_refund_fee;
	}
	public void setCoupon_refund_fee(BigDecimal coupon_refund_fee) {
		this.coupon_refund_fee = coupon_refund_fee;
	}
	public String getCoupon_refund_count() {
		return coupon_refund_count;
	}
	public void setCoupon_refund_count(String coupon_refund_count) {
		this.coupon_refund_count = coupon_refund_count;
	}
	public String getTotal_refund_count() {
		return total_refund_count;
	}
	public void setTotal_refund_count(String total_refund_count) {
		this.total_refund_count = total_refund_count;
	}
	public String getRefund_count() {
		return refund_count;
	}
	public void setRefund_count(String refund_count) {
		this.refund_count = refund_count;
	}
	public String getRefund_account() {
		return refund_account;
	}
	public void setRefund_account(String refund_account) {
		this.refund_account = refund_account;
	}
	public String getRefund_recv_accout() {
		return refund_recv_accout;
	}
	public void setRefund_recv_accout(String refund_recv_accout) {
		this.refund_recv_accout = refund_recv_accout;
	}
	public String getRefund_request_source() {
		return refund_request_source;
	}
	public void setRefund_request_source(String refund_request_source) {
		this.refund_request_source = refund_request_source;
	}
	public String getSuccess_time() {
		return success_time;
	}
	public void setSuccess_time(String success_time) {
		this.success_time = success_time;
	}
	
}

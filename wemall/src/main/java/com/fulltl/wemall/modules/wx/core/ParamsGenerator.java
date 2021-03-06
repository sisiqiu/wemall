package com.fulltl.wemall.modules.wx.core;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fulltl.wemall.common.config.Global;
import com.fulltl.wemall.common.service.BaseService;
import com.fulltl.wemall.common.utils.DateUtils;
import com.fulltl.wemall.common.utils.IdGen;
import com.fulltl.wemall.common.utils.RequestUtil;
import com.fulltl.wemall.modules.wemall.entity.WemallOrder;
import com.fulltl.wemall.modules.wemall.entity.WemallRefund;
import com.fulltl.wemall.modules.wx.core.utils.WeixinTradeSignature;
import com.google.common.collect.Maps;

/**
 * 参数map生成器
 * @author Administrator
 *
 */
public class ParamsGenerator {
	
	private static Logger logger = LoggerFactory.getLogger(ParamsGenerator.class);
	
	/**
	 * 为调用微信接口统一下单构建参数map
	 * @return
	 */
	public static Map<String,String> generateParamsForUnifiedOrder(WemallOrder wemallOrder, String openId, HttpServletRequest request) {
		//String basePath = BaseService.getBasePath(request);
		String basePath = Global.getConfig("weixin.appUrl");
		Map<String, String> paramsMap = generateCommonParams();
		
		paramsMap.put("body", wemallOrder.getTitle());
		//paramsMap.put("out_trade_no", wemallOrder.getOrderNo());
		paramsMap.put("out_trade_no", wemallOrder.getPlatformOrderNo());
		paramsMap.put("total_fee", wemallOrder.getOrderPrice().toString());//订单总金额，单位为分
		paramsMap.put("spbill_create_ip", RequestUtil.getIpAddress(request));//用户端实际ip
		Date curDate = new Date();
		paramsMap.put("time_start", DateUtils.formatDate(curDate, "yyyyMMddHHmmss"));//订单生成时间，格式为yyyyMMddHHmmss
		try {
			paramsMap.put("time_expire", DateUtils.formatDate(DateUtils.addTimeByStr(curDate, WeixinTradeConfig.timeoutExpress), "yyyyMMddHHmmss"));
		} catch (Exception e) {
			logger.error("订单过期时间设定格式错误!", e);
		}
		//订单失效时间，格式为yyyyMMddHHmmss
		logger.info("notify_url===" + basePath + WeixinTradeConfig.notify_url);
		paramsMap.put("notify_url", basePath + WeixinTradeConfig.notify_url);//接收微信支付异步通知回调地址
		paramsMap.put("trade_type", WeixinTradeConfig.trade_type);//交易类型,支付类型
		paramsMap.put("openid", openId);//交易类型,支付类型
		
		//最后根据参数map生成签名
		generateSignForParams(paramsMap);
		return paramsMap;
	}
	
	/**
	 * 为调用微信接口申请退款构建参数map
	 * @return
	 */
	public static Map<String,String> generateParamsForRefund(WemallRefund wemallRefund) {
		Map<String, String> paramsMap = generateCommonParams();
		
		paramsMap.put("out_refund_no", wemallRefund.getRefundId());
		//paramsMap.put("out_trade_no", wemallRefund.getOrderNo());
		paramsMap.put("out_trade_no", wemallRefund.getPlatformOrderNo());
		paramsMap.put("total_fee", wemallRefund.getOrderPrice().toString());//订单总金额，单位为分
		paramsMap.put("refund_fee", wemallRefund.getRefundFee().toString());//退款金额，单位为分
		//paramsMap.put("refund_desc", wemallRefund.getRefundDesc());
		
		//最后根据参数map生成签名
		generateSignForParams(paramsMap);
		return paramsMap;
	}
	
	/**
	 * 为调用微信接口查询订单构建参数map
	 * @param orderNo 商户订单号，商户网站订单系统中唯一订单号
	 * @param trade_no 微信订单号
	 * @return
	 */
	public static Map<String, String> generateParamsForOrderQuery(String orderNo, String trade_no) {
		Map<String, String> paramsMap = generateCommonParams();
		
		if(StringUtils.isNotBlank(trade_no)) {
			paramsMap.put("transaction_id", trade_no);
		} else {
			paramsMap.put("out_trade_no", orderNo);
		}
		
		//最后根据参数map生成签名
		generateSignForParams(paramsMap);
		return paramsMap;
	}
	
	/**
	 * 为调用微信接口查询退款构建参数map
	 * @param orderNo 商户订单号
	 * @param trade_no 微信订单号
	 * @param refundId 退款商户业务号
	 * @return
	 */
	public static Map<String, String> generateParamsForRefundQuery(String orderNo, String trade_no, String refundId) {
		Map<String, String> paramsMap = generateCommonParams();
		
		if(StringUtils.isNotBlank(refundId)) {
			paramsMap.put("refund_id", refundId);
		} else if(StringUtils.isNotBlank(trade_no)) {
			paramsMap.put("transaction_id", trade_no);
		} else {
			paramsMap.put("out_trade_no", orderNo);
		}
		
		//最后根据参数map生成签名
		generateSignForParams(paramsMap);
		return paramsMap;
	}
	
	/**
	 * 为调用微信接口下载对账单构建参数map
	 * @param bill_type
	 * @param bill_date
	 * @param tar_type
	 * @return
	 */
	public static Map<String, String> generateParamsForDownloadBill(String bill_type, String bill_date,
			boolean tar) {
		Map<String, String> paramsMap = generateCommonParams();
		
		paramsMap.put("bill_type", bill_type);
		paramsMap.put("bill_date", bill_date);
		if(tar) paramsMap.put("tar_type", "GZIP");
		
		//最后根据参数map生成签名
		generateSignForParams(paramsMap);
		return paramsMap;
	}
	
	/**
	 * 通用数据参数生成
	 * @return
	 */
	public static Map<String, String> generateCommonParams() {
		Map<String, String> paramsMap = Maps.newHashMap();
		paramsMap.put("appid", WeixinTradeConfig.appid);
		paramsMap.put("mch_id", WeixinTradeConfig.mch_id);
		paramsMap.put("nonce_str", IdGen.randomBase62(15));
		paramsMap.put("sign_type", WeixinTradeConfig.sign_type);
		return paramsMap;
	}
	
	/**
	 * 通用生成签名加入参数map中
	 * @param paramsMap
	 */
	public static void generateSignForParams(Map<String, String> paramsMap) {
		if("MD5".equals(WeixinTradeConfig.sign_type)) {
			paramsMap.put("sign", WeixinTradeSignature.getSignStrByMD5(WeixinTradeConfig.key, paramsMap));
		} else if("HMAC-SHA256".equals(WeixinTradeConfig.sign_type)) {
			paramsMap.put("sign", WeixinTradeSignature.getSignStrByHMACSHA256(WeixinTradeConfig.key, paramsMap));
		}
	}
	
	
	/**
	 * 根据预付款id获取调取微信支付签名
	 * @return
	 */
	public static Map<String, String> generateParamsByPrepayId(String prepayId) {
		Map<String, String> paramsMap = Maps.newHashMap();
		paramsMap.put("appId", WeixinTradeConfig.appid);
		paramsMap.put("timeStamp", Long.toString((long)(System.currentTimeMillis()/1000)));
		paramsMap.put("nonceStr", IdGen.randomBase62(15));
		paramsMap.put("package", "prepay_id=" + prepayId);
		paramsMap.put("nonceStr", IdGen.randomBase62(15));
		paramsMap.put("signType", WeixinTradeConfig.sign_type);

		if("MD5".equals(WeixinTradeConfig.sign_type)) {
			paramsMap.put("paySign", WeixinTradeSignature.getSignStrByMD5(WeixinTradeConfig.key, paramsMap));
		} else if("HMAC-SHA256".equals(WeixinTradeConfig.sign_type)) {
			paramsMap.put("paySign", WeixinTradeSignature.getSignStrByHMACSHA256(WeixinTradeConfig.key, paramsMap));
		}
		return paramsMap;
	}

	/**
	 * map转xml
	 * @return
	 */
	public static String mapToXML(Map<String, String> map) {
		StringBuffer result = new StringBuffer();
		result.append("<xml>");
		for(String key : map.keySet()) {
			result.append("<");
			result.append(key);
			result.append(">");
			result.append(map.get(key));
			result.append("</");
			result.append(key);
			result.append(">");
		}
		result.append("</xml>");
		return result.toString();
	}
}

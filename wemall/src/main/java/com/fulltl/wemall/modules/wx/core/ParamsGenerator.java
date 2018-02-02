package com.fulltl.wemall.modules.wx.core;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fulltl.wemall.common.service.BaseService;
import com.fulltl.wemall.common.utils.DateUtils;
import com.fulltl.wemall.common.utils.IdGen;
import com.fulltl.wemall.common.utils.RequestUtil;
import com.fulltl.wemall.modules.sys.entity.SlSysOrder;
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
	public static Map<String,String> generateParamsForUnifiedOrder(SlSysOrder slSysOrder, HttpServletRequest request) {
		String basePath = BaseService.getBasePath(request);
		Map<String, String> paramsMap = generateCommonParams();
		
		paramsMap.put("body", slSysOrder.getDescription());
		paramsMap.put("out_trade_no", slSysOrder.getOrderNo());
		paramsMap.put("total_fee", new BigDecimal(slSysOrder.getActualPayment()).movePointRight(2).toString());//订单总金额，单位为分
		logger.debug("请求微信下单的用户ip为：" + RequestUtil.getIpAddress(request));
		logger.debug("请求微信下单的用户ip为：" + RequestUtil.getIP(request));
		paramsMap.put("spbill_create_ip", RequestUtil.getIpAddress(request));//用户端实际ip
		Date curDate = new Date();
		paramsMap.put("time_start", DateUtils.formatDate(curDate, "yyyyMMddHHmmss"));//订单生成时间，格式为yyyyMMddHHmmss
		try {
			paramsMap.put("time_expire", DateUtils.formatDate(DateUtils.addTimeByStr(curDate, WeixinTradeConfig.timeoutExpress), "yyyyMMddHHmmss"));
		} catch (Exception e) {
			logger.error("订单过期时间设定格式错误!", e);
		}
		//订单失效时间，格式为yyyyMMddHHmmss
		paramsMap.put("notify_url", basePath + WeixinTradeConfig.notify_url);//接收微信支付异步通知回调地址
		paramsMap.put("trade_type", "APP");//交易类型,支付类型
		
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
		paramsMap.put("out_trade_no", wemallRefund.getOrderNo());
		paramsMap.put("total_fee", new BigDecimal(wemallRefund.getOrderPrice()).toString());//订单总金额，单位为分
		paramsMap.put("refund_fee", new BigDecimal(wemallRefund.getRefundFee()).toString());//订单总金额，单位为分
		paramsMap.put("refund_desc", wemallRefund.getRefundDesc());
		
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
}

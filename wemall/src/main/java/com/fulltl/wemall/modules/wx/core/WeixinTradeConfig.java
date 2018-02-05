package com.fulltl.wemall.modules.wx.core;

import com.fulltl.wemall.common.config.Global;

/* *
 *类名：WeixinConfig
 *功能：基础配置类
 *详细：设置帐户有关信息及返回路径
 */
public class WeixinTradeConfig {

	// ↓↓↓↓↓↓↓↓↓↓请在这里配置您的基本信息↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓

	// 应用ID,您的APPID，收款账号既是您的APPID对应账号
	public static String appid = Global.getConfig("weixin.trade.appid");

	// 微信支付分配的商户号
	public static String mch_id = Global.getConfig("weixin.trade.mch_id");

	// 商户密钥key
	public static String key = Global.getConfig("weixin.trade.key");
	
	// 商户签名方式（MD5签名方式，HMAC-SHA256签名方式）
	public static String sign_type = Global.getConfig("weixin.trade.sign_type");

	// 服务器异步通知页面路径 需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
	public static String notify_url = Global.getConfig("frontPath")
			+ "/interface/his/weixin/core/trade/notifyUrl";

	// 页面跳转同步通知页面路径 需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
	public static String return_url = Global.getConfig("frontPath")
			+ "/weixin/core/trade/returnUrl";

	// 商户pkcs12格式证书文件路径
	public static String pkcs12_cert_path = Global.getConfig("weixin.trade.pkcs12_certPath");

	// 订单超时时间，m-分钟，h-小时，d-天
	public static String timeoutExpress = "30m";
	
	// ↑↑↑↑↑↑↑↑↑↑请在这里配置您的基本信息↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑

	//微信统一下单接口链接
	public static final String UNIFIED_ORDER_URL = "https://api.mch.weixin.qq.com/pay/unifiedorder";
	//微信查询订单接口链接
	public static final String ORDER_QUERY_URL = "https://api.mch.weixin.qq.com/pay/orderquery";
	//微信关闭订单接口链接
	public static final String CLOSE_ORDER_URL = "https://api.mch.weixin.qq.com/pay/closeorder";
	//微信申请退款接口链接
	public static final String REFUND_URL = "https://api.mch.weixin.qq.com/secapi/pay/refund";
	//微信查询退款接口链接
	public static final String REFUND_QUERY_URL = "https://api.mch.weixin.qq.com/pay/refundquery";
	//微信下载对账单接口链接
	public static final String DOWNLOAD_BILL_URL = "https://api.mch.weixin.qq.com/pay/downloadbill";
	//微信拉取订单评价数据接口链接
	public static final String BATCH_QUERY_COMMENT_URL = "https://api.mch.weixin.qq.com/billcommentsp/batchquerycomment";
	
}

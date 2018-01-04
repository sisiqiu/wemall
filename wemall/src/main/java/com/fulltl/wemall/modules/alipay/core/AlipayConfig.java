package com.fulltl.wemall.modules.alipay.core;

import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.fulltl.wemall.common.config.Global;

/* *
 *类名：AlipayConfig
 *功能：基础配置类
 *详细：设置帐户有关信息及返回路径
 */
public class AlipayConfig {

	// ↓↓↓↓↓↓↓↓↓↓请在这里配置您的基本信息↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓

	// 应用ID,您的APPID，收款账号既是您的APPID对应支付宝账号
	public static String app_id = Global.getConfig("alipay.appId");

	// 商户私钥，您的PKCS8格式RSA2私钥
	public static String merchant_private_key = Global.getConfig("alipay.privateKey");

	// 支付宝公钥,查看地址：https://openhome.alipay.com/platform/keyManage.htm
	// 对应APPID下的支付宝公钥。
	public static String alipay_public_key = Global.getConfig("alipay.publicKey");

	// 服务器异步通知页面路径 需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
	public static String notify_url = Global.getConfig("frontPath")
			+ "/interface/his/alipay/core/trade/notifyUrl";

	// 页面跳转同步通知页面路径 需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
	public static String return_url = Global.getConfig("frontPath")
			+ "/alipay/core/trade/returnUrl";

	// 签名方式
	public static String sign_type = "RSA2";

	// 字符编码格式
	public static String charset = "UTF-8";

	// 支付宝网关
	public static String gatewayUrl = Global.getConfig("alipay.gatewayUrl");

	// 支付宝网关
	//public static String log_path = "E:\\eclipse-cas\\workspace\\AutoPatch\\logs";

	// 订单超时时间
	public static String timeoutExpress = "30m";
	
	// ↑↑↑↑↑↑↑↑↑↑请在这里配置您的基本信息↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑

	private static volatile AlipayClient alipayClient;// 沙箱下的网关
	
	/**
	 * 获取阿里客户端
	 * 
	 * @return
	 */
	public static AlipayClient getAlipayClient() {
		if (alipayClient == null) {
			synchronized (AlipayClient.class) {
				if (alipayClient == null) {
					alipayClient = new DefaultAlipayClient(gatewayUrl, app_id, merchant_private_key, "json", charset,
							alipay_public_key, sign_type);
				}
			}
		}
		return alipayClient;
	}

	/**
	 * 写日志，方便测试（看网站需求，也可以改成把记录存入数据库）
	 * 
	 * @param sWord
	 *            要写入日志里的文本内容
	 */
	/*public static void logResult(String sWord) {
		FileWriter writer = null;
		try {
			writer = new FileWriter(log_path + "alipay_log_" + System.currentTimeMillis() + ".txt");
			writer.write(sWord);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (writer != null) {
				try {
					writer.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}*/
}

package com.fulltl.wemall.modules.qq.core;

import com.fulltl.wemall.common.config.Global;

/**
 * QQ相关配置
 * 详细：设置账户信息及相关接口的请求路径
 * @author Administrator
 *
 */
public class QQConfig {
	
	public static String appId = Global.getConfig("qq.appId");
	public static String appKey = Global.getConfig("qq.appKey");
	
	//获取Authorization Code的请求地址，最初浏览器应请求这个地址，并将REDIRECT_URI设置为此控制器的接口。
	public static String getCodeUrl = "https://graph.qq.com/oauth2.0/authorize";
	public static String getCodeParamStr = "response_type=code&client_id=APP_ID&state=STATE&scope=get_user_info&display=PC&redirect_uri=REDIRECT_URI";
	
	//获取token的请求地址：WAP网站为https://graph.z.qq.com/moc2/token
	public static String getTokenUrl = "https://graph.qq.com/oauth2.0/token";
	public static String getTokenParamStr = "grant_type=authorization_code&client_id=APP_ID&client_secret=APP_KEY&code=CODE&redirect_uri=REDIRECT_URI"; //REDIRECT_URI与上一步一致

	//获取openId的请求地址：WAP网站为https://graph.z.qq.com/moc2/me
	public static String getOpenIdUrl = "https://graph.qq.com/oauth2.0/me";
	public static String getOpenIdParamStr = "access_token=ACCESS_TOKEN"; 

	//获取用户信息user_info的请求地址：
	public static String getUserInfoUrl = "https://graph.qq.com/user/get_user_info";
	public static String getUserInfoParamStr = "access_token=ACCESS_TOKEN&oauth_consumer_key=APP_ID&openid=OPENID"; 
	
}

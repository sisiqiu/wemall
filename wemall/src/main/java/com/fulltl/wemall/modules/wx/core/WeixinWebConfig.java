package com.fulltl.wemall.modules.wx.core;

import com.fulltl.wemall.common.config.Global;

/**
 * 微信相关配置
 * 详细：设置账户信息及相关接口的请求路径
 * @author Administrator
 *
 */
public class WeixinWebConfig {
	
	public static String appId = Global.getConfig("weixin.web.appId");
	public static String appSecret = Global.getConfig("weixin.web.appSecret");
	
	//获取Code的请求地址，最初浏览器应请求这个地址，并将REDIRECT_URI设置为此控制器的接口。
	//此过程可以通过微信的js进行。
	public static String getCodeUrl = "https://open.weixin.qq.com/connect/qrconnect";
	public static String getCodeParamStr = "appid=APPID&redirect_uri=REDIRECT_URI&response_type=code&scope=snsapi_login&state=STATE#wechat_redirect";
	
	//获取token的请求地址，同时也会获取openid
	public static String getTokenUrl = "https://api.weixin.qq.com/sns/oauth2/access_token";
	public static String getTokenParamStr = "appid=APP_ID&secret=APP_SECRET&code=CODE&grant_type=authorization_code";

	//获取用户信息user_info的请求地址
	public static String getUserInfoUrl = "https://api.weixin.qq.com/sns/userinfo";
	public static String getUserInfoParamStr = "access_token=ACCESS_TOKEN&openid=OPENID"; 
	
	//获取token的请求地址（根据refresh_token刷新token）
	public static String getTokenByRefTokenUrl = "https://api.weixin.qq.com/sns/oauth2/refresh_token";
	public static String getTokenByRefTokenParamStr = "appid=APPID&grant_type=refresh_token&refresh_token=REFRESH_TOKEN"; 
	
}

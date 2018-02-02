package com.fulltl.wemall.modules.wx.core.utils.accessToken;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.fulltl.wemall.modules.wx.core.pojo.OAuth2AccessToken;
import com.fulltl.wemall.modules.wx.core.pojo.WXOAuthUserInfo;


/**
 * 微信OAuth2网页授权工具类
 * @author Administrator
 *
 */
public class WXOAuth2AuthorizeUtil {
	private static Logger logger = LoggerFactory.getLogger(WXUtil.class);
	
	/**
     * oauth2授权获取openId，需要替换APPID，REDIRECT_URI，STATE（可选带参）
     */
	private final static String oauth2_authorize_url_base = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=APPID&redirect_uri=REDIRECT_URI&response_type=code&scope=snsapi_base&state=STATE#wechat_redirect";
    /**
     * oauth2授权获取用户详情，需要替换APPID，REDIRECT_URI，STATE（可选带参）
     */
	private final static String oauth2_authorize_url_userinfo = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=APPID&redirect_uri=REDIRECT_URI&response_type=code&scope=snsapi_userinfo&state=STATE#wechat_redirect";
    
    /**
     * oauth2授权获取网页授权access_token以及openId的url，需要替换APPID，SECRET，CODE
     */
	private final static String oauth2_access_token_url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code";
    /**
     * 使用网页授权接口调用凭证access_token获取用户信息，此access_token与基础支持的access_token不同。
     * 需要替换ACCESS_TOKEN，OPENID
     */
	private final static String user_info_url = "https://api.weixin.qq.com/sns/userinfo?access_token=ACCESS_TOKEN&openid=OPENID";
    
    /**
     * 获取snsapi_base方式回调授权页面的url，用于重定向
     * @param appId
     * @param redirectUri 回调到服务端用于接收code的接口，最好路径上带参数
     * @return
     */
    public static String getBaseAuthorizeUrl(String appId, String redirectUri, String state) {
    	return oauth2_authorize_url_base.replace("APPID", appId)
    									.replace("REDIRECT_URI", redirectUri)
    									.replace("STATE", state);
    }
    
    /**
     * 获取snsapi_userinfo方式回调授权页面的url，用于重定向
     * @param appId
     * @param redirectUri 回调到服务端用于接收code的接口
     * @return
     */
    public static String getUserInfoAuthorizeUrl(String appId, String redirectUri, String state) {
    	return oauth2_authorize_url_userinfo.replace("APPID", appId)
    										.replace("REDIRECT_URI", redirectUri)
    										.replace("STATE", state);
    }
    
    /**
     * 获取openId和accessToken。
     * 返回格式：
     * 	{
		    "access_token": "OezXcEiiBSKSxW0eoylIeAsR0GmYd1awCffdHgb4fhS_KKf2CotGj2cBNUKQQvj-G0ZWEE5-uBjBz941EOPqDQy5sS_GCs2z40dnvU99Y5AI1bw2uqN--2jXoBLIM5d6L9RImvm8Vg8cBAiLpWA8Vw",
		    "expires_in": 7200,
		    "refresh_token": "OezXcEiiBSKSxW0eoylIeAsR0GmYd1awCffdHgb4fhS_KKf2CotGj2cBNUKQQvj-G0ZWEE5-uBjBz941EOPqDQy5sS_GCs2z40dnvU99Y5CZPAwZksiuz_6x_TfkLoXLU7kdKM2232WDXB3Msuzq1A",
		    "openid": "oLVPpjqs9BhvzwPj5A-vTYAX3GLc",
		    "scope": "snsapi_userinfo,"
		}
     * @param appId
     * @param appSecret
     * @param code
     * @return
     * @throws Exception 
     */
    public static OAuth2AccessToken getOpenIdAndAccessToken(String appId, String secret, String code) throws Exception {
    	String obtainOAuth2AccessTokenUrl = oauth2_access_token_url.replace("APPID", appId)
    																.replace("SECRET", secret)
    																.replace("CODE", code);
    	//请求obtainOAuth2AccessTokenUrl获取返回的数据。并转为OAuth2AccessToken对象
		return JSONObject.parseObject(WXUtil.getStrByHttpRequest(obtainOAuth2AccessTokenUrl, "GET", null), OAuth2AccessToken.class);
    }
    
    /**
     * 获取微信用户信息
     * @param oauth2AccessToken
     * @param openId
     * @return
     * @throws Exception 
     */
    public static WXOAuthUserInfo getUserInfo(String oauth2AccessToken, String openId) throws Exception {
    	String obtainWXUserInfoUrl = user_info_url.replace("ACCESS_TOKEN", oauth2AccessToken)
    												.replace("OPENID", openId);
    	//请求obtainWXUserInfoUrl获取用户信息数据，并转为WXUserInfo对象
    	return JSONObject.parseObject(WXUtil.getStrByHttpRequest(obtainWXUserInfoUrl, "GET", null), WXOAuthUserInfo.class);
    }
}

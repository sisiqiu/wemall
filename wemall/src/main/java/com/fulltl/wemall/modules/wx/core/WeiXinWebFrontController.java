package com.fulltl.wemall.modules.wx.core;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import com.google.common.collect.Maps;
import com.fulltl.wemall.common.web.BaseController;
import com.fulltl.wemall.modules.sys.entity.User;
import com.fulltl.wemall.modules.sys.service.SystemService;
import com.fulltl.wemall.modules.wx.core.pojo.WXOAuthUserInfo;

/**
 * 微信网页前台控制器。包含微信网页第三方授权登陆接口。
 * 
 * @author ldk
 * @version 2017-10-27
 */
@Controller
@RequestMapping(value = "${frontPath}/wx/core/web")
public class WeiXinWebFrontController extends BaseController {

	private Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	private SystemService systemService;
	
	@RequestMapping(value = "returnUrl")
	public String returnUrl(HttpServletRequest request, HttpServletResponse response, Model model) throws UnsupportedEncodingException {
		// Authorization Code
		String code = WebUtils.getCleanParam(request, "code");
		// 原始的state值
		String state = WebUtils.getCleanParam(request, "state");
		// 授权成功后要跳转的应用url
		String myRedirectUrl = WebUtils.getCleanParam(request, "myRedirectUrl");
		
		//验证是否获取权限code
		if(StringUtils.isBlank(code)) {
			logger.error("微信第三方授权获取到的code为空！");
			return myRedirectUrl;
		}
		
		//获取access_token
		Map<String, Object> getAtAndOpenIdMap = getAccessTokenAndOpenIdProcess(code);
		String access_token = getAtAndOpenIdMap.get("access_token").toString();
		String scope = getAtAndOpenIdMap.get("scope").toString();
		String openid = getAtAndOpenIdMap.get("openid").toString();
		String refresh_token = getAtAndOpenIdMap.get("refresh_token").toString();
		
		Map<String, Object> retMap = Maps.newHashMap();
		// 根据获取的授权的范围，判断是否要取用户信息，并进行获取系统用户登陆。
		if("snsapi_base".equals(scope)) {
			//只获取基本信息user_id，用户信息不获取
			retMap = getUserByOpenId(openid);
		} else if("snsapi_userinfo".equals(scope)) {
			retMap = getUserInfoProcess(access_token, openid);
		} else {
			logger.debug("非正常的scope值为：" + scope);
		}
		if(!"200".equals(retMap.get("ret"))) {
			logger.error(retMap.get("retMsg").toString());
		}
		logger.info("重定向的网页为：" + myRedirectUrl);
		return "redirect:"+myRedirectUrl;
	}

	//--------------获取access_token，openId部分begin-----------------------------//
	/**
	 * 获取access_token的过程，返回获取的access_token
	 * @param code
	 * @return 若ret为200，则获取成功，将包含access_token，openid，refresh_token，scope信息
	 */
	private Map<String, Object> getAccessTokenAndOpenIdProcess(String code) {
		Map<String, Object> retMap = Maps.newHashMap();
		//测试可否用HttpClient获取access_token，如果不能，则跳转。
		HttpClient clientForToken = new HttpClient(); 
		// 使用 GET 方法 ，如果服务器需要通过 HTTPS 连接，那只需要将下面 URL 中的 http 换成 https
		HttpMethod methodForToken = new GetMethod(WeixinWebConfig.getTokenUrl + "?" + WeixinWebConfig.getTokenParamStr
																.replace("APP_ID", WeixinWebConfig.appId)
																.replace("APP_SECRET", WeixinWebConfig.appSecret)
																.replace("CODE", code));
		String access_token = ""; //定义获取到的access_token
		String openid = "";
		String refresh_token = "";
		String scope = ""; //用户授权的作用域
		try {
			clientForToken.executeMethod(methodForToken);
			// 打印服务器返回的状态
			logger.info("获取access_token，openid过程：" + methodForToken.getStatusLine().toString());
			// 打印返回的信息，获取到access_token和expires_in；接口调用有错误时，会返回code和msg字段
			logger.info("获取access_token，openid过程：" + methodForToken.getResponseBodyAsString());
			//OAuth2AccessToken
			//access_token = "";
			//openid = "";
			//refresh_token = "";
			//scope = ""; //用户授权的作用域
			retMap.put("ret", "200");
			retMap.put("retMsg", "获取成功！");
			retMap.put("access_token", access_token);
			retMap.put("openid", openid);
			retMap.put("refresh_token", refresh_token);
			retMap.put("scope", scope);
		} catch (HttpException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			// 释放连接
			methodForToken.releaseConnection();
		}
		return retMap;
	}

	//--------------获取access_token，openId部分end-------------------------------//
	//--------------获取userInfo部分begin-----------------------------------------//

	/**
	 * 根据access_token和openid获取用户信息的过程，并执行添加对应的系统用户和登陆操作的过程。
	 * @param access_token
	 * @param openid
	 */
	private Map<String, Object> getUserInfoProcess(String access_token, String openid) {
		Map<String, Object> retMap = Maps.newHashMap();
		//校验数据
		if(StringUtils.isBlank(access_token)) {
			retMap.put("ret", "-1");
			retMap.put("retMap", "access_token不能为空！");
			return retMap;
		}
		if(StringUtils.isBlank(openid)) {
			retMap.put("ret", "-1");
			retMap.put("retMap", "openid不能为空！");
			return retMap;
		}
		
		boolean getUserInfoSuccess = true; //标识获取用户信息成功
		
		//测试可否用HttpClient获取openId
		HttpClient clientForUserInfo = new HttpClient();
		// 使用 GET 方法 ，如果服务器需要通过 HTTPS 连接，那只需要将下面 URL 中的 http 换成 https
		HttpMethod methodForUserInfo = new GetMethod(WeixinWebConfig.getUserInfoUrl + "?" + WeixinWebConfig.getUserInfoParamStr
																.replace("ACCESS_TOKEN", access_token)
																.replace("OPENID", openid));
		try {
			clientForUserInfo.executeMethod(methodForUserInfo);
			// 打印服务器返回的状态
			logger.info("获取userInfo过程：" + methodForUserInfo.getStatusLine().toString());
			// 打印返回的信息，获取到client_id和openid；接口调用有错误时，会返回code和msg字段
			logger.info("获取userInfo过程：" + methodForUserInfo.getResponseBodyAsString());
			//WXOAuthUserInfo
			
			//判断调用接口是否有错误，如果有错，将getUserInfoSuccess设置为false；如果没错，构造WXOAuthUserInfo，填充相关信息，添加用户。
			/*if(接口调用失败) {
				logger.info("getUserInfo调用失败");
				getUserInfoSuccess = false;
			} else {
				logger.info("getUserInfo调用成功");
				getUserInfoSuccess = true;
		    	Map userInfoMap = new Gson().fromJson(methodForUserInfo.getResponseBodyAsString(), Map.class);
		    	WXOAuthUserInfo wxOAuthUserInfo = WXOAuthUserInfo.valueOf(userInfoMap);
		    	User u = systemService.quickGetUserBy(wxOAuthUserInfo);
		    	retMap = systemService.loginByUser(u);
			}*/
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("获取用户userInfo过程出错！错误信息：" + e.getMessage());
			getUserInfoSuccess = false;
		} finally {
			// 释放连接
			methodForUserInfo.releaseConnection();
		}
		
		if(!getUserInfoSuccess) {
			//获取用户详情未成功，使用基础openid添加系统用户。
			retMap = getUserByOpenId(openid);
		}
		
		return retMap;
	}
	//--------------获取userInfo部分end-------------------------------------------//

	/**
	 * 使用基础的openid获取或添加系统用户，并登陆
	 * @param openid
	 * @return
	 */
	private Map<String, Object> getUserByOpenId(String openid) {
		WXOAuthUserInfo wxOAuthUserInfo = new WXOAuthUserInfo(openid);
		User u = systemService.quickGetUserBy(wxOAuthUserInfo);
		Map<String, Object> retMap = Maps.newHashMap();
		try {
			retMap = systemService.loginByUser(u, false);
		} catch (Exception e1) {
			logger.error("登陆出错", e1);
		}
    	return retMap;
	}
}
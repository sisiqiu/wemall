package com.fulltl.wemall.modules.alipay.core;

import java.io.UnsupportedEncodingException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alipay.api.AlipayApiException;
import com.alipay.api.request.AlipaySystemOauthTokenRequest;
import com.alipay.api.request.AlipayUserInfoShareRequest;
import com.alipay.api.response.AlipaySystemOauthTokenResponse;
import com.alipay.api.response.AlipayUserInfoShareResponse;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.fulltl.wemall.common.config.Global;
import com.fulltl.wemall.common.web.BaseController;
import com.fulltl.wemall.modules.alipay.core.pojo.AlipayOAuthUserInfo;
import com.fulltl.wemall.modules.sys.entity.User;
import com.fulltl.wemall.modules.sys.service.SystemService;
import com.fulltl.wemall.modules.wx.core.pojo.WXOAuthUserInfo;

/**
 * 支付宝前台控制器。包含支付宝第三方授权登陆接口。
 * 
 * @author ldk
 * @version 2017-10-26
 */
@Controller
@RequestMapping(value = "${frontPath}/alipay/core/web")
public class AlipayWebFrontController extends BaseController {

	private Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	private SystemService systemService;
	
	@RequestMapping(value = "returnUrl")
	public String returnUrl(HttpServletRequest request, HttpServletResponse response, Model model) throws UnsupportedEncodingException, AlipayApiException {
		// 支付宝用户号
		String app_id = WebUtils.getCleanParam(request, "app_id");
		// 获取第三方登录授权
		String alipay_app_auth = WebUtils.getCleanParam(request, "source");
		// 第三方授权code
		String app_auth_code = WebUtils.getCleanParam(request, "auth_code");
		// 获取授权的范围scope
		String scope = WebUtils.getCleanParam(request, "scope");
		// state
		String state = WebUtils.getCleanParam(request, "state");
		// 授权成功后要跳转的url
		String myRedirectUrl = WebUtils.getCleanParam(request, "myRedirectUrl");
		
		//验证是否获取权限code
		if(StringUtils.isBlank(app_auth_code)) {
			return myRedirectUrl;
		}
		
		Map<String, Object> retMap = Maps.newHashMap();
		// 使用auth_code换取接口access_token及用户userId
		AlipaySystemOauthTokenRequest oauthTokenRequest = new AlipaySystemOauthTokenRequest();
		oauthTokenRequest.setCode(app_auth_code);
		oauthTokenRequest.setGrantType("authorization_code");
		// 第三方授权
		AlipaySystemOauthTokenResponse responseToken = AlipayConfig.getAlipayClient().execute(oauthTokenRequest);
		if (responseToken.isSuccess()) {
			logger.info("oauthTokenRequest调用成功");
			logger.info("accessToken=" + responseToken.getAccessToken());
			logger.info("userId=" + responseToken.getUserId());
			retMap = handleWithOauthTokenResp(responseToken, scope);
		} else {
			logger.error("oauthTokenRequest调用失败");
		}
		if(!"200".equals(retMap.get("ret"))) {
			logger.error(retMap.get("retMsg").toString());
		}
		
		logger.info("重定向的页面为：" + myRedirectUrl);
		return "redirect:"+myRedirectUrl;
	}

	/**
	 * 根据授权范围scope，处理alipay.system.oauth.token接口返回的结果。并添加对应系统用户和登陆。
	 * @param responseToken alipay.system.oauth.token接口返回的结果
	 * @param scope 授权范围scope。auth_base--基础用户id；auth_user--用户信息。
	 * @return 处理的结果，key分别为ret和retMsg，value分别为编码（200为正确，其余为错误）和提示信息。
	 */
	private Map<String, Object> handleWithOauthTokenResp(AlipaySystemOauthTokenResponse responseToken, String scope) {
		Map<String, Object> retMap = Maps.newHashMap();
		
		// 根据获取的授权的范围，判断是否要取用户信息。
		if("auth_base".equals(scope)) {
			//只获取基本信息user_id，用户信息不获取
	    	retMap = getUserByUserId(responseToken.getUserId());
		} else if("auth_user".equals(scope)) {
			//通过access_token调用用户信息共享接口获取用户信息
			//获取用户信息
			String auth_token = responseToken.getAccessToken();
			try {
				AlipayUserInfoShareRequest userInfoRequest = new AlipayUserInfoShareRequest();
			    AlipayUserInfoShareResponse userinfoShareResponse = AlipayConfig.getAlipayClient().execute(userInfoRequest, auth_token);
			    if(userinfoShareResponse.isSuccess()) {
			    	logger.info("userInfoRequest调用成功");
			    	Map userInfoMap = new Gson().fromJson(userinfoShareResponse.getBody(), Map.class);
			    	AlipayOAuthUserInfo alipayOAuthUserInfo = AlipayOAuthUserInfo.valueOf((Map)userInfoMap.get("alipay_user_info_share_response"));
			    	User u = systemService.quickGetUserBy(alipayOAuthUserInfo);
			    	try {
						retMap = systemService.loginByUser(u, false);
					} catch (Exception e) {
						logger.error("登陆出错", e);
					}
			    } else {
			    	logger.error("userInfoRequest调用失败");
			    	retMap = getUserByUserId(responseToken.getUserId());
			    }
			} catch (AlipayApiException e) {
			    //处理异常
			    e.printStackTrace();
			}
		} else {
			logger.debug("非正常的scope值为：" + scope);
		}
		
		return retMap;
	}
	
	/**
	 * 使用基础的user_id获取或添加系统用户，并登陆
	 * @param user_id
	 * @return
	 */
	private Map<String, Object> getUserByUserId(String user_id) {
		AlipayOAuthUserInfo alipayOAuthUserInfo = new AlipayOAuthUserInfo(user_id);
		User u = systemService.quickGetUserBy(alipayOAuthUserInfo);
		Map<String, Object> retMap = Maps.newHashMap();
		try {
			retMap = systemService.loginByUser(u, false);
		} catch (Exception e1) {
			logger.error("登陆出错", e1);
		}
    	return retMap;
	}
}
/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.fulltl.wemall.modules.wx.core;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.fulltl.wemall.common.config.Global;
import com.fulltl.wemall.common.utils.CacheUtils;
import com.fulltl.wemall.common.utils.StringUtils;
import com.fulltl.wemall.common.web.BaseController;
import com.fulltl.wemall.modules.wx.core.pojo.AccessToken;
import com.fulltl.wemall.modules.wx.core.pojo.Button;
import com.fulltl.wemall.modules.wx.core.pojo.FirstLevelButton;
import com.fulltl.wemall.modules.wx.core.pojo.Menu;
import com.fulltl.wemall.modules.wx.core.pojo.OAuth2AccessToken;
import com.fulltl.wemall.modules.wx.core.pojo.SecondLevelButton;
import com.fulltl.wemall.modules.wx.core.pojo.WXOAuthUserInfo;
import com.fulltl.wemall.modules.wx.core.utils.SignUtil;
import com.fulltl.wemall.modules.wx.core.utils.accessToken.ObtainAccessTokenScheduler;
import com.fulltl.wemall.modules.wx.core.utils.accessToken.WXOAuth2AuthorizeUtil;
import com.fulltl.wemall.modules.wx.core.utils.accessToken.WXUtil;
import com.fulltl.wemall.modules.wx.entity.UserBehavior;
import com.fulltl.wemall.modules.wx.entity.WxServiceaccount;
import com.fulltl.wemall.modules.wx.entity.WxSubscriber;
import com.fulltl.wemall.modules.wx.entity.WxWechatMenu;
import com.fulltl.wemall.modules.wx.service.WxServiceaccountService;
import com.fulltl.wemall.modules.wx.service.WxSubscriberService;
import com.fulltl.wemall.modules.wx.service.WxWechatMenuService;

/**
 * 微信前台控制器。包含微信核心相关与公众号通讯的接口，以及用户可见的部分接口。
 * 
 * @author ldks
 * @version 2017-10-11
 */
@Controller
@RequestMapping(value = "${frontPath}/wx/core")
public class WeiXinFrontController extends BaseController {

	private Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	private ProcessRequest processRequest;
	@Autowired
	private WxWechatMenuService wxWechatMenuService;
	@Autowired
	private WxServiceaccountService wxServiceaccountService;
	@Autowired
	private ObtainAccessTokenScheduler obtainAccessTokenScheduler;
	@Autowired
	private WxSubscriberService wxSubscriberService;

	/**
	 * 服务端接入微信初始握手认证接口
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET)
	@ResponseBody
	public String signup(HttpServletRequest request, HttpServletRequest response) {
		// 微信加密签名
		String signature = WebUtils.getCleanParam(request, "signature");
		// 时间戳
		String timestamp = WebUtils.getCleanParam(request, "timestamp");
		// 随机数
		String nonce = WebUtils.getCleanParam(request, "nonce");
		// 随机字符串
		String echostr = WebUtils.getCleanParam(request, "echostr");
		// 通过检验signature对请求进行校验，若校验成功则原样返回echostr，表示接入成功，否则接入失败
		if (SignUtil.checkSignature(signature, timestamp, nonce)) {
			logger.info("微信服务端接入成功！");
			obtainAccessTokenScheduler.obtainAccessToken();
		}
		return echostr;
	}

	/**
	 * 微信各种消息的接收接口。
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	public String handleMsgs(HttpServletRequest request, HttpServletResponse response) {
		// 调用核心业务类接收消息、处理消息
		String respMessage = StringUtils.EMPTY;
		try {
			respMessage = processRequest.process(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return respMessage;
	}

	/**
	 * 接收要进行微信网页授权的页面跳转。
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "redirectWithAuthorize")
	@ResponseBody
	public void redirectWithWXAuth2(HttpServletRequest request, HttpServletResponse response) {
		//获取授权成功后要重定向的视图
		String redirect = WebUtils.getCleanParam(request, "redirect");
		String serviceId = WebUtils.getCleanParam(request, "serviceId");
		//获取要授权的类型，有两种：1--使用userInfo接口。其他--使用openId接口。
		String getUserInfo = WebUtils.getCleanParam(request, "getUserInfo");
		//System.err.println("serviceId=" + serviceId + "; getUserInfo=" + getUserInfo);
		
		String url = StringUtils.EMPTY;
		if(getUserInfo.equals("1")) {
			//获取snsapi_userinfo方式回调授权页面的url，用于重定向到微信授权。
			url = WXOAuth2AuthorizeUtil.getUserInfoAuthorizeUrl(Global.getConfig("weixin.appId"), 
					Global.getConfig("weixin.appUrl") + "/" + adminPath + "/wx/core/obtainCode/" + getUserInfo + "?redirect=" + redirect,
					serviceId);
		} else {
			//获取snsapi_base方式回调授权页面的url，用于重定向到微信授权。
			url = WXOAuth2AuthorizeUtil.getBaseAuthorizeUrl(Global.getConfig("weixin.appId"), 
					Global.getConfig("weixin.appUrl") + "/" + adminPath + "/wx/core/obtainCode/" + getUserInfo + "?redirect=" + redirect,
					serviceId);
		}
		
		try {
			response.sendRedirect(url);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 获取网页授权code的接口，并进行获取openId，获取用户信息。
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "obtainCode/{getUserInfo}")
	public String obtainCode(HttpServletRequest request, HttpServletResponse response, @PathVariable("getUserInfo") String getUserInfo, Model model) {
		String code = WebUtils.getCleanParam(request, "code");
		//要进行跳转的url
		String redirect = WebUtils.getCleanParam(request, "redirect");
		String serviceId = WebUtils.getCleanParam(request, "state");
		
		//System.err.println("code=" + code + " redirect=" + redirect + "; state=" + serviceId + "; getUserInfo=" + getUserInfo);
		OAuth2AccessToken oauth2AccessToken = null;
		WXOAuthUserInfo userInfo = null;
		try {
			//获取openId和accessToken
			oauth2AccessToken = WXOAuth2AuthorizeUtil.getOpenIdAndAccessToken(Global.getConfig("weixin.appId"), Global.getConfig("weixin.secret"), code);
			//将用户openId放入model中
			String openId = oauth2AccessToken.getOpenid();
			//跟要授权的类型getUserInfo判断是否获取用户信息。
			if(getUserInfo.equals("1")) {
				//需要获取用户信息
				//获取用户信息
				userInfo = WXOAuth2AuthorizeUtil.getUserInfo(oauth2AccessToken.getAccess_token(), oauth2AccessToken.getOpenid());
				//将用户信息写入数据库或放入model中
				//根据openId查询关注用户表，若查到了，则更新其中的信息字段后执行关注行为。若没有查到，新建一个，执行关注行为
				//serviceId根据之前取appId时取的那个服务号的serviceId。暂时写死为1
				WxSubscriber curSubscriber = wxSubscriberService.getWxSubscriberBy(openId, serviceId);
				curSubscriber.initByOAuthUserInfo(userInfo);
				//更新微信用户对应的 关注表
				wxSubscriberService.updateWXUserBy(UserBehavior.FOCUS_ON, curSubscriber, null);
				
				model.addAttribute("userInfo", userInfo);
			} else {
				//不需要获取用户信息，只需要获取openId即可。
				model.addAttribute("openId", openId);
			}
			
			model.addAttribute("serviceId", serviceId);
			//System.err.println(JSONObject.toJSONString(userInfo));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return redirect;
	}
	
	/**
	 * 微信网页授权，用于获取code，并返回获取openId的结果信息
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "obtainCodeForOpenId")
	@ResponseBody
	public String obtainCode(HttpServletRequest request, HttpServletResponse response, Model model) {
		String ret = "400";
        String retMsg = "操作失败";
		Map<String, Object> retMap = new HashMap<String, Object>();
		
		String code = WebUtils.getCleanParam(request, "code");
		String serviceId = WebUtils.getCleanParam(request, "state");
		String redirect = WebUtils.getCleanParam(request, "redirect"); /////////////
		OAuth2AccessToken oauth2AccessToken = null;
		String openId = StringUtils.EMPTY;
		try {
			//获取openId和accessToken
			oauth2AccessToken = WXOAuth2AuthorizeUtil.getOpenIdAndAccessToken(Global.getConfig("weixin.appId"), Global.getConfig("weixin.secret"), code);
			openId = oauth2AccessToken.getOpenid();
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(openId.equals(StringUtils.EMPTY)) {
			retMap.put("ret", ret);
			retMap.put("retMsg", retMsg);
		} else {
			//retMap.put("ret", "200");
			//retMap.put("retMsg", "获取成功！");
			retMap.put("openId", openId);
			String redirectUrl = Global.getConfig("weixin.appUrl") + redirect;
			//System.err.println(redirectUrl);
			try {
				if(redirectUrl.contains("?")) {
					response.sendRedirect(redirectUrl + "&openId=" + openId);
				} else {
					response.sendRedirect(redirectUrl + "?openId=" + openId);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return new Gson().toJson(retMap);
	}
	
	/**
	 * 用于获取当前openId接口，需要传参serviceId。redirect。
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "getOpenId")
	@ResponseBody
	public String getOpenId(HttpServletRequest request, HttpServletResponse response) {
		String ret = "400";
        String retMsg = "操作失败";
		Map<String, Object> retMap = new HashMap<String, Object>();
		
		String serviceId = WebUtils.getCleanParam(request, "serviceId");
		String redirect = WebUtils.getCleanParam(request, "redirect");
		//System.err.println("+================getOpenId" + serviceId);
		if(StringUtils.isNotEmpty(serviceId)) {
			//获取snsapi_userinfo方式回调授权页面的url，用于重定向到微信授权。
			String url = WXOAuth2AuthorizeUtil.getBaseAuthorizeUrl(Global.getConfig("weixin.appId"), 
					Global.getConfig("weixin.appUrl") + "/" + frontPath + "/wx/core/obtainCodeForOpenId?redirect=" + redirect, /////////////
					serviceId);
			try {
				response.sendRedirect(url);
			} catch (IOException e) {
				e.printStackTrace();
				retMap.put("ret", ret);
				retMap.put("retMsg", retMsg);
				return new Gson().toJson(retMap);
			}
			return "";
		} else {
			retMap.put("ret", "433");
			retMap.put("retMsg", "缺少服务号Id:serviceId");
			return new Gson().toJson(retMap);
		}
	}

}
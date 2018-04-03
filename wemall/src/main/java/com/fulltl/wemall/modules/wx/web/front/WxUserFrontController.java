package com.fulltl.wemall.modules.wx.web.front;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fulltl.wemall.common.sms.sendmode.ccp.SMSVerify;
import com.fulltl.wemall.modules.sys.entity.User;
import com.fulltl.wemall.modules.sys.security.UsernamePasswordToken;
import com.fulltl.wemall.modules.sys.service.SystemService;
import com.fulltl.wemall.modules.sys.utils.UserUtils;
import com.fulltl.wemall.modules.wx.entity.UserBehavior;
import com.fulltl.wemall.modules.wx.entity.WxUserInfo;
import com.fulltl.wemall.modules.wx.service.WxUserInfoService;
import com.fulltl.wemall.test.entity.TestData;
import com.google.common.collect.Maps;
import com.google.gson.Gson;

/**
 * 前端微信用户绑定控制器
 * @author ldk
 * @version 2015-04-06
 */
@Controller
@RequestMapping(value = "${frontPath}/wx/user")
public class WxUserFrontController {
	@Autowired
	private SystemService systemService;
	@Autowired
	private WxUserInfoService wxUserInfoService;
	
	@RequestMapping(value = "bindUser")
	@ResponseBody
	public String bindUser(TestData testData, HttpServletRequest request, HttpServletResponse response, Model model) {
		String ret = "400";
        String retMsg = "操作失败";
		Map<String, Object>  retMap = new HashMap<String, Object>();
		/*String mobile = WebUtils.getCleanParam(request, "mobile");
        String verifyServID = WebUtils.getCleanParam(request, "verifyServID");
        String sms_code = WebUtils.getCleanParam(request, "sms_code");
        String openId = WebUtils.getCleanParam(request, "openId");
        String serviceId = WebUtils.getCleanParam(request, "serviceId");
        
        SMSVerify sms = new SMSVerify();
        User user = null;
        UsernamePasswordToken userToken = new UsernamePasswordToken();
        try {
        	//验证短信验证码
            if (sms.checkVerifyCode(mobile, sms_code, verifyServID, false).equals("0")) {
            	//短信验证码验证通过
            	*//**
            	 *  如果不允许多个微信号绑定同一个系统用户，则可以根据手机号查询微信绑定用户表，
            	 *  看存在与否，不存在，则允许绑定，否则提示该手机已绑定过微信号。
            	 *  现在，暂定允许多个微信号绑定同一个系统用户。
            	 *//*
                user = systemService.quickGetUserByMobileForWX(mobile);
                // 自动登录
                userToken.setUsername(user.getMobile());
                userToken.setPassword("123456".toCharArray());
                // 短信验证码登陆，跳过密码对比校验
                userToken.setLogin_type("SMSCode");
                UserUtils.getSubject().login(userToken);

                //执行绑定用户
                WxUserInfo curWxUserInfo = wxUserInfoService.getWxUserInfoBy(openId, serviceId);
                wxUserInfoService.updateWXUserInfoBy(UserBehavior.BIND, curWxUserInfo, user);
                
                ret = "200";
                retMsg = "操作成功";
                retMap.put("loginname", user.getLoginName());
                retMap.put("password", user.getPassword());
            } else {
                ret = "204";
                retMsg = "短信验证码错误！请重试！";
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        retMap.put("ret", ret);
        retMap.put("retMsg", retMsg);*/
		return new Gson().toJson(retMap);
	}

	@RequestMapping(value = "loginByWxOpenId")
	@ResponseBody
	public String loginByWxOpenId(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> retMap = Maps.newHashMap();
		String openId = WebUtils.getCleanParam(request, "openId");
		if(StringUtils.isBlank(UserUtils.getUser().getId())) {
			//未登陆，才进行登陆。
			retMap = wxUserInfoService.loginByOpenId(openId);
		} else {
			retMap.put("ret", "200");
			retMap.put("retMsg", "用户之前已登陆，故未进行openId登陆！");
		}
		System.out.println("当前用户：" + UserUtils.getUser());
		return new Gson().toJson(retMap);
	}
}

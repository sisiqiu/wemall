/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.fulltl.wemall.common.servlet;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.fulltl.wemall.common.sms.sendmode.ccp.SMSVerify;

import org.apache.commons.lang3.StringUtils;

/**
 * 获取短信验证码
 * @author KangYang
 * @version 2017-09-25
 */
@SuppressWarnings("serial")
public class SMSCodeServlet extends HttpServlet {
    
    private static final long serialVersionUID = 1L;
    private Logger logger = LoggerFactory.getLogger(getClass());
	
	public static final String SMS_CODE = "getSMSCode";
	
	
	public SMSCodeServlet() {
		super();
	}
	
	public void destroy() {
		super.destroy(); 
	}
	

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String mobile = request.getParameter("mobile"); // AJAX验证，成功返回true
		if (StringUtils.isNotBlank(mobile)){
			response.getOutputStream().print(getSMSCode(mobile));
		}
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
	    String mobile = request.getParameter("mobile");
	    if (StringUtils.isNotBlank(mobile)){
            response.getOutputStream().print(getSMSCode(mobile));
        }
	}
	
	 /**
	  * 发送验证码
	  */
	 public String getSMSCode(String mobile) {
	     Map<String,String> retMap = new HashMap<String, String>();
	     String ret = "-1";
	     String retMsg = "操作成功";
	     Gson gs = new Gson();
	     // 短信发送
	     SMSVerify sms = new SMSVerify();
	     String[] contentArray = { "[VerifyCode]", "5分钟" }; // 定义5分钟内有效

	     // 发送短信: TODO 应将该校验串缓存 10 分钟，以便与客户端的请求参数进行比较??
	     // 如果是新注册用户，直接发送
	     try {
	        String mobileMessageServ = sms.sendSmsVerifyCode(mobile, 4, "204863", contentArray);
	        String[] messageServ = mobileMessageServ.split("\\|");
	        if (messageServ != null && messageServ[0].equals("0")) {
	            ret = "0";
	            retMsg = "操作成功";
	            retMap.put("mobile", mobile);
	            retMap.put("verifyID", messageServ[1]);// 服务器缓存的验证码id
	        } 
	    } catch (Exception e) {
	        logger.error("短信发送失败", e);
	        e.printStackTrace();
	    }
	     retMap.put("ret", ret);
	     retMap.put("retMsg", retMsg);
	     
	     return gs.toJson(retMap);
	 }
}

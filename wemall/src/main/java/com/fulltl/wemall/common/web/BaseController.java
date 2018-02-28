/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.fulltl.wemall.common.web;

import java.beans.PropertyEditorSupport;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import javax.validation.Validator;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ui.Model;
import org.springframework.validation.BindException;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.fulltl.wemall.common.beanvalidator.BeanValidators;
import com.fulltl.wemall.common.config.Global;
import com.fulltl.wemall.common.mapper.JsonMapper;
import com.fulltl.wemall.common.utils.DateUtils;
import com.fulltl.wemall.modules.sys.utils.UserUtils;

/**
 * 控制器支持类
 * @author ThinkGem
 * @version 2013-3-23
 */
public abstract class BaseController {

	protected static Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
	
	/**
	 * 日志对象
	 */
	protected Logger logger = LoggerFactory.getLogger(getClass());

	/**
	 * 管理基础路径
	 */
	@Value("${adminPath}")
	protected String adminPath;
	
	/**
	 * 前端基础路径
	 */
	@Value("${frontPath}")
	protected String frontPath;
	
	/**
	 * 前端会员基础路径
	 */
	@Value("${frontMemberPath}")
	protected String frontMemberPath;
	
	/**
	 * 前端URL后缀
	 */
	@Value("${urlSuffix}")
	protected String urlSuffix;
	
	/**
	 * 前端URL后缀
	 */
	@Value("${weixin.serviceId}")
	protected String globalWxServiceId;
	
	/**
	 * 前端URL后缀
	 */
	@Value("${weixin.appUrl}")
	protected String globalAppUrl;
	
	/**
	 * 验证Bean实例对象
	 */
	@Autowired
	protected Validator validator;

	/**
	 * 服务端参数有效性验证
	 * @param object 验证的实体对象
	 * @param groups 验证组
	 * @return 验证成功：返回true；严重失败：将错误信息添加到 message 中
	 */
	protected boolean beanValidator(Model model, Object object, Class<?>... groups) {
		try{
			BeanValidators.validateWithException(validator, object, groups);
		}catch(ConstraintViolationException ex){
			List<String> list = BeanValidators.extractPropertyAndMessageAsList(ex, ": ");
			list.add(0, "数据验证失败：");
			addMessage(model, list.toArray(new String[]{}));
			return false;
		}
		return true;
	}
	
	/**
	 * 服务端参数有效性验证
	 * @param object 验证的实体对象
	 * @param groups 验证组
	 * @return 验证成功：返回true；严重失败：将错误信息添加到 flash message 中
	 */
	protected boolean beanValidator(RedirectAttributes redirectAttributes, Object object, Class<?>... groups) {
		try{
			BeanValidators.validateWithException(validator, object, groups);
		}catch(ConstraintViolationException ex){
			List<String> list = BeanValidators.extractPropertyAndMessageAsList(ex, ": ");
			list.add(0, "数据验证失败：");
			addMessage(redirectAttributes, list.toArray(new String[]{}));
			return false;
		}
		return true;
	}
	
	/**
	 * 服务端参数有效性验证
	 * @param object 验证的实体对象
	 * @param groups 验证组，不传入此参数时，同@Valid注解验证
	 * @return 验证成功：继续执行；验证失败：抛出异常跳转400页面。
	 */
	protected void beanValidator(Object object, Class<?>... groups) {
		BeanValidators.validateWithException(validator, object, groups);
	}
	
	/**
	 * 添加Model消息
	 * @param message
	 */
	protected void addMessage(Model model, String... messages) {
		StringBuilder sb = new StringBuilder();
		for (String message : messages){
			sb.append(message).append(messages.length>1?"<br/>":"");
		}
		model.addAttribute("message", sb.toString());
	}
	
	/**
	 * 添加Flash消息
	 * @param message
	 */
	protected void addMessage(RedirectAttributes redirectAttributes, String... messages) {
		StringBuilder sb = new StringBuilder();
		for (String message : messages){
			sb.append(message).append(messages.length>1?"<br/>":"");
		}
		redirectAttributes.addFlashAttribute("message", sb.toString());
	}
	
	/**
	 * 客户端返回JSON字符串
	 * @param response
	 * @param object
	 * @return
	 */
	protected String renderString(HttpServletResponse response, Object object) {
		return renderString(response, JsonMapper.toJsonString(object), "application/json");
	}
	
	/**
	 * 客户端返回字符串
	 * @param response
	 * @param string
	 * @return
	 */
	protected String renderString(HttpServletResponse response, String string, String type) {
		try {
			response.reset();
	        response.setContentType(type);
	        response.setCharacterEncoding("utf-8");
			response.getWriter().print(string);
			return null;
		} catch (IOException e) {
			return null;
		}
	}

	/**
	 * 参数绑定异常
	 */
	@ExceptionHandler({BindException.class, ConstraintViolationException.class, ValidationException.class})
    public String bindException() {  
        return "error/400";
    }
	
	/**
	 * 授权登录异常
	 */
	@ExceptionHandler({AuthenticationException.class})
    public String authenticationException() {  
        return "error/403";
    }
	
	/**
	 * 初始化数据绑定
	 * 1. 将所有传递进来的String进行HTML编码，防止XSS攻击
	 * 2. 将字段中Date类型转换为String类型
	 */
	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		// String类型转换，将所有传递进来的String进行HTML编码，防止XSS攻击
		binder.registerCustomEditor(String.class, new PropertyEditorSupport() {
			@Override
			public void setAsText(String text) {
				setValue(text == null ? null : StringEscapeUtils.escapeHtml4(text.trim()));
			}
			@Override
			public String getAsText() {
				Object value = getValue();
				return value != null ? value.toString() : "";
			}
		});
		// Date 类型转换
		binder.registerCustomEditor(Date.class, new PropertyEditorSupport() {
			@Override
			public void setAsText(String text) {
				setValue(DateUtils.parseDate(text));
			}
//			@Override
//			public String getAsText() {
//				Object value = getValue();
//				return value != null ? DateUtils.formatDateTime((Date)value) : "";
//			}
		});
	}
	
	/**
	 * 根据request获取基础路径basePath
	 * @param request
	 * @return
	 */
	protected String getBasePath(HttpServletRequest request) {
		String basePath = request.getScheme() + "://" + request.getServerName() + request.getContextPath();
		return basePath;
	}
	
	/**
	 * 统一格式化返回数据
	 * @param retMap
	 * @return
	 */
	protected Map<String, Object> formatReturnMsg(Map<String, Object> map) {
		Map<String, Object> retMap = Maps.newHashMap();
		
		Object ret = map.get("ret");
		Object retMsg = map.get("retMsg");
		/*Object sid = map.get("sid");
		Object access_token = map.get("access_token");*/
		
		Object data = map.get("data");
		if(map.size() == 3 && 
				ret != null && 
				retMsg != null && 
				data != null /*&& 
				sid != null && 
				access_token != null*/) {
			//数据无需格式化
			return map;
		}
		if(ret == null || retMsg == null) {
			//状态值或状态描述不存在
			retMap.put("ret", "-1");
			retMap.put("retMsg", "没有处理结果状态值或结果描述！");
		} else {
			//进行格式化
			retMap.put("ret", ret);
			retMap.put("retMsg", retMsg);
			map.remove("ret");
			map.remove("retMsg");
		}
		
		//sid格式化
		/*if(sid == null) {
			String userSid = UserUtils.getUser().getSid();
			retMap.put("sid", StringUtils.isBlank(userSid) ? UserUtils.getSession().getId().toString() : userSid);
		} else {
			retMap.put("sid", sid);
			map.remove("sid");
		}*/
		//access_token格式化
		/*if(access_token == null) {
			retMap.put("access_token", "");
		} else {
			retMap.put("access_token", access_token);
			map.remove("access_token");
		}*/
		//最后判断data
		if(map.containsKey("data") && map.size() == 1) {
			retMap.put("data", data);
		} else {
			retMap.put("data", map);
		}
		
		return retMap;
	}
	
	/**
	 * 统一将请求记录进日志
	 * @param request
	 */
	@ModelAttribute
	protected void logRequest(HttpServletRequest request) {
		String loginName = UserUtils.getUser().getLoginName();
		logger.info(loginName + "用户请求：" + request.getRequestURI() + "\n参数："
				+ request.getQueryString());
	}
	
}

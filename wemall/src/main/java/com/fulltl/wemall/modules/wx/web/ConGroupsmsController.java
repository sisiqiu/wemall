/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.fulltl.wemall.modules.wx.web;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.gson.Gson;
import com.fulltl.wemall.common.config.Global;
import com.fulltl.wemall.common.persistence.Page;
import com.fulltl.wemall.common.utils.RegExpValidatorUtil;
import com.fulltl.wemall.common.utils.StringUtils;
import com.fulltl.wemall.common.web.BaseController;
import com.fulltl.wemall.modules.wx.entity.ConGroupsms;
import com.fulltl.wemall.modules.wx.service.ConGroupsmsService;

/**
 * 群发短信管理Controller
 * @author ldk
 * @version 2017-10-24
 */
@Controller
@RequestMapping(value = "${adminPath}/wx/conGroupsms")
public class ConGroupsmsController extends BaseController {

	@Autowired
	private ConGroupsmsService conGroupsmsService;
	
	@ModelAttribute
	public ConGroupsms get(@RequestParam(required=false) String id) {
		ConGroupsms entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = conGroupsmsService.get(id);
		}
		if (entity == null){
			entity = new ConGroupsms();
		}
		return entity;
	}
	
	@RequiresPermissions("wx:conGroupsms:view")
	@RequestMapping(value = {"list", ""})
	public String list(ConGroupsms conGroupsms, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<ConGroupsms> page = conGroupsmsService.findPage(new Page<ConGroupsms>(request, response), conGroupsms); 
		model.addAttribute("page", page);
		return "modules/wx/conGroupsmsList";
	}

	@RequiresPermissions("wx:conGroupsms:view")
	@RequestMapping(value = "form")
	public String form(ConGroupsms conGroupsms, Model model) {
		model.addAttribute("conGroupsms", conGroupsms);
		return "modules/wx/conGroupsmsForm";
	}

	@RequiresPermissions("wx:conGroupsms:edit")
	@RequestMapping(value = "save")
	public String save(ConGroupsms conGroupsms, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, conGroupsms)){
			return form(conGroupsms, model);
		}
		//将转译的内容恢复。
		conGroupsms.setContentArr(this.escapeHtml(conGroupsms.getContentArr()));
		conGroupsms.setMobileArr(this.escapeHtml(conGroupsms.getMobileArr()));
		
		//校验发送目标数组(手机数组)格式。
		try {
			List<String> mobileNos = new Gson().fromJson(conGroupsms.getMobileArr(), List.class);
			for(String mobile: mobileNos) {
				if(!RegExpValidatorUtil.isMobile(mobile)) {
					addMessage(model, "数据验证失败：手机格式错误！错误手机：" + mobile);
					return form(conGroupsms, model);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			addMessage(model, "数据验证失败：发送目标数组格式错误！正确格式：['手机号','手机号','手机号',...,'手机号']");
			return form(conGroupsms, model);
		}
		//校验短信内容数组格式。
		try {
			List<String> contentArray = new Gson().fromJson(conGroupsms.getContentArr(), List.class);
		} catch (Exception e) {
			e.printStackTrace();
			addMessage(model, "数据验证失败：短信内容数组格式错误！正确格式：['{1}的内容','{2}的内容','{3}的内容',...,'{n}的内容']");
			return form(conGroupsms, model);
		}
		//验证发送策略是否是0--即时发送
		if(!conGroupsms.getSendType().equals("0")) {
			addMessage(model, "数据验证失败：抱歉！目前只支持即时发送，定时发送暂未开发！");
			return form(conGroupsms, model);
		}
		
		//conGroupsmsService.save(conGroupsms);
		Map<String, Object> retMap = conGroupsmsService.saveWithSendSMS(conGroupsms);
		addMessage(redirectAttributes, retMap.get("retMsg").toString());
		return "redirect:"+Global.getAdminPath()+"/wx/conGroupsms/?repage";
	}
	
	@RequiresPermissions("wx:conGroupsms:edit")
	@RequestMapping(value = "delete")
	public String delete(ConGroupsms conGroupsms, RedirectAttributes redirectAttributes) {
		conGroupsmsService.delete(conGroupsms);
		addMessage(redirectAttributes, "删除群发短信成功");
		return "redirect:"+Global.getAdminPath()+"/wx/conGroupsms/?repage";
	}
	
	/**
	 * 获取转译后的字符串
	 * @param str
	 * @return
	 */
	private String escapeHtml(String str) {
		return str.replaceAll("&amp;", "&")
			.replaceAll("&lt;", "<")
			.replaceAll("&gt;", ">")
			.replaceAll("&quot;", "\"")
			.replaceAll("&#39;", "'")
			.replaceAll("&#x2F;", "/");
	}
}
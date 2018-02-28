/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.fulltl.wemall.modules.wx.web;

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

import com.fulltl.wemall.common.config.Global;
import com.fulltl.wemall.common.persistence.Page;
import com.fulltl.wemall.common.web.BaseController;
import com.fulltl.wemall.common.utils.StringUtils;
import com.fulltl.wemall.modules.wx.entity.WxUserInfo;
import com.fulltl.wemall.modules.wx.service.WxUserInfoService;

/**
 * 微信用户信息管理Controller
 * @author ldk
 * @version 2018-02-18
 */
@Controller
@RequestMapping(value = "${adminPath}/wx/wxUserInfo")
public class WxUserInfoController extends BaseController {

	@Autowired
	private WxUserInfoService wxUserInfoService;
	
	@ModelAttribute
	public WxUserInfo get(@RequestParam(required=false) String id) {
		WxUserInfo entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = wxUserInfoService.get(id);
		}
		if (entity == null){
			entity = new WxUserInfo();
		}
		return entity;
	}
	
	@RequiresPermissions("wx:wxUserInfo:view")
	@RequestMapping(value = {"list", ""})
	public String list(WxUserInfo wxUserInfo, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<WxUserInfo> page = wxUserInfoService.findPage(new Page<WxUserInfo>(request, response), wxUserInfo); 
		model.addAttribute("page", page);
		return "modules/wx/wxUserInfoList";
	}

	@RequiresPermissions("wx:wxUserInfo:view")
	@RequestMapping(value = "form")
	public String form(WxUserInfo wxUserInfo, Model model) {
		model.addAttribute("wxUserInfo", wxUserInfo);
		return "modules/wx/wxUserInfoForm";
	}

	@RequiresPermissions("wx:wxUserInfo:edit")
	@RequestMapping(value = "save")
	public String save(WxUserInfo wxUserInfo, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, wxUserInfo)){
			return form(wxUserInfo, model);
		}
		wxUserInfoService.save(wxUserInfo);
		addMessage(redirectAttributes, "保存微信用户信息成功");
		return "redirect:"+Global.getAdminPath()+"/wx/wxUserInfo/?repage";
	}
	
	@RequiresPermissions("wx:wxUserInfo:edit")
	@RequestMapping(value = "delete")
	public String delete(WxUserInfo wxUserInfo, RedirectAttributes redirectAttributes) {
		wxUserInfoService.delete(wxUserInfo);
		addMessage(redirectAttributes, "删除微信用户信息成功");
		return "redirect:"+Global.getAdminPath()+"/wx/wxUserInfo/?repage";
	}

}
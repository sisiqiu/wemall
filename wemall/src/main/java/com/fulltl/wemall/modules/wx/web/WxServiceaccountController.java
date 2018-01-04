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
import com.fulltl.wemall.common.utils.StringUtils;
import com.fulltl.wemall.common.web.BaseController;
import com.fulltl.wemall.modules.wx.entity.WxServiceaccount;
import com.fulltl.wemall.modules.wx.service.WxServiceaccountService;

/**
 * 微信服务号管理Controller
 * @author ldk
 * @version 2017-10-13
 */
@Controller
@RequestMapping(value = "${adminPath}/wx/wxServiceaccount")
public class WxServiceaccountController extends BaseController {

	@Autowired
	private WxServiceaccountService wxServiceaccountService;
	
	@ModelAttribute
	public WxServiceaccount get(@RequestParam(required=false) String id) {
		WxServiceaccount entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = wxServiceaccountService.get(id);
		}
		if (entity == null){
			entity = new WxServiceaccount();
		}
		return entity;
	}
	
	@RequiresPermissions("wx:wxServiceaccount:view")
	@RequestMapping(value = {"list", ""})
	public String list(WxServiceaccount wxServiceaccount, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<WxServiceaccount> page = wxServiceaccountService.findPage(new Page<WxServiceaccount>(request, response), wxServiceaccount); 
		model.addAttribute("page", page);
		return "modules/wx/wxServiceaccountList";
	}

	@RequiresPermissions("wx:wxServiceaccount:view")
	@RequestMapping(value = "form")
	public String form(WxServiceaccount wxServiceaccount, Model model) {
		model.addAttribute("wxServiceaccount", wxServiceaccount);
		return "modules/wx/wxServiceaccountForm";
	}

	@RequiresPermissions("wx:wxServiceaccount:edit")
	@RequestMapping(value = "save")
	public String save(WxServiceaccount wxServiceaccount, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, wxServiceaccount)){
			return form(wxServiceaccount, model);
		}
		wxServiceaccountService.save(wxServiceaccount);
		addMessage(redirectAttributes, "保存微信服务号成功");
		return "redirect:"+Global.getAdminPath()+"/wx/wxServiceaccount/?repage";
	}
	
	@RequiresPermissions("wx:wxServiceaccount:edit")
	@RequestMapping(value = "delete")
	public String delete(WxServiceaccount wxServiceaccount, RedirectAttributes redirectAttributes) {
		wxServiceaccountService.delete(wxServiceaccount);
		addMessage(redirectAttributes, "删除微信服务号成功");
		return "redirect:"+Global.getAdminPath()+"/wx/wxServiceaccount/?repage";
	}

}
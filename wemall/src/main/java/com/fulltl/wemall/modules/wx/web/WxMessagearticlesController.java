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
import com.fulltl.wemall.modules.wx.entity.WxMessagearticles;
import com.fulltl.wemall.modules.wx.service.WxMessagearticlesService;

/**
 * 微信消息文章实体类Controller
 * @author ldk
 * @version 2017-10-11
 */
@Controller
@RequestMapping(value = "${adminPath}/wx/wxMessagearticles")
public class WxMessagearticlesController extends BaseController {

	@Autowired
	private WxMessagearticlesService wxMessagearticlesService;
	
	@ModelAttribute
	public WxMessagearticles get(@RequestParam(required=false) String id) {
		WxMessagearticles entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = wxMessagearticlesService.get(id);
		}
		if (entity == null){
			entity = new WxMessagearticles();
		}
		return entity;
	}
	
	@RequiresPermissions("wx:wxMessagearticles:view")
	@RequestMapping(value = {"list", ""})
	public String list(WxMessagearticles wxMessagearticles, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<WxMessagearticles> page = wxMessagearticlesService.findPage(new Page<WxMessagearticles>(request, response), wxMessagearticles); 
		model.addAttribute("page", page);
		return "modules/wx/wxMessagearticlesList";
	}

	@RequiresPermissions("wx:wxMessagearticles:view")
	@RequestMapping(value = "form")
	public String form(WxMessagearticles wxMessagearticles, Model model) {
		model.addAttribute("wxMessagearticles", wxMessagearticles);
		return "modules/wx/wxMessagearticlesForm";
	}

	@RequiresPermissions("wx:wxMessagearticles:edit")
	@RequestMapping(value = "save")
	public String save(WxMessagearticles wxMessagearticles, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, wxMessagearticles)){
			return form(wxMessagearticles, model);
		}
		wxMessagearticlesService.save(wxMessagearticles);
		addMessage(redirectAttributes, "保存微信消息文章成功");
		return "redirect:"+Global.getAdminPath()+"/wx/wxMessagearticles/?repage";
	}
	
	@RequiresPermissions("wx:wxMessagearticles:edit")
	@RequestMapping(value = "delete")
	public String delete(WxMessagearticles wxMessagearticles, RedirectAttributes redirectAttributes) {
		wxMessagearticlesService.delete(wxMessagearticles);
		addMessage(redirectAttributes, "删除微信消息文章成功");
		return "redirect:"+Global.getAdminPath()+"/wx/wxMessagearticles/?repage";
	}

}
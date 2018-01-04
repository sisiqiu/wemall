/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.fulltl.wemall.modules.sys.web;

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
import com.fulltl.wemall.modules.sys.entity.SlAppsession;
import com.fulltl.wemall.modules.sys.service.SlAppsessionService;

/**
 * appsession管理Controller
 * @author kangyang
 * @version 2017-12-06
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/slAppsession")
public class SlAppsessionController extends BaseController {

	@Autowired
	private SlAppsessionService slAppsessionService;
	
	@ModelAttribute
	public SlAppsession get(@RequestParam(required=false) String id) {
		SlAppsession entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = slAppsessionService.get(id);
		}
		if (entity == null){
			entity = new SlAppsession();
		}
		return entity;
	}
	
	@RequiresPermissions("sys:slAppsession:view")
	@RequestMapping(value = {"list", ""})
	public String list(SlAppsession slAppsession, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<SlAppsession> page = slAppsessionService.findPage(new Page<SlAppsession>(request, response), slAppsession); 
		model.addAttribute("page", page);
		return "modules/sys/slAppsessionList";
	}

	@RequiresPermissions("sys:slAppsession:view")
	@RequestMapping(value = "form")
	public String form(SlAppsession slAppsession, Model model) {
		model.addAttribute("slAppsession", slAppsession);
		return "modules/sys/slAppsessionForm";
	}

	@RequiresPermissions("sys:slAppsession:edit")
	@RequestMapping(value = "save")
	public String save(SlAppsession slAppsession, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, slAppsession)){
			return form(slAppsession, model);
		}
		slAppsessionService.save(slAppsession);
		addMessage(redirectAttributes, "保存会话成功");
		return "redirect:"+Global.getAdminPath()+"/sys/slAppsession/?repage";
	}
	
	@RequiresPermissions("sys:slAppsession:edit")
	@RequestMapping(value = "delete")
	public String delete(SlAppsession slAppsession, RedirectAttributes redirectAttributes) {
		slAppsessionService.delete(slAppsession);
		addMessage(redirectAttributes, "删除会话成功");
		return "redirect:"+Global.getAdminPath()+"/sys/slAppsession/?repage";
	}

}
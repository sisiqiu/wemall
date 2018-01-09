/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.fulltl.wemall.modules.wemall.web;

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
import com.fulltl.wemall.modules.wemall.entity.WemallSpec;
import com.fulltl.wemall.modules.wemall.service.WemallSpecService;

/**
 * 属性类别管理Controller
 * @author ldk
 * @version 2018-01-05
 */
@Controller
@RequestMapping(value = "${adminPath}/wemall/wemallSpec")
public class WemallSpecController extends BaseController {

	@Autowired
	private WemallSpecService wemallSpecService;
	
	@ModelAttribute
	public WemallSpec get(@RequestParam(required=false) String id) {
		WemallSpec entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = wemallSpecService.get(id);
		}
		if (entity == null){
			entity = new WemallSpec();
		}
		return entity;
	}
	
	@RequiresPermissions("wemall:wemallSpec:view")
	@RequestMapping(value = {"list", ""})
	public String list(WemallSpec wemallSpec, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<WemallSpec> page = wemallSpecService.findPage(new Page<WemallSpec>(request, response), wemallSpec); 
		model.addAttribute("page", page);
		return "modules/wemall/wemallSpecList";
	}

	@RequiresPermissions("wemall:wemallSpec:view")
	@RequestMapping(value = "form")
	public String form(WemallSpec wemallSpec, Model model) {
		model.addAttribute("wemallSpec", wemallSpec);
		return "modules/wemall/wemallSpecForm";
	}

	@RequiresPermissions("wemall:wemallSpec:edit")
	@RequestMapping(value = "save")
	public String save(WemallSpec wemallSpec, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, wemallSpec)){
			return form(wemallSpec, model);
		}
		wemallSpecService.save(wemallSpec);
		addMessage(redirectAttributes, "保存属性类别成功");
		return "redirect:"+Global.getAdminPath()+"/wemall/wemallSpec/?repage";
	}
	
	@RequiresPermissions("wemall:wemallSpec:edit")
	@RequestMapping(value = "delete")
	public String delete(WemallSpec wemallSpec, RedirectAttributes redirectAttributes) {
		wemallSpecService.delete(wemallSpec);
		addMessage(redirectAttributes, "删除属性类别成功");
		return "redirect:"+Global.getAdminPath()+"/wemall/wemallSpec/?repage";
	}

}
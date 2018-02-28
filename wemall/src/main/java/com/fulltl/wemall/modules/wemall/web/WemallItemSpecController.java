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
import com.fulltl.wemall.modules.wemall.entity.WemallItemSpec;
import com.fulltl.wemall.modules.wemall.service.WemallItemSpecService;

/**
 * 商品-属性管理Controller
 * @author ldk
 * @version 2018-02-01
 */
@Controller
@RequestMapping(value = "${adminPath}/wemall/wemallItemSpec")
public class WemallItemSpecController extends BaseController {

	@Autowired
	private WemallItemSpecService wemallItemSpecService;
	
	@ModelAttribute
	public WemallItemSpec get(@RequestParam(required=false) String id) {
		WemallItemSpec entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = wemallItemSpecService.get(id);
		}
		if (entity == null){
			entity = new WemallItemSpec();
		}
		return entity;
	}
	
	@RequiresPermissions("wemall:wemallItemSpec:view")
	@RequestMapping(value = {"list", ""})
	public String list(WemallItemSpec wemallItemSpec, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<WemallItemSpec> page = wemallItemSpecService.findPage(new Page<WemallItemSpec>(request, response), wemallItemSpec); 
		model.addAttribute("page", page);
		return "modules/wemall/wemallItemSpecList";
	}

	@RequiresPermissions("wemall:wemallItemSpec:view")
	@RequestMapping(value = "form")
	public String form(WemallItemSpec wemallItemSpec, Model model) {
		model.addAttribute("wemallItemSpec", wemallItemSpec);
		return "modules/wemall/wemallItemSpecForm";
	}

	@RequiresPermissions("wemall:wemallItemSpec:edit")
	@RequestMapping(value = "save")
	public String save(WemallItemSpec wemallItemSpec, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, wemallItemSpec)){
			return form(wemallItemSpec, model);
		}
		wemallItemSpecService.save(wemallItemSpec);
		addMessage(redirectAttributes, "保存商品-属性信息成功");
		return "redirect:"+Global.getAdminPath()+"/wemall/wemallItemSpec/?repage";
	}
	
	@RequiresPermissions("wemall:wemallItemSpec:edit")
	@RequestMapping(value = "delete")
	public String delete(WemallItemSpec wemallItemSpec, RedirectAttributes redirectAttributes) {
		wemallItemSpecService.delete(wemallItemSpec);
		addMessage(redirectAttributes, "删除商品-属性信息成功");
		return "redirect:"+Global.getAdminPath()+"/wemall/wemallItemSpec/?repage";
	}

}
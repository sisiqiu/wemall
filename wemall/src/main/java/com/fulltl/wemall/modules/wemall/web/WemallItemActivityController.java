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
import com.fulltl.wemall.modules.wemall.entity.WemallItemActivity;
import com.fulltl.wemall.modules.wemall.service.WemallItemActivityService;

/**
 * 商品活动中间表管理Controller
 * @author fulltl
 * @version 2018-01-13
 */
@Controller
@RequestMapping(value = "${adminPath}/wemall/wemallItemActivity")
public class WemallItemActivityController extends BaseController {

	@Autowired
	private WemallItemActivityService wemallItemActivityService;
	
	@ModelAttribute
	public WemallItemActivity get(@RequestParam(required=false) String id) {
		WemallItemActivity entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = wemallItemActivityService.get(id);
		}
		if (entity == null){
			entity = new WemallItemActivity();
		}
		return entity;
	}
	
	@RequiresPermissions("wemall:wemallItemActivity:view")
	@RequestMapping(value = {"list", ""})
	public String list(WemallItemActivity wemallItemActivity, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<WemallItemActivity> page = wemallItemActivityService.findPage(new Page<WemallItemActivity>(request, response), wemallItemActivity); 
		model.addAttribute("page", page);
		return "modules/wemall/wemallItemActivityList";
	}

	@RequiresPermissions("wemall:wemallItemActivity:view")
	@RequestMapping(value = "form")
	public String form(WemallItemActivity wemallItemActivity, Model model) {
		model.addAttribute("wemallItemActivity", wemallItemActivity);
		return "modules/wemall/wemallItemActivityForm";
	}

	@RequiresPermissions("wemall:wemallItemActivity:edit")
	@RequestMapping(value = "save")
	public String save(WemallItemActivity wemallItemActivity, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, wemallItemActivity)){
			return form(wemallItemActivity, model);
		}
		wemallItemActivityService.save(wemallItemActivity);
		addMessage(redirectAttributes, "保存商品活动中间表成功");
		return "redirect:"+Global.getAdminPath()+"/wemall/wemallItemActivity/?repage";
	}
	
	@RequiresPermissions("wemall:wemallItemActivity:edit")
	@RequestMapping(value = "delete")
	public String delete(WemallItemActivity wemallItemActivity, RedirectAttributes redirectAttributes) {
		wemallItemActivityService.delete(wemallItemActivity);
		addMessage(redirectAttributes, "删除商品活动中间表成功");
		return "redirect:"+Global.getAdminPath()+"/wemall/wemallItemActivity/?repage";
	}

}
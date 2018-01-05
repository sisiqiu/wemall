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
import com.fulltl.wemall.modules.wemall.entity.WemallItemSort;
import com.fulltl.wemall.modules.wemall.service.WemallItemSortService;

/**
 * 商品分类管理Controller
 * @author ldk
 * @version 2018-01-05
 */
@Controller
@RequestMapping(value = "${adminPath}/wemall/wemallItemSort")
public class WemallItemSortController extends BaseController {

	@Autowired
	private WemallItemSortService wemallItemSortService;
	
	@ModelAttribute
	public WemallItemSort get(@RequestParam(required=false) String id) {
		WemallItemSort entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = wemallItemSortService.get(id);
		}
		if (entity == null){
			entity = new WemallItemSort();
		}
		return entity;
	}
	
	@RequiresPermissions("wemall:wemallItemSort:view")
	@RequestMapping(value = {"list", ""})
	public String list(WemallItemSort wemallItemSort, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<WemallItemSort> page = wemallItemSortService.findPage(new Page<WemallItemSort>(request, response), wemallItemSort); 
		model.addAttribute("page", page);
		return "modules/wemall/wemallItemSortList";
	}

	@RequiresPermissions("wemall:wemallItemSort:view")
	@RequestMapping(value = "form")
	public String form(WemallItemSort wemallItemSort, Model model) {
		model.addAttribute("wemallItemSort", wemallItemSort);
		return "modules/wemall/wemallItemSortForm";
	}

	@RequiresPermissions("wemall:wemallItemSort:edit")
	@RequestMapping(value = "save")
	public String save(WemallItemSort wemallItemSort, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, wemallItemSort)){
			return form(wemallItemSort, model);
		}
		wemallItemSortService.save(wemallItemSort);
		addMessage(redirectAttributes, "保存商品分类成功");
		return "redirect:"+Global.getAdminPath()+"/wemall/wemallItemSort/?repage";
	}
	
	@RequiresPermissions("wemall:wemallItemSort:edit")
	@RequestMapping(value = "delete")
	public String delete(WemallItemSort wemallItemSort, RedirectAttributes redirectAttributes) {
		wemallItemSortService.delete(wemallItemSort);
		addMessage(redirectAttributes, "删除商品分类成功");
		return "redirect:"+Global.getAdminPath()+"/wemall/wemallItemSort/?repage";
	}

}
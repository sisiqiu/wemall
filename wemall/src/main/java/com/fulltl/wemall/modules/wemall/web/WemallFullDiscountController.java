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
import com.fulltl.wemall.modules.wemall.entity.WemallFullDiscount;
import com.fulltl.wemall.modules.wemall.service.WemallFullDiscountService;

/**
 * 满减送活动管理Controller
 * @author ldk
 * @version 2018-01-05
 */
@Controller
@RequestMapping(value = "${adminPath}/wemall/wemallFullDiscount")
public class WemallFullDiscountController extends BaseController {

	@Autowired
	private WemallFullDiscountService wemallFullDiscountService;
	
	@ModelAttribute
	public WemallFullDiscount get(@RequestParam(required=false) String id) {
		WemallFullDiscount entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = wemallFullDiscountService.get(id);
		}
		if (entity == null){
			entity = new WemallFullDiscount();
		}
		return entity;
	}
	
	@RequiresPermissions("wemall:wemallFullDiscount:view")
	@RequestMapping(value = {"list", ""})
	public String list(WemallFullDiscount wemallFullDiscount, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<WemallFullDiscount> page = wemallFullDiscountService.findPage(new Page<WemallFullDiscount>(request, response), wemallFullDiscount); 
		model.addAttribute("page", page);
		return "modules/wemall/wemallFullDiscountList";
	}

	@RequiresPermissions("wemall:wemallFullDiscount:view")
	@RequestMapping(value = "form")
	public String form(WemallFullDiscount wemallFullDiscount, Model model) {
		model.addAttribute("wemallFullDiscount", wemallFullDiscount);
		return "modules/wemall/wemallFullDiscountForm";
	}

	@RequiresPermissions("wemall:wemallFullDiscount:edit")
	@RequestMapping(value = "save")
	public String save(WemallFullDiscount wemallFullDiscount, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, wemallFullDiscount)){
			return form(wemallFullDiscount, model);
		}
		wemallFullDiscountService.save(wemallFullDiscount);
		addMessage(redirectAttributes, "保存满减送活动成功");
		return "redirect:"+Global.getAdminPath()+"/wemall/wemallFullDiscount/?repage";
	}
	
	@RequiresPermissions("wemall:wemallFullDiscount:edit")
	@RequestMapping(value = "delete")
	public String delete(WemallFullDiscount wemallFullDiscount, RedirectAttributes redirectAttributes) {
		wemallFullDiscountService.delete(wemallFullDiscount);
		addMessage(redirectAttributes, "删除满减送活动成功");
		return "redirect:"+Global.getAdminPath()+"/wemall/wemallFullDiscount/?repage";
	}

}
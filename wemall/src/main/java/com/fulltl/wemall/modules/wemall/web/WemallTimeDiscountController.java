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
import com.fulltl.wemall.modules.wemall.entity.WemallTimeDiscount;
import com.fulltl.wemall.modules.wemall.service.WemallTimeDiscountService;

/**
 * 限时打折活动管理Controller
 * @author ldk
 * @version 2018-01-05
 */
@Controller
@RequestMapping(value = "${adminPath}/wemall/wemallTimeDiscount")
public class WemallTimeDiscountController extends BaseController {

	@Autowired
	private WemallTimeDiscountService wemallTimeDiscountService;
	
	@ModelAttribute
	public WemallTimeDiscount get(@RequestParam(required=false) String id) {
		WemallTimeDiscount entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = wemallTimeDiscountService.get(id);
		}
		if (entity == null){
			entity = new WemallTimeDiscount();
		}
		return entity;
	}
	
	@RequiresPermissions("wemall:wemallTimeDiscount:view")
	@RequestMapping(value = {"list", ""})
	public String list(WemallTimeDiscount wemallTimeDiscount, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<WemallTimeDiscount> page = wemallTimeDiscountService.findPage(new Page<WemallTimeDiscount>(request, response), wemallTimeDiscount); 
		model.addAttribute("page", page);
		return "modules/wemall/wemallTimeDiscountList";
	}

	@RequiresPermissions("wemall:wemallTimeDiscount:view")
	@RequestMapping(value = "form")
	public String form(WemallTimeDiscount wemallTimeDiscount, Model model) {
		model.addAttribute("wemallTimeDiscount", wemallTimeDiscount);
		return "modules/wemall/wemallTimeDiscountForm";
	}

	@RequiresPermissions("wemall:wemallTimeDiscount:edit")
	@RequestMapping(value = "save")
	public String save(WemallTimeDiscount wemallTimeDiscount, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, wemallTimeDiscount)){
			return form(wemallTimeDiscount, model);
		}
		wemallTimeDiscountService.save(wemallTimeDiscount);
		addMessage(redirectAttributes, "保存限时打折活动成功");
		return "redirect:"+Global.getAdminPath()+"/wemall/wemallTimeDiscount/?repage";
	}
	
	@RequiresPermissions("wemall:wemallTimeDiscount:edit")
	@RequestMapping(value = "delete")
	public String delete(WemallTimeDiscount wemallTimeDiscount, RedirectAttributes redirectAttributes) {
		wemallTimeDiscountService.delete(wemallTimeDiscount);
		addMessage(redirectAttributes, "删除限时打折活动成功");
		return "redirect:"+Global.getAdminPath()+"/wemall/wemallTimeDiscount/?repage";
	}

}
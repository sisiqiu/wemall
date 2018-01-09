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
import com.fulltl.wemall.modules.wemall.entity.WemallCashbackDiscount;
import com.fulltl.wemall.modules.wemall.service.WemallCashbackDiscountService;

/**
 * 限时返现活动管理Controller
 * @author ldk
 * @version 2018-01-05
 */
@Controller
@RequestMapping(value = "${adminPath}/wemall/wemallCashbackDiscount")
public class WemallCashbackDiscountController extends BaseController {

	@Autowired
	private WemallCashbackDiscountService wemallCashbackDiscountService;
	
	@ModelAttribute
	public WemallCashbackDiscount get(@RequestParam(required=false) String id) {
		WemallCashbackDiscount entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = wemallCashbackDiscountService.get(id);
		}
		if (entity == null){
			entity = new WemallCashbackDiscount();
		}
		return entity;
	}
	
	@RequiresPermissions("wemall:wemallCashbackDiscount:view")
	@RequestMapping(value = {"list", ""})
	public String list(WemallCashbackDiscount wemallCashbackDiscount, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<WemallCashbackDiscount> page = wemallCashbackDiscountService.findPage(new Page<WemallCashbackDiscount>(request, response), wemallCashbackDiscount); 
		model.addAttribute("page", page);
		return "modules/wemall/wemallCashbackDiscountList";
	}

	@RequiresPermissions("wemall:wemallCashbackDiscount:view")
	@RequestMapping(value = "form")
	public String form(WemallCashbackDiscount wemallCashbackDiscount, Model model) {
		model.addAttribute("wemallCashbackDiscount", wemallCashbackDiscount);
		return "modules/wemall/wemallCashbackDiscountForm";
	}

	@RequiresPermissions("wemall:wemallCashbackDiscount:edit")
	@RequestMapping(value = "save")
	public String save(WemallCashbackDiscount wemallCashbackDiscount, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, wemallCashbackDiscount)){
			return form(wemallCashbackDiscount, model);
		}
		wemallCashbackDiscountService.save(wemallCashbackDiscount);
		addMessage(redirectAttributes, "保存限时返现活动成功");
		return "redirect:"+Global.getAdminPath()+"/wemall/wemallCashbackDiscount/?repage";
	}
	
	@RequiresPermissions("wemall:wemallCashbackDiscount:edit")
	@RequestMapping(value = "delete")
	public String delete(WemallCashbackDiscount wemallCashbackDiscount, RedirectAttributes redirectAttributes) {
		wemallCashbackDiscountService.delete(wemallCashbackDiscount);
		addMessage(redirectAttributes, "删除限时返现活动成功");
		return "redirect:"+Global.getAdminPath()+"/wemall/wemallCashbackDiscount/?repage";
	}

}
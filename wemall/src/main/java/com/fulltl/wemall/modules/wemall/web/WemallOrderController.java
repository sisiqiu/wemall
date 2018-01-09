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
import com.fulltl.wemall.modules.wemall.entity.WemallOrder;
import com.fulltl.wemall.modules.wemall.service.WemallOrderService;

/**
 * 订单管理Controller
 * @author ldk
 * @version 2018-01-05
 */
@Controller
@RequestMapping(value = "${adminPath}/wemall/wemallOrder")
public class WemallOrderController extends BaseController {

	@Autowired
	private WemallOrderService wemallOrderService;
	
	@ModelAttribute
	public WemallOrder get(@RequestParam(required=false) String id) {
		WemallOrder entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = wemallOrderService.get(id);
		}
		if (entity == null){
			entity = new WemallOrder();
		}
		return entity;
	}
	
	@RequiresPermissions("wemall:wemallOrder:view")
	@RequestMapping(value = {"list", ""})
	public String list(WemallOrder wemallOrder, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<WemallOrder> page = wemallOrderService.findPage(new Page<WemallOrder>(request, response), wemallOrder); 
		model.addAttribute("page", page);
		return "modules/wemall/wemallOrderList";
	}

	@RequiresPermissions("wemall:wemallOrder:view")
	@RequestMapping(value = "form")
	public String form(WemallOrder wemallOrder, Model model) {
		model.addAttribute("wemallOrder", wemallOrder);
		return "modules/wemall/wemallOrderForm";
	}

	@RequiresPermissions("wemall:wemallOrder:edit")
	@RequestMapping(value = "save")
	public String save(WemallOrder wemallOrder, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, wemallOrder)){
			return form(wemallOrder, model);
		}
		wemallOrderService.save(wemallOrder);
		addMessage(redirectAttributes, "保存订单成功");
		return "redirect:"+Global.getAdminPath()+"/wemall/wemallOrder/?repage";
	}
	
	@RequiresPermissions("wemall:wemallOrder:edit")
	@RequestMapping(value = "delete")
	public String delete(WemallOrder wemallOrder, RedirectAttributes redirectAttributes) {
		wemallOrderService.delete(wemallOrder);
		addMessage(redirectAttributes, "删除订单成功");
		return "redirect:"+Global.getAdminPath()+"/wemall/wemallOrder/?repage";
	}

}
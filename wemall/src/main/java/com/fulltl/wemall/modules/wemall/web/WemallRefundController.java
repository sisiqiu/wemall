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
import com.fulltl.wemall.modules.wemall.entity.WemallRefund;
import com.fulltl.wemall.modules.wemall.service.WemallRefundService;

/**
 * 退款管理Controller
 * @author ldk
 * @version 2018-01-05
 */
@Controller
@RequestMapping(value = "${adminPath}/wemall/wemallRefund")
public class WemallRefundController extends BaseController {

	@Autowired
	private WemallRefundService wemallRefundService;
	
	@ModelAttribute
	public WemallRefund get(@RequestParam(required=false) String refundId) {
		WemallRefund entity = null;
		if (StringUtils.isNotBlank(refundId)){
			entity = wemallRefundService.get(refundId);
		}
		if (entity == null){
			entity = new WemallRefund();
		}
		return entity;
	}
	
	@RequiresPermissions("wemall:wemallRefund:view")
	@RequestMapping(value = {"list", ""})
	public String list(WemallRefund wemallRefund, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<WemallRefund> page = wemallRefundService.findPage(new Page<WemallRefund>(request, response), wemallRefund); 
		model.addAttribute("page", page);
		return "modules/wemall/wemallRefundList";
	}

	@RequiresPermissions("wemall:wemallRefund:view")
	@RequestMapping(value = "form")
	public String form(WemallRefund wemallRefund, Model model) {
		model.addAttribute("wemallRefund", wemallRefund);
		return "modules/wemall/wemallRefundForm";
	}

	@RequiresPermissions("wemall:wemallRefund:edit")
	@RequestMapping(value = "save")
	public String save(WemallRefund wemallRefund, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, wemallRefund)){
			return form(wemallRefund, model);
		}
		wemallRefundService.save(wemallRefund);
		addMessage(redirectAttributes, "保存退款信息成功");
		return "redirect:"+Global.getAdminPath()+"/wemall/wemallRefund/?repage";
	}
	
	@RequiresPermissions("wemall:wemallRefund:edit")
	@RequestMapping(value = "delete")
	public String delete(WemallRefund wemallRefund, RedirectAttributes redirectAttributes) {
		wemallRefundService.delete(wemallRefund);
		addMessage(redirectAttributes, "删除退款信息成功");
		return "redirect:"+Global.getAdminPath()+"/wemall/wemallRefund/?repage";
	}

}
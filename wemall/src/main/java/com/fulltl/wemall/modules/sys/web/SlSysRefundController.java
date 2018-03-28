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
import com.fulltl.wemall.common.utils.StringUtils;
import com.fulltl.wemall.common.web.BaseController;
import com.fulltl.wemall.modules.sys.entity.SlSysRefund;
import com.fulltl.wemall.modules.sys.service.SlSysRefundService;

/**
 * 退款管理Controller
 * @author ldk
 * @version 2018-01-30
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/slSysRefund")
public class SlSysRefundController extends BaseController {

	@Autowired
	private SlSysRefundService slSysRefundService;
	
	@ModelAttribute
	public SlSysRefund get(@RequestParam(required=false) String id) {
		SlSysRefund entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = slSysRefundService.get(id);
		}
		if (entity == null){
			entity = new SlSysRefund();
		}
		return entity;
	}
	
	@RequiresPermissions("sys:slSysRefund:view")
	@RequestMapping(value = {"list", ""})
	public String list(SlSysRefund slSysRefund, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<SlSysRefund> page = slSysRefundService.findPage(new Page<SlSysRefund>(request, response), slSysRefund); 
		model.addAttribute("page", page);
		return "modules/sys/slSysRefundList";
	}

	@RequiresPermissions("sys:slSysRefund:view")
	@RequestMapping(value = "form")
	public String form(SlSysRefund slSysRefund, Model model) {
		model.addAttribute("slSysRefund", slSysRefund);
		return "modules/sys/slSysRefundForm";
	}

	@RequiresPermissions("sys:slSysRefund:edit")
	@RequestMapping(value = "save")
	public String save(SlSysRefund slSysRefund, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, slSysRefund)){
			return form(slSysRefund, model);
		}
		slSysRefundService.save(slSysRefund);
		addMessage(redirectAttributes, "保存退款记录成功");
		return "redirect:"+Global.getAdminPath()+"/sys/slSysRefund/?repage";
	}
	
	@RequiresPermissions("sys:slSysRefund:edit")
	@RequestMapping(value = "delete")
	public String delete(SlSysRefund slSysRefund, RedirectAttributes redirectAttributes) {
		slSysRefundService.delete(slSysRefund);
		addMessage(redirectAttributes, "删除退款记录成功");
		return "redirect:"+Global.getAdminPath()+"/sys/slSysRefund/?repage";
	}

}
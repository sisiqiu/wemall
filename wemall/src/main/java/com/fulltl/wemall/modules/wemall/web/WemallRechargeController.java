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
import com.fulltl.wemall.modules.wemall.entity.WemallRecharge;
import com.fulltl.wemall.modules.wemall.service.WemallRechargeService;

/**
 * 充值设定管理Controller
 * @author ldk
 * @version 2018-04-18
 */
@Controller
@RequestMapping(value = "${adminPath}/wemall/wemallRecharge")
public class WemallRechargeController extends BaseController {

	@Autowired
	private WemallRechargeService wemallRechargeService;
	
	@ModelAttribute
	public WemallRecharge get(@RequestParam(required=false) String id) {
		WemallRecharge entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = wemallRechargeService.get(id);
		}
		if (entity == null){
			entity = new WemallRecharge();
		}
		return entity;
	}
	
	@RequiresPermissions("wemall:wemallRecharge:view")
	@RequestMapping(value = {"list", ""})
	public String list(WemallRecharge wemallRecharge, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<WemallRecharge> page = wemallRechargeService.findPage(new Page<WemallRecharge>(request, response), wemallRecharge); 
		model.addAttribute("page", page);
		return "modules/wemall/wemallRechargeList";
	}

	@RequiresPermissions("wemall:wemallRecharge:view")
	@RequestMapping(value = "form")
	public String form(WemallRecharge wemallRecharge, Model model) {
		model.addAttribute("wemallRecharge", wemallRecharge);
		return "modules/wemall/wemallRechargeForm";
	}

	@RequiresPermissions("wemall:wemallRecharge:edit")
	@RequestMapping(value = "save")
	public String save(WemallRecharge wemallRecharge, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, wemallRecharge)){
			return form(wemallRecharge, model);
		}
		wemallRechargeService.save(wemallRecharge);
		addMessage(redirectAttributes, "保存充值设定成功");
		return "redirect:"+Global.getAdminPath()+"/wemall/wemallRecharge/?repage";
	}
	
	@RequiresPermissions("wemall:wemallRecharge:edit")
	@RequestMapping(value = "delete")
	public String delete(WemallRecharge wemallRecharge, RedirectAttributes redirectAttributes) {
		wemallRechargeService.delete(wemallRecharge);
		addMessage(redirectAttributes, "删除充值设定成功");
		return "redirect:"+Global.getAdminPath()+"/wemall/wemallRecharge/?repage";
	}

}
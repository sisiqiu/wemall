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
import com.fulltl.wemall.modules.wemall.entity.WemallSignIn;
import com.fulltl.wemall.modules.wemall.service.WemallSignInService;

/**
 * 签到管理Controller
 * @author ldk
 * @version 2018-01-05
 */
@Controller
@RequestMapping(value = "${adminPath}/wemall/wemallSignIn")
public class WemallSignInController extends BaseController {

	@Autowired
	private WemallSignInService wemallSignInService;
	
	@ModelAttribute
	public WemallSignIn get(@RequestParam(required=false) String id) {
		WemallSignIn entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = wemallSignInService.get(id);
		}
		if (entity == null){
			entity = new WemallSignIn();
		}
		return entity;
	}
	
	@RequiresPermissions("wemall:wemallSignIn:view")
	@RequestMapping(value = {"list", ""})
	public String list(WemallSignIn wemallSignIn, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<WemallSignIn> page = wemallSignInService.findPage(new Page<WemallSignIn>(request, response), wemallSignIn); 
		model.addAttribute("page", page);
		return "modules/wemall/wemallSignInList";
	}

	@RequiresPermissions("wemall:wemallSignIn:view")
	@RequestMapping(value = "form")
	public String form(WemallSignIn wemallSignIn, Model model) {
		model.addAttribute("wemallSignIn", wemallSignIn);
		return "modules/wemall/wemallSignInForm";
	}

	@RequiresPermissions("wemall:wemallSignIn:edit")
	@RequestMapping(value = "save")
	public String save(WemallSignIn wemallSignIn, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, wemallSignIn)){
			return form(wemallSignIn, model);
		}
		wemallSignInService.save(wemallSignIn);
		addMessage(redirectAttributes, "保存签到信息成功");
		return "redirect:"+Global.getAdminPath()+"/wemall/wemallSignIn/?repage";
	}
	
	@RequiresPermissions("wemall:wemallSignIn:edit")
	@RequestMapping(value = "delete")
	public String delete(WemallSignIn wemallSignIn, RedirectAttributes redirectAttributes) {
		wemallSignInService.delete(wemallSignIn);
		addMessage(redirectAttributes, "删除签到信息成功");
		return "redirect:"+Global.getAdminPath()+"/wemall/wemallSignIn/?repage";
	}

}
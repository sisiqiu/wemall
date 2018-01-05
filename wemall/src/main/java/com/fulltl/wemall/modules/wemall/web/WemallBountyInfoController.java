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
import com.fulltl.wemall.modules.wemall.entity.WemallBountyInfo;
import com.fulltl.wemall.modules.wemall.service.WemallBountyInfoService;

/**
 * 奖励金管理Controller
 * @author ldk
 * @version 2018-01-05
 */
@Controller
@RequestMapping(value = "${adminPath}/wemall/wemallBountyInfo")
public class WemallBountyInfoController extends BaseController {

	@Autowired
	private WemallBountyInfoService wemallBountyInfoService;
	
	@ModelAttribute
	public WemallBountyInfo get(@RequestParam(required=false) String id) {
		WemallBountyInfo entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = wemallBountyInfoService.get(id);
		}
		if (entity == null){
			entity = new WemallBountyInfo();
		}
		return entity;
	}
	
	@RequiresPermissions("wemall:wemallBountyInfo:view")
	@RequestMapping(value = {"list", ""})
	public String list(WemallBountyInfo wemallBountyInfo, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<WemallBountyInfo> page = wemallBountyInfoService.findPage(new Page<WemallBountyInfo>(request, response), wemallBountyInfo); 
		model.addAttribute("page", page);
		return "modules/wemall/wemallBountyInfoList";
	}

	@RequiresPermissions("wemall:wemallBountyInfo:view")
	@RequestMapping(value = "form")
	public String form(WemallBountyInfo wemallBountyInfo, Model model) {
		model.addAttribute("wemallBountyInfo", wemallBountyInfo);
		return "modules/wemall/wemallBountyInfoForm";
	}

	@RequiresPermissions("wemall:wemallBountyInfo:edit")
	@RequestMapping(value = "save")
	public String save(WemallBountyInfo wemallBountyInfo, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, wemallBountyInfo)){
			return form(wemallBountyInfo, model);
		}
		wemallBountyInfoService.save(wemallBountyInfo);
		addMessage(redirectAttributes, "保存奖励金明细信息成功");
		return "redirect:"+Global.getAdminPath()+"/wemall/wemallBountyInfo/?repage";
	}
	
	@RequiresPermissions("wemall:wemallBountyInfo:edit")
	@RequestMapping(value = "delete")
	public String delete(WemallBountyInfo wemallBountyInfo, RedirectAttributes redirectAttributes) {
		wemallBountyInfoService.delete(wemallBountyInfo);
		addMessage(redirectAttributes, "删除奖励金明细信息成功");
		return "redirect:"+Global.getAdminPath()+"/wemall/wemallBountyInfo/?repage";
	}

}
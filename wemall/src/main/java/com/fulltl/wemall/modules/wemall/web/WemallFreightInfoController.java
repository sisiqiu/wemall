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
import com.fulltl.wemall.modules.wemall.entity.WemallFreightInfo;
import com.fulltl.wemall.modules.wemall.service.WemallFreightInfoService;

/**
 * 物流明细管理Controller
 * @author ldk
 * @version 2018-01-05
 */
@Controller
@RequestMapping(value = "${adminPath}/wemall/wemallFreightInfo")
public class WemallFreightInfoController extends BaseController {

	@Autowired
	private WemallFreightInfoService wemallFreightInfoService;
	
	@ModelAttribute
	public WemallFreightInfo get(@RequestParam(required=false) String id) {
		WemallFreightInfo entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = wemallFreightInfoService.get(id);
		}
		if (entity == null){
			entity = new WemallFreightInfo();
		}
		return entity;
	}
	
	@RequiresPermissions("wemall:wemallFreightInfo:view")
	@RequestMapping(value = {"list", ""})
	public String list(WemallFreightInfo wemallFreightInfo, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<WemallFreightInfo> page = wemallFreightInfoService.findPage(new Page<WemallFreightInfo>(request, response), wemallFreightInfo); 
		model.addAttribute("page", page);
		return "modules/wemall/wemallFreightInfoList";
	}

	@RequiresPermissions("wemall:wemallFreightInfo:view")
	@RequestMapping(value = "form")
	public String form(WemallFreightInfo wemallFreightInfo, Model model) {
		model.addAttribute("wemallFreightInfo", wemallFreightInfo);
		return "modules/wemall/wemallFreightInfoForm";
	}

	@RequiresPermissions("wemall:wemallFreightInfo:edit")
	@RequestMapping(value = "save")
	public String save(WemallFreightInfo wemallFreightInfo, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, wemallFreightInfo)){
			return form(wemallFreightInfo, model);
		}
		wemallFreightInfoService.save(wemallFreightInfo);
		addMessage(redirectAttributes, "保存物流明细信息成功");
		return "redirect:"+Global.getAdminPath()+"/wemall/wemallFreightInfo/?repage";
	}
	
	@RequiresPermissions("wemall:wemallFreightInfo:edit")
	@RequestMapping(value = "delete")
	public String delete(WemallFreightInfo wemallFreightInfo, RedirectAttributes redirectAttributes) {
		wemallFreightInfoService.delete(wemallFreightInfo);
		addMessage(redirectAttributes, "删除物流明细信息成功");
		return "redirect:"+Global.getAdminPath()+"/wemall/wemallFreightInfo/?repage";
	}

}
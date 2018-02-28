/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.fulltl.wemall.modules.wemall.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fulltl.wemall.common.config.Global;
import com.fulltl.wemall.common.persistence.Page;
import com.fulltl.wemall.common.web.BaseController;
import com.fulltl.wemall.common.utils.StringUtils;
import com.fulltl.wemall.modules.wemall.entity.WemallSpec;
import com.fulltl.wemall.modules.wemall.entity.WemallSpecInfo;
import com.fulltl.wemall.modules.wemall.service.WemallSpecInfoService;

/**
 * 属性值管理Controller
 * @author ldk
 * @version 2018-01-05
 */
@Controller
@RequestMapping(value = "${adminPath}/wemall/wemallSpecInfo")
public class WemallSpecInfoController extends BaseController {

	@Autowired
	private WemallSpecInfoService wemallSpecInfoService;
	
	@ModelAttribute
	public WemallSpecInfo get(@RequestParam(required=false) String id) {
		WemallSpecInfo entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = wemallSpecInfoService.get(id);
		}
		if (entity == null){
			entity = new WemallSpecInfo();
		}
		return entity;
	}
	
	@RequiresPermissions("wemall:wemallSpecInfo:view")
	@RequestMapping(value = {"list", ""})
	public String list(WemallSpecInfo wemallSpecInfo, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<WemallSpecInfo> page = wemallSpecInfoService.findPage(new Page<WemallSpecInfo>(request, response), wemallSpecInfo); 
		model.addAttribute("page", page);
		return "modules/wemall/wemallSpecInfoList";
	}

	@RequestMapping(value = {"wemallSpecInfoList"})
	@ResponseBody
	public List<WemallSpecInfo> wemallSpecInfoList(WemallSpecInfo wemallSpecInfo, Model model) {
		List<WemallSpecInfo> list = wemallSpecInfoService.findList(wemallSpecInfo); 
		return list;
	}
	
	@RequiresPermissions("wemall:wemallSpecInfo:view")
	@RequestMapping(value = "form")
	public String form(WemallSpecInfo wemallSpecInfo, Model model) {
		model.addAttribute("wemallSpecInfo", wemallSpecInfo);
		return "modules/wemall/wemallSpecInfoForm";
	}

	@RequiresPermissions("wemall:wemallSpecInfo:edit")
	@RequestMapping(value = "save")
	public String save(WemallSpecInfo wemallSpecInfo, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, wemallSpecInfo)){
			return form(wemallSpecInfo, model);
		}
		wemallSpecInfoService.save(wemallSpecInfo);
		addMessage(redirectAttributes, "保存属性值信息成功");
		return "redirect:"+Global.getAdminPath()+"/wemall/wemallSpecInfo/?repage";
	}
	
	@RequiresPermissions("wemall:wemallSpecInfo:edit")
	@RequestMapping(value = "delete")
	public String delete(WemallSpecInfo wemallSpecInfo, RedirectAttributes redirectAttributes) {
		wemallSpecInfoService.delete(wemallSpecInfo);
		addMessage(redirectAttributes, "删除属性值信息成功");
		return "redirect:"+Global.getAdminPath()+"/wemall/wemallSpecInfo/?repage";
	}

}
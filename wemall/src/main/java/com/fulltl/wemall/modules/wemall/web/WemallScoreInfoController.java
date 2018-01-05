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
import com.fulltl.wemall.modules.wemall.entity.WemallScoreInfo;
import com.fulltl.wemall.modules.wemall.service.WemallScoreInfoService;

/**
 * 积分明细管理Controller
 * @author ldk
 * @version 2018-01-05
 */
@Controller
@RequestMapping(value = "${adminPath}/wemall/wemallScoreInfo")
public class WemallScoreInfoController extends BaseController {

	@Autowired
	private WemallScoreInfoService wemallScoreInfoService;
	
	@ModelAttribute
	public WemallScoreInfo get(@RequestParam(required=false) String id) {
		WemallScoreInfo entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = wemallScoreInfoService.get(id);
		}
		if (entity == null){
			entity = new WemallScoreInfo();
		}
		return entity;
	}
	
	@RequiresPermissions("wemall:wemallScoreInfo:view")
	@RequestMapping(value = {"list", ""})
	public String list(WemallScoreInfo wemallScoreInfo, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<WemallScoreInfo> page = wemallScoreInfoService.findPage(new Page<WemallScoreInfo>(request, response), wemallScoreInfo); 
		model.addAttribute("page", page);
		return "modules/wemall/wemallScoreInfoList";
	}

	@RequiresPermissions("wemall:wemallScoreInfo:view")
	@RequestMapping(value = "form")
	public String form(WemallScoreInfo wemallScoreInfo, Model model) {
		model.addAttribute("wemallScoreInfo", wemallScoreInfo);
		return "modules/wemall/wemallScoreInfoForm";
	}

	@RequiresPermissions("wemall:wemallScoreInfo:edit")
	@RequestMapping(value = "save")
	public String save(WemallScoreInfo wemallScoreInfo, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, wemallScoreInfo)){
			return form(wemallScoreInfo, model);
		}
		wemallScoreInfoService.save(wemallScoreInfo);
		addMessage(redirectAttributes, "保存积分明细成功");
		return "redirect:"+Global.getAdminPath()+"/wemall/wemallScoreInfo/?repage";
	}
	
	@RequiresPermissions("wemall:wemallScoreInfo:edit")
	@RequestMapping(value = "delete")
	public String delete(WemallScoreInfo wemallScoreInfo, RedirectAttributes redirectAttributes) {
		wemallScoreInfoService.delete(wemallScoreInfo);
		addMessage(redirectAttributes, "删除积分明细成功");
		return "redirect:"+Global.getAdminPath()+"/wemall/wemallScoreInfo/?repage";
	}

}
/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.fulltl.wemall.modules.cms.web;

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
import com.fulltl.wemall.modules.cms.entity.CmsPushTag;
import com.fulltl.wemall.modules.cms.service.CmsPushTagService;

/**
 * 标签管理Controller
 * @author ldk
 * @version 2017-12-09
 */
@Controller
@RequestMapping(value = "${adminPath}/cms/cmsPushTag")
public class CmsPushTagController extends BaseController {

	@Autowired
	private CmsPushTagService cmsPushTagService;
	
	@ModelAttribute
	public CmsPushTag get(@RequestParam(required=false) String id) {
		CmsPushTag entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = cmsPushTagService.get(id);
		}
		if (entity == null){
			entity = new CmsPushTag();
		}
		return entity;
	}
	
	@RequiresPermissions("cms:cmsPushTag:view")
	@RequestMapping(value = {"list", ""})
	public String list(CmsPushTag cmsPushTag, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<CmsPushTag> page = cmsPushTagService.findPage(new Page<CmsPushTag>(request, response), cmsPushTag); 
		model.addAttribute("page", page);
		return "modules/cms/cmsPushTagList";
	}

	@RequiresPermissions("cms:cmsPushTag:view")
	@RequestMapping(value = "form")
	public String form(CmsPushTag cmsPushTag, Model model) {
		model.addAttribute("cmsPushTag", cmsPushTag);
		return "modules/cms/cmsPushTagForm";
	}

	@RequiresPermissions("cms:cmsPushTag:edit")
	@RequestMapping(value = "save")
	public String save(CmsPushTag cmsPushTag, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, cmsPushTag)){
			return form(cmsPushTag, model);
		}
		cmsPushTagService.save(cmsPushTag);
		addMessage(redirectAttributes, "保存标签成功");
		return "redirect:"+Global.getAdminPath()+"/cms/cmsPushTag/?repage";
	}
	
	@RequiresPermissions("cms:cmsPushTag:edit")
	@RequestMapping(value = "delete")
	public String delete(CmsPushTag cmsPushTag, RedirectAttributes redirectAttributes) {
		cmsPushTagService.delete(cmsPushTag);
		addMessage(redirectAttributes, "删除标签成功");
		return "redirect:"+Global.getAdminPath()+"/cms/cmsPushTag/?repage";
	}

}
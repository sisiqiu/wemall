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
import com.fulltl.wemall.modules.cms.entity.CmsUserRegid;
import com.fulltl.wemall.modules.cms.service.CmsUserRegidService;

/**
 * 用户-极光注册id管理Controller
 * @author ldk
 * @version 2017-12-09
 */
@Controller
@RequestMapping(value = "${adminPath}/cms/cmsUserRegid")
public class CmsUserRegidController extends BaseController {

	@Autowired
	private CmsUserRegidService cmsUserRegidService;
	
	@ModelAttribute
	public CmsUserRegid get(@RequestParam(required=false) String id) {
		CmsUserRegid entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = cmsUserRegidService.get(id);
		}
		if (entity == null){
			entity = new CmsUserRegid();
		}
		return entity;
	}
	
	@RequiresPermissions("cms:cmsUserRegid:view")
	@RequestMapping(value = {"list", ""})
	public String list(CmsUserRegid cmsUserRegid, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<CmsUserRegid> page = cmsUserRegidService.findPage(new Page<CmsUserRegid>(request, response), cmsUserRegid); 
		model.addAttribute("page", page);
		return "modules/cms/cmsUserRegidList";
	}

	@RequiresPermissions("cms:cmsUserRegid:view")
	@RequestMapping(value = "form")
	public String form(CmsUserRegid cmsUserRegid, Model model) {
		model.addAttribute("cmsUserRegid", cmsUserRegid);
		return "modules/cms/cmsUserRegidForm";
	}

	@RequiresPermissions("cms:cmsUserRegid:edit")
	@RequestMapping(value = "save")
	public String save(CmsUserRegid cmsUserRegid, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, cmsUserRegid)){
			return form(cmsUserRegid, model);
		}
		cmsUserRegidService.save(cmsUserRegid);
		addMessage(redirectAttributes, "保存用户-极光注册id成功");
		return "redirect:"+Global.getAdminPath()+"/cms/cmsUserRegid/?repage";
	}
	
	@RequiresPermissions("cms:cmsUserRegid:edit")
	@RequestMapping(value = "delete")
	public String delete(CmsUserRegid cmsUserRegid, RedirectAttributes redirectAttributes) {
		cmsUserRegidService.delete(cmsUserRegid);
		addMessage(redirectAttributes, "删除用户-极光注册id成功");
		return "redirect:"+Global.getAdminPath()+"/cms/cmsUserRegid/?repage";
	}

}
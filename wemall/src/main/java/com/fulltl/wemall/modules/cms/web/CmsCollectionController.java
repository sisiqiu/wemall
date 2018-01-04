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
import com.fulltl.wemall.common.utils.StringUtils;
import com.fulltl.wemall.common.web.BaseController;
import com.fulltl.wemall.modules.cms.entity.CmsCollection;
import com.fulltl.wemall.modules.cms.service.CmsCollectionService;


/**
 * 对用户所收藏的商品进行管理Controller
 * @author hj
 * @version 2017-11-15
 */
@Controller
@RequestMapping(value = "${adminPath}/cms/cmsCollection")
public class CmsCollectionController extends BaseController {

	@Autowired
	private CmsCollectionService cmsCollectionService;
	
	@ModelAttribute
	public CmsCollection get(@RequestParam(required=false) String id) {
		CmsCollection entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = cmsCollectionService.get(id);
		}
		if (entity == null){
			entity = new CmsCollection();
		}
		return entity;
	}
	
	@RequiresPermissions("cms:cmsCollection:view")
	@RequestMapping(value = {"list", ""})
	public String list(CmsCollection cmsCollection, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<CmsCollection> page = cmsCollectionService.findPage(new Page<CmsCollection>(request, response), cmsCollection); 
		model.addAttribute("page", page);
		return "modules/cms/cmsCollectionList";
	}

	@RequiresPermissions("cms:cmsCollection:view")
	@RequestMapping(value = "form")
	public String form(CmsCollection cmsCollection, Model model) {
		model.addAttribute("cmsCollection", cmsCollection);
		return "modules/cms/cmsCollectionForm";
	}

	@RequiresPermissions("cms:cmsCollection:edit")
	@RequestMapping(value = "save")
	public String save(CmsCollection cmsCollection, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, cmsCollection)){
			return form(cmsCollection, model);
		}
		cmsCollectionService.save(cmsCollection);
		addMessage(redirectAttributes, "保存个人收藏管理成功");
		return "redirect:"+Global.getAdminPath()+"/cms/cmsCollection/?repage";
	}
	
	@RequiresPermissions("cms:cmsCollection:edit")
	@RequestMapping(value = "delete")
	public String delete(CmsCollection cmsCollection, RedirectAttributes redirectAttributes) {
		cmsCollectionService.delete(cmsCollection);
		addMessage(redirectAttributes, "删除个人收藏管理成功");
		return "redirect:"+Global.getAdminPath()+"/cms/cmsCollection/?repage";
	}

}
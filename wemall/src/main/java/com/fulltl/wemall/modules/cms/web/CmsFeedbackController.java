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
import com.fulltl.wemall.modules.cms.entity.CmsFeedback;
import com.fulltl.wemall.modules.cms.service.CmsFeedbackService;

/**
 * 管理用户反馈的信息Controller
 * @author hj
 * @version 2017-11-02
 */
@Controller
@RequestMapping(value = "${adminPath}/cms/cmsFeedback")
public class CmsFeedbackController extends BaseController {

	@Autowired
	private CmsFeedbackService cmsFeedbackService;
	
	@ModelAttribute
	public CmsFeedback get(@RequestParam(required=false) String id) {
		CmsFeedback entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = cmsFeedbackService.get(id);
		}
		if (entity == null){
			entity = new CmsFeedback();
		}
		return entity;
	}
	
	@RequiresPermissions("cms:cmsFeedback:view")
	@RequestMapping(value = {"list", ""})
	public String list(CmsFeedback cmsFeedback, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<CmsFeedback> page = cmsFeedbackService.findPage(new Page<CmsFeedback>(request, response), cmsFeedback); 
		model.addAttribute("page", page);
		return "modules/cms/cmsFeedbackList";
	}

	@RequiresPermissions("cms:cmsFeedback:view")
	@RequestMapping(value = "form")
	public String form(CmsFeedback cmsFeedback, Model model) {
		model.addAttribute("cmsFeedback", cmsFeedback);
		return "modules/cms/cmsFeedbackForm";
	}

	@RequiresPermissions("cms:cmsFeedback:edit")
	@RequestMapping(value = "save")
	public String save(CmsFeedback cmsFeedback, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, cmsFeedback)){
			return form(cmsFeedback, model);
		}
		cmsFeedbackService.save(cmsFeedback);
		addMessage(redirectAttributes, "保存用户反馈管理成功");
		return "redirect:"+Global.getAdminPath()+"/cms/cmsFeedback/?repage";
	}
	
	@RequiresPermissions("cms:cmsFeedback:edit")
	@RequestMapping(value = "delete")
	public String delete(CmsFeedback cmsFeedback, RedirectAttributes redirectAttributes) {
		cmsFeedbackService.delete(cmsFeedback);
		addMessage(redirectAttributes, "删除用户反馈管理成功");
		return "redirect:"+Global.getAdminPath()+"/cms/cmsFeedback/?repage";
	}

}
/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.fulltl.wemall.modules.wx.web;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

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
import com.fulltl.wemall.modules.wx.entity.ConActivity;
import com.fulltl.wemall.modules.wx.service.ConActivityService;

/**
 * 活动表Controller
 * @author 黄健
 * @version 2017-10-14
 */
@Controller
@RequestMapping(value = "${adminPath}/wx/conActivity")
public class ConActivityController extends BaseController {

	@Autowired
	private ConActivityService conActivityService;
	
	@ModelAttribute
	public ConActivity get(@RequestParam(required=false) String id) {
		ConActivity entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = conActivityService.get(id);
		}
		if (entity == null){
			entity = new ConActivity();
		}
		return entity;
	}
	
	@RequiresPermissions("wx:conActivity:view")
	@RequestMapping(value = {"list", ""})
	public String list(ConActivity conActivity, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<ConActivity> page = conActivityService.findPage(new Page<ConActivity>(request, response), conActivity); 
		model.addAttribute("page", page);
		return "modules/wx/conActivityList";
	}

	@RequiresPermissions("wx:conActivity:view")
	@RequestMapping(value = "form")
	public String form(ConActivity conActivity, Model model) {
		model.addAttribute("conActivity", conActivity);
		return "modules/wx/conActivityForm";
	}

	@RequiresPermissions("wx:conActivity:edit")
	@RequestMapping(value = "save")
	public String save(ConActivity conActivity, Model model, RedirectAttributes redirectAttributes) {
		if(StringUtils.isNotBlank(conActivity.getResourcepath())) {
			try {
				conActivity.setResourcepath(URLDecoder.decode(conActivity.getResourcepath(), "UTF-8"));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		if (!beanValidator(model, conActivity)){
			return form(conActivity, model);
		}
		conActivityService.save(conActivity);
		addMessage(redirectAttributes, "保存活动表成功");
		return "redirect:"+Global.getAdminPath()+"/wx/conActivity/?repage";
	}
	
	@RequiresPermissions("wx:conActivity:edit")
	@RequestMapping(value = "delete")
	public String delete(ConActivity conActivity, RedirectAttributes redirectAttributes) {
		conActivityService.delete(conActivity);
		addMessage(redirectAttributes, "删除活动表成功");
		return "redirect:"+Global.getAdminPath()+"/wx/conActivity/?repage";
	}

}
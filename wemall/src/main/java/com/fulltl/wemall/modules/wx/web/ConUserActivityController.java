/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.fulltl.wemall.modules.wx.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.sun.tools.javac.util.List;
import com.fulltl.wemall.common.config.Global;
import com.fulltl.wemall.common.persistence.Page;
import com.fulltl.wemall.common.web.BaseController;
import com.fulltl.wemall.common.utils.DateUtils;
import com.fulltl.wemall.common.utils.StringUtils;
import com.fulltl.wemall.common.utils.excel.ExportExcel;
import com.fulltl.wemall.modules.sys.entity.User;
import com.fulltl.wemall.modules.wx.entity.ConUserActivity;
import com.fulltl.wemall.modules.wx.service.ConUserActivityService;

/**
 * gvvController
 * @author bhcfg
 * @version 2017-10-14
 */
@Controller
@RequestMapping(value = "${adminPath}/wx/conUserActivity")
public class ConUserActivityController extends BaseController {

	@Autowired
	private ConUserActivityService conUserActivityService;
	
	@ModelAttribute
	public ConUserActivity get(@RequestParam(required=false) String id) {
		ConUserActivity entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = conUserActivityService.get(id);
		}
		if (entity == null){
			entity = new ConUserActivity();
		}
		return entity;
	}
	
	@RequiresPermissions("wx:conUserActivity:view")
	@RequestMapping(value = {"list", ""})
	public String list(ConUserActivity conUserActivity, HttpServletRequest request, HttpServletResponse response, Model model) {
		System.out.println("进入查询方法");
		
		//这个代码   
		Page<ConUserActivity> page = conUserActivityService.findPage(new Page<ConUserActivity>(request, response), conUserActivity); 
		
		for(ConUserActivity obj :page.getList()){
			System.out.println("用户活动数据="+obj.getId()+"\n"+obj.getActivityid());
		}
		model.addAttribute("page", page);
		return "modules/wx/conUserActivityList";
	}

	@RequiresPermissions("wx:conUserActivity:view")
	@RequestMapping(value = "form")
	public String form(ConUserActivity conUserActivity, Model model) {
		model.addAttribute("conUserActivity", conUserActivity);
		return "modules/wx/conUserActivityForm";
	}

	@RequiresPermissions("wx:conUserActivity:edit")
	@RequestMapping(value = "save")
	public String save(ConUserActivity conUserActivity, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, conUserActivity)){
			return form(conUserActivity, model);
		}
		conUserActivityService.save(conUserActivity);
		addMessage(redirectAttributes, "保存gbv成功");
		return "redirect:"+Global.getAdminPath()+"/wx/conUserActivity/?repage";
	}
	
	@RequiresPermissions("wx:conUserActivity:edit")
	@RequestMapping(value = "delete")
	public String delete(ConUserActivity conUserActivity, RedirectAttributes redirectAttributes) {
		conUserActivityService.delete(conUserActivity);
		addMessage(redirectAttributes, "删除gbv成功");
		return "redirect:"+Global.getAdminPath()+"/wx/conUserActivity/?repage";
	}

	@RequiresPermissions("wx:conUserActivity:view")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public String exportFile(ConUserActivity conUserActivity, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
			String fileName = "用户活动数据"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
			Page<ConUserActivity> page = conUserActivityService.findPage(new Page<ConUserActivity>(request, response), conUserActivity); 
    		new ExportExcel("用户活动数据", ConUserActivity.class).setDataList(page.getList()).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出用户失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/wx/conUserActivity/?repage";
    }
	
}
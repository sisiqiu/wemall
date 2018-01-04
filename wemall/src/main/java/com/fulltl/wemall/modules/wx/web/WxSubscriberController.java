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

import com.fulltl.wemall.common.config.Global;
import com.fulltl.wemall.common.persistence.Page;
import com.fulltl.wemall.common.utils.DateUtils;
import com.fulltl.wemall.common.utils.StringUtils;
import com.fulltl.wemall.common.utils.excel.ExportExcel;
import com.fulltl.wemall.common.web.BaseController;
import com.fulltl.wemall.modules.wx.entity.ConUserActivity;
import com.fulltl.wemall.modules.wx.entity.WxSubscriber;
import com.fulltl.wemall.modules.wx.service.WxSubscriberService;

/**
 * 微信关注用户管理Controller
 * @author ldk
 * @version 2017-10-13
 */
@Controller
@RequestMapping(value = "${adminPath}/wx/wxSubscriber")
public class WxSubscriberController extends BaseController {

	@Autowired
	private WxSubscriberService wxSubscriberService;
	
	@ModelAttribute
	public WxSubscriber get(@RequestParam(required=false) String id) {
		WxSubscriber entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = wxSubscriberService.get(id);
		}
		if (entity == null){
			entity = new WxSubscriber();
		}
		return entity;
	}
	
	@RequiresPermissions("wx:wxSubscriber:view")
	@RequestMapping(value = {"list", ""})
	public String list(WxSubscriber wxSubscriber, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<WxSubscriber> page = wxSubscriberService.findPage(new Page<WxSubscriber>(request, response), wxSubscriber); 
		model.addAttribute("page", page);
		return "modules/wx/wxSubscriberList";
	}

	@RequiresPermissions("wx:wxSubscriber:view")
	@RequestMapping(value = "form")
	public String form(WxSubscriber wxSubscriber, Model model) {
		model.addAttribute("wxSubscriber", wxSubscriber);
		return "modules/wx/wxSubscriberForm";
	}

	@RequiresPermissions("wx:wxSubscriber:edit")
	@RequestMapping(value = "save")
	public String save(WxSubscriber wxSubscriber, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, wxSubscriber)){
			return form(wxSubscriber, model);
		}
		wxSubscriberService.save(wxSubscriber);
		addMessage(redirectAttributes, "保存微信关注用户成功");
		return "redirect:"+Global.getAdminPath()+"/wx/wxSubscriber/?repage";
	}
	
	@RequiresPermissions("wx:wxSubscriber:edit")
	@RequestMapping(value = "delete")
	public String delete(WxSubscriber wxSubscriber, RedirectAttributes redirectAttributes) {
		wxSubscriberService.delete(wxSubscriber);
		addMessage(redirectAttributes, "删除微信关注用户成功");
		return "redirect:"+Global.getAdminPath()+"/wx/wxSubscriber/?repage";
	}
	
	@RequiresPermissions("wx:wxSubscriber:view")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public String exportFile(WxSubscriber wxSubscriber, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
			String fileName = "微信关注用户数据"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
			Page<WxSubscriber> page = wxSubscriberService.findPage(new Page<WxSubscriber>(request, response), wxSubscriber); 
    		new ExportExcel("微信关注用户数据", WxSubscriber.class).setDataList(page.getList()).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出微信关注用户失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/wx/wxSubscriber/?repage";
    }

}
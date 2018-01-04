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
import com.fulltl.wemall.modules.wx.entity.WxSubscriber;
import com.fulltl.wemall.modules.wx.entity.WxUserinfo;
import com.fulltl.wemall.modules.wx.service.WxSubscriberService;
import com.fulltl.wemall.modules.wx.service.WxUserinfoService;

/**
 * 微信绑定用户管理Controller
 * @author ldk
 * @version 2017-10-13
 */
@Controller
@RequestMapping(value = "${adminPath}/wx/wxUserinfo")
public class WxUserinfoController extends BaseController {

	@Autowired
	private WxUserinfoService wxUserinfoService;
	@Autowired
	private WxSubscriberService wxSubscriberService;
	
	@ModelAttribute
	public WxUserinfo get(@RequestParam(required=false) String id) {
		WxUserinfo entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = wxUserinfoService.get(id);
		}
		if (entity == null){
			entity = new WxUserinfo();
		}
		return entity;
	}
	
	@RequiresPermissions("wx:wxUserinfo:view")
	@RequestMapping(value = {"list", ""})
	public String list(WxUserinfo wxUserinfo, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<WxUserinfo> page = wxUserinfoService.findPage(new Page<WxUserinfo>(request, response), wxUserinfo); 
		model.addAttribute("page", page);
		return "modules/wx/wxUserinfoList";
	}

	@RequiresPermissions("wx:wxUserinfo:view")
	@RequestMapping(value = "form")
	public String form(WxUserinfo wxUserinfo, Model model) {
		model.addAttribute("wxUserinfo", wxUserinfo);
		return "modules/wx/wxUserinfoForm";
	}

	@RequiresPermissions("wx:wxUserinfo:edit")
	@RequestMapping(value = "save")
	public String save(WxUserinfo wxUserinfo, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, wxUserinfo)){
			return form(wxUserinfo, model);
		}
		
		WxSubscriber subscriber = wxSubscriberService.get(wxUserinfo.getSubscriberId().toString());
		if(subscriber == null) {
			addMessage(model, "数据验证失败：", "关注用户ID需要是微信关注用户的主键id！");
			return form(wxUserinfo, model);
		}
		wxUserinfoService.save(wxUserinfo);
		addMessage(redirectAttributes, "保存微信绑定用户成功");
		return "redirect:"+Global.getAdminPath()+"/wx/wxUserinfo/?repage";
	}
	
	@RequiresPermissions("wx:wxUserinfo:edit")
	@RequestMapping(value = "delete")
	public String delete(WxUserinfo wxUserinfo, RedirectAttributes redirectAttributes) {
		wxUserinfoService.delete(wxUserinfo);
		addMessage(redirectAttributes, "删除微信绑定用户成功");
		return "redirect:"+Global.getAdminPath()+"/wx/wxUserinfo/?repage";
	}
	
	@RequiresPermissions("wx:wxUserinfo:view")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public String exportFile(WxUserinfo wxUserinfo, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
			String fileName = "微信绑定用户数据"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
			Page<WxUserinfo> page = wxUserinfoService.findPage(new Page<WxUserinfo>(request, response), wxUserinfo); 
    		new ExportExcel("微信绑定用户数据", WxUserinfo.class).setDataList(page.getList()).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出微信绑定用户失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/wx/wxUserinfo/?repage";
    }

}
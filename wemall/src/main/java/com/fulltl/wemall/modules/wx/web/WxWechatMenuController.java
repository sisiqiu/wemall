/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.fulltl.wemall.modules.wx.web;

import java.util.List;
import java.util.Map;

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

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.fulltl.wemall.common.config.Global;
import com.fulltl.wemall.common.utils.StringUtils;
import com.fulltl.wemall.common.web.BaseController;
import com.fulltl.wemall.modules.wx.entity.WxWechatMenu;
import com.fulltl.wemall.modules.wx.service.WxWechatMenuService;

/**
 * 微信服务号菜单管理Controller
 * @author ldk
 * @version 2017-10-13
 */
@Controller
@RequestMapping(value = "${adminPath}/wx/wxWechatMenu")
public class WxWechatMenuController extends BaseController {

	@Autowired
	private WxWechatMenuService wxWechatMenuService;
	
	@ModelAttribute
	public WxWechatMenu get(@RequestParam(required=false) String id) {
		WxWechatMenu entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = wxWechatMenuService.get(id);
		}
		if (entity == null){
			entity = new WxWechatMenu();
		}
		return entity;
	}
	
	@RequiresPermissions("wx:wxWechatMenu:view")
	@RequestMapping(value = {"list", ""})
	public String list(WxWechatMenu wxWechatMenu, HttpServletRequest request, HttpServletResponse response, Model model) {
		List<WxWechatMenu> list = wxWechatMenuService.findList(wxWechatMenu); 
		model.addAttribute("list", list);
		return "modules/wx/wxWechatMenuList";
	}

	@RequiresPermissions("wx:wxWechatMenu:view")
	@RequestMapping(value = "form")
	public String form(WxWechatMenu wxWechatMenu, Model model) {
		if (wxWechatMenu.getParent()!=null && StringUtils.isNotBlank(wxWechatMenu.getParent().getId())){
			wxWechatMenu.setParent(wxWechatMenuService.get(wxWechatMenu.getParent().getId()));
			// 获取排序号，最末节点排序号+30
			if (StringUtils.isBlank(wxWechatMenu.getId())){
				WxWechatMenu wxWechatMenuChild = new WxWechatMenu();
				wxWechatMenuChild.setParent(new WxWechatMenu(wxWechatMenu.getParent().getId()));
				List<WxWechatMenu> list = wxWechatMenuService.findList(wxWechatMenu); 
				if (list.size() > 0){
					wxWechatMenu.setSort(list.get(list.size()-1).getSort());
					if (wxWechatMenu.getSort() != null){
						wxWechatMenu.setSort(wxWechatMenu.getSort() + 30);
					}
				}
			}
		}
		if (wxWechatMenu.getSort() == null){
			wxWechatMenu.setSort(30);
		}
		model.addAttribute("wxWechatMenu", wxWechatMenu);
		return "modules/wx/wxWechatMenuForm";
	}

	@RequiresPermissions("wx:wxWechatMenu:edit")
	@RequestMapping(value = "save")
	public String save(WxWechatMenu wxWechatMenu, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, wxWechatMenu)){
			return form(wxWechatMenu, model);
		}
		
		wxWechatMenu.setUrl(this.escapeHtml(wxWechatMenu.getUrl()));
		wxWechatMenuService.save(wxWechatMenu);
		addMessage(redirectAttributes, "保存微信服务号菜单成功");
		return "redirect:"+Global.getAdminPath()+"/wx/wxWechatMenu/?repage";
	}
	
	@RequiresPermissions("wx:wxWechatMenu:edit")
	@RequestMapping(value = "delete")
	public String delete(WxWechatMenu wxWechatMenu, RedirectAttributes redirectAttributes) {
		wxWechatMenuService.delete(wxWechatMenu);
		addMessage(redirectAttributes, "删除微信服务号菜单成功");
		return "redirect:"+Global.getAdminPath()+"/wx/wxWechatMenu/?repage";
	}

	@RequiresPermissions("user")
	@ResponseBody
	@RequestMapping(value = "treeData")
	public List<Map<String, Object>> treeData(@RequestParam(required=false) String extId, HttpServletResponse response) {
		List<Map<String, Object>> mapList = Lists.newArrayList();
		List<WxWechatMenu> list = wxWechatMenuService.findList(new WxWechatMenu());
		for (int i=0; i<list.size(); i++){
			WxWechatMenu e = list.get(i);
			if (StringUtils.isBlank(extId) || (extId!=null && !extId.equals(e.getId()) && e.getParentIds().indexOf(","+extId+",")==-1)){
				Map<String, Object> map = Maps.newHashMap();
				map.put("id", e.getId());
				map.put("pId", e.getParentId());
				map.put("name", e.getName());
				mapList.add(map);
			}
		}
		return mapList;
	}
	
	/**
	 * 获取转译后的字符串
	 * @param str
	 * @return
	 */
	private String escapeHtml(String str) {
		return str.replaceAll("&amp;", "&")
			.replaceAll("&lt;", "<")
			.replaceAll("&gt;", ">")
			.replaceAll("&quot;", "\"")
			.replaceAll("&#39;", "'")
			.replaceAll("&#x2F;", "/");
	}
}
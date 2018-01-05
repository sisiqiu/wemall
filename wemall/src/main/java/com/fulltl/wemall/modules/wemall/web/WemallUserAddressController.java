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
import com.fulltl.wemall.modules.wemall.entity.WemallUserAddress;
import com.fulltl.wemall.modules.wemall.service.WemallUserAddressService;

/**
 * 收货地址管理Controller
 * @author ldk
 * @version 2018-01-05
 */
@Controller
@RequestMapping(value = "${adminPath}/wemall/wemallUserAddress")
public class WemallUserAddressController extends BaseController {

	@Autowired
	private WemallUserAddressService wemallUserAddressService;
	
	@ModelAttribute
	public WemallUserAddress get(@RequestParam(required=false) String id) {
		WemallUserAddress entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = wemallUserAddressService.get(id);
		}
		if (entity == null){
			entity = new WemallUserAddress();
		}
		return entity;
	}
	
	@RequiresPermissions("wemall:wemallUserAddress:view")
	@RequestMapping(value = {"list", ""})
	public String list(WemallUserAddress wemallUserAddress, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<WemallUserAddress> page = wemallUserAddressService.findPage(new Page<WemallUserAddress>(request, response), wemallUserAddress); 
		model.addAttribute("page", page);
		return "modules/wemall/wemallUserAddressList";
	}

	@RequiresPermissions("wemall:wemallUserAddress:view")
	@RequestMapping(value = "form")
	public String form(WemallUserAddress wemallUserAddress, Model model) {
		model.addAttribute("wemallUserAddress", wemallUserAddress);
		return "modules/wemall/wemallUserAddressForm";
	}

	@RequiresPermissions("wemall:wemallUserAddress:edit")
	@RequestMapping(value = "save")
	public String save(WemallUserAddress wemallUserAddress, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, wemallUserAddress)){
			return form(wemallUserAddress, model);
		}
		wemallUserAddressService.save(wemallUserAddress);
		addMessage(redirectAttributes, "保存收货地址成功");
		return "redirect:"+Global.getAdminPath()+"/wemall/wemallUserAddress/?repage";
	}
	
	@RequiresPermissions("wemall:wemallUserAddress:edit")
	@RequestMapping(value = "delete")
	public String delete(WemallUserAddress wemallUserAddress, RedirectAttributes redirectAttributes) {
		wemallUserAddressService.delete(wemallUserAddress);
		addMessage(redirectAttributes, "删除收货地址成功");
		return "redirect:"+Global.getAdminPath()+"/wemall/wemallUserAddress/?repage";
	}

}
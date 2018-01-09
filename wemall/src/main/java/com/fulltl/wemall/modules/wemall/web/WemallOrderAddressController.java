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
import com.fulltl.wemall.modules.wemall.entity.WemallOrderAddress;
import com.fulltl.wemall.modules.wemall.service.WemallOrderAddressService;

/**
 * 订单-地址管理Controller
 * @author ldk
 * @version 2018-01-05
 */
@Controller
@RequestMapping(value = "${adminPath}/wemall/wemallOrderAddress")
public class WemallOrderAddressController extends BaseController {

	@Autowired
	private WemallOrderAddressService wemallOrderAddressService;
	
	@ModelAttribute
	public WemallOrderAddress get(@RequestParam(required=false) String id) {
		WemallOrderAddress entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = wemallOrderAddressService.get(id);
		}
		if (entity == null){
			entity = new WemallOrderAddress();
		}
		return entity;
	}
	
	@RequiresPermissions("wemall:wemallOrderAddress:view")
	@RequestMapping(value = {"list", ""})
	public String list(WemallOrderAddress wemallOrderAddress, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<WemallOrderAddress> page = wemallOrderAddressService.findPage(new Page<WemallOrderAddress>(request, response), wemallOrderAddress); 
		model.addAttribute("page", page);
		return "modules/wemall/wemallOrderAddressList";
	}

	@RequiresPermissions("wemall:wemallOrderAddress:view")
	@RequestMapping(value = "form")
	public String form(WemallOrderAddress wemallOrderAddress, Model model) {
		model.addAttribute("wemallOrderAddress", wemallOrderAddress);
		return "modules/wemall/wemallOrderAddressForm";
	}

	@RequiresPermissions("wemall:wemallOrderAddress:edit")
	@RequestMapping(value = "save")
	public String save(WemallOrderAddress wemallOrderAddress, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, wemallOrderAddress)){
			return form(wemallOrderAddress, model);
		}
		wemallOrderAddressService.save(wemallOrderAddress);
		addMessage(redirectAttributes, "保存订单-地址信息成功");
		return "redirect:"+Global.getAdminPath()+"/wemall/wemallOrderAddress/?repage";
	}
	
	@RequiresPermissions("wemall:wemallOrderAddress:edit")
	@RequestMapping(value = "delete")
	public String delete(WemallOrderAddress wemallOrderAddress, RedirectAttributes redirectAttributes) {
		wemallOrderAddressService.delete(wemallOrderAddress);
		addMessage(redirectAttributes, "删除订单-地址信息成功");
		return "redirect:"+Global.getAdminPath()+"/wemall/wemallOrderAddress/?repage";
	}

}
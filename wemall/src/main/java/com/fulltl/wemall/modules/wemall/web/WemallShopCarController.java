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
import com.fulltl.wemall.modules.wemall.entity.WemallShopCar;
import com.fulltl.wemall.modules.wemall.service.WemallShopCarService;

/**
 * 购物车管理Controller
 * @author ldk
 * @version 2018-01-05
 */
@Controller
@RequestMapping(value = "${adminPath}/wemall/wemallShopCar")
public class WemallShopCarController extends BaseController {

	@Autowired
	private WemallShopCarService wemallShopCarService;
	
	@ModelAttribute
	public WemallShopCar get(@RequestParam(required=false) String id) {
		WemallShopCar entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = wemallShopCarService.get(id);
		}
		if (entity == null){
			entity = new WemallShopCar();
		}
		return entity;
	}
	
	@RequiresPermissions("wemall:wemallShopCar:view")
	@RequestMapping(value = {"list", ""})
	public String list(WemallShopCar wemallShopCar, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<WemallShopCar> page = wemallShopCarService.findPage(new Page<WemallShopCar>(request, response), wemallShopCar); 
		model.addAttribute("page", page);
		return "modules/wemall/wemallShopCarList";
	}

	@RequiresPermissions("wemall:wemallShopCar:view")
	@RequestMapping(value = "form")
	public String form(WemallShopCar wemallShopCar, Model model) {
		model.addAttribute("wemallShopCar", wemallShopCar);
		return "modules/wemall/wemallShopCarForm";
	}

	@RequiresPermissions("wemall:wemallShopCar:edit")
	@RequestMapping(value = "save")
	public String save(WemallShopCar wemallShopCar, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, wemallShopCar)){
			return form(wemallShopCar, model);
		}
		wemallShopCarService.save(wemallShopCar);
		addMessage(redirectAttributes, "保存购物车信息成功");
		return "redirect:"+Global.getAdminPath()+"/wemall/wemallShopCar/?repage";
	}
	
	@RequiresPermissions("wemall:wemallShopCar:edit")
	@RequestMapping(value = "delete")
	public String delete(WemallShopCar wemallShopCar, RedirectAttributes redirectAttributes) {
		wemallShopCarService.delete(wemallShopCar);
		addMessage(redirectAttributes, "删除购物车信息成功");
		return "redirect:"+Global.getAdminPath()+"/wemall/wemallShopCar/?repage";
	}

}
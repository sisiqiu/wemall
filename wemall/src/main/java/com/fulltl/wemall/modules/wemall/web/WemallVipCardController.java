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
import com.fulltl.wemall.modules.wemall.entity.WemallVipCard;
import com.fulltl.wemall.modules.wemall.service.WemallVipCardService;

/**
 * 会员卡管理Controller
 * @author ldk
 * @version 2018-01-05
 */
@Controller
@RequestMapping(value = "${adminPath}/wemall/wemallVipCard")
public class WemallVipCardController extends BaseController {

	@Autowired
	private WemallVipCardService wemallVipCardService;
	
	@ModelAttribute
	public WemallVipCard get(@RequestParam(required=false) String id) {
		WemallVipCard entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = wemallVipCardService.get(id);
		}
		if (entity == null){
			entity = new WemallVipCard();
		}
		return entity;
	}
	
	@RequiresPermissions("wemall:wemallVipCard:view")
	@RequestMapping(value = {"list", ""})
	public String list(WemallVipCard wemallVipCard, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<WemallVipCard> page = wemallVipCardService.findPage(new Page<WemallVipCard>(request, response), wemallVipCard); 
		model.addAttribute("page", page);
		return "modules/wemall/wemallVipCardList";
	}

	@RequiresPermissions("wemall:wemallVipCard:view")
	@RequestMapping(value = "form")
	public String form(WemallVipCard wemallVipCard, Model model) {
		model.addAttribute("wemallVipCard", wemallVipCard);
		return "modules/wemall/wemallVipCardForm";
	}

	@RequiresPermissions("wemall:wemallVipCard:edit")
	@RequestMapping(value = "save")
	public String save(WemallVipCard wemallVipCard, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, wemallVipCard)){
			return form(wemallVipCard, model);
		}
		wemallVipCardService.save(wemallVipCard);
		addMessage(redirectAttributes, "保存会员卡成功");
		return "redirect:"+Global.getAdminPath()+"/wemall/wemallVipCard/?repage";
	}
	
	@RequiresPermissions("wemall:wemallVipCard:edit")
	@RequestMapping(value = "delete")
	public String delete(WemallVipCard wemallVipCard, RedirectAttributes redirectAttributes) {
		wemallVipCardService.delete(wemallVipCard);
		addMessage(redirectAttributes, "删除会员卡成功");
		return "redirect:"+Global.getAdminPath()+"/wemall/wemallVipCard/?repage";
	}

}
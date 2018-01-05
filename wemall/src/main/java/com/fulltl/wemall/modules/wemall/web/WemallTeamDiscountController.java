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
import com.fulltl.wemall.modules.wemall.entity.WemallTeamDiscount;
import com.fulltl.wemall.modules.wemall.service.WemallTeamDiscountService;

/**
 * 限时拼团活动管理Controller
 * @author ldk
 * @version 2018-01-05
 */
@Controller
@RequestMapping(value = "${adminPath}/wemall/wemallTeamDiscount")
public class WemallTeamDiscountController extends BaseController {

	@Autowired
	private WemallTeamDiscountService wemallTeamDiscountService;
	
	@ModelAttribute
	public WemallTeamDiscount get(@RequestParam(required=false) String id) {
		WemallTeamDiscount entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = wemallTeamDiscountService.get(id);
		}
		if (entity == null){
			entity = new WemallTeamDiscount();
		}
		return entity;
	}
	
	@RequiresPermissions("wemall:wemallTeamDiscount:view")
	@RequestMapping(value = {"list", ""})
	public String list(WemallTeamDiscount wemallTeamDiscount, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<WemallTeamDiscount> page = wemallTeamDiscountService.findPage(new Page<WemallTeamDiscount>(request, response), wemallTeamDiscount); 
		model.addAttribute("page", page);
		return "modules/wemall/wemallTeamDiscountList";
	}

	@RequiresPermissions("wemall:wemallTeamDiscount:view")
	@RequestMapping(value = "form")
	public String form(WemallTeamDiscount wemallTeamDiscount, Model model) {
		model.addAttribute("wemallTeamDiscount", wemallTeamDiscount);
		return "modules/wemall/wemallTeamDiscountForm";
	}

	@RequiresPermissions("wemall:wemallTeamDiscount:edit")
	@RequestMapping(value = "save")
	public String save(WemallTeamDiscount wemallTeamDiscount, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, wemallTeamDiscount)){
			return form(wemallTeamDiscount, model);
		}
		wemallTeamDiscountService.save(wemallTeamDiscount);
		addMessage(redirectAttributes, "保存限时拼团活动成功");
		return "redirect:"+Global.getAdminPath()+"/wemall/wemallTeamDiscount/?repage";
	}
	
	@RequiresPermissions("wemall:wemallTeamDiscount:edit")
	@RequestMapping(value = "delete")
	public String delete(WemallTeamDiscount wemallTeamDiscount, RedirectAttributes redirectAttributes) {
		wemallTeamDiscountService.delete(wemallTeamDiscount);
		addMessage(redirectAttributes, "删除限时拼团活动成功");
		return "redirect:"+Global.getAdminPath()+"/wemall/wemallTeamDiscount/?repage";
	}

}
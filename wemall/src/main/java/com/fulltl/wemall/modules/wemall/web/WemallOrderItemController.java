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
import com.fulltl.wemall.common.utils.StringUtils;
import com.fulltl.wemall.common.web.BaseController;
import com.fulltl.wemall.modules.wemall.entity.WemallOrderItem;
import com.fulltl.wemall.modules.wemall.service.WemallOrderItemService;

/**
 * 订单-商品管理Controller
 * @author ldk
 * @version 2018-01-05
 */
@Controller
@RequestMapping(value = "${adminPath}/wemall/wemallOrderItem")
public class WemallOrderItemController extends BaseController {

	@Autowired
	private WemallOrderItemService wemallOrderItemService;
	
	@ModelAttribute
	public WemallOrderItem get(@RequestParam(required=false, value="orderNo") String orderNo,
			@RequestParam(required=false, value="itemId") Integer itemId) {
		WemallOrderItem entity = null;
		if (StringUtils.isNotBlank(orderNo) && itemId != null){
			entity = wemallOrderItemService.get(orderNo, itemId);
		}
		if (entity == null){
			entity = new WemallOrderItem();
		}
		return entity;
	}
	
	@RequiresPermissions("wemall:wemallOrderItem:view")
	@RequestMapping(value = {"list", ""})
	public String list(WemallOrderItem wemallOrderItem, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<WemallOrderItem> page = wemallOrderItemService.findPage(new Page<WemallOrderItem>(request, response), wemallOrderItem); 
		model.addAttribute("page", page);
		return "modules/wemall/wemallOrderItemList";
	}

	@RequiresPermissions("wemall:wemallOrderItem:view")
	@RequestMapping(value = "form")
	public String form(WemallOrderItem wemallOrderItem, Model model) {
		if (StringUtils.isBlank(wemallOrderItem.getOrderNo()) && 
				wemallOrderItem.getItemId() != null){
			wemallOrderItem.setIsNewRecord(true);
		}
		model.addAttribute("wemallOrderItem", wemallOrderItem);
		return "modules/wemall/wemallOrderItemForm";
	}

	@RequiresPermissions("wemall:wemallOrderItem:edit")
	@RequestMapping(value = "save")
	public String save(WemallOrderItem wemallOrderItem, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, wemallOrderItem)){
			return form(wemallOrderItem, model);
		}
		wemallOrderItemService.save(wemallOrderItem);
		addMessage(redirectAttributes, "保存订单-商品信息成功");
		return "redirect:"+Global.getAdminPath()+"/wemall/wemallOrderItem/?repage";
	}
	
	@RequiresPermissions("wemall:wemallOrderItem:edit")
	@RequestMapping(value = "delete")
	public String delete(WemallOrderItem wemallOrderItem, RedirectAttributes redirectAttributes) {
		wemallOrderItemService.delete(wemallOrderItem);
		addMessage(redirectAttributes, "删除订单-商品信息成功");
		return "redirect:"+Global.getAdminPath()+"/wemall/wemallOrderItem/?repage";
	}

}
/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.fulltl.wemall.modules.sys.web;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fulltl.wemall.common.config.Global;
import com.fulltl.wemall.common.persistence.Page;
import com.fulltl.wemall.common.web.BaseController;
import com.fulltl.wemall.modules.sys.entity.SlSysOrder;
import com.fulltl.wemall.modules.sys.service.SlSysOrderMgrService;
import com.fulltl.wemall.modules.sys.service.SlSysOrderService;

/**
 * 订单管理Controller
 * @author ldk
 * @version 2017-11-27
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/slSysOrder")
public class SlSysOrderController extends BaseController {

	@Autowired
	private SlSysOrderService slSysOrderService;
	@Autowired
	private SlSysOrderMgrService slSysOrderMgrService;
	
	@ModelAttribute
	public SlSysOrder get(@RequestParam(required=false) String id) {
		SlSysOrder entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = slSysOrderService.get(id);
		}
		if (entity == null){
			entity = new SlSysOrder();
		}
		return entity;
	}
	
	@RequiresPermissions("sys:slSysOrder:view")
	@RequestMapping(value = {"list", ""})
	public String list(SlSysOrder slSysOrder, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<SlSysOrder> page = slSysOrderService.findPage(new Page<SlSysOrder>(request, response), slSysOrder); 
		model.addAttribute("page", page);
		return "modules/sys/slSysOrderList";
	}
	
	@RequiresPermissions("sys:slSysOrder:view")
	@RequestMapping(value = {"formulaList"})
	public String folumaList(SlSysOrder slSysOrder, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<SlSysOrder> page = slSysOrderService.findPage(new Page<SlSysOrder>(request, response), slSysOrder); 
		model.addAttribute("page", page);
		return "modules/sys/slSysFolumaOrderList";
	}

	@RequiresPermissions("sys:slSysOrder:view")
	@RequestMapping(value = "form")
	public String form(SlSysOrder slSysOrder, Model model) {
		model.addAttribute("slSysOrder", slSysOrder);
		return "modules/sys/slSysOrderForm";
	}

	@RequiresPermissions("sys:slSysOrder:edit")
	@RequestMapping(value = "save")
	public String save(SlSysOrder slSysOrder, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, slSysOrder)){
			return form(slSysOrder, model);
		}
		slSysOrderService.save(slSysOrder);
		addMessage(redirectAttributes, "保存订单成功");
		return "redirect:"+Global.getAdminPath()+"/sys/slSysOrder/?repage";
	}
	
	@RequiresPermissions("sys:slSysOrder:edit")
	@RequestMapping(value = "delete")
	public String delete(SlSysOrder slSysOrder, RedirectAttributes redirectAttributes) {
		slSysOrderService.delete(slSysOrder);
		addMessage(redirectAttributes, "删除订单成功");
		return "redirect:"+Global.getAdminPath()+"/sys/slSysOrder/?repage";
	}

	@RequiresPermissions("sys:slSysOrder:refund")
	@RequestMapping(value = "refund")
	public String refund(HttpServletRequest request, RedirectAttributes redirectAttributes) {
		String refundFee = WebUtils.getCleanParam(request, "refundFee");
		String orderNo = WebUtils.getCleanParam(request, "id");
		Map<String, Object> retMap = slSysOrderMgrService.refund(orderNo, refundFee, "");
		if(!"0".equals(retMap.get("ret"))) {
			addMessage(redirectAttributes, "退款失败!");
		} else {
			addMessage(redirectAttributes, "退款成功!");
		}
		return "redirect:"+Global.getAdminPath()+"/sys/slSysOrder/?repage";
	}
	
	@RequiresPermissions("sys:slSysOrder:view")
	@RequestMapping(value = "orderQuery")
	@ResponseBody
	public String orderQuery(HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		return slSysOrderMgrService.orderQuery(request, response);
	}
	
	@RequiresPermissions("sys:slSysOrder:view")
	@RequestMapping(value = "refundQuery")
	@ResponseBody
	public String refundQuery(HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		return slSysOrderMgrService.refundQuery(request, response);
	}
	
	@RequiresPermissions("sys:slSysOrder:downloadBill")
	@RequestMapping(value = "downloadBill")
	@ResponseBody
	public Map<String, Object> downloadBill(HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		Map<String, Object> retMap = slSysOrderMgrService.downloadBill(request, response);
		
		if(!"0".equals(retMap.get("ret"))) {
			retMap.put("retMsg", "下载对账单失败!失败原因：" + retMap.get("retMsg"));
		} else {
			retMap.put("retMsg",retMap.get("retMsg"));
		}
		return retMap;
	}
}
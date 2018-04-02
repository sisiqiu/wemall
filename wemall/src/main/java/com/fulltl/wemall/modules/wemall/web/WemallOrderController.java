/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.fulltl.wemall.modules.wemall.web;

import java.net.URLDecoder;
import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import com.fulltl.wemall.common.utils.StringUtils;
import com.fulltl.wemall.modules.pay.service.WemallOrderMgrService;
import com.fulltl.wemall.modules.wemall.entity.WemallOrder;
import com.fulltl.wemall.modules.wemall.entity.WemallOrder.OrderStatus;
import com.fulltl.wemall.modules.wemall.service.WemallOrderService;

/**
 * 订单管理Controller
 * @author ldk
 * @version 2018-01-05
 */
@Controller
@RequestMapping(value = "${adminPath}/wemall/wemallOrder")
public class WemallOrderController extends BaseController {

	@Autowired
	private WemallOrderService wemallOrderService;
	@Autowired
	private WemallOrderMgrService wemallOrderMgrService;
	
	@ModelAttribute
	public WemallOrder get(@RequestParam(required=false) String id) {
		WemallOrder entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = wemallOrderService.get(id);
		}
		if (entity == null){
			entity = new WemallOrder();
		}
		return entity;
	}
	
	@RequiresPermissions("wemall:wemallOrder:view")
	@RequestMapping(value = {"list", ""})
	public String list(WemallOrder wemallOrder, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<WemallOrder> page = wemallOrderService.findPage(new Page<WemallOrder>(request, response), wemallOrder); 
		model.addAttribute("page", page);
		return "modules/wemall/wemallOrderList";
	}

	@RequiresPermissions("wemall:wemallOrder:view")
	@RequestMapping(value = "form")
	public String form(WemallOrder wemallOrder, Model model) {
		Map<String, Object> allData = wemallOrderService.getOrderDetail(wemallOrder.getOrderNo());
		model.addAttribute("allData", allData);
		model.addAttribute("wemallOrder", wemallOrder);
		return "modules/wemall/wemallOrderForm";
	}

	@RequiresPermissions("wemall:wemallOrder:edit")
	@RequestMapping(value = "save")
	public String save(WemallOrder wemallOrder, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, wemallOrder)){
			return form(wemallOrder, model);
		}
		wemallOrderService.save(wemallOrder);
		addMessage(redirectAttributes, "保存订单成功");
		return "redirect:"+Global.getAdminPath()+"/wemall/wemallOrder/?repage";
	}
	
	@RequiresPermissions("wemall:wemallOrder:edit")
	@RequestMapping(value = "delete")
	public String delete(WemallOrder wemallOrder, RedirectAttributes redirectAttributes) {
		wemallOrderService.delete(wemallOrder);
		addMessage(redirectAttributes, "删除订单成功");
		return "redirect:"+Global.getAdminPath()+"/wemall/wemallOrder/?repage";
	}

	@RequiresPermissions("wemall:wemallOrder:refund")
	@RequestMapping(value = "refund")
	public String refund(HttpServletRequest request, RedirectAttributes redirectAttributes) {
		String refundFee = WebUtils.getCleanParam(request, "refundFee");
		String orderNo = WebUtils.getCleanParam(request, "orderNo");
		Map<String, Object> retMap = wemallOrderMgrService.refund(orderNo, refundFee, "");
		if(!"0".equals(retMap.get("ret"))) {
			addMessage(redirectAttributes, "退款失败!");
		} else {
			addMessage(redirectAttributes, "退款成功!");
		}
		return "redirect:"+Global.getAdminPath()+"/wemall/wemallOrder/?repage";
	}
	
	@RequiresPermissions("wemall:wemallOrder:view")
	@RequestMapping(value = "orderQuery")
	@ResponseBody
	public String orderQuery(HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		return wemallOrderMgrService.orderQuery(request, response);
	}
	
	@RequiresPermissions("wemall:wemallOrder:view")
	@RequestMapping(value = "refundQuery")
	@ResponseBody
	public String refundQuery(HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		return wemallOrderMgrService.refundQuery(request, response);
	}
	
	@RequiresPermissions("wemall:wemallOrder:downloadBill")
	@RequestMapping(value = "downloadBill")
	@ResponseBody
	public Map<String, Object> downloadBill(HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		Map<String, Object> retMap = wemallOrderMgrService.downloadBill(request, response);
		
		if(!"0".equals(retMap.get("ret"))) {
			retMap.put("retMsg", "下载对账单失败!失败原因：" + retMap.get("retMsg"));
		} else {
			retMap.put("retMsg",retMap.get("retMsg"));
		}
		return retMap;
	}
	
	/**
	 * 发货
	 * @param request
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions("wemall:wemallOrder:alreadyShipped")
	@RequestMapping(value = "alreadyShipped")
	public String alreadyShipped(HttpServletRequest request, RedirectAttributes redirectAttributes) {
		String orderNo = WebUtils.getCleanParam(request, "orderNo");
		String freightName = WebUtils.getCleanParam(request, "freightName");
		String freightNo = WebUtils.getCleanParam(request, "freightNo");
		WemallOrder wemallOrder = new WemallOrder();
		wemallOrder.setOrderNo(orderNo);
		wemallOrder.setFreightName(URLDecoder.decode(freightName));
		wemallOrder.setFreightNo(freightNo);
		wemallOrder.setConsignDate(new Date());
		wemallOrderService.updateAllStatusByOrderNo(wemallOrder, OrderStatus.alreadyShipped.getValue());
		addMessage(redirectAttributes, "发货成功!");
		return "redirect:"+Global.getAdminPath()+"/wemall/wemallOrder/?repage";
	}
}
/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.fulltl.wemall.modules.sys.web;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
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
import com.fulltl.wemall.modules.sys.entity.OrderDict;
import com.fulltl.wemall.modules.sys.service.OrderDictService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * 字典Controller
 * @author ThinkGem
 * @version 2014-05-16
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/orderDict")
public class OrderDictController extends BaseController {

	@Autowired
	private OrderDictService orderDictService;
	
	@ModelAttribute
	public OrderDict get(@RequestParam(required=false) String id) {
		if (StringUtils.isNotBlank(id)){
			return orderDictService.get(id);
		}else{
			return new OrderDict();
		}
	}
	
	@RequiresPermissions("sys:orderDict:view")
	@RequestMapping(value = {"list", ""})
	public String list(OrderDict orderDict, HttpServletRequest request, HttpServletResponse response, Model model) {
		List<String> typeList = orderDictService.findTypeList();
		model.addAttribute("typeList", typeList);
        Page<OrderDict> page = orderDictService.findPage(new Page<OrderDict>(request, response), orderDict); 
        model.addAttribute("page", page);
		return "modules/sys/orderDictList";
	}

	@RequiresPermissions("sys:orderDict:view")
	@RequestMapping(value = "form")
	public String form(OrderDict orderDict, Model model) {
		model.addAttribute("orderDict", orderDict);
		return "modules/sys/orderDictForm";
	}

	@RequiresPermissions("sys:orderDict:edit")
	@RequestMapping(value = "save")//@Valid 
	public String save(OrderDict orderDict, Model model, RedirectAttributes redirectAttributes) {
		if(Global.isDemoMode()){
			addMessage(redirectAttributes, "演示模式，不允许操作！");
			return "redirect:" + adminPath + "/sys/orderDict/?repage&type="+orderDict.getType();
		}
		if (!beanValidator(model, orderDict)){
			return form(orderDict, model);
		}
		orderDictService.save(orderDict);
		addMessage(redirectAttributes, "保存字典'" + orderDict.getLabel() + "'成功");
		return "redirect:" + adminPath + "/sys/orderDict/?repage&type="+orderDict.getType();
	}
	
	@RequiresPermissions("sys:orderDict:edit")
	@RequestMapping(value = "delete")
	public String delete(OrderDict orderDict, RedirectAttributes redirectAttributes) {
		if(Global.isDemoMode()){
			addMessage(redirectAttributes, "演示模式，不允许操作！");
			return "redirect:" + adminPath + "/sys/orderDict/?repage";
		}
		orderDictService.delete(orderDict);
		addMessage(redirectAttributes, "删除字典成功");
		return "redirect:" + adminPath + "/sys/orderDict/?repage&type="+orderDict.getType();
	}
	
	@RequiresPermissions("user")
	@ResponseBody
	@RequestMapping(value = "treeData")
	public List<Map<String, Object>> treeData(@RequestParam(required=false) String type, HttpServletResponse response) {
		List<Map<String, Object>> mapList = Lists.newArrayList();
		OrderDict orderDict = new OrderDict();
		orderDict.setType(type);
		List<OrderDict> list = orderDictService.findList(orderDict);
		for (int i=0; i<list.size(); i++){
			OrderDict e = list.get(i);
			Map<String, Object> map = Maps.newHashMap();
			map.put("id", e.getId());
			map.put("pId", e.getParentId());
			map.put("name", StringUtils.replace(e.getLabel(), " ", ""));
			mapList.add(map);
		}
		return mapList;
	}
	
	@ResponseBody
	@RequestMapping(value = "listData")
	public List<OrderDict> listData(@RequestParam(required=false) String type) {
		OrderDict orderDict = new OrderDict();
		orderDict.setType(type);
		return orderDictService.findList(orderDict);
	}

}

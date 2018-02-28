/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.fulltl.wemall.modules.wemall.web;

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

import com.fulltl.wemall.common.config.Global;
import com.fulltl.wemall.common.persistence.Page;
import com.fulltl.wemall.common.utils.StringUtils;
import com.fulltl.wemall.common.web.BaseController;
import com.fulltl.wemall.modules.wemall.entity.WemallItem;
import com.fulltl.wemall.modules.wemall.service.WemallItemService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * 商品管理Controller
 * @author ldk
 * @version 2018-01-05
 */
@Controller
@RequestMapping(value = "${adminPath}/wemall/wemallItem")
public class WemallItemController extends BaseController {

	@Autowired
	private WemallItemService wemallItemService;
	
	@ModelAttribute
	public WemallItem get(@RequestParam(required=false) String id) {
		WemallItem entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = wemallItemService.get(id);
		}
		if (entity == null){
			entity = new WemallItem();
		}
		return entity;
	}
	
	@RequiresPermissions("wemall:wemallItem:view")
	@RequestMapping(value = {"list", ""})
	public String list(WemallItem wemallItem, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<WemallItem> page = wemallItemService.findPage(new Page<WemallItem>(request, response), wemallItem); 
		model.addAttribute("page", page);
		return "modules/wemall/wemallItemList";
	}

	@RequiresPermissions("wemall:wemallItem:view")
	@RequestMapping(value = "form")
	public String form(WemallItem wemallItem, Model model) {
		model.addAttribute("wemallItem", wemallItem);
		return "modules/wemall/wemallItemForm";
	}

	@RequiresPermissions("wemall:wemallItem:edit")
	@RequestMapping(value = "save")
	public String save(WemallItem wemallItem, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, wemallItem)){
			return form(wemallItem, model);
		}
		wemallItemService.save(wemallItem);
		addMessage(redirectAttributes, "保存商品成功");
		return "redirect:"+Global.getAdminPath()+"/wemall/wemallItem/?repage";
	}
	
	@RequiresPermissions("wemall:wemallItem:edit")
	@RequestMapping(value = "delete")
	public String delete(WemallItem wemallItem, RedirectAttributes redirectAttributes) {
		wemallItemService.delete(wemallItem);
		addMessage(redirectAttributes, "删除商品成功");
		return "redirect:"+Global.getAdminPath()+"/wemall/wemallItem/?repage";
	}

	/**
	 * isShowHide是否显示隐藏菜单
	 * @param extId
	 * @param isShowHidden
	 * @param response
	 * @return
	 */
	@RequiresPermissions("user")
	@ResponseBody
	@RequestMapping(value = "treeData")
	public List<Map<String, Object>> treeData(@RequestParam(required=false) String extId,@RequestParam(required=false) String isShowHide, HttpServletResponse response) {
		List<Map<String, Object>> mapList = Lists.newArrayList();
		List<WemallItem> list = wemallItemService.findList(new WemallItem());
		for (int i=0; i<list.size(); i++){
			WemallItem e = list.get(i);
			/*if(isShowHide != null && isShowHide.equals("0") && e.getIsShow().equals("0")){
				continue;
			}*/
			Map<String, Object> map = Maps.newHashMap();
			map.put("id", e.getId());
			//map.put("pId", e.getParentId());
			map.put("name", e.getName());
			mapList.add(map);
		}
		return mapList;
	}
}
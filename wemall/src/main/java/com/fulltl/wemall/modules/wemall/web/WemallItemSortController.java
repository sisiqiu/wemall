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
import com.fulltl.wemall.common.utils.StringUtils;
import com.fulltl.wemall.common.web.BaseController;
import com.fulltl.wemall.modules.wemall.entity.WemallItemSort;
import com.fulltl.wemall.modules.wemall.service.WemallItemSortService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * 商品分类管理Controller
 * @author ldk
 * @version 2018-01-10
 */
@Controller
@RequestMapping(value = "${adminPath}/wemall/wemallItemSort")
public class WemallItemSortController extends BaseController {

	@Autowired
	private WemallItemSortService wemallItemSortService;
	
	@ModelAttribute
	public WemallItemSort get(@RequestParam(required=false) String id) {
		WemallItemSort entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = wemallItemSortService.get(id);
		}
		if (entity == null){
			entity = new WemallItemSort();
		}
		return entity;
	}
	
	@RequiresPermissions("wemall:wemallItemSort:view")
	@RequestMapping(value = {"list", ""})
	public String list(WemallItemSort wemallItemSort, HttpServletRequest request, HttpServletResponse response, Model model) {
		List<WemallItemSort> list = wemallItemSortService.findList(wemallItemSort); 
		model.addAttribute("list", list);
		return "modules/wemall/wemallItemSortList";
	}

	@RequiresPermissions("wemall:wemallItemSort:view")
	@RequestMapping(value = "form")
	public String form(WemallItemSort wemallItemSort, Model model) {
		if (wemallItemSort.getParent()!=null && StringUtils.isNotBlank(wemallItemSort.getParent().getId())){
			wemallItemSort.setParent(wemallItemSortService.get(wemallItemSort.getParent().getId()));
			// 获取排序号，最末节点排序号+30
			if (StringUtils.isBlank(wemallItemSort.getId())){
				WemallItemSort wemallItemSortChild = new WemallItemSort();
				wemallItemSortChild.setParent(new WemallItemSort(wemallItemSort.getParent().getId()));
				List<WemallItemSort> list = wemallItemSortService.findList(wemallItemSort); 
				if (list.size() > 0){
					wemallItemSort.setSort(list.get(list.size()-1).getSort());
					if (wemallItemSort.getSort() != null){
						wemallItemSort.setSort(wemallItemSort.getSort() + 30);
					}
				}
			}
		}
		if (wemallItemSort.getSort() == null){
			wemallItemSort.setSort(30);
		}
		model.addAttribute("wemallItemSort", wemallItemSort);
		return "modules/wemall/wemallItemSortForm";
	}

	@RequiresPermissions("wemall:wemallItemSort:edit")
	@RequestMapping(value = "save")
	public String save(WemallItemSort wemallItemSort, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, wemallItemSort)){
			return form(wemallItemSort, model);
		}
		wemallItemSortService.save(wemallItemSort);
		addMessage(redirectAttributes, "保存商品分类成功");
		return "redirect:"+Global.getAdminPath()+"/wemall/wemallItemSort/?repage";
	}
	
	@RequiresPermissions("wemall:wemallItemSort:edit")
	@RequestMapping(value = "delete")
	public String delete(WemallItemSort wemallItemSort, RedirectAttributes redirectAttributes) {
		wemallItemSortService.delete(wemallItemSort);
		addMessage(redirectAttributes, "删除商品分类成功");
		return "redirect:"+Global.getAdminPath()+"/wemall/wemallItemSort/?repage";
	}

	@RequiresPermissions("user")
	@ResponseBody
	@RequestMapping(value = "treeData")
	public List<Map<String, Object>> treeData(@RequestParam(required=false) String extId, HttpServletResponse response) {
		List<Map<String, Object>> mapList = Lists.newArrayList();
		List<WemallItemSort> list = wemallItemSortService.findList(new WemallItemSort());
		for (int i=0; i<list.size(); i++){
			WemallItemSort e = list.get(i);
			if (StringUtils.isBlank(extId) || (extId!=null && !extId.equals(e.getId()) && e.getParentIds().indexOf(","+extId+",")==-1)){
				Map<String, Object> map = Maps.newHashMap();
				map.put("id", e.getId());
				map.put("pId", e.getParentId());
				map.put("name", e.getName());
				mapList.add(map);
			}
		}
		return mapList;
	}
	
}
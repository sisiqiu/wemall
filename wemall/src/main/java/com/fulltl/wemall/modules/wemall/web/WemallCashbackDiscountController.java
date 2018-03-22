/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.fulltl.wemall.modules.wemall.web;

import java.util.Arrays;
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
import com.fulltl.wemall.common.web.BaseController;
import com.fulltl.wemall.common.utils.StringUtils;
import com.fulltl.wemall.modules.wemall.entity.WemallCashbackDiscount;
import com.fulltl.wemall.modules.wemall.entity.WemallItem;
import com.fulltl.wemall.modules.wemall.entity.WemallItemActivity;
import com.fulltl.wemall.modules.wemall.service.WemallCashbackDiscountService;
import com.fulltl.wemall.modules.wemall.service.WemallItemActivityService;
import com.fulltl.wemall.modules.wemall.service.WemallItemService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * 限时返现活动管理Controller
 * @author ldk
 * @version 2018-01-05
 */
@Controller
@RequestMapping(value = "${adminPath}/wemall/wemallCashbackDiscount")
public class WemallCashbackDiscountController extends BaseController {

	@Autowired
	private WemallCashbackDiscountService wemallCashbackDiscountService;
	
	@Autowired
	private WemallItemService wemallItemService;
	
	@Autowired
	private WemallItemActivityService wemallItemActivityService;
	@ModelAttribute
	public WemallCashbackDiscount get(@RequestParam(required=false) String id) {
		WemallCashbackDiscount entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = wemallCashbackDiscountService.get(id);
		}
		if (entity == null){
			entity = new WemallCashbackDiscount();
		}
		return entity;
	}
	
	@RequiresPermissions("wemall:wemallCashbackDiscount:view")
	@RequestMapping(value = {"list", ""})
	public String list(WemallCashbackDiscount wemallCashbackDiscount, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<WemallCashbackDiscount> page = wemallCashbackDiscountService.findPage(new Page<WemallCashbackDiscount>(request, response), wemallCashbackDiscount); 
		model.addAttribute("page", page);
		return "modules/wemall/wemallCashbackDiscountList";
	}

	@RequiresPermissions("wemall:wemallCashbackDiscount:view")
	@RequestMapping(value = "form")
	public String form(WemallCashbackDiscount wemallCashbackDiscount, Model model,HttpServletRequest request, HttpServletResponse response) {
		Page<WemallItem> page = wemallItemService.findPage(new Page<WemallItem>(request, response), new WemallItem()); 
		List<WemallItem> actItems = wemallItemActivityService.findItemsByActId(wemallCashbackDiscount.getId(),1);
		String actIds = ",";
		if(actItems.size()>0){
			for(WemallItem w :actItems){
				actIds += w.getId()+",";
			}
		}
		model.addAttribute("wemallCashbackDiscount", wemallCashbackDiscount);
		model.addAttribute("page", page);
		model.addAttribute("actItems", actItems);
		model.addAttribute("actIds", actIds);
		return "modules/wemall/wemallCashbackDiscountForm";
	}
	
	@RequiresPermissions("wemall:wemallCashbackDiscount:view")
	@RequestMapping(value = "pageData")
	@ResponseBody
	public Object pageData(WemallItem wemallItem,Model model,HttpServletRequest request, HttpServletResponse response) {
		Page<WemallItem> page = wemallItemService.findPage(new Page<WemallItem>(request, response), wemallItem); 
		Map<String, Object> pageDataMap = Maps.newHashMap();
		pageDataMap.put("page", page.toString());
		pageDataMap.put("list", page.getList());
		return pageDataMap;
	}

	@RequiresPermissions("wemall:wemallCashbackDiscount:edit")
	@RequestMapping(value = "save")
	public String save(WemallCashbackDiscount wemallCashbackDiscount, HttpServletRequest request, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, wemallCashbackDiscount)){
			return form(wemallCashbackDiscount, model, null, null);
		}
		wemallCashbackDiscountService.save(wemallCashbackDiscount);
		String actId = wemallCashbackDiscount.getId();
		String itemIds = request.getParameter("itemIds");
		String[] newIdArr = null;
		List<String> newIdList = null;
		List<String> retainList = null;
		if(!StringUtils.isBlank(itemIds)) {
			newIdArr = itemIds.split(",");
			newIdList = Arrays.asList(newIdArr);
			retainList = Lists.newArrayList();
			retainList.addAll(newIdList);
		}
		List<WemallItem> actItems = wemallItemActivityService.findItemsByActId(actId,1);
		List<String> oldIdList = Lists.newArrayList();
		if(actItems.size()>0){
			for(WemallItem w :actItems){
				oldIdList.add(w.getId());
			}
		}
		if(oldIdList.size() == 0) retainList = Lists.newArrayList();
		else if(retainList != null) retainList.retainAll(oldIdList);//交集确定
		
		List<String> removeIdList = Lists.newArrayList();
		if(oldIdList != null) removeIdList.addAll(oldIdList);
		if(retainList != null) removeIdList.removeAll(retainList);//删除集确定
		
		List<String> addIdList = Lists.newArrayList();
		if(newIdList != null) addIdList.addAll(newIdList);
		if(retainList != null) addIdList.removeAll(retainList);//新增集确定
		
		if(addIdList.size()>0){
			for(int i =0;i<addIdList.size();i++){
				WemallItemActivity wemallItemActivity = new WemallItemActivity();
				wemallItemActivity.setActivityId(Integer.valueOf(actId));
				wemallItemActivity.setItemId(Integer.valueOf(addIdList.get(i)));
				wemallItemActivity.setActivityType(1);
				wemallItemActivityService.save(wemallItemActivity );
			}
		}
		if(removeIdList.size()>0){
			for(int i =0;i<removeIdList.size();i++){
				WemallItemActivity wemallItemActivity = new WemallItemActivity();
				wemallItemActivity.setActivityId(Integer.valueOf(actId));
				wemallItemActivity.setItemId(Integer.valueOf(removeIdList.get(i)));
				wemallItemActivity.setActivityType(1);
				wemallItemActivityService.delete(wemallItemActivity);
			}
		}
		addMessage(redirectAttributes, "保存限时返现活动成功");
		return "redirect:"+Global.getAdminPath()+"/wemall/wemallCashbackDiscount/?repage";
	}
	
	@RequiresPermissions("wemall:wemallCashbackDiscount:edit")
	@RequestMapping(value = "delete")
	public String delete(WemallCashbackDiscount wemallCashbackDiscount, RedirectAttributes redirectAttributes) {
		wemallCashbackDiscountService.delete(wemallCashbackDiscount);
		addMessage(redirectAttributes, "删除限时返现活动成功");
		return "redirect:"+Global.getAdminPath()+"/wemall/wemallCashbackDiscount/?repage";
	}

}
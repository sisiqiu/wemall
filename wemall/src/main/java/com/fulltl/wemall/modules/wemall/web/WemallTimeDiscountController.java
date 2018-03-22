/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.fulltl.wemall.modules.wemall.web;

import java.util.Arrays;
import java.util.List;

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
import com.fulltl.wemall.modules.wemall.entity.WemallItem;
import com.fulltl.wemall.modules.wemall.entity.WemallItemActivity;
import com.fulltl.wemall.modules.wemall.entity.WemallTimeDiscount;
import com.fulltl.wemall.modules.wemall.service.WemallItemActivityService;
import com.fulltl.wemall.modules.wemall.service.WemallItemService;
import com.fulltl.wemall.modules.wemall.service.WemallTimeDiscountService;
import com.google.common.collect.Lists;

/**
 * 限时打折活动管理Controller
 * @author ldk
 * @version 2018-01-05
 */
@Controller
@RequestMapping(value = "${adminPath}/wemall/wemallTimeDiscount")
public class WemallTimeDiscountController extends BaseController {

	@Autowired
	private WemallTimeDiscountService wemallTimeDiscountService;
	
	@Autowired
	private WemallItemService wemallItemService;
	
	@Autowired
	private WemallItemActivityService wemallItemActivityService;
	
	@ModelAttribute
	public WemallTimeDiscount get(@RequestParam(required=false) String id) {
		WemallTimeDiscount entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = wemallTimeDiscountService.get(id);
		}
		if (entity == null){
			entity = new WemallTimeDiscount();
		}
		return entity;
	}
	
	@RequiresPermissions("wemall:wemallTimeDiscount:view")
	@RequestMapping(value = {"list", ""})
	public String list(WemallTimeDiscount wemallTimeDiscount, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<WemallTimeDiscount> page = wemallTimeDiscountService.findPage(new Page<WemallTimeDiscount>(request, response), wemallTimeDiscount); 
		model.addAttribute("page", page);
		return "modules/wemall/wemallTimeDiscountList";
	}

	@RequiresPermissions("wemall:wemallTimeDiscount:view")
	@RequestMapping(value = "form")
	public String form(WemallTimeDiscount wemallTimeDiscount, Model model,HttpServletRequest request, HttpServletResponse response) {
		Page<WemallItem> page = wemallItemService.findPage(new Page<WemallItem>(request, response), new WemallItem()); 
		List<WemallItem> actItems = wemallItemActivityService.findItemsByActId(wemallTimeDiscount.getId(),3);
		String actIds = ",";
		if(actItems.size()>0){
			for(WemallItem w :actItems){
				actIds += w.getId()+",";
			}
		}
		model.addAttribute("page", page);
		model.addAttribute("actItems", actItems);
		model.addAttribute("actIds", actIds);
		model.addAttribute("wemallTimeDiscount", wemallTimeDiscount);
		return "modules/wemall/wemallTimeDiscountForm";
	}

	@RequiresPermissions("wemall:wemallTimeDiscount:edit")
	@RequestMapping(value = "save")
	public String save(WemallTimeDiscount wemallTimeDiscount,HttpServletRequest request, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, wemallTimeDiscount)){
			return form(wemallTimeDiscount, model, null, null);
		}
		wemallTimeDiscountService.save(wemallTimeDiscount);
		String actId = wemallTimeDiscount.getId();
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
		List<WemallItem> actItems = wemallItemActivityService.findItemsByActId(actId,3);
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
				wemallItemActivity.setActivityType(3);
				wemallItemActivityService.save(wemallItemActivity );
			}
		}
		if(removeIdList.size()>0){
			for(int i =0;i<removeIdList.size();i++){
				WemallItemActivity wemallItemActivity = new WemallItemActivity();
				wemallItemActivity.setActivityId(Integer.valueOf(actId));
				wemallItemActivity.setItemId(Integer.valueOf(removeIdList.get(i)));
				wemallItemActivity.setActivityType(3);
				wemallItemActivityService.delete(wemallItemActivity);
			}
		}
		addMessage(redirectAttributes, "保存限时打折活动成功");
		return "redirect:"+Global.getAdminPath()+"/wemall/wemallTimeDiscount/?repage";
	}
	
	@RequiresPermissions("wemall:wemallTimeDiscount:edit")
	@RequestMapping(value = "delete")
	public String delete(WemallTimeDiscount wemallTimeDiscount, RedirectAttributes redirectAttributes) {
		wemallTimeDiscountService.delete(wemallTimeDiscount);
		addMessage(redirectAttributes, "删除限时打折活动成功");
		return "redirect:"+Global.getAdminPath()+"/wemall/wemallTimeDiscount/?repage";
	}

}
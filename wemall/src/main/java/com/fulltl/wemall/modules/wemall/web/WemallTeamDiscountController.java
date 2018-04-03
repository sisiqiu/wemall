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
import com.fulltl.wemall.modules.wemall.entity.WemallTeamDiscount;
import com.fulltl.wemall.modules.wemall.entity.WemallItemActivity.ActivityTypeEnum;
import com.fulltl.wemall.modules.wemall.service.WemallItemActivityService;
import com.fulltl.wemall.modules.wemall.service.WemallItemService;
import com.fulltl.wemall.modules.wemall.service.WemallTeamDiscountService;
import com.google.common.collect.Lists;

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
	
	@Autowired
	private WemallItemService wemallItemService;
	
	@Autowired
	private WemallItemActivityService wemallItemActivityService;
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
	public String form(WemallTeamDiscount wemallTeamDiscount, Model model,HttpServletRequest request, HttpServletResponse response) {
		Page<WemallItem> page = wemallItemService.findPage(new Page<WemallItem>(request, response), new WemallItem()); 
		List<WemallItem> actItems = wemallItemActivityService.findItemsByActId(wemallTeamDiscount.getId(), ActivityTypeEnum.TeamDiscount);
		String actIds = ",";
		if(actItems.size()>0){
			for(WemallItem w :actItems){
				actIds += w.getId()+",";
			}
		}
		model.addAttribute("page", page);
		model.addAttribute("actItems", actItems);
		model.addAttribute("actIds", actIds);
		model.addAttribute("wemallTeamDiscount", wemallTeamDiscount);
		return "modules/wemall/wemallTeamDiscountForm";
	}

	@RequiresPermissions("wemall:wemallTeamDiscount:edit")
	@RequestMapping(value = "save")
	public String save(WemallTeamDiscount wemallTeamDiscount,HttpServletRequest request, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, wemallTeamDiscount)){
			return form(wemallTeamDiscount, model, null, null);
		}
		wemallTeamDiscountService.save(wemallTeamDiscount);
		String actId = wemallTeamDiscount.getId();
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
		List<WemallItem> actItems = wemallItemActivityService.findItemsByActId(actId, ActivityTypeEnum.TeamDiscount);
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
				wemallItemActivity.setActivityType(ActivityTypeEnum.TeamDiscount.getValue());
				wemallItemActivityService.save(wemallItemActivity );
			}
		}
		if(removeIdList.size()>0){
			for(int i =0;i<removeIdList.size();i++){
				WemallItemActivity wemallItemActivity = new WemallItemActivity();
				wemallItemActivity.setActivityId(Integer.valueOf(actId));
				wemallItemActivity.setItemId(Integer.valueOf(removeIdList.get(i)));
				wemallItemActivity.setActivityType(ActivityTypeEnum.TeamDiscount.getValue());
				wemallItemActivityService.delete(wemallItemActivity);
			}
		}
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
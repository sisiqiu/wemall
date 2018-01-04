/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.fulltl.wemall.modules.sys.web;

import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.fulltl.wemall.common.config.Global;
import com.fulltl.wemall.common.persistence.Page;
import com.fulltl.wemall.common.utils.StringUtils;
import com.fulltl.wemall.common.web.BaseController;
import com.fulltl.wemall.modules.sys.entity.SlSysAdvertise;
import com.fulltl.wemall.modules.sys.service.SlSysAdvertiseService;
import com.fulltl.wemall.modules.sys.utils.UserUtils;

/**
 * 广告位管理Controller
 * @author ldk
 * @version 2017-11-22
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/slSysAdvertise")
public class SlSysAdvertiseController extends BaseController {

	@Autowired
	private SlSysAdvertiseService slSysAdvertiseService;
	
	@ModelAttribute
	public SlSysAdvertise get(@RequestParam(required=false) String id) {
		SlSysAdvertise entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = slSysAdvertiseService.get(id);
		}
		if (entity == null){
			entity = new SlSysAdvertise();
		}
		return entity;
	}
	
	@RequiresPermissions("sys:slSysAdvertise:view")
	@RequestMapping(value = {"list", ""})
	public String list(SlSysAdvertise slSysAdvertise, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<SlSysAdvertise> page = slSysAdvertiseService.findPage(new Page<SlSysAdvertise>(request, response), slSysAdvertise); 
		model.addAttribute("page", page);
		return "modules/sys/slSysAdvertiseList";
	}

	@RequiresPermissions("sys:slSysAdvertise:view")
	@RequestMapping(value = "form")
	public String form(SlSysAdvertise slSysAdvertise, Model model) {
		formatEntity(slSysAdvertise);
		model.addAttribute("slSysAdvertise", slSysAdvertise);
		return "modules/sys/slSysAdvertiseForm";
	}

	@RequiresPermissions("sys:slSysAdvertise:edit")
	@RequestMapping(value = "save")
	public String save(SlSysAdvertise slSysAdvertise, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, slSysAdvertise)){
			return form(slSysAdvertise, model);
		}
		String adBody = StringEscapeUtils.unescapeHtml4(slSysAdvertise.getAdBody());
		slSysAdvertise.setAdBody(adBody);
		
		try {
			new Gson().fromJson(adBody, Map.class);
		} catch (JsonSyntaxException e) {
			addMessage(model, "数据验证失败：", "body主题内容格式错误，推荐通过“生成”按钮生成！");
			return form(slSysAdvertise, model);
		}
		
		slSysAdvertiseService.save(slSysAdvertise);
		addMessage(redirectAttributes, "保存广告位成功");
		return "redirect:"+Global.getAdminPath()+"/sys/slSysAdvertise/?repage";
	}
	
	@RequiresPermissions("sys:slSysAdvertise:edit")
	@RequestMapping(value = "delete")
	public String delete(SlSysAdvertise slSysAdvertise, @RequestParam(required=false) Boolean isRe, RedirectAttributes redirectAttributes) {
		// 如果没有审核权限，则不允许删除或发布。
		if (!UserUtils.getSubject().isPermitted("sys:slSysAdvertise:audit")){
			addMessage(redirectAttributes, "你没有删除或发布权限");
		}
		slSysAdvertiseService.delete(slSysAdvertise, isRe);
		addMessage(redirectAttributes, (isRe!=null&&isRe?"发布":"删除")+"广告位成功");
		return "redirect:"+Global.getAdminPath()+"/sys/slSysAdvertise/?repage";
	
	}

	/**
	 * 格式化对象中的adBody字符串，为json，并赋值对应键值在属性上。
	 * @param slSysAdvertise
	 */
	private void formatEntity(SlSysAdvertise slSysAdvertise) {
		if(StringUtils.isNotBlank(slSysAdvertise.getAdBody())) {
			try {
				Map<String, String> json = new Gson().fromJson(slSysAdvertise.getAdBody(), Map.class);
				slSysAdvertise.setTitle(json.get("title"));
				slSysAdvertise.setPhoto(json.get("photo"));
			} catch (JsonSyntaxException e) {
			}
		}
	}
}
/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.fulltl.wemall.modules.cms.web;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;
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

import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.fulltl.wemall.common.config.Global;
import com.fulltl.wemall.common.persistence.Page;
import com.fulltl.wemall.common.utils.StringUtils;
import com.fulltl.wemall.common.web.BaseController;
import com.fulltl.wemall.modules.cms.entity.CmsFileresource;
import com.fulltl.wemall.modules.cms.service.CmsFileresourceService;

/**
 * 上传文件管理Controller
 * @author ldk
 * @version 2017-11-15
 */
@Controller
@RequestMapping(value = "${adminPath}/cms/cmsFileresource")
public class CmsFileresourceController extends BaseController {

	@Autowired
	private CmsFileresourceService cmsFileresourceService;
	
	@ModelAttribute
	public CmsFileresource get(@RequestParam(required=false) String id) {
		CmsFileresource entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = cmsFileresourceService.get(id);
		}
		if (entity == null){
			entity = new CmsFileresource();
		}
		return entity;
	}
	
	@RequiresPermissions("cms:cmsFileresource:view")
	@RequestMapping(value = {"list", ""})
	public String list(CmsFileresource cmsFileresource, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<CmsFileresource> page = cmsFileresourceService.findPage(new Page<CmsFileresource>(request, response), cmsFileresource); 
		model.addAttribute("page", page);
		return "modules/cms/cmsFileresourceList";
	}

	@RequiresPermissions("cms:cmsFileresource:view")
	@RequestMapping(value = "form")
	public String form(CmsFileresource cmsFileresource, Model model) {
		model.addAttribute("cmsFileresource", cmsFileresource);
		return "modules/cms/cmsFileresourceForm";
	}

	@RequiresPermissions("cms:cmsFileresource:edit")
	@RequestMapping(value = "save")
	public String save(CmsFileresource cmsFileresource, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, cmsFileresource)){
			return form(cmsFileresource, model);
		}
		cmsFileresourceService.save(cmsFileresource);
		addMessage(redirectAttributes, "保存上传文件信息成功");
		return "redirect:"+Global.getAdminPath()+"/cms/cmsFileresource/?repage";
	}
	
	@RequiresPermissions("cms:cmsFileresource:edit")
	@RequestMapping(value = "delete")
	public String delete(CmsFileresource cmsFileresource, RedirectAttributes redirectAttributes) {
		cmsFileresourceService.delete(cmsFileresource);
		addMessage(redirectAttributes, "删除上传文件信息成功");
		return "redirect:"+Global.getAdminPath()+"/cms/cmsFileresource/?repage";
	}

	@RequiresPermissions("cms:cmsFileresource:view")
	@RequestMapping(value = "confirmExistsData")
	@ResponseBody
	public String confirmExistsData(HttpServletRequest request, Model model) {
		Map<String, Object> retMap = Maps.newHashMap();
		String ret = "200";
		String retMsg = "";
		String resourcepath = WebUtils.getCleanParam(request, "resourcepath");
		if(StringUtils.isBlank(resourcepath)) {
			retMap.put("ret", "200");
			retMap.put("retMsg", "确认文件管理中都已存在这些文件数据。");
			return new Gson().toJson(retMap);
		}
		try {
			resourcepath = URLDecoder.decode(resourcepath, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		if(resourcepath.startsWith("|")) {
			resourcepath = resourcepath.substring(1);
		}
		String[] files = resourcepath.split("\\|");
		for(int i=0; i<files.length; i++) {
			List<CmsFileresource> fileResource = cmsFileresourceService.findByFilePath(files[i]);
			if(fileResource.isEmpty()) {
				ret = "417";
				retMsg = retMsg + i + ",";
			}
		}
		if(retMsg.endsWith(",")) {
			retMsg = retMsg.substring(0, retMsg.length()-1);
		}
		retMap.put("ret", ret);
		if("200".equals(ret)) {
			retMap.put("retMsg", "确认文件管理中都已存在这些文件数据。");
		} else {
			retMap.put("retMsg", retMsg);
		}
		return new Gson().toJson(retMap);
	}
	
}
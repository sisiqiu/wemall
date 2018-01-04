/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.fulltl.wemall.modules.cms.web;

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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.fulltl.wemall.common.config.Global;
import com.fulltl.wemall.common.persistence.Page;
import com.fulltl.wemall.common.utils.StringUtils;
import com.fulltl.wemall.common.web.BaseController;
import com.fulltl.wemall.modules.cms.entity.CmsJpushRecord;
import com.fulltl.wemall.modules.cms.entity.CmsPushTag;
import com.fulltl.wemall.modules.cms.service.CmsJpushRecordService;
import com.fulltl.wemall.modules.cms.service.CmsPushTagService;
import com.fulltl.wemall.modules.sys.entity.Role;
import com.fulltl.wemall.modules.sys.service.SystemService;

/**
 * 极光推送历史记录管理Controller
 * @author ldk
 * @version 2017-12-09
 */
@Controller
@RequestMapping(value = "${adminPath}/cms/cmsJpushRecord")
public class CmsJpushRecordController extends BaseController {

	@Autowired
	private CmsJpushRecordService cmsJpushRecordService;
	@Autowired
	private CmsPushTagService cmsPushTagService;
	@Autowired
	private SystemService systemService;
	
	@ModelAttribute
	public CmsJpushRecord get(@RequestParam(required=false) String id) {
		CmsJpushRecord entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = cmsJpushRecordService.get(id);
		}
		if (entity == null){
			entity = new CmsJpushRecord();
		}
		return entity;
	}
	
	@RequiresPermissions("cms:cmsJpushRecord:view")
	@RequestMapping(value = {"list", ""})
	public String list(CmsJpushRecord cmsJpushRecord, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<CmsJpushRecord> page = cmsJpushRecordService.findPage(new Page<CmsJpushRecord>(request, response), cmsJpushRecord); 
		model.addAttribute("page", page);
		return "modules/cms/cmsJpushRecordList";
	}

	@RequiresPermissions("cms:cmsJpushRecord:view")
	@RequestMapping(value = "form")
	public String form(CmsJpushRecord cmsJpushRecord, Model model) {
		model.addAttribute("cmsJpushRecord", cmsJpushRecord);
		List<Role> allRoleList = systemService.findAllRole();
		model.addAttribute("roleList", allRoleList);
		List<CmsPushTag> tagList = cmsPushTagService.findList(new CmsPushTag());
		model.addAttribute("tagList", tagList);
		String roleIds = cmsJpushRecord.getRoleIds();
		String roleNames = cmsJpushRecord.getRoleNames();
		model.addAttribute("curRoleList", getListByArr(roleIds, roleNames));
		String tagIds = cmsJpushRecord.getTagIds();
		String tagNames = cmsJpushRecord.getTagNames();
		model.addAttribute("curTagList", getListByArr(tagIds, tagNames));
		String userIds = cmsJpushRecord.getUserIds();
		String userNames = cmsJpushRecord.getUserNames();
		model.addAttribute("curUserList", getListByArr(userIds, userNames));
		return "modules/cms/cmsJpushRecordForm";
	}

	@RequiresPermissions("cms:cmsJpushRecord:edit")
	@RequestMapping(value = "save")
	public String save(CmsJpushRecord cmsJpushRecord, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, cmsJpushRecord)){
			return form(cmsJpushRecord, model);
		}
		if(StringUtils.isNotBlank(cmsJpushRecord.getId())) {
			addMessage(redirectAttributes, "保存失败，不允许更新推送记录！");
			return "redirect:"+Global.getAdminPath()+"/cms/cmsJpushRecord/?repage";
		}
		Map<String, Object> retMap = cmsJpushRecordService.saveAndPush(cmsJpushRecord);
		if("0".equals(retMap.get("ret"))) {
			addMessage(redirectAttributes, "推送成功");
		} else {
			addMessage(redirectAttributes, "推送失败，失败原因：" + retMap.get("retMsg"));
		}
		return "redirect:"+Global.getAdminPath()+"/cms/cmsJpushRecord/?repage";
	}
	
	@RequiresPermissions("cms:cmsJpushRecord:edit")
	@RequestMapping(value = "delete")
	public String delete(CmsJpushRecord cmsJpushRecord, RedirectAttributes redirectAttributes) {
		cmsJpushRecordService.delete(cmsJpushRecord);
		addMessage(redirectAttributes, "删除极光推送历史记录成功");
		return "redirect:"+Global.getAdminPath()+"/cms/cmsJpushRecord/?repage";
	}

	/**
	 * 根据id数组和name数组生成list数组
	 * @param ids
	 * @param names
	 * @return
	 */
	private List<Map<String,String>> getListByArr(String ids, String names) {
		if(StringUtils.isBlank(ids) || StringUtils.isBlank(names)) {
			return null;
		}
		String[] idArr = ids.split(",");
		String[] nameArr = names.split(",");
		List<Map<String, String>> list = Lists.newArrayList();
		for(int i=0; i<idArr.length; i++) {
			Map<String, String> map = Maps.newHashMap();
			map.put("id", idArr[i]);
			map.put("name", nameArr[i]);
			list.add(map);
		}
		return list;
	}
}
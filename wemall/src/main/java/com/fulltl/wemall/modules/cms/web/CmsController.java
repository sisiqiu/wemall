/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.fulltl.wemall.modules.cms.web;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.google.common.collect.Lists;
import com.fulltl.wemall.common.web.BaseController;
import com.fulltl.wemall.modules.cms.entity.Category;
import com.fulltl.wemall.modules.cms.service.CategoryService;
import com.fulltl.wemall.modules.sys.utils.UserUtils;

/**
 * 内容管理Controller
 * @author ThinkGem
 * @version 2013-4-21
 */
@Controller
@RequestMapping(value = "${adminPath}/cms")
public class CmsController extends BaseController {

	@Autowired
	private CategoryService categoryService;
	
	@RequiresPermissions("cms:view")
	@RequestMapping(value = "")
	public String index() {
		return "modules/cms/cmsIndex";
	}
	
	@RequiresPermissions("cms:view")
	@RequestMapping(value = "tree")
	public String tree(Model model) {
		List<Category> categorys = categoryService.findByUser(true, null);
		List<Category> permittedCategorys = Lists.newArrayList();
		List<String> permitCategoryIds = Lists.newArrayList();
		for(int i=0; i<categorys.size(); i++) {
			if("1".equals(categorys.get(i).getParentId())) {
				//如果是顶级栏目直属栏目，判断权限值。如果没有设置，默认显示。如果设置了，判断权限值。
				String permission = categorys.get(i).getViewConfig();
				if(StringUtils.isBlank(permission)) {
					permitCategoryIds.add(categorys.get(i).getId());
				} else {
					boolean isPermitted = UserUtils.getSubject().isPermitted(permission);
					if(isPermitted) permitCategoryIds.add(categorys.get(i).getId());
				}
			}
		}
		
		//循环添加允许访问的类别。
		for(Category c : categorys) {
			if(permitCategoryIds.contains(c.getId())) {
				permittedCategorys.add(c);
			} else {
				for(String pCategoryId : permitCategoryIds) {
					if(c.getParentIds().contains(","+pCategoryId+",")) {
						permittedCategorys.add(c);
						break;
					}
				}
			}
		}
		model.addAttribute("categoryList", permittedCategorys);
		return "modules/cms/cmsTree";
	}
	
	@RequiresPermissions("cms:view")
	@RequestMapping(value = "none")
	public String none() {
		return "modules/cms/cmsNone";
	}

}

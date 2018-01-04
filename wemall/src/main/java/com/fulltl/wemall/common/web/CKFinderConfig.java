/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.fulltl.wemall.common.web;

import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.SecurityUtils;

import com.ckfinder.connector.configuration.Configuration;
import com.ckfinder.connector.data.AccessControlLevel;
import com.ckfinder.connector.data.ResourceType;
import com.ckfinder.connector.utils.AccessControlUtil;
import com.google.common.collect.Maps;
import com.fulltl.wemall.common.config.Global;
import com.fulltl.wemall.common.utils.FileUtils;
import com.fulltl.wemall.modules.sys.security.SystemAuthorizingRealm.Principal;
import com.fulltl.wemall.modules.sys.utils.UserUtils;

/**
 * CKFinder配置
 * @author ThinkGem
 * @version 2014-06-25
 */
public class CKFinderConfig extends Configuration {
	//private String ordinaryUserRoleEnName = "hr";
	//private String adminRoleEnName = "dept";
	
	public CKFinderConfig(ServletConfig servletConfig) {
        super(servletConfig);
    }
	
	@Override
    protected Configuration createConfigurationInstance() {
		Principal principal = (Principal) UserUtils.getPrincipal();
		if (principal == null){
			return new CKFinderConfig(this.servletConf);
		}
		
		AccessControlLevel alc = this.getAccessConrolLevels().get(0);
		String viewCommonFile = SecurityUtils.getSubject().getSession().getAttribute("viewCommonFile").toString();
		boolean isView = true;//UserUtils.getSubject().isPermitted("cms:ckfinder:view");
		boolean isUpload = true;//UserUtils.getSubject().isPermitted("cms:ckfinder:upload");
		boolean isEdit = true;//UserUtils.getSubject().isPermitted("cms:ckfinder:edit");
		//针对公共文件库，获取是否是普通用户
		if("1".equals(viewCommonFile)) {
			//针对公用文件库，重新设置types
			/*this.types.remove("files");
			this.types.remove("images");
			this.types.remove("flash");*/
			/*boolean isOrdinaryUser = UserUtils.getSubject().hasRole(ordinaryUserRoleEnName);
			//获取是否是管理员
			boolean isAdmin = UserUtils.getSubject().hasRole(adminRoleEnName);
			if(isOrdinaryUser && !isAdmin) {
				//如果不是管理员，且是普通用户
				alc.setResourceType("commonFiles");
			} else {
				alc.setResourceType("*");
			}*/
			alc.setResourceType("commonFiles");
			//isView = UserUtils.getSubject().isPermitted("cms:ckfinder:commonView");
			isUpload = UserUtils.getSubject().isPermitted("cms:ckfinder:commonUpload");
			isEdit = UserUtils.getSubject().isPermitted("cms:ckfinder:commonEdit");
		} else {
			alc.setResourceType("*");
			//this.types.remove("commonFiles");
		}
		
		alc.setFolderView(isView);
		alc.setFolderCreate(isEdit);
		alc.setFolderRename(isEdit);
		alc.setFolderDelete(isEdit);
		alc.setFileView(isView);
		alc.setFileUpload(isUpload);
		alc.setFileRename(isEdit);
		alc.setFileDelete(isEdit);
		
//		for (AccessControlLevel a : this.getAccessConrolLevels()){
//			System.out.println(a.getRole()+", "+a.getResourceType()+", "+a.getFolder()
//					+", "+a.isFolderView()+", "+a.isFolderCreate()+", "+a.isFolderRename()+", "+a.isFolderDelete()
//					+", "+a.isFileView()+", "+a.isFileUpload()+", "+a.isFileRename()+", "+a.isFileDelete());
//		}
		AccessControlUtil.getInstance(this).loadACLConfig();
		try {
//			Principal principal = (Principal)SecurityUtils.getSubject().getPrincipal();
//			this.baseURL = ServletContextFactory.getServletContext().getContextPath()+"/userfiles/"+principal+"/";
			if("1".equals(viewCommonFile)) {
				//针对公用文件库，设置为公用目录
				this.baseURL = FileUtils.path(Servlets.getRequest().getContextPath() + Global.USERFILES_BASE_URL + "common/");
				this.baseDir = FileUtils.path(Global.getUserfilesBaseDir() + Global.USERFILES_BASE_URL + "common/");
			} else {
				//针对私有文件库，设置为私有目录
				this.baseURL = FileUtils.path(Servlets.getRequest().getContextPath() + Global.USERFILES_BASE_URL + principal + "/");
				this.baseDir = FileUtils.path(Global.getUserfilesBaseDir() + Global.USERFILES_BASE_URL + principal + "/");
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return new CKFinderConfig(this.servletConf);
    }

    @Override  
    public boolean checkAuthentication(final HttpServletRequest request) {
        return UserUtils.getPrincipal()!=null;
    }

}

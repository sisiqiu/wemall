/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.fulltl.wemall.common.web;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.web.servlet.ShiroHttpServletRequest;

import com.ckfinder.connector.ConnectorServlet;
import com.fulltl.wemall.common.config.Global;
import com.fulltl.wemall.common.utils.FileUtils;
import com.fulltl.wemall.common.utils.ParameterRequestWrapper;
import com.fulltl.wemall.common.utils.RegExpValidatorUtil;
import com.fulltl.wemall.common.utils.SpringContextHolder;
import com.fulltl.wemall.modules.cms.service.CmsFileresourceService;
import com.fulltl.wemall.modules.sys.security.SystemAuthorizingRealm.Principal;
import com.fulltl.wemall.modules.sys.utils.UserUtils;

/**
 * CKFinderConnectorServlet
 * @author ThinkGem
 * @version 2014-06-25
 */
public class CKFinderConnectorServlet extends ConnectorServlet {
	
	private static CmsFileresourceService cmsFileresourceService = SpringContextHolder.getBean(CmsFileresourceService.class);
	private static final long serialVersionUID = 1L;
	
	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		ParameterRequestWrapper req = new ParameterRequestWrapper((ShiroHttpServletRequest)request);
		prepareGetResponse(req, response, false);
		super.doGet(req, response);
	}
	
	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		ParameterRequestWrapper req = new ParameterRequestWrapper((ShiroHttpServletRequest)request);
		prepareGetResponse(req, response, true);
		super.doPost(req, response);
	}
	
	private void prepareGetResponse(final HttpServletRequest request,
			final HttpServletResponse response, final boolean post) throws ServletException {
		//判断当前是否是使用公用文件库，确定初始化哪个文件库
		boolean viewCommonFile = false;
		for(Cookie c : request.getCookies()) {
			if("viewCommonFile".equals(c.getName()) && "1".equals(c.getValue())) {
				//使用公共文件库
				viewCommonFile = true;
			}
		}
		if(viewCommonFile) {
			SecurityUtils.getSubject().getSession().setAttribute("viewCommonFile", "1");
		} else {
			SecurityUtils.getSubject().getSession().setAttribute("viewCommonFile", "0");
		}
		
		Principal principal = (Principal) UserUtils.getPrincipal();
		if (principal == null){
			return;
		}
		String command = request.getParameter("command");
		String type = request.getParameter("type");
		//循环所有的参数，判断是否为乱码，若是乱码，进行转码
		Enumeration paramsEnum = request.getParameterNames();
		while(paramsEnum.hasMoreElements()) {
			reSetParameter(request, paramsEnum.nextElement().toString());
		}
		// 初始化时，如果startupPath文件夹不存在，则自动创建startupPath文件夹
		if ("Init".equals(command)){
			String startupPath = request.getParameter("startupPath");// 当前文件夹可指定为模块名
			try {
				startupPath = URLDecoder.decode(startupPath, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			if (startupPath!=null){
				String[] ss = startupPath.split(":");
				if (ss.length==2){
					String realPath = StringUtils.EMPTY;
					//根据是否使用公有文件库，选择快捷生成文件夹在哪个文件库中。
					if(viewCommonFile) {
						realPath = Global.getUserfilesBaseDir() + Global.USERFILES_BASE_URL
								 + "common/" + ss[0] + ss[1];
					} else {
						realPath = Global.getUserfilesBaseDir() + Global.USERFILES_BASE_URL
								+ principal + "/" + ss[0] + ss[1];
					}
					FileUtils.createDirectory(FileUtils.path(realPath));
				}
			}
		}
		// 快捷上传，自动创建当前文件夹，并上传到该路径
		else if ("QuickUpload".equals(command) && type!=null){
			String currentFolder = request.getParameter("currentFolder");// 当前文件夹可指定为模块名
			String realPath = StringUtils.EMPTY;
			//根据是否使用公有文件库，选择快捷生成文件夹在哪个文件库中。
			if(viewCommonFile) {
				realPath = Global.getUserfilesBaseDir() + Global.USERFILES_BASE_URL
						+ "common/" + type + (currentFolder != null ? currentFolder : "");
			} else {
				realPath = Global.getUserfilesBaseDir() + Global.USERFILES_BASE_URL
						+ principal + "/" + type + (currentFolder != null ? currentFolder : "");
			}
			FileUtils.createDirectory(FileUtils.path(realPath));
		}
		if("DownloadFile".equals(command) && viewCommonFile) {
			String currentFolder = request.getParameter("currentFolder");// 当前文件夹可指定为模块名
			String fileName = request.getParameter("FileName");
			String filePath = Global.USERFILES_BASE_URL
					+ "common/" + type + (currentFolder != null ? currentFolder : "") + fileName;
			cmsFileresourceService.updateDownloadNum(filePath);
			/*String filePath = request.getRequestURL() + "?" + request.getQueryString();
			try {
				filePath = URLDecoder.decode(filePath, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}*/
			//System.err.println(filePath);
			//根据filePath进行更新下载数操作。
		}
		/*reSetParameter(request, "FileName");
		reSetParameter(request, "fileName");
		reSetParameter(request, "newFileName");*/
//		System.out.println("------------------------");
//		for (Object key : request.getParameterMap().keySet()){
//			System.out.println(key + ": " + request.getParameter(key.toString()));
//		}
	}

	/**
	 * 判断参数是否为乱码，若是乱码进行转码。
	 * @param request
	 * @param parameterName
	 */
	private void reSetParameter(final HttpServletRequest request, String parameterName) {
		String parameter = request.getParameter(parameterName);
		try {
			if(parameter != null && RegExpValidatorUtil.isMessyCode(parameter)) {
				//解决中文文件无法下载的问题。
				parameter = new String(parameter.getBytes("ISO8859-1"), "UTF-8");
				((ParameterRequestWrapper)request).addParameter(parameterName, parameter);
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
}

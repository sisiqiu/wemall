package com.fulltl.wemall.modules.wx.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fulltl.wemall.common.web.BaseController;

@Controller
@RequestMapping(value = "${frontPath}/wx/core/common")
public class CommonRedirect extends BaseController {
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@RequestMapping(value = "returnToOtherUrl")
	public void returnToOtherUrl(HttpServletRequest request, HttpServletResponse response, Model model) {
		String otherUrl = WebUtils.getCleanParam(request, "otherUrl");
		String queryString = request.getQueryString();
		String platformQueryString = queryString.substring(queryString.indexOf(otherUrl), queryString.length());
		try {
			response.sendRedirect(platformQueryString);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

package com.fulltl.wemall.modules.wemall.web.front;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fulltl.wemall.common.web.BaseController;

/**
 * 用户相关信息管理前端接口
 * @author ldk
 *
 */
@Controller
@RequestMapping(value = "${frontPath}/interface/his/order")
public class SlHisUserRelatedFrontController extends BaseController {

	@RequestMapping(value = {"listForAjax"})
	@ResponseBody
	public String getDoctor(HttpServletRequest request, HttpServletResponse response, Model model) {
		return "";
	}
}

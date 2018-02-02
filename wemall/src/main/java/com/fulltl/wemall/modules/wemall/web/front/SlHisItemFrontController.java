package com.fulltl.wemall.modules.wemall.web.front;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fulltl.wemall.common.web.BaseController;
import com.fulltl.wemall.modules.wemall.service.front.SlHisItemFrontService;

/**
 * 商品管理前端接口
 * @author ldk
 *
 */
@Controller
@RequestMapping(value = "${frontPath}/interface/his/cusService")
public class SlHisItemFrontController extends BaseController {
	
	@Autowired 
	private SlHisItemFrontService slHisItemFrontService ;
	
}

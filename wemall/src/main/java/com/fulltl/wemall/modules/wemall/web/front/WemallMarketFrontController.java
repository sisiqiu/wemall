package com.fulltl.wemall.modules.wemall.web.front;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fulltl.wemall.common.web.BaseController;
import com.fulltl.wemall.modules.wemall.service.front.WemallMarketFrontService;

/**
 * 营销管理前端接口
 * @author ldk
 *
 */
@Controller
@RequestMapping(value = "${frontPath}/interface/wemall/market")
public class WemallMarketFrontController extends BaseController {
	
	@Autowired
	private WemallMarketFrontService slHisMarketServiceFrontService ;
	
	/**
	 * 根据活动类别，获取当前未过期的活动列表的接口。
	 * 
	 * 测试用例：
	 * 	url：http://ldkadmin.viphk.ngrok.org/f/interface/wemall/market/findListByActivityType
	 *	参数：
	 *		activityType（*）=活动类别
	 *
	 * 	例：
	 * 
	 * 结果示例：{"ret":"0","data":{},"retMsg":"获取成功！"}
	 * 		或
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = {"findListByActivityType"})
	@ResponseBody
	public String findListByActivityType(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> map = slHisMarketServiceFrontService.findListByActivityType(request);
		return gson.toJson(formatReturnMsg(map));
	}
	
	/**
	 * 根据活动id和活动类别，获取参与该活动的商品列表。
	 * 
	 * 测试用例：
	 * 	url：http://ldkadmin.viphk.ngrok.org/f/interface/wemall/market/findItemsByActivity
	 *	参数：
	 *		activityId（*）=活动id
	 *		activityType（*）=活动类别
	 *
	 * 	例：
	 * 
	 * 结果示例：{"ret":"0","data":{},"retMsg":"获取成功！"}
	 * 		或
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = {"findItemsByActivity"})
	@ResponseBody
	public String findItemsByActivity(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> map = slHisMarketServiceFrontService.findItemsByActivity(request);
		return gson.toJson(formatReturnMsg(map));
	}
	
	/**
	 * 获取充值设置列表。
	 * 
	 * 测试用例：
	 * 	url：http://ldkadmin.viphk.ngrok.org/f/interface/wemall/market/getRechargeList
	 *	参数：
	 *
	 * 	例：
	 * 
	 * 结果示例：{"ret":"0","data":{},"retMsg":"获取成功！"}
	 * 		或
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = {"getRechargeList"})
	@ResponseBody
	public String getRechargeList(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> map = slHisMarketServiceFrontService.getRechargeList(request);
		return gson.toJson(formatReturnMsg(map));
	}
	
}

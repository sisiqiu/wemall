package com.fulltl.wemall.modules.wemall.web.front;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fulltl.wemall.common.web.BaseController;
import com.fulltl.wemall.modules.wemall.entity.WemallOrderItem;
import com.fulltl.wemall.modules.wemall.service.front.SlHisOrderFrontService;
import com.google.gson.Gson;

/**
 * 订单管理前端接口
 * @author ldk
 *
 */
@Controller
@RequestMapping(value = "${frontPath}/interface/wemall/order")
public class SlHisOrderFrontController extends BaseController {

	@Autowired
	private SlHisOrderFrontService slHisOrderFrontService;
	
	/**
	 * 获取订单商品列表的接口。
	 * 
	 * 测试用例：
	 * 	url：http://ldkadmin.viphk.ngrok.org/f/interface/wemall/order/getOrderItemList
	 *	参数：
	 *		pageNo（*）=页码
	 *		pageSize（*）=每页条数
	 *		orderNo=订单号
	 *		status=状态（1、未付款，2、已付款，3、未发货，4、已发货，5、交易成功，6、交易退货，7、交易关闭）
	 *
	 * 	例：
	 * 
	 * 结果示例：{"ret":"0","data":{"list":[{},{}],"count":3},"retMsg":"获取成功！"}
	 * 		或
	 * 		{"ret":"-1","data":{},"retMsg":"缺少页码和每页条数！"}
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = {"getOrderItemList"})
	@ResponseBody
	public String getOrderItemList(WemallOrderItem wemallOrderItem, HttpServletRequest request, HttpServletResponse response, Model model) {
		Map<String, Object> map = slHisOrderFrontService.getOrderItemList(wemallOrderItem, request);
		return gson.toJson(formatReturnMsg(map));
	}
	
	/**
	 * 获取订单商品详情的接口。
	 * 
	 * 测试用例：
	 * 	url：http://ldkadmin.viphk.ngrok.org/f/interface/wemall/order/getOrderItemDetail
	 *	参数：
	 *		orderNo（*）=订单号
	 *		itemId（*）=商品id
	 *
	 * 	例：
	 * 
	 * 结果示例：{"ret":"0","data":{},"retMsg":"获取成功！"}
	 * 		或
	 * 		{"ret":"-1","data":{},"retMsg":"商品id不能为空！"}
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = {"getOrderItemDetail"})
	@ResponseBody
	public String getOrderItemDetail(WemallOrderItem wemallOrderItem, HttpServletRequest request, HttpServletResponse response, Model model) {
		Map<String, Object> map = slHisOrderFrontService.getOrderItemDetail(wemallOrderItem, request);
		return gson.toJson(formatReturnMsg(map));
	}
	
	/**
	 * 买家评价订单商品的接口。
	 * 
	 * 测试用例：
	 * 	url：http://ldkadmin.viphk.ngrok.org/f/interface/wemall/order/evaluateOrderItem
	 *	参数：
	 *		orderNo（*）=订单号
	 *		itemId（*）=商品id
	 *		buyerMessage=买家留言
	 *		buyerScore（*）=买家评分
	 *
	 * 	例：
	 * 
	 * 结果示例：{"ret":"0","data":{},"retMsg":"评价成功！"}
	 * 		或
	 * 		{"ret":"-1","data":{},"retMsg":"商品id不能为空！"}
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = {"evaluateOrderItem"})
	@ResponseBody
	public String evaluateOrderItem(WemallOrderItem wemallOrderItem, HttpServletRequest request, HttpServletResponse response, Model model) {
		Map<String, Object> map = slHisOrderFrontService.evaluateOrderItem(wemallOrderItem, request);
		return gson.toJson(formatReturnMsg(map));
	}
}

package com.fulltl.wemall.modules.wemall.web.front;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fulltl.wemall.common.web.BaseController;
import com.fulltl.wemall.modules.wemall.entity.WemallOrder.OrderStatus;
import com.fulltl.wemall.modules.wemall.entity.WemallOrder;
import com.fulltl.wemall.modules.wemall.entity.WemallOrderItem;
import com.fulltl.wemall.modules.wemall.entity.WemallShopCar;
import com.fulltl.wemall.modules.wemall.service.front.WemallOrderFrontService;
import com.google.common.collect.Maps;
import com.google.gson.Gson;

/**
 * 订单管理前端接口
 * @author ldk
 *
 */
@Controller
@RequestMapping(value = "${frontPath}/interface/wemall/order")
public class WemallOrderFrontController extends BaseController {

	@Autowired
	private WemallOrderFrontService slHisOrderFrontService;
	
	/**
	 * 根据订单号获取订单详情的接口。
	 * 
	 * 测试用例：
	 * 	url：http://ldkadmin.viphk.ngrok.org/f/interface/wemall/order/getOrderDetail
	 *	参数：
	 *		orderNo=订单号
	 * 	例：
	 * 
	 * 结果示例：{"ret":"0","data":{},"retMsg":"获取成功！"}
	 * 		或
	 * 		{"ret":"-1","data":{},"retMsg":"订单号不能为空！"}
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = {"getOrderDetail"})
	@ResponseBody
	public String getOrderDetail(HttpServletRequest request, HttpServletResponse response, Model model) {
		Map<String, Object> map = slHisOrderFrontService.getOrderDetail(request);
		return gson.toJson(formatReturnMsg(map));
	}
	
	/**
	 * 获取订单列表的接口。
	 * 
	 * 测试用例：
	 * 	url：http://ldkadmin.viphk.ngrok.org/f/interface/wemall/order/getOrderList
	 *	参数：
	 *
	 * 	例：
	 * 
	 * 结果示例：{"ret":"0","data":{"list":[{},{}],"count":3},"retMsg":"获取成功！"}
	 * 		或
	 * 		{"ret":"-1","data":{},"retMsg":"用户未登录！"}
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = {"getOrderList"})
	@ResponseBody
	public String getOrderList(WemallOrder wemallOrder, HttpServletRequest request, HttpServletResponse response, Model model) {
		Map<String, Object> map = slHisOrderFrontService.getOrderList(wemallOrder, request);
		return gson.toJson(formatReturnMsg(map));
	}
	
	/**
	 * 获取订单商品列表的接口。
	 * 
	 * 测试用例：
	 * 	url：http://ldkadmin.viphk.ngrok.org/f/interface/wemall/order/getOrderItemList
	 *	参数：
	 *		pageNo（*）=页码
	 *		pageSize（*）=每页条数
	 *		orderNo=订单号
	 *		itemId=商品id
	 *		status=状态（1、未付款，2、已付款，3、未发货，4、已发货，5、交易成功，6、交易退货，7、交易关闭）
	 *		buyerComment=买家是否已评价（1--已评价，0--未评价）
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
	 * 获取商品评价列表的接口。
	 * 
	 * 测试用例：
	 * 	url：http://ldkadmin.viphk.ngrok.org/f/interface/wemall/order/getBuyerCommentList
	 *	参数：
	 *		pageNo（*）=页码
	 *		pageSize（*）=每页条数
	 *		itemId=商品id
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
	@RequestMapping(value = {"getBuyerCommentList"})
	@ResponseBody
	public String getBuyerCommentList(WemallOrderItem wemallOrderItem, HttpServletRequest request, HttpServletResponse response, Model model) {
		Map<String, Object> map = slHisOrderFrontService.getBuyerCommentList(wemallOrderItem, request);
		return gson.toJson(formatReturnMsg(map));
	}
	
	/**
	 * 获取订单商品详情的接口。
	 * 
	 * 测试用例：
	 * 	url：http://ldkadmin.viphk.ngrok.org/f/interface/wemall/order/getOrderItemDetail
	 *	参数：
	 *		id（*）=订单商品id
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
	 *		id（*）=订单商品id
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
	
	/////////////////////////////////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////以下为生成订单部分///////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * 根据购物车id列表生成订单的接口。
	 * 
	 * 测试用例：
	 * 	url：http://ldkadmin.viphk.ngrok.org/f/interface/wemall/order/generateOrderByShopCarIds
	 *	参数：
	 *		shopCarIds（*）=购物车id列表
	 *
	 * 	例：
	 * 
	 * 结果示例：{"ret":"0","data":{},"retMsg":"生成成功！"}
	 * 		或
	 * 		{"ret":"-1","data":{},"retMsg":"购物车id列表不能为空！"}
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = {"generateOrderByShopCarIds"})
	@ResponseBody
	public String generateOrderByShopCarIds(HttpServletRequest request, HttpServletResponse response, Model model) {
		Map<String, Object> map = slHisOrderFrontService.generateOrderByShopCarIds(request);
		return gson.toJson(formatReturnMsg(map));
	}
	
	/**
	 * 根据单个商品生成订单的接口。
	 * 
	 * 测试用例：
	 * 	url：http://ldkadmin.viphk.ngrok.org/f/interface/wemall/order/generateOrderByItem
	 *	参数：
	 *		itemId（*）=商品id
	 *		itemNum（*）=商品数量
	 *		itemSpecIds=商品属性id列表
	 *
	 * 	例：
	 * 
	 * 结果示例：{"ret":"0","data":{},"retMsg":"生成成功！"}
	 * 		或
	 * 		{"ret":"-1","data":{},"retMsg":"商品id不能为空！"}
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = {"generateOrderByItem"})
	@ResponseBody
	public String generateOrderByItem(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> map = slHisOrderFrontService.generateOrderByItem(request);
		return gson.toJson(formatReturnMsg(map));
	}
	
	/**
	 * 更新订单地址的接口。
	 * 
	 * 测试用例：
	 * 	url：http://ldkadmin.viphk.ngrok.org/f/interface/wemall/order/updateOrderAddress
	 *	参数：
	 *		userAddressId（*）=收货地址id
	 *		orderNo（*）=订单号
	 *
	 * 	例：
	 * 
	 * 结果示例：{"ret":"0","data":{},"retMsg":"更新成功！"}
	 * 		或
	 * 		{"ret":"-1","data":{},"retMsg":"收货地址id不能为空！"}
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = {"updateOrderAddress"})
	@ResponseBody
	public String updateOrderAddress(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> map = slHisOrderFrontService.updateOrderAddress(request);
		return gson.toJson(formatReturnMsg(map));
	}
	
	/**
	 * 根据订单号和付款方式获取预付款id的接口。
	 * 
	 * 测试用例：
	 * 	url：http://ldkadmin.viphk.ngrok.org/f/interface/wemall/order/getPrepareIdForPay
	 *	参数：
	 *		paymentType（*）=付款方式
	 *		orderNo（*）=订单号
	 *		buyerMessage=买家留言
	 *
	 * 	例：
	 * 
	 * 结果示例：{"ret":"0","data":{},"retMsg":"生成成功！"}
	 * 		或
	 * 		{"ret":"-1","data":{},"retMsg":"商品id不能为空！"}
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = {"getPrepareIdForPay"})
	@ResponseBody
	public String getPrepareIdForPay(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> map = slHisOrderFrontService.getPrepareIdForPay(request);
		return gson.toJson(formatReturnMsg(map));
	}
	
	/**
	 * 取消订单的接口。
	 * 
	 * 测试用例：
	 * 	url：http://ldkadmin.viphk.ngrok.org/f/interface/wemall/order/cancelOrder
	 *	参数：
	 *		orderNo（*）=订单号
	 *
	 * 	例：
	 * 
	 * 结果示例：{"ret":"0","data":{},"retMsg":"生成成功！"}
	 * 		或
	 * 		{"ret":"-1","data":{},"retMsg":"订单号不能为空！"}
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = {"cancelOrder"})
	@ResponseBody
	public String cancelOrder(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> map = slHisOrderFrontService.updateOrderAndOrderItemStatus(WebUtils.getCleanParam(request, "orderNo"), 
																OrderStatus.alreadyCancelled.getValue());
		return gson.toJson(formatReturnMsg(map));
	}
	
	/**
	 * 用户确认收货的接口。
	 * 
	 * 测试用例：
	 * 	url：http://ldkadmin.viphk.ngrok.org/f/interface/wemall/order/alreadyReceived
	 *	参数：
	 *		orderNo（*）=订单号
	 *
	 * 	例：
	 * 
	 * 结果示例：{"ret":"0","data":{},"retMsg":"生成成功！"}
	 * 		或
	 * 		{"ret":"-1","data":{},"retMsg":"订单号不能为空！"}
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = {"alreadyReceived"})
	@ResponseBody
	public String alreadyReceived(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> map = slHisOrderFrontService.updateOrderAndOrderItemStatus(WebUtils.getCleanParam(request, "orderNo"), 
																OrderStatus.alreadyReceived.getValue());
		return gson.toJson(formatReturnMsg(map));
	}
	
	/**
	 * 用户评论订单商品的接口。
	 * 
	 * 测试用例：
	 * 	url：http://ldkadmin.viphk.ngrok.org/f/interface/wemall/order/commentItem
	 *	参数：
	 *		orderNo（*）=订单号
	 *		itemId（*）=商品id
	 *		buyerMessage=买家留言
	 *		buyerScore（*）=买家评分
	 *
	 * 	例：
	 * 
	 * 结果示例：{"ret":"0","data":{},"retMsg":"生成成功！"}
	 * 		或
	 * 		{"ret":"-1","data":{},"retMsg":"订单号不能为空！"}
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = {"commentItem"})
	@ResponseBody
	public String commentItem(WemallOrderItem wemallOrderItem, HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> map = slHisOrderFrontService.commentItem(wemallOrderItem, request);
		return gson.toJson(formatReturnMsg(map));
	}
}

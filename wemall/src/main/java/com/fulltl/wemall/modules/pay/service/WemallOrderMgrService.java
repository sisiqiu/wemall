/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.fulltl.wemall.modules.pay.service;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alipay.api.AlipayApiException;
import com.fulltl.wemall.common.service.CrudService;
import com.fulltl.wemall.modules.sys.entity.OrderDict;
import com.fulltl.wemall.modules.sys.utils.OrderDictUtils;
import com.fulltl.wemall.modules.wemall.dao.WemallOrderDao;
import com.fulltl.wemall.modules.wemall.entity.WemallItemActivity;
import com.fulltl.wemall.modules.wemall.entity.WemallOrder;
import com.fulltl.wemall.modules.wemall.entity.WemallOrder.PaymentType;
import com.fulltl.wemall.modules.wemall.entity.WemallOrderItem;
import com.fulltl.wemall.modules.wemall.service.WemallItemActivityService;
import com.fulltl.wemall.modules.wemall.service.WemallOrderItemService;
import com.fulltl.wemall.modules.wemall.service.WemallOrderService;
import com.fulltl.wemall.modules.wemall.service.WemallScoreInfoService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * 订单管理Service
 * @author ldk
 * @version 2017-11-27
 */
@Service
@Transactional(readOnly = true)
public class WemallOrderMgrService extends CrudService<WemallOrderDao, WemallOrder> {
	@Autowired 
	private WemallOrderService wemallOrderService;
	@Autowired 
	private WemallOrderItemService wemallOrderItemService;
	@Autowired 
	private WemallScoreInfoService wemallScoreInfoService;
	@Autowired 
	private WemallItemActivityService wemallItemActivityService;
	@Autowired 
	private AlipayTradeService alipayTradeService;
	@Autowired 
	private WeixinTradeService weixinTradeService;
	
	/**
	 * 调用生成订单的方法
	 * @param title
	 * @param orderPrice
	 * @param paymentType
	 * @return key值为wemallOrder的value为订单对象
	 */
	@Transactional(readOnly = false)
	public Map<String, Object> generateOrderByType(String title, Integer orderPrice, Integer freightPrice, Integer paymentType, String shopCarIds) {
    	//构造订单对象
		Map<String, Object> resultMap = Maps.newHashMap();
		if(StringUtils.isBlank(title) || orderPrice == null) {
			resultMap.put("ret", "-1");
			resultMap.put("retMsg", "标题，订单价格不能为空！");
			return resultMap;
		}
		WemallOrder wemallOrder = null;
		resultMap = wemallOrderService.generateOrderBy(title, 
														orderPrice, 
														freightPrice,
														paymentType);
		if(!"0".equals(resultMap.get("ret"))) return resultMap;
		else wemallOrder = (WemallOrder)resultMap.get("wemallOrder");
		wemallOrder.setShopCarIds(shopCarIds);
		wemallOrderService.saveOrder(wemallOrder);
		
		return resultMap;
	}
	
	/**
	 * 根据订单号，退款金额，退款描述，执行退款逻辑
	 * @param orderNo
	 * @param refundFee
	 * @param refundDesc
	 * @return
	 */
	@Transactional(readOnly = false)
	public Map<String, Object> refund(String orderNo, String refundFee, String refundDesc) {
		Map<String, Object> retMap = new HashMap<String, Object>();
		Map<String, String> params = Maps.newHashMap();
		params.put("orderNo", orderNo); //订单号
		params.put("refundFee", refundFee); //退款金额
		params.put("refundDesc", refundDesc); //退款原因
		
		WemallOrder wemallOrder = wemallOrderService.get(orderNo);
		if(PaymentType.alipay.getValue().equals(wemallOrder.getPaymentType())) {
			try {
				retMap = alipayTradeService.refund(params, wemallOrder);
			} catch (AlipayApiException e) {
				logger.error("支付宝支付取消预约退款失败！对应用户：" + wemallOrder.getUser().getId() + "。", e);
				throw new RuntimeException();
			} catch (Exception e) {
				logger.error("支付宝支付取消预约退款失败！对应用户：" + wemallOrder.getUser().getId() + "。", e);
				throw new RuntimeException();
			}
		} else if(PaymentType.weixin.getValue().equals(wemallOrder.getPaymentType())) {
			try {
				retMap = weixinTradeService.refund(params, wemallOrder);
			} catch (Exception e) {
				logger.error("微信支付取消预约退款失败！对应用户：" + wemallOrder.getUser().getId() + "。", e);
				throw new RuntimeException();
			}
		}
		
		return retMap;
	}
	
	/**
	 * 根据积分抵扣，活动优惠等减价方式，对订单价格及订单其他信息做更新。
	 * @param wemallOrder
	 * @param wemallOrderItemList
	 * @param request
	 */
	@Transactional(readOnly = false)
	public Map<String, Object> updateOrderPriceForDiscount(WemallOrder wemallOrder, List<WemallOrderItem> wemallOrderItemList,
			HttpServletRequest request) {
		Map<String, Object> retMap = new HashMap<String, Object>();
		//参加活动id和活动类型
		String activityId = WebUtils.getCleanParam(request, "activityId");
		String activityType = WebUtils.getCleanParam(request, "activityType");
		
		//根据活动id和活动类型
		if(StringUtils.isNotBlank(activityId) && StringUtils.isNotBlank(activityType)) {
			//校验所有商品是否都可参加该活动
			WemallItemActivity wemallItemActivity = wemallItemActivityService.checkActivityByIdAndType(wemallOrderItemList, activityId, activityType);
			if(wemallItemActivity == null) {
				retMap.put("ret", "-1");
				retMap.put("retMsg", "抱歉，不可参加该活动！");
				return retMap;
			}
			
			//根据活动的优惠方式，执行减价，更新订单价格，最小为0
			Integer orderPrice = wemallItemActivityService.getPriceByOrderPrice(wemallOrder.getOrderPrice(), wemallItemActivity);
			wemallOrder.setOrderPrice(orderPrice > 0 ? orderPrice : 0);
			
			//更新订单对象，设置参与了该活动
			wemallOrder.setActivityId(activityId);
			wemallOrder.setActivityType(activityType);
		}
		
		//使用积分数
		String scoreUsageNumStr = WebUtils.getCleanParam(request, "scoreUsageNum");
		
		
		if(StringUtils.isNotBlank(scoreUsageNumStr)) {
			Integer scoreUsageNum;
			try {
				scoreUsageNum = Integer.parseInt(scoreUsageNumStr);
			} catch (NumberFormatException e) {
				retMap.put("ret", "-1");
				retMap.put("retMsg", "使用积分数必须为整数！");
				return retMap;
			}
			//校验所用积分数是否超过当前用户剩余积分数
			if(!wemallScoreInfoService.checkUserScore(scoreUsageNum)) {
				retMap.put("ret", "-1");
				retMap.put("retMsg", "抱歉，用户当前积分数不足！");
				return retMap;
			}
			
			int deductPrice = 0;
			//执行根据积分减价，更新订单价格，最小为0
			List<OrderDict> orderDictList = OrderDictUtils.getOrderDictList("orderPrice_about_set");
			for(OrderDict orderDict : orderDictList) {
				if(orderDict.getValue().equals("1")) {
					String price = orderDict.getPrice();
					deductPrice = new BigDecimal(scoreUsageNum)
						.multiply(new BigDecimal(100))
						.divide(new BigDecimal(price), 0, RoundingMode.FLOOR)
						.intValue();
					int orderPrice = wemallOrder.getOrderPrice() - deductPrice;
					wemallOrder.setOrderPrice(orderPrice > 0 ? orderPrice : 0);
				}
			}
			
			//校验当前使用积分额对应的抵扣金额  是否超过  商品列表的积分最大抵扣金额
			if(!wemallScoreInfoService.checkItemsScoreDeductPrice(wemallOrderItemList, deductPrice)) {
				retMap.put("ret", "-1");
				retMap.put("retMsg", "抱歉，积分使用超过商品的最大抵扣金额！");
				return retMap;
			}
			
			//更新订单对象，设置使用积分数
			wemallOrder.setScoreUsageNum(scoreUsageNum);
		}
		
		retMap.put("ret", "0");
		retMap.put("retMsg", "更新成功！");
		return retMap;
	}
	
	/**
	 * 根据订单号，更新订单状态
	 * @param orderNo
	 * @param status
	 */
	/*@Transactional(readOnly = false)
	public void updateStatusByOrderNo(String orderNo, Integer status) {
		wemallOrderService.updateAllStatusByOrderNo(orderNo, status);
	}*/
	
	/**
	 * 查询订单详情
	 * @param request
	 * @param response
	 * @return
	 */
	public String orderQuery(HttpServletRequest request, HttpServletResponse response) {
		String result = StringUtils.EMPTY;
		String orderNo = WebUtils.getCleanParam(request, "orderNo");
		String trade_no = WebUtils.getCleanParam(request, "trade_no");
		String paymentType = WebUtils.getCleanParam(request, "paymentType");
		if(PaymentType.alipay.getValue().toString().equals(paymentType)) {
			try {
				result = alipayTradeService.orderQuery(orderNo, trade_no);
			} catch (AlipayApiException e) {
				logger.error("查看支付宝订单详情错误！" + e);
			}
		} else if(PaymentType.weixin.getValue().toString().equals(paymentType)) {
			result = weixinTradeService.orderQuery(orderNo, trade_no);
		}
		return result;
	}
	
	/**
	 * 查询退款详情
	 * @param request
	 * @param response
	 * @return
	 */
	public String refundQuery(HttpServletRequest request, HttpServletResponse response) {
		String result = StringUtils.EMPTY;
		String orderNo = WebUtils.getCleanParam(request, "orderNo");
		String trade_no = WebUtils.getCleanParam(request, "trade_no");
		String refundId = WebUtils.getCleanParam(request, "refundId");
		String paymentType = WebUtils.getCleanParam(request, "paymentType");
		if(PaymentType.alipay.getValue().toString().equals(paymentType)) {
			try {
				result = alipayTradeService.refundQuery(orderNo, trade_no, refundId);
			} catch (AlipayApiException e) {
				logger.error("查看支付宝订单详情错误！" + e);
			}
		} else if(PaymentType.weixin.getValue().equals(paymentType)) {
			result = weixinTradeService.refundQuery(orderNo, trade_no, refundId);
		}
		return result;
	}
	
	/**
	 * 下载对账单
	 * @param request
	 * @param response
	 * @return
	 */
	public Map<String, Object> downloadBill(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> retMap = Maps.newHashMap();
		String paymentType = WebUtils.getCleanParam(request, "paymentType");
		if(PaymentType.alipay.getValue().toString().equals(paymentType)) {
			try {
				retMap = alipayTradeService.downloadBill(request, response);
			} catch (AlipayApiException e) {
				logger.error("支付宝下载对账单失败！", e);
				throw new RuntimeException();
			} catch (UnsupportedEncodingException e1) {
				logger.error("支付宝下载对账单失败！", e1);
				throw new RuntimeException();
			}
		} else if(PaymentType.weixin.getValue().toString().equals(paymentType)) {
			retMap = weixinTradeService.downloadBill(request, response);
		} else {
			retMap.put("ret", "-1");
			retMap.put("retMsg", "第三方类型错误");
		}
		return retMap;
	}

}
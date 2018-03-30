/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.fulltl.wemall.modules.pay.service;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
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
import com.fulltl.wemall.modules.wemall.dao.WemallOrderDao;
import com.fulltl.wemall.modules.wemall.entity.WemallOrder;
import com.fulltl.wemall.modules.wemall.entity.WemallOrder.PaymentType;
import com.fulltl.wemall.modules.wemall.service.WemallOrderItemService;
import com.fulltl.wemall.modules.wemall.service.WemallOrderService;
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
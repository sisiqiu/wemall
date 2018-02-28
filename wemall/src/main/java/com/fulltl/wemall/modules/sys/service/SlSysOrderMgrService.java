/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.fulltl.wemall.modules.sys.service;

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
import com.fulltl.wemall.modules.his.service.front.trade.AlipayTradeService;
import com.fulltl.wemall.modules.his.service.front.trade.WeixinTradeService;
import com.fulltl.wemall.modules.sys.dao.SlSysOrderDao;
import com.fulltl.wemall.modules.sys.entity.SlSysOrder;
import com.fulltl.wemall.modules.sys.entity.SlSysOrder.PayMethod;
import com.google.common.collect.Maps;

/**
 * 订单管理Service
 * @author ldk
 * @version 2017-11-27
 */
@Service
@Transactional(readOnly = true)
public class SlSysOrderMgrService extends CrudService<SlSysOrderDao, SlSysOrder> {
	@Autowired 
	private SlSysOrderService slSysOrderService;
	@Autowired 
	private AlipayTradeService alipayTradeService;
	@Autowired 
	private WeixinTradeService weixinTradeService;
	
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
		
		SlSysOrder slSysOrder = slSysOrderService.get(orderNo);
		if(PayMethod.alipay.toString().equals(slSysOrder.getPayMethod())) {
			try {
				retMap = alipayTradeService.refund(params, slSysOrder);
			} catch (AlipayApiException e) {
				logger.error("支付宝支付取消预约退款失败！对应用户：" + slSysOrder.getUser().getId() + "。", e);
				throw new RuntimeException();
			}
		} else if(PayMethod.weixin.toString().equals(slSysOrder.getPayMethod())) {
			retMap = weixinTradeService.refund(params, slSysOrder);
		}
		
		return retMap;
	}
	
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
		String payMethod = WebUtils.getCleanParam(request, "payMethod");
		if(PayMethod.alipay.toString().equals(payMethod)) {
			try {
				result = alipayTradeService.orderQuery(orderNo, trade_no);
			} catch (AlipayApiException e) {
				logger.error("查看支付宝订单详情错误！" + e);
			}
		} else if(PayMethod.weixin.toString().equals(payMethod)) {
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
		String payMethod = WebUtils.getCleanParam(request, "payMethod");
		if(PayMethod.alipay.toString().equals(payMethod)) {
			try {
				result = alipayTradeService.refundQuery(orderNo, trade_no, refundId);
			} catch (AlipayApiException e) {
				logger.error("查看支付宝订单详情错误！" + e);
			}
		} else if(PayMethod.weixin.toString().equals(payMethod)) {
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
		String payMethod = WebUtils.getCleanParam(request, "payMethod");
		if(PayMethod.alipay.toString().equals(payMethod)) {
			try {
				retMap = alipayTradeService.downloadBill(request, response);
			} catch (AlipayApiException e) {
				logger.error("支付宝下载对账单失败！", e);
				throw new RuntimeException();
			} catch (UnsupportedEncodingException e1) {
				logger.error("支付宝下载对账单失败！", e1);
				throw new RuntimeException();
			}
		} else if(PayMethod.weixin.toString().equals(payMethod)) {
			retMap = weixinTradeService.downloadBill(request, response);
		} else {
			retMap.put("ret", "-1");
			retMap.put("retMsg", "第三方类型错误");
		}
		return retMap;
	}
}
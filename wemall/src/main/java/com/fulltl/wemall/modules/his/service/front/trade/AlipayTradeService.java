package com.fulltl.wemall.modules.his.service.front.trade;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.web.util.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.domain.AlipayTradeAppPayModel;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradeAppPayRequest;
import com.alipay.api.response.AlipayTradeAppPayResponse;
import com.fulltl.wemall.common.service.BaseService;
import com.fulltl.wemall.modules.alipay.core.AlipayConfig;
import com.fulltl.wemall.modules.alipay.core.pojo.AlipayTradeAllEntity;
import com.fulltl.wemall.modules.sys.entity.SlSysOrder;
import com.fulltl.wemall.modules.sys.service.SlSysOrderService;
import com.fulltl.wemall.modules.sys.utils.UserUtils;

/**
 * 医院客服管理前端服务层
 * @author ldk
 *
 */
@Service
@Transactional(readOnly = true)
public class AlipayTradeService extends BaseService {
	@Autowired 
	private SlSysOrderService slSysOrderService;
	
	/**
	 * 处理支付宝异步通知交易状态的方法
	 * @param alipayTradeAllEntity
	 * @param request
	 * @return
	 */
	@Transactional(readOnly = false)
	public String handleNotify(AlipayTradeAllEntity alipayTradeAllEntity, HttpServletRequest request) {
		boolean signVerified = rsaCheckV1(request);
		//——请在这里编写您的程序（以下代码仅作参考）——
		
		/* 实际验证过程建议商户务必添加以下校验：
		1、需要验证该通知数据中的out_trade_no是否为商户系统中创建的订单号，
		2、判断total_amount是否确实为该订单的实际金额（即商户订单创建时的金额），
		3、校验通知中的seller_id（或者seller_email) 是否为out_trade_no这笔单据的对应的操作方（有的时候，一个商户可能有多个seller_id/seller_email）
		4、验证app_id是否为该商户本身。
		*/
		if(signVerified) {//验证成功
			SlSysOrder slSysOrder = new SlSysOrder();
			//验证支付宝通知有效性的方法。并填充传入的订单对象。
			boolean checkNotifyVerified = checkNotifyVerified(alipayTradeAllEntity, slSysOrder);
			if(checkNotifyVerified) return "fail";
			
			//商户订单号
			String out_trade_no = alipayTradeAllEntity.getOut_trade_no();
			//支付宝交易号
			String trade_no = alipayTradeAllEntity.getTrade_no();
			//交易状态
			String trade_status = alipayTradeAllEntity.getTrade_status();
			//交易退款时间
			Date gmt_refund = alipayTradeAllEntity.getGmt_refund();
			
			if(trade_status.equals("TRADE_FINISHED")) {
				//注意：
				//退款日期超过可退款期限后（如三个月可退款），支付宝系统发送该交易状态通知
				//交易结束，执行交易关闭更新逻辑
				slSysOrder.setPayState("4");	//交易结束
				slSysOrder.setStatus("6");		//交易关闭
				slSysOrderService.save(slSysOrder);
			} else if (trade_status.equals("TRADE_SUCCESS")) {
				//注意：
				//付款完成后，支付宝系统发送该交易状态通知
				if(gmt_refund != null) {
					//说明是退款成功，执行退款记录验证逻辑，若有匹配的记录，略过，若没有，执行添加。
					//商户业务号
					String out_biz_no = alipayTradeAllEntity.getOut_biz_no();
					//总退款金额
					BigDecimal refund_fee = alipayTradeAllEntity.getRefund_fee();
					
					//执行退款记录添加逻辑
				} else {
					//是付款成功，更新订单状态和预约状态
					slSysOrderService.updateOrderAndUpdateRegStatus(slSysOrder);
				}
			}
			
			return "success";
		}else {//验证失败
			return "fail";
			//调试用，写文本函数记录程序运行情况是否正常
			//String sWord = AlipaySignature.getSignCheckContentV1(params);
			//AlipayConfig.logResult(sWord);
		}
	}

	/**
	 * app调用生成订单的方法
	 * @param request
	 * @return
	 */
	@Transactional(readOnly = false)
	public Map<String, Object> generateOrder(HttpServletRequest request) {
		Map<String, Object> retMap = new HashMap<String, Object>();
		String basePath = getBasePath(request);
		SlSysOrder slSysOrder = null;
		String regId = WebUtils.getCleanParam(request, "regId"); //预约id
		String orderPrice = WebUtils.getCleanParam(request, "orderPrice"); //订单价格
		//构造订单对象
		Map<String, Object> resultMap = slSysOrderService.generateOrderBy(regId, orderPrice, "alipay");
		if(!"0".equals(resultMap.get("ret"))) return resultMap;
		else slSysOrder = (SlSysOrder)resultMap.get("slSysOrder");
		
		//实例化客户端
		AlipayClient alipayClient = AlipayConfig.getAlipayClient();
		//实例化具体API对应的request类,类名称和接口名称对应,当前调用接口名称：alipay.trade.app.pay
		AlipayTradeAppPayRequest appPayRequest = new AlipayTradeAppPayRequest();
		//SDK已经封装掉了公共参数，这里只需要传入业务参数。以下方法为sdk的model入参方式(model和biz_content同时存在的情况下取biz_content)。
		AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();
		model.setBody(slSysOrder.getDescription());	//订单描述
		model.setSubject(slSysOrder.getSubject());	//订单标题
		model.setOutTradeNo(slSysOrder.getOrderNo());	//订单号
		model.setTimeoutExpress(AlipayConfig.timeoutExpress);
		model.setTotalAmount(slSysOrder.getActualPayment());	//订单总金额，单位为元，精确到小数点后两位
		model.setProductCode("QUICK_MSECURITY_PAY");
		appPayRequest.setBizModel(model);
		appPayRequest.setNotifyUrl(basePath + AlipayConfig.notify_url);
		try {
	        //这里和普通的接口调用不同，使用的是sdkExecute
	        AlipayTradeAppPayResponse appPayResponse = alipayClient.sdkExecute(appPayRequest);
	        //System.out.println(appPayResponse.getBody());//就是orderString 可以直接给客户端请求，无需再做处理。

	        if("10000".equals(appPayResponse.getCode())) {
	        	//支付宝创建订单成功，向数据库中插入新创建订单信息，并将对应预约中的订单字段更新为订单号
	        	slSysOrderService.saveOrderAndUpdateReg(slSysOrder, regId);
	        	logger.info("为用户'" + UserUtils.getUser().getLoginName() + "'生成订单，订单号为：" + slSysOrder.getOrderNo());
	        	retMap.put("ret", "0");
	        	retMap.put("retMsg", "订单生成成功");
	        	retMap.put("responseBody", appPayResponse.getBody());
	        } else {
	        	retMap.put("ret", "60032");
	        	retMap.put("retMsg", "支付宝错误，" + appPayResponse.getMsg());
	        }
		} catch (AlipayApiException e) {
	        logger.error("app调用生成订单出错", e);
	        retMap.put("ret", "60032");
			retMap.put("retMsg", "支付宝错误，订单生成失败");
		}
		return retMap;
	}
	
	/**
	 * 支付宝验参，验签名
	 * @param request
	 * @return
	 */
	private boolean rsaCheckV1(HttpServletRequest request) {
		//获取支付宝POST过来反馈信息
		Map<String,String> params = new HashMap<String,String>();
		Map<String,String[]> requestParams = request.getParameterMap();
		for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext();) {
			String name = (String) iter.next();
			String[] values = (String[]) requestParams.get(name);
			String valueStr = "";
			for (int i = 0; i < values.length; i++) {
				valueStr = (i == values.length - 1) ? valueStr + values[i]
						: valueStr + values[i] + ",";
			}
			params.put(name, valueStr);
		}
		boolean signVerified = false;
		try {
			//调用SDK验证签名
			signVerified = AlipaySignature.rsaCheckV1(params, AlipayConfig.alipay_public_key, AlipayConfig.charset, AlipayConfig.sign_type);
		} catch (AlipayApiException e) {
			logger.error("支付宝验证签名失败！", e);
			signVerified = false;
		} 
		return signVerified;
	}
	
	/**
	 * 验证支付宝通知有效性的方法。并填充传入的订单对象为匹配到的系统订单。
	 * @param alipayTradeAllEntity
	 * @param slSysOrder 可谓null
	 * @return
	 */
	private boolean checkNotifyVerified(AlipayTradeAllEntity alipayTradeAllEntity, SlSysOrder slSysOrder) {
		//根据订单号查询订单，若没有，忽略通知
		String out_trade_no = alipayTradeAllEntity.getOut_trade_no(); //商户订单号
		slSysOrder = slSysOrderService.get(out_trade_no);
		if(slSysOrder == null) return false;
		//若查到的订单对象中实际付款金额与total_amount不等，忽略通知
		BigDecimal total_amount = alipayTradeAllEntity.getTotal_amount();
		if(!total_amount.toString().equals(slSysOrder.getActualPayment())) return false;
		//验证app_id是否为该商户本身。
		if(!AlipayConfig.app_id.equals(alipayTradeAllEntity.getApp_id())) return false;
		//校验通知中的seller_id（或者seller_email) 是否为out_trade_no这笔单据的对应的操作方（有的时候，一个商户可能有多个seller_id/seller_email
		
		return true;
	}
}

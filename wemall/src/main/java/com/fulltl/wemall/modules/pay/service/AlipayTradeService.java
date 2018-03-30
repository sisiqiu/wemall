package com.fulltl.wemall.modules.pay.service;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Iterator;
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
import com.alipay.api.AlipayClient;
import com.alipay.api.domain.AlipayTradeAppPayModel;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayDataDataserviceBillDownloadurlQueryRequest;
import com.alipay.api.request.AlipayTradeAppPayRequest;
import com.alipay.api.request.AlipayTradeFastpayRefundQueryRequest;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.response.AlipayDataDataserviceBillDownloadurlQueryResponse;
import com.alipay.api.response.AlipayTradeAppPayResponse;
import com.fulltl.wemall.common.service.BaseService;
import com.fulltl.wemall.common.utils.IdGen;
import com.fulltl.wemall.modules.alipay.core.AlipayConfig;
import com.fulltl.wemall.modules.alipay.core.pojo.AlipayTradeAllEntity;
import com.fulltl.wemall.modules.sys.utils.UserUtils;
import com.fulltl.wemall.modules.wemall.entity.WemallOrder;
import com.fulltl.wemall.modules.wemall.entity.WemallOrder.PaymentType;
import com.fulltl.wemall.modules.wemall.entity.WemallOrderItem;
import com.fulltl.wemall.modules.wemall.entity.WemallRefund;
import com.fulltl.wemall.modules.wemall.service.WemallItemService;
import com.fulltl.wemall.modules.wemall.service.WemallOrderItemService;
import com.fulltl.wemall.modules.wemall.service.WemallOrderService;
import com.fulltl.wemall.modules.wemall.service.WemallRefundService;
import com.google.common.collect.Maps;
import com.google.gson.Gson;

/**
 * 医院客服管理前端服务层
 * @author ldk
 *
 */
@Service
@Transactional(readOnly = true)
public class AlipayTradeService extends BaseService {
	@Autowired 
	private WemallOrderService wemallOrderService;
	@Autowired 
	private WemallRefundService wemallRefundService;
	@Autowired 
	private WemallItemService wemallItemService;
	@Autowired 
	private WemallOrderItemService wemallOrderItemService;
	
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
			//验证支付宝通知有效性的方法。并填充传入的订单对象。
			WemallOrder wemallOrder = checkNotifyVerified(alipayTradeAllEntity);
			if(wemallOrder == null) return "fail";
			
			//商户订单号
			String out_trade_no = alipayTradeAllEntity.getOut_trade_no();
			//支付宝交易号
			String trade_no = alipayTradeAllEntity.getTrade_no();
			//交易状态
			String trade_status = alipayTradeAllEntity.getTrade_status();
			//总退款金额
			BigDecimal refund_fee = alipayTradeAllEntity.getRefund_fee();
			logger.info("接收到支付宝付款成功异步通知，商户订单号：" + out_trade_no);
			System.err.println(trade_status);
			if(trade_status.equals("TRADE_FINISHED") || trade_status.equals("TRADE_CLOSED")) {
				//注意：
				//退款日期超过可退款期限后（如三个月可退款），支付宝系统发送该交易状态通知
				//交易结束，执行交易关闭更新逻辑
				wemallOrder.setStatus(7);		//交易关闭
				wemallOrderService.save(wemallOrder);
			} else if (trade_status.equals("TRADE_SUCCESS")) {
				
				//注意：
				//付款完成后，支付宝系统发送该交易状态通知
				if(refund_fee != null) {
					//说明是退款成功，执行退款记录验证逻辑，若有匹配的记录，略过，若没有，执行添加。
					//商户业务号
					String out_biz_no = alipayTradeAllEntity.getOut_biz_no();
					
					//执行退款记录添加逻辑
				} else {
					//是付款成功，更新订单状态和预约状态
					wemallOrder.setPayment(alipayTradeAllEntity.getTotal_amount().movePointRight(2).intValue());
					wemallOrderService.updateOrderAndUpdateCareAppoStatus(wemallOrder);
					/*if(OrderTypeEnum.careAppo.getValue().equals(slSysOrder.getOrderType())) {
					} else {
						slSysOrderService.updateOrderAndUpdateRegStatus(slSysOrder);
					}*/
					
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

	/*@Transactional(readOnly = false)
	public Map<String, Object> generateOrderByReg(HttpServletRequest request) {
		Map<String, String> params = Maps.newHashMap();
		params.put("id",WebUtils.getCleanParam(request, "id")); //预约id
		params.put("orderPrice",WebUtils.getCleanParam(request, "orderPrice")); //订单价格
		return generateOrderByType(params, request, AppoTypeEnum.reg);
	}
	
	@Transactional(readOnly = false)
	public Map<String, Object> generateOrderByCareAppo(HttpServletRequest request) {
		Map<String, String> params = Maps.newHashMap();
		params.put("id",WebUtils.getCleanParam(request, "id")); //预约id
		params.put("orderPrice",WebUtils.getCleanParam(request, "orderPrice")); //订单价格
		return generateOrderByType(params, request, AppoTypeEnum.careAppo);
	}*/
	
	/**
	 * app调用生成订单的方法
	 * @param params 必须参数basePath，id，orderPrice
	 * @param type
	 * @return
	 */
	/*@Transactional(readOnly = false)
	public Map<String, Object> generateOrderByType(Map<String, String> params, HttpServletRequest request, AppoTypeEnum type) {
		Map<String, Object> retMap = new HashMap<String, Object>();
		WemallOrder wemallOrder = null;
		String id = params.get("id"); //预约id
		String orderPrice = params.get("orderPrice"); //订单价格
		//构造订单对象
		Map<String, Object> resultMap = Maps.newHashMap();
		resultMap = wemallOrderService.generateOrderByCareAppo(id, orderPrice, PaymentType.alipay.getValue().toString());
		switch(type) {
		case reg:
			resultMap = slSysOrderService.generateOrderBy(id, orderPrice, PayMethod.alipay.toString());
			break;
		case careAppo:
			resultMap = slSysOrderService.generateOrderByCareAppo(id, orderPrice, PayMethod.alipay.toString());
			break;
		}
		
		if(!"0".equals(resultMap.get("ret"))) return resultMap;
		else wemallOrder = (WemallOrder)resultMap.get("wemallOrder");
		
		//支付宝创建订单成功，向数据库中插入新创建订单信息，并将对应预约中的订单字段更新为订单号
		wemallOrderService.saveOrderAndUpdateCareAppo(wemallOrder, id);
    	switch(type) {
		case reg:
			slSysOrderService.saveOrderAndUpdateReg(wemallOrder, id);
			break;
		case careAppo:
			slSysOrderService.saveOrderAndUpdateCareAppo(wemallOrder, id);
			break;
		}
		
		retMap = generatePrepareIdByOrder(id, wemallOrder, request);
		return retMap;
	}*/
	
	/**
	 * app调用根据订单号生成预付款id的方法
	 * @param params
	 * @param request
	 * @param careappo
	 * @return
	 */
	@Transactional(readOnly = false)
	public Map<String, Object> generatePrepareIdByType(WemallOrder wemallOrder, HttpServletRequest request) {
		Map<String, Object> retMap = new HashMap<String, Object>();
		if(wemallOrder == null) {
			retMap.put("ret", "-1");
        	retMap.put("retMsg", "订单不存在。");
        	return retMap;
		}
		retMap = generatePrepareIdByOrder(wemallOrder, request);
		return retMap;
	}
	
	/**
	 * app调用根据订单对象生成预付款id的方法
	 * @param params
	 * @param request
	 * @param careappo
	 * @return
	 */
	@Transactional(readOnly = false)
	private Map<String, Object> generatePrepareIdByOrder(WemallOrder wemallOrder, HttpServletRequest request) {
		Map<String, Object> retMap = new HashMap<String, Object>();
		String basePath = getBasePath(request);
		
		//判断是否存在第三方订单号，执行减库存
        if(StringUtils.isBlank(wemallOrder.getPlatformOrderNo())) {
        	//执行减库存
        	wemallItemService.reduceStorage(wemallOrder.getOrderNo());
        }
        
        //生成并设置第三方订单号
        wemallOrder.setPlatformOrderNo(IdGen.generatePlatformOrderNo(wemallOrder.getOrderNo()));
		
		//实例化客户端
		AlipayClient alipayClient = AlipayConfig.getAlipayClient();
		AlipayTradeAppPayRequest appPayRequest = generateAppPayRequestByOrder(wemallOrder, basePath);
		try {
	        //这里和普通的接口调用不同，使用的是sdkExecute
	        AlipayTradeAppPayResponse appPayResponse = alipayClient.sdkExecute(appPayRequest);
	        //System.out.println(appPayResponse.getBody());//就是orderString 可以直接给客户端请求，无需再做处理。
	        wemallOrder.setPrepayId(appPayResponse.getBody());
	        wemallOrder.setPaymentType(PaymentType.alipay.getValue());
        	
        	//更新预付款id和付款方式到订单中
	        wemallOrderService.updatePrepayIdAndPayMethod(wemallOrder);
        	
        	logger.info("为用户'" + UserUtils.getUser().getLoginName() + "'生成订单，订单号为：" + wemallOrder.getOrderNo());
        	retMap.put("ret", "0");
        	retMap.put("retMsg", "订单生成成功");
        	retMap.put("prepay_id", appPayResponse.getBody());
		} catch (AlipayApiException e) {
	        logger.error("app调用生成订单出错", e);
	        retMap.put("ret", "60032");
			retMap.put("retMsg", "支付宝错误，订单生成失败");
		}
		return retMap;
	}
	
	/**
	 * 退款
	 * @param params 要有参数orderNo，refundFee，refundDesc
	 * @param slSysOrder 传入对应的订单对象，如果为空，则会根据参数中的orderNo查询，如果不为空，省略查询
	 * @return
	 * @throws Exception 
	 */
	@Transactional(readOnly = false)
	public Map<String, Object> refund(Map<String, String> params, WemallOrder wemallOrder) throws Exception {
		Map<String, Object> retMap = Maps.newHashMap();
		
		//获得初始化的AlipayClient
		AlipayClient alipayClient = AlipayConfig.getAlipayClient();
		
		//设置请求参数
		AlipayTradeRefundRequest alipayRequest = new AlipayTradeRefundRequest();
		
		//商户订单号，商户网站订单系统中唯一订单号
		String orderNo = params.get("orderNo");
		//需要退款的金额，该金额不能大于订单金额，必填
		String refundFee = params.get("refundFee");
		//退款的原因说明
		String refundDesc = params.get("refundDesc");
		//退款业务号，标识一次退款请求，同一笔交易多次退款需要保证唯一，如需部分退款，则此参数必传
		String refundId = IdGen.generateRefundId();
		
		//查询订单
		if(wemallOrder == null) {
			wemallOrder = wemallOrderService.get(orderNo);
		}
		if(wemallOrder == null) {
			retMap.put("ret", "-1");
			retMap.put("retMsg", "没有对应订单");
			return retMap;
		}
		BigDecimal refundFeeDecimal = new BigDecimal(refundFee);
		BigDecimal actualPayment = new BigDecimal(wemallOrder.getPayment());
		if(refundFeeDecimal.compareTo(actualPayment) == 1) {
			retMap.put("ret", "-1");
			retMap.put("retMsg", "退款金额不能大于订单实际支付金额");
			return retMap;
		}
		
		String bizContent = StringUtils.EMPTY;
		bizContent = "{\"out_trade_no\":\""+ orderNo +"\"," 
				+ "\"refund_amount\":\""+ refundFee +"\"," 
				+ "\"refund_reason\":\""+ refundDesc +"\"," 
				+ "\"out_request_no\":\""+ refundId +"\"}";
		alipayRequest.setBizContent(bizContent);
		
		//先执行更新，防止之后支付宝通知的数据库操作在本数据库更新操作的顺序混乱
		//添加退款记录信息
		WemallRefund refund = new WemallRefund();
		/*refund.setOrderNo(orderNo);
		refund.setUser(slSysOrder.getUser());
		refund.setOrderPrice(slSysOrder.getOrderPrice());
		refund.setPayment(slSysOrder.getActualPayment());
		refund.setRefundFee(refundFee);
		refund.setRefundDesc(refundDesc);
		refund.setRefundId(refundId);
		refund.setRefundStatus("1");  //退款状态（0--未成功，1--已退款）
		refund.setRefundDate(new Date());//退款时间
		refund.setPayMethod(PayMethod.alipay.toString());
		refund.setIsNewRecord(true);*/
		wemallRefundService.save(refund);
		//更新订单退款金额
		wemallOrderService.updateOrderRefundFee(wemallOrder, refundFee);
		
		//请求
		String result = alipayClient.execute(alipayRequest).getBody();
		
		Map<String, Object> responseMap = new Gson().fromJson(result, Map.class);
		Map<String, Object> resultMap = (Map<String, Object>)responseMap.get("alipay_trade_refund_response");
		if("10000".equals(resultMap.get("code"))) {
			//退款成功
		} else {
			throw new Exception("退款失败，支付宝错误码：" + resultMap.get("code"));
		}
		
		//输出
		retMap.put("ret", "0");
		retMap.put("retMsg", "退款成功");
		return retMap;
	}
	
	/**
	 * 根据商户订单号或者支付宝交易号查询订单详情。商户订单号和支付宝交易号请二选一设置。都添加的话，将采用支付宝交易号。
	 * @param orderNo 商户订单号，商户网站订单系统中唯一订单号
	 * @param trade_no 支付宝交易号
	 * @return
	 * @throws AlipayApiException
	 */
	public String orderQuery(String orderNo, String trade_no) throws AlipayApiException {
		//获得初始化的AlipayClient
		AlipayClient alipayClient = AlipayConfig.getAlipayClient();
		
		//设置请求参数
		AlipayTradeQueryRequest alipayRequest = new AlipayTradeQueryRequest();
		
		String bizContent = StringUtils.EMPTY;
		if(StringUtils.isNotBlank(trade_no)) {
			bizContent = "{\"trade_no\":\""+ trade_no +"\"}";
		} else {
			bizContent = "{\"out_trade_no\":\""+ orderNo +"\"}";
		}
		alipayRequest.setBizContent(bizContent);
		
		//请求
		String result = alipayClient.execute(alipayRequest).getBody();
		
		//输出
		return result;
	}
	
	/**
	 * 根据商户订单号或者支付宝交易号查询退款详情。商户订单号和支付宝交易号请二选一设置。都添加的话，将采用支付宝交易号。
	 * @param orderNo 商户订单号，商户网站订单系统中唯一订单号
	 * @param trade_no 支付宝交易号
	 * @param refundId 退款业务号
	 * @return
	 * @throws AlipayApiException
	 */
	public String refundQuery(String orderNo, String trade_no, String refundId) throws AlipayApiException {
		//获得初始化的AlipayClient
		AlipayClient alipayClient = AlipayConfig.getAlipayClient();
		
		//设置请求参数
		AlipayTradeFastpayRefundQueryRequest alipayRequest = new AlipayTradeFastpayRefundQueryRequest();
		
		String bizContent = StringUtils.EMPTY;
		if(StringUtils.isNotBlank(trade_no)) {
			bizContent = "{\"out_trade_no\":\""+ orderNo +"\"," 
					+"\"trade_no\":\""+ trade_no +"\","
					+"\"out_request_no\":\""+ refundId +"\"}";
		} else {
			bizContent = "{\"out_trade_no\":\""+ orderNo +"\"," 
					+"\"out_request_no\":\""+ refundId +"\"}";
		}
		alipayRequest.setBizContent(bizContent);
		
		//请求
		String result = alipayClient.execute(alipayRequest).getBody();
		
		//输出
		return result;
	}
	
	/**
	 * 下载对账单
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws AlipayApiException
	 */
	public Map<String, Object> downloadBill(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException, AlipayApiException {
		Map<String, Object> retMap = Maps.newHashMap();
		String bill_type = WebUtils.getCleanParam(request, "bill_type");
		if(!"trade".equals(bill_type) && !"signcustomer".equals(bill_type)) {
			retMap.put("ret", "-1");
			retMap.put("retMsg", "bill_type参数不能为空，且bill_type账单类型为：trade、signcustomer；\n"
					+ "trade指商户基于支付宝交易收单的业务账单；signcustomer是指基于商户支付宝余额收入及支出等资金变动的帐务账单");
			return retMap;
		}
		String bill_date = WebUtils.getCleanParam(request, "bill_date");
		//获得初始化的AlipayClient
		AlipayClient alipayClient = AlipayConfig.getAlipayClient();
		AlipayDataDataserviceBillDownloadurlQueryRequest alipayRequest = new AlipayDataDataserviceBillDownloadurlQueryRequest();
		alipayRequest.setBizContent("{" +
					"\"bill_type\":\"" + bill_type + "\"," + //trade
					"\"bill_date\":\"" + bill_date + "\"" +
					"  }");
		AlipayDataDataserviceBillDownloadurlQueryResponse result = alipayClient.execute(alipayRequest);
		if(result.isSuccess()){
			String billDownloadUrl = result.getBillDownloadUrl();
			/*try {
				response.sendRedirect(billDownloadUrl);
			} catch (IOException e) {
				e.printStackTrace();
			}*/
			retMap.put("ret", "0");
			retMap.put("retMsg", billDownloadUrl);
			return retMap;
		} else {
			retMap.put("ret", "-1");
			retMap.put("retMsg", "调用失败, 错误码：" + result.getCode() + "; 错误信息：" + result.getSubMsg());
			return retMap;
		}
	}
	
	/**
	 * 根据订单实体类构建AlipayTradeAppPayRequest对象
	 * @param slSysOrder
	 * @param basePath
	 * @return
	 */
	private AlipayTradeAppPayRequest generateAppPayRequestByOrder(WemallOrder wemallOrder, String basePath) {
		//实例化具体API对应的request类,类名称和接口名称对应,当前调用接口名称：alipay.trade.app.pay
		AlipayTradeAppPayRequest appPayRequest = new AlipayTradeAppPayRequest();
		//SDK已经封装掉了公共参数，这里只需要传入业务参数。以下方法为sdk的model入参方式(model和biz_content同时存在的情况下取biz_content)。
		AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();
		model.setBody(wemallOrder.getBody());	//订单描述
		model.setSubject(wemallOrder.getTitle());	//订单标题
		model.setOutTradeNo(wemallOrder.getOrderNo());	//订单号
		model.setTimeoutExpress(AlipayConfig.timeoutExpress);
		//model.setTotalAmount(slSysOrder.getActualPayment());	//订单总金额，单位为元，精确到小数点后两位
		//model.setTotalAmount(wemallOrder.getOrderPrice());	//订单总金额，单位为元，精确到小数点后两位
		model.setProductCode("QUICK_MSECURITY_PAY");
		appPayRequest.setBizModel(model);
		appPayRequest.setNotifyUrl(basePath + AlipayConfig.notify_url);
		return appPayRequest;
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
	 * @return 若为null，则验证未通过，若不为空，验证通过
	 */
	private WemallOrder checkNotifyVerified(AlipayTradeAllEntity alipayTradeAllEntity) {
		//根据订单号查询订单，若没有，忽略通知
		String out_trade_no = alipayTradeAllEntity.getOut_trade_no(); //商户订单号
		WemallOrder wemallOrder = wemallOrderService.get(out_trade_no);
		if(wemallOrder == null) return null;
		//若查到的订单对象中实际付款金额与total_amount不等，忽略通知
		BigDecimal total_amount = alipayTradeAllEntity.getTotal_amount();
		//if(!total_amount.toString().equals(slSysOrder.getActualPayment())) return null;
		if(!total_amount.toString().equals(wemallOrder.getOrderPrice())) return null;
		//验证app_id是否为该商户本身。
		if(!AlipayConfig.app_id.equals(alipayTradeAllEntity.getApp_id())) return null;
		//校验通知中的seller_id（或者seller_email) 是否为out_trade_no这笔单据的对应的操作方（有的时候，一个商户可能有多个seller_id/seller_email
		
		return wemallOrder;
	}

}

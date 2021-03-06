package com.fulltl.wemall.modules.pay.service;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Date;
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

import com.fulltl.wemall.common.service.BaseService;
import com.fulltl.wemall.common.utils.BeanToMapUtils;
import com.fulltl.wemall.common.utils.CallServletUtil;
import com.fulltl.wemall.common.utils.IdGen;
import com.fulltl.wemall.common.utils.XMLUtils;
import com.fulltl.wemall.modules.sys.utils.UserUtils;
import com.fulltl.wemall.modules.wemall.entity.WemallOrder;
import com.fulltl.wemall.modules.wemall.entity.WemallOrder.OrderStatus;
import com.fulltl.wemall.modules.wemall.entity.WemallOrder.PaymentType;
import com.fulltl.wemall.modules.wemall.entity.WemallRefund;
import com.fulltl.wemall.modules.wemall.service.WemallItemService;
import com.fulltl.wemall.modules.wemall.service.WemallOrderItemService;
import com.fulltl.wemall.modules.wemall.service.WemallOrderService;
import com.fulltl.wemall.modules.wemall.service.WemallRefundService;
import com.fulltl.wemall.modules.wemall.service.WemallShopCarService;
import com.fulltl.wemall.modules.wx.core.ParamsGenerator;
import com.fulltl.wemall.modules.wx.core.WeixinTradeConfig;
import com.fulltl.wemall.modules.wx.core.pojo.trade.WeixinTradeAllEntity;
import com.fulltl.wemall.modules.wx.core.utils.ApiclientCert;
import com.fulltl.wemall.modules.wx.core.utils.WeixinTradeSignature;
import com.fulltl.wemall.modules.wx.entity.WxUserInfo;
import com.fulltl.wemall.modules.wx.service.WxUserInfoService;
import com.google.common.collect.Maps;


/**
 * 医院客服管理前端服务层
 * @author ldk
 *
 */
@Service
@Transactional(readOnly = true)
public class WeixinTradeService extends BaseService {
	@Autowired
	private WemallOrderService wemallOrderService;
	@Autowired
	private WemallRefundService wemallRefundService;
	@Autowired
	private WemallShopCarService wemallShopCarService;
	@Autowired
	private WemallItemService wemallItemService;
	@Autowired
	private WemallOrderItemService wemallOrderItemService;
	@Autowired
	private WxUserInfoService wxUserInfoService;
	
	/**
	 * 处理微信异步通知交易状态的方法
	 * @param weixinTradeAllEntity
	 * @param request
	 * @return
	 */
	@Transactional(readOnly = false)
	public String handleNotify(HttpServletRequest request) {
		Map<String, String> weixinParamsMap = Maps.newHashMap();
		logger.info("handleNotify");
		try {
			weixinParamsMap = XMLUtils.parseXml(request);
			logger.info("handleNotify : map---" + weixinParamsMap);
		} catch (Exception e) {
			logger.info("请求微信统一下单接口，返回数据解析出错。",e);
        	return "fail";
		}
		boolean signVerified = checkSignature(weixinParamsMap);//微信验参，验签名
		if(signVerified) {
			//验签成功
			WeixinTradeAllEntity weixinTradeAllEntity = BeanToMapUtils.toBean(WeixinTradeAllEntity.class, weixinParamsMap);
			
			Map<String, Object> retMap = checkResultSuccess(weixinTradeAllEntity);
			if(!"0".equals(retMap.get("ret"))) {
				logger.info("checkResultSuccess fail : retMap---" + retMap);
				return "fail";
			}
			
			//验证微信通知有效性的方法。并填充传入的订单对象。
			WemallOrder wemallOrder = checkNotifyVerified(weixinTradeAllEntity);
			if(wemallOrder == null) return "fail";
			
			// ↓↓↓↓↓↓↓↓↓↓所有验证通过,根据参数执行业务逻辑↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
			//商户订单号
			String out_trade_no = weixinTradeAllEntity.getOut_trade_no();
			//微信交易号
			String trade_no = weixinTradeAllEntity.getTransaction_id();
			//交易状态
			String return_code = weixinTradeAllEntity.getReturn_code();
			
			if("CLOSED".equals(return_code)) {
				//CLOSED—已关闭
				wemallOrderService.updateAllStatusByOrderNo(wemallOrder, OrderStatus.alreadyClosed.getValue());
			} else if ("SUCCESS".equals(return_code)) {
				//SUCCESS—支付成功
				//是付款成功，更新订单状态和预约状态
				wemallOrder.setPayment(Integer.parseInt(weixinTradeAllEntity.getTotal_fee()));
				wemallOrderService.updateAllStatusByOrderNo(wemallOrder, OrderStatus.alreadyPaid.getValue());
				//减库存
				/*WemallOrderItem query = new WemallOrderItem();
				query.setOrderNo(wemallOrder.getOrderNo());
				List<WemallOrderItem> wemallOrderItemList = wemallOrderItemService.findList(query);
				wemallItemService.reduceStorage(wemallOrderItemList);*/
			} else {
				logger.info("return_code fail : return_code=" + return_code);
				//没有trade_state字段，或其他trade_state字段值时
				//退款状态
				String refund_status = weixinTradeAllEntity.getRefund_status();
				if("SUCCESS".equals(refund_status)) {
					//说明是退款成功，执行退款记录验证逻辑，若有匹配的记录，略过，若没有，执行添加。
					//商户退款单号
					String out_refund_no = weixinTradeAllEntity.getOut_refund_no();
					
					//微信退款单号
					String refund_id = weixinTradeAllEntity.getRefund_id();
					
					//申请退款金额
					//BigDecimal refund_fee = weixinTradeAllEntity.getRefund_fee();
					
					//执行退款记录添加逻辑
				}
			}
			
			return "success";
		}else {//验证失败
			logger.info("signVerified fail : map---" + weixinParamsMap);
			return "fail";
			//调试用，写文本函数记录程序运行情况是否正常
			//String sWord = weixinSignature.getSignCheckContentV1(params);
			//weixinConfig.logResult(sWord);
		}
	}
	
	/*@Transactional(readOnly = false)
	public Map<String, Object> generateOrderByReg(HttpServletRequest request) {
		Map<String, String> params = Maps.newHashMap();
		params.put("id",WebUtils.getCleanParam(request, "id")); //预约id
		params.put("orderPrice",WebUtils.getCleanParam(request, "orderPrice")); //订单价格
		//return generateOrderByType(params, request, AppoTypeEnum.reg);
		return null;
	}
	
	@Transactional(readOnly = false)
	public Map<String, Object> generateOrderByCareAppo(HttpServletRequest request) {
		Map<String, String> params = Maps.newHashMap();
		params.put("id",WebUtils.getCleanParam(request, "id")); //预约id
		params.put("orderPrice",WebUtils.getCleanParam(request, "orderPrice")); //订单价格
		//return generateOrderByType(params, request, AppoTypeEnum.careAppo);
		return null;
	}*/

	/**
	 * app调用生成订单的方法
	 * @param request
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
		resultMap = wemallOrderService.generateOrderByCareAppo(id, orderPrice, PaymentType.weixin.getValue().toString());
		switch(type) {
		case reg:
			resultMap = slSysOrderService.generateOrderBy(id, orderPrice, PayMethod.weixin.toString());
			break;
		case careAppo:
			resultMap = slSysOrderService.generateOrderByCareAppo(id, orderPrice, PayMethod.weixin.toString());
			break;
		}
		if(!"0".equals(resultMap.get("ret"))) return resultMap;
		else wemallOrder = (WemallOrder)resultMap.get("wemallOrder");
		
		//微信统一下单成功，向数据库中插入新创建订单信息，并将对应预约中的订单字段更新为订单号
		wemallOrderService.saveOrderAndUpdateCareAppo(wemallOrder, id);
		switch(type) {
		case reg:
			slSysOrderService.saveOrderAndUpdateReg(slSysOrder, id);
			break;
		case careAppo:
			slSysOrderService.saveOrderAndUpdateCareAppo(slSysOrder, id);
			break;
		}
		
		retMap = generatePrepareIdByOrder(id, wemallOrder, request, type);
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
		String buyerMessage = WebUtils.getCleanParam(request, "buyerMessage");//订单号
		if(wemallOrder == null) {
			retMap.put("ret", "-1");
        	retMap.put("retMsg", "订单不存在。");
        	return retMap;
		}
		wemallOrder.setBuyerMessage(buyerMessage);
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
		
		//判断是否存在第三方订单号，执行减库存
        if(StringUtils.isBlank(wemallOrder.getPlatformOrderNo())) {
        	//执行减库存
        	wemallItemService.reduceStorage(wemallOrder.getOrderNo());
        }
        
        //生成并设置第三方订单号
        wemallOrder.setPlatformOrderNo(IdGen.generatePlatformOrderNo(wemallOrder.getOrderNo()));
		
		//为调用微信接口统一下单构建参数map
        WxUserInfo query = new WxUserInfo();
        query.setUser(wemallOrder.getUser());
        List<WxUserInfo> wxUserInfoList = wxUserInfoService.findList(query);
        if(wxUserInfoList == null || wxUserInfoList.size() == 0) {
        	retMap.put("ret", "-1");
        	retMap.put("retMsg", "用户不存在。");
        	return retMap;
        }
        
		Map<String, String> paramsMap = ParamsGenerator.generateParamsForUnifiedOrder(wemallOrder, wxUserInfoList.get(0).getOpenId(), request);
		
		//请求微信统一下单接口，获取返回数据
		String result = CallServletUtil.sendPost(WeixinTradeConfig.UNIFIED_ORDER_URL, ParamsGenerator.mapToXML(paramsMap), null);
		Map<String, String> weixinParamsMap = Maps.newHashMap();
		try {
			weixinParamsMap = XMLUtils.parseXml(result);
		} catch (Exception e) {
			logger.error("请求微信统一下单接口，返回数据解析出错。",e);
			retMap.put("ret", "-1");
        	retMap.put("retMsg", "请求微信统一下单接口，返回数据解析出错。");
        	return retMap;
		}
		boolean signVerified = checkSignature(weixinParamsMap);
		if(signVerified) {
			//验签成功
			WeixinTradeAllEntity weixinTradeAllEntity = BeanToMapUtils.toBean(WeixinTradeAllEntity.class, weixinParamsMap);
			
			retMap = checkResultSuccess(weixinTradeAllEntity);
			if(!"0".equals(retMap.get("ret"))) return retMap;
			
			wemallOrder.setPrepayId(weixinTradeAllEntity.getPrepay_id());
			wemallOrder.setPaymentType(PaymentType.weixin.getValue());
        	
        	//更新预付款id和付款方式到订单中
			wemallOrderService.updatePrepayIdAndPayMethod(wemallOrder);
			
        	logger.info("为用户'" + UserUtils.getUser().getLoginName() + "'生成订单，订单号为：" + wemallOrder.getOrderNo());
        	retMap.put("ret", "0");
        	retMap.put("retMsg", "订单生成成功");
        	retMap.put("needPay", "1");
        	retMap.put("prepay_id", weixinTradeAllEntity.getPrepay_id());
        	retMap.put("payRequestParams", ParamsGenerator.generateParamsByPrepayId(weixinTradeAllEntity.getPrepay_id()));
		} else {
			retMap.put("ret", "-1");
        	retMap.put("retMsg", "微信验签失败。");
        	return retMap;
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
		
		//构造退款对象
		WemallRefund refund = new WemallRefund();
		refund.setOrderNo(orderNo);
		refund.setPlatformOrderNo(wemallOrder.getPlatformOrderNo());
		refund.setUser(wemallOrder.getUser());
		refund.setOrderPrice(wemallOrder.getOrderPrice());
		refund.setPayment(wemallOrder.getPayment());
		refund.setRefundFee(Integer.parseInt(refundFee));
		refund.setRefundDesc(refundDesc);
		refund.setRefundId(refundId);
		refund.setRefundStatus(1);  //退款状态（0--未成功，1--已退款）
		refund.setRefundDate(new Date());//退款时间
		refund.setIsNewRecord(true);
		
		//为调用微信接口申请退款构建参数map
		Map<String, String> paramsMap = ParamsGenerator.generateParamsForRefund(refund);
		
		//添加退款记录信息
		wemallRefundService.save(refund);
		//更新订单退款金额
		wemallOrderService.updateOrderRefundFee(wemallOrder, refundFee);
		
		//请求微信申请退款接口，获取返回数据
		String result = ApiclientCert.sendPostByCert(WeixinTradeConfig.REFUND_URL, ParamsGenerator.mapToXML(paramsMap));
		Map<String, String> weixinParamsMap = Maps.newHashMap();
		try {
			weixinParamsMap = XMLUtils.parseXml(result);
		} catch (Exception e) {
			logger.error("请求微信申请退款接口，返回数据解析出错。",e);
			retMap.put("ret", "-1");
        	retMap.put("retMsg", "请求微信申请退款接口，返回数据解析出错。");
        	throw new Exception("请求微信申请退款接口，返回数据解析出错。");
        	//return retMap;
		}
		boolean signVerified = checkSignature(weixinParamsMap);
		if(signVerified) {
			//验签成功
			WeixinTradeAllEntity weixinTradeAllEntity = BeanToMapUtils.toBean(WeixinTradeAllEntity.class, weixinParamsMap);
			
			retMap = checkResultSuccess(weixinTradeAllEntity);
			if(!"0".equals(retMap.get("ret"))) return retMap;
			
			System.err.println(result);
			
			//输出
			retMap.put("ret", "0");
			retMap.put("retMsg", "退款成功");
			return retMap;
		} else {
			retMap.put("ret", "-1");
			retMap.put("retMsg", "退款验签失败");
			throw new Exception("退款验签失败");
			//return retMap;
		}
		
	}
	
	/**
	 * 根据商户订单号或者微信订单号查询订单详情。商户订单号和微信订单号请二选一设置。都添加的话，将采用微信订单号。
	 * @param orderNo 商户订单号，商户网站订单系统中唯一订单号
	 * @param trade_no 微信订单号
	 * @return
	 */
	public String orderQuery(String orderNo, String trade_no) {
		
		//为调用微信接口查询订单构建参数map
		Map<String, String> paramsMap = ParamsGenerator.generateParamsForOrderQuery(orderNo, trade_no);
		
		//请求微信查询订单接口，获取返回数据
		String result = CallServletUtil.sendPost(WeixinTradeConfig.ORDER_QUERY_URL, ParamsGenerator.mapToXML(paramsMap), null);
		
		//输出
		return result;
	}
	
	/**
	 * 根据商户订单号或者微信订单号查询退款详情。商户订单号和微信订单号请二选一设置。都添加的话，将采用微信订单号。
	 * @param orderNo 商户订单号，商户网站订单系统中唯一订单号
	 * @param trade_no 微信订单号
	 * @param refundId 退款业务号
	 * @return
	 */
	public String refundQuery(String orderNo, String trade_no, String refundId) {
		
		//为调用微信接口查询退款构建参数map
		Map<String, String> paramsMap = ParamsGenerator.generateParamsForRefundQuery(orderNo, trade_no, refundId);
		
		//请求微信查询退款接口，获取返回数据
		String result = CallServletUtil.sendPost(WeixinTradeConfig.REFUND_QUERY_URL, ParamsGenerator.mapToXML(paramsMap), null);
		
		//输出
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
		String bill_type = WebUtils.getCleanParam(request, "bill_type");
		if(!"ALL".equals(bill_type) && !"SUCCESS".equals(bill_type) && !"REFUND".equals(bill_type) && !"RECHARGE_REFUND".equals(bill_type)) {
			retMap.put("ret", "-1");
			retMap.put("retMsg", "bill_type参数不能为空，且bill_type账单类型为：ALL、SUCCESS、REFUND、RECHARGE_REFUND；\n"
					+ "ALL，返回当日所有订单信息，默认值;SUCCESS，返回当日成功支付的订单;REFUND，返回当日退款订单;RECHARGE_REFUND，返回当日充值退款订单");
			return retMap;
		}
		String bill_date = WebUtils.getCleanParam(request, "bill_date");
		
		//为调用微信接口下载对账单构建参数map
		Map<String, String> paramsMap = ParamsGenerator.generateParamsForDownloadBill(bill_type, bill_date, false);
		
		//请求微信下载对账单接口，获取返回数据
		InputStream inputStream = CallServletUtil.sendPostForInputStream(WeixinTradeConfig.DOWNLOAD_BILL_URL, ParamsGenerator.mapToXML(paramsMap));
		try {
			// 循环取出流中的数据
			byte[] b = new byte[100];
			int len;
			while ((len = inputStream.read(b)) > 0)
			response.getOutputStream().write(b, 0, len);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
        	try {
        		if(inputStream != null) inputStream.close();
        		response.getOutputStream().flush();
        		response.getOutputStream().close();
			} catch (IOException e) {
				e.printStackTrace();
			}
        }
		retMap.put("ret", "0");
		retMap.put("retMsg", "下载成功");
		
		return retMap;
	}
	
	/**
	 * 微信验参，验签名
	 * @param request
	 * @return
	 */
	private boolean checkSignature(Map<String,String> params) {
		//获取微信返回的信息，进行验参
		boolean signVerified = false;
		//调用SDK验证签名
		if("MD5".equals(WeixinTradeConfig.sign_type)) {
			signVerified = WeixinTradeSignature.checkSignStrByMD5(WeixinTradeConfig.key, params);
		} else if("HMAC-SHA256".equals(WeixinTradeConfig.sign_type)) {
			signVerified = WeixinTradeSignature.checkSignStrByHMACSHA256(WeixinTradeConfig.key, params);
		}
		return signVerified;
	}
	
	/**
	 * 验证微信通知有效性的方法。并填充传入的订单对象为匹配到的系统订单。
	 * @param weixinTradeAllEntity
	 * @return 若为null，则验证未通过，若不为空，验证通过
	 */
	private WemallOrder checkNotifyVerified(WeixinTradeAllEntity weixinTradeAllEntity) {
		/*实际验证过程建议商户务必添加以下校验：
		1、需要验证该通知数据中的out_trade_no是否为商户系统中创建的订单号，
		2、判断total_fee是否确实为该订单的实际金额（即商户订单创建时的金额），
		3、校验通知中的seller_id（或者seller_email) 是否为out_trade_no这笔单据的对应的操作方（有的时候，一个商户可能有多个seller_id/seller_email）
		4、验证appid是否为该商户应用appid。*/
		
		//根据订单号查询订单，若没有，忽略通知
		String out_trade_no = weixinTradeAllEntity.getOut_trade_no(); //商户订单号
		WemallOrder wemallOrder = wemallOrderService.get(IdGen.getOrderNoByPlatformOrderNo(out_trade_no));
		if(wemallOrder == null) {
			logger.error("checkResultSuccess fail : wemallOrder=null");
			return null;
		}
		//若查到的订单对象中实际付款金额与total_fee不等，忽略通知
		String total_amount = weixinTradeAllEntity.getTotal_fee();
		//if(!total_amount.toString().equals(slSysOrder.getActualPayment())) return null;
		if(!total_amount.equals(wemallOrder.getOrderPrice().toString())) {
			logger.error("checkResultSuccess fail : OrderPrice not equals");
			return null;
		}
		//验证appid是否为该商户本身。
		if(!WeixinTradeConfig.appid.equals(weixinTradeAllEntity.getAppid())) {
			logger.error("checkResultSuccess fail : appid not equals");
			return null;
		}
		//校验通知中的seller_id（或者seller_email) 是否为out_trade_no这笔单据的对应的操作方（有的时候，一个商户可能有多个seller_id/seller_email
		
		return wemallOrder;
	}
	
	/**
	 * 统一验证微信返回结果是否成功SUCCESS
	 * @param weixinTradeAllEntity
	 * @return
	 */
	private Map<String, Object> checkResultSuccess(WeixinTradeAllEntity weixinTradeAllEntity) {
		Map<String, Object> retMap = Maps.newHashMap();
		if("SUCCESS".equals(weixinTradeAllEntity.getReturn_code())) {
        	if("SUCCESS".equals(weixinTradeAllEntity.getResult_code())) {
        		//返回成功
            	retMap.put("ret", "0");
            	retMap.put("retMsg", "结果成功");
        	} else {
        		retMap.put("ret", "60034");
	        	retMap.put("retMsg", "微信统一下单失败，失败原因：" + weixinTradeAllEntity.getErr_code_des());
        	}
        } else {
        	retMap.put("ret", "-1");
        	retMap.put("retMsg", "签名失败");
        }
		return retMap;
	}

}

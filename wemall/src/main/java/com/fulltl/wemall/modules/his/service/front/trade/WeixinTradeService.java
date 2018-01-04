package com.fulltl.wemall.modules.his.service.front.trade;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.web.util.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Maps;
import com.fulltl.wemall.common.service.BaseService;
import com.fulltl.wemall.common.utils.BeanToMapUtils;
import com.fulltl.wemall.common.utils.CallServletUtil;
import com.fulltl.wemall.common.utils.DateUtils;
import com.fulltl.wemall.common.utils.IdGen;
import com.fulltl.wemall.common.utils.RequestUtil;
import com.fulltl.wemall.common.utils.XMLUtils;
import com.fulltl.wemall.modules.sys.entity.SlSysOrder;
import com.fulltl.wemall.modules.sys.service.SlSysOrderService;
import com.fulltl.wemall.modules.sys.utils.UserUtils;
import com.fulltl.wemall.modules.wx.core.WeixinTradeConfig;
import com.fulltl.wemall.modules.wx.core.pojo.trade.WeixinTradeAllEntity;
import com.fulltl.wemall.modules.wx.core.utils.WeixinTradeSignature;


/**
 * 医院客服管理前端服务层
 * @author ldk
 *
 */
@Service
@Transactional(readOnly = true)
public class WeixinTradeService extends BaseService {
	@Autowired
	private SlSysOrderService slSysOrderService;
	
	/**
	 * 处理微信异步通知交易状态的方法
	 * @param weixinTradeAllEntity
	 * @param request
	 * @return
	 */
	@Transactional(readOnly = false)
	public String handleNotify(HttpServletRequest request) {
		Map<String, String> weixinParamsMap = Maps.newHashMap();
		try {
			weixinParamsMap = XMLUtils.parseXml(request);
		} catch (Exception e) {
			logger.error("请求微信统一下单接口，返回数据解析出错。",e);
        	return "fail";
		}
		boolean signVerified = checkSignature(weixinParamsMap);//微信验参，验签名
		if(signVerified) {
			//验签成功
			WeixinTradeAllEntity weixinTradeAllEntity = BeanToMapUtils.toBean(WeixinTradeAllEntity.class, weixinParamsMap);
			
			Map<String, Object> retMap = checkResultSuccess(weixinTradeAllEntity);
			if(!"0".equals(retMap.get("ret"))) return "fail";
			
			SlSysOrder slSysOrder = new SlSysOrder();
			//验证微信通知有效性的方法。并填充传入的订单对象。
			boolean checkNotifyVerified = checkNotifyVerified(weixinTradeAllEntity, slSysOrder);
			if(checkNotifyVerified) return "fail";
			
			// ↓↓↓↓↓↓↓↓↓↓所有验证通过,根据参数执行业务逻辑↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
			//商户订单号
			String out_trade_no = weixinTradeAllEntity.getOut_trade_no();
			//微信交易号
			String trade_no = weixinTradeAllEntity.getTransaction_id();
			//交易状态
			String trade_state = weixinTradeAllEntity.getTrade_state();
			
			if("CLOSED".equals(trade_state)) {
				//CLOSED—已关闭
				slSysOrder.setPayState("4");	//交易结束
				slSysOrder.setStatus("6");		//交易关闭
				slSysOrderService.save(slSysOrder);
			} else if ("SUCCESS".equals(trade_state)) {
				//SUCCESS—支付成功
				//是付款成功，更新订单状态和预约状态
				slSysOrderService.updateOrderAndUpdateRegStatus(slSysOrder);
			} else {
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
					BigDecimal refund_fee = weixinTradeAllEntity.getRefund_fee();
					
					//执行退款记录添加逻辑
				}
			}
			
			return "success";
		}else {//验证失败
			return "fail";
			//调试用，写文本函数记录程序运行情况是否正常
			//String sWord = weixinSignature.getSignCheckContentV1(params);
			//weixinConfig.logResult(sWord);
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
		SlSysOrder slSysOrder = null;
		String regId = WebUtils.getCleanParam(request, "regId"); //预约id
		String orderPrice = WebUtils.getCleanParam(request, "orderPrice"); //订单价格
		//构造订单对象
		Map<String, Object> resultMap = slSysOrderService.generateOrderBy(regId, orderPrice, "weixin");
		if(!"0".equals(resultMap.get("ret"))) return resultMap;
		else slSysOrder = (SlSysOrder)resultMap.get("slSysOrder");
		
		//为调用微信接口统一下单构建参数map
		Map<String, String> paramsMap = generateParamsForUnifiedOrder(slSysOrder, request);
		
		//请求微信统一下单接口，获取返回数据
		String result = CallServletUtil.sendPost(WeixinTradeConfig.UNIFIED_ORDER_URL, paramsMap);
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
			
			//微信统一下单成功，向数据库中插入新创建订单信息，并将对应预约中的订单字段更新为订单号
    		slSysOrderService.saveOrderAndUpdateReg(slSysOrder, regId);
        	logger.info("为用户'" + UserUtils.getUser().getLoginName() + "'生成订单，订单号为：" + slSysOrder.getOrderNo());
        	retMap.put("ret", "0");
        	retMap.put("retMsg", "订单生成成功");
        	retMap.put("prepay_id", weixinTradeAllEntity.getPrepay_id());
		}
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
	 * @param slSysOrder 可谓null
	 * @return
	 */
	private boolean checkNotifyVerified(WeixinTradeAllEntity weixinTradeAllEntity, SlSysOrder slSysOrder) {
		/*实际验证过程建议商户务必添加以下校验：
		1、需要验证该通知数据中的out_trade_no是否为商户系统中创建的订单号，
		2、判断total_fee是否确实为该订单的实际金额（即商户订单创建时的金额），
		3、校验通知中的seller_id（或者seller_email) 是否为out_trade_no这笔单据的对应的操作方（有的时候，一个商户可能有多个seller_id/seller_email）
		4、验证appid是否为该商户应用appid。*/
		
		//根据订单号查询订单，若没有，忽略通知
		String out_trade_no = weixinTradeAllEntity.getOut_trade_no(); //商户订单号
		slSysOrder = slSysOrderService.get(out_trade_no);
		if(slSysOrder == null) return false;
		//若查到的订单对象中实际付款金额与total_fee不等，忽略通知
		BigDecimal total_amount = weixinTradeAllEntity.getTotal_fee();
		if(!total_amount.toString().equals(slSysOrder.getActualPayment())) return false;
		//验证appid是否为该商户本身。
		if(!WeixinTradeConfig.appid.equals(weixinTradeAllEntity.getAppid())) return false;
		//校验通知中的seller_id（或者seller_email) 是否为out_trade_no这笔单据的对应的操作方（有的时候，一个商户可能有多个seller_id/seller_email
		
		return true;
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
	
	/**
	 * 为调用微信接口统一下单构建参数map
	 * @return
	 */
	private Map<String,String> generateParamsForUnifiedOrder(SlSysOrder slSysOrder, HttpServletRequest request) {
		String basePath = getBasePath(request);
		Map<String, String> paramsMap = Maps.newHashMap();
		paramsMap.put("appid", WeixinTradeConfig.appid);
		paramsMap.put("mch_id", WeixinTradeConfig.mch_id);
		paramsMap.put("nonce_str", IdGen.randomBase62(15));
		paramsMap.put("sign_type", WeixinTradeConfig.sign_type);
		paramsMap.put("body", slSysOrder.getDescription());
		paramsMap.put("out_trade_no", slSysOrder.getOrderNo());
		paramsMap.put("total_fee", new BigDecimal(slSysOrder.getActualPayment()).movePointRight(2).toString());//订单总金额，单位为分
		logger.debug("请求微信下单的用户ip为：" + RequestUtil.getIpAddress(request));
		logger.debug("请求微信下单的用户ip为：" + RequestUtil.getIP(request));
		paramsMap.put("spbill_create_ip", RequestUtil.getIpAddress(request));//用户端实际ip
		Date curDate = new Date();
		paramsMap.put("time_start", DateUtils.formatDate(curDate, "yyyyMMddHHmmss"));//订单生成时间，格式为yyyyMMddHHmmss
		try {
			paramsMap.put("time_expire", DateUtils.formatDate(DateUtils.addTimeByStr(curDate, WeixinTradeConfig.timeoutExpress), "yyyyMMddHHmmss"));
		} catch (Exception e) {
			logger.error("订单过期时间设定格式错误!", e);
		}
		//订单失效时间，格式为yyyyMMddHHmmss
		paramsMap.put("notify_url", basePath + WeixinTradeConfig.notify_url);//接收微信支付异步通知回调地址
		paramsMap.put("trade_type", "APP");//交易类型,支付类型
		
		//最后根据参数map生成签名
		if("MD5".equals(WeixinTradeConfig.sign_type)) {
			paramsMap.put("sign", WeixinTradeSignature.getSignStrByMD5(WeixinTradeConfig.key, paramsMap));
		} else if("HMAC-SHA256".equals(WeixinTradeConfig.sign_type)) {
			paramsMap.put("sign", WeixinTradeSignature.getSignStrByHMACSHA256(WeixinTradeConfig.key, paramsMap));
		}
		return paramsMap;
	}
}

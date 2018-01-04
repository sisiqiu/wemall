package com.fulltl.wemall.modules.alipay.core;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayDataDataserviceBillDownloadurlQueryRequest;
import com.alipay.api.request.AlipayTradeCloseRequest;
import com.alipay.api.request.AlipayTradeFastpayRefundQueryRequest;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.response.AlipayDataDataserviceBillDownloadurlQueryResponse;
import com.fulltl.wemall.common.web.BaseController;


/**
 * 支付宝前台控制器。包含支付宝支付接口。
 * 
 * @author ldk
 * @version 2017-10-27
 */
@Controller
@RequestMapping(value = "${frontPath}/alipay/core/trade")
public class AlipayCoreTradeController extends BaseController {

	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@RequestMapping(value = "")
	public String jumpToAlipayIndex(HttpServletRequest request, HttpServletResponse response, Model model) throws UnsupportedEncodingException, AlipayApiException {
		return "sanlen_website/alipay/alipayIndex";
	}
	
	/**
	 * 页面跳转同步通知页面路径，支付成功与否不以此为准，以异步通知为准。
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws AlipayApiException
	 */
	@RequestMapping(value = "returnUrl")
	@ResponseBody
	public String returnUrl(HttpServletRequest request, HttpServletResponse response, Model model) throws UnsupportedEncodingException, AlipayApiException {
		
		//获取支付宝GET过来反馈信息
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
		
		boolean signVerified = AlipaySignature.rsaCheckV1(params, AlipayConfig.alipay_public_key, AlipayConfig.charset, AlipayConfig.sign_type); //调用SDK验证签名

		//——请在这里编写您的程序（以下代码仅作参考）——
		if(signVerified) {
			//商户订单号
			String out_trade_no = WebUtils.getCleanParam(request, "out_trade_no");
		
			//支付宝交易号
			String trade_no = WebUtils.getCleanParam(request, "trade_no");
		
			//付款金额
			String total_amount = WebUtils.getCleanParam(request, "total_amount");
			
			return "支付宝交易号trade_no:"+trade_no+"\n"
					+ "商户订单号out_trade_no:"+out_trade_no+"\n"
					+ "付款金额total_amount:"+total_amount;
		}else {
			return "验签失败";
		}
	}
	
	/**
	 * 服务器异步通知页面路径，支付成功与否已此异步通知为准。
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws AlipayApiException
	 */
	@RequestMapping(value = "notifyUrl")
	@ResponseBody
	public String notifyUrl(HttpServletRequest request, HttpServletResponse response, Model model) throws UnsupportedEncodingException, AlipayApiException {
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
		boolean signVerified = AlipaySignature.rsaCheckV1(params, AlipayConfig.alipay_public_key, AlipayConfig.charset, AlipayConfig.sign_type); //调用SDK验证签名

		//——请在这里编写您的程序（以下代码仅作参考）——
		
		/* 实际验证过程建议商户务必添加以下校验：
		1、需要验证该通知数据中的out_trade_no是否为商户系统中创建的订单号，
		2、判断total_amount是否确实为该订单的实际金额（即商户订单创建时的金额），
		3、校验通知中的seller_id（或者seller_email) 是否为out_trade_no这笔单据的对应的操作方（有的时候，一个商户可能有多个seller_id/seller_email）
		4、验证app_id是否为该商户本身。
		*/
		if(signVerified) {//验证成功
			//商户订单号
			String out_trade_no = WebUtils.getCleanParam(request, "out_trade_no");
		
			//支付宝交易号
			String trade_no = WebUtils.getCleanParam(request, "trade_no");
		
			//交易状态
			String trade_status = WebUtils.getCleanParam(request, "trade_status");
			
			logger.debug(out_trade_no);
			logger.debug(trade_no);
			logger.debug(trade_status);
			logger.debug(params.toString());
			if(trade_status.equals("TRADE_FINISHED")){
				//判断该笔订单是否在商户网站中已经做过处理
				//如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
				//如果有做过处理，不执行商户的业务程序
					
				//注意：
				//退款日期超过可退款期限后（如三个月可退款），支付宝系统发送该交易状态通知
			}else if (trade_status.equals("TRADE_SUCCESS")){
				//判断该笔订单是否在商户网站中已经做过处理
				//如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
				//如果有做过处理，不执行商户的业务程序
				
				//注意：
				//付款完成后，支付宝系统发送该交易状态通知
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
	 * 付款
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws AlipayApiException
	 */
	@RequestMapping(value = "pay")
	public void pay(HttpServletRequest request, HttpServletResponse response, Model model) throws UnsupportedEncodingException, AlipayApiException {
		//获得初始化的AlipayClient
		AlipayClient alipayClient = AlipayConfig.getAlipayClient();
		
		String basePath = request.getScheme() + "://" + request.getServerName() + request.getContextPath();
		//设置请求参数
		AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();
		alipayRequest.setReturnUrl(basePath + AlipayConfig.return_url);
		alipayRequest.setNotifyUrl(basePath + AlipayConfig.notify_url);
		logger.debug(basePath + AlipayConfig.notify_url);
		//商户订单号，商户网站订单系统中唯一订单号，必填
		String out_trade_no = WebUtils.getCleanParam(request, "WIDout_trade_no");
		
		//付款金额，必填
		String total_amount = WebUtils.getCleanParam(request, "WIDtotal_amount");
		//订单名称，必填
		String subject = WebUtils.getCleanParam(request, "WIDsubject");
		//商品描述，可空
		String body = WebUtils.getCleanParam(request, "WIDbody");
		alipayRequest.setBizContent("{\"out_trade_no\":\""+ out_trade_no +"\"," 
				+ "\"total_amount\":\""+ total_amount +"\"," 
				+ "\"subject\":\""+ subject +"\"," 
				+ "\"body\":\""+ body +"\"," 
				+ "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"}");
		
		//若想给BizContent增加其他可选请求参数，以增加自定义超时时间参数timeout_express来举例说明
		//alipayRequest.setBizContent("{\"out_trade_no\":\""+ out_trade_no +"\"," 
		//		+ "\"total_amount\":\""+ total_amount +"\"," 
		//		+ "\"subject\":\""+ subject +"\"," 
		//		+ "\"body\":\""+ body +"\"," 
		//		+ "\"timeout_express\":\"10m\"," 
		//		+ "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"}");
		//请求参数可查阅【电脑网站支付的API文档-alipay.trade.page.pay-请求参数】章节
		
		
		String form="";
	    try {
	        form = alipayClient.pageExecute(alipayRequest).getBody(); //调用SDK生成表单
	    } catch (AlipayApiException e) {
	        e.printStackTrace();
	    }
	    response.setContentType("text/html;charset=" + AlipayConfig.charset);
	    try {
			response.getWriter().write(form);//直接将完整的表单html输出到页面
			response.getWriter().flush();
			response.getWriter().close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 交易查询
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws AlipayApiException
	 */
	@RequestMapping(value = "query")
	@ResponseBody
	public String query(HttpServletRequest request, HttpServletResponse response, Model model) throws UnsupportedEncodingException, AlipayApiException {
		//获得初始化的AlipayClient
		AlipayClient alipayClient = AlipayConfig.getAlipayClient();
		
		//设置请求参数
		AlipayTradeQueryRequest alipayRequest = new AlipayTradeQueryRequest();
		
		//商户订单号，商户网站订单系统中唯一订单号
		String out_trade_no = WebUtils.getCleanParam(request, "WIDTQout_trade_no");
		//支付宝交易号
		String trade_no = WebUtils.getCleanParam(request, "WIDTQtrade_no");
		//请二选一设置
		String bizContent = StringUtils.EMPTY;
		if(StringUtils.isNotBlank(trade_no)) {
			bizContent = "{\"out_trade_no\":\""+ out_trade_no +"\","
						+"\"trade_no\":\""+ trade_no +"\"}";
		} else {
			bizContent = "{\"out_trade_no\":\""+ out_trade_no +"\"}";
		}
		alipayRequest.setBizContent(bizContent);
		
		//请求
		String result = alipayClient.execute(alipayRequest).getBody();
		
		//输出
		return result;
	}
	
	/**
	 * 退款
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws AlipayApiException
	 */
	@RequestMapping(value = "refund")
	@ResponseBody
	public String refund(HttpServletRequest request, HttpServletResponse response, Model model) throws UnsupportedEncodingException, AlipayApiException {
		//获得初始化的AlipayClient
		AlipayClient alipayClient = AlipayConfig.getAlipayClient();
		
		//设置请求参数
		AlipayTradeRefundRequest alipayRequest = new AlipayTradeRefundRequest();
		
		//商户订单号，商户网站订单系统中唯一订单号
		String out_trade_no = WebUtils.getCleanParam(request, "WIDTRout_trade_no");
		//支付宝交易号
		String trade_no = WebUtils.getCleanParam(request, "WIDTRtrade_no");
		//请二选一设置
		//需要退款的金额，该金额不能大于订单金额，必填
		String refund_amount = WebUtils.getCleanParam(request, "WIDTRrefund_amount");
		//退款的原因说明
		String refund_reason = WebUtils.getCleanParam(request, "WIDTRrefund_reason");
		//标识一次退款请求，同一笔交易多次退款需要保证唯一，如需部分退款，则此参数必传
		String out_request_no = WebUtils.getCleanParam(request, "WIDTRout_request_no");
		
		String bizContent = StringUtils.EMPTY;
		if(StringUtils.isNotBlank(trade_no)) {
			bizContent = "{\"out_trade_no\":\""+ out_trade_no +"\"," 
					+ "\"trade_no\":\""+ trade_no +"\"," 
					+ "\"refund_amount\":\""+ refund_amount +"\"," 
					+ "\"refund_reason\":\""+ refund_reason +"\"," 
					+ "\"out_request_no\":\""+ out_request_no +"\"}";
		} else {
			bizContent = "{\"out_trade_no\":\""+ out_trade_no +"\"," 
					+ "\"refund_amount\":\""+ refund_amount +"\"," 
					+ "\"refund_reason\":\""+ refund_reason +"\"," 
					+ "\"out_request_no\":\""+ out_request_no +"\"}";
		}
		alipayRequest.setBizContent(bizContent);
		
		//请求
		String result = alipayClient.execute(alipayRequest).getBody();
		
		//输出
		return result;
	}
	
	/**
	 * 退款查询
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws AlipayApiException
	 */
	@RequestMapping(value = "refundQuery")
	@ResponseBody
	public String refundQuery(HttpServletRequest request, HttpServletResponse response, Model model) throws UnsupportedEncodingException, AlipayApiException {
		//获得初始化的AlipayClient
		AlipayClient alipayClient = AlipayConfig.getAlipayClient();
		
		//设置请求参数
		AlipayTradeFastpayRefundQueryRequest alipayRequest = new AlipayTradeFastpayRefundQueryRequest();
		
		//商户订单号，商户网站订单系统中唯一订单号
		String out_trade_no = WebUtils.getCleanParam(request, "WIDRQout_trade_no");
		//支付宝交易号
		String trade_no = WebUtils.getCleanParam(request, "WIDRQtrade_no");
		//请二选一设置
		//请求退款接口时，传入的退款请求号，如果在退款请求时未传入，则该值为创建交易时的外部交易号，必填
		String out_request_no = WebUtils.getCleanParam(request, "WIDRQout_request_no");
		
		String bizContent = StringUtils.EMPTY;
		if(StringUtils.isNotBlank(trade_no)) {
			bizContent = "{\"out_trade_no\":\""+ out_trade_no +"\"," 
					+"\"trade_no\":\""+ trade_no +"\","
					+"\"out_request_no\":\""+ out_request_no +"\"}";
		} else {
			bizContent = "{\"out_trade_no\":\""+ out_trade_no +"\"," 
					+"\"out_request_no\":\""+ out_request_no +"\"}";
		}
		alipayRequest.setBizContent(bizContent);
		
		//请求
		String result = alipayClient.execute(alipayRequest).getBody();
		
		//输出
		return result;
	}
	
	/**
	 * 交易关闭
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws AlipayApiException
	 */
	@RequestMapping(value = "close")
	@ResponseBody
	public String close(HttpServletRequest request, HttpServletResponse response, Model model) throws UnsupportedEncodingException, AlipayApiException {
		//获得初始化的AlipayClient
		AlipayClient alipayClient = AlipayConfig.getAlipayClient();
		
		//设置请求参数
		AlipayTradeCloseRequest alipayRequest = new AlipayTradeCloseRequest();
		//商户订单号，商户网站订单系统中唯一订单号
		String out_trade_no = WebUtils.getCleanParam(request, "WIDTCout_trade_no");
		//支付宝交易号
		String trade_no = WebUtils.getCleanParam(request, "WIDTCtrade_no");
		//请二选一设置
		
		String bizContent = StringUtils.EMPTY;
		if(StringUtils.isNotBlank(trade_no)) {
			bizContent = "{\"out_trade_no\":\""+ out_trade_no +"\","
						+"\"trade_no\":\""+ trade_no +"\"}";
		} else {
			bizContent = "{\"out_trade_no\":\""+ out_trade_no +"\"}";
		}
		alipayRequest.setBizContent(bizContent);
		
		//请求
		String result = alipayClient.execute(alipayRequest).getBody();
		
		//输出
		return result;
	}
	
	@RequestMapping(value = "downloadBill")
	@ResponseBody
	public String downloadBill(HttpServletRequest request, HttpServletResponse response, Model model) throws UnsupportedEncodingException, AlipayApiException {
		String bill_type = WebUtils.getCleanParam(request, "bill_type");
		if(!"trade".equals(bill_type) && !"signcustomer".equals(bill_type)) {
			return "bill_type参数不能为空，且bill_type账单类型为：trade、signcustomer；\n"
					+ "trade指商户基于支付宝交易收单的业务账单；signcustomer是指基于商户支付宝余额收入及支出等资金变动的帐务账单";
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
			System.out.println(billDownloadUrl);
			System.out.println("调用成功");
			try {
				response.sendRedirect(billDownloadUrl);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return billDownloadUrl;
		} else {
			System.out.println("调用失败");
			return "调用失败, 错误码：" + result.getCode() + "; 错误信息：" + result.getSubMsg();
		}
	}
}
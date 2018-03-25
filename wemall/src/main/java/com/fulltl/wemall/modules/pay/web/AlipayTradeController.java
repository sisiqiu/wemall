package com.fulltl.wemall.modules.pay.web;

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
import org.springframework.beans.factory.annotation.Autowired;
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
import com.fulltl.wemall.modules.alipay.core.AlipayConfig;
import com.fulltl.wemall.modules.alipay.core.pojo.AlipayTradeAllEntity;
import com.fulltl.wemall.modules.pay.service.AlipayTradeService;
import com.google.gson.Gson;


/**
 * 支付宝前台控制器。包含支付宝支付接口。
 * 
 * @author ldk
 * @version 2017-10-27
 */
@Controller
@RequestMapping(value = "${frontPath}/interface/his/alipay/core/trade")
public class AlipayTradeController extends BaseController {

	private Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	private AlipayTradeService alipayTradeService;
	
	@RequestMapping(value = "")
	public String jumpToAlipayIndex(HttpServletRequest request, HttpServletResponse response, Model model) throws UnsupportedEncodingException, AlipayApiException {
		return "sanlen_website/alipay/alipayIndex";
	}
	
	/**
	 * 服务器异步通知页面路径，支付成功与否已此异步通知为准。此接口不需要进行签名过滤器进行验证。
	 * url：http://ldkadmin.viphk.ngrok.org/f/interface/his/alipay/core/trade/notifyUrl
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws AlipayApiException
	 */
	@RequestMapping(value = "notifyUrl")
	@ResponseBody
	public String notifyUrl(AlipayTradeAllEntity alipayTradeAllEntity, HttpServletRequest request, HttpServletResponse response, Model model) {
		return alipayTradeService.handleNotify(alipayTradeAllEntity, request);
	}
	
	/**
	 * 用于app端调用生成订单的接口
	 * url：http://ldkadmin.viphk.ngrok.org/f/interface/his/alipay/core/trade/generateOrder
	 * 参数：orderPrice（*）;		// 订单价格，单位为元，精确到小数点后两位
	 *		id（*）;		//预约id
	 *
	 *	例：{
	 *		orderPrice=88.88
	 * 		}
	 * 
	 * 结果示例：{
			    "ret": "0",
			    "access_token": "",
			    "data": "alipay_sdk=alipay-sdk-java-dynamicVersionNo&app_id=2016083100374619&biz_content=%7B%22out_trade_no%22%3A%22f54cf7ec63d04d298aa632716b58f7b5%22%2C%22product_code%22%3A%22QUICK_MSECURITY_PAY%22%2C%22subject%22%3A%22iphone+X%%2C%22timeout_express%22%32230m%22%2C%22total_amount%22%3A%22888.88%22%7D&charset=UTF-8&format=json&method=alipay.trade.app.pay&notify_url=http%3A%2F%2Flocalhost%2Ff%2Falipay%2Fcore%2Ftrade%2FnotifyUrl&sign=MGUOfbyBVVUwbZa5OLI1ZkRpU9hglbtUSoC6ZZimek4swyEHsYp%2BK6RFw1J%2BybT4rqTe6NTZj8CxwweSeUAa2LFLiQzbyH9odbonq7hEldRov31sDolMNDQwMeGWaLBE4TTLnDydtyCwhDkGjT5aOIRklvLFO3j7LfPoUeeYsbQ5Ac9uTmklecWj50EorVDZuwI9tcsX3FIx3i8N5R4cYiYVZDbbi%2B5HZwY%2BOPZZVXVVNt19%2Ftd9RHQV9zPc9Qr78f3XrEo4WEkxIEuwMQFtPrAKd7lrNsgavzEDoWhrnZ435fGeOWbisQoE5TGJtKY9f6XCxURXTw%3D%3D&sign_type=RSA2&timestamp=2017-12-12+15%3A19%3A04&version=1.0",
			    "retMsg": "订单生成成功",
			    "sid": "99543e048c0f44f8a6a6fc511c14102c"
			}
	 * 		或
	 * 		{"ret":"60015","data":{},"retMsg":"用户尚未登陆！请先登录！"}
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws AlipayApiException
	 */
	/*@RequestMapping(value = "generateOrder")
	@ResponseBody
	public String generateOrder(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> retMap=alipayTradeService.generateOrderByReg(request);
 		return new Gson().toJson(formatReturnMsg(retMap));
	}*/
	
	//////////////////////////////////////////////////////////////////////////////////////////
	//----------------------------------------分割线------------------------------------------//
	//////////////////////////////////////////////////////////////////////////////////////////
	
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
		
		String basePath = getBasePath(request);
		//设置请求参数
		AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();
		alipayRequest.setReturnUrl(basePath + AlipayConfig.return_url);
		alipayRequest.setNotifyUrl(basePath + AlipayConfig.notify_url);
		
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
package com.fulltl.wemall.modules.his.web.front.trade;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fulltl.wemall.common.web.BaseController;
import com.fulltl.wemall.modules.his.service.front.trade.WeixinTradeService;
import com.google.gson.Gson;


/**
 * 微信前台控制器。包含微信支付接口。
 * 
 * @author ldk
 * @version 2017-10-27
 */
@Controller
@RequestMapping(value = "${frontPath}/interface/his/weixin/core/trade")
public class WeixinTradeController extends BaseController {

	private Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	private WeixinTradeService weixinTradeService;
	
	/**
	 * 服务器异步通知页面路径，支付成功与否已此异步通知为准。此接口不需要进行签名过滤器进行验证。
	 * url：http://ldkadmin.viphk.ngrok.org/f/interface/his/weixin/core/trade/notifyUrl
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "notifyUrl")
	@ResponseBody
	public String notifyUrl(HttpServletRequest request, HttpServletResponse response, Model model) {
		return weixinTradeService.handleNotify(request);
	}
	
	/**
	 * 用于app端调用生成订单的接口
	 * url：http://ldkadmin.viphk.ngrok.org/f/interface/his/weixin/core/trade/generateOrder
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
			    "data": "",
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
	 */
	@RequestMapping(value = "generateOrder")
	@ResponseBody
	public String generateOrder(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> retMap=weixinTradeService.generateOrderByReg(request);
 		return new Gson().toJson(formatReturnMsg(retMap));
	}
	
}
package com.fulltl.wemall.modules.wemall.schedule;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fulltl.wemall.common.utils.BeanToMapUtils;
import com.fulltl.wemall.common.utils.XMLUtils;
import com.fulltl.wemall.modules.pay.service.WeixinTradeService;
import com.fulltl.wemall.modules.wemall.entity.WemallOrder;
import com.fulltl.wemall.modules.wemall.entity.WemallOrder.OrderStatus;
import com.fulltl.wemall.modules.wemall.service.WemallOrderService;
import com.fulltl.wemall.modules.wx.core.pojo.trade.WeixinTradeAllEntity;
import com.google.common.collect.Maps;

/**
 * 定时任务
 * @author ldk
 *
 */
@Service
@Lazy(false)
@Transactional(readOnly=false)
public class WemallOrderScheduler {
	private Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	private WemallOrderService wemallOrderService;
	@Autowired
	private WeixinTradeService weixinTradeService;
	
	/**
	 * 获取accessToken和jsapi_ticket的操作，并将在初始化构造时和每个整点执行。
	 */
	@PostConstruct
	@Scheduled(cron="0 0/5 * * * ?")
	public void updateOrderStatus() {
		System.err.println("执行定时任务updateOrderStatus"+new Date());
		WemallOrder query = new WemallOrder();
		query.setStatus(OrderStatus.unPaid.getValue());
		List<WemallOrder> list = wemallOrderService.findUnPaidOrderList(query);
		
		for(WemallOrder wemallOrder : list) {
			//循环判断订单是否确认付款
			String result = weixinTradeService.orderQuery(wemallOrder.getPlatformOrderNo(), null);
			Map<String, String> weixinParamsMap = Maps.newHashMap();
			try {
				weixinParamsMap = XMLUtils.parseXml(result);
			} catch (Exception e) {
				logger.error("判断订单是否确认付款出错",e);
			}
			WeixinTradeAllEntity weixinTradeAllEntity = BeanToMapUtils.toBean(WeixinTradeAllEntity.class,weixinParamsMap);;
			if("SUCCESS".equals(weixinTradeAllEntity.getReturn_code()) &&
					"SUCCESS".equals(weixinTradeAllEntity.getResult_code()) &&
					"SUCCESS".equals(weixinTradeAllEntity.getTrade_state())
					) {
				//判断已付款，更新订单状态和预约状态
				wemallOrder.setPayment(Integer.parseInt(weixinTradeAllEntity.getTotal_fee()));
				wemallOrderService.updateAllStatusByOrderNo(wemallOrder, OrderStatus.alreadyPaid.getValue());
			}
		}
	}
	
	public void updateSingleOrderStatus(WemallOrder wemallOrder) {
		//循环判断订单是否确认付款
		String result = weixinTradeService.orderQuery(wemallOrder.getPlatformOrderNo(), null);
		Map<String, String> weixinParamsMap = Maps.newHashMap();
		try {
			weixinParamsMap = XMLUtils.parseXml(result);
		} catch (Exception e) {
			logger.error("判断订单是否确认付款出错",e);
		}
		WeixinTradeAllEntity weixinTradeAllEntity = BeanToMapUtils.toBean(WeixinTradeAllEntity.class,weixinParamsMap);;
		if("SUCCESS".equals(weixinTradeAllEntity.getReturn_code()) &&
				"SUCCESS".equals(weixinTradeAllEntity.getResult_code()) &&
				"SUCCESS".equals(weixinTradeAllEntity.getTrade_state())
				) {
			//判断已付款，更新订单状态和预约状态
			wemallOrder.setPayment(Integer.parseInt(weixinTradeAllEntity.getTotal_fee()));
			wemallOrderService.updateAllStatusByOrderNo(wemallOrder, OrderStatus.alreadyPaid.getValue());
		}
	}
}

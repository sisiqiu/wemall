package com.fulltl.wemall.modules.wemall.schedule;

import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.fulltl.wemall.modules.pay.service.WeixinTradeService;
import com.fulltl.wemall.modules.wemall.entity.WemallOrder;
import com.fulltl.wemall.modules.wemall.entity.WemallOrder.OrderStatus;
import com.fulltl.wemall.modules.wemall.service.WemallOrderService;

/**
 * 定时任务
 * @author ldk
 *
 */
@Service
@Lazy(false)
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
	@Scheduled(cron="0 0/5 0 * * ?")
	public void updateOrderStatus() {
		System.err.println("执行定时任务updateOrderStatus"+new Date());
		WemallOrder query = new WemallOrder();
		query.setStatus(OrderStatus.unPaid.getValue());
		List<WemallOrder> list = wemallOrderService.findUnPaidOrderList(query);
		
		/*for(WemallOrder wemallOrder : list) {
			//循环判断订单是否确认付款
			String result = weixinTradeService.orderQuery(wemallOrder.getPlatformOrderNo(), null);
			if(true) {
				//判断已付款，更新订单状态和预约状态
				wemallOrderService.updateAllStatusByOrderNo(wemallOrder, OrderStatus.alreadyPaid.getValue());
			}
		}*/
		
	}
}

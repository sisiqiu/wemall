/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.fulltl.wemall.modules.wemall.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fulltl.wemall.common.persistence.Page;
import com.fulltl.wemall.common.service.CrudService;
import com.fulltl.wemall.common.utils.IdGen;
import com.fulltl.wemall.modules.sys.entity.OrderDict;
import com.fulltl.wemall.modules.sys.entity.User;
import com.fulltl.wemall.modules.sys.service.SystemService;
import com.fulltl.wemall.modules.sys.utils.OrderDictUtils;
import com.fulltl.wemall.modules.sys.utils.UserUtils;
import com.fulltl.wemall.modules.wemall.dao.WemallOrderDao;
import com.fulltl.wemall.modules.wemall.entity.WemallItemActivity;
import com.fulltl.wemall.modules.wemall.entity.WemallOrder;
import com.fulltl.wemall.modules.wemall.entity.WemallOrder.OrderStatus;
import com.fulltl.wemall.modules.wemall.entity.WemallOrderAddress;
import com.fulltl.wemall.modules.wemall.entity.WemallOrderItem;
import com.fulltl.wemall.modules.wemall.entity.WemallBountyInfo.BountyFromType;
import com.fulltl.wemall.modules.wemall.entity.WemallScoreInfo.ScoreFromType;
import com.google.common.collect.Lists;

/**
 * 订单管理Service
 * @author ldk
 * @version 2018-01-05
 */
@Service
@Transactional(readOnly = true)
public class WemallOrderService extends CrudService<WemallOrderDao, WemallOrder> {
	@Autowired 
	private SystemService systemService;
	@Autowired 
	private WemallOrderItemService wemallOrderItemService;
	@Autowired 
	private WemallShopCarService wemallShopCarService;
	@Autowired 
	private WemallItemService wemallItemService;
	@Autowired 
	private WemallOrderAddressService wemallOrderAddressService;
	@Autowired 
	private WemallScoreInfoService wemallScoreInfoService;
	@Autowired 
	private WemallBountyInfoService wemallBountyInfoService;
	@Autowired 
	private WemallItemActivityService wemallItemActivityService;
	
	public WemallOrder get(String id) {
		return super.get(id);
	}
	
	public List<WemallOrder> findList(WemallOrder wemallOrder) {
		return super.findList(wemallOrder);
	}
	
	public Page<WemallOrder> findPage(Page<WemallOrder> page, WemallOrder wemallOrder) {
		return super.findPage(page, wemallOrder);
	}
	
	@Transactional(readOnly = false)
	public void save(WemallOrder wemallOrder) {
		if(StringUtils.isBlank(wemallOrder.getOrderNo())) {
			wemallOrder.setOrderNo(IdGen.uuid());
		}
		super.save(wemallOrder);
	}
	
	@Transactional(readOnly = false)
	public void delete(WemallOrder wemallOrder) {
		super.delete(wemallOrder);
	}
	
	/**
	 * 根据订单标题和价格生成订单对象的接口
	 * @param title 订单标题
	 * @param orderPrice 订单价格
	 * @param paymentType 付款方式；0--微信支付;1--支付宝;2--货到付款
	 * @return key值为wemallOrder的value为订单对象
	 */
	@Transactional(readOnly = false)
	public Map<String, Object> generateOrderBy(String title, Integer orderPrice, Integer freightPrice, Integer paymentType) {
		Map<String, Object> retMap = new HashMap<String, Object>();
		
		//根据护理预约信息中的用户，验证是否是当前用户
		User user = UserUtils.getUser();
		retMap = systemService.checkCurrentUser(user);
		if(!"0".equals(retMap.get("ret"))) return retMap;

		//构造订单对象
		WemallOrder wemallOrder = new WemallOrder();
		wemallOrder.init();
        //设置其他字段值
		wemallOrder.setTitle(title);
        //slSysOrder.setDescription(subject);
		wemallOrder.setOriginalOrderPrice(orderPrice);	//原始订单价格
		wemallOrder.setOrderPrice(orderPrice);	//订单价格
		wemallOrder.setFreightPrice(freightPrice);	//总运费
        //slSysOrder.setActualPayment(orderPrice);	//实际支付价格
		wemallOrder.setPayment(0);	//实际支付价格
		//wemallOrder.setMobile(user.getMobile());
		wemallOrder.setUser(user);
		wemallOrder.setPaymentType(paymentType);
        
        //统一验证参数是否合法
        retMap = beanValidator(wemallOrder);
        if(!"0".equals(retMap.get("ret"))) return retMap;
		
        retMap.put("ret", "0");
		retMap.put("retMsg", "构造成功！");
		retMap.put("wemallOrder", wemallOrder);
		return retMap;
	}
	
	/**
	 * 护理预约：根据订单对象，保存订单对象
	 * @param slSysOrder
	 */
	@Transactional(readOnly = false)
	public void saveOrder(WemallOrder wemallOrder) {
		wemallOrder.setIsNewRecord(true);	//标记为插入
		this.save(wemallOrder);
	}
	
	/**
	 * 预约：更新订单对象为付款成功，并更新护理预约为。1--未完成，未接单
	 * @param slSysOrder
	 */
	@Transactional(readOnly = false)
	public void updateOrderAndUpdateCareAppoStatus(WemallOrder wemallOrder) {
		/*slSysOrder.setPayState("3");	//支付成功
		slSysOrder.setStatus("2");		//已付款
		this.save(slSysOrder);
		
		SlHisCareappo slHisCareappo = new SlHisCareappo();
		slHisCareappo.setOrderNo(slSysOrder.getOrderNo());
		slHisCareappo.setStatus("1");
		slHisCareappo.setPayTime(new Date());
		slHisCareappoService.updateStatusByOrderNo(slHisCareappo);
		
		//向mongodb中存储护理预约信息
		SlHisCareappo query = new SlHisCareappo();
		query.setOrderNo(slSysOrder.getOrderNo());
		query.setStatus("1");
		List<SlHisCareappo> findList = slHisCareappoService.findList(query);
		if(findList != null && findList.size() > 0) {
			//向mongodb中存储护理预约信息，此步骤最后应放在付款成功之后
			careAppoAddrDao.insertBySlHisCareAppo(findList.get(0));
			//推送预约数据到附近3公里范围内的未接单护工端，此步骤放在消息队列中执行
			pushCareAppoInfoProducer.sendDataToQueue(findList.get(0));
		}*/
	}

	/**
	 * 更新订单退款金额
	 * @param slSysOrder
	 * @param refundFee
	 */
	@Transactional(readOnly = false)
	public void updateOrderRefundFee(WemallOrder wemallOrder, String refundFee) {
		//更新退款
		wemallOrder.setTotalRefundFee(new BigDecimal(wemallOrder.getTotalRefundFee()).add(new BigDecimal(refundFee)).intValue());
		wemallOrder.setStatus(OrderStatus.alreadyRejected.getValue());
		dao.updateTotalRefundFee(wemallOrder);
		
		//若是全额退款，更新用户的累计订单数及累计消费额（撤回）
		if(wemallOrder.getPayment().toString().equals(wemallOrder.getTotalRefundFee())
			// && !OrderStatus.alreadyCancelled_alreadyPaid.getValue().equals(wemallOrder.getStatus())
				) {
			User user = UserUtils.get(wemallOrder.getUser().getId());
			user.setTotalOrderNum((user.getTotalOrderNum()==null?0:user.getTotalOrderNum())-1);
			user.setTotalConsumeNum(user.getTotalConsumeNum() - wemallOrder.getPayment());
			systemService.updateUserInfo(user);
			
			//将该订单消耗的积分退回给用户
			if(wemallOrder.getScoreUsageNum() != null && wemallOrder.getScoreUsageNum() != 0) {
				wemallScoreInfoService.updateUserScore(wemallOrder.getUser().getId(), wemallOrder.getScoreUsageNum(), ScoreFromType.rollback);
			}
			//将该订单消耗的奖励金退回给用户
			if(wemallOrder.getBountyUsageNum() != null && wemallOrder.getBountyUsageNum() != 0) {
				wemallBountyInfoService.updateUserBounty(wemallOrder.getUser().getId(), wemallOrder.getBountyUsageNum(), BountyFromType.rollback);
			}
		}
	}

	/**
	 * 更新预付款id和付款方式
	 * @param slSysOrder
	 */
	@Transactional(readOnly = false)
	public void updatePrepayIdAndPayMethod(WemallOrder wemallOrder) {
		wemallOrder.preUpdate();
		dao.updatePrepayIdAndPayMethod(wemallOrder);
	}

	/**
	 * 根据订单号，更新订单状态
	 * @param orderNo
	 * @param status
	 */
	@Transactional(readOnly = false)
	public void updateStatusByOrderNo(WemallOrder wemallOrder, Integer status) {
		wemallOrder.setStatus(status);
		dao.updateStatusByOrderNo(wemallOrder);
	}
	
	/**
	 * 根据订单号，更新订单状态
	 * @param orderNo
	 * @param status
	 */
	@Transactional(readOnly = false)
	public void updateAllStatusByOrderNo(WemallOrder wemallOrder, Integer status) {
		if(OrderStatus.alreadyPaid.getValue().equals(status)) {
			wemallOrder.setPaymentDate(new Date());
			//付款成功
			if(wemallOrder.getOrderCategory().equals("1")) {
				//商品订单
				if(StringUtils.isNotBlank(wemallOrder.getShopCarIds())) {
					//删除对应的购物车项
					List<String> shopCarIdList = Arrays.asList(wemallOrder.getShopCarIds().split(","));
					wemallShopCarService.delete(shopCarIdList);
				}
				//添加商品销量
				wemallItemService.increaseSalesNum(wemallOrder.getOrderNo());
				//对用户的积分执行扣除操作
				if(wemallOrder.getScoreUsageNum() != null && wemallOrder.getScoreUsageNum() != 0) {
					wemallScoreInfoService.updateUserScore(wemallOrder.getUser().getId(), wemallOrder.getScoreUsageNum()*(-1), ScoreFromType.itemScoreDeduction);
				}
				//对用户的奖励金执行扣除操作
				if(wemallOrder.getBountyUsageNum() != null && wemallOrder.getBountyUsageNum() != 0) {
					wemallBountyInfoService.updateUserBounty(wemallOrder.getUser().getId(), wemallOrder.getBountyUsageNum()*(-1), BountyFromType.buyItems);
				}
				//更新用户的累计订单数及累计消费额
				User user = UserUtils.get(wemallOrder.getUser().getId());
				user.setTotalOrderNum((user.getTotalOrderNum()==null?0:user.getTotalOrderNum())+1);
				user.setTotalConsumeNum(user.getTotalConsumeNum() + wemallOrder.getPayment());
				systemService.updateUserInfo(user);
			} else if(wemallOrder.getOrderCategory().equals("2")) {
				//充值订单
				//对用户的奖励金执行添加操作
				wemallBountyInfoService.updateUserBounty(wemallOrder.getUser().getId(), wemallOrder.getOrderPrice(), BountyFromType.recharge);
			}
		}
		if(OrderStatus.alreadyCancelled_alreadyPaid.getValue().equals(status)) {
			//订单已付款，已取消
			//释放库存
			wemallItemService.releaseStorage(wemallOrder.getOrderNo());
			//将该订单消耗的积分退回给用户
			/*if(wemallOrder.getScoreUsageNum() != null && wemallOrder.getScoreUsageNum() != 0) {
				wemallScoreInfoService.updateUserScore(wemallOrder.getUser().getId(), wemallOrder.getScoreUsageNum(), ScoreFromType.rollback);
			}*/
		}
		if(OrderStatus.alreadyReceived.getValue().equals(status)) {
			//订单已收货
			//根据订单实付金额 及 购买商品获取积分比例，为该用户增加相关积分
			Integer payment = wemallOrder.getPayment();
			OrderDict orderDict = OrderDictUtils.getOrderDictByTypeAndValue("score_about_set", "2");
			if(orderDict != null) {
				int intValue = new BigDecimal(orderDict.getPrice()).multiply(new BigDecimal(payment)).divide(new BigDecimal(100), 0, RoundingMode.FLOOR).intValue();
				wemallScoreInfoService.updateUserScore(wemallOrder.getUser().getId(), intValue, ScoreFromType.buyItems);
			}
		}
		if(OrderStatus.alreadyClosed.getValue().equals(status)) {
			//订单关闭
			if(wemallOrder.getPaymentDate() != null) {
				//判断是过期未付款订单，则根据第三方订单号匹配订单，释放库存，并将对应第三方订单号删除
				WemallOrder query = new WemallOrder();
				query.setPlatformOrderNo(wemallOrder.getPlatformOrderNo());
				List<WemallOrder> list = this.findList(query);
				if(list.size() > 0) {
					WemallOrder timeoutOrder = list.get(0);
					timeoutOrder.setPlatformOrderNo(null);
					//释放库存
					wemallItemService.releaseStorage(timeoutOrder.getOrderNo());
					//将对应第三方订单号删除
					dao.updatePlatformOrderNo(timeoutOrder);
				}
			}
		}
		
		this.updateStatusByOrderNo(wemallOrder, status);
		wemallOrderItemService.updateStatusByOrderNo(wemallOrder.getOrderNo(), status);
	}

	/**
	 * 获取定单详情
	 * @param orderNo
	 * @return
	 */
	public Map<String, Object> getOrderDetail(String orderNo, boolean putActivityAndScore) {
		Map<String, Object> map = new HashMap<String, Object>();
		//订单信息
		WemallOrder wemallOrder = this.get(orderNo);
		//订单收货地址信息
		WemallOrderAddress wemallOrderAddress = wemallOrderAddressService.get(orderNo);
		//订单商品信息
		WemallOrderItem query = new WemallOrderItem();
		query.setOrderNo(orderNo);
		List<WemallOrderItem> orderItemList = wemallOrderItemService.findList(query);
		
		//判断是否需要添加可参与活动列表以及最大可使用积分数信息
		if(putActivityAndScore) {
			List<String> itemIds = Lists.newArrayList();
			for(WemallOrderItem orderItem : orderItemList) {
				itemIds.add(orderItem.getItemId());
			}
			//获取可参加的活动列表
			List<WemallItemActivity> activityList = wemallItemActivityService.findListByItems(itemIds);
			//计算参加每种活动后的价格
			for(WemallItemActivity entity : activityList) {
				try {
					Integer priceByOrderPrice = wemallItemActivityService.getPriceByOrderPrice(wemallOrder.getOriginalOrderPrice(), entity);
					entity.setJoinPrice(priceByOrderPrice);
				} catch (Exception e) {
				}
			}
			//获取最大可使用积分数
			int canUseTotalScore = wemallScoreInfoService.getCanUseTotalScore(orderItemList);
			map.put("activityList", activityList);
			map.put("canUseTotalScore", canUseTotalScore);
			
			User u = UserUtils.getUser();
			map.put("userCurScoreNum", u.getCurScoreNum());
			map.put("userCurBountyNum", u.getCurBountyNum());
			//获取积分和人民币的兑换比率
			OrderDict orderDict = OrderDictUtils.getOrderDictByTypeAndValue("orderPrice_about_set", "1");
			if(orderDict != null) {
				String orderDictPrice = orderDict.getPrice();
				map.put("scoreRate", orderDictPrice);
			}
		}
		
		map.put("ret", "0");
		map.put("retMsg", "获取成功！");
		map.put("wemallOrder", wemallOrder);
		map.put("wemallOrderAddress", wemallOrderAddress);
		map.put("orderItemList", orderItemList);
		return map;
	}
	
	/**
	 * 用户申请退货
	 * @param wemallOrder
	 */
	@Transactional(readOnly = false)
	public void applyForReject(WemallOrder wemallOrder) {
		wemallOrder.setApplyForReject(1);
		wemallOrder.setRejectDate(new Date());
		wemallOrder.preUpdate();
		dao.applyForReject(wemallOrder);
	}

	public List<WemallOrder> findUnPaidOrderList(WemallOrder wemallOrder) {
		return dao.findUnPaidOrderList(wemallOrder);
	}
}
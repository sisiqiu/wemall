package com.fulltl.wemall.modules.wemall.service.front;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fulltl.wemall.common.persistence.Page;
import com.fulltl.wemall.common.service.BaseService;
import com.fulltl.wemall.modules.pay.service.AlipayTradeService;
import com.fulltl.wemall.modules.pay.service.WeixinTradeService;
import com.fulltl.wemall.modules.pay.service.WemallOrderMgrService;
import com.fulltl.wemall.modules.sys.entity.User;
import com.fulltl.wemall.modules.sys.service.SystemService;
import com.fulltl.wemall.modules.sys.utils.UserUtils;
import com.fulltl.wemall.modules.wemall.entity.WemallItem;
import com.fulltl.wemall.modules.wemall.entity.WemallOrder;
import com.fulltl.wemall.modules.wemall.entity.WemallOrder.OrderStatus;
import com.fulltl.wemall.modules.wemall.entity.WemallOrder.PaymentType;
import com.fulltl.wemall.modules.wemall.entity.WemallOrderAddress;
import com.fulltl.wemall.modules.wemall.entity.WemallOrderItem;
import com.fulltl.wemall.modules.wemall.entity.WemallRecharge;
import com.fulltl.wemall.modules.wemall.entity.WemallShopCar;
import com.fulltl.wemall.modules.wemall.entity.WemallUserAddress;
import com.fulltl.wemall.modules.wemall.service.WemallFreightInfoService;
import com.fulltl.wemall.modules.wemall.service.WemallItemService;
import com.fulltl.wemall.modules.wemall.service.WemallOrderAddressService;
import com.fulltl.wemall.modules.wemall.service.WemallOrderItemService;
import com.fulltl.wemall.modules.wemall.service.WemallOrderService;
import com.fulltl.wemall.modules.wemall.service.WemallRechargeService;
import com.fulltl.wemall.modules.wemall.service.WemallShopCarService;
import com.fulltl.wemall.modules.wemall.service.WemallUserAddressService;
import com.fulltl.wemall.modules.wx.entity.WxUserInfo;
import com.fulltl.wemall.modules.wx.service.WxUserInfoService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * 订单管理前端服务层
 * @author ldk
 *
 */
@Service
public class WemallOrderFrontService extends BaseService {
	@Autowired
	private WemallOrderItemService wemallOrderItemService;
	@Autowired
	private WemallOrderAddressService wemallOrderAddressService;
	@Autowired
	private WemallUserAddressService wemallUserAddressService;
	@Autowired
	private WemallFreightInfoService wemallFreightInfoService;
	@Autowired
	private WemallShopCarService wemallShopCarService;
	@Autowired
	private WemallItemService wemallItemService;
	@Autowired
	private WemallOrderService wemallOrderService;
	@Autowired
	private WemallOrderMgrService wemallOrderMgrService;
	@Autowired
	private SystemService systemService;
	@Autowired
	private AlipayTradeService alipayTradeService;
	@Autowired
	private WeixinTradeService weixinTradeService;
	@Autowired
	private WxUserInfoService wxUserInfoService;
	@Autowired
	private WemallRechargeService wemallRechargeService;
	
	/**
	 * 获取订单商品列表
	 * @param wemallOrderItem
	 * @param request
	 * @return
	 */
	public Map<String, Object> getOrderItemList(WemallOrderItem wemallOrderItem, HttpServletRequest request) {
		Integer pageNo = null;
		Integer pageSize  = null;
		Map<String ,Object> map=new HashMap<String, Object>();
		try {
			pageNo = Integer.parseInt(request.getParameter("pageNo"));
			pageSize = Integer.parseInt(request.getParameter("pageSize"));
		} catch (NumberFormatException e) {
			map.put("ret", "-1");
			map.put("retMsg", "缺少页码和每页条数！");
			return map;
		}
		User user = UserUtils.getUser();
		if(StringUtils.isBlank(user.getId())) {
			map.put("ret", "-1");
			map.put("retMsg", "请先登录！");
			return map;
		} else {
			wemallOrderItem.setUserId(user.getId());
		}
		
		Page<WemallOrderItem> page = wemallOrderItemService.findPage(new Page<WemallOrderItem>(pageNo, pageSize), wemallOrderItem);
		/*List<Map<String, Object>> dataList = Lists.newArrayList();
		for(WemallOrderItem entity : page.getList()) {
			dataList.add(entity.getSmallEntityMap());
		}*/
		map.put("list", page.getList());
		map.put("count", page.getCount());
		map.put("ret", "0");
		map.put("retMsg", "获取成功");
		return map;
	}
	
	/**
	 * 获取订单评价列表
	 * @param wemallOrderItem
	 * @param request
	 * @return
	 */
	public Map<String, Object> getBuyerCommentList(WemallOrderItem wemallOrderItem, HttpServletRequest request) {
		Integer pageNo = null;
		Integer pageSize  = null;
		Map<String ,Object> map=new HashMap<String, Object>();
		try {
			pageNo = Integer.parseInt(request.getParameter("pageNo"));
			pageSize = Integer.parseInt(request.getParameter("pageSize"));
		} catch (NumberFormatException e) {
			map.put("ret", "-1");
			map.put("retMsg", "缺少页码和每页条数！");
			return map;
		}
		
		wemallOrderItem.setBuyerComment(1);
		
		Page<WemallOrderItem> page = wemallOrderItemService.findPage(new Page<WemallOrderItem>(pageNo, pageSize), wemallOrderItem);
		List<Map<String, Object>> dataList = Lists.newArrayList();
		for(WemallOrderItem entity : page.getList()) {
			dataList.add(entity.getBuyerCommentMap());
		}
		map.put("list", page.getList());
		map.put("count", page.getCount());
		map.put("ret", "0");
		map.put("retMsg", "获取成功");
		return map;
	}

	/**
	 * 获取订单商品详情
	 * @param wemallOrderItem
	 * @param request
	 * @return
	 */
	public Map<String, Object> getOrderItemDetail(WemallOrderItem wemallOrderItem, HttpServletRequest request) {
		Map<String ,Object> map=new HashMap<String, Object>();
		/*if(StringUtils.isBlank(wemallOrderItem.getOrderNo())) {
			map.put("ret", "-1");
			map.put("retMsg", "订单号不能为空！");
			return map;
		}*/
		
		Map<String, Object> data = Maps.newHashMap();
		//查询订单商品对象
		WemallOrderItem entity = wemallOrderItemService.get(wemallOrderItem);
		data.put("orderItemInfo", entity);
		//查询订单地址
		/*WemallOrderAddress orderAddr = wemallOrderAddressService.get(wemallOrderItem.getOrderNo());
		data.put("orderAddrInfo", orderAddr);*/
		//查询订单商品物流明细信息
		/*if(StringUtils.isNotBlank(entity.getFreightNo())) {
			WemallFreightInfo queryFreightInfo = new WemallFreightInfo();
			queryFreightInfo.setFreightNo(entity.getFreightNo());
			List<WemallFreightInfo> freightInfoList = wemallFreightInfoService.findList(queryFreightInfo);
			data.put("freightInfoList", freightInfoList);
		}*/

		map.put("data", data);
		map.put("ret", "0");
		map.put("retMsg", "获取成功");
		return map;
	}
	
	/**
	 * 买家评价订单商品
	 * @param wemallOrderItem
	 * @param request
	 * @return
	 */
	@Transactional(readOnly = false)
	public Map<String, Object> evaluateOrderItem(WemallOrderItem wemallOrderItem, HttpServletRequest request) {
		Map<String ,Object> map=new HashMap<String, Object>();
		if(StringUtils.isBlank(wemallOrderItem.getOrderNo())) {
			map.put("ret", "-1");
			map.put("retMsg", "订单号不能为空！");
			return map;
		}
		if(StringUtils.isBlank(wemallOrderItem.getItemId())) {
			map.put("ret", "-1");
			map.put("retMsg", "商品id不能为空！");
			return map;
		}
		if(StringUtils.isBlank(wemallOrderItem.getBuyerScore())) {
			map.put("ret", "-1");
			map.put("retMsg", "买家评分不能为空！");
			return map;
		}
		User user = UserUtils.getUser();
		//查询订单商品对象
		WemallOrderItem entity = wemallOrderItemService.get(wemallOrderItem);
		//判断是否可评价。判断当前用户以及判断订单商品状态
		if(entity == null || new Integer(1).equals(entity.getBuyerComment())) {
			map.put("ret", "-1");
			map.put("retMsg", "评价失败！失败原因：订单不存在，或订单已评价！");
			return map;
		}
		if(StringUtils.isBlank(entity.getUserId()) || !entity.getUserId().equals(user.getId())) {
			map.put("ret", "-1");
			map.put("retMsg", "订单用户与当前用户不匹配，请先登录！");
			return map;
		}
		
		//保存订单商品评价
		wemallOrderItem.setBuyerNick(user.getName());
		wemallOrderItem.setBuyerComment(1);
		wemallOrderItemService.saveBuyerEvaluate(wemallOrderItem);
		
		map.put("ret", "0");
		map.put("retMsg", "评价成功");
		return map;
	}
	
	/////////////////////////////////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////以下为生成订单部分///////////////////////////////////////////
	//////////////////////////////////////////////生成流程：///////////////////////////////////////////////
	//////////////////////////////////////////////1.先计算总价，生成一个订单。///////////////////////////////
	//////////////////////////////////////////////2.再根据订单号，生成订单--商品信息。////////////////////////
	//////////////////////////////////////////////3.根据传入的收货地址id保存订单地址。////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * 根据购物车id列表生成订单的接口。
	 * @param request
	 * @return
	 */
	@Transactional(readOnly = false)
	public Map<String, Object> generateOrderByShopCarIds(HttpServletRequest request) {
		Map<String ,Object> map=new HashMap<String, Object>();
		String shopCarStr = WebUtils.getCleanParam(request, "shopCarStr");
		System.err.println(shopCarStr);
		//校验数据
		if(StringUtils.isBlank(shopCarStr)) {
			map.put("ret", "-1");
			map.put("retMsg", "请选择购物车项！");
			return map;
		}
		
		// 校验当前用户是否已登录
		User user = UserUtils.getUser();
		map = systemService.checkCurrentUser(user);
		if(!"0".equals(map.get("ret"))) return map;
		
		//1.先计算总价，生成一个订单。
		List<String> shopCarStrList = Arrays.asList(shopCarStr.split(","));
		List<String> shopCarIds = Lists.newArrayList();
		Map<String, Integer> shopCarMap = Maps.newHashMap();
		String shopCarIdStr = StringUtils.EMPTY;
		for(String tempShopCarStr : shopCarStrList) {
			String key = tempShopCarStr.split("\\|")[0];//购物车id
			String num = tempShopCarStr.split("\\|")[1];//数量num
			shopCarIdStr = shopCarIdStr + key + ",";
			shopCarIds.add(key);
			shopCarMap.put(key, Integer.parseInt(num));
		}
		if(StringUtils.isNotBlank(shopCarIdStr)) {
			shopCarIdStr = shopCarIdStr.substring(0, shopCarIdStr.length()-1);
		}
		
		List<WemallShopCar> wemallShopCarList = wemallShopCarService.findByIds(shopCarIds);
		
		//获取价格
		Integer orderPrice = 0;
		Integer totalFreightPrice = 0;
		List<WemallOrderItem> wemallOrderItemList = Lists.newArrayList();
		if(wemallShopCarList != null) {
			for(WemallShopCar wemallShopCar : wemallShopCarList) {
				Integer shopCarPrice = wemallShopCarService.getPriceByWemallShopCar(wemallShopCar, shopCarMap.get(wemallShopCar.getId()));
				
				if(wemallShopCar.getItem().getFreightFree().equals(0)) {
					//不免邮
					totalFreightPrice = totalFreightPrice + wemallShopCar.getItem().getFreightPrice();
				}
				orderPrice = orderPrice + shopCarPrice;
				
				//构造订单--商品信息，之后方便保存
				WemallOrderItem wemallOrderItem = new WemallOrderItem();
				wemallOrderItem.initBy(wemallShopCar, null, shopCarPrice);//价格为单个商品总价格
				wemallOrderItem.setItemNum(shopCarMap.get(wemallShopCar.getId()));
				wemallOrderItem.setUserId(user.getId());
				wemallOrderItemList.add(wemallOrderItem);
			}
		}
		
		//校验库存
		boolean checkStorage = wemallItemService.checkStorage(wemallOrderItemList);
		if(!checkStorage) {
			map.put("ret", "-1");
			map.put("retMsg", "抱歉，商品库存不足！");
			return map;
		}
		
		orderPrice = orderPrice + totalFreightPrice;
		map = wemallOrderMgrService.generateOrderByType("购物车购买商品", orderPrice, totalFreightPrice, null, shopCarIdStr, "1");
		
		WemallOrder wemallOrder = null;
		if(!"0".equals(map.get("ret"))) return map;
		else wemallOrder = (WemallOrder)map.get("wemallOrder");
		
		//2.再根据订单号，生成订单--商品信息。
		for(WemallOrderItem wemallOrderItem : wemallOrderItemList) {
			wemallOrderItem.setOrderNo(wemallOrder.getOrderNo());
			wemallOrderItemService.save(wemallOrderItem);
		}
		
		//3.根据传入的收货地址id保存订单地址。
		saveDefaultAddressForOrder(user, wemallOrder.getOrderNo());
		
		map.put("ret", "0");
		map.put("retMsg", "生成成功！");
		map.put("orderNo", wemallOrder.getOrderNo());
		return map;
	}

	/**
	 * 根据单个商品生成订单的接口。
	 * @param wemallShopCar
	 * @return
	 */
	@Transactional(readOnly = false)
	public Map<String, Object> generateOrderByItem(HttpServletRequest request) {
		Map<String ,Object> map=new HashMap<String, Object>();
		String itemId = WebUtils.getCleanParam(request, "itemId");
		String itemNum = WebUtils.getCleanParam(request, "itemNum");
		String itemSpecIds = WebUtils.getCleanParam(request, "itemSpecIds");
		//校验数据
		if(StringUtils.isBlank(itemId) ||
				StringUtils.isBlank(itemNum)) {
			map.put("ret", "-1");
			map.put("retMsg", "商品id，商品数量不能为空！");
			return map;
		}
		
		// 校验当前用户是否已登录
		User user = UserUtils.getUser();
		map = systemService.checkCurrentUser(user);
		if(!"0".equals(map.get("ret"))) return map;
		
		
		//1.先计算总价，生成一个订单。
		WemallShopCar wemallShopCar = new WemallShopCar();
		wemallShopCar.setItemId(Integer.parseInt(itemId));
		wemallShopCar.setItemNum(Integer.parseInt(itemNum));
		wemallShopCar.setItemSpecIds(itemSpecIds);
		
		WemallItem wemallItem = wemallItemService.get(wemallShopCar.getItemId().toString());
		wemallShopCar.setItem(wemallItem);
		
		//获取价格
		Integer orderPrice = wemallShopCarService.getPriceByWemallShopCar(wemallShopCar, null);
		Integer totalFreightPrice = 0;
		if(wemallShopCar.getItem().getFreightFree().equals(0)) {
			//不免邮
			totalFreightPrice = totalFreightPrice + wemallShopCar.getItem().getFreightPrice();
		}
		//校验库存
		WemallOrderItem wemallOrderItem = new WemallOrderItem();
		wemallOrderItem.initBy(wemallShopCar, null, orderPrice);
		wemallOrderItem.setUserId(user.getId());
		List<WemallOrderItem> wemallOrderItemList = Lists.newArrayList();
		wemallOrderItemList.add(wemallOrderItem);
		boolean checkStorage = wemallItemService.checkStorage(wemallOrderItemList);
		if(!checkStorage) {
			map.put("ret", "-1");
			map.put("retMsg", "抱歉，商品库存不足！");
			return map;
		}
		
		orderPrice = orderPrice + totalFreightPrice;
		map = wemallOrderMgrService.generateOrderByType("购买商品：" + wemallItem.getName(), orderPrice, totalFreightPrice, null, null, "1");
		
		WemallOrder wemallOrder = null;
		if(!"0".equals(map.get("ret"))) return map;
		else wemallOrder = (WemallOrder)map.get("wemallOrder");
		
		//2.再根据订单号，生成订单--商品信息。
		wemallOrderItem.setOrderNo(wemallOrder.getOrderNo());
		wemallOrderItemService.save(wemallOrderItem);
		
		//3.根据默认收货地址id保存订单地址。
		saveDefaultAddressForOrder(user, wemallOrder.getOrderNo());
		
		map.put("ret", "0");
		map.put("retMsg", "生成成功！");
		map.put("orderNo", wemallOrder.getOrderNo());
		return map;
	}
	
	/**
	 * 根据充值设定表id生成充值订单的接口。
	 * @param wemallShopCar
	 * @return
	 */
	@Transactional(readOnly = false)
	public Map<String, Object> generateOrderByRecharge(HttpServletRequest request) {
		Map<String ,Object> map=new HashMap<String, Object>();
		String rechargeId = WebUtils.getCleanParam(request, "rechargeId");
		//校验数据
		if(StringUtils.isBlank(rechargeId)) {
			map.put("ret", "-1");
			map.put("retMsg", "充值设定id不能为空！");
			return map;
		}
		
		// 校验当前用户是否已登录
		User user = UserUtils.getUser();
		map = systemService.checkCurrentUser(user);
		if(!"0".equals(map.get("ret"))) return map;
		
		//1.先计算总价，生成一个订单。
		WemallRecharge wemallRecharge = wemallRechargeService.get(rechargeId);
		
		//获取价格
		Integer orderPrice = wemallRecharge.getOriginalPrice();
		Integer totalFreightPrice = 0;
		
		orderPrice = orderPrice + totalFreightPrice;
		map = wemallOrderMgrService.generateOrderByType("充值：" + wemallRecharge.getName(), orderPrice, totalFreightPrice, null, null, "2");
		
		WemallOrder wemallOrder = null;
		if(!"0".equals(map.get("ret"))) return map;
		else wemallOrder = (WemallOrder)map.get("wemallOrder");
		
		//2.如果需要设定地址，根据默认收货地址id保存订单地址。
		if(wemallRecharge.getNeedaddress().equals("1")) {
			saveDefaultAddressForOrder(user, wemallOrder.getOrderNo());
		}
		
		map.put("ret", "0");
		map.put("retMsg", "生成成功！");
		map.put("orderNo", wemallOrder.getOrderNo());
		return map;
	}
	
	/**
	 * 为订单保存默认地址
	 * @param user
	 * @param orderNo
	 */
	@Transactional(readOnly = false)
	private void saveDefaultAddressForOrder(User user, String orderNo) {
		WemallUserAddress query = new WemallUserAddress();
		query.setUser(user);
		query.setIsDefault(1);
		//获取默认地址
		List<WemallUserAddress> wemallUserAddressList = wemallUserAddressService.findList(query);
		if(wemallUserAddressList != null && wemallUserAddressList.size() > 0) {
			//默认地址存在，则设置订单地址为默认地址
			WemallUserAddress wemallUserAddress = wemallUserAddressList.get(0);
			WemallOrderAddress wemallOrderAddress = new WemallOrderAddress();
			wemallOrderAddress.setIsNewRecord(true);
			wemallOrderAddress.initBy(wemallUserAddress, orderNo);
			wemallOrderAddressService.save(wemallOrderAddress);
		}
	}

	/**
	 * 更新订单--地址信息
	 * @param request
	 * @return
	 */
	@Transactional(readOnly = false)
	public Map<String, Object> updateOrderAddress(HttpServletRequest request) {
		Map<String ,Object> map=new HashMap<String, Object>();
		String userAddressId = WebUtils.getCleanParam(request, "userAddressId");
		String orderNo = WebUtils.getCleanParam(request, "orderNo");
		
		//校验数据
		if(StringUtils.isBlank(userAddressId) ||
				StringUtils.isBlank(orderNo)) {
			map.put("ret", "-1");
			map.put("retMsg", "订单号和用户地址id不能为空！");
			return map;
		}
		
		//3.根据传入的收货地址id保存订单地址。
		WemallUserAddress wemallUserAddress = wemallUserAddressService.get(userAddressId);
		WemallOrderAddress wemallOrderAddress = wemallOrderAddressService.get(orderNo);
		if(wemallOrderAddress == null) {
			//插入
			wemallOrderAddress = new WemallOrderAddress();
			wemallOrderAddress.setIsNewRecord(true);
		} else {
			//更新
		}
		wemallOrderAddress.initBy(wemallUserAddress, orderNo);
		wemallOrderAddressService.save(wemallOrderAddress);
		
		map.put("ret", "0");
		map.put("retMsg", "更新成功！");
		return map;
	}
	
	/**
	 * 根据预约id，订单号，付款方式生成并返回预付款id。
	 * @param request
	 * @return
	 */
	@Transactional(readOnly = false)
	public Map<String, Object> getPrepareIdForPay(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		String paymentType = WebUtils.getCleanParam(request, "paymentType");//付款方式
		String orderNo = WebUtils.getCleanParam(request, "orderNo");//订单号
		// 校验当前用户是否已登录
		User user = UserUtils.getUser();
		map = systemService.checkCurrentUser(user);
		if(!"0".equals(map.get("ret"))) return map;
		
		//校验库存
		WemallOrderItem query = new WemallOrderItem();
		query.setOrderNo(orderNo);
		List<WemallOrderItem> wemallOrderItemList = wemallOrderItemService.findList(query);
		boolean checkStorage = wemallItemService.checkStorage(wemallOrderItemList);
		if(!checkStorage) {
			map.put("ret", "-1");
			map.put("retMsg", "抱歉，商品库存不足！");
			return map;
		}
		
		WemallOrder wemallOrder = wemallOrderService.get(orderNo);
		if(wemallOrder == null) {
    		map.put("ret", "-1");
    		map.put("retMsg", "订单不存在。");
        	return map;
    	}
		wemallOrder.setPaymentType(Integer.parseInt(paymentType));
		//更新订单状态值时，对状态做校验
		map = wemallOrder.checkUpdateStatus(OrderStatus.alreadyPaid);
		if(!"0".equals(map.get("ret"))) return map;
		
		WemallOrderAddress wemallOrderAddress = wemallOrderAddressService.get(orderNo);
		
		if(wemallOrderAddress == null) {
    		map.put("ret", "-1");
    		map.put("retMsg", "请选择订单收货地址。");
        	return map;
    	}
		
		//根据积分抵扣，或活动优惠，或其他优惠方式，对价格做调整。
		map = wemallOrderMgrService.updateOrderPriceForDiscount(wemallOrder, wemallOrderItemList, request);
		if(!"0".equals(map.get("ret"))) return map;
		
		//判断金额是否为0，若为0，则直接成功。
		if(wemallOrder.getOrderPrice().equals(0)) {
			//执行减库存
        	wemallItemService.reduceStorage(wemallOrder.getOrderNo());
        	
        	String buyerMessage = WebUtils.getCleanParam(request, "buyerMessage");//订单号
    		wemallOrder.setBuyerMessage(buyerMessage);
    		wemallOrderService.updatePrepayIdAndPayMethod(wemallOrder);
    		wemallOrderService.updateAllStatusByOrderNo(wemallOrder, OrderStatus.alreadyPaid.getValue());
    		
    		map.put("ret", "0");
    		map.put("retMsg", "生成成功！");
    		map.put("needPay", "0");
    		return map;
		}
		
		//生成订单信息，获取要进行付款所需要的相关支付信息
		if(PaymentType.alipay.getValue().toString().equals(paymentType)) {
			map = alipayTradeService.generatePrepareIdByType(wemallOrder, request);
		} else if(PaymentType.weixin.getValue().toString().equals(paymentType)) {
			map = weixinTradeService.generatePrepareIdByType(wemallOrder, request);
		} else {
			map.put("ret", "-1");
			map.put("retMsg", "支付方式格式错误！");
			return map;
		}
		
		if(!"0".equals(map.get("ret"))) {
			logger.error("订单生成错误！错误码：" + map.get("ret") + "。错误信息：" + map.get("retMsg"));
			throw new RuntimeException();
		}
		
		map.put("ret", "0");
		map.put("retMsg", "生成成功！");
		return map;
	}
	
	/**
	 * 充值订单，根据订单号，付款方式生成并返回预付款id。
	 * @param request
	 * @return
	 */
	@Transactional(readOnly = false)
	public Map<String, Object> getPrepareIdForRechargePay(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		String paymentType = WebUtils.getCleanParam(request, "paymentType");//付款方式
		String orderNo = WebUtils.getCleanParam(request, "orderNo");//订单号
		String needAddress = WebUtils.getCleanParam(request, "needAddress");//是否需要收货地址
		// 校验当前用户是否已登录
		User user = UserUtils.getUser();
		map = systemService.checkCurrentUser(user);
		if(!"0".equals(map.get("ret"))) return map;
		
		WemallOrder wemallOrder = wemallOrderService.get(orderNo);
		if(wemallOrder == null) {
    		map.put("ret", "-1");
    		map.put("retMsg", "订单不存在。");
        	return map;
    	}
		wemallOrder.setPaymentType(Integer.parseInt(paymentType));
		//更新订单状态值时，对状态做校验
		map = wemallOrder.checkUpdateStatus(OrderStatus.alreadyPaid);
		if(!"0".equals(map.get("ret"))) return map;
		
		if("1".equals(needAddress)) {
			WemallOrderAddress wemallOrderAddress = wemallOrderAddressService.get(orderNo);
			if(wemallOrderAddress == null) {
				map.put("ret", "-1");
				map.put("retMsg", "请选择订单收货地址。");
				return map;
			}
		}
		
		//判断金额是否为0，若为0，则直接成功。
		if(wemallOrder.getOrderPrice().equals(0)) {
        	String buyerMessage = WebUtils.getCleanParam(request, "buyerMessage");//订单号
    		wemallOrder.setBuyerMessage(buyerMessage);
    		wemallOrderService.updatePrepayIdAndPayMethod(wemallOrder);
    		wemallOrderService.updateAllStatusByOrderNo(wemallOrder, OrderStatus.alreadyPaid.getValue());
    		
    		map.put("ret", "0");
    		map.put("retMsg", "生成成功！");
    		map.put("needPay", "0");
    		return map;
		}
		
		//生成订单信息，获取要进行付款所需要的相关支付信息
		if(PaymentType.alipay.getValue().toString().equals(paymentType)) {
			map = alipayTradeService.generatePrepareIdByType(wemallOrder, request);
		} else if(PaymentType.weixin.getValue().toString().equals(paymentType)) {
			map = weixinTradeService.generatePrepareIdByType(wemallOrder, request);
		} else {
			map.put("ret", "-1");
			map.put("retMsg", "支付方式格式错误！");
			return map;
		}
		
		if(!"0".equals(map.get("ret"))) {
			logger.error("订单生成错误！错误码：" + map.get("ret") + "。错误信息：" + map.get("retMsg"));
			throw new RuntimeException();
		}
		
		map.put("ret", "0");
		map.put("retMsg", "生成成功！");
		return map;
	}

	/**
	 * 根据订单号和状态值，更新订单及订单商品的状态
	 * @param request
	 * @return
	 */
	@Transactional(readOnly = false)
	public Map<String, Object> updateOrderAndOrderItemStatus(String orderNo, Integer status) {
		Map<String, Object> map = new HashMap<String, Object>();
		if(StringUtils.isBlank(orderNo) || status == null) {
			map.put("ret", "-1");
			map.put("retMsg", "订单号和状态值不能为空！");
			return map;
		}
		// 校验当前用户是否已登录
		User user = UserUtils.getUser();
		map = systemService.checkCurrentUser(user);
		if(!"0".equals(map.get("ret"))) return map;
		
		WemallOrder wemallOrder = wemallOrderService.get(orderNo);
		//更新订单状态值时，对状态做校验
		map = wemallOrder.checkUpdateStatus(OrderStatus.getOrderStatusByValue(status));
		if(!"0".equals(map.get("ret"))) return map;
		
		wemallOrderService.updateAllStatusByOrderNo(wemallOrder, status);
		
		if(OrderStatus.alreadyCancelled_alreadyPaid.getValue().equals(status)) {
			//执行退款逻辑
			if(!wemallOrder.getOrderPrice().equals(0)) {
				wemallOrderMgrService.refund(orderNo, wemallOrder.getOrderPrice().toString(), null);
			}
		}
		
		
		map.put("ret", "0");
		map.put("retMsg", "生成成功！");
		return map;
	}
	
	/**
	 * 用户提交退货申请的接口。
	 * @param request
	 * @return
	 */
	@Transactional(readOnly = false)
	public Map<String, Object> applyForReject(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		String orderNo = WebUtils.getCleanParam(request, "orderNo");
		if(StringUtils.isBlank(orderNo)) {
			map.put("ret", "-1");
			map.put("retMsg", "订单号不能为空！");
			return map;
		}
		// 校验当前用户是否已登录
		User user = UserUtils.getUser();
		map = systemService.checkCurrentUser(user);
		if(!"0".equals(map.get("ret"))) return map;
		
		WemallOrder wemallOrder = wemallOrderService.get(orderNo);
		if(wemallOrder.getStatus().equals(OrderStatus.alreadyPaid.getValue())) {
			map.put("ret", "-1");
			map.put("retMsg", "商家尚未发货，用户可选择取消订单来撤回订单！");
			return map;
		}
		if(wemallOrder.getStatus().equals(OrderStatus.alreadyShipped.getValue()) || 
				wemallOrder.getStatus().equals(OrderStatus.alreadyReceived.getValue()) || 
				wemallOrder.getStatus().equals(OrderStatus.alreadyCommented.getValue())) {
			//可执行退货
			wemallOrderService.applyForReject(wemallOrder);
			map.put("ret", "0");
			map.put("retMsg", "提交退货申请成功！");
			return map;
		} else {
			map.put("ret", "-1");
			map.put("retMsg", "订单不可提交退货申请！");
			return map;
		}
	}
	
	/**
	 * 用户评论商品。
	 * @param request
	 * @return
	 */
	@Transactional(readOnly = false)
	public Map<String, Object> commentItem(WemallOrderItem wemallOrderItem, HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();

		if(StringUtils.isBlank(wemallOrderItem.getId())) {
			map.put("ret", "-1");
			map.put("retMsg", "订单商品id不能为空！");
			return map;
		}
		
		if(StringUtils.isBlank(wemallOrderItem.getBuyerScore())) {
			map.put("ret", "-1");
			map.put("retMsg", "买家评分不能为空！");
			return map;
		}
		
		// 校验当前用户是否已登录
		User user = UserUtils.getUser();
		map = systemService.checkCurrentUser(user);
		if(!"0".equals(map.get("ret"))) return map;
		
		WxUserInfo wxUserInfo = wxUserInfoService.getByUserId(user.getId());
		wemallOrderItem.setBuyerNick(wxUserInfo.getNickName());
		wemallOrderItem.setBuyerPhoto(wxUserInfo.getHeadImgUrl());
		wemallOrderItem.setCommentTime(new Date());
		wemallOrderItem.setBuyerComment(1);//已评价
		wemallOrderItem.setStatus(OrderStatus.alreadyCommented.getValue());
		
		wemallOrderItemService.saveBuyerEvaluate(wemallOrderItem);
		
		map.put("ret", "0");
		map.put("retMsg", "生成成功！");
		return map;
	}

	/**
	 * 根据订单号获取订单详情的接口
	 * @param request
	 * @return
	 */
	public Map<String, Object> getOrderDetail(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		//要获取订单信息，订单收货地址信息，订单商品信息
		String orderNo = WebUtils.getCleanParam(request, "orderNo");
		
		// 校验当前用户是否已登录
		User user = UserUtils.getUser();
		map = systemService.checkCurrentUser(user);
		if(!"0".equals(map.get("ret"))) return map;
		if(StringUtils.isBlank(orderNo)) {
			map.put("ret", "-1");
			map.put("retMsg", "订单号不能为空！");
			return map;
		}
		map = wemallOrderService.getOrderDetail(orderNo, true);
		return map;
	}

	/**
	 * 获取订单列表的接口
	 * @param request
	 * @return
	 */
	public Map<String, Object> getOrderList(WemallOrder wemallOrder, HttpServletRequest request) {
		Integer pageNo = null;
		Integer pageSize  = null;
		Map<String ,Object> map=new HashMap<String, Object>();
		String statusList = WebUtils.getCleanParam(request, "statusList");
		try {
			pageNo = Integer.parseInt(request.getParameter("pageNo"));
			pageSize = Integer.parseInt(request.getParameter("pageSize"));
		} catch (NumberFormatException e) {
			map.put("ret", "-1");
			map.put("retMsg", "缺少页码和每页条数！");
			return map;
		}
		// 校验当前用户是否已登录
		User user = UserUtils.getUser();
		map = systemService.checkCurrentUser(user);
		if(!"0".equals(map.get("ret"))) return map;
		
		wemallOrder.setUser(user);
		if(StringUtils.isNotBlank(statusList)) {
			wemallOrder.setStatusList(Arrays.asList(statusList.split(",")));
		}
		Page<WemallOrder> page = wemallOrderMgrService.findPage(new Page<WemallOrder>(pageNo, pageSize), wemallOrder);
		/*List<Map<String, Object>> dataList = Lists.newArrayList();
		for(WemallOrderItem entity : page.getList()) {
			dataList.add(entity.getSmallEntityMap());
		}*/
		List<Map<String, Object>> list = Lists.newArrayList();
		for(WemallOrder entity : page.getList()) {
			list.add(wemallOrderService.getOrderDetail(entity.getOrderNo(), false));
		}
		map.put("list", list);
		map.put("count", page.getCount());
		map.put("ret", "0");
		map.put("retMsg", "获取成功");
		return map;
	}
	
	
}

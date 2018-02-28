package com.fulltl.wemall.modules.wemall.service.front;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fulltl.wemall.common.persistence.Page;
import com.fulltl.wemall.common.service.BaseService;
import com.fulltl.wemall.modules.sys.entity.User;
import com.fulltl.wemall.modules.sys.utils.UserUtils;
import com.fulltl.wemall.modules.wemall.entity.WemallOrderAddress;
import com.fulltl.wemall.modules.wemall.entity.WemallOrderItem;
import com.fulltl.wemall.modules.wemall.service.WemallFreightInfoService;
import com.fulltl.wemall.modules.wemall.service.WemallOrderAddressService;
import com.fulltl.wemall.modules.wemall.service.WemallOrderItemService;
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
	private WemallFreightInfoService wemallFreightInfoService;
	
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
		
		Map<String, Object> data = Maps.newHashMap();
		//查询订单商品对象
		WemallOrderItem entity = wemallOrderItemService.get(wemallOrderItem.getOrderNo(), wemallOrderItem.getItemId());
		data.put("orderItemInfo", entity);
		//查询订单地址
		WemallOrderAddress orderAddr = wemallOrderAddressService.get(wemallOrderItem.getOrderNo());
		data.put("orderAddrInfo", orderAddr);
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
		WemallOrderItem entity = wemallOrderItemService.get(wemallOrderItem.getOrderNo(), wemallOrderItem.getItemId());
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

}

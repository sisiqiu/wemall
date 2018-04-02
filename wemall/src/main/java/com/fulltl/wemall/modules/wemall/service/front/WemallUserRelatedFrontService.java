package com.fulltl.wemall.modules.wemall.service.front;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
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
import com.fulltl.wemall.modules.sys.entity.User;
import com.fulltl.wemall.modules.sys.service.SystemService;
import com.fulltl.wemall.modules.sys.utils.UserUtils;
import com.fulltl.wemall.modules.wemall.entity.WemallBountyInfo;
import com.fulltl.wemall.modules.wemall.entity.WemallItemSpec;
import com.fulltl.wemall.modules.wemall.entity.WemallScoreInfo;
import com.fulltl.wemall.modules.wemall.entity.WemallShopCar;
import com.fulltl.wemall.modules.wemall.entity.WemallUserAddress;
import com.fulltl.wemall.modules.wemall.entity.WemallVipCard;
import com.fulltl.wemall.modules.wemall.service.WemallBountyInfoService;
import com.fulltl.wemall.modules.wemall.service.WemallItemSpecService;
import com.fulltl.wemall.modules.wemall.service.WemallScoreInfoService;
import com.fulltl.wemall.modules.wemall.service.WemallShopCarService;
import com.fulltl.wemall.modules.wemall.service.WemallUserAddressService;
import com.fulltl.wemall.modules.wemall.service.WemallVipCardService;
import com.google.common.collect.Lists;

/**
 * 用户相关信息管理前端服务层
 * @author ldk
 *
 */
@Service
public class WemallUserRelatedFrontService extends BaseService {

	@Autowired
	private WemallShopCarService wemallShopCarService;
	@Autowired
	private WemallVipCardService wemallVipCardService;
	@Autowired
	private WemallScoreInfoService wemallScoreInfoService;
	@Autowired
	private WemallBountyInfoService wemallBountyInfoService;
	@Autowired
	private WemallUserAddressService wemallUserAddressService;
	@Autowired
	private SystemService systemService;
	@Autowired
	private WemallItemSpecService wemallItemSpecService;
	
	/**
	 * 获取购物车列表信息
	 * @param wemallShopCar
	 * @param request
	 * @return
	 */
	public Map<String, Object> getShopCarList(WemallShopCar wemallShopCar, HttpServletRequest request) {
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
		//验证用户登陆
		map = systemService.checkCurrentUser(user);
		if(!"0".equals(map.get("ret"))) return map;
		
		wemallShopCar.setUser(user);
		Page<WemallShopCar> page = wemallShopCarService.findPage(new Page<WemallShopCar>(pageNo, pageSize), wemallShopCar);
		/*List<Map<String, Object>> dataList = Lists.newArrayList();
		for(WemallItem entity : page.getList()) {
			dataList.add(entity.getSmallEntityMap());
		}*/
		List<WemallShopCar> shopCarList = page.getList();
		for(WemallShopCar w :shopCarList){
			wemallShopCarService.fillItemSpecs(w);
		}
		map.put("list",shopCarList);
		map.put("count", page.getCount());
		map.put("ret", "0");
		map.put("retMsg", "获取成功");
		return map;
	}

	/**
	 * 添加购物车信息
	 * @param wemallShopCar
	 * @param request
	 * @return
	 */
	@Transactional(readOnly = false)
	public Map<String, Object> addShopCarInfo(WemallShopCar wemallShopCar, HttpServletRequest request) {
		Map<String ,Object> map=new HashMap<String, Object>();
		//验证用户登陆
		User user = UserUtils.getUser();
		map = systemService.checkCurrentUser(user);
		if(!"0".equals(map.get("ret"))) return map;
		wemallShopCar.setStatus(1);
		wemallShopCar.setUser(user);
		
		//验证数据
		map = super.beanValidator(wemallShopCar);
		if(!"0".equals(map.get("ret"))) return map;
		
		//执行添加
		wemallShopCarService.save(wemallShopCar);
		
		map.put("ret", "0");
		map.put("retMsg", "添加成功");
		return map;
	}

	/**
	 * 删除购物车信息
	 * @param wemallShopCar
	 * @param request
	 * @return
	 */
	@Transactional(readOnly = false)
	public Map<String, Object> removeShopCarInfo(HttpServletRequest request) {
		Map<String ,Object> map=new HashMap<String, Object>();
		//验证用户登陆
		User user = UserUtils.getUser();
		map = systemService.checkCurrentUser(user);
		if(!"0".equals(map.get("ret"))) return map;
		
		String ids = WebUtils.getCleanParam(request, "ids");
		if(StringUtils.isBlank(ids)) {
			map.put("ret", "-1");
			map.put("retMsg", "购物车id列表不能为空!");
			return map;
		}
		
		//执行删除
		wemallShopCarService.delete(Arrays.asList(ids.split(",")));
		
		map.put("ret", "0");
		map.put("retMsg", "删除成功");
		return map;
	}

	/////////////////////////////////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////以上为购物车，以下为用户地址///////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * 获取当前用户对应的用户地址列表
	 * @param wemallUserAddress
	 * @param request
	 * @return
	 */
	public Map<String, Object> getUserAddressList(WemallUserAddress wemallUserAddress, HttpServletRequest request) {
		Map<String ,Object> map=new HashMap<String, Object>();
		//验证用户登陆
		User user = UserUtils.getUser();
		map = systemService.checkCurrentUser(user);
		if(!"0".equals(map.get("ret"))) return map;
		wemallUserAddress.setUser(user);
		
		List<WemallUserAddress> list = wemallUserAddressService.findList(wemallUserAddress);
		map.put("list", list);
		map.put("count", list.size());
		map.put("ret", "0");
		map.put("retMsg", "获取成功");
		return map;
	}

	/**
	 * 获取用户地址详情
	 * @param wemallUserAddress
	 * @param request
	 * @return
	 */
	public Map<String, Object> getUserAddressDetail(WemallUserAddress wemallUserAddress, HttpServletRequest request) {
		Map<String ,Object> map=new HashMap<String, Object>();
		
		//验证用户登陆
		User user = UserUtils.getUser();
		map = systemService.checkCurrentUser(user);
		if(!"0".equals(map.get("ret"))) return map;
		//验证用户地址id是否为空
		if(StringUtils.isBlank(wemallUserAddress.getId())) {
			map.put("ret", "-1");
			map.put("retMsg", "用户地址id不能为空!");
			return map;
		}
		
		WemallUserAddress entity = wemallUserAddressService.get(wemallUserAddress);
		map.put("data", entity);
		map.put("ret", "0");
		map.put("retMsg", "获取成功");
		return map;
	}

	/**
	 * 添加用户地址
	 * @param wemallUserAddress
	 * @param request
	 * @return
	 */
	@Transactional(readOnly = false)
	public Map<String, Object> addUserAddress(WemallUserAddress wemallUserAddress, HttpServletRequest request) {
		Map<String ,Object> map=new HashMap<String, Object>();
		//验证用户登陆
		User user = UserUtils.getUser();
		map = systemService.checkCurrentUser(user);
		if(!"0".equals(map.get("ret"))) return map;
		wemallUserAddress.setUser(user);
		//wemallUserAddress.setIsDefault(0);	//初始添加设定为非默认选中项
		
		//验证数据
		map = super.beanValidator(wemallUserAddress);
		if(!"0".equals(map.get("ret"))) return map;
		
		//执行添加
		wemallUserAddressService.save(wemallUserAddress);
		
		if(wemallUserAddress.getIsDefault().equals(1)) {
			//如果添加的是选中项
			this.setDefaultUserAddr(wemallUserAddress, request);
		}
		
		map.put("ret", "0");
		map.put("retMsg", "添加成功");
		map.put("id", wemallUserAddress.getId());
		return map;
	}

	/**
	 * 设置默认用户地址
	 * @param wemallUserAddress
	 * @param request
	 * @return
	 */
	@Transactional(readOnly = false)
	public Map<String, Object> setDefaultUserAddr(WemallUserAddress wemallUserAddress, HttpServletRequest request) {
		Map<String ,Object> map=new HashMap<String, Object>();
		//验证用户登陆
		User user = UserUtils.getUser();
		map = systemService.checkCurrentUser(user);
		if(!"0".equals(map.get("ret"))) return map;
		//验证用户地址id是否为空
		if(StringUtils.isBlank(wemallUserAddress.getId())) {
			map.put("ret", "-1");
			map.put("retMsg", "用户地址id不能为空!");
			return map;
		}
		
		//将对应用户的地址都设置为非默认项
		wemallUserAddressService.setNotDefaultUserAddr(user.getId());
		//将对应用户地址设置为默认项
		wemallUserAddressService.setDefaultUserAddr(wemallUserAddress.getId());
		
		map.put("ret", "0");
		map.put("retMsg", "设置成功");
		return map;
	}
	
	/**
	 * 更新用户地址
	 * @param wemallUserAddress
	 * @param request
	 * @return
	 */
	@Transactional(readOnly = false)
	public Map<String, Object> updateUserAddress(WemallUserAddress wemallUserAddress, HttpServletRequest request) {
		Map<String ,Object> map=new HashMap<String, Object>();
		//验证用户登陆
		User user = UserUtils.getUser();
		map = systemService.checkCurrentUser(user);
		if(!"0".equals(map.get("ret"))) return map;
		wemallUserAddress.setUser(user);
		
		//验证用户地址id是否为空
		if(StringUtils.isBlank(wemallUserAddress.getId())) {
			map.put("ret", "-1");
			map.put("retMsg", "用户地址id不能为空!");
			return map;
		}
		
		//验证数据
		map = super.beanValidator(wemallUserAddress);
		if(!"0".equals(map.get("ret"))) return map;
		
		//执行更新
		wemallUserAddressService.save(wemallUserAddress);
		
		if(wemallUserAddress.getIsDefault().equals(1)) {
			//如果添加的是选中项
			this.setDefaultUserAddr(wemallUserAddress, request);
		}
		
		map.put("ret", "0");
		map.put("retMsg", "更新成功");
		return map;
	}

	/**
	 * 删除用户地址
	 * @param wemallUserAddress
	 * @param request
	 * @return
	 */
	@Transactional(readOnly = false)
	public Map<String, Object> deleteUserAddress(HttpServletRequest request) {
		Map<String ,Object> map=new HashMap<String, Object>();
		//验证用户登陆
		User user = UserUtils.getUser();
		map = systemService.checkCurrentUser(user);
		if(!"0".equals(map.get("ret"))) return map;
		
		String ids = WebUtils.getCleanParam(request, "ids");
		if(StringUtils.isBlank(ids)) {
			map.put("ret", "-1");
			map.put("retMsg", "收货地址id列表不能为空!");
			return map;
		}
		
		//执行删除
		wemallUserAddressService.delete(Arrays.asList(ids.split(",")));
		
		map.put("ret", "0");
		map.put("retMsg", "添加成功");
		return map;
	}
	
	/////////////////////////////////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////以上为用户地址，以下为会员卡///////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * 获取会员卡列表
	 * @param wemallVipCard
	 * @param request
	 * @return
	 */
	public Map<String, Object> getVipCardList(WemallVipCard wemallVipCard, HttpServletRequest request) {
		Map<String ,Object> map=new HashMap<String, Object>();

		List<WemallVipCard> list = wemallVipCardService.findList(wemallVipCard);
		map.put("list", list);
		map.put("count", list.size());
		map.put("ret", "0");
		map.put("retMsg", "获取成功");
		return map;
	}
	
	/**
	 * 获取会员卡详情
	 * @param wemallVipCard
	 * @param request
	 * @return
	 */
	public Map<String, Object> getVipCardDetail(WemallVipCard wemallVipCard, HttpServletRequest request) {
		Map<String ,Object> map=new HashMap<String, Object>();
		
		if(StringUtils.isBlank(wemallVipCard.getId())) {
			map.put("ret", "-1");
			map.put("retMsg", "会员卡id不能为空!");
			return map;
		}
		
		WemallVipCard entity = wemallVipCardService.get(wemallVipCard);
		map.put("data", entity);
		map.put("ret", "0");
		map.put("retMsg", "获取成功");
		return map;
	}
	
	/**
	 * 用户领取会员卡
	 * @param wemallVipCard
	 * @param request
	 * @return
	 */
	@Transactional(readOnly = false)
	public Map<String, Object> receiveVipCard(WemallVipCard wemallVipCard, HttpServletRequest request) {
		Map<String ,Object> map=new HashMap<String, Object>();
		User user = UserUtils.getUser();
		if(StringUtils.isBlank(wemallVipCard.getId())) {
			map.put("ret", "-1");
			map.put("retMsg", "会员卡id不能为空!");
			return map;
		}
		//验证用户登陆
		map = systemService.checkCurrentUser(user);
		if(!"0".equals(map.get("ret"))) return map;
		
		WemallVipCard vipCard = wemallVipCardService.get(wemallVipCard);
		
		if(vipCard == null) {
			map.put("ret", "-1");
			map.put("retMsg", "该会员卡不存在!");
			return map;
		}
		
		//验证用户是否已拥有该会员卡
		String vipCardIds = user.getVipCardIds();
		if(StringUtils.isNotBlank(vipCardIds) && Arrays.asList(vipCardIds.split(",")).contains(wemallVipCard.getId())) {
			map.put("ret", "-1");
			map.put("retMsg", "用户已拥有该会员卡!");
			return map;
		}
		
		//验证用户是否有权限获取该会员卡
		Integer totalOrderNum = user.getTotalOrderNum();
		Integer totalConsumeNum = user.getTotalConsumeNum();
		Integer totalScoreNum = user.getTotalScoreNum();
		
		Integer orderNum = vipCard.getOrderNum();
		Integer consumeNum = vipCard.getConsumeNum();
		Integer scoreNum = vipCard.getScoreNum();
		if(totalOrderNum.compareTo(orderNum) < 0 &&
				totalConsumeNum.compareTo(consumeNum) < 0 &&
				totalScoreNum.compareTo(scoreNum) < 0
				) {
			map.put("ret", "-1");
			map.put("retMsg", "领取条件未达成，领取失败!");
			return map;
		}
		
		//条件达成，执行领取
		if(StringUtils.isBlank(vipCardIds)) {
			vipCardIds = wemallVipCard.getId();
		} else {
			vipCardIds = vipCardIds + "," + wemallVipCard.getId();
		}
		systemService.updateUserInfo(user);
		
		map.put("ret", "0");
		map.put("retMsg", "领取成功");
		return map;
	}
	
	/////////////////////////////////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////以上为会员卡，以下为积分明细和奖励金明细///////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * 获取积分明细列表
	 * @param wemallScoreInfo
	 * @param request
	 * @return
	 */
	public Map<String, Object> getScoreInfoList(WemallScoreInfo wemallScoreInfo, HttpServletRequest request) {
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
		//验证用户登陆
		map = systemService.checkCurrentUser(user);
		if(!"0".equals(map.get("ret"))) return map;
		
		wemallScoreInfo.setUser(user);
		Page<WemallScoreInfo> page = wemallScoreInfoService.findPage(new Page<WemallScoreInfo>(pageNo, pageSize), wemallScoreInfo);
		List<Map<String, Object>> dataList = Lists.newArrayList();
		for(WemallScoreInfo entity : page.getList()) {
			dataList.add(entity.getSmallEntityMap());
		}
		map.put("list", dataList);
		map.put("count", page.getCount());
		map.put("ret", "0");
		map.put("retMsg", "获取成功");
		return map;
	}

	/**
	 * 获取奖励金明细列表
	 * @param wemallBountyInfo
	 * @param request
	 * @return
	 */
	public Map<String, Object> getBountyInfoList(WemallBountyInfo wemallBountyInfo, HttpServletRequest request) {
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
		//验证用户登陆
		map = systemService.checkCurrentUser(user);
		if(!"0".equals(map.get("ret"))) return map;
		
		wemallBountyInfo.setUser(user);
		Page<WemallBountyInfo> page = wemallBountyInfoService.findPage(new Page<WemallBountyInfo>(pageNo, pageSize), wemallBountyInfo);
		List<Map<String, Object>> dataList = Lists.newArrayList();
		for(WemallBountyInfo entity : page.getList()) {
			dataList.add(entity.getSmallEntityMap());
		}
		map.put("list", dataList);
		map.put("count", page.getCount());
		map.put("ret", "0");
		map.put("retMsg", "获取成功");
		return map;
	}

}

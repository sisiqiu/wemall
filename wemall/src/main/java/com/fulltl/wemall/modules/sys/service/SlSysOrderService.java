/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.fulltl.wemall.modules.sys.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Objects;
import com.google.common.collect.Sets;
import com.fulltl.wemall.common.persistence.Page;
import com.fulltl.wemall.common.service.CrudService;
import com.fulltl.wemall.common.utils.DateUtils;
import com.fulltl.wemall.modules.sys.dao.SlSysOrderDao;
import com.fulltl.wemall.modules.sys.entity.SlSysOrder;
import com.fulltl.wemall.modules.sys.entity.SlSysOrder.OrderTypeEnum;
import com.fulltl.wemall.modules.sys.entity.User;
import com.fulltl.wemall.modules.sys.utils.UserUtils;

/**
 * 订单管理Service
 * @author ldk
 * @version 2017-11-27
 */
@Service
@Transactional(readOnly = true)
public class SlSysOrderService extends CrudService<SlSysOrderDao, SlSysOrder> {
	@Autowired 
	private SystemService systemService;
	
	public SlSysOrder get(String id) {
		return super.get(id);
	}
	
	public List<SlSysOrder> findList(SlSysOrder slSysOrder) {
		return super.findList(slSysOrder);
	}
	
	public Page<SlSysOrder> findPage(Page<SlSysOrder> page, SlSysOrder slSysOrder) {
		return super.findPage(page, slSysOrder);
	}
	
	@Transactional(readOnly = false)
	public void save(SlSysOrder slSysOrder) {
		super.save(slSysOrder);
	}
	
	@Transactional(readOnly = false)
	public void delete(SlSysOrder slSysOrder) {
		super.delete(slSysOrder);
	}
	
	/**
	 * 预约：根据预约id生成订单对象的接口
	 * @param regId 预约id
	 * @param orderPrice 订单价格
	 * @param payMethod 付款方式；alipay--支付宝；weixin--微信支付
	 * @return key值为slSysOrder的value为订单对象
	 */
	@Transactional(readOnly = false)
	public Map<String, Object> generateOrderBy(String regId, String orderPrice, String payMethod) {
		Map<String, Object> retMap = new HashMap<String, Object>();
		/*SlHisRegbooking slHisRegbooking = slHisRegbookingService.get(regId);
		//验证预约是否存在
		if(slHisRegbooking == null) {
			retMap.put("ret", "60033");
			retMap.put("retMsg", "抱歉，该预约不存在！");
			return retMap;
		}
		//根据预约信息中的用户，验证是否是当前用户
		User user = UserUtils.getUser();
		retMap = systemService.checkCurrentUser(user);
		if(!"0".equals(retMap.get("ret"))) return retMap;
		if(!user.equals(slHisRegbooking.getUser())) {
			retMap.put("ret", "60029");
			retMap.put("retMsg", "抱歉，该预约不属于当前用户！");
			return retMap;
		}
		//根据预约信息中的挂号日期，验证是否可以挂号
		if(!DateUtils.formatDate(slHisRegbooking.getRegDate()).equals(DateUtils.formatDate(new Date()))) {
			retMap.put("ret", "60030");
			retMap.put("retMsg", "抱歉，挂号必须在预约日期当天进行！");
			return retMap;
		}
		//验证价格是否匹配
		//根据科室id（或医生id）和服务类别id，获取价格对应服务，进而获取价格
		String serviceCat = slHisRegbooking.getServiceCat();
		String deptcatId = slHisRegbooking.getDeptcatId();
		String doctorId = slHisRegbooking.getDoctorId();
		String subject = StringUtils.EMPTY;	//订单标题
		if(StringUtils.isBlank(doctorId)) {
			//说明是预约科室的号，根据科室id和服务类别id，获取对应服务
			SlHisHospdeptservice hospdeptService = new SlHisHospdeptservice();
			hospdeptService.setDeptcatId(deptcatId);
			hospdeptService.setServiceCat(serviceCat);
			SlHisHospdeptservice slHisHospdeptservice = slHisHospdeptserviceService.get(hospdeptService);
			if(!Objects.equal(slHisHospdeptservice.getPrice(), orderPrice)) {
				retMap.put("ret", "60031");
				retMap.put("retMsg", "抱歉，订单金额错误，请刷新重试！");
				return retMap;
			}
			subject = slHisRegbooking.getDeptName() + slHisRegbooking.getCatName();
		} else {
			//说明是预约医生的号，根据医生id和服务类别id，获取对应服务
			SlHisDoctservice doctservice = new SlHisDoctservice();
			doctservice.setDoctorId(doctorId);
			doctservice.setServiceCat(serviceCat);
			SlHisDoctservice slHisDoctservice = slHisDoctserviceService.get(doctservice);
			if(!Objects.equal(slHisDoctservice.getPrice(), orderPrice)) {
				retMap.put("ret", "60031");
				retMap.put("retMsg", "抱歉，订单金额错误，请刷新重试！");
				return retMap;
			}
			subject = slHisRegbooking.getDoctorName() + slHisRegbooking.getCatName();
		}
		
		//构造订单对象
		SlSysOrder slSysOrder = new SlSysOrder();
        slSysOrder.initSlSysOrder();
        //设置其他字段值
        slSysOrder.setSubject(subject);
        //slSysOrder.setDescription(subject);
        slSysOrder.setOrderPrice(orderPrice);	//订单价格
        slSysOrder.setActualPayment(orderPrice);	//实际支付价格
        slSysOrder.setOrderType(slHisRegbooking.getRegType());
        slSysOrder.setMobile(user.getMobile());
        slSysOrder.setPayMethod(payMethod);	//付款方式；alipay--支付宝；weixin--微信支付
        
        //统一验证参数是否合法
        retMap = beanValidator(slSysOrder);
        if(!"0".equals(retMap.get("ret"))) return retMap;
		
        retMap.put("ret", "0");
		retMap.put("retMsg", "构造成功！");
		retMap.put("slSysOrder", slSysOrder);*/
		return retMap;
	}
	
	/**
	 * 预约：根据订单对象和预约id，保存订单对象，并更新对应预约为该订单对象的订单号
	 * @param slSysOrder
	 * @param regId
	 */
	@Transactional(readOnly = false)
	public void saveOrderAndUpdateReg(SlSysOrder slSysOrder, String regId) {
		/*slSysOrder.setIsNewRecord(true);	//标记为插入
		this.save(slSysOrder);
		slHisRegbookingService.updateOrderNoById(regId, slSysOrder.getOrderNo());*/
	}
	
	/**
	 * 预约：更新订单对象为付款成功，并更新订单号为此订单的预约的状态为已挂号。
	 * @param slSysOrder
	 */
	@Transactional(readOnly = false)
	public void updateOrderAndUpdateRegStatus(SlSysOrder slSysOrder) {
	/*	slSysOrder.setPayState("3");	//支付成功
		slSysOrder.setStatus("2");		//已付款
		this.save(slSysOrder);
		slHisRegbookingService.updateStatusByOrderNo(slSysOrder.getOrderNo(), "2");*/
	}
	
	/**
	 * 护理预约：根据护理预约id生成订单对象的接口
	 * @param careAppoId 护理预约id
	 * @param orderPrice 订单价格
	 * @param payMethod 付款方式；alipay--支付宝；weixin--微信支付
	 * @return key值为slSysOrder的value为订单对象
	 */
	@Transactional(readOnly = false)
	public Map<String, Object> generateOrderByCareAppo(String careAppoId, String orderPrice, String payMethod) {
		Map<String, Object> retMap = new HashMap<String, Object>();
		
		/*SlHisCareappo slHisCareappo = slHisCareappoService.get(careAppoId);
		//验证护理预约是否存在
		if(slHisCareappo == null) {
			retMap.put("ret", "60033");
			retMap.put("retMsg", "抱歉，该预约不存在！");
			return retMap;
		}
		//根据护理预约信息中的用户，验证是否是当前用户
		User user = UserUtils.getUser();
		retMap = systemService.checkCurrentUser(user);
		if(!"0".equals(retMap.get("ret"))) return retMap;
		if(!user.equals(slHisCareappo.getUser())) {
			retMap.put("ret", "60029");
			retMap.put("retMsg", "抱歉，该预约不属于当前用户！");
			return retMap;
		}
		//验证价格是否匹配
		if(!Objects.equal(slHisCareappo.getTotalPrice(), orderPrice)) {
			retMap.put("ret", "60031");
			retMap.put("retMsg", "抱歉，订单金额错误，请刷新重试！");
			return retMap;
		}
		//订单标题
		String type = slHisCareappo.getType(); //1--标准监护，2--重症监护
		String subject = StringUtils.EMPTY;
		if("1".equals(type)) {
			subject = "标准护理预约";
		} else if("2".equals(type)) {
			subject = "重症护理预约";
		}
		
		//构造订单对象
		SlSysOrder slSysOrder = new SlSysOrder();
        slSysOrder.initSlSysOrder();
        //设置其他字段值
        slSysOrder.setSubject(subject);
        //slSysOrder.setDescription(subject);
        slSysOrder.setOrderPrice(orderPrice);	//订单价格
        slSysOrder.setActualPayment(orderPrice);	//实际支付价格
        slSysOrder.setOrderType(OrderTypeEnum.careAppo.getValue());
        slSysOrder.setMobile(user.getMobile());
        slSysOrder.setPayMethod(payMethod);	//付款方式；alipay--支付宝；weixin--微信支付
        
        //统一验证参数是否合法
        retMap = beanValidator(slSysOrder);
        if(!"0".equals(retMap.get("ret"))) return retMap;
		
        retMap.put("ret", "0");
		retMap.put("retMsg", "构造成功！");
		retMap.put("slSysOrder", slSysOrder);*/
		return retMap;
	}
	
	/**
	 * 护理预约：根据订单对象和护理预约id，保存订单对象，并更新对应护理预约为该订单对象的订单号
	 * @param slSysOrder
	 * @param regId
	 */
	@Transactional(readOnly = false)
	public void saveOrderAndUpdateCareAppo(SlSysOrder slSysOrder, String careAppoId) {
		/*slSysOrder.setIsNewRecord(true);	//标记为插入
		this.save(slSysOrder);
		slHisCareappoService.updateOrderNoById(careAppoId, slSysOrder.getOrderNo());*/
	}
	
	/**
	 * 预约：更新订单对象为付款成功，并更新护理预约为。1--未完成，未接单
	 * @param slSysOrder
	 */
	@Transactional(readOnly = false)
	public void updateOrderAndUpdateCareAppoStatus(SlSysOrder slSysOrder) {
		/*slSysOrder.setPayState("3");	//支付成功
		slSysOrder.setStatus("2");		//已付款
		this.save(slSysOrder);
		slHisCareappoService.updateStatusByOrderNo(slSysOrder.getOrderNo(), "1");
		
		//向mongodb中存储护理预约信息
		SlHisCareappo query = new SlHisCareappo();
		query.setOrderNo(slSysOrder.getOrderNo());
		query.setStatus("1");
		List<SlHisCareappo> findList = slHisCareappoService.findList(query);
		if(findList != null && findList.size() > 0) {
			careAppoAddrDao.insertBySlHisCareAppo(findList.get(0));
		}*/
	}
}
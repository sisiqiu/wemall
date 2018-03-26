package com.fulltl.wemall.modules.wemall.web.front;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fulltl.wemall.common.web.BaseController;
import com.fulltl.wemall.modules.wemall.entity.WemallBountyInfo;
import com.fulltl.wemall.modules.wemall.entity.WemallItem;
import com.fulltl.wemall.modules.wemall.entity.WemallScoreInfo;
import com.fulltl.wemall.modules.wemall.entity.WemallShopCar;
import com.fulltl.wemall.modules.wemall.entity.WemallUserAddress;
import com.fulltl.wemall.modules.wemall.entity.WemallVipCard;
import com.fulltl.wemall.modules.wemall.service.front.WemallUserRelatedFrontService;
import com.google.gson.Gson;

/**
 * 用户相关信息管理前端接口
 * @author ldk
 *
 */
@Controller
@RequestMapping(value = "${frontPath}/interface/wemall/userRelated")
public class WemallUserRelatedFrontController extends BaseController {
	@Autowired
	private WemallUserRelatedFrontService slHisUserRelatedFrontService;
	
	/**
	 * 获取购物车列表的接口。
	 * 
	 * 测试用例：
	 * 	url：http://ldkadmin.viphk.ngrok.org/f/interface/wemall/userRelated/getShopCarList
	 *	参数：
	 *		pageNo（*）=页码
	 *		pageSize（*）=每页条数
	 *
	 *
	 * 	例：
	 * 
	 * 结果示例：{"ret":"0","data":{"list":[{},{}],"count":3},"retMsg":"获取成功！"}
	 * 		或
	 * 		{"ret":"60015","data":{},"retMsg":"用户尚未登陆！请先登录！"}
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = {"getShopCarList"})
	@ResponseBody
	public String getShopCarList(WemallShopCar wemallShopCar, HttpServletRequest request, HttpServletResponse response, Model model) {
		Map<String, Object> map = slHisUserRelatedFrontService.getShopCarList(wemallShopCar, request);
		return gson.toJson(formatReturnMsg(map));
	}
	
	/**
	 * 添加购物车信息的接口。
	 * 
	 * 测试用例：
	 * 	url：http://ldkadmin.viphk.ngrok.org/f/interface/wemall/userRelated/addShopCarInfo
	 *	参数：
	 *		itemId（*）=商品id
	 *		itemNum（*）=商品数量
	 *		itemSpecIds=商品属性id列表
	 *
	 * 	例：
	 * 
	 * 结果示例：{"ret":"0","data":{},"retMsg":"添加成功！"}
	 * 		或
	 * 		{"ret":"60015","data":{},"retMsg":"用户尚未登陆！请先登录！"}
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = {"addShopCarInfo"})
	@ResponseBody
	public String addShopCarInfo(WemallShopCar wemallShopCar, HttpServletRequest request, HttpServletResponse response, Model model) {
		Map<String, Object> map = slHisUserRelatedFrontService.addShopCarInfo(wemallShopCar, request);
		return new Gson().toJson(formatReturnMsg(map));
	}
	
	/**
	 * 删除购物车信息的接口。
	 * 
	 * 测试用例：
	 * 	url：http://ldkadmin.viphk.ngrok.org/f/interface/wemall/userRelated/removeShopCarInfo
	 *	参数：
	 *		ids（*）=id列表
	 *
	 * 	例：
	 * 
	 * 结果示例：{"ret":"0","data":{},"retMsg":"删除成功！"}
	 * 		或
	 * 		{"ret":"60015","data":{},"retMsg":"用户尚未登陆！请先登录！"}
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = {"removeShopCarInfo"})
	@ResponseBody
	public String removeShopCarInfo(HttpServletRequest request, HttpServletResponse response, Model model) {
		Map<String, Object> map = slHisUserRelatedFrontService.removeShopCarInfo(request);
		return new Gson().toJson(formatReturnMsg(map));
	}
	
	/////////////////////////////////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////以上为购物车，以下为用户地址///////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * 获取用户收货地址列表的接口。
	 * 
	 * 测试用例：
	 * 	url：http://ldkadmin.viphk.ngrok.org/f/interface/wemall/userRelated/getUserAddressList
	 *	参数：
	 *		isDefault=是否默认（1--默认，0--非默认）
	 * 	例：
	 * 
	 * 结果示例：{"ret":"0","data":{"list":[{},{}],"count":3},"retMsg":"获取成功！"}
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = {"getUserAddressList"})
	@ResponseBody
	public String getUserAddressList(WemallUserAddress wemallUserAddress, HttpServletRequest request, HttpServletResponse response, Model model) {
		Map<String, Object> map = slHisUserRelatedFrontService.getUserAddressList(wemallUserAddress, request);
		return gson.toJson(formatReturnMsg(map));
	}
	
	/**
	 * 获取用户收货地址详情的接口。
	 * 
	 * 测试用例：
	 * 	url：http://ldkadmin.viphk.ngrok.org/f/interface/wemall/userRelated/getUserAddressDetail
	 *	参数：
	 *		id（*）=主键id
	 *
	 * 	例：
	 * 
	 * 结果示例：{"ret":"0","data":{},"retMsg":"获取成功！"}
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = {"getUserAddressDetail"})
	@ResponseBody
	public String getUserAddressDetail(WemallUserAddress wemallUserAddress, HttpServletRequest request, HttpServletResponse response, Model model) {
		Map<String, Object> map = slHisUserRelatedFrontService.getUserAddressDetail(wemallUserAddress, request);
		return gson.toJson(formatReturnMsg(map));
	}
	
	/**
	 * 添加用户收货地址的接口。
	 * 
	 * 测试用例：
	 * 	url：http://ldkadmin.viphk.ngrok.org/f/interface/wemall/userRelated/addUserAddress
	 *	参数：
	 *		receiverCountry（*）=国家
	 *		receiverProvince（*）=省份
	 *		receiverCity（*）=城市
	 *		receiverDistrict（*）=区县
	 *		receiverAddress（*）=收货地址
	 *		receiverZip（*）=邮编
	 *		receiverName（*）=收货人姓名
	 *		receiverMobile（*）=收货人手机
	 *		receiverPhone（*）=收货人电话
	 *		isDefault（*）=是否默认
	 *
	 * 	例：
	 * 
	 * 结果示例：{"ret":"0","data":{},"retMsg":"添加成功！"}
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = {"addUserAddress"})
	@ResponseBody
	public String addUserAddress(WemallUserAddress wemallUserAddress, HttpServletRequest request, HttpServletResponse response, Model model) {
		Map<String, Object> map = slHisUserRelatedFrontService.addUserAddress(wemallUserAddress, request);
		return gson.toJson(formatReturnMsg(map));
	}
	
	/**
	 * 设置默认用户收货地址的接口。
	 * 
	 * 测试用例：
	 * 	url：http://ldkadmin.viphk.ngrok.org/f/interface/wemall/userRelated/setDefaultUserAddr
	 *	参数：
	 *		id（*）=主键id
	 *
	 * 	例：
	 * 
	 * 结果示例：{"ret":"0","data":{},"retMsg":"添加成功！"}
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = {"setDefaultUserAddr"})
	@ResponseBody
	public String setDefaultUserAddr(WemallUserAddress wemallUserAddress, HttpServletRequest request, HttpServletResponse response, Model model) {
		Map<String, Object> map = slHisUserRelatedFrontService.setDefaultUserAddr(wemallUserAddress, request);
		return gson.toJson(formatReturnMsg(map));
	}
	
	/**
	 * 更新用户收货地址的接口。
	 * 
	 * 测试用例：
	 * 	url：http://ldkadmin.viphk.ngrok.org/f/interface/wemall/userRelated/updateUserAddress
	 *	参数：
	 *		id（*）=主键id
	 *		receiverCountry（*）=国家
	 *		receiverProvince（*）=省份
	 *		receiverCity（*）=城市
	 *		receiverDistrict（*）=区县
	 *		receiverAddress（*）=收货地址
	 *		receiverZip（*）=邮编
	 *		receiverName（*）=收货人姓名
	 *		receiverMobile（*）=收货人手机
	 *		receiverPhone（*）=收货人电话
	 *		isDefault（*）=是否默认
	 *
	 * 	例：
	 * 
	 * 结果示例：{"ret":"0","data":{},"retMsg":"更新成功！"}
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = {"updateUserAddress"})
	@ResponseBody
	public String updateUserAddress(WemallUserAddress wemallUserAddress, HttpServletRequest request, HttpServletResponse response, Model model) {
		Map<String, Object> map = slHisUserRelatedFrontService.updateUserAddress(wemallUserAddress, request);
		return gson.toJson(formatReturnMsg(map));
	}
	
	/**
	 * 删除用户收货地址的接口。
	 * 
	 * 测试用例：
	 * 	url：http://ldkadmin.viphk.ngrok.org/f/interface/wemall/userRelated/deleteUserAddress
	 *	参数：
	 *		ids（*）=主键id列表
	 *
	 * 	例：
	 * 
	 * 结果示例：{"ret":"0","data":{},"retMsg":"删除成功！"}
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = {"deleteUserAddress"})
	@ResponseBody
	public String deleteUserAddress(WemallUserAddress wemallUserAddress, HttpServletRequest request, HttpServletResponse response, Model model) {
		Map<String, Object> map = slHisUserRelatedFrontService.deleteUserAddress(request);
		return gson.toJson(formatReturnMsg(map));
	}
	
	/////////////////////////////////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////以上为用户地址，以下为会员卡///////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * 获取会员卡列表的接口。
	 * 
	 * 测试用例：
	 * 	url：http://ldkadmin.viphk.ngrok.org/f/interface/wemall/userRelated/getVipCardList
	 *	参数：
	 *
	 * 	例：
	 * 
	 * 结果示例：{"ret":"0","data":{"list":[{},{}],"count":3},"retMsg":"获取成功！"}
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = {"getVipCardList"})
	@ResponseBody
	public String getVipCardList(WemallVipCard wemallVipCard, HttpServletRequest request, HttpServletResponse response, Model model) {
		Map<String, Object> map = slHisUserRelatedFrontService.getVipCardList(wemallVipCard, request);
		return gson.toJson(formatReturnMsg(map));
	}
	
	/**
	 * 获取会员卡详情的接口。
	 * 
	 * 测试用例：
	 * 	url：http://ldkadmin.viphk.ngrok.org/f/interface/wemall/userRelated/getVipCardDetail
	 *	参数：
	 *		id（*）=主键id
	 *
	 * 	例：
	 * 
	 * 结果示例：{"ret":"0","data":{},"retMsg":"获取成功！"}
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = {"getVipCardDetail"})
	@ResponseBody
	public String getVipCardDetail(WemallVipCard wemallVipCard, HttpServletRequest request, HttpServletResponse response, Model model) {
		Map<String, Object> map = slHisUserRelatedFrontService.getVipCardDetail(wemallVipCard, request);
		return gson.toJson(formatReturnMsg(map));
	}
	
	/**
	 * 用户领取会员卡的接口。
	 * 
	 * 测试用例：
	 * 	url：http://ldkadmin.viphk.ngrok.org/f/interface/wemall/userRelated/receiveVipCard
	 *	参数：
	 *		id（*）=主键id
	 *
	 * 	例：
	 * 
	 * 结果示例：{"ret":"0","data":{},"retMsg":"领取成功！"}
	 * 		或
	 * 		{"ret":"60015","data":{},"retMsg":"用户尚未登陆！请先登录！"}
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = {"receiveVipCard"})
	@ResponseBody
	public String receiveVipCard(WemallVipCard wemallVipCard, HttpServletRequest request, HttpServletResponse response, Model model) {
		Map<String, Object> map = slHisUserRelatedFrontService.receiveVipCard(wemallVipCard, request);
		return gson.toJson(formatReturnMsg(map));
	}
	
	/////////////////////////////////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////以上为会员卡，以下为积分明细和奖励金明细///////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * 获取积分明细列表的接口。
	 * 
	 * 测试用例：
	 * 	url：http://ldkadmin.viphk.ngrok.org/f/interface/wemall/userRelated/getScoreInfoList
	 *	参数：
	 *		pageNo（*）=页码
	 *		pageSize（*）=每页条数
	 *		fromType=获取途径（1--签到；2--抽奖；3--评论）
	 *		type=类型（0--消耗；1--增加）
	 *		beginCreateDate=开始 创建时间
	 *		endCreateDate=结束 创建时间
	 *
	 * 	例：
	 * 
	 * 结果示例：{"ret":"0","data":{"list":[{},{}],"count":3},"retMsg":"获取成功！"}
	 * 		或
	 * 		{"ret":"60015","data":{},"retMsg":"用户尚未登陆！请先登录！"}
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = {"getScoreInfoList"})
	@ResponseBody
	public String getScoreInfoList(WemallScoreInfo wemallScoreInfo, HttpServletRequest request, HttpServletResponse response, Model model) {
		Map<String, Object> map = slHisUserRelatedFrontService.getScoreInfoList(wemallScoreInfo, request);
		return gson.toJson(formatReturnMsg(map));
	}
	
	/**
	 * 获取奖励金明细列表的接口。
	 * 
	 * 测试用例：
	 * 	url：http://ldkadmin.viphk.ngrok.org/f/interface/wemall/userRelated/getBountyInfoList
	 *	参数：
	 *		pageNo（*）=页码
	 *		pageSize（*）=每页条数
	 *		fromType=获取途径（1--现金订单）
	 *		type=类型（0--消耗；1--增加）
	 *		beginCreateDate=开始 创建时间
	 *		endCreateDate=结束 创建时间
	 *
	 * 	例：
	 * 
	 * 结果示例：{"ret":"0","data":{"list":[{},{}],"count":3},"retMsg":"获取成功！"}
	 * 		或
	 * 		{"ret":"60015","data":{},"retMsg":"用户尚未登陆！请先登录！"}
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = {"getBountyInfoList"})
	@ResponseBody
	public String getBountyInfoList(WemallBountyInfo wemallBountyInfo, HttpServletRequest request, HttpServletResponse response, Model model) {
		Map<String, Object> map = slHisUserRelatedFrontService.getBountyInfoList(wemallBountyInfo, request);
		return gson.toJson(formatReturnMsg(map));
	}
	
}

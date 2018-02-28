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
import com.fulltl.wemall.modules.wemall.entity.WemallItem;
import com.fulltl.wemall.modules.wemall.entity.WemallItemSort;
import com.fulltl.wemall.modules.wemall.entity.WemallOrderItem;
import com.fulltl.wemall.modules.wemall.service.front.WemallItemFrontService;
import com.google.gson.Gson;

/**
 * 商品管理前端接口
 * @author ldk
 *
 */
@Controller
@RequestMapping(value = "${frontPath}/interface/wemall/item")
public class WemallItemFrontController extends BaseController {
	
	@Autowired 
	private WemallItemFrontService slHisItemFrontService;
	
	/**
	 * 获取商品类别列表的接口。
	 * 
	 * 测试用例：
	 * 	url：http://ldkadmin.viphk.ngrok.org/f/interface/wemall/item/getItemSortList
	 *	参数：
	 *		parent.id=直属父类别id，用于查询该类别下的直属类别
	 *		parentIds=父类别id，用于查询该类别下的所有类别
	 *
	 * 	例：
	 * 
	 * 结果示例：{"ret":"0","data":{"list":[{},{}],"count":3},"retMsg":"获取成功！"}
	 * 		或
	 * 		{"ret":"-1","data":{},"retMsg":"字典类型不能为空！"}
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = {"getItemSortList"})
	@ResponseBody
	public String getItemSortList(WemallItemSort wemallItemSort, HttpServletRequest request, HttpServletResponse response, Model model) {
		Map<String, Object> map = slHisItemFrontService.getItemSortList(wemallItemSort, request);
		return gson.toJson(formatReturnMsg(map));
	}
	
	/**
	 * 获取商品列表的接口。
	 * 
	 * 测试用例：
	 * 	url：http://ldkadmin.viphk.ngrok.org/f/interface/wemall/item/getItemList
	 *	参数：
	 *		pageNo（*）=页码
	 *		pageSize（*）=每页容量
	 *		sortId=类别id
	 *		isTop=是否置顶；1--是，0--否
	 *		isNew=是否新品；1--是，0--否
	 *		isOnShelf=是否上架；1--是，0--否
	 *		canUseBounty=是否可用奖励金；1--是，0--否
	 *		canUseCoupon=是否可用优惠券；1--是，0--否
	 *		canUseScoreDeduct=是否可用积分抵扣；1--是，0--否
	 *		canUseScoreExchange=是否可用积分兑换；1--是，0--否
	 *		orderBy=排序方式
	 *
	 * 	例：
	 * 
	 * 结果示例：{"ret":"0","data":{"list":[{},{}],"count":3},"retMsg":"获取成功！"}
	 * 		或
	 * 		{"ret":"-1","data":{},"retMsg":"缺少页码和每页条数！"}
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = {"getItemList"})
	@ResponseBody
	public String getItemList(WemallItem wemallItem, HttpServletRequest request, HttpServletResponse response, Model model) {
		Map<String, Object> map = slHisItemFrontService.getItemList(wemallItem, request);
		return gson.toJson(formatReturnMsg(map));
	}
	
	/**
	 * 获取商品详情的接口。
	 * 
	 * 测试用例：
	 * 	url：http://ldkadmin.viphk.ngrok.org/f/interface/wemall/item/getItemDetail
	 *	参数：
	 *		id（*）=商品id
	 *
	 * 	例：
	 * 
	 * 结果示例：{"ret":"0","data":{},"retMsg":"获取成功！"}
	 * 		或
	 * 		{"ret":"-1","data":{},"retMsg":"商品id不能为空！"}
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = {"getItemDetail"})
	@ResponseBody
	public String getItemDetail(WemallItem wemallItem, HttpServletRequest request, HttpServletResponse response, Model model) {
		Map<String, Object> map = slHisItemFrontService.getItemDetail(wemallItem, request);
		return gson.toJson(formatReturnMsg(map));
	}
	
}

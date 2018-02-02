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
import com.fulltl.wemall.modules.cms.entity.Article;
import com.fulltl.wemall.modules.cms.entity.CmsFeedback;
import com.fulltl.wemall.modules.sys.entity.SlSysAdvertise;
import com.fulltl.wemall.modules.wemall.service.front.SlHisOperateFrontService;
import com.google.gson.Gson;


/**
 * 运营，客服管理前端接口
 * @author ldk
 *
 */
@Controller
@RequestMapping(value = "${frontPath}/interface/his/operate")
public class SlHisOperateFrontController extends BaseController {
	
	@Autowired 
	private SlHisOperateFrontService slHisOperateFrontService;
	
	/**
	 * 根据资讯类型进行分页查询
	 * 	url：http://ldkadmin.viphk.ngrok.org/f/interface/his/operate/getArticleListByCategory
	 *	参数：category.id=资讯类别id
	 *		pageSize=每页查询条数
	 * 		pageNo=页码
	 *	例：{
	 * 		category.id=1
	 * 		pageSize=1
	 * 		pageNo=2
	 * 		}
	 * 结果示例：{"ret":"0","data":{"list":[{},{}],"count":3},"retMsg":"获取成功！"}
	 * 		或
	 * 		{"ret":"60021","data":{},"retMsg":"缺少文章类别id！"}
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = {"getArticleListByCategory"})
	@ResponseBody
	public Map<String, Object> getArticleListByCategory(Article article,HttpServletRequest request, HttpServletResponse response, Model model) {
		Map<String, Object> retMap = slHisOperateFrontService.getArticleListByCategory(article, request, response);
		return formatReturnMsg(retMap);
	}
	
	/**
	 * 查询单个资讯
	 * url：http://ldkadmin.viphk.ngrok.org/f/interface/his/operate/getArticleById
	 * 参数：id  = 该表主键
	 *	例：{
	 * 		id="1bfc5c49ee81494dbb86e92f9a1b799d"
	 * 		}
	 * 结果示例：{"ret":"0","data":article2,"retMsg":"获取成功！"}
	 * 		或
	 * 		{"ret":"-1","data":{},"retMsg":"文章id不能为空！"}
	 */
	@RequestMapping(value = {"getArticleById"})
	@ResponseBody
	public Map<String, Object> getArticleById(Article article,HttpServletRequest request, HttpServletResponse response, Model model) {
		Map<String ,Object> retMap=slHisOperateFrontService.getArticleById(article, request, response);
		return formatReturnMsg(retMap);
	}
	
	/**
	 * 查询广告列表
	 *url：http://ldkadmin.viphk.ngrok.org/f/interface/his/operate/getAdvertiseList
	 *参数：待定
	 *		pageSize=每页查询条数
	 * 		pageNo=页码
	 * 		orderBy=排序字段，可为空
	 * 例：{
	 * 		pageSize=1
	 * 		pageNo=2
	 * 		orderBy=title asc
	 *  	}
	 *结果示例：{"ret":"0","data":{"list":[{},{}],"count":3},"retMsg":"获取成功！"}
	 *		或
	 *		{"ret":"-1","data":{},"retMsg":"缺少页码和每页文章条数！"}
	 */
	@RequestMapping(value = {"getAdvertiseList"})
	@ResponseBody
	public String getAdvertiseList(SlSysAdvertise slSysAdvertise,HttpServletRequest request, HttpServletResponse response, Model model) {
		Map<String ,Object> retMap=slHisOperateFrontService.getAdvertiseList(slSysAdvertise, request, response);
		return new Gson().toJson(formatReturnMsg(retMap));
	}
	
	/**
	 * 查询单个广告
	 * url：http://ldkadmin.viphk.ngrok.org/f/interface/his/operate/getAdvertiseById
	 * 参数：id  = 该表主键
	 *	例：{
	 * 		id=200520170ad34dc8b1732228beb512c3
	 * 		}
	 * 结果示例：{"ret":"0","data":slSysAdvertise2,"count":page.getCount(),"retMsg":"获取成功！"}
	 * 		或
	 * 		{"ret":"-1","data":{},"retMsg":"广告栏id不能为空！"}
	 */
	@RequestMapping(value = {"getAdvertiseById"})
	@ResponseBody
	public String getAdvertiseById(SlSysAdvertise slSysAdvertise,HttpServletRequest request, HttpServletResponse response, Model model) {
		Map<String ,Object> retMap=slHisOperateFrontService.getAdvertiseById(slSysAdvertise, request, response);
		return new Gson().toJson(formatReturnMsg(retMap));
	}
	
	/**
	 * 根据用户反馈的建议给意见反馈表新增一条意见反馈数据
	 * 测试用例（参数与患者注册接口基本一致）：
	 * 	url：http://ldkadmin.viphk.ngrok.org/f/interface/his/operate/addFeedback
	 *	参数：opinionText;		// 反馈内容
	 *		telephone;		// 手机号码
	 *		username;		// 用户的名称（可选）
	 *	例：{
	 * 		opinionText=服务不周到,
	 *		telephone=13135355489,
	 *		username=小张（可选）
	 * 		}
	 * 
	 * 结果示例：{"ret":"0","retMsg":"添加成功！","data":{}}
	 * 		或
	 * 		{"ret":"60015","data":{},"retMsg":"用户尚未登陆！请先登录！"}
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = {"addFeedback"})
	@ResponseBody
	public String addFeedback(CmsFeedback cmsFeedback,HttpServletRequest request, HttpServletResponse response, Model model) {
		Map<String, Object> retMap=slHisOperateFrontService.addFeedback(cmsFeedback);
 		return new Gson().toJson(formatReturnMsg(retMap));
	}
	
}

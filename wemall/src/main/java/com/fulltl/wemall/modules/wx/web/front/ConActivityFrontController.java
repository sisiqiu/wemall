package com.fulltl.wemall.modules.wx.web.front;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.web.util.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.fulltl.wemall.common.persistence.Page;
import com.fulltl.wemall.common.utils.StringUtils;
import com.fulltl.wemall.common.web.BaseController;
import com.fulltl.wemall.modules.sys.entity.User;
import com.fulltl.wemall.modules.sys.utils.UserUtils;
import com.fulltl.wemall.modules.wx.entity.ConActivity;
import com.fulltl.wemall.modules.wx.entity.ConUserActivity;
import com.fulltl.wemall.modules.wx.service.ConActivityService;
import com.fulltl.wemall.modules.wx.service.ConUserActivityService;
import com.fulltl.wemall.modules.wx.service.WxUserinfoService;
@Controller
@RequestMapping(value = "${frontPath}/wx/conActivity")
public class ConActivityFrontController extends BaseController{
	//临时用户id;
	int  userId=1000;
	@Autowired
	private ConActivityService conActivityService;
	@Autowired
	private ConUserActivityService conUserActivityService;
	@Autowired
	private WxUserinfoService wxUserInfoService;
	
	/**
	 * 对openId和Service进行预处理
	 * @param request
	 * @param model
	 */
	private void preHandleForWXOpenId(HttpServletRequest request, Model model) {
		String openId = WebUtils.getCleanParam(request, "openId");
		if(StringUtils.isNotBlank("openId")) {
			model.addAttribute("openId", openId);
			//根据openId获取用户id，进行登陆。
			if(StringUtils.isBlank(UserUtils.getUser().getId())) {
				//未登陆，才进行登陆。
				wxUserInfoService.loginByOpenId(openId);
			}
		}
		model.addAttribute("serviceId", globalWxServiceId);
		model.addAttribute("globalAppUrl", globalAppUrl);
	}
	
	/**
	 * 跳转活动页面的控制器
	 * @param conActivity
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = {"viewActivities"})
	public String viewActivities(ConActivity conActivity, HttpServletRequest request, HttpServletResponse response, Model model) {
		System.out.println("进跳转方法");
		preHandleForWXOpenId(request, model);
		return "sanlen_website/wx/news-index";
	}
	/**
	 * 查询活动信息列表
	 * @param conActivity
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = {"listForAjax"})
	@ResponseBody
	public List<ConActivity> listForAjax(ConActivity conActivity, HttpServletRequest request, HttpServletResponse response, Model model) {
		System.out.println("进查活动的方法");
		
		System.out.println("路径="+request.getContextPath());
		Page<ConActivity> page = conActivityService.findPage(new Page<ConActivity>(request, response), conActivity); 
		return page.getList();
	}
	
	/**
	 * 跳转活动详情
	 * @param conActivity
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = {"viewActivity"})
	public String viewActivity(ConActivity conActivity, HttpServletRequest request, HttpServletResponse response, Model model) {
		/*System.out.println("进查活动详情方法");
		System.out.println("活动Id="+conActivity);
		ConActivity conActivityRe=conActivityService.get(conActivity);*/
		model.addAttribute("activityID",conActivity.getId());
		preHandleForWXOpenId(request, model);
		return "sanlen_website/wx/news-detail";
	}
	/**
	 * 查询活动详情
	 * @param conActivity
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = {"viewActivityInfo"})
	@ResponseBody
	public ConActivity viewActivityInfo(ConActivity conActivity, HttpServletRequest request, HttpServletResponse response, Model model) {
		System.out.println("进查活动详情方法");
		System.out.println("活动Id="+conActivity);
		//Map<String, Object> retMap = new HashMap<String, Object>();
		ConActivity conActivityRe=conActivityService.get(conActivity);
		model.addAttribute("conActivityRe",conActivityRe);
		//preHandleForWXOpenId(request, model);
		//retMap.put("activityInfo", conActivityRe);
		return conActivityRe;
	}
	private String getMapToJSON(Map<String, Object> retMap) {
        Gson gs = new Gson();

        return gs.toJson(retMap);
    }
	/**
	 * 查询活动详细信息
	 * @param conActivity
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = {"viewCurActivity"})
	public String viewCurActivity(HttpServletRequest request, HttpServletResponse response, Model model) {
		System.out.println("进查当前活动详情方法");
		ConActivity conActivityRe=conActivityService.findCurActivity();
		model.addAttribute("activityID",conActivityRe.getId());
		preHandleForWXOpenId(request, model);
		if(conActivityRe == null) {
			//若当前没有活动，则跳转到当前没有活动页面
			return "sanlen_website/wx/hasNoActivitys";
		}
		//return "sanlen_website/wx/hasNoActivitys";
		return "sanlen_website/wx/news-detail";
	}
	/**
	 * 查询当前用户活动状态
	 * @param conActivity
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = {"userActivityStatus"})
	@ResponseBody
	public String  userActivityStatus(ConActivity conActivity, HttpServletRequest request, HttpServletResponse response, Model model) {
		System.out.println("进入查询状态方法");
		String status="";
		User user = UserUtils.getUser();
		if(StringUtils.isNotEmpty(user.getId())) {
			System.out.println("StringUtils.isNotEmpty(user.getId())");
			System.out.println("活动Id="+conActivity.getId());
			ConUserActivity	conUserActivity2 = conUserActivityService.getByActUserId(conActivity.getId(), user.getId());
			if(conUserActivity2 !=null && conUserActivity2.getStatus()!=null){
				status = conUserActivity2.getStatus();
			}else{
				status = "没有数据";
			}
			
			//用户已登录，不为空
		} else {
			//用户未登录，为空
			status = "没有用户";
			System.out.println("用户为空");
		}
		return status;
	}
	/**
	 * 修改当前用户活动的状态
	 * @param conActivity
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = {"EditUserActivityStatus"})
	@ResponseBody
	public String EditUserActivityStatus(ConActivity conActivity, HttpServletRequest request, HttpServletResponse response, Model model) {
		//定义回调函数值
		String  editResult="";
		//接收页面值
		String EditType = WebUtils.getCleanParam(request, "editStatu");
		//获取当前用户
		User user = UserUtils.getUser();
		//查询当前用户活动信息
		ConUserActivity	conUserActivity = conUserActivityService.getByActUserId(conActivity.getId(), user.getId());
		//查询当前活动信息
		ConActivity conActivityRe=conActivityService.get(conActivity);
		//然后判断修改
		//等于1  表示从已参加修改成已签到
		if(EditType .equals("1") ){
			//验证当前活动的状态是否为可签到
			if(conActivityRe.getStatus().equals("3")){
				if(new Date().getTime()<conActivityRe.getAttendanceEndtime().getTime()){
					//设置需要修改的用户活动实体类
					conUserActivity.setStatus("2");
					conUserActivityService.save(conUserActivity);
					editResult="signed";
				}else{
					editResult="timeOut";
				}
			}
		}else if(EditType .equals("2")){
			if(conActivityRe.getStatus().equals("5")){
				//设置需要修改的用户活动实体类
				conUserActivity.setStatus("3");
				conUserActivityService.save(conUserActivity);
				editResult="lotteryed";
			}
		}
		return editResult;
	}
	/**
	 * 跳转参加会议页面
	 * @param conActivity
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = {"joinPageJump"})
	public String joinPageJump(ConActivity conActivity, HttpServletRequest request, HttpServletResponse response, Model model) {
		System.out.println("进入");
		System.out.println("活动Id="+conActivity.getId());
		model.addAttribute("activityId",conActivity.getId());
		model.addAttribute("serviceId", globalWxServiceId);
		return "sanlen_website/wx/apply";
	}
	/**
	 * 新增用户活动表
	 * @param conUserActivity
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = {"addUserActivity"})
	@ResponseBody
	public String addUserActivity(ConUserActivity conUserActivity, HttpServletRequest request, HttpServletResponse response, Model model) {
		System.out.println("进入到新增用户活动信息");
		System.out.println("名字="+conUserActivity.getUserName());
		System.out.println("信息="+conUserActivity.getInformation());
		//返回变量
		String isSuccess="";
		User user = UserUtils.getUser();
		//设置用户活动表的用户Id
		conUserActivity.setUser(user);
		conUserActivity.setStatus("1");
		//查询该用户有没有该活动的数据
		ConUserActivity  conUserActivitySelect=conUserActivityService.getByActUserId(conUserActivity.getActivityid()+"", user.getId());
		if(conUserActivitySelect ==null){
			System.out.println("为空");
			//实例化一个活动类
			ConActivity conActivity=new ConActivity();
			//设置该类的主键Id
			conActivity.setId(conUserActivity.getActivityid()+"");
			//查询当前活动信息
			ConActivity conActivityRe=conActivityService.get(conActivity);
			Integer maxpeoplenum = null, currentpeoplenum = null;
			try {
				maxpeoplenum = Integer.parseInt(conActivityRe.getMaxpeoplenum());
				currentpeoplenum = Integer.parseInt(conActivityRe.getCurrentpeoplenum());
			} catch (NumberFormatException e) {
				System.out.println("最大人数和当前参与人数必须为数字！");
				e.printStackTrace();
			}
			//如果最大参加人数大于当前报名人数
			System.out.println("最大人数="+maxpeoplenum);
			System.out.println("当前人数="+currentpeoplenum);
			if(maxpeoplenum>currentpeoplenum){
				//获取当前时间
				new Date().getTime();
				//获取活动时间
				
				if(new Date().getTime()<conActivityRe.getRegistrationEndtime().getTime()){
					//用户活动表新增一条用户活动信息
					conUserActivityService.save(conUserActivity);
					//声明一个当前参加人数加1的变量
					int nowBaom=currentpeoplenum+1;
					//将查询的活动表信息的当前人数设置为更新的当前人数
					conActivityRe.setCurrentpeoplenum(nowBaom+"");
					//调用修改方法
					conActivityService.save(conActivityRe);
					isSuccess="succeed";
				}else{
					System.out.println("时间已过");
					isSuccess="timeOut";
				}
				
				
			}else{
				isSuccess="man";
			}
			
		}else{
			System.out.println("不为空");
			isSuccess="fail";
		}
		return isSuccess;
	}
	/**
	 * 跳转至绑定页面
	 * @param conActivity
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = {"bindingPageJump"})
	public String bindingPageJump(ConActivity conActivity, HttpServletRequest request, HttpServletResponse response, Model model) {
		System.out.println("进入跳转绑定用户页面");
		//model.addAttribute("activityId",conActivity.getId());
		model.addAttribute("activityId", conActivity.getId());
		preHandleForWXOpenId(request, model);
		return "sanlen_website/wx/bind-phone";
	}
	
	@RequestMapping(value = {"prizeDraw"})
	public String prizeDraw(ConActivity conActivity, HttpServletRequest request, HttpServletResponse response, Model model) {
		return "sanlen_website/wx/draw-index";
	}
	/**
	 * 查询活动信息并跳转抽奖页面
	 * @param conActivity
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = {"cjActivityList"})
	public List<ConActivity> cjActivityList(ConActivity conActivity, HttpServletRequest request, HttpServletResponse response, Model model) {
		System.out.println("进查活动的方法");
		Page<ConActivity> page = conActivityService.findPage(new Page<ConActivity>(request, response), conActivity); 
		return page.getList();
	}
	/**
	 * 给用户添加奖品
	 * @param conActivity
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = {"addPrice"})
	@ResponseBody
	public String addPrice(ConUserActivity conUserActivity, HttpServletRequest request, HttpServletResponse response, Model model) {
		System.out.println("进入抽奖");
		//定义回调函数值
		String  isSuccess="成功";
			System.out.println("手机="+conUserActivity.getMobile());
			System.out.println("奖励="+conUserActivity.getPrice());
			
				
				//根据用户手机查询该用户用户活动表的信息
			ConUserActivity conUserActivitySelect=conUserActivityService.getByActUserPhone(conUserActivity.getMobile());
			if(null!=conUserActivitySelect){	
			//给用户活动表设奖品
				conUserActivitySelect.setPrice(conUserActivity.getPrice());
				conUserActivitySelect.setStatus("3");
				//修改
				conUserActivityService.save(conUserActivitySelect);
			}
		return isSuccess;
	}
	/**
	 * 根据用户状态与活动抽奖
	 * @param conUserActivity
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = {"userActByPrice"})
	@ResponseBody
	public List<ConUserActivity> userActByPrice(ConActivity conActivity, HttpServletRequest request, HttpServletResponse response, Model model) {
		System.out.println("进入查询可抽奖用户");
		//定义回调函数值
		
			//根据用户手机查询该用户用户活动表的信息
			List<ConUserActivity> conUserActivityList=conUserActivityService.getByActidStatu(conActivity.getId().toString(), "2");
			List<ConUserActivity> deleteList = Lists.newArrayList();
			for(ConUserActivity entity: conUserActivityList){
				if(StringUtils.isNotBlank(entity.getPrice())){
					deleteList.add(entity);
				}
			}
			conUserActivityList.removeAll(deleteList);
			for(ConUserActivity obj:conUserActivityList){
				System.out.println("手机="+obj.getMobile());
			}
		
		return conUserActivityList;
	}
}

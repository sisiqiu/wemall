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

import com.fulltl.wemall.common.persistence.Page;
import com.fulltl.wemall.common.sms.sendmode.ccp.SMSVerify;
import com.fulltl.wemall.common.utils.StringUtils;
import com.fulltl.wemall.common.web.BaseController;
import com.fulltl.wemall.modules.sys.entity.User;
import com.fulltl.wemall.modules.sys.security.UsernamePasswordToken;
import com.fulltl.wemall.modules.sys.service.SystemService;
import com.fulltl.wemall.modules.sys.utils.UserUtils;
import com.fulltl.wemall.modules.wx.entity.ConActivity;
import com.fulltl.wemall.modules.wx.entity.ConUserActivity;
import com.fulltl.wemall.modules.wx.entity.UserBehavior;
import com.fulltl.wemall.modules.wx.entity.WxUserInfo;
import com.fulltl.wemall.modules.wx.service.ConActivityService;
import com.fulltl.wemall.modules.wx.service.ConUserActivityService;
import com.fulltl.wemall.modules.wx.service.WxUserInfoService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.Gson;

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
	private SystemService systemService;
	@Autowired
	private WxUserInfoService wxUserInfoService;
	
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
	public String userActivityStatus(ConActivity conActivity, HttpServletRequest request, HttpServletResponse response, Model model) {
		Map<String, String> map = Maps.newHashMap();
		String status="";
		User user = UserUtils.getUser();
		if(StringUtils.isNotEmpty(user.getId())) {
			ConUserActivity	conUserActivity2 = conUserActivityService.getByActUserId(conActivity.getId(), user.getId());
			if(conUserActivity2 !=null && conUserActivity2.getStatus()!=null){
				map.put("userActivityStatus", conUserActivity2.getStatus());
			}else{
				map.put("errorStatus", "noUserActivity");
			}
			//用户已登录，不为空
		} else {
			//用户未登录，为空
			map.put("errorStatus", "noUser");
		}
		return new Gson().toJson(map);
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
				if(conActivityRe.getAttendanceEndtime() == null || new Date().getTime()<conActivityRe.getAttendanceEndtime().getTime()){
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
		//绑定用户
		Map<String, Object> retMap = Maps.newHashMap();
		String mobile = WebUtils.getCleanParam(request, "mobile");
		retMap = bingUser(request);
		if(!"200".equals(retMap.get("ret"))) return new Gson().toJson(retMap);
		
		//返回变量
		String isSuccess="";
		User user = UserUtils.getUser();
		//设置用户活动表的用户Id
		conUserActivity.setUser(user);
		conUserActivity.setStatus("1");
		//查询该用户有没有该活动的数据
		ConUserActivity  conUserActivitySelect=conUserActivityService.getByActUserId(conUserActivity.getActivityid()+"", user.getId());
		if(conUserActivitySelect ==null){
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
			if(maxpeoplenum>currentpeoplenum){
				//获取活动时间
				if(conActivityRe.getRegistrationEndtime() == null || new Date().getTime()<conActivityRe.getRegistrationEndtime().getTime()){
					//用户活动表新增一条用户活动信息
					conUserActivityService.save(conUserActivity);
					//声明一个当前参加人数加1的变量
					int nowBaom=currentpeoplenum+1;
					//将查询的活动表信息的当前人数设置为更新的当前人数
					conActivityRe.setCurrentpeoplenum(nowBaom+"");
					//调用修改方法
					conActivityService.save(conActivityRe);
					retMap.put("ret","200");
					retMap.put("retMsg","参加成功！");
				}else{
					retMap.put("ret","-1");
					retMap.put("retMsg","当前时间已过报名时间，不能参加");
				}
				
			}else{
				retMap.put("ret","-1");
				retMap.put("retMsg","抱歉，报名人数已满");
			}
			
		}else{
			retMap.put("ret","-1");
			retMap.put("retMsg","用户已参加该活动！");
		}
		return new Gson().toJson(retMap);
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
		Page<ConActivity> page = conActivityService.findPage(new Page<ConActivity>(request, response), conActivity); 
		return page.getList();
	}
	/**
	 * 给用户添加奖品
	 * 参数：userIdList=用户id列表
	 * 		activityid=活动id
	 * 		price=奖项
	 * 		priceGoods=奖品
	 * @param conActivity
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = {"addPrice"})
	@ResponseBody
	public String addPrice(HttpServletRequest request, HttpServletResponse response, Model model) {
		ConUserActivity conUserActivity = new ConUserActivity();
		String userIdList = WebUtils.getCleanParam(request, "userIdList");
		String activityid = WebUtils.getCleanParam(request, "activityid");
		String price = WebUtils.getCleanParam(request, "price");
		String priceGoods = WebUtils.getCleanParam(request, "priceGoods");
		//定义回调函数值
		Map<String, Object> retMap = Maps.newHashMap();
		if(StringUtils.isNotBlank(activityid) &&
				StringUtils.isNotBlank(price) &&
				StringUtils.isNotBlank(priceGoods)
				) {
			conUserActivity.setActivityid(Integer.parseInt(activityid));
			conUserActivity.setPrice(price);
			conUserActivity.setPriceGoods(priceGoods);
			conUserActivity.setUserIdList(userIdList);
			conUserActivity.setStatus("3");
			//修改
			conUserActivityService.updatePriceBy(conUserActivity);
		}
		retMap.put("ret", "0");
		retMap.put("retMsg", "更新成功");
		return new Gson().toJson(retMap);
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
		//定义回调函数值
		//根据用户手机查询该用户用户活动表的信息
		List<ConUserActivity> conUserActivityList=conUserActivityService.getByActidStatu(conActivity.getId().toString(), "1");
		List<ConUserActivity> deleteList = Lists.newArrayList();
		for(ConUserActivity entity: conUserActivityList){
			if(StringUtils.isNotBlank(entity.getPrice())){
				deleteList.add(entity);
			}
		}
		conUserActivityList.removeAll(deleteList);
		for(ConUserActivity obj:conUserActivityList){
		}
	
		return conUserActivityList;
	}
	
	/**
	 * 绑定用户
	 * @param request 
	 * @return
	 */
	private Map<String, Object> bingUser(HttpServletRequest request) {
		String ret = "400";
        String retMsg = "操作失败";
		Map<String, Object> retMap = new HashMap<String, Object>();
		/*String mobile = WebUtils.getCleanParam(request, "mobile");
        String verifyServID = WebUtils.getCleanParam(request, "verifyServID");
        String sms_code = WebUtils.getCleanParam(request, "sms_code");
        String openId = WebUtils.getCleanParam(request, "openId");
        String serviceId = WebUtils.getCleanParam(request, "serviceId");
        
        SMSVerify sms = new SMSVerify();
        User user = null;
        UsernamePasswordToken userToken = new UsernamePasswordToken();
        try {
        	//验证短信验证码
            if (sms.checkVerifyCode(mobile, sms_code, verifyServID, false).equals("0")) {
            	//短信验证码验证通过
            	*//**
            	 *  如果不允许多个微信号绑定同一个系统用户，则可以根据手机号查询微信绑定用户表，
            	 *  看存在与否，不存在，则允许绑定，否则提示该手机已绑定过微信号。
            	 *  现在，暂定允许多个微信号绑定同一个系统用户。
            	 *//*
                user = systemService.quickGetUserByMobileForWX(mobile);
                // 自动登录
                userToken.setUsername(user.getMobile());
                userToken.setPassword("123456".toCharArray());
                // 短信验证码登陆，跳过密码对比校验
                userToken.setLogin_type("SMSCode");
                UserUtils.getSubject().login(userToken);

                //执行绑定用户
                WxUserInfo curWxUserInfo = wxUserInfoService.getWxUserInfoBy(openId, serviceId);
                wxUserInfoService.updateWXUserInfoBy(UserBehavior.BIND, curWxUserInfo, user);
                
                ret = "200";
                retMsg = "操作成功";
                retMap.put("loginname", user.getLoginName());
                retMap.put("password", user.getPassword());
            } else {
                ret = "204";
                retMsg = "短信验证码错误！请重试！";
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        retMap.put("ret", ret);
        retMap.put("retMsg", retMsg);*/
		return retMap;
	}
}

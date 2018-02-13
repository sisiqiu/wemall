package com.fulltl.wemall.modules.wemall.web.front;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fulltl.wemall.common.web.BaseController;
import com.fulltl.wemall.modules.sys.service.SystemService;
import com.fulltl.wemall.modules.wemall.service.front.SlHisUserFrontService;
import com.google.common.collect.Maps;
import com.google.gson.Gson;

/**
 * 用户管理前端接口
 * @author ldk
 *
 */
@Controller
@RequestMapping(value = "${frontPath}/interface")
public class SlHisUserFrontController extends BaseController {
	
	@Autowired
	private SlHisUserFrontService slHisUserFrontService;
	
	/**
	 * 验证用户名是否已被使用。
	 * 
	 * 测试用例：
	 * 	url：http://ldkadmin.viphk.ngrok.org/f/interface/wemall/user/checkLoginName
	 *	参数：loginName=用户名  或  mobile=用户名
	 * 	例：loginName=13838548839
	 * 
	 * 结果示例：{"ret":"60001","retMsg":"该用户名已被使用！"}
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = {"wemall/user/checkLoginName"})
	@ResponseBody
	public String checkLoginName(HttpServletRequest request, HttpServletResponse response, Model model) {
		Map<String, Object> retMap = Maps.newHashMap();
		String mobile = WebUtils.getCleanParam(request, "mobile");
		String loginName = WebUtils.getCleanParam(request, "loginName");
		if(StringUtils.isNotBlank(mobile)) {
			retMap = slHisUserFrontService.checkLoginName(mobile);
		} else {
			retMap = slHisUserFrontService.checkLoginName(loginName);
		}
		return new Gson().toJson(formatReturnMsg(retMap));
	}
	
	/**
	 * 发送验证码
	 * 
	 * 测试用例：
	 * 	url：http://ldkadmin.viphk.ngrok.org/f/interface/wemall/user/getCode
	 *	参数：mobile=手机号
	 * 	例：mobile=13838548839
	 * 
	 * 结果示例：{"ret":"0","retMsg":"验证码发送成功！","data":{"verifyID":"ccf0d8b8e25c40ad948fdfafb93f382d","mobile":"13838548839"}}
	 * 参数说明：verifyID--验证码ID，mobile--接收验证码的手机号
	 * 
	 * @param mobile
	 * @return
	 */
    @RequestMapping(value = "wemall/user/getCode")
    @ResponseBody
    public String getSMSCode(HttpServletRequest request) {
    	String mobile = WebUtils.getCleanParam(request, "mobile");
    	return new Gson().toJson(formatReturnMsg(slHisUserFrontService.getSMSCode(mobile)));
    }
	
	/**
	 * 患者快速注册接口
	 * 
	 * 测试用例：
	 * 	url：http://ldkadmin.viphk.ngrok.org/f/interface/wemall/user/registerForPatient
	 *	参数：mobile=用户名，
	 *		verifyServID=验证码ID，
	 *		sms_code=验证码值，
	 *		rememberMe=是否记住我，
	 *		password=密码
	 * 	例：{
	 * 		mobile=13838548839,
	 * 		verifyServID=21481a100b8f4214a58740eafbb5f5a9，
	 *		sms_code=5314，
	 *		rememberMe=false，
	 *		password=123456
	 * 		}
	 * 
	 * 结果示例：{"ret":"60002","retMsg":"短信验证码错误！请重试！","data":{}}
	 *		或
	 *		  {"ret":"0","retMsg":"登陆成功！","data":{}}
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = {"wemall/user/registerForPatient"})
	@ResponseBody
	public String registerForPatient(HttpServletRequest request, HttpServletResponse response, Model model) {
		return new Gson().toJson(formatReturnMsg(slHisUserFrontService.registerAndGetResult(SystemService.USERTYPE_PATIENT, request)));
	}
	
	/**
	 * 医生快速注册接口
	 * 
	 * 测试用例（参数与患者注册接口基本一致）：
	 * 	url：http://ldkadmin.viphk.ngrok.org/f/interface/wemall/user/registerForDoctor
	 *	参数：mobile=用户名，
	 *		verifyServID=验证码ID，
	 *		sms_code=验证码值，
	 *		rememberMe=是否记住我，
	 *		password=密码
	 * 	例：{
	 * 		mobile=13838548839,
	 * 		verifyServID=21481a100b8f4214a58740eafbb5f5a9，
	 *		sms_code=5314，
	 *		rememberMe=false，
	 *		password=123456
	 * 		}
	 * 
	 * 结果示例：{"ret":"60002","retMsg":"短信验证码错误！请重试！","data":{}}
	 *		或
	 *		  {"ret":"0","retMsg":"登陆成功！","data":{}}
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = {"wemall/user/registerForDoctor"})
	@ResponseBody
	public String registerForDoctor(HttpServletRequest request, HttpServletResponse response, Model model) {
		return new Gson().toJson(formatReturnMsg(slHisUserFrontService.registerAndGetResult(SystemService.USERTYPE_DOCTOR, request)));
	}
	
	/**
	 * 患者登陆接口
	 * 
	 * 测试用例：
	 * 	url：http://ldkadmin.viphk.ngrok.org/f/interface/wemall/user/loginForPatient
	 *	参数：loginFrom=user_login
	 *		loginName=用户名，
	 *		password=密码，
	 *		rememberMe=是否记住我，
	 *
	 *		或
	 *
	 *		loginFrom=mobile_login
	 *		mobile=手机号，
	 *		verifyServID=验证码ID，
	 *		sms_code=验证码值，
	 *		rememberMe=是否记住我，
	 *
	 * 	例：{
	 * 		loginFrom=user_login,
	 * 		loginName=13838548839,
	 *		password=123456,
	 *		rememberMe=false，
	 * 		}
	 * 		
	 * 		或
	 * 
	 * 		{
	 * 		loginFrom=mobile_login,
	 * 		mobile=13838548839,
	 * 		verifyServID=21481a100b8f4214a58740eafbb5f5a9，
	 *		sms_code=5314，
	 *		rememberMe=false，
	 * 		}
	 * 
	 * 结果示例：{"ret":"0","retMsg":"登陆成功！","data":{"user":{}}}
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = {"wemall/user/loginForPatient"})
	@ResponseBody
	public String loginForPatient(HttpServletRequest request, HttpServletResponse response, Model model) {
		Map<String, Object> retMap = slHisUserFrontService.loginAndGetResult(SystemService.USERTYPE_PATIENT, request);
		return new Gson().toJson(formatReturnMsg(retMap));
	}
	
	/**
	 * 医生登陆接口
	 * 
	 * 测试用例（参数与患者登陆接口基本一致）：
	 * 	url：http://ldkadmin.viphk.ngrok.org/f/interface/wemall/user/loginForDoctor
	 *	参数：loginFrom=user_login
	 *		loginName=用户名，
	 *		password=密码，
	 *		rememberMe=是否记住我，
	 *
	 *		或
	 *
	 *		loginFrom=mobile_login
	 *		mobile=手机号，
	 *		verifyServID=验证码ID，
	 *		sms_code=验证码值，
	 *		rememberMe=是否记住我，
	 *
	 * 	例：{
	 * 		loginFrom=user_login,
	 * 		loginName=13838548839,
	 *		password=123456,
	 *		rememberMe=false，
	 * 		}
	 * 		
	 * 		或
	 * 
	 * 		{
	 * 		loginFrom=mobile_login,
	 * 		mobile=13838548839,
	 * 		verifyServID=21481a100b8f4214a58740eafbb5f5a9，
	 *		sms_code=5314，
	 *		rememberMe=false，
	 * 		}
	 * 
	 * 结果示例：{"ret":"0","retMsg":"登陆成功！","data":{"user":{}}}
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = {"wemall/user/loginForDoctor"})
	@ResponseBody
	public String loginForDoctor(HttpServletRequest request, HttpServletResponse response, Model model) {
		Map<String, Object> retMap = slHisUserFrontService.loginAndGetResult(SystemService.USERTYPE_DOCTOR, request);
		return new Gson().toJson(formatReturnMsg(retMap));
	}
	
	/**
	 * 根据系统用户id，查询该用户对应的医生信息的接口。
	 * 
	 * 测试用例（参数与患者注册接口基本一致）：
	 * 	url：http://ldkadmin.viphk.ngrok.org/f/interface/wemall/user/getDoctorInfo
	 *	参数：userId=用户ID，
	 * 
	 * 结果示例：{"ret":"0","retMsg":"获取成功！","data":{"userInfo":{...},"doctorInfo":{...}}}
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = {"wemall/user/getDoctorInfo"})
	@ResponseBody
	public String getDoctorInfo(HttpServletRequest request, HttpServletResponse response, Model model) {
		String userId = WebUtils.getCleanParam(request, "userId");
		return new Gson().toJson(formatReturnMsg(slHisUserFrontService.getDoctorInfo(userId)));
	}
	
	/**
	 * 根据旧手机号码，通过验证码，可修改为新手机号码的接口（同样要验证码校验）。
	 * step1:校验旧手机验证码的接口。设定可设定新手机号的有效时长，返回校验成功与否。
	 * 
	 * 测试用例（参数与患者注册接口基本一致）：
	 * 	url：http://ldkadmin.viphk.ngrok.org/f/interface/wemall/user/checkSMSCodeForOldMobile
	 *	参数：oldMobile=原手机号，
	 *		verifyServID=验证码ID，
	 *		sms_code=验证码值，
	 *	例：{
	 * 		oldMobile=13838548839,
	 *		verifyServID=239d43bd786a4b33a7abf927bb47ac3e,
	 *		sms_code=3572
	 * 		}
	 * 
	 * 结果示例：{"ret":"0","retMsg":"验证成功！","data":{"updateMobileVerifyID":"dab138ea16b94fe68bf7a9717eac03ad"}}
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = {"wemall/user/checkSMSCodeForOldMobile"})
	@ResponseBody
	public String checkSMSCodeForOldMobile(HttpServletRequest request, HttpServletResponse response, Model model) {
		return new Gson().toJson(formatReturnMsg(slHisUserFrontService.checkSMSCodeForOldMobile(request)));
	}
	
	/**
	 * 根据旧手机号码，通过验证码，可修改为新手机号码的接口（同样要验证码校验）。
	 * step2:更新手机号码(需要验证可设定新手机号的有效时间是否已超过，验证新手机验证码是否匹配)
	 * 
	 * 测试用例（参数与患者注册接口基本一致）：
	 * 	url：http://ldkadmin.viphk.ngrok.org/f/interface/wemall/user/updateMobile
	 *	参数：oldMobile=原手机号，
	 *		newMobile=新手机号，
	 *		verifyServID=新手机号对应的验证码ID，
	 *		sms_code=新手机号对应的验证码值，
	 * 		updateMobileVerifyID=原手机校验通过后，返回的更新手机权限验证id
	 * 	例：{
	 * 		oldMobile=13838548839,
	 * 		newMobile=13120716167,
	 *		verifyServID=c33470c2d5bd49b99dc13c7146a6b0d4,
	 *		sms_code=2403,
	 *		updateMobileVerifyID=dab138ea16b94fe68bf7a9717eac03ad
	 * 		}
	 * 
	 * 结果示例：{"ret":"0","retMsg":"更新成功！","data":{}}
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = {"wemall/user/updateMobile"})
	@ResponseBody
	public String updateMobile(HttpServletRequest request, HttpServletResponse response, Model model) {
		return new Gson().toJson(formatReturnMsg(slHisUserFrontService.updateMobile(request)));
	}
	
	/**
	 * 根据手机号，验证码，修改密码的接口。
	 * 
	 * 测试用例（参数与患者注册接口基本一致）：
	 * 	url：http://ldkadmin.viphk.ngrok.org/f/interface/wemall/user/updatePWByMobile
	 *	参数：mobile=手机号，
	 *		verifyServID=手机号对应的验证码ID，
	 *		sms_code=手机号对应的验证码值，
	 *		newPassword=新密码，
	 *
	 * 	例：{
	 * 		mobile=13838548839,
	 *		verifyServID=c33470c2d5bd49b99dc13c7146a6b0d4,
	 *		sms_code=2403,
	 *		newPassword=123456
	 * 		}
	 * 
	 * 结果示例：{"ret":"0","retMsg":"修改密码成功！","data":{}}
	 * 		或
	 * 		{"ret":"60002","retMsg":"短信验证码错误！请重试！","data":{}}
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = {"wemall/user/updatePWByMobile"})
	@ResponseBody
	public String updatePWByMobile(HttpServletRequest request, HttpServletResponse response, Model model) {
		return new Gson().toJson(formatReturnMsg(slHisUserFrontService.updatePWByMobile(request)));
	}
	
	/**
	 * 根据原密码，新密码，修改密码的接口。
	 * 
	 * 测试用例（参数与患者注册接口基本一致）：
	 * 	url：http://ldkadmin.viphk.ngrok.org/f/interface/wemall/user/updatePWByOldPW
	 *	参数：oldPassword=旧密码，
	 *		newPassword=新密码，
	 *
	 * 	例：{
	 * 		oldPassword=123456,
	 *		newPassword=aaaaaa
	 * 		}
	 * 
	 * 结果示例：{"ret":"0","retMsg":"修改密码成功！","data":{}}
	 * 		或
	 * 		{"ret":"60015","retMsg":"用户尚未登陆！请先登录！","data":{}}
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = {"wemall/user/updatePWByOldPW"})
	@ResponseBody
	public String updatePWByOldPW(HttpServletRequest request, HttpServletResponse response, Model model) {
		String oldPassword = WebUtils.getCleanParam(request, "oldPassword");
		String newPassword = WebUtils.getCleanParam(request, "newPassword");
		return new Gson().toJson(formatReturnMsg(slHisUserFrontService.updatePWByOldPW(oldPassword, newPassword)));
	}
	
	/**
	 * 医生或患者更新用户基本信息的接口。
	 * 
	 * 测试用例（参数与患者注册接口基本一致）：
	 * 	url：http://ldkadmin.viphk.ngrok.org/f/interface/wemall/user/updateUserInfo
	 *	参数：type=doctor或者patient，
	 *		photo=头像
	 *		name=昵称
	 *		realName=姓名
	 *		sex=性别(0--女，1--男)
	 *
	 * 	例：{
	 * 		type=doctor,
	 * 		photo=http://ldkadmin.viphk.ngrok.org/userfiles/1/aa.jpg,
	 *		name=小李,
	 *		realName=李四,
	 *		sex=1
	 * 		}
	 * 
	 * 结果示例：{"ret":"0","retMsg":"修改用户信息成功！","data":{}}
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = {"wemall/user/updateUserInfo"})
	@ResponseBody
	public String updateUserInfo(HttpServletRequest request, HttpServletResponse response, Model model) {
		String type = WebUtils.getCleanParam(request, "type");
		return new Gson().toJson(formatReturnMsg(slHisUserFrontService.updateUserInfo(request, type)));
	}
	
	/**
	 * 医生或患者更新身份证信息的接口
	 * 
	 * 测试用例（参数与患者注册接口基本一致）：
	 * 	url：http://ldkadmin.viphk.ngrok.org/f/interface/wemall/user/updateIdCard
	 *	参数：type=doctor或者patient，
	 *		idCard=身份证
	 *
	 * 	例：{
	 * 		type=doctor,
	 *		idCard=410102199401080079
	 * 		}
	 * 
	 * 结果示例：{"ret":"0","retMsg":"修改身份证信息成功！","data":{}}
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = {"wemall/user/updateIdCard"})
	@ResponseBody
	public String updateIdCard(HttpServletRequest request, HttpServletResponse response, Model model) {
		String type = WebUtils.getCleanParam(request, "type");
		return new Gson().toJson(formatReturnMsg(slHisUserFrontService.updateIdCard(request, type)));
	}
	
	/**
	 * 获取当前系统用户的接口
	 * 
	 * 测试用例（参数与患者注册接口基本一致）：
	 * 	url：http://ldkadmin.viphk.ngrok.org/f/interface/wemall/user/getCurrentUser
	 *	参数：无
	 *
	 * 	例：
	 *
	 * 结果示例：{"ret":"0","retMsg":""获取用户成功！","data":{"user":{}}}
	 * 		或
	 * 		{"ret":"60015","retMsg":"用户尚未登陆！请先登录！","data":{}}
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = {"wemall/user/getCurrentUser"})
	@ResponseBody
	public String getCurrentUser(HttpServletRequest request, HttpServletResponse response, Model model) {
		return new Gson().toJson(formatReturnMsg(slHisUserFrontService.getCurrentUser()));
	}
	
	/**
	 * 验证当前用户手机号的接口。
	 * 
	 * 测试用例：
	 * 	url：http://ldkadmin.viphk.ngrok.org/f/interface/wemall/user/checkCurUserMobile
	 *	参数：mobile=手机号，
	 *		verifyServID=手机号对应的验证码ID，
	 *		sms_code=手机号对应的验证码值，
	 *
	 * 	例：{
	 * 		mobile=13838548839,
	 *		verifyServID=c33470c2d5bd49b99dc13c7146a6b0d4,
	 *		sms_code=2403,
	 * 		}
	 * 
	 * 结果示例：{"ret":"0","retMsg":"验证成功！","data":{}}
	 * 		或
	 * 		{"ret":"60002","retMsg":"短信验证码错误！请重试！","data":{}}
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = {"wemall/user/checkCurUserMobile"})
	@ResponseBody
	public String checkCurUserMobile(HttpServletRequest request, HttpServletResponse response, Model model) {
		return new Gson().toJson(formatReturnMsg(slHisUserFrontService.checkCurUserMobile(request)));
	}
	
	/**
	 * 验证手机号的接口。
	 * 
	 * 测试用例：
	 * 	url：http://ldkadmin.viphk.ngrok.org/f/interface/wemall/user/checkMobile
	 *	参数：mobile=手机号，
	 *		verifyServID=手机号对应的验证码ID，
	 *		sms_code=手机号对应的验证码值，
	 *
	 * 	例：{
	 * 		mobile=13838548839,
	 *		verifyServID=c33470c2d5bd49b99dc13c7146a6b0d4,
	 *		sms_code=2403,
	 * 		}
	 * 
	 * 结果示例：{"ret":"0","retMsg":"验证成功！","data":{}}
	 * 		或
	 * 		{"ret":"60002","retMsg":"短信验证码错误！请重试！","data":{}}
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = {"wemall/user/checkMobile"})
	@ResponseBody
	public String checkMobile(HttpServletRequest request, HttpServletResponse response, Model model) {
		return new Gson().toJson(formatReturnMsg(slHisUserFrontService.checkMobile(request)));
	}
}

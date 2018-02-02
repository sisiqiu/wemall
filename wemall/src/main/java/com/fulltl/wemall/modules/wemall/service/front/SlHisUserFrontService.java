package com.fulltl.wemall.modules.wemall.service.front;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fulltl.wemall.common.service.BaseService;
import com.fulltl.wemall.common.sms.sendmode.ccp.SMSVerify;
import com.fulltl.wemall.common.utils.CacheUtils;
import com.fulltl.wemall.common.utils.IdGen;
import com.fulltl.wemall.common.utils.RegExpValidatorUtil;
import com.fulltl.wemall.common.utils.StringUtils;
import com.fulltl.wemall.modules.sys.entity.User;
import com.fulltl.wemall.modules.sys.security.UsernamePasswordToken;
import com.fulltl.wemall.modules.sys.service.SystemService;
import com.fulltl.wemall.modules.sys.utils.DictUtils;
import com.fulltl.wemall.modules.sys.utils.UserUtils;
import com.google.common.base.Objects;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.Gson;

/**
 * 用户管理前端服务层
 * @author ldk
 *
 */
@Service
@Transactional(readOnly = true)
public class SlHisUserFrontService extends BaseService {

	@Autowired
	private SystemService systemService;
	
	/**
	 * 验证用户名是否已被使用。
	 * @param loginName
	 * @return
	 */
	public Map<String, Object> checkLoginName(String loginName) {
		Map<String, Object> retMap = Maps.newHashMap();
		User user = systemService.getUserByLoginName(loginName);
		if(user != null) {
			retMap.put("ret", "60001");
			retMap.put("retMsg", "该用户名已被使用！");
		} else {
			retMap.put("ret", "0");
			retMap.put("retMsg", "验证成功，该用户名可以使用！");
		}
		return retMap;
	}
	
	/**
	 * 向手机发送验证码
	 * @param mobile
	 * @return
	 */
	public Map<String, Object> getSMSCode(String mobile) {
		Map<String, Object> retMap = new HashMap<String, Object>();

		if(!RegExpValidatorUtil.isMobile(mobile)) {
			retMap.put("ret", "60013");
            retMap.put("retMsg", "手机号格式错误！");
            return retMap;
		}
        // 短信发送
        SMSVerify sms = new SMSVerify();
        String[] contentArray = { "[VerifyCode]", "5分钟" }; // 定义5分钟内有效

        // 发送短信: TODO 应将该校验串缓存 10 分钟，以便与客户端的请求参数进行比较??
        // 如果是新注册用户，直接发送
        try {
            String mobileMessageServ = sms.sendSmsVerifyCode(mobile, 6, "204863", contentArray);
            String[] messageServ = mobileMessageServ.split("\\|");
            if (messageServ != null && messageServ[0].equals("0")) {
            	retMap.put("ret", "0");
            	retMap.put("retMsg", "验证码发送成功！");
                retMap.put("mobile", mobile);
                retMap.put("verifyID", messageServ[1]);// 服务器缓存的验证码id
            } else {
            	retMap.put("ret", "-1");
            	retMap.put("retMsg", new Gson().fromJson(messageServ[1], Map.class).get("statusMsg"));
            }
        } catch (Exception e) {
            logger.error("短信发送失败", e);
            retMap.put("ret", "60010");
            retMap.put("retMsg", "验证码发送失败！");
        }

        return retMap;
    }
	
	/**
	 * 根据类型，注册并获取结果map
	 * @param type
	 * @param request
	 * @return
	 */
	@Transactional(readOnly = false)
	public Map<String, Object> registerAndGetResult(String type, HttpServletRequest request) {
		Map<String, Object> retMap = Maps.newHashMap();
		String mobile = WebUtils.getCleanParam(request, "mobile");
		String verifyServID = WebUtils.getCleanParam(request, "verifyServID");
		String sms_code = WebUtils.getCleanParam(request, "sms_code");
		String password = WebUtils.getCleanParam(request, "password");
		boolean rememberMe = WebUtils.isTrue(request, "rememberMe");

		User oldUser = systemService.getUserByLoginName(mobile);
		if(oldUser != null) {
			retMap.put("ret", "60036");
			retMap.put("retMsg", "用户已存在，请勿重复注册！");
			return retMap;
		}
		
		//验证短信验证码
		SMSVerify sms = new SMSVerify();
        try {
        	if(SystemService.USERTYPE_DOCTOR.equals(type)) {
        		//如果是医生端，不进行验证码验证
        		retMap.put("ret", "0");
        		retMap.put("retMsg", "验证成功！");
        	} else {
        		retMap = systemService.checkVerifyCode(mobile, sms_code, verifyServID);
        	}
    		if(!"0".equals(retMap.get("ret"))) {
    			return retMap;
    		} else {
    			//验证成功
				logger.info(mobile + "的短信验证成功！");
				//短信验证成功，判断是医生类型还是患者类型，执行添加用户和医生（或患者）
		        if(SystemService.USERTYPE_PATIENT.equals(type)) {
		        	//retMap = systemService.registerForPatient(mobile, password);
				} else if(SystemService.USERTYPE_DOCTOR.equals(type)) {
					//retMap = systemService.registerForDoctor(mobile, password);
				}
				
				if("0".equals(retMap.get("ret"))) {
					//执行登陆
					User user = (User)retMap.get("user");
					retMap = systemService.loginByUser(user, false, true);
					retMap.put("user", user.getSmallUser());
				}
    		}
		} catch (Exception e) {
			//logger.error("用户注册时retMap中没有用户user，无法执行登陆！", e);
			logger.error("校验验证码出错，或用户注册登陆出错！", e);
		}
        
		return retMap;
	}
	
	/**
	 * 根据类型，登陆并获取结果Map。
	 * @param request
	 * @return
	 */
	@Transactional(readOnly = false)
	public Map<String, Object> loginAndGetResult(String type, HttpServletRequest request) {
		Map<String, Object> retMap = Maps.newHashMap();
		String loginName = WebUtils.getCleanParam(request, "loginName");
		String mobile = WebUtils.getCleanParam(request, "mobile");
		String loginFrom = WebUtils.getCleanParam(request, "loginFrom");
		String password = WebUtils.getCleanParam(request, "password");
		String verifyServID = WebUtils.getCleanParam(request, "verifyServID");
		String sms_code = WebUtils.getCleanParam(request, "sms_code");
		boolean rememberMe = WebUtils.isTrue(request, "rememberMe");
		
		User user = null;
		if ("user_login".equals(loginFrom)) {
			user = systemService.getUserByLoginName(loginName);
        } else if("mobile_login".equals(loginFrom)) {
        	user = systemService.getUserByLoginName(mobile);
        } else {
        	retMap.put("ret", "60003");
			retMap.put("retMsg", "登陆方式错误，请切换登陆方式");
			return retMap;
        }
		
		//验证是否存在用户
		if(user == null) {
			retMap.put("ret", "60004");
			retMap.put("retMsg", "用户名不存在, 请先注册");
			return retMap;
		}
		
		if("user_login".equals(loginFrom)) {
			//若是“user_login”登陆方式，验证用户名和密码
			UsernamePasswordToken userToken = new UsernamePasswordToken();
			userToken.setUsername(loginName);
			if(StringUtils.isBlank(password)) {
				retMap.put("ret", "60005");
				retMap.put("retMsg", "用户名或密码不正确，请重试");
				return retMap;
			} else {
				userToken.setPassword(password.toCharArray());
			}
            userToken.setRememberMe(rememberMe);
			//执行登陆。
			try {
				UserUtils.getSubject().login(userToken);
				systemService.addOrUpdateAppSession(retMap, user);
				retMap.put("ret", "0");
				retMap.put("retMsg", "登陆成功");
			} catch (UnknownAccountException uae) {
				logger.error("用户名不存在",uae);
				retMap.put("ret", "60004");
				retMap.put("retMsg", "用户名不存在，请先注册");
				return retMap;
			} catch (IncorrectCredentialsException ice) {
				logger.error("用户名密码不正确",ice);
				retMap.put("ret", "60005");
				retMap.put("retMsg", "用户名或密码不正确，请重试");
				return retMap;
			} catch (LockedAccountException lae) {
				logger.error("账户状态被锁",lae);
				retMap.put("ret", "60006");
				retMap.put("retMsg", "账户异常，请联系管理员");
				return retMap;
			} catch (ExcessiveAttemptsException eae) {
				logger.error("密码输入次数超出限制",eae);
				retMap.put("ret", "60007");
				retMap.put("retMsg", "账户异常，请联系管理员");
				return retMap;
			}
		} else if ("mobile_login".equals(loginFrom)) {
			//若是“mobile_login”登陆方式，验证短信验证码
			SMSVerify sms = new SMSVerify();
	        try {
	        	retMap = systemService.checkVerifyCode(mobile, sms_code, verifyServID);
	    		if(!"0".equals(retMap.get("ret"))) {
	    			return retMap;
	    		} else {
					retMap = systemService.loginByUser(user, rememberMe, true);
					//systemService.addOrUpdateAppSession(retMap, user);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		if("0".equals(retMap.get("ret"))) {
			//根据类型更新角色
			//systemService.updateUserRoleByType(user, type);
		}
		
		retMap.put("user", user.getSmallUser());
		return retMap;
	}
	
	/**
	 * 根据系统用户id，查询该用户对应的医生信息的接口。
	 * @param userId 系统用户id
	 * @return 医生对象，若没有则返回null
	 */
	public Map<String, Object> getDoctorInfo(String userId) {
		Map<String, Object> retMap = Maps.newHashMap();
		/*if(StringUtils.isBlank(userId)) {
			retMap.put("ret", "60018");
			retMap.put("retMsg", "用户id不能为空！");
			return retMap;
		}
		SlHisDoctor doctor = new SlHisDoctor();
		User user = systemService.getUser(userId);
		doctor.setUser(user);
		List<SlHisDoctor> doctorList = slHisDoctorService.findList(doctor);
		retMap.put("ret", "0");
		retMap.put("retMsg", "获取成功！");
		retMap.put("userInfo", user.getSmallUser());
		if(!doctorList.isEmpty()) {
			SlHisDoctor doctorInfo = null;
			try {
				doctorInfo = (SlHisDoctor) doctorList.get(0).clone();
			} catch (CloneNotSupportedException e) {
				e.printStackTrace();
			}
			doctorInfo.setUser(null);
			doctorInfo.setCreateBy(null);
			doctorInfo.setUpdateBy(null);
			//格式化职称信息
			doctorInfo.setTitle(DictUtils.getDictLabel(doctorInfo.getTitle(), "doctor_title", ""));
			retMap.put("doctorInfo", doctorInfo);
		}*/
		return retMap;
	}
	
	/**
	 * 根据旧手机号码，通过验证码，可修改为新手机号码的接口（同样要验证码校验）。
	 * step1:校验旧手机验证码的接口。并可设定新手机号的有效时长，返回校验成功与否。
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	public Map<String, Object> checkSMSCodeForOldMobile(HttpServletRequest request) {
		Map<String, Object> retMap = Maps.newHashMap();
		String oldMobile = WebUtils.getCleanParam(request, "oldMobile");
		String verifyServID = WebUtils.getCleanParam(request, "verifyServID");
		String sms_code = WebUtils.getCleanParam(request, "sms_code");
		
		//校验是否存在oldMobile对应的用户
		User user = systemService.getUserByLoginName(oldMobile);
		if(user == null) {
			retMap.put("ret", "60004");
			retMap.put("retMsg", "用户名不存在，请重新输入");
			return retMap;
		}
		
		//校验手机号oldMobile对应的验证码是否匹配
		retMap = systemService.checkVerifyCode(oldMobile, sms_code, verifyServID);
		if(!"0".equals(retMap.get("ret"))) return retMap;
		
		//若校验错误，返回校验结果；若校验成功，添加缓存"oldMobile_+旧手机号"，过期时间默认10分钟。
        // 将短信验证码写入到缓存中
		String updateMobileVerifyID = IdGen.uuid();
        CacheUtils.put("oldMobile_" + oldMobile, updateMobileVerifyID);
        retMap.put("ret", "0");
		retMap.put("retMsg", "验证成功！");
		retMap.put("updateMobileVerifyID", updateMobileVerifyID);
		logger.info(oldMobile + "用户开始修改手机号！");
		return retMap;
	}
	
	/**
	 * 根据旧手机号码，通过验证码，可修改为新手机号码的接口（同样要验证码校验）。
	 * step2:更新手机号码(需要验证可设定新手机号的有效时间是否已超过，验证新手机验证码是否匹配)
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@Transactional(readOnly = false)
	public Map<String, Object> updateMobile(HttpServletRequest request) {
		Map<String, Object> retMap = Maps.newHashMap();
		String oldMobile = WebUtils.getCleanParam(request, "oldMobile");
		String newMobile = WebUtils.getCleanParam(request, "newMobile");
		String verifyServID = WebUtils.getCleanParam(request, "verifyServID");
		String sms_code = WebUtils.getCleanParam(request, "sms_code");
		String updateMobileVerifyID = WebUtils.getCleanParam(request, "updateMobileVerifyID");
		
		//校验是否存在oldMobile对应的用户
		User user = systemService.getUserByLoginName(oldMobile);
		if(user == null) {
			retMap.put("ret", "60004");
			retMap.put("retMsg", "用户名不存在，请重新输入");
			return retMap;
		}
		
		//校验缓存"oldMobile_+旧手机号"是否存在（过期）。若不存在，返回结果值。
		Object canUpdateMobile = CacheUtils.get("oldMobile_" + oldMobile);
		if(canUpdateMobile == null) {
			retMap.put("ret", "60011");
			retMap.put("retMsg", "原手机验证已过期，请重新验证原手机！");
			return retMap;
		} else if(!updateMobileVerifyID.equals(canUpdateMobile.toString())) {
			retMap.put("ret", "60012");
			retMap.put("retMsg", "原手机更新授权码错误，请重新验证原手机！");
			return retMap;
		}
		
		//验证新手机号是否存在用户
		User newMobileUser = systemService.getUserByLoginName(newMobile);
		if(newMobileUser != null && StringUtils.isNotBlank(newMobileUser.getId())) {
			retMap.put("ret", "60001");
			retMap.put("retMsg", "该手机用户名已被使用，请重新输入！");
			return retMap;
		}
		
		//校验手机号newMobile对应的验证码是否匹配
		retMap = systemService.checkVerifyCode(newMobile, sms_code, verifyServID);
		if(!"0".equals(retMap.get("ret"))) return retMap;
		
		//都校验通过，执行更新手机号
		user.setLoginName(newMobile);
		user.setMobile(newMobile);
		if(user.getName().equals(oldMobile)) user.setName(newMobile);
		systemService.updateMobileById(user, oldMobile);
		
		retMap.put("ret", "0");
		retMap.put("retMsg", "更新成功！");
		CacheUtils.remove("oldMobile_" + oldMobile);
		logger.info(oldMobile + "用户完成修改手机号！手机号由" + oldMobile + "修改为" + newMobile);
			
		return retMap;
	}
	
	/**
	 * 根据手机号，验证码，修改密码的接口。
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@Transactional(readOnly = false)
	public Map<String, Object> updatePWByMobile(HttpServletRequest request) {
		Map<String, Object> retMap = Maps.newHashMap();
		String mobile = WebUtils.getCleanParam(request, "mobile");
		String verifyServID = WebUtils.getCleanParam(request, "verifyServID");
		String sms_code = WebUtils.getCleanParam(request, "sms_code");
		String newPassword = WebUtils.getCleanParam(request, "newPassword");
		//校验是否存在mobile对应的用户
		User user = systemService.getUserByLoginName(mobile);
		if(user == null) {
			retMap.put("ret", "60004");
			retMap.put("retMsg", "用户名不存在，请重新输入");
			return retMap;
		}
		//校验短信验证码
		retMap = systemService.checkVerifyCode(mobile, sms_code, verifyServID);
		if(!"0".equals(retMap.get("ret"))) {
			return retMap;
		} else {
			//短信验证码校验成功，执行更新
			systemService.updatePasswordById(user.getId(), user.getLoginName(), newPassword);
			retMap.put("ret", "0");
			retMap.put("retMsg", "修改密码成功！");
			logger.info(user.getLoginName() + "用户修改密码成功！");
		}
		return retMap;
	}
	
	/**
	 * 用户登陆后，根据原密码，新密码，修改密码的接口。
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@Transactional(readOnly = false)
	public Map<String, Object> updatePWByOldPW(String oldPassword, String newPassword) {
		Map<String, Object> retMap = Maps.newHashMap();
		User user = UserUtils.getUser();
		//验证用户登陆
		retMap = systemService.checkCurrentUser(user);
		if(!"0".equals(retMap.get("ret"))) return retMap;
		
		if (StringUtils.isNotBlank(oldPassword) && StringUtils.isNotBlank(newPassword)){
			if (SystemService.validatePassword(oldPassword, user.getPassword())){
				systemService.updatePasswordById(user.getId(), user.getLoginName(), newPassword);
				retMap.put("ret", "0");
				retMap.put("retMsg", "修改密码成功！");
				logger.info(user.getLoginName() + "用户修改密码成功！");
			}else{
				retMap.put("ret", "60014");
				retMap.put("retMsg", "修改密码失败！旧密码错误！");
			}
		} else {
			retMap.put("ret", "60016");
			retMap.put("retMsg", "原密码和新密码不能为空！");
		}
		return retMap;
	}
	
	/**
	 * 用户基本信息修改的接口。
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@Transactional(readOnly = false)
	public Map<String, Object> updateUserInfo(HttpServletRequest request, String type) {
		Map<String, Object> retMap = Maps.newHashMap();
		String photo = WebUtils.getCleanParam(request, "photo");
		String name = WebUtils.getCleanParam(request, "name");
		String realName = WebUtils.getCleanParam(request, "realName");
		String sex = WebUtils.getCleanParam(request, "sex");
		//更新系统用户的基本信息
		User user = UserUtils.getUser();
		retMap = systemService.checkCurrentUser(user);
		if(!"0".equals(retMap.get("ret"))) return retMap;
		user.setPhoto(photo);
		user.setName(name);
		user.setRealName(realName);
		user.setSex(sex);
		if(StringUtils.isNotBlank(name) && (RegExpValidatorUtil.isSpecialChar(name) || name.length() > 20)) {
			retMap.put("ret", "60042");
			retMap.put("retMsg", "昵称不可含有特殊字符，且长度不应超过20个字符！");
			return retMap;
		}
		if(StringUtils.isNotBlank(realName) && (RegExpValidatorUtil.isSpecialChar(realName) || realName.length() > 20)) {
			retMap.put("ret", "60043");
			retMap.put("retMsg", "真实姓名不可含有特殊字符，且长度不应超过20个字符！");
			return retMap;
		}
		
		systemService.updateUserInfo(user);
		
		//根据用户类别，patient--患者,doctor--医生，更新患者信息或医生信息。
		if(SystemService.USERTYPE_DOCTOR.equals(type)) {
			//用户类别为：医生
			/*SlHisDoctor slHisDoctor = new SlHisDoctor();
			slHisDoctor.setPhoto(photo);
			slHisDoctor.setDoctorName(realName);
			slHisDoctor.setSex(sex);
			slHisDoctor.setUser(user);
			slHisDoctorService.updateInfoByUserId(slHisDoctor);*/
		} else if (SystemService.USERTYPE_PATIENT.equals(type)) {
			//用户类别为：患者
			/*SlHisPersoninfo slHisPersoninfo = new SlHisPersoninfo();
			slHisPersoninfo.setPname(realName);
			slHisPersoninfo.setPsex(sex);
			slHisPersoninfo.setUser(user);
			slHisPersoninfoService.updateInfoByUserId(slHisPersoninfo);*/
			//slHisPersoninfoService.save(slHisPersoninfo);
		} else {
			retMap.put("ret", "60017");
			retMap.put("retMsg", "用户类型输入错误！");
			return retMap;
		}
		retMap.put("ret", "0");
		retMap.put("retMsg", "修改用户信息成功！");
		logger.info(user.getLoginName() + "用户修改个人信息成功！");
		return retMap;
	}
	
	/**
	 * 用户基本信息修改的接口。
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@Transactional(readOnly = false)
	public Map<String, Object> updateUserPhoto(String photo, String type) {
		Map<String, Object> retMap = Maps.newHashMap();
		//更新系统用户的基本信息
		User user = UserUtils.getUser();
		retMap = systemService.checkCurrentUser(user);
		if(!"0".equals(retMap.get("ret"))) return retMap;
		user.setPhoto(photo);
		systemService.updateUserInfo(user);
		
		//根据用户类别，patient--患者,doctor--医生，更新患者信息或医生信息。
		if(SystemService.USERTYPE_DOCTOR.equals(type)) {
			//用户类别为：医生
			/*SlHisDoctor slHisDoctor = new SlHisDoctor();
			slHisDoctor.setPhoto(photo);
			slHisDoctor.setUser(user);
			slHisDoctorService.updateInfoByUserId(slHisDoctor);*/
		} else if (SystemService.USERTYPE_PATIENT.equals(type)) {
			//用户类别为：患者
			/*SlHisPersoninfo slHisPersoninfo = new SlHisPersoninfo();
			slHisPersoninfo.setUser(user);
			slHisPersoninfoService.updateInfoByUserId(slHisPersoninfo);*/
		} else {
			retMap.put("ret", "60017");
			retMap.put("retMsg", "用户类型输入错误！");
			return retMap;
		}
		retMap.put("ret", "0");
		retMap.put("retMsg", "修改用户信息成功！");
		logger.info(user.getLoginName() + "用户修改个人信息成功！");
		return retMap;
	}
	
	/**
	 * 用户身份证修改的接口。
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@Transactional(readOnly = false)
	public Map<String, Object> updateIdCard(HttpServletRequest request, String type) {
		Map<String, Object> retMap = Maps.newHashMap();
		String idCard = WebUtils.getCleanParam(request, "idCard");
		//获取系统当前用户
		User user = UserUtils.getUser();
		retMap = systemService.checkCurrentUser(user);
		if(!"0".equals(retMap.get("ret"))) return retMap;
		
		//验证身份证格式
		if(!RegExpValidatorUtil.isIDCard(idCard)) {
			retMap.put("ret", "60019");
    		retMap.put("retMsg", "身份证格式错误！");
    		return retMap;
		}
		
		//根据用户类别，patient--患者,doctor--医生，更新患者信息或医生的身份证信息。
		if(SystemService.USERTYPE_DOCTOR.equals(type)) {
			//用户类别为：医生
			/*SlHisDoctor slHisDoctor = new SlHisDoctor();
			slHisDoctor.setIdCard(idCard);
			slHisDoctor.setUser(user);
			slHisDoctorService.updateInfoByUserId(slHisDoctor);*/
		} else if (SystemService.USERTYPE_PATIENT.equals(type)) {
			//用户类别为：患者
			/*SlHisPersoninfo slHisPersoninfo = new SlHisPersoninfo();
			slHisPersoninfo.setPidCard(idCard);
			slHisPersoninfo.setUser(user);
			slHisPersoninfoService.updateInfoByUserId(slHisPersoninfo);*/
		}
		retMap.put("ret", "0");
		retMap.put("retMsg", "修改身份证信息成功！");
		logger.info(user.getLoginName() + "用户修改身份证成功！");
		return retMap;
	}
	
	/**
	 * 获取当前用户的接口
	 * @return
	 */
	public Map<String, Object> getCurrentUser() {
		Map<String, Object> retMap = Maps.newHashMap();
		User user = UserUtils.getUser();
		retMap = systemService.checkCurrentUser(user);
		if(!"0".equals(retMap.get("ret"))) return retMap;
		retMap.put("ret", "0");
		retMap.put("retMsg", "获取用户成功！");
		retMap.put("user", user.getSmallUser());
		return retMap;
	}

	/**
	 * 验证当前用户手机号的接口
	 * @param request
	 * @return
	 */
	public Map<String, Object> checkCurUserMobile(HttpServletRequest request) {
		Map<String, Object> retMap = Maps.newHashMap();
		String mobile = WebUtils.getCleanParam(request, "mobile");
		String verifyServID = WebUtils.getCleanParam(request, "verifyServID");
		String sms_code = WebUtils.getCleanParam(request, "sms_code");
		
		//校验是否存在oldMobile对应的用户
		User user = UserUtils.getUser();
		if(user == null) {
			retMap.put("ret", "60004");
			retMap.put("retMsg", "用户名不存在，请重新输入");
			return retMap;
		}
		if(!Objects.equal(user.getMobile(), mobile)) {
			retMap.put("ret", "60028");
			retMap.put("retMsg", "当前用户手机号错误，请重新登陆");
			return retMap;
		}
		
		//校验手机号oldMobile对应的验证码是否匹配
		retMap = systemService.checkVerifyCode(mobile, sms_code, verifyServID);
		if(!"0".equals(retMap.get("ret"))) return retMap;
		
		retMap.put("ret", "0");
		retMap.put("retMsg", "验证成功");
		return retMap;
	}
	
	/**
	 * 验证手机号的接口
	 * @param request
	 * @return
	 */
	public Map<String, Object> checkMobile(HttpServletRequest request) {
		Map<String, Object> retMap = Maps.newHashMap();
		String mobile = WebUtils.getCleanParam(request, "mobile");
		String verifyServID = WebUtils.getCleanParam(request, "verifyServID");
		String sms_code = WebUtils.getCleanParam(request, "sms_code");
		
		//校验手机号oldMobile对应的验证码是否匹配
		retMap = systemService.checkVerifyCode(mobile, sms_code, verifyServID);
		if(!"0".equals(retMap.get("ret"))) return retMap;
		
		retMap.put("ret", "0");
		retMap.put("retMsg", "验证成功");
		return retMap;
	}
}

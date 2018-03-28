/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.fulltl.wemall.modules.wx.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fulltl.wemall.common.persistence.Page;
import com.fulltl.wemall.common.service.CrudService;
import com.fulltl.wemall.common.utils.IdGen;
import com.fulltl.wemall.common.utils.StringUtils;
import com.fulltl.wemall.modules.sys.entity.User;
import com.fulltl.wemall.modules.sys.security.UsernamePasswordToken;
import com.fulltl.wemall.modules.sys.service.SystemService;
import com.fulltl.wemall.modules.sys.utils.UserUtils;
import com.fulltl.wemall.modules.wx.core.pojo.ReceiveXmlEntity;
import com.fulltl.wemall.modules.wx.dao.WxUserInfoDao;
import com.fulltl.wemall.modules.wx.entity.UserBehavior;
import com.fulltl.wemall.modules.wx.entity.WxServiceaccount;
import com.fulltl.wemall.modules.wx.entity.WxUserInfo;


/**
 * 微信用户信息管理Service
 * @author ldk
 * @version 2018-02-18
 */
@Service
@Transactional(readOnly = true)
public class WxUserInfoService extends CrudService<WxUserInfoDao, WxUserInfo> {

	@Autowired
	private SystemService systemService;
	@Autowired
	private WxServiceaccountService wxServiceaccountService;
	
	public WxUserInfo get(String id) {
		return super.get(id);
	}
	
	public List<WxUserInfo> findList(WxUserInfo wxUserInfo) {
		return super.findList(wxUserInfo);
	}
	
	public Page<WxUserInfo> findPage(Page<WxUserInfo> page, WxUserInfo wxUserInfo) {
		return super.findPage(page, wxUserInfo);
	}
	
	@Transactional(readOnly = false)
	public void save(WxUserInfo wxUserInfo) {
		if(wxUserInfo.getIsNewRecord()) {
			wxUserInfo.setId(IdGen.uuid());
			wxUserInfo.preInsert();
			dao.insert(wxUserInfo);
		} else {
			wxUserInfo.preUpdate();
			dao.update(wxUserInfo);
		}

	}
	
	
	@Transactional(readOnly = false)
	public void delete(WxUserInfo wxUserInfo) {
		super.delete(wxUserInfo);
	}

	/**
	 * 根据接收微信Xml对象获取关注用户对象。
	 * @param receiveXmlEntity
	 * @return
	 */
	@Transactional(readOnly = false)
	public WxUserInfo getWxUserInfoBy(ReceiveXmlEntity receiveXmlEntity) {
		String openId = receiveXmlEntity.getFromUserName();
		String serviceId = receiveXmlEntity.getToUserName();
		return this.getWxUserInfoBy(openId, serviceId);
	}
	
	/**
	 * 根据openId，serviceId获取关注用户对象。
	 * @param openId
	 * @param serviceId
	 * @return
	 */
	@Transactional(readOnly = false)
	public WxUserInfo getWxUserInfoBy(String openId, String serviceId) {
		//查询关注用户表中是否有该用户
		WxUserInfo curWxUserInfo = dao.findByOpenId(openId);
		if(curWxUserInfo == null) {
			//不存在该对象时，则new一个该对象，填充好ServiceId，openId字段。
			curWxUserInfo = new WxUserInfo();
			curWxUserInfo.setServiceId(serviceId);
			curWxUserInfo.setOpenId(openId);
		}
		return curWxUserInfo;
	}

	/**
	 * 更新微信用户对应的 关注表和绑定表。
	 * @param userBehavior 用户行为
	 * @param wxUserInfo 用户关注对象（主要用户信息相关字段都已经设置完毕，这些字段将更新到用户绑定表）；
	 * 						不存在该对象时，则new一个该对象，填充好ServiceId，openId字段。
	 * @param user 可为null，绑定行为所需传参，表示绑定的系统用户。
	 */
	@Transactional(readOnly = false)
	public void updateWXUserInfoBy(UserBehavior userBehavior, WxUserInfo wxUserInfo) {
		//设定系统管理员，默认用户是系统管理员添加的
		User administrator = new User();
		administrator.setId("1");
		WxUserInfo curWxUserInfo = this.getWxUserInfoBy(wxUserInfo.getOpenId(), wxUserInfo.getServiceId());
		if(curWxUserInfo != null && StringUtils.isNotBlank(curWxUserInfo.getId())) {
			//不是新增
			switch(userBehavior) {
			case FOCUS_OUT:
				if(!Objects.equals(curWxUserInfo.getIsFocus(), 0)) {
					curWxUserInfo.setIsFocus(0);
					dao.updateInfoByOpenId(curWxUserInfo);
				}
				break;
			case FOCUS_ON:
				if(!Objects.equals(curWxUserInfo.getIsFocus(),1)) {
					curWxUserInfo.setIsFocus(1);
					dao.updateInfoByOpenId(curWxUserInfo);
				}
				break;
			case BIND:
				if(!Objects.equals(curWxUserInfo.getIsBindMobile(),1)) {
					curWxUserInfo.setIsBindMobile(1);
					dao.updateInfoByOpenId(curWxUserInfo);
				}
				break;
			}
		} else {
			/**
			 * 说明当前不存在该关注用户。添加该用户到关注用户表
			 */
			//取微信服务号
			WxServiceaccount wxServiceaccount = wxServiceaccountService.findByServiceId(wxUserInfo.getServiceId());
			if(wxServiceaccount == null) {
				//说明系统中没有该服务号。
				System.err.println("系统中暂无该服务号！");
				return;
			}
			
			wxUserInfo.setServiceId(wxServiceaccount.getServiceId());
			wxUserInfo.initUserInfo();
			wxUserInfo.setCreateBy(administrator);
			wxUserInfo.setCreateDate(new Date());
			wxUserInfo.setUpdateBy(administrator);
			wxUserInfo.setUpdateDate(new Date());
			
			//执行添加系统用户对象
			String password = IdGen.randomBase62(8);
			//添加
			User user = systemService.saveUserByTypeAndLoginName(curWxUserInfo.getOpenId(), curWxUserInfo.getOpenId(), password);
			wxUserInfo.setUser(user);
			
			switch(userBehavior) {
			case FOCUS_OUT:
				wxUserInfo.setIsFocus(0);
				this.save(wxUserInfo);
				break;
			case FOCUS_ON:
				wxUserInfo.setIsFocus(1);
				this.save(wxUserInfo);
				break;
			case BIND:
				wxUserInfo.setIsBindMobile(1);
				this.save(wxUserInfo);
				break;
			}
		}
	}

	@Transactional(readOnly = false)
	public void updateInfoAndLoginByOpenId(WxUserInfo curWxUserInfo) {
		if(StringUtils.isNotBlank(curWxUserInfo.getId())) {
			//更新，并登陆用户
			if(!Objects.equals(curWxUserInfo.getIsGetUserInfo(),1)) {
				//如果不相等，则需要更新
				curWxUserInfo.setIsGetUserInfo(1);
				dao.updateInfoByOpenId(curWxUserInfo);
			}
			User user = systemService.getUserByLoginName(curWxUserInfo.getOpenId());
			if(StringUtils.isNotBlank(curWxUserInfo.getNickName())) {
				//更新系统用户的属性
				if(!Objects.equals(user.getName(), curWxUserInfo.getNickName())) {
					user.setName(curWxUserInfo.getNickName());
					systemService.updateUserInfo(user);
				}
			}
			systemService.loginByUser(user, false);
			System.err.println(user.getLoginName() + "用户登录！");
			
		} else {
			String password = IdGen.randomBase62(8);
			//添加
			User user = systemService.saveUserByTypeAndLoginName(curWxUserInfo.getOpenId(), curWxUserInfo.getOpenId(), password);
            // 自动登录
			systemService.loginByUser(user, false);

            System.err.println(user.getLoginName() + "用户登录！");
            //执行添加微信用户对象
            curWxUserInfo.initUserInfo();
            curWxUserInfo.setUser(user);
            this.save(curWxUserInfo);
		}
	}

	/**
	 * 根据userId查询用户，查询是否存在用户，存在即登陆。
	 * @param openId
	 * @return 登陆结果，含key---ret,retMsg
	 */
	@Transactional(readOnly = false)
	public Map<String, Object> loginByOpenId(String openId) {
		Map<String, Object> retMap = new HashMap<String, Object>();
		if(StringUtils.isEmpty(openId)) {
			retMap.put("ret", "434");
			retMap.put("retMsg", "openId不能为空！");
			return retMap;
		}
		WxUserInfo wxUserInfo = dao.findByOpenId(openId);
		UsernamePasswordToken userToken = new UsernamePasswordToken();
		if(wxUserInfo != null) {
			//执行登陆
			String mobile = wxUserInfo.getMobile();
			User user = systemService.quickGetUserByMobileForWX(mobile);
            // 自动登录
            userToken.setUsername(user.getMobile());
            userToken.setPassword("123456".toCharArray());
            // 短信验证码登陆，跳过密码对比校验
            userToken.setLogin_type("SMSCode");
            UserUtils.getSubject().login(userToken);
            retMap.put("ret", "200");
            retMap.put("retMsg", "登陆成功！");
            return retMap;
		} else {
			retMap.put("ret", "435");
            retMap.put("retMsg", "不存在该用户！");
            return retMap;
		}
	}

	/**
	 * 根据用户id获取微信用户
	 * @param userId
	 * @return
	 */
	public WxUserInfo getByUserId(String userId) {
		return dao.getByUserId(userId);
	}
	
}
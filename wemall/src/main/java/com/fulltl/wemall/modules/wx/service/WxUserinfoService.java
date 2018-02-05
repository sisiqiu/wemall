/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.fulltl.wemall.modules.wx.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fulltl.wemall.common.persistence.Page;
import com.fulltl.wemall.common.service.CrudService;
import com.fulltl.wemall.common.utils.IdGen;
import com.fulltl.wemall.modules.sys.entity.User;
import com.fulltl.wemall.modules.sys.security.UsernamePasswordToken;
import com.fulltl.wemall.modules.sys.service.SystemService;
import com.fulltl.wemall.modules.sys.utils.UserUtils;
import com.fulltl.wemall.modules.wx.dao.WxUserinfoDao;
import com.fulltl.wemall.modules.wx.entity.WxUserinfo;

/**
 * 微信绑定用户管理Service
 * @author ldk
 * @version 2017-10-13
 */
@Service
@Transactional(readOnly = true)
public class WxUserinfoService extends CrudService<WxUserinfoDao, WxUserinfo> {

	@Autowired
	private SystemService systemService;
	
	public WxUserinfo get(String id) {
		return super.get(id);
	}
	
	public List<WxUserinfo> findList(WxUserinfo wxUserinfo) {
		return super.findList(wxUserinfo);
	}
	
	public Page<WxUserinfo> findPage(Page<WxUserinfo> page, WxUserinfo wxUserinfo) {
		return super.findPage(page, wxUserinfo);
	}
	
	@Transactional(readOnly = false)
	public void save(WxUserinfo wxUserinfo) {
		if(wxUserinfo.getIsNewRecord()) {
			wxUserinfo.setWxUserId(IdGen.uuid());
			wxUserinfo.preInsert();
			dao.insert(wxUserinfo);
		} else {
			wxUserinfo.preUpdate();
			dao.update(wxUserinfo);
		}
	}
	
	@Transactional(readOnly = false)
	public void delete(WxUserinfo wxUserinfo) {
		super.delete(wxUserinfo);
	}

	public WxUserinfo findBySubscriberId(Integer subscriberId) {
		return dao.findBySubscriberId(subscriberId);
	}
	
	/**
	 * 根据关注用户id 或 系统用户id判断用户是否绑定
	 * @param subscriberId 关注用户id
	 * @return
	 */
	public boolean isAlreadyBind(int subscriberId) {
		WxUserinfo wxUserInfo = dao.findBySubscriberId(subscriberId);
		return wxUserInfo == null ? false : true;
	}
	
	/**
	 * 根据系统用户id判断用户是否绑定
	 * @param sysUserId 系统用户id
	 * @return
	 */
	public boolean isAlreadyBind(String sysUserId) {
		WxUserinfo wxUserInfo = dao.findBySysUserId(sysUserId);
		return wxUserInfo == null ? false : true;
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
		WxUserinfo wxUserInfo = dao.findByOpenId(openId);
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

	@Transactional(readOnly = false)
	public void updateByOpenId(WxUserinfo wxUserInfo) {
		wxUserInfo.preUpdate();
		dao.updateByOpenId(wxUserInfo);
	}

	public WxUserinfo findByOpenId(String openId) {
		return dao.findByOpenId(openId);
	}

}
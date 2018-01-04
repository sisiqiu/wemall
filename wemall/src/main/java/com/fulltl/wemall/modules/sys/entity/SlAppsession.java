/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.fulltl.wemall.modules.sys.entity;

import org.hibernate.validator.constraints.Length;
import com.fulltl.wemall.modules.sys.entity.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fulltl.wemall.common.persistence.DataEntity;
import com.fulltl.wemall.common.utils.StringUtils;

/**
 * appsession管理Entity
 * @author kangyang
 * @version 2017-12-06
 */
public class SlAppsession extends DataEntity<SlAppsession> {
	
	private static final long serialVersionUID = 1L;
	private String sid;		// sessionID
	private User user;		// 用户ID
	private String loginName;		// 登陆名
	private String imei;		// 设备唯一号
	private String useragent;		// 访问端信息
	private String location;		// 位置IP
	private String accessToken;		// 密钥
	private Long accessTokenExpiry;		// 密钥过期时间
	private String pid;		// 健康档案ID
	
	private String oldSid;  // 原sessionID
	
	public SlAppsession() {
		super();
	}

	public SlAppsession(String id){
		super(id);
	}
	
	public SlAppsession(User user){
		super();
		this.user = user;
	}

	@Length(min=1, max=100, message="sessionID长度必须介于 1 和 100 之间")
	public String getSid() {
		return sid;
	}

	public void setSid(String sid) {
		this.sid = sid;
	}
	
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	@Length(min=0, max=100, message="登陆名长度必须介于 0 和 100 之间")
	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}
	
	@Length(min=0, max=50, message="设备唯一号长度必须介于 0 和 50 之间")
	public String getImei() {
		return imei;
	}

	public void setImei(String imei) {
		this.imei = imei;
	}
	
	@Length(min=0, max=120, message="访问端信息长度必须介于 0 和 120 之间")
	public String getUseragent() {
		return useragent;
	}

	public void setUseragent(String useragent) {
		this.useragent = useragent;
	}
	
	@Length(min=0, max=100, message="位置IP长度必须介于 0 和 100 之间")
	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}
	
	@Length(min=0, max=100, message="密钥长度必须介于 0 和 100 之间")
	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
	
	public Long getAccessTokenExpiry() {
		return accessTokenExpiry;
	}

	public void setAccessTokenExpiry(Long accessTokenExpiry) {
		this.accessTokenExpiry = accessTokenExpiry;
	}
	
	@Length(min=0, max=50, message="健康档案ID长度必须介于 0 和 50 之间")
	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}
	
	@JsonIgnore
	public String getOldSid() {
		return oldSid;
	}

	public void setOldSid(String oldSid) {
		this.oldSid = oldSid;
	}

	@Override
	public boolean getIsNewRecord() {
		return isNewRecord;
	}
}
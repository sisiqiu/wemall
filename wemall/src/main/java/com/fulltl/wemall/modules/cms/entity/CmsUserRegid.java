/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.fulltl.wemall.modules.cms.entity;

import java.util.Set;

import org.hibernate.validator.constraints.Length;

import com.fulltl.wemall.common.persistence.DataEntity;

/**
 * 用户-极光注册id管理Entity
 * @author ldk
 * @version 2017-12-09
 */
public class CmsUserRegid extends DataEntity<CmsUserRegid> {
	
	private static final long serialVersionUID = 1L;
	private String registrationId;		// 极光注册id
	private String userId;		// 用户id
	private String status;		// 状态
	
	private Set<String> userIdList; //用户id列表
	
	public CmsUserRegid() {
		super();
	}

	public CmsUserRegid(String id){
		super(id);
	}

	@Length(min=1, max=64, message="极光注册id长度必须介于 1 和 64 之间")
	public String getRegistrationId() {
		return registrationId;
	}

	public void setRegistrationId(String registrationId) {
		this.registrationId = registrationId;
	}
	
	@Length(min=1, max=64, message="用户id长度必须介于 1 和 64 之间")
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	@Length(min=1, max=1, message="状态长度必须介于 1 和 1 之间")
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Set<String> getUserIdList() {
		return userIdList;
	}

	public void setUserIdList(Set<String> userIdList) {
		this.userIdList = userIdList;
	}
	
}
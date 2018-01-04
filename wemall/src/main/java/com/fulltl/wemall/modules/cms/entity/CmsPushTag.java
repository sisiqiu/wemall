/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.fulltl.wemall.modules.cms.entity;

import java.util.List;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fulltl.wemall.common.persistence.DataEntity;

/**
 * 标签管理Entity
 * @author ldk
 * @version 2017-12-09
 */
public class CmsPushTag extends DataEntity<CmsPushTag> {
	
	private static final long serialVersionUID = 1L;
	private String name;		// 标签名
	private String status;		// 状态
	private String userIds;		// 用户id列表
	private String userNames;	// 用户名称列表
	
	private List<String> idList;	//id列表
	
	public CmsPushTag() {
		super();
	}

	public CmsPushTag(String id){
		super(id);
	}

	@Length(min=1, max=64, message="标签名长度必须介于 1 和 64 之间")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@Length(min=1, max=1, message="状态长度必须介于 1 和 1 之间")
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getUserIds() {
		return userIds;
	}

	public void setUserIds(String userIds) {
		this.userIds = userIds;
	}

	public String getUserNames() {
		return userNames;
	}

	public void setUserNames(String userNames) {
		this.userNames = userNames;
	}

	@JsonIgnore
	public List<String> getIdList() {
		return idList;
	}

	public void setIdList(List<String> idList) {
		this.idList = idList;
	}
	
}
/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.fulltl.wemall.modules.cms.entity;

import org.hibernate.validator.constraints.Length;

import com.fulltl.wemall.common.persistence.DataEntity;
import com.fulltl.wemall.modules.sys.entity.User;

import javax.validation.constraints.NotNull;

/**
 * 管理用户反馈信息Entity
 * @author hj
 * @version 2017-11-02
 */
public class CmsFeedback extends DataEntity<CmsFeedback> {
	
	private static final long serialVersionUID = 1L;
	private String opinionText;		// 反馈内容
	private String telephone;		// 手机号码
	private String username;		// 用户的名称
	private User user;		// 用户表id
	
	public CmsFeedback() {
		super();
	}

	public CmsFeedback(String id){
		super(id);
	}

	@NotNull(message="反馈内容不能为空")
	@Length(min=1, max=2000, message="反馈内容长度必须介于 1 和 2000 之间")
	public String getOpinionText() {
		return opinionText;
	}

	public void setOpinionText(String opinionText) {
		this.opinionText = opinionText;
	}
	
	@NotNull(message="手机号码不能为空")
	@Length(min=1, max=20, message="手机号码长度必须介于 1 和 20 之间")
	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
	
	@Length(min=0, max=100, message="用户的名称长度必须介于 0 和 100 之间")
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
	@NotNull(message="用户表id不能为空")
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
}
/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.fulltl.wemall.modules.wemall.entity;

import com.fulltl.wemall.modules.sys.entity.User;
import javax.validation.constraints.NotNull;
import java.util.Date;

import com.fulltl.wemall.common.persistence.DataEntity;

/**
 * 签到管理Entity
 * @author ldk
 * @version 2018-01-05
 */
public class WemallSignIn extends DataEntity<WemallSignIn> {
	
	private static final long serialVersionUID = 1L;
	private User user;		// 用户id
	private Date beginCreateDate;		// 开始 创建时间
	private Date endCreateDate;		// 结束 创建时间
	
	public WemallSignIn() {
		super();
	}

	public WemallSignIn(String id){
		super(id);
	}

	@NotNull(message="用户id不能为空")
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	public Date getBeginCreateDate() {
		return beginCreateDate;
	}

	public void setBeginCreateDate(Date beginCreateDate) {
		this.beginCreateDate = beginCreateDate;
	}
	
	public Date getEndCreateDate() {
		return endCreateDate;
	}

	public void setEndCreateDate(Date endCreateDate) {
		this.endCreateDate = endCreateDate;
	}
		
}
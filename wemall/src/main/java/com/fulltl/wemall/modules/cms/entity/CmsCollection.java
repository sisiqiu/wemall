/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.fulltl.wemall.modules.cms.entity;


import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

import com.fulltl.wemall.common.persistence.DataEntity;
import com.fulltl.wemall.modules.sys.entity.User;


/**
 * 对用户所收藏的商品进行管理Entity
 * @author hj
 * @version 2017-11-15
 */
public class CmsCollection extends DataEntity<CmsCollection> {
	
	private static final long serialVersionUID = 1L;
	private User user;		// 用户id
	private String type;		// 类型
	private String category;		// 细分类别
	private String module;		// 对应模块
	private String msgid;		// 模块数据id
	private String status;		// 状态值
	
	public CmsCollection() {
		super();
	}

	public CmsCollection(String id){
		super(id);
	}

	@NotNull(message="用户id不能为空")
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	@Length(min=1, max=30, message="类型长度必须介于 1 和 30 之间")
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	@Length(min=0, max=50, message="细分类别长度必须介于 0 和 50 之间")
	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}
	
	@Length(min=1, max=30, message="对应模块长度必须介于 1 和 30 之间")
	public String getModule() {
		return module;
	}

	public void setModule(String module) {
		this.module = module;
	}
	
	@Length(min=1, max=64, message="模块数据id长度必须介于 1 和 64 之间")
	public String getMsgid() {
		return msgid;
	}

	public void setMsgid(String msgid) {
		this.msgid = msgid;
	}
	
	@Length(min=0, max=1, message="状态值长度必须介于 0 和 1 之间")
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
}
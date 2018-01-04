/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.fulltl.wemall.modules.wx.entity;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fulltl.wemall.common.persistence.TreeEntity;

/**
 * 微信服务号菜单管理Entity
 * @author ldk
 * @version 2017-10-13
 */
public class WxWechatMenu extends TreeEntity<WxWechatMenu> {
	
	private static final long serialVersionUID = 1L;
	private Integer saId;		// 服务号主键ID
	private String serviceId;		// 服务号ID
	private Integer sort;		// 顺序
	private String menuKey;		// 菜单key
	private String msgType;		// 菜单类别
	private String type;		// 类型
	private String name;		// 显示名称
	private WxWechatMenu parent;		// 父菜单ID
	private String parentIds;		// 所有父级编号
	private String status;		// 状态
	private String url;		// 菜单URL
	
	public WxWechatMenu() {
		super();
	}

	public WxWechatMenu(String id){
		super(id);
	}

	public Integer getSaId() {
		return saId;
	}

	public void setSaId(Integer saId) {
		this.saId = saId;
	}
	
	@Length(min=0, max=100, message="服务号ID长度必须介于 0 和 100 之间")
	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}
	
	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}
	
	@Length(min=0, max=255, message="菜单key长度必须介于 0 和 255 之间")
	public String getMenuKey() {
		return menuKey;
	}

	public void setMenuKey(String menuKey) {
		this.menuKey = menuKey;
	}
	
	@Length(min=0, max=255, message="菜单类别长度必须介于 0 和 255 之间")
	public String getMsgType() {
		return msgType;
	}

	public void setMsgType(String msgType) {
		this.msgType = msgType;
	}
	
	@Length(min=0, max=255, message="类型长度必须介于 0 和 255 之间")
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	@Length(min=0, max=255, message="显示名称长度必须介于 0 和 255 之间")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@JsonBackReference
	@NotNull(message="父级编号不能为空")
	public WxWechatMenu getParent() {
		return parent;
	}

	public void setParent(WxWechatMenu parent) {
		this.parent = parent;
	}
	
	@Length(min=1, max=2000, message="所有父级编号长度必须介于 1 和 2000 之间")
	public String getParentIds() {
		return parentIds;
	}

	public void setParentIds(String parentIds) {
		this.parentIds = parentIds;
	}
	
	@Length(min=0, max=10, message="状态长度必须介于 0 和 10 之间")
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	@Length(min=0, max=2000, message="菜单URL长度必须介于 0 和 2000 之间")
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	public String getParentId() {
		return parent != null && parent.getId() != null ? parent.getId() : "0";
	}
}
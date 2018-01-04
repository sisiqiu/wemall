/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.fulltl.wemall.modules.cms.entity;

import org.hibernate.validator.constraints.Length;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.fulltl.wemall.common.persistence.DataEntity;

/**
 * 极光推送历史记录管理Entity
 * @author ldk
 * @version 2017-12-09
 */
public class CmsJpushRecord extends DataEntity<CmsJpushRecord> {
	
	private static final long serialVersionUID = 1L;
	private String type;		// 推送类型(0--即时推送；1--定时推送)
	private Date scheduleTime;		// 定时推送时间
	private String targetType;		// 推送目标类型(1--推送所有人;2--通过角色推送;3--通过标签推送;4--通过用户列表推送)
	private String roleIds;		// 角色id列表
	private String roleNames;		// 角色名称列表
	private String tagIds;		// 标签id列表
	private String tagNames;		// 标签名称列表
	private String userIds;		// 用户id列表
	private String userNames;		// 用户名称列表
	private String title;		// 推送标题
	private String content;		// 推送内容
	private String href;		// 链接地址
	private String extra;		// 附加信息
	private String status;		// 推送状态(1--成功，2--失败)
	private Date beginScheduleTime;		// 开始 定时推送时间
	private Date endScheduleTime;		// 结束 定时推送时间
	private Date beginCreateDate;		// 开始 创建时间
	private Date endCreateDate;		// 结束 创建时间
	
	public CmsJpushRecord() {
		super();
	}

	public CmsJpushRecord(String id){
		super(id);
	}

	@Length(min=1, max=1, message="推送类型长度必须介于 1 和 1 之间")
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getScheduleTime() {
		return scheduleTime;
	}

	public void setScheduleTime(Date scheduleTime) {
		this.scheduleTime = scheduleTime;
	}
	
	@Length(min=1, max=1, message="推送目标类型长度必须介于 1 和 1 之间")
	public String getTargetType() {
		return targetType;
	}

	public void setTargetType(String targetType) {
		this.targetType = targetType;
	}
	
	@Length(min=0, max=500, message="角色id列表长度必须介于 0 和 500 之间")
	public String getRoleIds() {
		return roleIds;
	}

	public void setRoleIds(String roleIds) {
		this.roleIds = roleIds;
	}
	
	@Length(min=0, max=500, message="角色名称列表长度必须介于 0 和 500 之间")
	public String getRoleNames() {
		return roleNames;
	}

	public void setRoleNames(String roleNames) {
		this.roleNames = roleNames;
	}
	
	@Length(min=0, max=500, message="标签id列表长度必须介于 0 和 500 之间")
	public String getTagIds() {
		return tagIds;
	}

	public void setTagIds(String tagIds) {
		this.tagIds = tagIds;
	}
	
	@Length(min=0, max=500, message="标签名称列表长度必须介于 0 和 500 之间")
	public String getTagNames() {
		return tagNames;
	}

	public void setTagNames(String tagNames) {
		this.tagNames = tagNames;
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
	
	@Length(min=1, max=100, message="推送标题长度必须介于 1 和 100 之间")
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	@Length(min=1, max=500, message="推送内容长度必须介于 1 和 500 之间")
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	@Length(min=1, max=200, message="链接地址长度必须介于 1 和 200 之间")
	public String getHref() {
		return href;
	}

	public void setHref(String href) {
		this.href = href;
	}
	
	@Length(min=0, max=200, message="附加信息长度必须介于 0 和 200 之间")
	public String getExtra() {
		return extra;
	}

	public void setExtra(String extra) {
		this.extra = extra;
	}
	
	@Length(min=1, max=1, message="推送状态长度必须介于 1 和 1 之间")
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	public Date getBeginScheduleTime() {
		return beginScheduleTime;
	}

	public void setBeginScheduleTime(Date beginScheduleTime) {
		this.beginScheduleTime = beginScheduleTime;
	}
	
	public Date getEndScheduleTime() {
		return endScheduleTime;
	}

	public void setEndScheduleTime(Date endScheduleTime) {
		this.endScheduleTime = endScheduleTime;
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
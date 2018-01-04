/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.fulltl.wemall.modules.wx.entity;

import java.util.Date;
import java.util.List;

import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.google.gson.Gson;
import com.fulltl.wemall.common.persistence.DataEntity;

/**
 * 群发短信管理Entity
 * @author ldk
 * @version 2017-10-24
 */
public class ConGroupsms extends DataEntity<ConGroupsms> {
	
	private static final long serialVersionUID = 1L;
	private String templeteId;		// 短信模板ID
	private String contentArr;		// 短信内容数组
	private String mobileArr;		// 发送目标数组
	private Date sendTime;		// 发送时间
	private String sendType;		// 发送策略
	private String status;		// 状态
	private Date beginSendTime;		// 开始 发送时间
	private Date endSendTime;		// 结束 发送时间
	
	public ConGroupsms() {
		super();
	}

	public ConGroupsms(String id){
		super(id);
	}

	@Length(min=1, max=20, message="短信模板ID长度必须介于 1 和 20 之间")
	public String getTempleteId() {
		return templeteId;
	}

	public void setTempleteId(String templeteId) {
		this.templeteId = templeteId;
	}
	
	public String getContentArr() {
		return contentArr;
	}

	public void setContentArr(String contentArr) {
		this.contentArr = contentArr;
	}
	
	public String getMobileArr() {
		return mobileArr;
	}

	public void setMobileArr(String mobileArr) {
		this.mobileArr = mobileArr;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getSendTime() {
		return sendTime;
	}

	public void setSendTime(Date sendTime) {
		this.sendTime = sendTime;
	}
	
	@Length(min=1, max=1, message="发送策略长度必须介于 1 和 1 之间")
	public String getSendType() {
		return sendType;
	}

	public void setSendType(String sendType) {
		this.sendType = sendType;
	}
	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	public Date getBeginSendTime() {
		return beginSendTime;
	}

	public void setBeginSendTime(Date beginSendTime) {
		this.beginSendTime = beginSendTime;
	}
	
	public Date getEndSendTime() {
		return endSendTime;
	}

	public void setEndSendTime(Date endSendTime) {
		this.endSendTime = endSendTime;
	}
	
}
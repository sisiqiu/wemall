/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.fulltl.wemall.modules.wx.entity;

import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fulltl.wemall.common.persistence.DataEntity;
import com.fulltl.wemall.common.utils.excel.annotation.ExcelField;
import com.fulltl.wemall.modules.sys.entity.User;

/**
 * 用户活动表Entity
 * @author 黄健
 * @version 2017-10-14
 */
public class ConUserActivity extends DataEntity<ConUserActivity> {
	
	private static final long serialVersionUID = 1L;
	private User user;		// 用户ID
	private String userName;		// 用户名称
	private Integer activityid;		// 活动ID
	private String mobile;		// 联系方式
	private String information;		// 参会信息
	private String note;		// 备注
	private String status;		// 用户状态
	private String price;		// 奖项
	private Date registrationTime;		// 报名时间
	private Date attendanceTime;		// 签到时间
	private Date lotteryTime;		// 抽奖时间
	
	public ConUserActivity() {
		super();
	}

	public ConUserActivity(String id){
		super(id);
	}

	@NotNull(message="用户ID不能为空")
	@ExcelField(title="系统用户名", value="user.name", align=2, sort=0)
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	@Length(min=0, max=100, message="用户名称长度必须介于 0 和 100 之间")
	@ExcelField(title="用户名称", align=2, sort=10)
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	@NotNull(message="活动ID不能为空")
	@ExcelField(title="活动id", align=2, sort=20)
	public Integer getActivityid() {
		return activityid;
	}

	public void setActivityid(Integer activityid) {
		this.activityid = activityid;
	}
	
	@Length(min=0, max=20, message="联系方式长度必须介于 0 和 20 之间")
	@ExcelField(title="联系方式", align=2, sort=30)
	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	
	@Length(min=0, max=200, message="参会信息长度必须介于 0 和 200 之间")
	@ExcelField(title="参会信息", align=2, sort=40)
	public String getInformation() {
		return information;
	}

	public void setInformation(String information) {
		this.information = information;
	}
	
	@Length(min=0, max=200, message="备注长度必须介于 0 和 200 之间")
	@ExcelField(title="备注", align=2, sort=50)
	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}
	
	@Length(min=0, max=1, message="用户状态长度必须介于 0 和 1 之间")
	@ExcelField(title="用户状态", dictType="con_activity_user_status", align=2, sort=60)
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	@Length(min=0, max=40, message="奖项长度必须介于 0 和 40 之间")
	@ExcelField(title="奖项", align=2, sort=70)
	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="报名时间", align=2, sort=80)
	public Date getRegistrationTime() {
		return registrationTime;
	}

	public void setRegistrationTime(Date registrationTime) {
		this.registrationTime = registrationTime;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="签到时间", align=2, sort=90)
	public Date getAttendanceTime() {
		return attendanceTime;
	}

	public void setAttendanceTime(Date attendanceTime) {
		this.attendanceTime = attendanceTime;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="抽奖时间", align=2, sort=100)
	public Date getLotteryTime() {
		return lotteryTime;
	}

	public void setLotteryTime(Date lotteryTime) {
		this.lotteryTime = lotteryTime;
	}
	
}
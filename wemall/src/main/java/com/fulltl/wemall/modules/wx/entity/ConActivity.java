/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.fulltl.wemall.modules.wx.entity;

import org.hibernate.validator.constraints.Length;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.fulltl.wemall.common.persistence.DataEntity;

/**
 * 活动表Entity
 * @author 黄健
 * @version 2017-10-14
 */
public class ConActivity extends DataEntity<ConActivity> {
	
	private static final long serialVersionUID = 1L;
	private String title;		// 活动标题
	private String picurl;		// 活动图片
	private String organizer;		// 举办方
	private String location;		// 举办地点
	private String lng;		// 经度
	private String lat;		// 纬度
	private String content;		// 活动内容
	private Date registrationStarttime;		// 报名开始时间
	private Date registrationEndtime;		// 报名结束时间
	private Date attendanceStarttime;		// 签到开始时间
	private Date attendanceEndtime;		// 签到结束时间
	private Date lotteryStarttime;		// 抽奖开始时间
	private Date lotteryEndtime;		// 抽奖结束时间
	private Date fromdate;		// 活动开始时间
	private Date enddate;		// 活动结束时间
	private String resourcepath;		// 活动资源路径
	private String maxpeoplenum;		// 最大人数
	private String currentpeoplenum;		// 当前已报名人数
	private String status;		// 状态
	private String prize;		// 奖项json
	private String liveUrl;		// 直播间地址
	private Date beginFromdate;		// 开始 活动开始时间
	private Date endFromdate;		// 结束 活动开始时间
	private Date beginEnddate;		// 开始 活动结束时间
	private Date endEnddate;		// 结束 活动结束时间
	
	public ConActivity() {
		super();
	}

	public ConActivity(String id){
		super(id);
	}

	@Length(min=1, max=200, message="活动标题长度必须介于 1 和 200 之间")
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	@Length(min=1, max=200, message="活动图片长度必须介于 1 和 200 之间")
	public String getPicurl() {
		return picurl;
	}

	public void setPicurl(String picurl) {
		this.picurl = picurl;
	}
	
	@Length(min=1, max=100, message="举办方长度必须介于 1 和 100 之间")
	public String getOrganizer() {
		return organizer;
	}

	public void setOrganizer(String organizer) {
		this.organizer = organizer;
	}
	
	@Length(min=1, max=200, message="举办地点长度必须介于 1 和 200 之间")
	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}
	
	@Length(min=1, max=20, message="经度长度必须介于 1 和 20 之间")
	@Pattern(regexp="^(((\\d|[1-9]\\d|1[1-7]\\d|0)\\.\\d{0,8})|(\\d|[1-9]\\d|1[1-7]\\d|0{1,3})|180\\.0{0,4}|180)$", 
			message="经度整数部分为0-180小数部分为0到8位")
	public String getLng() {
		return lng;
	}

	public void setLng(String lng) {
		this.lng = lng;
	}
	
	@Length(min=1, max=20, message="纬度长度必须介于 1 和 20 之间")
	@Pattern(regexp="^([0-8]?\\d{1}\\.\\d{0,8}|90\\.0{0,4}|[0-8]?\\d{1}|90)$", 
			message="纬度整数部分为0-90小数部分为0到8位")
	public String getLat() {
		return lat;
	}

	public void setLat(String lat) {
		this.lat = lat;
	}
	
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getRegistrationStarttime() {
		return registrationStarttime;
	}

	public void setRegistrationStarttime(Date registrationStarttime) {
		this.registrationStarttime = registrationStarttime;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getRegistrationEndtime() {
		return registrationEndtime;
	}

	public void setRegistrationEndtime(Date registrationEndtime) {
		this.registrationEndtime = registrationEndtime;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getAttendanceStarttime() {
		return attendanceStarttime;
	}

	public void setAttendanceStarttime(Date attendanceStarttime) {
		this.attendanceStarttime = attendanceStarttime;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getAttendanceEndtime() {
		return attendanceEndtime;
	}

	public void setAttendanceEndtime(Date attendanceEndtime) {
		this.attendanceEndtime = attendanceEndtime;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getLotteryStarttime() {
		return lotteryStarttime;
	}

	public void setLotteryStarttime(Date lotteryStarttime) {
		this.lotteryStarttime = lotteryStarttime;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getLotteryEndtime() {
		return lotteryEndtime;
	}

	public void setLotteryEndtime(Date lotteryEndtime) {
		this.lotteryEndtime = lotteryEndtime;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@NotNull(message="活动开始时间不能为空")
	public Date getFromdate() {
		return fromdate;
	}

	public void setFromdate(Date fromdate) {
		this.fromdate = fromdate;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@NotNull(message="活动结束时间不能为空")
	public Date getEnddate() {
		return enddate;
	}

	public void setEnddate(Date enddate) {
		this.enddate = enddate;
	}
	
	@Length(min=0, max=1000, message="活动资源路径长度必须介于 0 和 1000 之间")
	public String getResourcepath() {
		return resourcepath;
	}

	public void setResourcepath(String resourcepath) {
		this.resourcepath = resourcepath;
	}
	
	@Length(min=1, max=6, message="最大人数必须在0~999999之间")
	@Pattern(regexp="(\\d*)", message="最大人数必须为数字")
	public String getMaxpeoplenum() {
		return maxpeoplenum;
	}

	public void setMaxpeoplenum(String maxpeoplenum) {
		this.maxpeoplenum = maxpeoplenum;
	}
	
	@Length(min=1, max=6, message="当前已报名人数必须在0~999999之间")
	@Pattern(regexp="(\\d*)", message="当前已报名人数必须为数字")
	public String getCurrentpeoplenum() {
		return currentpeoplenum;
	}

	public void setCurrentpeoplenum(String currentpeoplenum) {
		this.currentpeoplenum = currentpeoplenum;
	}
	
	@Length(min=1, max=1, message="状态长度必须介于 1 和 1 之间")
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	@Length(min=0, max=1000, message="奖项json长度必须介于 0 和 1000 之间")
	public String getPrize() {
		return prize;
	}

	public void setPrize(String prize) {
		this.prize = prize;
	}
	
	public Date getBeginFromdate() {
		return beginFromdate;
	}

	public void setBeginFromdate(Date beginFromdate) {
		this.beginFromdate = beginFromdate;
	}
	
	public Date getEndFromdate() {
		return endFromdate;
	}

	public void setEndFromdate(Date endFromdate) {
		this.endFromdate = endFromdate;
	}
		
	public Date getBeginEnddate() {
		return beginEnddate;
	}

	public void setBeginEnddate(Date beginEnddate) {
		this.beginEnddate = beginEnddate;
	}
	
	public Date getEndEnddate() {
		return endEnddate;
	}

	public void setEndEnddate(Date endEnddate) {
		this.endEnddate = endEnddate;
	}

	public String getLiveUrl() {
		return liveUrl;
	}

	public void setLiveUrl(String liveUrl) {
		this.liveUrl = liveUrl;
	}
	
}
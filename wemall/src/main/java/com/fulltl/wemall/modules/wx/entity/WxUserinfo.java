/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.fulltl.wemall.modules.wx.entity;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.Length;

import com.fulltl.wemall.common.persistence.DataEntity;
import com.fulltl.wemall.common.utils.excel.annotation.ExcelField;
import com.fulltl.wemall.modules.sys.entity.User;

/**
 * 微信绑定用户管理Entity
 * @author ldk
 * @version 2017-10-13
 */
public class WxUserinfo extends DataEntity<WxUserinfo> {
	
	private static final long serialVersionUID = 1L;
	private String wxUserId;		// 微信用户ID
	private Integer subscriberId;		// 绑定着ID
	private String wxNo;		// 微信号(服务号)
	private String mobile;		// 手机号
	private String username;		// 用户名
	private String nickname;		// 昵称
	private String sex;		// 性别
	private String country;		// 国家
	private String province;		// 省市
	private String city;		// 县
	private String language;		// 语言
	private User slUser;		// 用户ID
	private String status;		// 状态
	private String description;		// 描述
	private String photo;		// 头像
	private String openId;		// openID
	
	public WxUserinfo() {
		super();
	}

	public WxUserinfo(String id){
		super(id);
	}

	@Length(min=1, max=64, message="微信用户ID长度必须介于 1 和 64 之间")
	@ExcelField(title="微信绑定用户id", align=2, sort=0)
	public String getWxUserId() {
		return wxUserId;
	}

	public void setWxUserId(String wxUserId) {
		this.wxUserId = wxUserId;
	}
	
	@ExcelField(title="微信关注用户id", align=2, sort=10)
	public Integer getSubscriberId() {
		return subscriberId;
	}

	public void setSubscriberId(Integer subscriberId) {
		this.subscriberId = subscriberId;
	}
	
	@Length(min=0, max=50, message="微信号长度必须介于 0 和 50 之间")
	@ExcelField(title="微信服务号", align=2, sort=20)
	public String getWxNo() {
		return wxNo;
	}

	public void setWxNo(String wxNo) {
		this.wxNo = wxNo;
	}
	
	@Length(min=0, max=20, message="手机号长度必须介于 0 和 20 之间")
	@ExcelField(title="手机号", align=2, sort=30)
	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	
	@Length(min=0, max=50, message="用户名长度必须介于 0 和 50 之间")
	@ExcelField(title="用户名", align=2, sort=40)
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
	@Length(min=0, max=50, message="昵称长度必须介于 0 和 50 之间")
	@ExcelField(title="昵称", align=2, sort=50)
	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	
	@Length(min=0, max=1, message="性别长度必须介于 0 和 1 之间")
	@ExcelField(title="性别", dictType="sex", align=2, sort=60)
	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}
	
	@Length(min=0, max=50, message="国家长度必须介于 0 和 50 之间")
	@ExcelField(title="国家", align=2, sort=70)
	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}
	
	@Length(min=0, max=50, message="省市长度必须介于 0 和 50 之间")
	@ExcelField(title="省市", align=2, sort=80)
	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}
	
	@Length(min=0, max=50, message="县长度必须介于 0 和 50 之间")
	@ExcelField(title="区县", align=2, sort=90)
	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}
	
	@Length(min=0, max=20, message="语言长度必须介于 0 和 20 之间")
	@ExcelField(title="语言", align=2, sort=100)
	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}
	
	@ExcelField(title="对应系统用户id", value="slUser.id", align=2, sort=110)
	public User getSlUser() {
		return slUser;
	}

	public void setSlUser(User slUser) {
		this.slUser = slUser;
	}
	
	@Length(min=1, max=1, message="状态长度必须介于 1 和 1 之间")
	@ExcelField(title="状态", dictType="wx_subscriber_status", align=2, sort=120)
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	@Length(min=0, max=200, message="描述长度必须介于 0 和 200 之间")
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	@Length(min=0, max=150, message="头像长度必须介于 0 和 150 之间")
	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}
	
	@Length(min=0, max=50, message="openID长度必须介于 0 和 50 之间")
	@ExcelField(title="openId", align=2, sort=25)
	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}
	
	/**
	 * 是否是新记录（默认：false），调用setIsNewRecord()设置新记录，使用自定义ID。
	 * 设置为true后强制执行插入语句，ID不会自动生成，需从手动传入。
     * @return
     */
	@Override
	public boolean getIsNewRecord() {
        return isNewRecord || StringUtils.isBlank(getWxUserId());
    }
	
	/**
	 * 根据关注用户对象和系统用户对象，初始化绑定用户对象的相关信息。wxSebscriber为null时不做微信部分信息更新，user为null时不做系统用户部分信息更新。
	 * @param wxSebscriber
	 * @param user
	 */
	public void initBySubscriberAndUser(WxSubscriber wxSebscriber, User user) {
		//微信信息部分
		if(wxSebscriber != null) {
			this.wxNo = wxSebscriber.getServiceId();
			this.subscriberId = wxSebscriber.getSubscriberId();
			this.openId = wxSebscriber.getOpenId();
			this.nickname = wxSebscriber.getNickname();
			this.sex = wxSebscriber.getSex();
			this.language = wxSebscriber.getLanguage();
			this.city = wxSebscriber.getCity();
			this.province = wxSebscriber.getProvince();
			this.country = wxSebscriber.getCountry();
			this.photo = wxSebscriber.getHeadImgUrl();
			this.status = wxSebscriber.getStatus();
		}
		//系统用户信息部分
		if(user != null) {
			this.mobile = user.getMobile();
			this.username = user.getName();
			this.slUser = user;
		}
	}
	
	/**
	 * 根据关注用户对象，初始化绑定用户对象的相关信息。
	 * @param wxSebscriber
	 */
	public void initBySubscriber(WxSubscriber wxSebscriber) {
		//微信信息部分
		this.wxNo = wxSebscriber.getServiceId();
		this.subscriberId = wxSebscriber.getSubscriberId();
		this.openId = wxSebscriber.getOpenId();
		this.nickname = wxSebscriber.getNickname();
		this.sex = wxSebscriber.getSex();
		this.language = wxSebscriber.getLanguage();
		this.city = wxSebscriber.getCity();
		this.province = wxSebscriber.getProvince();
		this.country = wxSebscriber.getCountry();
		this.photo = wxSebscriber.getHeadImgUrl();
		this.status = wxSebscriber.getStatus();
		//系统用户信息部分
		this.mobile = wxSebscriber.getMobile();
		this.username = wxSebscriber.getUsername();
		User u = new User();
		u.setId(wxSebscriber.getSlUserId());
		this.slUser = u;
	}
	
	/**
	 * 判断当前微信绑定用户信息是否需要更新。wxSebscriber为null时不做微信部分信息判断，user为null时不做系统用户部分信息判断。
	 * @param wxSebscriber
	 * @param user
	 * @return true为需要更新；false为不需要更新
	 */
	public boolean isNeedUpdate(WxSubscriber wxSebscriber, User user) {
		if(	//微信信息部分
			(wxSebscriber == null || (this.wxNo.equals(wxSebscriber.getServiceId()) &&
								this.subscriberId.equals(wxSebscriber.getSubscriberId()) &&
								this.openId.equals(wxSebscriber.getOpenId()) &&
								this.nickname.equals(wxSebscriber.getNickname()) &&
								this.sex.equals(wxSebscriber.getSex()) &&
								this.language.equals(wxSebscriber.getLanguage()) &&
								this.city.equals(wxSebscriber.getCity()) &&
								this.province.equals(wxSebscriber.getProvince()) &&
								this.country.equals(wxSebscriber.getCountry()) &&
								this.photo.equals(wxSebscriber.getHeadImgUrl()) &&
								this.status.equals(wxSebscriber.getStatus()))) &&
			//系统用户信息部分
			(user == null || (this.mobile.equals(user.getMobile()) &&
								this.username.equals(user.getName()) &&
								this.slUser.equals(user)))
			) {
			//所有字段都是最新的，不需要更新
			return false;
		} 
		return true;
	}
}
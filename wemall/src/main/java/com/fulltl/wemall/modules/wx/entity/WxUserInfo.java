/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.fulltl.wemall.modules.wx.entity;

import com.fulltl.wemall.modules.sys.entity.User;
import com.fulltl.wemall.modules.wx.core.pojo.WXOAuthUserInfo;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.apache.commons.lang.StringUtils;
import org.hibernate.validator.constraints.Length;

import com.fulltl.wemall.common.persistence.DataEntity;
import com.fulltl.wemall.common.utils.RegExpValidatorUtil;

/**
 * 微信用户信息管理Entity
 * @author ldk
 * @version 2018-02-18
 */
public class WxUserInfo extends DataEntity<WxUserInfo> {
	
	private static final long serialVersionUID = 1L;
	private User user;		// 用户id
	private String serviceId;		// 服务号id
	private String openId;		// 微信openid
	private String userName;		// 用户名
	private String nickName;		// 昵称
	private String sex;		// 性别
	private String country;		// 国籍
	private String province;		// 省
	private String city;		// 区县
	private String language;		// 语言
	private String headImgUrl;		// 头像url
	private String mobile;		// 手机号
	private Integer isFocus;		// 是否关注
	private Integer isGetUserInfo;		// 是否已获取用户信息
	private Integer isChangePw;		// 是否已修改密码
	private Integer isBindMobile;		// 是否已绑定手机
	
	public WxUserInfo() {
		super();
	}

	public WxUserInfo(String id){
		super(id);
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	@Length(min=1, max=50, message="服务号id长度必须介于 1 和 50 之间")
	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}
	
	@Length(min=1, max=50, message="微信openid长度必须介于 1 和 50 之间")
	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}
	
	@Length(min=0, max=50, message="用户名长度必须介于 0 和 50 之间")
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	@Length(min=0, max=50, message="昵称长度必须介于 0 和 50 之间")
	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	
	@Length(min=0, max=1, message="性别长度必须介于 0 和 1 之间")
	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}
	
	@Length(min=0, max=50, message="国籍长度必须介于 0 和 50 之间")
	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}
	
	@Length(min=0, max=50, message="省长度必须介于 0 和 50 之间")
	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}
	
	@Length(min=0, max=50, message="区县长度必须介于 0 和 50 之间")
	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}
	
	@Length(min=0, max=20, message="语言长度必须介于 0 和 20 之间")
	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}
	
	@Length(min=0, max=150, message="头像url长度必须介于 0 和 150 之间")
	public String getHeadImgUrl() {
		return headImgUrl;
	}

	public void setHeadImgUrl(String headImgUrl) {
		this.headImgUrl = headImgUrl;
	}
	
	@Length(min=0, max=20, message="手机号长度必须介于 0 和 20 之间")
	@Pattern(regexp=RegExpValidatorUtil.REGEX_NULL_OR_MOBILE, message="手机号格式错误")
	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	
	@NotNull(message="是否关注不能为空")
	public Integer getIsFocus() {
		return isFocus;
	}

	public void setIsFocus(Integer isFocus) {
		this.isFocus = isFocus;
	}
	
	@NotNull(message="是否已获取用户信息不能为空")
	public Integer getIsGetUserInfo() {
		return isGetUserInfo;
	}

	public void setIsGetUserInfo(Integer isGetUserInfo) {
		this.isGetUserInfo = isGetUserInfo;
	}
	
	@NotNull(message="是否已修改密码不能为空")
	public Integer getIsChangePw() {
		return isChangePw;
	}

	public void setIsChangePw(Integer isChangePw) {
		this.isChangePw = isChangePw;
	}
	
	@NotNull(message="是否已绑定手机不能为空")
	public Integer getIsBindMobile() {
		return isBindMobile;
	}

	public void setIsBindMobile(Integer isBindMobile) {
		this.isBindMobile = isBindMobile;
	}
	
	public void initUserInfo() {
		this.isFocus = 0;		// 是否关注
		this.isGetUserInfo = 0;		// 是否已获取用户信息
		this.isChangePw = 0;		// 是否已修改密码
		this.isBindMobile = 0;		// 是否已绑定手机
	}

	/**
	 * 根据微信OAuth2获取的微信用户信息对象更新关注用户对象的相关字段信息。
	 * @param userInfo
	 */
	public void initByOAuthUserInfo(WXOAuthUserInfo userInfo) {
		this.openId = userInfo.getOpenid();
		this.nickName = userInfo.getNickname();
		this.sex = userInfo.getSex() == null ? null : userInfo.getSex().toString();
		this.language = userInfo.getLanguage();
		this.country = userInfo.getCountry();
		this.province = userInfo.getProvince();
		this.city = userInfo.getCity();
		this.headImgUrl = userInfo.getHeadimgurl();
	}
	
	public boolean isAlreadyGetUserInfo() {
		if(StringUtils.isNotBlank(this.nickName) ||
				StringUtils.isNotBlank(this.sex) ||
				StringUtils.isNotBlank(this.language) ||
				StringUtils.isNotBlank(this.country) ||
				StringUtils.isNotBlank(this.province) ||
				StringUtils.isNotBlank(this.city) ||
				StringUtils.isNotBlank(this.headImgUrl)
				) {
			return true;
		} 
		return false;
	}
}
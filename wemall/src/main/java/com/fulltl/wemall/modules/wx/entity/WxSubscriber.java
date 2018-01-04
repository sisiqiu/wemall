/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.fulltl.wemall.modules.wx.entity;

import java.util.Date;
import java.util.Objects;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fulltl.wemall.common.persistence.DataEntity;
import com.fulltl.wemall.common.utils.excel.annotation.ExcelField;
import com.fulltl.wemall.modules.sys.entity.User;
import com.fulltl.wemall.modules.wx.core.pojo.WXOAuthUserInfo;

/**
 * 微信关注用户管理Entity
 * @author ldk
 * @version 2017-10-13
 */
public class WxSubscriber extends DataEntity<WxSubscriber> {
	
	private static final long serialVersionUID = 1L;
	private Integer subscriberId;		// 绑定着ID
	private String isServicePerson;		// 是否服务人员
	private String serviceId;		// 服务号ID
	private String openId;		// openID
	private String username;		// 用户名
	private String nickname;		// 昵称
	private String sex;		// 性别
	private String country;		// 国籍
	private String province;		// 省
	private String city;		// 区县
	private String language;		// 语言
	private String headImgUrl;		// 头像url
	private String subscribe;		// 是否预约
	private Date subscribeTime;		// 预约时间
	private Date unsubscribeTime;		// 取消预约时间
	private String wxUserId;		// 微信用户ID
	private String slUserId;		// 用户ID
	private String status;		// 状态；0--未关注未绑定；1--未关注已绑定；2--已关注未绑定；3--已关注已绑定
	private String description;		// 描述
	private Integer saId;		// sa_id
	private String sellerCode;		// 销售者编码
	private String loginName;		// 登陆名
	private String mobile;		// 手机号
	
	public WxSubscriber() {
		super();
	}

	public WxSubscriber(String id){
		super(id);
	}

	@ExcelField(title="微信关注用户id", align=2, sort=0)
	public Integer getSubscriberId() {
		return subscriberId;
	}

	public void setSubscriberId(Integer subscriberId) {
		this.subscriberId = subscriberId;
	}
	
	@Length(min=0, max=1, message="是否服务人员长度必须介于 0 和 1 之间")
	public String getIsServicePerson() {
		return isServicePerson;
	}

	public void setIsServicePerson(String isServicePerson) {
		this.isServicePerson = isServicePerson;
	}
	
	@Length(min=1, max=30, message="服务号ID长度必须介于 1 和 30 之间")
	@ExcelField(title="对应服务号", align=2, sort=10)
	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}
	
	@Length(min=1, max=50, message="openID长度必须介于 1 和 50 之间")
	@ExcelField(title="openId", align=2, sort=20)
	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}
	
	@Length(min=0, max=50, message="用户名长度必须介于 0 和 50 之间")
	@ExcelField(title="用户名", align=2, sort=30)
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
	@Length(min=0, max=50, message="昵称长度必须介于 0 和 50 之间")
	@ExcelField(title="昵称", align=2, sort=40)
	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	
	@Length(min=0, max=1, message="性别长度必须介于 0 和 1 之间")
	@ExcelField(title="性别", dictType="sex", align=2, sort=50)
	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}
	
	@Length(min=0, max=50, message="国籍长度必须介于 0 和 50 之间")
	@ExcelField(title="国籍", align=2, sort=60)
	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}
	
	@Length(min=0, max=50, message="省长度必须介于 0 和 50 之间")
	@ExcelField(title="省会", align=2, sort=70)
	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}
	
	@Length(min=0, max=50, message="区县长度必须介于 0 和 50 之间")
	@ExcelField(title="区县", align=2, sort=80)
	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}
	
	@Length(min=0, max=20, message="语言长度必须介于 0 和 20 之间")
	@ExcelField(title="语言", align=2, sort=90)
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
	
	@Length(min=1, max=1, message="是否预约长度必须介于 1 和 1 之间")
	public String getSubscribe() {
		return subscribe;
	}

	public void setSubscribe(String subscribe) {
		this.subscribe = subscribe;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getSubscribeTime() {
		return subscribeTime;
	}

	public void setSubscribeTime(Date subscribeTime) {
		this.subscribeTime = subscribeTime;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getUnsubscribeTime() {
		return unsubscribeTime;
	}

	public void setUnsubscribeTime(Date unsubscribeTime) {
		this.unsubscribeTime = unsubscribeTime;
	}
	
	@Length(min=0, max=64, message="微信用户ID长度必须介于 0 和 64 之间")
	public String getWxUserId() {
		return wxUserId;
	}

	public void setWxUserId(String wxUserId) {
		this.wxUserId = wxUserId;
	}
	
	@Length(min=0, max=64, message="用户ID长度必须介于 0 和 64 之间")
	@ExcelField(title="对应系统用户id", align=2, sort=5)
	public String getSlUserId() {
		return slUserId;
	}

	public void setSlUserId(String slUserId) {
		this.slUserId = slUserId;
	}
	
	@Length(min=1, max=1, message="状态长度必须介于 1 和 1 之间")
	@ExcelField(title="状态", dictType="wx_subscriber_status", align=2, sort=100)
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
	
	public Integer getSaId() {
		return saId;
	}

	public void setSaId(Integer saId) {
		this.saId = saId;
	}
	
	@Length(min=0, max=10, message="销售者编码长度必须介于 0 和 10 之间")
	public String getSellerCode() {
		return sellerCode;
	}

	public void setSellerCode(String sellerCode) {
		this.sellerCode = sellerCode;
	}
	
	@Length(min=0, max=50, message="登陆名长度必须介于 0 和 50 之间")
	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}
	
	@Length(min=0, max=30, message="手机号长度必须介于 0 和 30 之间")
	@ExcelField(title="手机号", align=2, sort=55)
	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	
	/**
	 * 是否是新记录（默认：false），调用setIsNewRecord()设置新记录，使用自定义ID。
	 * 设置为true后强制执行插入语句，ID不会自动生成，需从手动传入。
     * @return
     */
	@Override
	public boolean getIsNewRecord() {
        return isNewRecord || getSubscriberId() == null || getSubscriberId().equals(0);
    }
	
	/**
	 * 根据系统用户进行初始化
	 * @param user
	 */
	public void initByUser(User user) {
		this.username = user.getName();
		this.mobile = user.getMobile();
		this.slUserId = user.getId();
	}

	/**
	 * 根据微信OAuth2获取的微信用户信息对象更新关注用户对象的相关字段信息。
	 * @param userInfo
	 */
	public void initByOAuthUserInfo(WXOAuthUserInfo userInfo) {
		this.openId = userInfo.getOpenid();
		this.nickname = userInfo.getNickname();
		this.sex = userInfo.getSex().toString();
		this.language = userInfo.getLanguage();
		this.country = userInfo.getCountry();
		this.province = userInfo.getProvince();
		this.city = userInfo.getCity();
		this.headImgUrl = userInfo.getHeadimgurl();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (null == obj) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        if (!getClass().equals(obj.getClass())) {
            return false;
        }
        WxSubscriber that = (WxSubscriber) obj;
        if(Objects.equals(this.subscriberId, that.subscriberId) &&
        		Objects.equals(this.isServicePerson, that.isServicePerson) &&
        		Objects.equals(this.serviceId, that.serviceId) &&
        		Objects.equals(this.openId, that.openId) &&
        		Objects.equals(this.username, that.username) &&
        		Objects.equals(this.nickname, that.nickname) &&
        		Objects.equals(this.sex, that.sex) &&
        		Objects.equals(this.country, that.country) &&
        		Objects.equals(this.province, that.province) &&
        		Objects.equals(this.city, that.city) &&
        		Objects.equals(this.language, that.language) &&
        		Objects.equals(this.headImgUrl, that.headImgUrl) &&
        		Objects.equals(this.subscribe, that.subscribe) &&
        		Objects.equals(this.subscribeTime, that.subscribeTime) &&
        		Objects.equals(this.unsubscribeTime, that.unsubscribeTime) &&
        		Objects.equals(this.wxUserId, that.wxUserId) &&
        		Objects.equals(this.slUserId, that.slUserId) &&
        		Objects.equals(this.status, that.status) &&
        		Objects.equals(this.description, that.description) &&
        		Objects.equals(this.saId, that.saId) &&
        		Objects.equals(this.sellerCode, that.sellerCode) &&
        		Objects.equals(this.loginName, that.loginName) &&
        		Objects.equals(this.mobile, that.mobile)) {
        	return true;
        }
        return false;
	}
}
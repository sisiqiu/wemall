/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.fulltl.wemall.modules.wx.entity;

import org.hibernate.validator.constraints.Length;

import com.fulltl.wemall.common.persistence.DataEntity;
import com.fulltl.wemall.common.utils.StringUtils;

/**
 * 微信服务号管理Entity
 * @author ldk
 * @version 2017-10-13
 */
public class WxServiceaccount extends DataEntity<WxServiceaccount> {
	
	private static final long serialVersionUID = 1L;
	private Integer saId;		// 自增ID
	private String serviceId;		// 服务号ID
	private String serviceNo;		// 服务号NO
	private String saName;		// 服务号名称
	private String serviceUrl;		// 服务号URL
	private String token;		// token
	private String appId;		// appID
	private String appSecret;		// app秘钥
	private String paysignKey;		// 支付签名key
	private String partnerId;		// partnerid
	private String partnerKey;		// partnerkey
	private String notifyUrl;		// 回调url
	private String redirectUri;		// redirect_uri
	private String authStatus;		// 认证状态
	private String accountType;		// 服务号类型
	private Integer companyId;		// 公司ID
	private String slAppcode;		// app识别码
	private String status;		// 状态
	private String description;		// 描述
	private String accessToken;		// tonken
	private Integer accessTokenExpiry;		// token过期时间
	private String servicePerson;		// 服务人员
	private Integer templateId;		// 回复模板ID
	
	public WxServiceaccount() {
		super();
	}

	public WxServiceaccount(String id){
		super(id);
	}

	public Integer getSaId() {
		return saId;
	}

	public void setSaId(Integer saId) {
		this.saId = saId;
	}
	
	@Length(min=1, max=50, message="服务号ID长度必须介于 1 和 50 之间")
	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}
	
	@Length(min=1, max=50, message="服务号NO长度必须介于 1 和 50 之间")
	public String getServiceNo() {
		return serviceNo;
	}

	public void setServiceNo(String serviceNo) {
		this.serviceNo = serviceNo;
	}
	
	@Length(min=0, max=50, message="服务号名称长度必须介于 0 和 50 之间")
	public String getSaName() {
		return saName;
	}

	public void setSaName(String saName) {
		this.saName = saName;
	}
	
	@Length(min=0, max=255, message="服务号URL长度必须介于 0 和 255 之间")
	public String getServiceUrl() {
		return serviceUrl;
	}

	public void setServiceUrl(String serviceUrl) {
		this.serviceUrl = serviceUrl;
	}
	
	@Length(min=0, max=30, message="token长度必须介于 0 和 30 之间")
	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
	
	@Length(min=0, max=30, message="appID长度必须介于 0 和 30 之间")
	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}
	
	@Length(min=0, max=50, message="app秘钥长度必须介于 0 和 50 之间")
	public String getAppSecret() {
		return appSecret;
	}

	public void setAppSecret(String appSecret) {
		this.appSecret = appSecret;
	}
	
	@Length(min=0, max=50, message="支付签名key长度必须介于 0 和 50 之间")
	public String getPaysignKey() {
		return paysignKey;
	}

	public void setPaysignKey(String paysignKey) {
		this.paysignKey = paysignKey;
	}
	
	@Length(min=0, max=50, message="partnerid长度必须介于 0 和 50 之间")
	public String getPartnerId() {
		return partnerId;
	}

	public void setPartnerId(String partnerId) {
		this.partnerId = partnerId;
	}
	
	@Length(min=0, max=50, message="partnerkey长度必须介于 0 和 50 之间")
	public String getPartnerKey() {
		return partnerKey;
	}

	public void setPartnerKey(String partnerKey) {
		this.partnerKey = partnerKey;
	}
	
	@Length(min=0, max=255, message="回调url长度必须介于 0 和 255 之间")
	public String getNotifyUrl() {
		return notifyUrl;
	}

	public void setNotifyUrl(String notifyUrl) {
		this.notifyUrl = notifyUrl;
	}
	
	@Length(min=0, max=255, message="redirect_uri长度必须介于 0 和 255 之间")
	public String getRedirectUri() {
		return redirectUri;
	}

	public void setRedirectUri(String redirectUri) {
		this.redirectUri = redirectUri;
	}
	
	@Length(min=1, max=1, message="认证状态长度必须介于 1 和 1 之间")
	public String getAuthStatus() {
		return authStatus;
	}

	public void setAuthStatus(String authStatus) {
		this.authStatus = authStatus;
	}
	
	@Length(min=1, max=1, message="服务号类型长度必须介于 1 和 1 之间")
	public String getAccountType() {
		return accountType;
	}

	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}
	
	public Integer getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
	}
	
	@Length(min=0, max=20, message="app识别码长度必须介于 0 和 20 之间")
	public String getSlAppcode() {
		return slAppcode;
	}

	public void setSlAppcode(String slAppcode) {
		this.slAppcode = slAppcode;
	}
	
	@Length(min=1, max=1, message="状态长度必须介于 1 和 1 之间")
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
	
	@Length(min=0, max=200, message="tonken长度必须介于 0 和 200 之间")
	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
	
	public Integer getAccessTokenExpiry() {
		return accessTokenExpiry;
	}

	public void setAccessTokenExpiry(Integer accessTokenExpiry) {
		this.accessTokenExpiry = accessTokenExpiry;
	}
	
	@Length(min=0, max=200, message="服务人员长度必须介于 0 和 200 之间")
	public String getServicePerson() {
		return servicePerson;
	}

	public void setServicePerson(String servicePerson) {
		this.servicePerson = servicePerson;
	}
	
	public Integer getTemplateId() {
		return templateId;
	}

	public void setTemplateId(Integer templateId) {
		this.templateId = templateId;
	}
	
	/**
	 * 是否是新记录（默认：false），调用setIsNewRecord()设置新记录，使用自定义ID。
	 * 设置为true后强制执行插入语句，ID不会自动生成，需从手动传入。
     * @return
     */
	@Override
	public boolean getIsNewRecord() {
        return isNewRecord || getSaId() == null || getSaId().equals(0);
    }
}
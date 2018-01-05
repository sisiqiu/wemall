/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.fulltl.wemall.modules.wemall.entity;

import com.fulltl.wemall.modules.sys.entity.User;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

import com.fulltl.wemall.common.persistence.DataEntity;

/**
 * 收货地址管理Entity
 * @author ldk
 * @version 2018-01-05
 */
public class WemallUserAddress extends DataEntity<WemallUserAddress> {
	
	private static final long serialVersionUID = 1L;
	private User user;		// 用户id
	private String receiverCountry;		// 国家
	private String receiverProvince;		// 省份
	private String receiverCity;		// 城市
	private String receiverDistrict;		// 区县
	private String receiverAddress;		// 收货地址
	private String receiverZip;		// 邮政编码
	private String receiverName;		// 收货人姓名
	private String receiverMobile;		// 收货人手机
	private String receiverPhone;		// 收货人电话
	private Integer isDefault;		// 是否默认
	
	public WemallUserAddress() {
		super();
	}

	public WemallUserAddress(String id){
		super(id);
	}

	@NotNull(message="用户id不能为空")
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	@Length(min=1, max=40, message="国家长度必须介于 1 和 40 之间")
	public String getReceiverCountry() {
		return receiverCountry;
	}

	public void setReceiverCountry(String receiverCountry) {
		this.receiverCountry = receiverCountry;
	}
	
	@Length(min=1, max=40, message="省份长度必须介于 1 和 40 之间")
	public String getReceiverProvince() {
		return receiverProvince;
	}

	public void setReceiverProvince(String receiverProvince) {
		this.receiverProvince = receiverProvince;
	}
	
	@Length(min=1, max=40, message="城市长度必须介于 1 和 40 之间")
	public String getReceiverCity() {
		return receiverCity;
	}

	public void setReceiverCity(String receiverCity) {
		this.receiverCity = receiverCity;
	}
	
	@Length(min=1, max=40, message="区县长度必须介于 1 和 40 之间")
	public String getReceiverDistrict() {
		return receiverDistrict;
	}

	public void setReceiverDistrict(String receiverDistrict) {
		this.receiverDistrict = receiverDistrict;
	}
	
	@Length(min=1, max=100, message="收货地址长度必须介于 1 和 100 之间")
	public String getReceiverAddress() {
		return receiverAddress;
	}

	public void setReceiverAddress(String receiverAddress) {
		this.receiverAddress = receiverAddress;
	}
	
	@Length(min=1, max=10, message="邮政编码长度必须介于 1 和 10 之间")
	public String getReceiverZip() {
		return receiverZip;
	}

	public void setReceiverZip(String receiverZip) {
		this.receiverZip = receiverZip;
	}
	
	@Length(min=1, max=20, message="收货人姓名长度必须介于 1 和 20 之间")
	public String getReceiverName() {
		return receiverName;
	}

	public void setReceiverName(String receiverName) {
		this.receiverName = receiverName;
	}
	
	@Length(min=1, max=20, message="收货人手机长度必须介于 1 和 20 之间")
	public String getReceiverMobile() {
		return receiverMobile;
	}

	public void setReceiverMobile(String receiverMobile) {
		this.receiverMobile = receiverMobile;
	}
	
	@Length(min=1, max=20, message="收货人电话长度必须介于 1 和 20 之间")
	public String getReceiverPhone() {
		return receiverPhone;
	}

	public void setReceiverPhone(String receiverPhone) {
		this.receiverPhone = receiverPhone;
	}
	
	@NotNull(message="是否默认不能为空")
	public Integer getIsDefault() {
		return isDefault;
	}

	public void setIsDefault(Integer isDefault) {
		this.isDefault = isDefault;
	}
	
}